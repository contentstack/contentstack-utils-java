package com.contentstack.utils.render;
import com.contentstack.utils.embedded.StyleType;
import org.json.JSONObject;
import org.jsoup.nodes.Attributes;



public interface Options {
    String renderOptions(StyleType type, JSONObject embeddedObject, Attributes attributes);
}

