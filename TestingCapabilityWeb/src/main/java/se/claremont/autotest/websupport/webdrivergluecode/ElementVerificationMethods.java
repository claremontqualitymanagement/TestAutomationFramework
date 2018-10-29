package se.claremont.autotest.websupport.webdrivergluecode;

import org.openqa.selenium.WebElement;
import se.claremont.autotest.common.StringComparisonType;
import se.claremont.autotest.common.guidriverpluginstructure.GuiElement;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.tableverification.CellMatchingType;
import se.claremont.autotest.common.support.tableverification.TableData;
import se.claremont.autotest.websupport.DomElement;

import java.awt.image.BufferedImage;
import java.util.List;

public class ElementVerificationMethods extends BrowserVerificationMethods{

    DomElement domElement;
    WebInteractionMethods web;

    public ElementVerificationMethods(GuiElement domElement, WebInteractionMethods web){
        super(web);
        this.domElement = (DomElement)domElement;
        this.web = web;
        wasSuccess = null;
        noFailsInBuilderChain = true;
    }

    ElementVerificationMethods(GuiElement guiElement, WebInteractionMethods web, boolean onlySuccessesSoFar){
        super(web);
        this.domElement = (DomElement)guiElement;
        this.web = web;
        wasSuccess = null;
        if(!onlySuccessesSoFar) noFailsInBuilderChain = false;
    }

    public ElementVerificationMethods textEquals(String expectedString){
        return text(expectedString, StringComparisonType.Exact);
    }

    public ElementVerificationMethods textEqualsIgnoreCase(String expectedString){
        return text(expectedString, StringComparisonType.ExactIgnoreCase);
    }

    public ElementVerificationMethods textContains(String expectedString){
        return text(expectedString, StringComparisonType.Contains);
    }

    public ElementVerificationMethods textContainsIgnoreCase(String expectedString){
        return text(expectedString, StringComparisonType.ContainsIgnoreCase);
    }

    public ElementVerificationMethods textMatchesRegex(String pattern){
        return text(pattern, StringComparisonType.Regex);
    }

    private String getText(){
        WebElement element = web.getRuntimeElementWithoutLogging(domElement);
        if(element == null) return null;
        String text = element.getText();
        if(text == null)
            text = element.getAttribute("value");
        if(text == null)
            text = element.getAttribute("data");
        if(text == null)
            text = element.getAttribute("option");
        if(text == null)
            text = element.getAttribute("text");
        return text;
    }

    private ElementVerificationMethods text(String expectedPattern, StringComparisonType stringComparisonType){
        boolean isMatch = stringComparisonType.match(getText(), expectedPattern);
        long startTime = System.currentTimeMillis();
        while (!isMatch && ((System.currentTimeMillis() - startTime) < web.getStandardTimeout() * 1000)){
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {}
            isMatch = stringComparisonType.match(getText(), expectedPattern);
        }
        if(isMatch){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Text for element '" + domElement.LogIdentification() + "' matched '" + expectedPattern + "'.");
            wasSuccess = true;
        } else {
            wasSuccess = false;
            noFailsInBuilderChain = false;
            if(web.exists(domElement)){
                testCase.log(LogLevel.VERIFICATION_FAILED, "Text for element '" + domElement.LogIdentification() + "' was '" + web.getText(domElement) + "' which did not match the expected pattern '" + expectedPattern + "'.");
            } else {
                testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not match text of element '" + domElement.LogIdentification() + "' since it could not be identified.");
            }
            web.saveScreenshot(null);
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
        }
        return this;
    }

    public ElementVerificationMethods isEnabled(){
        boolean success = enabled();
        long startTime = System.currentTimeMillis();
        while (!success && (System.currentTimeMillis() - startTime) < web.getStandardTimeout() * 1000){
            try{
                Thread.sleep(50);
            } catch (Exception ignored){}
            success = enabled();
        }
        if(success){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Element '" + domElement.LogIdentification() + "' was enabled, as expected.");
            wasSuccess = true;
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Element '" + domElement.LogIdentification() + "' was expected to be enabled but never became enabled within the timeout.");
            web.saveScreenshot(web.getRuntimeElementWithoutLogging(domElement));
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
            wasSuccess = false;
            noFailsInBuilderChain = false;
        }
        return this;
    }

