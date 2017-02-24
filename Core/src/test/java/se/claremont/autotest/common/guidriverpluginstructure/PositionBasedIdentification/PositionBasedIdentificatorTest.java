package se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by jordam on 2017-02-22.
 */
@SuppressWarnings("WeakerAccess")
public class PositionBasedIdentificatorTest {
    TestPositionBasedIdentificationObject leftLabel = new TestPositionBasedIdentificationObject("Label for editfield", 10, 100, 50, 150, "myTool.Label");
    TestPositionBasedIdentificationObject rightEditField = new TestPositionBasedIdentificationObject("Edit field", 150, 250, 50, 150, "myTool.EditField");
    TestPositionBasedIdentificationObject bottomLeftButton = new TestPositionBasedIdentificationObject("Ok button", 10, 100, 200, 250, "myTool.Button");
    TestPositionBasedIdentificationObject bottomRightButton = new TestPositionBasedIdentificationObject("Cancel button", 160, 250, 200, 250, "myTool.Button");

    public ElementsList gui(){
        ArrayList<PositionBasedGuiElement> elementsList = new ArrayList<>();
        elementsList.add(bottomLeftButton);
        elementsList.add(leftLabel);
        elementsList.add(bottomRightButton);
        elementsList.add(rightEditField);
        return new ElementsList(elementsList);
    }

    @Test
    public void referencingToTheRightOfAndBelow(){
        PositionBasedGuiElement element = PositionBasedIdentificator.fromAllTheElements(gui()).
                keepElementsToTheRightOf(bottomLeftButton).
                theObjectMostToTheBottom();
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

        public Integer getLeftPosition() {
            return left;
        }

        public Integer getRightPosition() {
            return right;
        }

        public Integer getTopPosition() {
            return top;
        }

        public Integer getBottomPosition() {
            return bottom;
        }

        public String getTypeName() {
            return type;
        }

        public String getName(){return name;}
    }


}
