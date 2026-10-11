# Claim: SOLR-8628 premise run part 3 (branch-head leg) on vm1

Claimant host: vm1. Job: gates/SOLR-8628-premise.md.
Capability tags: premise-run. Staffing: 1.
Started: 2026-10-11T14:58:00Z (UTC).
Branch: solr-8628-submit at head `ce8211e05e062444e7afc1c5ea33fb6eff581d8f` (live tip, verified by ls-remote at claim time).

Why vm1 holds this claim: vm3 ran the premise job (claims/pool-vm2-gate-backlog-8628.md), completed parts 1 and 2 and the merge-base proof leg of part 3, paused and released the claim at 2026-10-11T14:10:34Z (UTC), and the vm3 host then went down. At Nick's direction (2026-10-11 ~08:56 MDT) vm1 takes over the one remaining leg: part 3 at the branch head.

Already complete, not to be rerun (recorded in gates/SOLR-8628-premise.md): part 1, empty-directory scope on current upstream main `d76fc14b9857e77b830d7a82514b28c523a239c2`; part 2, the write.lock-only state is reachable (abrupt-halt variant on Lucene 10.4.0 left exactly `write.lock`); part 3 merge-base leg, production SolrCore.java at `cabedd1d968059215188f4e7563fb303241899ed` with the branch test retained: 1 test, 1 expected AssertionError (`a new index must have been created`, TestCoreContainer.java:134), seed `8628C0FFEE8628A1`.

This claim runs only the branch-head leg: `:solr:core:test --tests org.apache.solr.core.TestCoreContainer.testCreateCoreOverIndexDirWithOnlyWriteLock` at the branch head with seed `8628C0FFEE8628A1`, counted from fresh JUnit XML; the test must pass. No gate steps, no branch edits, nothing removed from the branch.

Worktree: ~/workspace/solr-worktrees/solr-8628-premise. Runner: ~/workspace/tools/g8628-premise.sh (blocking flock on ~/workspace/tools/.solr-test-queue.lock, so it serializes behind the SOLR-5262 gate). Log: ~/workspace/tools/g8628-premise.log.

On completion: the verdict is recorded in gates/SOLR-8628-premise.md and receipts/SOLR-8628.md and this claim is marked DONE. If the head leg passes, the premise holds and a separate first-gate job follows (posted by the main side, not part of this run). If it does not discriminate, the receipt records NO GATE by finding with the run evidence.

Heartbeat: 2026-10-11T14:58:00Z (UTC) (claim; runner starts detached on vm1).

- Status: DONE, 2026-10-11T15:12:59Z (UTC). The head leg ran and FAILED with the identical AssertionError as the base leg (1 test, 1 failure, seed 8628C0FFEE8628A1); the premise does not discriminate as the test is written. Verdict recorded in gates/SOLR-8628-premise.md and receipts/SOLR-8628.md (NO GATE by finding).
- Heartbeat: 2026-10-11T16:20Z (UTC) (outcome recorded; claim DONE).
