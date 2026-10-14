# SOLR-17055: implementation choice for the sort-path fix, round 1

- Date: 2026-10-14. Written by the main side in answer to `reports/vm2-open-items-handoff-round-1.md`, item 1, questions 1 to 3.
- Recommendation in one line: land vm2's fix commit `2fe0986158391908ac89564090eb97e245788023`; the review side's pushed commit `2d154d1a402ed9c1705d187773e7a3009ed0645d` is not chosen, and its gate job is superseded by this report.
- This report moves no branch and pushes no fix. The one branch move it calls for (restoring the fork tip to the parent, below) is a main-side act.

## Reading asymmetry, stated up front

- The pushed commit `2d154d1a402` was read in full from the fork (production diff and test diff).
- vm2's commit `2fe0986158` is not on the fork and cannot be fetched from it (the fork answers "not our ref" for that SHA). It is assessed from the records vm2's own gate wrote: the fix gate result in `gates/SOLR-17055-sort-path.md`, the sort-path section of `receipts/SOLR-17055.md`, and `gates/SOLR-17055-sort-path-proof-rerun.md`. Where a detail of vm2's code is not visible in those records, this report says so instead of guessing.

## The premise both fixes answer

At the gated head `c2396f425fa2`, the branch capped `numFound` at topK for every knn request but trimmed the merge queue only for score order (`sort == null`). A knn query with `sort=id asc` returned 9 documents in the list (3 shards times topK 3) against a single-node control of 3, failing at `.response.size():9!=3` (premise run on vm2, seed `17055C0FFEE17055`, recorded in `gates/SOLR-17055-sort-path.md`). The acceptance bar from that job: for a distributed knn request with any sort, the response holds at most topK documents; they are the global topK by score; they are ordered by the requested sort; `start` and `rows` apply to that set.

## Side-by-side comparison

Both implementations make the same three production moves, in `QueryComponent` only:

1. **Shard requests ask for scores for a knn query.** Pushed: `createMainQuery` adds `|| (shardRows != 0 && getKnnTopK(rb.getQuery()) > 0)` to `shardQueryIncludeScore`, so shards return scores for any knn request with rows, whatever the sort or `fl`. vm2's gate record states the same behavior ("the shard request also asks for scores for a knn query"); its exact condition is not visible in the records.
2. **The merge takes the global topK by score before applying the sort.** Pushed: a score-ordered `ShardDocQueue` of size topK becomes the intake queue when `knnTopK > 0 && sort != null`; its survivors are then pushed into the normal sort queue, which applies start and rows. vm2: shard docs are collected, sorted by score, the top topK kept, and only those ordered by the requested sort and windowed by start and rows. The mechanisms differ (bounded queue against collect-and-sort) but shard hits are already bounded at shards times topK, so the difference is not material. Neither is materially narrower; both leave the score-only path (`sort == null`) exactly as the gated head has it.
3. **`maxScore` handling in the sorted path.** Both suppress it when the request did not ask for scores; see question 3 below for the one visible difference in the condition.

Tests: both add a sort case to `DistributedKnnTopKTest.test()` with the same shape (`fl=id,score`, `rows=20`, `sort=id asc`). The pushed test asserts numFound 3, 3 documents, and ids "1", "2", "3" at positions 0 to 2. vm2's committed test text is not readable from the fork; its job text calls for the same case asserting the ids of the topK documents in sort order.

Gate state, the decisive difference:

- vm2's commit has a nearly complete gate behind it. Its first gate (log `logs/vm2/queue-g17055-gate-20261014T113414Z.out`) ran every head leg green: changelog, spotless, tree clean, Error Prone compile, `DistributedKnnTopKTest` 1 of 1, module check, all rc 0. The one leg that failed used the wrong proof base (the branch merge-base, where the score-order case fails first and the sort assertion is never reached). The main side ruled on the proof base (decision recorded at commit `cba5907c2c` and in the receipt): the fix commit's parent `c2396f425fa2` with the fix's test files overlaid. The premise run at that base already produced exactly the expected failure, sort assertion with the score-order case passing. Only the formal rerun recording remains, issued as `gates/SOLR-17055-sort-path-proof-rerun.md`.
- The pushed commit has no run behind it at all. Its gate job `gates/SOLR-17055-fix-gate.md` is filed and unrun. Choosing it discards a nearly complete gate and restarts from zero, for an implementation the records show to be functionally convergent with the gated one.

## Question 2: does the `sort=id asc` case prove the order follows the sort?

