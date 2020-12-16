package com.contentstack.utils;

import com.contentstack.utils.callbacks.ContentCallback;
import com.contentstack.utils.callbacks.MetadataCallback;
import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.render.DefaultOptionsCallback;
import com.contentstack.utils.callbacks.OptionsCallback;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

public class Utils {

    // Interface Metadata Callback
    private interface MetadataCallback { void embeddedObject(Metadata metadata); }

    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    /**
     * @param entryObj: Objects that contains RTE with embedded objects
     * @param keyPath keyPath
     * @param renderObject renderObject
     */
    public static void  render(JSONObject entryObj, String[] keyPath, OptionsCallback renderObject){
        ContentCallback callback = content -> {
            if (content instanceof JSONArray) {
                JSONArray contentArray = (JSONArray) content;
                return renderContents(contentArray, entryObj, renderObject);
            }else if (content instanceof String){
                String contentString = (String) content;
                return renderContent(contentString, entryObj, renderObject);
            }
            return null;
        };

        if (entryObj!=null && entryObj.has("_embedded_items")){
            // when keyPath is provided by user
            if (keyPath!=null){
                for (String path : keyPath) {
                    findContent(entryObj, path, callback);
                }
            }else {
                // if keyPath is not given, extract all available keyPath from _embedded_items
                JSONObject embedKeys = entryObj.getJSONObject("_embedded_items");
                ArrayList<String> pathKeys = new ArrayList<>(embedKeys.keySet());
                for (String path : pathKeys) {
                    findContent(entryObj, path, callback);
                }
            }
        }
    }

    /**
     * Find dot separated keys
     * @param entryObj Json Object
     * @param path keyPath
     * @param contentCallback content callback
     */
    private static void findContent(JSONObject entryObj, String path, ContentCallback contentCallback ) {
        String [] arrayString = path.split("\\.");
        getContent(arrayString, entryObj, contentCallback);
    }

    /**
     * getContent accepts arrayString
     * @param arrayString
     * @param entryObj
     * @param contentCallback
     */
    private static void getContent(String[] arrayString, JSONObject entryObj, ContentCallback contentCallback) {
        if (arrayString!=null && arrayString.length!=0){
            String key = arrayString[0];
            if (arrayString.length == 1) {
                Object varContent = entryObj.opt(key);
                if (varContent instanceof String || varContent instanceof JSONArray) {
                    entryObj.put(key, contentCallback.contentObject(varContent));
                }
            } else {
                List<String> list = new ArrayList<>(Arrays.asList(arrayString));
                list.remove(key);
                String[] newArrayString = list.toArray(new String[0]);
                if (entryObj.opt(key) instanceof JSONObject) {
                     getContent(newArrayString, entryObj.optJSONObject(key), contentCallback);
                } else if (entryObj.opt(key) instanceof JSONArray) {
                    JSONArray  jsonArray = entryObj.optJSONArray(key);
                    for (int idx = 0; idx < jsonArray.length(); idx++) {
                        getContent(newArrayString, jsonArray.optJSONObject(idx), contentCallback);
                    }
                }
            }
        }
    }


    /**
     *
     * @param jsonArray Objects that contains RTE with embedded objects
     * @param keyPath String array keyPath
     * @param renderObject renderObjects
     */
    public void render(JSONArray jsonArray, String[] keyPath, OptionsCallback renderObject){
        jsonArray.forEach(jsonObj-> render((JSONObject) jsonObj, keyPath, renderObject));
    }


    /**
     * Accepts to render content on the basis of below content
     * @param rteStringify   String of the rte available for the embedding
     * @param embedObject JSONObject to get the _embedded_object (_embedded_entries/_embedded_assets)
     * @param options     Options take takes input as (StyleType type, JSONObject embeddedObject)
     * @return String of rte with replaced tag
     */
    public static String renderContent(String rteStringify, JSONObject embedObject, Options options) {

        final String[] sReplaceRTE = {rteStringify};
        Document html = Jsoup.parse(rteStringify);
        getEmbeddedObjects(html, metadata -> {

            Optional<JSONObject> filteredContent = Optional.empty();
                boolean available = embedObject.has("_embedded_items");
                if (available) {
                    JSONObject jsonArray = embedObject.optJSONObject("_embedded_items");
                    filteredContent = findEmbeddedItems(jsonArray, metadata);
                }
            if (filteredContent.isPresent()) {
                JSONObject contentToPass = filteredContent.get();
                String stringOption = getStringOption(optionsCallback, metadata, contentToPass);
                sReplaceRTE[0] = html.body().html().replace(metadata.getOuterHTML(), stringOption);
            }
        });

        return sReplaceRTE[0];
    }


