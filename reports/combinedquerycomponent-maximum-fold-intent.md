# Report: CombinedQueryComponent maximum fold, intent

Assignment: assignments/pool-combinedquerycomponent-maximum-fold-intent.md
Claim: claims/pool-combinedquerycomponent-maximum-fold-intent.md (not edited here)
Ref: Apache Solr main d76fc14b9857e77b830d7a82514b28c523a239c2
Method: read-only git reads (show, blame, log, grep) from the worktree wt/pr-prepare-suggester, plus reads of the premise report and the ticket draft. No build, test or gate was run. Nothing was sent to GitHub, Jira, email or a comment. Line numbers are at the ref unless marked otherwise. Component path: solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java (abbreviated CQC below).

## Summary

- The maximum at CQC lines 441 and 442 was written with the component (SOLR-17319, commit 42cd889e8f0a). No file in the repository states what a combined count is meant to be.
- The same commit's QueryAndResponseCombiner.simpleCombine also takes a maximum of sub-query matches and labels its total GREATER_THAN_OR_EQUAL_TO. Since SOLR-18195 it floors that total at the number of distinct returned documents. The mergeIds numFound has no such floor, and it is the value SolrJ clients read.
- From the code (not run): two disjoint sub-queries of two hits each return four documents, and SolrJ sees numFound 2.
- The exactness fold in the draft (fix 3) cannot be fixed on its own. Once it is corrected, an all-exact multi-query response reports numFoundExact=true for a maximum that is not the count of distinct matches. The count must be decided first.

## Q1. Origin

- Blame on CQC: lines 330-331 (per-query accumulators), 342-343 (per-query numFound and exactness fold), 384-386 (approximate block), 434 (numFound--), 441-442 (the max folds), 445-449 (hits log, numFound, numFoundExact): all 42cd889e8f0a.
- 42cd889e8f0a is "SOLR-17319 : New Combined Query / hybrid search (RRF) (#3418)", by Sonu Sharma, 2025-12-19, with co-authors Christine Poerschke and David Smiley. Its message describes the new CombinedQuerySearchHandler, the RRF combiner and a QueryComponent refactoring "to enable a subclass to customize merging shard results". It says nothing about totals.
- Later commits changed the file but not the fold or the accumulation (blame shows no line of 330-331, 342-343, 384-386, 434 or 441-442 from a later commit): ef75ab82a5a8 (SOLR-18195, collapse, 2026-04-18) changed lines 322-325 and 408-410; 4a3bfa598e3 (SOLR-18290, shards.rows, 2026-06-26) changed lines 326 and 333.
- QueryAndResponseCombiner.java: lines 82 and 85 (totalMatches as a maximum), 92 (union of sub-query DocSets), 126 (TotalHits GREATER_THAN_OR_EQUAL_TO) are from 42cd889e8f0a. Line 124, the floor Math.max(combinedResultsLength, totalMatches - removedByCollapse), is from ef75ab82a5a8.
- Ticket text: research/jira-context has no SOLR-17319 packet (a Glob for SOLR-1731* found only SOLR-17311.json). This subagent has no Jira access, so the SOLR-17319 text and the PR #3418 review thread were not read. Neither is in the repository.

## Q2. Documentation

