# SOLR-17752 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

`SolrResponseUtil.getSubsectionFromShardResponse` logged the full shard
request (`srsp.getShardRequest()`, which contains the raw query string and
potentially customer-sensitive data) on its corrupted-response WARN path.
The one-line fix logs `srsp.getNodeName()` instead — the same identifier the
`SolrException` thrown on the very next line already uses. The `solrResponse`
detail that helps diagnose the corruption is kept.

File changed:
- `solr/core/src/java/org/apache/solr/util/SolrResponseUtil.java` (1 line)

This was the only `getShardRequest()` in an error/warn log line in the file.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Test added (round 4, not compiled or run): `TestShardResponseLogging`
(`solr/core/src/test/org/apache/solr/handler/component/`, same package because
the `ShardResponse` setters are package-private). It builds a shard response
whose header exists but whose `response` section is missing, with a shard
request carrying `q=name:customer_secret_term`, captures the WARN with
`LogListener`, and asserts it names the node and does not contain the term.
The earlier suggestion to assert on the thrown exception message was dropped:
that message never contained the query, so it would also pass without the fix.

## Round 4 review changes

See `research/branch-reviews/round-4/SOLR-17752-review.md`.

- Added `changelog/unreleased/SOLR-17752.yml`.
- Not changed (needs a decision): the same WARN still prints `solrResponse`,
  whose header can echo the request params (`echoParams`) and so still carry
  the query; and `getNodeName()` can be null (shard / shard address would be
  better context).

## Patch limits

- Trivial one-line change; risk is essentially nil, but it is uncompiled.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
