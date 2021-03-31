import com.contentstack.utils.embedded.ItemType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.logging.Level;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestEmbedItemType {

    private final static Logger logger = Logger.getLogger(TestEmbedItemType.class.getName());

    @BeforeClass
    public static void startTestEmbedItemType() {
        logger.setLevel(Level.ALL);
        logger.info("Initiated TestEmbed testcases");
    }

    @Test
    public void testAvailableEntryItemTypes(){
        ItemType itemType = ItemType.ENTRY;
        Assert.assertEquals("ENTRY", itemType.name());
    }

    @Test
    public void testAvailableAssetItemTypes(){
        ItemType itemType = ItemType.ASSET;
        Assert.assertEquals("ASSET", itemType.name());
    }

}
