package example.update.constraints;

public class Battery extends SimpleEnergy {

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
        super(null);
        battery = 100;
    }

    public Object clone() {
        return new Battery(null);
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------


    public void consume() {
        battery -= (1 / this.capacityMultiplier);
    }

    public void charge(int amount) {
        this.battery += amount;
    }

    public void setCapacityMultiplier(float capacityMultiplier) {
        this.capacityMultiplier = capacityMultiplier;
    }

    public int getLevel(){
        return ( (int) battery);
    }
}
