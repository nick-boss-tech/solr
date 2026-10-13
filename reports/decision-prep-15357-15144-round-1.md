# Decision prep: SOLR-15357 (owner decision 2) and SOLR-15144 (options a, b, c), round 1

Assignment: `assignments/pool-decision-prep-15357-15144-round-1.md` on `origin/pr-prepare`.
Scope: read-only. No build, test, Gradle run, gate job, push, GitHub write, or repository write. The claim file under `claims/` was not written because it is a repository write; the main agent should add it. Nothing here is decided. Each item ends with one recommendation for the owner to accept or overrule.

## Heads and records checked

| Ref | `ls-remote origin` head | Receipt head | Match |
|---|---|---|---|
| `refs/heads/pr-prepare` | `5ecd784df17dcc439e63ab30270dfe82a28dd24c` | worktree HEAD is the same | yes |
| `refs/heads/solr-15357-submit` | `e9e7cc3a0c1c8a521b755d884dc26c8859f21600` | `receipts/SOLR-15357.md` line 5 | yes |
| `refs/heads/solr-15144-submit` | `68b0fc31e05663a441d5efdc465b8e4f222831f8` | `receipts/SOLR-15144.md` line 4 | yes |

Receipt lines relied on:
- `receipts/SOLR-15357.md` line 5: "Status: GATE GREEN at head e9e7cc3a0c1c8a521b755d884dc26c8859f21600 (branch solr-15357-submit, fast-forward from d717899b8736309bd72f54b11230f198ae226e7d)."
- `receipts/SOLR-15357.md` line 6: "Test and title only; no production change."
- `receipts/SOLR-15357.md` line 22: "Owner decision 2 asks one question: should SOLR-15357 be widened to skip copy targets in the real-time get decoration path and the `fl=*` path, or stay narrow with that gap listed in Limits and a follow-up offered?"
- `receipts/SOLR-15144.md` line 7: "Proof: pre-fix proof PASS in the hardening receipt; the round 35 troubleshooting confirms the unit premise discriminates (base production fails exactly 1 of 2, testDocumentFetchIsNotSkippedWhenTimeIsUsedUp)."
- `receipts/SOLR-15144.md` line 8: "Round 35 finding: a distributed test added for the round failed identically at head and on base, and troubleshooting concluded the branch's production change alone does not produce the claimed end-to-end behavior on this base, because the coordinator's partial-results short-circuit discards the fetched documents. That report marks the branch NOT DONE and not PR-ready, with owner options: (a) ship as the unit-level fix with corrected claims, (b) widen the branch to SearchHandler, or (c) hold. The added distributed test was not committed; the fork tip is unchanged."

Premise and record corrections:
1. The assignment says the branch "fixes a decoration path so copy targets are skipped in one place." The diff does not do that. The change adds sub-fields to the shared copy-target bookkeeping. No decoration code changes. The diff and the records govern (see A.1).
2. Several reports audited `d717899b873`, the head before the fast-forward to `e9e7cc3a0c`. The fast-forward changed only a test file and the changelog title (receipt line 6). The production files and the line numbers cited below are unchanged across that range, so the older audit line numbers still hold at the current head.
3. The category record (`reports/search-components-4.md` lines 26 and 51) names options (a), (b) and (c) but does not spell them out. The full text exists only in the receipt (line 8, above) and in `reports/search-components-4-s5.md` (findings 1 to 4 and owner decision 1). No record gives a longer text for option (c) than "hold".
4. The owner-decision lists disagree on history rewrites. `reports/search-components-4.md` owner decision 10 (line 59) lists 15479, 15895, 15319, 17791, 7520 and 17372, and omits 15144. `reports/search-components-4-s5.md` owner decision 2 (line 61) says SOLR-15144 needs a rebuild or squash before any PR. The owner should settle this. It is not decided here.
5. The round 35 troubleshooting report that the receipt names (`goal files/reviews-2026-10-07-round35-bulk/15144.md`) is not in the branch tree. The coordinator discard point is therefore not pinned by any record on the branch.

