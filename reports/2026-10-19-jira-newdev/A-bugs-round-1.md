# JIRA newdev bugs, round 1 (A)

One section per ticket. Assignment: `assignments/pool-jira-newdev-bugs-round-1.md`.

## SOLR-15231: zk upconfig does not recognize ZK_HOST style url (vm3, 2026-10-19)

- Status: already fixed on apache/main at `c8ab71430f`, by code reading. No branch pushed. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-15231.md`.
- The report is a comma-separated host list with a chroot (`<host>,<host>,<host>/solr`), where the configset went to the first host's root instead of `/solr`.
- Evidence on the base, read from the code:
  - The `-z` value is passed through unchanged: `solr/core/src/java/org/apache/solr/cli/ConnectionOptions.java` lines 117 to 125 return the connection string as given.
  - The chroot is kept by `CloudSolrClient.CloudSolrClientConnection.parseZkQuorum` (`solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java` lines 1765 to 1780): the part from the first `/` becomes `zkChroot`, and `toString` puts it back.
  - `SolrZkClient` splits the connect string at its first `/` (`solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/SolrZkClient.java` lines 139 to 149) and applies the chroot as the ZooKeeper namespace (line 184), so every path resolves under `/solr`.
  - Upload writes to `/configs/<name>` through that client (`solr/core/src/java/org/apache/solr/cli/ConfigSetUploadTool.java` lines 131 to 150).
  - The chroot-aware universal connection string arrived with SOLR-18130 (`bf610531f1`), which is the change that fixes this path.
- Not run: no ZooKeeper reproduction on `c8ab71430f`. A live run would be needed to say the original report cannot happen; this section records a code-reading verdict only.
- Gate: none, since no branch was pushed.

## SOLR-16193: suggester not built, but API returns success (vm3, 2026-10-19)

- Status: not reproduced on apache/main at `c8ab71430f`. No branch pushed. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-16193.md`.
- The ticket expects a non-200 when the suggester build fails. The reporter's log shows `IllegalStateException: suggester was not built` while `suggest.build=true&suggest=true` returns 200.
- Code reading: `SolrSuggester.build` (`solr/core/src/java/org/apache/solr/spelling/suggest/SolrSuggester.java` line 170) rethrows only `AlreadyClosedException`. `SuggestComponent.process` (`solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java` around line 287) has no catch around `getSuggestions`. The `not built` text comes from Lucene's lookup.
- Probe: a temporary test (not committed) indexed one document, deleted all documents, committed, then ran `qt=/suggest&suggest.dictionary=suggest_fuzzy_doc_dict&suggest.build=true&suggest.q=exampel`. It passed with `numFound=0` and no error in the log (`/workspace/gates/logs/vm3/solr-16193-repro/focused.log`, seed `16193E50`).
- Not covered: the reporter's own handler and dictionary config (for example a build-on-commit or stored-dictionary setup). That path would need the reporter's configuration to reproduce, so this is recorded as not reproduced, not as fixed.
- Gate: none, since no branch was pushed.

## SOLR-16220: Missing SslContextFactory NPE for SolrJ https client (vm3, 2026-10-19)

