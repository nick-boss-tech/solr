# Assignment: Search components round 1, sub-batch 2 of 4 (handler components)

Claim first: add `claims/search-components-2.md`, then work. Report: `reports/search-components-2.md`. Drafts, for the tickets the audit finds draftable, go in `pr-drafts/search-components/` following `pr-formula.md`.

Staffing: run this round with 6 subagents in parallel, split by ticket cluster, with the lead writing the roll-up report. Hard cap from Nick (2026-10-09): never more than 6 subagents running at once across ALL assignments combined; subagents slow each other down past that. Run rounds one after another, not together.

## Scope

Twenty-one tickets from the Search components section of `branch-focus-inventory-2026-10-08.md`: SOLR-3044, SOLR-6759, SOLR-6975, SOLR-7550, SOLR-8009, SOLR-8020, SOLR-8767, SOLR-8939, SOLR-8954, SOLR-9124, SOLR-10305, SOLR-11470, SOLR-13568, SOLR-13876, SOLR-14451, SOLR-15018, SOLR-17055, SOLR-17539, SOLR-17748, SOLR-17976, SOLR-18109. All branches are `solr-<ticket>-submit` on the fork.

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-3044: PARKED at the live tip 04b877e9de1, NO GATE. Audit only; its production hunks are already on main via SOLR-18373 and its retarget question is the owner's call. Do not draft it.
- SOLR-6759: NO GATE, awaiting pipeline at 62974ef8d18, premise unverified. Audit only.
- SOLR-6975: gate green at the live tip 761aa629bb8, DistributedQueryComponentOptimizationTest 10 of 10, proof grounded; the receipt names the Choices candidate and the Limits line the description owes.
- SOLR-7550: gate green at the live tip 687165651f9, PeerSync counts 1 of 1 and 2 of 2; proof INCONCLUSIVE by construction, and the generic-500 trade-off is an owner-level decision to make explicit, per the receipt.
- SOLR-8009: gate green at the live tip 8795661ddc9, focused counts 10, 4, 2 and 1, proof recorded; the receipt names the fan-out behavior and the SOLR-8954 duplicate-result note for the draft.
- SOLR-8020: gate green at the live tip 79f790523d2, ComponentStageLimitsTest 5 of 5, proof discriminating; no Choices section is owed, per the receipt.
- SOLR-8767: gate green at the live tip 3b5f2d23573, TestRealTimeGet 7 of 7, proof PASS as recorded; the /get response-shape change needs an explicit owner decision and user-facing description, per the receipt.
- SOLR-8939: gate green at the live tip a855a2d8965, counts 2 of 2 and 2 of 2, proof grounded; unit-level proof only, per the receipt.
- SOLR-8954: gate green at the live tip 1d981abe700, counts 4, 2 and 4, proof recorded, with the receipt's Limits lines.
- SOLR-9124: NO GATE, awaiting pipeline at 101e12d2085, premise unverified. Audit only.
- SOLR-10305: NO GATE, awaiting pipeline at c21ca8c0e75, premise unverified, and no record supplies its topic. Audit only.
- SOLR-11470: gate green at the live tip f44c294da37, 126 of 126 focused tests; the Proof must rest on the added {!bool} parser test, not the shipped lucene test, per the receipt.
- SOLR-13568: live PR (apache/solr #5014) at ac5d60c214c, gate green there, TestExpandComponent 9 of 9, proof PASS. Consistency pass only: branch, receipt, and the live PR description should agree. Do not draft new PR text; flag any description drift.
- SOLR-13876: gate green at the live tip 2e110473dba, TestExpandComponent 9 of 9; the slice-local versus group-wide maxScore question is the owner's call, posed as the Choice with slice-local implemented.
- SOLR-14451: gate green at the live tip e60891d8716, counts 7 and 7, proof recorded; the option-semantics question stays the owner's call.
- SOLR-15018: gate green at the live tip d0f29b4630c, counts 1, 15 and 4, proof recorded; the head is a test-only strengthening on the earlier gated head, both stated in the receipt.
- SOLR-17055: NO GATE, awaiting pipeline at f0c401a290b, premise unverified. Audit only.
- SOLR-17539: live PR (apache/solr #4998); gate green at bce505f45ac, and the live tip f9d201a278b is one docs-only commit past it, with GitHub runs successful at both heads. Consistency pass only, and flag the head difference rather than treating either head as wrong. The round 9 review's four description-wording findings are a main-side item; note them, do not re-derive them.
- SOLR-17748: gate green at the live tip dfacaf34766, counts 4 and 1, proof recorded; the receipt names the wider guards note and the behavior change to state plainly.
- SOLR-17976: gate green at the live tip 56ea43c448e, focused counts 2, 1, 6, 9 and 3, proof recorded, with the receipt's PR notes on the tie-order change and the new ShardDoc field.
- SOLR-18109: gate green at the live tip b19395e1f60, counts 7 of 7 and 3 of 3, test only, proof skipped by construction as recorded.

## Interactions to check

- SOLR-8009, SOLR-8767, SOLR-8954 and SOLR-15018 all change RealTimeGetComponent.java or its request path: state the landing order and check the hunks against each other; 8009's receipt already names the duplicate-result behavior it inherits from 8954's path.
- SOLR-13568 and SOLR-13876 both change ExpandComponent.java and share TestExpandComponent: the two maxScore and caching behaviors must not contradict each other in the drafts; state which lands first.
- SOLR-8939 and SOLR-17748 both change QueryComponent.java (8939 the stored-fields request factory side, 17748 returnFields null handling); check for hunk overlap.
- SOLR-17976 changes CombinedQueryComponent.java and QueryComponent.java merge behavior; SOLR-3044's parked work touched the same files but is already on main, so note only whether 17976's diff assumes the modernized NamedList behavior.
- SOLR-11470 (rerank negative queries) neighbors sub-batch 4's SOLR-15479 and SOLR-11310 on the rerank path; note the relation in the report without auditing those tickets here.

## The audit

Per ticket: read the Jira ticket, the branch diff against its base, and the receipt. Verify the recorded state against the branch (a moved tip or a diff that differs from the record gets flagged, not silently adopted). For the gated tickets, judge PR readiness: premise, scope, changelog entry, and what the description must state or avoid claiming. Follow the standing rules: no new Jira ticket unless someone asks; a needed follow-up is named in Limits with a stated plan to submit it; public text carries no internal process vocabulary; Proof states plainly what ran, at which head, what passed, and that the new test fails without the fix. Where a draft must name a Lucene version behavior, check it against both the 9.x and 10.x lines.

## Deliverables

1. `reports/search-components-2.md`: per-ticket verdict (draftable, held with reason, audit-only result for the parked and no-gate tickets, consistency pass result for the two live PRs, or owner decision with the options stated plainly), plus any disagreement with the receipts.
2. Drafts in `pr-drafts/search-components/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. Owner decisions go in the report as a short list at the end.
