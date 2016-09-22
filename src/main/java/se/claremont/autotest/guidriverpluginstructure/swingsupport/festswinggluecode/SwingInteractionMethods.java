package se.claremont.autotest.guidriverpluginstructure.swingsupport.festswinggluecode;

import org.fest.swing.fixture.FrameFixture;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.guidriverpluginstructure.GuiDriver;
import se.claremont.autotest.guidriverpluginstructure.GuiElement;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.SwingElement;

/**
 * Created by jordam on 2016-09-18.
 */
public class SwingInteractionMethods implements GuiDriver{

    private FrameFixture naive;

    @Ignore
    @Test
    public void shouldChangeTextInTextFieldWhenClickingButton() {
        naive.button("byeButton").click();
        naive.label("messageLabel").requireText("Bye!");
    }

    public void click(GuiElement guiElement){
        FrameFixture frameFixture = new FrameFixture(guiElement.name);
        SwingElement swingElement = (SwingElement) guiElement;
    }

    @Override
    public void write(GuiElement guiElement, String textToWrite) {

    }

    @Override
    public String getText(GuiElement guiElement) {
        return null;
    }

    @Override
    public boolean exists(GuiElement guiElement) {
        return false;
    }

    @Override
    public boolean existsWithTimeout(GuiElement guiElement, int timeOutInSeconds) {
        return false;
    }

    @Override
    public void verifyObjectExistence(GuiElement guiElement) {

    }

    @Override
    public void verifyObjectExistenceWithTimeout(GuiElement guiElement, int timeoutInSeconds) {

    }

    @Override
    public void chooseInDropdown(GuiElement guiElement, String choice) {

    }

    @Override
    public boolean isDisplayed(GuiElement guiElement) {
        return false;
    }

    @Override
    public boolean isNotDisplayed(GuiElement guiElement) {
        return false;
    }

    @Override
    public void verifyObjectIsDisplayed(GuiElement guiElement) {

    }

    @Override
    public void verifyObjectIsNotDisplayed(GuiElement guiElement) {

    }

    @Override
    public void chooseRadioButton(GuiElement guiElement, String text) {

    }

    @Override
    public void verifyImage(GuiElement guiElement, String pathToOracleImage) {

    }
}
