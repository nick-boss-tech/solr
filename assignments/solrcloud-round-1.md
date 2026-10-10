# Assignment: SolrCloud round 1 (audit, then drafts for the draftable tickets)

Claim first: add `claims/solrcloud-round-1.md`, then work. Report: `reports/solrcloud-round-1.md`. Drafts, for the tickets the audit finds draftable, go in `pr-drafts/solrcloud/` following `pr-formula.md`.

Staffing: run this round with 6 subagents in parallel, split by ticket cluster, with the lead writing the roll-up report. Hard cap from Nick (2026-10-09): never more than 6 subagents running at once across ALL assignments combined; subagents slow each other down past that. Run rounds one after another, not together.

Convergence (standing rule): start from the receipts in `receipts/`. A branch already gated at its exact live tip, or already decided on the main side's record, is NOT re-audited; the audit verifies the recorded state against the branch and judges PR readiness. The round is one audit pass; the main side then does one answers pass; then drafts; then an openings slate goes to Nick. Do not open a second audit loop on settled branches.

The review side does not run gates or builds (Windows builds are revoked). Gate evidence comes from the main side's receipts; where a receipt says NO GATE or gated at an older head, say what is owed instead of substituting a local run.

## Scope

Thirty-one tickets, from the owner's inventory (SolrCloud, overseer and cluster state section of `branch-focus-inventory-2026-10-08.md`): SOLR-3865, 4754, 5813, 7394, 9155, 10234, 10641, 11288, 11479, 12651, 12991, 12998, 13136, 13186, 13239, 13369, 14919, 15035, 15106, 15386, 15674, 15863, 16013, 16437, 17281, 17292, 17680, 17733, 18277, 18391. Branches are `solr-<ticket>-submit` on the fork, with two exceptions: SOLR-7394's branch is `solr-7394-recovery`, and SOLR-18391 has two branches, `solr-18391-submit` and `solr-18391-graceful-create-submit`. Rows the inventory cross-files here but whose primary category is elsewhere (for example SOLR-8576, Core admin) are not audited in this round; rows filed here as primary that also touch another category (11288, 12998, 15035, 15674, 15863, 17680) are audited here.

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-3865: NO GATE, fresh arrival, premise unverified, branch still carries its TESTING.md note. Audit only; do not draft it yet. A premise run and first gate are main-side work still owed.
- SOLR-4754: NO GATE, fresh arrival, premise unverified, branch still carries its TESTING.md note. Audit only, like 3865.
- SOLR-5813: gate green at the live tip 90b8baa08ae (CloudDescriptorTest 3 of 3, proof grounded). PR-ready. Its round 28 report carries a Choice for the draft (default to the core name against rejecting the empty name) and a Limits line naming the unaddressed SOLR-5811 knock-on.
- SOLR-7394: gate green at the live tip 9857d9ee802, and already a live PR on apache/solr. Consistency pass only.
- SOLR-9155: gate green at the live tip 9f08d033023 (ZkControllerGetLeaderTest 1 of 1, proof grounded). PR-ready; its report owes the draft one Choice (SolrException with the flag restored against a declared InterruptedException) and Limits lines.
- SOLR-10234: NO GATE, fresh arrival, premise unverified. Audit only.
- SOLR-10641: NO GATE, fresh arrival; a sweep classified the tip as tidy-only drift (one assertEquals line join in its test). Audit only. Whether a passing pin ships as its own PR is an open owner call; state it as an owner decision, do not draft.
- SOLR-11288: gate green at the live tip cd094c3c623 (TestCollectionAPI 4 of 4, proof grounded). PR-ready but partial for its audit ticket: the escaping concern is unaddressed, and the draft's Limits name it with a follow-up offer.
- SOLR-11479: NO GATE. The branch moved after arrival; the live tip 7e538e844c4 is the code commit (AddReplicaCmd rejects property.coreNodeName when several replicas would share it). Audit only; a first gate is main-side work still owed.
- SOLR-12651: gated at an older head 90032e274b7 (TestLocalFSCloudBackupRestore 2 tests, proof grounded); the live tip f3131d1ee84 has moved since. Audit against the gated head, flag the moved tip, and note a re-gate at the live tip is main-side work owed before any opening. A ticket-thread question (whether restore cleanup should be optional for retry and resume) goes in the draft's description.
- SOLR-12991: gate green at the live tip 1a86966179e (focused class 1 of 1, proof grounded). PR-ready; its report owes a Choice (WARN against keeping ERROR) and Limits lines.
- SOLR-12998: gated at an older head, and already a live PR on apache/solr. Consistency pass only.
- SOLR-13136: gated at an older head, and already a live PR on apache/solr. Consistency pass only.
- SOLR-13186: gate green at the live tip b436d90d2a8 (OverseerElectionContextTest 1 of 1, proof grounded). PR-ready; Limits lines only.
- SOLR-13239: gated green as-is at the live tip 699a1fce368, and submission-held per Nick regardless. Audit verifies the record only; do not draft it.
- SOLR-13369: gate green at the live tip dfa0db5bdf9 (TriLevelCompositeIdRoutingTest 1 of 1), test-only, and its proof was NOT obtained by run: the premise stands by construction on the ticket's Jenkins record and the router reading. Any draft's Proof must say exactly that and must not claim a failing base run.
- SOLR-14919: gate green at the live tip 84e7bcaeb57 (IgnoreCommitOptimizeUpdateProcessorFactoryTest 1 of 1, proof grounded).
- SOLR-15035: gate green (hardened) at the live tip 12d7491e82c (AddReplicaTest 4 of 4, proof passed and caught that the banked patch alone was a no-op on current main).
- SOLR-15106: gate green at the live tip 40b7e5d0efa (OverseerProcessorExitTest 1 of 1). PR-ready; the untested processor-exit versus session-expiry race stays a Limits line with a follow-up offer.
- SOLR-15386: gate green at the live tip ca8cb61ee95 (NodeMutatorTest 3 of 3); proof inconclusive by construction, recorded as such. An owner question (cluster-state snapshot sufficiency) is open on the main side; state it in the report's owner list.
- SOLR-15674: gate green (hardened) at the live tip ba01c83d4c5 (IndexSchemaFactoryCacheTest 1 of 1); proof inconclusive by construction. PAIRED with SOLR-15478 (Configsets and config API round 1, gated at 0478bdf0ac5): both replace a data-version freshness check with the znode mzxid. Audit them together and state the pairing in the report and in both drafts.
- SOLR-15863: gate green at the live tip f381fd8d4dd (BackupCmdTest 4 of 4, BackupCoreAPITest 6 of 6, LocalFSCloudIncrementalBackupTest 7 of 7; premise grounded at the earlier gated head, inconclusive by construction at this head, mutation proof in its round 36 report). PR-ready; its report owes one Choice and Limits lines (see the receipt).
- SOLR-16013: gated at an older head e1bd21fd11a (OverseerCloseOrderingTest 1 of 1, proof passed after the shipped test was rewritten to pin the invariant); the live tip ba26b702891 has moved since. Audit against the gated head, flag the moved tip, and note a re-gate at the live tip is main-side work owed before any opening.
- SOLR-16437: NO GATE. The only test evidence is a GitHub corroboration run at its head, which does not settle gate state. Audit only; a first gate is main-side work still owed if the audit finds the branch draftable.
- SOLR-17281: NO GATE, PARKED under review on Nick's side. Audit only; do not draft.
- SOLR-17292: gate green (hardened) at the live tip e43200b0fb6 (TestPerReplicaStates 4 of 4, proof passed). The behavior change (persist now propagates failures callers used to never see) must be stated plainly in the draft.
- SOLR-17680: gate green at the live tip f4b8ce83365 (CreateRoutedAliasTest 15 of 15, CreateAliasAPITest 13 of 13, proof grounded). The draft must also name that the v2 aliases endpoint with two routers starts working, which the changelog title does not say.
- SOLR-17733: gate green at the live tip 636196b7954 (TestDistribFileStore 1 test, proof grounded). The draft must state the contract change (an API delete now removes the ZooKeeper entry) and note the ticket's sync complaint was not reproduced.
- SOLR-18277: gate green (hardened) at the live tip 17c0e644812, and its pull request has already merged on apache/solr. Retire candidate: the audit confirms the merged state and reports; do not draft.
- SOLR-18391: two live PRs on apache/solr (solr-18391-submit as a draft PR; solr-18391-graceful-create-submit gated green at an older head 3a0ff1262bf, live tip adcda10b501). Consistency pass only for both. The alias-failure deletion question is already posed in the live PR's Choices with a recommendation on record (keep the delete); do not re-open it.

