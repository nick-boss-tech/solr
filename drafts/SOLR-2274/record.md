# Skip record: SOLR-2274

- Ticket: SOLR-2274, SolrCore load failure can leave newly created index locked. JIRA status at check: Open, checked 2026-10-18.
- Verdict: close-fixed.
- Draft branch: solr-2274-draft-close.
- Pinned head: fork origin/main 498a09aea9.
- Checked by: vm5, 2026-10-18T21:32:17Z.

## Ticket reading

The ticket is Open with no resolution at check. The summary is above; the report row is the evidence below.

## Code reading

Commit `75b1831967` (SOLR-12801, 2018-11-29) added `IOUtils.closeQuietly(writer)` in the `SolrCore.initIndex` finally block, `solr/core/src/java/org/apache/solr/core/SolrCore.java:885`.

## History

- `75b1831967`: ancestor of origin/main.

## Reproduction attempts

Not attempted. This is a fix-commit verdict: the commit that fixes the reported problem is on main, and its changed code is still present at the pinned head. A reproduction is not required for close-fixed.

## Gap analysis

The reported behavior is no longer present at the pinned head. Fix commit `75b1831967` on main.

## Verdict justification

close-fixed: the fixing commit (or, for a removed feature, the removal commit and an empty search) is named above, each cited commit is an ancestor of 498a09aea9, and the changed code or the empty search is recorded at the pinned head. Rubric minimum for close-fixed is met.
