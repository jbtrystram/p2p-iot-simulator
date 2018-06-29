package example.update;

import example.update.strategies.Energy;
import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;

import java.util.LinkedList;


public class TopologyNeighborhoodMaintainer implements NeighborhoodMaintainer{

    // Fields =================================

    String prefix;

    // file containing the topology
    private final String PAR_TOPOLOGY_FILENAME = "topology_file";

    // We need to access the energy protocol
    private final int energyPid;
    private static final String PAR_ENERGY_PROT = "energy_protocol";

    private LinkedList<Node> neighbors;
    private String topologyFile;


    @Override
    public NeighborhoodMaintainer clone() {
        return new TopologyNeighborhoodMaintainer(prefix);
    }

    public TopologyNeighborhoodMaintainer(String prefix){
        //get the node NodeCoordinates protocol pid
        this.prefix = prefix;
        this.topologyFile = Configuration.getString(prefix + "." + PAR_TOPOLOGY_FILENAME);
        this.energyPid = Configuration.getPid(prefix + "." + PAR_ENERGY_PROT);

        neighbors = new LinkedList();
    }

    @Override
    public LinkedList<Node> getNeighbors() {
        return neighbors;
    }

    @Override
    public void processEvent(Node localNode, int pid, Object event) {

    }

    @Override
    public void nextCycle(Node localNode, int protId) {

        // cache a list with real nodes IDs
        int[] cache = new int[Network.size()]  ;
        for (int i=0; i<Network.size(); i++){
            cache[(int) Network.get(i).getID()] = i;
        }


        Energy power = ((Energy) localNode.getProtocol(energyPid));

        // don't send any annouce if offline and empty neighbors list
        if ( ! power.getOnlineStatus() ){
            neighbors.clear();
        }else {
            EasyCSV csv = new EasyCSV(topologyFile);
            String[] givenNeighbors = csv.content.get(localNode.getIndex());

            for(int i=1; i <givenNeighbors.length-1; i++) {
                Node current = Network.get( cache[Integer.parseInt(givenNeighbors[i])]);
                if (! neighbors.contains(current)) {
                    neighbors.add(current);
                }
            }
        }
    }
}
