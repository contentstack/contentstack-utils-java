---
name: testing
description: JUnit 4, Surefire, JaCoCo, fixtures, offline policy — use when adding or changing tests
---

# Testing – Contentstack Utils (Java)

## When to use

- Creating or editing tests under `src/test/java/com/contentstack/utils/`.
- Adding JSON under `src/test/resources/`.
- Investigating CI failures or coverage gaps.

## Instructions

### Framework

- **JUnit 4** (`junit:junit`, test scope in `pom.xml`).
- **Maven Surefire** runs classes from `src/test/java`.

### Surefire caveat

- **`testFailureIgnore`** is **`true`** in `pom.xml`. Always inspect **`target/surefire-reports/`** — a successful Maven exit code does not guarantee all tests passed.

### Naming and layout

- Mirror package **`com.contentstack.utils`**.
- Existing patterns: `UtilTests`, `DefaultOptionTests`, `AssetLinkTest`, `TestRte`, `TestMetadata`, `Test*`, `*Test`, `*Tests`.
- No separate `*IT` Maven profile in this repo; optional **`sample/`** uses the Delivery SDK with env vars (see `sample/README.md`).

### Fixtures

- JSON and assets under **`src/test/resources/`** (e.g. `multiple_rich_text_content.json`, `reports/`). Loading patterns: `ReadResource`, `UtilTests` `@BeforeClass`.

### Helpers

- `ReadResource`, `DefaultOptionClass`, and similar helpers — keep tests **deterministic** and **offline** (no live API for default unit tests).

### Coverage

- **JaCoCo** on `mvn test`; HTML report **`target/site/jacoco/index.html`**.

## References

- **`skills/dev-workflow/SKILL.md`** — when to run tests before PRs.
- **`skills/framework/SKILL.md`** — Maven/Surefire configuration.
- **`skills/code-review/SKILL.md`** — test expectations for reviews.
