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

    /**
     * Parent field tag for an array must use the field-level metakey (for variant resolution), not the last
     * element's {@code _metadata.uid} suffix — otherwise a per-element variant key (e.g. {@code items.uidB})
     * would incorrectly win for the parent {@code items} tag.
     */
    @Test
    public void arrayFieldParentTagUsesFieldMetakeyNotLastElementMetadata() {
        JSONObject applied = new JSONObject();
        applied.put("items", "fieldVar");
        applied.put("items.uidB", "wrongVar");
        JSONObject metaA = new JSONObject();
        metaA.put("uid", "uidA");
        JSONObject metaB = new JSONObject();
        metaB.put("uid", "uidB");
        JSONObject el0 = new JSONObject();
        el0.put("_metadata", metaA);
        el0.put("x", "a");
        JSONObject el1 = new JSONObject();
        el1.put("_metadata", metaB);
        el1.put("x", "b");
        JSONArray arr = new JSONArray().put(el0).put(el1);
        JSONObject entry = new JSONObject();
        entry.put("uid", "e1");
        entry.put("_applied_variants", applied);
        entry.put("items", arr);
        Utils.addEditableTags(entry, "ct", false, "en-us", null);
        String parentItemsTag = entry.getJSONObject("$").getString("items");
        Assert.assertTrue("parent field should resolve variant via key \"items\"",
                parentItemsTag.contains("ct.e1_fieldVar.en-us.items"));
        Assert.assertFalse("parent field must not apply last element's variant (items.uidB -> wrongVar)",
                parentItemsTag.contains("e1_wrongVar"));
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

    /**
     * Referenced entry may declare its own {@code locale}; recursive {@code getTag} must receive {@code refLocale}
     * (not the parent entry locale) so nested plain objects use the correct path segment, and nested refs in arrays
     * without {@code locale} fall back to that reference locale (not the top-level entry locale).
     */
    @Test
    public void referenceInArrayPassesRefLocaleToNestedGetTag() {
        JSONObject nested = new JSONObject();
        nested.put("name", "Nested");
        JSONObject subRef = new JSONObject();
        subRef.put("_content_type_uid", "child_ct");
        subRef.put("uid", "c1");
        subRef.put("x", "v");
        JSONObject ref = new JSONObject();
        ref.put("_content_type_uid", "author_ct");
        ref.put("uid", "refuid");
        ref.put("locale", "fr-fr");
        ref.put("profile", nested);
        ref.put("nested_refs", new JSONArray().put(subRef));
        JSONObject entry = new JSONObject();
        entry.put("uid", "e1");
        entry.put("authors", new JSONArray().put(ref));
        Utils.addEditableTags(entry, "post", false, "en-us", null);
        Assert.assertEquals("data-cslp=author_ct.refuid.fr-fr.profile.name", nested.getJSONObject("$").getString("name"));
        Assert.assertEquals("data-cslp=child_ct.c1.fr-fr.x", subRef.getJSONObject("$").getString("x"));
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
