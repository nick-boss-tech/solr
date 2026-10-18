# Skip record: SOLR-3697

- Ticket: SOLR-3697, ConcurrentUpdateSolrServer's scheduler thread pool is not shut down properly. JIRA status at check: Open, checked 2026-10-18.
- Verdict: close-fixed.
- Draft branch: solr-3697-draft-close.
- Pinned head: fork origin/main 498a09aea9.
- Checked by: vm5, 2026-10-18T21:32:17Z.

## Ticket reading

The ticket is Open with no resolution at check. The summary is above; the report row is the evidence below.

## Code reading

`git grep ConcurrentUpdateSolrServer 498a09aea9` returns 0 hits. The class was removed by `4a6d85790a` (SOLR-6895). `FullSolrCloudDistribCmdsTest.java:823` uses `ConcurrentUpdateJettySolrClient` in try-with-resources.

## History

- `4a6d85790a`: ancestor of origin/main.

## Reproduction attempts

Not attempted. This is a fix-commit verdict: the commit that fixes the reported problem is on main, and its changed code is still present at the pinned head. A reproduction is not required for close-fixed.

## Gap analysis

The removal is recorded as close-fixed by the owner's decision on 2026-10-18 (the rubric's close-obsolete fits this case; the grep evidence meets its minimum).

## Verdict justification

close-fixed: the fixing commit (or, for a removed feature, the removal commit and an empty search) is named above, each cited commit is an ancestor of 498a09aea9, and the changed code or the empty search is recorded at the pinned head. Rubric minimum for close-fixed is met.
