# Gate job: SOLR-9124 live-head gate at 07b5f39e35

Status: DONE. GATE FAILED at head 07b5f39e35 on 2026-10-16 (step 4, tests executed and failed at the head; not relaunched). Escalated to the main agent. See the Result section.

- Branch: solr-9124-submit. Head: `07b5f39e35f32fad95017140ec40840c7aed30a4` (fork tip, confirmed by ls-remote on 2026-10-16). If the tip has moved at run time, stop and record the new tip here.
- Prior gated head: `17a279d4dce1a8a3e41669d248b047489abd3cd6` (full gate GREEN, receipts/SOLR-9124.md), followed by a title-only commit `64b8256cde72`. The one commit on top, 07b5f39e35, is ungated; this job gates it. Merge-base with upstream main: `cabedd1d968059215188f4e7563fb303241899ed`.
- Intended host: vm1 (directed by the main side 2026-10-15 evening, after the 2026-10-15 23:19 MDT queue pass registered the moved tip).
- Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.search.stats.TestDefaultStatsCache`, `org.apache.solr.search.stats.TestExactStatsCache`, `org.apache.solr.search.stats.TestLRUStatsCache`, `org.apache.solr.search.stats.TestExactSharedStatsCache` (packages copied from the files on the branch). Seed: `9124C0FFEE9124`.
- Diff scope of 07b5f39e35 against 64b8256cde72: 3 files. `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java`: `needsDistributedStats` also returns true when grouping is on and the within-group sort spec includes score, so a grouped query whose main sort carries no score still sends global stats. `solr/core/src/test/org/apache/solr/search/stats/TestBaseStatsCache.java`: `checkGroupedScores` now also calls a new `checkWithinGroupOrder`, which runs a grouped query with main sort `id asc` and `group.sort` of `score desc,id asc` and compares the top doc per group between the control node and the shards. `solr/core/src/test/org/apache/solr/search/stats/TestDefaultStatsCache.java`: the indexed docs gain a `grp_s` field so the grouping works. Anything else in the tree is out of scope.
- Why: the head commit changes production behavior (when distributed stats are requested) and extends the proof test; the receipt's GATE GREEN covers only the tree at 17a279d4dce1 plus a title-only commit. Competition check: at pr-prepare tip f3e2a6920f (2026-10-16), no file in claims/, assignments/, gates/ or receipts/ names 07b5f39e35; the only 9124 gate file, gates/SOLR-9124-changelog-title.md, is DONE and covers the title-only commit; the claims mentioning 9124 are closed census and answers rounds claiming no gate. No claim or named-host assignment covers gating this head. This claim is the first.
- Runner and logs on vm1: ~/workspace/tools/g9124-live-gate.sh, ~/workspace/tools/g9124-live-gate.log (one runner for all steps; head worktree ~/workspace/solr-worktrees/solr-9124-gate; proof worktree ~/workspace/solr-worktrees/solr-9124-proof; proof XML archived at ~/workspace/tools/g9124-live-proof-xml/, head XML at ~/workspace/tools/g9124-live-head-xml/).

## What to run

Use the host's standard gate runner (~/workspace/tools/solr-gradle.sh behind the blocking flock on ~/workspace/tools/.solr-test-queue.lock). Do not run the build tool directly. Check the worktree out at the head first.

- [x] Step 0: changelog YAML parse of `changelog/unreleased/SOLR-9124-grouped-exact-stats.yml`. rc 0.
- [x] Step 1: tidy (`:solr:core:tidy`; every changed Java file on the branch is in :solr:core). rc 0, tree unchanged after tidy (no GATE INVALID).
- [x] Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`. rc 0. The head commit compiles clean; the unverified-author risk did not materialize as a compile defect.
- [x] Step 3, proof leg: scratch worktree at the merge-base `cabedd1d9680` (base production) with the branch's two test files `TestBaseStatsCache.java` and `TestDefaultStatsCache.java` at 07b5f39e35 copied in (the other three focused classes are unchanged from base and inherit from TestBaseStatsCache); the four focused classes ran at the seed; counts and failures read from the fresh JUnit XML (archived at ~/workspace/tools/g9124-live-proof-xml/ on vm1). Result: 5 tests, 3 failures. By content: `TestExactStatsCache` failed at `checkGroupedScores` (TestBaseStatsCache.java:96) with grouped scores diverging from the control node (expected {3=1.1446949, ...}, was {3=1.0757567, ...}): the established premise failure, so the premise still discriminates. `TestLRUStatsCache` and `TestExactSharedStatsCache` passed that scores assertion on base (as at the earlier gates) and then failed inside the new `checkWithinGroupOrder` with the same server error as at the head (see step 4): those two are the head commit's test defect showing on base, not premise evidence.
- [x] Step 4, focused head run: cleanTest first, then the four focused classes at the seed in the head worktree. Counts from the fresh JUnit XML (archived at ~/workspace/tools/g9124-live-head-xml/ on vm1). Result: FAILED. 5 tests, 3 failures: `TestExactStatsCache` 1 of 2, `TestLRUStatsCache` 1 of 1, `TestExactSharedStatsCache` 1 of 1; `TestDefaultStatsCache` 1 of 1 passed in full. All three failures are the identical server error from the new check: `org.apache.solr.common.SolrException: can not use FieldCache on multivalued field: grp_s`, thrown on the shards query in `checkWithinGroupOrder` (TestBaseStatsCache.java:111), called from `checkGroupedScores` (line 97). In `TestExactStatsCache` the grouped-scores assertion passed at the head before the new check ran, so the production change is not implicated by this failure.
- [ ] Step 5: module check `:solr:core:check -x test`. Not run: the gate stopped at step 4.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure route.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Proof expectation | decided: on base production the stats cache classes fail by content (grouped score divergence, and the within-group-order divergence where it runs); the premise verdict rests on those failures | none | this job, from the receipt and the head diff |
| Branch push | decided: none. The head is already the fork tip; do not edit the branch | none | main side brief, 2026-10-15 |
| Failure route | default: record the failing test, seed and reproduce line here and in the claim, mark the job accordingly, and stop | if tests executed and failed, no relaunch of the unchanged gate | WORKFLOW.md |

