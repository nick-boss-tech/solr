# SolrCloud round 1, part p5 (audit only)

Result: no ticket is draftable this round; all six live heads match the claim and the receipts and none moved; 6 FIX and 15 NOTE items; the most important FIX is item 1 (handoff notes and process wording on all six outbound tips).

Scope: read only. Heads checked with `git ls-remote` (read only) and `gh pr list` for nick-boss-tech on apache/solr (read only). Trial merges used `git merge-tree --write-tree` (no ref written). No builds, tests, Gradle, gh writes, drafts, or commits.

## Heads checked

| Ticket | Branch | Claimed and live head | Receipt | Branch base | Result |
|---|---|---|---|---|---|
| 3865 | solr-3865-submit | 363e8f0651e9 | NO GATE | cabedd1d968 | match |
| 4754 | solr-4754-submit | d2e9038881f6 | NO GATE | cabedd1d968 | match |
| 10234 | solr-10234-submit | 16825538a766 | NO GATE | cabedd1d968 | match |
| 10641 | solr-10641-submit | 4ab4bd2040f3 | NO GATE | cabedd1d968 | match |
| 16437 | solr-16437-submit | 673ae584de03 | NO GATE | e2cdb2d7e8ae | match |
| 17281 | solr-17281-submit | ba21ca32791b | NO GATE, parked | 86bc6f29224 | match |

Main is 8e62c2686882 (upstream/main, live). None of the six has a PR on apache/solr.

## Findings

1. FIX. All six tips. Process notes sit on the outbound branches.
   Evidence: root note files SOLR-3865-TESTING.md (363e8f0651e9), SOLR-4754-TESTING.md (d2e9038881f6), SOLR-10234-TESTING.md (16825538a766), SOLR-10641-TESTING.md (4ab4bd2040f3), SOLR-16437-TESTING.md (673ae584de03), SOLR-17281-DECISION.md (ba21ca32791b; its own text says "remove before any PR"). The seven live or gated branches checked (7394, 13186, 15106, 9155, 5813, 18277, 17292) carry no root SOLR-* file. The code commits d145b5db774, 390b1938b50, 48784679f96, a0a8e2325f3 and b7d642d4caa each have the body "Hypothetical, unrun regression test; see SOLR-<n>-TESTING.md." The handoff commits have subjects such as "add hypothetical-reproduction handoff doc". Those words are process vocabulary under the shared rules.
   Replacement: before any push or open, drop the note file and the handoff commits, and delete the body line from each code commit. The subject lines can stay. Rewriting the fork history needs Nick's go-ahead (decision 8).

2. FIX. SOLR-4754. ZkController.java lines 1019-1020 at tip d2e9038881f6. The error text names a property the shipped config does not read.
   Evidence: solr/server/solr/solr.xml:42 reads `<str name="host">${solr.host.advertise:}</str>`. solr/bin/solr:934 sets -Dsolr.host.advertise. Nothing in solr/server or solr/core resources reads a plain ${host} property. So "-Dhost=<name>" does nothing with the shipped solr.xml.
   Replacement for lines 1019-1020:
   ```
             "Could not determine the host name to register in ZooKeeper; set the"
                 + " solr.host.advertise property (e.g. -Dsolr.host.advertise=<name>)"
                 + " to a host name");
   ```

3. FIX. SOLR-4754. The guard does not fix the ticket's symptom.
   Evidence: on main (and at tip) ZkController.java lines 1008-1009 send an empty value to AddressUtils.getHostToAdvertise(), which returns an IP address string (AddressUtils.java lines 34-62). The shipped solr.xml sends "" when unset (solr.xml:42). So the empty base_url in the JIRA (research/jira-context/SOLR-4754.json) cannot reach the new check. The new check fires only for a scheme-only value such as "http://" or a blank value. The changelog fragment changelog/unreleased/SOLR-4754-fail-on-empty-host.yml line 1 describes the registered name, and its links block (lines 6-8) cites the ticket as fixed.
   Replacement for line 1 of that fragment:
   ```
   title: Fail node startup with a clear error when the configured host is only a URL scheme (for example host=http://), which would register an empty host name in ZooKeeper.
   ```
   The PR text must say the ticket's empty value already falls back to the advertised address on main. Whether the SOLR-4754 link stays is decision 2.

