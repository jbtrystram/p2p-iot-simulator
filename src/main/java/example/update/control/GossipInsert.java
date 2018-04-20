package example.update.control;

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

    private static final String FILE_SIZE = "file_size";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * Protocol identifier, obtained from config property {@link #GOSSIP_PROTOCOL}.
     */
    private final int gossipPID;
    private final int networkPID;

    private final int fileSize;
    private boolean done;

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
        fileSize = Configuration.getInt(prefix + "." + FILE_SIZE);

        done = false;
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------

    /**
     * create a random SoftwarePackage with given parametters
     */
    private SoftwareJob randomJob(String name, String version) {

        int qos = SoftwareJob.QOS_INSTALL_MANDATORY;
        int priority = SoftwareJob.PRIORITY_STANDARD;
        int size = fileSize;

        SoftwareJob job = new SoftwareJob(name, version, LocalDateTime.MAX, priority, qos, size);

        return job;
    }


    public boolean execute() {

        if (! done) {
            Node n = Network.get(CommonState.r.nextInt(Network.size()));

            SoftwareJob job = randomJob("linux", "4.15");
            NetworkMessage msg = new NetworkMessage(job, n);

            //trigger gossip
            EDSimulator.add(70, msg, n, gossipPID);

            //fill the data on the node
            ((NetworkAgent) n.getProtocol(networkPID)).completeJob(job);
            System.out.println("node " + n.getID() + "is the seed");
            done = true;
        }
        return false;
    }

}