## On green

- Refresh receipts/SOLR-9124.md with a new top section at 07b5f39e35 (earlier sections stay as history).
- Mark this job DONE and the claim DONE in the same push sequence.
- Append one line to the takeover log on the main host.

## Result

DONE, GATE FAILED at 07b5f39e35f3 (vm1, 2026-10-16; log ~/workspace/tools/g9124-live-gate.log, flock acquired 2026-10-16T05:30:14Z, GATE RUNNER DONE at the step 4 failure). Steps 0 to 2 green; step 3 proof leg ran (premise confirmed for `TestExactStatsCache` by content); step 4 executed tests at the head and failed; step 5 not run. The gate is not relaunched.

Classification: **test defect in the head commit 07b5f39e35**, not a production failure and not an environment refusal. The commit's new `checkWithinGroupOrder` groups on `grp_s`, a field the commit adds to the indexed docs in `TestDefaultStatsCache`. In the distributed test harness the `*_s` dynamic field is multivalued in some schema variants, and grouping on a multivalued field is rejected server-side ("can not use FieldCache on multivalued field: grp_s"). Which classes fail is schema-variant dependent: `TestDefaultStatsCache` drew a single-valued variant and passed in full; the other three classes drew the multivalued variant and failed at the same line. This is the same trap the first gate on this branch recorded with `shard_i` (see receipts/SOLR-9124.md history): the grouped checks must group on a field that is single-valued in every schema variant, the way `checkGroupedScores` groups on `id`. The fix belongs in the test code (choose such a field for the within-group check, or index the group value into one); the production change in the commit is not implicated: at the head, the grouped-scores assertion that failed on base passes, and the failure sits entirely in the new check's query.

Next step (main side): a fix round for the test defect, then a fresh gate at the new head. Any public text describing the branch as verified at 07b5f39e35 is falsified by this run; the last green gate remains 17a279d4dce1 plus the title-only commit.
