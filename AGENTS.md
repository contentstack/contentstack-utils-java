# Contentstack Utils (Java) – Agent guide

This document is the main entry point for AI agents working in **contentstack-utils-java**. Repo: [contentstack-utils-java](https://github.com/contentstack/contentstack-utils-java).

## Project

- **Name:** Contentstack Utils (Java) — Maven artifact `com.contentstack.sdk:utils`.
- **Purpose:** Library for **rendering Rich Text Editor (RTE) content and embedded items** from Contentstack entry JSON (REST/CDA-style with `_embedded_items`) and GraphQL-shaped responses. It does **not** perform HTTP calls or manage API keys; use it with the [Contentstack Java Delivery SDK](https://www.contentstack.com/docs/developers/sdks/content-delivery-sdk/java) after fetching entries.

## Tech stack

- **Language:** Java **17** (`maven-compiler-plugin` `<release>17</release>` in `pom.xml`).
- **Build:** Maven.
- **Testing:** JUnit 4, Maven Surefire, JaCoCo (see `pom.xml`; Surefire uses `testFailureIgnore`).
- **JSON / HTML:** `org.json`, Jsoup, `commons-text`; `javax.validation` API; `spring-web` is a compile dependency (not a public HTTP client surface for this module).

## Main entry points

- **`Contentstack.stack(...)`** — Not in this repo; provided by the Java SDK (see root `README.md`).
- **`com.contentstack.utils.Utils`** — `render`, `renderContent`, `jsonToHTML`, variant helpers, etc.
- **`com.contentstack.utils.GQL`** — GraphQL entry `jsonToHTML`.
- **`com.contentstack.utils.render.DefaultOption`** / **`interfaces.Option`** — custom RTE/embedded rendering.
- **Paths:** `src/main/java/com/contentstack/utils/` (production), `src/test/java/com/contentstack/utils/` (tests). Optional **`sample/`** demonstrates Delivery SDK + Utils together.

## Commands

- **Build and test:** `mvn clean test`
- **Compile only:** `mvn clean compile`
- **Single test class:** `mvn test -Dtest=UtilTests`
- **Javadoc (optional):** `mvn javadoc:javadoc`

CI uses Java **17** (`.github/workflows/maven-publish.yml`). **Snyk** runs on PRs (`.github/workflows/sca-scan.yml`).

## Rules and skills

### `.cursor/rules/`

| Resource | Role |
|----------|------|
| **README.md** | Index of all rules and when they apply |
| **dev-workflow.md** | Branches, tests, PRs, optional TDD |
| **java.mdc** | Applies to `**/*.java`: Java 17, `com.contentstack.utils`, JSON/Jsoup |
| **contentstack-utils-java.mdc** | Applies to `src/main/java/**/*.java`: Utils/GQL, RTE, embedded JSON |
| **testing.mdc** | Applies to `src/test/**/*.java`: JUnit 4, fixtures, JaCoCo |
| **code-review.mdc** | Always applied: PR checklist |

### `skills/`

| Skill | Use when |
|-------|----------|
| **contentstack-utils-java** | Changing RTE, embedded items, `Utils` / `GQL`, `DefaultOption`, callbacks |
| **testing** | Adding or refactoring tests and fixtures |
| **code-review** | Reviewing PRs or pre-merge self-review |
| **framework** | Editing `pom.xml`, plugins, publishing, or `sample/` dependency management |

See **`skills/README.md`** for details. For editor-wide Cursor user skills (if configured), this repo’s project skills live only under **`./skills/`**.

## Official documentation

- [Contentstack Developers](https://www.contentstack.com/docs/)
- [Content Delivery API](https://www.contentstack.com/docs/apis/content-delivery-api/)
- Root **`README.md`** — Maven coordinates and embedded-items examples
