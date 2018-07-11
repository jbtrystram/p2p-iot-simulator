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

package example.update.observation;

import example.update.constraints.NodeCoordinates;
import peersim.config.Configuration;
import peersim.core.Node;
import peersim.core.*;

import peersim.graph.Graph;
/**
 * This class prints to files the topology wiring using a Matplotlib friendly
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

    /**
     * Output logfile name base.
     *
     * @config
     */
    private static final String PAR_FILENAME_BASE = "file_base";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * Coordinate protocol identifier. Obtained from config property
     * {@link #PAR_COORDINATES_PROT}.
     */
    private final int coordPid;

    /* logfile to print data. Name obrtained from config
     * {@link #PAR_FILENAME_BASE}.
     */
    private final String graph_filename;

    Writer output;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    /**
     * Standard constructor that reads the configuration parameters. Invoked by
     * the simulation engine.
     *
     * @param prefix the configuration prefix for this class.
     */
    public InetObserver(String prefix) {
        coordPid = Configuration.getPid(prefix + "." + PAR_COORDINATES_PROT);
        graph_filename = "raw_dat/" + Configuration.getString(prefix + "."
                + PAR_FILENAME_BASE, "graph_dump");

        // initialize writer
        output = new Writer(graph_filename);
    }


    // Control interface method.
    public boolean execute() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < Network.size(); i++) {

            Node current = Network.get(i);

            int x = ((NodeCoordinates) current
                    .getProtocol(coordPid)).getX();
            int y = ((NodeCoordinates) current
                    .getProtocol(coordPid)).getY();

            str.append(current.getID() + ";" + x + ";" + y + System.lineSeparator() );
        }
        output.write(str.toString());
        return false;
    }
}