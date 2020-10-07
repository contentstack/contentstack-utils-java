import com.contentstack.utils.helper.EmbeddedObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmbeddedModelTests {

    private static final Logger logger = Logger.getLogger(UtilTests.class.getName());
    private static JSONArray rteArray = null;

    @BeforeClass
    public static void executeOnceBeforeTestStarts() throws IOException {
        // Setting level for the logging.
        final String ENTRY_BLOCK = "src/test/resources/response.json";
        // Read file from resource by filename
        //JSONArray arrayResp = readJsonFile("response");
        JSONArray localJson = new ReadResource().readJson(ENTRY_BLOCK);
        // Read an object from the JSONArray
        JSONObject entryObject = (JSONObject) localJson.get(0);
        // Find the rich_text_editor available in the Object
        boolean available = entryObject.has("rich_text_editor");
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }
    }


    @Test
    public void test_embedded_object_model() {

        Document html = Jsoup.parse(rteArray.toString());
        Elements embeddedEntries = html.body().getElementsByClass("embedded-entry");
        embeddedEntries.forEach((entry) -> {
            String type = entry.attr("type");
            String uid = entry.attr("data-sys-entry-uid");
            String contentType = entry.attr("data-sys-content-type-uid");
            String style = entry.attr("sys-style-type");
            String outerHTML = entry.outerHtml();
            EmbeddedObject embeddedEntry = new EmbeddedObject(type, uid, contentType, style, outerHTML, entry.attributes());
            logger.info(embeddedEntry.toString());
            Assert.assertEquals("", outerHTML);
        });

    }


}
