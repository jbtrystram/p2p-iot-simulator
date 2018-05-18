package example.update.initialisation;

import example.update.constraints.NetworkRange;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class RangeInitializer implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------
    /**
     * The protocol to operate on.
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
    public RangeInitializer(String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        mean = Configuration.getInt(prefix + "." + PAR_MEAN, 500);
        deviation = Configuration.getInt(prefix + "." + PAR_DEV, 100);

    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------
    /**
     * Initialize the range for each node following a normal distribution
     * with the given settings.
     */
    public boolean execute() {

        Node n ;
        NetworkRange protocol;

        for (int i = 0; i < Network.size(); i++) {
            n = Network.get(i);
            protocol = (NetworkRange) n.getProtocol(pid);

            int range =  Math.abs((int)((CommonState.r.nextGaussian()*deviation)+mean));
            protocol.setRange(range);
        }
        return false;
    }

}