package example.update;

import com.sun.org.apache.xpath.internal.operations.Bool;
import peersim.config.Configuration;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

import java.lang.reflect.Array;
import java.util.*;


public class NetworkAgent implements EDProtocol{

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
    //TODO : do something cleaner & smarter in softwareDB instead of this.
    public void update(List<SoftwareJob> packages) {
        // update the local list.
        for ( int i=0; i<packages.size() ;i++ ){
            for (int j=0; j<localData.size() ;j++) {

                if (packages.get(i).getId().equals(localData.get(j).getKey())) {
                    localData.add(i, localData.remove(j));
                } else {
                    localData.add(i, new AbstractMap.SimpleEntry(packages.get(i).getId(),
                            new boolean[packages.get(i).size/pieceSize]));
                }
            }
        }

        //send request to neighbors

    }
    //receiving data from other peers
    public void processEvent(Node node, int i, Object o) {

        // REQUEST

        // RESPONSE

        // PROCESS REQUEST

    }

    // send REQUEST messsages
    private void requestNeighbors(){


    }
}

class DataMessage{

    final static int REQUEST = 0;
    final static int RESPONSE = 1;

    int type;

    String hash;
    int pieceNumber;


    public DataMessage(int type, String hash, int pieceNumber) {
        this.type = type;
        this.hash = hash;
        this.pieceNumber = pieceNumber;
    }
}