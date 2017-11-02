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
import peersim.util.FileNameGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

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

    /**
     * Utility class to generate incremental indexed filenames from a common
     * base given by {@link #graph_filename}.
     */
    private final FileNameGenerator fng;

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
        graph_filename = Configuration.getString(prefix + "."
                + PAR_FILENAME_BASE, "graph_dump");
        fng = new FileNameGenerator(graph_filename, ".dat");
    }



    // Control interface method.
    public boolean execute() {

        try {
             // initialize output streams
            String fname = fng.nextCounterName();
            FileOutputStream outStream = new FileOutputStream(fname);
            System.out.println("InetObserver : Writing to file " + fname);
            PrintStream pstr = new PrintStream(outStream);

            // dump topology:
            writeGraph(pstr);

            outStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }


    private void writeGraph(PrintStream file) {

        for (int i = 0; i < Network.size(); i++) {

            Node current = Network.get(i);

            int x = ((NodeCoordinates) current
                    .getProtocol(coordPid)).getX();
            int y = ((NodeCoordinates) current
                    .getProtocol(coordPid)).getY();

            file.println(i + ";" + x + ";" + y);
        }
    }
}