---

## Item A: SOLR-15357, owner decision 2 (widen or stay narrow)

### A.1 What the branch changes

Diff `b5c71bc5573..e9e7cc3a0c`: 9 files, 360 insertions, 3 deletions. Production code is in six files: `FieldType.java` (new `getSubFields` hook, default empty), overrides in `CurrencyFieldType.java`, `AbstractSubTypeFieldType.java` (PointType) and `BBoxField.java`, and bookkeeping in `IndexSchema.java` (sub-field counts added in `incrementCopyFieldTargetCount`, around lines 1073-1081 at head; `getSubFields` at 1098; `isCopyFieldTarget` at 1595) and `ManagedIndexSchema.java` (decrement and delete path; the delete refusal is at line 503 at head). `RealTimeGetComponent.java` and `SolrDocumentFetcher.java` are not in the diff.

Callers of `isCopyFieldTarget` at head (grep of `solr/core/src/java`):
- `RealTimeGetComponent.java` 633 (in-place update from the update log, `removeCopyFieldTargets`)
- `RealTimeGetComponent.java` 879 (`toSolrInputDocument`: `getInputDocument` and atomic-update materialization)
- `RealTimeGetComponent.java` 917 (`toSolrDoc`, the /get stored-field document: "don't return copyField targets")
- `DocumentBuilder.java` 483
- `ManagedIndexSchema.java` 503 (delete refusal)
- `SolrDocumentFetcher.java` 190-191 (feeds `nonStoredDVsWithoutCopyTargets`, which only `UpgradeCoreIndex.java` 423 reads)

So 15357 makes sub-fields report as copy targets to these existing checks. The decoration path is untouched.

### A.2 The sibling paths

Decoration set. In `SolrDocumentFetcher.java`, `nonStoredDVsUsedAsStored` is built at lines 186-187 (docValues, not stored, `useDocValuesAsStored`) with no copy-target check. The only set that checks copy targets is `nonStoredDVsWithoutCopyTargets` (190-191). `getNonStoredDVs(true)` returns the unchecked set (758-759). `decorateDocValueFields` (593-612) sets every named docValues field that has a value and has no copy-target filter.

Path 1, real-time get: `RealTimeGetComponent.java` 356-357:
`docFetcher.decorateDocValueFields(doc, docid, docFetcher.getNonStoredDVs(true), reuseDvIters);`
- Input shape: a /get by id on a document whose sub-field has docValues values in the index.
- Can a copy target appear: only when the sub-field has docValues values written.
  - Currency, base and head: no. Base `CurrencyFieldType.java` 182 and 184 call `createField`, which writes no docValues. Docs are written with docValues only by `createFields` (`PointField.java`, base 262-306). SOLR-15358 switches the two calls to `createFields` (`reports/schema-analysis-round-1-s2.md` finding 5 and owner decision 3). After that, currency sub-field values exist in the index.
  - PointType: base `PointType.java` 67-83 calls `sf.createFields` for each dimension, so a point sub-field gets docValues values when its sub-field type has docValues.
  - BBoxField: base `BBoxField.java` 141-157 gives sub-fields docValues when the number type has them.
- Observed: the value is added to the /get document. The stored-field filter at line 917 does not see docValues-only sub-fields, because the document it iterates is built from stored fields (by reading, not run). So decoration is the only route for docValues-only copy targets, and 15357 does not cover it.

Path 2, search with `fl=*`, a pattern or explicit names: `SolrDocumentFetcher.java` 862-890 (`calcDocValueFieldsForReturn`) builds `dvFields` (790), which are decorated at 919.
- Wildcard: line 866, `result.addAll(getNonStoredDVs(true));`
- Pattern: line 878, loop over `getNonStoredDVs(true)`
- Explicit names: line 890, `result.retainAll(getNonStoredDVs(false));`. An explicitly named docValues field is kept even when it is a copy target.
- Input shape and condition: the same as path 1.
- Observed: values appear in search results.
- Context: no copy-target filter exists on the search stored-field path at head (grep above), so stored copy targets already appear in search results.

