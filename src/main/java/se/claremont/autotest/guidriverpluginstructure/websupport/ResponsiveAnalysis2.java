package se.claremont.autotest.guidriverpluginstructure.websupport;

import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Dim;
import org.openqa.selenium.*;
import se.claremont.autotest.common.LogFolder;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.common.TestRun;
import se.claremont.autotest.support.StringManagement;
import se.claremont.autotest.support.SupportMethods;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jordam on 2016-11-06.
 */
public class ResponsiveAnalysis2 {
    WebDriver driver;
    List<CompareElement> compareElements = new ArrayList<>();
    List<Dimension> resolutions = new ArrayList<>();
    List<ElementTreeCollection> elementTreeCollectionList = new ArrayList<>();
    TestCase testCase;

    public ResponsiveAnalysis2(List<Dimension> resolutions, WebDriver driver, TestCase testCase){
        SupportMethods.saveToFile("Just making sure log folder exist.", LogFolder.testRunLogFolder + testCase.testName + "dummy.txt");
        testCase.log(LogLevel.DEBUG, "Initiating responsive analysis for resolutions " + resolutionsToSTring() + " and url '" + driver.getCurrentUrl() + "'.");
        this.testCase = testCase;
        this.resolutions.addAll(resolutions);
        this.driver = driver;
    }

    private String resolutionsToSTring(){
        List<String> returnStrings = new ArrayList<>();
        for(Dimension resolution : resolutions){
            returnStrings.add(resolution.getWidth() + "x" + resolution.getHeight());
        }
        return "'" + String.join("', '", returnStrings) + "'";
    }

    public void performAnalysis(){
        createElementTreeCollections();
        logTreeCollectionsStatistics();
        compareElementTreeCollections();
    }

    private void logTreeCollectionsStatistics(){
        StringBuilder sb = new StringBuilder();
        for(ElementTreeCollection elementTreeCollection : elementTreeCollectionList){
            sb.append(elementTreeCollection.toString()).append(SupportMethods.LF);
        }
        testCase.log(LogLevel.INFO, "Statistics from element gathering:" + SupportMethods.LF + sb.toString());
    }

    private void compareElementTreeCollections(){
        joinElementLists();
        for(int i = 0; i < compareElements.size()-1 ; i++){
            boolean found = false;
            String compareResults;
            for(int j = i+1; j < compareElements.size(); j++){
                compareResults = compareElements.get(i).evaluateAgainst(compareElements.get(j));
                if(compareResults == "") {
                    found = true;
                    break;
                }
                if(compareResults != null) {
                    testCase.log(LogLevel.INFO, StringManagement.htmlContentToDisplayableHtmlCode(compareResults));
                    found = true;
                    continue;
                }
            }
            if(!found){
                testCase.log(LogLevel.INFO, StringManagement.htmlContentToDisplayableHtmlCode(compareElements.get(i).toString()) + " disappeared.");
            }
        }
    }

    private void joinElementLists(){
        for(ElementTreeCollection elementTreeCollection : this.elementTreeCollectionList){
            for(AssessableElement assessableElement : elementTreeCollection.assessableElements){
                compareElements.add(new CompareElement(elementTreeCollection.resolution, assessableElement));
            }
        }
    }

    private void createElementTreeCollections() {
        for(Dimension resolution : resolutions){
            driver.manage().window().setSize(resolution);
            ElementTreeCollection elementTreeCollection = new ElementTreeCollection(resolution, testCase);
            elementTreeCollection.collectVisibleElementsOnCurrentPage(driver);
            elementTreeCollectionList.add(elementTreeCollection);
        }
    }
    private BufferedImage getScreenshot(){
        try {
            testCase.log(LogLevel.DEBUG, "Capturing screenshot of web page.");
            File returnImage = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            return ImageIO.read(returnImage);
        }catch (Exception e2){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not take screenshot of web page. " + e2.getMessage());
            return null;
        }
    }

    private class ElementTreeCollection {
        BufferedImage screenImage;
        Dimension resolution;
        TestCase testCase;
        public List<AssessableElement> assessableElements = new ArrayList<>();

        public ElementTreeCollection(Dimension resolution, TestCase testCase){
            this.testCase = testCase;
            this.resolution = resolution;
        }

        public void collectVisibleElementsOnCurrentPage(WebDriver driver) {
            screenImage = getScreenshot();
            List<WebElement> childElements = recursiveWebElementGathering(driver.findElement(By.xpath("//body")));
            for(WebElement child : childElements){
                try {
                    if(child.isDisplayed()) assessableElements.add(new AssessableElement(child, screenImage, testCase));
                }catch (Exception e){
                    testCase.log(LogLevel.DEBUG, "This did not work out to assessible elements. Not supposed to happen. " + e.getMessage());
                }
            }
        }

        public String toString(){
            return "Resolution: '" + resolution.getWidth() + "x" + resolution.getHeight() + "'. Found elements: " + assessableElements.size() + ".";
        }

