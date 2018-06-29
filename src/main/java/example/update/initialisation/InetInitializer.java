/*
 * Copyright (c) 2003-2005 The BISON Project
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package example.update.initialisation;

import example.update.constraints.NodeCoordinates;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

import java.util.List;

import example.update.EasyCSV;

/**
 * <p>
 * This initialization class collects the simulation parameters from the config
 * file and generates uniformly random 2D-coordinates for each node. The
 * coordinates are distributed on a unit (1.0) square.
 * </p>
 * <p>
 * The first node in the {@link Network} is considered as the root node and its
 * coordinate is set to the center of the square.
 * </p>
 *
 *
 * @author Gian Paolo Jesi
 */
public class InetInitializer implements Control {
    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------
    /**
     * The protocol to operate on.
     *
     * @config
     */
    private static final String PAR_PROT = "protocol";

    // get max node speed from config file
    private static final String MAX_NODE_SPEED = "max_node_speed";

    // get min node speed from config file. Under this value node won't move
    private static final String MIN_NODE_SPEED = "min_node_speed";

    // get filename of nodes location
    private static final String NODES_LOCATION_FILENAME_PARAM = "nodes_location_file";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
    private final int pid;
    private final int maxSpeed;
    private final int minSpeed;

    private final String nodeLocationFile;

    private List<String[]> positions;

    // cache a list with real nodes IDs
    int[] cache = new int[Network.size()]  ;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    /**
     * Standard constructor that reads the configuration parameters. Invoked by
     * the simulation engine.
     *
     * @param prefix
     *            the configuration prefix for this class.
     */
    public InetInitializer(String prefix) {

        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        maxSpeed = Configuration.getInt(prefix + "." + MAX_NODE_SPEED);
        minSpeed = Configuration.getInt(prefix + "." + MIN_NODE_SPEED);

        nodeLocationFile = Configuration.getString(prefix + "." + NODES_LOCATION_FILENAME_PARAM, null);

        if (nodeLocationFile != null ) {
            EasyCSV parser = new EasyCSV(nodeLocationFile);
            positions = parser.content;
        }
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------

    private void setupNode(int nodeIndex, int xCoord, int yCoord, int speed, int angle){
        Node n = Network.get(nodeIndex);
        NodeCoordinates protocol = (NodeCoordinates) n.getProtocol(pid);

        // Set coordinates x,y
        protocol.setX(xCoord);
        protocol.setY(yCoord);

        // Set angle and speed
        protocol.setAngle(angle);
        if (speed >= minSpeed) {
            protocol.setSpeed(speed);
        }
        else {
            protocol.setSpeed(0);
        }
    }

    private void randomSetup(int index){
        for (int i = index; i < Network.size(); i++){
        int x = CommonState.r.nextInt(1000);
        int y = CommonState.r.nextInt(1000);
        int speed = CommonState.r.nextInt(maxSpeed);
        int angle = CommonState.r.nextInt(360);

        this.setupNode(cache[i], x, y, speed,angle);
        }
    }


    /**
     * Initialize the node coordinates. The first node in the {@link Network} is
     * the root node by default and it is located in the middle (the center of
     * the square) of the surface area.
     */
    public boolean execute() {


        for (int i=0; i<Network.size(); i++){
            cache[(int) Network.get(i).getID()] = i;
        }

        if (nodeLocationFile != null) {
                int i = 0;
                while (i < Network.size() && i < positions.size() ) {

                    if (positions.get(i).length > 0) {
                        setupNode(cache[i], Integer.parseInt(positions.get(i)[0]), Integer.parseInt(positions.get(i)[1]), 0, 0);
                    }
                    i++;
                }

                // test if the file had enough data. if not, complete with random data
                if (i < Network.size() - 1) {
                    System.err.println(nodeLocationFile + " doesn't contain enough data for "
                            + Network.size() + " nodes. Completing with random data" );
                    this.randomSetup(i);
                } else if (i <= Network.size() - 1) {
                    System.err.println(nodeLocationFile + " contain too much data for " + Network.size() + " nodes. Ignoring the rest of the file.");
                }

            return false;
        }



        System.err.println("No node location file provided, going random");
        this.randomSetup(0);
        return false;
    }

}
