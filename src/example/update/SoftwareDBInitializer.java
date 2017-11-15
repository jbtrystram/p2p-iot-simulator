package example.update;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

import java.util.HashSet;
import java.security.MessageDigest;


/**
 * Initialize node software DB with software
 */
public class SoftwareDBInitializer implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------
    /**
     * The protocol to operate on.
     *
     * @config
     */
    private static final String PAR_PROT = "protocol";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
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
    public SoftwareDBInitializer(String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------
    /**
     * Initialize the energy state to true in the SimpleEnergy Protocol.
     * -> "power on"the nodes
     */
    public boolean execute() {

        for (int i = 0; i < Network.size(); i+=(Network.size()/5) ) {
            Node n = Network.get(i);
            SoftwareDB protocol = (SoftwareDB) n.getProtocol(pid);

            // create a random software package
            String name;
            double version = 1.1;
            int size;
            HashSet<String> hashes;

            SoftwarePackage soft = new SoftwarePackage(name, version, size, hashes);

            protocol.addLocalSoftware();
        }
        return false;
    }

}

