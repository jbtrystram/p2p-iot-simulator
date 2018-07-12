package example.update.observation;

import example.update.NetworkAgent;

import example.update.Scheduler;
import example.update.constraints.NetworkRange;
import org.nfunk.jep.function.Str;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class DisseminationObserver implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The protocols to look at.
     *
     * @config
     */
    private static final String PAR_NET = "transfer_protocol";
    private static final String PAR_SCHED = "sched_protocol";

    //The total number of jobs to be disseminated. Used to stop the simulation if everything is done
    private static  final String PAR_TOTAL = "jobs_total_count";

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
     * {@link #}.
     */
    private final int netPid;
    private final int schedPid;

    /* logfile to print data. Name obtained from config
     * {@link #PAR_FILENAME_BASE}.
     */
    private final String filename;

    Writer progressOutput;

    private final int totalJobsNumber;

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
    public DisseminationObserver(String prefix) {

        netPid = Configuration.getPid(prefix + "." + PAR_NET);
        schedPid = Configuration.getPid(prefix + "." + PAR_SCHED);

        filename = "raw_dat/" +Configuration.getString(prefix + "."
                + PAR_FILENAME_BASE, "progress_dump");
        progressOutput = new Writer(filename);

        totalJobsNumber = Configuration.getInt(prefix + "."
                + PAR_TOTAL);
    }

    /* checks if the simulation should stop
    i.e. all jobs are 100% on all nodes
     */
    private boolean fullCompletionCheck(){

        for (int i = 0; i < Network.size(); i++) {
                ArrayList<Integer> values = ((NetworkAgent) Network.get(i).getProtocol(netPid)).jobProgress();
                for (int j=0; j < values.size(); j++){
                    if (values.get(j) != 100) return false;
                }
            }
        System.out.println("Stopping simulation : the given number of jobs are done.");
        System.out.println("If this should not happen, adjust 'jobs_total_count' setting in the configuration file. ");
        System.out.println();
        System.out.println("Total exchanged data");
        for (int i = 0; i < Network.size(); i++) {
            System.out.println(i+": "+((NetworkAgent) Network.get(i).getProtocol(netPid)).overhead);
        }
        return true;
    }



    // Control interface method. does the file handling
    public boolean execute() {

        Scheduler sched;
        HashSet<String> createdTasks = new HashSet<>();

        //get the list of jobs that exist in the network
        for (int i = 0; i < Network.size(); i++) {

            sched = ((Scheduler) Network.get(i).getProtocol(schedPid));
            sched.getJobList().forEach(item ->{
                    createdTasks.add(item);
            });
        }

        // we need consistency in the list
        List<String> sortedList = new ArrayList(createdTasks);
        Collections.sort(sortedList);

        //create the header of the file
        StringBuilder out = new StringBuilder();
        out.append("id;");
        sortedList.forEach(item -> {
            out.append(item).append(";");
        });
        out.append(System.lineSeparator());

        // create an output log file with progress for each jobID.
        for (int i = 0; i < Network.size(); i++) {

            Node current = Network.get(i);
            out.append(current.getID()+";");
            sortedList.forEach(item -> {
                String progress = ((NetworkAgent) current.getProtocol(netPid)).jobProgress(item);

                if (! progress.isEmpty() ) {
                    out.append(progress).append(";");
                }
                else {
                    out.append("0;");
                }
            });
            out.append(System.lineSeparator());
        }
        progressOutput.write(out.toString());

        if (sortedList.size() == totalJobsNumber) return fullCompletionCheck();
        else return false;
    }
}