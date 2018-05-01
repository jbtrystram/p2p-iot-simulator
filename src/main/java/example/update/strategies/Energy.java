package example.update.strategies;

import example.update.constraints.EnergySource;
import peersim.core.Protocol;

import peersim.config.Configuration;


public class Energy implements Protocol {

    // config parameters
    String ENERGY_SOURCE = "energy_source";
    String prefix;
    EnergySource source;


    // fields
    int energySourcePID;

    public Energy(String prefix) {
        this.prefix = prefix;
        energySourcePID = Configuration.getPid(prefix + "." + ENERGY_SOURCE);
    }

    public Object clone() {
        return new Energy(prefix);
    }


}
