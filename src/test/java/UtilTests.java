import com.contentstack.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runners.MethodSorters;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UtilTests {


    static final Logger log = Logger.getLogger(UtilTests.class.getName());


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
        JSONArray localJson = new ReadResource().readJson(ENTRY_BLOCK);
        // Read an object from the JSONArray
        JSONObject entryObject = (JSONObject) localJson.get(0);
        // Find the rich_text_editor available in the Object
        boolean available = entryObject.has("rich_text_editor");
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }
        assert rteArray != null;
        Utils.renderContents(rteArray, entryObject, (type, embeddedObject, linkText) -> {
            switch (type) {
                case BLOCK:
                    String title = embeddedObject.getString("title");
                    String multi_line = embeddedObject.getString("multi_line");
                    return "<p>"+title+"</p><span>"+multi_line+"</span>";
//                case INLINE:
//                    String titleInline = embeddedObject.getString("title");
//                    String mlInline = embeddedObject.getString("multi_line");
//                    return "<p>" + titleInline + "</p><span>" + mlInline + "</span>";
//                case LINKED:
//                    String titleLinked = embeddedObject.getString("title");
//                    String mlLinked = embeddedObject.getString("multi_line");
//                    return "<p>" + titleLinked + "</p><span>" + mlLinked + "</span>";
//                case DISPLAYABLE:
//                    String titleDiplayable = embeddedObject.getString("title");
//                    String mlDiplayable = embeddedObject.getString("multi_line");
//                    return "<p>" + titleDiplayable + "</p><span>" + mlDiplayable + "</span>";
                default:
                    return null;
            }
        });
    }


    @Test
    public void test_2_embedded_inline_entry() throws IOException {
        final String ENTRY_INLINE = "src/test/resources/entry_inline.json";

        JSONArray rteArray = null;
        JSONArray arrayResp = new ReadResource().readJson(ENTRY_INLINE);
        JSONObject entryObject = (JSONObject) arrayResp.get(0);
        boolean available = entryObject.has("rich_text_editor");
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }

        assert rteArray != null;
        Utils.renderContents(rteArray, entryObject, (type, embeddedObject, linkText) -> {
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
        JSONArray arrayResp = new ReadResource().readJson(ENTRY_LINKED);
        JSONObject entryObject = (JSONObject) arrayResp.get(0);
        boolean available = entryObject.has("rich_text_editor");
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
            //System.out.println(rteArray);
        }

        assert rteArray != null;
        Utils.renderContents(rteArray, entryObject, (type, embeddedObject, linkText) -> {
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


//    @Test
//    public void test_4_embedded_downloadable_asset() throws IOException {
//        String ASSET_DOWNLOADABLE = "src/test/resources/multiple_objects.json";
//        JSONArray rteArray = null;
//        JSONArray arrayResp = new ResourceFile().readJson(ASSET_DOWNLOADABLE);
//        JSONObject entryObject = (JSONObject) arrayResp.get(0);
//        boolean available = entryObject.has("rich_text_editor");
//        if (available) {
//            Object RTE = entryObject.get("rich_text_editor");
//            rteArray = ((JSONArray) RTE);
//            //System.out.println(rteArray);
//        }
//
//        assert rteArray != null;
//        Utils.renderContents(rteArray, entryObject, (type, embeddedObject, linkText) -> {
//
//            switch (type) {
//
//                case DOWNLOADABLE:
//                    // statements of LINKED
//                    String title = embeddedObject.getString("title");
//                    String multi_line = embeddedObject.getString("multi_line");
//                    return "<p>" + title + "</p><span>" + multi_line + "</span>";
//
//                default:
//                    return null;
//            }
//
//        });
//    }


    @Test
    public void test_embedded_displayable_asset() throws IOException {
        String ASSET_DISPLAYABLE = "src/test/resources/asset_displayable.json";
        JSONArray rteArray = null;
        JSONArray arrayResp = new ReadResource().readJson(ASSET_DISPLAYABLE);
        JSONObject entryObject = (JSONObject) arrayResp.get(0);
        boolean available = entryObject.has("rich_text_editor");
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }

        assert rteArray != null;
        Utils.renderContents(rteArray, entryObject, (type, embeddedObject, linkText) -> {
            switch (type) {
                case DISPLAY:
                    // statements of displayable
                    return null;
                default:
                    return null;
            }
        });
    }


}
