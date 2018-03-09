package example.update;

import peersim.cdsim.CDProtocol;
import peersim.config.FastConfig;
import peersim.edsim.EDProtocol;
import peersim.config.Configuration;
import peersim.core.Node;
import peersim.transport.Transport;

import java.util.HashSet;
import java.util.List;

/**
 * Created by jibou on 07/11/17.
 */
public class Gossiper implements EDProtocol{ //, CDProtocol {

    // Fields =================================
    String prefix;

    // We need to access the node neighbors protocol
    private final int neighPid;
    private static final String PAR_NEIGHBORS_PROT = "neighbors_protocol";

    private final int dbpid;
    private static final String PAR_DATABASE_PROT = "database_protocol";

    private final int supervisorPid;
    private static final String PAR_SUPER_PROT = "supervisor_protocol";

    // Variables =========================================

    HashSet<byte[]> receviedMessages;


    // Initialization ==========================

    // Constructor
    public Gossiper(String prefix){
        this.prefix = prefix;
    //get the node NodeCoordinates protocol pid
        this.neighPid = Configuration.getPid(prefix + "." + PAR_NEIGHBORS_PROT);
        this.dbpid = Configuration.getPid(prefix + "." + PAR_DATABASE_PROT);
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
     *
     * It get the neighbors list from the neighbour protocol and update the available software list accordingly
     */
    //TODO : don't share whole db each round but forward message upon reception.
    // And discard already recevied messages.
    // more gossip style !
    // let's go full event-driven
    private void forward(Node sender, Node local, int protocolID, SoftwarePackage soft) {

            // create a announce message
            NetworkMessage msg = new NetworkMessage(soft, local);

            //get neighbors list
            ((NeighborhoodMaintainer) local.getProtocol(neighPid)).getNeighbors().
                    // iterate
                            forEach(neigh -> {
                                if (neigh != sender) {
                                    // send the message to each of them
                                    ((Transport) local.getProtocol(FastConfig.getTransport(neighPid))).send(
                                            local, neigh, msg, protocolID);
                                }
                            });
    }

    /**
     * This is the standard method to define to process incoming messages.
     * It parse the receivied messages from other nodes
     */
    // This method is executed whenever a message is recevied. we forward it if it was never recevied.
    public void processEvent( Node node, int pid, Object event ) {

        // recevied message
        NetworkMessage message = (NetworkMessage)event;

        if( message.sender!=null && !receviedMessages.contains(message.announcedPackage.getId()) ){
            receviedMessages.add(message.announcedPackage.getId());

            // process each software into the local db.
            SoftwareDB db = (SoftwareDB) node.getProtocol(dbpid);
            db.addNeigborSoftware(message.announcedPackage, message.sender);

            //forward the message to neigbors.
            forward(message.sender, node, pid, message.announcedPackage);

            // notify local Scheduler of the new neighbor
            ((Scheduler)node.getProtocol(supervisorPid)).newNeighborNotification(message.sender);
        }
    }

}

//--------------------------------------------------------------------------
//--------------------------------------------------------------------------

/**
 * The type of a message. It contains a list of software packages
 * And the sender Node {@link peersim.core.Node}.
 */
class NetworkMessage {



    final SoftwarePackage announcedPackage;
    final Node sender;

    // Constructor
    public NetworkMessage(SoftwarePackage announcedPackage, Node sender ) {
        this.announcedPackage = announcedPackage;
        this.sender = sender;
    }
}
