package example.update.constraints;


import peersim.core.Protocol;

/**
 * <p>
 * This class runs into each nodes and
 * simply stores the node's remaining storage usedSpace.
 * </p>
 */
public class Storage implements Protocol {


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    // total storage of the node, in KILOBYTES.
    private int totalSpace;

    // currently available usedSpace, in KILOBYTES
    private int usedSpace;


    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public Storage(String prefix) {

        usedSpace =0;
    }

    public Object clone() {
        return new Storage(null);
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------


    public void setTotalSpace(int KiloBytes) {
        totalSpace = KiloBytes;
    }

    public boolean allocateSpace(int KiloBytes) {
        if (usedSpace+KiloBytes <= totalSpace){
            usedSpace += KiloBytes;
            return true;
        } else return false;
    }

    public void freeSpace(int KiloBytes) {
        if (usedSpace-KiloBytes < 0) {
            usedSpace = 0;
        } else usedSpace -= KiloBytes;
    }

    public int getUsage(){
        return ( (int) ((float)usedSpace/ (float)totalSpace)*100);
    }
}