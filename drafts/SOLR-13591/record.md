# Skip record: SOLR-13591

- Ticket: SOLR-13591, AlreadyClosedException in SolrJ.
- Verdict: keep-open. This replaces the earlier close-obsolete verdict on solr-13591-draft-close, which this pass does not support.
- Draft branch: solr-13591-draft-open (new branch from origin/coordination; solr-13591-draft-close is left in place).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the earlier C4 reading (vm2, 2026-10-19) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-07-01, updated 2021-09-08, no fix versions. The reporter upgraded from 7.7 to 8.1 and saw AlreadyClosedException with a null message, raised from `ZkClientClusterStateProvider.getZkStateReader` and reached through `connect()` and the request retry path. The 2021-09-08 comment says the reporter traced the occurrences to calling `ZkClientClusterStateProvider.close()` and then using the client.

## Code reading

Read at the pin:

- The named connect path is gone. `ZkClientClusterStateProvider` (`solr/solrj-zookeeper/src/java/org/apache/solr/client/solrj/impl/ZkClientClusterStateProvider.java`) has no `connect()` method, and no `connect()` call remains in `solr/solrj/src/java` or `solr/solrj-zookeeper/src/java`. The class `BaseCloudSolrClient` is not present in the solrj tree. `CloudSolrClient.requestWithRetryOnStaleState` (`solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:619`) makes no connect call. Commit `0c77e420e8` (SOLR-18380, 2026-08-25, "remove CloudSolrClient.connect(), ClusterStateProvider.connect()") is an ancestor of the pin.
- The class the ticket is about still exists, and it still throws the same exception after close. `ZkClientClusterStateProvider.java:150-157` (`getZkStateReader`) throws `AlreadyClosedException` when `isClosed` is set, both on the quick check and inside the synchronized block. `:185-199` (`close`) sets `isClosed` and nulls `zkStateReader` before closing it. Public methods such as `getClusterState` (:134 area) and the alias calls (:103 to :113) call `getZkStateReader()`, so a call after close reaches the throw.
- Commit history: the throw was introduced with the class by `88bc36ae9e` (SOLR-15342, SolrJ-Zookeeper module split), an ancestor of the pin. Its exact introduction was not checked beyond that commit.

Grep at the pin:

- `grep -rn 'connect()' --include=*.java solr/solrj/src/java solr/solrj-zookeeper/src/java` returns no matches.
- `grep -rn 'class BaseCloudSolrClient' --include=*.java solr` returns no class definition.
- The ticket names no test class and no backticked identifier with a hit in the ref (evidence tool output).

## History

- `0c77e420e8` (SOLR-18380, 2026-08-25): removed `connect()` from the cloud client and cluster-state provider. Ancestor of the pin.
- `88bc36ae9e` (SOLR-15342): created the SolrJ-Zookeeper module with `ZkClientClusterStateProvider`, which contains the throw. Ancestor of the pin.
- No commit at the pin names SOLR-13591.

## Reproduction attempts

Not run. No build or test run in this pass. The reference guide was searched for a sentence that answers a call to a closed SolrJ client; none was found under `solr/solr-ref-guide/modules` (the matches were JDBC and stream examples), so close-answered is not available.

## Gap analysis

- close-obsolete does not hold. The rubric requires that the class or code the ticket is about has been removed and that a search finds no remaining use. The class remains, and the throw in `getZkStateReader` remains reachable from its public methods.
- close-answered does not hold. No docs page answers the question, and the 2021 comment is not a docs link.
- The 2021 comment gives a candidate cause (close() followed by use). That is not checked at the pin: no run shows whether the exception appears without a prior close(), and the ticket's original stack came from the removed connect path.
- Missing piece: a run on the pin that uses a client without close() and watches for AlreadyClosedException, or a log from the reporter showing the sequence of calls.

## Verdict justification

keep-open: the named connect path was removed by SOLR-18380, but the class and its closed-state throw remain, and no run settles whether the reported exception can still occur without a prior close(). The earlier close-obsolete verdict is withdrawn because its premise (the class or code removed, no remaining use) is not true at the pin.

Missing for a closing verdict: a run showing the exception cannot occur without close(), with its command and log, which would support close-cannot-reproduce; or a docs page that states the closed-client behavior, which would support close-answered.

## Limits

This record does not establish a reproduction, and it does not check the upstream main commits. It does not post anything to JIRA or GitHub.
