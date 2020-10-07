package com.contentstack.utils.helper;
import com.contentstack.utils.embedded.StyleType;
import org.jsoup.nodes.Attributes;

/**
 * POJO for the Embedded Objects, that helps to carry the objects
 */
public class EmbeddedObject {

    /* type of embedded object*/
    String type;
    /* uid of embedded object*/
    String uid;
    /* contentTypeUid of embedded object*/
    String contentTypeUid;
    /* styleTypeUid of embedded object*/
    StyleType sysStyleType;
    /* Outer HTML of embedded object*/
    String outerHTML;

    Attributes attributes;

    public EmbeddedObject(String type, String uid, String contentTypeUid, String sysStyleType, String outerHTML, Attributes attributes) {
        this.type = type;
        this.uid = uid;
        this.contentTypeUid = contentTypeUid;
        this.sysStyleType = StyleType.valueOf(sysStyleType.toUpperCase());
        this.outerHTML = outerHTML;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "EmbeddedObject{" +
                "type='" + type + '\'' +
                ", uid='" + uid + '\'' +
                ", contentTypeUid='" + contentTypeUid + '\'' +
                ", sysStyleType=" + sysStyleType +
                ", outerHTML='" + outerHTML + '\'' +
                ", attributes='" + attributes + '\'' +
                '}';
    }


    public String getType() { return type; }

    public Attributes getAttributes() { return attributes; }

    public String getUid() {
        return uid;
    }

    public String getContentTypeUid() {
        return contentTypeUid;
    }

    public StyleType getSysStyleType() {
        return sysStyleType;
    }

    public String getOuterHTML() {
        return outerHTML;
    }

}
