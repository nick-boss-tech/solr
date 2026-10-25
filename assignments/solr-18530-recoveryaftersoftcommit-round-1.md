# Assignment: SOLR-18530 RecoveryAfterSoftCommitTest, root cause and matched pair (round 1)

Status: OPEN
Tags: implementation, premise-run, records
Intended host: any pool Linux host with the `implementation` tag (per its `hosts/` file). Not vm5: vm5 is the RC3 smoke host and ran the diagnosis.

Owner direction (2026-10-25): the RC3 failures must be fixed, the fixes applied to RC3, and the result run through the smoke tester. This item has no confirmed cause and no matched pair yet.

## Facts (from vm5's round 2, do not re-derive)

- Failing test: `RecoveryAfterSoftCommitTest.test` (`solr/core/src/test/org/apache/solr/cloud/RecoveryAfterSoftCommitTest.java`). Failed once in five RC3 smoke-loop runs (run-001). Passed in runs 002, 003, 005, 006.
- Seed from the loop XML: `787C6229591C6F1E`.
- Failure: `SolrServerException: IOException occurred when talking to server at .../collection1_shard1_replica_n3/update`, root cause `java.nio.channels.ClosedChannelException`, thrown from `RecoveryAfterSoftCommitTest.java:106` (an `add` through `CloudSolrClient`), during the test's own replica restart.
- Not the hard-link problem: zero `Too many links` (EMLINK) hits in its XML across runs 001 to 006. The `/workspace` NFS EMLINK problem (`reports/rc3-test-failure-causes-round-2.md`, section 3) does not explain this one.
- Not reproduced on RC3 in vm5's round. No matched pair exists.
- Candidate fix in place: the SOLR-18530 commits already on `rc3-fixset-candidate` (commits `11361ee2e80` "send post-partition writes in RecoveryAfterSoftCommitTest through a leaders-only client" and `0911393e01e` "drop the changelog entry"). Their effect on RC3 is unproven.
- Source tree for RC3: `749712b71940c9f96d824c533f32f5c56dcd3924` (branch_10_1).

## Work

1. Base: RC3 tip, host lock, record the JDK. Run `RecoveryAfterSoftCommitTest` at seed `787C6229591C6F1E` with `-Ptests.workDir` on a local disk (for example `/tmp/...`), so that hard-link failures cannot mix in. If it fails, that is the base leg. If it passes, repeat for a few seeds from the loop before concluding anything.
2. Root-cause with probes (rung 2), not by guessing. The useful questions: which replica the client sends to after the restart, whether the connection is a pooled one closed by the restarted jetty, and whether the test waits for the replica to be active before sending.
3. Fixed leg: the same method and seed with the candidate commits from `rc3-fixset-candidate` applied to RC3 (only the SOLR-18530 hunks, not unrelated main-line changes).
4. Matched pair (owner's rule, `assignments/rc3-proof-pair-requirement-round-1.md`): base and fixed legs must be the same method, same seed, same host, same local temp dir. A pass on a different seed does not count.
5. Report: `reports/solr-18530-recovery-after-soft-commit-round-1.md` with the probes, the cause or "not established", the matched pair, and the branch name and head SHA if a fix branch is made.

## Rules

- Never force-push. Fork branch only. No PR, no JIRA or GitHub comment, no dev-list text.
- One host lock at a time. Record each decision with its rung (`material/decision-ladder.md`).
- Use `-Ptests.workDir=/tmp/...` for every Gradle run on this pool; `/workspace` is NFS and rejects hard links.

## Deliverables

- Report file above, pushed to `coordination-ui`.
- If a fix branch is made: pushed to the fork, with its head SHA in the report.
