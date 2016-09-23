package se.claremont.autotest.common;

import se.claremont.autotest.support.SupportMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Class to manage the Settings file (properties file)
 *
 * Created by jordam on 2016-09-13.
 */
class SettingsFile {
    private final Settings settings;
    private final String filePath;

    public SettingsFile(Settings settings, String filePath){
        this.settings = settings;
        this.filePath = filePath;
    }

    void writeToFile(){
        SupportMethods.saveToFile(settings.toString(), filePath);
    }

    void readFromFile(){
        loadAllPropertiesFromFile(filePath);
    }

    private void loadAllPropertiesFromFile(String filePath){
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = getClass().getResourceAsStream(filePath);
            if(input == null){
                System.out.println("No input for '" + filePath + "'");
            } else {
                prop.load(input);
            }
        } catch (Exception e) {
            System.out.println("Could not load properties from file '" + filePath + "'.");
        }
        //java -jar app.jar -Dapp.properties="/path/to/custom/app.properties"
        String externalFileName = System.getProperty("app.properties");
        InputStream fin = null;
        try {
            fin = new FileInputStream(new File(externalFileName));
        } catch (Exception e) {
            System.out.println("Could not find external properties file '" + externalFileName + "'.");
        }
        try {
            prop.load(fin);
        } catch (Exception e) {
            System.out.println("Could not load properties from external properties file '" + externalFileName + "'.");
        }

        try {
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                settings.setValueForProperty(key, value);
                System.out.println("Key : '" + key + "', Value : '" + value + "'.");
            }}catch (Exception ignored) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
