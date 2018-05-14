package example.update.control;

import example.update.constraints.energy.SimpleEnergy;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jibou on 20/10/17.
 */
public class EnergySwitcher implements Control {

    // Fields ===========================
    int energyPid;
    private static final String PAR_ENERGY_PROT = "energy_protocol";

    double distribution;
    private static final String PAR_ENERGY_DISTRIBUTION = "energy_switching_probability";

    // Constructor
    public EnergySwitcher(String prefix){
        energyPid = Configuration.getPid(prefix + "." + PAR_ENERGY_PROT);
        distribution = Configuration.getDouble(prefix + "." + PAR_ENERGY_DISTRIBUTION);
    }


    public boolean execute() {
        int node;
        SimpleEnergy nodeEnergy;

        //create a list with the nodes id
        List<Integer> nodesIDs = new ArrayList<>();
        for (int i=0; i<Network.size(); i++ ){
            nodesIDs.add(i);
        }
        //shuffle the list
        Collections.shuffle(nodesIDs, CommonState.r);

        // Now use the random list to go through the network and switch a few nodes off.
        for (int i=0; i < Network.size()*(distribution/100); i++) {
            node = nodesIDs.get(i);
            nodeEnergy = (SimpleEnergy) (Network.get(node).getProtocol(energyPid));
            if (nodeEnergy.getOnlineStatus()) { nodeEnergy.setOnlineStatus(false);}
            else {nodeEnergy.setOnlineStatus(true);}
        }


        return false;
    }
}
