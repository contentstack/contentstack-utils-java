package com.contentstack.utils.interfaces;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.node.MarkType;
import org.json.JSONObject;


/**
 * The interface Option.
 */
public interface Option {
    /**
     * Render options string.
     *
     * @param embeddedObject
     *         the embedded object
     * @param metadata
     *         the metadata
     * @return the string
     */
    String renderOptions(JSONObject embeddedObject, Metadata metadata);

    /**
     * Render mark string.
     *
     * @param markType
     *         the mark type
     * @param renderText
     *         the render text
     * @return the string
     */
    String renderMark(MarkType markType, String renderText);

    /**
     * Render node string.
     *
     * @param nodeType
     *         the node type
     * @param nodeObject
     *         the node object
     * @param callback
     *         the callback
     * @return the string
     */
    String renderNode(String nodeType, JSONObject nodeObject, NodeCallback callback);
}

