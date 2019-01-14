package example.update.initialisation;

import example.update.EasyCSV;
import example.update.constraints.*;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dynamics.NodeInitializer;

import java.util.List;

public class AntennaInitializer implements Control {

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
    private static final String PAR_LOCATION_PROT = "location_protocol";


    private static final String PAR_MAX_BANDWIDTH = "max_bandwidth";
    private static final String PAR_MAX_STORAGE = "max_storage";
    private static final String PAR_MAX_RANGE = "max_range";
    private static final String PAR_CELL_DATASET = "cell_dataset";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
    private final int cat_pid;
    private final int bw_pid;
    private final int range_pid;
    private final int storage_pid;
    private final int location_pid;

    private final int maxRange;
    private final int maxStorage;
    private final int maxBandwidth;

    private final String cellDataset;

    private List<String[]> antennas;

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
    public AntennaInitializer(String prefix) {

        cat_pid = Configuration.getPid(prefix + "." + PAR_CAT_PROT);
        bw_pid = Configuration.getPid(prefix + "." + PAR_BANDWIDTH_PROT);
        range_pid = Configuration.getPid(prefix + "." + PAR_RANGE_PROT);
        storage_pid = Configuration.getPid(prefix + "." + PAR_STORAGE_PROT);
        location_pid = Configuration.getPid(prefix + "." + PAR_LOCATION_PROT);
        maxBandwidth = Configuration.getInt(prefix+"."+PAR_MAX_BANDWIDTH);
        maxStorage = Configuration.getInt(prefix+"."+PAR_MAX_STORAGE);
        maxRange = Configuration.getInt(prefix+"."+PAR_MAX_RANGE);

        cellDataset = Configuration.getString(prefix+"."+PAR_CELL_DATASET);

        EasyCSV parser = new EasyCSV(cellDataset);
        antennas = parser.content;
    }

        // ------------------------------------------------------------------------
        // Methods
        // ------------------------------------------------------------------------

        public boolean execute() {

        for (int i = 0; i < antennas.size(); i++) {

            Node n = Network.get(i);
            String[] item = antennas.get(i);

            ((NodeCategory) n.getProtocol(cat_pid)).setType(NodeCategory.ANTENNA);

            ((NetworkRange) n.getProtocol(range_pid)).setRange(maxRange);

            ((Storage) n.getProtocol(storage_pid)).setTotalSpace(maxStorage);

            NodeCoordinates coordProtocol  = (NodeCoordinates) n.getProtocol(location_pid);
            coordProtocol.setX(Integer.parseInt(item[0]));
            coordProtocol.setY(Integer.parseInt(item[1]));

            Bandwidth bwprotocol = (Bandwidth) n.getProtocol(bw_pid);
            bwprotocol.setDownlink(maxBandwidth);
            bwprotocol.setUplink(maxBandwidth);
        }

        return false;
    }
}