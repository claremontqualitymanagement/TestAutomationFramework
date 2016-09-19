package se.claremont.autotest.guidriverpluginstructure;

/**
 * Any kind of GUI element
 *
 * Created by jordam on 2016-08-25.
 */
@SuppressWarnings({"SameParameterValue", "unused", "UnusedParameters"})
public interface GuiDriver {

    void click(GuiElement guiElement);

    void write(GuiElement guiElement, String textToWrite);

    String getText(GuiElement guiElement);

    boolean exists(GuiElement guiElement);
    boolean existsWithTimeout(GuiElement guiElement, int timeOutInSeconds);

    void verifyObjectExistence(GuiElement guiElement);
    void verifyObjectExistenceWithTimeout(GuiElement guiElement, int timeoutInSeconds);

    void chooseInDropdown(GuiElement guiElement, String choice);

    void chooseRadioButton(GuiElement guiElement, String text);

    void verifyImage(GuiElement guiElement, String pathToOracleImage);

}
