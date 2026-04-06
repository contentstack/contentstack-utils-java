---
name: java
description: Java 17, package layout, naming, JSON/Jsoup, Javadoc — use when editing any production or test Java in this repo
---

# Java – Contentstack Utils (Java)

## When to use

- Editing any `.java` file under this repository.
- Adding dependencies or debating style vs other Contentstack SDKs.

## Instructions

### Language and runtime

- **Java 17** via `maven-compiler-plugin` `<release>17</release>` in `pom.xml`. Ignore legacy `maven.compiler.source/target` `1.8` — the compiler plugin wins.
- Avoid raw types; use generics where applicable.

### Package and layout

- Production code lives under **`com.contentstack.utils`** and subpackages: `render`, `node`, `embedded`, `helper`, `interfaces`.
- Do not add new top-level packages without team alignment.

### Naming

- **Classes:** PascalCase (`Utils`, `DefaultOption`, `AutomateCommon`).
- **Methods / variables:** camelCase.
- **Tests:** Mixed conventions already in use — see **`skills/testing/SKILL.md`**.

### JSON and HTML

- Prefer **`org.json`** (`JSONObject`, `JSONArray`) for APIs and internals used by `Utils` and `GQL`.
- Use **Jsoup** for RTE HTML; follow patterns in `AutomateCommon` and `Utils`.

### Validation and utility types

- `javax.validation.constraints` (e.g. `@NotNull`) on some public methods — keep Javadoc aligned with null behavior.
- Private constructors for non-instantiable types where the codebase already does (`GQL`, `AutomateCommon`).

### Dependencies

- Small JAR consumed by the Contentstack Java SDK — prefer minimal dependencies; justify additions in `pom.xml`.
- **No Lombok** in this repo unless explicitly adopted by the team.

### Documentation

- Javadoc on public API; examples must match real entry points (`Utils.render`, `GQL.jsonToHTML`, `DefaultOption`).

## References

- **`skills/contentstack-utils-java/SKILL.md`** — RTE, embedded, GraphQL shapes.
- **`skills/testing/SKILL.md`** — test naming and layout.
- **`AGENTS.md`** — tech stack summary.
