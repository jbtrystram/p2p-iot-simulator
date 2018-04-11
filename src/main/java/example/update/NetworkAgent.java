package example.update;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class NetworkAgent implements EDProtocol, CDProtocol{

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    // Protocol to interact with : softwareDB, neighborhood maintainer and Supervisor
    private static final String DB_PROT = "database_protocol";
    private static final String GOSSIP_PROT = "gossip_protocol";
    private static final String NEIGH_PROT = "neighborhood_protocol";

    private static final String BANDW = "bandwidth";
    private static final String PIECE_SIZE = "piece_size";


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------


    private final int dbPID;
    private final int gossipPID;
    private final int neighborhoodPID;

    private int bandwidth;
    private final int pieceSize;

    private boolean downloading = false;


    // my local list <SoftwarePackage.ID, array>
    ArrayList<Map.Entry<String, boolean[]>> localData;


    // constructor
    String prefix;
    public NetworkAgent(String prefix) {
        this.prefix = prefix;

        // get PIDs of SoftwareDB and Downloader
        dbPID = Configuration.getPid(prefix + "." + DB_PROT);
        gossipPID = Configuration.getPid(prefix + "." + GOSSIP_PROT);
        neighborhoodPID = Configuration.getPid(prefix + "." + NEIGH_PROT);

        bandwidth = Configuration.getInt(prefix + "." +BANDW);
        pieceSize = Configuration.getInt(prefix + "." +PIECE_SIZE);

        localData = new ArrayList<>();
    }



    public NetworkAgent clone(){
        return new NetworkAgent(prefix);
    }


    // method trigerred by scheduler -> gives an ordered list of softwares to DL.
    //TODO : do something cleaner in a softwareDB class

    public void update(List<SoftwareJob> packages) {
        // update the local list.
        for ( int i=0; i<packages.size() ;i++ ){
            for (int j=0; j<localData.size() ;j++) {

                if (packages.get(i).getId().equals(localData.get(j).getKey())) {
                    localData.add(i, localData.remove(j));
                } else {
                    localData.add(i, new SimpleEntry(packages.get(i).getId(),
                            new boolean[packages.get(i).size/pieceSize]));
                }
            }
        }
    }

    private Map.Entry<String, Integer> getRandomDownload() {

       for (int i = 1; i <= localData.size(); i++) {

           int rand = CommonState.r.nextInt(localData.get(i).getValue().length);

           for (int j = rand; j < localData.get(i).getValue().length; j++) {
               if (localData.get(i).getValue()[j] == false) {
                   return new SimpleEntry<>(localData.get(i).getKey(), j);
               }
           }
           for (int j = 0; j < rand; j++) {
               if (localData.get(i).getValue()[j] == false) {
                   return new SimpleEntry<>(localData.get(i).getKey(), j);
               }
           }
       }
       return null;
    }



    public void nextCycle(Node node, int pid) {
        if (!downloading){

            Map.Entry<String, Integer> toDownload = getRandomDownload();
            if ( toDownload != null) {

                //craft a new message
                DataMessage msg = new DataMessage(DataMessage.REQUEST, toDownload.getKey(), toDownload.getValue(), node);
                requestNeighbors(node, msg, pid);
            }
        }
    }

    //receiving data from other peers
    public void processEvent(Node localNode, int pid, Object data) {

        DataMessage event = (DataMessage)(data);

        switch (event.type) {

            case DataMessage.REQUEST:
                if ( !downloading && event.sender != localNode && localDataContains(event.hash) ){
                    // is this piece complete
                    if ( localData.get(getLocalIndex(event.hash)).getValue()[event.pieceNumber] ) {
                        DataMessage reply = new DataMessage(DataMessage.RESPONSE, event.hash, event.pieceNumber, localNode);
                        EDSimulator.add(0, reply, event.sender, pid);

                        //send the data
                        this.downloading = true;
                        // use the lowest bandwith of the 2 nodes
                        int bdw = ( bandwidth < ((NetworkAgent)event.sender.getProtocol(pid)).bandwidth ) ?
                                    bandwidth : ((NetworkAgent)event.sender.getProtocol(pid)).bandwidth ;
                        reply = new DataMessage(DataMessage.DATA, event.hash, event.pieceNumber, localNode);
                        EDSimulator.add(pieceSize/bdw, reply, event.sender, pid);

                    }
                }
                break;

            case DataMessage.RESPONSE:
                //downloading data
                this.downloading = true;
                break;

            case DataMessage.DATA:
                //mark piece as completed into DB
                localData.get(getLocalIndex(event.hash)).getValue()[event.pieceNumber] = true;

                this.downloading = false;

                //send ack
                DataMessage msg = new DataMessage(DataMessage.DATAACK, event.hash, event.pieceNumber, localNode);
                EDSimulator.add(0, msg, event.sender, pid);
                break;

                //TODO : what if energy cuts during a trasnfer? seeder is locked.
            case DataMessage.DATAACK:
                //we can stop uploading
                this.downloading = false;
                break;
        }
    }

    private boolean localDataContains(String hash){
        for (Map.Entry<String, boolean[]> tuple : localData) {
            if (hash.equals(tuple.getKey())) {
                return true;
            }
        }
        return false;
    }

    private int getLocalIndex(String hash){
        for (int i=0; i<localData.size() ; i++) {
            if (hash.equals(localData.get(i).getKey())) {
                return i;
            }
        }
        return -1;
    }

    // send REQUEST messsages
    private void requestNeighbors(Node localNode, DataMessage msg, int pid){

        //get neighbors list
        ((NeighborhoodMaintainer) localNode.getProtocol(neighborhoodPID)).getNeighbors()
                .forEach(neighbor -> {
                    // send request message to neighbor with no latency
                    EDSimulator.add(0, msg, neighbor, pid);
                });
    }
}

class DataMessage{

    final static int REQUEST = 0;
    final static int RESPONSE = 1;
    final static int DATA = 2;
    final static int DATAACK = 3;
     // ? //enum type1 {REQUEST, RESPONSE}

    int type;

    String hash;
    int pieceNumber;
    Node sender;


    public DataMessage(int type, String hash, int pieceNumber, Node sender) {
        this.sender = sender;
        this.type = type;
        this.hash = hash;
        this.pieceNumber = pieceNumber;
    }
}