# Jira comment draft: SOLR-18138

Status: DRAFT (not posted), redrafted 2026-10-27 to follow comment-formula.md (evidence first, one question, possible states, recommendation, footer). Addressee: [~epugh] (reporter). Prepared by vm6 from `reports/solr-18138-cli-zk-unreachable-round-1.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

[~epugh] The command in the ticket does not go through {{CLIUtils.getSolrZkClient}}. {{bin/solr zk ls}} builds its own client in [ZkLsTool.java:111-115|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/cli/ZkLsTool.java#L111-L115], and 11 other CLI and package-manager sites build clients the same way. A change in the shared helper would not change your output.

Two places produce what you saw:

 * The connect timeout is raised in [SolrZkClient.java:201-216|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/SolrZkClient.java#L201-L216] as a {{SolrException}} wrapping {{TimeoutException("Timeout while waiting for Zookeeper Client to connect: N ms")}}.
 * Each ZooKeeper tool logs the full exception at ERROR before rethrowing ([ZkLsTool.java:119|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/cli/ZkLsTool.java#L119]). That log line is where the stack trace in the ticket comes from. The shared error handler in [ToolBase.java:157-165|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/cli/ToolBase.java#L157-L165] prints only the message unless {{--verbose}} is set.

A clearer message needs a change to the message text and to the per-tool log line.

Is a clearer CLI message what you want for this ticket?

Possible states for the ticket:

 * Close after a CLI message fix that covers every ZooKeeper tool. There are two ways to build it:
 ## Handle it once in {{ToolBase}}. Catch the connect timeout in the shared error path, print a message that says ZooKeeper did not answer within the configured timeout and that the server it points at may be down, and skip the per-tool ERROR trace for that case. The message cannot name the host, because {{ToolBase}} does not hold it.
 ## Route all 12 direct client builders through the {{CLIUtils}} helper, which takes the host and catches the timeout there, and remove the per-tool trace. The message can name the host. This touches 11 files.
 * Keep open for the server-side text. The {{KeeperErrorCode = ConnectionLoss for /live_nodes}} from {{bin/solr zk ls -s}} and the {{/aliases.json}} error from LISTALIASES are raw ZooKeeper text passed through the admin API. Changing them is a separate change to the server's error handling.
 * Split the server-side text into its own ticket.

Recommendation: close after option 1. It is one change in one place, it covers every ZooKeeper command, and the host is already on the command line the user typed.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
