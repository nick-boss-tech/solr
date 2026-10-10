# Assignment: Schema, analysis and field types round 1 (audit, then drafts for the draftable tickets)

Claim first: add `claims/schema-analysis-round-1.md`, then work. Report: `reports/schema-analysis-round-1.md`. Drafts, for the tickets the audit finds draftable, go in `pr-drafts/schema-analysis/` following `pr-formula.md`.

Staffing: run this round with 4 subagents in parallel, split by ticket cluster, with the lead writing the roll-up report. Hard cap from Nick (2026-10-09): never more than 6 subagents running at once across ALL assignments combined; subagents slow each other down past that. Run rounds one after another, not together. Suggested clusters: 9349 and 14199; 15357, 15358 and 10403 (the currency and sub-field family); 10131, 15712 and 15945; 16977, 17047 and 18134 (vectors and analysis). The lead verifies live tips, checks the interaction section across clusters, and writes the roll-up.

## Scope

Eleven tickets, from the owner's inventory (Schema, analysis and field types section of `branch-focus-inventory-2026-10-08.md`): SOLR-9349, SOLR-10131, SOLR-10403, SOLR-14199, SOLR-15357, SOLR-15358, SOLR-15712, SOLR-15945, SOLR-16977, SOLR-17047, SOLR-18134. All branches are `solr-<ticket>-submit` on the fork. Rows whose primary category is elsewhere are not re-audited here even where their files overlap (SOLR-3704 is Highlighting, SOLR-10252 is Spellcheck, SOLR-15478 is Configsets).

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-9349: gate green at the live tip 1e79bb42123, 29 of 29 across four schema API classes, proof recorded.
- SOLR-10131: NO GATE, never pipelined, branch still carries its TESTING.md note. Audit only; state what a premise run and gate must show. Do not draft it.
- SOLR-10403: NO GATE, never pipelined, branch still carries its handoff doc. Audit only, like 10131.
- SOLR-14199: gate green at the live tip e743c90da79, PR-ready; its ledger review names two Limits lines the draft must carry.
- SOLR-15357: gate green at the live tip d717899b873, CopyFieldSubFieldsTest 4 of 4. Its proof is classified: the behavioral tests are pins, because the test calls the new getSubFields API that base does not have. Any draft's Proof must say that plainly rather than claiming a base failure.
- SOLR-15358: gate green at the live tip cbb345f2e7d; its changelog carries a reindex note the draft must keep.
- SOLR-15712: NO GATE, never pipelined, and tidy-only DRIFT at the tip (a javadoc rewrap tidy would make in its test). Audit only; the tidy fold-in is main-side packaging work before its gate.
- SOLR-15945: gate green at the live tip adb0fd450c0, NonIndexedSpatialFieldTest 1 of 1, proof recorded.
- SOLR-16977: gate green at the live tip 1142f9563ab, PR-ready, with a scope split that is an owner call: the ticket's document-vector half is not addressed. State it as an owner decision in the report and as a Limit in any draft.
- SOLR-17047: gate green at the live tip 1ba7e33bfe7, PR-ready, BadIndexSchemaTest 29 of 29 with the hnswM=0 gap fixed in round 35.
- SOLR-18134: gate green at the live tip c5a0bdb21e8, PR-ready. The direction (ship the branch's own factory workaround rather than wait for the upstream Lucene fix) is the owner's recorded call; the draft poses it to maintainers as a Choice.

## Interactions to check

- SOLR-15357 and SOLR-15358 are a pair and the audit treats them together. Both change CurrencyFieldType.java: 15358 builds the amount and currency-code sub-fields through SchemaField.createFields so their docValues settings are honored, and 15357 adds the FieldType.getSubFields hook with a CurrencyFieldType override and counts sub-fields as copyField targets in IndexSchema and ManagedIndexSchema. Check the hunks for overlap, state which should land first, and check whether 15358's docValues-honoring sub-fields are exactly the sub-fields 15357 counts. SOLR-10403 changes CurrencyValue, the value class the currency field type produces; note any bearing on the pair without auditing 10403 as part of it.
- SOLR-9349 and SOLR-15357 both change ManagedIndexSchema.java (9349 in deleteFields, 15357 in the copy-target count mirrors). Check for overlap and landing order.
- SOLR-14199 and SOLR-15357 both change FieldType.java (14199 removes the getExistenceQuery carve-out; 15357 adds getSubFields). Check for overlap.
- SOLR-16977 changes DenseVectorField.java and SOLR-17047 changes SchemaCodecFactory, SolrCore, and the dense vector field classes including BinaryQuantizedDenseVectorField. Check whether their DenseVectorField hunks overlap and whether 17047's eager format construction changes any assumption in 16977's zero-vector check.
- SOLR-15945 changes AbstractSpatialPrefixTreeFieldType; SOLR-18134 changes the analysis package (ZeroPositionIncrementFilter and its factory). No shared files are expected; confirm from the diffs rather than assuming.

## The audit

Per ticket: read the Jira ticket, the branch diff against its base, and the receipt. Verify the recorded state against the branch (a moved tip or a diff that differs from the record gets flagged, not silently adopted). For the gated tickets, judge PR readiness: premise, scope, changelog entry, and what the description must state or avoid claiming. Follow the standing rules: no new Jira ticket unless someone asks; a needed follow-up is named in Limits with a stated plan to submit it; public text carries no internal process vocabulary; Proof states plainly what ran, at which head, what passed, and that the new test fails without the fix (for 15357, whose tests are pins, Proof states the pin honestly). Where a claim rests on Lucene behavior (18134's filter and the vectors tickets are the likely cases), check it against both the 9.x and 10.x lines, since the target branch decides which Lucene the PR merges against.

## Deliverables

1. `reports/schema-analysis-round-1.md`: per-ticket verdict (draftable, held with reason, audit-only result for the three ungated tickets, or owner decision with the options stated plainly), plus any disagreement with the receipts.
2. Drafts in `pr-drafts/schema-analysis/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. Owner decisions go in the report as a short list at the end.