    /**
     * Take below items to return updated string
     * @param rteArray JSONArray of the rte available for the embedding
     * @param entryObject JSONObject to get the _embedded_object (_embedded_entries/_embedded_assets)
     * @param optionsCallback Options take takes input as (StyleType type, JSONObject embeddedObject)
     * @return String of rte with replaced tag
     */
    public static JSONArray renderContents(JSONArray rteArray, JSONObject entryObject, OptionsCallback optionsCallback) {
        JSONArray jsonArrayRTEContent = new JSONArray();
        for (Object RTE : rteArray) {
            String stringify = (String) RTE;
            String renderContent = renderContent(stringify, entryObject, optionsCallback);
            jsonArrayRTEContent.put(renderContent);
        }
        return jsonArrayRTEContent;
    }


    /**
     * Matches the uid and _content_type_uid from the
     *
     * @param jsonArray JSONArray: array of the _embedded_entries
     * @param metadata EmbeddedObject: contains the model class information
     * @return Optional<JSONObject>
     */
    private static Optional<JSONObject> findEmbeddedEntry(JSONArray jsonArray, Metadata metadata) {
        Optional<JSONObject> filteredContent = StreamSupport.stream(jsonArray.spliterator(), false)
                .map(val -> (JSONObject) val)
                .filter(val -> val.optString("uid").equalsIgnoreCase(metadata.getItemUid()))
                .filter(val -> val.optString("_content_type_uid").equalsIgnoreCase(metadata.getContentTypeUid()))
                .findFirst();
        return filteredContent;
    }

    /**
     * Matches the uid and _content_type_uid from the
     *
     * @param jsonArray JSONArray: array of the _embedded_assets
     * @param metadata EmbeddedObject: contains the model class information
     * @return Optional<JSONObject>
     */
    private static Optional<JSONObject> findEmbeddedItems(JSONObject jsonObject, Metadata metadata) {
        Set<String> allKeys = jsonObject.keySet();
        for (String key: allKeys) {
            JSONArray jsonArray = jsonObject.optJSONArray(key);
            Optional<JSONObject> filteredContent = StreamSupport.stream(jsonArray.spliterator(), false)
                    .map(val -> (JSONObject) val)
                    .filter(val -> val.optString("uid").equalsIgnoreCase(metadata.getItemUid())).findFirst();
            if (filteredContent.isPresent()){
                return filteredContent;
            }
        }
        return Optional.empty();
    }

    private static String getStringOption(OptionsCallback optionsCallback, Metadata metadata, JSONObject contentToPass) {
        // TODO: Sending HashMap as HTML Attributes
        String stringOption = options.renderOptions(
                contentToPass, metadata);
        if (stringOption == null) {
            DefaultOptions defaultOptions = new DefaultOptions();
            stringOption = defaultOptions.renderOptions(
                    contentToPass, metadata);
        }
        return stringOption;
    }


    private static void getEmbeddedObjects(Document html, MetadataCallback metadataCallback) {

        Elements embeddedEntries = html.body().getElementsByClass("embedded-entry");
        Elements embeddedAssets = html.body().getElementsByClass("embedded-asset");

        embeddedEntries.forEach((entry) -> {
            String text = entry.text();
            String type = entry.attr("type");
            String uid = entry.attr("data-sys-entry-uid");
            String contentType = entry.attr("data-sys-content-type-uid");
            String style = entry.attr("sys-style-type");
            String outerHTML = entry.outerHtml();
            Metadata metadata = new Metadata(text, type, uid, contentType, style, outerHTML, entry.attributes());
            metadataCallback.embeddedObject(metadata);
        });

        embeddedAssets.forEach((asset) -> {
            String text = asset.text();
            String type = asset.attr("type");
            String uid = asset.attr("data-sys-asset-uid");
            String style = asset.attr("sys-style-type");
            String outerHTML = asset.outerHtml();
            Metadata metadata = new Metadata(text, type, uid, "asset", style, outerHTML, asset.attributes());
            metadataCallback.embeddedObject(metadata);
        });
    }


}


