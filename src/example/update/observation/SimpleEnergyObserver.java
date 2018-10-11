package example.update.observation;

import example.update.strategies.Energy;
import example.update.constraints.energy.EnergySource;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;


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

    /* writer handling file output. Name obtained from config
    * {@link #PAR_FILENAME_BASE}.
    */
    private final Writer writer;



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
        writer = new Writer("raw_dat/"+Configuration.getString(prefix + "."
                + PAR_FILENAME_BASE, "energy_dump"));
    }

    // Control interface method. does the file handling
    public boolean execute() {

        StringBuilder log = new StringBuilder();
        for (int i = 0; i < Network.size(); i++) {

            Node current = Network.get(i);
            EnergySource energy = ((Energy) current.getProtocol(pid)).getPowerSource();


            if (energy.getOnlineStatus() ) {
                log.append(current.getID() +  ";" + energy.getLevel()).append(System.lineSeparator());

            }else log.append(current.getID() +  ";" + 0).append(System.lineSeparator());
        }
        writer.write(log.toString());


        return false;
    }

}
