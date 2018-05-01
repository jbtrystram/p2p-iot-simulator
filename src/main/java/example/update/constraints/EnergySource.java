package example.update.constraints;

import peersim.core.Protocol;

public interface EnergySource extends Protocol {
    Object clone();

    void consume();

    void charge(int amount);

    void setCapacityMultiplier(float capacityMultiplier);

    int getLevel();

    boolean getOnlineStatus();
}
