# JIRA newdev small round 1

Assignment: `assignments/pool-jira-newdev-small-round-1.md`. Base: apache/main c8ab71430f.

## Item 4: SOLR-8755 (vm4), 2026-10-19T18:51:27Z

- Verified: JIRA status Open, no resolution, on 2026-10-19 (anonymous REST read). Scope is clear: tests for `org.apache.solr.parser.TokenMgrError`.
- Status: fixed branch. Branch `solr-8755-tokenmgrerror-tests` on the fork: test commit 31bdbd2488 (test only), draft commit e338b6e26e (`drafts/SOLR-8755/pr-description.md`).
- Gate: `:solr:core:test --tests org.apache.solr.parser.TokenMgrErrorTest` and `:solr:core:spotlessJavaCheck` at 31bdbd2488, 2026-10-19: 5 of 5 tests pass, 0 failures, 0 skipped. Log `/workspace/gates/logs/vm4/solr-8755-test-3.log`, XML `/workspace/gates/logs/vm4/solr-8755-TEST-TokenMgrErrorTest.xml`.
- No fail-before run: test-only change, no source change.
- Observation, not changed: the lexical error message quotes the numeric code point (`'120' (120)`), not the character. Recorded in the draft as a maintainer question.

## Item 3: SOLR-18123 (vm4), 2026-10-19T18:59:12Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Stopped, no branch. Scope is unclear: the ticket asks that test infrastructure stop imposing the allow-paths restriction by default. That is a change to the shared test base and affects many tests, and the ticket itself says isolation should be decided another way. The assignment says to record and stop when scope is unclear. Owner or maintainer decision needed on the test-infrastructure design.

## Item 2: SOLR-17727 (vm4), claimed 2026-10-19T18:59:12Z

Claim: `claims/pool-jira-newdev-small-round-1-solr-17727.md`. Work follows on branch `solr-17727-server-header`.


## Item 2: SOLR-17727 (vm4), stopped, 2026-10-19T19:03:40Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Stopped, no branch. The Server header is off on purpose. `solr/server/etc/jetty.xml` sets `sendServerVersion` from `solr.jetty.send.server.version`, default `false`. Jetty's own switch only sends `Jetty(<version>)`, not Solr's version, so sending Solr's version needs a custom `HttpConfiguration` customizer loaded by the server's XML config. That is a new class on the server classpath that I could not verify without a packaged start, and it reverses a default that hides the version. The assignment says to stop when the change conflicts with the wanted result; a maintainer or the owner should decide whether Solr should expose its version.

## Item 1: SOLR-18209 (vm4), fixed branch, 2026-10-19T19:26:34Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Status: fixed branch. `solr-18209-list-of` on the fork: code commit f73f2d4358 (three files, four lists: `NodeHealth.UNHEALTHY_STATES`, `SchemaDesignerConfigSetHelper.includeLangIds`, `ContentStreamBase` `UNHELPFUL_TYPES` and the three suffix lists), draft commit c8fef7c4cd (`drafts/SOLR-18209/pr-description.md`).
- Gate: `ContentStreamTest` 7 of 7, `NodeHealthTest` 8 of 8, `TestSchemaDesignerConfigSetHelper` 8 of 8, 0 failures, 0 skipped; spotless passes for solrj and core. Log `/workspace/gates/logs/vm4/solr-18209-gate.log`.
- No new test: the existing tests named above cover the changed fields. HashSet-wrapped fields left alone (mutable sets). Recorded in the draft as a maintainer question.

## Item 9: SOLR-18481 (vm4), fixed branch, 2026-10-19T19:48:24Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Status: fixed branch. `solr-18481-xff` on the fork: code commit 5aedffcccd (`HttpSolrProxy` appends the client address to `X-Forwarded-For`, with the TODO removed), draft commit 8f687ca04d (`drafts/SOLR-18481/pr-description.md`).
- Gate: `HttpSolrProxyForwardedForTest` 5 of 5, `HttpSolrProxyTest` 2 of 2, 0 failures, 0 skipped; spotless passes. Log `/workspace/gates/logs/vm4/solr-18481-gate.log`.
- The ticket's forward cap and current-node exclusion are not in this change. The draft asks the maintainer whether to add them.

