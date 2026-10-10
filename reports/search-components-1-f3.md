# Search components round 1, sub-batch 1, part f3: SOLR-6193 (scope audit)

Result: SOLR-6193 is HELD for an owner scope call. The branch fixes distributed `facet.pivot` merging only, not the `facet.field` case the ticket names. A pivot-only draft is written at `pr-drafts/search-components/SOLR-6193.md` for option A; do not post it under SOLR-6193 as a closure until the owner picks option A and the FIX items below are applied.

Heads used: `origin/solr-6193-submit` = `ec94bf50c80ce75b44eaf78452e60e43972bc284` (matches the claim table). Base: `c3cdf7b46e8cfff3673f76d881f32cf8e7b00622` (upstream main merge-base). Local `solr-6193-submit` is stale at `76467e19fd0`; use the origin ref.

## Findings

**1. FIX. The branch does not change the ticket's `facet.field` path (scope).**
- Evidence: `git diff --stat c3cdf7b46e8 origin/solr-6193-submit` lists 5 files: `PivotFacet.java` (+3/-1), `PivotFacetField.java`, `PivotFacetValue.java`, `DistributedFacetPivotLargeTest.java`, and the changelog. No `FacetComponent.java` or field-facet code. The ticket summary and example are `facet.field` (`research/jira-context/SOLR-6193.json`, Summary and Description, which give `facet.field={!key myblah facet.offset=10}blah&f.blah.facet.offset=20`). All re-enabled and added cases use `facet.pivot` (test lines 277-279, 567-569, 760, 790, 817).
- Replacement (PR text and any closure): "This change covers distributed facet.pivot merging. It does not change the facet.field case in SOLR-6193."

**2. FIX. The changelog claims two options the tests do not cover.**
- File: `changelog/unreleased/SOLR-6193-pivot-facet-local-params.yml`, line 2.
- Evidence: the title says local `facet.offset` and `facet.pivot.mincount` are honored. A search of every `"{!...}` local-param string in `DistributedFacetPivotLargeTest.java` at the head finds only `facet.limit`, `facet.sort`, `f.<field>.facet.limit`, and key/tag/ex/stats/range params. No local `facet.offset` or `facet.pivot.mincount` case exists.
- Replacement for line 2: "Distributed pivot facets now use facet.limit and facet.sort given as local params of facet.pivot when merging shard responses."

**3. FIX. Local `facet.offset` on a pivot may be applied twice (untested; by reading, not run).**
- Evidence: the merge applies the offset (`PivotFacetField.java` at head, line 73 read, and the offset arithmetic near lines 225-242 at head). The shard also reads the local offset: `PivotFacetProcessor.java` line 78 calls `parseParams`, which builds `SolrParams.wrapDefaults(localParams, global)` (`SimpleFacets.java` line 191), and `getTermCounts` applies `params.getFieldInt(field, FACET_OFFSET, 0)` (`SimpleFacets.java` line 442). The coordinator removes only the request-level offset from shard requests (`FacetComponent.java` line 554 at base, and line 645 for the per-field one). The local offset stays in the `facet.pivot` string sent to shards.
- Replacement: remove facet.offset from the changelog title (item 2). In the PR, keep the Limits line in the draft. Alternatively, add a distributed case with a local `facet.offset` that asserts the single-node result, then claim it. Owner call 3.

**4. FIX. The shard request is sized from request-level params, so a local `facet.limit` above 100, or `-1`, can get too few values per shard. The receipt's mechanism sentence is wrong.**
- Evidence: `FacetComponent.java` base lines 637 (`originalParams = rb.req.getParams()`), 640-641 (`requestedLimit` from request params, default 100), 664 (`shardLimit = requestedLimit + offset`), 684-685 (writes request-level `f.<field>.facet.limit` and `f.<field>.facet.pivot.mincount` into each shard request). Those request-level keys beat a plain local `facet.limit` on the shard (`DefaultSolrParams.java` lines 35-38: local first, then defaults, for the `f.` name; `SolrParams.java` lines 144-147 `getFieldParam`). So the shard does not apply the local limit. The coordinator's merge does the cut. The local `4` case passes because each shard returns its first 100 values in index order, and the merge keeps the first 4. The coordinator sizes its over-request from request-level 100, so a larger local limit or `-1` can truncate. `FacetComponent.java` is not touched by 6193 or by 11129, so neither branch fixes this.
- Receipt replacement (`receipts/SOLR-6193.md` line 7, the "Proof" bullet): "Proof: on the base code the coordinator merge reads facet.limit only from the request (PivotFacetField.java lines 64-67, base), so the first commented-out case (local facet.limit=4) returns the default 100 values. The shard does not apply the local limit; the coordinator's merge applies it."
- PR text: keep the Limits line in the draft. Owner call 3 decides whether to fix it here.

