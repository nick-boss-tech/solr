# Pool assignment: SOLR-17539 live PR body findings

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-solr-17539-body-findings.md.

## Purpose

SOLR-17539 is a live PR (apache/solr #4998, branch `solr-17539-submit`). Its round 9 review (2026-10-04, description against diff at head `bce505f45acd20f80686cb448e17e7c5717d6f25`) returned four description-wording findings and one optional test item. The findings were never applied to the body. This assignment verifies each finding against the current live body and the gate receipt, and produces the exact replacement text for each finding that still holds, so the main side can apply them under the standing description-edit authorization. The review record itself lives in the main side's goal files and is not on this branch; the findings are carried in full below.

Gate state, from `receipts/SOLR-17539.md`: GATE GREEN at `bce505f45acd` (TestDocValuesIteratorCache 2 of 2, SolrCoreTest 9 of 9, TestConfigOverlay 2 of 2, neighbor TestRealTimeGet 4 of 4). The live tip `f9d201a278b` is one docs-only commit past the gated head (ref-guide wording). The body was not edited by the gate.

## The five findings to verify

1. **The reason given for "no base proof" is partly wrong (finding 1).** The body says the tests "use the new constructor and accessor this PR adds". `DocValuesIteratorCache(SolrIndexSearcher, boolean cache)` already exists on upstream main with the Javadoc "if false, caching is disabled"; `ExportWriter` and the single-field real-time-get lookup already pass `false`. The PR adds the field, `isCaching()`, the fetcher accessor and the factory. The conclusion survives (the effect cannot be observed through the base API), but the sentence should name the accessors and the factory, not a constructor.
2. **Nothing shows the option reduces heap (finding 2).** The tests assert the mode flag and that values still come back. The ticket is a memory report (4 GB to 6 GB). No measurement, before or after, is in the Proof, and Limits does not say so. The fix is one sentence: the heap effect is inferred from the code, not measured.
3. **"Both places that build the cache" is not the full list (finding 3).** Upstream constructs the cache in five places. Two were already uncached (`ExportWriter.java:493`, `RealTimeGetComponent.java:585`), two are routed through the new factory, and `UpgradeCoreIndex.java:173` still builds a caching instance unconditionally, which the research record treats as deliberate. The body should say "the two request paths" and name `UpgradeCoreIndex` in Limits.
4. **The cost of turning it off is understated (finding 4).** Limits says iterators are re-created. With the cache off, every `getSupplier` call builds a new `FieldDocValuesSupplier`, which allocates four arrays sized to the number of segments, and the per-segment "this field is absent / below the first doc" shortcut is lost because it lives in those arrays. That is per field per document; for the reporter's shape (a wide glob, few fields populated per hit) the off setting pulls a fresh iterator for every field in the glob on every document. Not measured; the wording should match it.
5. **The Config API claim has no behavioral test (finding 5, optional).** `TestConfigOverlay` only checks that the property is editable. No test sets it through the Config API and observes the fetcher after reload. The solrconfig route is behaviorally covered by `testDisabledViaSolrConfig`; the Config API set-and-observe route is not.

## Method

1. Read the current live body of PR #4998 in full (GitHub API or `gh`), and record its head and the date read.
2. For each finding: quote the exact body passage it concerns, check the passage against the branch code at the live tip and the receipt above, and mark the finding STILL HOLDS or SETTLED (with the evidence: the passage no longer exists, the code moved, or the claim is already stated).
3. For each finding that still holds, give the exact replacement text for the passage, written to `pr-formula.md` (bold summary lines untouched unless the passage is one, plain language, no internal process vocabulary, citations at the head SHA for code the change produces and at the merge-base for pre-change code, saying so).
4. For finding 5, state whether the Config API set-and-observe test is still absent from the branch at the live tip, and what it would assert if written. No test is written in this assignment.
5. Settled category decisions are not reopened: the choice section's alternative (bound or narrow the cache) was checked in round 9 and stands; do not re-derive it.

## Deliverable

`reports/solr-17539-body-findings.md`, with one section per finding: verdict (STILL HOLDS or SETTLED), the quoted passage, the evidence, and the exact replacement text where the finding holds. Close with the live head the body was read at.

## Limits

Read-only apart from the claim and the report. No edit to the live body, the branch, or any draft in this assignment; the main side applies the replacement text. No comment, review, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
