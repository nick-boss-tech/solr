# Replication and backup convergence, round 1: main-side response

Round 1 returned NOT CONVERGED: 8 of the 9 drafts were NOT CONVERGED, with 2 blocking, 11 should-fix and 10 note findings. This lane verified every finding against the receipts and the branch code before applying anything, applied each holding finding's exact replacement text to its draft, and dispositions the rest below. Two should-fix findings (RB-R1-03, RB-R1-04) hold but their replacement text edits a branch changelog fragment rather than the draft; they are recorded here verbatim for the branch lanes. One blocking finding (RB-R1-01) holds as a defect but its replacement is written for a branch state that does not exist yet; it stands open, and the reason is written out. The round's two receipt items were verified and are corrected in this lane. Application method: exact-string replacements, each asserted to occur exactly once before applying; the full diff was then reviewed file by file.

## Counts

| Grade | Items | Applied in this lane | Branch-side, recorded below | Not applied |
|---|---|---|---|---|
| Blocking | 2 | 1 (RB-R1-10) | 0 | 1 (RB-R1-01, precondition unmet) |
| Should-fix | 11 | 9 | 2 (RB-R1-03, RB-R1-04) | 0 |
| Note | 10 | 6 (RB-R1-12, 15 in part, 24, 25, 27, 28) | 0 | 4 standing |

After application, 7 of the 9 drafts are CONVERGED on their text. SOLR-8430 is not (RB-R1-01 open). SOLR-9865's text is converged and its posting hold now stands in the draft itself.

## Fact checks before applying

- **SOLR-8430 (RB-R1-01):** `ReplicationAPIBase.java` at `49af21be58927` (lines 98 to 110) holds a static `ConcurrentHashMap` of limiters, filled by `computeIfAbsent` and never pruned. The defect holds, and `material/replication-backup-round-1-answers.md` adopts fixing it in this branch, not shipping it as a Limits line. But the register's replacement is written for the capped head ("once the cap lands"), and the cap has not landed: `git ls-remote` shows `solr-8430-submit` still at `49af21be58927`, and the cap job `gates/SOLR-8430.md` is open. The draft's first Limits bullet still describes the uncapped branch accurately today.
- **SOLR-8430 (RB-R1-03):** the fragment at `49af21be58927` is titled "Replication maxWriteMBPerSec throttling is shared by all concurrent file requests on a node instead of being applied separately to each request". The code shares only equal rates, so the title contradicts the change it ships with.
- **SOLR-9091 (RB-R1-04):** the fragment at `e6bfa5c62681` says a corrupt file "fails the restore (and rolls back) instead of being switched in silently". At that head, `verifyRestoredFile` runs inside the download copy (RestoreCore.java lines 180 and 187) and the throw at lines 189 to 192 comes before the switch, so no rollback is involved.
- **SOLR-9382 (RB-R1-06, 07):** at head `2952bae9d392`, lines 806 to 809 of `ReplicationHandler.java` are the new method's javadoc and the method starts at line 810. `ManagedResourceStorage.java` line 445 at the merge-base `cabedd1d968` returns `resourceId.replace('/', '_') + ".json"`.
- **SOLR-9598 (RB-R1-08):** receipts/SOLR-9598.md records that "at the head the class passes under three seeds including that one", matching the replacement word for word in substance.
- **SOLR-9865 (RB-R1-10):** `git merge-base --is-ancestor cd46e4a3521 4937608bb181` confirms the previous-name write-back commit is an ancestor of the head, and the draft presented it as this change's own work with no mention of the overlapping SOLR-18532 change. The recorded decision (18532 lands first; this branch drops the overlapping hunk at packaging) is now stated in the draft's Choice, its Limits and an OWNER NOTE hold.
- **SOLR-11650 (RB-R1-23):** `git merge-base` for the branch returns `14c7aac0d151`, the commit the draft's pre-change links use.
- **Receipt item, SOLR-11650:** the receipt's base-run line called `e044bf20b405` the merge-base. `git merge-base` gives `14c7aac0d151`, and `IndexFetcher.java`, `ReplicationHandler.java` and `URLUtil.java` are identical at both commits (empty diff), so the run's facts stand and only the label was wrong. Corrected in receipts/SOLR-11650.md in this lane.
- **Receipt item, SOLR-17287:** the receipt's "UpdateLog.discardLog no longer force-closes cleared logs" described an intermediate commit. The branch diff for `UpdateLog.java` against `e2cdb2d7e8ae` has zero deleted lines: `clearAndActivate` and `discardLog` are additions only, as the draft already states. Corrected in receipts/SOLR-17287.md in this lane, using the wording the round 1 audit (part g1, finding 9) recorded.
- **Status item, the TestRestoreCore count (SOLR-9865, SOLR-17287):** settled on the main side. Both receipts record a focused run at the head on 2026-10-11 from fresh JUnit XML: 4 tests, 0 failures; the fourth testcase, `testBackupFailsMissingAllowPaths`, has no `@Test` annotation and runs by its test-name prefix. Both drafts state 4 of 4 and carry no placeholder. No draft edit follows.

## Per-finding dispositions

### SOLR-8430
- RB-R1-02 (should-fix): applied (the compile sentence deleted; the bold summary already carries it).
- RB-R1-01 (blocking): NOT APPLIED, stands open. See the fact check: the replacement (delete the Limits bullet, add "The map holds at most 64 rate values...", replace the bullet with "Past 64 distinct rate values, a new stream gets its own limiter and is not shared.", re-point links and Proof) applies in the same pass that lands the cap and its focused re-run, which the answers record assigns to the gate job. The draft stays HELD; its OWNER NOTE already names the cap as posting step (1).
- RB-R1-03 (should-fix): holds, branch-side. Branch replacement, recorded for the branch lane (the answers adopt this retitle): "Replication maxWriteMBPerSec throttling is shared by concurrent file requests on a node that use the same rate, instead of each request getting its own limiter."

