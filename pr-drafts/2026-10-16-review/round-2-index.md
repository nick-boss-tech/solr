# Round 2 index: PR text drafts (2026-10-16 review follow-ups)

Assignment: `assignments/pool-review-2026-10-16-text-round-2.md`. Claim: `claims/pool-review-2026-10-16-text-round-2.md` (vm4, 2026-10-18T04:27:57Z). Live PR bodies and heads read through the GitHub API on 2026-10-18 at about 04:33Z. No live PR was edited, no comment posted, no receipt edited.

Visible characters are counted as the body with link targets and markdown marks removed. This matches the assignment's own counts to within about 10 characters for items 1 to 3, the counts the assignment gave for the stored bodies.

| Item | PR | Head used | Status | Visible chars | Notes |
| --- | --- | --- | --- | --- | --- |
| 1 | 5130 (SOLR-4374) | 433e6c6ba990 | drafted | 3465 | Under 3,500. Catch-all and colon behavior change kept. |
| 2 | 5131 (SOLR-5394) | 74532d0f76a6 | drafted | 3456 | Under 3,500. Scope sentence adds BoolField. Draft does not say the head is gated. |
| 3 | 5132 (SOLR-6207) | 05cc2776d0c1 | drafted | 3480 | Under 3,500. Documentation-pin sentence added. Corroboration run not added. |
| 4 | 5116 (SOLR-18540) | 58e07b4571 (script commit 192aacaaf89) | drafted | 3868 | No length limit set. Linux run tied to 192aacaaf89. Date question escalated (below). |
| 5 | 5117 (SOLR-18541) | 81a88e40dd42 | drafted, re-draft trigger | 3483 | Under 3,500. Proof corrected to receipt (6 of 9 fail on base; gate 2026-10-16). Re-draft if code round 2 item 3 changes numFound or approximateTotalHits. |
| 6 | 5134 (SOLR-6831) | 4afed04987b2 (live head is 5c1124bd, see escalation) | drafted against receipt head | 3427 | Under 3,500. Limits sentence corrected. Distributed partialResults=false stated as one test, unconfirmed. |
| 7 | 5135 (SOLR-6975) | c666155ed058 | drafted, re-draft trigger | 3831 | No length limit set. Leading-key sentence replaced; grouping sentence limited to the single-key case. Re-draft if code round 1 item 3 changes ordering. |

## Items for the main agent

1. Item 6: the live PR head moved. PR #5134 is at `5c1124bdef95f4fa1d5bbbb6cf2dfd495111f528` (commit 2026-10-17 06:39:46 UTC, "SOLR-6831: withhold pivot data when a shard refinement answer is omitted"). That commit changes `FacetComponent.java` and `TestQueryLimits.java` on top of `4afed04987b2`. No receipt or gate file covers it. The stored body cited "9 of 9 at 5c1124bd", which has no receipt. The draft is written against `4afed049`, the head the assignment and the receipt name. Decide which head the body should name once `5c1124bd` is gated.
2. Item 6: the stored body's Limits sentence, "so the early stop does not apply there", is wrong at `4afed049`. In `FacetComponent.refinePivotFacets` (lines 1033 to 1067 at `4afed049`), an omitted shard answer counts as incomplete, the same as a partial one. The draft corrects this.
3. Item 4: the receipt `receipts/SOLR-18540.md` dates the Linux run 2026-10-12 on head `58e07b4571`. The commit `58e07b4571` is dated 2026-10-13 04:40:35 UTC. The `gradlew` blob is identical at `192aacaaf89` and `58e07b4571`, which the draft relies on. The draft keeps the receipt's run date and names `192aacaaf89` as the script tested. If the receipt is the wrong document, escalate; the receipt was not edited.
4. Items 5 and 7: the re-draft triggers are open. `assignments/pool-review-2026-10-16-code-round-2.md` is HANDED to vm1 and has not run, so numFound and approximateTotalHits are drafted as the code stands. `assignments/pool-review-2026-10-16-code-round-1.md` item 3 has not landed, so the ordering sentence in item 7 is drafted as the code stands.
5. Item 7: the comparator reasoning was read from `ShardFieldSortedHitQueue.java` at `c666155` (`lessThan` at lines 82 to 111, the DOC comparator at lines 123 to 126). It was not run with a multi-key request.
6. Item 5: `QueryAndResponseCombiner.simpleCombine` takes `Math.max` over the sub-query counts (checked at `81a88e40dd`, `QueryAndResponseCombiner.java` around line 85). The Limits note says so.

## Not in this round

- `assignments/pool-merged-pr-followup-promises-round-1.md` was not claimed. Its last pass by vm5 is DONE (03:55Z today). The newest merged `apache/solr` PR by `nick-boss-tech` is #5027 (2026-10-17T00:24Z), so there is no new merge to audit.
- `assignments/pool-retire-round-final-reports-round-1.md` was not claimed. Its prior claim body reads DONE (2026-10-15), and commit `6e0910c561` holds the nine packs and the index. That commit is not an ancestor of `origin/coordination`, and `reports/retire-final-*.md` are absent from the tip. The coordinator should reconcile this.
