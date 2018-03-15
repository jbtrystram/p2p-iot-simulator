package example.update;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

import java.util.*;

public class Scheduler implements EDProtocol, CDProtocol{

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

    /**
     * Energy protocol identifier. Obtained from config property
     * {@link #DB_PROT}, {@link #GOSSIP_PROT}.
     */
    private final int dbPID;
    private final int gossipPID;
    private int cycle_counter =0;

    private ArrayList<SoftwareJob> jobsList;
    int space;
    final int bandwidth;

    String prefix;
    public Scheduler(String prefix) {
        this.prefix = prefix;

        // get PIDs of SoftwareDB and Downloader
        dbPID = Configuration.getPid(prefix + "." + DB_PROT);
        gossipPID = Configuration.getPid(prefix + "." + GOSSIP_PROT);

        jobsList = new ArrayList<>();
        this.space = Configuration.getInt(prefix + "." + DISK_SPACE);
        this.bandwidth = Configuration.getInt(prefix + "." + BANDW);
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
    public void processGossipMessage(Node neigh, SoftwareJob newJob){

        // TODO update downloader agent that will download and interact with DB.
        // no strategy at this time : simply add the software our neighbour have but empty
        //SoftwareDB db = (SoftwareDB) CommonState.getNode().getProtocol(this.dbPID);
        //db.addNeigborSoftware(message, neigh);
          /*
     * Once NeighborhoodMaintainer messages have been sent, pass the neighbor list to
     * the local instance of software DB to remove neighbors that are not around anymore
     */
        // db.keepOnly(((NeighborhoodMaintainer) node.getProtocol(neighPid)).getNeighbors());


        if( ! newJob.isExpired() || newJob.isDoable(bandwidth) ) {
            priceIT(newJob);
            jobsList.add(newJob);
            updateTasks();
        }
    }


    public void nextCycle(Node node, int protocolID) {

        updateTasks();

        if (cycle_counter%4 == 0) {
            // TODO : gossip completed jobs ?
            // periodically, gossip all the packages once
            Gossiper localGossiper = (Gossiper) node.getProtocol(gossipPID);

            jobsList.forEach(soft -> {
                localGossiper.gossip(node, node, gossipPID, soft);
            });
        }
        cycle_counter += 1;
    }


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

