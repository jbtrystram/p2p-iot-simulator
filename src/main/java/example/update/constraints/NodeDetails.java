package example.update.constraints;

import example.update.initialisation.InetInitializer;
import peersim.config.Configuration;
import peersim.core.Protocol;

/**
 * <p>
 * This class runs into each nodes and
 * stores the node's specifics.
 * </p>
 */

public class NodeDetails implements Protocol {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------
    //TODO : find a clever way to populate these values.

    private static final String BANDW = "bandwidth";
    private static final String STORAGE = "storage";


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** node bandwidth coordinates */
    public final int uplink, downlink;


    /** Storage space */
    public final int totalStorage;

    private int availableStorage;

    String prefix;
    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------
    /**
     * Standard constructor that reads the configuration parameters. Invoked by
     * the simulation engine. By default, all the coordinates components are set
     * to -1 value. The {@link InetInitializer} class provides a coordinates
     * initialization.
     *
     * @param prefix
     *            the configuration prefix for this class.
     */
    public NodeDetails(String prefix) {
        this.prefix = prefix;

        uplink = downlink = Configuration.getInt(prefix + "." +BANDW);
        totalStorage = availableStorage = Configuration.getInt(prefix + "." +STORAGE);
    }

    public Object clone() {
            return  new NodeDetails(this.prefix);
    }

}