- Reference guide, query-guide/pages/json-combined-query-dsl.adoc, lines 20-21: "The Combined Query feature aims to execute multiple queries of multiple kinds across multiple shards of a collection and combine their result basis an algorithm (like Reciprocal Rank Fusion)." The page has no numFound, approximateTotalHits or union wording anywhere.
- Class javadoc, CQC lines 67-76: "Extends QueryComponent with support for executing multiple queries and combining their results, like hybrid search with RRF. Does nothing special if not activated. When activated, this component only works in distributed search / coordinator mode." It says nothing about union, counts or totals.
- Correction to the premise report (section 4, line 75) and to the ticket draft (line 68): both attribute to this javadoc the statement that a fused numFound is the size of the union. The javadoc does not say that. The nearest union wording is the javadoc of QueryAndResponseCombiner.simpleCombine, line 66: "Combine query result list as a union, optionally deduplicating by a collapse field." That sentence describes the result list.
- The only count contract in the guide is in common-query-parameters.adoc, minExactCount section, lines 581-593: "The numFoundExact boolean attribute is included in all responses, indicating if the numFound value is exact or an approximation. If it's an approximation, the real number of hits for the query is guaranteed to be greater or equal numFound." This makes an approximate numFound a lower bound on the real hit count.
- approximateTotalHits is not mentioned anywhere in solr-ref-guide. The maxHitsAllowed section (common-query-parameters.adoc, lines 409-426) documents only maxHitsTerminatedEarly and says the limit applies per shard.
- No design document in the repository discusses combined totals beyond the commit messages above.

## Q3. Existing tests

- CombinedQueryComponentTest.testMultipleLexicalQueryWithDebug (method line 122, assertion line 136) and testCombinerPlugin (method line 185, assertion line 199): two sub-queries, asserts numFound 10. Inferred, not run: both sub-queries match all ten documents (df is text at solrconfig-combined-query.xml line 175, no q.op so the default is OR, and every text field reads "test text for doc N"). So union = max = 10 and sum = 20. These tests exclude the sum but cannot tell the maximum from the union.
- CombinedQueryComponentTest.testFacetWithSmallMatchesAcrossSegments (method line 211): two disjoint sub-queries, id:(1 OR 2) and id:(3 OR 4). It asserts four documents returned and facet counts [blue (2), red (2)], which is the union of four documents. It does not assert numFound. From the code, numFound here is 2 (the maximum), so this is the one existing case where the maximum and the union differ, and nothing checks it.
- DistributedCombinedQueryComponentTest has several two-sub-query cases with overlapping id sets (for example lexical2 "id:(4^=1 OR 5^=3 OR 7^=4 OR 10^=2)" at line 156 and again at 175, 195, 212, 230, 248, 294). They assert ids and facets, and no numFound.
- DistributedCombinedQueryComponentTest line 281 and CombinedQuerySolrCloudTest line 286 assert numFound 4, but both use a single sub-query, so every fold gives 4.
- No combined-query test asserts approximateTotalHits or numFoundExact. A grep of the four combined test classes and of the combine/ tests (ReciprocalRankFusionTest, TestCombiner) for numFound, approximate, maxHits and partialResults found only the lines above.

## Q4. Consumers

Path on a combined request (reading, not run):

1. QueryComponent.handleResponses (line 660) calls mergeIds at line 685, which is the CQC override. It computes numFound (441), approximateTotalHits (442), logs "hits" (445), sets numFound and numFoundExact on a new SolrDocumentList (448-449) and stores it as the response docs (453).
2. prepareCombinedResponseBuilder, called from process, adds a BasicResultContext at line 253. That context's DocList comes from simpleCombine, whose matches are Math.max(combinedResultsLength, max sub-query matches - removedByCollapse) (QueryAndResponseCombiner lines 85 and 124). It logs "hits" again at line 258.
3. CQC does not override finishStage. QueryComponent.finishStage at STAGE_GET_FIELDS calls regularFinishStage (lines 754-768), which adds the mergeIds SolrDocumentList (line 766). SolrQueryResponse.addResponse appends (lines 277-279), so the response values hold two entries named "response": the BasicResultContext first and the SolrDocumentList second.
4. SolrJ QueryResponse.setResponse assigns _results for every "response" entry (QueryResponse.java lines 120-122), so the last one wins. A SolrJ client therefore sees the mergeIds maximum as numFound and numFoundExact. Readers that use rsp.getResponse(), which returns the first entry (TabularResponseWriter line 53, CSVResponseWriter line 369), see the simpleCombine value. JavaBinResponseWriter line 142 writes ctx.getDocList().matches() for a result context.

Downstream readers of the totals:

