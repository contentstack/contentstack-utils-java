package com.contentstack.utils.helper;

import com.contentstack.utils.embedded.StyleType;
import org.jsoup.nodes.Attributes;


public class Metadata {

    String text;
    String itemType;
    String itemUid;
    String contentTypeUid;
    StyleType styleType;
    String outerHTML;
    Attributes attributes;

    /**
     * Instantiates a new Metadata.
     *
     * @param text
     *         the text
     * @param itemType
     *         the item type
     * @param itemUid
     *         the item uid
     * @param contentTypeUid
     *         the content type uid
     * @param styleType
     *         the style type
     * @param outerHTML
     *         the outer html
     * @param attributes
     *         the attributes
     */
    public Metadata(String text, String itemType, String itemUid, String contentTypeUid,
                    String styleType, String outerHTML, Attributes attributes) {
        this.text = text;
        this.itemType = itemType;
        this.itemUid = itemUid;
        this.contentTypeUid = contentTypeUid;
        this.styleType = StyleType.valueOf(styleType.toUpperCase());
        this.outerHTML = outerHTML;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "EmbeddedObject{" +
                "text='" + text + '\'' +
                "type='" + itemType + '\'' +
                ", uid='" + itemUid + '\'' +
                ", contentTypeUid='" + contentTypeUid + '\'' +
                ", sysStyleType=" + styleType +
                ", outerHTML='" + outerHTML + '\'' +
                ", attributes='" + attributes + '\'' +
                '}';
    }

    public String getText() {
        return text;
    }

    public String getItemType() {
        return itemType;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public String getItemUid() {
        return itemUid;
    }

    public String getContentTypeUid() {
        return contentTypeUid;
    }

    public StyleType getStyleType() {
        return styleType;
    }

    public String getOuterHTML() {
        return outerHTML;
    }

}
