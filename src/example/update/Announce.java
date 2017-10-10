package example.update;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;
import peersim.core.Protocol;


public class Announce implements CDProtocol {

    // Fields =================================

    // config file prefix
    String prefix;

    // We need to access the node coordinates protocol
    private final int coordPid;
    private static final String PAR_COORDINATES_PROT = "coord_protocol";

    // A reference to this node
    long nodeId;
    int nodeIndex;
    // =========================================


    // Initialization ==========================

    // Constructor
    public Announce(String prefix){
        //get the node NodeCoordinates protocol pid\
        this.prefix = prefix;
        this.coordPid = Configuration.getPid(prefix + "." + PAR_COORDINATES_PROT);
    }

    public Announce clone() {
        return new Announce(prefix);
    }
    // =========================================


    // Methods =================================

    public void setNodeId(long id) { this.nodeId = id; }

    public long getNodeId() { return this.nodeId; }

    public void setNodeIndex(int index) { this.nodeIndex = index; }

    public int getNodeIndex() { return this.nodeIndex; }

    /*public void parseAnnounce(){

    }*/

    // This is the method called by the simulator at each cycle
    public void nextCycle(Node node, int protId){

        // Go through all the nodes in the network
        for(int i =0; i< Network.size(); i++) {

            System.out.println( ((NodeCoordinates)node.getProtocol(coordPid))
                    .getDistance( (NodeCoordinates)Network.get(i).getProtocol(coordPid)) );

        }

    }

}
