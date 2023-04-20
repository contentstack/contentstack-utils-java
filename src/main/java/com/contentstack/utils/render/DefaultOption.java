package com.contentstack.utils.render;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.interfaces.NodeCallback;
import com.contentstack.utils.interfaces.Option;
import com.contentstack.utils.node.MarkType;
import org.json.JSONObject;


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

    @Override
    public String renderNode(String nodeType, JSONObject nodeObject, NodeCallback callback) {
        String children = callback.renderChildren(nodeObject.optJSONArray("children"));
        switch (nodeType) {
            case "p":
                return "<p>" + children + "</p>";
            case "a":
                return "<a href=\"" + getNodeStr(nodeObject, "href") + "\">" + children + "</a>";
            case "img":
                return "<img src=\"" + getNodeStr(nodeObject, "src") + "\" />" + children;
            case "embed":
                return "<iframe src=\"" + getNodeStr(nodeObject, "src") + "\"" + children + "</iframe>";
            case "h1":
                return "<h1>" + children + "</h1>";
            case "h2":
                return "<h2>" + children + "</h2>";
            case "h3":
                return "<h3>" + children + "</h3>";
            case "h4":
                return "<h4>" + children + "</h4>";
            case "h5":
                return "<h5>" + children + "</h5>";
            case "h6":
                return "<h6>" + children + "</h6>";
            case "ol":
                return "<ol>" + children + "</ol>";
            case "ul":
                return "<ul>" + children + "</ul>";
            case "li":
                return "<li>" + children + "</li>";
            case "hr":
                return "<hr />";
            case "table":
                return "<table>" + children + "</table>";
            case "thead":
                return "<thead>" + children + "</thead>";
            case "tbody":
                return "<tbody>" + children + "</tbody>";
            case "tfoot":
                return "<tfoot>" + children + "</tfoot>";
            case "tr":
                return "<tr>" + children + "</tr>";
            case "th":
                return "<th>" + children + "</th>";
            case "td":
                return "<td>" + children + "</td>";
            case "blockquote":
                return "<blockquote>" + children + "</blockquote>";
            case "code":
                return "<code>" + children + "</code>";
            case "reference":
                return "";
            default:
                return children;
        }
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
