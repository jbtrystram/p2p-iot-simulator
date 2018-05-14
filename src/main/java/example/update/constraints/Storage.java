package example.update.constraints;


/**
 * <p>
 * This class runs into each nodes and
 * simply stores the node's remaining storage usedSpace.
 * </p>
 */
public class Storage {


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    // total storage of the node, in KILOBYTES.
    private int totalSpace;

    // currently usedSpace, in KILOBYTES
    private int usedSpace;


    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public Storage() {

        usedSpace =0;
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

    public int getFreeSpace(){
        return totalSpace - usedSpace;
    }

    public int getUsage(){
        return ( (int) ((float)usedSpace/ (float)totalSpace)*100);
    }
}