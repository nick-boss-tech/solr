# SolrCloud round 1, part p1 (SOLR-15674, 5813, 17292, 12991)

Result: 5813 and 12991 are draftable. 15674 is draftable as the default-path half of a pair with 15478. 17292 is drafted but held until the node-down path (Finding 1) is decided or fixed. Drafts are in `pr-drafts/solrcloud/`.

Heads checked (all match the claim table and `origin/solr-<ticket>-submit`): 5813 `90b8baa08aefd83cbee7e6e17ca9c59fcc93b30f`, 15674 `ba01c83d4c5ad03c7af63fd4061df119a8146d49`, 17292 `e43200b0fb6da9c5f443b9368628e5f2103f8e31`, 12991 `1a86966179e1817e4572fb0008a3b82a7c14184f`, 15478 `0478bdf0ac5cd100d5020451a6732ca5cbaa1a52`. Bases: 5813 `cabedd1d968`, 15674 `b5c71bc5573`, 17292 `14c7aac0d15`, 12991 `97d973814336`. Trial merges against local `upstream/main` (8e62c268688) are clean for all four.

## Findings

1. **FIX (17292, branch change, owner decision before opening).** `ZkController.java` L3051-L3085 (`publishNodeAsDown`, the non-distributed `else` branch). The per-collection `persist` at L3064 and the DOWNNODE `offer` at L3070-L3075 sit in one `try`. A KeeperException from `persist` jumps to the catch at L3082-L3083, so the offer is skipped for the whole node. Base `persist` returned quietly, so the offer ran. The Overseer handles DOWNNODE with `NodeMutator.downNode` (`Overseer.java` L579-L580), so this skips the node-down work for every collection on the node, not only the per-replica one. Replacement for L3064 (wrap the one call; the loop then continues and the offer still runs):
   ```java
               try {
                 PerReplicaStatesOps.downReplicas(
                         replicasPerCollectionOnNode.get(collName).stream()
                             .map(Replica::getName)
                             .collect(Collectors.toList()),
                         PerReplicaStatesOps.fetch(
                             coll.getZNode(), zkClient, coll.getPerReplicaStates()))
                     .persist(coll.getZNode(), zkClient);
               } catch (KeeperException e) {
                 log.warn("Could not mark replicas down in per replica states for {}", collName, e);
               }
   ```
   Not applied (read-only). If applied, the 17292 draft's Choice section is removed and its "What this change does" bullet for node-down changes to "logged as a warning; the node-down message is still sent".

2. **NOTE (17292, behavior to state).** `ZkStateWriter.java` L263-L274 and L342: when `persist` throws inside the flush, `updates.clear()` at L342 is skipped, so the commands stay queued. `Overseer.java` L375-L377 (queue removal after the flush) is skipped, and the catch at L387-L390 logs and sets `refreshClusterState = true`. The exception is not BadVersion, so `invalidState` stays false (`ZkStateWriter.java` L348-L351). The draft states this. No replacement needed unless the owner wants a bounded retry.

3. **NOTE (17292, duplicate logging).** `PerReplicaStatesOps.java` L151-L156 logs at ERROR and then throws. A caller that logs the exception again logs it twice. Optional: drop the log call at L152-L155, or use WARN. The draft's Limits says so.

4. **NOTE (15674 and 15478 premises differ; drafts must not share the same premise line).** The 15478 cache exists only with `shareSchema`: `ConfigSetService.java` L267-L269 (`schemaCache = shareSchema ? ... : null`) and L304 (`configSet != null && schemaCache != null`). The 15674 check runs on the object cache, which is not behind `shareSchema`: `SolrConfig.java` L199-L209 (`getFromCache` with `loader.getCoreContainer().getObjectCache()`), `IndexSchemaFactory.java` L120-L137 (ZooKeeper `solrCloudManager` object cache), and `CoreContainer.java` L284 (`new ObjectCache()`, always set). The 15674 draft says the configset cache is the one `shareSchema` enables. Suggested sentence for the 15478 draft's Limits (not my draft, not edited): "SOLR-15674 makes the SolrCloud object-cache check use the same ID, so the default path is covered there. This change covers only the configset cache that shareSchema enables."

5. **NOTE (version-reset wording must match).** The 15674 draft uses the 15478 draft's account: "A new znode starts that version over at 0" and "ZooKeeper does not reuse this ID" (`pr-drafts/configsets/SOLR-15478.md`, "What happens today" and "What this change does"). Keep these two sentences identical in both drafts if either is edited.

