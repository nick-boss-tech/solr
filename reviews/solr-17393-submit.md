# solr-17393-submit

- Branch: origin/solr-17393-submit
- Head: dd6c82924fff (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base c3cdf7b46e8c, 26 commits behind, 3 commits ahead)
- Scope: 3 commits, 4 files. `SuggestComponent.java` (`merge` rewritten to sort then truncate with `MERGE_ORDER`; `merge` made package-private), `SuggestComponentMergeTest.java` (new, unit tests of `merge()` only), changelog `SOLR-17393-suggester-merge-order.yml` (`type: fixed`), and `SOLR-17393-TESTING.md` (author handoff, says nothing was run).
- Verdict: Not ready (unchanged from the bulk verdict)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head. The author's TESTING.md labels its own claims as unrun, so those are treated as hypothesis.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-17393-review.md` (Not ready) was written at this head. No delta in the tree.

- Bulk F1 (HIGH, tie-break only sees candidates each shard already returned): **confirmed**. The impact is larger than the bulk text says. See finding 1.
- Bulk F1 sub-claim, "the regression exercises a distributed suggest request (`DistributedSuggestComponentTest.java:65`)": **changed**. This branch does not touch `DistributedSuggestComponentTest.java`. The only new regression is `SuggestComponentMergeTest`, a unit test of `merge()` with hand-built shard lists. No distributed or shard-side test exists on the branch.
- Bulk F2 (MEDIUM, comparator ignores payload): **confirmed** at code level. See finding 3.
- Bulk F3 (MEDIUM, sort all candidates instead of a bounded queue): **changed to LOW**. Each shard's list is already capped at `count` by its own lookup, so merge input is at most shards x count per token. The old `LookupPriorityQueue` held `count` entries. The extra temporary memory is real but bounded. See finding 4.
- Bulk F4 (LOW, remove `SOLR-17393-TESTING.md` from the outbound patch): **confirmed as a ship-time item, not a branch defect**. The round-36 handoff says to leave TESTING.md in place and that the Linux side removes it at ship time, after gating. Nothing to do on this branch. See item 7.

## Findings (ranked)

1. **HIGH, verified (code path). The tie-break runs after each shard has already truncated to `count`.** `process()` reads `suggest.count` (default 1, `solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java:273`) and calls `suggester.getSuggestions(options)` (`:292`). `SolrSuggester.getSuggestions` passes `options.count` to Lucene's `lookup.lookup(...)` (`solr/core/src/java/org/apache/solr/spelling/suggest/SolrSuggester.java:234,239,246`), so each shard returns only its own top-`count` in the lookup's order. `finishStage` then sorts only those returned candidates (`SuggestComponent.java:375-377`). If a shard's local order for equal weights differs from text order, a lexically earlier candidate can be cut on that shard and never reach the merge. With the default `suggest.count=1` and no weight field, each shard contributes one candidate picked by its local order, so the result is "lexically first of the per-shard picks", not "lexically first overall". The branch's determinism holds only for the returned set. Whether this changes the JIRA's `TEST` example depends on each shard's local tie order, which the branch does not touch and this review did not read (hypothesis; see Not checked).
   - Test gap, verified: `testEqualWeightsDoNotDependOnShardResponseOrder` gives each shard no more candidates than `count` (count 3, at most 2 per shard). It never reaches the truncation boundary. A regression needs one shard holding more equal-weight candidates than `count`.
   - Proposed fix (not applied): see owner call 1.

2. **MEDIUM, verified (code path). Distributed and local responses now disagree on equal-weight order.** `process()` returns the lookup's own order with no sort (`SuggestComponent.java:273-293`). `finishStage` re-sorts equal weights by text (`:375`). The same query therefore orders equal-weight suggestions differently on a single node and in a distributed request. The author's TESTING.md notes this too. The branch changes only the distributed path and does not document the difference. Covered by owner call 2.

3. **MEDIUM, verified (code path; scenario hypothesis). The comparator ignores payload.** `MERGE_ORDER` compares weight, then `key.toString()` (`SuggestComponent.java:79-83`). `toNamedList` serializes `payload` for every entry (`:467`, `:473`), and `merge` does not dedupe same-text entries (`SuggesterResult.java:36`, `add`). Two shard entries with the same text and weight but different payloads compare equal, so the stable sort keeps shard-response order, and payload order can vary with shard order. That same-text, different-payload suggestions occur in practice (for example with `DocumentDictionaryFactory` and a payload field) is a hypothesis this review did not test.
   - Proposed fix (not applied): add payload as a final sort key, or narrow the guarantee to distinct text. Owner call 2.

4. **LOW, verified. Temporary memory is shards x count per token.** `merge` collects every shard list and sorts before truncating (`SuggestComponent.java:365-377`). Input is already capped at `count` per shard by the shard-side lookup, so the bound is shards x count. The old code held `count` entries in a `LookupPriorityQueue`. A bounded queue using `MERGE_ORDER` with capacity `count` would keep the total order and the old memory bound.

5. **LOW, verified. The changelog overstates the guarantee.** `changelog/unreleased/SOLR-17393-suggester-merge-order.yml` says the Suggester "now returns suggestions with equal weights in a stable order (by suggestion text) instead of an order that depends on which shard responded first." Findings 1 and 3 mean the order is stable only over returned candidates and, for equal text, only per shard response order. Reword after owner calls 1 and 2 are settled.

6. **LOW, hypothesis. The new unit test may pass on the old code.** `testEqualWeightsDoNotDependOnShardResponseOrder` could pass before the fix if the old heap happens to order these three inputs the same way. The author's TESTING.md raises the same risk. A fail-before proof needs a run, which this round forbids, so this review cannot settle it.

7. **Ship-time item, not a branch defect.** `SOLR-17393-TESTING.md` is in the diff (bulk F4). Per the round-36 handoff it stays on the branch until ship time.

## Owner calls (not decided here)

1. **Where the tie-break must apply.** Option A: sort equal weights in the shard-side lookup before truncation. This fixes finding 1 at the source but changes the shard query path. Option B: over-fetch per shard and truncate after the merge, which costs more shard work and payload. Option C: keep the post-truncation merge and document that the order is deterministic only over returned candidates. This review does not pick one.
2. **Tie policy.** Is lexical suggestion text the right definition of "closest" for equal weights? Should payload be the final key? Should the local, non-distributed path use the same order (finding 2)? The JIRA packet, as summarized in the bulk review, does not define "closest".
3. **Submission strategy.** SOLR-9637 is stacked on this branch. The strategy is the owner's call. This review does not restack and does not recommend one.

## Proposed fixes (not applied; the owner decides)

- Finding 1: choose option A, B, or C above. For A or B, add a regression in which one shard holds more equal-weight candidates than `count` and the lexically first candidate is outside that shard's local top-N.
- Finding 3: add payload as the last sort key, or document the distinct-text scope.
- Finding 4: replace the list sort with a bounded queue ordered by `MERGE_ORDER`, capacity `count`.
- Finding 5: reword the changelog after the decisions above.

## Not checked

- Nothing compiled, formatted, or run. The generic inference in `Comparator.comparingLong(...).reversed().thenComparing(res -> res.key.toString())` is read, not compiled.
- Lucene's `AnalyzingSuggester` / `AnalyzingLookup` tie order for equal weights was not read. Finding 1's effect on the JIRA example is therefore a hypothesis.
- The JIRA packet was not re-read here. JIRA framing comes from the bulk review and the author's TESTING.md.
- `SuggesterResult` was checked only at `add` (line 36). The payload path was checked only in `toNamedList`.
- The new test's fail-before behavior was not established (finding 6).
