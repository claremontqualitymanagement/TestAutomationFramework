package se.claremont.autotest.common;

import se.claremont.autotest.support.SupportMethods;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by jordam on 2017-01-05.
 */
public class BaseFolderHtmlIndexFile {

    public BaseFolderHtmlIndexFile(){
        SupportMethods.saveToFile(htmlContent(), TestRun.settings.getValue(Settings.SettingParameters.BASE_LOG_FOLDER) + "index.html" );
    }

    private String folderNamesAsHtmlTable(){
        StringBuilder htmlTableRows = new StringBuilder();
        File file = new File(TestRun.settings.getValue(Settings.SettingParameters.BASE_LOG_FOLDER));
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        String baseFolder = TestRun.settings.getValue(Settings.SettingParameters.BASE_LOG_FOLDER).replace("\\", "/");
        for(String directory : directories) {
            String[] directoryNameParts = directory.split("_");
            String timestamp = "";
            if(directoryNameParts.length > 2){
                timestamp = directoryNameParts[0] + " " + directoryNameParts[1];
            }
            String runName = "";
            for(int i = 2; i < directoryNameParts.length; i++){
                runName += " " + directoryNameParts[i];
            }
            htmlTableRows
                    .append("         <tr class=\"resultdirectory\" onclick=\"openInNewTab('" + TestRun.reportLinkPrefix() + ":///" + baseFolder + directory + "/_summary.html');\">").append(System.lineSeparator())
                    .append("            <td class=\"timestamp\">").append(System.lineSeparator())
                    .append("               ").append(timestamp).append(System.lineSeparator())
                    .append("            </td>").append(System.lineSeparator())
                    .append("            <td class=\"runname\">").append(System.lineSeparator())
                    .append("               ").append(runName).append(System.lineSeparator())
                    .append("            </td>").append(System.lineSeparator())
                    .append("            <td>").append(System.lineSeparator())
                    .append("            </td>").append(System.lineSeparator())
                    .append("         </tr>").append(System.lineSeparator());
        }
        return htmlTableRows.toString();
    }


