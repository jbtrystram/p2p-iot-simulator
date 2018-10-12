package example.update.control;

import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dynamics.NodeInitializer;

public class NetworkDynamic {

    // Fields ===========================

    /**
     * Config parameter which gives the prefix of node initializers. An arbitrary
     * number can be specified (Along with their parameters).
     * These will be applied  on the newly created nodes. T
     * Example:
     control.0 DynamicNetwork
     control.0.init.0 RandNI
     control.0.init.0.k 5
     control.0.init.0.protocol somelinkable
     */
    private static final String PAR_INIT = "init";

    /** node initializers to apply on the newly added nodes */
    private final NodeInitializer[] inits;

    // Constructor
    public NetworkDynamic(String prefix){
        Object[] tmp = Configuration.getInstanceArray(prefix + "." + PAR_INIT);
        inits = new NodeInitializer[tmp.length];
        for (int i = 0; i < tmp.length; ++i) {
            inits[i] = (NodeInitializer) tmp[i];
        }
    }

    // create a new node and initialise it
    public Node add() {

            Node newNode = (Node) Network.prototype.clone();
            for (int j = 0; j < inits.length; ++j) {
                inits[j].initialize(newNode);
            }
            Network.add(newNode);
        return newNode;
    }

    // Delete a node
    //
    public void remove(int id){
        Network.remove(id);
    }
}