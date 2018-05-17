package example.update.observation;

import example.update.NetworkAgent;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class netAgentObserver implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The protocol to look at.
     *
     * @config
     */
    private static final String PAR_PROT = "protocol";

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
    public netAgentObserver (String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        filename = "raw_dat/" +Configuration.getString(prefix + "."
                + PAR_FILENAME_BASE, "data_pieces_dump");
        output = new Writer(filename);
    }

    // Control interface method. does the file handling
    public boolean execute() {

            for (int i = 0; i < Network.size(); i++) {

                String out = ((NetworkAgent) Network.get(i).getProtocol(pid)).jobProgress();

                if (! out.isEmpty() ) {
                    output.write(i+";"+out+System.lineSeparator());
                }
                else {
                    output.write(i+";"+System.lineSeparator());
                }
            }
        return false;
    }
}