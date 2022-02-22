package com.contentstack.utils.preset;

import com.contentstack.utils.presets.Preset;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PresetTest {

    Preset preset = new Preset();
    JSONObject assetFile = readJson("management/asset.json");

    private static JSONObject readJson(String file) {
        JSONObject mockJsonObject = null;
        String path = "src/test/resources/" + file;
        try {
            Object obj = new JSONParser().parse(new FileReader(new File(path).getPath()));
            mockJsonObject = (JSONObject) obj;
        } catch (IOException | ParseException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return mockJsonObject;
    }

    @Test
    public void testResolvePresetWithNullAssetFile() {
        try {
            preset.resolvePresetByPresetName(null, "theExtensionUid", "Local Preset");
        } catch (Exception e) {
            Assert.assertEquals(
                    "Argument for @NotNull parameter 'asset' of com/contentstack/utils/presets/Preset.resolvePresetByPresetName must not be null",
                    e.getLocalizedMessage());
        }
    }

    @Test
    public void testResolvePresetNullExtensionUid() {
        try {
            preset.resolvePresetByPresetName(assetFile, null, "Local Preset");
        } catch (Exception e) {
            Assert.assertEquals(
                    "Argument for @NotNull parameter 'extensionUid' of com/contentstack/utils/presets/Preset.resolvePresetByPresetName must not be null",
                    e.getLocalizedMessage());
        }
    }

    @Test
    public void testResolvePresetEmptyExtensionUid() {
        try {
            preset.resolvePresetByPresetName(assetFile, "", "Local Preset");
        } catch (Exception e) {
            Assert.assertEquals("Please provide valid extension uid", e.getLocalizedMessage());
        }
    }

    @Test
    public void testResolvePresetNullPresetName() {
        try {
            preset.resolvePresetByPresetName(assetFile, "theExtensionUid", null);
        } catch (Exception e) {
            Assert.assertEquals(
                    "Argument for @NotNull parameter 'presetName' of com/contentstack/utils/presets/Preset.resolvePresetByPresetName must not be null",
                    e.getLocalizedMessage());
        }
    }

    @Test
    public void testResolveEmptyPresetName() {
        try {
            preset.resolvePresetByPresetName(assetFile, "theExtensionUid", "");
        } catch (Exception e) {
            Assert.assertEquals("Please provide valid Preset Name", e.getLocalizedMessage());
        }
    }

    @Test
    public void testResolvePresetByPresetName() {
        String url = preset.resolvePresetByPresetName(assetFile, "theExtensionUid", "Local Preset");
        Assert.assertEquals(
                "http://localhost:8000/v3/assets/crop_area.jpeg?height=500&width=500&orient=2&format=jpeg&quality=100",
                url);
    }

    @Test
    public void testResolvePresetByPresetNameWithUrlQuestionMark() {
        JSONObject assetWithQuestionMarkJson = readJson("management/asset_with_question_mark.json");
        String url = preset.resolvePresetByPresetName(assetWithQuestionMarkJson, "theExtensionUid", "Local Preset");
        Assert.assertEquals(
                "http://localhost:8000/v3/assets/crop_area.jpeg?format=jpeg&quality=100&height=500&width=500&orient=2&format=jpeg&quality=100",
                url);
    }

    @Test
    public void testResolvePresetByPresetUID() {
        String url = preset.resolvePresetByPresetUID(assetFile, "theExtensionUid", "the_fake_preset_uid");
        Assert.assertEquals(
                "http://localhost:8000/v3/assets/crop_area.jpeg?height=500&width=500&orient=2&format=jpeg&quality=100",
                url);
    }

    @Test
    public void testResolvePresetByPresetUIDWithUrlQuestionMark() {
        JSONObject assetWithQuestionMarkJson = readJson("management/asset_with_question_mark.json");
        String url = preset.resolvePresetByPresetUID(assetWithQuestionMarkJson, "theExtensionUid",
                "the_fake_preset_uid");
        Assert.assertEquals(
                "http://localhost:8000/v3/assets/crop_area.jpeg?format=jpeg&quality=100&height=500&width=500&orient=2&format=jpeg&quality=100",
                url);
    }

    @Test
    public void testResolvePresetBy() {
        JSONObject assetWithQuestionMarkJson = readJson("management/asset_with_question_mark.json");
        String url = preset.resolvePresetByPresetUID(assetWithQuestionMarkJson, "theExtensionUid",
                "the_fake_preset_uid");
        Assert.assertEquals(
                "http://localhost:8000/v3/assets/crop_area.jpeg?format=jpeg&quality=100&height=500&width=500&orient=2&format=jpeg&quality=100",
                url);
    }

}
