package example.update.control;

import example.update.EasyCSV;
import example.update.constraints.NodeCoordinates;
import peersim.config.Configuration;
import peersim.core.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatasetNodeMover implements Control {


    // Fields ===========================
    private int coordPid;
    private static final String PAR_COORDINATES_PROT = "coord_protocol";

    private String dataFile;
    private static final String DATA_FILE = "data_file";

    NetworkDynamic dynamic;

    // ======= Variables

    private int cursor = 1;

    private List<String[]> positions;

    HashMap<Long, Integer> IDtoIndex;
    ArrayList<Long> activity;
    HashMap<Long, Long> dataIDtoID = new HashMap<>();

    // Constructor
    public DatasetNodeMover(String prefix){
        coordPid = Configuration.getPid(prefix + "." + PAR_COORDINATES_PROT);

        dataFile = Configuration.getString(prefix + "." + DATA_FILE);

        EasyCSV parser = new EasyCSV(dataFile);
        positions = parser.content;

        dynamic = new NetworkDynamic(prefix);
    }
    // dataset should be : timestamp;id_node;x;y;id_node;x;y;...


    // This fonction read datafile given in parameter to update nodes position.
    private NodeCoordinates getCoordinatesProtocol(long nodeID) {

        if (! dataIDtoID.containsKey(nodeID) ) {

            Node newNode = dynamic.add();

            dataIDtoID.put(nodeID, newNode.getID());
            return (NodeCoordinates) newNode.getProtocol(coordPid);
        } else {
            return (NodeCoordinates) Network.get(IDtoIndex.get(dataIDtoID.get(nodeID))).getProtocol(coordPid);
        }
    }

    private void refreshCache(){
        // cache a list with real nodes IDs
        IDtoIndex = new HashMap<>();
        for (int i = 0; i < Network.size(); i++) {
            IDtoIndex.put(Network.get(i).getID(), i);
        }
    }

    //Move the nodes
    public boolean execute() {

        //inits as false.
        activity = new ArrayList<>();
        long time = CommonState.getTime();
        refreshCache();

        if (cursor < positions.size()) {
            while (cursor < positions.size() && Long.parseLong(positions.get(cursor)[0]) <= time) {

                String[] row = positions.get(cursor);
                for (int i = 1; i < row.length - 1; i += 3) {
                    long nodeId = Long.parseLong(row[i]);
                    NodeCoordinates coordinates = getCoordinatesProtocol(nodeId);

                    coordinates.setX(Integer.parseInt(row[i + 1]));
                    coordinates.setY(Integer.parseInt(row[i + 2]));

                    refreshCache();
                    activity.add(dataIDtoID.get(nodeId));

                }
                cursor += 1;
            }
        } else {
            System.out.println("Reached end of movement dataset. Stopping simulation");
            return true;
        }
        // remove unmoved nodes
        boolean removed;
        do {
            removed = false;
            for (Long fixId : IDtoIndex.keySet()) {
                if (! activity.contains(fixId)) {
                    dynamic.remove(IDtoIndex.get(fixId));
                    refreshCache();
                    dataIDtoID.values().removeIf(val -> fixId.equals(val));
                    removed = true;
                    break;
                }
            }
        }while(removed);
        return false;
    }
}