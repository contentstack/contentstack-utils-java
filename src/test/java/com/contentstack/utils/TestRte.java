package com.contentstack.utils;

import com.contentstack.utils.render.DefaultOption;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static com.contentstack.utils.RTEResult.*;
import static com.contentstack.utils.RTEString.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRte {

    @BeforeClass
    public static void startTestEmbedItemType() {
    }

    @Test
    public void testsBlankDocument() {
        JSONObject rteObject = new JSONObject(kPlainTextJson);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kPlainTextHtml, result);
    }

    @Test
    public void testsH1Json() {
        JSONObject rteObject = new JSONObject(kH1Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH1Html, result);
    }

    @Test
    public void testsH2Json() {
        JSONObject rteObject = new JSONObject(kH2Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH2Html, result);
    }

    @Test
    public void testsH3Json() {
        JSONObject rteObject = new JSONObject(kH3Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH3Html, result);
    }

    @Test
    public void testH4Json() {
        JSONObject rteObject = new JSONObject(kH4Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH4Html, result);
    }

    @Test
    public void testsH5Json() {
        JSONObject rteObject = new JSONObject(kH5Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH5Html, result);
    }

    @Test
    public void testH6Json() {
        JSONObject rteObject = new JSONObject(kH6Json);
        String result = Utils.jsonToHTML(rteObject, new DefaultOption(), null);
        Assert.assertEquals(kH6Html, result);
    }

    @Test
    public void testOrderListJson() {
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
    public void testsImgJson() {
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

    @Test
    public void testHERFID() throws IOException {
        final String rte = "src/test/resources/reports/wfs_fees.json";
        JSONObject theRTE = new ReadResource().readJson(rte);
        String result = Utils.jsonToHTML(theRTE, new DefaultOption(), null);
        System.out.println(result);
    }
}
