# Assignment: SOLR-11475 proof fix, round 1

Capability tags: [implementation, gate]. Intended host: either Linux host (vm1 or vm2); Windows never claims build work. Posted by the main side, 2026-10-15, from the update-processing audit at `audits/update-processing/SOLR-11475.md` (2026-10-15).

Claim first: before starting, file a claim under `claims/` naming this assignment (for example `claims/pool-solr-11475-proof-fix-round-1.md`), per WORKFLOW.md. Heartbeat and staleness follow the pool rules.

## Grounding

- Branch: `solr-11475-submit`, pinned at `229947201fd797e57e2d3db1ff2c6552097c9734` (fork tip, verified by `git ls-remote` at filing, 2026-10-15). Merge-base: `14c7aac0d15`. Module: `:solr:core`. The branch backs the owner's open PR at that head. If the tip has moved at claim time, stop and record the new tip here before building anything; do not run at any other head without noting it.
- Read `audits/update-processing/SOLR-11475.md` in full before claiming, and the branch's TESTING records (the TESTING.md entries for this ticket on pr-prepare and any SOLR-11475-TESTING.md the branch carries) before editing.
- Gate state (`receipts/SOLR-11475.md`): gated head `0de48e492fd4` (2026-10-08) is tree-identical to the pinned live head, so the focused results stand for this tree: PeerSyncTest 1/1, PeerSyncWithLeaderTest 1/1, PeerSyncWithBufferUpdatesTest 1/1, PeerSyncWithIndexFingerprintCachingTest 1/1, PeerSyncWithLeaderAndIndexFingerprintCachingTest 1/1, module check rc=0.
- The defect this assignment fixes is in the proof, not the production change. The pre-fix proof is recorded INCONCLUSIVE: on the pre-fix production the staged test never finishes. The old loop in `PeerSync.java` at the merge-base (lines 805-837) never moves past a version that appears on both sides with opposite signs and appends the same range string on every pass until the forked test JVM dies with OutOfMemoryError. A run that ends in OutOfMemoryError is neither a pass nor an assertion failure, so the branch has no clean fail-before proof (audit finding 2). An OutOfMemoryError in the forked test JVM can also take down other tests in the same JVM, so that base run is not trusted for anything else in it.
- Upstream CI context, recorded so nobody chases it: the upstream `Solr Tests via Crave` job failed at the live head `229947201fd7` with runner-side messages (`Error: The destination pipeline/prs/5082 does not exist` and repeated `java.util.ServiceConfigurationError: Locale provider adapter "CLDR" cannot be instantiated`); the excerpt shows no PeerSync assertion. The audit treats that failure as no evidence about the code. This assignment does not touch upstream CI and does not re-run it; the proof below is local.

## Scope

1. Diagnose why the fail-before run exhausts memory, and record the diagnosis with its evidence in the claim file or a report on pr-prepare. Separate the three candidate causes: the staged test's shape (how it drives the looping code path), the heap settings of the run (the forked test JVM's heap against the rate the loop appends), and the reverted production's actual behavior (the merge-base loop never advancing past the opposite-sign version). The diagnosis decides the repair; a bigger heap is not a repair, because it only moves where the loop dies.
2. Make the smallest test change that gives the proof a clean assertion-shaped failure on the pre-fix production, without weakening what the proof shows about the fix. The audit's readiness item names the acceptable shapes: a bounded loop or a time limit on the test, so the staged test fails with an assertion on the pre-fix production instead of running out of memory. The repaired proof must still drive the same opposite-sign input through the same code path, must fail on the pre-fix production by its assertion (judged by the failure text in the fresh JUnit XML), and must pass at the branch head because the fix steps over the pair. A proof that ends in a timeout kill, an error, or OutOfMemoryError does not count as repaired. Test code only: this assignment does not authorize a production change. If the diagnosis shows no test-only change can produce an assertion failure on this input, stop, record that finding with the runs behind it, and do not proceed to the gate.
3. Run the full hardening gate at the resulting head (blocking flock on the test queue, the claiming host's Gradle route per its host file, `gradle.properties` copied into every new worktree, never two Gradle builds at once; results counted from fresh JUnit XML only):
   - Changelog YAML parse with a real parser on the branch's fragment (`changelog/unreleased/SOLR-11475-peersync-version-ranges-loop.yml`).
   - Tidy, with the GATE INVALID guard: after tidy, with `gradle/libs.versions.toml` restored, the tree must be unchanged; if tidy changes the test file, commit the result and re-gate on the committed tree.
   - Error Prone compile (`-Pvalidation.errorprone=true`) for main and test sources of the module.
   - The repaired fail-before proof: the final test code against the pre-fix production, failing with the designed assertion, by content.
   - Focused tests at the head, counted from fresh JUnit XML: PeerSyncTest, PeerSyncWithLeaderTest, PeerSyncWithBufferUpdatesTest, PeerSyncWithIndexFingerprintCachingTest, PeerSyncWithLeaderAndIndexFingerprintCachingTest. Confirm each class's exact package by grep on the branch before composing any run command, and compose the run in a separate step from the grep.
   - Module check: `:solr:core:check -x test`.

## The standing blocker this job does not touch

The sign-mismatch behavior call recorded in the audit remains a PR-readiness blocker. This job does not decide it, and a green gate under this job does not erase it. The draft's "A choice to check" stays exactly as it stands; do not edit the draft's account of the sign-mismatch behavior under this assignment.

## On green, on failure

- On green: push plainly to `fork solr-11475-submit` (fast-forward; the branch backs the owner's own open PR, and pushes to it carry standing authorization). Refresh `receipts/SOLR-11475.md` at the new head: the new tip, the gate date and log name, the focused counts, and the repaired proof's result replacing the INCONCLUSIVE line (the old INCONCLUSIVE record stays visible as history, scoped to the old test code). Append the outcome to the takeover log and mark the claim DONE.
- On an executed-test failure after the repair: record it (failing test, seed, reproduce line, classification if the log establishes one) in the receipt and the takeover log, mark the claim with the blocker, and stop. Do not relaunch an unchanged gate; a gate that executed tests and failed is investigated before any relaunch.
- Stop condition: either the green path above has landed (repaired proof recorded by content, branch pushed, receipt refreshed, claim DONE), or a stop under step 2 or the failure path is recorded with its evidence.
