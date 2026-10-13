# Prepared live body for PR #5117 (SOLR-18541)

Status: NOT READY TO APPLY. The body below has one HOLD bracket in the Proof (search for "HOLD"). Remove each HOLD and its sentence, or fill it from a recorded result, before applying.

- Head this text describes: a93f9f6a997e748514cf2575a6e94c5f1c01f85e (the live PR head on 2026-10-13).
- Check first: PR #5117 had no issue comments, reviews or review comments at the check on 2026-10-13. If a maintainer has commented since, stop and ask Nick first.
- Apply only with Nick's explicit go-ahead: the body text is everything below the line of dashes. Save it to a file and send it with the REST endpoint (for example `gh api -X PATCH repos/apache/solr/pulls/5117 -F body=@<file>`, using the workspace wrapper that holds the token). `gh pr edit` fails on this token's scopes.
- After applying: read the body back and compare it with this text.
- The draft this text came from is pr-drafts/search-components/SOLR-18541.md on pr-prepare; its staging notes list each change with its record.

------------------------------------------------------------

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18541

## What happens today

**Combined queries report totals that count shards the merge dropped, double-count shards that send no approximateTotalHits header, and invert the exactness flag.**

In `CombinedQueryComponent.mergeIds`, a shard's totals are folded into its query's totals before the partial-results skip is tested. At the merge-base, the numFound and exactness folds run at [lines 342 to 343](https://github.com/nick-boss-tech/solr/blob/597ed67740831566b708b793f52554b2e49e7e17/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L342-L343) and the approximate total block at [lines 381 to 387](https://github.com/nick-boss-tech/solr/blob/597ed67740831566b708b793f52554b2e49e7e17/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L381-L387), while the skip is only tested at [lines 399 to 400](https://github.com/nick-boss-tech/solr/blob/597ed67740831566b708b793f52554b2e49e7e17/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L399-L400). A partial shard that is skipped for a query has already added its hits to that query: with fabricated shard responses, the merged numFound was 7 where 3 documents were merged, and approximateTotalHits was 15 instead of 5. When a shard sends no approximateTotalHits header, the fallback adds the running merged total, which already includes that shard, so two shards with 2 and 3 documents merge to an approximate total of 7 instead of 5. The exactness fold keeps the merged flag true only while shards report their counts as not exact, so an all-exact merge reports numFoundExact as false and an all-inexact merge reports it as true.

## What this change does

**The skip is tested before any total is folded in, the fallback adds the shard's own count, the exactness fold takes the form QueryComponent uses, and any skipped shard clears the merged numFoundExact flag.**

The skip now sits above the totals in the same (query, shard) iteration, at [lines 389 to 411](https://github.com/nick-boss-tech/solr/blob/a93f9f6a997e748514cf2575a6e94c5f1c01f85e/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L389-L411). A skipped shard contributes nothing to that query's numFound or approximateTotalHits. It still counts toward partialResults, segmentTerminatedEarly and maxHitsTerminatedEarly. It also clears numFoundExact, whatever count it sent, because a shard that stopped early can hold matches it did not report. A shard with no approximateTotalHits header adds its own per-query count. The exactness fold starts from true and is cleared when a contributing shard reports its count as not exact. How documents are merged, and how the per-query totals combine across sub-queries, do not change.

## Proof

**The seven tests from the first version fail on the base code with the wrong totals above, and all ten pass with this change.**

`CombinedQueryComponentMergeTotalsTest` drives fabricated shard responses through mergeIds. On the base production code, the seven tests from the first version fail: numFound 7 instead of 3, approximateTotalHits 15 instead of 5, 7 instead of 5 and 21 instead of 11, and numFoundExact inverted in both directions. The three skip tests added later check the exactness rule. With the production code reverted to the first head, the hits case fails with numFoundExact expected false and found true. [HOLD: the no-count and no-hits tests have no recorded fail-before at this head. Add the observed failure for each, or delete this bracket and the sentence before it.] With this change, `CombinedQueryComponentMergeTotalsTest` passes 10 of 10 at head a93f9f6a997e, verified 2026-10-13.

## A choice to check

**This change keeps the current rule that the combined numFound is the largest single sub-query count. The alternative is to count the distinct documents that match any sub-query.**

The per-query totals still combine by maximum, so numFound can be smaller than the number of distinct documents the sub-queries match together: two disjoint sub-queries with two hits each return four documents while numFound is 2. With the exactness fix, an all-exact response from those sub-queries now reports numFoundExact true, where the old inverted fold reported false. The merged approximateTotalHits passes through the same maximum. In the two multi-query cases in the new test, per-query approximate totals of 5 and 11 merge to 11, and totals of 5 and 2 merge to 5; on the base code the fallback error inflated those merged values to 21 and 7. I kept the maximum because what numFound should mean for a combined query is a wider decision than the three defects in the ticket, and the new tests pin the current behavior so a change would be visible. Should numFound instead report the distinct documents that match any sub-query, and should numFoundExact be false whenever more than one sub-query contributes?

## Limits

**What this change does not settle is named here.**

The tests drive the merge with fabricated shard responses. The merge was also run, on the code before this change, against shard responses from a real timeAllowed exit. An exit during the first sub-query sends no per-query entry for it, and the merge folded nothing from that shard. An exit after the first sub-query sends a shorter per-query list, and the merge threw IndexOutOfBoundsException ("Index 1 out of bounds for length 1") at the unchecked list access ([lines 338 to 340](https://github.com/nick-boss-tech/solr/blob/a93f9f6a997e748514cf2575a6e94c5f1c01f85e/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L338-L340)). This change leaves those lines alone, so the same error is expected at this head. That run was not repeated at this head. A size check on the list would fix it, and I kept it out of this change. A shard with no per-query entry under a score-only sort could reach the document loop with no document list; no real exit produced that shape. For sorts on fields other than score, the skip rule applies to the shape a first-sub-query exit produces. That shape is tested with fabricated responses only. The fallback and exactness defects need no partial results at all. A follow-up ticket and PR for the list check can be opened on request. What numFoundExact should report when several sub-queries contribute is part of the choice above; this change settles the single-sub-query case.

Changelog: [changelog/unreleased/SOLR-18541-combined-query-merge-totals.yml](https://github.com/nick-boss-tech/solr/blob/a93f9f6a997e748514cf2575a6e94c5f1c01f85e/changelog/unreleased/SOLR-18541-combined-query-merge-totals.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
