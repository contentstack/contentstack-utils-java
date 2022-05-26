package com.contentstack.utils;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.render.DefaultOption;
import org.json.JSONObject;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultOptionTests {

    private static final Logger logger = Logger.getLogger(UtilTests.class.getName());
    private static JSONObject localJsonObj;

    @BeforeClass
    public static void startDefaultOptionsTests() throws IOException {
        logger.setLevel(Level.ALL);
        final String EMBEDDED_ITEMS = "src/test/resources/embedded_items.json";
        localJsonObj = (JSONObject) new ReadResource().readJson(EMBEDDED_ITEMS).getJSONArray("entries").get(0);
    }

    @Test
    public void testTestAttributes() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        Assert.assertEquals("entry", attributes.get("type").toLowerCase());
    }

    @Test
    public void testRenderOptions() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        Assert.assertEquals("entry", attributes.get("type").toLowerCase());
        final DefaultOption defaultOptions = new DefaultOption();
        Metadata metadata = new Metadata("TextTest", "entry", "6723673", "content_type_uid", "block", "outerHTMLTet",
                attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<div><p></p><div><p>Content type: <span></span></p></div>", result);
    }

    @Test
    public void testDefaultOptions() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOption defaultOptions = new DefaultOption();
        Metadata metadata = new Metadata("TextTest", "entry", "6723673", "content_type_uid", "block", "outerHTMLTet",
                attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<div><p></p><div><p>Content type: <span></span></p></div>", result);
    }

    @Test
    public void testEntryInline() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOption defaultOptions = new DefaultOption();
        Metadata metadata = new Metadata("TextTest", "entry", "6723673", "content_type_uid", "inline",
                "outerHTMLTet", attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<span></span>", result);
    }

    @Test
    public void testEmbeddedLink() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOption defaultOptions = new DefaultOption();
        Metadata metadata = new Metadata("TextTest", "entry", "6723673", "content_type_uid", "link",
                "outerHTMLTet", attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<a href=\"\"></a>", result);
    }

    @Test
    public void testEmbeddedDefaultDisplayable() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOption defaultOptions = new DefaultOption();
        Metadata metadata = new Metadata("TextTest", "entry", "6723673", "content_type_uid", "display",
                "outerHTMLTet", attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<img src=\"\" alt=\"\" />", result);
    }

}
