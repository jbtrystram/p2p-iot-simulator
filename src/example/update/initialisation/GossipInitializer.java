package example.update.initialisation;

import example.update.SoftwareJob;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;

import example.update.NetworkMessage;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Initialize node software DB with software
 */
public class GossipInitializer implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------
    /**
     * The protocol to operate on.
     *
     * @config
     */
    private static final String GOSSIP_PROTOCOL = "gossip_protocol";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Protocol identifier, obtained from config property {@link #GOSSIP_PROTOCOL}. */
    private final int gossip_pid;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    /**
     * Standard constructor that reads the configuration parameters. Invoked by
     * the simulation engine.
     *
     * @param prefix
     *            the configuration prefix for this class.
     */
    public GossipInitializer(String prefix) {

        gossip_pid = Configuration.getPid(prefix + "." + GOSSIP_PROTOCOL);

    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------

    /**
     * create a random SoftwarePackage with given parametters
     */
    private SoftwareJob randomJob(String name, String version){

        int qos = SoftwareJob.QOS_INSTALL_MANDATORY;
        int priority = SoftwareJob.PRIORITY_STANDARD;
        int size = 500;

        SoftwareJob job = new SoftwareJob(name, version, LocalDateTime.MAX, priority, qos, size);

       return job;
    }


    public boolean execute() {

       for (int i = 0; i < Network.size(); i+=(Network.size()/2) ) {
            Node n = Network.get(i);

            SoftwareJob job = randomJob("swag", "27");
            NetworkMessage msg = new NetworkMessage(job,n);

            //trigger gossip
            EDSimulator.add(70, msg, n, gossip_pid);
            //protocol.addLocalSoftware(randomJob("fedora", 27, 15));
            //protocol.getLocalSoftwareList().forEach(soft -> {
            //    soft.comleteAllPieces();
            //});
        }
        return false;
    }

}

