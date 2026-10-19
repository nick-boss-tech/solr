# SOLR-18481: Proxied requests should use the X-Forwarded-For header

## What happens today

When a Solr node forwards a request to another node, `HttpSolrProxy` copies the client's headers and sends them on. It does not record the hop. The class carried a TODO for `X-Forwarded-For`. The upstream node sees the forwarding node's address and nothing about the original client.

## What this change does

For each proxied request, `HttpSolrProxy` now sets one `X-Forwarded-For` header. It is the client's existing hops, if any, followed by the address of the node that forwards the request, joined with `, `. Any `X-Forwarded-For` lines the client sent are replaced by that single value. Requests with no hop to record send no header.

Behavior change, stated openly: a proxied request now carries `X-Forwarded-For` where it had none before. The existing `_forwardedCount` loop guard is unchanged. The ticket also mentions capping forwards and excluding the current node, and this change does not do that.

## Proof

- New `HttpSolrProxyForwardedForTest` (5 tests): no prior hops, one prior hop, two prior hops in order, missing remote address, and nothing to forward.
- The existing `HttpSolrProxyTest` (2 cloud tests) still passes.
- The helper is new, so there is no base-versus-change run: the base has no such method to compare against.

Head 5aedffcccd on 2026-10-19: `HttpSolrProxyForwardedForTest` 5 of 5 and `HttpSolrProxyTest` 2 of 2 pass, 0 failures, 0 skipped. `:solr:core:spotlessJavaCheck` passes. Logs: `/workspace/gates/logs/vm4/solr-18481-gate.log`, `solr-18481-TEST-*.xml`.

## Question for the maintainer

Should the proxy also cap the number of forwards and skip the current node in the `X-Forwarded-For` chain, as the ticket suggests as a better loop guard? That is a separate change to `_forwardedCount`.
