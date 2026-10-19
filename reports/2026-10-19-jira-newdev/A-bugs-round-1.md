# JIRA newdev bugs, round 1 (A)

One section per ticket. Assignment: `assignments/pool-jira-newdev-bugs-round-1.md`.

## SOLR-15231: zk upconfig does not recognize ZK_HOST style url (vm3, 2026-10-19)

- Status: already fixed on apache/main at `c8ab71430f`, by code reading. No branch pushed. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-15231.md`.
- The report is a comma-separated host list with a chroot (`<host>,<host>,<host>/solr`), where the configset went to the first host's root instead of `/solr`.
- Evidence on the base, read from the code:
  - The `-z` value is passed through unchanged: `solr/core/src/java/org/apache/solr/cli/ConnectionOptions.java` lines 117 to 125 return the connection string as given.
  - The chroot is kept by `CloudSolrClient.CloudSolrClientConnection.parseZkQuorum` (`solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java` lines 1765 to 1780): the part from the first `/` becomes `zkChroot`, and `toString` puts it back.
  - `SolrZkClient` splits the connect string at its first `/` (`solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/SolrZkClient.java` lines 139 to 149) and applies the chroot as the ZooKeeper namespace (line 184), so every path resolves under `/solr`.
  - Upload writes to `/configs/<name>` through that client (`solr/core/src/java/org/apache/solr/cli/ConfigSetUploadTool.java` lines 131 to 150).
  - The chroot-aware universal connection string arrived with SOLR-18130 (`bf610531f1`), which is the change that fixes this path.
- Not run: no ZooKeeper reproduction on `c8ab71430f`. A live run would be needed to say the original report cannot happen; this section records a code-reading verdict only.
- Gate: none, since no branch was pushed.
