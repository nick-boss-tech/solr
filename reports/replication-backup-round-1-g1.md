# Replication and backup round 1, part g1 (RestoreCore cluster)

Result: SOLR-9091 is audit only (premise holds, no gate, not drafted). SOLR-9865 and SOLR-17287 are draftable but held: the TestRestoreCore counts in the receipts do not match the head files, and the 9865 test comment states a mechanism the code does not show. The three RestoreCore diffs do not conflict. Landing order: 9865, then 17287, then 9091.

Heads checked with `git rev-parse --short origin/...` on 2026-10-10: solr-9091-submit e31bdaa4d27, solr-9865-submit 4937608bb18, solr-17287-submit 6957daf8261. All three match the claim table. No tip moved.

## Findings

1. FIX. `solr/core/src/test/org/apache/solr/handler/TestRestoreCore.java` lines 237-238 at 9865 tip (4937608bb18). The comment says the running core keeps its in-memory index directory, so the rollback only takes effect on reopen. Evidence: upstream/main `DefaultSolrCoreState.java` line 257 (`createMainIndexWriter`) passes `core.getNewIndexDir()`, which reads index.properties (`SolrCore.java` lines 404-415 and 441-449). `SolrCore.java` line 2300 reads `getNewIndexDir()` in `openNewSearcher`, and lines 2373-2412 open the searcher on that directory. The code as read does not show the stated mechanism. Receipt SOLR-9865 line 7 uses the same wording. The premise logs are not on disk, so this is unconfirmed. Exact replacement for the comment: `// reload the core so the check below reads index.properties on a fresh open`. The drafts state no mechanism.

2. FIX. Receipt counts. SOLR-9865 receipt line 6 says "TestRestoreCore 4" (11 of 11 in total). SOLR-17287 receipt line 6 says "TestRestoreCore 4 of 4". Each head file has 3 `@Test` methods. For 9865: lines 90, 182, 245 (testSimpleRestore, testFailedRestoreAfterSuccessfulRestoreKeepsCurrentIndex, testFailedRestore). For 17287: lines 90, 173, 269 (testSimpleRestore, testRestoreClearsUpdateLog, testFailedRestore). upstream/main has 2. The gate JUnit XML would show which four ran, and it is not on disk. Replacement: in both drafts, the count is `[CONFIRM: count]` until the XML is checked. Change receipt "TestRestoreCore 4" to the confirmed number. The 9865 total of 11 depends on it.

3. FIX. `SOLR-9091-TESTING.md` line 18 says "`doRestore` already rolls back to the live index on failure." Evidence (9091 tip, `RestoreCore.java`): the corrupt-file check runs in the download task (lines 180 and 187). `future.get()` (line 201) rethrows a RuntimeException (line 209) before the switch at line 219. The rollback block (line 230 onward) is never reached, so the index is never switched. Replacement: "A corrupt file fails the download step before the index is switched, so the live index is not touched." Same fix for the test comment at `TestRestoreCore.java` line 295 (9091 tip): replace "the failed restore rolled back to the live index" with "the failed restore left the live index in place."

4. NOTE. 9091 `verifyRestoredFile` (`RestoreCore.java` lines 104-109, 9091 tip) checks the copied file's own footer with `CodecUtil.checksumEntireFile`. It does not compare against the checksum recorded in the backup. The ticket's complaint is the swallowed backup-side checksum (upstream `RestoreCore.java` line 323: "Could not read checksum from index file", returns null). The branch does not change that swallow. Wording for any future draft: "checks each copied file against the checksum in its own footer."

5. NOTE. Interactions (RestoreCore.java, tips). 9091 adds a helper and two calls at lines 104-109, 180 and 187, all before the switch. 9865 changes lines 208-210 and 232-236, on the failed-switch path only. 17287 adds lines 250-255, on the successful-switch path only. The hunks share no lines. Pairwise trial merges (`git merge-tree --write-tree`) are clean for 9091+9865, 9091+17287 and 9865+17287. No contradiction: 9865 writes the previous directory back on a failed switch, and 17287 clears the log only after a successful switch. Together they are consistent. After restore A succeeds and clears the log, a failed restore B rolls back to A, so updates logged after A still apply to the right directory. 9091 fails before the switch, so neither path sees its failures. Three-way merge was not run as one tree (merge-tree needs commit objects).

6. NOTE. Landing order: 9865 first (gated, 3 files, smallest RestoreCore change). 17287 second (gated, adds one UpdateLog method and a test resource). 9091 last (no gate, needs a focused proof run with fail-before, and its handoff file removed). The text merges do not depend on order.