- Response numFound and numFoundExact: as above. The mergeIds value is the one SolrJ reads.
- The "hits" log holds two entries, the mergeIds maximum and the simpleCombine value. SpellCheckComponent reads the first entry (line 172) or ResponseBuilder.getNumberDocumentsFound (line 175). Which entry comes first was not traced.
- SpellCheckComponent decisions: suggestions are computed only when hits <= maxResultsForSuggest (line 183), and isCorrectlySpelled is hits > maxResultsForSuggest, or > 0 when unset (lines 416-417). Any change of fold moves these thresholds.
- approximateTotalHits header: written only when some shard flagged maxHitsTerminatedEarly and the value is above 0 (CQC lines 479-487). A different fold changes the number, not the header rules.
- shards.info: per-query, per-shard numFound (CQC lines 547-548) is not folded. It is the only place a client sees per-query totals.
- Facets: FacetComponent line 267 uses rb.getResults().docSet, which is the simpleCombine union of sub-query DocSets (QueryAndResponseCombiner line 92). Facet counts already use union semantics, so today numFound and the facet total can disagree.
- Documents, order and scores: computeResultIdsWithCombiner does not read the totals. RRF does not read them either (no numFound in combine/). The fold changes no document, order or score.

Observable behaviour beyond the number:

(a) numFound below the returned documents. A maximum can be smaller than the number of distinct documents on the page. A sum and a union cannot be. The simpleCombine floor (line 124) exists for the BasicResultContext path, and mergeIds has no equivalent. Inferred for testFacetWithSmallMatchesAcrossSegments: SolrJ numFound 2 with four documents returned.

(b) Exactness coupling. The current fold is inverted (premise, verified: line 343 keeps the flag true only while shards report FALSE). So all-exact multi-query input currently reports numFoundExact=false, and inexact input reports true. The draft's fix 3 (`!Boolean.FALSE.equals(...)`) would make all-exact multi-query input report true, for a maximum that is a lower bound of the union whenever sub-query sets overlap. That contradicts the guide's contract (common-query-parameters.adoc lines 592-593). The flag and the count must be ruled together.

(c) Under the current inverted flag the lower-bound reading is accidentally consistent on exact input (numFoundExact=false for a lower bound). That consistency disappears once the flag is corrected without a decision on the count.

(d) Union availability. simpleCombine already builds combinedDocSet as a union of the sub-query DocSets (line 92), and its size would be the exact union wherever the DocSets are populated. The facet test relies on that. Whether a plain request populates them was not traced.

## Q5. The sibling: QueryComponent.mergeIds

- Lines 1160-1165: the approximate fallback adds numFound before the current shard is folded in at line 1171. This is the sibling's own error, as the premise says.
- Line 1171: numFound += docs.getNumFound(), a sum across shards. Lines 1173-1175 are the correct exactness fold. Line 1232 logs "hits". setResponseDocs at line 1304 sets numFound and numFoundExact.
- Origin of the approximate block: 900bf3db869 (Siju Varghese, SOLR-17447, 2025-04-10, "Support early terminating a search based on maxHitsAllowed per shard"). Origin of the numFound sum: blame attributes line 1171 to c99af207c761 (a 2022 reformat). The sum is older: git log -S finds it present at the 2016 git import db22b4d1f71. Earlier SVN history was not traced.
- Why the sum is right there: shards hold disjoint documents for one query, so the sum of per-shard counts is that query's total, with no double counting. The approximate total sums the shard values in the same way.
- Why the combined component uses a maximum: its sub-queries are different queries over the same documents, so their match sets can overlap. A sum overcounts, a maximum undercounts, and the union is the count. simpleCombine, from the same origin commit, follows the same shape: maximum of sub-query matches, relation GREATER_THAN_OR_EQUAL_TO.
- Deliberate or accidental: the shape difference reads as deliberate, because the data model explains it and simpleCombine labels its maximum as a lower bound from the start. Applying the maximum to numFound in mergeIds is at least partly accidental. The mergeIds totals block looks like the sibling loop with the accumulation swapped for per-query variables and a maximum. The numFound-- at line 434 is a sum-era decrement for dropped duplicates, and it now lowers a running maximum rather than the query's own total. The exactness fold is the inverted form that QueryComponent does not have. The response floor from simpleCombine is absent. Nothing in the history discusses this block as a whole. This is a reading of the code, not a finding from a commit message.

