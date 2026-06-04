# Region Endpoint Integration Specification

## Overview

Contentstack services are deployed across multiple cloud providers and geographic regions. SDKs must resolve service endpoints dynamically using the Contentstack Regions Registry rather than relying on hardcoded URLs.

This ensures:

- Consistent endpoint resolution across all SDKs
- Automatic support for newly introduced regions
- Automatic support for newly introduced services
- Single source of truth for endpoint configuration
- Elimination of region-specific host logic inside SDKs

---

## Regions Registry

All endpoint information is maintained in the Contentstack Regions Registry.

### Registry URL

```text
https://artifacts.contentstack.com/regions.json
```

The registry contains:

- Region identifiers
- Region aliases
- Default region information
- Service endpoint mappings

### Example

```json
{
  "regions": [
    {
      "id": "na",
      "alias": ["us", "aws-na"],
      "isDefault": true,
      "endpoints": {
        "contentDelivery": "https://cdn.contentstack.io",
        "contentManagement": "https://api.contentstack.io"
      }
    }
  ]
}
```

---

## Endpoint Resolution Contract

All SDKs must expose a public endpoint resolution API.

```text
getContentstackEndpoint(
    region,
    service,
    omitProtocol = false
)
```

### Parameters

| Parameter | Description |
|-----------|-------------|
| `region` | Region identifier or alias |
| `service` | Service name |
| `omitProtocol` | Removes protocol prefix from returned URL |

### Returns

- Service URL when a service is specified
- Complete endpoint map when service is omitted

---

## Region Resolution Rules

Region matching must:

- Ignore case
- Trim whitespace
- Support aliases
- Support both dash (`-`) and underscore (`_`) variants where defined

### Examples

| Input | Resolved Region |
|--------|----------------|
| `na` | `na` |
| `us` | `na` |
| `aws-na` | `na` |
| `AWS_NA` | `na` |
| `eu` | `eu` |
| `azure-na` | `azure-na` |
| `gcp-eu` | `gcp-eu` |

If no region is found:

```text
Invalid region
```

---

## Service Resolution Rules

SDKs must:

1. Locate the resolved region.
2. Locate the service name within the region endpoints.
3. Return the endpoint URL.

### Example

```text
Region: eu
Service: contentDelivery

Result:
https://eu-cdn.contentstack.com
```

If the service is unavailable:

```text
Service not found
```

---

## Supported Service Names

- `contentDelivery`
- `contentManagement`
- `graphqlDelivery`
- `graphqlPreview`
- `preview`
- `auth`
- `application`
- `images`
- `assets`
- `automate`
- `launch`
- `developerHub`
- `brandKit`
- `genAI`
- `personalizeManagement`
- `personalizeEdge`
- `composableStudio`
- `assetManagement`

SDKs must not hardcode this list. The registry remains the source of truth.

---

## Registry Loading Requirements

Recommended priority:

1. In-memory cache
2. Local registry file
3. Registry download fallback

Examples:

- JavaScript SDK: Build-time download
- PHP SDK: Install-time download with runtime fallback
- Java SDK: Build-time download via Maven (`generate-resources` phase) with runtime fallback

---

## SDK Integration Requirements

```text
Resolve Region
      ↓
Resolve contentDelivery Endpoint
      ↓
Configure SDK Host
      ↓
Execute API Requests
```

The SDK host must be configured using the resolved endpoint rather than a hardcoded hostname.

---

## Error Handling

| Scenario | Error |
|-----------|--------|
| Empty Region | Empty region provided |
| Invalid Region | Invalid region |
| Invalid Service | Service not found |
| Registry Unavailable | Unable to load regions registry |

---

## Caching Requirements

Goals:

- Avoid repeated disk reads
- Avoid repeated network requests
- Improve endpoint lookup performance

Cache implementation is SDK-specific.

---

## Future Compatibility

SDK implementations must not:

- Hardcode endpoint URLs
- Hardcode region mappings
- Hardcode service name mappings

All endpoint information must originate from the Regions Registry.

---

## SDK Examples

### Java

```java
import com.contentstack.utils.Endpoint;

// Get a specific service URL
String cdaUrl = Endpoint.getContentstackEndpoint("eu", "contentDelivery");
// → "https://eu-cdn.contentstack.com"

// Get the host without the https:// scheme
String host = Endpoint.getContentstackEndpoint("eu", "contentDelivery", true);
// → "eu-cdn.contentstack.com"

// Get all endpoints for a region
Map<String, String> all = Endpoint.getContentstackEndpoint("eu");
// → { "contentDelivery": "https://eu-cdn.contentstack.com", ... }

// Get all endpoints without the scheme
Map<String, String> hosts = Endpoint.getContentstackEndpoint("eu", true);
```

