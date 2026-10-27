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

- CLI-only: in `CLIUtils.getSolrZkClient`, catch the connect failure and rethrow with a message that names the ZooKeeper host and the timeout, and says that the node hosting embedded ZooKeeper may be stopped. One change covers the seven call sites (grep count of `getSolrZkClient(`, `solr/core/src/java/org/apache/solr/cli/`).
- Server side (not proposed here): the ConnectionLoss text. That needs its own ticket or a decision from Eric.

## Verdict

Concrete. The CLI change is small and local, and the ticket asks for it. No code change is made in this unit. The draft proposes the CLI change and asks whether the server-side message is wanted too.

## Not checked

- Whether `bin/solr` (the shell script) prints its own message before calling the CLI.
- The shape of each of the seven call sites (only the count was taken).
