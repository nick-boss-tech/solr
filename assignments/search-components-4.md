# Assignment: Search components round 1, sub-batch 4 of 4 (search core, grouping, stats, REST and managed resources)

Claim first: add `claims/search-components-4.md`, then work. Report: `reports/search-components-4.md`. Drafts, for the tickets the audit finds draftable, go in `pr-drafts/search-components/` following `pr-formula.md`.

Staffing: run this round with 6 subagents in parallel, split by ticket cluster, with the lead writing the roll-up report.

## Scope

Twenty-two tickets from the Search components section of `branch-focus-inventory-2026-10-08.md`: SOLR-6207, SOLR-7520, SOLR-8051, SOLR-8088, SOLR-9595, SOLR-10694, SOLR-11310, SOLR-12044, SOLR-13851, SOLR-14381, SOLR-14931, SOLR-15144, SOLR-15319, SOLR-15479, SOLR-15895, SOLR-16444, SOLR-17155, SOLR-17372, SOLR-17791, SOLR-17841, SOLR-18196, SOLR-18506. All branches are `solr-<ticket>-submit` on the fork.

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-6207: gate green at the live tip b099a9f1be5, SolrQueryRequestBaseTest 1 of 1; a documentation pin with no fail-before by design, as the receipt states.
- SOLR-7520: gate green at the live tip 10b6931e1c0, focused counts 1, 1, 17 and 2, proof recorded; the coordinator gap goes in Limits with a follow-up offer, per the receipt.
- SOLR-8051: NO GATE, awaiting pipeline at 44588ce6719, premise unverified. Audit only; do not draft it.
- SOLR-8088: NO GATE, awaiting pipeline at 2398c9bea08, premise unverified, branch carries its TESTING.md. Audit only.
- SOLR-9595: NO GATE, awaiting pipeline at 7ff1350ab7b, premise unverified. Audit only.
- SOLR-10694: NO GATE, awaiting pipeline at 093d90c62de, premise unverified, branch carries its TESTING.md; the receipt also carries the tidy-only DRIFT flag for CSVResponseWriter.java.
- SOLR-11310: gate green at the live tip e38ddec5279, TestLTRReRankingPipeline 4 of 4 and TestReRankQParserPlugin 11 of 11, proof recorded twice; the receipt names the owner position to confirm before opening, posed as a Choice.
- SOLR-12044: NO GATE, awaiting pipeline at 6185b96e52a, premise unverified. Audit only.
- SOLR-13851: gated green as-is at the live tip b60f4d642d1, TestIndexSearcher 6 of 6, and SUBMISSION-HELD regardless. Audit only; do not draft it.
- SOLR-14381: gate green at the live tip a28b3f672cb, compat test 2 of 2 and parity test 9 of 9, proof recorded.
- SOLR-14931: gate green at the live tip 1a7d678d9a1, TestMacros 2 and TestShardMacroExpansion 1, proof recorded.
- SOLR-15144: gate green at the live tip 68b0fc31e05 on the unit record, but QUALIFIED: the round 35 end-to-end finding and its three owner options are in the receipt. Audit the options; a draft follows the owner's option, so present it as an owner decision, with option (a)'s corrected claims stated if the audit supports them.
- SOLR-15319: gate green at the live tip 4bda91f46fc, TestDistribIDF 4 of 4 and TestExactStatsCacheLegacyResponse 3 of 3, proof recorded; the dispatched GitHub runs' conclusions are not in the record and no draft may claim them.
- SOLR-15479: gate green at the live tip 57bd53ce4d0, TestReRankQParserPlugin 14 of 14, proof recorded; the maxScore window question is posed as a Choice, per the receipt.
- SOLR-15895: gate green at the live tip 02930909397, counts 2 of 2 and 5 of 5; proof INCONCLUSIVE as recorded, and no draft may state it as a pass.
- SOLR-16444: gate green at the live tip 8ffee94a5e7, counts 5 of 5 and 3 of 3, proof recorded; the dispatched GitHub run's conclusion is not in the record and no draft may claim it.
- SOLR-17155: gate green at the live tip 1413237f7a7, TopGroupsResultTransformerTest 1 of 1, proof recorded; GitHub evidence on record is at the pre-conversion head only, as the receipt states.
- SOLR-17372: GATE FAILED at the live tip 048862fda8a on premise non-reproduction; head legs all pass. Audit only; its three owner options are in the receipt. Do not draft it, and do not reuse the round 35 report's Proof claims.
- SOLR-17791: gate green at the live tip a39c1c97376, counts 2, 10 and 3, proof recorded end to end; the API shape call is deferred in the record and both options go in the PR text.
- SOLR-17841: gate green at the live tip 478731e6760, coverage only, proof not applicable by construction; RETIRE candidate with the retire call pending. Audit only; do not draft it.
- SOLR-18196: ALREADY MERGED via PR #4995 at f3248fe2310; the branch is redundant with main and a retire candidate. Audit only; do not draft it.
- SOLR-18506: live PR (apache/solr #5029) at 77c019e1ff0, gate green, test only. Consistency pass only: branch, receipt, and the live PR description should agree. The corrected Jira description v2 still awaits the owner's paste and v1 must not be used; flag any description drift, do not draft new PR text.

## Interactions to check

- SOLR-8051 and SOLR-15319 both change ExactStatsCache.java (8051 the global stats NPE, 15319 the distributed IDF keying): 15319 is gated and 8051 has no gate; the report should state how the two diffs compose and which should land first.
- SOLR-7520 and SOLR-14381 both work the grouping machinery (CommandHandler, Grouping, the shard result transformers); SOLR-17155's transformer test sits in the same serializer family as 14381's compat test. Check for overlapping hunks across the three.
- SOLR-11310 and SOLR-15479 share TestReRankQParserPlugin and the rerank path (ReRankCollector on one side, SolrIndexSearcher maxScore on the other); state the landing order.
- SOLR-15895, SOLR-16444 and SOLR-17791 all change the managed resources family (ManagedResourceStorage, RestManager, ManagedResource): check the three diffs against each other, since all three are gated and draftable candidates.
- SOLR-15144's option (b), widening to SearchHandler, would cross into sub-batch 2's handler components; note it in the report as a cross-batch consequence, do not audit SearchHandler here.

## The audit

Per ticket: read the Jira ticket, the branch diff against its base, and the receipt. Verify the recorded state against the branch (a moved tip or a diff that differs from the record gets flagged, not silently adopted). For the gated tickets, judge PR readiness: premise, scope, changelog entry, and what the description must state or avoid claiming. Follow the standing rules: no new Jira ticket unless someone asks; a needed follow-up is named in Limits with a stated plan to submit it; public text carries no internal process vocabulary; Proof states plainly what ran, at which head, what passed, and that the new test fails without the fix. Where a draft must name a Lucene version behavior, check it against both the 9.x and 10.x lines.

## Deliverables

1. `reports/search-components-4.md`: per-ticket verdict (draftable, held with reason, audit-only result for the no-gate, held, failed-gate and retire tickets, consistency pass result for the live PR, or owner decision with the options stated plainly), plus any disagreement with the receipts.
2. Drafts in `pr-drafts/search-components/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. Owner decisions go in the report as a short list at the end.
