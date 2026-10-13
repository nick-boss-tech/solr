# SolrCloud READY drafts, batch B, plus the two NOT READY drafts, round 1: verdicts

Verdicts: two SATISFIED (READY TO OPEN), two held by owner items, one NOT SATISFIED pending a gate, and one held by an owner item. Nothing was opened, closed, flipped or commented on. Gate jobs only as files under `gates/`.

Three review agents read the six drafts against live heads, receipts, and the code at the cited SHAs. Every citation was checked at its SHA.

## SOLR-16013

- Verdict: **HELD by an owner item (a code gap).**
- Live head `408317d7e241` matches the receipt. The gate is green at 2026-10-12 14:50 UTC, and the base run is recorded as 2 tests with 2 failures at seed `2E201C17D50DCEAA`.
- Held by: the interrupt. An interrupt that arrives while the wait is blocked is held until both closes finish, and JDK 21 `FutureTask.get()` skips the interrupt check when the task is already done. That path is not covered by the closing check in `reports/fresh-recheck-closing-round-1.md`. Owner choice: a code fix (for example `Thread.interrupted()` before the wait), with a branch patch and a gate at the new head, or a Limits line that names the edge.
- Edit: the sentence on restoring the interrupt now says that an interrupt arriving during the wait is held until the closes finish.

## SOLR-17292

- Verdict: **HELD by an owner item (the run date).**
- Live head `f614a42fbc80` matches the receipt. The counts match: TestPerReplicaStates 4 of 4, and the base run gives 1 failure.
- Held by: the receipt has no run date of its own. "2026-10-10" is the re-gate finish date from the receipts ledger. Owner: confirm the run's date from the gate log, or add a run-dated line to the receipt.
- Edit: the second Limits bullet now says the Overseer loop logs a failed flush, and that a single per-replica update is removed from the queue without a retry. The reason is in the code: the callback runs in a `finally`, which removes the queue entry even when persist throws.

## SOLR-17680

- Verdict: **SATISFIED. READY TO OPEN.**
- Live head `52b641ca1a` matches the receipt. The counts and the base-code run match the receipt.
- Edits, for length (about 3,500 visible characters): the Proof, the Choice and some wording were trimmed without changing any claim. The Limits now say that without `maxCardinality`, a category router gets the 400.
- Optional, not blocking: the base run in the Proof has no date of its own. The receipt gives 13:42 to 13:48 UTC on 2026-10-13.

## SOLR-17733

- Verdict: **SATISFIED. READY TO OPEN.**
- Live head `636196b7954` matches the receipt. The dated run is 2026-10-13 04:07 UTC, and the base-code claim matches the receipt.
- The Choice's three routes match the code at the head.
- Edit: a voice fix on the sync sentence.

## SOLR-14919

- Verdict: **NOT SATISFIED.** Remaining record item: the gate at the live tip.
- Live head `8f1f4ef91e79` is what vm2 claimed. vm2's run stopped at the focused-head leg, and no receipt exists at that head. The receipt covers `84e7bcaeb57`. The diff from `84e7` to `8f1f` changes only comments in the processor and its test, the recovery marker line in `RecoveryStrategy`, and the changelog title. The draft's claim holds.
- Edits: the three links now point at `8f1f4ef91e` (the line ranges hold). The Proof sentence on what changed since `84e7` is corrected. The follow-up offer is added to the replication Limit. "The loop sends" became "The test sends".
- Owner items: (1) the gate at `8f1f4ef91e`, which is gate job `gates/SOLR-14919-processor-only-gate.md`. (2) The Proof date `2026-10-03` is the receipt's ledger date. The receipt gives no run date. (3) The Proof names `84e7` and the links name `8f1f` on purpose. Confirm the links at `8f1f` rest on the diff until the gate lands.

## SOLR-9155

- Verdict: **SATISFIED. READY TO OPEN.**
- Live head `29bf3738289` matches the receipt. The receipt records ZkControllerGetLeaderTest 1 of 1, 0 failures and 0 errors, from a suite run at 2026-10-13 05:51 UTC.
- Edits: the bracket is replaced by the receipt's counts and date. The premise run's dates are stated as the receipt gives them. The rejoin citation now covers the "Unable to rejoin election" catch. The Choice and the Limits are tidied. The ops-logging bullet names the ticket's ask and the log-and-rethrow point.
- Visible length: about 3,480 characters, under the guide. The earlier estimate of 380 characters to cut was wrong; the draft is near the limit without further cuts.
- Owner item, not changed: "drafting" in the approved AI footer.

## Owner decisions this batch raises

1. SOLR-16013: a code fix for the interrupt edge, or a Limits line.
2. SOLR-17292: the date of the passing run.
3. SOLR-14919: the gate at `8f1f4ef91e`, and a run date for the Proof.
4. The base-code dated runs in `gates/solrcloud-base-dated-runs-batch-a.md` (SOLR-5813, SOLR-12991, SOLR-13186, SOLR-15863).
5. SOLR-11479 opens only after SOLR-15035, which is still HOLD.
6. SOLR-15106 and SOLR-15386 were READY in the round 5 decisions and are in neither batch. Which assignment covers them?
