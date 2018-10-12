package example.update;

import peersim.config.Configuration;
import peersim.core.Node;

import java.util.ArrayList;

public class BTScheduler extends Scheduler {

    @Override
    public Scheduler clone() {
        return new NaiveScheduler(prefix);
    }

    String prefix;
    public BTScheduler(String prefix) {
        this.prefix = prefix;

        // get PIDs of gossiper and Downloader
        gossipPID = Configuration.getPid(prefix + "." + GOSSIP_PROT);
        netPID = Configuration.getPid(prefix + "." + NET_PROT);

        jobsList = new ArrayList<>();
        this.spaceProtocol = Configuration.getPid(prefix + "." + DISK_SPACE_PROT);
        this.bandwidthProtocol = Configuration.getPid(prefix + "." + BANDW_PROT);
    }


    private int cycle_counter=0;
    // This method is called each cycle and this where the strategy is implemented.
    // the job list must be updated according to the proper strategy.
    //TODO : some feedback from net agent
    public void updateTasks(Node localNode){

    }
}
