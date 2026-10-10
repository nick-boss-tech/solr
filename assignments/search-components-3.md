# Assignment: Search components round 1, sub-batch 3 of 4 (doc transformers, response writers, loaders and handlers)

Claim first: add `claims/search-components-3.md`, then work. Report: `reports/search-components-3.md`. Drafts, for the tickets the audit finds draftable, go in `pr-drafts/search-components/` following `pr-formula.md`.

Staffing: run this round with 6 subagents in parallel, split by ticket cluster, with the lead writing the roll-up report. Hard cap from Nick (2026-10-09): never more than 6 subagents running at once across ALL assignments combined; subagents slow each other down past that. Run rounds one after another, not together.

## Scope

Seventeen tickets from the Search components section of `branch-focus-inventory-2026-10-08.md`: SOLR-4374, SOLR-7390, SOLR-7498, SOLR-8003, SOLR-8240, SOLR-9148, SOLR-9396, SOLR-9864, SOLR-10424, SOLR-11153, SOLR-11364, SOLR-12543, SOLR-13245, SOLR-14678, SOLR-15041, SOLR-16155, SOLR-18356. All branches are `solr-<ticket>-submit` on the fork.

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-4374: gate green at the live tip 801c62290f7, ReturnFieldsTest 16 of 16, proof recorded.
- SOLR-7390: gate green at the live tip 7463dd006a7, ReturnFieldsTest 15 of 15, proof recorded; the gate ran at a received head whose code content is recorded as identical to the shipped head.
- SOLR-7498: gate green at the live tip 2050d8e447a, focused tests 10 total across three classes, proof recorded; the receipt names the PR notes (existing "null" values in indexes; metadata-level test).
- SOLR-8003: NO GATE, awaiting pipeline at f1c99a44961, premise unverified, tip adds only the handoff doc. Audit only; do not draft it.
- SOLR-8240: gate green at the live tip fad7a1dd8e2, JsonLoaderTest 32 of 32 and TestInitParams 7 of 7, proof recorded with the shipped test's one-line df correction noted.
- SOLR-9148: gate green at the live tip 30f0d7a42d5, TestSQLHandler 35 of 35; the proof rests on the previous head's gate and the live-tip run is a docs-only smoke, both stated in the receipt.
- SOLR-9396: gate green at the live tip a5ab2eda4e6, focused tests 17 of 17, proof recorded, with the receipt's Limits line.
- SOLR-9864: gate green at the live tip b5826e466b9, focused tests 27 of 27, proof recorded.
- SOLR-10424: gate green at the live tip e629ab8bb29 per the latest record, TechproductsJsonDocsParamsTest 1 of 1, premise re-proven after the conf/conf test defect was fixed. The receipt carries the full history; any draft's Proof must match the fixed test's record, not the original shipped claim.
- SOLR-11153: gate green at the live tip 093df0d65bd, focused tests 33 total, proof recorded against the converted test text.
- SOLR-11364: gate green at the live tip 562d3e7e685, focused run 1 of 1 at the tip with the wider 58-test battery at the earlier head, both stated in the receipt; the rebase-before-PR housekeeping is an owner item in the record.
- SOLR-12543: gate green at the live tip 88d236db6b7, TestExportHandlerHttpStatus 3 of 3 and TestExportWriter 19 of 19, proof recorded; the fix is partial and the draft must say so.
- SOLR-13245: gate green at the live tip 16e62ab6542, DaemonStreamApiTest 2 of 2, proof recorded; the scope question (node-local versus collection-wide) is the owner's call, posed as a Choice.
- SOLR-14678: gate green at the live tip 5d94e6cf398, TestChildDocTransformerHierarchy 18 of 18, proof recorded; the per-document versus request-global framing is an owner item in the record.
- SOLR-15041: gate green at the live tip 55fca0a7c24, TestCSVLoader 8 of 8; the record states the proof only as PASS with no premise count, so no draft may cite premise counts for it.
- SOLR-16155: gate green at the live tip 0881de1ed68 by a hybrid gate, but SUBMISSION FLAGGED: upstream PR #1151 covers this ticket with broader scope and the main record's verdict is do not open a PR of its own. Audit only; do not draft it. Whether anything is submitted is the owner's call.
- SOLR-18356: NO GATE, RETIRED as obsolete at c179cd35713; upstream already landed the identical removal. Audit only; the only question is the branch deletion, which is the owner's call. Do not draft it.

## Interactions to check

- SOLR-4374 and SOLR-7390 both change SolrReturnFields.java and share ReturnFieldsTest: state the landing order and check the hunks and the test file for overlap.
- SOLR-9396 (subquery transformer) and SOLR-14678 (child doc transformer) both sit on the DocTransformer machinery; SOLR-14678 also changes DocTransformer.java and DocTransformers.java themselves, so check SOLR-9396's assumptions against the changed base classes.
- SOLR-8240 (JsonLoader honors field mapping params) and SOLR-10424 (the techproducts params.json drops mapUniqueKeyOnly) work the same mapUniqueKeyOnly behavior from two sides; the report should state how the two changes compose.
- SOLR-11364 (useDocValuesAsStored) reads through SolrDocumentFetcher, which SOLR-16155's DocumentBuilder work neighbors; note any interaction without auditing 16155 past its flagged state.

## The audit

Per ticket: read the Jira ticket, the branch diff against its base, and the receipt. Verify the recorded state against the branch (a moved tip or a diff that differs from the record gets flagged, not silently adopted). For the gated tickets, judge PR readiness: premise, scope, changelog entry, and what the description must state or avoid claiming. Follow the standing rules: no new Jira ticket unless someone asks; a needed follow-up is named in Limits with a stated plan to submit it; public text carries no internal process vocabulary; Proof states plainly what ran, at which head, what passed, and that the new test fails without the fix. Where a draft must name a Lucene version behavior, check it against both the 9.x and 10.x lines.

## Deliverables

1. `reports/search-components-3.md`: per-ticket verdict (draftable, held with reason, audit-only result for the flagged and retired tickets, or owner decision with the options stated plainly), plus any disagreement with the receipts.
2. Drafts in `pr-drafts/search-components/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. Owner decisions go in the report as a short list at the end.