**5. NOTE. Precedence: a request-level per-field setting beats a plain local value. The ticket example, read by code, still gives 20.**
- Evidence (by reading, not run): `SimpleFacets.java` line 191 (`wrapDefaults(localParams, global)`) and line 442 (`getFieldInt(field, FACET_OFFSET, 0)`). For the ticket's example, `getFieldParam("blah", "facet.offset")` checks `f.blah.facet.offset` first, and the request has 20. So single-node returns 20 for `myblah` by this reading. The ticket says single-node returns 10. The same order is used in the branch (`PivotFacetField.java` tip line 71) and in 11129 (`FacetComponent.java` 11129 tip line 1397).
- Replacement for the PR "A choice to check" (already in the draft): "Keep the current order" versus "plain local value wins". Owner call 2. Do not write "the ticket example is fixed" anywhere.

**6. FIX. Commit history carries process words, and one subject is doubled. Fix before the PR opens.**
- Evidence: `900ca1aa793` subject "SOLR-6193: SOLR-6193: distributed pivot facets honor facet params given as local params", body "Hypothetical, unrun regression test; see SOLR-6193-TESTING.md." `ec94bf50c80` subject "SOLR-6193: remove the hypothetical-reproduction handoff doc and apply tidy formatting to the re-enabled test cases". Commits show on the PR once it is open, and a force push is not allowed after that.
- Replacement: squash the four fork commits into one before the PR opens, with subject "SOLR-6193: use local facet.limit and facet.sort in distributed pivot merges" and no body. Owner call 5.

**7. NOTE. Local ref is stale.**
- Evidence: `git for-each-ref` shows local `solr-6193-submit` at `76467e19fd02` (before the tidy commit). `origin/solr-6193-submit` is `ec94bf50c80c`, the claim's live head. `research/pipeline/HANDOFF-task3-skiplist.md` line 123 also names `76467e19fd02`.
- Replacement: use `origin/solr-6193-submit` for everything in this ticket. Re-point or delete the local ref as part of item 6.

**8. NOTE. The "round 35" review and the "premise audit WEAK" classification are not on disk under those names.**
- Evidence: the claim and assignment cite a round 35 review and a premise audit that classifies scope WEAK. Searches of `research/` and `wt/*/reviews` find no round 35 file and no WEAK classification for 6193. The closest files are `research/branch-reviews/round-28/SOLR-6193-review.md` (2026-10-07, Needs work, head `ec94bf50c80`) and `wt/code-review/reviews/solr-6193-submit.md` (labeled "round 36 Group B", 2026-10-08, Needs work). Both agree on the scope gap.
- Replacement for the report: "The round 28 review and the round 36 Group B review both give Needs work on scope." Owner call 6.

**9. NOTE. Overlap with 11129: no textual conflict. Same precedence pattern in both.**
- Evidence: the two branches' own file sets are disjoint. 6193: `PivotFacet.java`, `PivotFacetField.java`, `PivotFacetValue.java`, `DistributedFacetPivotLargeTest.java`, its changelog. 11129: `FacetComponent.java`, `DistributedFacetLocalParamsMinCountTest.java`, its changelog. Trial merge (`git merge-tree --write-tree origin/solr-6193-submit origin/solr-11129-submit`, no ref written) exits 0 with tree `0c4c5dcd26ae4db1fa38147ec79cd0eb89fd528b`. Both add `SolrParams.wrapDefaults(localParams, rb.req.getParams())` (6193 at `PivotFacetField.java` tip line 71; 11129 at `FacetComponent.java` tip line 1397). Both leave the shard-request sizing in `FacetComponent.java` (base 637-685) unchanged.
- Landing order: either order. No hunk overlap. Both PRs carry the same precedence rule, so decide it once (owner call 2). Both branches are behind current upstream: 11129's base `14c7aac0d151` is an ancestor of 6193's base `c3cdf7b46e8`. Sync both before opening PRs.

