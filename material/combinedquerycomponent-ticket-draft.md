# Draft Jira ticket: CombinedQueryComponent mergeIds totals (skip ordering, fallback, inverted exactness fold)

Status: NOT filed. Draft for Nick to file. Nick's rule (2026-10-11): new-ticket texts go to him in one consolidated email, sent only once every included ticket has been completely reviewed; this text joins that email when its review returns SATISFIED. One passage in the ticket text is a bracketed open item (the maximum fold) pending Nick's ruling; the text is not fileable until he resolves it.

Source: reports/combinedquerycomponent-premise.md on this branch, including the main-side run addendum (verdicts CONFIRMED BY RUN, 2026-10-11, on Apache Solr main snapshot d76fc14b9857e77b830d7a82514b28c523a239c2, seed C0FFEE42C0FFEE42, probe class org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest; log and JUnit XML on the main side). The premise report recommends one ticket covering the three defects, filed after the mergeIds ticket is filed.

The ticket text below the line is Jira wiki markup: the Title line is the ticket summary and the rest is the description.

---

Type: Bug
Title: CombinedQueryComponent.mergeIds counts a skipped partial shard in the per-query totals, adds the running total as the approximateTotalHits fallback, and folds hitCountIsExact inverted

🤖 _AI text below_ 🤖 _(posted on behalf of Nick Shanin)_

h2. Symptom

Combined queries (the hybrid search path that fuses the result sets of several queries, for example with reciprocal rank fusion) merge their per-query shard results in {{CombinedQueryComponent.mergeIds}}. Three defects sit in the totals that merge reports:

 * When one shard answers with partial results for a query and sends no {{sort_values_<q>}} for it (the {{timeAllowed}} / query-limit exit), the merging node drops that shard's documents for the query but still counts the shard in the query's totals. The merged {{numFound}} for that query includes hits from a shard that contributed no documents to it (7 instead of 3 in the reproduction below), and the merged {{approximateTotalHits}} includes the skipped shard's approximate count (15 instead of 5).
 * When a shard response carries no {{approximateTotalHits}} header, the merge falls back to adding the running per-query total, which already includes the current shard. Headerless shards therefore accumulate cumulatively instead of once each: two shards with 2 and 3 documents and no headers merge to an approximate total of 7 instead of 5 in arrival order A then B (A the shard with 2 documents, B the shard with 3; in the other order the total is 8). The merged {{numFound}} itself is correct in that case (5); only the approximate total is wrong.
 * The {{hitCountIsExact}} fold is inverted: it keeps the merged flag true only while shards report their counts as not exact. An all-exact merge, the ordinary case, reports {{numFoundExact=false}}, and an all-inexact merge reports {{numFoundExact=true}}. Only a mixed merge happens to come out right.

The response header does carry {{partialResults=true}} when a shard is partial, so callers know the result set is partial; nothing tells them the counts are not the counts of the merged result.

h2. Mechanism

{{CombinedQueryComponent.mergeIds}} (main, {{solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java}}) loops over the queries to combine (line 328), and inside that over the shard responses (line 334). The per-query totals {{queryNumFound}} and {{queryApproximateTotalHits}} reset for each query (lines 330 to 331). {{hitCountIsExact}} is declared once, outside the query loop (line 315), so one flag serves every query, and it is written to the response at line 449 ({{responseDocs.setNumFoundExact(hitCountIsExact)}}). The per-query totals reach the response through lines 441 to 442, and each shard's contribution is folded in before the partial-results skip:

 * {{queryNumFound += docs.getNumFound()}} at line 342
 * the {{hitCountIsExact}} fold at line 343: {{hitCountIsExact = hitCountIsExact && Boolean.FALSE.equals(docs.getNumFoundExact())}}
 * the exception {{continue}} at lines 348 to 350 also comes after those folds
 * the partial flag is read from the response header at lines 359 to 364
 * the approximate block at lines 381 to 387: the header value is added at line 386, and when the header is absent the fallback at line 384 adds {{queryNumFound}}
 * the {{sort_values_<q>}} lookup is at lines 390 to 396, and {{needsUnmarshalling}} is computed at line 398
 * the skip itself is at lines 399 to 400: {{if (thisResponseIsPartial && sortFieldValues.size() == 0 && needsUnmarshalling) continue;}}

So a skipped shard has already contributed its count, its exactness and its approximate count to the per-query totals before the {{continue}} fires.

The fallback operand is the second defect. {{queryNumFound}} at line 384 already includes the current shard, because line 342 ran earlier in the same iteration, so each headerless shard adds the running total over all shards seen so far, headered or not. With only headerless shards the approximate total is the sum over shards of each shard's count multiplied by the number of headerless shards arriving at or after it, which is why the value depends on arrival order (shard responses are in completion order). The sibling fallback in {{QueryComponent}} has the same line shape with the opposite error: there the running total is read before the current shard is folded in, so two shards of 2 and 3 give 2 instead of 5; here they give 7 instead of 5.

