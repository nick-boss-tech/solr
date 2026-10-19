# Skip record: SOLR-13708

- Ticket: SOLR-13708, when deleteById call with _route_ is forwarded from leader to replicas, version is getting dropped at SolrJ unmarshal and making replicas go into recovery. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-08-21, updated 2020-01-21, no fix versions, no links, no comments.
- Verdict: close-fixed.
- Draft branch: solr-13708-draft-close (new; supersedes the keep-open draft solr-13708-draft-open, which is kept).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Fix commit, ancestry and changed code checked at this head (detached worktree).
- Source: remediation redo of batch 05, item 25, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-3, 2026-10-06, confirmed-skip) said the reporter's change is already on main; the commit and the lines are checked here.

## Ticket reading

When a leader forwards a deleteById that carries `_route_` to its replicas, the version is dropped when the SolrJ codec unmarshals the request, and the replica then goes into recovery. The reporter gives the change they want in the `_route_` branch of JavaBinUpdateRequestCodec: pass the version on that path.

## Fix commit and code at the pinned head

- solr/solrj/src/java/org/apache/solr/client/solrj/request/JavaBinUpdateRequestCodec.java:136-140: in the `_route_` branch, `updateRequest.deleteById(entry.getKey(), (String) params.get(ShardParams._ROUTE_), version)` (the version is passed on this path). The non-route branch keeps `deleteById(entry.getKey(), version)` at :140.
- The commit that made this change is 5c3465eb49 (2021-08-16). Its message is "SOLR-8889: Fixed various problems in Solr and SolrJ that could cause deleteById commands with "_route_" information to processed by the wrong shard, and/or fail when forwarded to replicas from the shard leader." It changes solr/solrj/src/java/org/apache/solr/client/solrj/request/JavaBinUpdateRequestCodec.java and solr/solrj/src/java/org/apache/solr/client/solrj/request/UpdateRequest.java.
- `git merge-base --is-ancestor 5c3465eb49 498a09aea97aac86c2cc7ac114113b946d249942` succeeds, so the fix is on the pin.

The commit does not name SOLR-13708. The link to this ticket rests on the same code change (version carried on the `_route_` delete path) and the same symptom (a `_route_` delete forwarded to replicas fails). That is the reporter's own requested change, and it is what the code now does.

## Reproduction attempts

Not run. A run would send a `_route_` delete through a leader to a replica and check the replica stays active. This pass did not start a cluster; the record rests on the code change and the commit message.

## Gap analysis

The reporter's requested change is in the code, and the commit describes the same failure. The record does not show a run of the replica recovery scenario on the pin, which is the one check left.

## Verdict justification

close-fixed: a commit on main (5c3465eb49, SOLR-8889) makes the change the reporter asked for in the `_route_` delete branch of JavaBinUpdateRequestCodec, the commit is an ancestor of the pinned head, and the changed code is present at JavaBinUpdateRequestCodec.java:136-140. The commit names a different ticket, so the link to this one is by the code change and the symptom, stated here.

## Limits

This record does not run the replica recovery scenario. It does not post anything to JIRA or GitHub. No PR is opened from this draft.
