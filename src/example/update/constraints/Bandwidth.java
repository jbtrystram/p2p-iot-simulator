package example.update.constraints;

import peersim.core.Protocol;

public class Bandwidth implements Protocol {

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    // total uplink of the node, in KILOBYTES PER SECONDS.
    private int uplinkCapacity;
    private int uplinkUsed;

    // total downlink of the node, in KILOBYTES PER SECONDS.
    private int downlinkCapacity;
    private int downlinkUsed;


    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------
    public Bandwidth(String prefix) {

        uplinkUsed = 0;
        downlinkUsed = 0;
    }

    public Object clone() {
        return new Bandwidth(null);
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------


    public void setUplink(int bitrate) {
        uplinkCapacity = bitrate;
    }

    public void setDownlink(int bitrate) {
        downlinkCapacity = bitrate;
    }

    public boolean allocateUpload(int KiloBytes) {
        if (uplinkUsed+KiloBytes <= uplinkCapacity){
            uplinkUsed += KiloBytes;
            return true;
        } else return false;
    }

    public void freeUpload(int KiloBytes) {
        if (uplinkUsed-KiloBytes < 0) {
            uplinkUsed = 0;
        } else uplinkUsed -= KiloBytes;
    }

    public boolean allocateDownload(int KiloBytes) {
        if (downlinkUsed+KiloBytes <= downlinkCapacity){
            downlinkUsed += KiloBytes;
            return true;
        } else return false;
    }

    public void freeDownload(int KiloBytes) {
        if (downlinkUsed-KiloBytes < 0) {
            downlinkUsed = 0;
        } else downlinkUsed -= KiloBytes;
    }

    public int getUplinkUsage(){
        return ( (int) ((float)uplinkUsed/ (float)uplinkCapacity)*100);
    }

    public int getDownlinkUsage(){
        return ( (int) ((float)downlinkUsed/ (float)downlinkCapacity)*100);
    }

    public int getDownlinkCapacity() {
        return downlinkCapacity;
    }

    public int getUplinkCapacity() {
        return uplinkCapacity;
    }
}
