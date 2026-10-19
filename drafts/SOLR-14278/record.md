# Skip record: SOLR-14278

- Ticket: SOLR-14278, "data loss during live shard split if leader dies". JIRA status Open, resolution none, fix versions none, created 2020-02-22, updated 2022-10-25. Incorporates SOLR-13813 (Open). Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14278-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14278-vm2.md).

## Ticket reading

During a live shard split, if the parent shard's leader is killed and not restarted, some documents indexed during the split are missing afterwards. The reporter's test with the split commented out did not fail overnight. With the split, the failure rate was reported at 30 to 50 percent on the reporter's hardware. A later comment (2020-02-26) says most failures came from a test bug, where update requests that reached the index were counted as failed; the test was corrected. Four comments (2020-02-24 to 2022-10-25). No commit names the ticket; the linked SOLR-13813 is open.

## Code reading

- solr/core/src/java/org/apache/solr/cloud/api/collections/SplitShardCmd.java:187-211 (origin/main 498a09aea97): the split command finds the parent shard's leader (getLeaderRetry with a 10 second wait, line 190) and records the leader's ephemeral owner (line 211) before the split. The leader's death during the split is the event the ticket describes; the handling after the leader's death (how the new sub-shards catch up on updates the old leader accepted) was not traced in this pass.
- No commit on main names SOLR-14278 or SOLR-13813 (bounded `git log --grep`: no output).

Commands at the pinned head:
- git show origin/main:solr/core/src/java/org/apache/solr/cloud/api/collections/SplitShardCmd.java | grep -n -i leader: lines 187-211.
- git log --oneline --grep='SOLR-14278\|SOLR-13813' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14278 or SOLR-13813: none.

## Reproduction attempts

Not run. The reported failure is a timed multi-node test (split, concurrent indexing, leader kill), whose rate was reported at 30 to 50 percent on the reporter's hardware and corrected for a test bug in the comments. A run was not set up in this pass.

## Gap analysis

The data loss is a claim about the split's handling of updates when the leader dies. The leader handling at the start of the split is in place; the path that decides which updates reach the sub-shards is not traced, and the one comment that gives a result says the observed failures were a test bug. Whether any data loss remains needs a corrected test run with the split and a leader kill.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: no commit names the ticket or SOLR-13813. close-cannot-reproduce does not hold: no reproduction was made, and the handling after the leader's death was not traced. The missing pieces are (1) the trace of how the sub-shards receive updates accepted by the old leader during the split, and (2) a run of the corrected split-with-leader-kill test on the pin.

## Limits

This record does not reproduce the failure. It does not establish whether the comment's test-bug explanation covers all of the reported losses. It does not read SOLR-13813.
