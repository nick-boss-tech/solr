# Replication and backup convergence, round 2

Verdict: **NOT CONVERGED.** Four of the eight re-checked drafts converge on their text: SOLR-9382, 9598, 9865 and 12246. Four do not: SOLR-8430, 9091, 11650 and 12085.

Counts: drafts re-checked 8. Dispositions confirmed on the live text: the response's applied items for 9382, 12246, 9598 and 9865 match, and so do the round 1 items on 8430 (RB-R1-02), 9091 (RB-R1-05), 9598 (RB-R1-08, 09) and 11650 (RB-R1-23 to 25). Dispositions disputed: 3. (a) The response labels SOLR-9091 CONVERGED while RB-R1-04 is open. (b) The response labels SOLR-12085 CONVERGED while RB-R2-22 is open. (c) The response says the SOLR-11650 posting hold stands, but the draft carries no hold marker (RB-R2-21). New findings: 3 should-fix (RB-R2-01, 21, 22) and 2 notes (RB-R2-02, 03). Still open should-fix or blocking: RB-R1-01 (blocking), RB-R1-03, RB-R1-04, RB-R2-01, RB-R2-21, RB-R2-22.

Scope: `assignments/pool-replication-backup-convergence-round-2.md`, checked against `reports/replication-backup-convergence-round-1-response.md`. Two read-only subagents covered the drafts in slices (8430, 9091, 9382, 9598 and 9865, 11650, 12085, 12246). Every live head was checked with `git ls-remote origin refs/heads/<branch>`. None is on hold for a head mismatch. Settled decisions and the landing orders were not reopened. The drafts were not edited.

## Verdict per draft

- **SOLR-8430: NOT CONVERGED.** RB-R1-01 (blocking) still open: the limiter cap has not landed, the tip is unchanged at `49af21be`, and `gates/SOLR-8430.md` is unclaimed with no focused re-run recorded. RB-R1-03 (should-fix, branch-side) still open: the fragment title at `49af21be` has not changed. RB-R2-01 (should-fix) applies when the cap lands. RB-R1-02 confirmed. The draft stays held.
- **SOLR-9091: NOT CONVERGED.** RB-R1-04 (should-fix, branch-side) still open: the fragment at `e6bfa5c` still says "(and rolls back)". RB-R1-05 confirmed. RB-R2-02 (note): when the title lands, re-point the links and say only the changelog changed. The response's CONVERGED label is disputed.
- **SOLR-9382: CONVERGED.** RB-R1-06 and 07 confirmed. RB-R1-14 (length note) recorded by the main side.
- **SOLR-9598: CONVERGED on text.** RB-R1-08 and 09 confirmed. RB-R1-15: the landing order is recorded, and the citation part is still open as a note. RB-R2-03 (note): the branch's `RestoreCmd` javadoc and the changelog spelling are branch-side record items. The owner hold stands and is not judged here.
- **SOLR-9865: CONVERGED on text.** RB-R1-10 (blocking) is closed in the text: the Choice carries the third route, the prescribed Limits sentence is verbatim, and the OWNER NOTE hold is in place. RB-R1-11 and 13 (notes) standing. RB-R1-12 confirmed. The packaging call has not been recorded as made; the draft is judged as drafted, with the write-back still on the tip.
- **SOLR-11650: NOT CONVERGED.** RB-R2-21 (should-fix): the response says the posting hold stands, but the draft has no OWNER NOTE and no hold marker, so a reader can post it. RB-R1-23, 24 and 25 confirmed. The owed title, javadoc and test-comment commits have not landed; the tip is unchanged at `e4f5e941`.
- **SOLR-12085: NOT CONVERGED.** RB-R2-22 (should-fix): the bold opener says the 30-second wait applies to "each replication", but the wait runs only on the non-full-copy path that does not use `fetchFromLeader`. RB-R1-22 confirmed. The changelog title commit has not landed; the tip is unchanged at `c8dba502`.
- **SOLR-12246: CONVERGED.** RB-R1-21 and RB-R1-28 confirmed.

## Register

**RB-R2-01 | SOLR-8430 | should-fix.** Three statements describe one shared limiter without the 64-value bound that RB-R1-01 would add. They are true only up to 64 distinct rate values once the cap lands. Replacements, in the same pass as RB-R1-01: line 18: "**File streams that ask for the same rate now share one limiter on the node, for up to 64 distinct rate values.**"; line 20: replace "one shared limiter for each rate value" with "one shared limiter for each rate value, up to 64 distinct values", keeping the link; line 32: replace "This change keeps one limiter per rate value." with "This change keeps one limiter per rate value, up to 64 distinct values."

