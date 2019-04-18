package se.claremont.taf.core.guidriverpluginstructure;

/**
 * Any kind of GUI element interaction.
 * Generally speaking methods starting with 'verify'... return void, but logs to test case log.
 * Most
 *
 * Created by jordam on 2016-08-25.
 */
@SuppressWarnings({"SameParameterValue", "unused", "UnusedParameters"})
public interface GuiDriver {

    //Interaction methods for actions in GUI. Logs 'Executed', 'Execution problem', or possibly 'Framework error' log entries.
    void click                      (GuiElement guiElement);
    void write                      (GuiElement guiElement, String textToWrite);
    void chooseRadioButton          (GuiElement guiElement, String text);
    void selectInDropdown(GuiElement guiElement, String choice);

    //General methods for debugging purposes. Writes 'Debug' log entries.
    String getText(GuiElement guiElement);

    //Control mechanisms for test flow control. Logs debug information only.
    boolean exists                 (GuiElement guiElement);
    boolean existsWithTimeout      (GuiElement guiElement, int timeOutInSeconds);
    boolean isDisplayed            (GuiElement guiElement);
    boolean isNotDisplayed         (GuiElement guiElement);

    //Verification methods for test steps. Can write log entries of 'Debug', 'Verification passed', 'Verification failed', 'Verification problem', or possibly 'Framework error' log entries.
    void    verifyObjectIsDisplayed               (GuiElement guiElement);
    void    verifyObjectIsNotDisplayed            (GuiElement guiElement);
    void    verifyObjectExistence                 (GuiElement guiElement);
    void    verifyObjectExistenceWithTimeout      (GuiElement guiElement, int timeoutInSeconds);
    void    verifyImage                           (GuiElement guiElement, String pathToOracleImage);

}
