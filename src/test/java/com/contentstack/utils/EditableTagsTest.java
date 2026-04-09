package com.contentstack.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Live Preview editable tags — parity with contentstack-utils-javascript {@code entry-editable.ts}.
 */
public class EditableTagsTest {

    @Test
    public void getTagReturnsEmptyForNullContent() {
        JSONObject tags = Utils.getTag(null, "a.b.c", false, "en-us",
                new EditableTags.AppliedVariantsState(null, false, ""));
        Assert.assertNotNull(tags);
        Assert.assertEquals(0, tags.length());
    }

    @Test
    public void addEditableTagsPrimitivesAsStrings() {
        JSONObject entry = new JSONObject();
        entry.put("uid", "entry1");
        entry.put("title", "Hello");
        entry.put("count", 42);
        Utils.addEditableTags(entry, "Blog", false, "en-us", null);
        JSONObject dollar = entry.getJSONObject("$");
        Assert.assertEquals("data-cslp=blog.entry1.en-us.title", dollar.getString("title"));
        Assert.assertEquals("data-cslp=blog.entry1.en-us.count", dollar.getString("count"));
    }

    @Test
    public void addEditableTagsPrimitivesAsObjects() {
        JSONObject entry = new JSONObject();
        entry.put("uid", "e1");
        entry.put("title", "Hi");
        Utils.addEditableTags(entry, "Post", true, "en-us", null);
        JSONObject cslp = entry.getJSONObject("$").getJSONObject("title");
        Assert.assertEquals("post.e1.en-us.title", cslp.getString("data-cslp"));
    }

    @Test
    public void contentTypeUidLowercasedAndLocaleLowercasedByDefault() {
        JSONObject entry = new JSONObject();
        entry.put("uid", "u1");
        entry.put("title", "x");
        Utils.addEditableTags(entry, "LANDING", false, "EN-US", null);
        Assert.assertTrue(entry.getJSONObject("$").getString("title").contains(".en-us."));
    }

    @Test
    public void useLowerCaseLocaleFalsePreservesLocaleCasing() {
        JSONObject entry = new JSONObject();
        entry.put("uid", "u1");
        entry.put("title", "x");
        EditableTagsOptions opt = new EditableTagsOptions().setUseLowerCaseLocale(false);
        Utils.addEditableTags(entry, "ct", false, "EN-US", opt);
        Assert.assertTrue(entry.getJSONObject("$").getString("title").contains(".EN-US."));
    }

    @Test
    public void nestedObjectGetsChildDollarMap() {
        JSONObject inner = new JSONObject();
        inner.put("name", "inner");
        JSONObject entry = new JSONObject();
        entry.put("uid", "e1");
        entry.put("group", inner);
        Utils.addEditableTags(entry, "ct", false, "en-us", null);
        JSONObject groupDollar = inner.getJSONObject("$");
        Assert.assertEquals("data-cslp=ct.e1.en-us.group.name", groupDollar.getString("name"));
        Assert.assertEquals("data-cslp=ct.e1.en-us.group", entry.getJSONObject("$").getString("group"));
    }

    @Test
    public void arrayFieldIndexAndParentTags() {
        JSONObject entry = new JSONObject();
        entry.put("uid", "e1");
        entry.put("items", new JSONArray().put("a").put("b"));
        Utils.addEditableTags(entry, "ct", false, "en-us", null);
        JSONObject dollar = entry.getJSONObject("$");
        Assert.assertEquals("data-cslp=ct.e1.en-us.items.0", dollar.getString("items__0"));
        Assert.assertEquals("data-cslp=ct.e1.en-us.items.1", dollar.getString("items__1"));
        Assert.assertEquals("data-cslp-parent-field=ct.e1.en-us.items", dollar.getString("items__parent"));
        Assert.assertEquals("data-cslp=ct.e1.en-us.items", dollar.getString("items"));
    }

    @Test
    public void referenceInArrayUsesRefPrefix() {
        JSONObject ref = new JSONObject();
        ref.put("_content_type_uid", "author_ct");
        ref.put("uid", "refuid");
        ref.put("title", "Author");
        JSONObject entry = new JSONObject();
        entry.put("uid", "e1");
        entry.put("authors", new JSONArray().put(ref));
        Utils.addEditableTags(entry, "post", false, "en-us", null);
        JSONObject refDollar = ref.getJSONObject("$");
        Assert.assertEquals("data-cslp=author_ct.refuid.en-us.title", refDollar.getString("title"));
    }

    @Test
    public void variantDirectFieldAppendsVariantToUidSegment() {
        JSONObject applied = new JSONObject();
        applied.put("title", "varA");
        JSONObject entry = new JSONObject();
        entry.put("uid", "eu1");
        entry.put("_applied_variants", applied);
        entry.put("title", "T");
        Utils.addEditableTags(entry, "blog", false, "en-us", null);
        String tag = entry.getJSONObject("$").getString("title");
        Assert.assertTrue(tag.startsWith("data-cslp=v2:"));
        Assert.assertTrue(tag.contains("blog.eu1_varA.en-us.title"));
    }

    @Test
    public void appliedVariantsFromSystem() {
        JSONObject applied = new JSONObject();
        applied.put("field1", "v1");
        JSONObject system = new JSONObject();
        system.put("applied_variants", applied);
        JSONObject entry = new JSONObject();
        entry.put("uid", "u1");
        entry.put("system", system);
        entry.put("field1", "x");
        Utils.addEditableTags(entry, "ct", false, "en-us", null);
        String tag = entry.getJSONObject("$").getString("field1");
        Assert.assertTrue(tag.contains("v2:"));
        Assert.assertTrue(tag.contains("u1_v1"));
    }

    @Test
    public void parentVariantisedPathInheritance() {
        JSONObject applied = new JSONObject();
        applied.put("parent", "pv");
        JSONObject entry = new JSONObject();
        entry.put("uid", "e1");
        entry.put("_applied_variants", applied);
        entry.put("parent", new JSONObject().put("child", "val"));
        Utils.addEditableTags(entry, "ct", false, "en-us", null);
        JSONObject parentObj = entry.getJSONObject("parent");
        String childTag = parentObj.getJSONObject("$").getString("child");
        Assert.assertTrue(childTag.startsWith("data-cslp=v2:"));
        Assert.assertTrue(childTag.contains("e1_pv"));
    }

    @Test
    public void addTagsAliasMatchesAddEditableTags() {
        JSONObject a = new JSONObject();
        a.put("uid", "1");
        a.put("t", "x");
        JSONObject b = new JSONObject();
        b.put("uid", "1");
        b.put("t", "x");
        Utils.addEditableTags(a, "c", false, "en-us", null);
        Utils.addTags(b, "c", false, "en-us", null);
        Assert.assertEquals(a.getJSONObject("$").toString(), b.getJSONObject("$").toString());
    }
}
