package se.claremont.taf.testrun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.taf.backendserverinteraction.TafBackendServerConnection;
import se.claremont.taf.backendserverinteraction.TestlinkAdapterServerConnection;
import se.claremont.taf.reporting.TafVersionGetter;
import se.claremont.taf.support.StringManagement;
import se.claremont.taf.support.SupportMethods;
import se.claremont.taf.support.Utils;

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

    //Some of these setting parameters are suppressed from log display in the summary reportTestRun, where these settings otherwise is displayed.

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
    @SuppressWarnings("SpellCheckingInspection")
    private void loadDefaults() {

        //TODO: We need to figure out were to set root folder TAF, in mac jvm doesn't have sufficient rights!!!
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

    /**
     * Standard settings parameters for TAF.
     */
    @SuppressWarnings("SameParameterValue")
    public enum SettingParameters {

        //General test run settings
        //============================

        /**
         * Run settings can be set from CLI, programatically (TestRun.setSettingsValue() or
         * TestRun.setCustomSettingsValue()) or via a runSettings.properties file in the
         * BASE_LOG_FOLDER. If a specific run settings file should be used this parameter
         * points out the path to this file.
         */
        RUN_SETTINGS_FILE("Run settings file"),

        /**
         * Sets the execution mode for a test run. Values adhered to are 'none', 'methods', 'classes',
         * or any positive number. The number corresponds to the maximum number of concurrent threads
         * used for execution.
         */
        PARALLEL_TEST_EXECUTION_MODE("Parallel test execution"),

        //General reporting settings
        //============================

        /**
         * The root folder for logging. If not explicitly set a folder called TAF directly under
         * the current user home folder will be used.
         */

        BASE_LOG_FOLDER("Report log folder"),
        /**
         * Normally the test run log folder is set to a new folder in the BASE_LOG_FOLDER.
         * The new folder is named from the execution time and the test sets executed.
         * Sometimes you want to set a specific log folder, then this parameter should be used.
         */

        CLOUD_LOG_URL("Cloud log url"),
        /**
         * Sometimes you want to set a specific url to cloud storage, then this parameter should be used.
         */

        TEST_RUN_LOG_FOLDER("Log folder for test run"),

        /**
         * Setting the link access protocol type for screenshots and other debugging
         * info saved when executing tests. Examples: 'http', 'https', 'file', 'smb' and so forth.
         */
        HTML_REPORTS_LINK_PREFIX("HTML reports link prefix"),

        /**
         * HTML based log reports can have a custom logo at the top.
         * The path to this logo is set from this parameter.
         * This parameter will be suppressed from display in repors.
         */
        PATH_TO_LOGO("Path to custom report top logotype image", true),

        /**
         * Setting the threshold for log post log levels for log posts being printed
         * to console output.
         */
        CONSOLE_LOG_LEVEL("Console log level"),

        //Web driver parameters
        //=========================

        /**
         * Path to the Selenium driver for PhantomJS.
         */
        PHANTOMJS_PATH_TO_EXE("Path to PhantomJS binary"),

        /**
         * Path to the Selenium driver for Firefox.
         */
        FIREFOX_PATH_TO_BROWSER_EXE("Path to Firefox browser binary"),

        //Specific reporters setup parameters
        //========================================

        //Email setup
        //----------------------------------------

        /**
         * The email address to populate the sender field of a email report.
         */
        EMAIL_SENDER_ADDRESS("Email sender address"),

        /**
         * The email acount user name, if needed for sending email. Will be suppressed from display in reports.
         */
        EMAIL_ACCOUNT_USER_NAME("Email accound user name", true),

        /**
         * The email account password, if needed for sending email. Will be suppressed from display in reports.
         */
        EMAIL_ACCOUNT_USER_PASSWORD("Email account user password", true),

        /**
         * The email server address (without port). This parameter will be suppressed from display in reports.
         */
        EMAIL_SERVER_ADDRESS("Email server address", true),

        /**
         * The email server port. This parameter will be suppressed from display in reports.
         */
        EMAIL_SERVER_PORT("Email server port", true),

        /**
         * The email mechanism used. This parameter will be suppressed from display in reports.
         */
        EMAIL_SMTP_OR_GMAIL("Email send method (SMTP or GMAIL)", true),

        /**
         * A list of the email report recipients. The list should be comma separated, and preferably not include spaces.
         */
        EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES("Report email recipients"),

        //TAF Backend server setup
        //----------------------------------------

        /**
         * If this parameter is explicitly set an attempt will be made to report test run results to the
         * TAF Backend Server at this address.
         */
        URL_TO_TAF_BACKEND("URL to TAF Backend Server"),

        //TAF Testlink Adapter server reporting setup
        //----------------------------------------

        /**
         * If this parameter is changed from its default value an attempt is made to report test results
         * to the Testlink Adapter server at this location after the test run is complete. If the adapter
         * server is correctly setup test results end up in Testlink.
         */
        URL_TO_TESTLINK_ADAPTER("URL to TAF Testlink Adapter Server");

        private final boolean isSuppressedFromLogDisplay;
        private final String friendlyName;

        @SuppressWarnings("unused") //Needed for automatic JSON management
        SettingParameters() {
            this.friendlyName = StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(this.toString().replace("_", " "));
            this.isSuppressedFromLogDisplay = false;
        }

        SettingParameters(String friendlyName, boolean isSuppressedFromLogDisplay) {
            this.isSuppressedFromLogDisplay = isSuppressedFromLogDisplay;
            this.friendlyName = friendlyName;
        }

        SettingParameters(String friendlyName) {
            this.isSuppressedFromLogDisplay = false;
            this.friendlyName = friendlyName;
        }

        public boolean isSuppressedFromLogDisplay() {
            return isSuppressedFromLogDisplay;
        }

        public String friendlyName() {
            return this.friendlyName;
        }
    }
}
