# PR 5211 test review, Linux proof (phase G, round 1)

Claim: `claims/pr-5211-test-review-gate-round-1.md` (vm6). Job: `gates/SOLR-18569-pr5211-test-review-f00c7bde83bf.md`. Phase I: `reports/pr-5211-test-review-implementation-round-1.md`. Receipt: `receipts/SOLR-18569.md`.

## Pins

- Pinned head: `f00c7bde83bf5d82ac4607ecf086a4397eca563b` on `solr-18569-pr5211-test-review` (nick-boss-tech/solr), re-read by ls-remote after the runs.
- Upstream PR apache/solr #5211 head: `6f72e0cac151d139626299a680f90a7c9c123ceb`, unchanged, open, no PR comments or review comments at the re-read.
- Host: vm6, Linux 6.1 aarch64, JDK 21.0.12.1+1, GRADLE_USER_HOME `/workspace/gradle-home`. Runner: `pool/bin/bg-steps.sh` calling `pool/bin/sweep-run.sh`, under the host build lock.
- Seed: `18569C0FFEE01`. Focused class: `org.apache.solr.cloud.ZkControllerTest`. Sensitivity method: `testStartupWaitsForLeftoverEphemeralNodesToDisappear`.
- Logs: `/workspace/gates/logs/vm6/pr-5211-gate/` (per-leg directories `pr5211-l1`, `pr5211-l2`, `pr5211-l3`; each has `steps.tsv`, `state`, step logs, headroom logs, and a copy of the JUnit XML under `<step>.xml/`).
- Preflight: `gate-preflight.sh` at the pinned head, tidy clean (`preflight.log`, `preflight-tidy.log`).

## Mutation (legs 1 and 2 only)

In `solr/core/src/java/org/apache/solr/cloud/ZkController.java`, the body of package-private `checkForExistingEphemeralNode()` was emptied (54 lines removed, signature unchanged). The early return is the defect the test must catch. Applied in scratch worktrees `/workspace/worktrees/pool-vm6-5211-m1` (at `6f72e0cac151`) and `/workspace/worktrees/pool-vm6-5211-m2` (at `f00c7bde83bf`). Neither mutation was committed or pushed.

## Results

| Leg | Tree | Command | Result | rc | Times (UTC) |
| --- | --- | --- | --- | --- | --- |
| 1, original-test control | PR head `6f72e0cac151`, mutated | `:solr:core:cleanTest`, then `:solr:core:test --tests ...testStartupWaitsForLeftoverEphemeralNodesToDisappear -Ptests.seed=18569C0FFEE01` | PASS: 1 test, 0 failures, 0 errors | 0 | 17:33 to 17:40 |
| 2, strengthened-test negative control | `f00c7bde83bf`, mutated | same, after `cleanTest` | FAIL as expected: 1 test, 1 failure | 1 | 17:40 to 17:46 |
| 3, final head | `f00c7bde83bf`, clean | `tidy`; `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`; `:solr:core:cleanTest`; `:solr:core:test --tests org.apache.solr.cloud.ZkControllerTest -Ptests.seed=18569C0FFEE01`; `:solr:core:check -x test` | all PASS | 0, 0, 0, 0, 0 | 17:46 to 18:03 |

Details:

- Leg 1 XML: `pr5211-l1/l1-original-positive.xml/TEST-org.apache.solr.cloud.ZkControllerTest.xml`, testsuite tests=1, failures=0, errors=0, skipped=0. The original method passes with the early return in place. So the original test does not detect that defect.
- Leg 2 failure, from the XML: `java.lang.AssertionError: /node_roles/overseer/preferred/127.0.0.1:8983_solr should be gone when the check returns`, raised at `ZkControllerTest.java:252` in the lambda, called from `runWithLeftoverEphemeralNodes` (line 318) and `testStartupWaitsForLeftoverEphemeralNodesToDisappear` (line 229). This is the new assertion firing on a leftover that is still present. It is a sensitivity result, not a compile, harness, worker or timeout failure.
- Leg 3 tidy: rc 0, the tree stayed clean (no repair commit).
- Leg 3 Error Prone compile: rc 0.
- Leg 3 focused class XML: testsuite tests=13, failures=0, errors=0, skipped=2. The two skipped methods are `testVersionCompatibilityFailsStartup` and `testMinorVersionCompatibilityFailsStartup`, both `@Ignore("Would need to disable ObjectReleaseTracker")` at `ZkControllerTest.java` lines 581 and 649, outside the changed method. The changed method `testStartupWaitsForLeftoverEphemeralNodesToDisappear` passes at the final head.
- Leg 3 module check `:solr:core:check -x test`: rc 0.

## Log noise, not failures

Both the passing leg 1 and the failing leg 2 logs contain five `Locale provider adapter "CLDR" cannot be instantiated` lines from the JDK. They appear in both runs and do not affect the outcome.

## Head and repairs

- Final head: `f00c7bde83bf5d82ac4607ecf086a4397eca563b`. Tidy changed nothing, so no repair commit and no new head.
- Gate job file and claim are unchanged after posting. Results are recorded here and in the receipt, as WORKFLOW.md requires.

## Limits

- Linux aarch64 only. No Windows run was done, and the assignment does not ask for one.
- The 1 second deleter delay is still the time margin. Leg 2 shows the test fails at the assertion on this host; it does not prove the margin holds on a slower host. A coordinated handshake would need an observable point inside production code, which the assignment rules out.
- The mutation is an empty body, a stronger defect than a single early `return`. It shows the assertion placement works; it does not measure other early-return shapes.
- No public comment, review, PR, or JIRA write. The comment draft is `drafts/pr-5211-review-with-diff-round-1.md`, unposted, for Nick's review.
