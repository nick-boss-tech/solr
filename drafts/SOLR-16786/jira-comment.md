We checked Solr main at commit 498a09aea9 (October 7, 2026). The part of Solr that handles a lost ZooKeeper session and reconnects a node is still there, and it re-registers the node after a session expires. We could not tell from the report whether the node's session expired or stayed connected, and this version is much newer than the 8.6 build in the report, so the code here does not show what happened on 8.6.

What we checked:

- The ZooKeeper reconnect and session-expiry handling in ZkController, and how it re-registers cores after a session is lost.

How we tried to reproduce it:

- We did not run a reproduction for this check. Reproducing it needs a ZooKeeper ensemble with network interruptions and a Solr node that stays out of live nodes.

What this means:

- The report does not show which part failed. The answer depends on the Solr node log and the ZooKeeper server log for the failure window.

If you still see this:

- Please send the Solr version and the Solr node log and ZooKeeper server log for one failure window, including the session expiry and reconnect messages. With those, we can check whether the newer code behaves differently.
