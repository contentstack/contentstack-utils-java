package com.contentstack.utils.callbacks;
import com.contentstack.utils.helper.Metadata;
import org.json.JSONObject;
import org.jsoup.nodes.Attributes;

public interface Options {
    //String renderOptions(StyleType type, JSONObject embeddedObject, Attributes attributes);

    String renderOptions( JSONObject embeddedObject, Metadata metadata);
}

