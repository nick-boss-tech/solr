# Gate: PR 5225 BATS follow-up submission candidate, round 1

Status: OPEN. This gate is the authoritative gate definition for the intended submission candidate confirmed by Nick Shanin on 2026-10-25. It supersedes the old gate's branch target for this candidate only. Do not retarget or edit `gates/pr-5225-allowpaths-denial-round-1.md`; its evidence remains specific to its original candidate.

- Tags: gate, BATS, packaging. Intended host: any capable Linux host with JDK 21, JDK 25, Gradle and BATS; no host has claimed this gate yet.
- Fork branch: `nick-boss-tech/solr:solr-5225-bats-followup`.
- Exact implementation head: `35378004f1797c5b9ae3ba68d2503e4edb3840e8`.
- Parent: `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`.
- Base: Apache Solr main after PR #5225 merged, `07fdebdda54a24085086281da1cde701e122564d`.
- Outbound diff: one assertion-line change in `solr/packaging/test/test_security_manager.bats`.
- Focused case: the single version-adaptive test `allowPaths - backup`.
- Preflight: `tidy` passed at the exact implementation head on 2026-10-26T01:26Z; the worktree was clean afterward. Log: `/workspace/gates/logs/phone-agent-2/pr5225-followup-gate/tidy-preflight-exact-head.log`.
- Current evidence: JDK 25 Commons CLI and Picocli each passed one test at the exact head; reports and logs are listed in `reports/pr-5225-jdk25-regex-fix-final-head-follow-up.md`. This is accepted as the completed JDK 25 portion, but the JDK 21 legs and mutation controls remain outstanding at this head.
- Receipt target: `receipts/pr-5225-bats-followup-round-1.md`. Report and claim names should carry `pr-5225-bats-followup-round-1`.

## Runtime matrix

Use OpenJDK 21.0.12.1 to run Gradle. Set task-level `JAVA_HOME` and `SOLR_JAVA_HOME` to the runtime JDK, `SOLR_INCLUDE=/dev/null` so no discovered include can override them, and `SOLR_PICOCLI` explicitly for each parser.

1. JDK 21 / Commons CLI: run `:solr:packaging:integrationTests --tests test_security_manager.bats --rerun`, `SOLR_PICOCLI=false`. Expect exactly one executed test and an `access denied` response.
2. JDK 21 / Picocli: use a temporary root-build-only init script whose `testFiles` is `['test/test_security_manager.bats']`; run `:solr:packaging:integrationTestsPicocli --rerun`, `SOLR_PICOCLI=true`. Expect exactly one executed test and an `access denied` response.
3. JDK 25 / Commons CLI: run the same focused `integrationTests` command with `SOLR_PICOCLI=false`. Expect exactly one executed test, the generic `Could not backup all shards` response, and a fresh `solr.log` entry naming `<backup-dir>-other/test-fail/COLL_NAME` before `must be relative to SOLR_HOME`.
4. JDK 25 / Picocli: use the root-build-only single-file init script and run `:solr:packaging:integrationTestsPicocli --rerun`, `SOLR_PICOCLI=true`. Expect the same one-test result and path-specific log evidence.

The JDK 25 test is expected to execute, not skip: this candidate contains one version-adaptive test, not the original gate branch's separate Security Manager and explicitly-disabled cases. Preserve fresh JUnit XML, complete Solr logs, parser settings, runtime/build JDK versions, and exact commands for every leg. Do not treat environment or startup failures as passes.

## Final checks

Run `tidy` and `git diff --check` at the final exact head, then `:solr:packaging:check -x test`. Require the clean source tree to remain at the pinned SHA. This is not a request for repo-wide application tests. Record the `errorProneSkipped` result accurately if it occurs; do not claim Error Prone validation unless it actually ran.

## Discriminating controls

All mutations run only in isolated scratch trees and are never pushed.

1. Change the out-of-allowlist failure text to a distinct unrelated backup error, while leaving the allowed backup successful. The collection API must still return the generic `Could not backup all shards` wrapper, but the test must fail because the path-specific diagnostic is absent.
2. Repeat with that unrelated-error mutation and pre-seed `solr.log` with a matching path-denial line before the test. The test must still fail. This candidate assertion currently searches the whole log without a checkpoint; if stale evidence makes the test pass, report that the control failed and stop for Nick's decision. Do not change the candidate source under this gate without a new authorization and superseding record.
3. As a synthetic baseline, run the original Apache PR #5225 test in a separate scratch tree with the Security Manager explicitly disabled and the same unrelated-error mutation. Record whether its generic alternative accepts the unrelated failure. Do not infer production risk or a pre-existing failure rate.

## Completion

A GREEN verdict requires all four final-head matrix legs with exactly the expected assertions and fresh XML/logs, successful final checks, and all three controls discriminating as specified. If any control fails to discriminate, keep the gate OPEN and report the evidence; do not call the assertion proven. Publish the add-only report and `receipts/pr-5225-bats-followup-round-1.md` with the final fork SHA and test matrix. No PR opening, PR comment, Jira write, or upstream write is authorized by this gate.