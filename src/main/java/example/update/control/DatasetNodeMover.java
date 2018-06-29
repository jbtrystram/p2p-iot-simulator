package example.update.control;

import example.update.EasyCSV;
import example.update.constraints.NodeCoordinates;
import peersim.Simulator;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;

import java.util.List;

public class DatasetNodeMover implements Control {


    // Fields ===========================
    private int coordPid;
    private static final String PAR_COORDINATES_PROT = "coord_protocol";

    private String dataFile;
    private static final String DATA_FILE = "data_file";

    // ==========================

    // ======= Variables

    private int cursor = 0;

    private List<String[]> positions;

    // Constructor
    public DatasetNodeMover(String prefix){
        coordPid = Configuration.getPid(prefix + "." + PAR_COORDINATES_PROT);

        dataFile = Configuration.getString(prefix + "." + DATA_FILE);

        EasyCSV parser = new EasyCSV(dataFile);
        positions = parser.content;
    }
    // dataset should be : timestamp;id_node;x;y;id_node;x;y;...


    // This fonction read datafile given in parameter to update nodes position.
    private NodeCoordinates getCoordinatesProtocol(int nodeID) {

        if (Network.
        (NodeCoordinates) Network.get(nodeId).getProtocol(coordPid);

        if (node >= positions.size() || TICK > positions.get(0).length){
            System.err.println(dataFile + " doesn't contain enough data to move nodes. Quiting");
            System.exit(1);
        }

        return new int[] {Integer.parseInt(positions.get(node)[TICK]) , Integer.parseInt(positions.get(node)[TICK+1]) };
    }

    //Move the nodes
    public boolean execute() {
        long time = CommonState.getTime();

        while (Long.parseLong(positions.get(cursor)[0]) <= time){

            String[] row = positions.get(cursor);
            for (int i=1 ; i < row.length; i+=3){

                int nodeId = Integer.parseInt(row[i]);
                NodeCoordinates coordinates = getCoordinatesProtocol(nodeId);

                coordinates.setX(Integer.parseInt(row[i+1]));
                coordinates.setY(Integer.parseInt(row[i+2]));

            }
            cursor+=1;
        }
        return false;
    }
}
