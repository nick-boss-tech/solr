# Flaky-fix review round 1, slice 1: SOLR-18530

## Verdict

**Ready for draft. Hold the opening for owner decisions D1 to D4.**

The branch is a test-only change. It makes the same routing change as the primary fix in the root-cause part report (test 2). The fixed test passed 6 of 6 runs. The original test passed 5 of 5 at the CI seed, so the mechanism is not reproduced. The draft says so and claims no before-and-after result.

Draft: `pr-drafts/flaky-fixes/SOLR-18530.md` (about 3,550 visible characters).

## Inputs and head check

- `git rev-parse origin/solr-18530-submit` = `98e5368d996518b9f4a94f85d7c2fcd933d3f485`, equal to the named head. Base `3f5d4c5bf8ac` checked.
- Read: assignment slice 1, `pr-formula.md`, `WORKFLOW.md`, `reports/flaky-tests-root-cause-round-1-t2.md`, `receipts/SOLR-18530.md`, `gates/SOLR-18530.md`, the branch diff, both changed files at head, `dev-docs/changelog.adoc` at head, `CheckRetryUnrollTest.java` at head.
- Ticket text: not in any local file. A search for 18530 finds only references in the assignment, claim, gate, receipt and report files. The verdict relies on the t2 report. No Jira or web call was made.
- The gate log (`g18530-gate.log` on vm1) is not in this workspace. The receipt is the only Proof source, and it is used as written.
- Head commit author is Nick Shanin, the ICLA name. The commit has no body and no trailers.
- No em dashes or en dashes in the diff, the changelog file, or the test file. No `ICLA pending`, `Solr Issues Workspace`, `Claude`, or `Co-Authored` strings in the diff.

## Audit answers

**Q1. Does the change match the t2 mechanism? Yes, with one gap.**
- The change is the t2 Part 4 primary fix. The snippet matches the head test at `solr/core/src/test/org/apache/solr/cloud/RecoveryAfterSoftCommitTest.java` lines 108-117 (`createNewCloudSolrClient(zkServer.getZkAddress(), DEFAULT_COLLECTION, true, 30000, 120000)`), with the same arguments.
- Gap: t2 Part 5 steps 0 and 1 were not done. Step 0 (compare the CI failing URL port with the cut proxy port) needs the CI log, which is not here. Step 1 (N = 20 base runs, expecting failures in any-replica runs) was not done. The receipt shows 5 base runs with 0 failures. The mechanism is consistent with the code but not shown by reproduction.

**Q2. Does the test still prove what it exists to prove? Yes.**
- The cut is unchanged: `getProxyForReplica` at line 97, `proxy.close()` at line 99.
- `MAX_DOCS = 2 + MAX_BUFFERED_DOCS + ULOG_NUM_RECORDS_TO_KEEP` = 6 at line 107 is unchanged. The first loop (lines 83-88) adds 3 documents. The post-cut loop adds i = 3 to 5, which is 3 documents, more than `ULOG_NUM_RECORDS_TO_KEEP` = 2. Peer sync is still excluded.
- Pre-existing, not caused by this change: the test body has no assertion on document counts or on the recovery path. Its only check is `ensureAllReplicasAreActive(..., 30)` at line 130. The draft's Limits says this.

**Q3. Are the post-cut adds correct on the leaders-only client, with no path left on the pooled connection? Yes in the normal case, with one caveat.**
- The new client is built after the cut (lines 108-110), so its connection pool starts empty. Its only pooled connections go to the leader's proxy, which the test does not close.
- `createNewCloudSolrClient` (`solr/test-framework/src/java/org/apache/solr/cloud/AbstractFullDistribZkTestBase.java` lines 2431-2449) calls `sendUpdatesOnlyToShardLeaders()`. That sets only `shardLeadersOnly` (`solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java` lines 1416-1417), which feeds `updatesToLeaders` (`CloudHttp2SolrClient.java` line 48).
- The helper's randomizing builder sets `directUpdatesToLeadersOnly` at random (`solr/test-framework/src/java/org/apache/solr/SolrTestCaseJ4.java` lines 2517-2518). The helper never sets it, so the flag varies by run.
- With `directUpdatesToLeadersOnly` true, `buildUrlMap` lists only the leader (`CloudSolrClient.java` lines 470-482). The cut replica is in no endpoint list.
- With it false, the list is the leader first, then the other replicas, including the cut one (same lines). The cut replica is a fallback.
- Failover for updates is only on a connect exception or `RequestNotSentException` (`solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java` lines 675-677). A `ClosedChannelException` from the leader does not move the add to the cut replica.
- Caveat: if the leader refuses a connection, LB can move the add to the cut replica. A fresh connect there is refused by the closed listener, so the stale pool is not used. The leader is never cut in this test, so this is not expected. It is not a defect for this failure. The comment and the draft say "leader first", not "never".
- Leader-side forwards: `DistributedZkUpdateProcessor.java` lines 1217-1240. A failed forward from the leader to a non-forward replica is logged and not returned to the client. The leader's own pooled connection to the cut replica therefore does not reach the client.
- Client lifetime: `leaderClient` is closed by try-with-resources at line 117, before the sleep and the reopen. `cloudClient` is not used after the cut.

