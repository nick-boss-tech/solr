# Gate job: SOLR-8939 tidy-fix gate (fresh full gate at the tidy-fix head)

Status: CLAIMED (vm1, claims/pool-vm1-gate-8939-tidy-fix.md, 2026-10-16).

- Branch: solr-8939-submit. Starting head: `0a97b52c7d97341d45817205ac1057669a756d50` (fork tip, confirmed by ls-remote on 2026-10-16). The tidy fix below adds one commit on top; this job gates the resulting head.
- Prior records: gates/SOLR-8939-live-head-gate.md ran at 0a97b52c7d and stopped GATE INVALID at step 1: `:solr:core:tidy` inserts exactly one blank line in `solr/core/src/java/org/apache/solr/handler/component/ResponseBuilder.java`, after line 208 (`public Map<Object, ShardDoc> resultIds;`), before the comment block preceding the new `pageShardDocs` field. Steps 2 to 5 never ran; no tests were executed at that head. The GATE GREEN at a855a2d896 stands for that tree only.
- The fix: one formatting-only commit on solr-8939-submit that runs `:solr:core:tidy` and commits exactly that blank line (gradle/libs.versions.toml restored if touched). No other file may change; if tidy changes anything else, stop and report.
- Intended host: vm1 (main side brief). Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.handler.component.QueryComponentIdFormatTest` (4 tests at the head) and `org.apache.solr.search.grouping.distributed.requestfactory.StoredFieldsShardRequestFactoryTest` (2 tests at the head). Seed: `8939C0FFEE8939`. Merge-base with upstream main: `97d973814336101e12475558d7419321c743de79`.
- Runner and logs on vm1: ~/workspace/tools/g8939-tidy-fix-gate.sh, ~/workspace/tools/g8939-tidy-fix-gate.log; head worktree ~/workspace/solr-worktrees/solr-8939-fix; proof worktree ~/workspace/solr-worktrees/solr-8939-proof (prepared by the live-head gate lane: merge-base production plus the branch's StoredFieldsShardRequestFactoryTest copied in, blob-verified against the branch, and the scratch same-second probe class, never committed); head XML archived at ~/workspace/tools/g8939-tidy-fix-head-xml/, proof XML at ~/workspace/tools/g8939-tidy-fix-proof-xml/.

## What to run

Use the host's standard gate runner (~/workspace/tools/solr-gradle.sh behind the blocking flock on ~/workspace/tools/.solr-test-queue.lock). Do not run the build tool directly. Check the head worktree out at the tidy-fix head first.

- [ ] Step 0: changelog YAML parse of `changelog/unreleased/SOLR-8939-date-unique-key-millis.yml`.
- [ ] Step 1: tidy (`:solr:core:tidy`). The tree must be unchanged after tidy; if tidy changes any file, the run is GATE INVALID and stops.
- [ ] Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- [ ] Step 3, proof legs, in the proof worktree at the merge-base `97d973814336` (base production), judged by content from fresh JUnit XML:
  - Leg A (established premise): the branch's `StoredFieldsShardRequestFactoryTest.java` copied in; run that class at the seed. Expected: 2 tests, exactly 1 failure, `testDateUniqueKeyIsSentAsIsoWithMilliseconds`, because base production sends the date key with `Date.toString()`, which carries no milliseconds. The string-key test passes on base.
  - Leg B (new-commit discriminator): `QueryComponentIdFormatTest` at the head does not compile against base production (its first three tests call `QueryComponent.idToString`, which the branch adds), so those tests are INCONCLUSIVE-by-compile against base. The new `testSameSecondDateKeysAllReachThePage` uses only base APIs and runs on base production as the scratch probe class `QueryComponentSameSecondProbeTest` holding only that test (never committed). Expected: exactly 1 failure, the response-docs size assertion, expected 3 but was 2, because on base `setResultIdsAndResponseDocs` sizes the page from the `resultIds` map keyed by `id.toString()` and the two same-second date keys collapse into one entry. Any other failure shape stops the job and follows the failure route.
- [ ] Step 4, focused head run: cleanTest first, then both focused classes at the seed in the head worktree. Count from the fresh JUnit XML. Expected: QueryComponentIdFormatTest 4 of 4, StoredFieldsShardRequestFactoryTest 2 of 2. Nothing else runs on the worktree between the test run and the XML read.
- [ ] Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure route.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tidy fix scope | decided: exactly the one blank line in ResponseBuilder.java that the live-head gate recorded | if tidy changes any other file, stop and report instead of committing | main side brief, 2026-10-16 |
| Proof expectation | decided: leg A fails by content (2 run, exactly 1 failure, the date rendering mismatch); leg B's idToString tests are INCONCLUSIVE-by-compile and its same-second probe fails by content (expected 3, was 2) | none | gates/SOLR-8939-live-head-gate.md |
| Failure route | default: record the failing test, seed and reproduce line here and in the claim, mark the job accordingly, and stop | if tests executed and failed, no relaunch of the unchanged gate | WORKFLOW.md |

## On green

- Refresh receipts/SOLR-8939.md with a new top section naming the tidy-fix head (earlier gates kept as history).
- Mark this job DONE and the claim DONE in the same push sequence.
- Append one line to the takeover log on the main host.
- Flag any PR body or draft sentences the new head and this gate falsify for the main side; do not edit public text from this lane.

## Checklist

- [ ] fork tip confirmed by ls-remote
- [ ] tidy fix commit is exactly the recorded blank line
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean
- [ ] step 2 Error Prone compile
- [ ] step 3 leg A, expected failure by content
- [ ] step 3 leg B probe, expected failure by content
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE
