# Replication and backup convergence, round 1

Verdict: **NOT CONVERGED.** Eight of the nine drafts are NOT CONVERGED. SOLR-17287 is CONVERGED with notes only.

Counts: drafts checked 9. Drafts with no finding 0. Drafts with a blocking finding 2 (SOLR-8430, SOLR-9865). Drafts with should-fix findings and no blocking finding 6 (SOLR-9091, 9382, 9598, 11650, 12085, 12246). Drafts with notes only 1 (SOLR-17287). Findings 23: 2 blocking, 11 should-fix, 10 note. Register IDs RB-R1-01 to 15 come from one subagent (slice 1), and RB-R1-21 to 28 from another (slice 2).

Scope: `assignments/pool-replication-backup-convergence-round-1.md`, with the answers in `material/replication-backup-round-1-answers.md`. Settled decisions were not reopened. Every head was checked with `git ls-remote origin refs/heads/<branch>`, and all nine match the heads the drafts name. No draft is on hold. Merge-bases were computed against the local `upstream/main`. Draft edits are for the main side. This round wrote no draft.

## Verdict per draft

- **SOLR-8430: NOT CONVERGED.** RB-R1-01 (blocking), RB-R1-02, RB-R1-03 (should-fix).
- **SOLR-9091: NOT CONVERGED.** RB-R1-04, RB-R1-05 (should-fix); RB-R1-14 (note).
- **SOLR-9382: NOT CONVERGED.** RB-R1-06, RB-R1-07 (should-fix); RB-R1-14 (note).
- **SOLR-9598: NOT CONVERGED.** RB-R1-08, RB-R1-09 (should-fix); RB-R1-15 (note).
- **SOLR-9865: NOT CONVERGED.** RB-R1-10 (blocking); RB-R1-11, 12, 13 (note).
- **SOLR-11650: NOT CONVERGED.** RB-R1-23 (should-fix); RB-R1-24, 25 (note).
- **SOLR-12085: NOT CONVERGED.** RB-R1-22 (should-fix).
- **SOLR-12246: NOT CONVERGED.** RB-R1-21 (should-fix); RB-R1-28 (note).
- **SOLR-17287: CONVERGED.** RB-R1-26, 27 (note).

## Register

Blocking and should-fix rows carry the replacement text the subagent gave. Note rows are summarized.

**RB-R1-01 | SOLR-8430 | blocking.** Limits bullet 1: "Limiters stay in memory for the life of the node, one for each rate value that a request has used." The static map in `ReplicationAPIBase.java` (lines 98 to 110 at `49af21be`) is never pruned. The answers say the defect is in this branch's own new code and is fixed here, not shipped as a Limits line. Replacement, once the cap lands: delete the bullet. Add to the body: "The map holds at most 64 rate values, so a client cannot grow it without limit." Replace the bullet with: "Past 64 distinct rate values, a new stream gets its own limiter and is not shared." Re-point the code links and Proof to the new head after the focused re-run.

**RB-R1-02 | SOLR-8430 | should-fix.** Proof: "On the base code the test fails to compile, because `rateLimiterFor` is added by this change." It repeats the bold summary. Replacement: delete the sentence and keep "This is a new-API check, so it is inconclusive by construction."

**RB-R1-03 | SOLR-8430 | should-fix.** The changelog fragment at `49af21be` line 1 says the throttle is "shared by all concurrent file requests on a node". That contradicts the draft's summary and the code, which shares only equal rates. Branch edit: "Replication maxWriteMBPerSec throttling is shared by concurrent file requests on a node that use the same rate, instead of each request getting its own limiter."

**RB-R1-04 | SOLR-9091 | should-fix.** The fragment at `e6bfa5c` line 2 says a corrupt backup file "fails the restore (and rolls back) instead of being switched in silently." The draft says the restore fails before the switch. The check sits in the download copy (`RestoreCore.java` lines 180 and 187), and the throw at lines 189 to 192 comes before the switch. Branch edit: "Replication-handler restore now verifies every file copied from the backup against the checksum in its footer, so a corrupt backup file fails the restore before it is switched in."

**RB-R1-05 | SOLR-9091 | should-fix.** The Limits link (line 30) points at `e6bfa5c` `RestoreCore.java` lines 335 to 338 as pre-change code, without saying so. Replacement: "(at the merge-base, [RestoreCore.java](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/handler/RestoreCore.java#L316-L327))."

**RB-R1-06 | SOLR-9382 | should-fix.** Line 15 links `ReplicationHandler.java` L810 to L843 at `2952bae`, with the display and the anchor disagreeing (L806 to 809 is the javadoc). Change the anchor to `#L810-L843`.

**RB-R1-07 | SOLR-9382 | should-fix.** Line 9 omits `.json`. `ManagedResourceStorage.java` line 445 at `cabedd1d` adds `.json`. Replacement: "...each file name is the resource ID with `/` replaced by `_`, plus `.json`."

