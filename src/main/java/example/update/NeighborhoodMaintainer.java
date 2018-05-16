package example.update;

import example.update.constraints.NetworkRange;
import example.update.constraints.NodeCoordinates;
import example.update.strategies.Energy;
import example.update.constraints.energy.EnergySource;
import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;

import java.lang.Math;
import java.util.LinkedList;

public class NeighborhoodMaintainer implements CDProtocol, EDProtocol {

    // Fields =================================

    String prefix;

    // We need to access the node coordinates protocol
    private final int coordPid;
    private static final String PAR_COORDINATES_PROT = "coord_protocol";

    // We need to access the energy protocol
    private final int energyPid;
    private static final String PAR_ENERGY_PROT = "energy_protocol";

    // protocol containing the range of the node
    private final int rangeProtcol;
    private static final String PAR_RANGE_PROT = "range_protocol";

    private LinkedList<Node> neighbors = new LinkedList();

    // =========================================


    // Initialization ==========================

    // Constructor
    public NeighborhoodMaintainer(String prefix) {
        //get the node NodeCoordinates protocol pid
        this.prefix = prefix;
        this.coordPid = Configuration.getPid(prefix + "." + PAR_COORDINATES_PROT);
        this.rangeProtcol = Configuration.getPid(prefix + "." + PAR_RANGE_PROT);
        this.energyPid = Configuration.getPid(prefix + "." + PAR_ENERGY_PROT);
    }

    public NeighborhoodMaintainer clone() {
        return new NeighborhoodMaintainer(prefix);
    }
    // =========================================


    // Methods =================================


    public LinkedList<Node> getNeighbors() {
        return neighbors;
    }


    // this is invoked when discovering a new neighbor.
    public void addNeighbor(Node sender) {
        if (! this.neighbors.contains(sender)) {
            this.neighbors.addLast(sender);
        }
    }

    public void removeNeighbor(Node sender){
        if (this.neighbors.contains(sender)) {
            this.neighbors.remove(sender);
        }
    }

    //receive messages
    public void processEvent(Node localNode, int pid, Object event) {
        Node neighbor = (Node)event;
        this.addNeighbor(neighbor);
    }

    // Invoked if we are offline
    public void cleanNeighborsList(){
        this.neighbors.clear();
    }

    // This is the method called by the simulator at each cycle
    public void nextCycle(Node localNode, int protId) {

        EnergySource power = ((Energy) localNode.getProtocol(energyPid)).getPowerSource();

        // don't send any annouce if offline and empty neighbors list
        if ( ! power.getOnlineStatus() ){
            cleanNeighborsList();
        }
        else {

            // get range of the local node
            int localRange = ((NetworkRange) localNode.getProtocol(rangeProtcol)).range;

            // go through all the nodes in the network to send announce
            for (int i = 0; i < Network.size(); i++) {

                int peerRange = ((NetworkRange) Network.get(i).getProtocol(rangeProtcol)).range;
                int distance = ((NodeCoordinates) localNode.getProtocol(coordPid))
                        .getDistance((NodeCoordinates) Network.get(i).getProtocol(coordPid));

                if (localNode.getID() != Network.get(i).getID()
                       && distance <= Math.min(localRange, peerRange)
                        && (power.getOnlineStatus())) {

                    // Node is in range and online : send announce to add myself in Node list
                    EDSimulator.add(0, localNode, Network.get(i), protId);
                }
                // Node not in range or offline; remove it from my neighbors list
                else {
                    ((NeighborhoodMaintainer) localNode.getProtocol(protId)).removeNeighbor(Network.get(i));

                }
            }
        }
    }
}
