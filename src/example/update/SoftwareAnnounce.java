package example.update;

import peersim.cdsim.CDProtocol;
import peersim.config.FastConfig;
import peersim.edsim.EDProtocol
import peersim.config.Configuration;
import peersim.core.Node;
import peersim.transport.Transport;

import java.util.LinkedList;

/**
 * Created by jibou on 07/11/17.
 */
public class SoftwareAnnounce implements EDProtocol, CDProtocol {

    // Fields =================================
    String prefix;

    // We need to access the node neighbors protocol
    private final int neighPid;
    private static final String PAR_NEIGHBORS_PROT = "neighbors_protocol";

    private LinkedList<SoftwarePackage> packages = new LinkedList<>();

    // =========================================


    // Initialization ==========================

    // Constructor
    SoftwareAnnounce(String prefix){
        this.prefix = prefix;
    //get the node NodeCoordinates protocol pid
        this.neighPid = Configuration.getPid(prefix + "." + PAR_NEIGHBORS_PROT);
    }

    public Object clone() {
        return new SoftwareAnnounce(prefix);
    }


    /**
     * This method define periodic activity.
     * The frequency of execution of this method is defined by a
     * {@link peersim.edsim.CDScheduler} component in the configuration.
     *
     * It get the neighbors list from the neighbour protocol and update the available software list accordingly
     */
    public void nextCycle(Node node, int protocolID) {

    }

    /**
     * This is the standard method to define to process incoming messages.
     * It parse the receivied messages from other nodes to build and maintain
     */

    // TODO :  design the  announce message to be exchanged between nodes.
    // Process the recevied announce message into DB
    // Send announce message periodically to the neigbor list with the nextcycle() method
    // write a SoftwareDBInitializer
    // Write appropriate observer
    // test !
    public void processEvent( Node node, int pid, Object event ) {

        AnnounceMessage announce = (AnnounceMessage)event;

        if( announce.sender!=null )
            ((Transport)node.getProtocol(FastConfig.getTransport(pid))).
                    send(
                            node,
                            announce.sender,
                            new AnnounceMessage(value,null),
                            pid);

        value = (value + announce.value) / 2;
    }

}

//--------------------------------------------------------------------------
//--------------------------------------------------------------------------

/**
 * The type of a message. It contains a list of software packages
 * And the sender Node {@link peersim.core.Node}.
 */
class AnnounceMessage {

    final LinkedList<SoftwarePackage> announcedPackages;
    final Node sender;

    // Constructor
    public AnnounceMessage( LinkedList<SoftwarePackage> announcedPackages, Node sender )
    {
        this.announcedPackages = announcedPackages;
        this.sender = sender;
    }
}
