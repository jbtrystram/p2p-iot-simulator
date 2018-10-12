package example.update;

import java.security.MessageDigest;
import java.util.*;

/**
 * A class representing a software package.
 * Stores completed hashes
 */
public class SoftwarePackage {

    private final String name;
    private int size;
    private double version;
    //private final byte[] id;

    private HashMap<String, Boolean> hashesStatus;

    public SoftwarePackage(String name, double version, int size, Set<String> hashes){
        this.name = name;
        this.version = version;
        this.size = hashes.size();
        this.hashesStatus = new HashMap<>();
        hashes.forEach((hash) -> this.hashesStatus.put(hash, false));

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            }catch(Exception e) {
            System.err.println("exception in message digest creation : " + e.getMessage());
            System.exit(1);
        }
       // this.id = md.digest((name + version).getBytes());
    }

    public String getName() {
        return name;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getVersion() {
        return version;
    }

    //public byte[] getId() { return id; }

    public List<String> getPieces(){return new ArrayList<String>(this.hashesStatus.keySet());}

    public Set<String> getCompletedPieces(){
        Set<String> tmp = new HashSet<>();
        hashesStatus.forEach((k,v) -> {
            if (v) tmp.add(k); });
        return tmp;}

    public void setHashes(Set<String> hashes){
        hashes.forEach((hash) -> this.hashesStatus.put(hash, false));
    }


    public boolean hasHash(String hash) {
        if (this.hashesStatus.containsKey(hash)) return true;
        else return false;
    }

    public void comletePiece(String hash) {
        if ( hashesStatus.containsKey(hash) ){
            hashesStatus.replace(hash, true);
        }
    }

    public boolean getPieceStatus(String hash){
        return hashesStatus.get(hash);
    }

    private void resetPiece(String hash) {
        if ( hashesStatus.containsKey(hash) ){
            hashesStatus.replace(hash, false);
        }
    }

    public void comleteAllPieces(){
        this.hashesStatus.keySet().forEach(key ->{
            comletePiece(key);
        });
    }

    public void resetAllPieces() {
        this.hashesStatus.keySet().forEach(key ->{
            resetPiece(key);
        });
    }
}

