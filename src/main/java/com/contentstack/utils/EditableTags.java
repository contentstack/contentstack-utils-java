package com.contentstack.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Live Preview editable tags (CSLP) — parity with contentstack-utils-javascript
 * {@code entry-editable.ts}.
 */
public final class EditableTags {

    /**
     * Variant / meta-key state threaded through {@link #getTag(Object, String, boolean, String, AppliedVariantsState)}.
     */
    public static final class AppliedVariantsState {
        private final JSONObject appliedVariants;
        private final boolean shouldApplyVariant;
        private final String metaKey;

        public AppliedVariantsState(JSONObject appliedVariants, boolean shouldApplyVariant, String metaKey) {
            this.appliedVariants = appliedVariants;
            this.shouldApplyVariant = shouldApplyVariant;
            this.metaKey = metaKey != null ? metaKey : "";
        }

        public JSONObject getAppliedVariants() {
            return appliedVariants;
        }

        public boolean isShouldApplyVariant() {
            return shouldApplyVariant;
        }

        public String getMetaKey() {
            return metaKey;
        }
    }

    private EditableTags() {
    }

    /**
     * Adds Contentstack Live Preview (CSLP) data tags to an entry for editable UIs.
     * Mutates the entry by attaching a {@code $} property with tag strings or objects
     * ({@code data-cslp} / {@code data-cslp-parent-field}) for each field.
     *
     * @param entry          CDA-style entry JSON (must not be {@code null}); must contain {@code uid}
     * @param contentTypeUid content type UID (e.g. {@code blog_post})
     * @param tagsAsObject   if {@code true}, tags are JSON objects; if {@code false}, {@code data-cslp=...} strings
     * @param locale         locale code (default in overloads: {@code en-us})
     * @param options        optional; controls locale casing (default lowercases locale)
     */
    public static void addEditableTags(JSONObject entry, String contentTypeUid, boolean tagsAsObject, String locale,
            EditableTagsOptions options) {
        if (entry == null) {
            return;
        }
        boolean useLowerCaseLocale = true;
        if (options != null) {
            useLowerCaseLocale = options.isUseLowerCaseLocale();
        }
        String ct = contentTypeUid == null ? "" : contentTypeUid.toLowerCase();
        String loc = locale == null ? "en-us" : locale;
        if (useLowerCaseLocale) {
            loc = loc.toLowerCase();
        }
        JSONObject applied = entry.optJSONObject("_applied_variants");
        if (applied == null) {
            JSONObject system = entry.optJSONObject("system");
            if (system != null) {
                applied = system.optJSONObject("applied_variants");
            }
        }
        boolean shouldApply = applied != null;
        String uid = entry.optString("uid", "");
        String prefix = ct + "." + uid + "." + loc;
        AppliedVariantsState state = new AppliedVariantsState(applied, shouldApply, "");
        entry.put("$", getTag(entry, prefix, tagsAsObject, loc, state));
    }

    /**
     * @see #addEditableTags(JSONObject, String, boolean, String, EditableTagsOptions)
     */
    public static void addEditableTags(JSONObject entry, String contentTypeUid, boolean tagsAsObject) {
        addEditableTags(entry, contentTypeUid, tagsAsObject, "en-us", null);
    }

    /**
     * @see #addEditableTags(JSONObject, String, boolean, String, EditableTagsOptions)
     */
    public static void addEditableTags(JSONObject entry, String contentTypeUid, boolean tagsAsObject, String locale) {
        addEditableTags(entry, contentTypeUid, tagsAsObject, locale, null);
    }

    /**
     * Alias for {@link #addEditableTags(JSONObject, String, boolean, String, EditableTagsOptions)} — matches JS
     * {@code addTags}.
     */
    public static void addTags(JSONObject entry, String contentTypeUid, boolean tagsAsObject, String locale,
            EditableTagsOptions options) {
        addEditableTags(entry, contentTypeUid, tagsAsObject, locale, options);
    }

    /**
     * Recursive tag map for the given content (entry object or array). Exposed for parity with JS tests.
     *
     * @param content          {@link JSONObject}, {@link JSONArray}, or null
     * @param prefix           path prefix ({@code contentTypeUid.entryUid.locale...})
     * @param tagsAsObject     string vs object tag form
     * @param locale           locale for reference entries
     * @param appliedVariants  variant state
     * @return map of field keys to tag string or tag object
     */
    public static JSONObject getTag(Object content, String prefix, boolean tagsAsObject, String locale,
            AppliedVariantsState appliedVariants) {
        if (content == null || JSONObject.NULL.equals(content)) {
            return new JSONObject();
        }
        if (content instanceof JSONArray) {
            return getTagForArray((JSONArray) content, prefix, tagsAsObject, locale, appliedVariants);
        }
        if (content instanceof JSONObject) {
            return getTagForJSONObject((JSONObject) content, prefix, tagsAsObject, locale, appliedVariants);
        }
        return new JSONObject();
    }