Test schema (`solr/core/src/test-files/solr/collection1/conf/schema-copyfield-subfields.xml`): `*_l_pl` is plong, stored=false, docValues=true; `*_s_c` is string, stored=false, docValues=true; `pdouble` has no docValues (line 22); BBoxField has `storeSubFields="false"`. So the branch's own test does not reach point or bbox decoration. The currency /get test passes because currency has no docValues values, on base as well (receipt and s2 finding 3).

Pre-existing behavior, not created by this branch: on base, decoration already returns any directly targeted docValues field that has `useDocValuesAsStored` (base `SolrDocumentFetcher.java` has the same unchecked set).

### A.3 Options priced

Option N: stay narrow (s2 owner decision 2, option b).
- Code: none.
- Limits and follow-up: drafted in A.5.
- Behavior change: none beyond 15357 as built.
- Risk: no new output on this branch. Currency has no docValues values at base or head. The point and bbox decoration exposure already exists at base.
- Left undone: the decoration sites (`RealTimeGetComponent.java` 357; `SolrDocumentFetcher.java` 866, 878, 890). Once SOLR-15358 lands with default docValues-as-stored, currency sub-field values appear in /get and in search output. That is new output relative to base. So SOLR-15358 must not land with default docValues-as-stored until owner decision 3 is settled (s2 owner decision 3).
- Review surface: unchanged, 9 files.

Option W: widen (s2 owner decision 2, option a).
- Code: four edits, `RealTimeGetComponent.java` 357 and `SolrDocumentFetcher.java` 866, 878, 890. Each must become the intersection of the used-as-stored set and "not a copy target". Do not swap in `getNonStoredDVsWithoutCopyTargets()`. That set is built from all non-stored docValues fields (186-191), so a swap would add fields with `useDocValuesAsStored=false` to /get and search output. A helper is needed in `SolrDocumentFetcher`, which is on the search hot path.
- Tests: a combined run with SOLR-15358 (s2 says this is needed). Per-path coverage: /get, `fl=*`, pattern, explicit name. Point and bbox sub-fields with docValues need a schema the branch does not have.
- Behavior change beyond the defect:
  1. Direct docValues copy targets stop appearing in /get and `fl=*` output. Base returns them by reading (see A.2).
  2. Search `fl=*` output would differ from stored copy targets, which search still returns.
  3. Explicit `fl` names change behavior at line 890.
- Review surface: two more production files (`RealTimeGetComponent.java`, `SolrDocumentFetcher.java`) plus a helper. The Jira ask covers sub-fields only.
- Left undone: owner decision 3 (default docValues-as-stored on SOLR-15358 sub-fields) is still needed unless widening is done.

Not one of decision 2's options: the draft's own "A choice to check" (where the bookkeeping lives, and whether the delete check changes). That is s2 owner decision 4. Noted only.

### A.4 Recommendation for item A: stay narrow (option N)

Reasons:
1. 15357 as built adds no new /get or search output on this branch. The decoration exposure it leaves is either already present at base (point and bbox with docValues) or only appears with SOLR-15358 (currency).
2. Widening changes output that works today (direct docValues copy targets and explicit names), touches the search hot path, and is wider than the sub-field defect the Jira describes. Its proof needs the SOLR-15358 branch to be meaningful.
3. The real dependency is SOLR-15358. Narrow 15357 leaves the decoration gap open, so SOLR-15358 should not land with default docValues-as-stored until owner decision 3 is settled. That is the cost of narrow.

Owner overrule (one line): "Widen: real-time get line 357 and fl=* lines 866, 878 and 890, if SOLR-15358 must land with default docValues-as-stored in the same window."

