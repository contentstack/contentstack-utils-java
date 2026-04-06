package com.contentstack.utils;

import com.contentstack.utils.render.DefaultOption;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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
    }

    @Test
    public void testRenderFunction() {
//        DefaultOption option = (embeddedObject, metadata) -> {
//            StyleType styleType = metadata.getStyleType();
//            return null;
//        };
        String[] keys = new String[2];
        keys[0] = "global_rich_multiple.group.rich_text_editor";
        keys[1] = "global_rich_multiple.group.rich_text_editor_multiple";
        Utils.render(localJsonObj, keys, new DefaultOption());
    }

//    @Test
//    public void testWithoutKeyPath() {
//        String[] keys = new String[2];
//        keys[0] = "global_rich_multiple.group.rich_text_editor";
//        keys[1] = "global_rich_multiple.group.rich_text_editor_multiple";
//        Utils.render(localJsonObj, null, new DefaultOption());
//    }
//
//    @Test
//    public void testEmbeddedBlockEntry() {
//        JSONArray rteArray = null;
//        boolean available = localJsonObj.has("rich_text_editor");
//        if (available) {
//            Object RTE = localJsonObj.get("rich_text_editor");
//            rteArray = ((JSONArray) RTE);
//        }
//        assert rteArray != null;
//        Utils.renderContents(rteArray, localJsonObj, (embeddedObject, metadata) -> {
//            StyleType type = metadata.getStyleType();
//            if (type == BLOCK) {
//                String title = embeddedObject.getString("title");
//                String multi_line = embeddedObject.getString("multi_line");
//                return "<p>" + title + "</p><span>" + multi_line + "</span>";
//            }
//            return null;
//        });
//
//        Utils.renderContents(rteArray, localJsonObj, (embeddedObject, metadata) -> {
//            if (metadata.getStyleType() == BLOCK) {
//                String title = embeddedObject.getString("title");
//                String multi_line = embeddedObject.getString("multi_line");
//                return "<p>" + title + "</p><span>" + multi_line + "</span>";
//            }
//            return null;
//        });
//    }
//
//    @Test
//    public void testEmbeddedInlineEntry() {
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
//                case LINK:
//                    // statements of LINK
//                    return null;
//
//                default:
//                    return null;
//            }
//        });
//    }

