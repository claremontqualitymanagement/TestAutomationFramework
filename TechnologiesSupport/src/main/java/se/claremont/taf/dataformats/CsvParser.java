package se.claremont.taf.dataformats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.taf.support.SupportMethods;
import se.claremont.taf.support.tableverification.TableData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class for verification and manipulation of CSV files (comma separated files).
 * Constructors attempt to understand if a headline row is present based on analysis of numeric data fields in rows.
 *
 * Created by jordam on 2016-10-18.
 */
@SuppressWarnings("WeakerAccess")
public class CsvParser {
    private final static Logger logger = LoggerFactory.getLogger( CsvParser.class );
    public TableData csvContent;

    public CsvParser(FileReader fileReader){
        this(csvFileContent(fileReader));
    }

    public CsvParser(String csvString){
        csvContent = new TableData(null, "csv data", csvString, SupportMethods.LF, ",");
    }

    @SuppressWarnings("SameParameterValue")
    public CsvParser(String csvString, String dataValueDelimiter){
        csvContent = new TableData(null, "csv data", csvString, SupportMethods.LF, dataValueDelimiter);
    }

    public CsvParser(String csvString, String lineBreak, String dataValueDelimiter){
        csvContent = new TableData(null, "csv content", csvString, lineBreak, dataValueDelimiter);
    }

    public CsvParser(String csvString, String[] headlines, String lineBreak, String dataValueDelimiter){
        csvContent = new TableData(null, "csv data", csvString, headlines, lineBreak, dataValueDelimiter);
    }

    public static String csvFileContent(FileReader fileReader){
        String fileContent = null;
        try(BufferedReader br = new BufferedReader(fileReader)) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            fileContent = sb.toString();
        } catch (FileNotFoundException e) {
            logger.error( "Could not read content of file '" + fileReader.toString() + "'. It does not seem to be found.", e );
        } catch (IOException ioe) {
            logger.error( "Could not get file content from file '" + fileReader.toString() + "'. " + ioe.getMessage(), ioe );
        }
        return fileContent;
    }

}
