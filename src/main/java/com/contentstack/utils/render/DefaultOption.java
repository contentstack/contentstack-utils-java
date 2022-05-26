package com.contentstack.utils.render;

import com.contentstack.utils.callbacks.Options;
import com.contentstack.utils.helper.Metadata;
import org.json.JSONObject;

/**
 * DefaultOptionsCallback
 */
public class DefaultOption implements Options {

    /**
     * Accepts below params to provides defaults options
     *
     * @param embeddedObject
     *         entry embedded object
     * @param metadata
     *         for of the entry object
     * @return String as result
     */
    @Override
    public String renderOptions(JSONObject embeddedObject, Metadata metadata) {
        switch (metadata.getStyleType()) {
            case BLOCK:
                return "<div><p>" + findTitleOrUid(embeddedObject) + "</p><div><p>Content type: <span>"
                        + embeddedObject.optString("_content_type_uid") + "</span></p></div>";
            case INLINE:
                return "<span>" + findTitleOrUid(embeddedObject) + "</span>";
            case LINK:
                return "<a href=\"" + embeddedObject.optString("url") + "\">" + findTitleOrUid(embeddedObject) + "</a>";
            case DISPLAY:
                return "<img src=\"" + embeddedObject.optString("url") + "\" alt=\"" + findAssetTitle(embeddedObject)
                        + "\" />";
            default:
                return "";
        }
    }

    /**
     * Returns Title From The Embedded Object of type entry
     *
     * @param embeddedObject
     *         JSONObject
     * @return String
     */
    private String findTitleOrUid(JSONObject embeddedObject) {

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
    private String findAssetTitle(JSONObject embeddedObject) {
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
