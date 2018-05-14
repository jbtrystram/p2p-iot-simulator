package example.update.initialisation;

import example.update.constraints.Bandwidth;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class BandwidthInitializer implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------
    /**
     * The protocol to operate on.
     *
     * @config
     */
    private static final String PAR_PROT = "protocol";
    private static final String PAR_MAX_BANDWIDTH = "max_bandwidth";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
    private final int pid;

    private final int maxBandwidth;

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
    public BandwidthInitializer(String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        maxBandwidth = Configuration.getInt(prefix+"."+PAR_MAX_BANDWIDTH);

    }

        // ------------------------------------------------------------------------
        // Methods
        // ------------------------------------------------------------------------

        //TODO : try another approach (gaussian distribution ?)
        public boolean execute() {

        Node n ;
        Bandwidth protocol;

        for (int i = 0; i < Network.size(); i++) {
            n = Network.get(i);
            protocol = (Bandwidth) n.getProtocol(pid);

            protocol.setDownlink(CommonState.r.nextInt(maxBandwidth));
            protocol.setUplink(CommonState.r.nextInt(maxBandwidth));
        }
        return false;
    }

}