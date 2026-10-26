# Assignment: PR 5221 license-header fixes, Linux gate, round 1

Status: OPEN

- Type: phase-specific gate assignment; this file is the gate job definition. Claim this assignment once; no second job is implied.
- Intended host: any capable Linux gate host with JDK 21, Git, Python 3, and access to the pinned Gradle 9.7.0 distribution and Solr build dependencies.
- Capability tags: gate, review.
- Requested by Nick in the Windows chat on October 25, 2026 (Eastern): implement the reviewed fixes on top of PR 5221, then hand off a gate job as an assignment.
- Implementation state: **Implemented, Proof Pending**. No Gradle or fixture test has run on Windows.
- Source repository: `https://github.com/nick-boss-tech/solr.git`.
- Branch: `solr-pr-5221-license-headers-submit`.
- Exact head: `2855d3a04f29cc9121ceb677b9e495849cdae25a`.
- Proof base: apache/solr PR 5221 head `e0453c0931cab27fcb5c001f6bca3e412b3b1570`.
- Upstream comparison commit for actual Solr task invocations: `07fdebdda54a24085086281da1cde701e122564d`.
- Implementation claim: `claims/pr-5221-license-headers-windows-round-1.md`.
- Implementation report: `reports/pr-5221-license-headers-implementation-round-1.md`.
- Required receipt: `receipts/PR-5221-license-headers-round-1.md`.

## Scope and intended behavior

This branch retains PR 5221's history and adds two commits. It does not move the public PR head. The follow-up changes are restricted to:

1. `gradle/license-headers.gradle`.
2. `dev-docs/gradle-help/formatting.txt`.
3. `dev-tools/scripts/test_license_headers.py`.

The task still handles Git-added and nonignored untracked Java files. It now preserves recognizable leading notices and generated sources; uses NUL-delimited Git paths and remote main refs with `-PlicenseHeadersBase=<ref>` override; fails failed Git queries before mutation; preserves the input BOM and existing body bytes for valid UTF-8; and orders insertion before actual Java formatting tasks in the root build. Ignored output, symlinks, known generated parser files, and Java in the independent `build-tools/` and `ui/` builds are not rewritten. Independent builds keep manual header addition because their formatter graphs cannot be ordered by the root build's project tasks.

No new dependency or Solr runtime behavior is involved. There is no focused Solr JUnit class; the proof is the real Gradle script exercised by a Python fixture suite, plus actual Solr build integration and checks.

## Claim and exact-head preflight

Read the live board instructions and inspect claims/receipts for this assignment before claiming. Use the host's local build lock, one gate at a time. Record the host, UTC start, JDK/Gradle/Git/Python versions, absolute detached worktree path, log paths, branch/head, and proof base in a new claim. Confirm the fork tip equals the exact head above; fetch and check out that SHA in a clean, gate-owned worktree. Never reuse another worker's checkout.

Use external gate-owned logs, not files in the source worktree. The example commands below use `/tmp/pr-5221-license-headers-round-1` for outputs; record the actual absolute paths and expanded commands if the host uses its established gate root instead.

Before executing proof, run the detached tidy preflight:

```sh
./gradlew tidy --console=plain -PlicenseHeadersBase=07fdebdda54a24085086281da1cde701e122564d
git diff --exit-code
git status --short
```

A dirty preflight is a repair, not green proof. Commit only scoped fixes as Nick Shanin, with no attribution trailers, and plain-push them to this fork branch. Publish a new exact-head gate record naming the replacement SHA before running proof on it; do not silently reinterpret this job as covering another head. Never force-push, and never edit the original PR branch.

## Leg 1: functional suite at the head

From the detached Solr root:

```sh
python3 dev-tools/scripts/test_license_headers.py --log-dir /tmp/pr-5221-license-headers-round-1/head
```

Require **12 tests, no failures or errors**, and retain the unittest summary and every per-invocation Gradle log. The suite creates and removes only its own disposable Git repositories. It runs the actual checked-in Gradle script through the Solr wrapper; it does not compile Solr. Coverage includes:

- Committed, staged, and untracked Java additions, with an existing modified tracked Java file left alone.
- Unicode, spaces, tabs, and newlines in paths.
- ASF, copyright/MIT/BSD, SPDX, long leading notices, generated comments, annotations, and known parser paths.
- An ordinary Java string containing ASF text, which must not suppress a real missing header.
- Ignored output, forced-staged build output, separate builds, nested repositories, and symlinks.
- UTF-8 body preservation, BOM and CRLF handling, and byte-for-byte repeat-run idempotence.
- Remote main refs without local main, local-main fallback, explicit override, unavailable/invalid refs, and a failed diff against unrelated history, with no header writes on Git-query failure.
- A linked Git worktree and a source archive without Git metadata.
- Parallel tidy against two fixture subprojects, with the formatting tasks checking that the headers already exist, followed by a second unchanged run.