## Interactions to check

- The mzxid pair: SOLR-15674 (this round) and SOLR-15478 (Configsets round 1, in flight on the review side). Same mechanism in two places (IndexSchemaFactory cache check; ZkConfigSetService modification version). State the landing order and whether either draft's wording about version resets must match the other's.
- The AddReplicaCmd pair: SOLR-11479 (rejects property.coreNodeName for several replicas) and SOLR-15035 (persists numShards so ADDREPLICA matches CREATE) both change AddReplicaCmd.java. 11479 is audit-only this round; note the overlap and the likely landing order without auditing 15035 twice.
- The overseer lifecycle cluster: SOLR-13186 (election registration cleanup on a skipped start), SOLR-15106 (overseer thread return), SOLR-16013 (overseer close ordering), SOLR-15386 (NodeMutator and the overseer queue). Check the four for shared files and conflicting assumptions about overseer shutdown and election.
- The ZkController cluster: SOLR-9155 (getLeader interrupt handling), SOLR-15386 (ZkController and NodeMutator), SOLR-16013 (ZkController.close). Check hunks against each other and state a landing order.
- The backup and restore pair: SOLR-12651 (RestoreCmd cleanup) and SOLR-15863 (BackupCmd and backup properties) share TestLocalFSCloudBackupRestore. Check the test class for overlap and state which lands first.
- SOLR-14919 also touches RecoveryStrategy.java (Update processing cross-file); SOLR-7394's branch is recovery too. Check the RecoveryStrategy hunks of 7394 and 14919 against each other (7394 is consistency-only; the check is for 14919's draft).
- Live PR branches (7394, 12998, 13136, 18391 both) are consistency only: verify each live PR's head against its receipt and report any drift; do not draft replacements.

