package se.claremont.autotest.common;

import se.claremont.autotest.support.SupportMethods;
import se.claremont.tools.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Relevant test execution parameters. Usually used in a TestRun context.
 * Created by jordam on 2016-08-17.
 */
public class Settings {
    private final ArrayList<ValuePair> parameters = new ArrayList<>();
    private final ArrayList<ValuePair> hiddenParameters = new ArrayList<>(); //Not displayed in reports

    public Settings(){
        loadDefaults();
        SettingsFile settingsFile = new SettingsFile(this, getValueForProperty("baseLogFolder") + "runSettings.properties");
        settingsFile.readFromFile();
        settingsFile.writeToFile();
    }

    /**
     * Default values for Settings parameters
     */
    private void loadDefaults(){

        //TODO: We need to figure out were to set root folder TAF, in mac jvm doesn't have sufficent rights!!!
        //macintosh
        if( Utils.getInstance().amIMacOS() ) {
            //setValueForProperty("baseLogFolder", Utils.getInstance().getRootDirectory() + "TAF" + File.separator);
            setValueForProperty("baseLogFolder", Utils.getInstance().getUserWorkingDirectory() + File.separator + "TAF" + File.separator);
            setValueForProperty("pathToLogo", "https://www.prv.se/globalassets/in-swedish/prv_logox2.png");
            setValueForProperty("testRunLogFolder", "");
            setValueForProperty("chromeDriverPathToExe", Utils.getInstance().getUserWorkingDirectory() + File.separator + "drivers" + File.separator + "chromedriver");
            setValueForProperty("firefoxPathToBrowserExe", File.separator + "Applications" + File.separator + "Firefox.app" + File.separator);
        }
        // lets assume jvm is running upon windows os.
        else {
            setValueForProperty("baseLogFolder", System.getProperty("user.home") + File.separator + "TAF" + File.separator);
            setValueForProperty("pathToLogo", "https://www.prv.se/globalassets/in-swedish/prv_logox2.png");
            setValueForProperty("testRunLogFolder", "");
            setValueForProperty("chromeDriverPathToExe", System.getProperty("user.home") + File.separator + "TAF" + File.separator + "chromedriver.exe");
            setValueForProperty("firefoxPathToBrowserExe", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
        }
        setValueForProperty("emailRecipients", "jorgen.damberg@gmail.com");
        setValueForProperty("emailHostServerAddress", "smtp.gmail.com");
        setValueForProperty("emailAccountUserName", "autotestcqm@gmail.com");
        setValueForHiddenProperty("emailAccountPassword", "Claremont16!");
        setValueForProperty("emailHostPort", "587");
    }

    /**
     * Sets a value for a property in settings
     *
     * @param propertyName Name of the property
     * @param propertyValue Value of the property (will be treated as a string)
     */
    public void setValueForProperty(String propertyName, String propertyValue){
        for (ValuePair valuePair : parameters){
            if(valuePair.parameter.toLowerCase().equals(propertyName.toLowerCase())){
                valuePair.value = propertyValue.trim();
                return;
            }
        }
        parameters.add(new ValuePair(propertyName, propertyValue.trim()));
    }

    /**
     * Sets a value for a a hidden property in settings. Not displayed in reports.
     *
     * @param propertyName Name of the property
     * @param propertyValue Value of the property (will be treated as a string)
     */
    public void setValueForHiddenProperty(String propertyName, String propertyValue){
        for (ValuePair valuePair : hiddenParameters){
            if(valuePair.parameter.toLowerCase().equals(propertyName.toLowerCase())){
                valuePair.value = propertyValue.trim();
                return;
            }
        }
        hiddenParameters.add(new ValuePair(propertyName, propertyValue.trim()));
    }

    /**
     * Retrieves the current value for a property
     *
     * @param propertyName The name of the property to retrieve the value for
     * @return Returns the string based value of the property
     */
    public String getValueForProperty(String propertyName){
        for(ValuePair valuePair : parameters){
            if(valuePair.parameter.toLowerCase().equals(propertyName.toLowerCase())) return valuePair.value;
        }
        System.out.println("Sorry. Could not find parameter '" + propertyName + "' among settings parameters.");
        return null;
    }

    /**
     * Retrieves the current value for a property
     *
     * @param propertyName The name of the property to retrieve the value for
     * @return Returns the string based value of the property
     */
    public String getValueForHiddenProperty(String propertyName){
        for(ValuePair valuePair : hiddenParameters){
            if(valuePair.parameter.toLowerCase().equals(propertyName.toLowerCase())) return valuePair.value;
        }
        System.out.println("Sorry. Could not find parameter '" + propertyName + "' among settings parameters.");
        return null;
    }

    public @Override String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(ValuePair valuePair : parameters){
            stringBuilder.append(valuePair.parameter).append("=").append(valuePair.value).append(SupportMethods.LF);
        }
        return stringBuilder.toString();
    }

    String toHtmlTable(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table class=\"settingsTable\">").append(SupportMethods.LF);
        for(ValuePair valuePair : parameters){
            stringBuilder.append("  <tr>").append(valuePair.parameter).append("</td><td>").append(valuePair.value).append("</td></tr>").append(SupportMethods.LF);
        }
        stringBuilder.append("</table>").append(SupportMethods.LF);
        return stringBuilder.toString();
    }

}
