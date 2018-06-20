package example.update.constraints.energy;

/**
 * <p>
 * This class runs into each nodes and
 * simply stores the node's energy state.
 * </p>
 */
public class SimpleEnergy implements EnergySource {

    /* Internal node energy state */
    private boolean online;


    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public SimpleEnergy(String prefix) {
        /* Un-initialized node is offline */
       online = false;
    }

    public Object clone() {
        return  new SimpleEnergy("");
    }

    public boolean getOnlineStatus() {
        return online;
    }

    public void setOnlineStatus(boolean status) { online = status; }

    // unlimited energy !
    public void consume() {}

    public void charge(int amount){
        this.setOnlineStatus(true);
    }

    public void setCapacityMultiplier(float capacityMultiplier){}

    //always on
    public int getLevel(){
     if (online) return 101;
     else return 0;
    }
}
