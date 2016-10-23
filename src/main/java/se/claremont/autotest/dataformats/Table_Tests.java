package se.claremont.autotest.dataformats;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.claremont.autotest.support.SupportMethods;

/**
 * Created by jordam on 2016-10-18.
 */
public class Table_Tests {

    @Test
    public void csvConstructor(){
        Table table = new Table("headline1,headline2,headline3" + SupportMethods.LF + "1,2,3", SupportMethods.LF, ",");
        System.out.println(table.toString());
        Assert.assertTrue("No headline identified despite headline.", table.headLineExist("headline2"));
    }

    @Test
    public void toHtml(){
        Table table = new Table("headline1,headline2,headline3" + SupportMethods.LF + "1,2,3", SupportMethods.LF, ",");
        System.out.println(table.toHtmlTable());
        Assert.assertTrue("No html exist.", table.toHtmlTable().contains("<table>") && table.toHtmlTable().contains("<th>headline3</th>"));
    }

    @Test
    public void getRows(){
        Table table = new Table("headline1,headline2,headline3" + SupportMethods.LF + "1,2,3" + SupportMethods.LF + "4,5,6", SupportMethods.LF, ",");
        System.out.println(table.toHtmlTable());
        System.out.println(table.headlineRow.toString());
        System.out.println(table.toString());
        for(Table.Row row : table.rowsWithMatchingValueForHeadline("headline2", ".*2.*")){
            System.out.println(row.toString());
        }
    }

}
