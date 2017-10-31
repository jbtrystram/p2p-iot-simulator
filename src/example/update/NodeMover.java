package example.update;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

import java.awt.print.Book;

/**
 * Created by jibou on 11/10/17.
 */
public class NodeMover implements Control {


    // Fields ===========================
    int coordPid;
    private static final String PAR_COORDINATES_PROT = "coord_protocol";

    private int maxSpeed;
    private static final String NODE_SPEED = "node_max_speed";

    private int mapSize;
    private static final String MAP_SIZE = "map_size";

    // Constructor
    public NodeMover(String prefix){
        coordPid = Configuration.getPid(prefix + "." + PAR_COORDINATES_PROT);
        //maxSpeed = Configuration.getInt(prefix + "." + NODE_SPEED);
        mapSize = Configuration.getInt(prefix + "." + MAP_SIZE);
    }

    // This fonction compute the node next position with the given location.
    private void changeCoordinates(NodeCoordinates coord){
        boolean outOfBonds;
        // we need doubles to have precision during the computation
        double radians = Math.toRadians(coord.getAngle());
        double newX, newY;

        newX = coord.getX() + ( coord.getSpeed() * Math.cos(radians));
        newY = coord.getY() + ( coord.getSpeed() * Math.sin(radians));

        // Avoid go out of boundaries
        int angle = coord.getAngle();

        //Simple bounce
        if (newX > mapSize) {
            newX = mapSize - (newX - mapSize);
            if (270 < angle && angle <= 360 ) { coord.setAngle(180+360-angle);}
            else if ( 0 <= angle && angle < 90) { coord.setAngle(180-angle);}
        }
        if (newX < 0 ) {
            newX = -newX;
            if (90 < angle && angle <= 180 ) { coord.setAngle(180-angle);}
            else if (180 <= angle && angle < 270) { coord.setAngle(180+360-angle);}
        }

        if (newY > mapSize) {
            newY = mapSize - (newY - mapSize);
            if (0 < angle && angle <= 90 ) { coord.setAngle(360-angle);}
            else if (90 <= angle && angle < 180) { coord.setAngle(360-angle);}
        }
        if (newY < 0 ) {
                newY = -newY;
                if (270 <= angle && angle < 360 ) { coord.setAngle(360-angle);}
                else if (180 < angle && angle <= 270) { coord.setAngle(360-angle);}
            }

        coord.setX((int)newX);
        coord.setY((int)newY);
    }

    //Move the nodes
    public boolean execute() {
        System.out.println("Node Mover is run");

        int node;
        NodeCoordinates coordinates;
        for (node=0; node < Network.size(); node++) {
            coordinates = (NodeCoordinates) Network.get(node).getProtocol(coordPid);
            changeCoordinates(coordinates);
        }
        return false;
    }
}
