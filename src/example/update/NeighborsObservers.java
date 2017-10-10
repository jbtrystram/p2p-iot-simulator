package example.update;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

/**
 * Observe the list of neighbors maintained by the Announce protocol
 */
public class NeighborsObservers  implements Control {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The neigbors protocol to look at.
     *
     * @config
     */
    private static final String PAR_NEIGHBORS_PROT = "neigbors_protocol";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * Coordinate protocol identifier. Obtained from config property
     * {@link #PAR_NEIGHBORS_PROT}.
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
    public NeighborsObservers (String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_NEIGHBORS_PROT);
    }

    // Control interface method.
    public boolean execute() {

        for (int i = 0; i < Network.size(); i++) {

            Node current = (Node) Network.get(i);

            //System.out.println(i+" have "+ ((Announce)current.getProtocol(pid)).getNeighbors().size() +" neighbors");
            String neigbors = "";
            for (int j=0; j < ((Announce)current.getProtocol(pid)).getNeighbors().size(); j++)
                neigbors += ((Announce)current.getProtocol(pid)).getNeighbors().get(j).getID() + ", ";
            System.out.println("Node "+i+" neigbors-> "+neigbors);
        }

        return false;
    }
}