# PR 5235 compatibility fixes: receipt, round 3

Written by vm2, the gating host. Gate: `gates/pr-5235-compatibility-fixes-round-3.md`, superseded for the tidy repair by `gates/pr-5235-compatibility-fixes-round-3-superseding-caf8dde.md` (board `dc64c75f92c`), with addendum `gates/pr-5235-compatibility-fixes-round-3-self-review-addendum.md`. Claim: `claims/pr-5235-compatibility-fixes-round-3-vm2.md`. Outcome: `reports/pr-5235-compatibility-fixes-round-3-outcome.md`.

## Inputs and ancestry

- Original PR head O: `9ff44cdd0b818c6f1296d69651732f794acd76b7` (apache/solr PR 5235, fork `dsmiley/solr:SOLR-18351-backport-10x`, base `branch_10x` at `f45b38d5f6c0`). Live head re-checked at 21:00 UTC: unchanged.
- Fixed head F: `caf8dde9063fd2470c02d9938b5723e691c0e11e` on `nick-boss-tech/solr:solr-18351-submit`. Chain: `9ff44cdd` (O), then `5f279c7a` (author fixes and tests, Windows), then `caf8dde9` (formatting only: javadoc line wrap in `CommandOperation.java`, Nick Shanin, no trailers, required by tidy).
- O is an ancestor of F and of the live head. Incremental O..F: six production files (ApiBag, EmbeddedSolrServer, SolrRequest, JavaBinRequestWriter, XMLRequestWriter, CommandOperation) and three regression suites. `HttpJettySolrClientTest` is unchanged.
- Head disclosure: the self-review addendum states the fixed head is `5f279c7aa44a` and that no new head exists. The fork tip is `caf8dde9063f`, one javadoc wrap later, which this gate pins. The addendum's statement should be corrected in the self-review record.
- Test blob hashes, identical on the O and F legs (overlay by `git restore --source`, verified with `git hash-object`): `TestEmbeddedContentStreamCompatibility.java` `1002edb43d8e`, `TestCommandOperationCompatibility.java` `e3a8821c3c14`, `TestLegacyContentStreamCompatibility.java` `4afa8061f83b`.
- O tree: `/workspace/worktrees/pr5235-r3-O`, O production with those three test files overlaid; tracked production unchanged. F tree: `/workspace/worktrees/pr5235-r3-F` (clean at `caf8dde`). Root-check tree: `/workspace/worktrees/pr5235-r3-Fclone`, a full clone at the same tree (`0221b938`).
- Seed `5235C0FFEE18351` on all focused and neighboring runs.
- Toolchain: vm2 (Linux, aarch64), JDK `/workspace/jdk/jdk-21.0.12.1+1`, Gradle from the branch wrapper, `GRADLE_USER_HOME=/workspace/gradle-home`, `--no-daemon --no-build-cache`, host lock `/workspace/tools/.gradle-vm2.lock`, runner `/workspace/gates/logs/vm2/pr5235-r3/run-r3.sh`.

## Steps, exit codes and times (UTC)

| Step | Tree | Command | Exit | Times |
| --- | --- | --- | --- | --- |
| tidy-F (before repair) | F at `5f279c7` | `./gradlew tidy` | 0 (changed one javadoc line; committed as `caf8dde`) | 19:26 |
| tidy-F | F at `caf8dde` | `./gradlew tidy` | 0, tree clean | 19:29:54 |
| O-solrj | O | `:solr:solrj:cleanTest :solr:solrj:test --tests ...TestCommandOperationCompatibility` | 1 (2 of 2 failed) | 19:29:59 to 19:35:06 |
| O-core | O | `:solr:core:cleanTest :solr:core:test --tests ...TestEmbeddedContentStreamCompatibility` | 1 (2 of 4 failed) | 19:35:13 to 19:38:51 |
| O-jetty | O | `:solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests ...TestLegacyContentStreamCompatibility` | 1 (8 of 12 failed) | 19:38:58 to 19:41:11 |
| F-solrj | F | same as O-solrj | 0 (2 of 2 pass) | 19:41:18 to 19:45:25 |
| F-core | F | same as O-core | 0 (4 of 4 pass) | 19:45:32 to 19:49:03 |
| F-jetty | F | same as O-jetty | 0 (12 of 12 pass) | 19:49:10 to 19:51:22 |
| neighbor-solrj | F | `:solr:solrj:cleanTest :solr:solrj:test --tests ...HttpJdkSolrClientTest --tests ...EmbeddedSolrServerTest` | 0 | 19:51:29 to 19:54:06 |
| neighbor-jetty | F | `:solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests ...HttpJettySolrClientTest` | 0 | 19:54:06 to 19:56:41 |
| neighbor-core | F | `:solr:core:cleanTest :solr:core:test --tests ...TestEmbeddedSolrServerSchemaAPI --tests ...TestEmbeddedSolrServerResponseParser` | 0 | 19:56:41 to 19:59:06 |
| tidy-F-2 | F | `./gradlew tidy` | 0, tree clean | 20:01:01 |
| root-check (worktree attempt) | F worktree | `env -u NODE_ENV ./gradlew check -x test` | 1 at `:solr:solr-ref-guide:buildLocalAntoraSite` | 20:01:09 to 20:17:57 |
| root-check (clone) | `pr5235-r3-Fclone` at `caf8dde` | `env -u NODE_ENV ./gradlew check -x test` | 0, BUILD SUCCESSFUL in 20m 30s | 20:28:55 to 20:49:29 |

## Method outcomes (18 methods, fresh JUnit XML, per leg)