**10. FIX (for part f2, 11129 branch). The coordinator comment overstates the precedence.**
- File: `FacetComponent.java` on `origin/solr-11129-submit`, line 1396.
- Evidence: `getFieldParam` and `DefaultSolrParams.get` (above). A request `f.<field>.facet.mincount` still beats a local plain `facet.mincount`. The shards use the same order (`SimpleFacets.java` line 191).
- Replacement: "// local params (e.g. {!facet.mincount=1}fld) take precedence over plain request params, as they do on the shards; per-field request params (f.fld.facet.mincount) still win"

**11. NOTE. Research notes overstate what 11129 covers for SOLR-6193.**
- Evidence: `research/pipeline/HANDOFF.md` line 133 says 11129 "covers SOLR-9260 / part of SOLR-6193". `research/pipeline/skips.md` line 281 says the coordinator part is "covered by solr-11129-submit". By reading, 11129's coordinator still gives 20 for the ticket's offset example (item 5).
- Replacement: "SOLR-11129 honors the other local field params on the coordinator. The ticket's offset example still resolves to the request-level f.blah.facet.offset (by reading; not run)."

## Task results

**SOLR-6193 (scope audit): HELD, owner scope call.** The gate passed for the pivot tests at `ec94bf50c80`, per the receipt, but the branch changes only the distributed pivot merge. The ticket is a `facet.field` problem, and the ticket's example still resolves to the request-level offset (items 1 and 5). A draft follows the audit: `pr-drafts/search-components/SOLR-6193.md` is pivot-only, names the head `ec94bf50c80`, states the `facet.field` case as not covered, puts the precedence choice in "A choice to check", and puts the offset and sizing limits in Limits. Its Proof uses the receipt counts (30 of 30 focused, 1 of 1 for the pivot large test). The base-failure statement rests on the receipt, because the premise log is not on disk. Do not post until the owner picks option A and items 2, 3, 4 and 6 are applied. The overlap with 11129 is clean in text and shares one precedence rule (item 9).

## Owner decisions

1. SOLR-6193 scope. (A) Narrow to pivot merges and say so; the draft is ready after the FIX items. (B) Keep the ticket open and add a `facet.field` fix, in this branch or a new one. (C) Hold until the precedence rule is decided.
2. Precedence. Keep the current order (request per-field beats plain local, as single-node does), or let the plain local value win (matches the ticket example, changes single-node results). The same call covers SOLR-11129.
3. Pivot offset and shard sizing. Fix in this PR (route the shard request through the local-aware view, which touches `FacetComponent.java`, also touched by 11129), or leave both as Limits and add tests later.
4. Local `facet.pivot.mincount`. Add a test, or leave it in Limits.
5. History. Squash and reword the fork commits before the PR opens (item 6).
6. Which review counts as "round 35". The files on disk are round 28 and round 36 Group B. Confirm before the lead cites either.

## Not checked

- No builds, tests, Gradle, or `gh` calls. No `git fetch` or `ls-remote`; the origin refs in this repo were used as the live heads.
- Not on disk: `g6193-gate.log`, `g6193-premise.log`, and GitHub run 37290625099. The receipt counts were not checked against logs.
- Items 3, 4, 5 and 11 come from reading code at base `c3cdf7b46e8` and head `ec94bf50c80`. None were run. `SimpleFacets.java`, `SolrParams.java` and `DefaultSolrParams.java` are unchanged by the branch (diff stat), so the base reading holds at head.
- The shard request copy (how `sreq.params` is built from the request) was not read in full. The reading assumes the `facet.pivot` string with its local params reaches shards.
- Jira: only the local packet `research/jira-context/SOLR-6193.json` (last updated 2014-09-02) was read. Not checked live. The ASF Jira CSV at the Solr-issues root has no SOLR-6193 row.
- Data size of the pivot test index was not counted, so the truncation risk in item 4 is by reading.
- The 11129 comparison used its tip `6c1356bdff7` and `FacetComponent.java` only.
- No Lucene version claims appear in the draft, so the 9.x and 10.x check does not apply.
- Overlap with 5394, 10492, and 10844 was not checked (not in this part).
- `pr-drafts/search-components/SOLR-15319.md` was not read or changed.
- Nothing committed, posted, pushed, or changed in any branch.
