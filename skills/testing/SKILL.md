---
name: testing
description: Use when adding or refactoring tests — JUnit 4, fixtures, Surefire testFailureIgnore, JaCoCo
---

# Testing – Contentstack Utils (Java)

Use this skill when creating or modifying test classes under `src/test/java` or fixtures under `src/test/resources`.

## Framework

- **JUnit 4** (`junit:junit` in `pom.xml`).
- **Maven Surefire** runs tests from `src/test/java`.

## Surefire

`pom.xml` sets **`testFailureIgnore`** to **`true`**. Check **`target/surefire-reports/`**; a green Maven exit code does not guarantee all tests passed.

## Naming and layout

- Mirror package **`com.contentstack.utils`**; examples: `UtilTests`, `DefaultOptionTests`, `AssetLinkTest`, `TestRte`.
- Helpers: `ReadResource`, `DefaultOptionClass`, etc.

## Fixtures

- JSON under **`src/test/resources/`**. Paths are often relative to the module root (see `UtilTests`).

## Coverage

- **JaCoCo** — report at **`target/site/jacoco/index.html`** after `mvn test`.

## Credentials

- Default unit tests are **offline**. The **`sample/`** project may require stack env vars — see `sample/README.md`.

## References

- Project rule: `.cursor/rules/testing.mdc`
