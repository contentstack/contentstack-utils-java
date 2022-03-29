package com.contentstack.utils;

import com.contentstack.utils.callbacks.Content;
import com.contentstack.utils.callbacks.Metadata;
import com.contentstack.utils.callbacks.OptionsCallback;
import com.contentstack.utils.presets.Constant;
import com.contentstack.utils.render.DefaultOption;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.stream.StreamSupport;

public class Utils {

    /**
     * @param entryObj:    Objects that contains RTE with embedded objects
     * @param keyPath      keyPath
     * @param renderObject renderObject
     */
    public static void render(JSONObject entryObj, String[] keyPath, OptionsCallback renderObject) {
        Content callback = content -> {
            if (content instanceof JSONArray) {
                JSONArray contentArray = (JSONArray) content;
                return renderContents(contentArray, entryObj, renderObject);
            } else if (content instanceof String) {
                String contentString = (String) content;
                return renderContent(contentString, entryObj, renderObject);
            }
            return null;
        };

        if (entryObj != null && entryObj.has(Constant.EMBEDDED_ITEMS)) {
            if (keyPath != null) {
                for (String path : keyPath) {
                    findContent(entryObj, path, callback);
                }
            } else {
                // Case when KeyPath is not given by user, 
                // Extract all available keyPath from _embedded_items
                JSONObject embedKeys = entryObj.getJSONObject(Constant.EMBEDDED_ITEMS);
                ArrayList<String> pathKeys = new ArrayList<>(embedKeys.keySet());
                for (String path : pathKeys) {
                    findContent(entryObj, path, callback);
                }
            }
        }
    }

    /**
     * Find dot separated keys
     * 
     * @param entryObj Json Object
     * @param path     keyPath
     * @param content  content callback
     */
    private static void findContent(JSONObject entryObj, String path, Content content) {
        String[] arrayString = path.split("\\.");
        getContent(arrayString, entryObj, content);
    }

    /**
     *
     * @param arrayString list of keys available
     * @param entryObj    entry object
     * @param content     content callback
     */
    private static void getContent(String[] arrayString, JSONObject entryObj, Content content) {
        if (arrayString != null && arrayString.length != 0) {
            String key = arrayString[0];
            if (arrayString.length == 1) {
                Object varContent = entryObj.opt(key);
                if (varContent instanceof String || varContent instanceof JSONArray) {
                    entryObj.put(key, content.contentObject(varContent));
                }
            } else {
                List<String> list = new ArrayList<>(Arrays.asList(arrayString));
                list.remove(key);
                String[] newArrayString = list.toArray(new String[0]);
                if (entryObj.opt(key) instanceof JSONObject) {
                    getContent(newArrayString, entryObj.optJSONObject(key), content);
                } else if (entryObj.opt(key) instanceof JSONArray) {
                    JSONArray jsonArray = entryObj.optJSONArray(key);
                    for (int idx = 0; idx < jsonArray.length(); idx++) {
                        getContent(newArrayString, jsonArray.optJSONObject(idx), content);
                    }
                }
            }
        }
    }

    /**
     *
     * @param jsonArray    Objects that contains RTE with embedded objects
     * @param keyPath      String array keyPath
     * @param renderObject renderObjects
     */
    public void render(JSONArray jsonArray, String[] keyPath, OptionsCallback renderObject) {
        jsonArray.forEach(jsonObj -> render((JSONObject) jsonObj, keyPath, renderObject));
    }

