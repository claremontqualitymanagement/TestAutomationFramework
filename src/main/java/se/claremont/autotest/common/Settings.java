package se.claremont.autotest.common;

import se.claremont.autotest.support.SupportMethods;

import java.util.ArrayList;

/**
 * Relevant test execution parameters. Usually used in a TestRun context.
 * Created by jordam on 2016-08-17.
 */
public class Settings {
    private final ArrayList<ValuePair> parameters = new ArrayList<>();

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
        setValueForProperty("baseLogFolder", "C:\\Temp\\");
        setValueForProperty("pathToLogo", "https://www.prv.se/globalassets/in-swedish/prv_logox2.png");
        setValueForProperty("testRunLogFolder", "");
        setValueForProperty("chromeDriverPathToExe", "C:\\Temp\\chromedriver.exe");
        setValueForProperty("firefoxPathToBrowserExe", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
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
                valuePair.value = propertyValue;
                return;
            }
        }
        parameters.add(new ValuePair(propertyName, propertyValue));
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

    public @Override String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(ValuePair valuePair : parameters){
            stringBuilder.append(valuePair.parameter).append("=").append(valuePair.value).append(SupportMethods.LF);
        }
        return stringBuilder.toString();
    }

}
