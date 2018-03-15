package example.update.initialisation;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

/**
 * This class initialize the NeighborhoodMaintainer protocol with the correct nodeID.
 */
public class AnnounceInitializer implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------
    /**
     * The protocol to operate on.
     *
     * @config
     */
    private static final String PAR_PROT = "protocol";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
    private final int pid;

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
    public AnnounceInitializer(String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------
    /**
     * Initialize the nodeID in announce protocol.
     */
    public boolean execute() {
    /* there is nothing to initialize really
        Node n ;
        NeighborhoodMaintainer protocol;

        for (int i = 0; i < Network.size(); i++) {
            n = Network.get(i);
            //protocol = (NeighborhoodMaintainer) n.getProtocol(pid);
           // protocol.setMyself(n.getID());
        } */
        return false;
    }
}
