# UI playbook: the new Solr Admin UI

Facts about the new UI effort, kept current as the factory learns them. The assessment evidence lives in `reports/new-ui-*.md` (four rounds, 19 of Christos Malliaridis's tickets, assessed 2026-10-23).

## The effort

- Owner: Christos Malliaridis (malliaridis), committer. Eric Pugh relayed on 2026-10-23 that Christos is interested in Nick's help and would reach out.
- The new UI is Kotlin with Compose Multiplatform, living at the repository root of apache/solr as `ui/`, with `platform/` and `kotlin-js-store` alongside. It is its own build: `ui/settings.gradle.kts` sets rootProject.name = "solr-ui" (independent sub-project since PR #4650, September 2026). Run its tasks from the repo root as `./gradlew -p ui <task>`.
- Modules: `ui/shared` (the bulk of the components and tests), `ui/webApp` (the browser app), `ui/desktopApp` (desktop clients; its build already declares native distributions for macOS, Windows, and Linux).
- SOLR-16465 (migrate the Admin UI) was retitled to a React migration and is Resolved; it is not the live effort. The live effort is this Kotlin UI and the roughly 63 tickets under Christos's name.

## Test setup

- CI: `.github/workflows/new-ui-check.yml` runs `./gradlew -p ui check` on ubuntu-latest, where a browser is preinstalled. Run 39835983878 (2026-10-22) shows `:shared:wasmJsBrowserTest` and `:shared:jvmTest` executing and passing.
- The wasmJs browser tests run under Karma, which launches a real browser binary; it locates Chrome through the `CHROME_BIN` environment variable. The tests were disabled in PR #3195 (February 2025); the disable lived in the old `solr/ui` build file and did not survive PR #4650, so the shared module's tests are enabled by default now. `ui/webApp` still sets `enabled = false` for its own browser tests.
- No-browser behavior is unverified: a vm1 probe on 2026-10-23 (`CHROME_BIN` pointed at a nonexistent path) died in `:kotlinWasmNpmInstall` with certificate errors through that VM's egress proxy, before any test ran. The pool probe is `assignments/ui-17676-nobrowser-probe-round-1.md`.
- Local material from earlier setup work is in `material/ui-test-setup/`.

## Where the tickets stand

Full per-ticket verdicts are in the four round reports. The headlines:

- SOLR-17861 (extend updateLicenses to cover the KMP module): the ranked starter contribution. First slice: a `ui`-local check of LICENSE/NOTICE presence and SHA-1 checksums for the desktopApp JVM runtime classpath on linux_x64, wired into `./gradlew -p ui check`.
- SOLR-17970 (publish desktop clients to nightlies): sound packaging already declared; the blocker is ASF infrastructure (non-Linux build hosts, a Jenkins job). A finishing item, not a starter.
- SOLR-17676 (enable wasmJs tests): done for the shared module on GitHub Actions (see Test setup); the comment asking Christos whether that satisfies the ticket is drafted at `jira/jira-comment-SOLR-17676-draft.md`.
- Collections, configsets, index, query, and component tickets: per-ticket slices and verdicts in `reports/new-ui-collections-configsets-round-1.md` and `reports/new-ui-index-query-components-round-1.md`. Auth tickets (SOLR-17885, SOLR-17845) in `reports/new-ui-auth-round-1.md`; a status note for SOLR-17845 was drafted on the old board and sits in its archive.

## How Nick engages here

The staged ask from `comment-formula.md`: evidence first, a sufficiency question, the decision ladder, our recommended scope, then wait for engagement before any review ask. Christos is the named sponsor candidate on these tickets. Nothing posts without Nick's go.
