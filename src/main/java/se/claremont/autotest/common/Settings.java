package se.claremont.autotest.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.support.SupportMethods;
import se.claremont.tools.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Relevant test execution parameters. Usually used in a TestRun context.
 * Secret items are not displayed in html report
 *
 * Created by jordam on 2016-08-17.
 */
@SuppressWarnings("SameParameterValue")
public class Settings extends HashMap<String, String>{

    private final static Logger logger = LoggerFactory.getLogger( Settings.class );

    //Some of these setting parameters are suppressed from log display in the summary report, where these settings othervice is displayed.
    public enum SettingParameters{
        EMAIL_SENDER_ADDRESS         (),
        EMAIL_ACCOUNT_USER_NAME      (),
        EMAIL_ACCOUNT_USER_PASSWORD  (true),
        EMAIL_SERVER_ADDRESS         (true),
        EMAIL_SERVER_PORT            (true),
        EMAIL_SMTP_OR_GMAIL          (true),
        EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES(),
        BASE_LOG_FOLDER              (),
        PATH_TO_LOGO                 (true),
        CHROME_DRIVER_PATH_TO_EXE    (),
        PHANTOMJS_PATH_TO_EXE        (),
        FIREFOX_PATH_TO_BROWSER_EXE  (),
        TEST_RUN_LOG_FOLDER          ();

        private boolean isSuppressedFromLogDisplay;

        SettingParameters(){
            this.isSuppressedFromLogDisplay = false;
        }

        SettingParameters(boolean isSuppressedFromLogDisplay){
            this.isSuppressedFromLogDisplay = isSuppressedFromLogDisplay;
        }

        public boolean isSuppressedFromLogDisplay(){
            return isSuppressedFromLogDisplay;
        }

        public String friendlyName(){
            return SupportMethods.stringToCapitalInitialCharacterForEachWordAndNoSpaces(this.toString().replace("_", " "));
        }
    }

    public String getValue(SettingParameters settingParameters){
        return get(settingParameters.friendlyName());
    }

    public void setValue(SettingParameters settingParameters, String value){
        put(settingParameters.friendlyName(), value);
    }

    public void setCustomValue(String parameter, String value){
        put(parameter, value);
    }

    public String getCustomValue(String parameter){
        return get(parameter);
    }
    // Man ska inte behöva ange värde för alla parametrar.
    // Man ska kunna lägga till custom-värden när det behövs
    // Det ska gå att hantera lösenord och liknande icke-utskriftgrejer separat
    // Det ska gå att uppdatera värden i runtime
    //

    private void toFile(String outputFilePath){
        StringBuilder stringBuilder = new StringBuilder();
        for(String key : this.keySet()){
            stringBuilder.append(key).append("=").append(this.get(key)).append(SupportMethods.LF);
        }
        SupportMethods.saveToFile(stringBuilder.toString(), outputFilePath);
    }

