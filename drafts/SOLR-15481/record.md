# Skip record: SOLR-15481

- Ticket: SOLR-15481, Exception when starting a node with index replication. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-06-16, updated 2021-06-23, no fix versions, no links, no comments.
- Verdict: pr-needed.
- Draft branch: solr-15481-draft-pr (new; supersedes the keep-open draft solr-15481-draft-open, which is kept).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at this head (detached worktree).
- Source: remediation redo of batch 03, item 6, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) is background only.

## Ticket reading

Upgrading from Solr 7.4 to 8.8.2, a leader/follower setup with no SolrCloud and no sharding. Starting a follower fails with "Invalid URL syntax in 'shards' parameter: null" during the index fetch. The reporter's workaround is `-Dsolr.disable.shardsWhitelist=true`, and they note the replication docs do not mention it.

## Code reading

- The exact 8.8.2 message is not on the pinned head. The "shards" wording is in solr/core/src/java/org/apache/solr/handler/component/CloudReplicaSource.java:143, which is the distributed search path, not replication. SOLR-15217 (c7abf3f32c, 2021-03-24) renamed the whitelist and extracted AllowListUrlChecker, which is the code that now runs for replication.
- solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:304-305: on a follower, the leader core URL is checked with `getAllowListUrlChecker().checkAllowList(List.of(leaderCoreUrl), clusterState)`. For a standalone node `clusterState` is null.
- solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:314-319: a SolrException from the check is rethrown as FORBIDDEN, "The 'leaderUrl' parameter value ... is not allowed", with the clue from AllowListUrlChecker.SET_SOLR_DISABLE_URL_ALLOW_LIST_CLUE.
- solr/core/src/java/org/apache/solr/security/AllowListUrlChecker.java:133-150: a URL passes only if its host:port is a live node of the cluster or is in the configured allow list. solr/core/src/java/org/apache/solr/security/AllowListUrlChecker.java:158-161: `getLiveHostUrls(null)` returns an empty set. So in standalone mode the only way to pass is the allow list.
- The disable switch at this head is the property `solr.security.allow.urls.enabled` (AllowListUrlChecker.java:46, read at :105-106). The reporter's `solr.disable.shardsWhitelist` no longer exists.

So a standalone follower whose leader host is not in `allowUrls` still fails at startup, with a FORBIDDEN error instead of the reported message.

## Documentation

solr/solr-ref-guide/modules/deployment-guide/pages/user-managed-index-replication.adoc, the leaderUrl description at :167 and the examples at :250 and :289, does not mention the allow list or the `allowUrls` setting. The allow list is documented only for shards and joins (user-managed-distributed-search.adoc:74-76, join-query-parser.adoc:321, json-combined-query-dsl.adoc:31). The reporter's complaint about the docs still holds at this head.

## History

- SOLR-15217 (c7abf3f32c, 2021-03-24) introduced the leader check in IndexFetcher's path (the file had no such check before this commit; its parent has none).
- SOLR-18164 (1af33ac699, 2026-04-24) changed the same IndexFetcher lines (singleton removal), not the check itself.

## Reproduction attempts

Not run. The code path above predicts the failure for a standalone follower with a leader outside `allowUrls`. A run would need a built distribution with two nodes.

## Gap analysis

The ticket asks two things: why replication fails on a non-sharded node, and that the workaround be documented. The first has an answer in code: it is the leader-URL allow list (SOLR-15217), which is a security check by design, not a whitelist bug. The second is a real gap: nothing on the replication page says the leader URL must be allowed. The disable switch named in the ticket is gone, so the docs change should name the current setting, `allowUrls` in solr.xml and `solr.security.allow.urls.enabled`, not the old property.

## Verdict justification

pr-needed: the code path shows the failure for a standalone follower outside the allow list (IndexFetcher.java:304-319, AllowListUrlChecker.java:133-161), and the change needed is a documentation change on the replication page naming the allow list and its setting, instead of the removed property. The PR would add a paragraph and an example to user-managed-index-replication.adoc near the leaderUrl description. The check is a ref-guide build plus a reviewer read of the setting names against AllowListUrlChecker.java:46.

Caveat: the code change in question is a design choice (the check is intended). If maintainers prefer to relax the check for standalone followers, the PR is different, and that is a maintainer decision.

## Limits

This record does not reproduce the failure on a built distribution. It does not post anything to JIRA or GitHub. No PR is opened from this draft.
