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

Suggested tests (not written):

1. Construct a `ShardResponse` with a query-bearing shard request and a
   subsection-less response; invoke `getSubsectionFromShardResponse`; assert
   the thrown `SolrException` message does not contain the query text.
2. Log-appender assertion that the WARN names the node but not the query params.
3. Negative check: no `getShardRequest()` remains in any warn/error log line
   in `SolrResponseUtil`.

## Patch limits

- Trivial one-line change; risk is essentially nil, but it is uncompiled.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
