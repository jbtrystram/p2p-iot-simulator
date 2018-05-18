package example.update.observation;


import peersim.util.FileNameGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Writer {

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------


    /**
     * Utility class to generate incremental indexed filenames from a common
     * base given by in constructor.
     */
    private final FileNameGenerator fng;

    PrintStream fileStream;


    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------
    /**
     * Standard constructor that reads the configuration parameters. Invoked by
     * the simulation engine.
     *
     * @param filename
     *            the base filename to be used
     */
    public Writer (String filename) {

        fng = new FileNameGenerator(filename, ".dat");
    }


    public void write(String data) {

        // initialize output streams
        try {
            String fname = fng.nextCounterName();
            FileOutputStream outStream = new FileOutputStream(fname);
            fileStream = new PrintStream(outStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // write into it
        fileStream.print(data);

        // close it
        fileStream.close();
    }
}