# Update-29 receipts reconciliation

Claim: `claims/update-29-receipts-reconciliation.md` (commit `a71d523df42`). Receipts: `receipts/SOLR-<ticket>.md` from the backfill commit `cfb8f96c4c3`. Drafts: `pr-drafts/update-processing/SOLR-<ticket>.md` at the same commit. Live tips: `git ls-remote` and a read-only `fetch` of each submit branch, on 2026-10-09.

Desk check, read only. Seven subagents each checked four or five tickets and wrote a scratch file. The lead merged them, and checked the tickets with the largest mismatches against the receipts and the drafts before writing this report. No gate was run, no draft was edited, and nothing was posted.

`receipts/README.md` says a receipt at a branch's exact live tip means the branch is gated at that tip. If a receipt and a live tip disagree, the tip has moved, and the draft should say so. Receipts are the main side's record, so where a draft and a receipt disagree, the draft is what needs correcting, unless the disagreement is about the receipt itself.

## Summary

- **29 tickets.** 15 match. 4 have moved tips. 9 mismatch with their receipts. 1 has a receipt and no draft (SOLR-18505).
- **Most mismatches are drafts that predate their receipt.** Eight drafts say no local gate or no base run exists, but the receipt records one.
- **Four tips have moved.** Two moved by one commit (4841, 5754), one by two commits (7022), and one by one changelog commit (16673, where the draft already says so).
- **Two merged-tree hashes disagree.** The receipts for 5754 and 5939 name a merged tree that does not resolve in this repo. The drafts name a different one that does.
- **Confirmations needed from the main side:** the SOLR-11483 gate, and the two merged-tree hashes. No new owner decisions.

## Per-ticket verdicts

| Ticket | Verdict | Receipt head | Live tip | Note |
|---|---|---|---|---|
| 3657 | MISMATCH | 14edaca577c0 | same | Draft says no local gate; receipt names one (17/17, 11/11, 2 pre-fix failures). |
| 4841 | MOVED and MISMATCH | f8850ffd421 | e4c878627108 | One commit (changelog type). Draft cites an unnamed gate record `524afb62181`, and says pre-fix was read only; receipt says run. |
| 5065 | MATCH | ab894a996c8a | same | None. |
| 5505 | MISMATCH | 44c444aa5cd3 | same | Draft says pre-fix read only; receipt records a base run with 1 failure. |
| 5754 | MOVED and MISMATCH | 7fbe0128d8b0 | 46b919e2d4e8 | One commit (deletes TESTING.md). Merged-tree hash mismatch (see below). Draft says base does not compile; receipt records no proof step. |
| 5887 | MISMATCH | c4c57ef7bcbd | same | Draft says no base run; receipt records one, 1 failure of 17. |
| 5939 | MISMATCH | f8d4bdbea518 | same | Merged-tree hash mismatch. Draft says base checked by reading; receipt records a base run failing 2 of 2. |
| 5941 | MATCH | a4df7bfd214b | same | Draft omits the receipt's pre-fix failing run at `62516cc338ef`. |
| 6045 | MATCH | e4b77fa7ae53 | same | None. |
| 6065 | MATCH | 3d2cec9e1ab3 | same | None. |
| 6973 | MATCH | fa5b59ba07b4 | same | None. |
| 7022 | MOVED | 6a233ab2fdb | db357868610b | Two commits (changelog title, Javadoc). The live tip was never gated. The draft's CI corroboration `7940e98b0ee0` is not on the live branch. |
| 7504 | MATCH | 22b77196e662 | same | None. |
| 11475 | MATCH | 0de48e492fd4 | same | None. |
| 11483 | MISMATCH | 4431a250f665 | same | Draft says no local gate at this head; receipt names one (TestRecovery 21/21), and a base run of 21 tests with 1 failure. Draft's CI head `759f705e205b` is not named. |
| 12245 | MATCH | f325d5d0576e | same | None. |
| 12703 | MATCH | 63c84919c80b | same | None. |
| 12705 | MATCH | 8624b7c3238b | same | Wording only: draft says "tests" (plural) where the receipt says "the new test" (singular). Not a count or head mismatch. |
| 12864 | MATCH | b9c6c1e71ffa | same | None. |
| 13265 | MISMATCH | c134b34aa27f | same | Date only: receipt says 2026-10-07; draft says "Verified 2026-10-09 at this head". |
| 13696 | MATCH | da4fa6df1178 | same | Pre-fix heads in the draft (`98ad9d3fcc33`, `a4e0da422327`, `08f9384e47c0`, `c3cdf7b46e8`) are not named by the receipt. They resolve locally. |
| 13943 | MATCH | cc155cf68e1d | same | Stacked base `1d0b8a0a73cd` is the 13696 receipt's "r7-green head", an older head than its gated head. |
| 14262 | MISMATCH | 1e8d2b0075d7 | same | Draft says no base run and no local gate; receipt records a base run failing 1 of 1 and a local gate. Draft's CI commit `2a25f5e7a233` is not named. |
| 14718 | MISMATCH (minor) | 29c09959791a | same | Draft says the 1-of-1 count is not recorded; receipt records `SolrCmdDistributorTest` 1 of 1. |
| 16356 | MATCH (flag) | 39c0585072f0 | same | Draft cites `dcb16c775d62` and `56ec140e3636`, which the receipt does not name. |
| 16655 | MATCH | 5e2317443f41 | same | None. "08:03 MDT" in the draft is not in the receipt; not a conflict. |
| 16673 | MOVED | d5c19e64ba1b | d7170b12f312 | One commit (changelog title). The draft already says so. |
| 16910 | MISMATCH | 9fce3e9a7058 | same | Draft says no base run and no counts; receipt records a base run with 1 failure and 3 of 3. The draft predates the receipt. |
| 18505 | NO DRAFT | e28739b4069d | same | Receipt reads MATCH. Its round 21 head `8ca33200e37` is one comment-only commit behind the live tip. |

