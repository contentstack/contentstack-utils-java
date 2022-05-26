package com.contentstack.utils.callbacks;

import com.contentstack.utils.helper.Metadata;
import org.json.JSONObject;

public interface Options {
    String renderOptions(JSONObject embeddedObject, Metadata metadata);
}
