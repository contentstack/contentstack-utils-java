# Development workflow – Contentstack Utils (Java)

Use this as the standard workflow when contributing to **contentstack-utils-java**. For Java style and test conventions, see **java.mdc** and **testing.mdc**.

## Branches

- Default integration for PRs is often **`staging`**; merging into **`master`** may be restricted (see `.github/workflows/check-branch.yml`).
- Feature/fix branches often use ticket-style names (e.g. `fix/DX-5734`).

## Running tests

- **All tests:** `mvn test`
- **Build only:** `mvn clean compile`
- **Sample module** (after installing the parent JAR): `mvn -f sample/pom.xml compile` — see `sample/README.md` for credentials and env vars if using the Delivery SDK.

Run tests before opening a PR. Review **`target/surefire-reports/`** if anything looks flaky (`testFailureIgnore` is set in `pom.xml`).

## Pull requests

- Ensure the build passes: `mvn clean test` (and sample compile if you changed `sample/`).
- Follow the **code-review** rule (`.cursor/rules/code-review.mdc`) for the PR checklist.
- Keep public API backward-compatible unless releasing a breaking version; update `Changelog.md` when behavior changes.

## Optional: TDD

If the team uses TDD, follow RED–GREEN–REFACTOR: failing test first, then implementation, then refactor. The **testing** rule and **skills/testing** describe structure and Surefire/JaCoCo behavior.

## CI and security

- **Java 17** in publish workflow (`.github/workflows/maven-publish.yml`).
- **Snyk** Maven scan on PRs (`.github/workflows/sca-scan.yml`).
- **Javadoc:** `mvn javadoc:javadoc` (optional locally); attach phase may use `-Xdoclint:none` per `pom.xml`.

## Lint / format

- No Checkstyle/Spotless in repo — match existing style in surrounding files.
