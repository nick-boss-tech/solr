# SOLR-18138 CLI when ZooKeeper is unreachable: code read

Ticket: SOLR-18138, "Handle in CLI when Solr hosting ZK is not reachable" (Open, Minor, reporter Eric Pugh, created 2026-02-26, no comments). Reproduced in the ticket: with the node hosting embedded ZooKeeper stopped, `bin/solr zk ls -z 127.0.0.1:9983 /` prints a 15-second timeout and `bin/solr zk ls -s http://localhost:9004 /` prints `KeeperErrorCode = ConnectionLoss for /live_nodes` from the server. The ticket asks for a clearer CLI message.
Pinned main: `e59ce3c97ddb`. Not run: this is a code read. The reporter's output is the reproduction.

## What the code does

1. The CLI ZK client is built in one place: `solr/core/src/java/org/apache/solr/cli/CLIUtils.java:395-406` (`getSolrZkClient`), with a fixed `SolrZkClientTimeout.DEFAULT_ZK_CLIENT_TIMEOUT` (`:404`).
2. The connect timeout is raised in `solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/SolrZkClient.java:201-216`. It throws `TimeoutException("Timeout while waiting for Zookeeper Client to connect: N ms")` and wraps it: `throw new SolrException(SERVER_ERROR, e)` (`:215`).
3. The top-level CLI error path prints only the message: `ToolBase.java:112-120` and `:157-165` print `ERROR: <getMessage()>` and a stack trace only with `--verbose`. The wrapped message is therefore `java.util.concurrent.TimeoutException: Timeout while waiting for Zookeeper Client to connect: 15000 ms`, which matches the ticket's output.
4. The ZK tools also log the full exception before rethrowing, for example `ZkLsTool.java:89` ("Could not complete ls operation for reason:"), which produces the stack trace in the ticket. Same pattern in `ZkCpTool.java:260`, `ConfigSetUploadTool.java:126`, `ZkRmTool.java:94`, and others.
5. The HTTP path (`bin/solr zk ls -s`) is server side. The ConnectionLoss text is the raw ZooKeeper message passed through the admin API. The CLI prints it as-is. Changing it means changing the server's error handling, which is a wider change than this ticket's title.

## Proposed change, not made

- CLI-only: in `CLIUtils.getSolrZkClient`, catch the connect failure and rethrow with a message that names the ZooKeeper host and the timeout, and says that the node hosting embedded ZooKeeper may be stopped. One change covers the six call sites (`git grep -n getSolrZkClient e59ce3c97d -- solr/core/src/java/org/apache/solr/cli/`: six callers plus the definition at CLIUtils.java:395).
- Server side (not proposed here): the ConnectionLoss text. That needs its own ticket or a decision from Eric.

## Verdict

Superseded by the correction below: the first plan (one change in `getSolrZkClient`) does not reach the reproduced command. No code change is made in this unit.

## Not checked

- Whether `bin/solr` (the shell script) prints its own message before calling the CLI.
- The shape of each of the six call sites (only the count was taken).

## Correction 2026-10-27: the proposed change does not reach the reproduced command

Checked at `e59ce3c97d` after the first read. The earlier "one change in `getSolrZkClient`" plan is not sufficient.

1. The reproduced command does not use `getSolrZkClient`. `bin/solr zk ls` is registered at `SolrCLI.java:368` to `ZkLsTool`, whose `callTool()` builds its own client directly at `ZkLsTool.java:111-115` (`new SolrZkClient.Builder()`). The ticket's first error comes from that line.
2. `getSolrZkClient` has six callers, but there are 12 direct `new SolrZkClient.Builder()` sites besides the `CLIUtils` helper: 11 in `solr/core/src/java/org/apache/solr/cli/` and `cli/packagemanager/PackageManager.java:92`, including `ZkCpTool.java:251`, `ClusterTool.java:136`, `AuthTool.java:418`, `UpdateACLTool.java:93,115`, `ConfigSetDownloadTool.java:154`, `ConfigSetUploadTool.java:159`, and `ZkRmTool.java:136`. Any connect timeout from these sites bypasses the proposed catch.
3. The stack trace in the ticket is not from `ToolBase`. Every ZK tool catches and logs the full exception at ERROR before rethrowing (`ZkLsTool.java:89` and `:119`, `ZkCpTool.java:260`, `ZkRmTool.java:94`, `ZkMvTool.java:117`, `ZkMkrootTool.java:128`, `ConfigSetUploadTool.java:126`, `ConfigSetDownloadTool.java:123`). `ToolBase.call()` (`ToolBase.java:157-165`) prints only the message unless `--verbose`. A change that only rewrites the message would still leave the trace printed by these logs.
4. `ToolBase` is the one place every tool's error passes through (`runTool` and `call`). A timeout check there would cover all 12 sites, but `ToolBase` does not hold the ZooKeeper host string, so the host name would have to come from the tool.
5. The ticket also asks about the server error on LISTALIASES (`KeeperErrorCode = ConnectionLoss for /aliases.json`). The draft does not address that question.

Verdict: the plan is incomplete. Options are (a) catch the connect timeout in `ToolBase` and stop the per-tool ERROR trace for that case, which covers all sites with a generic message; or (b) route all 12 direct builders through one helper in `CLIUtils` that names the host, then fix the logging. The draft to Eric must be rewritten after one of these is chosen. It is on HOLD and not posted.
