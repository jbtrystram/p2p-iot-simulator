package example.update.control;

import example.update.EasyCSV;
import example.update.constraints.NodeCoordinates;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

import java.util.List;


/**
 * Created by jibou on 11/10/17.
 */
public class NodeMover implements Control {


    // Fields ===========================
    private int coordPid;
    private static final String PAR_COORDINATES_PROT = "coord_protocol";

    private int maxSpeed;
    private static final String NODE_SPEED = "node_max_speed";

    private int mapSize;
    private static final String MAP_SIZE = "map_size";

    private String dataFile;
    private static final String DATA_FILE = "data_file";

    // ==========================

    // ======= Variables

    private int TICK = 0;

    private List<String[]> positions;

    // Constructor
    public NodeMover(String prefix){
        coordPid = Configuration.getPid(prefix + "." + PAR_COORDINATES_PROT);

        mapSize = Configuration.getInt(prefix + "." + MAP_SIZE);

        dataFile = Configuration.getString(prefix + "." + DATA_FILE, null);

        if ( dataFile != null) {
            EasyCSV parser = new EasyCSV(dataFile);
            positions = parser.content;
        }

    }


    // This fonction read datafile given in parameter to update nodes position.
    private int[] readCoordinates(int node) {

        if (node >= positions.size() || TICK > positions.get(0).length){
            System.err.println(dataFile + " doesn't contain enough data to move nodes. Quiting");
            System.exit(1);
        }

        return new int[] {Integer.parseInt(positions.get(node)[TICK]) , Integer.parseInt(positions.get(node)[TICK+1]) };
    }

    // This fonction compute the node next position with the given location.
    private int[] computeCoordinates(NodeCoordinates coord){
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

        return new int[] {(int)newX, (int)newY} ;
    }

    //Move the nodes
    public boolean execute() {

        int node;
        NodeCoordinates coordinates;
        int[] newCoordinates;

        for (node=0; node < Network.size(); node++) {
            coordinates = (NodeCoordinates) Network.get(node).getProtocol(coordPid);

            if (dataFile == null) {
                newCoordinates = computeCoordinates(coordinates);
            }else{
                newCoordinates = readCoordinates((int)(Network.get(node).getID()));
            }
            coordinates.setX(newCoordinates[0]);
            coordinates.setY(newCoordinates[1]);
        }
        TICK +=2;
        return false;
    }
}
