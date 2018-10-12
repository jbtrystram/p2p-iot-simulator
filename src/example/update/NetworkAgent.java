package example.update;

import example.update.constraints.Bandwidth;
import example.update.strategies.Energy;
import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;


public class NetworkAgent implements EDProtocol, CDProtocol{

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    // Protocol to interact with : softwareDB, neighborhood maintainer and Supervisor
    private static final String NEIGH_PROT = "neighborhood_protocol";
    private static final String ENERGY_PROT = "energy_protocol";
    private static final String BANDW = "bandwidth_protocol";
    private static final String PIECE_SIZE = "piece_size";


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    private final int neighborhoodPID;
    private final int powerSourcePID;


    private int bandwidthPid;
    private final int pieceSize;

    private boolean downloading = false;


    // my local list <SoftwarePackage.ID, array>
    List<Map.Entry<String, boolean[]>> localData;

    public long overhead =0;

    // constructor
    String prefix;
    public NetworkAgent(String prefix) {
        this.prefix = prefix;

        // get PIDs of SoftwareDB and Downloader
        neighborhoodPID = Configuration.getPid(prefix + "." + NEIGH_PROT);
        powerSourcePID = Configuration.getPid(prefix + "." + ENERGY_PROT);
        bandwidthPid = Configuration.getPid(prefix + "." +BANDW);
        pieceSize = Configuration.getInt(prefix + "." +PIECE_SIZE);

        localData = new ArrayList<>();
    }



    public NetworkAgent clone(){
        return new NetworkAgent(prefix);
    }


    // method trigerred by scheduler -> gives an ordered list of softwares to DL.
    //TODO : do something cleaner in a softwareDB class

    public void update(List<SoftwareJob> packages) {

        if(localData.isEmpty()) {
            localData.add(new SimpleEntry(packages.get(0).getId(),
                    new boolean[(int) Math.ceil( (double)packages.get(0).size / (double)pieceSize)]));
            Arrays.fill(localData.get(0).getValue(), Boolean.FALSE);
        } else {
            // update the local list.
            for (int i = 0; i < packages.size(); i++) {
                for (int j = 0; j < localData.size(); j++) {
                    if (packages.get(i).getId().equals(localData.get(j).getKey())) {
                            localData.add(i, localData.remove(j));
                    } else if (! localDataContains(packages.get(i).getId())) {
                        localData.add(i, new SimpleEntry(packages.get(i).getId(),
                                new boolean[(int) Math.ceil( (double)packages.get(i).size / (double)pieceSize)]));
                        Arrays.fill(localData.get(i).getValue(), Boolean.FALSE);
                    }
                }
            }
        }
    }

