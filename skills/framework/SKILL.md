---
name: framework
description: Use when changing Maven build, Java release, plugins, publishing, or dependency hygiene
---

# Framework (build & tooling) – Contentstack Utils (Java)

Use this skill when editing **`pom.xml`**, CI-related Maven config, or release/publishing behavior — analogous to HTTP/config layers in the Java CDA SDK repo, but here the “framework” is **Maven**, **JaCoCo**, **Surefire**, and **Central publishing**.

## Build commands

```bash
mvn clean compile
mvn test
mvn package
```

Local install without GPG/Javadoc (common for dev):

```bash
mvn install -DskipTests -Dmaven.javadoc.skip=true -Dgpg.skip=true
```

## Java version

- **`maven-compiler-plugin`** — **`<release>17</release>`**. Align local JDK and CI (`.github/workflows/maven-publish.yml`) with **Java 17**.

## Key plugins

| Plugin | Role |
|--------|------|
| `maven-surefire-plugin` | Runs JUnit; note `testFailureIgnore` |
| `jacoco-maven-plugin` | Coverage; `target/site/jacoco/` |
| `maven-javadoc-plugin` | Javadoc JAR (`-Xdoclint:none` in execution) |
| `central-publishing-maven-plugin` | Maven Central on release |
| `maven-gpg-plugin` | Signs artifacts for release |

## Coordinates

- **groupId:** `com.contentstack.sdk`
- **artifactId:** `utils`
- **version:** from root `pom.xml`

## Dependency hygiene

- Minimal dependencies; this JAR is consumed by the Contentstack Java SDK.
- **Snyk** on PRs: `.github/workflows/sca-scan.yml`.
- **`sample/pom.xml`:** Keep `contentstack.utils.version` aligned with the root version when bumping releases; use `dependencyManagement` for transitive CVE overrides as needed.

## Formatting

- No repo-wide Checkstyle/Spotless — match existing style.
