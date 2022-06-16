package com.contentstack.utils;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.interfaces.ContentCallback;
import com.contentstack.utils.interfaces.MetaToEmbedCallback;
import com.contentstack.utils.interfaces.Option;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

import static com.contentstack.utils.AutomateCommon.*;


/**
 * The Utils Class enables few functions like render, renderContent, jsonToHtml, RTE and SRTE to process the entry data
 * and converts in html format.
 */
public class Utils {


    static final Logger logger = Logger.getLogger(Utils.class.getName());

    /**
     * Render.
     *
     * @param entryObj
     *         the entry obj
     * @param pathString
     *         the key path
     * @param renderObject
     *         the render object
     */
    public static void render(JSONObject entryObj, String[] pathString, Option renderObject) {

        ContentCallback callback = content -> {
            if (content instanceof JSONArray) {
                JSONArray contentArray = (JSONArray) content;
                return renderContents(contentArray, entryObj, renderObject);
            } else if (content instanceof String) {
                String contentString = (String) content;
                return renderContent(contentString, entryObj, renderObject);
            }
            return null;
        };

        if (entryObj != null && entryObj.has("_embedded_items")) {
            // when pathString is provided by user
            if (pathString != null && pathString.length > 0) {
                for (String path : pathString) {
                    findContent(entryObj, path, callback);
                }
            } else {
                // if pathString is not given, extract all available pathString from _embedded_items
                JSONObject embedKeys = entryObj.getJSONObject("_embedded_items");
                ArrayList<String> pathKeys = new ArrayList<>(embedKeys.keySet());
                for (String path : pathKeys) {
                    findContent(entryObj, path, callback);
                }
            }
        }
    }


    /**
     * Render content string.
     *
     * @param rteStringify
     *         the rte stringify
     * @param embedObject
     *         the embed object
     * @param option
     *         the option
     * @return the string
     */
    public static String renderContent(String rteStringify, JSONObject embedObject, Option option) {
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
                String stringOption = getStringOption(option, metadata, contentToPass);
                sReplaceRTE[0] = html.body().html().replace(metadata.getOuterHTML(), stringOption);
            }
        });
        return sReplaceRTE[0];
    }

    public static JSONArray renderContents(JSONArray rteArray, JSONObject entryObject, Option option) {
        JSONArray jsonArrayRTEContent = new JSONArray();
        for (Object RTE : rteArray) {
            String stringify = (String) RTE;
            String renderContent = renderContent(stringify, entryObject, option);
            jsonArrayRTEContent.put(renderContent);
        }
        return jsonArrayRTEContent;
    }

    private static Optional<JSONObject> findEmbeddedItems(JSONObject jsonObject, Metadata metadata) {
        Set<String> allKeys = jsonObject.keySet();
        for (String key : allKeys) {
            JSONArray jsonArray = jsonObject.optJSONArray(key);
            Optional<JSONObject> filteredContent = StreamSupport.stream(jsonArray.spliterator(), false)
                    .map(val -> (JSONObject) val)
                    .filter(val -> val.optString("uid").equalsIgnoreCase(metadata.getItemUid())).findFirst();
            if (filteredContent.isPresent()) {
                return filteredContent;
            }
        }
        return Optional.empty();
    }

    /**
     * Json to html.
     *
     * @param entryArray
     *         the entry array
     * @param keyPath
     *         the key path
     * @param option
     *         the render option
     */
    public static void jsonToHTML(@NotNull JSONArray entryArray, @NotNull String[] keyPath, @NotNull Option option) {
        entryArray.forEach(jsonObj -> jsonToHTML((JSONObject) jsonObj, keyPath, option));
    }


    /**
     * Json to html.
     *
     * @param entry
     *         the entry
     * @param keyPath
     *         the key path
     * @param renderOption
     *         the render object
     */
    public static void jsonToHTML(@NotNull JSONObject entry, @NotNull String[] keyPath, Option renderOption) {

        MetaToEmbedCallback converter = metadata -> {
            boolean available = entry.has("_embedded_items");
            if (available) {
                JSONObject jsonArray = entry.optJSONObject("_embedded_items");
                return findEmbeddedItems(jsonArray, metadata);
            }
            return Optional.empty();
        };


        ContentCallback callback = content -> {
            if (content instanceof JSONArray) {
                JSONArray contentArray = (JSONArray) content;

                return enumerateContents(contentArray, renderOption, converter);
            } else if (content instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) content;
                return enumerateContent(jsonObject, renderOption, converter);
            }
            return null;
        };

        if (keyPath.length > 0) {
            for (String path : keyPath) {
                logger.info(path);
                findContent(entry, path, callback);
            }
        }
    }


    /**
     * Render.
     *
     * @param jsonArray
     *         the json array
     * @param keyPath
     *         the key path
     * @param renderObject
     *         the render object
     */
    public void render(@NotNull JSONArray jsonArray, @NotNull String[] keyPath, @NotNull Option renderObject) {
        jsonArray.forEach(jsonObj -> render((JSONObject) jsonObj, keyPath, renderObject));
    }


}


