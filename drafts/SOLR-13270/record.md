# Skip record: SOLR-13270

- Ticket: SOLR-13270, SolrJ does not send "Expect: 100-continue" header.
- Verdict: close-obsolete (scoped to the Apache HttpClient client the ticket names).
- Draft branch: solr-13270-draft-close. The verdict changed from keep-open, so this branch is new; the open draft (solr-13270-draft-open) is left in place for the earlier verdict and is not supported.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 2 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-02-25, updated 2020-02-10, no fix versions. Links: "Reference SOLR-14249 (Resolved)". The report: SolrJ sets `setExpectContinueEnabled(true)` on its HttpClient request config, but the "Expect: 100-continue" header is not sent. Thirteen comments (2019-02-25 to 2019-05-21). The discussion places the override in `HttpSolrClient.executeMethod`, where SolrJ replaces the request config the user set. A patch was proposed (2019-04-08) and not committed; the reporter could not confirm it (2019-04-12, 2019-05-21).

## Code reading

Read at the pin:

- The HttpComponents request config is absent from the tree: `git grep -n -i 'RequestConfig\|100-continue\|expectcontinue' 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java'` returns no lines.
- The Apache HttpClient client was moved out of main by SOLR-17962 (cf7f5079e8, "Move deprecated SolrClient impls (Apache HttpClient) to test-framework", an ancestor of the pin, checked with `git merge-base --is-ancestor`). SOLR-18188 (`changelog/unreleased/SOLR-18188-testsStopUsingApacheHttpClient.yml`) records tests moving off it.
- `solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java:69` is now `public abstract class HttpSolrClient extends SolrClient`, so no concrete class in main builds on the Apache request config.
- The clients that ship now are `HttpJdkSolrClient.java` (JDK HTTP client) and the Jetty-based `Http2SolrClient` / `CloudHttp2SolrClient.java`. The JDK client sets Content-Type and User-Agent headers (`HttpJdkSolrClient.java:300, :452, :479`). No header named Expect is set in `solr/solrj/src/java` or `solr/core/src/java` (the "Expect" hits in those trees are natural-language messages such as "Expected ZooKeeper connection string").

## History

- `cf7f5079e8` (SOLR-17962, moved the Apache HttpClient client out of main): ancestor of the pin.
- The ticket cites SOLR-14249 (Resolved). No commit in the pinned history names SOLR-14249 in its subject (`git log --grep=SOLR-14249`, case-insensitive, returns nothing that names it).

## Reproduction attempts

Not run. This pass ran no builds or tests. The close-obsolete basis is the grep and the removal commit, not a run.

## Gap analysis

- The code the ticket is about (the Apache HttpClient request config and its override in `executeMethod`) no longer exists in main; the grep is empty and the client was moved out by SOLR-17962.
- Scope: this verdict covers the Apache HttpClient client. It does not say that a Jetty-based or JDK-based SolrJ client should send `Expect: 100-continue`. Whether those clients should send it is a new request, not this ticket, and would need its own record.

## Verdict justification

close-obsolete: the feature the ticket names (SolrJ's Apache HttpClient request configuration, including the Expect setting) was removed from main by SOLR-17962, and a search of the Java sources at the pin finds no remaining use (grep above, zero lines).

Missing for this verdict: the grep result is taken in this pass and stated here; no run.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
