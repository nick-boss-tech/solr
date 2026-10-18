# Skip record: SOLR-11008

- Ticket: SOLR-11008, TestMetricsHandler.testPropertyFilter failure. JIRA status at check: Open, checked 2026-10-18.
- Verdict: close-fixed.
- Draft branch: solr-11008-draft-close.
- Pinned head: fork origin/main 498a09aea9.
- Checked by: vm5, 2026-10-18T21:32:17Z.

## Ticket reading

The ticket is Open with no resolution at check. The summary is above; the report row is the evidence below.

## Code reading

Checked on main. Commit 7c4584bd4f changed `MetricsHandlerTest` to use `solrconfig-minimal.xml` and to wait until the core is up before the test starts (`solr/core/src/test/org/apache/solr/handler/admin/MetricsHandlerTest.java`, line 40). The reporter said in 2018 that the test no longer fails, and the change was committed on that basis.

Proposed resolution: Fixed

## History

- `7c4584bd4f`: ancestor of origin/main.

## Reproduction attempts

Not attempted. This is a fix-commit verdict: the commit that fixes the reported problem is on main, and its changed code is still present at the pinned head. A reproduction is not required for close-fixed.

## Gap analysis

Fix commit named in the draft; the draft text is the evidence.

## Verdict justification

close-fixed: the fixing commit (or, for a removed feature, the removal commit and an empty search) is named above, each cited commit is an ancestor of 498a09aea9, and the changed code or the empty search is recorded at the pinned head. Rubric minimum for close-fixed is met.
