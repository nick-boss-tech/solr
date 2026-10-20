# CSV Blocker and Critical slice 02 (vm4)

Assignment: `assignments/pool-csv-critical-blocker-round-1.md`, slice 02. Claim: `claims/pool-csv-critblock-02.md`.
Basis: JIRA REST `issues.apache.org/jira/rest/api/2/issue/<KEY>` read 2026-10-20 (UTC) for status, links and comments. Upstream reference `c8ab71430f` (apache/main). The local clone is shallow; upstream history is checked through the GitHub API (`repos/apache/solr/compare`, `commits?path=`, `search/commits`).

Status: PARTIAL. Rows marked PENDING have not been classified yet. The claim stays open until every row has a class and evidence.

| Ticket | Summary | Class | Evidence | Next step |
| --- | --- | --- | --- | --- |
| SOLR-8767 | RealTimeGetComponent and stored/copyField exclusion | PENDING | Open, updated 2018. Link: SOLR-3743 (Closed) | Read SOLR-3743; check the RTG field list at base |
| SOLR-8887 | Security features export deprecated DefaultHttpClient | OBSOLETE | No reference to `DefaultHttpClient` in `solr/core/src/java` or `solr/solrj/src/java` at base. Reporter comment (2016-11-21) suggests closing as a duplicate of SOLR-4509 (Resolved) | Owner note: close as duplicate of SOLR-4509 |
| SOLR-8905 | AutoAddReplicas deletes local core.properties | PENDING | Open, no links | Read the AutoAddReplicas delete path at base |
| SOLR-9138 | CDATA should be required for text fields in XML output | PENDING | Open, no links | Check the XML response writer for CDATA handling |
| SOLR-9170 | Detailed diagnostics for ZooKeeper connection issues | STILL-PRESENT (feature) | Open diagnostics request, no links. It asks for new logging or output, not a defect | Owner scoping: a feature request, not a slice-sized fix |
| SOLR-9956 | ArrayIndexOutOfBounds when indexing a large amount | PENDING | Open, updated 2020, no links | Read the stack in the ticket and the matching base code |
| SOLR-10928 | Support elevate.q in QueryElevationComponent | STILL-PRESENT (feature) | No `elevate.q` handling in `QueryElevationComponent.java` at base (grep empty) | Owner scoping: new parameter, not a slice-sized fix |
| SOLR-10950 | Context filtering for FuzzyLookupFactory | STILL-PRESENT (feature) | No `context` handling in `FuzzyLookupFactory.java` at base (grep empty) | Owner scoping: new suggester feature |
| SOLR-11310 | QueryElevation does not work with Solr LTR | PENDING | Open, updated 2020, no links | Reproduce the LTR plus elevation ordering |
| SOLR-11330 | Solr arabic search | PENDING | Open, updated 2019, no links, no detail | Read the ticket text; likely no repro without the reporter's data |
| SOLR-11650 | BasicAuth credentials shown in clear text on slave nodes | PENDING | Open, updated 2019, no links | Check the replication config path at base |
| SOLR-11668 | Lookup not supported or StackOverflowError after upgrade from 6.3.0 | PENDING | Open, updated 2019, no links | Read the stack; check the lookup path at base |
| SOLR-11972 | Range of numeric values with units | STILL-PRESENT (feature) | Open feature request, no links | Owner scoping: new query syntax |
| SOLR-12069 | Default operator parameter q.op ignored | PENDING | Open. `QueryParsing.java` line 45 defines `q.op` and `ExtendedDismaxQParser` respects it (lines 440, 1010). Which parser ignores it is not in the ticket | Find the parser path the ticket uses and reproduce |
| SOLR-12315 | Number of docs per group depends on rows | PENDING | Open, updated 2019, no links | Reproduce grouping with rows and group.limit |
| SOLR-12415 | LBHttpSolrClient does not detect a node that comes back | PENDING | Open, updated 2019, no links | Check LBSolrClient zombie recovery at base |
| SOLR-12743 | Memory leak introduced in Solr 7.3.0 | NOT-REPRODUCIBLE | Two comments: 2020-01-05 asks whether it is fixed, 2020-01-07 the reporter cannot confirm (cluster still on 7.5). No heap dump or repro | Owner note: close as no longer reproducible without data |
| SOLR-12801 | Fix the tests, remove BadApples and AwaitsFix | STILL-PRESENT (umbrella) | Open umbrella test cleanup. Linked LUCENE-8545 and LUCENE-8547 are Open. Comment (2018) says tlog replica tests are off until SOLR-12313 is fixed | Owner scoping: test cleanup, not a defect |
| SOLR-12833 | Use timed-out lock in DistributedUpdateProcessor | PENDING | Status Reopened, resolution Fixed, fix versions 7.7 to 8.1. Base `DistributedUpdateProcessor.java` uses `ulog.getLocks()` (line 206) | Read the reopen reason and check the lock path at base |
| SOLR-13360 | StringIndexOutOfBoundsException: index -3 | PENDING | Open. Link SOLR-16308 (Resolved, duplicate, no fix). Reporter asks in 2026-06 for a fix | Reproduce from the ticket stack or mark as duplicate chain |
| SOLR-13460 | Exception writing document id (sitecore URL) | PENDING | Open, updated 2019, no links | Read the stack; reproduce the document id path |
| SOLR-13770 | Replicas do not recover after restart (nested TLOG dirs) | PENDING | Open, updated 2020, no links | Read the recovery directory path at base |
| SOLR-13789 | Core reload with config change puts instance in bad state | PENDING | Patch Available (a PR is linked in JIRA, status not checked here) | Check the PR and whether it is merged |
| SOLR-13850 | Atomic updates with PreAnalyzedField | PENDING | Open, updated 2020, no links | Reproduce atomic update on a PreAnalyzedField |
| SOLR-14200 | Shard tolerance in the Streaming API | PENDING | Patch Available (PR in JIRA, status not checked here) | Check the PR and whether it is merged |
