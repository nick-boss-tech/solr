# Skip record: SOLR-16084

- Ticket: SOLR-16084, "When Solr server runs with HTTPS java clients can't connect but curl can". JIRA status Open, resolution none, fix versions none, created 2022-03-07, updated 2022-05-12. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-16084-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-16084-vm2.md).

## Ticket reading

The reporter runs Solr 8.11.1 with HTTPS. The Http2SolrClient fails with "IOException occured when talking to server" and a java.io.IOException: Broken pipe from Jetty's SslConnection flush. curl and Apache HttpClient work against the same server. The report has no comments and names no test class. Its stack trace is from SolrJ 8.11.1.

## Code reading

- The class named in the report, Http2SolrClient, is not in origin/main. It was renamed to HttpJettySolrClient in the jetty package by commit 2c55000296b (SOLR-18005, "SolrJ: consolidate Jetty-using classes to a jetty package"). The file is solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java.
- HttpJettySolrClient.java:224-260 (origin/main 498a09aea97): the SSL context factory comes from builder.sslConfig or the default. The client connector is a Jetty 12 ClientConnector (line 259-261) with that factory set on it. This is a different connector from the 8.11.1 client in the report, so the report's Jetty stack does not map to this code.
- HttpJettySolrClient.java:485 and :513: the error message "IOException occurred when talking to server at: <url>" (the report's wording, spelled with one u, comes from the 8.11.1 release).

Commands at the pinned head:
- git grep -n 'occured when talking\|occurred when talking' origin/main: no match for the 8.11.1 spelling; the current message is at HttpJettySolrClient.java:485.
- git log --format='%h %ad %s' -S 'class Http2SolrClient' origin/main: 2c55000296b (rename), 21740acc339 (proxy support), f80e8e11672 (original).

## History

- Commits on origin/main naming SOLR-16084: none.
- 21740acc339 (SOLR-16859, 2023-08-16): proxy support for the old Http2SolrClient, an ancestor of the pinned head.
- 2c55000296b (SOLR-18005, 2025-11-30): rename and move; ancestor of the pinned head.

## Reproduction attempts

Not attempted. A reproduction needs a Solr server with HTTPS and the current client sending requests to it. That is a SolrJ test with SSL randomization, which was not set up in this pass. The 8.11.1 client in the report is not in this tree, so the report's exact failure cannot be run here.

## Gap analysis

The client changed between the report's release and the pinned head (the rename, the move to Jetty 12 ClientConnector, and the proxy support). Whether the broken pipe still happens with the current client is not established. The report alone cannot say whether it is still a bug in this code, or was fixed by the Jetty upgrade.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. The missing piece is a reproduction against the current client with an HTTPS server, to check whether the report applies to this code. A close verdict needs that run or a documented fix; a pr-needed verdict needs the failing cause in the current code.

## Limits

This record does not reproduce the broken pipe. It does not test HTTPS on the pinned head. It does not establish whether the Jetty 12 upgrade fixed the report.
