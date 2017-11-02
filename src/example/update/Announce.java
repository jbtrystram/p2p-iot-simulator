package example.update;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;

import java.util.ArrayList;
import java.util.LinkedList;

public class Announce implements CDProtocol {

    // Fields =================================

    String prefix;

    // We need to access the node coordinates protocol
    private final int coordPid;
    private static final String PAR_COORDINATES_PROT = "coord_protocol";

    // We need to access the energy protocol
    private final int energyPid;
    private static final String PAR_ENERGY_PROT = "energy_protocol";

    // Maximum distance allowed to reach neigbors
    private final int maxDistance;
    private static final String PAR_NEIGH_DISTANCE = "max_distance";

    private LinkedList<Node> neighbors = new LinkedList();

    // =========================================


    // Initialization ==========================

    // Constructor
    public Announce(String prefix) {
        //get the node NodeCoordinates protocol pid
        this.prefix = prefix;
        this.coordPid = Configuration.getPid(prefix + "." + PAR_COORDINATES_PROT);
        this.maxDistance = Configuration.getInt(prefix + "." + PAR_NEIGH_DISTANCE);
        this.energyPid = Configuration.getPid(prefix + "." + PAR_ENERGY_PROT);
    }

    public Announce clone() {
        return new Announce(prefix);
    }
    // =========================================


    // Methods =================================


    public LinkedList<Node> getNeighbors() {
        return neighbors;
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

    // Invoked if we are offline
    public void cleanNeighborsList(){
        this.neighbors.clear();
    }

    // This is the method called by the simulator at each cycle
    public void nextCycle(Node node, int protId) {

        // don't send any annouce if offline and empty neighbors list
        if ( !(( (SimpleEnergy) (node.getProtocol(energyPid))).getOnlineStatus()) ){
            cleanNeighborsList();
        }
        else {
           /* ArrayList<Node> toremove = new ArrayList<>();
            // Firstly, ping old neighbors list to maintain it
            for (Node neigh : neighbors) {
                if ( ! ((SimpleEnergy)(neigh.getProtocol(energyPid))).getOnlineStatus() ){
                    System.out.println("Node "+node.getID()+": "+neigh.getID()+" is offline, i remove it");
                    toremove.add(neigh);
                }
            }
            for (Node removeNode : toremove){
                neighbors.remove(removeNode);
            } */

            // Then go through all the nodes in the network to send announce
            for (int i = 0; i < Network.size(); i++) {
                int distance = ((NodeCoordinates) node.getProtocol(coordPid))
                        .getDistance((NodeCoordinates) Network.get(i).getProtocol(coordPid));

                if (node.getID() != Network.get(i).getID()
                       && distance <= maxDistance
                        && ((SimpleEnergy)(Network.get(i).getProtocol(energyPid))).getOnlineStatus()) {
                    // Node is in range and online : send announce to add myself in Node list
                    ((Announce) Network.get(i).getProtocol(protId)).addAnnounce(node);
                }
                // Node not in range or offline; remove myself from Node list
                else {
                    ((Announce) Network.get(i).getProtocol(protId)).removeAnnounce(node);
                }
            }
        }

    }
}
