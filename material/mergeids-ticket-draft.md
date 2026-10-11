# Draft Jira ticket: mergeIds counts a skipped partial shard in the merged totals, and the approximateTotalHits fallback adds the running total

Status: NOT filed. Updated 2026-10-11 after the fix branch review (reports/mergeids-fix-review.md): the fallback change is now described, the approximateTotalHits symptom and reproduction values are in, and CombinedQueryComponent is named as a follow-up. Nick already received the earlier text by email; he needs this updated text before filing.

Source: staged verbatim from the draft ticket in the main side's verification report (goal hidden_files/side-finding-mergeids-verification.md; verdict CONFIRMED BY RUN, 2026-10-06), verified against Apache Solr main snapshot cabedd1d968059215188f4e7563fb303241899ed.

The ticket text below the line is Jira wiki markup: the Title line is the ticket summary and the rest is the description.

---

Type: Bug
Title: QueryComponent.mergeIds counts a partial shard whose documents are skipped in the merged totals, and adds the running total as the approximateTotalHits fallback

🤖 _AI text below_ 🤖 _(posted on behalf of Nick Shanin)_

h2. Symptom

In a distributed search sorted on a field (so sort values need unmarshalling at merge time), when one shard answers with partial results and no {{sort_values}} (the {{timeAllowed}} / query-limit case that SOLR-14758 fixed for an NPE and SOLR-15595 narrowed), the merging node drops that shard's documents but still counts the shard in the merged totals. The response shows:

 * {{numFound}} larger than the number of documents reachable by paging, because it includes the skipped shard's hits
 * that same count reported with {{numFoundExact=true}}: the skipped shard's own count is exact, so nothing about the flag is wrong on the shard, but the total it qualifies counts a shard that contributed no documents
 * a {{maxScore}} that no returned document attains, when the skipped shard held the highest score
 * an {{approximateTotalHits}} that includes the skipped shard's approximate count, when the total is written to the response header (11 instead of 4 in the reproduction below)

The response header does carry {{partialResults=true}}, so callers know the result set is partial; nothing tells them the count is not the count of the merged result.

A second defect sits in the same accumulation. When a shard response carries no {{approximateTotalHits}} header, the merge falls back to adding {{numFound}}, but at that point {{numFound}} is the running total of the shards merged so far, not the current shard's count, which is only folded in afterwards. Two shards with 2 and 3 documents and no headers merge to an approximate total of 2 instead of 5.

h2. Mechanism

{{QueryComponent.mergeIds}} (main, {{solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java}}) folds each shard response into the merged totals before the partial-results skip:

 * {{approximateTotalHits}} is accumulated around line 1160
 * when a shard sends no {{approximateTotalHits}} header, the fallback at line 1162 adds {{numFound}}, the running merged total at that point, instead of the current shard's own count, which is only folded in at line 1171
 * {{maxScore}} is merged around line 1169
 * {{numFound += docs.getNumFound()}} at line 1171
 * the {{hitCountIsExact}} fold is at line 1174

The skip itself is at line 1193: {{if (thisResponseIsPartial && sortFieldValues.size() == 0 && needsUnmarshalling) continue;}}. The only downward adjustment, {{numFound--}} at line 1225, runs per document when the merge queue rejects one, so it never compensates for a shard that was skipped wholesale. {{setResultIdsAndResponseDocs}} then writes the accumulated values into the response, including {{setNumFoundExact(hitCountIsExact)}}.

The skipped shard does not rescue the exactness flag: in the SOLR-14758 scenario the timeout lands in {{doFieldSortValues}} after the shard's search has completed, so the shard's own count is exact and it sends {{numFoundExact=true}}; more generally the shard's exact flag comes from the DocList hit-count relation, which the partial-results state does not downgrade. So {{hitCountIsExact}} survives the merge.

The response flags are not totals and are not part of this defect: {{partialResults}} and the max-hits flag still fold in from every shard, including a skipped one. {{shards.info}} also still reports each shard's own totals, so its per-shard numbers do not sum to the merged totals.

h2. Reproduction

At the merge level, using the mock harness from {{QueryComponentPartialResultsTest}} (added by SOLR-15595) with realistic totals set on the fabricated shard lists (that test never sets {{numFound}}, which is why this was not visible): shard1 partial with no {{sort_values}}, 2 documents, {{numFound=2}}, {{numFoundExact=true}}, {{maxScore=99.0}}, an {{approximateTotalHits}} header of 7 and {{maxHitsTerminatedEarly=true}}; shard2 normal, 3 documents, {{numFound=3}}, {{numFoundExact=true}}, {{maxScore=3.0}}, an {{approximateTotalHits}} header of 4; sort on a field requiring unmarshalling. On main the merged list holds 3 documents (correct) with {{numFound=5}}, {{numFoundExact=true}}, {{maxScore=99.0}} and {{approximateTotalHits=11}}.

For the fallback: two shards with 2 and 3 documents, neither sending an {{approximateTotalHits}} header, one flagging {{maxHitsTerminatedEarly}}; on main the merged {{approximateTotalHits}} is 2 instead of 5.

End to end, the ingredients are the SOLR-14758 ones: a multi-shard collection, a sorted query, and a {{timeAllowed}} (or another query limit) that trips on one shard while its sort values are being derived.

h2. Fix options

Three smallest changes present themselves:

 * Test the skip condition before accumulating the shard's totals (equivalently, move the {{approximateTotalHits}} / {{maxScore}} / {{numFound}} / {{hitCountIsExact}} accumulation below the skip), so a skipped shard never enters the merged totals and the totals describe exactly the shards whose documents are merged.
 * Keep the totals, but set {{hitCountIsExact = false}} when a shard is skipped, so the response stops asserting the count is exact. That leaves {{numFound}} and {{maxScore}} counting the skipped shard, so it corrects only one of the symptoms above.
 * Correct the fallback: when a shard sends no {{approximateTotalHits}} header, add that shard's own count ({{docs.getNumFound()}}) instead of the running merged total.

The first change is the recommended one. Correcting the totals when the skip fires instead means subtracting the skipped shard's contribution after the fact. That can repair the counts, but it cannot correctly undo {{maxScore}}: a maximum cannot be decremented, so recovering the merged maximum without the skipped shard would need per-shard tracking and a recomputation, and {{hitCountIsExact}} would need recomputation as well. Testing the skip before accumulating leaves every total describing the same set of shards, so the reorder is the sound fix. The third change is independent of the skip and is part of the same fix: it is one line in the same accumulation, and without it the merged approximate total stays wrong whenever a shard sends no header.

Related: SOLR-14758, SOLR-15595. SOLR-15144 reports a different symptom in the same area (empty document list under {{timeAllowed}}) and does not appear to be the same mechanism. {{CombinedQueryComponent.mergeIds}} folds its per-query totals in before the same partial skip in the same way; that component is not covered here and is left as a follow-up.

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
