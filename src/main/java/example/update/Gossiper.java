package example.update;

import peersim.cdsim.CDProtocol;
import peersim.config.FastConfig;
import peersim.edsim.EDProtocol;
import peersim.config.Configuration;
import peersim.core.Node;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;

import java.util.HashSet;
import java.util.List;

/**
 * Created by jibou on 07/11/17.
 */
public class Gossiper implements EDProtocol {

    // Fields =================================
    String prefix;

    // We need to access the node neighbors protocol
    private final int neighPid;
    private static final String PAR_NEIGHBORS_PROT = "neighbors_protocol";

    private final int supervisorPid;
    private static final String PAR_SUPER_PROT = "supervisor_protocol";

    // Variables =========================================

    HashSet<String> receviedMessages;


    // Initialization ==========================

    // Constructor
    public Gossiper(String prefix) {
        this.prefix = prefix;
        //get the node NodeCoordinates protocol pid
        this.neighPid = Configuration.getPid(prefix + "." + PAR_NEIGHBORS_PROT);
        this.supervisorPid = Configuration.getPid(prefix + "." + PAR_SUPER_PROT);
        receviedMessages = new HashSet<>();
    }

    public Object clone() {
        return new Gossiper(prefix);
    }


    /**
     * This method define periodic activity.
     * The frequency of execution of this method is defined by a
     * {@link peersim.edsim.CDScheduler} component in the configuration.
     * <p>
     * It get the neighbors list from the neighbour protocol and update the available software list accordingly
     */
    public void gossip(Node sender, Node local, int protocolID, SoftwareJob soft) {

        // create a announce message
        NetworkMessage msg = new NetworkMessage(soft, local);

        //get neighbors list
        ((NeighborhoodMaintainer) local.getProtocol(neighPid)).getNeighbors().
                // iterate
                        forEach(neigh -> {
                    if (neigh != sender) {
                        // send the message to each of them
                        EDSimulator.add(25, msg, neigh, protocolID);
                    }
                });
    }

    /**
     * This is the standard method to define to process incoming messages.
     * It parse the receivied messages from other nodes
     */
    // This method is executed whenever a message is recevied. we forward it if it was never recevied.
    public void processEvent(Node localNode, int pid, Object event) {

        // recevied message
        NetworkMessage message = (NetworkMessage) event;

        if (message.sender != null && !receviedMessages.contains(message.announcedPackage.getId())) {
            // add it to history
            receviedMessages.add(message.announcedPackage.getId());

            // notify local Scheduler of the received message
            ((Scheduler) localNode.getProtocol(supervisorPid)).processGossipMessage(localNode, message.announcedPackage);

            //forward the message to neigbors.
            gossip(message.sender, localNode, pid, message.announcedPackage);
        }
    }
}