#### Region aliases

```java
// All of the following resolve to the same NA region
Endpoint.getContentstackEndpoint("na",     "contentDelivery"); // → https://cdn.contentstack.io
Endpoint.getContentstackEndpoint("us",     "contentDelivery"); // → https://cdn.contentstack.io
Endpoint.getContentstackEndpoint("aws-na", "contentDelivery"); // → https://cdn.contentstack.io
Endpoint.getContentstackEndpoint("AWS_NA", "contentDelivery"); // → https://cdn.contentstack.io
```

#### Available via `Utils` (proxy)

```java
import com.contentstack.utils.Utils;

// Identical result to Endpoint.getContentstackEndpoint()
String url  = Utils.getContentstackEndpoint("eu", "contentDelivery");
String host = Utils.getContentstackEndpoint("eu", "contentDelivery", true);
Map<String, String> all = Utils.getContentstackEndpoint("eu");
```

#### Error handling

```java
try {
    Endpoint.getContentstackEndpoint("", "contentDelivery");
} catch (IllegalArgumentException e) {
    // "Empty region provided. Please provide a valid region."
}

try {
    Endpoint.getContentstackEndpoint("invalid", "contentDelivery");
} catch (IllegalArgumentException e) {
    // "Invalid region: invalid"
}

try {
    Endpoint.getContentstackEndpoint("na", "unknownService");
} catch (IllegalArgumentException e) {
    // "Service \"unknownService\" not found for region \"na\""
}
```

#### Integration with Delivery SDK

```java
import com.contentstack.sdk.Config;
import com.contentstack.sdk.Contentstack;
import com.contentstack.sdk.Query;
import com.contentstack.sdk.QueryResult;
import com.contentstack.sdk.QueryResultsCallBack;
import com.contentstack.sdk.ResponseType;
import com.contentstack.sdk.Stack;
import com.contentstack.utils.Endpoint;

// 1. Resolve the host for the chosen region (omit https:// for setHost)
String host = Endpoint.getContentstackEndpoint("eu", "contentDelivery", true);
// → "eu-cdn.contentstack.com"

// 2. Wire it into a Config and create the Stack
Config config = new Config();
config.setHost(host);

Stack stack = Contentstack.stack("<API_KEY>", "<DELIVERY_TOKEN>", "<ENVIRONMENT>", config);

// 3. Fetch entries — all requests now go to the EU CDN
Query query = stack.contentType("blog").query();
query.find(new QueryResultsCallBack() {
    @Override
    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
        if (error != null) {
            System.err.println(error.getErrorMessage());
            return;
        }
        queryResult.getResultObjects().forEach(entry ->
                System.out.println(entry.getTitle()));
    }
});
```

Change one string to switch regions — everything else stays the same:

```java
// NA
String host = Endpoint.getContentstackEndpoint("na", "contentDelivery", true);
// → "cdn.contentstack.io"

// EU
String host = Endpoint.getContentstackEndpoint("eu", "contentDelivery", true);
// → "eu-cdn.contentstack.com"

// Azure NA
String host = Endpoint.getContentstackEndpoint("azure-na", "contentDelivery", true);
// → "azure-na-cdn.contentstack.com"

// GCP EU
String host = Endpoint.getContentstackEndpoint("gcp-eu", "contentDelivery", true);
// → "gcp-eu-cdn.contentstack.com"
```

Read region from environment variable (recommended for production):

```java
String region = System.getenv().getOrDefault("CONTENTSTACK_REGION", "na");

Config config = new Config();
config.setHost(Endpoint.getContentstackEndpoint(region, "contentDelivery", true));

Stack stack = Contentstack.stack(
        System.getenv("CONTENTSTACK_API_KEY"),
        System.getenv("CONTENTSTACK_DELIVERY_TOKEN"),
        System.getenv("CONTENTSTACK_ENVIRONMENT"),
        config
);
```

#### Refreshing `regions.json`

```bash
# Runs automatically on every Maven build (generate-resources phase)
mvn generate-resources

# Or refresh manually
bash scripts/download-regions.sh
```
