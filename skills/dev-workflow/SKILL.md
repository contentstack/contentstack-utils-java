---
name: dev-workflow
description: Branches, CI, build and test commands, PR expectations, optional TDD — use when onboarding or before a PR
---

# Development workflow – Contentstack Utils (Java)

## When to use

- Starting work in this repository or onboarding.
- Before opening or updating a pull request.
- You need the canonical commands for build, test, sample, and CI pointers.

## Instructions

### Branches

- Default integration for PRs is often **`staging`**; merging into **`master`** may be restricted (see `.github/workflows/check-branch.yml`).
- Feature/fix branches often use ticket-style names (e.g. `fix/DX-5734`).

### Running tests and builds

- **All tests:** `mvn test`
- **Compile only:** `mvn clean compile`
- **Full check before PR:** `mvn clean test` (and `mvn -f sample/pom.xml compile` if you changed `sample/` — install parent first; see `skills/framework/SKILL.md`).
- Review **`target/surefire-reports/`** when debugging: **`testFailureIgnore`** is `true` in `pom.xml` (see `skills/testing/SKILL.md`).

### Pull requests

- Describe the change; link issues/tickets when applicable.
- Keep public API backward-compatible unless releasing a breaking version; update **`Changelog.md`** for user-visible behavior.
- Use **`skills/code-review/SKILL.md`** as the review checklist.

### Optional: TDD

- If the team uses TDD: RED → GREEN → REFACTOR. Structure and Surefire behavior are in **`skills/testing/SKILL.md`**.

### CI and security

- **Java 17** in `.github/workflows/maven-publish.yml`.
- **Snyk** Maven scan on PRs: `.github/workflows/sca-scan.yml`.
- **Javadoc:** optional locally `mvn javadoc:javadoc`; attach phase uses `-Xdoclint:none` per `pom.xml`.

### Lint / format

- No repo-wide Checkstyle/Spotless — match existing style (`skills/java/SKILL.md`).

## References

- **`skills/testing/SKILL.md`** — Surefire, JaCoCo, fixtures.
- **`skills/framework/SKILL.md`** — Maven install skips, publishing, `sample/pom.xml`.
- **`AGENTS.md`** — commands quick reference.
