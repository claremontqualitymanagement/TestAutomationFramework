package se.claremont.autotest.common.testrun;

import se.claremont.autotest.common.support.StringManagement;

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
