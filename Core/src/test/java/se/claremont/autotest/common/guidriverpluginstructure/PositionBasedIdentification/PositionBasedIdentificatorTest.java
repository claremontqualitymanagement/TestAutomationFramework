package se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedIdentificator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-02-22.
 */
public class PositionBasedIdentificatorTest {
    TestPositionBasedIdentificationObject leftLabel = new TestPositionBasedIdentificationObject("Label for editfield", 10, 100, 50, 150, "myTool.Label");
    TestPositionBasedIdentificationObject rightEditField = new TestPositionBasedIdentificationObject("Edit field", 150, 250, 50, 150, "myTool.EditField");
    TestPositionBasedIdentificationObject bottomLeftButton = new TestPositionBasedIdentificationObject("Ok button", 10, 100, 200, 250, "myTool.Button");
    TestPositionBasedIdentificationObject bottomRightButton = new TestPositionBasedIdentificationObject("Cancel button", 160, 250, 200, 250, "myTool.Button");

    public ElementsList gui(){
        ElementsList elementsList = new ElementsList();
        elementsList.add(bottomLeftButton);
        elementsList.add(leftLabel);
        elementsList.add(bottomRightButton);
        elementsList.add(rightEditField);
        return elementsList;
    }

    @Test
    public void referencingToTheRightOfAndBelow(){
        PositionBasedGuiElement element = PositionBasedIdentificator.fromAllTheElements(gui()).keepElementsToTheRightOf(bottomLeftButton).theObjectMostToTheBottom();
        Assert.assertNotNull("Ended up with no element", element);
        TestPositionBasedIdentificationObject testObject = (TestPositionBasedIdentificationObject) element;
        Assert.assertTrue(testObject.getName(), testObject.getName().equals("Cancel button"));
    }

    @Test
    public void referencingToTheLeftOfAndBelow(){
        PositionBasedGuiElement element = PositionBasedIdentificator.fromAllTheElements(gui()).keepElementsToTheLeftOf(bottomRightButton).theObjectMostToTheBottom();
        Assert.assertNotNull("Ended up with no element", element);
        TestPositionBasedIdentificationObject testObject = (TestPositionBasedIdentificationObject) element;
        Assert.assertTrue(testObject.getName(), testObject.getName().equals("Ok button"));
    }

    @Test
    public void referencingToTheLeftOfAndAbove(){
        PositionBasedGuiElement element = PositionBasedIdentificator.fromAllTheElements(gui()).keepElementsToTheLeftOf(bottomRightButton).theObjectMostToTheTop();
        Assert.assertNotNull("Ended up with no element", element);
        TestPositionBasedIdentificationObject testObject = (TestPositionBasedIdentificationObject) element;
        Assert.assertTrue(testObject.getName(), testObject.getName().equals("Label for editfield"));
    }

    @Test
    public void typeFilter(){
        PositionBasedGuiElement element = PositionBasedIdentificator.fromAllTheElements(gui()).keepElementsOfType("EditField").theOnlyElementThatShouldBeLeft();
        Assert.assertNotNull("Ended up with no element", element);
        TestPositionBasedIdentificationObject testObject = (TestPositionBasedIdentificationObject) element;
        Assert.assertTrue(testObject.getName(), testObject.getName().equals("Edit field"));
    }


    public class TestPositionBasedIdentificationObject implements PositionBasedGuiElement {
        int left;
        int right;
        int top;
        int bottom;
        String type;
        String name;

        public TestPositionBasedIdentificationObject(String name, int left, int right, int top, int bottom, String type){
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
            this.type = type;
            this.name = name;
        }

        public int getLeftPosition() {
            return left;
        }

        public int getRightPosition() {
            return right;
        }

        public int getTopPosition() {
            return top;
        }

        public int getBottomPosition() {
            return bottom;
        }

        public String getTypeName() {
            return type;
        }

        public String getName(){return name;}
    }


}
