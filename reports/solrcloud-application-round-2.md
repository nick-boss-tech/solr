# SolrCloud application, round 2

Verdict. JIRA is reachable through the REST API, using the PAT in Downloads. The JIRA reads settle one open item and narrow another. They also show that the processor-only route for SOLR-14919 does not fix the failure the ticket reports. That is the item to decide first. The SOLR-9155 route is now the declared route, and its build and test work is in a vm2 assignment.

## Finding that changes SOLR-14919 (decide first)

- SOLR-14919's summary is "IgnoreCommitOptimizeUpdateProcessor does not work during replication". Its description says the change "causes recovery (replication) to fail when the IgnoreCommitOptimizeUpdateProcessorFactory is set to return a status code other than 200", and that the `COMMIT_END_POINT` parameter "is not being sent in the commitOnLeader function of RecoveryStrategy".
- The processor-only route fixes the forwarded-commit 400. It does not fix the recovery failure the ticket reports. The draft now says so in "What happens today" and in Limits.
- The recovery marker is the piece that addresses the reported failure. Upstream commented it out in commit `75b183196798`, whose JIRA log lists SOLR-12933 "Fix SolrCloud distributed commit". The ticket does not say why it was commented out. Re-enabling it changes commit fan-out, and whether that is safe is not known from here.
- Decision for Nick: do not open either route until vm2 job 5 is back. Job 5 asks whether recovery succeeds at the processor-only head when the processor returns 403, and whether a distributed-commit test passes with the marker. If processor-only fails recovery, the PR should not be described as fixing SOLR-14919.

## JIRA reads

- **SOLR-15035** (confirmed). The ticket summary is "core.properties different when using ADDREPLICA .vs. when the replica created with CREATE". The draft describes that difference. The receipt is green at the tip `12d7491e82c` with AddReplicaTest 4 of 4. The hold was only the summary check, so SOLR-15035 is now READY TO OPEN. It opens first in its group, before SOLR-11479.
- **SOLR-12651** (supports the adopted route). The reporter writes "We should clean up all the state if a restore fails", which supports delete-on-failure. The optional-cleanup and resume question is a later comment, and the draft's Choice already poses it.
- **SOLR-9155**. The ticket says `getLeader` "does not handle InterruptedException, and instead rethrows it as a SolrException". It also asks for no exceptions as flow control, no log-and-rethrow, and remedy steps in the message. The ticket does not pick a route. The route is now declared (see below).
- **SOLR-13369** (supports the adopted route). The ticket's Jenkins seed puts route prefix `app9/2!user32` in shard2 and shard3, as the debug log shows. A later comment by the ticket's author agrees with the SOLR-13210 author that, with a bit mask, neither prefix is guaranteed to stay in one shard. The test was disabled upstream in 2019 (commit `2533fd1`). The draft's Choice now cites that comment. The sweep on main is vm2 job 4.
- **SOLR-9598**. The packet is fetched but not read in this round. The ordering for SOLR-12651 depends on its default call, which stays with its owner.

## Branch change this round

- `solr-9155-submit` at `dbd88c84ff2` (fast-forward push). `getLeader` declares `throws InterruptedException`, and the test expects it. The register path already declares `Exception`, and the rejoin path already catches `Exception`. The changelog title is updated to match. The change is compile-checked only by reading. vm2 gate job 1 is the check.
- The rejoin path now wraps an interrupt in "Unable to rejoin election" without restoring the flag. The base code does the same, so this is not a regression. The draft states it.

## Drafts changed this round

- `SOLR-9155`: rewritten for the declared route. The Proof has a bracket for the run result, as the 12651 draft did before its gate.
- `SOLR-14919`: "What happens today" names the ticket's recovery failure and says this change does not fix it.
- `SOLR-13369`: the Choice cites the later JIRA comment.

## Readiness changes

- SOLR-15035: HOLD becomes READY TO OPEN (summary confirmed; receipt green at the tip).
- SOLR-9155: DECISION becomes "route decided, gate owed" (declared route; vm2 job 1).
- SOLR-13369: DECISION stays for the Choice. The evidence supports the test-side route. The sweep is vm2 job 4.
- SOLR-14919: DECISION becomes HOLD, because the route does not address the ticket's reported failure (vm2 job 5).
- SOLR-12651: unchanged. The gate runs at `404f13b501c`, and its ordering waits on SOLR-9598.

## Corrections to the earlier application report

- The caveats I sent had a garbled fragment, "either denial.". The sentence I meant: I did not work around the classifier's denials of a `git commit --amend` and of a push.
- The three history rewrites are covered by Nick's standing agreement to force-push. No further confirmation is pending on them.
- The long SOLR-15674 draft is accepted for a complex ticket.

## Not done

- No JIRA writes, no PR opens, no comments, and no live PR description edits.
- No build, test, or Gradle run here. All of that is in `assignments/pool-solrcloud-vm2-round-2.md`.
