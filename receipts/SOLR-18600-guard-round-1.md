# Receipt addendum: SOLR-18600 binary compatibility guard, build round 1 (phone agent)

Status: GUARD BUILT AND GATED at `83cf27ac11d2e0f44762fbf594e8e42c1c7ffc2c`. The restoration
receipt `receipts/SOLR-18600.md` covers the fix itself; this addendum covers the guard the
2026-11-01 design record scoped (`reports/solr-18600-solrj-binary-compat-check-design-round-1.md`)
and the rebase of the fix onto current branch_10x. Nick directed the build on 2026-11-02
("build the hard thing and present it as the solution that's more than just a patch").

## Branch state

- Branch `solr-18600-http-abi-restore-submit`, base is current `branch_10x` at `eb742ad89a`
  (the old base `a336bf871b` is 29 commits behind; the fork head `c40665af8d` was rebased,
  not cherry-picked).
- Restoration commit after rebase: `568d383f2e`. The rebase conflicted only in
  `HttpSolrClient.java`, against PR #5235 (SOLR-18351), which had rewritten two methods the
  fix moves into `HttpSolrClientBase`. Resolution: the restored slim `HttpSolrClient` stands;
  in `HttpSolrClientBase`, `validateGetRequest` now uses `getContentWriterOrStreams` as #5235
  wrote it, and the new `isMultipart(RequestWriter.ContentWriter)` overload sits alongside
  the restored 10.0.0 `isMultipart(Collection<ContentStream>)`, so both the 10.0.0 subclass
  surface and the current in-repo caller compile.
- Guard commit: `83cf27ac11` (7 files: root `build.gradle` apply line,
  `gradle/libs.versions.toml`, `gradle/validation/binary-compat.gradle` (new),
  `solr/solrj/binary-compat-exclusions.txt` (new), `solr/solrj/gradle.lockfile`,
  `changelog/unreleased/SOLR-18600-binary-compat-check.yml`).

## What the guard does

- New task pair in `gradle/validation/binary-compat.gradle`, applied to `:solr:solrj` only
  and wired into `:solr:solrj:check`: `binaryCompatReport` runs japicmp 0.23.1 in-process
  and writes a text report plus change descriptors under
  `solr/solrj/build/reports/binary-compat/`; `checkBinaryCompat` fails the build on any
  binary-incompatible descriptor not excused in `solr/solrj/binary-compat-exclusions.txt`,
  and warns on exclusion entries that match no current change.
- Compared surface: the solrj jar plus the solr-api jar it always ships with, against the
  same pair from the baseline release. solrj bundles copies of solr-api classes; comparing
  solrj alone reports bundle reshuffling between the two jars as API removals.
- Scope per the design's first slice: public and protected surface
  (`AccessModifier.PROTECTED`), binary-incompatible changes (removals, signature and return
  type changes, visibility reductions, field and constructor changes). The design's second
  slice clauses (added abstract methods, changed constant values) are not enforced yet.

## Design questions resolved

- Baseline default: the previous release on the same branch line, 10.1.0, carried as the
  Gradle property `solrj.binaryCompat.baselineVersion`. A guard can only police changes
  since the last release; the 10.0.0 comparison stays available through the property and
  is exercised in the proof below. `-Psolrj.binaryCompat.enabled=false` disables the check
  (major-version lines); `-Psolrj.binaryCompat.candidateJar` plus
  `-Psolrj.binaryCompat.candidateApiJar` compare the baseline against published jars
  instead of the built ones, which is how the check itself was validated.
- Deviation from the design sketch: the japicmp Gradle plugin's task runs its comparison
  in a worker daemon process, and worker daemons cannot start on this VM ("Never received
  a connection from Gradle Worker Daemon", reproduced three times with intact plugin,
  japicmp and javassist jars). The guard therefore calls the same japicmp library
  (com.github.siom79.japicmp:japicmp 0.23.1, the version the plugin wraps) directly from
  the task action. Same comparisons, same change model, no worker process.

## Proof

- The check catches the break it exists for: with baseline 10.0.0 against the published
  10.1.0 jar pair, `checkBinaryCompat` FAILS with 480 binary-incompatible descriptors,
  including `HttpSolrClientBase` CLASS_REMOVED, `HttpSolrClientBuilderBase` CLASS_REMOVED,
  `SolrRequest.processWithBaseUrl` METHOD_REMOVED and the rest of the declared-type cluster
  from the ticket (descriptor list retained in the gate evidence).
- The restoration is complete against 10.0.0: at the branch head with the baseline
  property set to 10.0.0, the report lists 167 descriptors and none of them is in the
  SOLR-18600 cluster (no base-class, builder-base, `processWithBaseUrl`, `withHttpClient`
  or `getHttpClient` items). The 167 are the line's other breaks taken between 10.0.0 and
  10.1.0, which already shipped and cannot be unshipped; they are why the default baseline
  is the previous release rather than 10.0.0.
- The gate is green at the head on the default baseline: 50 descriptors against 10.1.0,
  all excused by the 24 entries in the exclusions file, no stale-entry warnings. The
  entries are the restoration itself (18 entries citing SOLR-18600: against 10.1.0 the
  restored 10.0.0 descriptors read as changes, because 10.1.0's narrowed shapes were the
  break), ApiMapping's removal (PR #5242), and one entry each for SOLR-18450, SOLR-15823
  and SOLR-18351, each verified against the commit that made the change.
- Restoration re-gate at the rebased head (seed 18600C0FFEE18600 where the original gate
  used it): tidy clean; Error Prone compiles green for solrj, solrj-jetty, solrj-zookeeper
  and core (main and test); HttpSolrClientBinaryCompatTest 4/4, HttpJettySolrClientBinaryCompatTest
  2/2, HttpJdkSolrClientTest 40/40, HttpJettySolrClientTest 52/52 (49 at the old base; the
  class gained tests on branch_10x), CloudHttp2SolrClientBuilderTest 14/14,
  CloudSolrClientCacheTest 6/6, ConcurrentUpdateJdkSolrClientTest 11/11, LBSolrClientTest 8,
  LBSolrClientRetryUnsentTest 3; `:solr:solrj:check -x test` and `:solr:solrj-jetty:check -x test`
  green (the solrj check includes the new guard); root `check -x test` in a normal clone
  green (see Limits for the one environment leg).

## Limits

- One known unrestored item: `CloudSolrClient$Builder.withInternalClientBuilder` existed
  in 10.0.0, was removed in 10.1.0, and the restoration does not bring it back (internal
  builder surface; restoring it was not part of the ticket's declared-type table). It is
  the only 10.0.0 descriptor in the builder cluster still missing at the head.
- Root check environment leg: the first root-check run in the fresh clone failed only at
  `:solr:webapp:verifyUiWasm`, because the Gradle relay delivered a corrupted UI wasm zip
  (checksum mismatch, the standing relay corruption). The byte-correct zip (SHA-256
  654494303e4af...ad2, fetched by curl) was seeded into the clone's ui-download directory
  and the rerun is green; the seeding is an environment workaround, not a branch change.
- The guard covers the solrj and solr-api jars. solrj-jetty and solrj-zookeeper join in a
  later slice, per the design. Core is out of scope by design.
- The baseline default has to move at each release (10.1.0 today); the script header says
  so. Nothing in the build derives it automatically.
- No PR opened and nothing posted to Jira; opening is Nick's call. A draft PR description
  follows the receipt on the board at `drafts/solr-18600-guard-pr-description-round-1.md`.
