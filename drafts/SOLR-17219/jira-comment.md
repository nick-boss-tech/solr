We checked this against Solr main at commit 498a09aea9. We have not reproduced the failure, so the ticket stays open.

What we checked:

- The error text in the ticket is raised when a configset file cannot be read from ZooKeeper. The read is retried only when the ZooKeeper session has expired. Any other ZooKeeper failure during the read is reported at once, without a retry. That matches the reported behavior when many cores recover at the same time.
- We did not find the change in 9.6.1 that the reporters say fixes it, so we cannot point to a fixing commit.

What this means: a retry for other ZooKeeper errors during a configset read may be the fix, but the failing ZooKeeper error has not been confirmed.

If you still see this: please attach the full stack trace, including the nested cause after "Error opening", and the ZooKeeper log for the same minute. The nested cause tells us which ZooKeeper error to retry.
