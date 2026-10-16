# Gate job: SOLR-8051 (live-head gate at d4fe2e6f6620)

Status: DONE

- Branch: solr-8051-submit. Head: `d4fe2e6f6620e918d29eeb63fe481cf28ec9645f` (fork tip, confirmed by ls-remote on 2026-10-16). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `65d0c968d8a41f949cf7564df896827ec112c8dc` (tree-identical to the gated head e50a2aa3437c; see receipts/SOLR-8051.md). That evidence does not cover the one commit on top of it, d4fe2e6f66, which this job gates. Merge-base with upstream/main: `cabedd1d968059215188f4e7563fb303241899ed`.
- Intended host: vm1 (Nick directed this run on 2026-10-15 evening).
- Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.search.stats.ExactStatsCacheMergeTest` and `org.apache.solr.handler.component.ExactStatsCacheNoAnswerTest` (packages copied from the files on the branch). Seed: `8051C0FFEE8051`.
- Diff scope of d4fe2e6f66 against 65d0c968d8a4: 4 files, +96/-5. `changelog/unreleased/SOLR-8051-exact-stats-missing-shard.yml` (title reworded), `solr/core/src/java/org/apache/solr/search/stats/ExactStatsCache.java` (comment and log-guard wording inside the null-body branch only), new test `ExactStatsCacheNoAnswerTest.java`, and `ExactStatsCacheMergeTest.java` extended (an answered shard added and the merged terms asserted). Anything else in the tree is out of scope.
- Why: the head commit was authored from the Windows side on 2026-10-15 evening and is ungated; the receipt's GATE GREEN covers only the tree at 65d0c968d8a4. Competition check: at pr-prepare tips b270410ac7 and 6fd7676539 (2026-10-16), no file in claims/, assignments/ or gates/ names d4fe2e6f66 or ExactStatsCacheNoAnswerTest, and no claim or named-host assignment covers gating this head. This claim is the first.
- Runner and logs on vm1: ~/workspace/tools/g8051-gate.sh, ~/workspace/tools/g8051-gate.log (head worktree ~/workspace/solr-worktrees/solr-8051-gate); proof leg ~/workspace/tools/g8051-proof.sh, ~/workspace/tools/g8051-proof.log (proof worktree ~/workspace/solr-worktrees/solr-8051-proof).

## What to run

Use the host's standard gate runner (~/workspace/tools/solr-gradle.sh behind the blocking flock on ~/workspace/tools/.solr-test-queue.lock). Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-8051-exact-stats-missing-shard.yml`.
- Step 1: tidy (`:solr:core:tidy`; every changed Java file on the branch is in :solr:core). If tidy changes any file (post-tidy status, with gradle/libs.versions.toml restored), the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: scratch worktree at the merge-base `cabedd1d9680` (base production) with the branch's two test files at d4fe2e6f6620 copied in; run both focused classes at the seed; count and read failures from the fresh JUnit XML. Expected, by content:
  - `ExactStatsCacheMergeTest` FAILS with a NullPointerException in `ExactStatsCache.doMergeToGlobalStats`: base production looks up `ShardParams.SHARD_NAME` on `res.getResponse()` for a shard response whose body was never set. That failure is the premise. Any other failure shape stops the job and follows the failure route.
  - `ExactStatsCacheNoAnswerTest` is expected to PASS on base production as well, and that expectation is stated from the code before the run: the tolerant exception skip it exercises (`HttpShardHandler.getShardsTolerantAsBool(req)` with `r.getException() != null`, at the top of `doMergeToGlobalStats`) is identical at the merge-base and at the head, and the branch's production change is only the null-body skip below it. This leg is a regression pin for the tolerant path, not a discriminator; the premise for the production change is carried by the MergeTest leg. If it fails on base, record the failure text and judge by content before proceeding.
- Step 4, focused head run: cleanTest first, then both focused classes at the seed in the head worktree. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Proof expectation for ExactStatsCacheNoAnswerTest | decided: it passes on base production too (tolerant skip unchanged at base); a pin, not a discriminator; the proof verdict rests on the MergeTest leg | none | main side brief, 2026-10-15 |
| Branch push | decided: none. The head is already the fork tip; do not edit the branch | none | main side brief, 2026-10-15 |
| Failure route | default: record the failing test, seed and reproduce line here and in the claim, mark the job accordingly, and stop | if tests executed and failed, no relaunch of the unchanged gate | WORKFLOW.md |

No row may leave the choice to the claimant.

## On green

1. Write `receipts/SOLR-8051.md` with a new top section: head d4fe2e6f6620, gate date and host, log paths, step results, the proof-leg result (test name and failure text), and the focused counts from the fresh XML. Keep the earlier sections as history.
2. Append one line to the takeover log (main host file files/remaining-branches-takeover-log.md).
3. Mark this job DONE and the claim DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog parse
- [x] step 1 tidy clean
- [x] step 2 Error Prone compile
- [x] step 3 proof leg, expected failure by content
- [x] step 4 focused head run, counted from fresh XML
- [x] step 5 module check
- [x] receipt written
- [x] job DONE, claim DONE

## Outcome (vm1, 2026-10-16): DONE, GATE GREEN at d4fe2e6f6620

- Log: ~/workspace/tools/g8051-gate.log on vm1 (GATE RESULT: GREEN, GATE RUNNER DONE). JUnit XML archived at ~/workspace/tools/g8051-head-xml/ and ~/workspace/tools/g8051-proof-xml/. Seed 8051C0FFEE8051.
- Steps: changelog YAML parse OK rc 0; tidy rc 0, post-tidy tree unchanged; Error Prone compile rc 0; focused head runs from fresh JUnit XML: ExactStatsCacheMergeTest 1 of 1, ExactStatsCacheNoAnswerTest 1 of 1, 0 failures, 0 errors, 0 skipped; module check :solr:core:check -x test rc 0; post-gate tree clean.
- Proof leg at merge-base cabedd1d9680 production with the branch test files: ExactStatsCacheMergeTest failed with the premise NullPointerException (SolrResponse.getException at SolrResponse.java:49, from ExactStatsCache.doMergeToGlobalStats at ExactStatsCache.java:117, on a response whose body is null). ExactStatsCacheNoAnswerTest passed on base production too (1 of 1), matching the expectation recorded in this job before the run: a regression pin for the tolerant path, not a discriminator.
- Receipt: receipts/SOLR-8051.md, new top section at d4fe2e6f6620. No branch push was needed; the head was already the fork tip and the branch was not edited.

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
