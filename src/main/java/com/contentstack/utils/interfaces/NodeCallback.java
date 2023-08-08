package com.contentstack.utils.interfaces;

import org.json.JSONArray;

/**
 * The interface Node callback.
 */
public interface NodeCallback {

    /**
     * The function takes a JSONArray of nodes and returns a string representation of their children.
     *
     * @param nodeJsonArray The `nodeJsonArray` parameter is a JSONArray object that contains a
     *                      collection of JSON objects representing nodes. Each JSON object represents a node and contains
     *                      information about the node's properties and children.
     * @return The method is returning a String.
     */
    String renderChildren(JSONArray nodeJsonArray);
}
