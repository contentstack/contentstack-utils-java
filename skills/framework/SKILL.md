---
name: framework
description: Maven, plugins, Java 17, publishing, sample module deps — use when editing pom.xml or release tooling
---

# Framework (build & tooling) – Contentstack Utils (Java)

## When to use

- Editing root **`pom.xml`** or **`sample/pom.xml`**.
- Changing release/publish flow, signing, or CI-related Maven settings.
- Aligning **`sample/`** `contentstack.utils.version` with a new library release.

## Instructions

### Build commands

```bash
mvn clean compile
mvn test
mvn package
```

Local install without GPG/Javadoc (common for dev before `sample/`):

```bash
mvn install -DskipTests -Dmaven.javadoc.skip=true -Dgpg.skip=true
```

### Java version

- **`maven-compiler-plugin`** — **`<release>17</release>`**. Match **Java 17** locally and in `.github/workflows/maven-publish.yml`.

### Key plugins (root `pom.xml`)

| Plugin | Role |
|--------|------|
| `maven-surefire-plugin` | Runs JUnit; note `testFailureIgnore` |
| `jacoco-maven-plugin` | Coverage → `target/site/jacoco/` |
| `maven-javadoc-plugin` | Javadoc JAR; `-Xdoclint:none` in execution |
| `central-publishing-maven-plugin` | Maven Central publishing |
| `maven-gpg-plugin` | Signs artifacts on release |

### Coordinates

- **groupId:** `com.contentstack.sdk`
- **artifactId:** `utils`
- **version:** root `pom.xml` `<version>`

### Dependency hygiene

- Keep the dependency set small; this JAR is pulled in by the Contentstack Java SDK.
- **Snyk** on PRs: `.github/workflows/sca-scan.yml`.
- **`sample/pom.xml`:** Keep **`contentstack.utils.version`** in sync when bumping the root version; use **`dependencyManagement`** for transitive CVE overrides when needed.

### Formatting

- No Checkstyle/Spotless — match surrounding code (`skills/java/SKILL.md`).

## References

- **`skills/dev-workflow/SKILL.md`** — when to run full build/test.
- **`skills/testing/SKILL.md`** — Surefire/JaCoCo behavior.
- **`AGENTS.md`** — CI file pointers.
