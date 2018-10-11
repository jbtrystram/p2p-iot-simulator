package example.update.constraints;

import peersim.core.Protocol;

public class NetworkRange implements Protocol {

    // range of the device, in meters.
    public int range;


    public NetworkRange(String prefix) {
        // uninitialised range is 0
        range = 0;
    }

    public Object clone() {
        return  new NetworkRange("");
    }

    public void setRange(int value) {
        range = value;
    }
}