**RB-R2-02 | SOLR-9091 | note.** When the RB-R1-04 title commit lands, the `e6bfa5c` links at lines 15, 23 and 33 must be re-pointed, and line 23 should say "Only the changelog title changed after this verified run."

**RB-R2-03 | SOLR-9598 | note (record gap).** The response does not list two branch-side items as state. At `c8407773`, `RestoreCmd.java` lines 315 to 319 still say the wait "opts out" in a way that contradicts `CreateCollectionRequestBody.java` lines 42 to 47. The changelog fragment line 2 still says "behaviour"; the answers adopt "behavior". Record both as unlanded branch-side state, with the javadoc replacement from round 1's `g3` finding 6.

**RB-R2-21 | SOLR-11650 | should-fix.** The file opens with the PR-text marker and has no OWNER NOTE or hold. The response and round 1 both say the posting hold stands until the owed commits land, and the changelog title at the tip still overclaims. Insert before line 1: "OWNER NOTE (not PR text; delete before posting). HOLD. Written against `e4f5e941cd8e88524196b03f2022261c2215c3ba`. Posting waits until the owed commits land on `solr-11650-submit`: the changelog title narrowing, the URLUtil javadoc replacement and the test comment replacement. When they land, re-point the citations and Proof to the new head and add: Only comments and the changelog title changed after the verified run."

**RB-R2-22 | SOLR-12085 | should-fix.** Line 7 (the bold opener): "A follower that keeps older commits waits 30 seconds on each replication, then copies the whole index." The merge-base code waits only on the `if (!isFullCopyNeeded && !fetchFromLeader)` path (`IndexFetcher.java` lines 621 and 639 to 649), and line 9 of the draft states that condition. Replacement: "**A follower that keeps older commits waits 30 seconds on each replication that is not already a full copy and does not use fetchFromLeader, then copies the whole index.**" The owed title commit should carry the same scope.

## Carried items still open

- **RB-R1-01 (SOLR-8430, blocking):** the cap has not landed; the draft stays held.
- **RB-R1-03 (SOLR-8430, branch-side):** the fragment title at `49af21be` still says the throttle is "shared by all concurrent file requests on a node".
- **RB-R1-04 (SOLR-9091, branch-side):** the fragment title at `e6bfa5c` still says "(and rolls back)".
- **RB-R1-11 and 13 (SOLR-9865, notes):** the uncited rollback line and the length note.
- **RB-R1-15 (SOLR-9598, note):** the citation part of the OWNER NOTE.
- **SOLR-12085 title and SOLR-11650 owed commits:** not landed.
- **SOLR-9865 packaging call:** not recorded as made.

## Record notes for the main side

- **PR #5100 must be read before the SOLR-9865 packaging call.** The third route and the prescribed Limits sentence assume that PR keeps a write-back if it lands. Round 1 recorded that PR as saying it will drop its rollback edit later, and the response does not address it.
- The response's CONVERGED labels for SOLR-9091 and SOLR-12085 are disputed (above).
- `gates/SOLR-9091.md` line 5 says the packaging commit is "local only, not pushed"; the live tip shows it pushed. That file is stale.
- The answers say no SOLR-9382 draft exists this round, but `pr-drafts/replication-backup/SOLR-9382.md` does.
- Receipts show green gates at `e6bfa5c` (9091) and `2952bae` (9382). They supersede the answers' NO GATE lines, dated 2026-10-10.
- The URLUtil javadoc owed at `e4f5e941` (lines 110 to 112) says a raw "#" ends the authority. The redaction pattern does not stop at "#", so `http://solr:pa#ss@host` is redacted. The draft makes no "#" claim. The owed javadoc replacement should drop the "#" sentence or the pattern should change. This is a branch-lane note.

## Not checked

- JIRA ticket quotes.
- PR #5100's current text.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Four drafts hold open should-fix or blocking items: SOLR-8430 (RB-R1-01, RB-R1-03, RB-R2-01), 9091 (RB-R1-04), 11650 (RB-R2-21) and 12085 (RB-R2-22).
