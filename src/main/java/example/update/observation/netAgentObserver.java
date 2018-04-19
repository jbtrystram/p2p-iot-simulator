package example.update.observation;

import example.update.NetworkAgent;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

import peersim.util.FileNameGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;

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
    public netAgentObserver (String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        filename = "raw_dat/" +Configuration.getString(prefix + "."
                + PAR_FILENAME_BASE, "data_pieces_dump");
        fng = new FileNameGenerator(filename, ".dat");
    }

    // Control interface method. does the file handling
    public boolean execute() {

        try {
            // initialize output streams
            String fname = fng.nextCounterName();
            FileOutputStream outStream = new FileOutputStream(fname);
            System.out.println("NetAgentObserver : Writing to file " + fname);
            PrintStream pstr = new PrintStream(outStream);



            for (int i = 0; i < Network.size(); i++) {

                String out = ((NetworkAgent) Network.get(i).getProtocol(pid)).jobProgress();

                if (! out.isEmpty() ) {
                    pstr.println(i+";"+out);
                }
                else {
                    pstr.println(i+";"+0+";");
                }
            }

            outStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
