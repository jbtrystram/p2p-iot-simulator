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
 * Created by jibou on 20/10/17.
 */
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

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * Energy protocol identifier. Obtained from config property
     * {@link #PAR_ENERGY_PROT}.
     */
    private final int pid;


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
    }

    // Control interface method.
    public boolean execute() {

        String offline = "";
        for (int i = 0; i < Network.size(); i++) {

            Node current = Network.get(i);

            if (((SimpleEnergy)current.getProtocol(pid)).getOnlineStatus()) {
                offline += i+",";
            }
        }
        System.out.println("offline nodes : "+offline);
        return false;
    }


}
