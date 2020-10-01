import com.contentstack.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UtilTestcases {

    static final Logger log = Logger.getLogger(UtilTestcases.class.getName());


//    String blockEntry = "<div class=\"embedded-entry block-entry\" type=\"entry\" data-sys-entry-uid=\"blt55f6d8cbd7e03a1f\" data-sys-content-type-uid=\"article\" sys-style-type = \"block\"> \n" +
//            "<span>{{title}}</span>\n" +
//            "</div>";
//
//    String inlineEntry = "<span class=\"embedded-entry inline-entry\" type=\"entry\" data-sys-entry-uid=\"blt55f6d8cbd7e03a1f\" data-sys-content-type-uid=\"article\" style=\"display:inline;\" sys-style-type = \"inline\"> \n" +
//            "<data data-sys-field-uid=\"title\">{{title}}</data>\n" +
//            "</span>";
//
//    String linkedEntry = "<span class=\"embedded-entry linked-entry\" type=\"entry\" data-sys-entry-uid=\"blt55f6d8cbd7e03a1f\" data-sys-content-type-uid=\"article\" style=\"display:inline;\" sys-style-type = \"link\"> \n" +
//            "  <a data-sys-field-uid=\"title\" href=\"{{url}}\">{{title}}</a>\n" +
//            "</span>";
//
//    String downloadableAsset = "<span class=\"embedded-entry linked-entry\" type=\"entry\" data-sys-entry-uid=\"blt55f6d8cbd7e03a1f\" data-sys-content-type-uid=\"article\" style=\"display:inline;\" sys-style-type = \"link\"> \n" +
//            "  <a data-sys-field-uid=\"title\" href=\"{{url}}\">{{title}}</a>\n" +
//            "</span>";
//
//    String displayableAsset = "<span class=\"embedded-entry linked-entry\" type=\"entry\" data-sys-entry-uid=\"blt55f6d8cbd7e03a1f\" data-sys-content-type-uid=\"article\" style=\"display:inline;\" sys-style-type = \"link\"> \n" +
//            "  <a data-sys-field-uid=\"title\" href=\"{{url}}\">{{title}}</a>\n" +
//            "</span>";

    /**
     * Executed once, before the start of all tests. It is used to perform time
     * intensive activities, for example, to connect to a database. Methods marked
     * with this annotation need to be defined as static to work with JUnit.
     */
    @BeforeClass
    public static void executeOnceBeforeTestStarts() {
        log.setLevel(Level.ALL);
        log.info("execute Once Before Test Starts");
    }

    /**
     * Executed once, after all tests have been finished. It is used to
     * perform clean-up activities, for example, to disconnect from a database.
     * Methods annotated with this annotation need to be defined as static to work with JUnit.
     */
    @AfterClass
    public static void executeAfterAllTestFinishes() {
        // one-time cleanup code
        log.info("execute After All Test Finishes");
    }

    /**
     * Executed before each test. It is used to prepare the test environment
     * (e.g., read input data, initialize the class).
     */
    @Before
    public void executeBeforeEachTest() {
        log.info("execute Before Each Test");
    }


    /**
     * Executed after each test. It is used to cleanup the test environment
     * (e.g., delete temporary data, restore defaults).
     * It can also save memory by cleaning up expensive memory structures
     */
    @After
    public void ExecuteAfterEachTest() {
        log.info("Execute After Each Test.");
    }


    @Test
    public void test_1_embedded_block_entry() throws IOException {
        final String ENTRY_BLOCK = "src/test/resources/response.json";

        // JSONArray from the resource
        JSONArray rteArray = null;
        // Read file from resource by filename
        //JSONArray arrayResp = readJsonFile("response");
        JSONArray localJson = new ResourceFile().readJson(ENTRY_BLOCK);
        // Read an object from the JSONArray
        JSONObject entryObject = (JSONObject) localJson.get(0);
        // Find the rich_text_editor available in the Object
        boolean available = entryObject.has("rich_text_editor");
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }
        assert rteArray != null;
        Utils.renderContents(rteArray, entryObject, (type, embeddedObject) -> {
            switch (type) {
                case BLOCK:
                    String title = embeddedObject.getString("title");
                    String multi_line = embeddedObject.getString("multi_line");
                    return "<p>" + title + "</p><span>" + multi_line + "</span>";
                default:
                    return null;
            }
        });
    }


    @Test
    public void test_2_embedded_inline_entry() throws IOException {
        final String ENTRY_INLINE = "src/test/resources/entry_inline.json";

        JSONArray rteArray = null;
        JSONArray arrayResp = new ResourceFile().readJson(ENTRY_INLINE);
        JSONObject entryObject = (JSONObject) arrayResp.get(0);
        boolean available = entryObject.has("rich_text_editor");
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }

        assert rteArray != null;
        Utils.renderContents(rteArray, entryObject, (type, embeddedObject) -> {
            switch (type) {
                case BLOCK:
                    // statements of BLOCK
                    String title = embeddedObject.getString("title");
                    String multi_line = embeddedObject.getString("multi_line");
                    return "<p>" + title + "</p><span>" + multi_line + "</span>";
                case INLINE:
                    // statements of INLINE
                    return null;

                case LINKED:
                    // statements of LINKED
                    return null;

                default:
                    return null;
            }
        });
    }


    @Test
    public void test_3_embedded_linked_entry() throws IOException {
        final String ENTRY_LINKED = "src/test/resources/entry_linked.json";

        JSONArray rteArray = null;
        JSONArray arrayResp = new ResourceFile().readJson(ENTRY_LINKED);
        JSONObject entryObject = (JSONObject) arrayResp.get(0);
        boolean available = entryObject.has("rich_text_editor");
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
            //System.out.println(rteArray);
        }

        assert rteArray != null;
        Utils.renderContents(rteArray, entryObject, (type, embeddedObject) -> {
            switch (type) {
                case BLOCK:
                    // statements of BLOCK
                    //blockRTE();
                    String title = embeddedObject.getString("title");
                    String multi_line = embeddedObject.getString("multi_line");
                    return "<p>" + title + "</p><span>" + multi_line + "</span>";

                case INLINE:
                    // statements of INLINE
                    return null;

                case LINKED:
                    // statements of LINKED
                    return null;

                default:
                    return null;
            }

        });
    }


    @Test
    public void test_4_embedded_downloadable_asset() throws IOException {
        String ASSET_DOWNLOADABLE = "src/test/resources/asset_downloadable.json";

        JSONArray rteArray = null;
        JSONArray arrayResp = new ResourceFile().readJson(ASSET_DOWNLOADABLE);
        JSONObject entryObject = (JSONObject) arrayResp.get(0);
        boolean available = entryObject.has("rich_text_editor");
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
            //System.out.println(rteArray);
        }

        assert rteArray != null;
        Utils.renderContents(rteArray, entryObject, (type, embeddedObject) -> {

            switch (type) {

                case DOWNLOADABLE:
                    // statements of LINKED
                    // linkedRTE();
                    return null;

                default:
                    return null;
            }

        });
    }


    @Test
    public void test_embedded_displayable_asset() throws IOException {
        String ASSET_DISPLAYABLE = "src/test/resources/asset_displayable.json";
        JSONArray rteArray = null;
        JSONArray arrayResp = new ResourceFile().readJson(ASSET_DISPLAYABLE);
        JSONObject entryObject = (JSONObject) arrayResp.get(0);
        boolean available = entryObject.has("rich_text_editor");
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }

        assert rteArray != null;
        Utils.renderContents(rteArray, entryObject, (type, embeddedObject) -> {
            switch (type) {
                case DISPLAYABLE:
                    // statements of displayable
                    return null;
                default:
                    return null;
            }
        });


    }
}
