package com.contentstack.utils;

import com.contentstack.utils.interfaces.ContentCallback;
import com.contentstack.utils.interfaces.MetaToEmbedCallback;
import com.contentstack.utils.render.DefaultOption;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.contentstack.utils.AutomateCommon.*;


public class GQL {


    // The `private GQL() throws IllegalAccessException` is a private constructor of the `GQL` class. It is
// throwing an `IllegalAccessException` with a message "Invalid Access! Could not create instance of
// GQL".
    private GQL() throws IllegalAccessException {
        throw new IllegalAccessException("Invalid Access! Could not create instance of GQL");
    }


    // The line `static JSONArray embeddedItems = null` declares a static variable named
    // `embeddedItems` of type `JSONArray` and initializes it with a value of `null`. This
    // variable is used to store the embedded items extracted from the GraphQL entry object
    // during the conversion process.
    private static JSONArray embeddedItems = null;


    /**
     * The function `jsonToHTML` converts a JSON object to HTML based on a given path and render
     * option.
     *
     * @param gqlEntry     The `gqlEntry` parameter is a JSONObject that represents the GraphQL entry
     *                     object. It contains the data that needs to be converted to HTML.
     * @param path         An array of strings representing the path to the desired content in the JSON object.
     * @param renderOption The `renderOption` parameter is of type `DefaultOption` and is used to
     *                     specify the rendering options for the HTML output. It is an enum that contains different options
     *                     for rendering the content.
     */
    public static void jsonToHTML(@NotNull JSONObject gqlEntry, @NotNull String[] path, @NotNull DefaultOption renderOption) {

        ContentCallback callback = content -> {
            JSONObject contentDict = (JSONObject) content;
            if (contentDict.has("embedded_itemsConnection")) {
                JSONObject embeddedConnection = contentDict.optJSONObject("embedded_itemsConnection");
                if (embeddedConnection.has("edges")) {
                    embeddedItems = embeddedConnection.optJSONArray("edges");
                }
            }

            if (contentDict.has("json")) {
                MetaToEmbedCallback converter = metadata -> {
                    if (embeddedItems != null) {
                        Optional<JSONObject> filteredContent = StreamSupport.stream(embeddedItems.spliterator(), false).map(JSONObject.class::cast).filter(itemDict -> {
                            JSONObject nodeObject = itemDict.optJSONObject("node");
                            if (nodeObject.has("uid")) {
                                String uid = nodeObject.optString("uid");
                                return uid.equals(metadata.getItemUid());
                            }
                            return false;
                        }).findFirst();
                        if (filteredContent.isPresent()) {
                            return filteredContent;
                        }
                    }
                    return Optional.empty();
                };
                Object contentJson = contentDict.opt("json");
                if (contentJson instanceof JSONArray) {
                    JSONArray contentArray = (JSONArray) contentJson;
                    return enumerateContents(contentArray, renderOption, converter);
                } else if (contentJson instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) contentJson;
                    return enumerateContent(jsonObject, renderOption, converter);
                }
            }
            return null;
        };
        for (String pathKey : path) {
            findContent(gqlEntry, pathKey, callback);
        }
    }
}