        private List<WebElement> recursiveWebElementGathering(WebElement webElement){
            List<WebElement> childElements = new ArrayList<>();
            childElements.add(webElement);
            for(WebElement child : webElement.findElements(By.xpath("*"))){
                try {
                    childElements.addAll(recursiveWebElementGathering(child));
                }catch (Exception e){
                    testCase.log(LogLevel.DEBUG, "This did not work out to web element. Not supposed to happen. " + e.getMessage());
                }
            }
            return childElements;
        }

    }

    private class AssessableElement {
        Dimension size = new Dimension(0,0);
        Point position = new Point(0,0);
        WebElement element = null;
        TestCase testCase;
        BufferedImage elementImage = null;
        String innerHtml = "";

        public AssessableElement(WebElement webElement, BufferedImage screenImage, TestCase testCase){
            this.testCase = testCase;
            try {
                this.size = webElement.getSize();
            }catch (Exception e){
                testCase.log(LogLevel.DEBUG, "Could not get element size. " + e.getMessage());
            }
            try {
                this.position = webElement.getLocation();
            }catch (Exception e){
                testCase.log(LogLevel.DEBUG, "Could not get element location. " + e.getMessage());
            }
            try {
                this.innerHtml = webElement.getAttribute("innerHTML");
            }catch (Exception e){
                testCase.log(LogLevel.DEBUG, "No InnerHtml found. " + e.getMessage());
            }
            this.elementImage = image(screenImage);
        }

        public void log(){
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.INFO, toString(), toHtml());
        }

        public String toString(){
            StringBuilder sb = new StringBuilder("Element: Position = '");
            sb.append(position.getX()).append("x").append(position.getY()).append("', size = '").append(size.getWidth()).append("x").append(size.getHeight()).append("'");
            if(innerHtml != null && innerHtml.length() > 0){
                sb.append(SupportMethods.LF).append("InnerHTML:").append(SupportMethods.LF).append(innerHtml);
            } else {
                sb.append(", empty InnerHtml.");
            }
            return sb.toString();
        }

        public String toHtml(){
            String html = StringManagement.htmlContentToDisplayableHtmlCode(toString());

            if(elementImage != null){
                String fileName = LogFolder.testRunLogFolder + testCase.testName + TestRun.fileCounter + ".png";
                html += "<br><img src=\"" + StringManagement.filePathToHtmlSrc(fileName) + "\">";
                writeImageToFile(fileName);
                TestRun.fileCounter++;
            }
            return html;
        }

        public BufferedImage image(BufferedImage screenImage){
            try {
                return screenImage.getSubimage(this.position.getX(), this.position.getY(), this.size.width, this.size.height);
            }catch (Exception e){
                testCase.log(LogLevel.DEBUG, "Could nog crop browser window to element. " + e.getMessage());
                return null;
            }
        }


        public void writeImageToFile(String filePath){
            if(elementImage == null) return;
            try {
                ImageIO.write(elementImage, "png", new File(filePath));
            } catch (Exception e) {
                testCase.log(LogLevel.DEBUG, "Could not write image to file '" + filePath + "'. " + e.getMessage());
            }
        }

        public boolean isSame(AssessableElement assessableElement){
            if(this.size != assessableElement.size) return false;
            if(this.position != assessableElement.position) return false;
            if(!innerHtml.equals(assessableElement.innerHtml)) return false;
            return true;
        }

        public boolean canBeFoundInCollection(ElementTreeCollection elementTreeCollection){
            for(AssessableElement assessableElement : elementTreeCollection.assessableElements){
                if(this.isSame(assessableElement)) return true;
            }
            return false;
        }

    }

    enum DisplayStatus{
        UNEVALUATED,
        EXACT_MATCH_IN_ALL_RESOLUTIONS,
        DISAPPEARING_OR_APPREARING_IN_AT_LEAST_ONE_RESOLUTION,
        RE_SCALED_IN_ATLEAST_ONE_RESOLUTION
    }

    class CompareElement{
        Dimension resolution;
        AssessableElement assessableElement;
        public DisplayStatus displayStatus = DisplayStatus.UNEVALUATED;

        public CompareElement(Dimension resolution, AssessableElement assessableElement){
            this.resolution = resolution;
            this.assessableElement = assessableElement;
        }

        public String evaluateAgainst(CompareElement compareElement) {
            if(this.assessableElement.isSame(compareElement.assessableElement)){
                displayStatus = DisplayStatus.EXACT_MATCH_IN_ALL_RESOLUTIONS;
                return "";
            } else if(this.assessableElement.innerHtml.equals(compareElement.assessableElement.innerHtml)){
                return this.assessableElement.toString() + " reposition/re-scaled in resolution '" + compareElement.resolution.getWidth() + "x" + compareElement.resolution.getHeight() + "'";
            }
            return null;
        }
    }



}
