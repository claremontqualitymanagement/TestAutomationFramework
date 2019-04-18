package se.claremont.taf.core.reporting.testrunreports;

import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Attempts to create a rudimentary HTML file listing all found test runs - to access test run HTML files.
 *
 * Created by jordam on 2017-01-05.
 */
@SuppressWarnings("WeakerAccess")
public class BaseFolderHtmlIndexFile {
    final SimpleDateFormat directoryPartTimeFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
    final SimpleDateFormat outputTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public BaseFolderHtmlIndexFile(){
        SupportMethods.saveToFile(htmlContent(), TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER) + "index.html" );
    }

    private String folderNamesAsHtmlTable(){
        StringBuilder htmlTableRows = new StringBuilder();
        File file = new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER));
        @SuppressWarnings("Convert2Lambda") String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        if(directories == null) return null;
        Arrays.sort(directories, Collections.reverseOrder());
        String baseFolder = TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER).replace("\\", "/");
        for(String directory : directories) {
            String[] directoryNameParts = directory.split("_");
            Date timestamp = null;
            if(directoryNameParts.length > 2){
                try {
                    timestamp = directoryPartTimeFormat.parse(directoryNameParts[0] + " " + directoryNameParts[1]);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
            StringBuilder runName = new StringBuilder();
            for(int i = 2; i < directoryNameParts.length; i++){
                runName.append(" ").append(directoryNameParts[i]);
            }
            htmlTableRows.append("         <tr class=\"resultdirectory\" onclick=\"openInNewTab('").append(TestRun.reportLinkPrefix()).append(":///").append(baseFolder).append(directory).append("/_summary.html');\">").append(System.lineSeparator())
                    .append("            <td class=\"timestamp\">").append(System.lineSeparator())
                    .append("               ").append(outputTimeFormat.format(timestamp)).append(System.lineSeparator())
                    .append("            </td>").append(System.lineSeparator())
                    .append("            <td class=\"runname\">").append(System.lineSeparator())
                    .append("               ").append(runName).append(System.lineSeparator())
                    .append("            </td>").append(System.lineSeparator())
                    .append("         </tr>").append(System.lineSeparator());
        }
        return htmlTableRows.toString();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private String htmlContent(){
        return "<!DOCTYPE html>" + System.lineSeparator() +
                "<html lang=\"en\">" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "   <head>" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "      <meta charset='utf-8'>" + System.lineSeparator() +
                "       <meta http-equiv=\"Content-Language\" content=\"en\">" + System.lineSeparator() +
                "      <link rel=\"shortcut icon\" href=\"http://46.101.193.212/TAF/images/facicon.png\">" + System.lineSeparator() +
                "      <meta name=\"description\" content=\"Test run results for TAF\"/>" + System.lineSeparator() +
                "      <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>" + System.lineSeparator() +
                "      <meta http-equiv=\"refresh\" content=\"15\" />" + System.lineSeparator() +
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
                //"            width: 80px; " + System.lineSeparator() +
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
                "" + System.lineSeparator() +
                "      <div id=\"head\">" + System.lineSeparator() +
                "         <img class=\"toplogo\" src=\"" + TestRun.getSettingsValue(Settings.SettingParameters.PATH_TO_LOGO) + "\">" + System.lineSeparator() +
                "         <br>" + System.lineSeparator() +
                "         <br>" + System.lineSeparator() +
                "         <h1 class=\"pagetitle\">TAF Result Viewer</h1>" + System.lineSeparator() +
                "         <p class=\"updatetime\">Last updated " + outputTimeFormat.format(new Date()) + "</p>" + System.lineSeparator() +
                "      </div>" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "      <table class=\"testruns striped\">" + System.lineSeparator() +
                "         <tr data-href=\"C:/Users/jordam/TAF/20170104_164907_sun_reflect_NativeMethodAccessorImpl/wrapUpCurrentTestSetShould.html\">" + System.lineSeparator() +
                "            <th>Time</th>" + System.lineSeparator() +
                "            <th>Run name</th>" + System.lineSeparator() +
                "         </tr>" + System.lineSeparator() +
                "" + folderNamesAsHtmlTable() +
                "      </table>" + System.lineSeparator() +
                "      " + System.lineSeparator() +
                "      <br>" + System.lineSeparator() +
                "      <br>" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "      <table class=\"footer\" width=\"100%\">" + System.lineSeparator() +
                "         <tr>" + System.lineSeparator() +
                "            <td class=\"bottomlogo\" width=\"100%\">" + System.lineSeparator() +
                "               <a href=\"http://www.claremont.se\" target=\"_blank\">" + System.lineSeparator() +
                "                  <img alt=\"Claremont logo\" class=\"bottomlogo\" src=\"http://46.101.193.212/TAF/images/claremontlogo.gif\">" + System.lineSeparator() +
                "               </a>" + System.lineSeparator() +
                "            </td>" + System.lineSeparator() +
                "         </tr>" + System.lineSeparator() +
                "         <tr>" + System.lineSeparator() +
                "            <td width=\"100%\" class=\"copyright\"><br>(c) Claremont 2017</td>" + System.lineSeparator() +
                "         </tr>" + System.lineSeparator() +
                "      </table>" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "   </body>" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "</html>" +
                "" + System.lineSeparator();
    }
}
