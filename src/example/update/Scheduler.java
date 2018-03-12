package example.update;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

import java.util.ArrayList;

public class Scheduler implements EDProtocol, CDProtocol{

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    // Protocol to interact with : softwareDB and Downloader
    private static final String DB_PROT = "database_protocol";
    private static final String GOSSIP_PROT = "gossip_protocol";


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * Energy protocol identifier. Obtained from config property
     * {@link #DB_PROT}, {@link #GOSSIP_PROT}.
     */
    private final int dbPID;
    private final int gossipPID;
    private int cycle_counter =0;

    String prefix;
    public Scheduler(String prefix) {
        this.prefix = prefix;

        // get PIDs of SoftwareDB and Downloader
        dbPID = Configuration.getPid(prefix + "." + DB_PROT);
        gossipPID = Configuration.getPid(prefix + "." + GOSSIP_PROT);
    }

    public Scheduler clone(){
        return new Scheduler(prefix);
    }

    //receive messages
    public void processEvent(Node node, int pid, Object event) {
        // not used yet

    }

    //process event received from gossiper
    public void processGossipMessage(Node neigh, SoftwarePackage message){
       // System.out.println("supervisor "+ CommonState.getNode().getID()+ "  "+ neigh.getID());

        // no strategy at this time : simply add the software our neighbour have but empty
        SoftwareDB db = (SoftwareDB) CommonState.getNode().getProtocol(this.dbPID);

        db.addNeigborSoftware(message, neigh);
    }


    // TODO : invoke strategy, GOSSIP JOBS, KEEP JOBS LIST IN SCHEDULER
    public void nextCycle(Node node, int protocolID) {
//
        if (cycle_counter%4 == 0) {
            // periodically, gossip all the packages once
            Gossiper localGossiper = (Gossiper) node.getProtocol(gossipPID);
            SoftwareDB database = (SoftwareDB) node.getProtocol(dbPID);

            database.getLocalSoftwareList().forEach(soft -> {
                System.out.println("node "+node.getID()+"periodic gossip "+soft.getName());
                localGossiper.gossip(node, node, gossipPID, soft);
            });
        }
        cycle_counter += 1;
    }




  /*
     * Once NeighborhoodMaintainer messages have been sent, pass the neighbor list to
     * the local instance of software DB to remove neighbors that are not around anymore
     */
       // db.keepOnly(((NeighborhoodMaintainer) node.getProtocol(neighPid)).getNeighbors());

    // ask downloader to download something

    // may ask uploader to stop uploading
}
