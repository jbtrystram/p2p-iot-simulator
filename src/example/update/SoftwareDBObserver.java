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
 * Created by jibou on 15/11/17.
 */
public class SoftwareDBObserver implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The protocol to look at.
     *
     * @config
     */
    private static final String PAR_PROT = "database_protocol";

    /**
     * Output logfile name base.
     *
     * @config
     */
    private static final String PAR_FILENAME_BASE = "filename";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * Energy protocol identifier. Obtained from config property
     * {@link #PAR_PROT}.
     */
    private final int pid;

    /* logfile to print data. Name obtained from config
    * {@link #PAR_FILENAME_BASE}.
    */
    private final String filename;

    /**
     * Utility class to generate incremental indexed filenames from a common
     * base given by {@link #filename}.
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
    public SoftwareDBObserver (String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        filename = "raw_dat/" +Configuration.getString(prefix + "."
                + PAR_FILENAME_BASE, "energy_dump");
        fng = new FileNameGenerator(filename, ".dat");
    }

    // Control interface method. does the file handling
    public boolean execute() {

        for (int i = 0; i < Network.size(); i++) {

            String out =  ((SoftwareDB) Network.get(i).getProtocol(pid)).toString();
            if (! out.isEmpty()) {
                System.out.println("Node " + i + "\n" + out);
            }
        }
        return false;
    }
}
