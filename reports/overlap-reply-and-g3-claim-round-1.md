# Overlap reply on the other contributor's PR, and the vm2 G3 claim: round 1

Trigger: pr-prepare tip `a4c4ecf8720`, from `59f3bbf`. Two new records: the SOLR-18010 receipt's overlap answer and the reply recorded as posted, and vm2's claim on the SOLR-18010 G3 gate. Round claim: `claims/pool-pr-prepare-tip-a4c4ecf-round-1.md`.

Scope: read-only checks of the branch records. No GitHub call, no PR, comment or live body touched, no branch moved, no gate relaunched, no build, test or Gradle run. Nothing posted by this round. Any correction to a posted reply is the owner's to post.

## 1. The posted reply: what the records support

The receipt's only verbatim quote is the other contributor's comment (`6151615916`). The text of your reply (`6153544473`) is not stored on the branch, so the checks below test the receipt's summary of it.

| Claim | Result | Basis on the branch |
|---|---|---|
| (a) PR #5118 stays a draft | Supported as current state | Round 6 table reads "Open, draft". No record says ready or closed. Keep-or-close is still open (public-record-check-round-7). The sentence itself is not in the quoted comment |
| (b) Rework after #5065 merges, rebased, with the lock removed and the write path kept as a follow-up | Supported by the addendum's fate analysis, conditional on #5065 merging | The addendum says to rebase #5118 onto #5065 after it merges, remove the lock, and keep the write path as a follow-up. No ticket exists for that follow-up. Owner item O2, on symlink and mount handling, is undecided. #5065 is stalled |
| (c1) Temp-file-and-move "loses security.json's 600 permissions" | Not supported by the record | The only permission value recorded is the G1 run: 0640 expected, 0644 observed, with the copy removed. The 600 figure is a code-reading prediction for the temporary file (finding B1), which the record does not confirm. The claim should not stand as written |
| (c2) Replaces a symlink with a plain file | Supported | The replace-semantics premise (Q1 run) records that `security.json` became a regular file |
| (c3) Single-file Docker mount fails | Unresolved | The premise records the single-file bind mount as SKIPPED, with "the EBUSY reading is not tested here". No record of a read-only SOLR_HOME |
| (d) Owner approval and exact re-read | Unresolved | The receipt says the owner approved the text and that the body was re-read. The owner's words are not quoted, and the sent body is not stored |

Correction, for you to post or decline: if the reply repeats the "600 permissions" statement, the branch does not support it. The recorded permission value is 0644 against 0640 expected, and the mount failure is a prediction, not an observation. The sent body should also be stored in `receipts/SOLR-18010.md`, so that what was posted can be checked against the record.

## 2. The vm2 G3 claim

Head: `review-r1-solr-18010` is `e99be67c9a6fe15f86306104843a30b35196f159`, and `solr-18010-submit` is `2b5ad801c9e88807337f4f0a3fdd689126ad37d1`. Both unchanged. Route 1 is not pushed.

- The claim (`10bf552ca6d`, 19:35:37Z) and the G3 job both name `e99be67c9a6f`, the current head. The claim matches the branch.
- The purpose does not match. A G3 run at `e99be67c9a6f` tests the current test file. The corrected parent run recorded that test as passing at `2b5ad801c9e` (6 of 6). So the run cannot show the fail-before, and G3 has no fail-before step.
- The job conflicts with its own stop rule. The G3 file says that if the parent baseline finds the new test does not pin the review patch's change, the run stops and reports. The 18:24Z record says exactly that. The claim cites that record and does not address the stop rule.
- No go-ahead for the route 1 push is recorded on the branch. Every SOLR-18010 record says the push waits for your approval. The only approval on the branch is for the PR #5065 reply, which is a different action.

Verdict: the claim is at the right fork head, but it does not serve its purpose, and it conflicts with the job's stop rule. The fact that would change this is a recorded go-ahead and a route 1 push (a fast-forward from `e99be67c9a6f`) that fails at the parent and passes at the new head. After that push, the current-head claim is stale and G3 needs re-pointing.

## 3. Owner and main-side items

1. The 600-permissions statement in the posted reply: correct it or decline, and say which, since the owner posts any correction.
2. Store the sent body of the PR #5065 reply in `receipts/SOLR-18010.md`.
3. vm2: release or re-point the G3 claim. Do not run G3 at `e99be67c9a6f` as the fail-before proof.
4. Your go-ahead, or a decision to hold, on the route 1 push to `review-r1-solr-18010`.
5. The single-file mount behaviour stays unresolved until a host with mount rights runs it.
