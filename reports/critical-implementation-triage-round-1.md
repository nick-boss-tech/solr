# Critical implementation triage, round 1

Claim: `claims/critical-implementation-triage-round-1-vm2.md`. Assignment: `assignments/critical-implementation-triage-round-1.md`.
Source: `material/critical-tickets-census-2026-10-24.md` (86 tickets). Each ticket was read live from the Jira REST API on 2026-10-25 (summary, description, status, fix versions). Collisions were checked with GitHub PR search on apache/solr and with this board. No code was run and no production code is written here. Each premise-run below is a plan, not a result.

## Ranked 10

Ordered by how directly the ticket states a failure, how small the fix surface looks from the ticket text, and whether a reproduction is cheap. The ranking is a judgement from ticket text, not a verified fix size.

| # | Ticket | Defect in one line | Premise-run plan | Expected fix surface | Collision |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-17370 (Bug, Open) | Search with facets throws `IllegalAccessException` when a string field holds numeric values. | Index a string field with values such as `123` and facet on it with a second field. Run on main. Confirm or kill: the exception appears only when the field mixes numeric-looking values. | Facet accumulator selection for string fields in `search/facet`. | Prior PR #3346 closed unmerged on 2025-08-30 (reason not checked). Not an open collision. |
| 2 | SOLR-12069 (Bug, Open) | `q.op` is ignored on the standard query parser. | Reproduce on the techproducts example as the ticket says. Run `q=a b` with `q.op=AND` and with the default. Confirm or kill: the results match the default operator. | Default-operator plumbing in the query parser. | None found. |
| 3 | SOLR-16139 (Bug, Open) | JSON stat facet functions such as `avg` fail on a nested facet over a `SortableTextField`. Regression from 8.11.0 to 8.11.1. | Use the reporter's docker repro if it exists in the ticket. Otherwise index a `SortableTextField` and run a nested `avg` facet. Confirm or kill: the regression reproduces on main. | Stat facet handling for analysed string fields. | None found. |
| 4 | SOLR-14638 (Bug, Open) | Edismax `field()` boost gives zero score for documents with a missing field, changed between 7.7.1 and 7.7.2. | Build the boost query from the ticket and score one document with the field filled and one with it empty. Confirm or kill: the empty-field document scores zero on main. | Edismax boost-function handling for missing values. | None found. |
| 5 | SOLR-14996 (Bug, Open) | Facet counts are wrong when a tagged filter exclusion is combined with collapsing. | Run the ticket's query with and without `{!collapse}`. Confirm or kill: the facet count differs from the `numFound` for the same filter. | Interaction of `collapse` and `ex` tags in facet counting. | None found. |
| 6 | SOLR-17291 (Bug, Open) | "indexDeletionPolicy must not be null" when a new replica opens its first searcher. | Create a replica on a configuration with the deletion policy unset. Confirm or kill: `openNewSearcher` receives a null policy. | Core open path where the deletion policy is read. | None found. |
| 7 | SOLR-14833 (Bug, Open) | Empty highlight entry for some Arabic phrase queries with the regex fragmenter. | Replay the ticket's request against a small index with `hl.fragmenter=regex`. Confirm or kill: the highlight entry is empty when a match exists. | Regex fragmenter with multi-term phrase matches. | None found. |
| 8 | SOLR-7351 (Bug, Open) | MoreLikeThis does not work with date fields. | Run the ticket's MLT request on a `TrieDateField`. Confirm or kill: the interesting-terms list is empty. | MLT term extraction for date fields. | None found. |
| 9 | SOLR-15326 (Bug, Open) | A streaming expression drops one record from a join between two collections. | Build the two-collection example from the ticket. Confirm or kill: the expression returns one fewer record than a direct query. | Streaming join over a reference field. | None found. Weaker than the rest: the ticket text is partly unclear on the expected output. |
| 10 | SOLR-13770 (Bug, Open) | Replica recovery after restart creates deep nested directories and fails. | Restart a node that has several indexes and tlog replay in progress, following the ticket's steps. Confirm or kill: the nested directories appear and recovery fails. | Tlog replay and directory creation during recovery. | None found. Reproduction needs a cluster run. |

## Near-misses

Each of these looked tractable and fails one test.

- SOLR-17122 (unique, HLL counts). Fails the "defect" test. The ref guide says unique is "not exact" beyond 100 values, and HLL is a "cardinality estimate" (`solr-ref-guide/.../json-facet-api.adoc`). The reporter's numbers may still be outside the documented error, but that is not established.
- SOLR-6213 (StackOverflow in leader election). Fails the collision test. Open PRs #5209 and #5210 cover it.
- SOLR-17433 (SolrStream 60 s timeout). Fails the collision test. Open PR #4626 covers it.
- SOLR-14200 (shard tolerance in streaming). Fails the collision test. Open PR #5199 covers it.
- SOLR-18010 (security.json corruption when adding a role). Fails the collision test: open PR #5118 and a board receipt `receipts/SOLR-18010.md`. Security-route flag: the defect corrupts security configuration. No exploit detail is written here.
- SOLR-11650 (BasicAuth credentials shown in clear text on slaves). Security-route. Not ranked.
- SOLR-7517 (core.properties persist error only logged). Design-first: the fix changes startup behaviour. Not ranked.
- SOLR-9138 (CDATA for text and string fields). Feature request. Not ranked.
- SOLR-12315 (group document count depends on `rows`). Probably the documented group semantics. Not verified. Not ranked.

## Limits

- Ticket text and PR search only. Nothing was reproduced, traced, or run. The premise-run plans are the next step.
- Collisions were checked only for PR titles that name the ticket. A PR that does not name the ticket would be missed.
- The ranking is not a fix-size estimate.
- No Jira, GitHub, or PR write. No production code.