7. NOTE. `SOLR-9091-TESTING.md` is still at the root of 9091 tip e31bdaa4d27. 9865 and 17287 removed their handoff files. Remove it on the ticket branch before any PR.

8. NOTE. 17287 cleanup order. `RestoreCore.java` line 245 (`deleteNonSnapshotIndexFiles`, declared `throws IOException` at `SolrCore.java` line 683) runs before the clear at lines 250-255. If the cleanup throws after the switch, the clear is skipped. Drafted as a Limits line. Owner decides whether to move the clear above line 245.

9. NOTE. 17287 receipt line 8 says "UpdateLog.discardLog no longer force-closes cleared logs." That describes an intermediate commit. Against e2cdb2d7e8a the diff only adds lines: `discardLog` is a new private helper (`UpdateLog.java` line 2074), and no existing UpdateLog method changes. Wording for the receipt: "discardLog releases the log's own reference; the file is deleted when the last reader releases it." The draft describes the net change.

10. NOTE. Premise checks against the public tickets (read by WebFetch, no token). SOLR-9091 (opened 2016-05-09, Open, unassigned) says backup-side checksum read errors are swallowed and backup-only files are copied without a checksum. Matches the base code. SOLR-9865 (created 2016-12-14, Open) says deleting index.properties rolls back to `index` instead of `index.timestamp1`. Matches. The ticket also proposes auto-discovery of the newest index directory when index.properties is missing. That is a live alternative, used in the 9865 draft's choice section. SOLR-17287 (created 2024-05-11, Open) asks for the update log to be cleared on a core restore. The branch limits this to standalone cores, which the draft states as a choice.

## Task results

**SOLR-9091.** Audit only, no gate. Premise holds: upstream `RestoreCore.java` line 175 copies backup-only files with no check, and line 323 swallows the checksum read error. The footer check is a sound direction, but nothing has run. The TESTING.md guesses are still open, including whether every backup index file carries a Lucene footer and whether the byte flip is large enough to discriminate. Not drafted. Owed: a focused proof through the queue with fail-before, plus Findings 3, 7 and 4 fixed. Verdict: audit only.

**SOLR-9865.** Draftable, held. Draft at `pr-drafts/replication-backup/SOLR-9865.md`, written against head 4937608bb181. Holds: the TestRestoreCore count (Finding 2) and the test comment (Finding 1). After both, PR-ready. The choice section offers the ticket's auto-discovery alternative. Verdict: hold for two confirmations.

**SOLR-17287.** Draftable, held. Draft at `pr-drafts/replication-backup/SOLR-17287.md`, written against head 6957daf8261. Cross-area: `UpdateLog.java` has additions only, and the draft says so. UpdateLogTest 6 of 6 matches the head file (6 `@Test`). Holds: the TestRestoreCore count (Finding 2). Limits line covers Finding 8. Verdict: hold for one confirmation.

## Owner decisions

1. Confirm the TestRestoreCore counts for 9865 and 17287 from the gate JUnit XML, or correct the receipts (Finding 2).
2. 9865 test comment: adopt the replacement in Finding 1, or confirm the mechanism from the premise logs.
3. 9865 choice: keep write-back (drafted), or put the ticket's auto-discovery route to maintainers.
4. 17287: keep the clear after cleanup (drafted, with a Limits line), or move it above line 245 (Finding 8).
5. 17287: keep SolrCloud unchanged as a choice question (drafted), or drop the choice section.
6. 9091: remove the handoff file and schedule a focused proof with fail-before. Not in this round's drafts.
7. Landing order: 9865, then 17287, then 9091.

## Not checked

- No builds, tests or gate runs. Gate and premise logs (`g9865-gate.log`, `g9865-premise.log`, `g9865-premise2.log`, `g17287fix-gate.log`) were not found under `C:\Users\shaninna\dev\Solr-issues` to depth 4 outside `wt`. Not searched deeper.
- Receipt counts for TestReplicationHandlerBackup (2), TestSnapshotCoreBackup (5) and the 9865 total of 11 were not verified.
- The 9865 mechanism (Finding 1) is not confirmed. Tracing it needs a run.
- Whether the 9865 rollback changes SolrCloud restore behavior was not traced beyond the shared method.
- Whether every file in a backup index directory has a Lucene footer (9091 TESTING.md guess 1).
- Three-way trial merge as one tree was not run; pairwise only.
- The apache-jira MCP server is not in this session's toolset. Ticket text was read from the public Jira pages with WebFetch.
- Drafts name no Lucene version.
- Live PR state is part g4's, not this part's.
