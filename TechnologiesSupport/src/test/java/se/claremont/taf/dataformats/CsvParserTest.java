package se.claremont.taf.dataformats;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.claremont.taf.support.tableverification.TableData;
import se.claremont.taf.testset.UnitTestClass;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

public class CsvParserTest extends UnitTestClass{
    private CsvParser csvParser;
    private FileReader fileReader;

    @Before
    public void setup() throws FileNotFoundException {
        URL resource = CsvParserTest.class.getClassLoader().getResource("data.csv");
        assertNotNull(resource);
        fileReader = new FileReader(resource.getPath());
        csvParser = new CsvParser(fileReader);
    }

    @Test
    public void test_FileReaderConstructor() throws FileNotFoundException {
        TableData table = csvParser.csvContent;
        assertNotNull(table);
        String htmlTable = table.toHtml();

        assertNotNull(htmlTable);
        Assert.assertFalse(htmlTable.isEmpty());
    }

    @Test
    public void test_ContentConstructor() throws FileNotFoundException {
        TableData table = csvParser.csvContent;
        assertNotNull(table);
        String htmlTable = table.toHtml();

        assertNotNull(htmlTable);
        Assert.assertFalse(htmlTable.isEmpty());
    }

    @Test
    public void test_ContentWithDelimiterConstructor() throws FileNotFoundException {
        URL resource = CsvParserTest.class.getClassLoader().getResource("data.csv");
        assertNotNull(resource);
        fileReader = new FileReader(resource.getPath());

        String fileContent = CsvParser.csvFileContent(fileReader);

        assertNotNull("File content should not be null", fileContent);

        csvParser = new CsvParser(fileContent, ",");

        TableData table = csvParser.csvContent;
        assertNotNull(table);
        String htmlTable = table.toHtml();

        assertNotNull(htmlTable);
        Assert.assertFalse(htmlTable.isEmpty());
    }

}
