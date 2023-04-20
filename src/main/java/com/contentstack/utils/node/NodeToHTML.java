package com.contentstack.utils.node;

import com.contentstack.utils.interfaces.Option;
import org.json.JSONObject;

/**
 * The type Node to html.
 */
public class NodeToHTML {


    private NodeToHTML() {
        throw new IllegalStateException("Could not create instance of NodeToHTML");
    }

    /**
     * Text node to html string.
     *
     * @param nodeText
     *         the node text
     * @param renderOption
     *         the render option
     * @return the string
     */
    public static String textNodeToHTML(JSONObject nodeText, Option renderOption) {
        String text = nodeText.optString("text");

        // compare with the nodeText options
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
        return text;
    }

}
