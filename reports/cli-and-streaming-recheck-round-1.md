# SOLR-10667, SOLR-12347 and SOLR-12505: recheck against the new records, round 1

Trigger: vm1's verification of the SOLR-10667 and SOLR-12347 premise retries (both marked GATE GREEN in their gate files, dated 2026-10-11 in the gate files), and vm2's SOLR-12505 round 3 investigation (fixture defect fixed at `1a1875d95f`, full gate not yet run). Pr-prepare tip checked: `0593841be06` for the reviewers' reads.

Scope: drafts only. No live PR body edited. No fork branch changed. No build, test or Gradle run. Nothing posted.

## Verdicts

| Ticket | Draft | Head | Verdict | Applied | Held |
|---|---|---|---|---|---|
| SOLR-10667 | `pr-drafts/cli/SOLR-10667.md` | 9ce9ef1db5e9 | NOT SATISFIED (the base-distribution absence was not in the draft; the citation range was short) | Base absence sentence with date; packaging link range; Limits citation linked | A literal fail-on-base claim (needs a base BATS run); the hidden `.gitignore` cause; choice section (owner) |
| SOLR-12347 | `pr-drafts/cli/SOLR-12347.md` | 56d38389c75a | NOT SATISFIED (section order; date; head distribution undated; the Windows `SOLR_STOP_WAIT` sentence) | All eight proposed edits; the choice section moved after Proof | The 180 versus 600 question (owner); a Windows run; systemd `TimeoutSec=180s` (unchanged, owner) |
| SOLR-12505 | `pr-drafts/streaming/SOLR-12505.md` | 1a1875d95f36 | NOT SATISFIED until the full gate at `1a1875d95f` lands | None to the live draft. The candidate text is in `pr-drafts/streaming/SOLR-12505-candidate-round-1.md` | Every gate-dependent count; the new test's head result; the base check as the proof leg; the owner items below |

## SOLR-10667

Checks: the head `9ce9ef1db5e9` and the merge-base `cabedd1d968` match the record. The count (1 test, 0 failures at head) matches the gate record. The packaging link lines and the Limits citation hold at their SHAs.

Applied:
1. The Proof now says the base distribution has no `modules/ltr/example`, checked 2026-10-11, and names the packaged head `1e3f022552be` for the "before this change" distribution.
2. The packaging citation range is widened to lines 80 to 102 (both the text and the link), to cover the example-directory step the reviewer read.
3. The Limits citation to the reference page is now a link at `9ce9ef1db5e9`.

Held:
- A literal "fails on base" claim for the new BATS check. The record has no base run, so it stays out of the draft until a base BATS run on a gate host.
- The hidden `.gitignore` is absent from the distribution, and the cause is unknown. The Proof says so; no explanation is given.
- Whether moving the generated client out of `example/` is a live alternative (a Choice section) is the owner's call.

## SOLR-12347

Checks: the head `56d38389c75a` and the stop-wait values (600 seconds at head, 180 at base) match the record. The Windows `solr.cmd` percentage cut (about 570 seconds) holds at the link.

Applied:
1. Windows scope: "In `bin/solr`, that still holds. On Windows the start wait has never followed `SOLR_STOP_WAIT`; this change leaves it at 180 seconds."
2. Section order: "A choice to check" moved after Proof and before Limits.
3. Verified date: 2026-10-11, as the gate record says.
4. Head distribution: "at b77acba2ad6 (before the packaging commit)".
5. "about 570 seconds" becomes "570 seconds".
6. Deleted "It does not assert the start default (see Limits)." from Proof. Limits still says it.

Open in this draft (not changed in this round):
- The draft names two different base commits: `b6b2b8f10e` in "What happens today" and `cabedd1d968` in the Proof's base distribution sentence. The reviewer checked each against upstream main, but the draft should say which is the merge-base and why there are two. Owner or the next round.

## SOLR-12505

Checks: the fork head `1a1875d95f3680c20447006efc3c949c57f5135f` matches ls-remote (fast-forwarded from `5f20e1171bc`). The live draft describes only the defType route at `5f20e1171bc`. It does not mention the validate route or the new test, so it is out of date against the branch. It must not be posted as it is.

What the record supports now, which the candidate uses:
- The validate route: the check at FetchStream.java lines 153 to 159, the constructor path through StreamFactory at lines 593 to 611.
- The new test's name and its line range (1009 to 1036), and the defType test's range (946 to 1006), both at the head.
- The defType line at lines 255 to 257 at the head (the live draft linked 247 to 249 at 5f20).
- The Eric Pugh sentence is cut from the candidate. The commit count is 76 behind on the local ref, not 72, and the candidate says `[N]` until the recount at posting.

What waits for the full gate at `1a1875d95f`:
- Every count in Proof (StreamDecoratorTest N tests, S skipped, F failures, from fresh XML, dated).
- The new test's head result, and the base check as the proof leg (FetchStream reverted to `5f20e1171bc`).
- The bracketed symptom line in section 1 (`invalid query parser 'i'`).
- The changelog title, if owner item 4 changes the YAML, and the receipt refresh.

Owner items for SOLR-12505 (a record cannot settle these):
1. Unicode whitespace gap, by code reading (not run). The check uses `\s` and `.`, but the local-params reader ends an unquoted value at `Character.isWhitespace`. Names with U+3000, U+2028, U+001C to U+001F and similar pass the check and then break the batch. Recommended: widen the check with `\p{javaWhitespace}`, which needs a new full gate. Alternative: keep the check and say "ASCII whitespace".
2. Choice section: validate versus escape (escape would need quoting, since the local-params reader has no escaping for unquoted values), and defType versus the terms parser. The draft should ask one pointed question.
3. StreamFactory cause text: a Limits line or its own ticket.
4. Changelog title describes only defType; it needs a branch edit or a changelog follow-up.
5. Quote and backslash names: the record shows space, brace and bang failing at the base, not quote or backslash. Do not say "always failed before" until checked.
6. JIRA attributions (Eric Pugh, Joel Bernstein, David Smiley, the comment ID) need a check against the JIRA thread before posting.
7. Merge-base recount at posting (the local upstream ref is stale).
8. The new test's base is the parent `5f20e1171bc`, not the merge-base. Either re-run the new test at the merge-base or say so in the text.
9. `receipts/SOLR-12505.md` still says the fork tip is `5f20e1171bc`, and the gate refresh owns that after the full run.
10. The fork branch `ci/12505-stream` (`b673677a4af`, parent `5f20e1171bc`) is in no record. Confirm it is not a second PR head and is not needed.

Candidate file: `pr-drafts/streaming/SOLR-12505-candidate-round-1.md`, with a staging header marking it not for posting.

## Owed records (not repaired this round)

- Both `receipts/SOLR-10667.md` and `receipts/SOLR-12347.md` date the vm1 green result 2026-10-10. The gate files and claims say 2026-10-11. The receipts need the correct date. The receipts are records, so the correction belongs with the record owner or the next gate-record round, not in the draft.
- Premise legs are undated per leg. SOLR-12347 part 1 is a code read, not a run. No base JUnit count is recorded for SOLR-12347.

## Housekeeping

- The local ref `refs/heads/pr-prepare` is stale. The worktree HEAD is the one used for reads and pushes. Nothing was reset or deleted.
