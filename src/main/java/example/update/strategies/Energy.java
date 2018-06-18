package example.update.strategies;

import example.update.constraints.energy.EnergySource;
import peersim.cdsim.CDProtocol;
import peersim.core.Node;

// placeholder class containing a reference to the
// node's energy sources
public class Energy implements CDProtocol {

    public Object clone(){
        return new Energy(null);
    }

    private EnergySource powerSource;
    private int powerDraws;

    public Energy(String prefix){
        powerDraws = 0;
    }

    public void consume(int amount){
        powerDraws +=1;
        // drain battery every 100 battery-hungry action are done (we would not consume 1% of the battery for 1 msg)
        if (powerDraws % 100 == 0) {
            powerSource.consume(amount);
        }
    }

    public boolean getOnlineStatus(){
        return powerSource.getOnlineStatus();
    }

    public EnergySource getPowerSource() {return powerSource;}

    public void setPowerSource(EnergySource powerSource) {
        this.powerSource = powerSource;
    }

    // this decrease battery level linearly over time (tunable rate with the scheduler step.)
    public void nextCycle(Node node, int pid) {
        powerSource.consume();
    }
}