The fold at line 343 is the third defect. {{SolrDocumentList.numFoundExact}} defaults to true ({{solr/solrj/src/java/org/apache/solr/common/SolrDocumentList.java}}, line 50), and a shard writes true when its count is exact. The written form keeps the merged flag true only while shards report FALSE, the inverse of the meaning the flag carries at line 449. {{QueryComponent}} uses the other form (lines 1173 to 1174 at main): {{if (hitCountIsExact && Boolean.FALSE.equals(docs.getNumFoundExact())) hitCountIsExact = false;}}, which clears the flag when a shard reports not exact and otherwise leaves it.

Nothing in the skip depends on a total: its inputs are the partial flag, the sort values and {{needsUnmarshalling}}, so the skip can be decided inside each (query, shard) iteration before any fold, the same reorder shape as the {{QueryComponent}} fix.

h2. Reproduction

Confirmed by run on this checkout (main {{d76fc14b9857}}) with a scratch probe harness: the class {{CombinedQueryComponentMergeTotalsTest}} in the component's own test package, driving fabricated shard responses through {{mergeIds}}, seed {{C0FFEE42C0FFEE42}}. The probe is a scratch harness used to confirm the defects; it is not a proposed test. All seven probe cases failed on unmodified main, every failure an assertion carrying the predicted wrong value:

 * Partial skip, one query, sorted on a field so sort values need unmarshalling: a partial shard with an {{approximateTotalHits}} header of 10, {{numFound=4}} and no {{sort_values_0}}; a complete shard with a header of 5 and {{numFound=3}}. Merged {{numFound}} was 7 instead of 3, and merged {{approximateTotalHits}} was 15 instead of 5.
 * Fallback, two complete shards with 2 and 3 documents, neither sending an {{approximateTotalHits}} header, the first flagging {{maxHitsTerminatedEarly}}: merged {{numFound}} was 5 (correct), and merged {{approximateTotalHits}} was 7 instead of 5 (2, then the running 2 + 3 folded again).
 * Exactness, two shards whose per-query lists both report {{numFoundExact=true}}: the merged flag was false instead of true. With both reporting false, the merged flag was true instead of false.

None of the component's existing tests asserts {{numFoundExact}} or {{approximateTotalHits}} on the merged response, which is why this was not visible.

End to end, the skip defect needs a combined query in a distributed search where one shard exits early ({{timeAllowed}} or another query limit) for one of the queries, so it answers partial without sort values for that query. The fallback and the fold need no partial results at all: any shard response without the {{approximateTotalHits}} header exercises the fallback, and the fold fires on every merge.

h2. Fix options

Three smallest changes present themselves, one per defect:

 * Test the skip condition before accumulating the shard's per-query totals (equivalently, move the folds at lines 342 and 343 and the approximate block at lines 381 to 387 below the skip at lines 399 to 400), so a skipped shard never enters that query's totals. One detail rides with it: after the reorder, a query whose shards are all skipped contributes a count of zero, and how that case bears on the shared exactness flag needs the same care as the all-skipped case in the {{QueryComponent}} fix.
 * Correct the fallback operand: when a shard sends no {{approximateTotalHits}} header, add that shard's own count ({{docs.getNumFound()}}) instead of the running {{queryNumFound}}, with a null guard giving 0, because {{docs}} can be null when a shard has no per-query entry.
 * Correct the fold direction at line 343: {{hitCountIsExact = hitCountIsExact && !Boolean.FALSE.equals(docs.getNumFoundExact())}}, equivalently the {{if}} form {{QueryComponent}} uses at lines 1173 to 1174, so the merged flag is true only when every contributing list reports its count exact.

The first change is the recommended one, with the second and third as part of the same fix. Correcting the totals when the skip fires instead means reconstructing what the skipped shard added after the fact: for the approximate total that means knowing which branch contributed and, on the fallback branch, what the running total was at that shard's arrival position, and no subtraction can repair the exactness flag, which is a fold over every shard and cannot be un-folded without per-shard tracking. Testing the skip before accumulating leaves every total describing the same set of shards. The second change is required rather than optional: the fallback fires for complete shards too, not only skipped ones, so a reorder alone leaves the double count. The third is one line in the same accumulation, and without it the merged response keeps reporting exact merges as inexact.

[BRACKETED OPEN ITEM, PENDING NICK'S RULING, NOT PART OF THE SETTLED TEXT AND NOT TO BE FILED WITH THIS ITEM UNRESOLVED: lines 441 to 442 fold the per-query totals into the combined totals by maximum. Whether the maximum is the intended combined count is undecided. The class documentation describes fusing per-query result sets, whose match count is the size of the union of the matches; the maximum equals the union only when the match sets nest, and the reference guide defines no {{numFound}} for combined queries. A two-query run showed the maximum passing the winning query's wrong total through unchanged and masking the losing query's error entirely, never amplifying it. The fix's expected numbers depend on this ruling, so it should be settled before the fix.]

Related: SOLR-14758, SOLR-15595 (the partial-results work this component shares with {{QueryComponent}}). The sibling defect in {{QueryComponent.mergeIds}}, which counts a skipped partial shard in the merged totals and whose {{approximateTotalHits}} fallback adds the running total, is covered by its own ticket, filed separately; that ticket names this component as a follow-up covering all three of its defects, and this ticket is that follow-up.

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
