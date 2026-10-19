# Skip record: SOLR-13455

- Ticket: SOLR-13455, JettySolrRunner does not enable HTTP2 unless jetty.testMode is set.
- Verdict: close-fixed. This replaces the earlier keep-open verdict on solr-13455-draft-open (a859602b97).
- Draft branch: solr-13455-draft-close (new branch from origin/coordination; the keep-open draft is left in place).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the queue audit (round audit-3, 2026-10-06) and the earlier record are replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-05-09, updated 2019-10-19, no fix versions, no links. The description: JettySolrRunner adds the HTTP2 connection factory only when the `jetty.testMode` system property is set, which affects embedded Solr and the test framework outside test mode. The ticket cites a Stack Overflow question.

## Code reading

Fixing commit: `890ef78421` (SOLR-15428, "Integrate the OpenJDK JMH micro benchmark framework...", 2021-08-09). Ancestor of the pin. Its diff adds `new HTTP2CServerConnectionFactory(configuration)` to the non-testMode connector in `solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java`.

Changed code present at the pin:

- `JettySolrRunner.java:214`: the `jetty.testMode` branch (the SSL and HTTP/2 variants with ALPN, and the `onlyHttp1` variants) runs only when the property is set.
- `JettySolrRunner.java:281-287` (the `else` for non-testMode): creates a `ServerConnector` with `HttpConnectionFactory` and `HTTP2CServerConnectionFactory(configuration)`. So HTTP/2 (cleartext h2c) is enabled without `jetty.testMode`.

Greps at the pin:

- `git grep -n 'HTTP2CServerConnectionFactory' 498a09aea97aac86c2cc7ac114113b946d249942 -- solr` shows the test-framework uses at `JettySolrRunner.java:270`, `:287`, and the server config `solr/server/etc/jetty-http.xml:35`.

History of the file: the HTTP/2 connector was first added in `f80e8e1167` (2018, the http2 branch merge); the non-testMode connector came with `890ef78421`; `a18f5b3c7c` (SOLR-16158, 2022) moved the file to the test framework without changing the connector.

## Reproduction attempts

Not run. The change is a code path, not a run; no build or test run in this pass.

## Gap analysis

- The ticket asks that HTTP/2 be enabled without `jetty.testMode`. At the pin the non-testMode connector always includes `HTTP2CServerConnectionFactory`, so the reported behavior is absent.
- Scope note, not part of the ticket: the non-testMode branch ignores `config.onlyHttp1` (the testMode branch honors it at :237-243 and :263-265). Whether that matters is a separate question and is not part of this verdict.
- The ticket says HTTP2; the fix adds HTTP/2 over cleartext (h2c) on the same connector, not ALPN-negotiated HTTP/2 over TLS. The non-testMode path has no TLS (`sslcontext` is only set in the testMode branch), so h2c is the only HTTP/2 form available there. The ticket's wording (HTTP2ConnectionFactory) was for the testMode form; the reading here is that the ticket's concern (HTTP/2 available outside testMode) is met.

## Verdict justification

close-fixed: the fixing commit `890ef78421` (SOLR-15428) is named, it is an ancestor of the pin, and the non-testMode connector with the HTTP/2 factory is present at `JettySolrRunner.java:281-287`. The rubric minimum for close-fixed is met.

## Limits

This record does not establish a run with HTTP/2 from an embedded server outside testMode. It does not check whether an external client negotiates HTTP/2 on the h2c connector. It does not post anything to JIRA or GitHub.
