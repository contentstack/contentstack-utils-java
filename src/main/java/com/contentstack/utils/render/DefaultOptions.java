package com.contentstack.utils.render;
import com.contentstack.utils.embedded.StyleType;
import com.contentstack.utils.helper.Metadata;
import org.json.JSONObject;
import org.jsoup.nodes.Attributes;



public class DefaultOptions implements Options {

    /**
     * Accepts below params to provides defaults options
     * @param type enum of StyleType, To set what type of operation has to perform
     * @param embeddedObject JSONObject, Takes entry object
     * @param attributes KEY Attributes
     * @return String
     */
    @Override
    public String renderOptions(JSONObject embeddedObject, Metadata metadata) {

        switch (metadata.getStyleType()) {
            case BLOCK:
                return "<div><p>"+findTitleOrUid(embeddedObject)+"</p><div><p>Content type: <span>"+embeddedObject.optString("_content_type_uid")+"</span></p></div>";
            case INLINE:
                return "<span>"+findTitleOrUid(embeddedObject)+"</span>";
            case LINKED:
                return "<a href=\""+embeddedObject.optString("url")+"\">"+findTitleOrUid(embeddedObject)+"</a>";
            case DISPLAY:
                return "<img src=\""+embeddedObject.optString("url")+"\" alt=\""+findAssetTitle(embeddedObject)+"\" />";
            default:
                return "";
        }

    }


    /**
     * Returns Title From The Embedded Object of type entry
     * @param embeddedObject JSONObject
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
     * @param embeddedObject JSONObject
     * @return String
     */
    private String findAssetTitle(JSONObject embeddedObject) {
        String _title = "";
        if (embeddedObject != null) {
            if (embeddedObject.has("title") && !embeddedObject.optString("title").isEmpty()) {
                _title = embeddedObject.optString("title");
            }else if (embeddedObject.has("filename")) {
                _title = embeddedObject.optString("filename");
            } else if (embeddedObject.has("uid")) {
                _title = embeddedObject.optString("uid");
            }
        }
        return _title;
    }
}
