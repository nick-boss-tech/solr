# Skip record: SOLR-9400

- Ticket: SOLR-9400, "Casting error when using SortedMapBackedCache". JIRA status read 2026-10-20T14:24:58Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: close-obsolete (confirmed; the recorded verdict is kept).
- Draft branch: solr-9400-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T14:24:58Z (UTC).

## Ticket reading

Full import through the Data Import Handler fails with a ClassCastException when an entity uses `cacheImpl='SortedMapBackedCache'`. The reporter asks for a clearer exception naming the entity and field.

## Code reading (pinned head)

- `git grep -l SortedMapBackedCache 498a09aea97aac86c2cc7ac114113b946d249942 -- solr`: empty. The cache class is not in the tree.
- `git grep -l "org.apache.solr.handler.dataimport" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr`: empty. The Data Import Handler package is not in the tree.

## History

- evidence.py at the pinned head: no commits naming SOLR-9400.

## Reproduction attempts

Not applicable: the handler the ticket is about is absent, so there is nothing to run.

## Verdict justification

close-obsolete meets the rubric: the feature the ticket is about (the Data Import Handler and its cache) is removed, and the grep under `solr/` at the pinned head is empty for both names.

## Limits

This record does not name the commit that removed the handler.