Fact that would change this: a decision to land SOLR-15358 with default docValues-as-stored in the same window. Then narrow would ship a new /get and `fl=*` leak for currency sub-fields, and widening (or a default change for sub-fields) becomes required.

### A.5 Draft Limits text for option N (plain language, for later public use)

> Real-time get and searches with `fl=*` can still return docValues fields that are copyField destinations. Those paths read docValues fields without the copyField check, and this change does not touch them. Currency sub-fields are written without docValues on this branch, so they are not returned there. Point and bounding box sub-fields can carry docValues when their field type is set up that way. Once SOLR-15358 is merged, currency sub-field values can appear in real-time get and `fl=*` output as well. A follow-up will cover these paths.

Before posting the public draft (`pr-drafts/schema-analysis/SOLR-15357.md`), remove its "A choice to check" section, which is internal decision text, and re-point its head links from `d717899b873` to `e9e7cc3a0c` after the owner rules. The AI-assistance wording is a separate policy item and is not decided here.

---

## Item B: SOLR-15144, options (a), (b), (c)

### B.1 The branch change (verified)

Diff `b5c71bc5573..68b0fc31e05`: 3 files, 74 insertions. `TimeAllowedLimit.java` (+6), `TimeAllowedLimitTest.java` (+61), `changelog/unreleased/SOLR-15144.yml` (+7).

`TimeAllowedLimit.java` at head, lines 97-103:
- 98: `if ((sreq.purpose & ShardRequest.PURPOSE_GET_FIELDS) != 0) {`
- 99-100 (comment): "the ids were already merged; skipping the fetch of their fields, or limiting it, would / only return the count without documents"
- 101: `params.remove(CommonParams.TIME_ALLOWED);`
- 102: `return false;`

The only caller of the hook is `SearchHandler.java` 586-597 at base: `queryLimits.adjustShardRequestLimits(...)` at 587, and the `continue` at 595 that drops the shard request. So the branch change stops that skip for document-fetch sub-requests once the budget is spent, and removes `timeAllowed` so the shard runs the fetch with no time limit.

Changelog title, line 1: "A distributed request whose timeAllowed ran out in the first phase now still fetches the documents of the ids it merged, instead of returning a count with an empty document list".

The comment and the title claim end-to-end behavior that receipt line 8 says does not hold.

### B.2 Options priced

Option (a): ship as the unit-level fix with corrected claims.
- Changes: title becomes "Document fetch requests are no longer skipped when timeAllowed is used up in an earlier phase" (text from `reports/search-components-4-s5.md` finding 1, checked against the code above). Comment at lines 99-100 becomes "the ids were already merged; skipping the fetch of their fields leaves them without documents, so the fetch is not time limited here". The PR text gains one sentence: when the time is used up before the document fetch, the fetch request is sent without a timeAllowed value, so the shard runs it with no time limit. Code is unchanged. The two text edits move the head, so the proof record must name the new head.
- Risk:
  1. The shard runs the fetch with no time limit whenever the budget is spent. The work is bounded by the number of rows, not by time.
  2. The change applies to every distributed request whose budget is spent before the fetch. That is the intended case.
  3. End to end, the receipt says the distributed test fails identically at head and base. So under (a) the result that the distributed test sees does not change. A public claim that fetches "are no longer skipped" with no visible effect in the only end-to-end test is a weak claim.
  4. Review surface is three files. The unit test is the proof; base fails 1 of 2 (receipt line 7).
- Landing: no interaction with sub-batch 2, because `SearchHandler.java` is untouched. The commit subjects `820f47ae5df` ("SOLR-15144: testing handoff for external reviewer") and `68b0fc31e05` ("SOLR-15144: drop the TESTING.md handoff note") carry process words. They need a rebuild or squash before any PR, under any option (`reports/search-components-4-s5.md` finding 5).
- Left undone: the symptom (documents missing under timeAllowed) is not shown fixed end to end. The coordinator path stays open. Name it as a Limits line and a follow-up.

