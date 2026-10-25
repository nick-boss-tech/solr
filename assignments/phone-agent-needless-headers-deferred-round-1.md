# Assignment: remove needless license headers from export-ignored files (deferred, round 1)

Status: DEFERRED. Not claimable until Nick reopens it. Intended host: phone-agent.
Posted by: vm6, 2026-10-25, at Nick's direction.

## Why

David Smiley's condition on the PR 5222 thread: "If it's a small/easy to change to the build to fail if there are needless headers, then okay. Otherwise, it's not worth automating IMO." The two PRs vm6 is opening cover the build side for `dev-docs/` (guard) and for Java sources (tidy). The remaining question is the export-ignored files outside `dev-docs/` that carry ASF headers: 25 under `dev-tools/` and 10 under `solr/monitoring/dev/`. Nick deferred this work because it is not small.

## Scope when reopened

Three steps, in order. Two PRs total with the guard and tidy PRs, so this may be one PR or fold into the guard follow-up. Nick decides.

1. **RAT exclusion for `solr/monitoring/dev/`.** The git-checkout block of `gradle/validation/rat-sources.gradle` does not exclude this path, so removing its headers would fail `rat` in a git checkout. The fallback (no-git) block needs no change: the source archive already drops it. `dev-tools/` is already excluded from RAT.
2. **Remove the headers from the 35 files.** Only the ASF block at the top is removed. Keep a shebang (22 files start with `#!`) or an XML declaration (`solr.rdf`) on line 1. Each file must still parse afterwards: `python -m py_compile` for `.py`, `bash -n` for `.sh`, a YAML load for `.yml`/`.yaml`, an XML parse for `.rdf`, and a `docker compose config` check for the monitoring compose file if docker is available.
3. **Extend the guard.** The dev-docs guard (`gradle/validation/dev-docs-license.gradle`) scans `dev-docs/` only. Extend its scope to `dev-tools/` and `solr/monitoring/dev/` after steps 1 and 2 are merged. Otherwise it fails at once.

## Proof required

- `./gradlew rat` in a git checkout passes after step 1 and after step 2.
- Each of the 35 files parses after step 2 (the checks above), with the file list in the PR body.
- `./gradlew checkDevDocsLicenseHeaders` (or its extended successor) passes on the head and fails on a planted header.

## Limits

- Nothing posts to GitHub or JIRA from this assignment. Opening a PR needs Nick's go.
- The removal is a change to ASF-licensed files outside the release. It is not to be done without Nick's reopening.

## Open question for Nick (when reopened)

Should dev-only content follow one standard, with no license headers outside the release? If no, this assignment closes and the guard stays on `dev-docs/`.