## Item 12: SOLR-9464 (vm4), stopped, 2026-10-19T19:53:09Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Stopped, no branch. The change flips `CloudSolrClient` `directUpdatesToLeadersOnly` from `false` to `true`. The ticket's own history says the flag was added under SOLR-9090 with a default of `false` "corresponding to existing behaviour". A default flip changes update routing for every SolrJ cloud client that does not set the flag. The assignment says to record and stop when the change conflicts with a deliberate default. A maintainer decision is needed on whether to change the default for all clients.

## Item 13: SOLR-6948 (vm4), already fixed at base, 2026-10-19T19:55:24Z

- Verified: JIRA status Open, no resolution, on 2026-10-19, but the reported flow is no longer what the code does at apache/main c8ab71430f.
- Stopped, no branch. `solr/core/src/java/org/apache/solr/cli/RunExampleTool.java` checks collection existence right after the name prompt (lines 1202 to 1213). When the collection exists, the user is asked only whether to reuse it, and reuse returns the name at once (`return collectionName`, line 1217 area). The shard, replica, and config prompts come after that check and are skipped on reuse. That is the behavior the ticket asks for. The ticket reads as already done. A maintainer should confirm and close it.

## Item 8: SOLR-18320 (vm4), stopped, 2026-10-19T20:11:48Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Stopped, no branch. The report is a warning from the ZK credentials injector on `bin/solr create --solr-connection` with no ZK host. The class is loaded from `ZkController`, `CloudConfig`, and `SolrXmlConfig` (the config setter at `SolrXmlConfig.java:545`). I did not find which path the HTTP-only create command takes to reach it. Confirming the path needs a live `bin/solr create` run against a Solr node, which I did not run. Scope is unclear without that run, so no fix is proposed.

## Item 10: SOLR-17415 (vm4), already done in code, 2026-10-19T20:11:48Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Stopped, no branch. At apache/main c8ab71430f the code no longer uses `TimeLimitingCollector`. `CommandHandler.searchWithTimeLimiter` calls `searcher.search` directly, and `Grouping.searchWithTimeLimiter` does the same. Two stale javadoc comments still name the class (`search/Grouping.java:426`, `search/grouping/CommandHandler.java:243`), and the method names are misleading. Those are cleanup with no behavior to test, so a branch would not carry a regression test. A maintainer can close the ticket or ask for the comment cleanup.

## Item 5: SOLR-18169 (vm4), stopped, 2026-10-19T20:32:14Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Stopped, no branch. The change is to the slim packaging and the Docker image build (`solr/docker/build.gradle` has the slim distribution switch at line 29). Gating it needs a Docker build, and Docker is not installed on this host. A packaging change cannot be gated here, so no branch is pushed.

## Item 6: SOLR-16307 (vm4), blocked on the docs build, 2026-10-19T20:32:14Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Blocked, no branch. The affected blocks are `[source,powershell]` in `solr-ref-guide/modules/deployment-guide/pages/enabling-ssl.adoc` (lines 105, 160, 187). The fix is in the ref guide highlighting, and the gate for a ref guide change is the Antora site build. That build fails on this host with `Cannot find module '@asciidoctor/tabs'` (logs `/workspace/gates/logs/vm4/docs-item4-site.log` and `docs-item4-site-2.log`). No gate, no branch.

## Item 7: SOLR-18503 (vm4), blocked on the docs build, 2026-10-19T20:32:14Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Blocked, no branch. The ticket asks for seven JSON Facet keys to be documented in the ref guide. The docs change is the gate target, and the Antora site build fails on this host with the same `@asciidoctor/tabs` error, so the change cannot be gated. No branch.

## Item 11: SOLR-17262 (vm4), stopped, 2026-10-19T20:32:14Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Stopped, no branch. The ticket asks for SolrParams-style typed getters on `NamedList` and names `getLong`, `getFloat`, `getDouble` as examples. It does not fix the full method set, the names beyond those examples, or the error behavior on a missing or wrong-typed value. Those are API decisions. The assignment says to stop when the scope is unclear. A maintainer should choose the method set first.
