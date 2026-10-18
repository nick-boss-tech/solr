# Skip record: SOLR-4427

- Ticket: SOLR-4427, Solr should complain with a readable warning if more than one of a plugin type occurs in s. JIRA status at check: Open, checked 2026-10-18.
- Verdict: close-fixed.
- Draft branch: solr-4427-draft-close.
- Pinned head: fork origin/main 498a09aea9.
- Checked by: vm5, 2026-10-18T21:32:17Z.

## Ticket reading

The ticket is Open with no resolution at check. The summary is above; the report row is the evidence below.

## Code reading

`fbbdba2614` (SOLR-5108) makes `loadPluginInfo` throw on more than one section for non-MULTI_OK types (`SolrConfig.java:624-630`). `directoryFactory` (`:475`) and `queryConverter` (`:457`) are non-MULTI_OK. The behavior is an error, not the warning the ticket asked for.

## History

- `fbbdba2614`: ancestor of origin/main.

## Reproduction attempts

Not attempted. This is a fix-commit verdict: the commit that fixes the reported problem is on main, and its changed code is still present at the pinned head. A reproduction is not required for close-fixed.

## Gap analysis

The reported behavior is no longer present at the pinned head. Fix commit `fbbdba2614` on main.

## Verdict justification

close-fixed: the fixing commit (or, for a removed feature, the removal commit and an empty search) is named above, each cited commit is an ancestor of 498a09aea9, and the changed code or the empty search is recorded at the pinned head. Rubric minimum for close-fixed is met.