## RECOMMENDATION

Outcome: pose it as an open question for the maintainer (the third outcome). The bracketed item at ticket draft line 68 becomes the question below. Keep the draft's exactness fix out of the filed text until the question is answered.

Why not "keep the maximum as intended": no source states a combined count. Keeping it as a stated property would also require stating it is a lower bound, adding the floor, and setting numFoundExact to false for multi-query responses. That is a change, not a keep.

Why not "fourth defect" yet: the fix needs a semantics the repository does not settle. A distinct-document union (the combinedDocSet already exists, and the facet total already uses it) and a lower bound with a floor (the simpleCombine labelling) both have support in the code. A fix chosen now would guess the intent.

Proposed wording for the bracketed item:

[OPEN QUESTION FOR THE MAINTAINER: For a combined query with two or more sub-queries, the response numFound is currently the largest single sub-query match count. Sub-queries can match overlapping documents, so this number can be smaller than the number of documents the response returns. For example, two disjoint sub-queries with two hits each return four documents while numFound is 2. Which count should numFound report: (a) the number of distinct documents that match any sub-query, or (b) a lower bound such as the largest single sub-query count, floored at the number of distinct documents returned? Whichever is chosen, should numFoundExact be false whenever more than one sub-query contributes? The answer sets the expected numbers for the fix and decides whether the exactness fix can land with it.]

Confidence:
- The repository cannot settle the intended semantics: high (about 85 percent).
- The current maximum is not a complete design as written (numFound below the returned count, exactness claimed for an overlapping maximum): about 75 percent.
- The maximum is a deliberate lower-bound choice rather than an accident: about 50 to 55 percent. simpleCombine favours deliberate; the mergeIds copy favours accidental.

Evidence that would change this:
- A SOLR-17319 or PR #3418 statement of intended numFound. If "lower bound, with a floor", move to keep: state the lower-bound property, add the floor and numFoundExact=false for multi-query, and call the floor a defect if it is missing. If "distinct count", move to fourth defect with the union fix (combinedDocSet size or a dedup count) and the exactness fix in the same change.
- A run of testFacetWithSmallMatchesAcrossSegments with a numFound assertion. If SolrJ shows 2 with four documents, the below-returned-count defect is confirmed whichever semantics is chosen.
- A run showing the SolrJ client receives the simpleCombine value rather than the mergeIds value. That would change Q4 and lessen the weight of the mergeIds maximum.
- Evidence that per-query DocSets are not populated for plain requests. That would make the union option more expensive and tilt toward the lower-bound reading.
- Any test or document in the repository stating the combined count. None was found.

## Notes for the draft and the premise report (no edits made here)

- Ticket draft line 68 and premise report section 4 (line 75) cite the class javadoc for the union statement. Replace that citation with the simpleCombine javadoc (QueryAndResponseCombiner line 66), which describes the result list only, or drop the union sentence.
- Draft line 68 is otherwise accurate: the reference guide defines no numFound for combined queries.
- Draft fix 3 (line 64) should not be filed without the count decision (Q4 b).

## Not verified

- No run: the wire form of the duplicate "response" entries, which "hits" entry SpellCheck reads, whether plain requests populate per-query DocSets, and how per-query DocLists are built on the distributed sub-builder path.
- Whether the two-sub-query tests match all ten documents (inferred from data and configuration).
- SOLR-17319 Jira text and the PR #3418 discussion.
- Claim and heartbeat: not touched. The coordinator marks the claim DONE with the deliverable.
