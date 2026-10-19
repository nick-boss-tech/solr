# Skip record: SOLR-13305

- Ticket: SOLR-13305, ZkClientClusterStateProvider Zookeeper timeout should be configurable.
- Verdict: keep-open.
- Draft branch: solr-13305-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the queue audit (round audit-3, 2026-10-06, confirmed-skip) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-03-07, updated 2019-06-08, no fix versions, no links. The report: the ZooKeeper connect timeout is hardcoded to 10000 ms and should be configurable, with the mailing-list request to set it through SOLR_WAIT_FOR_ZK. The only comment (krisden, 2019-03-07) asks whether the reporter will provide a patch.

## Code reading

Current state at the pin:

- `solr/solrj/src/java/org/apache/solr/client/solrj/impl/SolrZkClientTimeout.java:27`: the default connect timeout is `EnvUtils.getPropertyAsInteger("solr.zookeeper.client.connect.timeout", 15000)`. The property name is the one the audit cites. The line was moved to this form by `074392ae78` (SOLR-17864, 2025-09-11), an ancestor of the pin, from `Integer.getInteger("zkConnectTimeout", 15000)`.
- `solr/solrj-zookeeper/src/java/org/apache/solr/client/solrj/impl/ZkClientClusterStateProvider.java:253` (`getZkConnectTimeout`) and `:259` (`setZkConnectTimeout`): the setter the audit cites exists.
- `solr/bin/solr` and `solr/bin/solr.cmd` at the pin: `ZK_CLIENT_TIMEOUT` (default 30000) is passed as `-Dsolr.zookeeper.client.timeout` (`solr/bin/solr:1103-1104`, `solr.cmd:952-954`). There is no environment variable for the connect timeout. `SOLR_WAIT_FOR_ZK` and `SOLR_ZOOKEEPER_CLIENT_CONNECT_TIMEOUT` are not in `solr/bin` (grep at the pin, both names, empty).

History relevant to the premise:

- At the ticket's creation date, the tree at `20de3d2ee0` (2019-03-07, the last commit before that date) already had `int zkConnectTimeout = 15000;` in `solr/solrj/src/java/org/apache/solr/client/solrj/impl/ZkClientClusterStateProvider.java:46` and `BaseCloudSolrClient.setZkConnectTimeout` at `BaseCloudSolrClient.java:310-311`. So the "hardcoded 10000 ms" in the ticket did not match the code on the day it was filed.
- The string `setZkConnectTimeout` first appears in `b55f338757` (2010-10-13, SOLR-1873), per a pickaxe search. Ancestor of the pin.

## Reproduction attempts

Not run. No build or test run in this pass.

## History

- `b55f338757` (SOLR-1873, 2010): first appearance of the connect-timeout setter (pickaxe).
- `074392ae78` (SOLR-17864, 2025): the system property name in use now. Ancestor of the pin.
- No commit at the pin names SOLR-13305 (evidence tool: none).

## Gap analysis

- The configurability the ticket asks for exists: by setter in the client (since 2010 per the pickaxe) and by system property (current name `solr.zookeeper.client.connect.timeout`).
- The ticket's named mechanism, the `SOLR_WAIT_FOR_ZK` environment variable, does not exist at the pin, and no bin-script variable sets the connect timeout. So the requested mechanism is not present, even though the timeout is configurable by other routes.
- Whether to close the ticket as already configurable, or to keep it open for an environment variable in the scripts, is a maintainer decision. The rubric does not have a close verdict for an ask that was met by a different mechanism than the ticket named.

## Verdict justification

keep-open: the timeout is configurable in the code at the pin, but the ticket names an environment variable that does not exist, and whether a script variable is wanted is a decision for the maintainers. Close-fixed would need a fixing commit for the named mechanism (none exists), and close-obsolete does not fit (the setter and property are present).

Missing for a closing verdict: a maintainer ruling that the setter and system property satisfy the ticket, or a request for the script variable as a new change.

## Limits

This record does not run a configured connect timeout. It does not check the upstream commits cited in the audit. It does not post anything to JIRA or GitHub.