    private void readFromFileIfItExistElseTryToCreateFile(String settingsFilePath){
        List<String> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(settingsFilePath))) {
            stream.forEach(lines::add);
            for(String line : lines){
                if(!line.contains("=")) continue;
                setCustomValue(line.split("=")[0], line.split("=")[line.split("=").length-1]);
            }
        } catch (IOException e) { //No file exist yet
            logger.error( "Could not read Settings from file '" + settingsFilePath + "'.", e );
            try {
                toFile(settingsFilePath);
            }catch (Exception ex){
                logger.error( "Could neither read Settings from file, nor write to file '" + settingsFilePath + "'. " + ex.toString(), ex );
            }
        }
    }

    private boolean isSuppressedFromLogDisplay(String key){
        try {
            return SettingParameters.valueOf(key).isSuppressedFromLogDisplay();
        } catch (Exception e){
            return false;
        }
    }

    /**
     * Set up settings realm, reading settings from file if the file exist
     */
    public Settings(){
        loadDefaults();
        readFromFileIfItExistElseTryToCreateFile(getValue(SettingParameters.BASE_LOG_FOLDER) + "runSettings.properties");
    }

    /**
     * Default values for Settings parameters
     */
    private void loadDefaults(){

        //TODO: We need to figure out were to set root folder TAF, in mac jvm doesn't have sufficent rights!!!
        //macintosh
        if( Utils.getInstance().amIMacOS() ) {
            //setValueForProperty("baseLogFolder", Utils.getInstance().getRootDirectory() + "TAF" + File.separator);
            setValue(SettingParameters.BASE_LOG_FOLDER, Utils.getInstance().getUserWorkingDirectory() + File.separator + "TAF" + File.separator);
            setValue(SettingParameters.PATH_TO_LOGO, "https://www.prv.se/globalassets/in-swedish/prv_logox2.png");
            setValue(SettingParameters.TEST_RUN_LOG_FOLDER, "");
            setValue(SettingParameters.CHROME_DRIVER_PATH_TO_EXE, Utils.getInstance().getUserWorkingDirectory() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "drivers" + File.separator + "chromedriver");
            setValue(SettingParameters.FIREFOX_PATH_TO_BROWSER_EXE, File.separator + "Applications" + File.separator + "Firefox.app" + File.separator);
            setValue(SettingParameters.PHANTOMJS_PATH_TO_EXE, Utils.getInstance().getUserWorkingDirectory() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "drivers" + File.separator + "phantomjs-2.1-macosx" );
        }
        else if (Utils.getInstance().amILinuxOS() ) {
            //setValueForProperty("baseLogFolder", Utils.getInstance().getRootDirectory() + "TAF" + File.separator);
            setValue(SettingParameters.BASE_LOG_FOLDER, Utils.getInstance().getUserWorkingDirectory() + File.separator + "TAF" + File.separator);
            setValue(SettingParameters.PATH_TO_LOGO, "https://www.prv.se/globalassets/in-swedish/prv_logox2.png");
            setValue(SettingParameters.TEST_RUN_LOG_FOLDER, "");
            setValue(SettingParameters.CHROME_DRIVER_PATH_TO_EXE, Utils.getInstance().getUserWorkingDirectory() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "drivers" + File.separator + "chromedriver");
            //setValue(SettingParameters.CHROME_DRIVER_PATH_TO_EXE, File.separator + "usr" + File.separator + "bin" + File.separator + "google-chrome-stable");
            setValue(SettingParameters.FIREFOX_PATH_TO_BROWSER_EXE, File.separator + "usr" + File.separator + "bin" + File.separator + "firefox");
            setValue(SettingParameters.PHANTOMJS_PATH_TO_EXE, Utils.getInstance().getUserWorkingDirectory() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "drivers" + File.separator + "phantomjs-2.1.1-linux-x86_64" );
        }
        // lets assume jvm is running upon windows os.
        else {
            setValue(SettingParameters.BASE_LOG_FOLDER, System.getProperty("user.home") + File.separator + "TAF" + File.separator);
            setValue(SettingParameters.PATH_TO_LOGO, "https://www.prv.se/globalassets/in-swedish/prv_logox2.png");
            setValue(SettingParameters.TEST_RUN_LOG_FOLDER, "");
            setValue(SettingParameters.CHROME_DRIVER_PATH_TO_EXE, System.getProperty("user.home") + File.separator + "TAF" + File.separator + "chromedriver.exe");
            setValue(SettingParameters.FIREFOX_PATH_TO_BROWSER_EXE, "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
            setValue(SettingParameters.PHANTOMJS_PATH_TO_EXE, getValue(SettingParameters.BASE_LOG_FOLDER) + "phantomjs.exe");
        }
        setValue(SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES, "");
        setValue(SettingParameters.EMAIL_SENDER_ADDRESS, "");
        setValue(SettingParameters.EMAIL_SERVER_ADDRESS, "");
        setValue(SettingParameters.EMAIL_ACCOUNT_USER_NAME, "");
        setValue(SettingParameters.EMAIL_ACCOUNT_USER_PASSWORD, "");
        setValue(SettingParameters.EMAIL_SERVER_PORT, "");
        setValue(SettingParameters.EMAIL_SMTP_OR_GMAIL, "");
    }

    String toHtmlTable(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table class=\"settingsTable\">").append(SupportMethods.LF);
        for(SettingParameters valuePair : SettingParameters.values()){
            if(valuePair.isSuppressedFromLogDisplay()) continue;
            stringBuilder.append("  <tr class=\"settings\"><td class=\"settingsParameterName\">").append(valuePair.friendlyName()).append("</td><td class=\"settingsParameterValue\">").append(getValue(valuePair)).append("</td></tr>").append(SupportMethods.LF);
        }
        stringBuilder.append("</table>").append(SupportMethods.LF);
        return stringBuilder.toString();
    }

}
