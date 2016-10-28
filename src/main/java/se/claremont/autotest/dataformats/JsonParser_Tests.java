package se.claremont.autotest.dataformats;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.dataformats.JsonParser;
/**
 * Created by jordam on 2016-10-28.
 */
public class JsonParser_Tests {

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
        Assert.assertTrue(JsonParser.isJson(contentSample));
    }

    @Test
    public void negativeIsJsonCheck(){
        Assert.assertFalse(JsonParser.isJson("sdgfsdgf:sdgfasdf. asdfasdg, asdgas;asdgasdg{}()"));
    }
}
