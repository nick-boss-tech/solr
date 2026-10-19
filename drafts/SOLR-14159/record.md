# Skip record: SOLR-14159

- Ticket: SOLR-14159, "Fix errors in TestCloudConsistency". JIRA status Open, resolution none, created 2020-01-03, last updated 2020-04-14, checked 2026-10-19 UTC. Reference: SOLR-13486 (Open).
- Verdict: keep-open.
- Draft branch: solr-14159-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

Flaky failures in `TestCloudConsistency`, moved from SOLR-13486. Two failure texts are in the comments: "Server refused connection" (about one in a hundred runs, per the reporter) and "Timeout waiting for active collection". The reporter attached a debug patch. hossman could not reproduce either in about a thousand local iterations (2020-01-07). hossman committed a test-side patch (spin loops) on 2020-01-22 that he said would help even if it did not fix the reported failures. The last comment (erickerickson, 2020-04-14) reports no failures in four weeks and unassigns the ticket.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/test/org/apache/solr/cloud/TestCloudConsistency.java`: the test exists at the pinned head.
- The fixing commit `6b3e7feba1` (2020-01-22) changes `TestCloudConsistency.java` (49 lines) and `TestTlogReplayVsRecovery.java` (24 lines) to remove spin loops in favor of waits. It changes test code only; no production file is in the commit.
- So the commit does not fix the production behavior behind the "Server refused connection" failure. It removes tight loops that may have contributed to the flakiness.

## History

- `6b3e7feba1` (2020-01-22), SOLR-14159, "Eliminate some 'spin loops' in tests that may be contributing factors to odd test failures". Ancestor of the pinned head: yes.
- The ticket's debug patch and the "FailWithHossFix" logs are attachments; they were not read in this closeout.
- No later commit names the ticket (`git log --grep` at the pinned head returns only `6b3e7feba1`).

## Reproduction attempts

1. (Earlier, by the ticket's author, 2020-01-07) about a thousand local iterations: no reproduction of either failure. This is not a run on the pinned head.
2. (This closeout) not attempted. A run at the pinned head would beast `TestCloudConsistency` with a fixed seed sweep (for example 20 seeds, each once) and count the two failure texts from the fresh XML. That run is the missing piece; it was not run here because it is a multi-minute cluster test per seed and a flaky-test result needs many seeds to mean anything.

## Gap analysis

No failure has been seen since 2020 in the ticket's record, and the one fix in the history is test-side. Whether the production path still has the connection-refused race is not shown either way on the pinned head. The keep-open answer is about the evidence: the attempts are old and not on this head.

## Verdict justification

keep-open: close-cannot-reproduce needs a reproduction attempt on the pinned head with a result, and none was run here (the 2020 attempt was on an older head). close-fixed does not apply: the only fixing commit is test-side and the ticket says it does not fix the failure. close-obsolete does not apply: the test still exists. The keep-open minimum is met: the earlier attempts, the commit, and the missing piece (a seed sweep on the pinned head, with the two failure texts counted) are named.

## Limits

This record does not run the test. It does not read the debug attachments. It does not check the SOLR-13486 thread.
