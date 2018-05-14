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

    public Energy(String prefix){
    }

    public EnergySource getPowerSource() {return powerSource;}

    public void setPowerSource(EnergySource powerSource) {
        this.powerSource = powerSource;
    }
}