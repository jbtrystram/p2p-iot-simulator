package example.update;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

import java.util.ArrayList;
import java.util.HashSet;

public class Supervisor implements EDProtocol{

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    // Protocol to interact with : softwareDB and Downloader
    private static final String DB_PROT = "database_protocol";
    private static final String DOWNLOAD_PROT = "downloader_protocol";


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * Energy protocol identifier. Obtained from config property
     * {@link #DB_PROT}, {@link #DOWNLOAD_PROT}.
     */
    private final int dbPID;
    //private final int downloadPID;

    String prefix;
    public Supervisor(String prefix) {
        this.prefix = prefix;

        // get PIDs of SoftwareDB and Downloader
        dbPID = Configuration.getPid(prefix + "." + DB_PROT);
        //downloadPID = Configuration.getPid(prefix + "." + DOWNLOAD_PROT);
    }

    public Supervisor clone(){
        return new Supervisor(prefix);
    }

    //receive messages
    public void processEvent(Node node, int pid, Object event) {
        // not used yet

    }

    //process event recevied from softwareDB
    public void newNeighborNotification(Node neigh){
        System.out.println("supervisor "+ CommonState.getNode().getID()+ " noticed new node: "+ neigh.getID());

        // no strategy at this time : simply add the software our neighbour have but empty
        SoftwareDB db = (SoftwareDB) CommonState.getNode().getProtocol(this.dbPID);

        // get softwares owned by this neighbour
        ArrayList<SoftwarePackage> toAdd = db.getNeigbourPackageList(neigh);
        // add them in localdB but empty !
        if (toAdd != null){
            toAdd.forEach(soft -> {
               db.addLocalSoftware(soft);
            });
        }
    }


    // TODO : invoke strategy in a separate method


    // ask downloader to download something

    // may ask uploader to stop uploading
}
