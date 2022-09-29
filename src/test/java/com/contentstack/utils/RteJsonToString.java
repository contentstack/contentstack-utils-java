package com.contentstack.utils;

import com.contentstack.utils.render.DefaultOption;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static com.contentstack.utils.RTEResult.*;
import static com.contentstack.utils.RTEString.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RteJsonToString {

    @BeforeClass
    public static void startTestEmbedItemType() {}

    @Test
    public void testkBlankDocument() {
        JSONObject rteObject = new JSONObject(kPlainTextJson);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kPlainTextHtml, result);
    }

    @Test
    public void testkH1Json() {
        JSONObject rteObject = new JSONObject(kH1Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH1Html, result);
    }

    @Test
    public void testkH2Json() {
        JSONObject rteObject = new JSONObject(kH2Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH2Html, result);
    }

    @Test
    public void testkH3Json() {
        JSONObject rteObject = new JSONObject(kH3Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH3Html, result);
    }

    @Test
    public void testkH4Json() {
        JSONObject rteObject = new JSONObject(kH4Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH4Html, result);
    }

    @Test
    public void testkH5Json() {
        JSONObject rteObject = new JSONObject(kH5Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH5Html, result);
    }

    @Test
    public void testkH6Json() {
        JSONObject rteObject = new JSONObject(kH6Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH6Html, result);
    }

    @Test
    public void testkOrderListJson() {
        JSONObject rteObject = new JSONObject(kOrderListJson);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kOrderListHtml, result);
    }

    @Test
    public void testKCodeHtmlTypes() {
        JSONObject rteObject = new JSONObject(kUnorderListJson);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kIUnorderListHtml, result);
    }

    @Test
    public void testkImgJson() {
        JSONObject rteObject = new JSONObject(kImgJson);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kImgHtml, result);
    }

    @Test
    public void testAvailableEntryItemTypes() {
        JSONObject rteObject = new JSONObject(kParagraphJson);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kParagraphHtml, result);
    }
}