    private boolean enabled(){
        WebElement element = web.getRuntimeElementWithLogging(domElement);
        return (element != null && element.isEnabled());
    }

    public ElementVerificationMethods exists(){
        boolean success = web.getRuntimeElementWithoutLogging(domElement) != null;
        long startTime = System.currentTimeMillis();
        while (!success && (System.currentTimeMillis() - startTime) < web.getStandardTimeout() * 1000){
            try{
                Thread.sleep(50);
            }catch (Exception ignored){}
            success = web.getRuntimeElementWithoutLogging(domElement) != null;
        }
        if(success){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Element '" + domElement.LogIdentification() + "' existed, as expected.");
            wasSuccess = true;
        } else {
            wasSuccess = false;
            noFailsInBuilderChain = false;
            testCase.log(LogLevel.VERIFICATION_FAILED, "Element '" + domElement.LogIdentification() + "' did not exist, but was expected to exist.");
            web.saveScreenshot(null);
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
        }
        return this;
    }

    public ElementVerificationMethods doesNotExist(){
        boolean success = web.getRuntimeElementWithoutLogging(domElement) == null;
        long startTime = System.currentTimeMillis();
        while (!success && (System.currentTimeMillis() - startTime) < web.getStandardTimeout() * 1000){
            try{
                Thread.sleep(50);
            }catch (Exception ignored){}
            success = web.getRuntimeElementWithoutLogging(domElement) == null;
        }
        if(success){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Element '" + domElement.LogIdentification() + "' did not exist, as expected.");
            wasSuccess = true;
        } else {
            wasSuccess = false;
            noFailsInBuilderChain = false;
            testCase.log(LogLevel.VERIFICATION_FAILED, "Element '" + domElement.LogIdentification() + "' existed, but was expected not to.");
            web.saveScreenshot(web.getRuntimeElementWithoutLogging(domElement));
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
        }
        return this;
    }

    public ElementVerificationMethods isDisabled(){
        boolean success = !enabled();
        long startTime = System.currentTimeMillis();
        while (!success && (System.currentTimeMillis() - startTime) < web.getStandardTimeout() * 1000){
            try{
                Thread.sleep(50);
            } catch (Exception ignored){}
            success = !enabled();
        }
        if(success){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Element '" + domElement.LogIdentification() + "' was disabled, as expected.");
            wasSuccess = true;
        } else {
            wasSuccess = false;
            noFailsInBuilderChain = false;
            testCase.log(LogLevel.VERIFICATION_FAILED, "Element '" + domElement.LogIdentification() + "' was expected to be disabled but never became disabled within the timeout.");
            web.saveScreenshot(web.getRuntimeElementWithoutLogging(domElement));
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
        }
        return this;
    }

    public ElementVerificationMethods isDisplayed(){
        boolean success = displayed();
        long startTime = System.currentTimeMillis();
        while (!success && (System.currentTimeMillis() - startTime) < web.getStandardTimeout() * 1000){
            try{
                Thread.sleep(50);
            }catch (Exception ignored){}
            success = displayed();
        }
        if(success){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Element '" + domElement.LogIdentification() + "' was displayed, as expected.");
            wasSuccess = true;
        } else {
            wasSuccess = false;
            noFailsInBuilderChain = false;
            testCase.log(LogLevel.VERIFICATION_FAILED, "Element '" + domElement.LogIdentification() + "' was not displayed, but was expected to be.");
            web.saveScreenshot(web.getRuntimeElementWithoutLogging(domElement));
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
        }
        return this;
    }

