package com.contentstack.utils;

import com.contentstack.utils.helper.Metadata;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

@FixMethodOrder
public class TestMetadata {

    private final static Logger logger = Logger.getLogger(TestEmbedItemType.class.getName());
    private static Metadata metadata;

    @BeforeClass
    public static void startTestEmbedItemType() {
        logger.setLevel(Level.ALL);
        logger.info("Initiated com.contentstack.utils.TestMetadata");
        metadata = new Metadata("SomeTextAsString", "entry",
                "uid98483478734", "contentTypeUid", "block",
                "stringOuterHTML", new Attributes());
    }

    @Test
    public void testMetadataText() {
        Assert.assertEquals("SomeTextAsString", metadata.getText());
    }

    @Test
    public void testMetadataItemType() {
        Assert.assertEquals("entry", metadata.getItemType());
    }

    @Test
    public void testMetadataItemUid() {
        Assert.assertEquals("uid98483478734", metadata.getItemUid());
    }

    @Test
    public void testMetadataContentTypeUid() {
        Assert.assertEquals("contentTypeUid", metadata.getContentTypeUid());
    }

    @Test
    public void testMetadataStyleType() {
        Assert.assertEquals("block", metadata.getStyleType().name().toLowerCase());
    }

    @Test
    public void testMetadataOuterHtml() {
        Assert.assertEquals("stringOuterHTML", metadata.getOuterHTML());
    }

    @Test
    public void testMetadataToString() {
        Assert.assertEquals(
                "EmbeddedObject{text='SomeTextAsString'type='entry', uid='uid98483478734', contentTypeUid='contentTypeUid', sysStyleType=BLOCK, outerHTML='stringOuterHTML', attributes=''}",
                metadata.toString());
    }

    @Test
    public void testMetadataGetAttribute() {
        Assert.assertEquals("", metadata.getAttributes().html().toLowerCase());
    }
}
