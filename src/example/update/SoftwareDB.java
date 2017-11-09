package example.update;

import peersim.core.Node;
import peersim.core.Protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class Stores the running software on the local node
 * and running software on other nodes
 *
 */
public class SoftwareDB implements Protocol {

    /* Local available software */
    private ArrayList <SoftwarePackage> localPieces;

    /* Hashmap : neighbor -> list of packages   */
    private HashMap<Node, ArrayList<SoftwarePackage>> neighborsPieces;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public SoftwareDB(String prefix) {
        /* initialize empty lists */
        this.localPieces = new ArrayList<>();
        this.neighborsPieces =  new HashMap<>();
    }

    public Object clone() {
        return  new SoftwareDB("");
    }

    //TODO finish
    public boolean addLocalSoftware(String hash, SoftwarePackage soft) {
        for (int i =0; i < localPieces.size(); i++) {
            if ( ! localPieces.get(i).hasHash(hash) ) {
                localPieces.get(i).
            }
        }

    }

    // Return a list of the local running software. Polled by the announce protocol regularly
    ArrayList<SoftwarePackage> getLocalSoftwareList(){
        return this.localPieces;
    }

    // the built-in method "contains" would return false if an existing software but with
    // different completed hashes was submitted (different object)
    // this method return the SoftwarePackage ID in the list if it exists, -1 if not
    private int sameSoftware(SoftwarePackage soft, List<SoftwarePackage> list){

        for (int i =0; i<list.size() ;i++) {
            SoftwarePackage listedSoftware = list.get(i);
             if ( listedSoftware.getName().equals(soft.getName())
                    && listedSoftware.getVersion() == soft.getVersion()) {
                return i;
            }
        }
        return -1;
    }



    public void addNeigborSoftware(SoftwarePackage soft, Node neighbor) {
        // unknown neighbor ?
        if (! neighborsPieces.containsKey( neighbor )) {
            neighborsPieces.put(neighbor, new ArrayList<>());
        }

        // already Existing software?
        int softID = sameSoftware(soft, neighborsPieces.get(neighbor));
        // yes : remove it
        if ( softID != -1 ) {
            neighborsPieces.get(neighbor).remove(softID);
        }
        neighborsPieces.get(neighbor).add(soft);
    }
}
