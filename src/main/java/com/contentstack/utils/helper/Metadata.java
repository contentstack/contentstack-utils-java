package com.contentstack.utils.helper;
import com.contentstack.utils.embedded.StyleType;
import com.sun.xml.internal.bind.v2.TODO;
import org.jsoup.nodes.Attributes;

/**
 * POJO for the Embedded Objects, that helps to carry the objects
 */
public class Metadata {

    String text ;
    /* type of embedded object*/
    //TODO: itemType Enum type to be taken ( implementation incomplete)
    String itemType;
    /* itemUid of embedded object*/
    String itemUid;
    /* contentTypeUid of embedded object*/
    String contentTypeUid;
    /* styleTypeUid of embedded object*/
    StyleType styleType;
    /* Outer HTML of embedded object*/
    String outerHTML;
    /* attributes of embedded object*/
    Attributes attributes;

    public Metadata(String text, String itemType, String itemUid, String contentTypeUid, String styleType, String outerHTML, Attributes attributes) {
       this.text= text;
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

    public String getText() { return text; }

    public String getItemType() { return itemType; }

    public Attributes getAttributes() { return attributes; }

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
