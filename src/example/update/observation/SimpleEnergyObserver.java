package example.update.observation;

import example.update.SimpleEnergy;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.util.FileNameGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class SimpleEnergyObserver implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The protocol to look at.
     *
     * @config
     */
    private static final String PAR_ENERGY_PROT = "energy_protocol";

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
     * {@link #PAR_ENERGY_PROT}.
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
    public SimpleEnergyObserver (String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_ENERGY_PROT);
        filename = "raw_dat/" +Configuration.getString(prefix + "."
                + PAR_FILENAME_BASE, "energy_dump");
        fng = new FileNameGenerator(filename, ".dat");
    }

    // Control interface method. does the file handling
    public boolean execute() {
        try {
            // initialize output streams
            String fname = fng.nextCounterName();
            FileOutputStream outStream = new FileOutputStream(fname);
            System.out.println("EnergyObserver : Writing to file " + fname);
            PrintStream pstr = new PrintStream(outStream);

            // dump energy states
            writeData(pstr);

            outStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }


    // do the actual work
    private void writeData(PrintStream file) {

        String offline = "";
        for (int i = 0; i < Network.size(); i++) {

            Node current = Network.get(i);

            if (((SimpleEnergy)current.getProtocol(pid)).getOnlineStatus()) {
                file.println(i + ";" + 1);

            }else file.println(i + ";" + 0);
        }
    }


}
