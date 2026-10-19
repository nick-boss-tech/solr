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

## SOLR-16193: suggester not built, but API returns success (vm3, 2026-10-19)

- Status: not reproduced on apache/main at `c8ab71430f`. No branch pushed. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-16193.md`.
- The ticket expects a non-200 when the suggester build fails. The reporter's log shows `IllegalStateException: suggester was not built` while `suggest.build=true&suggest=true` returns 200.
- Code reading: `SolrSuggester.build` (`solr/core/src/java/org/apache/solr/spelling/suggest/SolrSuggester.java` line 170) rethrows only `AlreadyClosedException`. `SuggestComponent.process` (`solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java` around line 287) has no catch around `getSuggestions`. The `not built` text comes from Lucene's lookup.
- Probe: a temporary test (not committed) indexed one document, deleted all documents, committed, then ran `qt=/suggest&suggest.dictionary=suggest_fuzzy_doc_dict&suggest.build=true&suggest.q=exampel`. It passed with `numFound=0` and no error in the log (`/workspace/gates/logs/vm3/solr-16193-repro/focused.log`, seed `16193E50`).
- Not covered: the reporter's own handler and dictionary config (for example a build-on-commit or stored-dictionary setup). That path would need the reporter's configuration to reproduce, so this is recorded as not reproduced, not as fixed.
- Gate: none, since no branch was pushed.

## SOLR-16220: Missing SslContextFactory NPE for SolrJ https client (vm3, 2026-10-19)

- Status: not reproduced for the documented case on apache/main at `c8ab71430f`. No branch pushed. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-16220.md`.
- Code reading: `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java` lines 229 to 233 use `getDefaultSslContextFactory()` when no `SSLConfig` is set, so a client built with an `https` URL and no SSL config gets a factory.
- Probe (temporary test, not committed): `new HttpJettySolrClient.Builder("https://127.0.0.1:1/solr").build()` then a query. The output shows the request reached the socket connect step (`SolrServerException: access denied ("java.net.SocketPermission" ...)`), with no `Missing SslContextFactory` NPE. The test passed (`/workspace/gates/logs/vm3/solr-16220-repro/`, seed `16220E50`). The sandbox's socket check stopped the connect, so this shows the destination was built, not that a live TLS handshake succeeds.
- Variant not covered: `SSLConfig.createClientContextFactory()` returns null when the config is not in SSL mode (`solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/SSLConfig.java` line 144). A client built with a non-SSL `SSLConfig` and an `https` URL would pass a null factory to Jetty. That is the path that matches the ticket's NPE, and it needs an owner decision on the intended behavior (fall back to the default factory, or fail with a clear message). Not fixed here.
- Gate: none, since no branch was pushed.
