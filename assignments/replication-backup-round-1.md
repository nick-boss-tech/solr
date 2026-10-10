# Assignment: Replication and backup round 1 (audit, then drafts for the draftable tickets)

Claim first: add `claims/replication-backup-round-1.md`, then work. Report: `reports/replication-backup-round-1.md`. Drafts, for the tickets the audit finds draftable, go in `pr-drafts/replication-backup/` following `pr-formula.md`.

Staffing: run this round with 6 subagents in parallel, split by ticket cluster, with the lead writing the roll-up report. Hard cap from Nick (2026-10-09): never more than 6 subagents running at once across ALL assignments combined; subagents slow each other down past that. Run rounds one after another, not together.

Convergence (standing rule): start from the receipts in `receipts/`. A branch already gated at its exact live tip, or already decided on the main side's record, is NOT re-audited; the audit verifies the recorded state against the branch and judges PR readiness. The round is one audit pass; the main side then does one answers pass; then drafts; then an openings slate goes to Nick. Do not open a second audit loop on settled branches.

The review side does not run gates or builds (Windows builds are revoked). Gate evidence comes from the main side's receipts; where a receipt says NO GATE or gated at an older head, say what is owed instead of substituting a local run.

## Scope

Thirteen tickets, from the owner's inventory (Replication and backup section of `branch-focus-inventory-2026-10-08.md`): SOLR-5589, 6711, 8430, 9091, 9382, 9598, 9865, 11650, 12085, 12246, 17287, 18249, 18280. Branches are `solr-<ticket>-submit` on the fork. Two tickets are cross-filed elsewhere but have their audit home here: SOLR-11650 (also SolrJ and clients) and SOLR-17287 (also Update processing and atomic updates).

Not in this round, so the round does not go looking for them: SOLR-12651, SOLR-15863, SOLR-14919 and SOLR-7394 all touch backup, restore or recovery, and all four have their audit home in SolrCloud round 1 (already assigned and answered there). In particular, the restore-cleanup question on the SOLR-12651 ticket thread (whether restore cleanup should be optional for retry and resume) belongs in that ticket's draft in the SolrCloud round; this round does not re-audit 12651 and does not draft it. Where this round's branches interact with those four, the interaction is named below and checked from this round's side only.

## Starting state (from the receipts in `receipts/`; a receipt at the exact live tip settles gate state)

- SOLR-5589: NO GATE, fresh arrival, premise unverified, branch still carries its TESTING.md note. Audit only; do not draft it yet. A premise run and first gate are main-side work still owed.
- SOLR-6711: NO GATE, fresh arrival, premise unverified, branch still carries its TESTING.md note. Audit only, like 5589.
- SOLR-8430: gate green at the live tip 49af21be589 (ReplicationRateLimiterTest 3 of 3, probe 1 of 1, proof grounded in both legs). PR-ready.
- SOLR-9091: NO GATE, fresh arrival, premise unverified, branch still carries its TESTING.md note. Audit only. Its topic is not recorded in the inventory; the audit reads it from the ticket and the diff (RestoreCore.java is the main file).
- SOLR-9382: NO GATE, fresh arrival, premise unverified, branch still carries its TESTING.md note. Audit only.
- SOLR-9598: gate green at the live tip c8407773f76 (TestLocalFSCloudBackupRestore 2 of 2, RestoreCollectionAPITest 7 of 7, BackupRestoreApiErrorConditionsTest 4 of 4). PR-ready. The proof is timing-dependent and the receipt says so; any draft's Proof must state it the same way and must not claim a deterministic base failure.
- SOLR-9865: gate green at the live tip 4937608bb18 (11 of 11 across TestRestoreCore, TestReplicationHandlerBackup, TestSnapshotCoreBackup). PR-ready. The proof needed a test correction (a core reload step) before it discriminated; the receipt explains why, and any draft's Proof must carry that explanation rather than a bare claim.
- SOLR-11650: gate green at the live tip e4f5e941cd8 (URLUtilTest 18 of 18, IndexFetcherLeaderUrlRedactionTest 2 of 2, TestUserManagedReplicationWithAuth 3 of 3, proof grounded). PR-ready.
- SOLR-12085: gate green at the live tip c8dba502339 (IndexFetcherUnusedFilesTest 1 of 1 counted; the TestReplicationHandler suite in the run is @Nightly and was skipped, as the receipt states). PR-ready; a draft's Proof must not claim end-to-end replication coverage the gate did not run.
- SOLR-12246: gate green at the live tip 3ffc2539ec7 (IndexFetcherCompareFileTest 1 of 1 counted; same @Nightly skip as 12085). PR-ready. Its record carries a Choice for the draft: the branch lowers the whole "did not match" statement from WARN to INFO; the alternative is keeping WARN when the lengths also differ and lowering only the equal-length mismatch. The real cause of the differing .liv checksums on full recovery is not addressed by the branch and stays a Limits line.
- SOLR-17287: gate green at the live tip 6957daf8261 (TestRestoreCore 4 of 4, UpdateLogTest 6 of 6, proof grounded). PR-ready.
- SOLR-18249: gate green at the live tip deffea4c51b (ShardBackupMetadataTest 4 of 4, ShardBackupMetadataOverwriteTest 1 of 1, DeleteBackupCmdTest 5 of 5), and already a live PR on apache/solr. Consistency pass only.
- SOLR-18280: NO GATE on the main side, test-only branch, and already a live PR on apache/solr; the round 31 review dispositioned it with no code change. Consistency pass only; its test state rests on the live PR's own CI at the head, as the receipt states.

