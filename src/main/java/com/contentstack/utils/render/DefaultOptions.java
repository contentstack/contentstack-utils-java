package com.contentstack.utils.render;
import com.contentstack.utils.embedded.StyleType;
import org.json.JSONObject;

public class DefaultOptions implements Options {

    @Override
    public String renderOptions(StyleType type, JSONObject embeddedObject, String linkText) {
        switch (type) {
            case BLOCK:
                return "<div><p>${entry.title || entry.uid}</p><div>p type: <span>${entry._content_type_uid}</span></p></div>";
            case INLINE:
                return "<span>${entry.title || entry.uid}</span>";
            case LINKED:
                return "<a href=\"${entry.url}\">${entry.title || entry.uid}</a>";
            case DOWNLOADABLE:
                return "<a href=\"${asset.url}\">${asset.title || asset.filename || asset.uid}</a>";
            case DISPLAYABLE:
                return "<img src=\"${asset.url}\" alt=\"${asset.title || asset.filename || asset.uid}\" />";
            default:
                return "";
        }

    }
}
