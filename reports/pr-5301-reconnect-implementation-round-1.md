# SOLR-18623 replacement-session recovery follow-up, round 1

Status: Implemented, Proof Pending.

- Authority: Nick's direct chat instruction to patch on top of PR 5301, hand it to Linux verification, and prepare a comment.
- Implementation claim: `claims/pr-5301-reconnect-windows-round-1.md`, posted and verified at `3a926cc3bd7c7dfb72116a51863d056542837a0a`.
- Contributor H0: `01e820f0539cddc4697d26176d216614a776a99a`, PR 5301, base branch_9x at `4c61bb8b1a31d2a01779d083031d551160c70130`. Fetched named ref `upstream/pr-5301-head` equals H0. The named ref avoids another task's fetch replacing shared FETCH_HEAD.
- Live discussion refreshed before commit: David Smiley (`dsmiley`) approved H0 at 2026-11-05 20:38:34 UTC, praising the slow logging appender approach. The PR head/base are unchanged, with no conversation comments. That approval is for H0, not this follow-up; there is no reviewer instruction conflicting with the retained stress fixture or the added deterministic coverage.
- Fork repository/branch: `nick-boss-tech/solr:solr-18623-reconnect-liveness`.
- Pushed H1: `19579b2ed2397929c0cc903a6c682331cc865586`; committed, plain-pushed, and independently read back from the fork refs API with the same full SHA. It is one ordinary child of H0, authored/committed by Nick Shanin (`nick.boss.us@gmail.com`) with no trailers.
- [Pinned follow-up diff](https://github.com/nick-boss-tech/solr/compare/01e820f0539cddc4697d26176d216614a776a99a...19579b2ed2397929c0cc903a6c682331cc865586).
- Worktree: `C:\Users\shaninna\dev\Solr-issues\wt\PR-5301-reconnect-ordering`.
- Gate: `gates/pr-5301-reconnect-liveness-round-1.md`; reserved receipt `receipts/PR-5301-reconnect-liveness-round-1.md`.
- Prepared comment: `reports/pr-5301-comment-draft-round-1.md`; local `research/branch-reviews/2026-11-05-pr-batch/PR-5301-comment-draft.md`. It states verification is pending and is not approved evidence of execution.

## Patch

ConnectionManager's replacement-specific connection wait checks the candidate ZooKeeper object's state. A terminal candidate causes the existing failure/close/retry route to run instead of blocking the sole reconnect executor behind its own queued expiration. Ordinary public connection waits retain their existing behavior, and the PR's event numbering and stale-state guard are retained.

Five new ordinary tests directly control delivered sequence numbers and processing order, assert connected/likely-expired state, and verify accepted listener notifications. The replacement test uses the existing DefaultConnectionStrategy ZooKeeper-creation seam and Mockito keepers, with one actual single-thread reconnect executor. It queues the replacement expiration behind the first callback and awaits completion of both futures. It verifies only the healthy keeper is installed, the failed keeper is closed, and exactly two replacement attempts occur. Its finally block closes the manager and interrupts/joins the executor so the original failure becomes a bounded assertion rather than a leaked-thread timeout.

The real-ZooKeeper nightly stress test is retained. Its appender delays only the initial Disconnected warning, counts post-state-update/rejection messages separately, and waits for processed counts to match delayed counts after flaps and expirations. A completed logging sleep alone can no longer satisfy its drain condition. This remains stress coverage; the ordinary tests provide deterministic state assertions.

A separate changelog fragment describes retrying expired replacement sessions and names Nick Shanin, with no nick field. The contributor's existing fragment and author fields are untouched. The earlier review's request to remove Houston's nick remains withdrawn.

## Scope, checks and limits

Exact outbound files:

- `changelog/unreleased/SOLR-18623-reconnect-liveness.yml`
- `solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/ConnectionManager.java`
- `solr/core/src/test/org/apache/solr/common/cloud/ConnectionManagerOrderingTest.java`
- `solr/core/src/test/org/apache/solr/cloud/TestConnectionManagerReconnectStress.java`

The source change uses this branch's Java 11 API/language level and Gradle 8.10 wrapper; no dependency is added. The existing core ConnectionManagerTest, test-framework TestConnectionStrategy, SolrZkClientTest and SocketProxy infrastructure informed the scope. The new test uses no configset, network server or new production test hook. The retained integration stress and neighboring client suite cover the real watcher path; the ordinary tests isolate manager behavior and do not independently prove wrapper dispatch.

The committed diff is four files, +246/-5 relative to H0. Source/scope/whitespace checks passed before committing; the scoped staging check required exactly the four outbound paths. Author/committer identity and contributor ancestry were verified. Windows ran no Gradle, compilation, formatter or tests. Required Linux proof is five new focused methods, existing manager and client neighbors, explicitly enabled nightly stress, tidy/changelog/root checks, one exact-H0 production liveness assertion failure and three separately labeled synthetic stale-state mutation failures. No result is claimed before the gate receipt exists.

The source assessment supports both repairs, with runtime proof pending. External strategy listener ordering remains an existing boundary because notification occurs outside the manager monitor; this patch does not claim to serialize those notifications. No new permission, wire format or trust boundary is introduced.

The public PR and contributor branch remain unchanged. No public comment, review, JIRA write or PR body edit is made in this slice. The comment draft must be reconciled with the final receipt, remote offered head and live PR discussion before any publication; use the user's standing clean-gate rules and any subsequent draft-review hold.
