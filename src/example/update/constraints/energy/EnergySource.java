package example.update.constraints.energy;


public interface EnergySource {

    void consume();

    void charge(int amount);

    void setCapacityMultiplier(float capacityMultiplier);

    int getLevel();

    boolean getOnlineStatus();

    void setOnlineStatus(boolean status);
}
