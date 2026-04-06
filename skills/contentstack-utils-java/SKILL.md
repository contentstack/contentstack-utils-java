---
name: contentstack-utils-java
description: Public API — Utils, GQL, DefaultOption, RTE/embedded JSON; use when changing SDK behavior or contracts
---

# Contentstack Utils API – Contentstack Utils (Java)

## When to use

- Changing **`Utils`**, **`GQL`**, **`DefaultOption`**, **`Option`**, callbacks, or embedded/RTE logic.
- Reviewing JSON shape assumptions for CDA or GraphQL responses.

## Instructions

### Scope

- Artifact **`com.contentstack.sdk:utils`** only **transforms** JSON that apps or the [Java Delivery SDK](https://www.contentstack.com/docs/developers/sdks/content-delivery-sdk/java) already fetched.
- **Authentication, API keys, delivery tokens, and `includeEmbeddedItems()`** are out of scope here — handled by the SDK or app code.

### Entry points

- **`com.contentstack.utils.Utils`** — `render`, `renderContent`, `renderContents`, `jsonToHTML` for REST/CDA-style JSON with `_embedded_items`; dot-paths into entries (e.g. `group.field`). Variant-related helpers as documented in `Utils`.
- **`com.contentstack.utils.GQL`** — `jsonToHTML` for GraphQL-shaped entries (`embedded_itemsConnection`, `edges`, `node`, JSON RTE under `json`). Do not instantiate `GQL` (private constructor).

### Rendering and options

- Implement **`com.contentstack.utils.interfaces.Option`** or extend **`com.contentstack.utils.render.DefaultOption`** for custom embedded HTML, marks, and nodes.
- Use **`com.contentstack.utils.interfaces.NodeCallback`** and **`com.contentstack.utils.helper.Metadata`** with **`embedded.StyleType`** / **`embedded.ItemType`** as in existing code.

### Data flow and compatibility

- Shared traversal: **`AutomateCommon`**; JSON RTE trees: **`NodeToHTML`**.
- Preserve keys and HTML class names (`_embedded_items`, `embedded-entry`, etc.) unless shipping a **breaking** version with changelog.
- Prefer null-safe **`opt*`** / **`has`** on `JSONObject` / `JSONArray`.

### Alignment with Contentstack

- Entry JSON shapes align with the [Content Delivery API](https://www.contentstack.com/docs/apis/content-delivery-api/) as consumed by the Java SDK; root **`README.md`** shows `Contentstack.stack`, `Entry`, `Query` usage **outside** this JAR.

## References

- **`skills/java/SKILL.md`** — language and package conventions.
- **`skills/testing/SKILL.md`** — tests for API changes.
- Root **`README.md`** — Maven coordinates and embedded-items examples.
