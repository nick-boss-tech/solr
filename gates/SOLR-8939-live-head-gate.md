# Gate job: SOLR-8939 live-head gate at 0a97b52c7d

Status: CLAIMED by vm1 (claims/pool-vm1-gate-8939-live-head.md).

- Branch: solr-8939-submit. Head: `0a97b52c7d97341d45817205ac1057669a756d50` (fork tip, confirmed by ls-remote on 2026-10-16). If the tip has moved at run time, stop and record the new tip here.
- Prior gated head: `a855a2d8965c8eae5df171ad9b5148b7e95f7425` (full gate GREEN, receipts/SOLR-8939.md, 2026-10-06). That evidence does not cover the one commit on top of it, 0a97b52c7d, which this job gates. Merge-base with upstream main: `97d973814336101e12475558d7419321c743de79`.
- Intended host: vm1 (main side brief, 2026-10-15 evening batch).
- Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.handler.component.QueryComponentIdFormatTest` (4 tests at the head) and `org.apache.solr.search.grouping.distributed.requestfactory.StoredFieldsShardRequestFactoryTest` (2 tests at the head). Seed: `8939C0FFEE8939`.
- Diff scope of 0a97b52c7d against a855a2d896: 4 files. `QueryComponent.java`, `CombinedQueryComponent.java`, `ResponseBuilder.java`: the merge keeps the page's hits as an ordered list (`ResponseBuilder.pageShardDocs`) alongside the string-keyed `resultIds` map, and the stored-fields merge walks that list, so two hits whose date unique keys render to the same string (same second, different milliseconds) no longer collapse into one map entry and drop off the page. `QueryComponentIdFormatTest.java`: two new tests, `testDateKeyWithoutMillisecondsUsesIsoForm` and `testSameSecondDateKeysAllReachThePage`. Full branch diff against the merge-base: 7 files, including `changelog/unreleased/SOLR-8939-date-unique-key-millis.yml` (unchanged by the head commit). Anything else in the tree is out of scope.
- Why: the head commit is ungated; the receipt's GATE GREEN covers only the tree at a855a2d896. Competition check: at the claim push, no file in claims/, assignments/ or gates/ names 0a97b52c7d, no gate job for 8939 exists, and the only files naming 8939 are the receipt, the draft, and DONE review and audit rounds claiming no gate. This claim is the first.
- Runner and logs on vm1: ~/workspace/tools/g8939-live-gate.sh, ~/workspace/tools/g8939-live-gate.log (one runner for all steps; head worktree ~/workspace/solr-worktrees/solr-8939-gate; proof worktree ~/workspace/solr-worktrees/solr-8939-proof; head XML archived at ~/workspace/tools/g8939-live-head-xml/, proof XML at ~/workspace/tools/g8939-live-proof-xml/).

## What to run

Use the host's standard gate runner (~/workspace/tools/solr-gradle.sh behind the blocking flock on ~/workspace/tools/.solr-test-queue.lock). Do not run the build tool directly. Check the worktree out at the head first.

- [ ] Step 0: changelog YAML parse of `changelog/unreleased/SOLR-8939-date-unique-key-millis.yml`.
- [ ] Step 1: tidy (`:solr:core:tidy`; every changed Java file on the branch is in :solr:core). If tidy changes any file (post-tidy status, with gradle/libs.versions.toml restored), the run is GATE INVALID and stops.
- [ ] Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- [ ] Step 3, proof leg, in the proof worktree at the merge-base `97d973814336` (base production), two legs:
  - Leg A (established premise): copy the branch's `StoredFieldsShardRequestFactoryTest.java` at 0a97b52c7d in and run that class at the seed. Expected, by content, stated from the code before the run: 2 tests, exactly 1 failure, `testDateUniqueKeyIsSentAsIsoWithMilliseconds`, because base production sends the date key with `Date.toString()`, which carries no milliseconds. The string-key test passes on base.
  - Leg B (new-commit discriminator): `QueryComponentIdFormatTest` at the head does not compile against base production (its first three tests call `QueryComponent.idToString`, which the branch adds), so those tests are INCONCLUSIVE-by-compile against base, as the draft already records. The new `testSameSecondDateKeysAllReachThePage` uses only base APIs, so it runs on base production as a scratch probe class holding only that test (never committed). Expected, by content: exactly 1 failure, the response-docs size assertion, expected 3 but was 2, because on base `setResultIdsAndResponseDocs` sizes the page from the `resultIds` map keyed by `id.toString()` and the two same-second date keys (10:15:30.456 and 10:15:30.123) render to the same string and collapse into one entry. Any other failure shape stops the job and follows the failure route.
- [ ] Step 4, focused head run: cleanTest first, then both focused classes at the seed in the head worktree. Count from the fresh JUnit XML. Expected: QueryComponentIdFormatTest 4 of 4, StoredFieldsShardRequestFactoryTest 2 of 2. Nothing else runs on the worktree between the test run and the XML read.
- [ ] Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure route.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Proof expectation | decided: leg A fails by content (2 run, exactly 1 failure, the date rendering mismatch); leg B's idToString tests are INCONCLUSIVE-by-compile and its same-second probe fails by content (expected 3, was 2) | none | this job, from the receipt and the head diff |
| Branch push | decided: none. The head is already the fork tip; do not edit the branch | none | main side brief, 2026-10-15 |
| Head-commit compile failure | decided: a compile failure at step 2 is a defect in the head commit (recent Windows-side commits in this program arrived unverified); record the exact file, line and error here and in the receipt, and stop | if step 2 fails, no environment classification | main side brief, 2026-10-15 |
| Failure route | default: record the failing test, seed and reproduce line here and in the claim, mark the job accordingly, and stop | if tests executed and failed, no relaunch of the unchanged gate | WORKFLOW.md |

## On green

- Refresh receipts/SOLR-8939.md with a new top section at 0a97b52c7d (earlier gate kept as history).
- Mark this job DONE and the claim DONE in the same push sequence.
- Append one line to the takeover log on the main host.
- Flag any PR body or draft sentences the new head and this gate falsify for the main side; do not edit public text from this lane.
