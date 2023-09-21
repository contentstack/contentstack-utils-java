package com.contentstack.utils.render;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.interfaces.NodeCallback;
import com.contentstack.utils.interfaces.Option;
import com.contentstack.utils.node.MarkType;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import java.util.*;


public class DefaultOption implements Option {

    /**
     * The function `renderOptions` takes in a JSON object and metadata and returns a string based on the
     * style type of the metadata.
     *
     * @param embeddedObject The embeddedObject parameter is a JSONObject that contains the data of the
     *                       embedded object. It may have different properties depending on the type of object being embedded.
     * @param metadata       The `metadata` parameter is an object of type `Metadata` which contains information
     *                       about the style type of the embedded object. It has a method `getStyleType()` which returns the
     *                       style type of the embedded object.
     * @return The method is returning a string based on the value of the `metadata.getStyleType()`
     * parameter. The returned string depends on the style type and the content of the `embeddedObject`.
     */
    @Override
    public String renderOptions(JSONObject embeddedObject, Metadata metadata) {
        switch (metadata.getStyleType()) {
            case BLOCK:
                return "<div><p>" + findTitleOrUid(embeddedObject) + "</p><div><p>Content type: <span>" + embeddedObject.optString("_content_type_uid") + "</span></p></div>";
            case INLINE:
                return "<span>" + findTitleOrUid(embeddedObject) + "</span>";
            case LINK:
                return "<a href=\"" + embeddedObject.optString("url") + "\">" + findTitleOrUid(embeddedObject) + "</a>";
            case DISPLAY:
                return "<img src=\"" + embeddedObject.optString("url") + "\" alt=\"" + findAssetTitle(embeddedObject) + "\" />";
            default:
                return "";
        }
    }

    /**
     * The function takes a mark type and text as input and returns the text wrapped in HTML tags based on
     * the mark type.
     *
     * @param markType The `markType` parameter is of type `MarkType` and represents the type of formatting
     *                 to be applied to the `text` parameter. The `MarkType` enum contains the following values:
     * @param text     The `text` parameter is a string that represents the content that needs to be rendered
     *                 with the specified mark type.
     * @return The method returns a string that represents the given text with the specified mark type
     * applied. If the mark type is not recognized, the method returns the original text without any
     * modifications.
     */
    @Override
    public String renderMark(MarkType markType, String text) {
        // Replace "\n" with "<br/>" tags
        text = renderHtmlWithLineBreaks(text);
        switch (markType) {
            case SUPERSCRIPT:
                return "<sup>" + text + "</sup>";
            case SUBSCRIPT:
                return "<sub>" + text + "</sub>";
            case INLINECODE:
                return "<span>" + text + "</span>";
            case STRIKETHROUGH:
                return "<strike>" + text + "</strike>";
            case UNDERLINE:
                return "<u>" + text + "</u>";
            case ITALIC:
                return "<em>" + text + "</em>";
            case BOLD:
                return "<strong>" + text + "</strong>";
            case BREAK:
                return "<br />" + text;
            default:
                return text;
        }
    }

    private String escapeInjectHtml(JSONObject nodeObj, String nodeType) {
        String injectedHtml = getNodeStr(nodeObj, nodeType);
        return StringEscapeUtils.escapeHtml4(injectedHtml);
    }

