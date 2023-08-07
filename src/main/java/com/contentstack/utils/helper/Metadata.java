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


    // The `public Metadata` constructor is initializing the values of the `Metadata` class with the
    // provided parameters.
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

    /**
     * The toString() function returns a string representation of the EmbeddedObject object.
     *
     * @return The `toString()` method is returning a string representation of an `EmbeddedObject` object.
     * The returned string includes the values of the `text`, `itemType`, `itemUid`, `contentTypeUid`,
     * `styleType`, `outerHTML`, and `attributes` properties of the object.
     */
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

    /**
     * The getText() function returns the value of the text variable.
     *
     * @return The method is returning a String value.
     */
    public String getText() {
        return text;
    }

    /**
     * The getItemType() function returns the type of an item.
     *
     * @return The method is returning the value of the variable "itemType".
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * The function returns the attributes of an object.
     *
     * @return The method is returning an object of type Attributes.
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * The getItemUid() function returns the itemUid value.
     *
     * @return The method is returning the value of the variable "itemUid".
     */
    public String getItemUid() {
        return itemUid;
    }

    /**
     * The function returns the content type UID as a string.
     *
     * @return The method is returning the value of the variable "contentTypeUid".
     */
    public String getContentTypeUid() {
        return contentTypeUid;
    }

    /**
     * The function returns the value of the styleType variable.
     *
     * @return The method is returning the value of the variable "styleType" of type StyleType.
     */
    public StyleType getStyleType() {
        return styleType;
    }

    /**
     * The getOuterHTML() function returns the outer HTML of an element.
     *
     * @return The method is returning the value of the variable "outerHTML".
     */
    public String getOuterHTML() {
        return outerHTML;
    }

}