## Interactions to check

- The RestoreCore cluster: SOLR-9091 (audit only), SOLR-9865 and SOLR-17287 all change RestoreCore.java. Check the three diffs against each other, state a landing order, and make sure 9865's rollback and 17287's update-log reset do not contradict each other. 9091's audit reads its premise against the same file the other two change.
- The IndexFetcher cluster: SOLR-6711 (audit only), SOLR-11650, SOLR-12085 and SOLR-12246 all change IndexFetcher.java. Check the four for overlapping hunks and conflicting assumptions, and state a landing order for the three gated ones.
- The ReplicationHandler cluster: SOLR-5589, SOLR-6711 and SOLR-9382 (all audit only) and SOLR-11650 change ReplicationHandler.java or its API surface (8430 changes the replication admin API). The audit-only three share one handler; read them together so their premises are judged against the same current code.
- The backup and restore overlap with SolrCloud round 1: SOLR-9598 (this round) shares TestLocalFSCloudBackupRestore with SOLR-12651 and SOLR-15863 (SolrCloud round 1). Check the test class for overlap from 9598's side and state which lands first; do not re-audit the SolrCloud pair. SOLR-14919 and SOLR-7394 meet in RecoveryStrategy.java, which is SolrCloud round 1's check, not this round's.
- SOLR-11650 is cross-filed under SolrJ and clients (URLUtil lives in solrj) and SOLR-17287 under Update processing (UpdateLog.java): both are audited here, and their drafts must state the cross-area effect plainly rather than leaving it to the other category's round.
- Live PR branches (18249, 18280) are consistency only: verify each live PR's head against its receipt and report any drift; do not draft replacements.

## The audit

Per ticket: read the Jira ticket, the branch diff against its base, and the receipt. Verify the recorded state against the branch (a moved tip or a diff that differs from the record gets flagged, not silently adopted). For the gated tickets, judge PR readiness: premise, scope, changelog entry, and what the description must state or avoid claiming. Follow the standing rules: no new Jira ticket unless someone asks for one; a scope question goes in a draft's Limits or A Choice; a needed follow-up fix is named in Limits or a Choice with a stated plan to submit it; public PR text carries no internal process vocabulary (no gate, receipt, ledger, rc=0, "fresh JUnit XML", or "pre-fix proof" as a label); Proof sections state plainly what ran, at which head, what passed, and that the new test fails without the fix (where a receipt records the proof as timing-dependent or partial, the draft says that instead). Titles must be accurate. Where a draft must name a Lucene version, name every version that applies: main and branch_10x pin Lucene 10.4.0, branch_9x pins Lucene 9.12.3; if one version is mentioned, the others that apply are mentioned too.

## Deliverables

1. `reports/replication-backup-round-1.md`: per-ticket verdict (draftable, held with reason, consistency pass result for the live PRs, audit-only outcome for the NO GATE tickets, or owner decision with the options stated plainly), plus any disagreement with the receipts.
2. Drafts in `pr-drafts/replication-backup/` for the draftable tickets, each naming the head it was written against.

Do not open PRs, post comments, edit branches, or run builds. Owner decisions go in the report as a short list at the end.