**RB-R1-08 | SOLR-9598 | should-fix.** Proof: "passed three times at this head." The receipt says "passes under three seeds including that one." Replacement: "`TestLocalFSCloudBackupRestore` passed under three seeds at this head, including the seed that failed on the base code."

**RB-R1-09 | SOLR-9598 | should-fix.** Proof: "The result depends on timing, so the base failure is not deterministic, and this proof does not claim one." It repeats the bold summary. Delete the sentence.

**RB-R1-10 | SOLR-9865 | blocking.** "What this change does", the Choice and the Proof present the previous-name write-back (commit `cd46e4a3521`, an ancestor of the head) as this PR's change. The assignment says 18532 lands first and 9865's branch drops its overlapping hunk at packaging. The live PR for 18532 says it will drop its rollback edit later, and this draft never names that PR. Hold posting until the packaging call is made. Choice: "Three routes are open." and add a third route on PR #5100. Limits: "If PR #5100 lands first, its change covers this rollback, and this change keeps only the regression test." If the edit is dropped, What-this-change and the Proof must be re-derived at the packaged head.

**RB-R1-11 | SOLR-9865 | note.** Line 9 "the rollback deletes index.properties" has no citation. The merge-base line `RestoreCore.java` 227 is unconditional. Optional citation.

**RB-R1-12 | SOLR-9865 | note.** Line 38 "so this is the only form it can hold" is overstated. Optional: "index.properties names directories inside the data directory, so a directory name is the form it stores."

**RB-R1-13 | SOLR-9865 | note.** About 4,250 characters, over the 3,500 guide. The answers do not cover this draft. Main side to accept as complex or trim.

**RB-R1-14 | SOLR-9091, SOLR-9382 | note.** Length: 9091 about 3,680 characters; 9382 about 3,600. Main side to record.

**RB-R1-15 | SOLR-9598 | note.** The owner note in the draft asks to decide landing order, and item 2 cites the wrong report finding. The answers adopt 9598 before SOLR-12651, so the order item is stale. When the javadoc or changelog commit lands, add: "Only a comment changed since the verified run."

**RB-R1-21 | SOLR-12246 | should-fix.** Line 9: "Before this change, a file whose checksum differs is logged at WARN." The links point at `14c7aac0`, the merge-base, and the text does not say so. Replacement: "On the merge-base commit, a file whose checksum differs is logged at WARN." and "On the same method at the merge-base commit, a file with no checksum...".

**RB-R1-22 | SOLR-12085 | should-fix.** Line 9: "Before this change, with `SolrDeletionPolicy` set to keep more than one commit". The links are merge-base links and the text does not say so. Replacement: "On the merge-base commit, with `SolrDeletionPolicy` set to keep more than one commit".

**RB-R1-23 | SOLR-11650 | should-fix.** (a) Line 9: "Before this change, the replication details response returns that value as configured." Replacement: "On the merge-base commit, the replication details response returns that value as configured." (b) The line 31 bullet: "The "Leader at ... is not available" warnings print the leader URL as configured." Replacement: "On the merge-base commit, the "Leader at ... is not available" warnings print the leader URL as configured, and this branch does not change them."

**RB-R1-24 | SOLR-11650 | note.** Optional: the receipt has a base run for the details case (2026-10-11, 1 of 1). The Proof could add: "On the merge-base code, the details case also fails with the password visible."

**RB-R1-25 | SOLR-11650 | note.** Line 21 "The redaction test fails on the base code" should say "The two redaction tests", and "in both messages" should say "in both error messages".

**RB-R1-26 | SOLR-17287 | note.** About 4,220 characters. The answers do not list this draft under the length item. Main side to record stand or trim.

**RB-R1-27 | SOLR-17287 | note.** Line 17 "cross-filed" is tracker vocabulary. Optional: "so the change also falls in the Update processing and atomic updates areas."

**RB-R1-28 | SOLR-12246 | note.** Line 29 "matches the length-only path" uses a different name from the draft's own line 9. Optional: "matches the no-checksum branch directly above it, which already logs at INFO."

## Status items, not graded

- SOLR-17287: the draft has no `[CONFIRM: count]` text. The Proof reads "TestRestoreCore 4 of 4", and the receipt records a 2026-10-11 focused run at 4 tests. The count question stays open on the main side, as the answers record.
- SOLR-9865: the same open count note applies. No placeholder remains in the draft. The JUnit XML is not on disk, so the count is not settled here.
- SOLR-12085: the changelog title at `c8dba50` still says "on every replication". When the title commit lands, re-point the Proof and the changelog link.
- SOLR-11650: the owed title, javadoc and test-comment commits have not landed. Hold posting until they do.
- Receipts (not draft findings): `receipts/SOLR-17287.md` still carries a line the answers ask to correct. `receipts/SOLR-11650.md` labels the merge-base `e044bf20b405`; `git merge-base` gives `14c7aac0`, and the files are identical at both.

## Not checked

- JIRA ticket text, so ticket-sourced quotes are not checked.
- `g9865-premise.log` and `g9865-premise2.log`, which are not in this worktree.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Two blocking and eleven should-fix findings are open.
