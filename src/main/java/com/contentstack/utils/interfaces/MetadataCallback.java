package com.contentstack.utils.interfaces;


import com.contentstack.utils.helper.Metadata;

/**
 * The interface Metadata callback.
 */
public interface MetadataCallback {
    /**
     * Embedded object.
     *
     * @param metadata
     *         the metadata
     */
    void embeddedObject(Metadata metadata);
}
