# SOLR-8614: add SolrClient.setUseXmlFormat

Draft PR description, not opened. Branch `solr-8614-xml-toggle` at `22d9aab9f6`, base `apache/main` at `c8ab71430f`.

## Summary

Adds `SolrClient.setUseXmlFormat(boolean)`. When true, the client writes requests with `XMLRequestWriter` and reads responses with `XMLResponseParser`, so the `wt` parameter is `xml`. When false, it returns to `JavaBinRequestWriter` and `JavaBinResponseParser`, the binary default.

## Choices (ladder rung 4)

- Surface: one public method on `SolrClient`, following the ticket's one-liner. No builder option.
- Base default: `SolrClient.setUseXmlFormat` throws `UnsupportedOperationException` for a subclass that does not override it. A silent no-op would hide a missed client, and this keeps third-party subclasses source compatible.
- HTTP: `HttpSolrClient` sets its `requestWriter` and `parser` fields. `HttpJdkSolrClient` and `HttpJettySolrClient` inherit it.
- Concurrent update clients delegate to their inner `HttpSolrClient`.
- Load balancing: `LBSolrClient` sends every request through one shared `HttpSolrClient` (the builder's template), so the setter updates that client and refreshes the cached `parser` and `requestWriter` copies. `CloudSolrClient` delegates to its load balancer, which covers `CloudHttp2SolrClient`.
- `CollectionScopedSolrClient` delegates to the wrapped client.
- `EmbeddedSolrServer` is a no-op: in-process requests have no wire format.
- Reset: false restores the binary writer and parser defaults. If the builder had configured a custom writer or parser, false replaces it. A caller who needs a custom writer or parser should set it after the toggle.
- Per-request override: `SolrRequest.setResponseParser` still wins over the client setting, as before. There is no per-request request writer, so the ticket's concern about a request-level writer does not arise.
- Threading: the setter writes plain fields on `HttpSolrClient`. Call it before the client is shared between threads.

## Caveats

- `LBSolrClient` and `CloudSolrClient` propagate through delegation. The new test covers `HttpJdkSolrClient` and `HttpJettySolrClient` directly. There is no direct test for load-balanced or cloud clients.
- Only the base XML format switch is covered. Other wire-level differences in XML responses, such as streaming callbacks that rely on `StreamingJavaBinResponseParser`, are unchanged and not tested.
- The change adds public API to an abstract class. Maintainers may trim it.

## Tests

- `HttpSolrClientTestBase.testSetUseXmlFormatTogglesWireFormat`, run from `HttpJdkSolrClientTest` and `HttpJettySolrClientTest`. Checks writer and parser types, the `wt=xml` query parameter, and the return to binary.
- Gate: `/workspace/gates/logs/vm4/solr-8614/test2.log`, XML under `/workspace/gates/logs/vm4/solr-8614/xml/`.

## Changelog

`changelog/unreleased/SOLR-8614-setUseXmlFormat.yml`, type `added`.
