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
        //Sanitization of text
        String cleanedText = escapeTextNodes(text)
                    .replace("\n", "<br />")
                    .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
      
        if (nodeText.has("superscript")) {
            cleanedText = renderOption.renderMark(MarkType.SUPERSCRIPT, cleanedText);
        }
        if (nodeText.has("subscript")) {
            cleanedText = renderOption.renderMark(MarkType.SUBSCRIPT, cleanedText);
        }
        if (nodeText.has("inlineCode")) {
            cleanedText = renderOption.renderMark(MarkType.INLINECODE, cleanedText);
        }
        if (nodeText.has("strikethrough")) {
            cleanedText = renderOption.renderMark(MarkType.STRIKETHROUGH, cleanedText);
        }
        if (nodeText.has("underline")) {
            cleanedText = renderOption.renderMark(MarkType.UNDERLINE, cleanedText);
        }
        if (nodeText.has("italic")) {
            cleanedText = renderOption.renderMark(MarkType.ITALIC, cleanedText);
        }
        if (nodeText.has("bold")) {
            cleanedText = renderOption.renderMark(MarkType.BOLD, cleanedText);
        }
        if (nodeText.has("break")) {
            if (!cleanedText.contains("<br />")) {
                cleanedText = renderOption.renderMark(MarkType.BREAK, cleanedText);
            }
            // cleanedText = renderOption.renderMark(MarkType.BREAK, cleanedText);
        }
        return cleanedText;
    }

    private static String escapeTextNodes(String input) {
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");
    }
}
