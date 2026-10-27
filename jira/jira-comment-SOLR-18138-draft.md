# Jira comment draft: SOLR-18138

Status: DRAFT (not posted). Shape: staged ask, evidence first. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `report-18138.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I read the CLI ZooKeeper path at apache/solr main {{e59ce3c97d}}. The message you saw comes from a single place:

 * {{CLIUtils.getSolrZkClient}} ([CLIUtils.java:395-406|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/cli/CLIUtils.java#L395-L406]) builds the client with the default timeout.
 * {{SolrZkClient}} wraps the connect timeout as {{SolrException(SERVER_ERROR, TimeoutException("Timeout while waiting for Zookeeper Client to connect: N ms"))}} ([SolrZkClient.java:201-216|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/SolrZkClient.java#L201-L216]).
 * {{ToolBase}} prints only that message ([ToolBase.java:112-120|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/cli/ToolBase.java#L112-L120]), and the stack trace only with {{--verbose}}.

So the CLI change is local: one catch in {{getSolrZkClient}} that names the ZooKeeper host and says the node hosting embedded ZooKeeper may be stopped. That covers the seven callers.

The server-side text ({{KeeperErrorCode = ConnectionLoss for /live_nodes}}) is the raw ZooKeeper message passed through the admin API. Changing it is a separate change.

Eric, is the CLI message what you wanted for this ticket, or should the server-side ConnectionLoss text be in scope too?

Possible states: close with the CLI message only; keep open for the server-side text; or split the server-side text into its own ticket. The recommendation is the first: it is the part the title asks for, and the server text needs a decision about the admin API.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
