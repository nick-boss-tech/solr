# Skip record: SOLR-15237

- Ticket: SOLR-15237, Distributed search with index sharding is not working with basic authentication plugin enabled. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-03-09, updated 2024-02-27, no fix versions, duplicates SOLR-15235 and SOLR-15236 (both Resolved). The 2024-02-27 comment says it reproduces on 9.0 and 9.5.
- Verdict: pr-needed.
- Draft branch: solr-15237-draft-pr (new; supersedes the keep-open draft solr-15237-draft-open, which is kept).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and every cited name grepped at this head (detached worktree).
- Source: remediation redo of batch 03, item 19, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (vm2, from the C reading) cited a commit and lines that are confirmed here (see below).

## Ticket reading

With BasicAuthPlugin on (blockUnknown true, forwardCredentials false), a distributed query across shards on standalone nodes returns 401 on the sub-requests, so index sharding fails. The reporter follows the 8.8 distributed-search docs. Duplicates SOLR-15235 and SOLR-15236 are resolved.

## Code reading at the pinned head

- solr/core/src/java/org/apache/solr/core/CoreContainer.java:603-611, `setupHttpClientForAuthPlugin`: the security builder is installed on the shard handler factory, the update shard handler and the Solr client provider only when `pkiAuthenticationSecurityBuilder != null`. Lines 608-610 are the three setSecurityBuilder calls.
- solr/core/src/java/org/apache/solr/core/CoreContainer.java:839-840: `pkiAuthenticationSecurityBuilder` is constructed only in the ZooKeeper branch (the block that calls `zkSys.getZkController()`).
- solr/core/src/java/org/apache/solr/handler/component/SearchHandler.java:190-191: a per-handler shard handler factory is given `core.getCoreContainer().getPkiAuthenticationSecurityBuilder()`. The getter is at CoreContainer.java:670.

So on a standalone node (no ZooKeeper), the PKI builder is null, and no builder is installed on any shard client. The shard sub-requests carry no credentials. With blockUnknown set, the receiving node rejects them with 401, which is the failure the ticket reports.

The grep at the pin for `HttpClientBuilderPlugin` in solr/core/src/java returns the interface uses in HttpSolrClientProvider.java:80, HttpShardHandlerFactory.java:325, ShardHandlerFactory.java:34 and AuthenticationPlugin.java:135. BasicAuthPlugin is not one of them: `public class BasicAuthPlugin extends AuthenticationPlugin implements ConfigEditablePlugin, SpecProvider` (BasicAuthPlugin.java:48-49).

The credential forwarding the reporter's configuration uses (`forwardCredentials`, read at BasicAuthPlugin.java:55 and :75) is applied in BasicAuthPlugin.interceptInternodeRequest (BasicAuthPlugin.java:215-216). That method is called only from the PKI listener's onQueued (PKIAuthenticationPlugin.java:327). The listener exists only when pkiAuthenticationSecurityBuilder is set, which is the ZooKeeper case. So in standalone mode the forwarding code is never reached.

## History

- The commit cited in the earlier C4 reading is 739fe3b0a5 (2022-02-08, SOLR-14569, "Configuring a shardHandlerFactory on the /select requestHandler results in HTTP 401 when searching on alias in secured Solr"). It is an ancestor of the pin. Its subject is about the SearchHandler per-handler factory, which is the line 190-191 path above. The record does not claim that commit removed a standalone wiring: this pass did not read its diff, so the removal claim in the earlier record is not confirmed.
- No commit names SOLR-15237.

## Proposed change (recorded choice)

The change is to make the internode listener exist in standalone mode, so that the existing BasicAuthPlugin forwarding (BasicAuthPlugin.java:215-216) runs for shard sub-requests. That means building `PKIAuthenticationPlugin` (CoreContainer.java:839-840) outside the ZooKeeper branch. The constructor takes a node name (CoreContainer.java:841 passes `zkSys.getZkController().getNodeName()`), which standalone mode does not have. So the choice is which node name the standalone listener uses. Recorded choice for the PR draft: use the core container's configured node name, which the review must confirm exists at this head. The ZooKeeper path is unchanged. The owner may overrule.

## Check the PR would add

A standalone test: two nodes with BasicAuthPlugin (blockUnknown true), a distributed query across both with credentials, expecting 200, and the same query without credentials expecting 401. The test is not written here; it is named for the PR.

## Reproduction attempts

Not run in this pass. The code path above shows the failure. A run on the pin would start two standalone nodes with BasicAuthPlugin and a sharded query, which this pass did not do.

## Verdict justification

pr-needed: the code path shows the failure for standalone nodes (CoreContainer.java:603-611, :839-840; SearchHandler.java:190-191), the reported symptom (401 on shard sub-requests with blockUnknown) follows from it, and a change is needed in the standalone shard path. The choice of fix is recorded above and can be changed in review.

Caveat: the reproduction on the pin is not run; this verdict rests on the code path, which the rubric accepts for pr-needed.

## Limits

This record does not reproduce the 401. It does not post anything to JIRA or GitHub. No PR is opened from this draft.
