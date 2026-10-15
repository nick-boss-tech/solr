# Pool assignment: draft fidelity fixes application (the four landed rounds whose fixes are still owed)

Status: DONE

Capability tags: `review`, `draft`. Intended host: windows. Staffing: 2. Claim path: claims/pool-draft-fidelity-fixes-application.md (slice-level claims by fidelity round are fine).

Purpose: four draft fidelity rounds have landed their reports, but unlike the Streaming and SolrJ round and the Suggester round, their direct draft fixes were never applied. This assignment applies them. It follows the established fidelity pattern: wording and citation drift that the evidence settles mechanically is fixed directly in the drafts under `pr-drafts/`; anything that touches a claim, a number, a date, a head, or a decision is not applied and is flagged for the main side instead.

## Scope: the four rounds and their reports

- CLI, Security, Build/docs and Metrics: `reports/draft-fidelity-cli-security-builddocs-metrics.md` plus its part files (`-s1` to `-s4`).
- Configsets, Query parsing, and Schema and analysis: `reports/draft-fidelity-configsets-query-schema.md` plus its part files (`-s1` to `-s6`).
- Core admin, Replication and backup, and Spellcheck: `reports/draft-fidelity-coreadmin-replication-spellcheck.md` plus its part files (`-s1` to `-s5`).
- Search components and eDisMax: `reports/draft-fidelity-searchcomponents-edismax.md` plus its part files (`-s1` to `-s12`).

Out of scope, do not redo: the Streaming and SolrJ fidelity round (its fixes were applied on 2026-10-10) and the Suggester fidelity round (it fixed its drafts directly). If one of the four in-scope reports cross-references a Streaming, SolrJ, or Suggester draft, leave that draft alone and note the cross-reference in the report.

## Method for every report item

1. Work item by item from the four roll-up reports, reading the part file behind any item the roll-up summarizes. Each DRIFT item names a draft under `pr-drafts/`, the text at issue, and the replacement or correction the round derived from the receipt evidence, the live branch tip, or the category decision record.
2. Before editing, read the draft as it stands now and confirm the item's quoted text is still there. Drafts may have moved since the round landed. If the quoted text is gone or already corrected, record the item as already settled and move on; do not force an edit.
3. Apply the fix directly only where the correct wording or citation follows mechanically from the evidence and does not alter the substance: a wrong line anchor or SHA in a link, a stale link form, wording broader than its cited code, a citation that must point at the merge-base rather than the head (or the reverse, per the citation rule in `pr-formula.md`), internal process vocabulary with an equivalent plain statement, or a section summary that does not match its section.
4. Do NOT apply any item that would change a claim, a test count or other number, a verification date, a named head or base SHA as a fact (as opposed to a link target), a scope statement, or a recorded decision. Flag it instead (see the deliverable). This holds even where the report proposes exact replacement text: if the replacement changes what the draft asserts rather than how it says it, it is flagged, not applied.
5. Where a report itself flags two records in conflict, or marks an item as needing a main-side call, do not resolve it from one record alone. Flag it with both records quoted.
6. After editing a draft, re-check the edited passage against the receipt or decision record the item cites, and confirm the draft still follows `pr-formula.md` presentation: AI header and footer where the draft carries them, a bold one-line summary opening each section, plain language.

## Deliverable and limits

Deliverable: `reports/draft-fidelity-fixes-application.md`, with counts up front per round (items applied, items already settled, items flagged), every direct edit listed (draft file, old text, replacement text, and the supporting evidence), and every flagged item stated with the draft location, the exact text at issue, and the conflicting records or the reason it needs a main-side call.

The only files this assignment may edit are the drafts under `pr-drafts/` named by the four in-scope reports, plus the claim and the deliverable report. Do not edit receipts, material, other reports, branch code, or live PR text. No builds or test runs.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverables). No em dashes in authored text.
