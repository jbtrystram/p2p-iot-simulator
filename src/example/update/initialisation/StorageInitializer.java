package example.update.initialisation;

import example.update.strategies.Storage;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dynamics.NodeInitializer;

public class StorageInitializer implements Control, NodeInitializer {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------
    /**
     * The protocol to operate on.
     * And the normal law's mean and deviation. THIS SHOULD BE MEGABYTES
     *
     * @config
     */
    private static final String PAR_PROT = "protocol";
    private static final String PAR_MEAN = "gaussian_mean";
    private static final String PAR_DEV = "gaussian_deviation";


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
    private final int pid;
    private final int mean;
    private final int deviation;

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
    public StorageInitializer(String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        mean = Configuration.getInt(prefix + "." + PAR_MEAN, 1000);
        deviation = Configuration.getInt(prefix + "." + PAR_DEV, 600);

    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------
    /**
     * Initialize the range for each node following a normal distribution
     * with the given settings.
     */
    public boolean execute() {

        for (int i = 0; i < Network.size(); i++) {
            initialize(Network.get(i));

        }
        return false;
    }

    public void initialize(Node n){
        Storage protocol = (Storage) n.getProtocol(pid);
        protocol.init((int) Math.abs(1000* (CommonState.r.nextGaussian()*deviation+mean)));
    }
}