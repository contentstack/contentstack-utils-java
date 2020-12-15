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

    final static Logger logger = Logger.getLogger(UtilTests.class.getName());
    private static JSONObject localJsonObj;

    @BeforeClass
    public static void startUtilTests() throws IOException {
        logger.setLevel(Level.INFO);
        final String EMBEDDED_ITEMS = "src/test/resources/multiple_rich_text_content.json";
        localJsonObj = new ReadResource().readJson(EMBEDDED_ITEMS);
        localJsonObj = (JSONObject) localJsonObj.getJSONArray("entries").get(0);
        logger.info("Reading Local Json Object"+localJsonObj);
    }

    @Test
    public void test_01_renderFunction() {
        Options options = new Options() {
            @Override
            public String renderOptions(JSONObject embeddedObject, Metadata metadata) {
                StyleType styleType = metadata.getStyleType();
                return null;
            }
        };
        String[] keys = new String[2];
        keys[0] = "global_rich_multiple.group.rich_text_editor";
        keys[1] = "global_rich_multiple.group.rich_text_editor_multiple";
        Utils.render(localJsonObj, keys, new DefaultOptions());
        //logger.info("localJson:"+localJsonObj);
    }

    @Test
    public void test_02_EmbeddedBlockEntry() {
        JSONArray rteArray = null;
        // Find the rich_text_editor available in the Object
        boolean available = localJsonObj.has("rich_text_editor");
        if (available) {
            Object RTE = localJsonObj.get("rich_text_editor");
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


//    @Test
//    public void test_2_embedded_inline_entry() {
//        JSONArray rteArray = null;
//        boolean available = localJsonObj.has("rich_text_editor");
//        if (available) {
//            Object RTE = localJsonObj.get("rich_text_editor");
//            rteArray = ((JSONArray) RTE);
//        }
//        assert rteArray != null;
//        Utils.renderContents(rteArray, localJsonObj, (embeddedObject, metadata) -> {
//            switch (metadata.getStyleType()) {
//                case BLOCK:
//                    // statements of BLOCK
//                    String title = embeddedObject.getString("title");
//                    String multi_line = embeddedObject.getString("multi_line");
//                    return "<p>" + title + "</p><span>" + multi_line + "</span>";
//                case INLINE:
//                    // statements of INLINE
//                    return null;
//
//                case LINKED:
//                    // statements of LINKED
//                    return null;
//
//                default:
//                    return null;
//            }
//        });
//    }
//
//
//    @Test
//    public void test_3_embedded_linked_entry() {
//        JSONArray rteArray = null;
//        boolean available = localJsonObj.has("rich_text_editor");
//        if (available) {
//            Object RTE = localJsonObj.get("rich_text_editor");
//            rteArray = ((JSONArray) RTE);
//            //System.out.println(rteArray);
//        }
//        assert rteArray != null;
//        Utils.renderContents(rteArray, localJsonObj, (embeddedObject, metadata) -> {
//            switch (metadata.getStyleType()) {
//                case BLOCK:
//                    // statements of BLOCK
//                    //blockRTE();
//                    String title = embeddedObject.getString("title");
//                    String multi_line = embeddedObject.getString("multi_line");
//                    return "<p>" + title + "</p><span>" + multi_line + "</span>";
//
//                case INLINE:
//                    // statements of INLINE
//                    return null;
//
//                case LINKED:
//                    // statements of LINKED
//                    return null;
//
//                default:
//                    return null;
//            }
//
//        });
//    }
//
//
//    @Test
//    public void test_embedded_displayable_asset() {
//        JSONArray rteArray = null;
//        boolean available = localJsonObj.has("rich_text_editor");
//        if (available) {
//            Object RTE = localJsonObj.get("rich_text_editor");
//            rteArray = ((JSONArray) RTE);
//        }
//        assert rteArray != null;
//        Utils.renderContents(rteArray, localJsonObj, (embeddedObject, metadata) -> {
//            if (metadata.getStyleType() == StyleType.DISPLAY) {// statements of displayable
//                return null;
//            }
//            return null;
//        });
//    }
//
//    @Test
//    public void justTest(){
//        //String[] blankArray = new String[0];
//        //new Utils().render({}, null, eck);
//    }

}
