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
import java.util.stream.StreamSupport;

import static com.contentstack.utils.AutomateCommon.*;


public class Utils {


    /**
     * The `render` function takes a JSON object, an array of path strings, and an option object, and
     * renders the contents of the JSON object based on the provided paths and options.
     *
     * @param entryObj     The entryObj parameter is a JSONObject that represents an entry or a data
     *                     object. It contains various properties and values.
     * @param pathString   An array of strings representing the paths to the content in the JSON object.
     * @param renderObject The `renderObject` parameter is an object of type `Option`. It is used to
     *                     specify rendering options for the content.
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
     * The function takes a string, a JSON object, and an option, and replaces certain elements in the
     * string with values from the JSON object based on the option.
     *
     * @param rteStringify The `rteStringify` parameter is a string representation of the content to be
     *                     rendered. It is passed to the `Jsoup.parse()` method to create a `Document` object.
     * @param embedObject  The `embedObject` parameter is a JSONObject that contains embedded items. It
     *                     may have a key "_embedded_items" which holds a JSONObject of embedded items.
     * @param option       The "option" parameter is of type "Option". It is an object that represents a
     *                     specific option for rendering the content. The exact structure and properties of the "Option"
     *                     object are not provided in the code snippet, so it would be necessary to refer to the
     *                     documentation or other parts of the code
     * @return The method is returning the modified RTE (Rich Text Editor) content as a string.
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

    /**
     * The function takes an array of strings, an object, and an option, and returns a new array with the
     * rendered contents of each string.
     *
     * @param rteArray    An array of RTE (Rich Text Editor) content strings.
     * @param entryObject The `entryObject` parameter is a JSONObject that contains the data needed for
     *                    rendering the content. It likely contains key-value pairs representing different properties or
     *                    attributes of the content.
     * @param option      The "option" parameter is an object of type "Option". It is used as an argument in the
     *                    "renderContent" method.
     * @return The method is returning a JSONArray object.
     */
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
            Optional<JSONObject> filteredContent = StreamSupport.stream(jsonArray.spliterator(), false).map(val -> (JSONObject) val).filter(val -> val.optString("uid").equalsIgnoreCase(metadata.getItemUid())).findFirst();
            if (filteredContent.isPresent()) {
                return filteredContent;
            }
        }
        return Optional.empty();
    }

    /**
     * The function converts a JSONArray to HTML using a specified key path and options.
     *
     * @param entryArray A JSONArray containing JSON objects.
     * @param keyPath    The keyPath parameter is an array of strings that represents the path to a specific
     *                   key in a JSON object. Each string in the array represents a key in the path. For example, if the
     *                   keyPath is ["person", "name"], it means that we want to access the value of the "
     * @param option     The "option" parameter is an object of type "Option". It is used to specify additional
     *                   options or settings for the JSON to HTML conversion process.
     */
    public static void jsonToHTML(@NotNull JSONArray entryArray, @NotNull String[] keyPath, @NotNull Option option) {
        entryArray.forEach(jsonObj -> jsonToHTML((JSONObject) jsonObj, keyPath, option));
    }


    /**
     * The function `jsonToHTML` converts a JSON object to HTML using a specified key path and
     * rendering options.
     *
     * @param entry        The `entry` parameter is a `JSONObject` that represents the JSON data that you want
     *                     to convert to HTML. It contains the data that you want to render as HTML.
     * @param keyPath      The keyPath parameter is an array of strings that represents the path to the
     *                     desired content in the JSON object. Each string in the array represents a key in the JSON object
     *                     hierarchy. The method will traverse the JSON object using the keys in the keyPath array to find
     *                     the desired content.
     * @param renderOption The renderOption parameter is an option that determines how the content
     *                     should be rendered. It is of type Option, which is likely an enum or a class with different
     *                     rendering options. The specific options available and their meanings would depend on the
     *                     implementation of the Option class.
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

        for (String path : keyPath) {
            findContent(entry, path, callback);
        }
    }


    /**
     * The function converts a JSON object to HTML using a specified rendering option and optional embedded
     * items.
     *
     * @param jsonRTE       A JSONObject representing the JSON data to be converted to HTML.
     * @param renderOption  The `renderOption` parameter is an option that determines how the JSON content
     *                      should be rendered as HTML. It could be an enum or a class that defines different rendering options.
     * @param embeddeditems The `embeddeditems` parameter is a `JSONObject` that contains embedded items.
     *                      It is used to find and retrieve embedded items based on their metadata.
     * @return The method is returning a String.
     */
    public static String jsonToHTML(@NotNull JSONObject jsonRTE, Option renderOption, JSONObject embeddeditems) {
        MetaToEmbedCallback converter = metadata -> {
            if (embeddeditems != null && !embeddeditems.isEmpty()) {
                return findEmbeddedItems(embeddeditems, metadata);
            }
            return Optional.empty();
        };
        return enumerateContent(jsonRTE, renderOption, converter);
    }


    /**
     * The function `jsonToHTML` converts a JSON array to HTML using a specified rendering option and
     * optional embedded items.
     *
     * @param jsonRTE       A JSONArray object containing the JSON data to be converted to HTML.
     * @param renderOption  The `renderOption` parameter is an option that determines how the JSON data
     *                      should be rendered as HTML. It could be an enum or a custom class that defines different rendering
     *                      options.
     * @param embeddeditems The `embeddeditems` parameter is a `JSONObject` that contains embedded items.
     *                      It is used to find and retrieve embedded items based on the metadata provided.
     * @return The method is returning an Object.
     */
    public static Object jsonToHTML(@NotNull JSONArray jsonRTE, Option renderOption, JSONObject embeddeditems) {
        MetaToEmbedCallback converter = metadata -> {
            if (embeddeditems != null && !embeddeditems.isEmpty()) {
                return findEmbeddedItems(embeddeditems, metadata);
            }
            return Optional.empty();
        };
        return enumerateContents(jsonRTE, renderOption, converter);
    }


    /**
     * The function takes a JSONArray, a keyPath array, and an Option object, and iterates over each
     * JSONObject in the JSONArray to call another render function.
     *
     * @param jsonArray    A JSONArray object that contains a collection of JSON objects.
     * @param keyPath      The `keyPath` parameter is an array of strings that represents the path to a specific
     *                     key in a JSON object. Each string in the array represents a key in the path. For example, if the key
     *                     path is `["foo", "bar", "baz"]`, it means that you want
     * @param renderObject The `renderObject` parameter is an object of type `Option`.
     */
    public void render(@NotNull JSONArray jsonArray, @NotNull String[] keyPath, @NotNull Option renderObject) {
        jsonArray.forEach(jsonObj -> render((JSONObject) jsonObj, keyPath, renderObject));
    }


}


