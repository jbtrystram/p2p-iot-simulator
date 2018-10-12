package example.update;

import example.update.constraints.Bandwidth;
import example.update.strategies.Storage;
import peersim.cdsim.CDProtocol;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

import java.util.*;

public abstract class Scheduler implements EDProtocol, CDProtocol{

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    // Protocol to interact with : softwareDB and Downloader
    static final String NET_PROT = "networking_protocol";
    static final String GOSSIP_PROT = "gossip_protocol";

    static final String BANDW_PROT = "bandwidth_protocol";
    static final String DISK_SPACE_PROT = "disk_space_protocol";


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * protocol identifiers. Obtained from config property
     * {@link #NET_PROT}, {@link #GOSSIP_PROT}.
     */
    int netPID;
    int gossipPID;

    ArrayList<SoftwareJob> jobsList;
    int spaceProtocol;
    int bandwidthProtocol;

    //receive messages
    public void processEvent(Node node, int pid, Object event) {
        // not used yet
    }

       //process event received from gossiper
    public void processGossipMessage(Node localNode, SoftwareJob newJob){

        if( ! newJob.isExpired() && newJob.isDoable( ((Bandwidth) localNode.getProtocol(bandwidthProtocol)).getDownlinkCapacity() )
                && ((Storage)localNode.getProtocol(spaceProtocol)).allocateStorage(newJob.size) ) {

            jobsList.add(newJob);
            updateTasks(localNode);
        }
    }

    public void nextCycle(Node node, int protocolID) {

        updateTasks(node);

        //pass list to the downlader
        if (! jobsList.isEmpty()) {
            ((NetworkAgent) node.getProtocol(netPID)).update(jobsList);
        }
    }

    void gossipAllJobs(Node localNode){
        Gossiper localGossiper = (Gossiper) localNode.getProtocol(gossipPID);

        jobsList.forEach(soft -> {
            localGossiper.gossip(localNode, localNode, gossipPID, soft);
        });
    }

    public abstract Scheduler clone();

    // Implementing classes should implement their strategy in this class.
    public abstract void updateTasks(Node node);

    public ArrayList<String> getJobList(){

        ArrayList jobs = new ArrayList<String>();

        jobsList.forEach(item -> jobs.add( item.getId() ));
        return jobs;
    }
}