    public ElementVerificationMethods isNotDisplayed(){
        boolean success = !displayed();
        long startTime = System.currentTimeMillis();
        while (!success && (System.currentTimeMillis() - startTime) < web.getStandardTimeout() * 1000){
            try{
                Thread.sleep(50);
            }catch (Exception ignored){}
            success = !displayed();
        }
        if(success){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Element '" + domElement.LogIdentification() + "' was displayed, as expected.");
            wasSuccess = true;
        } else {
            wasSuccess = false;
            noFailsInBuilderChain = false;
            testCase.log(LogLevel.VERIFICATION_FAILED, "Element '" + domElement.LogIdentification() + "' was not displayed, but was expected to be.");
            web.saveScreenshot(web.getRuntimeElementWithoutLogging(domElement));
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
        }
        return this;
    }

    private boolean displayed(){
        WebElement element = web.getRuntimeElementWithoutLogging(domElement);
        if(element == null) return false;
        return element.isDisplayed();
    }

    /**
     * Verifies if html table data holds expected data. Top row expected to hold headlines.
     *
     * @param headlineColonValueSemicolonSeparatedString The data to find, in the pattern example of 'Headline1:ExpectedCorrespondingCellValue1;Headline2:ExpectedCorrespondingCellValue2'. If all values can be matched on the same row the test is passed.
     * @param cellMatchingType Type of matching performed.
     */
    public ElementVerificationMethods tableRows(String[] headlineColonValueSemicolonSeparatedString, CellMatchingType cellMatchingType){
        boolean doneOk = false;
        long startTime = System.currentTimeMillis();
        while (!doneOk && System.currentTimeMillis() - startTime <= web.getStandardTimeout() * 1000){
            TableData tableData = web.tableDataFromGuiElement(domElement, false);
            if(tableData == null ){
                DomElement table = domElement;
                testCase.log(LogLevel.VERIFICATION_PROBLEM, "Table data for " + table.LogIdentification() + " is null.");
                web.saveScreenshot(web.getRuntimeElementWithoutLogging(table));
                web.saveDesktopScreenshot();
                web.saveHtmlContentOfCurrentPage();
                web.writeRunningProcessListDeviationsSinceTestCaseStart();
                wasSuccess = false;
                noFailsInBuilderChain = false;
                return this;
            }
            boolean nonErroneous = true;
            for(String searchPattern : headlineColonValueSemicolonSeparatedString){
                if(!tableData.rowExist(searchPattern, cellMatchingType)){
                    nonErroneous = false;
                }
            }
            if(nonErroneous) doneOk = true;
        }
        TableData tableData = web.tableDataFromGuiElement(domElement, true);
        if(tableData == null) return this;
        wasSuccess = tableData.verifyRows(headlineColonValueSemicolonSeparatedString, cellMatchingType);
        if(!wasSuccess) noFailsInBuilderChain = false;
        return this;
    }



    private String getAttributeValue(String attributeName){
        WebElement element = web.getRuntimeElementWithoutLogging(domElement);
        if(element == null) return null;
        return element.getAttribute(attributeName);
    }

    public ElementVerificationMethods attributeValue(String attributeName, String attributeValuePattern, StringComparisonType stringComparisonType){
        boolean success = stringComparisonType.match(getAttributeValue(attributeName), attributeValuePattern);
        long startTime = System.currentTimeMillis();
        while (!success && (System.currentTimeMillis() - startTime) < web.getStandardTimeout() * 1000){
            try{
                Thread.sleep(50);
            }catch (Exception ignored){}
            success = stringComparisonType.match(getAttributeValue(attributeName), attributeValuePattern);
        }
        if(success){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Value for attribute '" + attributeName + "' for element '" + domElement.LogIdentification() + "' was '" + getAttributeValue(attributeName) + ", successfully matching '" + attributeValuePattern + "'.");
            wasSuccess = true;
        } else {
            wasSuccess = false;
            noFailsInBuilderChain = false;
            if(web.exists(domElement)){
                if(getAttributeValue(attributeName) == null){
                    testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not find any value for attribute '" + attributeName + "' for element '" + domElement.LogIdentification() + "'.");
                } else {
                    testCase.log(LogLevel.VERIFICATION_FAILED, "Value for attribute '" + attributeName + "' was expected to match '" + attributeValuePattern + "', but it was '" + getAttributeValue(attributeName) + "' for element '" + domElement.LogIdentification() + "'.");
                }
            } else {
                testCase.log(LogLevel.VERIFICATION_PROBLEM, "Tried to verify attribute value '" + attributeName + "' for element '" + domElement.LogIdentification() + "', but the element was never identified.");
            }
            web.saveScreenshot(web.getRuntimeElementWithoutLogging(domElement));
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
        }
        return this;
    }

