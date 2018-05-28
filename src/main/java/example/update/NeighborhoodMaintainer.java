package example.update;

import peersim.cdsim.CDProtocol;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

import java.util.LinkedList;

/**
 * Created by jibou on 28/05/18.
 */
public interface NeighborhoodMaintainer extends CDProtocol, EDProtocol {
    NeighborhoodMaintainer clone();

    LinkedList<Node> getNeighbors();

    //receive messages
    void processEvent(Node localNode, int pid, Object event);

    // This is the method called by the simulator at each cycle
    void nextCycle(Node localNode, int protId);
}
