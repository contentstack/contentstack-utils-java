package com.contentstack.utils;
import com.contentstack.utils.helper.Metadata;
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

    // Interface Metadata Callback
    private interface MetadataCallback { void embeddedObject(Metadata metadata); }

    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    /**
     * @param entryObj: Objects that contains RTE with embedded objects
     * @param keyPath keyPath
     * @param renderObject renderObject
     */
    public void render(JSONObject entryObj, String[] keyPath, Options renderObject){

        for (int i = 0; i < keyPath.length; i++) {
            String path = keyPath[i];
            // Check path is available in the entryObj
            // if available , return the entry as entryPath to renderContent's second param
            entryObj = findEntryByPath(entryObj, path);
            // Pass entryObj to the renderContent Second parameter
            renderContent(path, entryObj, new Options() {
                @Override
                public String renderOptions(JSONObject embeddedObject, Metadata attributes) {
                    return null;
                }
            });
        }
    }

    private JSONObject findEntryByPath(JSONObject entryObj, String path) {

        return null;
    }


    /**
     *
     * @param jsonArray Objects that contains RTE with embedded objects
     * @param keyPath String array keyPath
     * @param renderObject renderObjects
     */
    public void render(JSONArray jsonArray, String[] keyPath, Options renderObject){
        jsonArray.forEach(jsonObj->{
            render((JSONObject) jsonObj, keyPath, renderObject);
        });
    }


    /**
     * Accepts to render content on the basis of below content
     * @param rteStringify   String of the rte available for the embedding
     * @param embedObject JSONObject to get the _embedded_object (_embedded_entries/_embedded_assets)
     * @param options     Options take takes input as (StyleType type, JSONObject embeddedObject)
     * @return String of rte with replaced tag
     */
    public static String renderContent(String rteStringify, JSONObject embedObject, Options options) {

        final String[] sReplaceRTE = {rteStringify};
        Document html = Jsoup.parse(rteStringify);
        getEmbeddedObjects(html, metadata -> {

            Optional<JSONObject> filteredContent = Optional.empty();
            // Find the type of _embedded object
            if (metadata.getItemType().equalsIgnoreCase("entry")) {
                boolean available = embedObject.has("_embedded_entries");
                if (available) {
                    JSONArray jsonArray = embedObject.optJSONArray("_embedded_entries");
                    filteredContent = findEmbeddedEntry(jsonArray, metadata);
                }
            } else if (metadata.getItemType().equalsIgnoreCase("asset")) {
                boolean available = embedObject.has("_embedded_assets");
                if (available) {
                    JSONArray jsonArray = embedObject.optJSONArray("_embedded_assets");
                    filteredContent = findEmbeddedAsset(jsonArray, metadata);
                }
            }
            // check if filteredContent is not null
            if (filteredContent.isPresent()) {
                JSONObject contentToPass = filteredContent.get();
                String stringOption = getStringOption(options, metadata, contentToPass);
                sReplaceRTE[0] = html.body().html().replace(metadata.getOuterHTML(), stringOption);
            }
        });

        return sReplaceRTE[0];
    }


    /**
     * Take below items to return updated string
     * @param rteArray JSONArray of the rte available for the embedding
     * @param entryObject JSONObject to get the _embedded_object (_embedded_entries/_embedded_assets)
     * @param options Options take takes input as (StyleType type, JSONObject embeddedObject)
     * @return String of rte with replaced tag
     */
    public static JSONArray renderContents(JSONArray rteArray, JSONObject entryObject, Options options) {

        JSONArray jsonArrayRTEContent = new JSONArray();
        for (Object RTE : rteArray) {
            String stringify = (String) RTE;
            String renderContent = renderContent(stringify, entryObject, options);
            jsonArrayRTEContent.put(renderContent);
        }
        logger.info(jsonArrayRTEContent.toString());
        return jsonArrayRTEContent;
    }


    /**
     * Matches the uid and _content_type_uid from the
     *
     * @param jsonArray JSONArray: array of the _embedded_entries
     * @param metadata EmbeddedObject: contains the model class information
     * @return Optional<JSONObject>
     */
    private static Optional<JSONObject> findEmbeddedEntry(JSONArray jsonArray, Metadata metadata) {
        Optional<JSONObject> filteredContent = StreamSupport.stream(jsonArray.spliterator(), false)
                .map(val -> (JSONObject) val)
                .filter(val -> val.optString("uid").equalsIgnoreCase(metadata.getItemUid()))
                .filter(val -> val.optString("_content_type_uid").equalsIgnoreCase(metadata.getContentTypeUid()))
                .findFirst();
        return filteredContent;
    }

    /**
     * Matches the uid and _content_type_uid from the
     *
     * @param jsonArray JSONArray: array of the _embedded_assets
     * @param metadata EmbeddedObject: contains the model class information
     * @return Optional<JSONObject>
     */
    private static Optional<JSONObject> findEmbeddedAsset(JSONArray jsonArray, Metadata metadata) {
        Optional<JSONObject> filteredContent = StreamSupport.stream(jsonArray.spliterator(), false)
                .map(val -> (JSONObject) val)
                .filter(val -> val.optString("uid").equalsIgnoreCase(metadata.getItemUid()))
                .findFirst();
        return filteredContent;
    }

    private static String getStringOption(Options options, Metadata metadata, JSONObject contentToPass) {
        // TODO: Sending HashMap as HTML Attributes
        String stringOption = options.renderOptions(
                contentToPass, metadata);
        if (stringOption == null) {
            DefaultOptions defaultOptions = new DefaultOptions();
            stringOption = defaultOptions.renderOptions(
                    contentToPass, metadata);
        }
        return stringOption;
    }


    private static void getEmbeddedObjects(Document html, MetadataCallback metadataCallback) {

        Elements embeddedEntries = html.body().getElementsByClass("embedded-entry");
        Elements embeddedAssets = html.body().getElementsByClass("embedded-asset");

        embeddedEntries.forEach((entry) -> {
            String text = entry.text();
            String type = entry.attr("type");
            String uid = entry.attr("data-sys-entry-uid");
            String contentType = entry.attr("data-sys-content-type-uid");
            String style = entry.attr("sys-style-type");
            String outerHTML = entry.outerHtml();
            Metadata metadata = new Metadata(text, type, uid, contentType, style, outerHTML, entry.attributes());
            logger.info(metadata.toString());
            metadataCallback.embeddedObject(metadata);
        });

        embeddedAssets.forEach((asset) -> {
            String text = asset.text();
            String type = asset.attr("type");
            String uid = asset.attr("data-sys-asset-uid");
            String style = asset.attr("sys-style-type");
            String outerHTML = asset.outerHtml();
            Metadata metadata = new Metadata(text, type, uid, "asset", style, outerHTML, asset.attributes());
            metadataCallback.embeddedObject(metadata);
        });
    }


}


