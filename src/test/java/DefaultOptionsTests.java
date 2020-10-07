import com.contentstack.utils.embedded.StyleType;
import com.contentstack.utils.render.DefaultOptions;
import org.json.JSONArray;
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
public class DefaultOptionsTests {


    private static final Logger log = Logger.getLogger(UtilTests.class.getName());
    private static DefaultOptions defaultOptions = new DefaultOptions();
    //private static JSONObject ENTRY_OBJECT = new JSONObject();
    //private static JSONObject _embedded_object = new JSONObject();

    @BeforeClass
    public static void executeOnceBeforeTestStarts() throws IOException {
        // Setting level for the logging.
        log.setLevel(Level.ALL);
        //defaultOptions = new DefaultOptions();
        //String ASSET_DISPLAYABLE = "src/test/resources/multiple_objects.json";
        //JSONArray arrayResp = new ResourceFile().readJson(ASSET_DISPLAYABLE);
        //ENTRY_OBJECT = (JSONObject) arrayResp.get(0);
        //JSONArray _embedded_entries = ENTRY_OBJECT.optJSONArray("_embedded_entries");
        //_embedded_object = (JSONObject) _embedded_entries.get(0);
    }


    @Test
    public void test_embedded_default() throws IOException {

        final String ENTRY_BLOCK = "src/test/resources/multiple_objects.json";
        // JSONArray from the resource
        JSONArray rteArray = null;
        // Read file from resource by filename
        //JSONArray arrayResp = readJsonFile("response");
        JSONArray localJson = new ReadResource().readJson(ENTRY_BLOCK);
        // Read an object from the JSONArray
        JSONObject entryObject = (JSONObject) localJson.get(0);
        // Find the rich_text_editor available in the Object
        Attributes attributes = new ReadResource().returnEntryAttributes(entryObject);
        String result = defaultOptions.renderOptions(StyleType.BLOCK, entryObject, attributes);
        //Assert.assertEquals("<div><p>Alaukik assets</p><div><p>Content type: <span>alaukik_assets</span></p></div>", result);
        Assert.assertEquals("<div><p>this is unique title</p><div><p>Content type: <span></span></p></div>", result);
    }


    @Test
    public void test_embedded_default_inline() throws IOException {

        String ASSET_DISPLAYABLE = "src/test/resources/multiple_objects.json";
        JSONArray arrayResp = new ReadResource().readJson(ASSET_DISPLAYABLE);
        JSONObject entryObject = (JSONObject) arrayResp.get(0);
        Attributes attributes = new ReadResource().returnEntryAttributes(entryObject);
        String result = defaultOptions.renderOptions(StyleType.INLINE, entryObject, attributes);
        log.info(result);
        Assert.assertEquals("<span>this is unique title</span>", result);
    }


    @Test
    public void test_embedded_default_linked() throws IOException {

        String ASSET_DISPLAYABLE = "src/test/resources/multiple_objects.json";
        JSONArray arrayResp = new ReadResource().readJson(ASSET_DISPLAYABLE);
        JSONObject entryObject = (JSONObject) arrayResp.get(0);
        Attributes attributes = new ReadResource().returnEntryAttributes(entryObject);
        String result = defaultOptions.renderOptions(StyleType.LINKED, entryObject, attributes);
        log.info(result);
        Assert.assertEquals("<a href=\"/this-is-unique-title\">this is unique title</a>", result);
    }


    @Test
    public void test_embedded_default_displayable() throws IOException {

        String ASSET_DISPLAYABLE = "src/test/resources/multiple_objects.json";
        JSONArray arrayResp = new ReadResource().readJson(ASSET_DISPLAYABLE);
        JSONObject entryObject = (JSONObject) arrayResp.get(0);
        Attributes attributes = new ReadResource().returnAssetAttributes(entryObject);
        String result = defaultOptions.renderOptions(StyleType.DISPLAYABLE, entryObject, attributes);
        log.info(result);
        Assert.assertEquals("<img src=\"/this-is-unique-title\" alt=\"this is unique title\" />", result);
    }




}
