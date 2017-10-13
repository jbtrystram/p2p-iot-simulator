package example.update;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.util.FileNameGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Observe the list of neighbors maintained by the Announce protocol
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

        /**
     * Utility class to generate incremental indexed filenames from a common
     * base given by {@link #graph_filename}.
     */
    private final FileNameGenerator fng;



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
        graph_filename = Configuration.getString(prefix + "."
                + PAR_FILENAME_BASE, "neighbors_dump");
        fng = new FileNameGenerator(graph_filename, ".dat");
    }

    // Control interface method.
    public boolean execute() {


        try {
            // initialize output streams
            String fname = fng.nextCounterName();
            FileOutputStream outStream = new FileOutputStream(fname);
            System.out.println("NeighborsObserver : Writing to file " + fname);
            PrintStream pstr = new PrintStream(outStream);

            // dump neigbors relations:
            writeNeigbors(pstr);

            outStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private void writeNeigbors(PrintStream file) {


        for (int i = 0; i < Network.size(); i++) {

            Node current = Network.get(i);

            String neighbors = "";
            for (int j=0; j < ((Announce)current.getProtocol(pid)).getNeighbors().size(); j++) {
                neighbors += ((Announce) current.getProtocol(pid)).getNeighbors().get(j).getID() + ";";
            }
            file.println(i+";"+neighbors);
        }


    }
}