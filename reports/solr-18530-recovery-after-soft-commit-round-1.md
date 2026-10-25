# SOLR-18530 RecoveryAfterSoftCommitTest: matched pair (vm5, round 1, 2026-10-25)

## Result

Matched pair, same method (`RecoveryAfterSoftCommitTest.test`), seed `787C6229591C6F1E`, `-Dtests.iters=10`, same host, temp dir on local disk (`-Ptests.workDir=/tmp/...`).

| Leg | Tree | Result | Log |
|---|---|---|---|
| base | RC3 tip `749712b7194` | 2 of 10 iterations FAIL: `ClosedChannelException` at the add (seeds `F0285DF3F7E002E6`, `B68391566443C461`) | `/workspace/smoke-rc3/rasc-repro/base-787C-x10.log` |
| fixed | RC3 tip plus the test change from `11361ee2e80` (test file only) | 10 of 10 PASS | `/workspace/smoke-rc3/rasc-repro/fixed-787C-x10.log` |

Seed `F0285DF3F7E002E6` is the one that failed in smoke-loop run-001, so the loop failure reproduces on RC3.

The base leg's first failure is an add to `collection1_shard1_replica_n3` on port 36499, the replica behind the test's proxy. The client gets `ClosedChannelException` (`base-787C-x10.log` lines 118 and 184).

## Not established

- The mechanism written in the test comment (the failing add had already committed on the leader, so the retry is a bad idea) was not checked. The base log does not show whether the document was committed. The matched pair shows only that the test change removes the failure.
- The fix is test-side. The candidate's commit `11361ee2e80` keeps the same test assertions and changes only which client sends the post-partition adds. Whether that is a workaround for a real client bug or a test-only race is still open. A follow-up probe would check whether the failing document is present after the test, and where the leader was when the add went out.

## Candidate

`rc3-fixset-candidate` already contains `11361ee2e80` and `0911393e01e`, so the candidate covers SOLR-18530 with this evidence. No change to the candidate was needed.

## Not done

No PR, no JIRA or GitHub comment, no dev-list text.
