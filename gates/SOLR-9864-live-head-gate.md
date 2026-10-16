# Gate job: SOLR-9864 live-head gate at 0fdc9e97b2

Status: CLAIMED

- Branch: solr-9864-submit. Head: `0fdc9e97b2b0930641f1d4174e6053c69895e4d3` (fork tip, confirmed by ls-remote on 2026-10-16). If the tip has moved at run time, stop and record the new tip here.
- Prior gated head: `176ed67491b50fef0669d629341ed05db4b2e700` (full gate GREEN 2026-10-14, receipts/SOLR-9864.md). That evidence does not cover the one commit on top of it, 0fdc9e97b2, which this job gates. Merge-base with upstream main: `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`.
- Intended host: vm1 (main side directed this run 2026-10-15 evening).
- Capability tags: gate.
- Module: `:solr:solrj`. Focused classes: `org.apache.solr.client.solrj.request.SolrQueryTest`, `org.apache.solr.client.solrj.request.QueryRequestTest`, `org.apache.solr.client.solrj.request.TestUpdateRequest` (packages copied from the files on the branch). Seed: `9864C0FFEE9864`.
- Diff scope of 0fdc9e97b2 against 176ed67491b5: 1 file, test only. `solr/solrj/src/test/org/apache/solr/client/solrj/request/SolrQueryTest.java` gains two test methods, `testGetCopyWithoutSortClauses` and `testGetCopyRemoveReplaceAndClearSortClauses` (32 insertions, no deletions), taking the class from 17 to 19 test methods. Production is byte-identical to the previously gated head. Against the merge-base the branch diff is 3 files: the changelog fragment, `SolrQuery.java` (+3 lines in `getCopy()`: the sortClauses list is copied into a new ArrayList), and `SolrQueryTest.java` (+48 lines total). Anything else in the tree is out of scope.
- Why: the head commit is ungated; the receipt's GATE GREEN covers only the tree at 176ed67491b5. Competition check: at pr-prepare tip f3e2a6920f (2026-10-16), no file in claims/, assignments/, gates/ or receipts/ names 0fdc9e97b2; the existing 9864 gate jobs (gates/SOLR-9864-review-javadoc-placement.md, gates/SOLR-9864-review-history.md) and the claim claims/pool-vm1-gate-9864-review-javadoc-placement.md are all DONE and apply to earlier heads only. No claim or named-host assignment covers gating this head. This claim is the first.
- Standing ruling (Nick, 2026-10-14): this branch's history is not rewritten for commit-message defects; the head is gated as it stands.
- Runner and logs on vm1: ~/workspace/tools/g9864-live-gate.sh, ~/workspace/tools/g9864-live-gate.log (one runner for all steps; head worktree ~/workspace/solr-worktrees/solr-9864-gate; proof worktree ~/workspace/solr-worktrees/solr-9864-proof; proof XML archived at ~/workspace/tools/g9864-live-proof-xml/, head XML at ~/workspace/tools/g9864-live-head-xml/).

## What to run

Use the host's standard gate runner (~/workspace/tools/solr-gradle.sh behind the blocking flock on ~/workspace/tools/.solr-test-queue.lock). Do not run the build tool directly. Check the worktree out at the head first.

- [ ] Step 0: changelog YAML parse of `changelog/unreleased/SOLR-9864-solrquery-getcopy-sorts.yml`.
- [ ] Step 1: tidy (`:solr:solrj:tidy`). If tidy changes any file (post-tidy status, with gradle/libs.versions.toml restored), the run is GATE INVALID and stops.
- [ ] Step 2: Error Prone compile: `:solr:solrj:compileJava :solr:solrj:compileTestJava -Pvalidation.errorprone=true`.
- [ ] Step 3, proof leg: scratch worktree at the merge-base `14c7aac0d151` (base production) with the branch's test file `SolrQueryTest.java` at 0fdc9e97b2 copied in; run the focused class at the seed; count and read failures from the fresh JUnit XML. Expected, by content, stated from the code before the run: base `getCopy()` never copies `sortClauses`, so a copy starts with no sorts. `testGetCopyKeepsSortClauses` fails (the copy had none; the established premise) and the new `testGetCopyRemoveReplaceAndClearSortClauses` fails at its first content assertion (`copy.getSortField()` is not "id desc" after removing "price" from a copy that never received the clauses). The new `testGetCopyWithoutSortClauses` passes on base: the original has no sorts, and adding a sort to the copy works without the production fix, so it pins the no-sort case rather than discriminating. Expected shape: 19 tests run, exactly 2 failures, the two named above. Any other failure shape stops the job and follows the failure route.
- [ ] Step 4, focused head run: cleanTest first, then the three focused classes at the seed in the head worktree. Count from the fresh JUnit XML. Expected: SolrQueryTest 19 of 19, QueryRequestTest 5 of 5, TestUpdateRequest 5 of 5. Nothing else runs on the worktree between the test run and the XML read.
- [ ] Step 5: module check `:solr:solrj:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure route.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Proof expectation | decided: on base production, SolrQueryTest runs 19 with exactly 2 failures (testGetCopyKeepsSortClauses and testGetCopyRemoveReplaceAndClearSortClauses), judged by content; testGetCopyWithoutSortClauses passes on base as a pin | none | this job, from the receipt and the head diff |
| Branch push | decided: none. The head is already the fork tip; do not edit the branch | none | main side brief, 2026-10-15 |
| Compile failure in the head commit | decided: a compile failure is a defect in the head commit; record the exact file, line and error and stop | none | main side brief, 2026-10-15 |
| Failure route | default: record the failing test, seed and reproduce line here and in the claim, mark the job accordingly, and stop | if tests executed and failed, no relaunch of the unchanged gate | WORKFLOW.md |

## On green

- Refresh receipts/SOLR-9864.md with a new top section at 0fdc9e97b2 (earlier sections stay as history).
- Mark this job DONE and the claim DONE in the same push sequence.
- Append one line to the takeover log on the main host.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, expected failure by content
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