No. It proves the topK set and its count, which is the premise; it does not prove the ordering follows the requested sort. The fixture makes the two orders coincide: doc `i` carries the vector `[1, i/10, 0.1, 0.1]` and the query vector is `[1.0, 0.0, 0.1, 0.1]`, so similarity falls monotonically as the id grows and the score order is exactly 1, 2, 3. A correct implementation returns the topK set ordered by `id asc`, which is [1, 2, 3]. A broken implementation that ignored the sort and returned the topK set in score order would also return [1, 2, 3], and the single-node control comparison cannot separate them either, because the control's sorted answer is the same list. An `id desc` case discriminates: the correct answer is [3, 2, 1], a score-order-only answer is [1, 2, 3], and a sort-everything-then-truncate answer starts from the wrong end of the id range entirely. This weakness belongs to the fixture, not to either implementation; both tests carry the same shape, so it does not choose between them. It is answered as a follow-up in the gate path below.

## Question 3: the `maxScore` suppression, keep or drop?

Keep it. The reasoning:

- The suppression exists because the fix itself creates the need for it. Both fixes make shard requests return scores for a knn query even when the user did not ask for any. Without suppression, the merged response for a knn query with a sort and no score in `fl` would gain a `maxScore` key that the same request on a single node does not produce: this branch's own history established that a single core reports `maxScore` only when scores are requested, while `mergeIds` propagates the shards' value. That exact asymmetry already cost this ticket one failed gate (`.response.maxScore:0.99756694!=null` in the paging case at the first re-gate, recorded in the receipt). A fix that closes the count gap while opening a response-shape gap in the same path is not finished.
- Both implementations, written independently, chose to suppress. That convergence is evidence the behavior is considered, not incidental.
- The legitimate complaint is that the focused test does not exercise it (the sort case asks for `fl=id,score`). The remedy for an untested behavior is a test that pins it, which the follow-up below adds; dropping a correct behavior because its test is missing would trade a known gap for a known inconsistency.
- One refinement worth checking when the winner's follow-up runs: the pushed commit's condition keeps `maxScore` when the sort is on score (`!ss.includesScore()` must also hold before suppressing), which is the more precise form, since a score sort makes the value meaningful to the client. vm2's records describe its condition only as "the request did not ask for scores", so whether it has the same exception is not visible. The follow-up's no-score sort case should pin the behavior either way and say which form the landed code has.

## Recommendation and gate path

**Land vm2's commit `2fe0986158391908ac89564090eb97e245788023`.** It fixes the premise by the same three moves as the alternative, it is the commit the recorded proof-base decision was built around, and it carries a nearly complete gate. The pushed commit's bounded-queue mechanism is tidy, but tidiness is not worth discarding green head legs and re-running a full gate for a functionally convergent change.

What happens to the other implementation: commit `2d154d1a402` stays as the fork tip of `solr-17055-submit` until the main side acts; nothing is deleted. Its gate job `gates/SOLR-17055-fix-gate.md` is marked SUPERSEDED with a pointer here and must not be run. If the main side ever revisits the choice, this report's comparison is the record.

Gate path for the winner, in order:

1. Main side restores `solr-17055-submit` to `c2396f425fa2` (the pushed commit remains reachable through this report and the handoff record; the restore is a branch move, which stays a main-side act). `gates/SOLR-17055-sort-path-proof-rerun.md` now says this in its own text: if the tip is not `c2396f425fa2` when the job is picked up, do not force anything, stop and record.
2. vm2 runs the proof-rerun job as written, at head `2fe0986158`: step 1 confirms the recorded head legs still stand, step 2 runs the fail-before at `c2396f425fa2` with the fix commit's test files overlaid (expected: fails at the sort assertion `.response.size():9!=3`, score-order case passing), step 3 records both legs in the job and in `gates/SOLR-17055-sort-path.md`.
3. On green, the job's own on-green steps proceed unchanged: push `2fe0986158` plainly as a fast-forward, refresh `receipts/SOLR-17055.md` at the new head, re-point `pr-drafts/search-components/SOLR-17055.md` and rewrite its Limits, mark both sort-path jobs DONE.
4. Test-strengthening follow-up, before the draft's OWED BEFORE POSTING hold is lifted: one small commit on the landed head adding (a) a `sort=id desc` case expecting ids 3, 2, 1, and (b) a sort case with `fl=id` only, whose control comparison pins the `maxScore` behavior from question 3. It goes through the same focused-class gate (head run plus, for the desc case, a fail-before against `c2396f425fa2` production, where the control comparison fails on size as the premise run showed). If the main side defers it instead, the gap must be named in the draft's Limits; it must not pass silently.

## Gate file updates made with this report

- `gates/SOLR-17055-fix-gate.md`: status changed from OPEN to SUPERSEDED (not run, not to be run), with a pointer to this report.
- `gates/SOLR-17055-sort-path-proof-rerun.md`: stays OPEN as the one live path; a fork-resolution note is added recording the moved tip, the restore step, and the stop-and-record rule.
- `gates/SOLR-17055-sort-path.md`: unchanged; by its own text it stays open until the proof-rerun records the fail-before and the on-green steps complete.
