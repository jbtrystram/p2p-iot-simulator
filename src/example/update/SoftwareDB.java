package example.update;

import peersim.core.Node;
import peersim.core.Protocol;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.HashSet;
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


    public void addLocalSoftware(SoftwarePackage soft) {

          // already Existing software?
        int softID = sameSoftware(soft, localPieces);
        // yes : remove it
        if ( softID != -1 ) {
            localPieces.remove(softID);
        }
        localPieces.add(soft);
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


    // Remove neighbors in neighborsPieces that are not in the provided list
    public void keepOnly(List<Node> list){
        HashSet<Node> toRemove = new HashSet<>();
        neighborsPieces.keySet().forEach( node -> {
            if ( ! list.contains(node) ) {
                toRemove.add(node);
            }
        });
        toRemove.forEach(node -> {
            neighborsPieces.remove(node);
        });
    }

    public boolean localIsEmpty(){
        if (this.localPieces.size() == 0 ){
            return true;
        }
        else return false;
    }

    // Nicely print the content of the softwareDB
    public String toString(){
        StringBuilder output = new StringBuilder();

        // localDB
        if (localPieces.size() != 0 ) {
            output.append("Local DB :\n");
            localPieces.forEach(soft -> {
                output.append("    -" + soft.getName() + ":" + soft.getVersion());
                output.append("; hashes : ");
                soft.getPieces().forEach(piece -> {
                    if (piece != null) {
                        output.append("v");
                    } else {
                        output.append(".");
                    }
                });
                output.append("\n");
            });
        }

        // neighbors
        if (neighborsPieces.size() != 0) {
            output.append("-----------------\n");
            output.append("neigbors DB :\n ");
            neighborsPieces.keySet().forEach(neighbour -> {
                output.append("   N" +neighbour.getID()+"\n");
                neighborsPieces.get(neighbour).forEach(soft -> {
                    output.append("    -" + soft.getName() + ":" + soft.getVersion());
                    output.append("; hashes : ");
                    soft.getPieces().forEach(piece -> {
                        if (piece != null) {
                            output.append("v");
                        } else {
                            output.append(".");
                        }
                    });
                    output.append("\n");
                });
                output.append("   ++++\n");
            });
            output.append("===================================\n");
        }

        return output.toString();
    }
}