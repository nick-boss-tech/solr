# Skip record: SOLR-13486

- Ticket: SOLR-13486, race condition between leader's "replay on startup" and non-leader's "recover from leader" can leave replicas out of sync (TestTlogReplayVsRecovery).
- Verdict: keep-open.
- Draft branch: solr-13486-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the earlier A3 round 1 record (reports/2026-10-18-skip-repro/A3-other-round-1.md) is replaced by this pass; its run is re-read, not repeated.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-05-21, updated 2020-07-24, no fix versions. Linked: SOLR-14183 (Open), SOLR-14184 (Closed), SOLR-14159 (Open), SOLR-14679 (Closed). The description: a leader with uncommitted tlog docs restarts, and a replica starts peer sync from the leader before the leader's tlog replay is done, so the replica can miss docs. The 2019-05-21 comments (hossman) attach logs of the test failing on the ticket's scenario; the 2019-07-18 comment (caomanhdat) proposes a flag to block /replication until tlog replay finishes.

## Code reading

Read at the pin:

- `solr/core/src/test/org/apache/solr/cloud/TestTlogReplayVsRecovery.java:48-51`: the class is annotated `@AwaitsFix` with bugUrl `https://issues.apache.org/jira/browse/SOLR-13486;https://issues.apache.org/jira/browse/SOLR-14183`. The annotation first appears in `0fac7c1a26` (2020-01-03) and was extended to name SOLR-14183 in `9a2497f637` (2020-01-11); both are ancestors of the pin. So every test in the class is skipped by design, which is why the earlier run showed one test skipped and none run.
- Startup replay: `solr/core/src/java/org/apache/solr/update/UpdateLog.java:1429`, `:1465` and `:2069` construct `LogReplayer` (class at :2092) for recovery logs, the active log, and the buffer log.
- Recovery from leader: `solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java:680-687` attempts PeerSync from the leader (`PeerSyncWithLeader`); `:701-702` and `:728` start `replay(core)` after buffered updates or after the recovery path; `:844` holds the `testing_beforeReplayBufferingUpdates` hook; `replay` is at :846.

What the reading does not show: whether the leader-side replay finishes before the replica's PeerSync reads the leader's updates at the pin. That ordering is what the test would exercise, and the code reading alone does not settle it.

## History

- `0fac7c1a26` (SOLR-13486, 2020-01-03, "Text improvements"): the `@AwaitsFix` annotation first appears here. Ancestor of the pin.
- `9a2497f637` (SOLR-13486, 2020-01-11, "Fix trivial test bug in TestTlogReplayVsRecovery"): extended the annotation to also name SOLR-14183, and added a TODO that says the randomized commit-on-close setting waits for SOLR-13486 to be fixed. Ancestor of the pin.
- These two commits are the only ones at the pin that name SOLR-13486.

## Reproduction attempts

- Earlier round (A3, vm1 record): `:solr:core:test --rerun --tests org.apache.solr.cloud.TestTlogReplayVsRecovery`, seed A0A71901198D5D30, build SUCCESSFUL, 1 test, 1 skipped; no test ran. Not evidence either way.
- This pass: no build or test run.

## Gap analysis

- The test cannot exercise the race while `@AwaitsFix` is present, and the annotation is the reason the earlier run was empty.
- Missing piece: a run of TestTlogReplayVsRecovery on the pin with the `@AwaitsFix` removed locally (a probe, not a commit), with the seed, the result, and the log path. That run would show whether the replica misses the leader's uncommitted docs. The code path at RecoveryStrategy.java:680-702 and UpdateLog.java:2092 would then be the place to read the ordering.
- SOLR-14183 is linked in the annotation and is itself an open ticket on the same bug line; whether it changes this verdict is not checked in this pass.

## Verdict justification

keep-open: the ticket is a real open question, the test that would check it is disabled by an AwaitsFix annotation that points at this ticket, and the code paths are present at the pin but their ordering is not settled by reading. A close verdict needs a run with the test enabled (close-cannot-reproduce) or a fix commit (close-fixed). A pr-needed verdict needs the failing run.

Missing for a closing or PR verdict: the probe run described above, with the annotation removed locally, and its log path.

## Limits

This record does not establish a reproduction. It does not check SOLR-14183's content beyond the link. It does not post anything to JIRA or GitHub.
