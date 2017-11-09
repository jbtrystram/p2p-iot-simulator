package example.update;

import java.util.*;

/**
 * A class representing a software package.
 * Stores completed hashes
 */
public class SoftwarePackage {

    private String name;
    private int size;
    private double version;

    private HashMap<String, Boolean> hashesStatus;

    SoftwarePackage(String name, double version){
        this.name = name;
        this.version = version;
        this.size = 0;
        this.hashesStatus = new HashMap<>();
    }

    SoftwarePackage(String name, double version, int size, Set<String> hashes){
        this.name = name;
        this.version = version;
        this.size = hashes.size();
        this.hashesStatus = new HashMap<>();
        hashes.forEach((hash) -> this.hashesStatus.put(hash, false));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setVersion(double version) { this.version = version; }

    public Set<String> getPieces(){return this.hashesStatus.keySet();}

    public Set<String> getCompletedPieces(){
        Set<String> tmp = new HashSet<>();
        hashesStatus.forEach((k,v) -> {
            if (v) tmp.add(k); });
        return tmp;}

    public void setHashes(Set<String> hashes){
        hashes.forEach((hash) -> this.hashesStatus.put(hash, false));
    }

    public boolean hasHash(String hash) {
        if (this.hashesStatus.containsKey(hash) ) return true;
        else return false;
    }


    public void comletePiece(String hash) {
        if ( hashesStatus.containsKey(hash) ){
            hashesStatus.replace(hash, true);
        }
    }
}

