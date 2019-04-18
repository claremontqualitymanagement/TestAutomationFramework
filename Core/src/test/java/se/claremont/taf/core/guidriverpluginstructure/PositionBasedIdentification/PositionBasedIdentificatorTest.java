package se.claremont.taf.core.guidriverpluginstructure.PositionBasedIdentification;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;

import java.util.ArrayList;

/**
 * Tests position based element identification
 *
 * Created by jordam on 2017-02-22.
 */
@SuppressWarnings("WeakerAccess")
public class PositionBasedIdentificatorTest extends UnitTestClass {
    final TestPositionBasedIdentificationObject leftLabel = new TestPositionBasedIdentificationObject("Label for editfield", 10, 100, 50, 150, "myTool.Label");
    final TestPositionBasedIdentificationObject rightEditField = new TestPositionBasedIdentificationObject("Edit field", 150, 250, 50, 150, "myTool.EditField");
    final TestPositionBasedIdentificationObject bottomLeftButton = new TestPositionBasedIdentificationObject("Ok button", 10, 100, 200, 250, "myTool.Button");
    final TestPositionBasedIdentificationObject bottomRightButton = new TestPositionBasedIdentificationObject("Cancel button", 160, 250, 200, 250, "myTool.Button");

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
        PositionBasedGuiElement element = (PositionBasedGuiElement)PositionBasedIdentificator.fromAllTheElements(gui()).
                keepElementsToTheRightOf(bottomLeftButton).
                theObjectMostToTheBottom();
        Assert.assertNotNull("Ended up with no element", element);
        TestPositionBasedIdentificationObject testObject = (TestPositionBasedIdentificationObject) element;
        Assert.assertTrue(testObject.getName(), testObject.getName().equals("Cancel button"));
    }

    @Test
    public void referencingToTheLeftOfAndBelow(){
        PositionBasedGuiElement element = (PositionBasedGuiElement)PositionBasedIdentificator.fromAllTheElements(gui()).keepElementsToTheLeftOf(bottomRightButton).theObjectMostToTheBottom();
        Assert.assertNotNull("Ended up with no element", element);
        TestPositionBasedIdentificationObject testObject = (TestPositionBasedIdentificationObject) element;
        Assert.assertTrue(testObject.getName(), testObject.getName().equals("Ok button"));
    }

    @Test
    public void referencingToTheLeftOfAndAbove(){
        PositionBasedGuiElement element = (PositionBasedGuiElement)PositionBasedIdentificator.fromAllTheElements(gui()).keepElementsToTheLeftOf(bottomRightButton).theObjectMostToTheTop();
        Assert.assertNotNull("Ended up with no element", element);
        TestPositionBasedIdentificationObject testObject = (TestPositionBasedIdentificationObject) element;
        Assert.assertTrue(testObject.getName(), testObject.getName().equals("Label for editfield"));
    }

    @Test
    public void typeFilter(){
        PositionBasedGuiElement element = (PositionBasedGuiElement)PositionBasedIdentificator.fromAllTheElements(gui()).keepElementsOfType("EditField").theOnlyElementThatShouldBeLeft();
        Assert.assertNotNull("Ended up with no element", element);
        TestPositionBasedIdentificationObject testObject = (TestPositionBasedIdentificationObject) element;
        Assert.assertTrue(testObject.getName(), testObject.getName().equals("Edit field"));
    }


    public class TestPositionBasedIdentificationObject implements PositionBasedGuiElement {
        final int left;
        final int right;
        final int top;
        final int bottom;
        final String type;
        final String name;

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

        @Override
        public Object runtimeElement() {
            return this;
        }

        @Override
        public ArrayList<PositionBasedGuiElement> childElements() {
            return new ArrayList<>();
        }

        public String getName(){return name;}
    }


}
