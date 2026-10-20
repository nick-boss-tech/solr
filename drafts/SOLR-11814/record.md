# Skip record: SOLR-11814

- Ticket: SOLR-11814, "Solr REST test cases trips assertion error but doesn't fail the test". JIRA status read 2026-10-20T07:42:49Z (UTC): Open, resolution None, updated 2019-06-08. Linked: SOLR-8933 (Closed).
- Verdict: close-obsolete. This replaces the recorded keep-open: the assertion the ticket reports is no longer in the tree, and one of the three named test classes is renamed.
- Draft branch: solr-11814-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T07:42:49Z (UTC).

## Ticket reading

Three REST tests (TestManagedStopFilterFactory, TestManagedSynonmFilterFactory, TestRestManager) log a Jetty warning with an `AssertionError: Attempted close of response output stream` from `SolrDispatchFilter`, while the tests still pass. No comments beyond a pointer to SOLR-8933.

## Code reading (pinned head)

- The grep the verdict rests on: `git grep -l "Attempted close of response" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr`. Result: empty. The assertion message is not in the tree.
- The class the ticket names as `TestManagedSynonmFilterFactory` is not in the tree: `git grep -l "class TestManagedSynonymFilterFactory" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr` is empty. The test now lives as `solr/core/src/test/org/apache/solr/rest/schema/analysis/TestManagedSynonymGraphFilterFactory.java`.
- `TestManagedStopFilterFactory` (`solr/core/src/test/org/apache/solr/rest/schema/analysis/TestManagedStopFilterFactory.java`) and `TestRestManager` (`solr/core/src/test/org/apache/solr/rest/TestRestManager.java`) are present. Their current assertions are not read here.
- The dispatch code that the trace names was restructured: `SolrDispatchFilter` is now `SolrServlet` (SOLR-18112, commit b3b2f83fe9, 2026-03-26; ancestor of the pinned head). That commit is the nearest candidate for the removal; its diff was not read line by line.

## History

- evidence.py at the pinned head: no commits naming SOLR-11814.
- b3b2f83fe9 (SOLR-18112) is the restructuring commit above; it does not name the ticket.

## Reproduction attempts

Not run. The assertion is absent, so a run of the three tests would check the current state, not the old warning. The verdict does not depend on a run; it depends on the grep.

## Gap analysis

The trace's code is gone, and the test class in the ticket was renamed. What is left is test-quality review of the three REST tests, which is not this ticket's question.

## Verdict justification

close-obsolete meets the rubric: the code the ticket is about is removed, and the grep for its message at the pinned head under `solr/` is empty. The renamed test class is recorded here for the reader; the tests that remain do not reproduce the warning as far as this pass shows.

## Limits

This record does not confirm that each of the three tests passes without the warning on the pinned head. It does not establish the exact commit that removed the assertion.
