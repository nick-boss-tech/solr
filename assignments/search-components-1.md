# Assignment: Search components round 1, sub-batch 1 of 4 (facets)

Claim first: add `claims/search-components-1.md`, then work. Report: `reports/search-components-1.md`. Drafts, for the tickets the audit finds draftable, go in `pr-drafts/search-components/` following `pr-formula.md`.

Staffing: run this round with 6 subagents in parallel, split by ticket cluster, with the lead writing the roll-up report. Hard cap from Nick (2026-10-09): never more than 6 subagents running at once across ALL assignments combined; subagents slow each other down past that. Run rounds one after another, not together.

## Scope

Eleven tickets, the facet family of the Search components section of `branch-focus-inventory-2026-10-08.md`: SOLR-5394, SOLR-6193, SOLR-6831, SOLR-10492, SOLR-10844, SOLR-11129, SOLR-12556, SOLR-15331, SOLR-16290, SOLR-17051, SOLR-18482. All branches are `solr-<ticket>-submit` on the fork.

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-5394: gate green at the live tip 967445622f9, SimpleFacetsTest 49 tests with 1 skipped, proof recorded; its report draft carries a Choice and Limits.
- SOLR-6193: gate green at the live tip ec94bf50c80, focused set 30 of 30, proof recorded. SCOPE DISPUTED: the premise audit and the round 35 review say the branch covers distributed facet.pivot merging while the ticket is about facet.field local params. Audit the scope question first; a draft follows the audit's answer, not the gate.
- SOLR-6831: gate green at the live tip 96b33ba5f6f, TestQueryLimits 4 of 4, proof recorded.
- SOLR-10492: gate green at the live tip ecf21e2192c, focused tests 20 of 20, proof recorded; the group.field fallback edge belongs in the description, per the receipt.
- SOLR-10844: gate green at the live tip e87515c56d0, SimpleFacetsTest 49 tests with 1 skipped, premise matrix recorded; the receipt names the Choices candidate and Limits lines the description owes.
- SOLR-11129: gate green at the live tip 6c1356bdff7, focused tests 3 of 3, proof recorded; the receipt's scope note (the coordinator now honors the other per-field local params too, while the changelog names only facet.mincount) belongs in the draft.
- SOLR-12556: gate green at the live tip 033ec65a0e1, TestJsonFacetRefinement 13 tests with 1 skipped, proof recorded; the receipt names the Limits (buckets first seen during refinement) and the changelog sentence that is true only for buckets known before refinement.
- SOLR-15331: gate green at the live tip 6769b4cd8a1, NestableJsonFacetTest 2 of 2. Proof INCONCLUSIVE by construction; no draft may state a proof pass for it.
- SOLR-16290: HELD gated pin at the live tip ff760120c81, test only. Audit only; its disposition (pin PR, fund the real fix, or bank) is the owner's call already on record. Do not draft it.
- SOLR-17051: gate green at the live tip 148544e9ed5, TestJsonFacets 30 of 30, proof recorded as PASS with no failure shape in the record.
- SOLR-18482: live PR (apache/solr #5009) at 49ca9099d8e. NO full gate at the tip is recorded; a confirmation run at the tip is green. Consistency pass only: branch, receipt, and the live PR description should agree. Do not draft new PR text; flag any description drift.

## Interactions to check

- SOLR-5394, SOLR-10492 and SOLR-10844 all change SimpleFacets.java or its test class: state the landing order and any hunk overlap.
- SOLR-6193 (PivotFacet) and SOLR-11129 (FacetComponent) both change how the coordinator treats per-field facet parameters in distributed search; check the two diffs for overlapping merge logic.
- SOLR-12556 (facet refinement mergers), SOLR-17051 (FacetFieldMerger and FacetFieldProcessor) and SOLR-15331 (SolrJ facet response parsing) sit on the JSON facet path at three different layers; note where a claim in one draft depends on another layer's behavior.
- SOLR-16290's pinned failure is in the same JSON facet domain machinery (exclusion recompute) as SOLR-12556's refinement area; note the relation in the report without auditing 16290 past its held state.

## The audit

Per ticket: read the Jira ticket, the branch diff against its base, and the receipt. Verify the recorded state against the branch (a moved tip or a diff that differs from the record gets flagged, not silently adopted). For the gated tickets, judge PR readiness: premise, scope, changelog entry, and what the description must state or avoid claiming. Follow the standing rules: no new Jira ticket unless someone asks; a needed follow-up is named in Limits with a stated plan to submit it; public text carries no internal process vocabulary; Proof states plainly what ran, at which head, what passed, and that the new test fails without the fix. Where a draft must name a Lucene version behavior, check it against both the 9.x and 10.x lines.

## Deliverables

1. `reports/search-components-1.md`: per-ticket verdict (draftable, held with reason, consistency pass result for the live PR, or owner decision with the options stated plainly), plus any disagreement with the receipts.
2. Drafts in `pr-drafts/search-components/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. Owner decisions go in the report as a short list at the end.
