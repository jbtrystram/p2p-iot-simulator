package example.update.strategies;

import example.update.constraints.energy.EnergySource;
import peersim.core.Protocol;

// placeholder class containing a reference to the
// node's energy sources
public class Energy implements Protocol {

    public Object clone(){
        return new Energy(null);
    }

    private EnergySource powerSource;
    private long powerDraws;

    public Energy(String prefix){
        powerDraws = 0;
    }

    public void consume(int amount){
        powerDraws +=amount;
        // drain battery every 100 battery-hungry action are done (we would not consume 1% of the battery for 1 msg)
        if (powerDraws % 300 == 0) {
            powerSource.consume();
        }
    }

    public boolean getOnlineStatus(){
        return powerSource.getOnlineStatus();
    }

    public EnergySource getPowerSource() {return powerSource;}

    public void setPowerSource(EnergySource powerSource) {
        this.powerSource = powerSource;
    }
}