6. **NOTE (15674, watcher not covered).** `SolrCore.java` L3413, L3423-L3425 and L3467-L3475 (`checkStale`) still compare data versions (`stat.getVersion() > currentVersion`, L3475). A missing node returns true (L3471-L3472). A node deleted and created again before the check, at a version not above the loaded one, does not trigger a reload. The branch does not touch this. The draft's Limits says so. Owner decides: Limits line (drafted) or a follow-up ticket.

7. **NOTE (15674, managed-schema fallback is never cached).** `ManagedIndexSchemaFactory.java` L251 (`schemaInputStream = new ByteArrayInputStream(data);`) gives a plain stream, so `loadConfig` sets mzxid to -1 (`IndexSchemaFactory.java` L149). The check at L187 then never matches, and that schema is parsed on each load. Replacement for L251 (the `stat` variable is declared at L217 and reused at L249):
   ```java
             schemaInputStream = new ZkSolrResourceLoader.ZkByteArrayInputStream(data, managedSchemaPath, stat);
   ```
   Not applied. The draft's Limits names the gap. If applied, delete the last Limits bullet.

8. **NOTE (15674, public signatures).** `ZkSolrResourceLoader.java` L56 now returns `Pair<String, Stat>`, and `IndexSchemaFactory.java` L213 adds `public VersionedConfig(int, long, ConfigNode)`. In-repo callers are `SolrConfig.java` L387, `IndexSchemaFactory.java` L150, and the test. The draft states the change.

9. **NOTE (15674, test coverage).** `IndexSchemaFactoryCacheTest.java` L48-L83 uses a mocked `ZkSolrResourceLoader` (L51-L53) and a fake supplier (L63-L68). It does not cover the `SolrConfig.ResourceProvider` change (`SolrConfig.java` L161, L170, L389). The draft's Limits names this.

10. **NOTE (15674 and 15478 landing, no code dependency).** The two branches share no file (`git diff --name-only` against each base). Trial merge of `solr-15674-submit` with `solr-15478-submit` is clean. Either order merges. Recommendation in Owner decisions.

11. **NOTE (5813, existing cores).** `CoreDescriptor.java` L209 builds the `CloudDescriptor` from persisted core properties, so a core that already has an empty `collection=` now loads under its core name. This is untested. The draft's Limits says so.

12. **NOTE (5813, setter).** `CloudDescriptor.java` L103 (`setCollectionName`) has no guard. Its only main-source caller is `reload` (L128-L131), which already skips empty values. No change needed.

13. **NOTE (5813, receipt and ticket agree; ticket title differs).** The Jira packet title asks for a clean failure ("should fail nicely"). The 2014 commit messages (`research/jira-context/SOLR-5813.json`, comments 2 and 4) say the tests check for "should default to core name". The draft states both. The receipt's Choice matches.

14. **NOTE (12991, receipt wording is not what the ticket says).** The receipt says "the ticket accepts either" WARN or ERROR. The Jira packet (`research/jira-context/SOLR-12991.json`) has the reporter asking for "WARNs or ERRORs". The 2018 comment by Mark Miller says a failed connection during cluster changes is normal and "more informational". The base already logs at ERROR (`RecoveryStrategy.java` base L830 and L835), so keeping ERROR meets the ticket as written. The draft's Choice says this.

15. **NOTE (12991, log volume).** Each failed attempt now writes a stack trace (`RecoveryStrategy.java` L830-L837 on the branch). Limits line drafted.

