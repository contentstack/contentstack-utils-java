package com.contentstack.utils.presets;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constant {

    private Constant() throws IllegalAccessException {
        throw new IllegalAccessException("Not allowed");
    }

    // constants
    static final String URL = "url";
    static final String QUALITY = "quality";
    static final String EFFECTS = "effects";
    static final String OPTIONS = "options";
    static final String HEIGHT = "height";
    static final String WIDTH = "width";
    static final String ORIENT = "orient";
    static final String BRIGHTNESS = "brightness";
    static final String CONTRAST = "contrast";
    static final String SHARPEN = "sharpen";
    static final String BLUR = "blur";
    static final String TRANSFORM = "transform";
    static final String FLIP_MODE = "flip-mode";
    static final String IMAGE_TYPE = "image-type";

    protected static void throwException(JSONObject jsonAsset, @NotNull String localisedMessage) {
        if (jsonAsset.isEmpty()) {
            try {
                throw new KeyNotFoundException(localisedMessage);
            } catch (KeyNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    protected static JSONObject validator(JSONObject asset) {
        throwException(asset, "asset file can not be empty");
        if (!asset.containsKey("_metadata")) {
            throwException(asset, "_metadata keys not found");
        }
        JSONObject localMetadata = (JSONObject) asset.get("_metadata");
        if (!asset.containsKey("extensions")) {
            throwException(localMetadata, "extensions not present in the object");
        }
        return localMetadata;
    }

    protected static List<JSONObject> extractMetadata(
            JSONObject extensionUid, String presetName, String random) {
        JSONObject localMetadata = (JSONObject) extensionUid.get("local_metadata");
        JSONObject globalMetadata = (JSONObject) extensionUid.get("global_metadata");
        List<JSONObject> localKeys = returnPresetObject(localMetadata, presetName, random);
        List<JSONObject> globalKeys = Collections.emptyList();
        if (localKeys.isEmpty()) {
            globalKeys = returnPresetObject(globalMetadata, presetName, random);
        }
        return globalKeys.isEmpty() ? localKeys : globalKeys;
    }

    protected static List<JSONObject> returnPresetObject(
            @NotNull JSONObject metadata,
            @NotNull String presetName,
            String random) {
        JSONArray presetArray = (JSONArray) metadata.get("presets");
        return getByPresetName(presetArray, presetName, random);
    }

    protected static List<JSONObject> getByPresetName(
            JSONArray presetArray, String presetName, String random) {
        List<JSONObject> listPreset = new ArrayList<>();
        presetArray.forEach(preset -> {
            JSONObject presetObj = (JSONObject) preset;
            if (presetObj.containsKey(random)) {
                String localPresetName = (String) presetObj.get(random);
                if (localPresetName.equalsIgnoreCase(presetName)) {
                    listPreset.add(presetObj);
                }
            }
        });
        return listPreset;
    }
}
