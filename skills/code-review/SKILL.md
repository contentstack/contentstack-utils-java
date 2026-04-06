---
name: code-review
description: Use when reviewing PRs or before opening a PR — API design, compatibility, dependencies, security, tests
---

# Code review – Contentstack Utils (Java)

Use this skill when performing or preparing a pull request review for this repository.

## When to use

- Reviewing another contributor’s PR.
- Self-review before submission.
- Verifying changes meet API, compatibility, security, and test standards.

## Instructions

Work through the checklist below. Optionally tag items with **Blocker**, **Major**, or **Minor**.

### 1. API design and stability

- [ ] **Public API:** New or changed public surface on `Utils`, `GQL`, `DefaultOption`, or `interfaces` is documented and justified for Maven consumers.
- [ ] **Backward compatibility:** Breaking changes only with version/changelog alignment.
- [ ] **Naming:** Consistent with existing Utils and RTE terminology.

**Severity:** Unapproved breaking API change = Blocker. Missing Javadoc on new public API = Major.

### 2. Robustness

- [ ] **JSON:** No unintended behavior change for `_embedded_items` / GraphQL shapes without tests and release notes.
- [ ] **HTML output:** Customer-visible markup changes are documented.

**Severity:** Silent breaking HTML/JSON behavior = Major.

### 3. Dependencies and security

- [ ] **Dependencies:** Version bumps in `pom.xml` are intentional; consider Snyk/SCA.
- [ ] **SCA:** Address or defer security findings with a ticket.

**Severity:** New critical/high CVE in scope = Blocker.

### 4. Testing

- [ ] **Coverage:** New logic has tests; Surefire reports reviewed when using global `testFailureIgnore`.
- [ ] **Quality:** Tests are deterministic and assert meaningful behavior.

**Severity:** No tests for new behavior = Blocker.

### 5. Optional severity summary

- **Blocker:** Breaking API without approval, security issue, missing tests for new code.
- **Major:** Missing docs, risky dependency bump, unclear JSON/HTML behavior.
- **Minor:** Style, typos, minor refactors.

## References

- Project rule: `.cursor/rules/code-review.mdc`
- Testing skill: `skills/testing/SKILL.md`
