# Skip record: SOLR-2913

- Ticket: SOLR-2913, Interrupted Exception when closing a SolrCore. JIRA status at check: Open, checked 2026-10-18.
- Verdict: close-fixed.
- Draft branch: solr-2913-draft-close.
- Pinned head: fork origin/main 498a09aea9.
- Checked by: vm5, 2026-10-18T21:32:17Z.

## Ticket reading

The ticket is Open with no resolution at check. The summary is above; the report row is the evidence below.

## Code reading

`solr/solrj/src/java/org/apache/solr/common/util/ExecutorUtil.java:152-156` catches InterruptedException, restores the interrupt flag and logs nothing. Change from `431ed53d46` (SOLR-7956). `SolrCore.java:1742` still calls it.

## History

- `431ed53d46`: ancestor of origin/main.

## Reproduction attempts

Not attempted. This is a fix-commit verdict: the commit that fixes the reported problem is on main, and its changed code is still present at the pinned head. A reproduction is not required for close-fixed.

## Gap analysis

The reported behavior is no longer present at the pinned head. Fix commit `431ed53d46` on main.

## Verdict justification

close-fixed: the fixing commit (or, for a removed feature, the removal commit and an empty search) is named above, each cited commit is an ancestor of 498a09aea9, and the changed code or the empty search is recorded at the pinned head. Rubric minimum for close-fixed is met.
