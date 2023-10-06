package com.contentstack.utils.node;

import com.contentstack.utils.interfaces.Option;
import org.json.JSONObject;


/**
 * The NodeToHTML class provides a method to convert a text node in JSON format to an HTML string,
 * applying various rendering options.
 */
public class NodeToHTML {


    private NodeToHTML() {
        throw new IllegalStateException("Could not create instance of NodeToHTML");
    }


    /**
     * The function converts a JSON object representing a text node into HTML, applying various
     * formatting options based on the provided renderOption.
     *
     * @param nodeText     The `nodeText` parameter is a JSONObject that represents a text node. It
     *                     contains various options for rendering the text, such as superscript, subscript, inline code,
     *                     strikethrough, underline, italic, bold, and line break.
     * @param renderOption The renderOption parameter is an object of type Option. It is used to
     *                     specify the rendering options for the text node.
     * @return The method returns the modified text after applying the specified rendering options.
     */
    public static String textNodeToHTML(JSONObject nodeText, Option renderOption) {
        String text = nodeText.optString("text");
        text = text.replace("\n", "");
        if (nodeText.has("superscript")) {
            text = renderOption.renderMark(MarkType.SUPERSCRIPT, text);
        }
        if (nodeText.has("subscript")) {
            text = renderOption.renderMark(MarkType.SUBSCRIPT, text);
        }
        if (nodeText.has("inlineCode")) {
            text = renderOption.renderMark(MarkType.INLINECODE, text);
        }
        if (nodeText.has("strikethrough")) {
            text = renderOption.renderMark(MarkType.STRIKETHROUGH, text);
        }
        if (nodeText.has("underline")) {
            text = renderOption.renderMark(MarkType.UNDERLINE, text);
        }
        if (nodeText.has("italic")) {
            text = renderOption.renderMark(MarkType.ITALIC, text);
        }
        if (nodeText.has("bold")) {
            text = renderOption.renderMark(MarkType.BOLD, text);
        }
        if (nodeText.has("break")) {
            text = renderOption.renderMark(MarkType.BREAK, text);
        }
        return text;
    }

}