Option (b): widen to SearchHandler.
- Changes: no record names the change. Candidate sites read at base, not traced to a fix:
  - `SearchHandler.java` 586-597 (the hook call and skip; could exempt document-fetch sub-requests)
  - `SearchHandler.java` 615-642 (partial flags from shard responses set on the merged response)
  - `QueryComponent.java` 1102-1103 (a shard exception sets partial), 1141-1142 (partial header read per shard response), 1239-1243 (merged response marked partial)
  - `ResponseBuilder.java` 489-500 (a partial local result replaces the document list with an empty DocSlice)
  The receipt's diagnosis places the discard in the coordinator's partial-results short-circuit. This read did not reduce that to one line, so the change site is not pinned.
- Risks: it crosses into sub-batch 2's handler components (`reports/search-components-4.md` line 46; s5 finding 4). Partial-results behavior is shared by every distributed request, so the change reaches past timeAllowed. It needs a new distributed test (none is committed), an enqueued proof (no Gradle on this host), and a fail-before check. Largest review surface of the three.
- Left undone: nothing is drafted. The timeline depends on sub-batch 2.
- Name: a follow-up ticket, or an owner Choice in the PR.

Option (c): hold.
- Changes: none. The branch stays NOT DONE and not PR-ready (receipt line 8). The records name (c) only as "hold" (`receipts/SOLR-15144.md` line 8; `reports/search-components-4-s5.md` owner decision 1).
- Risk: no public surface changes. The head keeps the overclaiming changelog title and comment (s5 findings 1 and 2; `reports/search-components-4.md` corrections list). They must be corrected under any option before a post.
- Left undone: the decision itself, the coordinator discard point, and the history-rewrite question (item 4 above).
- Name: held status in the category record. No PR and no draft.

### B.3 Recommendation for item B: option (c), hold for now

Reasons:
1. The only end-to-end evidence in the records (the distributed test) shows no change under the branch's current code. Option (a) would publish a narrow claim whose visible effect is not shown.
2. Option (b) has no named change site, no test, and reaches sub-batch 2's code. It is the largest cost for an unknown fix.
3. Hold keeps the overclaim off every public surface and costs nothing now. The step that would settle it is a reading (or a gated run) that names the coordinator discard point.

Owner overrule (one line): "Option (a): publish the narrow fix now with the s5 title, comment and Limits wording."

Fact that would change this: a record or a gated distributed run showing that the branch's current change alone returns documents in the distributed test. Then option (a) is the recommendation, and receipt line 8 would be wrong. A decision that a SOLR-15144 PR must go out this wave regardless of symptom coverage would also point to (a).

Drafted Limits line for option (a), plain language:
> The coordinator path is not changed. A fetch that starts with time left still runs under the shard's own timeAllowed, as before. A follow-up will cover the coordinator path.

---

## Gaps and conflicts, named

1. The item A premise ("fixes a decoration path ... in one place") conflicts with the diff. The diff and the records govern.
2. The round 35 troubleshooting report named in the receipt is not in the branch tree. The coordinator discard point is not pinned on the branch.
3. Option (c) has no text beyond "hold". Option (b) names no change site.
4. Owner decision 10 in `reports/search-components-4.md` omits 15144; `reports/search-components-4-s5.md` owner decision 2 includes it.
5. The 15357 public draft still cites `d717899b873` and carries an internal section. This is draft cleanup after the owner rules, not part of the decision.

## Checked and not checked

- Checked: the records named in the assignment and the ones they point to; both branch diffs; the cited lines at each head; base callers of the skip points; base creation of currency, point and bbox sub-fields.
- Not checked: whether any existing test pins direct docValues copy targets in /get or `fl=*` (not searched); the exact coordinator discard point; point and bbox sub-field docValues in shipped configsets beyond the files cited; GitHub state; live JIRA text.
- No test, build or Gradle run. Statements about /get and `fl=*` output come from reading the code, as the s2 report also states.
