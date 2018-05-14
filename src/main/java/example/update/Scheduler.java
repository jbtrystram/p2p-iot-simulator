package example.update;

import example.update.constraints.Bandwidth;
import example.update.strategies.Storage;
import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

import java.util.*;

public class Scheduler implements EDProtocol, CDProtocol{

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    // Protocol to interact with : softwareDB and Downloader
    private static final String NET_PROT = "networking_protocol";
    private static final String GOSSIP_PROT = "gossip_protocol";

    private static final String BANDW_PROT = "bandwidth_protocol";
    private static final String DISK_SPACE_PROT = "disk_space_protocol";


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * protocol identifiers. Obtained from config property
     * {@link #NET_PROT}, {@link #GOSSIP_PROT}.
     */
    private final int netPID;
    private final int gossipPID;
    private int cycle_counter =0;

    private ArrayList<SoftwareJob> jobsList;
    int spaceProtocol;
    int bandwidthProtocol;

    String prefix;
    public Scheduler(String prefix) {
        this.prefix = prefix;

        // get PIDs of gossiper and Downloader
        gossipPID = Configuration.getPid(prefix + "." + GOSSIP_PROT);
        netPID = Configuration.getPid(prefix + "." + NET_PROT);

        jobsList = new ArrayList<>();
        this.spaceProtocol = Configuration.getPid(prefix + "." + DISK_SPACE_PROT);
        this.bandwidthProtocol = Configuration.getPid(prefix + "." + BANDW_PROT);
    }

    public Scheduler clone(){
        return new Scheduler(prefix);
    }

    //receive messages
    public void processEvent(Node node, int pid, Object event) {
        // not used yet
    }

    //Compute a job cost
    private void priceIT(SoftwareJob job){
        job.setCost(job.size*job.expectedQoS);
    }

    //process event received from gossiper
    public void processGossipMessage(Node localNode, SoftwareJob newJob){

        if( ! newJob.isExpired() && newJob.isDoable( ((Bandwidth) localNode.getProtocol(bandwidthProtocol)).getDownlinkCapacity() )
                && ((Storage)localNode.getProtocol(spaceProtocol)).allocateStorage(newJob.size) ) {
            priceIT(newJob);
            jobsList.add(newJob);
            updateTasks();
        }
    }


    public void nextCycle(Node node, int protocolID) {

        updateTasks();

        //pass list to the downlader
        if (! jobsList.isEmpty()) {
            ((NetworkAgent) node.getProtocol(netPID)).update(jobsList);

            if (cycle_counter % 4 == 0) {
                // TODO : gossip completed jobs ?
                // periodically, gossip all the packages once
                Gossiper localGossiper = (Gossiper) node.getProtocol(gossipPID);

                jobsList.forEach(soft -> {
                    localGossiper.gossip(node, node, gossipPID, soft);
                });
            }
        }
        cycle_counter += 1;
    }


    // TODO : feedback from DL Agent to take into account network stats
    public void updateTasks(){
        ArrayList<SoftwareJob> prioList = new ArrayList<>(jobsList);
        ArrayList<SoftwareJob> costList = new ArrayList<>(jobsList);
        ArrayList<SoftwareJob> ageList = new ArrayList<>(jobsList);

        double prioCoeff = 1;
        double costCoeff = 1.75;
        double ageCoeff = 1.5;

        prioList.sort( Comparator.comparing(SoftwareJob::getPriority));

        costList.sort( Comparator.comparing(SoftwareJob::getCost));

        ageList.sort( Comparator.comparing(SoftwareJob::getDateCreated));

        HashMap<Double,SoftwareJob> rankedJobs = new HashMap<>();
        prioList.forEach(s -> {
            double score = (prioList.indexOf(s)+1)*prioCoeff + (costList.indexOf(s)+1)*costCoeff + (ageList.indexOf(s)+1)*ageCoeff;
            rankedJobs.put(score , s);
        });

        jobsList.clear();
        while(! rankedJobs.isEmpty()){
            Double key = Collections.min(rankedJobs.keySet());
            jobsList.add(rankedJobs.remove(key));
        }
    }

    public int numberOfJobs(){
        return jobsList.size();
    }
}