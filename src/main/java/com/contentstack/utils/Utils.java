package com.contentstack.utils;

import com.contentstack.utils.helper.EmbeddedObject;
import com.contentstack.utils.helper.EmbeddedObjectCallback;
import com.contentstack.utils.render.DefaultOptions;
import com.contentstack.utils.render.Options;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    /**
     * Accepts to render content on the basis of below content
     * @param rteString   String of the rte available for the embedding
     * @param embedObject JSONObject to get the _embedded_object (_embedded_entries/_embedded_assets)
     * @param options     Options take takes input as (StyleType type, JSONObject embeddedObject)
     * @return String of rte with replaced tag
     */
    public static String renderContent(String rteString, JSONObject embedObject, Options options) {

        final String[] sReplaceRTE = {rteString};
        Document html = Jsoup.parse(rteString);
        getEmbeddedObjects(html, embeddedObject -> {
            Optional<JSONObject> filteredContent = null;

            // Find the type of _embedded object
            if (embeddedObject.getType().equalsIgnoreCase("entry")) {

                boolean available = embedObject.has("_embedded_entries");
                if (available) {
                    JSONArray jsonArray = embedObject.optJSONArray("_embedded_entries");
                    filteredContent = findEmbeddedEntry(jsonArray, embeddedObject);
                }
            } else if (embeddedObject.getType().equalsIgnoreCase("asset")) {

                boolean available = embedObject.has("_embedded_assets");
                if (available) {
                    JSONArray jsonArray = embedObject.optJSONArray("_embedded_assets");
                    filteredContent = findEmbeddedAsset(jsonArray, embeddedObject);
                }
            }

            // check if filteredContent is not null
            if (filteredContent.isPresent()) {
                JSONObject contentToPass = filteredContent.get();
                String stringOption = getStringOption(options, embeddedObject, contentToPass);
                sReplaceRTE[0] = html.body().html().toString().replace(embeddedObject.getOuterHTML(), stringOption);
            }
        });

        return sReplaceRTE[0];
    }


    /**
     * Take below items to return updated string
     *
     * @param rteArray    JSONArray of the rte available for the embedding
     * @param entryObject JSONObject to get the _embedded_object (_embedded_entries/_embedded_assets)
     * @param options     Options take takes input as (StyleType type, JSONObject embeddedObject)
     * @return String of rte with replaced tag
     */
    public static JSONArray renderContents(JSONArray rteArray, JSONObject entryObject, Options options) {

        JSONArray jsonArrayRTEContent = new JSONArray();
        for (Object RTE : rteArray) {
            String stringify = (String) RTE;
            String renderContent = renderContent(stringify, entryObject, options);
            jsonArrayRTEContent.put(renderContent);
        }
        //System.out.println("arrayRTE: " + jsonArrayRTEContent);
        logger.info(jsonArrayRTEContent.toString());
        return jsonArrayRTEContent;
    }


    /**
     * Matches the uid and _content_type_uid from the
     *
     * @param jsonArray      JSONArray: array of the _embedded_entries
     * @param embeddedObject EmbeddedObject: contains the model class information
     * @return Optional<JSONObject>
     */
    private static Optional<JSONObject> findEmbeddedEntry(JSONArray jsonArray, EmbeddedObject embeddedObject) {
        Optional<JSONObject> filteredContent = StreamSupport.stream(jsonArray.spliterator(), false)
                .map(val -> (JSONObject) val)
                .filter(val -> val.optString("uid").equalsIgnoreCase(embeddedObject.getUid()))
                .filter(val -> val.optString("_content_type_uid").equalsIgnoreCase(embeddedObject.getContentTypeUid()))
                .findFirst();
        return filteredContent;
    }

    private static Optional<JSONObject> findEmbeddedAsset(JSONArray jsonArray, EmbeddedObject embeddedObject) {

        Optional<JSONObject> filteredContent = StreamSupport.stream(jsonArray.spliterator(), false)
                .map(val -> (JSONObject) val)
                .filter(val -> val.optString("uid").equalsIgnoreCase(embeddedObject.getUid()))
                .findFirst();
        return filteredContent;
    }

    private static String getStringOption(Options options, EmbeddedObject embeddedObject, JSONObject contentToPass) {
        String stringOption = options.renderOptions(embeddedObject.getSysStyleType(), contentToPass, "");
        if (stringOption == null) {
            DefaultOptions defaultOptions = new DefaultOptions();
            stringOption = defaultOptions.renderOptions(embeddedObject.getSysStyleType(), contentToPass, "");
        }
        return stringOption;
    }


    private static void getEmbeddedObjects(Document html, EmbeddedObjectCallback objectCallback) {

        Elements embeddedEntries = html.body().getElementsByClass("embedded-entry");
        Elements embeddedAssets = html.body().getElementsByClass("embedded-asset");

        embeddedEntries.forEach((entry) -> {
            String type = entry.attr("type");
            String uid = entry.attr("data-sys-entry-uid");
            String contentType = entry.attr("data-sys-content-type-uid");
            String style = entry.attr("sys-style-type");
            String outerHTML = entry.outerHtml();
            EmbeddedObject embeddedEntry = new EmbeddedObject(type, uid, contentType, style, outerHTML);
            objectCallback.embeddedObject(embeddedEntry);
        });

        embeddedAssets.forEach((entry) -> {
            String type = entry.attr("type");
            String uid = entry.attr("data-sys-asset-uid");
            String style = entry.attr("sys-style-type");
            String outerHTML = entry.outerHtml();
            EmbeddedObject embeddedAsset = new EmbeddedObject(type, uid, "asset", style, outerHTML);
            objectCallback.embeddedObject(embeddedAsset);
        });
    }


}