    /**
     * The function takes in a node type and a JSON object representing a node, and returns the
     * corresponding HTML string representation of the node.
     *
     * @param nodeType   The `nodeType` parameter is a String that represents the type of HTML element to
     *                   be rendered. It can have values such as "p", "a", "img", "embed", "h1", "h2", "h3", "h4", "h5",
     *                   "h
     * @param nodeObject The `nodeObject` parameter is a JSONObject that contains the properties and
     *                   values of a node in a tree structure. It represents a specific node in the tree that needs to be
     *                   rendered.
     * @param callback   The `callback` parameter is an instance of the `NodeCallback` interface. It is
     *                   used to render the children of the current node. The `renderChildren` method of the `callback`
     *                   is called with the `children` JSON array of the current node as the argument. The `renderChildren
     * @return The method `renderNode` returns a string representation of an HTML element based on the
     * given `nodeType` and `nodeObject`.
     */
    @Override
    public String renderNode(String nodeType, JSONObject nodeObject, NodeCallback callback) {

        String strAttrs = strAttrs(nodeObject);

        String children = callback.renderChildren(nodeObject.optJSONArray("children"));

        switch (nodeType) {
            case "p":
                return "<p" + strAttrs + ">" + children + "</p>";
            case "a":
                return "<a" + strAttrs + " href=\"" + escapeInjectHtml(nodeObject, "href") + "\">" + children + "</a>";
            case "img":
                String assetLink = getNodeStr(nodeObject, "asset-link");
                if (!assetLink.isEmpty()) {
                    return "<img" + strAttrs + " src=\"" + escapeInjectHtml(nodeObject, "asset-link") + "\" />" + children;
                }
                return "<img" + strAttrs + " src=\"" + escapeInjectHtml(nodeObject, "src") + "\" />" + children;
            case "embed":
                return "<iframe" + strAttrs + " src=\"" + escapeInjectHtml(nodeObject, "src") + "\"" + children + "</iframe>";
            case "h1":
                return "<h1" + strAttrs + ">" + children + "</h1>";
            case "h2":
                return "<h2" + strAttrs + ">" + children + "</h2>";
            case "h3":
                return "<h3" + strAttrs + ">" + children + "</h3>";
            case "h4":
                return "<h4" + strAttrs + ">" + children + "</h4>";
            case "h5":
                return "<h5" + strAttrs + ">" + children + "</h5>";
            case "h6":
                return "<h6" + strAttrs + ">" + children + "</h6>";
            case "ol":
                return "<ol" + strAttrs + ">" + children + "</ol>";
            case "ul":
                return "<ul" + strAttrs + ">" + children + "</ul>";
            case "li":
                return "<li" + strAttrs + ">" + children + "</li>";
            case "hr":
                return "<hr" + strAttrs + " />";
            case "table":
                return "<table " + strAttrs + ">" + children + "</table>";
            case "thead":
                return "<thead " + strAttrs + ">" + children + "</thead>";
            case "tbody":
                return "<tbody" + strAttrs + ">" + children + "</tbody>";
            case "tfoot":
                return "<tfoot" + strAttrs + ">" + children + "</tfoot>";
            case "tr":
                return "<tr" + strAttrs + ">" + children + "</tr>";
            case "th":
                return "<th" + strAttrs + ">" + children + "</th>";
            case "td":
                return "<td" + strAttrs + ">" + children + "</td>";
            case "blockquote":
                return "<blockquote" + strAttrs + ">" + children + "</blockquote>";
            case "code":
                return "<code" + strAttrs + ">" + children + "</code>";
            case "reference":
                return "";
            default:
                return children;
        }
    }


    /**
     * Returns the string replacing </n> is with the <br/> tags
     *
     * @param content the content
     * @return string with br tags
     * @apiNote the support for the br tags are included
     * @since v1.3.0
     */
    private String renderHtmlWithLineBreaks(String content) {
        // Replace "\n" with "<br/>" tags
        String htmlContent = content.replaceAll("\\n", "<br />");

        // Now, you can render the HTML content
        // (You can use your rendering method here, e.g., send it to a WebView or display it in a GUI component)

        // For demonstration purposes, let's just print it
        System.out.println(htmlContent);

        return htmlContent;
    }


