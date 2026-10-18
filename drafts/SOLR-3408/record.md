# Skip record: SOLR-3408

- Ticket: SOLR-3408, org.apache.lucene.store.AlreadyClosedException: MMapIndexInput already closed: MMapIndexIn. JIRA status at check: Open, checked 2026-10-18.
- Verdict: close-fixed.
- Draft branch: solr-3408-draft-close.
- Pinned head: fork origin/main 498a09aea9.
- Checked by: vm5, 2026-10-18T21:32:17Z.

## Ticket reading

The ticket is Open with no resolution at check. The summary is above; the report row is the evidence below.

## Code reading

`git grep MMapIndexInput 498a09aea9` has no hits. The Lucene sources were removed by `168df90d79` (SOLR-14497); only `MMapDirectoryFactory.java:41` remains.

## History

- `168df90d79`: ancestor of origin/main.

## Reproduction attempts

Not attempted. This is a fix-commit verdict: the commit that fixes the reported problem is on main, and its changed code is still present at the pinned head. A reproduction is not required for close-fixed.

## Gap analysis

The removal is recorded as close-fixed by the owner's decision on 2026-10-18 (the rubric's close-obsolete fits this case; the grep evidence meets its minimum).

## Verdict justification

close-fixed: the fixing commit (or, for a removed feature, the removal commit and an empty search) is named above, each cited commit is an ancestor of 498a09aea9, and the changed code or the empty search is recorded at the pinned head. Rubric minimum for close-fixed is met.
