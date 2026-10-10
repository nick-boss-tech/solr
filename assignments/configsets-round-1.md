# Assignment: Configsets and config API round 1 (audit, then drafts for the draftable tickets)

Claim first: add `claims/configsets-round-1.md`, then work. Report: `reports/configsets-round-1.md`. Drafts, for the tickets the audit finds draftable, go in `pr-drafts/configsets/` following `pr-formula.md`.

## Scope

Seven tickets, from the owner's inventory (Configsets and config API section of `branch-focus-inventory-2026-10-08.md`): SOLR-6960, SOLR-7267, SOLR-7323, SOLR-13706, SOLR-15478, SOLR-17363, SOLR-18178. Branches are `solr-<ticket>-submit` on the fork, except SOLR-18178 whose live branch is `solr-18178-verify`. Rows the inventory marks "also: Configsets" (10252, 10424, 15674, 17539, 9750) have their primary category elsewhere and are not re-audited here.

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-6960: gate green at 4569ad9d591, one test-only commit behind the live tip 9bef536fc12. A main-side top-up verification of the added test is launched with this round. Audit and draft against the live tip, noting the top-up as pending until the main side confirms it.
- SOLR-7267: gate green on the gated tree; the live tip 59f34a339b0 adds only the handoff-doc removal. PR-ready. Its review report records one Choice (additive cs alias as implemented, or renaming cz to cs).
- SOLR-7323: NO GATE, never pipelined, branch still carries its TESTING.md note. A premise run and first gate are main-side work launched with this round. Audit only; do not draft it yet.
- SOLR-13706: gate green at the live tip 590dd5c24d9, and already a live PR (apache/solr #5015, ready). Consistency pass only: branch, receipt, and the live PR description should agree. Do not draft new PR text; flag any description drift.
- SOLR-15478: gate green at the live tip 0478bdf0ac5, hardened. Draftable candidate.
- SOLR-17363: gate green at the live tip b8e8e1c4846, test rewrite only, production untouched. Its covered scope excludes a still-existing stale replica, pinned by a guard test; the owner's scope call on that gap is deferred on the main side's record, so state it in the report as an owner decision and in any draft as a Limit.
- SOLR-18178: gate green at the live tip b75e7d4d3c4, and already a live PR (apache/solr #4968, ready), round 2 review clean at that head. Consistency pass only, like 13706.

## Interactions to check

- SOLR-6960 and SOLR-13706 both change SolrConfig.java (6960 also RequestHandlers.java; 13706 also PluginInfo.java). Check the hunks for overlap and state the landing order if both proceed.
- SOLR-7323 and SOLR-18178 both change FileSystemConfigSetService.java. 18178 is already live; check 7323's hunk against 18178's shipped change for conflict or changed context.
- SOLR-15478 changes ZkConfigSetService version reporting. SOLR-15674 (SolrCloud category, not in scope) works the same configset-reuse area from the loader side; note the pairing in the report without auditing 15674.
- SOLR-17363's tests drive the /config path through SolrConfigHandler; SOLR-18129 (live PR, not in scope) changed duplicate-key handling in config request data. Note any interaction the diff shows.

## The audit

Per ticket: read the Jira ticket, the branch diff against its base, and the receipt. Verify the recorded state against the branch (a moved tip or a diff that differs from the record gets flagged, not silently adopted). For the gated tickets, judge PR readiness: premise, scope, changelog entry, and what the description must state or avoid claiming. The 6960 ledger review names its Limits material (implicit handlers reported without the merge, with a follow-up offer; overlay-defined handlers reported as stored; the proof is a unit-level serialization test, not a live /config request); check it against the diff, including the test the live tip adds. Follow the standing rules: no new Jira ticket unless someone asks; a needed follow-up is named in Limits with a stated plan to submit it; public text carries no internal process vocabulary; Proof states plainly what ran, at which head, what passed, and that the new test fails without the fix. Where a PR description must name a Lucene version behavior, check it against both the 9.x and 10.x lines.

## Deliverables

1. `reports/configsets-round-1.md`: per-ticket verdict (draftable, held with reason, consistency pass result for the two live PRs, or owner decision with the options stated plainly), plus any disagreement with the receipts.
2. Drafts in `pr-drafts/configsets/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. Owner decisions go in the report as a short list at the end.
