package example.update;

import com.univocity.parsers.csv.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class EasyCSV {

    public List<String[]> content;

    EasyCSV(String pathToFile){


        //------------- FIELDS
        File file = new File(pathToFile);


        // CSV parser settings (newline character, separator..)
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");

        // creates a CSV parser
        CsvParser parser = new CsvParser(settings);

        // open the file to read
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

            // parses all rows in one go.
            content = parser.parseAll(fis);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

