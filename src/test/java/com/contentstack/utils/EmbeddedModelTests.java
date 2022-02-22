package com.contentstack.utils;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmbeddedModelTests {

    private static final Logger logger = Logger.getLogger(UtilTests.class.getName());
    private static JSONObject localJsonObj;

    @BeforeClass
    public static void startDefaultOptionsTests() throws IOException {
        logger.setLevel(Level.ALL);
        final String EMBEDDED_ITEMS = "src/test/resources/embedded_items.json";
        localJsonObj = new ReadResource().readJson(EMBEDDED_ITEMS);
    }

    @Test
    public void testRichTextAvailable() {
        JSONObject entries = (JSONObject) localJsonObj.optJSONArray("entries").get(0);
        String rich_text_editor = (String) entries.optJSONArray("rich_text_editor").get(0);
        Assert.assertNotNull("", rich_text_editor);
    }

}
