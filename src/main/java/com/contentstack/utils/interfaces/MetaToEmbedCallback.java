package com.contentstack.utils.interfaces;

import com.contentstack.utils.helper.Metadata;
import org.json.JSONObject;

import java.util.Optional;


/**
 * The interface Meta to embed.
 */
public interface MetaToEmbedCallback {
    /**
     * To embed optional.
     *
     * @param metadata
     *         the metadata
     * @return the optional
     */
    Optional<JSONObject> toEmbed(Metadata metadata);
}