    private static JSONObject getTagForJSONObject(JSONObject content, String prefix, boolean tagsAsObject,
            String locale, AppliedVariantsState appliedVariants) {
        JSONObject tags = new JSONObject();
        Iterator<String> keys = content.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            handleKey(tags, key, content.opt(key), prefix, tagsAsObject, locale, appliedVariants);
        }
        return tags;
    }

    private static JSONObject getTagForArray(JSONArray content, String prefix, boolean tagsAsObject, String locale,
            AppliedVariantsState appliedVariants) {
        JSONObject tags = new JSONObject();
        for (int i = 0; i < content.length(); i++) {
            String key = Integer.toString(i);
            handleKey(tags, key, content.opt(i), prefix, tagsAsObject, locale, appliedVariants);
        }
        return tags;
    }

    /** One entry from {@code Object.entries} — same structure for {@link JSONObject} and {@link JSONArray}. */
    private static void handleKey(JSONObject tags, String key, Object value, String prefix, boolean tagsAsObject,
            String locale, AppliedVariantsState appliedVariants) {
        if ("$".equals(key)) {
            return;
        }
        boolean shouldApplyVariant = appliedVariants.isShouldApplyVariant();
        JSONObject applied = appliedVariants.getAppliedVariants();

        String metaUid = metaUidFromValue(value);
        String metaKeyPrefix = appliedVariants.getMetaKey().isEmpty() ? "" : appliedVariants.getMetaKey() + ".";
        String updatedMetakey = shouldApplyVariant ? metaKeyPrefix + key : "";
        if (!metaUid.isEmpty() && !updatedMetakey.isEmpty()) {
            updatedMetakey = updatedMetakey + "." + metaUid;
        }
        // For array fields, per-element processing below must not overwrite this — line 220's field tag uses it.
        String fieldMetakey = updatedMetakey;

        if (value instanceof JSONArray) {
            JSONArray arr = (JSONArray) value;
            for (int index = 0; index < arr.length(); index++) {
                Object obj = arr.opt(index);
                if (obj == null || JSONObject.NULL.equals(obj)) {
                    continue;
                }
                String childKey = key + "__" + index;
                String parentKey = key + "__parent";
                metaUid = metaUidFromValue(obj);
                String elementMetakey = shouldApplyVariant ? metaKeyPrefix + key : "";
                if (!metaUid.isEmpty() && !elementMetakey.isEmpty()) {
                    elementMetakey = elementMetakey + "." + metaUid;
                }
                String indexPath = prefix + "." + key + "." + index;
                String fieldPath = prefix + "." + key;
                putTag(tags, childKey, indexPath, tagsAsObject, applied, shouldApplyVariant, elementMetakey);
                putParentTag(tags, parentKey, fieldPath, tagsAsObject);
                if (obj instanceof JSONObject) {
                    JSONObject jobj = (JSONObject) obj;
                    if (jobj.has("_content_type_uid") && jobj.has("uid")) {
                        JSONObject newApplied = jobj.optJSONObject("_applied_variants");
                        if (newApplied == null) {
                            JSONObject sys = jobj.optJSONObject("system");
                            if (sys != null) {
                                newApplied = sys.optJSONObject("applied_variants");
                            }
                        }
                        boolean newShould = newApplied != null;
                        String refLocale = jobj.has("locale") && !jobj.isNull("locale")
                                ? jobj.optString("locale", locale)
                                : locale;
                        String refPrefix = jobj.optString("_content_type_uid") + "." + jobj.optString("uid") + "."
                                + refLocale;
                        jobj.put("$", getTag(jobj, refPrefix, tagsAsObject, refLocale,
                                new AppliedVariantsState(newApplied, newShould, "")));
                    } else {
                        jobj.put("$", getTag(jobj, indexPath, tagsAsObject, locale,
                                new AppliedVariantsState(applied, shouldApplyVariant, elementMetakey)));
                    }
                }
            }
        } else if (value instanceof JSONObject) {
            JSONObject valueObj = (JSONObject) value;
            valueObj.put("$", getTag(valueObj, prefix + "." + key, tagsAsObject, locale,
                    new AppliedVariantsState(applied, shouldApplyVariant, updatedMetakey)));
        }

        String fieldTagPath = prefix + "." + key;
        putTag(tags, key, fieldTagPath, tagsAsObject, applied, shouldApplyVariant, fieldMetakey);
    }

    private static String metaUidFromValue(Object value) {
        if (!(value instanceof JSONObject)) {
            return "";
        }
        JSONObject jo = (JSONObject) value;
        JSONObject meta = jo.optJSONObject("_metadata");
        if (meta == null) {
            return "";
        }
        return meta.optString("uid", "");
    }

    private static void putTag(JSONObject tags, String key, String dataValue, boolean tagsAsObject,
            JSONObject appliedVariants, boolean shouldApplyVariant, String metaKey) {
        TagsPayload payload = new TagsPayload(appliedVariants, shouldApplyVariant, metaKey);
        if (tagsAsObject) {
            tags.put(key, getTagsValueAsObject(dataValue, payload));
        } else {
            tags.put(key, getTagsValueAsString(dataValue, payload));
        }
    }

    private static void putParentTag(JSONObject tags, String key, String dataValue, boolean tagsAsObject) {
        if (tagsAsObject) {
            tags.put(key, getParentTagsValueAsObject(dataValue));
        } else {
            tags.put(key, getParentTagsValueAsString(dataValue));
        }
    }

    private static final class TagsPayload {
        private final JSONObject appliedVariants;
        private final boolean shouldApplyVariant;
        private final String metaKey;

        private TagsPayload(JSONObject appliedVariants, boolean shouldApplyVariant, String metaKey) {
            this.appliedVariants = appliedVariants;
            this.shouldApplyVariant = shouldApplyVariant;
            this.metaKey = metaKey != null ? metaKey : "";
        }
    }

    static String applyVariantToDataValue(String dataValue, JSONObject appliedVariants, boolean shouldApplyVariant,
            String metaKey) {
        if (shouldApplyVariant && appliedVariants != null) {
            Object direct = appliedVariants.opt(metaKey);
            if (direct != null && !JSONObject.NULL.equals(direct)) {
                String variant = String.valueOf(direct);
                String[] newDataValueArray = ("v2:" + dataValue).split("\\.", -1);
                if (newDataValueArray.length > 1) {
                    newDataValueArray[1] = newDataValueArray[1] + "_" + variant;
                    return String.join(".", newDataValueArray);
                }
            }
            String parentVariantisedPath = getParentVariantisedPath(appliedVariants, metaKey);
            if (parentVariantisedPath != null && !parentVariantisedPath.isEmpty()) {
                Object v = appliedVariants.opt(parentVariantisedPath);
                if (v != null && !JSONObject.NULL.equals(v)) {
                    String variant = String.valueOf(v);
                    String[] newDataValueArray = ("v2:" + dataValue).split("\\.", -1);
                    if (newDataValueArray.length > 1) {
                        newDataValueArray[1] = newDataValueArray[1] + "_" + variant;
                        return String.join(".", newDataValueArray);
                    }
                }
            }
        }
        return dataValue;
    }

    static String getParentVariantisedPath(JSONObject appliedVariants, String metaKey) {
        try {
            if (appliedVariants == null) {
                return "";
            }
            List<String> variantisedFieldPaths = new ArrayList<>(appliedVariants.keySet());
            variantisedFieldPaths.sort(Comparator.comparingInt(String::length).reversed());
            String[] childPathFragments = metaKey.split("\\.", -1);
            if (childPathFragments.length == 0 || variantisedFieldPaths.isEmpty()) {
                return "";
            }
            for (String path : variantisedFieldPaths) {
                String[] parentFragments = path.split("\\.", -1);
                if (parentFragments.length > childPathFragments.length) {
                    continue;
                }
                boolean all = true;
                for (int i = 0; i < parentFragments.length; i++) {
                    if (!Objects.equals(parentFragments[i], childPathFragments[i])) {
                        all = false;
                        break;
                    }
                }
                if (all) {
                    return path;
                }
            }
            return "";
        } catch (RuntimeException e) {
            return "";
        }
    }

    private static JSONObject getTagsValueAsObject(String dataValue, TagsPayload payload) {
        String resolved = applyVariantToDataValue(dataValue, payload.appliedVariants, payload.shouldApplyVariant,
                payload.metaKey);
        JSONObject o = new JSONObject();
        o.put("data-cslp", resolved);
        return o;
    }

    private static String getTagsValueAsString(String dataValue, TagsPayload payload) {
        String resolved = applyVariantToDataValue(dataValue, payload.appliedVariants, payload.shouldApplyVariant,
                payload.metaKey);
        return "data-cslp=" + resolved;
    }

    private static JSONObject getParentTagsValueAsObject(String dataValue) {
        JSONObject o = new JSONObject();
        o.put("data-cslp-parent-field", dataValue);
        return o;
    }

    private static String getParentTagsValueAsString(String dataValue) {
        return "data-cslp-parent-field=" + dataValue;
    }
}
