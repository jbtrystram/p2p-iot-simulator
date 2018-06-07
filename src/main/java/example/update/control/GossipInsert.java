package example.update.control;

import example.update.EasyCSV;
import example.update.NetworkAgent;
import example.update.NetworkMessage;
import example.update.SoftwareJob;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Initialize node software DB with software
 */
public class GossipInsert implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------
    /**
     * The protocol to operate on.
     *
     * @config
     */
    private static final String GOSSIP_PROTOCOL = "gossip_protocol";
    private static final String NETWORK_PROTOCOL = "network_protocol";

    private static final String FILE = "filename";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * Protocol identifier, obtained from config property {@link #GOSSIP_PROTOCOL}.
     */
    private final int gossipPID;
    private final int networkPID;
    private final String file;

    //the list of jobs to insert.
    private LinkedHashMap<Long, SoftwareJob> jobs = new LinkedHashMap();

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    /**
     * Standard constructor that reads the configuration parameters. Invoked by
     * the simulation engine.
     *
     * @param prefix the configuration prefix for this class.
     */
    public GossipInsert(String prefix) {

        gossipPID = Configuration.getPid(prefix + "." + GOSSIP_PROTOCOL);
        networkPID = Configuration.getPid(prefix + "." + NETWORK_PROTOCOL);
        file = Configuration.getString(prefix + "." + FILE);

        // Parse the CSV into the hashmap
        EasyCSV csv = new EasyCSV(file);
        for (int i=1; i<csv.content.size() ;i++){

            SoftwareJob job = new SoftwareJob(csv.content.get(i)[1], csv.content.get(i)[2],
                    LocalDateTime.MAX.toString(), SoftwareJob.PRIORITY_STANDARD,
                    SoftwareJob.QOS_INSTALL_BEST_EFFORT,
                    Integer.parseInt(csv.content.get(i)[3])/1000);

            jobs.put( Long.parseLong(csv.content.get(i)[0]) , job);
        }
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------


    public boolean execute() {

        for (int i=0; i < jobs.keySet().size(); i++){

            long scheduledTime = (long) jobs.keySet().toArray()[i];

            if (CommonState.getTime() >= scheduledTime) {
                Node n = Network.get(1);

                NetworkMessage msg = new NetworkMessage(jobs.get(scheduledTime), n);

                //trigger gossip
                EDSimulator.add(70, msg, n, gossipPID);

                //fill the data on the node
                ((NetworkAgent) n.getProtocol(networkPID)).completeJob(jobs.get(scheduledTime));
                System.out.println("node " + n.getID() + " is the seed for" + jobs.get(scheduledTime).name+"-"+jobs.get(scheduledTime).version);

                jobs.remove(scheduledTime);
            }
        }
        return false;
    }
}