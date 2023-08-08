package com.contentstack.utils.interfaces;


import com.contentstack.utils.helper.Metadata;

/**
 * The interface Metadata callback.
 */
public interface MetadataCallback {

    /**
     * The function "embeddedObject" takes a Metadata object as a parameter.
     *
     * @param metadata The metadata parameter is an object of type Metadata. It is used to pass
     *                 additional information or data to the embeddedObject function.
     */
    void embeddedObject(Metadata metadata);
}
