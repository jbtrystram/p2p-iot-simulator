package example.update;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

import java.security.NoSuchAlgorithmException;
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
     * create a random SoftwarePackage with given parametters
     *
     */
    private SoftwarePackage randomSoftwarePackage(String name, double version, int hashesNumber){
        // Inint an empty hashset
        HashSet<String> hashes = new HashSet<>();

        // create hashes
        for (int j =0; j<hashesNumber;j++){

            byte[] hash = new byte[5];
            //create something to hash
            for (int k =0 ; k < hash.length; k++){
                hash[k] = (byte)CommonState.r.nextInt();
            }
            // hash with md5
            try {
                hash = MessageDigest.getInstance("MD5").digest(hash);
            }
            catch (NoSuchAlgorithmException e) {
                throw new Error("No MD5 support in this VM.");
            }

            // Add the obtained hash into the array
            hashes.add(hash.toString());
        }
        int size = hashes.size() * 32;
       return new SoftwarePackage(name, version, size, hashes);
    }

    /**
     * Initialize the energy state to true in the SimpleEnergy Protocol.
     * -> "power on"the nodes
     */
    public boolean execute() {

        for (int i = 0; i < Network.size(); i+=(Network.size()/2) ) {
            Node n = Network.get(i);
            SoftwareDB protocol = (SoftwareDB) n.getProtocol(pid);

            protocol.addLocalSoftware(randomSoftwarePackage("fedora", 27, 15));
        }
        return false;
    }

}

