# SOLR-18461 curl examples on techproducts: measurement

Ticket: SOLR-18461, "Make more example commands curl based and using techproducts" (Open, Major, reporter Eric Pugh, created 2026-09-20, no comments). The ticket wants the Ref Guide examples on one dataset so that each curl command can be run and checked.
Pinned main: `e59ce3c97ddb`. Counts are from `git grep` over `solr/solr-ref-guide`.

## Counts

- `curl` command lines in the Ref Guide: 463.
- Lines naming the `techproducts` collection: 232.
- Lines naming `gettingstarted`: 38.
- Lines naming `films`: 11.
- Lines naming none of these: 182.

The 182 are not all the same kind. Some are v2 calls with a placeholder such as `{collectionName}` (for example `task-management.adoc:120`), and some are cluster or node calls with no collection. Those need a per-line read before they can be converted.

## Reading

- `techproducts` is already the most common dataset in the guide. The ticket's direction is an extension of existing practice, not a change of dataset.
- The 182 unnamed lines are the real work. Converting them means choosing a collection for each, which is a per-page edit.
- The ticket's second goal (running every curl command in a check) needs a runner. No such runner is in the tree at the pinned SHA (not searched beyond `solr/solr-ref-guide`).

## Verdict

Concrete counts, large per-page work. No doc edit is made here. The draft asks Eric whether the 182 unnamed lines should be converted, and whether he wants a runner in scope.

## Not checked

- Whether any of the 232 `techproducts` lines are inside non-curl examples; the count is by line text.
- Whether a runner exists outside the Ref Guide tree.
