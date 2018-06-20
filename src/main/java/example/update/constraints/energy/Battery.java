package example.update.constraints.energy;

public class Battery implements EnergySource {

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    // Battery level.
    private float battery;

    // value of 1 percent. This way we can simulate various batteries capacity.
    float capacityMultiplier;

    boolean online;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public Battery() {
        battery = 100;
        capacityMultiplier = 1;
    }


    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------


    @Override
    public void consume() {
        if (battery > 0 && battery - (1 / this.capacityMultiplier) < 0){
            battery = 0;
        }else if( battery > 0 ){
            battery -= 1;
        }
    }

    @Override
    public void charge(int amount) {
        this.battery += amount;
    }

    @Override
    public void setCapacityMultiplier(float capacityMultiplier) {
        this.capacityMultiplier = capacityMultiplier;
    }

    @Override
    public int getLevel(){
        return ( (int) battery);
    }

    @Override
    public boolean getOnlineStatus() {
        if (battery > 0 && online) {
            return true;
        } else return false;
    }

    public void setOnlineStatus(boolean online) {
        this.online = online;
    }
}