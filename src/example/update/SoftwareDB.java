package example.update;

import peersim.core.Node;
import peersim.core.Protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class Stores the running software on the local node
 * and running software on other nodes
 *
 */
public class SoftwareDB implements Protocol {

    /* Local available software */
    private ArrayList <SoftwarePackage> localPackages;

    /* Hashmap : piece -> neighbor  */
    private HashMap<simpleSoftwarePiece, ArrayList<Node>> pieces;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public SoftwareDB(String prefix) {
        /* initialize empty list */
        this.localPieces = new LinkedList<>();
        this.pieces =  new HashMap<simpleSoftwarePiece, ArrayList<Node>>();
    }

    public Object clone() {
        return  new SoftwareDB("");
    }

    public void addPiece(String hash, SoftwarePackage soft, Node node) {
        if (! localPieces.contains(hash)) {
            localPieces.addLast(new simpleSoftwarePiece(hash, soft));
        }
    }

    public void addNeigborPiece(String hash, SoftwarePackage soft, Node neighbor) {
        if (! pieces.containsKey(new simpleSoftwarePiece(hash, soft))) {
            localPieces.addLast(new simpleSoftwarePiece(hash, soft));
        }
    }

    public void setOnlineStatus(boolean status) { online = status; }
}
