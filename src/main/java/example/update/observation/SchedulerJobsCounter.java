package example.update.observation;


import example.update.Scheduler;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by jibou on 15/11/17.
 */
public class SchedulerJobsCounter implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The protocol to look at.
     *
     * @config
     */
    private static final String PAR_PROT = "protocol";

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
    public SchedulerJobsCounter(String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        filename = "raw_dat/" + "schedulerCount.txt";
    }

        // Control interface method. does the file handling
        public boolean execute() {
            try {
                // initialize output streams
                FileOutputStream outStream = new FileOutputStream(filename, true);
                PrintStream pstr = new PrintStream(outStream);

                int count = 0;
                for (int i = 0; i < Network.size(); i++) {

                    Node current = Network.get(i);
                    count += ((Scheduler) current.getProtocol(pid)).numberOfJobs();
                }
                pstr.println(count);

                outStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return false;
        }
}