- Status: not reproduced for the documented case on apache/main at `c8ab71430f`. No branch pushed. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-16220.md`.
- Code reading: `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java` lines 229 to 233 use `getDefaultSslContextFactory()` when no `SSLConfig` is set, so a client built with an `https` URL and no SSL config gets a factory.
- Probe (temporary test, not committed): `new HttpJettySolrClient.Builder("https://127.0.0.1:1/solr").build()` then a query. The output shows the request reached the socket connect step (`SolrServerException: access denied ("java.net.SocketPermission" ...)`), with no `Missing SslContextFactory` NPE. The test passed (`/workspace/gates/logs/vm3/solr-16220-repro/`, seed `16220E50`). The sandbox's socket check stopped the connect, so this shows the destination was built, not that a live TLS handshake succeeds.
- Variant not covered: `SSLConfig.createClientContextFactory()` returns null when the config is not in SSL mode (`solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/SSLConfig.java` line 144). A client built with a non-SSL `SSLConfig` and an `https` URL would pass a null factory to Jetty. That is the path that matches the ticket's NPE, and it needs an owner decision on the intended behavior (fall back to the default factory, or fail with a clear message). Not fixed here.
- Gate: none, since no branch was pushed.

## SOLR-18214: base configset during configset creation is not optional (vm3, 2026-10-19)

- Status: stopped, no branch. The fix is a design choice the ticket leaves open. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-18214.md`.
- Ticket: the v2 create request should accept an omitted `baseConfigSet`, which the ticket reads as creating a configset from scratch. Today it fails.
- Code on `c8ab71430f`:
  - v2 body: `solr/api/src/java/org/apache/solr/client/api/model/CloneConfigsetRequestBody.java` line 30 declares `baseConfigSet` with a Jackson `defaultValue` of `_default`, which is not applied on the request path.
  - v2 handler: `solr/core/src/java/org/apache/solr/handler/configsets/CloneConfigSet.java` line 65 checks the base exists and rejects an omitted (null) base as "Base ConfigSet does not exist".
  - v1 `CREATE`: `solr/core/src/java/org/apache/solr/handler/admin/ConfigSetsHandler.java` lines 133 to 135 map an omitted base to `_default` on purpose. That behavior must stay for v1.
