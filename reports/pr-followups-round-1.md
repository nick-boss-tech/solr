
# Report: pr-followups-round-1

## Item 3: SOLR-16630 closeout reply (draft, held)

HOLD. Do not post until a gate on head `a02cc7ba6b` is green and its receipt is written. The receipt on the board covers only `c9caf1e4b8`.

Facts checked on 2026-10-24:
- PR #5106 head is `a02cc7ba6b`. The fork branch `solr-16630-submit` and `refs/pull/5106/head` both point there.
- The conversion commits `a31707acfd`, `7ab3c03a1c` and `136145b598` are ancestors of the head.
- Tomas Fernandez Lobbe's commit `2a35f8de72` ("don't let the toggle thread wait out the window in testNRTRestart", 2026-10-20) is an ancestor of the head. The receipt does not mention it.
- `a02cc7ba6b` ("clarify query phase duration comment", Nick Shanin, 2026-10-20) changes one comment in `TestCoordinatorRole.java`. The receipt does not mention it.
- `grep -c "Thread.sleep"` on `solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java` at the head returns 0.
- The last green gate is at `c9caf1e4b8` (receipt: tidy clean, Error Prone compile, module check, six focused seeds green). Both later commits are after it.
- The reply the receipt describes (comment 6171998479, 2026-10-14) was deleted by the owner. `material/pr-5106-reply-draft-reconstructed.md` is not on `coordination-ui` or on the archived board. This draft is new, and it does not reuse that framing.

Draft (GitHub markdown, plain language, no internal process terms):

```
🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Thanks, Tomas. The sleep-to-event conversion is done on this branch. `TestCoordinatorRole.java` has no `Thread.sleep` calls at the current head. The remaining waits are latches, or bounded polls that exit on an observed value.

I accept your commit 2a35f8de72, which keeps the toggle thread from waiting out the window in `testNRTRestart`. The follow-up at a02cc7ba6b only rewords a comment.

Test status: the focused test passed at c9caf1e4b8 with the CI seed and five other seeds. That run did not include 2a35f8de72 or a02cc7ba6b. I will post the result of a run on the current head here before this reply is considered final.
```

Notes for the owner:
- Posting needs your go, and the run on `a02cc7ba6b` needs to be green first. Until then, the reply should say what is verified, which is the sleep count and the commit history.
- The placeholder "I will post the result of a run" is deliberate. Replace it with the run result when it exists, or drop it.
- Whether to accept Tomas's commit before the run is your call. The draft accepts it as written, which is the assignment's request.

Watch items (item 4): no replies are recorded on SOLR-18470 or on PR #5202 in this round. No action.
