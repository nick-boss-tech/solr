# Gate job: SOLR-6759 (live-head gate at 680044f1d2)

Status: DONE

- Branch: solr-6759-submit. Head: `680044f1d2a6a36b4543f4d52bb552e32b142919` (fork tip, confirmed by ls-remote on 2026-10-16). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `7f8d08e34b11d01dce6ea7161cbc9096a8a7444f` (tree-identical to the gated head 42f03eb52376; see receipts/SOLR-6759.md). That evidence does not cover the one commit on top of it, 680044f1d2, which this job gates. Merge-base with upstream/main: `cabedd1d968059215188f4e7563fb303241899ed`.
- Intended host: vm1 (Nick asked the main side to run this one on 2026-10-15 evening; the main side posted this job for vm1).
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.component.TestExpandComponent` (package copied from the file on the branch). Seed: `6759C0FFEE6759`.
- Diff scope of 680044f1d2 against 7f8d08e34b11: 3 files, +43/-3. `changelog/unreleased/SOLR-6759-expand-complete-postfilter.yml` (title reworded), `solr/core/src/java/org/apache/solr/handler/component/ExpandComponent.java` (the expand search is wrapped in try/finally so the DelegatingCollector complete() call the parent commit added now also runs when the search exits early, for example when a query limit stops it), and `TestExpandComponent.java` (new test testExpandCompletesPostFilterCollectorsWhenQueryLimitExceeded). Anything else in the tree is out of scope.
- Why: the head commit is ungated; the receipt's GATE GREEN covers only the tree at 7f8d08e34b11. Competition check: at pr-prepare tip da2dbfa8cb (2026-10-16), claims/, assignments/ and gates/ were searched by filename and by content for 6759 and for 680044f1d2. The mentions are old audit, review and draft-fidelity records, all DONE or audit-only; no file names this head, no claim covers gating it, and no assignment names another host for it. This claim is the first.
- Runner and logs on vm1: ~/workspace/tools/g6759-gate.sh, ~/workspace/tools/g6759-gate.log (head worktree ~/workspace/solr-worktrees/solr-6759-gate); proof leg runs inside the same runner's step 3 against the proof worktree ~/workspace/solr-worktrees/solr-6759-proof, proof XML archived at ~/workspace/tools/g6759-proof-xml/.

## What to run

Use the host's standard gate runner (~/workspace/tools/solr-gradle.sh behind the blocking flock on ~/workspace/tools/.solr-test-queue.lock). Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-6759-expand-complete-postfilter.yml`.
- Step 1: tidy (`:solr:core:tidy`; every changed Java file on the branch is in :solr:core). If tidy changes any file (post-tidy status, with gradle/libs.versions.toml restored), the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: scratch worktree at the merge-base `cabedd1d9680` (base production) with the branch's test apparatus at 680044f1d2 copied in (`TestExpandComponent.java`, `CompleteTrackingQParserPlugin.java`, and the `completetracking` registration line in `solrconfig-collapseqparser.xml`); run the focused class at the seed; count and read failures from the fresh JUnit XML. Expected, by content, stated from the code before the run: base ExpandComponent never calls complete() on the post filter collector chain (at the merge-base the expand search at line 447 is followed directly by the partial-results check). So:
  - `testExpandCompletesPostFilterCollectors` FAILS on base: COMPLETED is one less than CREATED, the shape the receipt recorded at the earlier gate. That failure is the premise.
  - `testExpandCompletesPostFilterCollectorsWhenQueryLimitExceeded` FAILS on base: there is no completion call on any path, so the created post filter collectors are never completed (the tracking counts cannot match), and nothing in base turns an expand search stopped by a query limit into a completed partial response. A failure of this test in any shape not traceable to the missing complete() call stops the job and follows the failure route.
  - The rest of the class passes on base (the earlier gate's base run was 9 tests with exactly the 1 premise failure; the class now has one more test, the limit test above).
- Step 4, focused head run: cleanTest first, then the focused class at the seed in the head worktree. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Proof expectation for the limit test | decided: it fails on base production, by content traceable to base never calling complete() on the post filter chain; any other failure shape stops the job | none | main side brief, 2026-10-15 |
| Branch push | decided: none. The head is already the fork tip; do not edit the branch | none | main side brief, 2026-10-15 |
| Failure route | default: record the failing test, seed and reproduce line here and in the claim, mark the job accordingly, and stop | if tests executed and failed, no relaunch of the unchanged gate | WORKFLOW.md |

No row may leave the choice to the claimant.

## On green

1. Write `receipts/SOLR-6759.md` with a new top section: head, gate date and host, log path, step results, the proof-leg result (test names and failure text), and the focused counts from the fresh XML. Earlier sections stay as history.
2. Mark this job DONE and the claim DONE in the same push as the receipt.
3. Append one line to the takeover log on the main host (~/workspace/goals/apache-solr-contribution-submission/files/remaining-branches-takeover-log.md).

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

## Outcome (vm1, 2026-10-16): DONE, GATE GREEN at 680044f1d2a6

- All six steps passed at 680044f1d2a6a36b4543f4d52bb552e32b142919: changelog parse rc 0; tidy rc 0, tree unchanged; Error Prone compile rc 0; focused TestExpandComponent 10 tests, 0 failures, 0 errors, 0 skipped from fresh JUnit XML at seed 6759C0FFEE6759; module check rc 0; post-gate tree clean. Receipt: receipts/SOLR-6759.md, top section.
- Proof leg 1 (merge-base production, branch tests in place): 10 tests, exactly 1 failure, testExpandCompletesPostFilterCollectors with "expected:<2> but was:<1>" on the complete() count. The premise discriminates as expected.
- Correction to the Decisions row above: the new limit test did NOT fail on the merge-base. A supplementary leg (~/workspace/tools/g6759-proof-parent.log) also ran it against the parent head 7f8d08e34b11 production with the new test file overlaid: 10 tests, 0 failures there too. Reason, from the merge-base run log: a query limit stops the expand search through the searcher's graceful partial-results exit, which completes the collector chain itself (the limit trips inside the request, the handler logs "Query terminated", the request returns status 0 with partial results); at the parent head the search then returns and the post-search complete() call runs. The test therefore pins the limit scenario on every tree and does not discriminate the delta commit; the exception path the try/finally adds is exercised by no run in this gate and rests on the code reading. Recorded here and in the receipt so no later reader takes the test for a fail-before proof of the delta.
- Author context, relayed by the owner after the runs (2026-10-16): the commit's author states the unwrapped call (the parent commit's shape) skips complete() when a limit throws out of the search, that the new test would fail against that shape, and that on base production the new test should fail on the completion counts because base never calls complete() on that path. The two counted runs above contradict that expectation: the test passes on base and on the parent shape, because the injected limit exits the search gracefully (partial results) instead of throwing out of it. The author's shape concern stands for a search that throws; this test does not drive one. The record follows the runs, and the disagreement is recorded here so the main side can put it to the author with the logs named above.
- Flagged for the main side (not edited by this lane): the live PR body and pr-drafts/search-components/SOLR-6759.md both say the expand search makes the complete() call "without the finally block", and the Limits section says "The call sits after the search, not in a finally block. If the expand search ends with an exception, complete() is skipped. No test covers that path." The new head falsifies all of it: the call is in a finally block, it is no longer skipped when the search ends early, and the new test covers the query-limit path. Their Proof sections also still cite verification at 42f03eb52376 and the tree at 7f8d08e34b11.

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