## The audit

Per ticket: read the Jira ticket, the branch diff against its base, and the receipt. Verify the recorded state against the branch (a moved tip or a diff that differs from the record gets flagged, not silently adopted). For the gated tickets, judge PR readiness: premise, scope, changelog entry, and what the description must state or avoid claiming. Follow the standing rules: no new Jira ticket unless someone asks for one; a scope question goes in a draft's Limits or A Choice; a needed follow-up fix is named in Limits or a Choice with a stated plan to submit it; public PR text carries no internal process vocabulary (no gate, receipt, ledger, rc=0, "fresh JUnit XML", or "pre-fix proof" as a label); Proof sections state plainly what ran, at which head, what passed, and that the new test fails without the fix (where a receipt records the proof as inconclusive by construction or a pin, the draft says that instead). Titles must be accurate. Where a draft must name a Lucene version, name every version that applies: main and branch_10x pin Lucene 10.4.0, branch_9x pins Lucene 9.12.3; if one version is mentioned, the others that apply are mentioned too.

## Deliverables

1. `reports/solrcloud-round-1.md`: per-ticket verdict (draftable, held with reason, consistency pass result for the live PRs, audit-only outcome for the NO GATE and parked tickets, retire confirmation for 18277, or owner decision with the options stated plainly), plus any disagreement with the receipts.
2. Drafts in `pr-drafts/solrcloud/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. Owner decisions go in the report as a short list at the end.
