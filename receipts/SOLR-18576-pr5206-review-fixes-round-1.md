# Receipt: SOLR-18576, PR 5206 review fixes, round 1 (vm6 gate)

Verified head: `6c6ff8d9a0e173c02b5a11f8ad3b13a59ebd36fa` (candidate F). Parent O `4c8f12f526f54e4e90a8ea49d46c1d28d899cb65`. Fork `nick-boss-tech/solr:solr-18576-submit-apache-main` and PR 5206 head both equal F at 2026-10-27 07:22Z. Older receipt `receipts/SOLR-18576.md` proves O only and is kept as is.

Gate host: vm6. Gate file `gates/pr-5206-reconnection-cleanup-round-1.md` (not edited). Claim `claims/pr-5206-reconnection-cleanup-round-1-vm6.md`. Full report `reports/pr-5206-reconnection-cleanup-round-1-gate.md`.

## Results at F

- Tidy: rc 0, tree clean (job `pr5206-F-6c6ff8d9a0e`, 06:00Z).
- Error Prone compile (`-Pvalidation.errorprone=true --rerun-tasks`): rc 0.
- Seed `7E5494314774A841`, BasicDistributedZk2Test: tests 1, skipped 0, failures 0, errors 0.
- Random iterations `-Ptests.iters=4`, BasicDistributedZk2Test: tests 4, skipped 0, failures 0, errors 0. Actual seed `A6A04801FC75C1A7`.
- Root check `check -x test --no-build-cache`: rc 0 (job `pr5206-F-clone-root`, 07:20Z). The first attempt in the linked gate worktree stopped at the ref-guide Antora step because Antora needs a git directory, not a `.git` file. Rerun in a full clone at F, as an environment relaunch. Cause and change in `claims/pr-5206-reconnection-cleanup-round-1-vm6-result-2.md`.
- Final tidy: rc 0, tree clean (07:22Z).

## PR description draft (not posted)

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18576

## What happens today

The reconnection step in `BasicDistributedZk2Test` forces a ZooKeeper session expiry, then waits for the client to reconnect and indexes a document. Before this change, the result of the 30-second `blockUntilConnected` wait was not checked, so a client that never reconnected still went on to index. The expiry listener was also removed only on the success path.

## What this change does

It asserts the result of the existing 30-second `blockUntilConnected` call, with a reconnection-specific failure message. The expiry wait, the latch assertion, and the reconnection assertion now sit inside a `try`, and the listener is removed in a `finally`, so cleanup runs on success, failure, or interruption. Registration stays before forced expiry, and indexing stays after the checked waits. Only the test file changes.

## Proof

`BasicDistributedZk2Test` passes at this head: 1 test with the original seed `7E5494314774A841`, and 4 random-seed tests (seed `A6A04801FC75C1A7`), all with no failures, errors, or skips. Tidy and the Error Prone compile pass. The root check passes in a full clone. Verified 2026-10-27 at this head.

The new assertions have not been shown to fail on a base or mutated run. The passing runs do not exercise the expiry-failure, timeout, or interruption branches; those were checked by reading the source. The original intermittent failure was not reproduced, so this change is not evidence that the race is gone.

## Limits

- The original intermittent failure was not reproduced before or after the earlier change.
- The timeout and interruption cleanup paths are source-inspected, not executed.
- No production code, dependencies, or changelog changed.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