### SOLR-9091
- RB-R1-05 (should-fix): applied (the Limits link now points at the merge-base lines 316 to 327, the same lines What happens today cites).
- RB-R1-04 (should-fix): holds, branch-side. Branch replacement, recorded for the packaging lane: "Replication-handler restore now verifies every file copied from the backup against the checksum in its footer, so a corrupt backup file fails the restore before it is switched in."
- RB-R1-14 (note): stands. The length (about 3,680 characters) is recorded and accepted; the draft sits in the RestoreCore landing order (9865, 17287, 9091) as the answers adopt.

### SOLR-9382
- RB-R1-06, RB-R1-07 (should-fix): applied.
- RB-R1-14 (note): stands, as for SOLR-9091 (about 3,600 characters).

### SOLR-9598
- RB-R1-08, RB-R1-09 (should-fix): applied.
- RB-R1-15 (note): applied in part. The OWNER NOTE's landing-order item is updated to the adopted order (9598 before SOLR-12651, per the answers); the note's other point, that the OWNER NOTE's item (2) cites the wrong report finding, names no correct target, so that citation stands for the main side to correct when the hold clears. The conditional sentence ("Only a comment changed since the verified run") applies when the comment commit lands, not before. The draft stays HELD for its remaining owner items.

### SOLR-9865
- RB-R1-10 (blocking): applied. The Choice now opens "Three routes are open." and carries the third route (the SOLR-18532 change in PR #5100, which keeps the previous index.properties content and writes it back on rollback). The Limits gain the prescribed sentence: "If PR #5100 lands first, its change covers this rollback, and this change keeps only the regression test." An OWNER NOTE at the top of the draft now records the posting hold until the packaging call is made, in the same form the SOLR-8430 and SOLR-9598 drafts use. If the write-back is dropped at packaging, What this change does and the Proof are re-derived at the packaged head, as the register states.
- RB-R1-12 (note): applied ("so this is the only form it can hold" replaced with the register's sentence).
- RB-R1-11 (note): stands. The rollback-deletes claim rests on the merge-base `RestoreCore.java` line 227; adding that citation is optional and is left for the packaging pass that re-derives this draft.
- RB-R1-13 (note): stands. The length (about 4,250 characters) is accepted: the draft now carries a three-route Choice over an overlap with a live PR, which is the complex case.

### SOLR-11650
- RB-R1-23 (should-fix): applied, both parts.
- RB-R1-24 (note): applied. receipts/SOLR-11650.md now records the 2026-10-11 base run for the follower-details case, and the Proof gains the register's sentence: "On the merge-base code, the details case also fails with the password visible."
- RB-R1-25 (note): applied ("The redaction test" to "The two redaction tests"; "in both messages" to "in both error messages").

### SOLR-12085
- RB-R1-22 (should-fix): applied.
- Status item (not graded): the changelog title at `c8dba502339` still overstates the change, per the round. Branch-side: when the title commit lands, the draft's Proof and changelog link are re-pointed to the new head. No draft edit until then; the draft is CONVERGED on its text at the current head.

### SOLR-12246
- RB-R1-21 (should-fix): applied, both sentences.
- RB-R1-28 (note): applied ("matches the length-only path" to the register's no-checksum wording, which receipts/SOLR-12246.md also uses).

### SOLR-17287
- RB-R1-27 (note): applied ("cross-filed" tracker vocabulary replaced with the register's sentence).
- RB-R1-26 (note): stands. The length (about 4,220 characters) is accepted: the draft documents a cross-area change (RestoreCore plus UpdateLog) with a SolrCloud Choice.
- The draft was CONVERGED in round 1; its receipt is corrected in this lane (above), and the draft already described the net UpdateLog change correctly.

## Record items

- SOLR-11650's owed title, javadoc and test-comment commits have not landed (`git ls-remote` shows the tip still at `e4f5e941cd8e`); the posting hold from the answers stands. When they land, the draft's citations and Proof head are re-pointed and the moved-head sentence is added, per the answers.
- Landing orders were checked as cross-draft consistency, per the assignment: RestoreCore 9865, then 17287, then 9091; IndexFetcher 12246, then 12085, then 11650, with 6711 last (no draft in this category); 9598 before 12651. No draft contradicts an order; the 9598 OWNER NOTE now records its order instead of asking for the decision.

## Verdicts after application

CONVERGED: SOLR-9091 (branch-side RB-R1-04 owed before opening), 9382, 9598 (text converged; hold stands), 9865 (text converged; posting hold stands until the packaging call), 11650 (text converged; hold stands until the owed branch commits land), 12085 (branch-side title owed), 12246, 17287.
NOT CONVERGED: SOLR-8430 (RB-R1-01 open, pending the cap and its focused re-run).

## What round 2 needs to settle

Round 2 re-checks the 8 drafts round 1 marked NOT CONVERGED (SOLR-8430, 9091, 9382, 9598, 9865, 11650, 12085, 12246), verifies each disposition above against the live draft text, and gives a verdict of CONVERGED or NOT CONVERGED per draft. It re-checks the RB-R1-01 state (whether the cap has landed and the focused re-run is recorded; if so, whether the Limits bullet and body sentence now read as the register prescribes), the branch-side items RB-R1-03 and RB-R1-04 (whether the fragment commits have landed, and the 12085 title commit), and SOLR-9865's hold state against the packaging call. The assignment is `assignments/pool-replication-backup-convergence-round-2.md`.