    /**
     * Checks that the expected headlines exist in table
     *
     * @param expectedHeadlines The list of expected headlines
     */
    public ElementVerificationMethods tableHeadlines(List<String> expectedHeadlines){
        boolean found = web.waitForElementToAppear(domElement).wasSuccess;
        if(!found){
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not find " + domElement.LogIdentification() + " to verify headlines '" + String.join("', '", expectedHeadlines) + "' in." );
            return this;
        }
        TableData tableData = web.tableDataFromGuiElement(domElement, false);
        if(tableData == null) {
            testCase.log(LogLevel.FRAMEWORK_ERROR, "Could not construct TableData for HTML table " + domElement.LogIdentification() + " when trying to verify headlines '" + String.join("', '", expectedHeadlines) + "'.");
            web.saveScreenshot(web.getRuntimeElementWithoutLogging(domElement));
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
            wasSuccess = false;
            noFailsInBuilderChain = false;
            return this;
        }
        if(!tableData.verifyHeadingsExist(expectedHeadlines)){
            wasSuccess = false;
            noFailsInBuilderChain = false;
            web.saveScreenshot(web.getRuntimeElementWithoutLogging(domElement));
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
        } else {
            wasSuccess = true;
        }
        return this;
    }

    public ElementVerificationMethods isAnimated() {
        long startTime = System.currentTimeMillis();
        web.waitForElementToAppear(domElement, web.getStandardTimeout());
        BufferedImage bufferedImage1 = web.grabElementImage(domElement);
        BufferedImage bufferedImage2 = web.grabElementImage(domElement);
        boolean elementHasFinishedRendering= !web.bufferedImagesAreEqual(bufferedImage1, bufferedImage2);
        while(!elementHasFinishedRendering && (System.currentTimeMillis() - startTime) < web.getStandardTimeout() * 1000){
            web.wait(50);
            bufferedImage2 = web.grabElementImage(domElement);
            elementHasFinishedRendering = !web.bufferedImagesAreEqual(bufferedImage1, bufferedImage2);
            //Initial change detection to make sure element is fully rendered and animation is started
        }
        web.wait(50);
        bufferedImage1 = web.grabElementImage(domElement);
        boolean animationHasStarted = !web.bufferedImagesAreEqual(bufferedImage1, bufferedImage2);
        while(!animationHasStarted && (System.currentTimeMillis() - startTime) < web.getStandardTimeout() * 1000){
            web.wait(50);
            bufferedImage1 = web.grabElementImage(domElement);
            animationHasStarted = !web.bufferedImagesAreEqual(bufferedImage1, bufferedImage2);
            //Second change detection to make sure element actually changes
        }
        if(animationHasStarted){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Element " + domElement.LogIdentification() + " is detected to be animated.");
            wasSuccess = true;
        } else {
            wasSuccess = false;
            noFailsInBuilderChain = false;
            testCase.log(LogLevel.VERIFICATION_FAILED, "Element " + domElement.LogIdentification() + " could not be detected to be animated within the timeout of " + web.getStandardTimeout() + " seconds.");
            web.saveScreenshot(web.getRuntimeElementWithoutLogging(domElement));
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
        }
        return this;
    }

}
