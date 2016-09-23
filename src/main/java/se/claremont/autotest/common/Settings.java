package se.claremont.autotest.common;

import se.claremont.autotest.support.SupportMethods;

import java.io.File;
import java.util.ArrayList;

/**
 * Relevant test execution parameters. Usually used in a TestRun context.
 * Created by jordam on 2016-08-17.
 */
public class Settings {
    private final ArrayList<ValuePair> parameters = new ArrayList<>();
    private final ArrayList<ValuePair> hiddenParameters = new ArrayList<>();

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
        setValueForProperty("baseLogFolder", System.getProperty("java.io.tmpdir") + "TAF" + File.separator);
        setValueForProperty("pathToLogo", "");
        setValueForProperty("testRunLogFolder", "");
        setValueForProperty("chromeDriverPathToExe", System.getProperty("java.io.tmpdir") + "TAF" + File.separator + "chromedriver.exe");
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
        System.out.println("Sorry. Could not find hidden parameter '" + propertyName + "' among settings hidden parameters.");
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
