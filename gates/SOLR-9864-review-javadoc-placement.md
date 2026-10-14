# Gate job: SOLR-9864 review finding, javadoc placement in SolrQueryTest

- Branch: solr-9864-submit. Head: `b5826e466b9a8664adf11e0f247cff220f2f5953` (live head of PR #5150, read by the slice 6 review on 2026-10-14). The job applies to that head only.
- Filed by: the slice 6 review of the opened search components drafts (assignment `assignments/pool-search-components-opened-review-round-1.md`). The review did not push and did not commit.
- Status: OPEN.

## Finding answered

The new test `testGetCopyKeepsSortClauses` is inserted between the javadoc block that begins "Verifies that getSort() returns an immutable map," (`solr/solrj/src/test/org/apache/solr/client/solrj/request/SolrQueryTest.java`, lines 128 to 131 at the head) and the method it does not belong to, `testGetSortImmutable` (line 148). The comment now documents the wrong test, and `testGetSortImmutable` has lost its comment.

## Patch

Move the method `testGetCopyKeepsSortClauses` (head lines 132 to 146, followed by one blank line) so that it sits above the `/* ... */` block at line 128. The javadoc block then sits directly above `testGetSortImmutable` again, as on the base.

Nothing else changes: the test body, the imports, and the production change in `SolrQuery.java` stay as they are. The file keeps 17 test methods, as at the head.

## Gate shape

1. Changelog YAML parse of `changelog/unreleased/SOLR-9864-solrquery-getcopy-sorts.yml`. The fragment is unchanged; parse it anyway and record the result.
2. Tidy for `solr/solrj`, with the versions-file restore: run tidy, restore the versions file it rewrites, then check that the diff against the head holds only the moved method.
3. Error Prone compile: `-Pvalidation.errorprone=true :solr:solrj:compileTestJava`.
4. Pre-fix proof leg: overlay the moved `SolrQueryTest.java` on the base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`. `SolrQueryTest` must report exactly one failure, `testGetCopyKeepsSortClauses`, from fresh JUnit XML.
5. Focused runs at the new head, counted from fresh JUnit XML: `SolrQueryTest` 17 of 17, `QueryRequestTest` 5 of 5, `TestUpdateRequest` 5 of 5. These match the counts the PR body states.
6. Module check: `:solr:solrj:check` passes.

## On green

- Commit the move as one commit under the Nick Shanin identity, with no co-author trailer. Push fast-forward to `solr-9864-submit` only.
- Refresh `receipts/SOLR-9864.md` with the new head and the gate lines.
- Point the PR body's links at the new head. The body cites only `SolrQuery.java`, which does not change, so only the head SHA in the links moves.
- Re-review PR #5150 against the new head before any description edit.

## Completion (vm1, 2026-10-14)

- Status: DONE. GATE GREEN at 176ed67491b50fef0669d629341ed05db4b2e700, pushed as a plain fast-forward and verified by ls-remote.
- Steps: changelog fragment parses (unchanged file); tidy rc=0 with a clean tree after the versions-file restore (post-tidy diff against the head holds only the moved method); Error Prone compile rc=0; proof leg: moved test file on base 14c7aac0d151 runs 17 with exactly 1 failure, testGetCopyKeepsSortClauses, verified by content; focused at the head: SolrQueryTest 17 of 17, QueryRequestTest 5 of 5, TestUpdateRequest 5 of 5 from fresh JUnit XML (seed 9864C0FFEE9864); module check rc=0 (run as :solr:solrj:check -x test, the standing module-check form; this file wrote :solr:solrj:check).
- PR #5150 re-reviewed at the new head, then its body links re-pointed to the new head and read back. Receipt: receipts/SOLR-9864.md (new top section). Claim: claims/pool-vm1-gate-9864-review-javadoc-placement.md, DONE.