4. FIX. SOLR-4754. The proof cannot fail on the base, so the fail-before verdict is INCONCLUSIVE by construction.
   Evidence: ZkControllerTest.java lines 82-93 (tip) call ZkController.normalizeHostName statically. On the base the method is `private String normalizeHostName(String host)` (cabedd1d968 ZkController.java line 1007), so the base test does not compile. SOLR-4754-TESTING.md has no fail-before section at all. Under the receipts rule this is "did not compile on old code".
   Replacement for SOLR-4754-TESTING.md: add a section "Fail-before: INCONCLUSIVE by construction. The test calls a helper that is private and non-static on the base, so the base does not compile. A real fail-before needs a test that reaches the guard through the constructor path." Do not claim a failing base run.

5. FIX. SOLR-3865. The JIRA path is already closed on main, and the changelog still links the ticket.
   Evidence: research/jira-context/SOLR-3865.json describes connect() creating a ZkStateReader and leaking it when the connect fails. Main closes that reader in each catch: ZkClientClusterStateProvider.java lines 162-174 (zk.close() at lines 167, 171 and 174). The branch fixes a different gap: close() before first use. On main close() is at lines 185-196, and the test condition is at line 187. The fragment changelog/unreleased/SOLR-3865-zk-provider-close-before-use.yml links SOLR-3865 (links block at lines 6-8).
   Replacement: keep the title on line 1. If the link stays, the PR text must say the failed connect path named in the ticket is already closed on main (lines 162-174). If the owner drops the link, delete the links block (lines 6-8). Decision 1.

6. FIX. SOLR-10641. The tidy fold-in the receipt names is still owed.
   Evidence: OverseerTaskQueueTest.java lines 54-56 at tip 4ab4bd2040f3 split the assertEquals arguments one per line. The joined one-line form is 104 columns, too long. The argument line fits in 95 columns. The receipt says the tidy commit is folded in when the branch returns.
   Replacement for lines 54-56 (inferred from line lengths; Spotless not run):
   ```
       assertEquals(
           "response", new String(zkClient.getData(watchID, null, null), StandardCharsets.UTF_8));
   ```

7. NOTE. SOLR-3865. The second test does not set a short connect timeout.
   Evidence: SOLR-3865 test file lines 37-38 (tip 363e8f0651e9). testCloseIsIdempotent builds the provider without setZkConnectTimeout(200). On the base, the test would wait the default 15000 ms (SolrZkClientTimeout.java lines 26-27) before failing.
   Replacement: insert `    provider.setZkConnectTimeout(200);` after line 38.

8. NOTE. SOLR-3865. Behavior change to state in the text.
   Evidence: after close(), every later call throws AlreadyClosedException, including a call that used to connect again (ZkClientClusterStateProvider.java lines 150-152 and 185-196 on main). The only in-tree consumer I read, KafkaCrossDcConsumer.java lines 110-117, already rebuilds its client when the provider is closed, so the change fits it. The other 20 files that name the class were listed, not read.

9. NOTE. SOLR-10641. The premise is partly on main already, and the pins pass on the base.
   Evidence: the ticket's second idea (several deletes in one multi) is on main: ZkDistributedQueue.java lines 213-232 batch deletes 1000 per transaction. The branch covers only the response write and request delete pair (OverseerTaskQueue.java lines 123 and 151-161). The ticket's async getData idea is not touched. Both new tests give the same result on the base (the TESTING note says so), so the receipt's NOT_PROVEN expectation holds. Any text must say the tests pin behavior and do not measure round trips.

10. NOTE. SOLR-10641. Both failure shapes of a multi are handled.
   Evidence: OverseerTaskQueue.java lines 156-161 return false when the multi throws NoNodeException or when a result carries a non-zero error. Either way the old sequential code runs. Not read: Curator and ZooKeeper client source, which decide which shape appears in practice.

11. NOTE. SOLR-16437. The premise holds in both modes (checked by reading).
   Evidence: Overseer path (default): ReplicaMutator.java lines 162-163 look the replica up, and the caller already got success after the queue offer. Overseer-disabled path: DistributedClusterStateUpdater.java lines 811-829 log and skip a failed mutation, so the caller also gets success. The new check at CollApiCmds.java lines 343-362 (tip 673ae584de03) runs before both paths. Its BAD_REQUEST reaches HTTP 400 through CollectionHandlingUtils.java lines 482-489 and SolrResponse.java lines 48-57 (main). The new test (CollectionsAPISolrJTest.java lines 1293-1323) compiles on the base, and its first expectThrows fails on the base because the base returns success.

12. NOTE. SOLR-16437. The same gap exists in DELETEREPLICAPROP, which the branch does not cover.
   Evidence: CollApiCmds.java lines 354-378 on main (DeleteReplicaPropCmd) has no existence check. The JIRA text (research/jira-context/SOLR-16437.json) asks for "other APIs" too.

