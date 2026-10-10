# Assignment: Core admin and collections API round 1 (audit, then drafts for the draftable tickets)

Claim first: add `claims/core-admin-round-1.md`, then work. Report: `reports/core-admin-round-1.md`. Drafts, for the tickets the audit finds draftable, go in `pr-drafts/core-admin/` following `pr-formula.md`.

Staffing: run this round with 6 subagents in parallel, split by ticket cluster, with the lead writing the roll-up report. Hard cap from Nick (2026-10-09): never more than 6 subagents running at once across ALL assignments combined; subagents slow each other down past that. Run rounds one after another, not together.

Convergence (standing rule): start from the receipts in `receipts/`. A branch already gated at its exact live tip, or already decided on the main side's record, is NOT re-audited; the audit verifies the recorded state against the branch and judges PR readiness. The round is one audit pass; the main side then does one answers pass; then drafts; then an openings slate goes to Nick. Do not open a second audit loop on settled branches.

The review side does not run gates or builds (Windows builds are revoked). Gate evidence comes from the main side's receipts; where a receipt says NO GATE or gated at an older head, say what is owed instead of substituting a local run.

## Scope

Thirty-three tickets, from the owner's inventory (Core admin and collections API section of `branch-focus-inventory-2026-10-08.md`): SOLR-4502, 4989, 5011, 5262, 6438, 8275, 8554, 8576, 8628, 9750, 11431, 11939, 12007, 12849, 12916, 13097, 13246, 14098, 15003, 15024, 15805, 16108, 16499, 16725, 16849, 16887, 17297, 17377, 17708, 17731, 18010, 18278, 18317. Branches are `solr-<ticket>-submit` on the fork, with two exceptions: SOLR-11431's branch is `solr-11431-core-init-503`, and SOLR-18317's row in this category is the banked server-side variant `solr-18317-server-submit` (not the merged submit branch). Rows filed here as primary that also touch another category (8576, 9750, 15003, 16499, 17731) are audited here; rows whose primary category is elsewhere (for example SOLR-11288 and SOLR-16437, SolrCloud) are not re-audited here.

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-4502: NO GATE, fresh arrival, premise unverified, branch still carries its TESTING.md note. Audit only; do not draft it yet.
- SOLR-4989: gate green at the live tip 5bac95376cd4 (LukeRequestHandlerTest 10 of 10, proof grounded). PR-ready; no Choice owed, Limits lines named in its report.
- SOLR-5011: NO GATE, fresh arrival, premise unverified. Audit only.
- SOLR-5262: NO GATE, and no pipeline record on the main side at all. Audit only; state what a premise run and first gate must show.
- SOLR-6438: gate green at the live tip 8c77988d591 (MergeIndexesTest 4 of 4 plus two neighbor classes, proof grounded).
- SOLR-8275: gate green at the live tip e52e10fa50a (TestPrepRecovery 3 of 3, proof grounded). PR-ready; Limits lines named in its report.
- SOLR-8554: NO GATE, fresh arrival, premise unverified. Audit only.
- SOLR-8576: gate green at the live tip 4c46f95c785 (CollectionsAPISolrJTest 25 tests, 1 skipped), test-only. Its new test is a pin (it passes on base too); any draft's Proof presents it as added coverage, not as a failing base run.
- SOLR-8628: NO GATE, fresh arrival, premise unverified. Audit only.
- SOLR-9750: gate green at the live tip f97da6da14a (11 focused tests, proof grounded).
- SOLR-11431: gate green at the live tip 968fad873c4 (TestCoreContainer 25 tests, 3 skipped, proof grounded), and already a live PR on apache/solr. Consistency pass only.
- SOLR-11939: NO GATE, fresh arrival; the branch is a documentation change (a reference-guide page) under a TESTING.md note. Audit only; a docs-only branch needs no test gate, so judge submission readiness as a review question and say so.
- SOLR-12007: gate green at the live tip bdeba582fd6 (four focused classes green, proof grounded). PR-ready, with one design question posed for the owner (the synchronous-cleanup route, against a background cleanup with ordering guards): state it in the report's owner list and pose it as a Choice in any draft.
- SOLR-12849: gated green at an older head, and already a live PR on apache/solr. Consistency pass only.
- SOLR-12916: NO GATE, fresh arrival, premise unverified. Audit only.
- SOLR-13097: gated (fixed, green) at an older head, and already a live PR on apache/solr, closed out and awaiting the reviewer's re-review. Consistency pass only.
- SOLR-13246: gate green at the live tip 6817c6c0c26 (focused class 4 of 4, proof grounded; the gate fixed two received defects, recorded in the receipt). PR-ready; Limits lines named in its report.
- SOLR-14098: reconciled green at the live tip dd4995515e8 (unit test 5 of 5, SolrCloud end-to-end 2 of 2; the record is a ledger table row with no per-branch gate log; flag, do not fill in, any gap that matters).
- SOLR-15003: gated green at an older head 8f5b6f8360a (TestReplicationHandler 3 of 3, proof passed); the live tip 1004abee39a has moved twice since and a gate at the live tip is main-side work still owed. Audit against the gated head and flag the moved tip; do not draft it as ready until the main side gates the tip.
- SOLR-15024: gate green (hardened) at the live tip f95b5010b3f (LukeRequestHandlerTest 9 of 9, proof passed). The gate narrowed the branch to charFilters only; the wider filters question is raised for maintainers in the planned PR text.
- SOLR-15805: gate green (hardened) at the live tip 3432f950f0a (CoreContainerProviderTest 1 of 1, proof passed).
- SOLR-16108: gate green at the live tip 70ad7371b31 (SplitHandlerTest 5 of 5); proof inconclusive by construction, recorded as such. An owner call on this branch is open on the main side; state it in the report's owner list.
- SOLR-16499: NO GATE. The only test evidence is a GitHub corroboration run at its head, which does not settle gate state. Audit only; a first gate is main-side work still owed if the audit finds the branch draftable.
- SOLR-16725: gate green at the live tip be1838ef8cc (LocalFSCloudIncrementalBackupTest 7 tests, 1 skipped, proof grounded).
- SOLR-16849: gate green at the live tip d612b055da2 (SegmentsInfoRequestHandlerTest 7 of 7), test-only salvage: the ticket was already fixed on main by SOLR-18083 and the branch carries only the regression test. Any draft's Proof states the pin honestly, and the report notes the close-as-fixed option from the design record.
- SOLR-16887: gate green at the live tip 42675f65d6f, a BATS branch (counts from the BATS logs, no JUnit XML, no GitHub corroboration by design). PR-ready.
- SOLR-17297: gate green at the live tip c0ab38fc0a8 (TestCoreContainer 26 tests, 3 skipped, proof passed). One inverse-direction review finding was confirmed by run at this head and stays an owner decision (widen the fix or ship as is); state it in the report's owner list. An owner call on this branch is already open on the main side.
- SOLR-17377: gate green at the live tip 22b5f209a11 (41 focused tests, proof grounded). The branch implements option 1 of a design decision recorded in its handoff; state which option shipped, do not re-open it.
- SOLR-17708: gate green at the live tip 10a7fa07a79 (JaxRsSingleAuthorizationTest 2 of 2 plus two integration classes, proof grounded). PR-ready; one review finding is dispositioned as a deliberate structural exception and belongs in the draft's Limits.
- SOLR-17731: gate green at the live tip f2b4ba164f5 (V2ResourcePathOverlapTest 2 of 2, proof grounded). PR-ready. An owner call on this branch is open on the main side.
- SOLR-18010: gated (hardened) at an older head fadc5ee31e3 (BasicAuthStandaloneTest 1 of 1, proof passed); the live tip c3685bb37d9 has moved since and no gate at the live tip is recorded. Audit against the gated head, flag the moved tip, and read the settling run in the takeover record before drafting any claim about concurrent security.json edits.
- SOLR-18278: gate green at the live tip bf19c9fa449 (TestHttpSolrClientProvider 2 of 2), and a retire candidate: the retire call is the owner's and still pending. Its proof fails by construction (a behavior-preserving refactor; the tests pass on base too). State the retire question in the report's owner list; draft only if the audit argues against retiring, and then with an honest Proof.
- SOLR-18317: NO GATE at the banked head, HELD. The combined branch was narrowed at a reviewer's request; the Admin UI half shipped and merged, and this server-side half returns later as its own PR only if a reviewer wants it. Audit confirms the banked state only; do not draft from this branch.

