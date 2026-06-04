package com.contentstack.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Resolves Contentstack API endpoints for any region and service.
 *
 * <p>Endpoint data is loaded from the bundled {@code regions.json} resource.
 * The parsed result is cached for the lifetime of the JVM process.
 * If the bundled file is absent, a live download from
 * {@code https://artifacts.contentstack.com/regions.json} is attempted as a fallback.
 *
 * <pre>{@code
 * // Get a specific service URL
 * String cdnUrl = Endpoint.getContentstackEndpoint("eu", "contentDelivery");
 * // → "https://eu-cdn.contentstack.com"
 *
 * // Get the host without the https:// scheme
 * String host = Endpoint.getContentstackEndpoint("eu", "contentDelivery", true);
 * // → "eu-cdn.contentstack.com"
 *
 * // Get all endpoints for a region
 * Map<String, String> all = Endpoint.getContentstackEndpoint("eu");
 * }</pre>
 */
public class Endpoint {

    private static final String REGIONS_URL = "https://artifacts.contentstack.com/regions.json";
    private static final String REGIONS_RESOURCE = "regions.json";

    private static JSONArray regionsData = null;

    private Endpoint() {}

    /**
     * Returns the URL for a specific service in the given region.
     *
     * @param region  canonical region ID ({@code na}, {@code eu}, {@code au}, {@code azure-na},
     *                {@code azure-eu}, {@code gcp-na}, {@code gcp-eu}) or any accepted alias.
     *                Case-insensitive; {@code -} and {@code _} separators are equivalent.
     * @param service service name (e.g. {@code contentDelivery}, {@code contentManagement})
     * @return full URL including {@code https://}
     * @throws IllegalArgumentException if region or service is unknown, or region is empty
     * @throws RuntimeException         if {@code regions.json} cannot be loaded
     */
    public static String getContentstackEndpoint(String region, String service) {
        return getContentstackEndpoint(region, service, false);
    }

    /**
     * Returns the URL for a specific service in the given region.
     *
     * @param region     canonical region ID or alias
     * @param service    service name
     * @param omitHttps  when {@code true}, strips {@code https://} from the result
     * @return URL string, with or without scheme depending on {@code omitHttps}
     * @throws IllegalArgumentException if region or service is unknown, or region is empty
     * @throws RuntimeException         if {@code regions.json} cannot be loaded
     */
    public static String getContentstackEndpoint(String region, String service, boolean omitHttps) {
        if (service == null || service.trim().isEmpty()) {
            throw new IllegalArgumentException("Service must not be empty. Use getContentstackEndpoint(region) to get all endpoints.");
        }
        JSONObject regionRow = resolveRegion(region);
        JSONObject endpoints = regionRow.getJSONObject("endpoints");
        if (!endpoints.has(service)) {
            throw new IllegalArgumentException("Service \"" + service + "\" not found for region \"" + regionRow.getString("id") + "\"");
        }
        String url = endpoints.getString(service);
        return omitHttps ? stripHttps(url) : url;
    }

    /**
     * Returns all endpoint URLs for the given region as an ordered map.
     *
     * @param region canonical region ID or alias
     * @return map of service name → URL (includes {@code https://})
     * @throws IllegalArgumentException if region is unknown or empty
     * @throws RuntimeException         if {@code regions.json} cannot be loaded
     */
    public static Map<String, String> getContentstackEndpoint(String region) {
        return getContentstackEndpoint(region, false);
    }

    /**
     * Returns all endpoint URLs for the given region as an ordered map.
     *
     * @param region    canonical region ID or alias
     * @param omitHttps when {@code true}, strips {@code https://} from every URL
     * @return map of service name → URL
     * @throws IllegalArgumentException if region is unknown or empty
     * @throws RuntimeException         if {@code regions.json} cannot be loaded
     */
    public static Map<String, String> getContentstackEndpoint(String region, boolean omitHttps) {
        JSONObject regionRow = resolveRegion(region);
        JSONObject endpoints = regionRow.getJSONObject("endpoints");
        Map<String, String> result = new LinkedHashMap<>();
        for (String serviceName : endpoints.keySet()) {
            String url = endpoints.getString(serviceName);
            result.put(serviceName, omitHttps ? stripHttps(url) : url);
        }
        return result;
    }

    // ── internal ──────────────────────────────────────────────────────────────

    private static JSONObject resolveRegion(String region) {
        if (region == null || region.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty region provided. Please provide a valid region.");
        }
        JSONArray regions = loadRegions();
        String normalized = region.trim().toLowerCase().replace('_', '-');

        // First pass: exact match on region id field
        for (int i = 0; i < regions.length(); i++) {
            JSONObject row = regions.getJSONObject(i);
            if (row.getString("id").equals(normalized)) {
                return row;
            }
        }

        // Second pass: match on accepted alternate names (case-insensitive, normalised separators)
        for (int i = 0; i < regions.length(); i++) {
            JSONObject row = regions.getJSONObject(i);
            JSONArray alternateNames = row.getJSONArray("alias");
            for (int j = 0; j < alternateNames.length(); j++) {
                String alternateName = alternateNames.getString(j).toLowerCase().replace('_', '-');
                if (alternateName.equals(normalized)) {
                    return row;
                }
            }
        }

        throw new IllegalArgumentException("Invalid region: " + region);
    }

    private static synchronized JSONArray loadRegions() {
        if (regionsData != null) {
            return regionsData;
        }

        // Try bundled classpath resource first
        InputStream is = Endpoint.class.getClassLoader().getResourceAsStream(REGIONS_RESOURCE);
        if (is != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String json = reader.lines().collect(Collectors.joining("\n"));
                regionsData = new JSONObject(json).getJSONArray("regions");
                return regionsData;
            } catch (Exception e) {
                // fall through to live download
            }
        }

        // Fallback: download from Contentstack
        try {
            String json = downloadRegions();
            regionsData = new JSONObject(json).getJSONArray("regions");
            return regionsData;
        } catch (Exception e) {
            throw new RuntimeException(
                    "contentstack/utils: regions.json not found and could not be downloaded. " +
                    "Ensure the JAR was built correctly or network access is available.", e);
        }
    }

    private static String downloadRegions() throws IOException {
        URL url = new URL(REGIONS_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        try (InputStream is = conn.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } finally {
            conn.disconnect();
        }
    }

    private static String stripHttps(String url) {
        return url.replaceAll("^https?://", "");
    }

    /** Clears the in-memory cache. For use in tests only. */
    static void resetCache() {
        regionsData = null;
    }
}
