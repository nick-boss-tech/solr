# solr-10322-submit

- Branch: origin/solr-10322-submit
- Head: 80ce9d7a3c8a
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 3 commits. `solr/solrj-streaming/.../io/stream/TopicStream.java` (+7/-3), `StreamExpressionTest.java` (+24 test), changelog fragment `SOLR-10322-topic-stream-client-cache.yml` (+8)
- Verdict: Nearly (for the NPE scope; the ticket-scope question is an owner call below)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Bulk review (round 28, snapshot `0523a10639d`): Needs work, two findings.
- Delta since the snapshot: `git diff 0523a10639d 80ce9d7a3c8a` touches only the changelog title. The code is unchanged. The title now says the fix is the NPE when the stream context has no client cache, and no longer implies an auth fix.
- Bulk finding 1 (test does not cover BasicAuth) still holds. The changelog narrowing removes the overclaim but not the gap. See finding 2 below.
- Bulk finding 2 (fallback cache may have no credentials) is confirmed at this head. See finding 1 below.

## Findings (ranked)

1. **MEDIUM, verified.** The diff changes only the no-cache path. In `TopicStream.constructStreams` (around line 541) and `getPersistedCheckpoints` (around line 486), the stream now uses its own `clientCache` when the context cache is null. The context-supplied path is unchanged. The server `StreamHandler` always supplies a context cache: `solr/core/.../handler/StreamHandler.java:233` sets `context.setSolrClientCache(solrClientCache)`, and that cache comes from `ZkController.getSolrClientCache()` (`ZkController.java:685-698`). The JIRA (SOLR-10322) describes a daemon failing under BasicAuth on the server, which is the server path. This diff does not touch that path. Hypothesis: the reported symptom is not fixed by this branch. The server-side cache's credentials were not read (`InternalSolrClientCache` not inspected).

2. **MEDIUM, verified in code, outcome hypothesis.** On the no-cache path the fallback is a bare `SolrClientCache`. `basicAuthCredentials` defaults to `null` (`SolrClientCache.java:46`), is set only by `setBasicAuthCredentials` (line 79-80), and `getHttpSolrClient` only applies it when set (line 150). No code on this branch calls `setBasicAuthCredentials`. So after the fix, checkpoint and replica reads on a BasicAuth cluster run unauthenticated. The expected result is an HTTP 401 surfaced in place of the NPE. That outcome was not run.

3. **LOW, verified.** The new test `testTopicStreamContextWithoutClientCache` (`StreamExpressionTest.java`, around line 2640) covers only the NPE path. There is no BasicAuth or auth-enabled assertion. The id `1000001` differs from the `1000000` used by `testTopicStream`, so the two tests do not share checkpoint state. Hypothesis: the test fails on `upstream/main` with an NPE from `getPersistedCheckpoints`, since the checkpoint collection in the test (`collection1`) has active replicas. Not run.

4. **LOW, verified.** The changelog fragment parses as a YAML block with `type: fixed`, `authors: - name: Nick Shanin`, and a `SOLR-10322` link. The author is the ICLA name. Format was not checked against the changelog tooling (Linux gate).

5. **Positive, verified.** Ownership is correct. `SolrStream.open()` sets `doCloseCache = false` when given a cache (`SolrStream.java`, `open`). `TopicStream` keeps `doCloseCache` for its own cache. So the shared cache is closed once, by its owner, with no double close.

## Owner call (not decided here)

Does SOLR-10322 close on an NPE-only fix? The JIRA title is "Streaming expressions Daemon can't connect to topic checkpoint when basic authentication is enabled". The branch now says, honestly, that it fixes only the NPE when no cache is supplied. Options for the owner:

- (a) Close the ticket on the NPE fix, retitle or re-scope the JIRA, and keep the changelog as it is.
- (b) Keep the ticket open for the auth path. That needs an auth-enabled test that exercises the server `StreamHandler` cache and the no-cache path, plus a check that the `ZkController` cache carries credentials.

## Not checked

- Not compiled, formatted, or run. Test pass/fail on base and on head is a hypothesis.
- `InternalSolrClientCache` and the `ZkController` credential setup were not read, so the server path's auth behaviour is not established.
- Changelog tooling (parse, tidy) and Error Prone were not run.
- Not checked: whether a checkpoint collection with BasicAuth ever reaches the no-cache path in production.
