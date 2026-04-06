# Cursor Rules – Contentstack Utils (Java)

This directory contains Cursor AI rules for **contentstack-utils-java**. Rules give persistent context so the AI follows project conventions and Contentstack RTE / embedded-item patterns.

## How rules are applied

- **File-specific rules** use the `globs` frontmatter: they apply when you open or edit files matching that pattern.
- **Always-on rules** use `alwaysApply: true`: they are included in every conversation in this project.

## Rule index

| File | Applies when | Purpose |
|------|--------------|---------|
| **dev-workflow.md** | (Reference only; no glob) | Development workflow: branches, running tests, PR expectations, optional TDD. |
| **java.mdc** | Editing any `**/*.java` file | Java 17 standards: naming, package layout under `com.contentstack.utils`, JSON/Jsoup, Javadoc, dependencies. |
| **contentstack-utils-java.mdc** | Editing `src/main/java/**/*.java` | Utils-specific patterns: `Utils` / `GQL`, RTE and embedded JSON, `DefaultOption` / callbacks; alignment with entry JSON shapes (no HTTP client in this module). |
| **testing.mdc** | Editing `src/test/**/*.java` | Testing patterns: JUnit 4, fixtures, JaCoCo, Surefire (including `testFailureIgnore`). |
| **code-review.mdc** | Always | PR / review checklist: API stability, error handling, backward compatibility, dependencies and security (e.g. SCA), tests. |

## Related

- **AGENTS.md** (repo root) – Main entry point for AI agents: project overview, entry points, commands, pointers to rules and skills.
- **skills/** – Reusable skill docs (`contentstack-utils-java`, `testing`, `code-review`, `framework`) for deeper guidance on specific tasks.
