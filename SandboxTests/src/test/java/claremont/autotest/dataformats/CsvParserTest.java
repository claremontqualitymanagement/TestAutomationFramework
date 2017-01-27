package claremont.autotest.dataformats;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.dataformats.CsvParser;
import se.claremont.autotest.dataformats.Table;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

public class CsvParserTest {
    private CsvParser csvParser;

    @Before
    public void setup() {
        csvParser = null;

    }

    @Test
    @Ignore
    public void test_FileReaderConstructor() throws FileNotFoundException {
        URL resource = CsvParserTest.class.getClassLoader().getResource("src/resources/data.csv");
        FileReader fileReader = new FileReader(resource.getFile());
        csvParser = new CsvParser(fileReader);

        Table table = csvParser.csvContent;
        Assert.assertNotNull(table);
        String htmlTable = table.toHtmlTable();

        Assert.assertNotNull(htmlTable);
        Assert.assertFalse(htmlTable.isEmpty());
    }

    @Test
    @Ignore
    public void test_ContentConstructor() throws FileNotFoundException {
        URL resource = CsvParserTest.class.getClassLoader().getResource("src/resources/data.csv");
        FileReader fileReader = new FileReader(resource.getFile());

        String fileContent = CsvParser.csvFileContent(fileReader);

        csvParser = new CsvParser(fileContent);

        Table table = csvParser.csvContent;
        Assert.assertNotNull(table);
        String htmlTable = table.toHtmlTable();

        Assert.assertNotNull(htmlTable);
        Assert.assertFalse(htmlTable.isEmpty());
    }

    @Test
    @Ignore
    public void test_ContentWithDelimiterConstructor() throws FileNotFoundException {
        URL resource = CsvParserTest.class.getClassLoader().getResource("src/resources/data.csv");
        FileReader fileReader = new FileReader(resource.getFile());

        String fileContent = CsvParser.csvFileContent(fileReader);

        csvParser = new CsvParser(fileContent, ",");

        Table table = csvParser.csvContent;
        Assert.assertNotNull(table);
        String htmlTable = table.toHtmlTable();

        Assert.assertNotNull(htmlTable);
        Assert.assertFalse(htmlTable.isEmpty());
    }

}
