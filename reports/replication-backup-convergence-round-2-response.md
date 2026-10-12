# Replication and backup convergence, round 2: main-side response

Round 2 returned NOT CONVERGED: 4 of the 8 re-checked drafts converge on their text (SOLR-9382, 9598, 9865, 12246) and 4 do not (SOLR-8430, 9091, 11650, 12085). Its new findings are 3 should-fix (RB-R2-01, RB-R2-21, RB-R2-22) and 2 notes (RB-R2-02, RB-R2-03). This lane verified every round 2 finding against the receipts and the branch code before applying anything. RB-R2-21 and RB-R2-22 hold and are applied in the register's exact replacement text. RB-R2-01 holds as a statement about the post-cap text but is NOT APPLIED: like RB-R1-01, it applies in the same pass as the SOLR-8430 cap gate on vm2, and that gate has not closed, so the draft stays held and neither replacement is forced in early. No finding is rebutted this round. The SOLR-8430 state also moved after the round read it, and the moved state is recorded below. Application method: exact-string replacements, each asserted to occur exactly once before applying; the full diff was then reviewed file by file.

## Counts

| Grade | Round 2 new items | Applied in this lane | Held on the SOLR-8430 cap gate | Record items |
|---|---|---|---|---|
| Should-fix | 3 (RB-R2-01, 21, 22) | 2 (RB-R2-21, 22) | 1 (RB-R2-01) | 0 |
| Note | 2 (RB-R2-02, 03) | 0 | 0 | 2 |
| Carried | RB-R1-01 (blocking), RB-R1-03, RB-R1-04 | 0 | RB-R1-01 | RB-R1-03 landed branch-side; RB-R1-04 still open |

After application, 6 of the 8 re-checked drafts are CONVERGED on their text. SOLR-8430 is not (held on its cap gate) and SOLR-9091 is not (RB-R1-04 open).

## Fact checks before applying

- **SOLR-12085 (RB-R2-22):** the branch's merge-base is `14c7aac0d151`. In `IndexFetcher.java` there, the wait loop (lines 639 to 648) runs inside `if (!isFullCopyNeeded && !fetchFromLeader)` at line 621. The draft's bold opener stated the 30-second wait for "each replication" without the condition its own next paragraph states.
- **SOLR-11650 (RB-R2-21):** the draft opened with the PR-text marker and carried no OWNER NOTE and no hold, while the round 1 response and the answers record the posting hold as standing. `git ls-remote` shows `solr-11650-submit` still at `e4f5e941cd8e88524196b03f2022261c2215c3ba`, the head the register's note text names; the owed title, javadoc and test-comment commits have not landed.
- **SOLR-8430 (RB-R1-01, RB-R1-03, RB-R2-01):** the state moved after round 2 read it. `git ls-remote` shows `solr-8430-submit` at `0d64c35c5a7d`. At that head, `ReplicationAPIBase.java` carries the cap (`MAX_SHARED_RATE_LIMITERS` of 64, with a private limiter past the cap), the changelog fragment carries RB-R1-03's replacement title word for word, and a second commit resets the shared limiters per test. `gates/SOLR-8430.md` records the gate at the implementation head `094f0716a8` as FAILED (2026-10-12T04:12:44Z): the focused run executed and one test failed, a test fixture defect (order dependence on the static limiter map, seed 8430C0FFEE008430), classified and recorded by vm2 with the fixture fix prescribed; the claim `claims/pool-vm2-gate-backlog-8430.md` stands open at FAILED and the re-gate at the new head has not run. The cap premise leg failed by content, as designed.
- **SOLR-9091 (RB-R1-04):** the fragment at `e6bfa5c62681` still says a corrupt file "fails the restore (and rolls back)". The retitle has not landed.
- **SOLR-9598 (RB-R2-03):** at `c8407773`, `RestoreCmd.java` (lines 315 to 319) says `waitForFinalState=false` "opts out", while `CreateCollectionRequestBody.java` (lines 42 to 47) deprecates that parameter saying Solr is moving toward always waiting with no option to opt out. The changelog fragment still says "behaviour" in its title; the answers adopt "behavior".

## Per-finding dispositions

