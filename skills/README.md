# Skills – Contentstack Utils (Java)

This directory contains **skills**: reusable guidance for AI agents (and developers) on specific tasks. Each skill is a folder with a `SKILL.md` file (YAML frontmatter: `name`, `description`).

## When to use which skill

| Skill | Use when |
|-------|----------|
| **contentstack-utils-java** | Implementing or changing RTE/embedded behavior: `Utils`, `GQL`, `DefaultOption`, callbacks, JSON shapes for CDA/GraphQL entries. |
| **testing** | Writing or refactoring tests: JUnit 4, fixtures, Surefire, JaCoCo, offline unit tests. |
| **code-review** | Reviewing a PR or preparing your own: API stability, compatibility, dependencies/security, tests. |
| **framework** | Changing `pom.xml`, Java release level, plugins (Surefire, JaCoCo, javadoc, GPG, Central publishing), or sample module dependency management. |

## How agents should use skills

- **contentstack-utils-java:** Apply when editing `src/main/java/com/contentstack/utils/**` or adding RTE/embedded-related behavior. Follow existing JSON/HTML contracts and public API rules.
- **testing:** Apply when creating or modifying `src/test/**`. Respect `testFailureIgnore` and JaCoCo layout.
- **code-review:** Apply when simulating or performing a PR review; use the checklist and optional severity levels.
- **framework:** Apply when touching build or release tooling; keep consumer impact (Java SDK) in mind.

Each skill’s `SKILL.md` contains more detail and cross-links to `.cursor/rules/`.
