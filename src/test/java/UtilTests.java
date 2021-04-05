import com.contentstack.utils.Utils;
import com.contentstack.utils.callbacks.Option;
import com.contentstack.utils.embedded.StyleType;
import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.render.DefaultOption;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
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
        Option option = (embeddedObject, metadata) -> {
            StyleType styleType = metadata.getStyleType();
            return null;
        };
        String[] keys = new String[2];
        keys[0] = "global_rich_multiple.group.rich_text_editor";
        keys[1] = "global_rich_multiple.group.rich_text_editor_multiple";
        Utils.render(localJsonObj, keys, new DefaultOption());
    }

    @Test
    public void test_02_WithoutKeyPath() {
        Option option = (embeddedObject, metadata) -> {
            StyleType styleType = metadata.getStyleType();
            return null;
        };
        String[] keys = new String[2];
        keys[0] = "global_rich_multiple.group.rich_text_editor";
        keys[1] = "global_rich_multiple.group.rich_text_editor_multiple";
        Utils.render(localJsonObj, null, new DefaultOption());
    }

    @Test
    public void test_03_EmbeddedBlockEntry() {
        JSONArray rteArray = null;
        boolean available = localJsonObj.has("rich_text_editor");
        if (available) {
            Object RTE = localJsonObj.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }
        assert rteArray != null;
        Utils.renderContents(rteArray, localJsonObj, (embeddedObject, metadata) -> {
            StyleType type = metadata.getStyleType();
            if (type == StyleType.BLOCK) {
                String title = embeddedObject.getString("title");
                String multi_line = embeddedObject.getString("multi_line");
                return "<p>" + title + "</p><span>" + multi_line + "</span>";
            }
            return null;
        });

        Utils.renderContents(rteArray, localJsonObj, (embeddedObject, metadata) -> {
            if (metadata.getStyleType() == StyleType.BLOCK) {
                String title = embeddedObject.getString("title");
                String multi_line = embeddedObject.getString("multi_line");
                return "<p>" + title + "</p><span>" + multi_line + "</span>";
            }
            return null;
        });
    }


    @Test
    public void test_2_embedded_inline_entry() {
        JSONArray rteArray = null;
        boolean available = localJsonObj.has("rich_text_editor");
        if (available) {
            Object RTE = localJsonObj.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }
        assert rteArray != null;
        Utils.renderContents(rteArray, localJsonObj, (embeddedObject, metadata) -> {
            switch (metadata.getStyleType()) {
                case BLOCK:
                    // statements of BLOCK
                    String title = embeddedObject.getString("title");
                    String multi_line = embeddedObject.getString("multi_line");
                    return "<p>" + title + "</p><span>" + multi_line + "</span>";
                case INLINE:
                    // statements of INLINE
                    return null;

                case LINK:
                    // statements of LINKED
                    return null;

                default:
                    return null;
            }
        });
    }


    @Test
    public void test_3_embedded_linked_entry() {
        JSONArray rteArray = null;
        boolean available = localJsonObj.has("rich_text_editor");
        if (available) {
            Object RTE = localJsonObj.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }
        assert rteArray != null;
        Utils.renderContents(rteArray, localJsonObj, (embeddedObject, metadata) -> {
            switch (metadata.getStyleType()) {
                case BLOCK:
                    //Statements of BLOCK
                    //blockRTE();
                    String title = embeddedObject.getString("title");
                    String multi_line = embeddedObject.getString("multi_line");
                    return "<p>" + title + "</p><span>" + multi_line + "</span>";

                case INLINE:
                    // Statements of INLINE
                    return null;

                case LINK:
                    // Statements of LINKED
                    return null;

                default:
                    return null;
            }

        });
    }


    @Test
    public void test_embedded_displayable_asset() {
        JSONObject rteObject = null;
        boolean available = localJsonObj.has("rich_text_editor");
        if (available) {
            Object RTE = localJsonObj.get("rich_text_editor");
            rteObject = ((JSONObject) RTE);
        }
        assert rteObject != null;
        String[] keyPath = {
                "rich_text_editor", "global_rich_multiple.group.rich_text_editor"
        };
        Utils.render(rteObject, keyPath, new Option() {
            @Override
            public String renderOptions(JSONObject embeddedObject, Metadata metadata) {
                if (metadata.getStyleType()==StyleType.BLOCK){
                    // Do something
                }
                return null;
            }
        });
    }


}
