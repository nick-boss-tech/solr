# Gate job: SOLR-9124 live-head gate at 07b5f39e35

Status: CLAIMED

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

- [ ] Step 0: changelog YAML parse of `changelog/unreleased/SOLR-9124-grouped-exact-stats.yml`.
- [ ] Step 1: tidy (`:solr:core:tidy`; every changed Java file on the branch is in :solr:core). If tidy changes any file (post-tidy status, with gradle/libs.versions.toml restored), the run is GATE INVALID and stops.
- [ ] Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`. A compile failure at this step is a defect in the head commit: record the exact error, file and line.
- [ ] Step 3, proof leg: scratch worktree at the merge-base `cabedd1d9680` (base production) with the branch's two test files `TestBaseStatsCache.java` and `TestDefaultStatsCache.java` at 07b5f39e35 copied in (the other three focused classes are unchanged from base and inherit from TestBaseStatsCache); run the four focused classes at the seed; count and read failures from the fresh JUnit XML. Expected, by content, stated from the code before the run: base `needsDistributedStats` never fires for these grouped queries, so grouped scores are computed with local shard stats and diverge from the control node: `TestExactStatsCache` fails its grouped-scores check (the established premise from the earlier gates), and the new within-group-order check fails for the same reason wherever it runs against base stats. Failures must be score or within-group-order divergences between control and shards in the stats cache classes. Any other failure shape (compile error, setup failure, an unrelated assertion) stops the job and follows the failure route.
- [ ] Step 4, focused head run: cleanTest first, then the four focused classes at the seed in the head worktree. Count from the fresh JUnit XML. Expected: all tests pass (5 tests at the prior head; the new commit adds checks inside existing test methods, not new test methods, so 5 is still expected). Nothing else runs on the worktree between the test run and the XML read.
- [ ] Step 5: module check `:solr:core:check -x test`.

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