    /**
     * The function takes a JSONObject as input and returns a string containing the attributes and
     * their values, excluding certain keys.
     *
     * @param nodeObject A JSONObject representing a node in a tree structure.
     * @return The method is returning a string representation of the attributes (key-value pairs) in
     * the given JSONObject, excluding certain keys specified in the ignoreKeys array.
     */
    String strAttrs(JSONObject nodeObject) {
        StringBuilder result = new StringBuilder();
        if (nodeObject.has("attrs")) {
            JSONObject attrsObject = nodeObject.optJSONObject("attrs");
            if (attrsObject != null && !attrsObject.isEmpty()) {
                for (String key : attrsObject.keySet()) {
                    Object objValue = attrsObject.opt(key);
                    String value = objValue.toString();
                    // If style is available, do styling calculations
                    if (Objects.equals(key, "style")) {
                        String resultStyle = stringifyStyles(attrsObject.optJSONObject("style"));
                        result.append(" ").append(key).append("=\"").append(resultStyle).append("\"");
                    } else {
                        String[] ignoreKeys = {"href", "asset-link", "src", "url"};
                        ArrayList<String> ignoreKeysList = new ArrayList<>(Arrays.asList(ignoreKeys));
                        if (!ignoreKeysList.contains(key)) {
                            result.append(" ").append(key).append("=\"").append(value).append("\"");
                        }
                    }
                }
            }
        }
        return result.toString();
    }


    private String stringifyStyles(JSONObject style) {
        Map<String, String> styleMap = new HashMap<>();

        // Convert JSONObject to a Map
        Iterator<String> keys = style.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = style.getString(key);
            styleMap.put(key, value);
        }

        StringBuilder styleString = new StringBuilder();

        for (Map.Entry<String, String> entry : styleMap.entrySet()) {
            String property = entry.getKey();
            String value = entry.getValue();

            styleString.append(property).append(": ").append(value).append("; ");
        }

        return styleString.toString();
    }


    /**
     * The function retrieves the value of a specified key from a JSONObject, and if it is empty or
     * null, it retrieves the value of the "url" key instead.
     *
     * @param nodeObject A JSONObject representing a node in a data structure.
     * @param key        The "key" parameter is a string that represents the key to be used to retrieve a
     *                   value from the "attrs" JSONObject. It could be either "href" or "src".
     * @return The method is returning the value of the "href" or "src" key from the "attrs" JSONObject
     * of the given "nodeObject". If the value is null or empty, it will return the value of the "url"
     * key from the "attrs" JSONObject.
     */
    private String getNodeStr(JSONObject nodeObject, String key) {
        String herf = nodeObject.optJSONObject("attrs").optString(key); // key might be [href/src]
        if (herf == null || herf.isEmpty()) {
            herf = nodeObject.optJSONObject("attrs").optString("url");
        }
        return herf;
    }


    /**
     * The function finds the title or uid value from a given JSONObject.
     *
     * @param embeddedObject The embeddedObject parameter is a JSONObject that contains data.
     * @return The method is returning a String value, which is either the value of the "title" key in
     * the embeddedObject JSONObject, or the value of the "uid" key if the "title" key is not present
     * or is empty.
     */
    protected String findTitleOrUid(JSONObject embeddedObject) {
        String _title = "";
        if (embeddedObject != null) {
            if (embeddedObject.has("title") && !embeddedObject.optString("title").isEmpty()) {
                _title = embeddedObject.optString("title");
            } else if (embeddedObject.has("uid")) {
                _title = embeddedObject.optString("uid");
            }
        }
        return _title;
    }


    /**
     * The function finds the title of an asset from a given JSON object.
     *
     * @param embeddedObject The embeddedObject parameter is a JSONObject that contains information
     *                       about an asset.
     * @return The method is returning a String value, which is the title of the asset.
     */
    protected String findAssetTitle(JSONObject embeddedObject) {
        String _title = "";
        if (embeddedObject != null) {
            if (embeddedObject.has("title") && !embeddedObject.optString("title").isEmpty()) {
                _title = embeddedObject.optString("title");
            } else if (embeddedObject.has("filename")) {
                _title = embeddedObject.optString("filename");
            } else if (embeddedObject.has("uid")) {
                _title = embeddedObject.optString("uid");
            }
        }
        return _title;
    }
}
