package example.update;

import peersim.cdsim.CDProtocol;
import peersim.config.FastConfig;
import peersim.edsim.EDProtocol;
import peersim.config.Configuration;
import peersim.core.Node;
import peersim.transport.Transport;

import java.util.List;

/**
 * Created by jibou on 07/11/17.
 */
public class SoftwareAnnounce implements EDProtocol, CDProtocol {

    // Fields =================================
    String prefix;

    // We need to access the node neighbors protocol
    private final int neighPid;
    private static final String PAR_NEIGHBORS_PROT = "neighbors_protocol";

    private final int dbpid;
    private static final String PAR_DATABASE_PROT = "database_protocol";

    // =========================================


    // Initialization ==========================

    // Constructor
    SoftwareAnnounce(String prefix){
        this.prefix = prefix;
    //get the node NodeCoordinates protocol pid
        this.neighPid = Configuration.getPid(prefix + "." + PAR_NEIGHBORS_PROT);
        this.dbpid = Configuration.getPid(prefix + "." + PAR_DATABASE_PROT);
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
        // get access to the node software database
        SoftwareDB db = (SoftwareDB) node.getProtocol(dbpid);

        // create a announce message
        AnnounceMessage msg = new AnnounceMessage(db.getLocalSoftwareList() , node);

        //get neighbors list
        ((Announce) node.getProtocol(neighPid)).getNeighbors().
                // iterate
                forEach( neigh -> {
                    // send the message to each of them
                    ((Transport) node.getProtocol(FastConfig.getTransport(neighPid))).send(
                        node, neigh, msg, protocolID);
                });


        /**
         * Once Announce messages have been sent, pass the neighbor list to
         * the local instance of software DB to remove neighbors that are not around anymore
         */
        db.keepOnly(((Announce) node.getProtocol(neighPid)).getNeighbors());
    }

    /**
     * This is the standard method to define to process incoming messages.
     * It parse the receivied messages from other nodes to build and maintain
     */

    // TODO Write appropriate observer
    // test !
    public void processEvent( Node node, int pid, Object event ) {

        // recevied message
        AnnounceMessage message = (AnnounceMessage)event;

        if( message.sender!=null ){
            // process each software into the local db.
            SoftwareDB db = (SoftwareDB) node.getProtocol(dbpid);

            message.announcedPackages.forEach( soft ->
                db.addNeigborSoftware(soft, message.sender)
            );
        }
    }

}

//--------------------------------------------------------------------------
//--------------------------------------------------------------------------

/**
 * The type of a message. It contains a list of software packages
 * And the sender Node {@link peersim.core.Node}.
 */
class AnnounceMessage {

    final List<SoftwarePackage> announcedPackages;
    final Node sender;

    // Constructor
    public AnnounceMessage( List<SoftwarePackage> announcedPackages, Node sender ) {
        this.announcedPackages = announcedPackages;
        this.sender = sender;
    }
}
