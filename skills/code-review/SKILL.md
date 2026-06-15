---
name: code-review
description: PR checklist and optional Blocker/Major/Minor — use when reviewing or before submitting a PR
---

# Code review – Contentstack Utils (Java)

## When to use

- Reviewing another contributor’s pull request.
- Self-review before merge.
- Auditing API, security, or test coverage for a change set.

## Instructions

### API design and stability

- [ ] **Public API:** New or changed methods on `Utils`, `GQL`, `DefaultOption`, or `interfaces` are necessary, Javadoc’d, and safe for `com.contentstack.sdk:utils` consumers.
- [ ] **Backward compatibility:** Breaking changes only with major version / **`CHANGELOG.md`** plan.
- [ ] **Naming:** Consistent with existing Utils and RTE/embedded terminology.

### Error handling and robustness

- [ ] **JSON:** Missing keys / `_embedded_items` behave predictably; no accidental NPEs or silent semantic changes.
- [ ] **Null safety:** `JSONObject` / `JSONArray` access follows existing `opt*` / `has` patterns.

### Dependencies and security

- [ ] **Dependencies:** `pom.xml` changes are justified; consider downstream Java SDK consumers.
- [ ] **SCA:** Snyk / team process (`.github/workflows/sca-scan.yml`) — address or defer with a ticket.

### Testing

- [ ] **Coverage:** New behavior has tests and fixtures under `src/test/java` / `src/test/resources` as needed.
- [ ] **Surefire:** With `testFailureIgnore`, verify **`target/surefire-reports/`**, not only exit code.

### Severity (optional)

| Level | Examples |
|-------|----------|
| **Blocker** | Unapproved breaking public API; critical CVE; no tests for new behavior. |
| **Major** | Undocumented HTML/JSON behavior change; missing Javadoc on new public API; risky dependency bump. |
| **Minor** | Style, typos, internal refactor with equivalent coverage. |

## References

- **`skills/testing/SKILL.md`** — test conventions and Surefire.
- **`skills/contentstack-utils-java/SKILL.md`** — API boundaries.
- **`AGENTS.md`** — stack and commands.
