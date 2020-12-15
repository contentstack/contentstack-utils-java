import com.contentstack.utils.render.DefaultOptions;
import org.json.JSONObject;
import org.jsoup.nodes.Attributes;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultOptionsTests {

    private static final Logger logger = Logger.getLogger(UtilTests.class.getName());
    private static JSONObject localJsonObj;

    @BeforeClass
    public static void startDefaultOptionsTests() throws IOException {
        logger.setLevel(Level.ALL);
        final String EMBEDDED_ITEMS = "src/test/resources/embedded_items.json";
        localJsonObj = (JSONObject) new ReadResource().readJson(EMBEDDED_ITEMS).getJSONArray("entries").get(0);
        logger.info("Reading Local Json Object..."+localJsonObj);
    }


    @Test
    public void test_embedded_default() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOptions defaultOptions = new DefaultOptions();
        // String result = defaultOptions.renderOptions(localJsonObj, attributes);
        // Assert.assertEquals("<div><p>Alaukik assets</p><div><p>Content type: <span>alaukik_assets</span></p></div>", result);
        // Assert.assertEquals("<div><p>this is unique title</p><div><p>Content type: <span></span></p></div>", result);
    }


    @Test
    public void test_embedded_default_inline() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        //String result = defaultOptions.renderOptions(StyleType.INLINE, entryObject, attributes);
        //log.info(result);
        //Assert.assertEquals("<span>this is unique title</span>", result);
    }


    @Test
    public void test_embedded_default_linked() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        //String result = defaultOptions.renderOptions()//(StyleType.LINKED, entryObject, attributes);
        //log.info(result);
        //Assert.assertEquals("<a href=\"/this-is-unique-title\">this is unique title</a>", result);
    }


    @Test
    public void test_embedded_default_displayable() {
        Attributes attributes = new ReadResource().returnAssetAttributes(localJsonObj);
        //String result = defaultOptions.renderOptions(StyleType.DISPLAY, entryObject, attributes);
        //log.info(result);
        //Assert.assertEquals("<img src=\"/this-is-unique-title\" alt=\"this is unique title\" />", result);
    }




}
