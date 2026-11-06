# SOLR-18628: idle h2c streamed update reproduction and fix (round 1)

Status: OPEN

Intended host: Windows source implementation host; Linux pool host for the required gate.

Capability tags: implementation, review, records. Gate runner must have the Linux gate capability, JDK 21 for Gradle, and JDK 25 for the SolrJ test runtime.

## Assignment

Investigate the reported failure where a streamed `HttpJdkSolrClient` update works over cleartext HTTP/2, the connection is idle long enough to expire, and the next streamed update fails with `RST_STREAM` / `invalid_preface`.

Ticket: https://issues.apache.org/jira/browse/SOLR-18628
Mailing-list report: https://www.mail-archive.com/users%40solr.apache.org/msg09633.html
Standalone reproducer: https://github.com/micopiira/solrj-h2c-idle-reproducer

Treat the cached `maybeTryHeadRequest` result as a hypothesis until a fail-before test confirms the path. Read current code and history, add a focused SolrJ regression test using the JDK keepalive-timeout property to shorten the idle interval, and retain an HTTP/1.1 control. Only make a production change if the regression reproduces. Assess the extra request cost if the fix re-probes HEAD for streamed cleartext requests; record any protocol or performance limitation.

## Deliverables

- A claim before source work, naming the host, capability tags, exact source base, scope, and decision-ladder rung.
- A ticket-keyed fork branch `solr-18628-submit` containing the narrow regression test and any justified fix, with a changelog fragment if production behavior changes.
- An exact-head Linux gate job covering fail-before and pass-after on JDK 25, the HTTP/1.1 control, formatting, the SolrJ test, and root `check -x test`.
- A report with the code paths and history read, reproduction result, test result and limitations.

Windows must not run Gradle or the local test queue. Keep the ticket Implemented, Proof Pending until the exact pushed head has a successful Linux receipt. Do not open a PR or post to Jira beyond this approved issue creation.
