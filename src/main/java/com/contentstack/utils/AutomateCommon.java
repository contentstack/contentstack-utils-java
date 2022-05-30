package com.contentstack.utils;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.interfaces.ContentCallback;
import com.contentstack.utils.interfaces.MetaToEmbedCallback;
import com.contentstack.utils.interfaces.MetadataCallback;
import com.contentstack.utils.interfaces.Option;
import com.contentstack.utils.node.NodeToHTML;
import com.contentstack.utils.render.DefaultOption;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AutomateCommon {


    /**
     * Find dot separated keys
     *
     * @param entryObj        Json Object
     * @param path            keyPath
     * @param contentCallback content callback
     */
    protected static void findContent(JSONObject entryObj, String path, ContentCallback contentCallback) {
        String[] arrayString = path.split("\\.");
        getContent(arrayString, entryObj, contentCallback);
    }


    /**
     * @param arrayString     list of keys available
     * @param entryObj        entry object
     * @param contentCallback content callback
     */
    private static void getContent(String[] arrayString, JSONObject entryObj, ContentCallback contentCallback) {
        if (arrayString != null && arrayString.length != 0) {
            String path = arrayString[0];
            if (arrayString.length == 1) {
                Object varContent = entryObj.opt(path);
                if (varContent instanceof String || varContent instanceof JSONArray || varContent instanceof JSONObject) {
                    entryObj.put(path, contentCallback.contentObject(varContent));
                }
            } else {
                List<String> list = new ArrayList<>(Arrays.asList(arrayString));
                list.remove(path);
                String[] newArrayString = list.toArray(new String[0]);
                if (entryObj.opt(path) instanceof JSONObject) {
                    getContent(newArrayString, entryObj.optJSONObject(path), contentCallback);
                } else if (entryObj.opt(path) instanceof JSONArray) {
                    JSONArray jsonArray = entryObj.optJSONArray(path);
                    for (int idx = 0; idx < jsonArray.length(); idx++) {
                        getContent(newArrayString, jsonArray.optJSONObject(idx), contentCallback);
                    }
                }
            }
        }
    }


    protected static String getStringOption(Option option, Metadata metadata, JSONObject contentToPass) {
        String stringOption = option.renderOptions(contentToPass, metadata);
        if (stringOption == null) {
            DefaultOption defaultOptions = new DefaultOption();
            stringOption = defaultOptions.renderOptions(contentToPass, metadata);
        }
        return stringOption;
    }


    protected static void getEmbeddedObjects(Document html, MetadataCallback metadataCallback) {
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


    protected static Object enumerateContents(JSONArray contentArray, Option renderObject, MetaToEmbedCallback item) {
        JSONArray jsonArrayRTEContent = new JSONArray();
        for (Object RTE : contentArray) {
            JSONObject jsonObject = (JSONObject) RTE;
            String renderContent = enumerateContent(jsonObject, renderObject, item);
            jsonArrayRTEContent.put(renderContent);
        }
        return jsonArrayRTEContent;
    }

    protected static String enumerateContent(JSONObject jsonObject, Option renderObject, MetaToEmbedCallback item) {
        if (jsonObject.length() > 0 && jsonObject.has("type") && jsonObject.has("children")) {
            if (jsonObject.opt("type").equals("doc")) {
                return _doRawProcessing(jsonObject.optJSONArray("children"), renderObject, item);
            }
        }
        return "";
    }


    private static String _doRawProcessing(@NotNull JSONArray children, Option renderObject, MetaToEmbedCallback embedItem) {
        StringBuilder stringBuilder = new StringBuilder();
        children.forEach(item -> {
            JSONObject child;
            if (item instanceof JSONObject) {
                child = (JSONObject) item;
                stringBuilder.append(_extractKeys(child, renderObject, embedItem));
            }
        });
        return stringBuilder.toString();
    }


    private static String _extractKeys(@NotNull JSONObject jsonNode, Option renderObject, MetaToEmbedCallback embedItem) {

        if (!jsonNode.has("type") && jsonNode.has("text")) {
            return NodeToHTML.textNodeToHTML(jsonNode, renderObject);
        } else if (jsonNode.has("type")) {
            String nodeType = jsonNode.optString("type");
            if (nodeType.equalsIgnoreCase("reference")) {
                JSONObject attrObj = jsonNode.optJSONObject("attrs");
                String attrType = attrObj.optString("type");
                com.contentstack.utils.helper.Metadata metadata;

                if (attrType.equalsIgnoreCase("asset")) {
                    String text = attrObj.optString("text");
                    String uid = attrObj.optString("asset-uid");
                    String style = attrObj.optString("display-type");
                    metadata = new com.contentstack.utils.helper.Metadata(text, attrType, uid,
                            "asset", style, "", new Attributes());
                } else {
                    String text = attrObj.optString("text");
                    String uid = attrObj.optString("entry-uid");
                    String contentType = attrObj.optString("content-type-uid");
                    String style = attrObj.optString("display-type");
                    metadata = new com.contentstack.utils.helper.Metadata(text, attrType, uid,
                            contentType, style, "", new Attributes());
                }

                Optional<JSONObject> filteredContent = embedItem.toEmbed(metadata);
                if (filteredContent.isPresent()) {
                    JSONObject contentToPass = filteredContent.get();
                    return getStringOption(renderObject, metadata, contentToPass);
                }

            } else {
                return renderObject.renderNode(nodeType, jsonNode, nodeJsonArray -> _doRawProcessing(nodeJsonArray, renderObject, embedItem));
            }
        }
        return "";
    }

}