## Interactions to check

- The LukeRequestHandler pair: SOLR-4989 (show=ALL schema section) and SOLR-15024 (charFilters list shape) both change LukeRequestHandler.java, and both gates ran LukeRequestHandlerTest. Check the hunks for overlap and state the landing order.
- The QuerySenderListener pair: SOLR-12916 (listeners added through the config API fail to parse queries; NO GATE, audit only) and SOLR-13246 (the listener's debug log line) both change QuerySenderListener.java. Note the overlap for the landing order without auditing 13246 twice.
- The request-path cluster: SOLR-12849 (alias parameter, GET against POST), SOLR-13097 (authorization in standalone mode), and SOLR-17708 (v2 APIs authorized twice) all sit on the HttpSolrCall path. 12849 and 13097 are live PRs (consistency only); check whether 17708's hunks overlap theirs, since landing order among the three matters if the PRs move.
- The core lifecycle cluster: SOLR-4502 (ShardHandlerFactory initialization), SOLR-5011 (resource loaders on core unload), SOLR-11431 (init failure status, live PR), SOLR-12007 (cleanup on core close), SOLR-15805 (startup exception swallowing), and SOLR-17297 and SOLR-17377 (both change NodeConfig.java; 17377 also changes SolrXmlConfig.java). Check the NodeConfig pair's hunks against each other directly and state a landing order for the cluster.
- The security cluster: SOLR-13097 (live PR), SOLR-17708, and SOLR-18010 all touch authorization or security configuration paths. Note shared files (HttpSolrCall, the security handlers) and keep the three accounts consistent with each other.
- SOLR-8576's test class (CollectionsAPISolrJTest) is shared with the SolrCloud round's SOLR-16437 corroboration run; 8576 is gated and draftable here, 16437 is audit-only there. The drafts must not claim the class's coverage twice.
- SOLR-15003 is also filed under Replication and backup, and its gate story involves the replication handler's snapshot tests; this round owns the ticket (primary category here) and states the cross-file once.

## The audit

Per ticket: read the Jira ticket, the branch diff against its base, and the receipt. Verify the recorded state against the branch (a moved tip or a diff that differs from the record gets flagged, not silently adopted). For the gated tickets, judge PR readiness: premise, scope, changelog entry, and what the description must state or avoid claiming. Follow the standing rules: no new Jira ticket unless someone asks for one; a scope question goes in a draft's Limits or A Choice; a needed follow-up fix is named in Limits or a Choice with a stated plan to submit it; public PR text carries no internal process vocabulary (no gate, receipt, ledger, rc=0, "fresh JUnit XML", or "pre-fix proof" as a label); Proof sections state plainly what ran, at which head, what passed, and that the new test fails without the fix (where a receipt records the proof as a pin or inconclusive by construction, the draft says that instead). Titles must be accurate. Where a draft must name a Lucene version, name every version that applies: main and branch_10x pin Lucene 10.4.0, branch_9x pins Lucene 9.12.3; if one version is mentioned, the others that apply are mentioned too.

## Deliverables

1. `reports/core-admin-round-1.md`: per-ticket verdict (draftable, held with reason, consistency pass result for the live PRs, audit-only outcome for the NO GATE tickets, banked confirmation for 18317, or owner decision with the options stated plainly), plus any disagreement with the receipts.
2. Drafts in `pr-drafts/core-admin/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. Owner decisions go in the report as a short list at the end.
