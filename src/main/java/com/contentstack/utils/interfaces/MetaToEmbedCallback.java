package com.contentstack.utils.interfaces;

import com.contentstack.utils.helper.Metadata;
import org.json.JSONObject;

import java.util.Optional;


/**
 * The interface Meta to embed.
 */
public interface MetaToEmbedCallback {

    /**
     * The function "toEmbed" takes in a Metadata object and returns an Optional object containing a
     * JSONObject.
     *
     * @param metadata The metadata parameter is an object that contains information about a resource,
     *                 such as its title, description, and URL.
     * @return The method is returning an Optional object that contains a JSONObject.
     */
    Optional<JSONObject> toEmbed(Metadata metadata);
}