**Q4. Is the changelog YAML plain, authored by Nick Shanin, and free of placeholders and em dashes? Yes on form, open on content.**
- `changelog/unreleased/SOLR-18530.yml`: five keys, no colon inside the title, so the unquoted scalar is valid YAML. `type: fixed` is an allowed value (`dev-docs/changelog.adoc` line 46). Author `Nick Shanin`. One link, SOLR-18530, with the JIRA URL. No placeholder, no em dash.
- Content issue (D1): `dev-docs/changelog.adoc` section 4 (lines 125-133) exempts pull requests that touch only tests. The type table reserves `fixed` for fixes to buggy behavior, and says that test infrastructure is `other` and usually needs no entry. The title describes test internals, not a user-visible fix. Not a blocker.

## Findings

1. **Test comment states the cause as fact (nit, optional).** `RecoveryAfterSoftCommitTest.java` lines 102-106. Support in code: the "IOException occurred" message is thrown only when `committed` is true (`HttpJettySolrClient.java` lines 472-490); `ClosedChannelException` is not in the cloud retry set (`CloudSolrClient.java` lines 214-216); `CheckRetryUnrollTest.java` lines 97-103 (in the base branch) asserts that `ClosedChannelException` is non-retriable. The "after the write has committed" part remains an inference (t2, about 80 percent). Optional rewording: "can fail after the request has reached the network".
2. **Gate evidence cannot be checked here.** The gate log is on vm1. The receipt's wording "consistent with the earlier settling runs" refers to runs not in the workspace. The draft does not cite them.
3. **Repetition is below the t2 plan.** t2 Step 1 asks for N = 20 base runs with failures expected in any-replica runs. The receipt has 5 base runs and 6 fixed runs, with no base failure. The draft says the before-and-after is inconclusive.
4. **Helper randomization.** See Q3. The fixed test passed 6 of 6 with the randomized helper, so changing it needs a new gate run.
5. **Other tests not checked.** t2 lists tests that cut a proxy (ForceLeaderTest, HttpPartitionTest, and others) and does not check whether they write through the cloud client after the cut. The draft names this in Limits and offers a follow-up.
6. **Draft choices.** The draft keeps the "A choice to check" section (leaders-only client versus retry in the test). The retry option is a live alternative with a real cost (it keeps any-replica writes during the partition), so the section fits pr-formula section 4. Owner may drop it (D4).

## Owner decisions (flagged, not taken)

- **D1. Changelog.** Keep the YAML and retitle it to a user-facing description, or drop it. If dropped, the draft's last line should become the section 4 exemption wording, not the `Changelog:` line. The draft currently keeps the line.
- **D2. More evidence before opening.** Whether to run t2 Step 0 (CI log port check) and more base runs (t2 Step 1, N = 20). That would turn "not reproduced" into a before-and-after. It needs a test run, which this review does not do.
- **D3. Routing determinism.** Keep the randomized helper (tested 6 of 6), or set `sendDirectUpdatesToShardLeadersOnly()` explicitly on a plain builder. The second option needs a new gate run.
- **D4. Choice section.** Keep or drop the leaders-only versus retry section in the draft.
- **D5. Opening.** The PR opening, and the claim DONE mark, stay with the main agent and Nick's approval. This review did not touch the claim file, the receipt, the gate file, or the branch.

## Files written

- `C:/Users/shaninna/dev/Solr-issues/wt/pr-prepare-suggester/reports/flaky-fix-review-round-1-s1.md` (this report)
- `C:/Users/shaninna/dev/Solr-issues/wt/pr-prepare-suggester/pr-drafts/flaky-fixes/SOLR-18530.md` (draft)
