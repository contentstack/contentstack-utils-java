package com.contentstack.utils;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.interfaces.ContentCallback;
import com.contentstack.utils.interfaces.MetaToEmbedCallback;
import com.contentstack.utils.interfaces.MetadataCallback;
import com.contentstack.utils.interfaces.Option;
import com.contentstack.utils.node.NodeToHTML;
import com.contentstack.utils.render.DefaultOption;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AutomateCommon {


    private static final String ASSET = "asset";

    private AutomateCommon() {
        throw new IllegalStateException("Not allowed to create instance of AutomateCommon");
    }

    /**
     * The function "findContent" splits a given path string into an array of strings, and then calls the
     * "getContent" function with the array, a JSONObject, and a contentCallback as parameters.
     *
     * @param entryObj        A JSONObject that contains the data you want to search through.
     * @param path            The `path` parameter is a string that represents the path to a specific content in a
     *                        JSON object. It is used to navigate through the JSON object and find the desired content.
     * @param contentCallback The `contentCallback` parameter is an instance of the `ContentCallback`
     *                        interface. It is used to provide a callback mechanism for handling the content found during the
     *                        search process. The `ContentCallback` interface typically defines a method that will be called with
     *                        the content found at each step of the search.
     */
    protected static void findContent(JSONObject entryObj, String path, ContentCallback contentCallback) {
        String[] arrayString = path.split("\\.");
        getContent(arrayString, entryObj, contentCallback);
    }


    /**
     * The function `getContent` recursively traverses a JSON object and modifies its content based on
     * a callback function.
     *
     * @param arrayString     An array of strings representing the path to the content in the JSON object.
     * @param entryObj        The `entryObj` parameter is a JSONObject that contains the data to be processed.
     * @param contentCallback The `contentCallback` parameter is an instance of the `ContentCallback`
     *                        interface. This interface defines a method called `contentObject()` which takes an object as
     *                        input and returns a modified version of that object. The purpose of this callback is to allow
     *                        the caller of the `getContent()` method to customize
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


    /**
     * The function returns a string representation of an option by rendering it with the provided
     * content and metadata, falling back to default options if necessary.
     *
     * @param option        The "option" parameter is an object of type Option, which represents a specific
     *                      option that can be rendered. It likely has properties and methods that allow it to generate the
     *                      desired output based on the provided content and metadata.
     * @param metadata      Metadata is an object that contains additional information about the option. It
     *                      may include details such as the option's label, description, and any other relevant information.
     * @param contentToPass The `contentToPass` parameter is a `JSONObject` that contains the data or
     *                      content that needs to be passed to the `renderOptions` method of the `Option` or `DefaultOption`
     *                      class. This data or content is used to generate the string representation of the option.
     * @return The method is returning a string value.
     */
    protected static String getStringOption(Option option, Metadata metadata, JSONObject contentToPass) {
        String stringOption = option.renderOptions(contentToPass, metadata);
        if (stringOption == null) {
            DefaultOption defaultOptions = new DefaultOption();
            stringOption = defaultOptions.renderOptions(contentToPass, metadata);
        }
        return stringOption;
    }


    /**
     * The function retrieves embedded objects from an HTML document and passes them to a callback
     * function for further processing.
     *
     * @param html             The "html" parameter is a Document object that represents an HTML document. It is
     *                         used to extract embedded objects from the HTML document.
     * @param metadataCallback The `metadataCallback` parameter is an instance of a class that
     *                         implements the `MetadataCallback` interface. This interface defines a method called
     *                         `embeddedObject` which is called for each embedded object found in the HTML document. The
     *                         `embeddedObject` method takes a `Metadata` object as a parameter,
     */
    protected static void getEmbeddedObjects(Document html, MetadataCallback metadataCallback) {
        Elements embeddedEntries = html.body().getElementsByClass("embedded-entry");
        Elements embeddedAssets = html.body().getElementsByClass("embedded-asset");
        embeddedEntries.forEach(entry -> {
            String text = entry.text();
            String type = entry.attr("type");
            String uid = entry.attr("data-sys-entry-uid");
            String contentType = entry.attr("data-sys-content-type-uid");
            String style = entry.attr("sys-style-type");
            String outerHTML = entry.outerHtml();
            Metadata metadata = new Metadata(text, type, uid, contentType, style, outerHTML, entry.attributes());
            metadataCallback.embeddedObject(metadata);
        });

        embeddedAssets.forEach(asset -> {
            String text = asset.text();
            String type = asset.attr("type");
            String uid = asset.attr("data-sys-asset-uid");
            String style = asset.attr("sys-style-type");
            String outerHTML = asset.outerHtml();
            Metadata metadata = new Metadata(text, type, uid, ASSET, style, outerHTML, asset.attributes());
            metadataCallback.embeddedObject(metadata);
        });
    }


    /**
     * The function takes a JSONArray of content, iterates through each element, and calls another
     * function to render and enumerate the content, returning a JSONArray of the rendered content.
     *
     * @param contentArray A JSONArray containing the content to be enumerated.
     * @param renderObject The renderObject parameter is an instance of the Option class.
     * @param item         The "item" parameter is a callback function that takes in a "Meta" object and
     *                     returns a value to be embedded in the rendered content.
     * @return The method is returning a JSONArray object.
     */
    protected static Object enumerateContents(JSONArray contentArray, Option renderObject, MetaToEmbedCallback item) {
        JSONArray jsonArrayRTEContent = new JSONArray();
        for (Object RTE : contentArray) {
            JSONObject jsonObject = (JSONObject) RTE;
            String renderContent = enumerateContent(jsonObject, renderObject, item);
            jsonArrayRTEContent.put(renderContent);
        }
        return jsonArrayRTEContent;
    }

    /**
     * The function checks if a JSON object has a specific type and children, and if so, it performs
     * some processing on the children.
     *
     * @param jsonObject   A JSONObject that contains the content to be enumerated.
     * @param renderObject The `renderObject` parameter is of type `Option`. It is used as an argument
     *                     in the `doRawProcessing` method.
     * @param item         The "item" parameter is a callback function that takes in a "Meta" object and returns
     *                     a string.
     * @return An empty string.
     */
    protected static String enumerateContent(JSONObject jsonObject, Option renderObject, MetaToEmbedCallback item) {
        if (!jsonObject.isEmpty() && jsonObject.has("type") && jsonObject.has("children")) {
            if (jsonObject.opt("type").equals("doc")) {
                return doRawProcessing(jsonObject.optJSONArray("children"), renderObject, item);
            }
        }
        return "";
    }


    /**
     * The function `doRawProcessing` processes a JSONArray of children, extracting keys from each
     * child and appending the results to a StringBuilder, then returns the final string.
     *
     * @param children     A JSONArray containing the children items to process.
     * @param renderObject The parameter "renderObject" is of type "Option". It is used as an option to
     *                     specify how the keys should be rendered during processing.
     * @param embedItem    The `embedItem` parameter is a callback function that takes a `Meta` object as
     *                     input and returns an embedded representation of that object.
     * @return The method is returning a string.
     */
    private static String doRawProcessing(@NotNull JSONArray children, Option renderObject, MetaToEmbedCallback embedItem) {
        StringBuilder stringBuilder = new StringBuilder();
        children.forEach(item -> {
            JSONObject child;
            if (item instanceof JSONObject) {
                child = (JSONObject) item;
                stringBuilder.append(extractKeys(child, renderObject, embedItem));
            }
        });
        return stringBuilder.toString();
    }


    /**
     * The function extracts keys from a JSON object and returns a string based on the type of the
     * node.
     *
     * @param jsonNode     The `jsonNode` parameter is a `JSONObject` that represents a node in a JSON
     *                     structure. It is used to extract information from the node and perform certain operations based
     *                     on its properties.
     * @param renderObject The `renderObject` parameter is an object of type `Option`. It is used to
     *                     specify rendering options for the extracted keys.
     * @param embedItem    The `embedItem` parameter is a callback function that takes a `Metadata` object
     *                     as input and returns an optional `JSONObject`. This callback is used to embed metadata into the
     *                     rendered content.
     * @return The method is returning a String.
     */
    private static String extractKeys(@NotNull JSONObject jsonNode, Option renderObject, MetaToEmbedCallback embedItem) {

        if (!jsonNode.has("type") && jsonNode.has("text")) {
            return NodeToHTML.textNodeToHTML(jsonNode, renderObject);
        } else if (jsonNode.has("type")) {
            String nodeType = jsonNode.optString("type");
            if (nodeType.equalsIgnoreCase("reference")) {
                JSONObject attrObj = jsonNode.optJSONObject("attrs");
                String attrType = attrObj.optString("type");
                com.contentstack.utils.helper.Metadata metadata;

                if (attrType.equalsIgnoreCase(ASSET)) {
                    String text = attrObj.optString("text");
                    String uid = attrObj.optString("asset-uid");
                    String style = attrObj.optString("display-type");
                    metadata = new com.contentstack.utils.helper.Metadata(text, attrType, uid,
                            ASSET, style, "", new Attributes());
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
                } else {
                    if (attrType.equalsIgnoreCase(ASSET)) {
                        return renderObject.renderNode("img", jsonNode, nodeJsonArray -> doRawProcessing(nodeJsonArray, renderObject, embedItem));
                    }
                }

            } else {
                return renderObject.renderNode(nodeType, jsonNode, nodeJsonArray -> doRawProcessing(nodeJsonArray, renderObject, embedItem));
            }
        }
        return "";
    }

}
