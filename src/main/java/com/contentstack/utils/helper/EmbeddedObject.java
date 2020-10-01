package com.contentstack.utils.helper;
import com.contentstack.utils.embedded.StyleType;

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

    public EmbeddedObject(String type, String uid, String contentTypeUid, String sysStyleType, String outerHTML) {
        this.type = type;
        this.uid = uid;
        this.contentTypeUid = contentTypeUid;
        this.sysStyleType = StyleType.valueOf(sysStyleType.toUpperCase());
        this.outerHTML = outerHTML;
    }

    @Override
    public String toString() {
        return "EmbeddedObject{" +
                "type='" + type + '\'' +
                ", uid='" + uid + '\'' +
                ", contentTypeUid='" + contentTypeUid + '\'' +
                ", sysStyleType=" + sysStyleType +
                ", outerHTML='" + outerHTML + '\'' +
                '}';
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContentTypeUid() {
        return contentTypeUid;
    }

    public void setContentTypeUid(String contentTypeUid) {
        this.contentTypeUid = contentTypeUid;
    }

    public StyleType getSysStyleType() {
        return sysStyleType;
    }

    public void setSysStyleType(StyleType sysStyleType) {
        this.sysStyleType = sysStyleType;
    }

    public String getOuterHTML() {
        return outerHTML;
    }

    public void setOuterHTML(String outerHTML) {
        this.outerHTML = outerHTML;
    }
}
