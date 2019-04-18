package se.claremont.taf.json;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests to assure JsonParser works as expected
 *
 * Created by jordam on 2016-10-28.
 */
public class JsonParserTest extends UnitTestClass{

    @Test
    public void nullJsonGet(){
        Assert.assertNull(JsonParser.get(null, ""));
    }

    @Test
    public void nullParameterNameGet(){
        Assert.assertNull(JsonParser.get("", null));
    }

    @Test
    public void nullJsonGetInt(){
        Assert.assertNull(JsonParser.getInt(null, ""));
    }

    @Test
    public void nullParameterNameGetInt(){
        Assert.assertNull(JsonParser.getInt("", null));
    }

    @Test
    public void positiveIsJsonCheck(){

        String contentSample = "{\"menu\": {\n" +
                "  \"id\": \"file\",\n" +
                "  \"value\": \"File\",\n" +
                "  \"popup\": {\n" +
                "    \"menuitem\": [\n" +
                "      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},\n" +
                "      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\n" +
                "      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\n" +
                "    ]\n" +
                "  }\n" +
                "}}";
        Assert.assertTrue(JsonParser.isJson(contentSample));
    }

    @Test
    public void negativeIsJsonCheck(){
        Assert.assertFalse(JsonParser.isJson("sdgfsdgf:sdgfasdf. asdfasdg, asdgas;asdgasdg{}()"));
    }
}