## Merged-tree hashes (5754 and 5939)

The receipts for both name a combined merged tree, `1ddbf36202d`. That hash does not resolve in this repo. The drafts name `f2e33340f0ea`, which does resolve. Its diffs fit the combination: from `f8d4bdbea518` only the 5754 files change, and from `7fbe0128d8b0` only the 5939 files change. The main side should confirm which hash is right, and correct whichever record is wrong.

## Moved tips

- **SOLR-4841.** Receipt `f8850ffd421`, live `e4c878627108`. One commit: the changelog type changes from fixed to added. The draft should say so, as the 16673 draft does.
- **SOLR-5754.** Receipt `7fbe0128d8b0`, live `46b919e2d4e8`. One commit: it deletes `SOLR-5754-TESTING.md`. The draft already describes this delta.
- **SOLR-7022.** Receipt `6a233ab2fdb`, live `db357868610b`. Two commits, a changelog title and a Javadoc change in `DirectUpdateHandler2.java`. The receipt states "Neither was re-gated." The draft's "change only wording" must also say the live tip was never gated. Its CI corroboration `7940e98b0ee0` sits on `origin/ci/7022-commitwait`, one commit on top of the gated head, and is not an ancestor of the live tip. It should be cited as CI on the gated head, not on the live tip.
- **SOLR-16673.** Receipt `d5c19e64ba1b`, live `d7170b12f312`. One commit. The draft already says so, and no code changed.

## Corrections for a drafting round

These follow from the receipts and need no owner decision. The next drafting round should make them, and cite only what each receipt names:

1. **Gate and base-run statements.** Correct the Proof in 3657, 5505, 5887, 5939, 14262, 16910, and 4841 to match its receipt. Each draft says no gate or no base run exists, or that a check was read rather than run, and the receipt records one.
2. **Unnamed heads and CI runs.** Either drop or label the CI heads and runs the receipt does not name: 3657 (`231e3553223`, run `37592597553`), 4841 (`524afb62181`), 5887 (run `37635905367`), 7022 (`7940e98b0ee0`), 11483 (`759f705e205b`), 14262 (`2a25f5e7a233`, run `37642970685`), 16356 (`dcb16c775d62`, `56ec140e3636`).
3. **Date.** Change the 13265 draft from "Verified 2026-10-09" to the receipt's 2026-10-07.
4. **Counts omitted.** Add the receipt's counts to 5941 (pre-fix failing run at `62516cc338ef`), 14718 (`SolrCmdDistributorTest` 1 of 1), and 11483 (TestRecovery 21 of 21 with a base run of 21 tests and 1 failure).
5. **Moved tips.** Say the tip has moved, and what changed, in the drafts for 4841, 5754, and 7022.
6. **Wording.** Change 12705's "the new tests" to match the receipt's "the new test", or leave it as it is. It is not a count or head mismatch.

## Confirmations needed from the main side

- **SOLR-11483.** The draft says no local gate exists at `4431a250f665`. The receipt names a 2026-10-06 local gate at that head, with TestRecovery 21 of 21 and a base run. This report treats the receipt as the main side's record, so the draft is what needs correcting. Confirm the receipt is right.
- **Merged-tree hash for 5754 and 5939.** Confirm `1ddbf36202d` or `f2e33340f0ea`, and correct the record that is wrong.
- **SOLR-14262 CI commit.** The draft cites a CI commit and run the receipt does not name. Confirm whether the receipt should name them.

## Other notes

- **SOLR-13943 history.** The branch's public history has add-and-remove pairs that net to zero: a changelog entry (`ae1d96e`, removed in `dd8f967`) and a handoff doc (`7e4df21`, removed in `12e660d`). The net diff against the base is two files. The draft's changelog line, "this branch adds no changelog fragment", holds at net. The history carries the pairs, which a maintainer reading the commits will see.
- **SOLR-13696 pre-fix heads.** The four heads the draft cites come from the owner's round 3 answers, not from the receipt. They resolve, but the receipt names none of them. The draft's Proof is consistent with the receipt because the receipt does not contradict it.

## Not done

No gates, tests, or builds were run. No draft or receipt was edited. No PR, comment, or submit-branch change was made. The seven scratch files, one per group, hold the quoted evidence for each verdict.
