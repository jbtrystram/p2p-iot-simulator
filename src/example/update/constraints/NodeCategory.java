package example.update.constraints;

import example.update.initialisation.CoordinatesInitializer;
import peersim.config.Configuration;
import peersim.core.Protocol;

/**
 * <p>
 * This class runs into each nodes and
 * stores the node's specifics.
 * </p>
 */

public class NodeCategory implements Protocol {

    // ------------------------------------------------------------------------
    // Values
    // ------------------------------------------------------------------------

    public static final int ANTENNA = 0;
    public static final int CAR = 1;


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** node bandwidth coordinates */
    private int type;

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
    public NodeCategory(String prefix) {
        this.prefix = prefix;

        type = -1;
        //uplink = downlink = Configuration.getInt(prefix + "." +BANDW);
        //totalStorage = availableStorage = Configuration.getInt(prefix + "." +STORAGE);
    }

    public Object clone() {
            return  new NodeCategory(this.prefix);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}