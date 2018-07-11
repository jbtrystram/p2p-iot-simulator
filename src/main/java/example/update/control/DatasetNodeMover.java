package example.update.control;

import example.update.EasyCSV;
import example.update.constraints.NodeCoordinates;
import peersim.config.Configuration;
import peersim.core.*;
import peersim.dynamics.NodeInitializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatasetNodeMover implements Control {


    // Fields ===========================
    private int coordPid;
    private static final String PAR_COORDINATES_PROT = "coord_protocol";

    /**
     * Config parameter which gives the prefix of node initializers. An arbitrary
     * number can be specified (Along with their parameters).
     * These will be applied  on the newly created nodes. T
     * Example:
     control.0 DynamicNetwork
     control.0.init.0 RandNI
     control.0.init.0.k 5
     control.0.init.0.protocol somelinkable
     */
    private static final String PAR_INIT = "init";

    private String dataFile;
    private static final String DATA_FILE = "data_file";

    /** node initializers to apply on the newly added nodes */
    private final NodeInitializer[] inits;

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

        // TODO offloads all this to another class extending DynamicNetwork ?
        Object[] tmp = Configuration.getInstanceArray(prefix + "." + PAR_INIT);
        inits = new NodeInitializer[tmp.length];
        for (int i = 0; i < tmp.length; ++i) {
            inits[i] = (NodeInitializer) tmp[i];
        }

    }
    // dataset should be : timestamp;id_node;x;y;id_node;x;y;...


    // This fonction read datafile given in parameter to update nodes position.
    private NodeCoordinates getCoordinatesProtocol(long nodeID) {

        if (! dataIDtoID.containsKey(nodeID) ) {
            // create a new node
            Node newNode = (Node) Network.prototype.clone();

            for (int j = 0; j < inits.length; ++j) {
                inits[j].initialize(newNode);
            }
            Network.add(newNode);

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
                    Network.remove(IDtoIndex.get(fixId));
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