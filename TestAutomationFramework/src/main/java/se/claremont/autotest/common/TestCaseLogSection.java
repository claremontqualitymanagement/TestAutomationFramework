package se.claremont.autotest.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static se.claremont.autotest.support.SupportMethods.LF;

/**
 * Created by jordam on 2016-11-01.
 */
public class TestCaseLogSection {
     ArrayList<LogPost> logPostList = new ArrayList<>();
    Date startTime;
    Date stopTime;

     public TestCaseLogSection(ArrayList<LogPost> logPosts, Date testStartTime, Date testStopTime){
         this.startTime = testStartTime;
         this.stopTime = testStopTime;
         this.logPostList.addAll(logPosts);
         //if(logPosts.size() > 0){
         //    this.logPostList.add(new LogPost(LogLevel.INFO, "Time spent in this section compared to whole test<br>" + timeProgressGraph(testStartTime, testStopTime, logPostList.get(0).date, logPostList.get(logPostList.size() - 1).date, 300)));
         //}
    }

    public static String htmlStyleInformation(){
        return  "      table.logsectionlogposts { width: 100%; }" + LF +
                "      h3.logsectiontitle.passed  { color: " + UxColors.GREEN.getHtmlColorCode() + ";  }" + LF +
                "      h3.logsectiontitle.failed  { color: " + UxColors.RED.getHtmlColorCode() + "; font-weight: bold;  }" + LF +
                htmlStyleInformationTimeGraph();
    }

    private static String htmlStyleInformationTimeGraph(){
        return "         table.timegraph.padding                  { background-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; width: 100%; }" + LF +
                "         table.timegraph.datatabell               { font-weight: normal; border-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; width: auto; }" + LF +
                "         tr.timegraph.rubrikrad                   { background-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; border: 1px solid " + UxColors.LIGHT_GREY.getHtmlColorCode() + ";}" + LF +
                "         td.timegraph                             { border: 1px solid " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; }" + LF +
                "         td.before                      { background-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; }" + LF +
                "         td.during                      { background-color: " + UxColors.DARK_BLUE.getHtmlColorCode() + "; }" + LF +
                "         td.after                       { background-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; }" + LF;
    }

    private boolean hasErrors(){
        for(LogPost logPost : logPostList){
            if(logPost.isFail()){
                return true;
            }
        }
        return false;
    }

    private LogLevel highestLogLevel(){
        LogLevel highest = LogLevel.DEBUG;
        for(LogPost logPost : logPostList){
            if(logPost.logLevel.getValue() > highest.getValue()){
                highest = logPost.logLevel;
            }
        }
        return highest;
    }


    public String toHtml(){
        StringBuilder html = new StringBuilder();
        if(logPostList.size() > 0) {
            html.append(timeProgressGraph(startTime, stopTime, logPostList.get(0).date, logPostList.get(logPostList.size() -1).date, 600)).append(LF);
            html.append("        <div class=\"expandable logsection level-").append(highestLogLevel().toString().toLowerCase());
            if(hasErrors()){
                html.append(" initially-expanded");
            }
            html.append("\">").append(LF);
            html.append("           <h3 title=\"Test step in class '").append(logPostList.get(0).testStepClassName).append("'\" class=\"logsectiontitle ");
            if(hasErrors()){
                html.append("failed\">");
            }else {
                html.append("passed\">");
            }
            //html.append("Test step: '<b>" + logPostList.get(0).testStepName + "</b>'   - in class: '" + logPostList.get(0).testStepClassName + "'</h3>").append(LF);
            html.append("Test step: '<b>").append(logPostList.get(0).testStepName.replace("<", "_").replace(">", "_")).append("</b>'</h3>").append(LF);
            html.append("           <div class=\"expandable-content\">").append(LF);
            html.append("              <table class=\"logsectionlogposts\">").append(LF);
            for(LogPost logPost : logPostList){
                html.append(logPost.toHtmlTableRow());
            }
            html.append("              </table>").append(LF);
            html.append("           </div>").append(LF);
            html.append("        </div>").append(LF).append(LF);

        }
        return html.toString();
    }

    @SuppressWarnings("SameParameterValue")
    private String timeProgressGraph(Date wholeTimePeriodStartTime, Date wholeTimePeriodEndTime, Date partialEventStartTime, Date partialEventEndTime, int graphWidth){
        if(logPostList.size() == 0) return null;
        long wholePeriod = wholeTimePeriodEndTime.getTime() - wholeTimePeriodStartTime.getTime();
        if (wholePeriod == 0) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("        <div class=\"timegraphtitle\" title=\"Test step start time: ").append(new SimpleDateFormat("HH:mm:ss").format(partialEventStartTime))
                .append(LF).append("Test step end time: ").append(new SimpleDateFormat("HH:mm:ss").format(partialEventEndTime)).append("\">").append(LF);
        sb.append("        <table class=\"timegraph\" width=\"100%\">").append(LF).append("           <tr>").append(LF);

        if(partialEventStartTime.getTime() - wholeTimePeriodStartTime.getTime() != 0){
            long widthOfInitPartPercent = 100*(partialEventStartTime.getTime() - wholeTimePeriodStartTime.getTime())/wholePeriod;
            sb.append("              <td width=\"").append(widthOfInitPartPercent).append("%\" class=\"before\"><span title=\"Whole time period start time: ").append(wholeTimePeriodStartTime.getTime()).append(LF).append("Part section start time: ").append(partialEventStartTime.getTime()).append("\"></span></td>").append(LF);
        }

        if(partialEventEndTime.getTime()-partialEventStartTime.getTime() != 0){
            long widthOfPartPercent = (100*(partialEventEndTime.getTime() - partialEventStartTime.getTime()))/wholePeriod;
            sb.append("              <td width=\"").append(widthOfPartPercent).append("%\" class=\"during\"><span title=\"Part section start time: ").append(partialEventStartTime.getTime()).append(LF).append("Part section end time: ").append(partialEventEndTime.getTime()).append("\"></span></td>").append(LF);
        }

        if(wholeTimePeriodEndTime.getTime() - partialEventEndTime.getTime() != 0){
            long widthOfEndPartPercent = (100*(wholeTimePeriodEndTime.getTime() - partialEventEndTime.getTime()))/wholePeriod;
            sb.append("              <td width=\"").append(widthOfEndPartPercent).append("%\" class=\"after\"><span title=\"Part section end time: ").append(partialEventEndTime.getTime()).append(LF).append("Whole section end time: ").append(wholeTimePeriodEndTime.getTime()).append("\"></span></td>").append(LF);
        }
        sb.append("           </tr>").append(LF).append("        </table>").append(LF).append("</div>").append(LF);
        return sb.toString();
    }

}


