package com.contentstack.utils;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.interfaces.NodeCallback;
import com.contentstack.utils.render.DefaultOption;


import org.json.JSONObject;
import org.json.JSONArray;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultOptionTests {
    private static final Logger logger = Logger.getLogger(UtilTests.class.getName());
    private static JSONObject localJsonObj;

    @BeforeClass
    public static void startDefaultOptionsTests() throws IOException {
        logger.setLevel(Level.ALL);
        final String EMBEDDED_ITEMS = "src/test/resources/embedded_items.json";
        localJsonObj = (JSONObject) new ReadResource().readJson(EMBEDDED_ITEMS).getJSONArray("entries").get(0);
    }

    @Test
    public void testTestAttributes() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        Assert.assertEquals("entry", attributes.get("type").toLowerCase());
    }

    @Test
    public void testRenderOptions() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        Assert.assertEquals("entry", attributes.get("type").toLowerCase());
        final DefaultOption defaultOptions = new DefaultOption();
        Metadata metadata = new Metadata("TextTest", "entry", "6723673", "content_type_uid", "block", "outerHTMLTet",
                attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<div><p></p><div><p>Content type: <span></span></p></div>", result);
    }

    @Test
    public void testDefaultOptions() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOption defaultOptions = new DefaultOption();
        Metadata metadata = new Metadata("TextTest", "entry", "6723673", "content_type_uid", "block", "outerHTMLTet",
                attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<div><p></p><div><p>Content type: <span></span></p></div>", result);
    }

    @Test
    public void testEntryInline() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOption defaultOptions = new DefaultOption();
        Metadata metadata = new Metadata("TextTest", "entry", "6723673", "content_type_uid", "inline",
                "outerHTMLTet", attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<span></span>", result);
    }

    @Test
    public void testEmbeddedLink() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOption defaultOptions = new DefaultOption();
        Metadata metadata = new Metadata("TextTest", "entry", "6723673", "content_type_uid", "link",
                "outerHTMLTet", attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<a href=\"\"></a>", result);
    }

    @Test
    public void testEmbeddedDefaultDisplayable() {
        Attributes attributes = new ReadResource().returnEntryAttributes(localJsonObj);
        final DefaultOption defaultOptions = new DefaultOption();
        Metadata metadata = new Metadata("TextTest", "entry", "6723673", "content_type_uid", "display",
                "outerHTMLTet", attributes);
        String result = defaultOptions.renderOptions(localJsonObj.optJSONObject("_embedded_items"), metadata);
        Assert.assertEquals("<img src=\"\" alt=\"\" />", result);
    }
     @Test
    public void testRenderNodeWithVoidTd() {
        DefaultOption defaultOptions = new DefaultOption();
        JSONObject nodeObject = new JSONObject();
        JSONObject attrs = new JSONObject();
        attrs.put("void", true);
        nodeObject.put("attrs", attrs);
        nodeObject.put("children", new JSONArray());

        NodeCallback callback = children -> {
            // Simple callback implementation for testing purposes
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < children.length(); i++) {
                sb.append(children.getJSONObject(i).getString("type"));
            }
            return sb.toString();
        };

        String result = defaultOptions.renderNode("td", nodeObject, callback);
        Assert.assertEquals("", result);
    }
    @Test
    public void testRenderNodeWithVoidTh() {
        DefaultOption defaultOptions = new DefaultOption();
        JSONObject nodeObject = new JSONObject();
        JSONObject attrs = new JSONObject();
        attrs.put("void", true);
        nodeObject.put("attrs", attrs);
        nodeObject.put("children", new JSONArray());

        NodeCallback callback = children -> {
            // Simple callback implementation for testing purposes
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < children.length(); i++) {
                sb.append(children.getJSONObject(i).getString("type"));
            }
            return sb.toString();
        };

        String result = defaultOptions.renderNode("th", nodeObject, callback);
        Assert.assertEquals("", result);
    }

    @Test
    public void testRenderNodeWithoutVoidTd() {
        DefaultOption defaultOptions = new DefaultOption();
        JSONObject nodeObject = new JSONObject();
        JSONObject attrs = new JSONObject();
        attrs.put("class", "example");
        nodeObject.put("attrs", attrs);
        JSONArray children = new JSONArray();
        JSONObject child = new JSONObject();
        child.put("type", "text");
        child.put("content", "example content");
        children.put(child);
        nodeObject.put("children", children);

        NodeCallback callback = childrenArray -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < childrenArray.length(); i++) {
                sb.append(childrenArray.getJSONObject(i).getString("content"));
            }
            return sb.toString();
        };

        String result = defaultOptions.renderNode("td", nodeObject, callback);
        Assert.assertEquals("<td class=\"example\">example content</td>", result);
    }

    @Test
    public void testRenderNodeWithoutVoidTh() {
        DefaultOption defaultOptions = new DefaultOption();
        JSONObject nodeObject = new JSONObject();
        JSONObject attrs = new JSONObject();
        attrs.put("class", "example");
        nodeObject.put("attrs", attrs);
        JSONArray children = new JSONArray();
        JSONObject child = new JSONObject();
        child.put("type", "text");
        child.put("content", "example content");
        children.put(child);
        nodeObject.put("children", children);

        NodeCallback callback = childrenArray -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < childrenArray.length(); i++) {
                sb.append(childrenArray.getJSONObject(i).getString("content"));
            }
            return sb.toString();
        };

        String result = defaultOptions.renderNode("th", nodeObject, callback);
        Assert.assertEquals("<th class=\"example\">example content</th>", result);
    }

}
