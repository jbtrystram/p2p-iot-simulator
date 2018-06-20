package example.update.control;

import example.update.constraints.energy.Battery;
import example.update.strategies.Energy;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;

/**
 * Created by jibou on 20/10/17.
 */
public class BatteryDecreaser implements Control {

    // Fields ===========================
    int energyPid;
    private static final String PAR_ENERGY_PROT = "energy_protocol";

    // Constructor
    public BatteryDecreaser(String prefix){
        energyPid = Configuration.getPid(prefix + "." + PAR_ENERGY_PROT);
    }


    public boolean execute() {
        Energy nodeEnergy;
        /*
        for (int i=0; i<Network.size(); i++ ) {
            nodeEnergy = (Energy)Network.get(i).getProtocol(energyPid);
            if (nodeEnergy.getPowerSource().getClass() == Battery.class){
                //decrease the battery a bit.
                //TODO :)
            }
        }
        */
        return false;
    }
}
