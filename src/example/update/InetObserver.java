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

package example.update;

import peersim.config.Configuration;
import peersim.core.Node;
import peersim.core.*;

import peersim.graph.Graph;

/**
 * This class prints to files the topology wiring using a Gnuplot friendly
 * syntax. Uses the {@link Graph} interface to visit the topology.
 *
 * @author Gian Paolo Jesi
 */
public class InetObserver implements Control {
    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The coordinate protocol to look at.
     *
     * @config
     */
    private static final String PAR_COORDINATES_PROT = "coord_protocol";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * Coordinate protocol identifier. Obtained from config property
     * {@link #PAR_COORDINATES_PROT}.
     */
    private final int coordPid;

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
    public InetObserver(String prefix) {
         coordPid = Configuration.getPid(prefix + "." + PAR_COORDINATES_PROT);
    }

    // Control interface method.
    public boolean execute() {
        for (int i = 1; i < Network.size(); i++) {

            Node current = (Node) Network.get(i);

            int x = ((NodeCoordinates) current
                    .getProtocol(coordPid)).getX();
            int y = ((NodeCoordinates) current
                    .getProtocol(coordPid)).getY();
             System.out.println("Node "+i+"; "+x+":"+y );
            /*
            for (int index : g.getNeighbours(i)) {
                Node n = (Node) g.getNode(index);
                double x_from = ((InetCoordinates) n
                        .getProtocol(coordPid)).getX();
                double y_from = ((InetCoordinates) n
                        .getProtocol(coordPid)).getY();
                ps.println(x_from + " " + y_from);
                ps.println(x_to + " " + y_to);
                ps.println();
            } */
        }

        return false;
    }
}
