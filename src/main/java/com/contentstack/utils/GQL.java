package com.contentstack.utils;

import com.contentstack.utils.interfaces.ContentCallback;
import com.contentstack.utils.interfaces.MetaToEmbedCallback;
import com.contentstack.utils.render.DefaultOption;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.contentstack.utils.AutomateCommon.*;

/**
 * The type Gql.
 */
public class GQL {

    private static JSONArray embeddedItems = null;

    /**
     * Json to html.
     *
     * @param gqlEntry     the gql entry is entry @{@link JSONObject}
     * @param path         the path is array of @{@link String}
     * @param renderOption the render option is instance of @{@link DefaultOption}
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
                        Optional<JSONObject> filteredContent = StreamSupport.stream(embeddedItems.spliterator(), false)
                                .map(val -> (JSONObject) val)
                                .filter(itemDict -> {
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

        if (path.length > 0) {
            for (String pathKey : path) {
                findContent(gqlEntry, pathKey, callback);
            }
        }
    }
}