//    @Test
//    public void testEmbeddedLinkEntry() {
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
//                    String title = embeddedObject.getString("title");
//                    String multi_line = embeddedObject.getString("multi_line");
//                    return "<p>" + title + "</p><span>" + multi_line + "</span>";
//
//                case INLINE:
//                    // Statements of INLINE
//                    return null;
//
//                case LINK:
//                    // Statements of LINK
//                    return null;
//
//                default:
//                    return null;
//            }
//
//        });
//    }
//
//    @Test
//    public void testEmbeddedDisplayableAsset() {
//        JSONObject rteObject = null;
//        boolean available = localJsonObj.has("rich_text_editor");
//        if (available) {
//            Object RTE = localJsonObj.get("rich_text_editor");
//            rteObject = ((JSONObject) RTE);
//        }
//        assert rteObject != null;
//        String[] keyPath = {
//                "rich_text_editor", "global_rich_multiple.group.rich_text_editor"
//        };
//        Utils.renderContents(rteObject, keyPath, (embeddedObject, metadata) -> {
//            if (metadata.getStyleType == BLOCK) {
//                // Do something
//            }
//            return null;
//        });
//    }


    @Test
    public void testCustomJSONRTE() {
        JSONObject rteObject = new JSONObject();
        String[] keyPath = {
                "rich_text_editor", "global_rich_multiple.group.rich_text_editor"
        };
        Utils.jsonToHTML(rteObject, keyPath, new DefaultOptionClass());
    }
     
    @Test 
    public void testUpdateAssetUrl() throws IOException{
        final String json = "src/test/resources/file.json";
        JSONObject localJsonObj1 = new ReadResource().readJson(json);
        Utils.UpdateAssetURLForGQL(localJsonObj1);
        // System.out.println(localJsonObj1);
    }

    private static Set<String> jsonArrayToStringSet(JSONArray arr) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < arr.length(); i++) {
            set.add(arr.getString(i));
        }
        return set;
    }

    @Test
    public void testGetVariantAliasesSingleEntry() throws IOException {
        final String json = "src/test/resources/variant_entry_single.json";
        JSONObject full = new ReadResource().readJson(json);
        JSONObject entry = full.getJSONObject("entry");
        String contentTypeUid = "movie";

        JSONObject result = Utils.getVariantAliases(entry, contentTypeUid);

        Assert.assertTrue(result.has("entry_uid") && !result.getString("entry_uid").isEmpty());
        Assert.assertEquals(contentTypeUid, result.getString("contenttype_uid"));
        JSONArray variants = result.getJSONArray("variants");
        Assert.assertNotNull(variants);
        Set<String> aliasSet = jsonArrayToStringSet(variants);
        Assert.assertEquals(
                new HashSet<>(Arrays.asList("cs_personalize_0_0", "cs_personalize_0_3")),
                aliasSet);
    }

    @Test
    public void testGetVariantMetadataTagsSingleEntry() throws IOException {
        final String json = "src/test/resources/variant_entry_single.json";
        JSONObject full = new ReadResource().readJson(json);
        JSONObject entry = full.getJSONObject("entry");
        String contentTypeUid = "movie";

        JSONObject result = Utils.getVariantMetadataTags(entry, contentTypeUid);

        Assert.assertTrue(result.has("data-csvariants"));
        String dataCsvariantsStr = result.getString("data-csvariants");
        JSONArray arr = new JSONArray(dataCsvariantsStr);
        Assert.assertEquals(1, arr.length());
        JSONObject first = arr.getJSONObject(0);
        Assert.assertTrue(first.has("entry_uid") && !first.getString("entry_uid").isEmpty());
        Assert.assertEquals(contentTypeUid, first.getString("contenttype_uid"));
        Set<String> aliasSet = jsonArrayToStringSet(first.getJSONArray("variants"));
        Assert.assertEquals(
                new HashSet<>(Arrays.asList("cs_personalize_0_0", "cs_personalize_0_3")),
                aliasSet);
    }

    @Test
    public void testGetVariantAliasesMultipleEntries() throws IOException {
        final String json = "src/test/resources/variant_entries.json";
        JSONObject full = new ReadResource().readJson(json);
        JSONArray entries = full.getJSONArray("entries");
        String contentTypeUid = "movie";

        JSONArray result = Utils.getVariantAliases(entries, contentTypeUid);

        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.length());
        JSONObject first = result.getJSONObject(0);
        Assert.assertTrue(first.has("entry_uid") && !first.getString("entry_uid").isEmpty());
        Assert.assertEquals(contentTypeUid, first.getString("contenttype_uid"));
        Set<String> firstSet = jsonArrayToStringSet(first.getJSONArray("variants"));
        Assert.assertEquals(new HashSet<>(Arrays.asList("cs_personalize_0_0", "cs_personalize_0_3")), firstSet);
        JSONObject second = result.getJSONObject(1);
        Assert.assertTrue(second.has("entry_uid") && !second.getString("entry_uid").isEmpty());
        Assert.assertEquals(1, second.getJSONArray("variants").length());
        Assert.assertEquals("cs_personalize_0_0", second.getJSONArray("variants").getString(0));
        JSONObject third = result.getJSONObject(2);
        Assert.assertTrue(third.has("entry_uid") && !third.getString("entry_uid").isEmpty());
        Assert.assertEquals(0, third.getJSONArray("variants").length());
    }

    @Test
    public void testGetVariantMetadataTagsMultipleEntries() throws IOException {
        final String json = "src/test/resources/variant_entries.json";
        JSONObject full = new ReadResource().readJson(json);
        JSONArray entries = full.getJSONArray("entries");
        String contentTypeUid = "movie";

        JSONObject result = Utils.getVariantMetadataTags(entries, contentTypeUid);

        Assert.assertTrue(result.has("data-csvariants"));
        String dataCsvariantsStr = result.getString("data-csvariants");
        JSONArray arr = new JSONArray(dataCsvariantsStr);
        Assert.assertEquals(3, arr.length());
        Assert.assertTrue(arr.getJSONObject(0).has("entry_uid") && !arr.getJSONObject(0).getString("entry_uid").isEmpty());
        Assert.assertEquals(2, arr.getJSONObject(0).getJSONArray("variants").length());
        Assert.assertTrue(arr.getJSONObject(1).has("entry_uid") && !arr.getJSONObject(1).getString("entry_uid").isEmpty());
        Assert.assertEquals(1, arr.getJSONObject(1).getJSONArray("variants").length());
        Assert.assertTrue(arr.getJSONObject(2).has("entry_uid") && !arr.getJSONObject(2).getString("entry_uid").isEmpty());
        Assert.assertEquals(0, arr.getJSONObject(2).getJSONArray("variants").length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVariantAliasesThrowsWhenEntryNull() {
        Utils.getVariantAliases((JSONObject) null, "landing_page");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVariantAliasesThrowsWhenContentTypeUidNull() throws IOException {
        final String json = "src/test/resources/variant_entry_single.json";
        JSONObject full = new ReadResource().readJson(json);
        JSONObject entry = full.getJSONObject("entry");
        Utils.getVariantAliases(entry, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVariantAliasesThrowsWhenContentTypeUidEmpty() throws IOException {
        final String json = "src/test/resources/variant_entry_single.json";
        JSONObject full = new ReadResource().readJson(json);
        JSONObject entry = full.getJSONObject("entry");
        Utils.getVariantAliases(entry, "");
    }

    @Test
    public void testGetVariantMetadataTagsWhenEntryNull() {
        JSONObject result = Utils.getVariantMetadataTags((JSONObject) null, "landing_page");
        Assert.assertTrue(result.has("data-csvariants"));
        Assert.assertEquals("[]", result.getString("data-csvariants"));
    }

    @Test
    public void testGetDataCsvariantsAttributeDelegatesToGetVariantMetadataTags() throws IOException {
        final String json = "src/test/resources/variant_entry_single.json";
        JSONObject full = new ReadResource().readJson(json);
        JSONObject entry = full.getJSONObject("entry");
        String contentTypeUid = "movie";
        JSONObject fromNew = Utils.getVariantMetadataTags(entry, contentTypeUid);
        @SuppressWarnings("deprecation")
        JSONObject fromOld = Utils.getDataCsvariantsAttribute(entry, contentTypeUid);
        Assert.assertEquals(fromNew.toString(), fromOld.toString());
    }

}



