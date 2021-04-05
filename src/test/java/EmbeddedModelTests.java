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
        logger.info("Reading localJsonObj"+localJsonObj);
    }


    @Test
    public void test_rich_text_Available() {
        JSONObject entries = (JSONObject) localJsonObj.optJSONArray("entries").get(0);
        String rich_text_editor = (String) entries.optJSONArray("rich_text_editor").get(0);
        Assert.assertEquals( "<p>hello</p><img class=\"embedded-asset\" data-redactor-type=\"embed\" data-widget-code=\"\" data-sys-asset-filelink=\"http://localhost:8000/v3/assets/blte964dd749943a934/blt6ba515fc6c148887/5d0b406eb0ed0f0c3d433115/dp.jpeg\" data-sys-asset-uid=\"bltba476c60baacb442\" data-sys-asset-filename=\"dp.jpeg\" data-sys-asset-contenttype=\"image/jpeg\" type=\"asset\" sys-style-type=\"display\"></img><div class=\"redactor-component embedded-entry block-entry redactor-component-active\" data-redactor-type=\"embed\" data-widget-code=\"\" data-sys-entry-uid=\"bltb5a04880fbb74f26\" data-sys-entry-locale=\"en-us\" data-sys-content-type-uid=\"samplect\" sys-style-type=\"block\" type=\"entry\"></div>\n" +
                "<p>hello</p>\n" +
                "<p></p>", rich_text_editor);
    }


}
