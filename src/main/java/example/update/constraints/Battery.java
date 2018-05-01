package example.update.constraints;

public class Battery implements EnergySource {

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    // Battery level.
    private float battery;

    // value of 1 percent. This way we can simulate various batteries capacity.
    float capacityMultiplier;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public Battery(String prefix) {
        battery = 100;
    }

    @Override
    public Object clone() {
        return new Battery(null);
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------


    @Override
    public void consume() {
        battery -= (1 / this.capacityMultiplier);
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
        if (battery > 0) {
            return true;
        } else return false;
    }
}
