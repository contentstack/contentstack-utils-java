---
name: contentstack-utils-java
description: Use when implementing RTE, embedded items, Utils/GQL, DefaultOption — JSON shapes and no HTTP client in-module
---

# Contentstack Utils (Java) – skill

Use this skill when changing **`Utils`**, **`GQL`**, **`DefaultOption`**, callbacks, or embedded/RTE behavior in this repository.

## Scope

This artifact (`com.contentstack.sdk:utils`) **renders** RTE and embedded content from JSON already obtained from Contentstack. **Authentication, stack keys, delivery tokens, and `includeEmbeddedItems()`** are handled by the **[Contentstack Java SDK](https://www.contentstack.com/docs/developers/sdks/content-delivery-sdk/java)** (or your own HTTP layer), not by this repo.

## Core types

- **`com.contentstack.utils.Utils`** — `render`, `renderContent`, `renderContents`, `jsonToHTML` (CDA-style JSON with `_embedded_items`).
- **`com.contentstack.utils.GQL`** — `jsonToHTML` for GraphQL entry shapes.
- **`com.contentstack.utils.render.DefaultOption`** / **`interfaces.Option`** — custom rendering; see root `README.md`.
- **`com.contentstack.utils.helper.Metadata`** — embedded metadata.

## JSON contracts

- REST: `_embedded_items`; RTE HTML classes such as `embedded-entry`, `embedded-asset`.
- GraphQL: `embedded_itemsConnection`, `edges`, `node`, `uid` matching metadata.

## References

- Project rule: `.cursor/rules/contentstack-utils-java.mdc`
- Root `README.md` for usage with `Contentstack.stack`, `Entry`, `Query`.
