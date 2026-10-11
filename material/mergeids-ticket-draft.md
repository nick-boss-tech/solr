# Draft Jira ticket: mergeIds counts a skipped partial shard in the merged totals

Status: NOT filed. Draft for Nick to file once the review in assignments/pool-mergeids-ticket-review.md returns SATISFIED.

Source: staged verbatim from the draft ticket in the main side's verification report (goal hidden_files/side-finding-mergeids-verification.md; verdict CONFIRMED BY RUN, 2026-10-06), verified against Apache Solr main snapshot cabedd1d968059215188f4e7563fb303241899ed.

The ticket text below the line is Jira wiki markup: the Title line is the ticket summary and the rest is the description.

---

Type: Bug
Title: QueryComponent.mergeIds counts numFound and maxScore for a partial shard whose documents are skipped, and still reports numFoundExact=true

h2. Symptom

In a distributed search sorted on a field (so sort values need unmarshalling at merge time), when one shard answers with partial results and no {{sort_values}} (the {{timeAllowed}} / query-limit case fixed against an NPE by SOLR-14758 and narrowed by SOLR-15595), the merging node drops that shard's documents but still counts the shard in the merged totals. The response shows:

 * {{numFound}} larger than the number of documents reachable by paging, because it includes the skipped shard's hits
 * {{numFoundExact=true}} on that same response, even though the count includes a shard that contributed no documents
 * a {{maxScore}} that no returned document attains, when the skipped shard held the highest score

The response header does carry {{partialResults=true}}, so callers know the result set is partial; nothing tells them the count is not the count of the merged result.

h2. Mechanism

{{QueryComponent.mergeIds}} (main, {{solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java}}) folds each shard response into the merged totals before the partial-results skip:

 * {{approximateTotalHits}} is accumulated around line 1160
 * {{maxScore}} is merged around line 1169
 * {{numFound += docs.getNumFound()}} at line 1171
 * the {{hitCountIsExact}} fold is at line 1174

The skip itself is at line 1193: {{if (thisResponseIsPartial && sortFieldValues.size() == 0 && needsUnmarshalling) continue;}}. The only downward adjustment, {{numFound--}} at line 1225, runs per document when the merge queue rejects one, so it never compensates for a shard that was skipped wholesale. {{setResultIdsAndResponseDocs}} then writes the accumulated values into the response, including {{setNumFoundExact(hitCountIsExact)}}.

The skipped shard does not rescue the exactness flag: in the SOLR-14758 scenario the timeout lands in {{doFieldSortValues}} after the shard's search has completed, so the shard's own count is exact and it sends {{numFoundExact=true}}; more generally the shard's exact flag comes from the DocList hit-count relation, which the partial-results state does not downgrade. So {{hitCountIsExact}} survives the merge.

h2. Reproduction

At the merge level, using the mock harness from {{QueryComponentPartialResultsTest}} (added by SOLR-15595) with realistic totals set on the fabricated shard lists (that test never sets {{numFound}}, which is why this was not visible): shard1 partial with no {{sort_values}}, 2 documents, {{numFound=2}}, {{numFoundExact=true}}, {{maxScore=99.0}}; shard2 normal, 3 documents, {{numFound=3}}, {{maxScore=3.0}}; sort on a field requiring unmarshalling. On main the merged list holds 3 documents (correct) with {{numFound=5}}, {{numFoundExact=true}} and {{maxScore=99.0}}.

End to end, the ingredients are the SOLR-14758 ones: a multi-shard collection, a sorted query, and a {{timeAllowed}} (or another query limit) that trips on one shard while its sort values are being derived.

h2. Fix options

Two smallest changes present themselves:

 * Test the skip condition before accumulating the shard's totals (equivalently, move the {{numFound}} / {{maxScore}} / {{hitCountIsExact}} accumulation below the skip), so a skipped shard never enters the merged totals and the totals describe exactly the shards whose documents are merged.
 * Keep the totals, but set {{hitCountIsExact = false}} when a shard is skipped, so the response stops asserting the count is exact.

The first change is the recommended one. Correcting the totals when the skip fires instead means subtracting the skipped shard's contribution after the fact. That can repair the counts, but it cannot correctly undo {{maxScore}}: a maximum cannot be decremented, so recovering the merged maximum without the skipped shard would need per-shard tracking and a recomputation, and {{hitCountIsExact}} would need recomputation as well. Testing the skip before accumulating leaves every total describing the same set of shards, so the reorder is the sound fix.

Related: SOLR-14758, SOLR-15595. SOLR-15144 reports a different symptom in the same area (empty document list under {{timeAllowed}}) and does not appear to be the same mechanism.

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
