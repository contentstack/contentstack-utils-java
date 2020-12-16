import com.contentstack.utils.Utils;
import com.contentstack.utils.embedded.ItemType;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.logging.Level;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRender {

    private final static Logger logger = Logger.getLogger(TestRender.class.getName());
    private static JSONObject localJsonObj;

    @BeforeClass
    public static void startTestEmbedItemType() {
        logger.setLevel(Level.ALL);
        logger.info("Initiated TestRender testcases");
    }

    @Test
    public void testAvailableEntryItemTypes(){

    }
}
