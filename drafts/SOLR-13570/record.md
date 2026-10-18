# Skip record: SOLR-13570

- Ticket: SOLR-13570, Unable to control timeout for requests from Overseer to other nodes. JIRA status at check: Open, checked 2026-10-18.
- Verdict: close-fixed.
- Draft branch: solr-13570-draft-close.
- Pinned head: fork origin/main 498a09aea9.
- Checked by: vm5, 2026-10-18T21:32:17Z.

## Ticket reading

The ticket is Open with no resolution at check. The summary is above; the report row is the evidence below.

## Code reading

The Overseer's `getShardHandler(...getDefaultHttpClient())` call was removed by `a80eb84d56` (SOLR-14354); `git grep getDefaultHttpClient` on `solr/core/src/java` has no hits. The hardcoded `socketTimeoutMillis = 120000` is gone too (no hits; removed in `7eb46b3f03`, SOLR-16595).

## History

- `7eb46b3f03`: ancestor of origin/main.
- `a80eb84d56`: ancestor of origin/main.

## Reproduction attempts

Not attempted. This is a fix-commit verdict: the commit that fixes the reported problem is on main, and its changed code is still present at the pinned head. A reproduction is not required for close-fixed.

## Gap analysis

The removal is recorded as close-fixed by the owner's decision on 2026-10-18 (the rubric's close-obsolete fits this case; the grep evidence meets its minimum).

## Verdict justification

close-fixed: the fixing commit (or, for a removed feature, the removal commit and an empty search) is named above, each cited commit is an ancestor of 498a09aea9, and the changed code or the empty search is recorded at the pinned head. Rubric minimum for close-fixed is met.
