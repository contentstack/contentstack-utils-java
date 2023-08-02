package com.contentstack.utils.render;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.interfaces.NodeCallback;
import com.contentstack.utils.interfaces.Option;
import com.contentstack.utils.node.MarkType;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class DefaultOption implements Option {

    /**
     * Accepts below params to provide defaults options
     *
     * @param embeddedObject
     *         entry embedded object
     * @param metadata
     *         for of the entry object
     * @return String as a result
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

    @Override
    public String renderMark(MarkType markType, String text) {
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

    @Override
    public String renderNode(String nodeType, JSONObject nodeObject, NodeCallback callback) {

        String strAttrs = strAttrs(nodeObject);


        String children = callback.renderChildren(nodeObject.optJSONArray("children"));
        switch (nodeType) {
            case "p":
                return "<p" + strAttrs + ">" + children + "</p>";
            case "a":
                return "<a" + strAttrs + "href=\"" + escapeInjectHtml(nodeObject, "href") + "\">" + children + "</a>";
            case "img":
                String assetLink = getNodeStr(nodeObject, "asset-link");
                if (!assetLink.isEmpty()) {
                    return "<img" + strAttrs + "src=\"" + escapeInjectHtml(nodeObject, "asset-link") + "\" />" + children;
                }
                return "<img" + strAttrs + "src=\"" + escapeInjectHtml(nodeObject, "src") + "\" />" + children;
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


    String strAttrs(JSONObject nodeObject) {
        StringBuilder result = new StringBuilder();
        if (nodeObject.has("attrs")) {
            JSONObject attrsObject = nodeObject.optJSONObject("attrs");
            if (attrsObject != null && !attrsObject.isEmpty()) {
                for (String key : attrsObject.keySet()) {
                    String value = attrsObject.getString(key);
                    String[] ignoreKeys = {"href", "asset-link", "src", "url"};
                    ArrayList<String> ignoreKeysList = new ArrayList<>(Arrays.asList(ignoreKeys));
                    if (!ignoreKeysList.contains(key)) {
                        result.append(" ").append(key).append("=\"").append(value).append("\"");
                    }
                }
            }
        }
        return result.toString();
    }


    private String getNodeStr(JSONObject nodeObject, String key) {
        String herf = nodeObject.optJSONObject("attrs").optString(key); // key might be [href/src]
        if (herf == null || herf.isEmpty()) {
            herf = nodeObject.optJSONObject("attrs").optString("url");
        }
        return herf;
    }


    /**
     * Returns Title From The Embedded Object of type entry
     *
     * @param embeddedObject
     *         JSONObject
     * @return String
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
     * Returns Title From The Embedded Object of type asset
     *
     * @param embeddedObject
     *         JSONObject
     * @return String
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
