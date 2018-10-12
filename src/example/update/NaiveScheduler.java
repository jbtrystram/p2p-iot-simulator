package example.update;

import peersim.config.Configuration;
import peersim.core.Node;

import java.util.*;


public class NaiveScheduler extends Scheduler {

    @Override
    public Scheduler clone() {
        return new NaiveScheduler(prefix);
    }

    String prefix;
    public NaiveScheduler(String prefix) {
        this.prefix = prefix;

        // get PIDs of gossiper and Downloader
        gossipPID = Configuration.getPid(prefix + "." + GOSSIP_PROT);
        netPID = Configuration.getPid(prefix + "." + NET_PROT);

        jobsList = new ArrayList<>();
        this.spaceProtocol = Configuration.getPid(prefix + "." + DISK_SPACE_PROT);
        this.bandwidthProtocol = Configuration.getPid(prefix + "." + BANDW_PROT);
        }


    private int cycle_counter=0;
    public void updateTasks(Node localNode){
        if (cycle_counter % 4 == 0) {
            // TODO : gossip completed jobs ?
            // periodically, gossip all the packages once
            gossipAllJobs(localNode);
        }
        cycle_counter +=1;
    }

}
