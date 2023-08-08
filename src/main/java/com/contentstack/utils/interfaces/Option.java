package com.contentstack.utils.interfaces;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.node.MarkType;
import org.json.JSONObject;


// The `Option` interface defines a set of methods that can be implemented by classes to provide
// rendering options for different types of content.
public interface Option {

    /**
     * The function takes in a JSON object and metadata, and returns a string representation of the
     * options rendered from the embedded object.
     *
     * @param embeddedObject The embeddedObject parameter is a JSONObject that contains the data to be
     *                       rendered. It can be any valid JSON object that you want to render or display in some way.
     * @param metadata       Metadata is an object that contains additional information about the embedded
     *                       object. It can include properties such as the object's type, size, creation date, and any other
     *                       relevant information.
     * @return The method is returning a String.
     */
    String renderOptions(JSONObject embeddedObject, Metadata metadata);


    /**
     * The function "renderMark" takes a MarkType and a String as input and returns a formatted version
     * of the String based on the MarkType.
     *
     * @param markType   The markType parameter is of type MarkType, which is an enumeration representing
     *                   different types of marks that can be applied to a text.
     * @param renderText The `renderText` parameter is a string that represents the text that needs to
     *                   be rendered.
     * @return The renderMark method returns a string.
     */
    String renderMark(MarkType markType, String renderText);


    /**
     * The function "renderNode" takes in a node type, a JSON object representing the node, and a
     * callback function, and returns a string.
     *
     * @param nodeType   A string representing the type of the node. This could be any valid string value
     *                   that identifies the type of the node, such as "div", "span", "p", etc.
     * @param nodeObject The `nodeObject` parameter is a JSONObject that represents a node in a tree
     *                   structure. It contains information about the node, such as its properties and children.
     * @param callback   The callback parameter is a function that will be called after the node has been
     *                   rendered. It allows you to perform additional actions or manipulate the rendered node in some
     *                   way.
     * @return The method is returning a String.
     */
    String renderNode(String nodeType, JSONObject nodeObject, NodeCallback callback);
}

