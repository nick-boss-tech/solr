# Jira comment draft: SOLR-18138

Status: DRAFT (not posted), redrafted 2026-10-27 after the code re-read; replaces the HOLD version. Shape: staged ask, evidence first, two options. Addressee: Eric Pugh (reporter). Prepared by vm6 from `reports/solr-18138-cli-zk-unreachable-round-1.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I read the CLI ZooKeeper path again at apache/solr main {{e59ce3c97d}}, and my first reading was wrong. The command in the ticket does not go through {{CLIUtils.getSolrZkClient}}. {{bin/solr zk ls}} builds its own client in [ZkLsTool.java:111-115|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/cli/ZkLsTool.java#L111-L115], and 11 other CLI and package-manager sites build clients the same way. A change in the shared helper would not change your output.

Two places produce what you saw:

 * The connect timeout is raised in [SolrZkClient.java:201-216|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/SolrZkClient.java#L201-L216] as a {{SolrException}} wrapping {{TimeoutException("Timeout while waiting for Zookeeper Client to connect: N ms")}}.
 * Each ZooKeeper tool logs the full exception at ERROR before rethrowing ([ZkLsTool.java:119|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/cli/ZkLsTool.java#L119]). That log line is where the stack trace in the ticket comes from. The shared error handler in [ToolBase.java:157-165|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/cli/ToolBase.java#L157-L165] prints only the message unless {{--verbose}} is set.

So a clearer message needs two changes: the message text, and the per-tool log line. There are two ways to do it:

 1. Handle it once in {{ToolBase}}. Catch the connect timeout in the shared error path, print a message that says ZooKeeper did not answer within the configured timeout and that the server it points at may be down, and skip the per-tool ERROR trace for that case. This covers every ZooKeeper tool in one place. The message cannot name the host, because {{ToolBase}} does not hold it.
 2. Route all 12 direct client builders through the {{CLIUtils}} helper, which takes the host and catches the timeout there, and remove the per-tool trace. The message can name the host. This touches 11 files.

The server-side message ({{KeeperErrorCode = ConnectionLoss for /live_nodes}} from {{bin/solr zk ls -s}}, and {{/aliases.json}} from LISTALIASES in the ticket) is raw ZooKeeper text passed through the admin API. Changing it is a separate change to the server's error handling.

Eric, which option do you want for the CLI message? And should the server-side text be in scope for this ticket, or split out?

Possible states for the ticket: close it after option 1 or 2 lands; keep it open for the server-side text; or split the server-side text into its own ticket. The recommendation is option 1: it is one change in one place, it covers every ZooKeeper command, and the host is already on the command line the user typed.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