    /**
     * Accepts to render content on the basis of below content
     * 
     * @param rteStringify String of the rte available for the embedding
     * @param embedObject  JSONObject to get the _embedded_object
     *                     (_embedded_entries/_embedded_assets)
     * @param option       Options take takes input as (StyleType type, JSONObject
     *                     embeddedObject)
     * @return String of rte with replaced tag
     */
    public static String renderContent(String rteStringify, JSONObject embedObject, OptionsCallback option) {
        final String[] sReplaceRTE = { rteStringify };
        Document html = Jsoup.parse(rteStringify);
        getEmbeddedObjects(html, metadata -> {
            Optional<JSONObject> filteredContent = Optional.empty();
            boolean available = embedObject.has(Constant.EMBEDDED_ITEMS);
            if (available) {
                JSONObject jsonArray = embedObject.optJSONObject(Constant.EMBEDDED_ITEMS);
                filteredContent = findEmbeddedItems(jsonArray, metadata);
            }
            if (filteredContent.isPresent()) {
                JSONObject contentToPass = filteredContent.get();
                String stringOption = getStringOption(option, metadata, contentToPass);
                sReplaceRTE[0] = html.body().html().replace(metadata.getOuterHTML(), stringOption);
            }
        });
        return sReplaceRTE[0];
    }

    /**
     * Take below items to return updated string
     * 
     * @param rteArray    JSONArray of the rte available for the embedding
     * @param entryObject JSONObject to get the _embedded_object
     *                    (_embedded_entries/_embedded_assets)
     * @param option      Options take takes input as (StyleType type, JSONObject
     *                    embeddedObject)
     * @return String of rte with replaced tag
     */
    public static JSONArray renderContents(JSONArray rteArray, JSONObject entryObject, OptionsCallback option) {
        JSONArray jsonArrayRTEContent = new JSONArray();
        for (Object RTE : rteArray) {
            String stringify = (String) RTE;
            String renderContent = renderContent(stringify, entryObject, option);
            jsonArrayRTEContent.put(renderContent);
        }
        return jsonArrayRTEContent;
    }

    /**
     * Matches the uid and _content_type_uid from the
     * 
     * @param jsonObject JSONObject: jsonObject of the _embedded_assets
     * @param metadata   EmbeddedObject: contains the model class information
     * @return Optional<JSONObject>
     */
    private static Optional<JSONObject> findEmbeddedItems(JSONObject jsonObject,
            com.contentstack.utils.helper.Metadata metadata) {
        Set<String> allKeys = jsonObject.keySet();
        for (String key : allKeys) {
            JSONArray jsonArray = jsonObject.optJSONArray(key);
            Optional<JSONObject> filteredContent = StreamSupport.stream(jsonArray.spliterator(), false)
                    .map(JSONObject.class::cast)
                    .filter(val -> val.optString("uid").equalsIgnoreCase(metadata.getItemUid())).findFirst();
            if (filteredContent.isPresent()) {
                return filteredContent;
            }
        }
        return Optional.empty();
    }

    private static String getStringOption(OptionsCallback option, com.contentstack.utils.helper.Metadata metadata,
            JSONObject contentToPass) {
        String stringOption = option.renderOptions(contentToPass, metadata);
        if (stringOption == null) {
            DefaultOption defaultOptions = new DefaultOption();
            stringOption = defaultOptions.renderOptions(contentToPass, metadata);
        }
        return stringOption;
    }

    private static void getEmbeddedObjects(Document html, Metadata metadataCallback) {
        Elements embeddedEntries = html.body().getElementsByClass("embedded-entry");
        Elements embeddedAssets = html.body().getElementsByClass("embedded-asset");
        embeddedEntries.forEach(entry -> {
            String text = entry.text();
            String type = entry.attr("type");
            String uid = entry.attr("data-sys-entry-uid");
            String contentType = entry.attr("data-sys-content-type-uid");
            String style = entry.attr("sys-style-type");
            String outerHTML = entry.outerHtml();
            com.contentstack.utils.helper.Metadata metadata = new com.contentstack.utils.helper.Metadata(text, type,
                    uid, contentType, style, outerHTML, entry.attributes());
            metadataCallback.embeddedObject(metadata);
        });

        embeddedAssets.forEach(asset -> {
            String text = asset.text();
            String type = asset.attr("type");
            String uid = asset.attr("data-sys-asset-uid");
            String style = asset.attr("sys-style-type");
            String outerHTML = asset.outerHtml();
            com.contentstack.utils.helper.Metadata metadata = new com.contentstack.utils.helper.Metadata(text, type,
                    uid, "asset", style, outerHTML, asset.attributes());
            metadataCallback.embeddedObject(metadata);
        });
    }

}
