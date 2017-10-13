package example.update;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;
import java.util.LinkedList;

public class Announce implements CDProtocol {

    // Fields =================================

    String prefix;

    // We need to access the node coordinates protocol
    private final int coordPid;
    private static final String PAR_COORDINATES_PROT = "coord_protocol";

    // Maximum distance allowed to reach neigbors
    private final int maxDistance;
    private static final String PAR_NEIGH_DISTANCE = "max_distance";

    private long myself;

    private LinkedList<Node> neighbors = new LinkedList();

    // =========================================


    // Initialization ==========================

    // Constructor
    public Announce(String prefix) {
        //get the node NodeCoordinates protocol pid
        this.prefix = prefix;
        this.coordPid = Configuration.getPid(prefix + "." + PAR_COORDINATES_PROT);
        this.maxDistance = Configuration.getInt(prefix + "." + PAR_NEIGH_DISTANCE);
    }

    public Announce clone() {
        return new Announce(prefix);
        /*try {

            return (Announce) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Announce does not support cloning");
            return null;
        }*/
    }
    // =========================================


    // Methods =================================


    public LinkedList<Node> getNeighbors() {
        return neighbors;
    }

    public void setMyself(long myself) {
        this.myself = myself;
    }

    public void addAnnounce(Node sender) {
        if (! this.neighbors.contains(sender)) {
            this.neighbors.addLast(sender);
            //System.out.println(myself +" received announce from "+sender.getID());
        }
    }

    public void removeAnnounce(Node sender){
        if (this.neighbors.contains(sender)) {
            this.neighbors.remove(sender);
        }

    }

    // This is the method called by the simulator at each cycle
    public void nextCycle(Node node, int protId) {

        // Go through all the nodes in the network
        for (int i = 0; i < Network.size(); i++) {
            int distance = ((NodeCoordinates) node.getProtocol(coordPid))
                    .getDistance((NodeCoordinates) Network.get(i).getProtocol(coordPid));

            if ( node.getID() != Network.get(i).getID() && distance
                    <= maxDistance) {
                // Node is in range : send announce\

                ((Announce) Network.get(i).getProtocol(protId)).addAnnounce(node);
            }
            // Node not in range ; try to remove it
            else{
                ((Announce) Network.get(i).getProtocol(protId)).removeAnnounce(node);
            }
        }

    }
}
