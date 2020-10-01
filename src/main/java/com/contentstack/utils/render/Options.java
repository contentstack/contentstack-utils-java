package com.contentstack.utils.render;
import com.contentstack.utils.embedded.StyleType;
import org.json.JSONObject;

public interface Options {
    String renderOptions(StyleType type, JSONObject embeddedObject, String linkText);
}

