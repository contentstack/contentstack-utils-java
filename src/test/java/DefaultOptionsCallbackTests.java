import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.render.DefaultOptionsCallback;
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
public class DefaultOptionsCallbackTests {

    private static final Logger logger = Logger.getLogger(UtilTests.class.getName());
    private static JSONObject localJsonObj;

    @BeforeClass
    public static void startDefaultOptionsTests() throws IOException {
        logger.setLevel(Level.ALL);
        final String EMBEDDED_ITEMS = "src/test/resources/embedded_items.json";
        localJsonObj = (JSONObject) new ReadResource().readJson(EMBEDDED_ITEMS).getJSONArray("entries").get(0);
    }

    @Test
    public void test_01_test_attributes() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        Assert.assertEquals("entry", attributes.get("type").toLowerCase());
    }

    @Test
    public void test_02_renderOptions() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        Assert.assertEquals("entry", attributes.get("type").toLowerCase());
        final DefaultOptionsCallback defaultOptions = new DefaultOptionsCallback();
        Metadata metadata = new Metadata("TextTest", "entry", "blt6723673", "content_type_uid", "block","outerHTMLTet", attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<div><p></p><div><p>Content type: <span></span></p></div>", result);
    }


    @Test
    public void test_03_defaultOptions() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOptionsCallback defaultOptions = new DefaultOptionsCallback();
        Metadata metadata = new Metadata("TextTest", "entry", "blt6723673", "content_type_uid", "block","outerHTMLTet", attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<div><p></p><div><p>Content type: <span></span></p></div>", result);
    }


    @Test
    public void test_04_entryInline() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOptionsCallback defaultOptions = new DefaultOptionsCallback();
        Metadata metadata = new Metadata("TextTest", "entry", "blt6723673", "content_type_uid", "inline","outerHTMLTet", attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<span></span>", result);
    }


    @Test
    public void test_05_embeddedLink() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOptionsCallback defaultOptions = new DefaultOptionsCallback();
        Metadata metadata = new Metadata("TextTest", "entry", "blt6723673", "content_type_uid", "link","outerHTMLTet", attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<a href=\"\"></a>", result);
    }

    @Test
    public void test_embedded_default_displayable() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOptionsCallback defaultOptions = new DefaultOptionsCallback();
        Metadata metadata = new Metadata("TextTest", "entry", "blt6723673", "content_type_uid", "display","outerHTMLTet", attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<img src=\"\" alt=\"\" />", result);
    }




}
