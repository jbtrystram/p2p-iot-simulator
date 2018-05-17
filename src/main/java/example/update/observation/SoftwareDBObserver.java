package example.update.observation;

import example.update.SoftwareDB;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.util.FileNameGenerator;


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
