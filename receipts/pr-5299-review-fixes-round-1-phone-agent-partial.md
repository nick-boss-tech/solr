# Receipt (partial): PR 5299 review fixes gate round 1, phone-agent

Status: **INCOMPLETE, not GREEN.** This is not the GREEN receipt at `receipts/pr-5299-review-fixes-round-1.md`; that path stays unwritten until a host executes every mandatory leg.
Job: `gates/pr-5299-review-fixes-round-1.md`. Report: `reports/pr-5299-review-fixes-round-1-phone-agent.md`. Claim: `claims/pr-5299-review-fixes-round-1-phone-agent.md`.
Candidate `f342e160417d64ed2dced989052b29a76d31996e` (tree `42e57845f4d23565944ae74b987955028465b9ba`), fork ref and live PR #5299 head read back at this SHA after the runs.

Executed and passing at this head on this host (JDK 21 only, BATS 1.8.2, Gradle 9.7.0): tidy clean; helper suite 14 of 14 with fresh JUnit XML (14 tests, 0 failures, 0 errors, 0 skipped); old-predicate comparison failing exactly the direct IP and direct DNS cases; predicate mutations failing their named cases (whole-log offset, arbitrary handshake text, wrapped-recognition removal); the original gate's six helper guard weakenings each failing their named case (connection refusal needs the full acceptance mutation; refute removal alone does not produce acceptance because the positive pattern still rejects it); report-guard fixtures 7 of 7 (passing report exit 0; no XML, zero, skipped, failed, errored, and two-case reports all rejected); workflow YAML, shell, and Python validation, with the previous-major derivation executed against source giving `solr:10-slim`; `git diff --check` clean; `:solr:packaging:check -x test` passing with Error Prone skipped (`:errorProneSkipped` in the log); root `check -x test` passing in a normal clone.

Not executed, capability-blocked on this host: all JDK 25 legs (no JDK 25 installed); the proof B Solr-starting matrix on either runtime and the proof A real TLS cases including the matched/mismatched client control (this VM runs as root and `bin/solr` refuses root); the proof D server controls (same root limit); proof C Docker legs and the report guard's real-report execution (no Docker binary or daemon). The hostname-policy paired control remains explicitly unresolved. Artifacts: `~/workspace/tools/gate-5299/`.

## PR description draft for owner review (not posted)

Replace the Proof and Limits sections of the PR #5299 body with:

## Proof

**The helper suite passes 14 of 14 at the current head, its guards are mutation-proven, and the repository checks pass; the Solr-starting matrix and the Docker execution are named as unproven in Limits.**

Verified 2026-11-05 at `f342e160417` (the review-follow-up commit over `99d4b8e68b`): the `test_bats_assertions.bats` suite passes 14 of 14 in a JDK 21 environment, including the five new hostname-predicate cases. Weakening the predicate in isolation fails the named cases: wrapped-message-only matching fails the direct IP and DNS cases, ignoring the log offset fails the earlier-diagnostics case, accepting arbitrary handshake text fails the unrelated-failures case, and removing the wrapped-diagnostic recognition fails its positive case. The six original helper guards (failed ZK listing, nested healthy status, nonzero exit, connection refusal, bare closed channel, unrelated TLS error) each fail their named case when weakened. The new rolling-upgrade CI job's report guard was executed against fixture reports: a single passing case is accepted, and missing, empty, skipped, failed, errored, and two-case reports are all rejected. Tidy, `git diff --check`, the packaging module check, and root `check -x test` all pass at this head.

## Limits

**The Solr-starting suites have not been re-run at this head, the JDK 25 legs have not run, and the Docker rolling-upgrade case has still never executed.**

The follow-up wires a dedicated CI job that builds the candidate image and requires an executed, passing rolling-upgrade case; that job has not run yet, so the rolling upgrade itself remains unproven until upstream CI or a Docker host executes it. The matched/mismatched client control for the hostname predicate is likewise unrun: the predicate is proven against the JDK producer forms and fixtures, not against a live pair of clients. The earlier verification round's matrix numbers quoted for the parent head remain historical and are not current-head proof.

🤖 _AI-generated text above_ 🤖