The fixture uses Spotless-named probe tasks, not the actual Spotless plugin. Leg 3 is therefore mandatory.

## Leg 2: fail-before proof using the same head fixtures

Extract only the original PR's task script; keep the new test driver unchanged:

```sh
git show e0453c0931cab27fcb5c001f6bca3e412b3b1570:gradle/license-headers.gradle > /tmp/pr-5221-license-headers-round-1/pr5221-original.gradle
python3 dev-tools/scripts/test_license_headers.py --script /tmp/pr-5221-license-headers-round-1/pr5221-original.gradle --log-dir /tmp/pr-5221-license-headers-round-1/base LicenseHeadersTest.test_unicode_git_paths LicenseHeadersTest.test_preserve_existing_notices_and_generated_sources LicenseHeadersTest.test_remote_upstream_main_without_local_main
```

The Python command must return nonzero. Each of these three selected test methods must reach its intended assertion failure: Unicode source remains headerless; existing notices/generated files are altered; and the clean committed source is missed without local main. The notice test has three subtests, so do not confuse its subtest failure count with the count of selected methods.

Gradle startup, wrapper downloads, script compilation failures, timeouts, or missing tools are **not** fail-before proof. Save the logs and the byte/assertion failures. If a selected method passes on the original task, diagnose and record it; do not claim that behavior as a demonstrated regression without a discriminating mutation. The ordering/idempotence checks may be pins unless an original or targeted mutation run demonstrates failure; distinguish those in the receipt.

## Leg 3: actual Solr formatting integration

In a fresh detached worktree of the same head, create one gate-owned, untracked Java probe at `solr/solrj/src/java/org/apache/solr/client/solrj/LicenseHeaderGateProbe.java`. Confirm that path does not already exist before creating it. Use a valid package/class with no header and deliberately unformatted Java, for example:

```java
package org.apache.solr.client.solrj; public class LicenseHeaderGateProbe{public static String value(){return "probe";}}
```

Run:

```sh
./gradlew tidy --parallel --max-workers=2 --console=plain -PlicenseHeadersBase=07fdebdda54a24085086281da1cde701e122564d
./gradlew :solr:solrj:spotlessJavaCheck --console=plain
```

Require the probe to have exactly one leading ASF notice and formatted Java. Retain task-order output demonstrating root `addLicenseHeaders` runs before `:solr:solrj:spotlessJava` and its apply task. Record a SHA-256 of the probe, rerun the same parallel tidy command, and require the hash to remain unchanged. Check for unrelated tracked changes. Remove only the probe created by this gate, then confirm the worktree is clean. Do not commit a planted probe or use a broad reset/clean to hide other changes.

## Leg 4: required repository validation

On the clean exact-head checkout after probe cleanup:

```sh
./gradlew check -x test --console=plain
git diff --exit-code
git status --short
```

Require success and a clean tree. This is the repository-required non-test validation, including the relevant formatting and RAT checks. Do not add exclusions for an unexplained failure. A dependency/environment refusal is recorded separately; it does not pass the gate. Do not expand into unrelated Solr runtime tests or the separate UI build without a concrete failure requiring it.

## Finish and repair rules

If a test executes and fails, investigate and repair rather than repeatedly relaunching unchanged proof. An environment failure before tests run is handled under the board's one-relaunch rule. Any implementation or formatting change needs a committed, pushed replacement head, a superseding exact-head gate record, and fresh proof; the receipt must identify the head actually covered.

On success, publish the receipt above with: final full SHA/tree and remote-tip match; platform/tool versions; commands and return codes; the 12-test summary; per-invocation log paths; original-script assertion failures; actual Solr probe hashes and task ordering; tidy/check results; and final clean status. Preserve raw logs as accessible gate evidence and include a short PR-facing description draft in the receipt's last section. Add a completion report naming this assignment and its claim. The original assignment/claim remain immutable.

No opening or editing a public PR, posting a GitHub/JIRA comment or review, writing to apache/solr, or changing Jaykay12's branch is authorized. This gate evaluates a separate fork follow-up for Nick to review.