    private String htmlContent(){
        return "<!DOCTYPE html>" + System.lineSeparator() +
                "<html lang=\"en\">" + System.lineSeparator() +
                "   <head>" + System.lineSeparator() +
                "      <meta charset='utf-8'>" + System.lineSeparator() +
                "       <meta http-equiv=\"Content-Language\" content=\"en\">" + System.lineSeparator() +
                "      <link rel=\"shortcut icon\" href=\"http://46.101.193.212/TAF/images/facicon.png\">" + System.lineSeparator() +
                "      <meta name=\"description\" content=\"Test run results for TAF\"/>" + System.lineSeparator() +
                "      <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>" + System.lineSeparator() +
                "      <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "      <title>TAF Result Viewer</title>" + System.lineSeparator() +
                "      " + System.lineSeparator() +
                "      <style>" + System.lineSeparator() +
                "      " + System.lineSeparator() +
                "         body {" + System.lineSeparator() +
                "            font-family: Helvetica Neue, Helvetica, Arial, sans-serif; " + System.lineSeparator() +
                "            font-size: 14px; " + System.lineSeparator() +
                "            background-color: white; " + System.lineSeparator() +
                "            width:90%; " + System.lineSeparator() +
                "            margin-left: 2%; " + System.lineSeparator() +
                "            margin-top: 1%; " + System.lineSeparator() +
                "            color: rgb(104,102,99); " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         h1, h2 {" + System.lineSeparator() +
                "            margin-top: 20px; " + System.lineSeparator() +
                "            margin-bottom: 10px; " + System.lineSeparator() +
                "            line-height: 1.1; " + System.lineSeparator() +
                "            font-family: inherit; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         h1 {" + System.lineSeparator() +
                "            font-size:24px; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         h1.pagetitle {" + System.lineSeparator() +
                "            color: rgb(119,150,178); " + System.lineSeparator() +
                "            font-size:24px; " + System.lineSeparator() +
                "            font-weight: bold; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "         h2 {" + System.lineSeparator() +
                "            font-size:20px; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         img.toplogo {" + System.lineSeparator() +
                "            max-width: 30%; " + System.lineSeparator() +
                "            max-height: 10%; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         table {" + System.lineSeparator() +
                "            border: 1px solid #DAD8D9; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         table.testruns {" + System.lineSeparator() +
                "            width: 100%; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         th {" + System.lineSeparator() +
                "            color: black;" + System.lineSeparator() +
                "            text-align: left;" + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         td {" + System.lineSeparator() +
                "            text-align: left;" + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         td.timestamp {" + System.lineSeparator() +
                "            color: rgb(104,102,99); " + System.lineSeparator() +
                "            width: 80px; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         td.runname {" + System.lineSeparator() +
                "            color: rgb(104,102,99); " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         td.runstatus.noerrors {" + System.lineSeparator() +
                "            color: #2db92d; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         td.runstatus.newerrors {" + System.lineSeparator() +
                "            color: rgb(242,102,100); " + System.lineSeparator() +
                "            font-weight: bold; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         td.runstatus.someknownerrors {" + System.lineSeparator() +
                "            color: rgb(242,102,100); " + System.lineSeparator() +
                "            font-weight: bold; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         td.runstatus.unknown {" + System.lineSeparator() +
                "            color: rgb(119,150,178);" + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         .copyright {" + System.lineSeparator() +
                "            background-color: white; " + System.lineSeparator() +
                "            color: rgb(119,150,178); " + System.lineSeparator() +
                "            text-align: center; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         table.striped { " + System.lineSeparator() +
                "            background-color:white; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         table.striped tr:nth-child(even) {" + System.lineSeparator() +
                "            background-color: #F2F2F2; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "         pre { " + System.lineSeparator() +
                "            font-family: Consolas, Menlo, Monaco, Lucida Console, Liberation Mono, DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New, monospace, serif;" + System.lineSeparator() +
                "            margin-bottom: 10px;" + System.lineSeparator() +
                "            overflow: auto;" + System.lineSeparator() +
                "            width: auto;" + System.lineSeparator() +
                "            padding: 5px;" + System.lineSeparator() +
                "            background-color: #eee;" + System.lineSeparator() +
                "            width: 70%;" + System.lineSeparator() +
                "            padding-bottom: 20px!ie7;" + System.lineSeparator() +
                "            max - height: 600px;" + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         table.footer {" + System.lineSeparator() +
                "            border: 0px solid white; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         .footer {" + System.lineSeparator() +
                "            border: 0px none; " + System.lineSeparator() +
                "            width: 100%; " + System.lineSeparator() +
                "            color: rgb(119,150,178); " + System.lineSeparator() +
                "            text-align: center; " + System.lineSeparator() +
                "            align: center; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         img.bottomlogo {" + System.lineSeparator() +
                "            width: 20%; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "         td.bottomlogo {" + System.lineSeparator() +
                "            text-align: center; " + System.lineSeparator() +
                "            background-color: white; " + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "        " + System.lineSeparator() +
                "      </style>" + System.lineSeparator() +
                "      " + System.lineSeparator() +
                "      <script>" + System.lineSeparator() +
                "      " + System.lineSeparator() +
                "         function openInNewTab(url) {" + System.lineSeparator() +
                "            var win = window.open(url, '_blank');" + System.lineSeparator() +
                "            win.focus();" + System.lineSeparator() +
                "         }" + System.lineSeparator() +
                "         " + System.lineSeparator() +
                "      </script>" + System.lineSeparator() +
                "      " + System.lineSeparator() +
                "   </head>" + System.lineSeparator() +
                "   " + System.lineSeparator() +
                "   <body>" + System.lineSeparator() +
                "      <div id=\"head\">" + System.lineSeparator() +
                "      <img src=\"http://46.101.193.212/TAF/images/claremontlogo.gif\" alt=\"logo\" class=\"toplogo\">" + System.lineSeparator() +
                "      <br>" + System.lineSeparator() +
                "      <br>" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "      <h1 class=\"pagetitle\">TAF Result Viewer</h1>" + System.lineSeparator() +
                "      <table class=\"testruns striped\">" + System.lineSeparator() +
                "       <tr data-href=\"C:/Users/jordam/TAF/20170104_164907_sun_reflect_NativeMethodAccessorImpl/wrapUpCurrentTestSetShould.html\">" + System.lineSeparator() +
                "            <th>Time</th>" + System.lineSeparator() +
                "            <th>Run name</th>" + System.lineSeparator() +
                "            <th>Run status</th>" + System.lineSeparator() +
                "         </tr>" + System.lineSeparator() +
                "" + folderNamesAsHtmlTable() +
                "      </table>" + System.lineSeparator() +
                "      " + System.lineSeparator() +
                "      <br>" + System.lineSeparator() +
                "      <br>" + System.lineSeparator() +
                "      <table class=\"footer\" width=\"100%\">" + System.lineSeparator() +
                "            <tr>" + System.lineSeparator() +
                "            <td class=\"bottomlogo\" width=\"100%\">" + System.lineSeparator() +
                "               <a href=\"http://www.claremont.se\" target=\"_blank\">" + System.lineSeparator() +
                "                  <img alt=\"Claremont logo\" class=\"bottomlogo\" src=\"http://46.101.193.212/TAF/images/claremontlogo.gif\">" + System.lineSeparator() +
                "               </a>" + System.lineSeparator() +
                "            </td>" + System.lineSeparator() +
                "            </tr>" + System.lineSeparator() +
                "         <tr>" + System.lineSeparator() +
                "            <td width=\"100%\" class=\"copyright\"><br>(c) Claremont 2017</td>" + System.lineSeparator() +
                "            </tr>" + System.lineSeparator() +
                "     </table>" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "   </body>" + System.lineSeparator() +
                "</html>";
    }
}
