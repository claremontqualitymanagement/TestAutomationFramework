package se.claremont.autotest.dataformats.table;

import se.claremont.autotest.support.SupportMethods;

/**
 * Created by jordam on 2016-11-09.
 */
public class TableVerifierLoggingHtmlStyles {

    public static String styles(){
        String htmlStyles = "      table.table.padding                               { background-color: darkgrey; border: 8px solid darkgrey; }" + SupportMethods.LF;
        htmlStyles += "      table.table.data                                  { background-color: white;}" + SupportMethods.LF;
        htmlStyles += "      tr.table.headline                                 { background-color: lightgray; }" + SupportMethods.LF;
        htmlStyles += "      td.table.headline.soughtafter                     { font-weight: bold; color: black; }" + SupportMethods.LF;
        htmlStyles += "      td.table.headline.ignored                         { font-weight: bold; color: darkgrey; }" + SupportMethods.LF;
        htmlStyles += "      tr.table.dataRow.perfectmatch                     { background-color: lightgreen; }" + SupportMethods.LF;
        htmlStyles += "      tr.table.dataRow.imperfectmatch                   { background-color: cornsilk; }" + SupportMethods.LF;
        htmlStyles += "      td.table.datacell.matchedcellonrowbutnotthiscell  { background-color: cornsilk; }" + SupportMethods.LF;
        htmlStyles += "      td.table.datacell.matchedcellsonrowbutnotthiscell { background-color: cornsilk; }" + SupportMethods.LF;
        htmlStyles += "      td.table.datacell.mismatched                      { background-color: salmon; }" + SupportMethods.LF;
        htmlStyles += "      td.table.datacell.matchedcell                     { background-color: lightgreen; font-weight: bold; }" + SupportMethods.LF;
        htmlStyles += "      td.table.datacell.matchedignored                  { background-color: white; color: grey; }" + SupportMethods.LF;
        return htmlStyles;
    }
}
