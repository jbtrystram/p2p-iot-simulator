package example.update.initialisation;

import example.update.constraints.Bandwidth;
import example.update.constraints.NodeCategory;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dynamics.NodeInitializer;

public class CategoryInitializer implements Control, NodeInitializer {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------
    /**
     * The protocol to operate on.
     *
     * @config
     */
    private static final String PAR_CAT_PROT = "category_protocol";
    private static final String PAR_BANDWIDTH_PROT = "_bandwidth_protocol";
    private static final String PAR_RANGE_PROT = "range_protocol";
    private static final String PAR_STORAGE_PROT = "storage_protocol";

    private static final String PAR_MAX_BANDWIDTH = "max_bandwidth";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
    private final int cat_pid;
    private final int bw_pid;
    private final int range_pid;
    private final int storage_pid;


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
    public CategoryInitializer(String prefix) {

        cat_pid = Configuration.getPid(prefix + "." + PAR_CAT_PROT);
        bw_pid = Configuration.getPid(prefix + "." + PAR_BANDWIDTH_PROT);
        range_pid = Configuration.getPid(prefix + "." + PAR_RANGE_PROT);
        storage_pid = Configuration.getPid(prefix + "." + PAR_STORAGE_PROT);
        maxBandwidth = Configuration.getInt(prefix+"."+PAR_MAX_BANDWIDTH);

    }

        // ------------------------------------------------------------------------
        // Methods
        // ------------------------------------------------------------------------

        public boolean execute() {

        for (int i = 0; i < Network.size(); i++) {
            initialize(Network.get(i));
        }
        return false;
    }

    //todo : parse csv
    public void initialize (Node n){

        int up;
        int down;

        int range;
        int storage;

        if(is_antenna){
            ((NodeCategory) n.getProtocol(cat_pid)).setType(NodeCategory.ANTENNA);
            up = down = maxBandwidth;
            range = maxAntennarange;
            storage = maxAntennaStorage;

        } else{
            up = CommonState.r.nextInt(maxBandwidth);
            up = (up > 0)  ? up : maxBandwidth/2 ;
            down = CommonState.r.nextInt(maxBandwidth);
            down = (down > 0)  ? down : maxBandwidth/2 ;

        }

        Bandwidth protocol = (Bandwidth) n.getProtocol(bw_pid);

        protocol.setDownlink(down);
        protocol.setUplink(up);
    }
}