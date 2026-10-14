# SOLR-8088 review gate: changelog scope

- Branch: solr-8088-submit (fork). Live PR: apache/solr #5144, draft.
- Applies to head: 567efa9b78c32df3454ed431a40a04c06ed09d46
- Status: OPEN. Not run. Filed by the slice 4 review; no push and no commit made.

## Finding answered

The changelog title says grouping on "a multiValued field" now fails with a 400. The code skips numeric fields. `checkGroupable` tests `field.getType().getNumberType() == null && field.multiValued()` (solr/core/src/java/org/apache/solr/search/grouping/distributed/command/SearchGroupsFieldCommand.java, line 84 at the head). The PR body's Limits already says numeric multiValued fields are not checked. The live PR title and the two bold summaries were narrowed on 2026-10-14 to "multiValued non-numeric field". The changelog must say the same thing.

## Proposed patch

File: changelog/unreleased/SOLR-8088-grouping-multivalued.yml

Title line, from:

    title: Distributed grouping on a multiValued field now fails with a clear 400 instead of an IllegalStateException from Lucene docValues.

to:

    title: Distributed grouping on a multiValued non-numeric field now fails with a clear 400 instead of an IllegalStateException from Lucene docValues.

No other line changes. The title stays a plain scalar with no colon followed by a space.

## Gate shape

1. Changelog YAML parse of the fragment, read closely (no YAML library on the Windows host).
2. Tidy, with the versions-file restore.
3. Error Prone compile: :solr:core compileJava and compileTestJava with -Pvalidation.errorprone=true.
4. Pre-fix proof legs: on base production with the branch test in place, SearchGroupsFieldCommandTest.testMultiValuedFieldIsRejected fails. The receipt recorded this at the gated head; re-run it because the head moves.
5. Focused run: SearchGroupsFieldCommandTest, counts from fresh JUnit XML (expect 2 of 2).
6. Module check: :solr:core.

## After the gate passes

Move the live head only through the gate lane. Then update the PR body's Changelog link and its Proof head line to the new head, and re-review PR #5144.

## Completion (vm1, 2026-10-14)

- Status: DONE. GATE GREEN at new head c36cfc094af45ef3209f2eefa0aab7621407fae0, pushed to solr-8088-submit as a fast-forward from 567efa9b78c3 and verified by ls-remote.
- Steps: changelog YAML parse rc=0; tidy rc=0, tree clean after the versions-file restore; Error Prone compile rc=0; focused SearchGroupsFieldCommandTest 2 of 2 from fresh JUnit XML (seed 8088C0FFEE8088); module check rc=0. Proof leg: base cabedd1d968 with the branch test in place runs 2 tests with exactly 1 failure, testMultiValuedFieldIsRejected, by content from fresh XML (the leg's first attempt never executed tests because of a runner copy defect, the new test directory not existing on the base; re-run once with the directory created).
- PR #5144 body: changelog link and Proof head line updated to the new head and re-read. Receipt: receipts/SOLR-8088.md (new top section). Claim: claims/pool-vm1-gate-8088-review-changelog-scope.md, DONE.
