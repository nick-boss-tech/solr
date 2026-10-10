# Assignment: Query parsing round 1 (audit, then drafts for the draftable tickets)

Claim first: add `claims/query-parsing-round-1.md`, then work. Report: `reports/query-parsing-round-1.md`. Drafts, for the tickets the audit finds draftable, go in `pr-drafts/query-parsing/` following `pr-formula.md`.

Staffing: run this round with 8 subagents in parallel, split by parser family or by ticket cluster, with the lead writing the roll-up report.

## Scope

Twenty-five tickets, from the owner's inventory (Query parsing section of `branch-focus-inventory-2026-10-08.md`): SOLR-874, 4824, 6014, 8977, 9048, 9149, 10897, 11391, 11761, 12212, 12532, 12608, 12871, 13202, 13838, 13903, 15615, 15906, 16130, 16267, 16570, 17280, 17311, 17796, 17882. Branches are `solr-<ticket>-submit` on the fork, except SOLR-16130 whose branch is `solr-16130-test-followup` (test-only). Rows the inventory marks "also: Query parsing" (16570 is filed here as primary; 16130's export code branch and any other cross-filed rows) have their primary category elsewhere where marked and are not re-audited here.

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-874: gate green at the live tip ac9ab337537, PR-ready (SolrPluginUtilsTest 10 of 10, DisMaxRequestHandlerTest 4 of 4, proof grounded). Its round 28 report owes the description two Choices items on dropping dangling operators.
- SOLR-4824: gate green at the live tip 76c777e4661 (TestSolrQueryParser 38 of 38, proof grounded). Its round 28 report carries a parameter-name Choice for the draft.
- SOLR-6014: gate green at the live tip 505849d2d4e (TestExtendedDismaxParser 39 of 39, proof grounded, GitHub corroboration succeeded). The dismax-only scope goes in Limits.
- SOLR-8977: gate green at the live tip 395b24964fd (38 of 38). The honest framing is in the receipt: the symptom is already fixed on current main for cores at luceneMatchVersion LUCENE_10_2_0 or later; the branch closes the configuration-dependent gap.
- SOLR-9048: gate green at the live tip b145018563c (48 of 48, proof grounded). The all-stopword semantics is a genuine Choice for the draft.
- SOLR-9149: gate green at the live tip 151dfed119e (48 focused tests, proof grounded).
- SOLR-10897: gate green at the live tip d9240d0750f (20 focused tests, proof grounded).
- SOLR-11391: NO GATE, never pipelined, branch still carries its TESTING.md note. Audit only; do not draft it yet. A premise run and first gate are main-side work still owed.
- SOLR-11761: gate green at the live tip 41893ee9ce6 (89 of 89, proof established over two rounds; the shipped test had a setup defect fixed at the gate).
- SOLR-12212: gate green at the live tip 876953fdc92 (80 focused tests). Its premise needed the auto-fix trap handled; see the receipt.
- SOLR-12532: gate green at the live tip 69c06da4467 (TestSolrQueryParser 38 of 38, proof grounded).
- SOLR-12608: gate green at the live tip d1dd8a1f9f0 (86 focused tests). Its premise used a scratch-adapted test; the receipt says why, and the draft's Proof must not claim more than that run shows.
- SOLR-12871: gate green at the live tip c79a49320cd (TestNestedDocsSort 12 of 12, proof grounded, GitHub corroboration succeeded).
- SOLR-13202: gate green at the live tip c0aec3a7ae0, and already a live PR (apache/solr #5012), APPROVED and awaiting merge. Consistency pass only; the owner has already decided not to touch an approved PR for tidies.
- SOLR-13838: reconciled green at the live tip 4520c25abe2 (both test classes 1 of 1, NaN pre-fix proof). The record is a ledger table row with no gate log; flag, do not fill in, any gap that matters.
- SOLR-13903: reconciled green at the live tip 73ef411dd11 (both test classes 1 of 1). Same record shape as 13838.
- SOLR-15615: gate green at the live tip be77267bf55 (CloudMLTQParserTest 15 of 15, proof grounded, GitHub corroboration succeeded). An earlier GATE INVALID at a pre-fix head is superseded; the receipt names the final gate.
- SOLR-15906: gate green at the live tip 50139a8e979 (TestSolrQueryParser 39 of 39, proof grounded). The shipped head fixes a regression an earlier head carried; the receipt tells that story.
- SOLR-16130: gate green at the live tip 3c48dec4b79, test-only, and already a live PR (apache/solr #5004). Consistency pass only, like 13202.
- SOLR-16267: gate green (hardened) at the live tip 8f4b0c6d2fb (TestJsonFacets 30 of 30, TestFunctionQuery 23 of 23, proof passed).
- SOLR-16570: NO GATE, awaiting pipeline, branch still carries its TESTING.md note. Audit only; do not draft it yet. A premise run and first gate are main-side work still owed.
- SOLR-17280: gate green at the live tip 40817c5cb7e (focused class 5 of 5, proof grounded). The filterCache tradeoff kept in the branch is Limits material.
- SOLR-17311: gate green (hardened) at the live tip 9f7524582f9 (TestNestedDocsSort 12 of 12, proof passed).
- SOLR-17796: gate green at the live tip 661165d2673 (TestCollapseQParserPlugin 21 of 21, proof grounded). The draft quotes the ticket body, not its title (see the receipt).
- SOLR-17882: NO GATE by finding: premise-dead, retire candidate, and the retire-or-re-aim call is the owner's and still pending. Audit only; do not draft.

## Interactions to check

- The SolrQueryParserBase cluster: SOLR-4824, 9149, 11761, 12532 and 12608 all change SolrQueryParserBase.java, and 4824, 9149, 11761, 12532 and 15906 all extend TestSolrQueryParser. State the landing order inside this cluster and which pairs conflict in production code or only in the shared test class.
- The grammar pair: SOLR-12212 and SOLR-17796 both change QueryParser.java and QueryParser.jj. Both branches carry a checked-in generated parser; check that each generated file matches its grammar and state the landing order.
- The join family: SOLR-8977 (GraphQueryParser, BJQParserTest), SOLR-9048 (FiltersQParser, BJQParserTest), SOLR-11391 (JoinQParserPlugin), SOLR-13202 (JoinQParserPlugin and the cross-collection parsers, live PR), SOLR-16130 (CrossCollectionJoinQueryTest, live PR). Shared test classes and shared helpers (ScoreJoinQParserPlugin.requireFromAndTo) need a stated order where they overlap.
- The childfield pair: SOLR-12871 and SOLR-17311 both change ChildFieldValueSourceParser.java and both extend TestNestedDocsSort. Check the hunks against each other directly; this is the likeliest real conflict in the category.
- The dismax pair: SOLR-874 (SolrPluginUtils, called from DisMaxQParser) and SOLR-6014 (DisMaxQParser itself, TestExtendedDismaxParser, which SOLR-11761 also extends). Note the coverage split the 6014 receipt records (dismax covered, a nested {!edismax} clause not).
- The auto-fix thread: QParser.autoFixPureNegative shaped the premises of SOLR-8977 and SOLR-12212, and SOLR-15906 changes QParser.java itself. If 15906 lands, say whether the other two branches' tests or framings change.
- The collapse pair: SOLR-16570 changes CollapsingQParserPlugin and SOLR-17796's tests are TestCollapseQParserPlugin; 16570 is audit-only this round, so note the pairing without auditing 16570's fix.

## The audit

Per ticket: read the Jira ticket, the branch diff against its base, and the receipt. Verify the recorded state against the branch (a moved tip or a diff that differs from the record gets flagged, not silently adopted). For the gated tickets, judge PR readiness: premise, scope, changelog entry, and what the description must state or avoid claiming. Follow the standing rules: no new Jira ticket unless someone asks; a needed follow-up is named in Limits with a stated plan to submit it; public text carries no internal process vocabulary; Proof states plainly what ran, at which head, what passed, and that the new test fails without the fix. Where a PR description must name a Lucene version behavior, check it against both the 9.x and 10.x lines (the 8977 receipt turns on a luceneMatchVersion boundary, and 4824's fix was verified against the Lucene 10.4.0 constructor delegation; both need the cross-version check before any version claim is drafted).

## Deliverables

1. `reports/query-parsing-round-1.md`: per-ticket verdict (draftable, held with reason, consistency pass result for the two live PRs, audit-only outcome for 11391, 16570 and 17882, or owner decision with the options stated plainly), plus any disagreement with the receipts.
2. Drafts in `pr-drafts/query-parsing/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. Owner decisions go in the report as a short list at the end.
