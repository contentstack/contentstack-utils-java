package com.contentstack.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EndpointTest {

    @Before
    @After
    public void resetCache() {
        Endpoint.resetCache();
    }

    // ── canonical IDs ──────────────────────────────────────────────────────

    @Test
    public void testNaContentDelivery() {
        assertEquals("https://cdn.contentstack.io",
                Endpoint.getContentstackEndpoint("na", "contentDelivery"));
    }

    @Test
    public void testNaContentManagement() {
        assertEquals("https://api.contentstack.io",
                Endpoint.getContentstackEndpoint("na", "contentManagement"));
    }

    @Test
    public void testEuContentDelivery() {
        assertEquals("https://eu-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("eu", "contentDelivery"));
    }

    @Test
    public void testEuContentManagement() {
        assertEquals("https://eu-api.contentstack.com",
                Endpoint.getContentstackEndpoint("eu", "contentManagement"));
    }

    @Test
    public void testAuContentDelivery() {
        assertEquals("https://au-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("au", "contentDelivery"));
    }

    @Test
    public void testAzureNaContentDelivery() {
        assertEquals("https://azure-na-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("azure-na", "contentDelivery"));
    }

    @Test
    public void testAzureEuContentDelivery() {
        assertEquals("https://azure-eu-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("azure-eu", "contentDelivery"));
    }

    @Test
    public void testGcpNaContentDelivery() {
        assertEquals("https://gcp-na-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("gcp-na", "contentDelivery"));
    }

    @Test
    public void testGcpEuContentDelivery() {
        assertEquals("https://gcp-eu-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("gcp-eu", "contentDelivery"));
    }

    // ── region aliases ─────────────────────────────────────────────────────

    @Test
    public void testAliasUs() {
        assertEquals("https://cdn.contentstack.io",
                Endpoint.getContentstackEndpoint("us", "contentDelivery"));
    }

    @Test
    public void testAliasAwsNaDash() {
        assertEquals("https://cdn.contentstack.io",
                Endpoint.getContentstackEndpoint("aws-na", "contentDelivery"));
    }

    @Test
    public void testAliasAwsNaUnderscore() {
        assertEquals("https://cdn.contentstack.io",
                Endpoint.getContentstackEndpoint("aws_na", "contentDelivery"));
    }

    @Test
    public void testAliasUppercaseNA() {
        assertEquals("https://cdn.contentstack.io",
                Endpoint.getContentstackEndpoint("NA", "contentDelivery"));
    }

    @Test
    public void testAliasUppercaseUS() {
        assertEquals("https://cdn.contentstack.io",
                Endpoint.getContentstackEndpoint("US", "contentDelivery"));
    }

    @Test
    public void testAliasUppercaseAWSNA() {
        assertEquals("https://cdn.contentstack.io",
                Endpoint.getContentstackEndpoint("AWS-NA", "contentDelivery"));
    }

    @Test
    public void testAliasAwsEu() {
        assertEquals("https://eu-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("aws-eu", "contentDelivery"));
    }

    @Test
    public void testAliasEuUppercase() {
        assertEquals("https://eu-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("EU", "contentDelivery"));
    }

    @Test
    public void testAliasAzureNaUnderscore() {
        assertEquals("https://azure-na-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("azure_na", "contentDelivery"));
    }

    @Test
    public void testAliasAzureEuUppercase() {
        assertEquals("https://azure-eu-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("AZURE-EU", "contentDelivery"));
    }

    @Test
    public void testAliasGcpNaUppercase() {
        assertEquals("https://gcp-na-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("GCP-NA", "contentDelivery"));
    }

    @Test
    public void testAliasGcpEuUnderscore() {
        assertEquals("https://gcp-eu-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("gcp_eu", "contentDelivery"));
    }

    // ── omitHttps ──────────────────────────────────────────────────────────

    @Test
    public void testOmitHttpsFalseReturnsScheme() {
        String url = Endpoint.getContentstackEndpoint("na", "contentDelivery", false);
        assertTrue(url.startsWith("https://"));
    }

    @Test
    public void testOmitHttpsTrueStripsScheme() {
        String host = Endpoint.getContentstackEndpoint("na", "contentDelivery", true);
        assertFalse(host.startsWith("https://"));
        assertEquals("cdn.contentstack.io", host);
    }

    @Test
    public void testOmitHttpsEu() {
        assertEquals("eu-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("eu", "contentDelivery", true));
    }

    @Test
    public void testOmitHttpsAzureNa() {
        assertEquals("azure-na-api.contentstack.com",
                Endpoint.getContentstackEndpoint("azure-na", "contentManagement", true));
    }

    @Test
    public void testOmitHttpsGcpEu() {
        assertEquals("gcp-eu-cdn.contentstack.com",
                Endpoint.getContentstackEndpoint("gcp-eu", "contentDelivery", true));
    }

    // ── various service keys ───────────────────────────────────────────────

    @Test
    public void testNaAuth() {
        assertEquals("https://auth-api.contentstack.com",
                Endpoint.getContentstackEndpoint("na", "auth"));
    }

    @Test
    public void testNaGraphqlDelivery() {
        assertEquals("https://graphql.contentstack.com",
                Endpoint.getContentstackEndpoint("na", "graphqlDelivery"));
    }

    @Test
    public void testNaPreview() {
        assertEquals("https://rest-preview.contentstack.com",
                Endpoint.getContentstackEndpoint("na", "preview"));
    }

    @Test
    public void testNaApplication() {
        assertEquals("https://app.contentstack.com",
                Endpoint.getContentstackEndpoint("na", "application"));
    }

    @Test
    public void testNaAssetManagement() {
        assertEquals("https://am-api.contentstack.com",
                Endpoint.getContentstackEndpoint("na", "assetManagement"));
    }

    @Test
    public void testEuAutomate() {
        assertEquals("https://eu-prod-automations-api.contentstack.com",
                Endpoint.getContentstackEndpoint("eu", "automate"));
    }

    // ── all endpoints map ──────────────────────────────────────────────────

    @Test
    public void testGetAllEndpointsForNa() {
        Map<String, String> endpoints = Endpoint.getContentstackEndpoint("na");
        assertNotNull(endpoints);
        assertFalse(endpoints.isEmpty());
        assertEquals("https://cdn.contentstack.io", endpoints.get("contentDelivery"));
        assertEquals("https://api.contentstack.io", endpoints.get("contentManagement"));
        assertEquals("https://auth-api.contentstack.com", endpoints.get("auth"));
    }

    @Test
    public void testGetAllEndpointsForEu() {
        Map<String, String> endpoints = Endpoint.getContentstackEndpoint("eu");
        assertNotNull(endpoints);
        assertEquals("https://eu-cdn.contentstack.com", endpoints.get("contentDelivery"));
        assertEquals("https://eu-api.contentstack.com", endpoints.get("contentManagement"));
    }

    @Test
    public void testGetAllEndpointsOmitHttps() {
        Map<String, String> endpoints = Endpoint.getContentstackEndpoint("eu", true);
        assertEquals("eu-cdn.contentstack.com", endpoints.get("contentDelivery"));
        assertEquals("eu-api.contentstack.com", endpoints.get("contentManagement"));
        for (String value : endpoints.values()) {
            assertFalse("No URL should start with https://", value.startsWith("https://"));
        }
    }

    @Test
    public void testGetAllEndpointsWithHttps() {
        Map<String, String> endpoints = Endpoint.getContentstackEndpoint("na", false);
        for (String value : endpoints.values()) {
            assertTrue("All URLs should start with https://", value.startsWith("https://"));
        }
    }

    // ── Utils proxy methods ────────────────────────────────────────────────

    @Test
    public void testUtilsProxyGetServiceUrl() {
        assertEquals("https://cdn.contentstack.io",
                Utils.getContentstackEndpoint("na", "contentDelivery"));
    }

    @Test
    public void testUtilsProxyGetServiceUrlOmitHttps() {
        assertEquals("cdn.contentstack.io",
                Utils.getContentstackEndpoint("na", "contentDelivery", true));
    }

    @Test
    public void testUtilsProxyGetAllEndpoints() {
        Map<String, String> endpoints = Utils.getContentstackEndpoint("eu");
        assertNotNull(endpoints);
        assertEquals("https://eu-cdn.contentstack.com", endpoints.get("contentDelivery"));
    }

    @Test
    public void testUtilsProxyGetAllEndpointsOmitHttps() {
        Map<String, String> endpoints = Utils.getContentstackEndpoint("eu", true);
        assertEquals("eu-cdn.contentstack.com", endpoints.get("contentDelivery"));
    }

    // ── error cases ────────────────────────────────────────────────────────

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyRegionThrows() {
        Endpoint.getContentstackEndpoint("", "contentDelivery");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRegionThrows() {
        Endpoint.getContentstackEndpoint(null, "contentDelivery");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownRegionThrows() {
        Endpoint.getContentstackEndpoint("asia-pacific", "contentDelivery");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownServiceThrows() {
        Endpoint.getContentstackEndpoint("na", "cms");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyServiceThrows() {
        Endpoint.getContentstackEndpoint("na", "", false);
    }

    @Test
    public void testUnknownRegionErrorMessage() {
        try {
            Endpoint.getContentstackEndpoint("invalid-region", "contentDelivery");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid region"));
            assertTrue(e.getMessage().contains("invalid-region"));
        }
    }

    @Test
    public void testUnknownServiceErrorMessage() {
        try {
            Endpoint.getContentstackEndpoint("na", "unknownService");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("unknownService"));
        }
    }

    @Test
    public void testEmptyRegionErrorMessage() {
        try {
            Endpoint.getContentstackEndpoint("", "contentDelivery");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().toLowerCase().contains("empty region"));
        }
    }

    // ── caching ────────────────────────────────────────────────────────────

    @Test
    public void testCacheIsUsedOnSubsequentCalls() {
        // First call loads and caches
        String first = Endpoint.getContentstackEndpoint("na", "contentDelivery");
        // Second call uses cache — result must be identical
        String second = Endpoint.getContentstackEndpoint("na", "contentDelivery");
        assertEquals(first, second);
    }
}
