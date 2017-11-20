import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.websupport.DomElement;
import se.claremont.autotest.websupport.elementidentification.By;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

public class DomElementTest{

    private static DomElement testElement1(){
        return new DomElement("dummyRecognition", DomElement.IdentificationType.BY_VISIBLE_TEXT);
    }

    @SuppressWarnings("CanBeFinal")
    private DomElement testElement2 = new DomElement("dummyRecognition", DomElement.IdentificationType.BY_VISIBLE_TEXT);

    @Test
    public void staticMethodNameSettingTest(){
        Assert.assertTrue(testElement1().name, testElement1().name.equals("testElement1"));
    }

    @Test
    @Ignore("Cannot get this to work with Java8")
    public void fieldNameSettingTest(){
        Assert.assertTrue(testElement2.name, testElement2.name.equals("testElement2"));
    }

    @Test
    @Ignore("Cannot get this to work with Java8")
    public void methodInternalVariableSettingTest(){
        DomElement internalElement = new DomElement("dummyRecognition", DomElement.IdentificationType.BY_VISIBLE_TEXT);
        Assert.assertTrue(internalElement.name, internalElement.name.equals("internalElement"));
    }

    @Test
    public void domElementWithByConditionToString(){
        DomElement internal = new DomElement(By
                .textContainsAnyOfTheStrings("FirstString", "SecondString", "ThirdString")
                .andByClass("TheClassName")
                .andByTextContainsString("TheTextToFind")
                .andByAttributeValue("AttributeName", "AttributeValue"));

        System.out.println(internal.toString());

        Assert.assertTrue(internal.toString().contains("FirstString"));
        Assert.assertTrue(internal.toString().contains("SecondString"));
        Assert.assertTrue(internal.toString().contains("ThirdString"));
        Assert.assertTrue(internal.toString().contains("TheClassName"));
        Assert.assertTrue(internal.toString().contains("TheTextToFind"));
        Assert.assertTrue(internal.toString().contains("AttributeName"));
        Assert.assertTrue(internal.toString().contains("AttributeValue"));
    }

    @Test
    @Ignore("Needs Chrome browser and Internet access.")
    public void claremontWebPageMapper(){
        TestCase testCase = new TestCase();
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("http://www.claremont.se");
        web.wait(2000);
        web.mapCurrentPageWithBy("C:\\temp\\claremont2.txt", true);
        web.makeSureDriverIsClosed();

    }

    @Test
    public void namelessVariableNameSettingTest(){
        Assert.assertTrue(new DomElement("'dummyRecognition'", DomElement.IdentificationType.BY_VISIBLE_TEXT).name, new DomElement("dummyRecognition", DomElement.IdentificationType.BY_VISIBLE_TEXT).name.equals("'dummyRecognition'"));
    }

    @Test
    public void seleniumByAsArg(){
        DomElement domElement = new DomElement(org.openqa.selenium.By.partialLinkText("Hej"));
        System.out.println(domElement.toString());
    }
}
