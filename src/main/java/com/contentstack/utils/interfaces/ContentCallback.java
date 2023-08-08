package com.contentstack.utils.interfaces;


/**
 * The interface Content.
 */
public interface ContentCallback {

    /**
     * The function contentObject takes an object as input and returns an object as output.
     *
     * @param content The content parameter is an object that represents the content to be passed to
     *                the contentObject function.
     * @return The contentObject is being returned.
     */
    Object contentObject(Object content);
}
