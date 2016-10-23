package se.claremont.autotest.dataformats;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.support.SupportMethods;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for verification and manipulation of CSV files (comma separated files).
 * Constructors attempt to understand if a headline row is present based on analysis of numeric data fields in rows.
 *
 * Created by jordam on 2016-10-18.
 */
public class CsvParser {
    public Table csvContent;

    public CsvParser(FileReader fileReader){
        this(csvFileContent(fileReader));
    }

    public CsvParser(String csvString){
        csvContent = new Table(csvString, SupportMethods.LF, ",");
    }

    public CsvParser(String csvString, String dataValueDelimiter){
        csvContent = new Table(csvString, SupportMethods.LF, dataValueDelimiter);
    }

    public CsvParser(String csvString, String lineBreak, String dataValueDelimiter){
        csvContent = new Table(csvString, lineBreak, dataValueDelimiter);
    }

    public CsvParser(String csvString, String[] headlines, String lineBreak, String dataValueDelimiter){
        List<String[]> dataRows = new ArrayList<>();
        for(String row : csvString.split(lineBreak)){
            dataRows.add(row.split(dataValueDelimiter));
        }
        csvContent = new Table(headlines, dataRows);
    }

    private static String csvFileContent(FileReader fileReader){
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
            System.out.println("Could not read content of file '" + fileReader.toString() + "'. It does not seem to be found.");
        } catch (IOException e) {
            System.out.println("Could not get file content from file '" + fileReader.toString() + "'. " + e.getMessage());
        }
        return fileContent;
    }

}
