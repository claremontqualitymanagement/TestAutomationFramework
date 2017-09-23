import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.websupport.DomElement;

public class DomElementTest {

    public static DomElement testElement1(){
        return new DomElement("dummyRecognition", DomElement.IdentificationType.BY_VISIBLE_TEXT);
    }

    public DomElement testElement2 = new DomElement("dummyRecognition", DomElement.IdentificationType.BY_VISIBLE_TEXT);

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
    public void namelessVariableNameSettingTest(){
        Assert.assertTrue(new DomElement("'dummyRecognition'", DomElement.IdentificationType.BY_VISIBLE_TEXT).name, new DomElement("dummyRecognition", DomElement.IdentificationType.BY_VISIBLE_TEXT).name.equals("'dummyRecognition'"));
    }
}