### SOLR-8430
- RB-R1-01 (blocking): still open, HELD on the cap gate. The cap code has landed, but the gate at the implementation head failed on a test fixture defect and the re-gate at the fixture-fixed head `0d64c35c5a7d` is outstanding under vm2's open claim. The draft's post-cap text (the Limits bullet replacement and the re-pointed links and Proof) applies in the same pass that closes that gate, not before.
- RB-R2-01 (should-fix): NOT APPLIED, held with RB-R1-01. Its three replacements describe the capped limiter and are true only once the cap is gated; they join the same pass.
- RB-R1-03 (should-fix, branch-side): LANDED. The fragment title at the live tip is the replacement recorded in the round 1 response, word for word. The draft's own references move with the post-cap pass above.
- RB-R1-02: confirmed by round 2; no further action.
- The draft stays HELD. NOT CONVERGED.

### SOLR-9091
- RB-R1-04 (should-fix, branch-side): still open. The branch replacement is recorded in the round 1 response for the packaging lane. NOT CONVERGED until it lands; round 2's dispute of the round 1 CONVERGED label is accepted.
- RB-R2-02 (note): stands as the conditional record. When the RB-R1-04 title commit lands, the `e6bfa5c` links at lines 15, 23 and 33 are re-pointed and line 23 gains "Only the changelog title changed after this verified run.", in that pass.

### SOLR-11650
- RB-R2-21 (should-fix): applied. The draft now opens with the register's OWNER NOTE in the sibling drafts' form (the note, then the separator, then the PR text), naming the owed commits and the posting hold. The draft is CONVERGED on its text; the posting hold stands until the owed commits land, and the citations and Proof are re-pointed in that pass.

### SOLR-12085
- RB-R2-22 (should-fix): applied. The bold opener now scopes the wait to a replication that is not already a full copy and does not use fetchFromLeader, in the register's exact replacement. The draft is CONVERGED on its text; the branch-side changelog title is still owed, and per the round it should carry the same scope when it lands.

### SOLR-9598
- RB-R2-03 (note): recorded here as unlanded branch-side state, as the round asks. Two items at `c8407773`: the `RestoreCmd` javadoc at lines 315 to 319 (replacement text in `reports/replication-backup-round-1-g3.md` finding 6) and the changelog fragment's "behaviour" spelling. Neither is a draft-text item. The draft stays CONVERGED on text, with its owner hold standing.

### SOLR-9382, SOLR-9865, SOLR-12246
- Round 2 confirms the round 1 dispositions on the live text; no new findings. CONVERGED (9865 on text, its posting hold standing until the packaging call).

## Record notes from the round, dispositioned

- `gates/SOLR-9091.md` line 5 says the packaging commit is local only and not pushed. `git ls-remote` shows `solr-9091-submit` at `e6bfa5c62681`, so the packaged head is pushed; the gate file's line is stale. The gate file is the gating host's record and is not edited by this lane; the correction is recorded here.
- The answers say no SOLR-9382 draft exists this round. `pr-drafts/replication-backup/SOLR-9382.md` exists and round 2 confirmed it; the answers line is superseded, as are its NO GATE lines for SOLR-9091 and SOLR-9382 (receipts record green gates at `e6bfa5c` and `2952bae`).
- The SOLR-9865 packaging call: the round notes the SOLR-18532 pull request must be read before that call is made. That reading is part of the call itself, which the draft's OWNER NOTE already gates; the call stays with the main side and is not a draft finding.
- The URLUtil javadoc owed on `solr-11650-submit` (lines 110 to 112) says a raw "#" ends the authority; the redaction pattern does not stop at "#". The draft makes no "#" claim. Recorded for the branch lane that lands the owed javadoc replacement: that replacement should drop the "#" sentence, or the pattern should change. No draft edit follows.

## Verdicts after application

CONVERGED: SOLR-9382, 9598 (text; owner hold stands), 9865 (text; posting hold stands), 11650 (text; posting hold now marked in the draft), 12085 (text; branch-side title owed), 12246.
NOT CONVERGED: SOLR-8430 (RB-R1-01 and RB-R2-01 held on the vm2 cap gate re-run), SOLR-9091 (RB-R1-04).

## What round 3 needs to settle

Round 3 re-checks only the two drafts still open (SOLR-8430, 9091): for SOLR-8430, whether the cap gate has closed green at the live tip with the receipt refreshed (and, if so, the draft against the RB-R1-01 and RB-R2-01 post-cap text, which the main side applies in that pass); for SOLR-9091, whether the RB-R1-04 fragment retitle has landed (and the RB-R2-02 link and sentence updates with it). No item was rebutted this round, so round 3 has no rebuttal to judge. The assignment is `assignments/pool-replication-backup-convergence-round-3.md`.