    private Map.Entry<String, Integer> getRandomDownload() {

       for (int i = 0; i < localData.size(); i++) {
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

    private void usePower(int multiplier, Node node){
        ((Energy) node.getProtocol(powerSourcePID)).consume(multiplier);
        overhead += multiplier;
    }

    private boolean isStillAround(Node neighbor, Node local){
       return  ((NeighborhoodMaintainer)local.getProtocol(neighborhoodPID)).getNeighbors().contains(neighbor);
    }


    public void nextCycle(Node node, int pid) {
        if (!downloading && ! localData.isEmpty()){

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
                if (!downloading && event.sender != localNode && localDataContains(event.hash)) {

                    // is this piece complete
                    if (localData.get(getLocalIndex(event.hash)).getValue()[event.pieceNumber]) {
                        DataMessage reply = new DataMessage(DataMessage.OFFER, event.hash, event.pieceNumber, localNode);
                        EDSimulator.add(1, reply, event.sender, pid);
                        usePower(1, localNode);
                    }
                }
                break;

            case DataMessage.OFFER:
                //downloading data
                if (!downloading) {
                    this.downloading = true;
                    //send ACCEPT
                    DataMessage accept = new DataMessage(DataMessage.ACCEPT, event.hash, event.pieceNumber, localNode);
                    EDSimulator.add(1, accept, event.sender, pid);
                    usePower(1, localNode);
                }
                break;

            case DataMessage.ACCEPT:
                if (!downloading) {
                    //System.out.println("Node "+ localNode.getID() +" piece "+event.pieceNumber+ "receivied ACCEPT from node "+event.sender.getID());
                    //upload the data
                    this.downloading = true;
                    DataMessage dataMsg;

                    // use the lowest bandwith of the 2 nodes
                    long localUplink = ((Bandwidth) localNode.getProtocol(bandwidthPid)).getUplinkCapacity();
                    long remoteDownlink = ((Bandwidth) event.sender.getProtocol(bandwidthPid)).getDownlinkCapacity();

                    long bdw = localUplink <= remoteDownlink ? localUplink : remoteDownlink;

                    dataMsg = new DataMessage(DataMessage.DATA, event.hash, event.pieceNumber, localNode);
                    EDSimulator.add(pieceSize / bdw, dataMsg, event.sender, pid);
                    //consume energy
                    usePower((int)(pieceSize/bdw), localNode);

                } else { // send a cancel message
                    DataMessage dataMsg;

                    dataMsg = new DataMessage(DataMessage.CANCEL, event.hash, event.pieceNumber, localNode);
                    EDSimulator.add(1, dataMsg, event.sender, pid);
                    usePower(1, localNode);
                }
                break;

            case DataMessage.DATA:
                //mark piece as completed into DB
                if (isStillAround(event.sender, localNode)) {
                    localData.get(getLocalIndex(event.hash)).getValue()[event.pieceNumber] = true;
                }
                this.downloading = false;

                //send ack
                DataMessage msg = new DataMessage(DataMessage.DATAACK, event.hash, event.pieceNumber, localNode);
                EDSimulator.add(1, msg, event.sender, pid);
                usePower(1, localNode);
                break;

                //TODO : what if energy cuts during a trasnfer? seeder is locked.
            case DataMessage.DATAACK:
                //System.out.println("node "+ localNode.getID() +" piece "+event.pieceNumber +" DATAACK message from node" + event.sender.getID());
                //we can stop uploading
                this.downloading = false;
                break;

            case DataMessage.CANCEL:
                this.downloading = false;
                // try again
                this.nextCycle(localNode, pid);
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
                    usePower(1, localNode);
                });
    }


    public List<Map.Entry<String, boolean[]>> getLocalData() {
        return localData;
    }

    public String jobProgress(String hash){
        if (localDataContains(hash)) {
            boolean[] tab = localData.get(getLocalIndex(hash)).getValue();
            int perc = 0;
            for (boolean b : tab) {
                if (b) perc++;
            }
            return String.valueOf((int) (((double) perc / (double) tab.length) * 100));
        } else return "";
    }

    //TODO : better looking hashes
    public ArrayList jobProgress(){
        ArrayList<Integer> progress = new ArrayList<>();
        for (Map.Entry<String, boolean[]> entry : localData) {

            boolean[] tab = entry.getValue();
            int perc = 0;
            for (boolean b : tab) {
                if (b) perc++;
            }
            progress.add( (int)(((double)perc/(double)tab.length)*100));
        }
        return progress;
    }

    //complete a job : hook for the initializer
    public void completeJob(SoftwareJob job){
        if ( ! localDataContains(job.getId()) ){

            boolean[] data = new boolean[(int) Math.ceil( (double)job.size / (double)pieceSize)];
            Arrays.fill(data, Boolean.TRUE);

            localData.add(new SimpleEntry(job.getId(), data));
        } else {
            Arrays.fill(localData.get(getLocalIndex(job.getId())).getValue(), Boolean.TRUE);
        }
    }
}

class DataMessage{

    final static int REQUEST = 0;
    final static int OFFER = 1;
    final static int ACCEPT = 2;
    final static int DATA = 3;
    final static int DATAACK = 4;
    final static int CANCEL = 5;
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