- Why stopped: "omitted" needs a defined result. Options: (a) default to `_default`, which the ticket says blocks from-scratch creation; (b) create an empty configset with no base, which needs a decision on the minimum files it gets (for example `solrconfig.xml`, `schema.xml`) and on how an empty configset is validated. Option (b) is a new creation path, beyond a minimal fix. Owner decision needed on which result, and on whether base-change propagation (the ticket's second point) is in scope.
- Gate: none, since no branch was pushed.

## SOLR-13183: NullPointerException at SolrDispatchFilter.doFilter (vm3, 2026-10-19)

- Status: not reproducible at `c8ab71430f`. No branch pushed. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-13183.md`.
- The ticket's `SolrDispatchFilter.doFilter` no longer exists at the base; it was renamed to `SolrServlet` by SOLR-18112. The base dispatches through `ServletUtils.getPathAfterContext`, which handles the input.
- Evidence: a throwaway test with the ticket's inputs returned HTTP 400 on the base in two runs. Seed `13183C0FFEE13183`.
- Gate: none, since no branch was pushed.

## SOLR-13184: NPE in ValueSourceParser for joindf with missing argument (vm3, 2026-10-19)

- Status: fixed on branch `solr-13184-joindf-missing-arg` at `6bb01a08cd` (base `c8ab71430f`; test commit `2e178c9d92`). Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-13184.md`. Receipt: `receipts/SOLR-13184.md`.
- Reproduced on the base: `{!frange l=10 u=100}joindf(genre:comedy,$x)` gives an NPE in `ValueSourceRangeFilter.hashCode` (null `qfield`), a 500 instead of 400. Seed `8B3C0622AB0B57FC`.
- Fix: the `joindf` parser in `solr/core/src/java/org/apache/solr/search/ValueSourceParser.java` throws `BAD_REQUEST` when the field or query argument is missing.
- Gate: tidy exit 0 (no changes); `JoinDocFreqMissingArgTest` 1 of 1 pass at head (seed `9BAA9229A0A59961`); `:solr:core:check -x test` exit 0.

## SOLR-13185: NPE in complexphrase query parsing with no query string (vm3, 2026-10-19)

- Status: fixed on branch `solr-13185-query-parse-null` at `84523828bb` (base `c8ab71430f`; test commit `365392a484`). Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-13185.md`. Receipt: `receipts/SOLR-13185.md`.
- Reproduced on the base: `defType=complexphrase` with no `q` gives an NPE in `StringReader` through `ComplexPhraseQueryParser.parse`. Seed `42E3C64708EB0C45`.
- Fix: `ComplexPhraseQParser.parse` in `solr/core/src/java/org/apache/solr/search/ComplexPhraseQParserPlugin.java` returns null for a null query string, matching the Surround parser.
- Gate: `TestComplexPhraseQParserPlugin` 7 of 7 pass at head (seed `EC3309AF3E79CF1D`); tidy and `:solr:core:check -x test` exit 0.

## SOLR-13188: NPE in IndexSearcher.rewrite via block-join parent with fq (vm3, 2026-10-19)

- Status: not reproducible at `c8ab71430f`. No branch pushed. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-13188.md`. Receipt: `receipts/SOLR-13188.md`.
- The ticket's query `{!parent fq={!collapse field=id}&rentDocument}` returns HTTP 400 on the base, because `BlockJoinParentQParser.parse` throws a `SyntaxError` when neither `which` nor `parentPath` is given. The NPE stack names `BitDocIdSetFilterWrapper`, which is not in the base.
- Evidence: `BJQParserTest` 19 of 19 pass at base, including the new 400 assertion (seed `3A1E14DB0B2A6B32`).
- Gate: none, since no branch was pushed.

## SOLR-13195: NPE for distributed queries when no shards are configured (vm3, 2026-10-19)

- Status: not reproducible over HTTP at `c8ab71430f`. No branch pushed. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-13195.md`. Receipt: `receipts/SOLR-13195.md`.
- Over HTTP, the ticket's request (`distrib=true&facet=on&facet.field=...`, no `shards`) returns HTTP 400 on the base: the node's own URL is not in `allowUrls`. Seed `7FF92B0309150483`.
- Open question for Nick: an in-process request with `distrib=true` and no shards (TestHarness or EmbeddedSolrServer, no HTTP call) hits an NPE at `HttpShardHandler.prepDistributed`, where `req.getHttpSolrCall()` is null. The fix was not made because it is not the HTTP symptom in the ticket.
- Gate: none, since no branch was pushed.

## SOLR-14970: elevation does not work without elevate.xml config (vm3, 2026-10-19)

- Status: stopped, no branch. The ticket needs behavior beyond a minimal fix, so the assignment's scope rule applies. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-14970.md`.
- Verdict by code reading, no test run: with no `config-file`, `QueryElevationComponent` throws `InitializationException` with `NO_CONFIG_FILE_DEFINED`, and `handleInitializationException` skips that cause without logging (`solr/core/src/java/org/apache/solr/handler/component/QueryElevationComponent.java` lines 265 to 273 and 298 to 305). The component is then a no-op, which matches the report.
- The ticket's example uses an `elevateIds` query parameter. No such parameter exists in `solr/core/src/java`, so the example cannot work with any config.
- Options for Nick: (a) minimal: log a warning when the config-file is missing, which is still a behavior change in the logs; (b) feature: accept elevated ids from the request without an elevate.xml. Option (b) goes beyond the ticket's minimal-fix scope.
- Gate: none.

## SOLR-5744: LangDetect processor NPE with JavaBin update (vm3, 2026-10-19)

- Status: not reproduced at `c8ab71430f`. No branch pushed. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-5744.md`.
- The ticket's NPE is in `LangDetectLanguageIdentifierUpdateProcessor.detectLanguage`. On the base, `SolrInputDocumentReader` skips missing fields, `asString` cannot throw an NPE, and `LanguageDetectionOrchestrator.detectAll` returns a non-null list on every path (checked with `javap` on language-detection-12.5.2.jar).
- Evidence: `LangDetectLanguageIdentifierUpdateProcessorFactoryTest` 13 of 13 pass on the base, including the empty and short document cases (seed in the claim's log).
- Not run: no JavaBin end-to-end update was run, so this is a code-reading verdict plus the existing tests.
- Gate: none, since no branch was pushed.

## SOLR-17433: SolrStream default client has a 60-second timeout (vm3, 2026-10-19)

- Status: not reproduced by code reading at `c8ab71430f`. No branch pushed. Claim: `claims/pool-vm3-jira-newdev-bugs-round-1-solr-17433.md`.
- The 60-second floor in `SolrClientCache` (`solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/SolrClientCache.java`, `MIN_TIMEOUT`) sets the connection timeout to 60 s. The idle timeout becomes 600 s on the base, and the total request timeout falls back to it, so a default `SolrStream` gets a 600-second total timeout.
- Not run: no live streaming test of longer than 60 s, so this is a code-reading verdict.
- Gate: none, since no branch was pushed.
