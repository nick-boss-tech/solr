# SOLR-18601 stopped-gate repair, Windows round 2

Status: IMPLEMENTED, PROOF PENDING. The prior failed gate is not reclassified as green.

- Date: October 28, 2026 Eastern.
- Claim: claims/pr-5253-cleanup-repair-windows-round-2.md, published at be344beb34313a759cbb00c749e73f32545db771.
- Board snapshot reviewed: a8ae208b8d60b1a59623ac60ebf0e2c57917288d, including receipts/PR-5253-cleanup-round-1.md and gates/pr-5253-cleanup-superseding-98d1127-round-2.md.
- Failed input H: 98d1127e4f5d4d149992e1dc494b95928bac057a. Retained vm2's tidy commit cc8c42399909fb8df85c94376a042d291fd56292 and compile repair H by fast-forwarding the clean ticket worktree. Those constructor errors were mistakes in the original Windows patch, corrected by vm2; no history was rewritten.
- Pushed repair I: 6a99e731dadd4c3004b46e2bb42a0e6cad59cfc4, one normal commit above H, fork nick-boss-tech/solr, branch solr-18601-submit. GitHub ref read-back matched I after push.
- Diff: https://github.com/nick-boss-tech/solr/compare/98d1127e4f5d4d149992e1dc494b95928bac057a...6a99e731dadd4c3004b46e2bb42a0e6cad59cfc4.
- Superseding gate: gates/pr-5253-cleanup-repair-round-3.md. The original PR remains a contributor-owned PR; no public write is part of this repair.

## Decision 1: the count oracle was wrong; no version-assignment change

Version 0 in the buffered ADD envelope is distinct from a missing document version. The existing code path at H, unchanged by I, is:

1. DistributedUpdateProcessor.versionAdd, lines 312-325, resolves versionOnUpdate from cmd.getVersion(), then the document's _version_, then the request parameter.
2. The cross-collection leader buffering branch, lines 396-406, writes the command before the later cmd.setVersion calls. Thus the ADD envelope can contain 0 while the serialized document still contains its source _version_. This does not imply that DELETE or every arbitrary zero-version update is valid.
3. UpdateLog.convertTlogEntryToAddUpdateCommand, lines 2537-2549, reconstructs both the serialized document and the envelope version. LogReplayer, lines 2321-2332, executes every ADD record with REPLAY flags, rather than iterating the distinct-version set.
4. On replay, versionAdd recovers the nonzero version from the document when the command version is zero; doVersionAdd's replica/replay branch sets that resolved version on the command. leaderLogicWithVersionIntegrityCheck, lines 1147-1153, still rejects a genuinely missing replay version.
5. RecentUpdates.getBufferUpdates is a Set of envelope versions. Its size is not the count of buffered records, and repeated zero ADD envelopes collapse there. It was the wrong oracle for this test.

I uses the existing solr_core_update_log_buffered_ops gauge through SolrMetricTestUtils. UpdateLog.computeBufferedOps counts bufferTlog.numRecords() minus the header while BUFFERING; TestRecovery.testBuffering already uses this metric as an operation count. No production versioning or UpdateLog API was changed.

The assertion still requires exactly three buffered operations. Existing document-update, delete, partial-data and routing assertions remain. It now also records the positive source versions via RTG and requires exact matching positive target versions after replay for both ADDs. The test is stronger than merely accepting a lower distinct-version count. Runtime confirmation of these assertions remains required from Linux; the code analysis is not a green test result.

## Decision 2: clean up AssertionError and report a failed command; do not catch all Error

MigrateCmd's protected migration phase now catches Exception or AssertionError. After a confirmed installation it performs the same guarded routing restoration/replay. The original AssertionError is retained as the cause of a SERVER_ERROR SolrException. This is necessary as well as cleanup: allowing the Error to escape would still make the observed Overseer runner reset/retry the task instead of recording the requested failure.

The cleanup helper accepts Throwable as its original-failure container, but catches only Exception or AssertionError. A restoration assertion is suppressed on the original failure and stops cleanup before any target request. A replay-request assertion is likewise suppressed. InterruptedException handling remains intact.

There is intentionally no catch(Error) or catch(Throwable) recovery block. Fatal resource/JVM errors, and other unhandled Error subclasses, propagate. Network requests, logging, allocation and replay are not promised after OutOfMemoryError or StackOverflowError. The Reference Guide makes the fatal-error limit explicit. This patch does not promise automatic recovery from every Error or unconfirmed routing installation.

New cloud regression testRetryAfterMigrateAssertionFailure injects a one-shot AssertionError after rule installation. It requires the original failure marker to return, one hook invocation, an ACTIVE target, removal of the failed rule, and a successful manual retry with the migrated document. The one-shot hook also bounds the negative proof at H: its automatic retry reaches the existing buffer rejection rather than repeatedly injecting assertions.

Two unit methods cover assertion failures during routing cleanup and replay-request setup, preserving original/suppressed failures. The existing three failure/timeout/interruption methods remain; the no-target-request verification now precedes suppressed-array checks so the prerequisite mutation has a direct unwanted-request assertion.

## Scope and evidence

Four files changed above H: MigrateCmd.java, MigrateRouteKeyTest.java, MigrateCmdCleanupTest.java, and collection-management.adoc. Three test methods were added, bringing the focused suite to eighteen (8 cloud, 5 cleanup, 5 routing-mutator). The compile repairs, prior routing restoration, replay behavior and contributor history remain intact.

Read the actual failed receipt; it reports fifteen tests at H, fourteen passing and one failing, with clean tidy/diff/compile. Its two extra cloud seeds, neighbors, root check, guide link check and negative legs are uncompleted. The host-local raw directory /workspace/gates/logs/vm2/solr18601-cleanup/ is not accessible from Windows; this report does not claim inspection of its raw logs/XML beyond the board receipt's account.

Windows verification: ticket diff and call signatures reviewed, git diff --check clean, only four intended outbound files, commit/push and exact remote read-back. No Gradle, formatter, compiler or tests ran on Windows. Eighteen fresh positive tests and all outstanding/expanded proof legs must pass at I (or a separately pinned repair head) before GREEN. The new job adds H-plus-new-cloud-harness negative proof for the assertion failure, retains original-O cleanup regressions, and expands the prerequisite mutation to the fourth removal-assertion case.

No PR comment, body edit, JIRA write, force-push or contributor-branch change was made. The receipt documenting H's failure remains preserved; the gate host owns its receipts.
