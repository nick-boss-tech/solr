# Pool assignment: linkage pass round 1 (build the work map's category tables)

Status: OPEN

Owner direction, 2026-10-19: tie the existing work together. `pool/work-map.md` sets the taxonomy and the linkage rules, and `pool/tickets-to-open.md` holds the registry of tickets that do not exist yet. This assignment builds the per-category tables the map points to, from the records we already have.

## What done looks like

For each category in your slice, one file at `material/work-map/<category-slug>.md` holding a table with one row per piece of work in that category: branch (with its tip verified against `git ls-remote` in this pass), PR where one exists, ticket or parent where it has none, siblings on the same code surface, what it supersedes or is superseded by, and its state (open PR, draft PR, gated, prepared, parked). Rows come from `receipts/`, `drafts/`, and the fork's branch list, reconciled: a branch with no receipt and no draft is still a row, with its record gap named in the state column.

While building the tables, also:

- Confirm or correct the "parent record to be confirmed" rows in the work map's unnumbered-branch table (the audit working branches), editing `pool/work-map.md` for your rows only.
- Add any ticket-to-open you find promised in a PR description or a draft and missing from `pool/tickets-to-open.md`, as a new row citing the record it came from.
- In the same push as each category file, add its link to the category's entry in `pool/work-map.md`.

## Slices (claim one, first-post claim wins)

- Slice A: categories 1 (update processing), 2 (search and query components), 3 (highlighting).
- Slice B: categories 4 (SolrJ and clients), 5 (replication, backup, restore), 6 (configsets and filestore), 7 (permissions and security declarations: pool-released items only; anything from the private track does not exist for this assignment).
- Slice C: categories 8 (streaming), 9 (suggester, spellcheck, language detection), 10 (core admin), 11 (SolrCloud and ZooKeeper).
- Slice D: categories 12 (build, scripts, release tooling), 13 (test quality), 14 (reference guide and docs), plus the final reconciliation: every fork branch accounted for in exactly one category table or the infrastructure list, with the count stated in the slice report.

## Floor

- Tips are verified against the fork in this pass; a row copied from a stale record without the check does not count.
- Where two records disagree about a branch's state, the row says so and names both; do not silently pick one.
- The four links (ticket or parent, category, siblings, supersedes) are filled or explicitly marked unknown with the reason. Unknown is an acceptable cell; a blank is not.
- Check `pool/advisory-balk-and-clear.md` before claiming: a slice whose tables already exist gets WON'T DO, not a rebuild.
