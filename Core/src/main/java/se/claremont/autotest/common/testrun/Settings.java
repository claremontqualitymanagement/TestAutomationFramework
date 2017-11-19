package se.claremont.autotest.common.testrun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.common.backendserverinteraction.TafBackendServerConnection;
import se.claremont.autotest.common.backendserverinteraction.TestlinkAdapterServerConnection;
import se.claremont.autotest.common.reporting.TafVersionGetter;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.support.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * <p>
 * Test execution parameters. Normally used in a TestRun context.
 * Hidden items are not displayed in test run reports.</p>
 * <p>
 *     Apart from the fix standard settings parameters, custom ones can be used too.
 * </p>
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class Settings extends HashMap<String, String> {

    private final static Logger logger = LoggerFactory.getLogger(Settings.class);

    //Some of these setting parameters are suppressed from log display in the summary reportTestRun, where these settings othervice is displayed.

    public String getValue(SettingParameters settingParameters) {
        if(settingParameters == SettingParameters.HTML_REPORTS_LINK_PREFIX ){
            if((get(settingParameters.toString()) == null || get(settingParameters.toString()).length() == 0) && (
                    get(settingParameters.friendlyName()) == null || get(settingParameters.friendlyName()).length() == 0)){
                String baseLogFolder = getValue(SettingParameters.BASE_LOG_FOLDER);
                if(baseLogFolder.contains("://") && baseLogFolder.indexOf("://") < 7){
                    return baseLogFolder.substring(0, baseLogFolder.indexOf("://"));
                } else {
                    return "file";
                }
            } else {
                if(get(settingParameters.toString()) != null) return get(settingParameters.toString());
                return get(settingParameters.friendlyName());
            }
        }
        String value = get(settingParameters.toString());
        if (value == null)
            value = get(settingParameters.friendlyName());
        return value;
    }

    public void setValue(SettingParameters settingParameters, String value) {
        if (this.containsKey(settingParameters.toString())) {
            replace(settingParameters.toString(), value);
        } else if (this.containsKey(settingParameters.friendlyName())) {
            replace(settingParameters.friendlyName(), value);
        } else {
            put(settingParameters.friendlyName(), value);
        }
    }

    void setCustomValue(String parameter, String value) {
        if (this.containsKey(parameter)) {
            replace(parameter, value);
        } else {
            put(parameter, value);
        }
    }

    String getCustomValue(String parameter) {
        return get(parameter);
    }


    /**
     * Writes current Settings values into runSettings.properties file.
     *
     * @param outputFilePath file path to runSettings.properties
     */
    public void writeSettingsParametersToFile(String outputFilePath) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : this.keySet()) {
            stringBuilder.append(key).append("=").append(this.get(key)).append(SupportMethods.LF);
        }
        SupportMethods.saveToFile(stringBuilder.toString(), outputFilePath);
        File settingsFile = new File(outputFilePath);
        if (settingsFile.exists()) {
            setValue(SettingParameters.RUN_SETTINGS_FILE, outputFilePath);
        }
    }

    private void readFromFileIfItExistElseTryToCreateFile(String settingsFilePath) {
        List<String> lines = new ArrayList<>();
        File settingFile = new File(settingsFilePath);
        if (settingFile.exists()) {
            setValue(SettingParameters.RUN_SETTINGS_FILE, settingsFilePath);
            try (Stream<String> stream = Files.lines(Paths.get(settingsFilePath))) {
                stream.forEach(lines::add);
                for (String line : lines) {
                    if (!line.contains("=")) continue;
                    setCustomValue(line.split("=")[0], line.substring(line.indexOf('=') + 1));
                }
            } catch (IOException ignored) {}//No file exist yet
        } else {
            logger.warn("Could not read TAF settings from file '" + settingsFilePath + "'. Don't worry. Continuing with default values, and attempting saving of the settings to file '" + settingsFilePath + "' for next time.");
            try {
                writeSettingsParametersToFile(settingsFilePath);
                setValue(SettingParameters.RUN_SETTINGS_FILE, settingsFilePath);
            } catch (Exception ex) {
                logger.warn("Could not save Settings to new settings file '" + settingsFilePath + "'. Error message: " + ex.getMessage(), ex);
            }
        }
    }

    private boolean isSuppressedFromLogDisplay(String key) {
        try {
            return SettingParameters.valueOf(key).isSuppressedFromLogDisplay();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Set up settings realm, reading settings from file if the file exist
     */
    public Settings() {
        loadDefaults();
        readFromFileIfItExistElseTryToCreateFile(getValue(SettingParameters.BASE_LOG_FOLDER) + "runSettings.properties");
    }

    public Settings(String pathToSettingsPropertiesFile) {
        loadDefaults();
        setValue(SettingParameters.RUN_SETTINGS_FILE, pathToSettingsPropertiesFile);
        readFromFileIfItExistElseTryToCreateFile(pathToSettingsPropertiesFile);
    }

    private void setHtmlReportPrefixFromBaseFolderIfNotSetExplicitly(){
        if(getValue(SettingParameters.HTML_REPORTS_LINK_PREFIX) == null || getValue(SettingParameters.HTML_REPORTS_LINK_PREFIX).length() == 0){
            String baseLogFolder = getValue(SettingParameters.BASE_LOG_FOLDER);
            if(baseLogFolder.contains("://") && baseLogFolder.indexOf("://") < 7){
                setValue(SettingParameters.HTML_REPORTS_LINK_PREFIX, baseLogFolder.substring(0, baseLogFolder.indexOf("://")));
            } else {
                setValue(SettingParameters.HTML_REPORTS_LINK_PREFIX, "file");
            }
        }
    }

    /**
     * Default values for Settings parameters
     */
    private void loadDefaults() {

        //TODO: We need to figure out were to set root folder TAF, in mac jvm doesn't have sufficent rights!!!
        //macintosh
        if (Utils.getInstance().amIMacOS()) {
            //setValueForProperty("baseLogFolder", Utils.getInstance().getRootDirectory() + "TAF" + File.separator);
            setValue(SettingParameters.BASE_LOG_FOLDER, Utils.getInstance().getUserWorkingDirectory() + File.separator + "TAF" + File.separator);
            //setValue(SettingParameters.CHROME_DRIVER_PATH_TO_EXE, Utils.getInstance().getUserWorkingDirectory() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "drivers" + File.separator + "chromedriver_mac64");
            setValue(SettingParameters.FIREFOX_PATH_TO_BROWSER_EXE, File.separator + "Applications" + File.separator + "Firefox.app" + File.separator);
            setValue(SettingParameters.PHANTOMJS_PATH_TO_EXE, Utils.getInstance().getUserWorkingDirectory() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "drivers" + File.separator + "phantomjs-2.1-macosx");
        } else if (Utils.getInstance().amILinuxOS()) {
            //setValueForProperty("baseLogFolder", Utils.getInstance().getRootDirectory() + "TAF" + File.separator);
            setValue(SettingParameters.BASE_LOG_FOLDER, Utils.getInstance().getUserWorkingDirectory() + File.separator + "TAF" + File.separator);
            //setValue(SettingParameters.CHROME_DRIVER_PATH_TO_EXE, Utils.getInstance().getUserWorkingDirectory() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "drivers" + File.separator + "chromedriver_linux64");
            setValue(SettingParameters.FIREFOX_PATH_TO_BROWSER_EXE, File.separator + "usr" + File.separator + "bin" + File.separator + "firefox");
            setValue(SettingParameters.PHANTOMJS_PATH_TO_EXE, Utils.getInstance().getUserWorkingDirectory() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "drivers" + File.separator + "phantomjs-2.1.1-linux-x86_64");
        }
        // lets assume jvm is running upon windows os.
        else {
            setValue(SettingParameters.BASE_LOG_FOLDER, Utils.getInstance().getUserHomeDirectory() + File.separator + "TAF" + File.separator);
            //setValue(SettingParameters.CHROME_DRIVER_PATH_TO_EXE, System.getProperty("user.home") + File.separator + "TAF" + File.separator + "chromedriver.exe");
            setValue(SettingParameters.FIREFOX_PATH_TO_BROWSER_EXE, "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
            setValue(SettingParameters.PHANTOMJS_PATH_TO_EXE, getValue(SettingParameters.BASE_LOG_FOLDER) + "phantomjs.exe");
        }
        setValue(SettingParameters.TEST_RUN_LOG_FOLDER, "");
        setValue(SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "1");
        setValue(SettingParameters.PATH_TO_LOGO, "http://46.101.193.212/TAF/images/claremontlogo.gif");
        setValue(SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES, "");
        setValue(SettingParameters.EMAIL_SENDER_ADDRESS, "");
        setValue(SettingParameters.EMAIL_SERVER_ADDRESS, "");
        setValue(SettingParameters.EMAIL_ACCOUNT_USER_NAME, "");
        //setValue(SettingParameters.HTML_REPORTS_LINK_PREFIX, null); //For SMB references this needs to be set after BaseLogFolder - hence set from constructor.
        //setValue(SettingParameters.HTML_REPORTS_LINK_PREFIX_TO_APPENDED_FILES_BASE_FOLDER, getValue(SettingParameters.HTML_REPORTS_LINK_PREFIX) + "://" + getValue(SettingParameters.BASE_LOG_FOLDER));
        setValue(SettingParameters.EMAIL_ACCOUNT_USER_PASSWORD, "");
        setValue(SettingParameters.EMAIL_SERVER_PORT, "");
        setValue(SettingParameters.EMAIL_SMTP_OR_GMAIL, "");
        setValue(SettingParameters.URL_TO_TAF_BACKEND, TafBackendServerConnection.defaultServerUrl);
        setValue(SettingParameters.URL_TO_TESTLINK_ADAPTER, TestlinkAdapterServerConnection.defaultServerUrl);
        //setValue(SettingParameters.PLUGIN_FOLDER, getValue(SettingParameters.BASE_LOG_FOLDER) + "Plugins");
    }

    public String toHtmlTable() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table class=\"settingsTable\">").append(SupportMethods.LF);
        for (SettingParameters valuePair : SettingParameters.values()) {
            if (valuePair.isSuppressedFromLogDisplay() || getValue(valuePair) == null || getValue(valuePair).equals(""))
                continue;
            stringBuilder.append("  <tr class=\"settings\"><td class=\"settingsParameterName\">").append(valuePair.friendlyName()).append("</td><td class=\"settingsParameterValue\">").append(getValue(valuePair)).append("</td></tr>").append(SupportMethods.LF);
        }

        String version = TafVersionGetter.tafVersion();
        if (version != null) {//Version is only identified when running tests from command line usage of TAF.
            stringBuilder.append("  <tr class=\"settings\"><td class=\"settingsParameterName\">").append("TAF version").append("</td><td class=\"settingsParameterValue\">").append(TafVersionGetter.tafVersion()).append("</td></tr>").append(SupportMethods.LF);
        }

        stringBuilder.append("</table>").append(SupportMethods.LF).append("<br>").append(SupportMethods.LF);
        return stringBuilder.toString();
    }

}
