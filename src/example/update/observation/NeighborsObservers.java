package example.update.observation;

import example.update.NeighborhoodMaintainer;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

/**
 * Observe the list of neighbors maintained by the NeighborhoodMaintainer protocol
 */
public class NeighborsObservers  implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The neigbors protocol to look at.
     *
     * @config
     */
    private static final String PAR_NEIGHBORS_PROT = "neigbors_protocol";

    /**
     * Output logfile name.
     *
     * @config
     */
    private static final String PAR_FILENAME_BASE = "file_base";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * Coordinate protocol identifier. Obtained from config property
     * {@link #PAR_NEIGHBORS_PROT}.
     */
    private final int pid;

    /* logfile to print data. Name obrtained from config
    * {@link #PAR_FILENAME_BASE}.
    */
    private final String graph_filename;

    Writer output;

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
    public NeighborsObservers (String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_NEIGHBORS_PROT);
        graph_filename = "raw_dat/" +Configuration.getString(prefix + "."
                + PAR_FILENAME_BASE, "neighbors_dump");

        output = new Writer(graph_filename);
    }

    // Control interface method.
    public boolean execute() {

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < Network.size(); i++) {

            Node current = Network.get(i);

            String neighbors = "";
            for (int j = 0; j < ((NeighborhoodMaintainer)current.getProtocol(pid)).getNeighbors().size(); j++) {
                neighbors += ((NeighborhoodMaintainer) current.getProtocol(pid)).getNeighbors().get(j).getID() + ";";
            }
            out.append(current.getID()+";"+neighbors+ System.lineSeparator());
        }
        output.write(out.toString());
        return false;
    }
}