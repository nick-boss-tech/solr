# Review round 1, unit 19: SOLR-17433 with SOLR-17143 (branch solr-17433-17143-submit), vm3

## Pass 1 (vm3, 2026-10-21T05:24:51Z)

- Head reviewed: `42b6c4fc9158524ceb6b91aa2f4c6583a387c6f9`. Base `14c7aac0d1`. Diff: `solr/solrj-streaming/.../SolrClientCache.java`, its test, and one line in `streaming-expressions.adoc`. No changelog in the branch.
- Ticket SOLR-17433 (Open, Critical): `SolrStream` creates a client from `SolrClientCache` that times out after 60 seconds; the reporter expects no timeout while a stream still returns content, and notes the 60 second floor conflicts with the 600 second default of the manual clients.
- What the branch does: the 60 second floors on connect and socket timeouts stay (now per instance, configurable through two new constructors); the default constructor still reads the system properties; every cached SolrJ client gets `withRequestTimeout(Long.MAX_VALUE)`. The idle timeout is still `max(minSocketTimeout, idle)`.
- Surrounding code check: the static fields `minConnTimeout` and `minSocketTimeout` became instance fields. A tree-wide grep finds no other static users. The one subclass, `InternalSolrClientCache` (core, server side), calls `super()` and reads `minSocketTimeout` after it, so it gets the same value as before. The base cache refuses to run inside Solr (`checkNotRunningInSolr`), so the unbounded request timeout does not reach the server-side cache.
- Finding, major (owner decision, not repaired): the ticket asks for no timeout while a stream keeps returning content. The branch removes the request timeout for every cached SolrJ client, including clients that users create for non-streaming calls through the cache. That is wider than the stream case and is a timeout policy choice, not a mechanical fix. Recorded for the owner.
- Finding, minor: the ticket's stated expectation of 600 seconds (matching `HttpClientUtil.DEFAULT_SO_TIMEOUT`) is not what the branch uses; the branch keeps 60 seconds for the socket floor and makes the request timeout unbounded. The two choices should be stated together in the owner's decision.
- Proof: none discriminating. The receipt says the new constructors do not exist on base, so no base run can show the fail-before. Recorded plainly.
- Repairs in pass 1: none.
- Regate: `gates/SOLR-17433-review-pass-1.md`, GREEN at `42b6c4fc9158524ceb6b91aa2f4c6583a387c6f9`. Logs `/workspace/gates/logs/vm3/solr-17433-review-pass-1/`. Changelog rc 0; tidy rc 0, empty status; Error Prone compile rc 0 (compileJava and compileTestJava executed, no skip warning); `SolrClientCacheTest`: 1 class, 4 tests, 0 failures, 0 errors, 0 skipped (fresh XML); `:solr:solrj-streaming:check -x test` rc 0.

Pass 1 result: GREEN at `42b6c4fc9158524ceb6b91aa2f4c6583a387c6f9`, with the owner decision open.