| # | Method | O (`9ff44cd`) | F (`caf8dde`) |
| --- | --- | --- | --- |
| 1 | solrj `testReadCommandsTwoArgumentSignature` | FAIL `NoSuchMethodException` `readCommands(Iterable, NamedList)` | PASS |
| 2 | solrj `testReadCommandsThreeArgumentSignature` | FAIL `NoSuchMethodException` (three-argument form) | PASS |
| 3 | core `testUnnamedContentStreamsRetainAllDocuments` | FAIL `Both content streams must be indexed` expected 2 but was 1 | PASS |
| 4 | core `testNamedContentStreamsRetainAllDocuments` (control) | PASS | PASS |
| 5 | core `testGenericLegacyStreamsRetainAllDocuments` | FAIL `SolrException: missing content stream` | PASS |
| 6 | core `testMultipartContentWriterTakesPrecedenceOverLegacyStreams` (control) | PASS | PASS |
| 7 | jetty `testJettyLegacyRequestWithJavaBinWriter` | FAIL body 6 bytes `wt=xml`, expected 21 `original-request-body` | PASS |
| 8 | jetty `testJettyLegacyRequestWithXmlWriter` | FAIL body 6 bytes `wt=xml` | PASS |
| 9 | jetty `testJdkLegacyRequestWithJavaBinWriter` | FAIL body 6 bytes `wt=xml` | PASS |
| 10 | jetty `testJdkLegacyRequestWithXmlWriter` | FAIL body 6 bytes `wt=xml` | PASS |
| 11 | jetty `testJettyJavaBinWriterStreamOverride` | FAIL body 21 bytes `original-request-body`, expected 18 `custom-writer-body` | PASS |
| 12 | jetty `testJettyXmlWriterStreamOverride` | FAIL body 21 bytes, expected 18 | PASS |
| 13 | jetty `testJdkJavaBinWriterStreamOverride` | FAIL body 21 bytes, expected 18 | PASS |
| 14 | jetty `testJdkXmlWriterStreamOverride` | FAIL body 21 bytes, expected 18 | PASS |
| 15 | jetty `testJettyContentWriterControl` (control) | PASS | PASS |
| 16 | jetty `testJettyFormContentWriterControl` (control) | PASS | PASS |
| 17 | jetty `testJdkContentWriterControl` (control) | PASS | PASS |
| 18 | jetty `testJdkFormContentWriterControl` (control) | PASS | PASS |

Totals: O 12 fail, 6 pass (controls 4, 6, 15 to 18). F 18 pass, 0 fail, 0 skip. The predicted O totals (12 fail, 6 controls) match.

## Original-leg observations and addendum correction

- Generic embedded case (5): the addendum corrected the expectation. O fails with `SolrException: missing content stream` before any document-count assertion. The captured XML message does not include a status code, so the addendum's `BAD_REQUEST` is not confirmed by this run. This is the missing-payload failure, not a doc-count result, and it is additional coverage of the explicit embedded fallback, not a fifth independent regression.
- Unnamed streams (3): O indexes one document, as the confirmed regression predicts. F indexes both.
- Legacy request bodies (7 to 10): O sends the form body `wt=xml` (6 bytes) in place of `original-request-body`. Captured with `OUTPUT` diagnostics, not from parameter parsing.
- Writer overrides (11 to 14): O sends `original-request-body` (21 bytes) in place of `custom-writer-body` (18 bytes).

## Neighboring tests (final F, `caf8dde`)

- solrj: 83 tests, 0 failures, 0 errors, 1 skip. `EmbeddedSolrServerTest.testQueryPerf` (skipped by the `monster` test group, not enabled). `HttpJdkSolrClientTest` 40 tests, 0 skips.
- solrj-jetty: `HttpJettySolrClientTest` 52 tests, 0 failures, 0 errors, 0 skips. Includes the two new author methods.
- core: 4 tests, 0 failures, 0 errors, 1 skip. `TestEmbeddedSolrServerSchemaAPI.testSchemaAddFieldAndVerifyExistence` (existing `assume` "needs to amend schema"). `TestEmbeddedSolrServerResponseParser` 2 tests, 0 skips.

The three skips are existing assumption-based or group-gated skips in test methods the gate did not add. They are reported, not counted as passes.

## Tidy and root check

- Tidy at `5f279c7`: rc 0, changed one javadoc line (committed as `caf8dde`).
- Tidy at `caf8dde`: rc 0, clean. Tidy again after neighbors: rc 0, clean.
- Root check, worktree attempt: rc 1. Gradle failed at `:solr:solr-ref-guide:buildLocalAntoraSite`. The info-level rerun shows the cause: `fatal ... "Local content source must be a git repository: /workspace/worktrees/pr5235-r3-F"`. The worktree's `.git` is a file, which Antora rejects. This is an environment condition, not a test or content result. Logs kept at `root-check.log` and `antora-diagnose.log`.
- Root check, full clone: rc 0, BUILD SUCCESSFUL in 20m 30s. `:solr:core:check` and the solrj and solrj-jetty checks executed, and `buildLocalAntoraSite` ran. 26 javadoc tasks are reported SKIPPED by the build configuration. No task failed. Tree clean after the run.
- Execution condition: `env -u NODE_ENV` for the root check, so NODE_ENV is unset in the environment. The clone has a full `.git` directory at the same tree hash as the worktree (`0221b938`).

## Evidence (vm2, not on the board)

- Runner status: `/workspace/gates/logs/vm2/pr5235-r3/status.txt`.
- Per-step logs: `<step>.log` in the same directory. Per-step XML and OUTPUT: `<step>-xml/`.
- Root check: `root-check.log` (worktree attempt, failed), `root-check-clone.log` (clone, passed).
- Antora diagnostic: `antora-diagnose.log`.
