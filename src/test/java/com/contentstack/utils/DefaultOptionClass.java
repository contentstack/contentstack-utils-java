package com.contentstack.utils;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.interfaces.NodeCallback;
import com.contentstack.utils.node.MarkType;
import com.contentstack.utils.render.DefaultOption;
import org.json.JSONObject;

public class DefaultOptionClass extends DefaultOption {

    @Override
    public String renderOptions(JSONObject embeddedObject, Metadata metadata) {
        switch (metadata.getStyleType()) {
            case BLOCK:
                return "<p>" + embeddedObject.getString("title") + "</p><span>" +
                        embeddedObject.getString("multi") + "</span>";
            case INLINE:
                return "<p>" + embeddedObject.getString("title") + "</p><span>" +
                        embeddedObject.getString("line") + "</span>";
            case LINK:
                return "<p>" + embeddedObject.getString("title") + "</p><span>" +
                        embeddedObject.getString("key") + "</span>";
            case DISPLAY:
                return "<p>" + embeddedObject.getString("someTitle") + "</p><span>" +
                        embeddedObject.getString("multi") + "</span>";
            default:
                return super.renderOptions(embeddedObject, metadata);
        }
    }

    @Override
    public String renderMark(MarkType markType, String renderText) {
        if (markType == MarkType.BOLD) {
            return "<b>" + renderText + "</b>";
        }
        return super.renderMark(markType, renderText);
    }

    @Override
    public String renderNode(String nodeType, JSONObject nodeObject, NodeCallback callback) {
        if (nodeType.equalsIgnoreCase("paragraph")) {
            String children = callback.renderChildren(nodeObject.optJSONArray("children"));
            return "<p class='class-id'>" + children + "</p>";
        }
        return super.renderNode(nodeType, nodeObject, callback);
    }
}