13. NOTE. SOLR-16437. Behavior change to state in the text.
   Evidence: the new check looks for the replica inside the named shard (Slice.getReplica). ReplicaMutator.java line 163 looks it up across all slices (DocCollection.getReplica, main lines 382-391). A valid replica name with the wrong shard is now a 400. Before, it changed the replica in the other shard.

14. NOTE. SOLR-16437. The changelog author entry differs from the others.
   Evidence: commit 673ae584de0 ("fix the changelog author entry") removes `nick: nick-boss-tech` from changelog/unreleased/SOLR-16437-addreplicaprop-validate-inputs.yml. The field is optional (dev-docs/changelog.adoc line 41). SOLR-4754 and SOLR-10234 keep it. SOLR-3865 and SOLR-10641 never had it.

15. NOTE. SOLR-10234. The choice and its reach.
   Evidence: the branch adds @SuppressFileSystems({"ExtrasFS", "HandleLimitFS"}) to SolrCloudTestCase (solr/test-framework/src/java/org/apache/solr/cloud/SolrCloudTestCase.java line 88) and BaseDistributedSearchTestCase (solr/test-framework/src/java/org/apache/solr/BaseDistributedSearchTestCase.java line 112). Every cloud test then loses the handle limit check. The ticket also offers a per node limit, which needs a Lucene side change. The Lucene 10.4.0 test-framework jar in the local Gradle cache has org/apache/lucene/tests/mockfile/HandleLimitFS.class, and its SuppressFileSystems annotation is @Inherited (read from class bytes, so the branch's inheritance comment holds). The tree has no "HandleLimitFS" string on main, so the simple name match is unproven until a gate runs.

16. NOTE. SOLR-10234. Changelog type for a test only change.
   Evidence: the fragment changelog/unreleased/SOLR-10234-cloud-tests-no-handle-limit.yml uses `type: other`. dev-docs/changelog.adoc line 88 says most "other" changes are too minor for an entry. Decision 4.

17. NOTE. SOLR-17281. Parked and not ready.
   Evidence: the one-line change is ShardLeaderElectionContext.java line 383 at tip ba21ca32791b (`replica.isActive(clusterState.getLiveNodes())`, which replaces the live node check on the base). SOLR-17281-DECISION.md and research/branch-reviews/round-6/SOLR-17281-review.md both say "not ready" and name the same choice: revert, or replace after the logs are in. The ticket packet (research/jira-context/SOLR-17281.json) has no logs.

18. NOTE. All six. The branch bases are behind main, and trial merges are clean.
   Evidence: bases cabedd1d968 (2026-10-06) for 3865, 4754, 10234 and 10641; e2cdb2d7e8ae for 16437; 86bc6f29224 (2026-06-18) for 17281. `git merge-tree --write-tree upstream/main <tip>` exits 0 for all six. A re-base may be wanted before open. Decision 8.

19. NOTE. SOLR-16437 and SOLR-17281. Local branch refs lag the live heads.
   Evidence: refs/heads/solr-16437-submit is at 4fdcad1dc581 and refs/heads/solr-17281-submit is at e5a5dc3ff93. The remote tracking refs origin/solr-16437-submit (673ae584de03) and origin/solr-17281-submit (ba21ca32791b) match live. This is local lag, not a fork move. Do not use the local refs.

20. NOTE. Interactions with other parts. No forced landing order.
   Evidence: trial merges between tips are clean (exit 0): 4754 with 9155, 15386 and 16013 in ZkController.java (4754 at lines 1007-1021; 9155 hunks near base lines 1675 and 1724; 15386 near 75, 847, 3028 and 3075; 16013 near 881 and 902). 16437 with 11479 in CollectionsAPISolrJTest.java (16437 near base lines 1292-1323; 11479 near 497-516). No other round tip touches OverseerTaskQueue.java, ShardLeaderElectionContext.java, ZkClientClusterStateProvider.java, or the test-framework cloud base classes.

21. NOTE. Live PRs. None of the six is public.
   Evidence: `gh pr list --repo apache/solr --author nick-boss-tech --state all --limit 100` (read only) lists no PR with a head of any of the six branches.

## Task results

SOLR-3865: audit only, no draft. Verdict: not ready. The head matches live and the receipt (NO GATE). The close before first use gap is real by reading (ZkClientClusterStateProvider.java: lazy connect in getZkStateReader at lines 150-181, close at lines 185-196). The JIRA path the ticket names is already closed on main (item 5). The premise run must show that on the base (main with the test file overlaid) both tests fail with a connect failure, not AlreadyClosedException. The first gate must show both tests pass with the change, run as the queue rules require. Items 5, 7 and 8 apply. No head moved.

SOLR-4754: audit only, no draft. Verdict: not ready. The head matches live and the receipt (NO GATE). The guard works by reading: a scheme-only host throws SolrException, and that exception leaves ZkContainer.java's try (line 117) through catches that do not name it (lines 221-232), so node startup fails. But the guard does not cover the ticket's symptom (item 3), the error text names the wrong property (item 2), and the test cannot fail on the base (item 4). The premise run must show that a node started with solr.host.advertise=http:// registers an empty host on the base. The first gate must show ZkControllerTest.testNormalizeHostName passes. No head moved.

SOLR-10234: audit only, no draft. Verdict: plausible, not ready. The head matches live and the receipt (NO GATE). The change compiles by reading (AbstractFullDistribZkTestBase is in test-framework, package org.apache.solr.cloud). The base SolrCloudTestCase inherits only ExtrasFS (SolrTestCaseJ4.java line 147), so the pin test fails on the base. It pins the annotation only and does not reproduce the 2048 limit. Decision 4 must be taken first. The premise run is the pin failing on the base, and the first gate is the pin passing. The Proof text must say it pins the annotation. No head moved.

SOLR-10641: audit only, no draft. Verdict: not drafted. The two new tests pass on the base as well, so there is no failing proof (the receipt's NOT_PROVEN expectation stands). The Curator operation shape matches existing code (ShardLeaderElectionContextBase.java line 153, ZkDistributedQueue.java line 217), and both failure shapes reach the old code (item 10). The tidy fold-in is owed (item 6). Whether a passing pin ships as its own PR is decision 3. No head moved.

SOLR-16437: audit only, no draft this round. Verdict: the strongest of the six. It would be draftable once a first gate runs on the main side. The head matches live and the receipt (NO GATE). The GitHub corroboration run does not settle gate state. The premise holds in both modes (item 11), and the new test fails on the base at its first assertion. Items 12, 13 and 14 are scope and wording points for decision 5. The first gate must show testAddReplicaPropRejectsUnknownReplica passes with the change and fails without it. No head moved; the local ref lags (item 19).

SOLR-17281: parked, audit only, no draft. Verdict: not ready. The head matches live and the receipt (NO GATE, parked). The one-line change points the wrong way by the branch's own round 6 review, and the DECISION file agrees. No gate is owed until decision 6 is taken. No head moved.

## Owner decisions

1. SOLR-3865: keep the SOLR-3865 link (with the PR text saying the reported path is already closed on main), or drop the link. No new Jira ticket unless asked.
2. SOLR-4754: keep the scheme-only guard as the whole change (it does not fix the reported symptom), or drop it. Keep or drop the SOLR-4754 link.
3. SOLR-10641: ship the passing pin as its own PR, or fold it into a later change. Confirm the tidy fold-in first.
4. SOLR-10234: suppress HandleLimitFS on all cloud and distributed base classes (wide effect), or pursue a per node limit on the Lucene side. Also decide whether a test only change gets a changelog fragment.
5. SOLR-16437: scope. ADDREPLICAPROP only, or also DELETEREPLICAPROP and the other APIs the ticket names. Accept the wrong shard rejection (item 13).
6. SOLR-17281: revert the one-line change (the branch becomes empty) and ask the reporter for election logs and terms.json, or replace it after the root cause is known. Parked either way.
7. Changelog author `nick:` rule: 16437 removed it, 4754 and 10234 keep it, 3865 and 10641 never had it. Pick one rule.
8. Approval to rewrite the six fork branches (drop the handoff notes and process wording, item 1) and to re-base onto main (item 18), before any push or open.

## Not checked

- No builds, tests, Gradle, or gate runs. Compile, fail-before, and timing statements come from reading the code.
- Curator and ZooKeeper client source were not read, so it is not confirmed whether a failed multi throws or returns error results (item 10).
- Lucene's simple name matching for SuppressFileSystems was not read from source. Class bytes were checked only: HandleLimitFS exists and the annotation is @Inherited. The 2048 value was not checked.
- The DOWN-on-restart claim in SOLR-17281 was not re-derived. It comes from the round 6 review and the DECISION file.
- Spotless and google-java-format output was inferred from line lengths (item 6), not run.
- The changelog fragments were checked by reading against dev-docs/changelog.adoc only. No validator ran.
- For 16437, the Overseer-disabled path was read, not exercised, and per replica state collections were not read.
- For 3865, only the Kafka consumer's supplier was read for use after close. The other 20 files were listed only.
- The GitHub corroboration run for 16437 was not queried (no gh run call made).
- Live Jira was not queried. The local packets in research/jira-context were read.
- The ZkDistributedQueue batch code was read at its call site only.
