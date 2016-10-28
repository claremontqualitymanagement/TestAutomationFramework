package se.claremont.autotest.dataformats;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.restsupport.*;
import se.claremont.autotest.restsupport.JsonParser;

/**
 * Created by jordam on 2016-10-28.
 */
public class JsonParser_Tests {

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


}
