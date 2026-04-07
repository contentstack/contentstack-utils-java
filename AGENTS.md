# Contentstack Utils (Java) – Agent guide

**Universal entry point** for contributors and AI agents. Detailed conventions live in **`skills/*/SKILL.md`**.

## What this repo is

| Field | Detail |
|--------|--------|
| **Name:** | [contentstack-utils-java](https://github.com/contentstack/contentstack-utils-java) — Maven `com.contentstack.sdk:utils` |
| **Purpose:** | Library for rendering **Rich Text Editor (RTE)** content and **embedded items** from Contentstack entry JSON (REST/CDA-style with `_embedded_items`) and GraphQL-shaped responses. Consumed by the [Contentstack Java Delivery SDK](https://www.contentstack.com/docs/developers/sdks/content-delivery-sdk/java) and apps that already hold entry JSON. |
| **Out of scope (if any):** | **No HTTP client** in this package: no stack API calls, tokens, or `includeEmbeddedItems()` — those belong to the Delivery SDK or your app. Optional **`sample/`** wires the SDK + Utils for manual testing only. |

## Tech stack (at a glance)

| Area | Details |
|------|---------|
| **Language** | Java **17** — `maven-compiler-plugin` `<release>17</release>` in root `pom.xml` (legacy `1.8` properties in `pom.xml` are not authoritative). |
| **Build** | **Maven** — root `pom.xml`; optional module `sample/pom.xml`. |
| **Tests** | **JUnit 4**, Maven **Surefire** (`src/test/java/com/contentstack/utils/**/*.java`). Surefire **`testFailureIgnore`** is `true` — check `target/surefire-reports/`. |
| **Lint / coverage** | No Checkstyle/Spotless in repo — match existing style. **JaCoCo** (`target/site/jacoco/` after `mvn test`). |
| **Other** | JSON: `org.json`, `json-simple` (provided). HTML: **Jsoup**. `spring-web` compile dependency — not a public REST client API for this module. **Snyk** on PRs (`.github/workflows/sca-scan.yml`). |

## Commands (quick reference)

| Command type | Command |
|--------------|---------|
| **Build** | `mvn clean compile` |
| **Test** | `mvn clean test` |
| **Lint** | *(none configured — rely on IDE and code review)* |

| Optional | Command / location |
|----------|---------------------|
| Single test class | `mvn test -Dtest=UtilTests` |
| Javadoc | `mvn javadoc:javadoc` |
| Sample (after `mvn install` with skips if needed) | `mvn -f sample/pom.xml compile` |
| **CI** | Java **17** publish: `.github/workflows/maven-publish.yml` · SCA: `.github/workflows/sca-scan.yml` · branch rules: `.github/workflows/check-branch.yml` |

## Where the documentation lives: skills

| Skill | Path | What it covers |
|-------|------|----------------|
| **Development workflow** | [`skills/dev-workflow/SKILL.md`](skills/dev-workflow/SKILL.md) | Branches, CI, build/test commands, PR expectations, optional TDD. |
| **Java (language & layout)** | [`skills/java/SKILL.md`](skills/java/SKILL.md) | Java 17, `com.contentstack.utils` packages, naming, JSON/Jsoup, dependencies. |
| **Contentstack Utils API** | [`skills/contentstack-utils-java/SKILL.md`](skills/contentstack-utils-java/SKILL.md) | Public API: `Utils`, `GQL`, `DefaultOption`, JSON contracts, RTE/embedded boundaries. |
| **Testing** | [`skills/testing/SKILL.md`](skills/testing/SKILL.md) | JUnit 4, fixtures, Surefire/JaCoCo, offline tests vs `sample/`. |
| **Code review** | [`skills/code-review/SKILL.md`](skills/code-review/SKILL.md) | PR checklist, optional Blocker/Major/Minor. |
| **Framework (build & tooling)** | [`skills/framework/SKILL.md`](skills/framework/SKILL.md) | Maven plugins, publishing, GPG, Central, `sample/` dependency hygiene. |

An index with **when to use** hints is in [`skills/README.md`](skills/README.md).

## Using Cursor (optional)

If you use **Cursor**, [`.cursor/rules/README.md`](.cursor/rules/README.md) only points to **`AGENTS.md`** — same docs as everyone else.
