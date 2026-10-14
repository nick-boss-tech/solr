# Search components opened review, round 1, slice 6 (PRs 5150 to 5153)

- Assignment: `assignments/pool-search-components-opened-review-round-1.md`. Claim: `claims/search-components-opened-review-round-1.md`.
- Reviewer: review agent, Windows host. All four PRs are open drafts, and every head was unchanged when re-read at the end of the run. Nothing pushed, committed, or flipped to ready. No builds, tests or Gradle. One live edit, on PR 5152's body.

## Common checks

- Live title, head SHA, ref, draft and state read with `gh.ps1`. Head branches fetched read-only, and merge-bases match each receipt's base.
- Changelog fragments parsed by reading (no YAML library on this host). All four are valid, and none has an unquoted colon-space.
- Mechanical scan of the live bodies: no em dashes, no "we", and no gate, receipt, ledger, JUnit, seed, Claude, pool or assignment wording. The AI header and footer are on all four, a bold summary opens each section, and the changelog line links the head SHA.

## PR #5150 (SOLR-9864), head `b5826e466b9a`. BLOCKED: code (gate lane) and history (Nick). The description is SATISFIED, with no edit.

- Diff: three production lines in `SolrQuery.getCopy`, one test and the changelog. The test-method counts (17 at head, 16 at base) match the receipt (27 of 27, 2026-10-05). The base `addSort` and `getCopy` code confirm the body's symptom claim.
- Finding (code): the new test sits between the javadoc "Verifies that getSort() returns an immutable map" and `testGetSortImmutable`, so the comment now documents the wrong test. Gate job: `gates/SOLR-9864-review-javadoc-placement.md`. Proposed patch: move `testGetCopyKeepsSortClauses` (head lines 132 to 146) above the javadoc block at line 128.
- Finding (history): public commit subjects include "add hypothetical-reproduction handoff doc" and its removal. Gate job: `gates/SOLR-9864-review-history.md`.

## PR #5151 (SOLR-10305), head `f559126972`. BLOCKED: history (Nick). The description is SATISFIED, with no edit.

- The delta from the gated head `c43de86d4c13` is the changelog and one javadoc line only, as the receipt says. `MinimalSchemaTest` has 4 tests at head and 3 at base. The base citations (L793, L482 to L492, L77 to L79) are confirmed. Owner decision 3 (standalone 403 stays) is in Limits.
- Note, not edited: the title and the bold "What this change does" line say a distributed request now gets a 400. The default standalone case still gets a 403, and Limits names that. Left as approved wording.
- Gate job: `gates/SOLR-10305-review-history.md`.

## PR #5152 (SOLR-10424), head `e629ab8bb293`. BLOCKED: history (Nick). The description is SATISFIED after one edit.

- Diff: one `params.json` line removed (the JSON stays valid), a 43-line test that reads the file, and a changelog fragment. The base citations (params L3 to L5, `ImplicitPlugins` L28 to L31, solrconfig L650 to L654, schema L180) and the head citation (schema L232, commented out) are confirmed. Limits says "land before, or together with, SOLR-8240", which matches owner decision 1 in `reports/search-components-3-w3.md`.
- Edit (one sentence in "What this change does"):
  - Before: "The sample schema has no catch-all field: the `*` dynamic field is commented out".
  - After: "The sample schema has no `*` dynamic field to catch unknown names: it is commented out".
  - Reason: `managed-schema.xml` L178 calls the `text` field a "catchall field", so the old sentence contradicted the file.
- Gate job: `gates/SOLR-10424-review-history.md`.

## PR #5153 (SOLR-10492), head `ecf21e2192cb`. BLOCKED: history (Nick). The description is SATISFIED, with no edit.

- Diff: `SimpleFacets` (the `group.field` fallback and the grouped refinement helper), a new test case with `facet.limit` 1 and 2 and overrequest off, and a changelog fragment. The base citations (L979 and `StoredFields` L66) are confirmed. The `facet.query` path (`getGroupedFacetQueryCount`, L330) confirms the Limits asymmetry. The Jira numbers (8 and 233) match the packet. The receipt counts (2, 17, 1) and the date 2026-10-05 match.
- Note: the `group=true`-less acceptance is stated as a behavior change but has no Choice section. The earlier owner call (`reports/search-components-1-f2.md`, Finding 5) is reflected as drafted. Flag it if Nick wants a Choice section.
- Gate job: `gates/SOLR-10492-review-history.md`.

## Gate jobs filed this slice (worktree `gates/`, not committed)

1. `gates/SOLR-9864-review-javadoc-placement.md`: the misplaced javadoc. Gate shape: changelog parse, tidy with the versions-file restore, Error Prone compile, the pre-fix proof leg, focused `SolrQueryTest` (17), module check.
2. `gates/SOLR-9864-review-history.md`, `gates/SOLR-10305-review-history.md`, `gates/SOLR-10424-review-history.md`, `gates/SOLR-10492-review-history.md`: the public commit subjects. The add and remove pairs are 9864 `f7d6028ddde` and `b5826e466b9`, 10305 `c21ca8c0e75` and `0cf26e5f331`, 10424 `06de62e5594` and `b48f4053430`, 10492 `8e58bae3050` and `ecf21e2192c`. The net tree is clean. Proposed patch: rebuild each branch from its merge-base with a byte-identical tree and plain subjects. Push only on Nick's direction, through `push-fork-lease.ps1` with the current head as the expected SHA.

## Receipt and slate disagreements

- SOLR-10424: the body's "verified 2026-10-08" rests on the receipt's status line, which calls it a main-side record date. The receipt has no gated-head date. `reports/draft-date-flags-round-1.md` says remove it, and `reports/census-followup-scheduled-round-2.md` accepted it. The body matches the receipt, and the main side should settle it.
- SOLR-10424: the slate row has no landing-order note. The order is recorded only in `reports/search-components-3-w3.md` (FIX 3, owner decision 1). Cross-check against PR 5145 (slice 4).
- SOLR-10492: the gate logs named in the receipt (`g10492-*.log`) are not on this host. The proof numbers are from the receipt only.
- SOLR-9864 and SOLR-10305: the receipt counts, dates and slate heads agree with the live heads.