16. **NOTE (12991 against 7394, consistency only, not in p1 scope).** The 12991 hunk (branch L829-L837) does not overlap the 7394 hunks (line numbers against 7394's base `56ec140e363`: L67, L202-L211, L478-L489, L654-L659). Trial merge of `solr-12991-submit` with `solr-7394-recovery` is clean.

17. **NOTE (12991, test shape).** `RecoveryStrategyLeaderUnreachableLogTest.java` rewrites the leader's `base_url` in `state.json` (helper `setLeaderBaseUrl`) and does not stop a node. Limits line drafted.

18. **NOTE (other parts' drafts in the shared folder).** `pr-drafts/solrcloud/` already holds drafts from other parts (SOLR-9155, 11288, 13186, 15106, 15386, 17680, 13369, 14919, 15035). Four of them (SOLR-9155, 13186, 15106, 15386) contain the words "the gate record gives no run date" in an Open line. That is internal wording that must not reach a public post. Not mine; flagged for the lead.

## Task results

**SOLR-15674 (draftable, drafted at `pr-drafts/solrcloud/SOLR-15674.md`, head ba01c83d4c5).** The receipt is accurate: head matches, the proof is inconclusive by construction, and the test cannot compile on base (base `VersionedConfig` has two arguments and `getZkResourceInfo` returns `Pair<String, Integer>`). The Jira packet (`research/jira-context/SOLR-15674.json`) confirms the changelog example (recreate a deleted collection with a different schema). Verdict: draftable as the default-path fix. Choice: mzxid alone against the earlier creation-ID-plus-version route (commits 4cc98731784 and 9b51865). Limits: configset cache (15478), core watcher (Finding 6), mocked test and solrconfig path (Finding 9), managed fallback (Finding 7). Draft is about 4.4 KB because of the citation links; it is over the 3,500-character guide but the ticket is complex.

**SOLR-5813 (draftable, PR-ready, drafted at `pr-drafts/solrcloud/SOLR-5813.md`, head 90b8baa08ae).** The receipt is accurate. The base line that caused the failure is `CloudDescriptor.java` L53 (`getProperty(CORE_COLLECTION, coreName)` keeps ""), so the base run's one failure on `testEmptyCollectionNameDefaultsToCoreName` (test L33-L38) follows from reading the code. The run log is not on disk. The branch is 37 commits behind local upstream/main, the same line is still on that ref, and the merge is clean. Verdict: PR-ready. Choice: default to the core name (drafted) against rejecting the empty name (title). Limits: SOLR-5811 (not on disk, only the 2014 comment is checked), existing core.properties (Finding 11).

**SOLR-17292 (held, drafted at `pr-drafts/solrcloud/SOLR-17292.md`, head e43200b0fb6).** The receipt said call sites were not traced. They are now traced: six call sites, all listed in the draft. The Jira packet says "The correct behavior should be to throw the relevant exception", so the change matches the ticket. The new test is 4 of 4 (test L143-L176). On base, its second half fails, because the always-stale persist returns without throwing (by reading the base loop; no run log). Hold reason: Finding 1 (node-down skip). The draft's Choice and the node-down bullet change once the owner decides. Do not post before then.

**SOLR-12991 (draftable, drafted at `pr-drafts/solrcloud/SOLR-12991.md`, head 1a86966179e).** The receipt is accurate for the code: the two base catches log at ERROR without the exception (base L830, L835), and the branch adds the exception and moves to WARN. The test waits 60 seconds for a WARN with the cause (test L71-L79). Verdict: draftable. Choice: WARN against keeping ERROR (Finding 14). Limits: two catches only, stack trace per retry, simulated leader (Findings 15 and 17).

## Owner decisions

1. SOLR-17292, node-down path (Finding 1): apply the replacement (recommended), or keep the skip and post the Choice as drafted.
2. SOLR-15674 and SOLR-15478 landing order: no code dependency. Recommendation: 15674 first. It covers the default SolrCloud path, while 15478 covers only `shareSchema` and its draft is held on that premise (`reports/configsets-round-1-b.md`, Finding 2).
3. SOLR-15674 Choice: post the mzxid-alone question as drafted, or drop it.
4. SOLR-15674 managed fallback (Finding 7): apply the one-line replacement before opening, or keep the Limits line.
5. SOLR-15674 core watcher (Finding 6): accept the Limits line, or open a follow-up.
6. SOLR-12991 level: WARN (drafted), or keep ERROR (allowed by the ticket).
7. SOLR-5813 behavior: default to the core name (drafted), or reject an empty name (ticket title). Also whether the existing core.properties effect needs more than a Limits line.

## Not checked

- No builds, tests, or Gradle. No `gh` calls. Nothing committed, pushed, or posted.
- Gate logs are not on disk (`g5813-gate.log`, `g15674-harden.log`, `g17292-harden.log`, `g12991-gate.log`, `g12991-gate2.log`). Receipt results are taken as recorded. Base-run failures for 5813, 17292 and 12991 are checked by reading the base code, not by a run.
- Jira content comes from the local packets in `research/jira-context/`, not live Jira. The SOLR-5811 packet is not on disk.
- Test compile: helper names checked by grep only (`LogListener.warn`, `assumeWorkingMockito`, `PerReplicaStatesOps.get`). The `PerReplicaStates` constructor used by the 17292 test is not checked.
- Trial merges used local `upstream/main` (8e62c268688). Not refetched.
- Not traced: callers of `publish` and `unregister` above the call sites in Finding 1; the default value of the distributed state update setting; Overseer retry behavior under sustained contention; `SolrConfig.effectiveId` (`SolrConfig.java` L1127-L1128, used at `SolrCore.java` L1109), which still uses the data version.
- The changelog YAML was read by eye, not parsed. The receipts say it parses.
- No Lucene version is named in these four drafts, so there is no version list to check.
- The 15478 draft and the configsets report were read for wording only; they were not re-audited.
