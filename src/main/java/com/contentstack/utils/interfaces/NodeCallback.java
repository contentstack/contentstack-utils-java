package com.contentstack.utils.interfaces;

import org.json.JSONArray;

/**
 * The interface Node callback.
 */
public interface NodeCallback {
    /**
     * Render children string.
     *
     * @param nodeJsonArray the node json array
     * @return the string
     */
    String renderChildren(JSONArray nodeJsonArray);
}
