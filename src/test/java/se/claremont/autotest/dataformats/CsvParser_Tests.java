package se.claremont.autotest.dataformats;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class CsvParser_Tests {
    private CsvParser csvParser;

    @Before
    public void setup() {
        csvParser = null;
    }

    @Test
    public void test_FileReaderConstructor() throws FileNotFoundException {
        URL resource = CsvParser_Tests.class.getClassLoader().getResource("dataformats/csv/data.csv");
        FileReader fileReader = new FileReader(resource.getFile());
        csvParser = new CsvParser(fileReader);

        Table table = csvParser.csvContent;
        assertNotNull(table);
        String htmlTable = table.toHtmlTable();

        assertNotNull(htmlTable);
        assertFalse(htmlTable.isEmpty());
    }

    @Test
    public void test_ContentConstructor() throws FileNotFoundException {
        URL resource = CsvParser_Tests.class.getClassLoader().getResource("dataformats/csv/data.csv");
        FileReader fileReader = new FileReader(resource.getFile());

        String fileContent = CsvParser.csvFileContent(fileReader);

        csvParser = new CsvParser(fileContent);

        Table table = csvParser.csvContent;
        assertNotNull(table);
        String htmlTable = table.toHtmlTable();

        assertNotNull(htmlTable);
        assertFalse(htmlTable.isEmpty());
    }

    @Test
    public void test_ContentWithDelimiterConstructor() throws FileNotFoundException {
        URL resource = CsvParser_Tests.class.getClassLoader().getResource("dataformats/csv/data.csv");
        FileReader fileReader = new FileReader(resource.getFile());

        String fileContent = CsvParser.csvFileContent(fileReader);

        csvParser = new CsvParser(fileContent, ",");

        Table table = csvParser.csvContent;
        assertNotNull(table);
        String htmlTable = table.toHtmlTable();

        assertNotNull(htmlTable);
        assertFalse(htmlTable.isEmpty());
    }

}
