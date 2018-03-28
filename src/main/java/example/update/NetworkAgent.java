package example.update;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

import java.util.*;

public class NetworkAgent implements EDProtocol{

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    // Protocol to interact with : softwareDB and Downloader
    private static final String DB_PROT = "database_protocol";
    private static final String GOSSIP_PROT = "gossip_protocol";

    private static final String BANDW = "bandwidth";
    private static final String DISK_SPACE = "disk_space";


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------


    private final int dbPID;
    private final int gossipPID;


    // constructor
    String prefix;
    public NetworkAgent(String prefix) {
        this.prefix = prefix;

        // get PIDs of SoftwareDB and Downloader
        dbPID = Configuration.getPid(prefix + "." + DB_PROT);
        gossipPID = Configuration.getPid(prefix + "." + GOSSIP_PROT);
    }



    public NetworkAgent clone(){
        return new NetworkAgent(prefix);
    }
    // method trigerred by scheduler



    //exchanging data with other peers
    public void processEvent(Node node, int i, Object o) {

    }
}