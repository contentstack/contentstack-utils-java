import com.contentstack.utils.embedded.ItemType;
import com.contentstack.utils.embedded.StyleType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

@FixMethodOrder
public class TestEmbedStyleType {

    private final static Logger logger = Logger.getLogger(TestEmbedStyleType.class.getName());

    @BeforeClass
    public static void startTestEmbedItemType() {
        logger.setLevel(Level.ALL);
        logger.info("Initiated Test Embed Style Type testcases");
    }

    @Test
    public void testAvailableStyleTypeBlock(){
        StyleType styleType = StyleType.BLOCK;
        Assert.assertEquals("BLOCK", styleType.name());
    }

    @Test
    public void testAvailableStyleTypeInline(){
        StyleType styleType = StyleType.INLINE;
        Assert.assertEquals("INLINE", styleType.name());
    }

    @Test
    public void testAvailableStyleTypeLink(){
        StyleType styleType = StyleType.LINK;
        Assert.assertEquals("LINK", styleType.name());
    }

    @Test
    public void testAvailableStyleTypeDisplay(){
        StyleType styleType = StyleType.DISPLAY;
        Assert.assertEquals("DISPLAY", styleType.name());
    }

}
