# Receipt: SOLR-18325 Solr 10 client feasibility (vm2, round 2)

Gating host: vm2. Job: `gates/solr-18325-solr10-feasibility-round-1.md` with superseding gate `gates/solr18325-superseding-8bfca8e.md`. Claim: `claims/solr-18325-solr10-feasibility-vm2-round-2.md`.

## Verdicts

- Client-first: INCOMPLETE. A required leg fails: `binaryHttp` (released 10.0.0 consumer bytecode against the candidate runtime) throws `NoSuchMethodError` on `HttpJdkSolrClient$Builder.withConnectionTimeout`, which expects a top-level `HttpSolrClientBuilderBase` return type. Concrete boundary: released 10.0.0 has `HttpSolrClientBuilderBase.java` at top level; the base `f45b38d5f6c0` and the candidate nest the builder as `HttpSolrClient.BuilderBase`. The prototype touches no `HttpSolrClient` file (diff count 0), so this is Solr branch evolution between 10.0.0 and the base, not the Jackson change.
- Full server migration: NOT ESTABLISHED by this job.
- Release readiness: NOT ESTABLISHED. Maintainers still decide internal breaks, the 3.1 LTS versus 3.2 line, and release scope.

## Heads and chain

- Incoming Windows prototype: `aa8cc9ab29a012da2ec4e302cae73238e7e78a1f` (base `f45b38d5f6c0`, apache/solr branch_10x).
- `958a8411cfe` tidy formatting, six source/test files (one `ObjectMapper` wrap in each of four core tests, two joins in `SystemInfoResponse`, one `expectThrows` wrap and one annotation split in `JacksonContentWriterTest`).
- `35a35d69900` dependency locks, 26 `gradle.lockfile` files: Jackson 3.2.1 core, databind and BOM added; Jackson 2.22.3 core and databind kept in the same lockfiles. No license or checksum metadata changed.
- `ed07d0dd67e` Error Prone fix: `CoreAdminJacksonCompatibilityTest` compares start time by epoch millis (`UndefinedEquals` under `-Werror`). Round 1 proof head; superseded (root check failed in `validateSourcePatterns`).
- `00be45f507` explicit type on the flagged declaration in `JacksonBehavior.java` (`var` with diamond). Format hook refused the push; amended.
- `8bfca8edd30` google-java-format of `JacksonBehavior.java`, which also reflowed 11 pre-existing non-conforming lines. Formatting only. This is the round 2 proof head, pushed as a fast-forward and read back from the fork.

## Commands and results at 8bfca8edd30 (all under `/workspace/tools/.gradle-vm2.lock`, `--no-daemon`, NODE_ENV unset)

- `tidy`: rc 0, tree clean. Log `r2/tidy.log`.
- `:solr:solrj:compileTestJava :solr:core:compileTestJava -Pvalidation.errorprone=true`: rc 0.
- Focused SolrJ (six classes, seed `1832510C0FFEE`, `cleanTest`, `--no-build-cache`): rc 0. XML in `r2/focused-solrj-xml/`.
- Focused core (seven classes, same seed): rc 1. 33 tests, 1 failure, 0 errors, 0 skipped. Failure: `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` (`The data directory was not cleaned up on unload after a failed core reload`). Inherited: it fails identically on base `f45b38d5f6c0` at the same seed (round 1 single-test comparison, `cadmin-base-xml`).
- `mavenToLocalFolder`: rc 0. Candidate `solr-solrj` `10.2.0-20261027.074222-1` (`10.2.0-SNAPSHOT`). JAR sha256 `e9a4236a75bf711f92c9...`, POM `6e6f07ebc61bec03...`. Round 1 JAR was `4a522eb12f95...` at a different build time; the only changes since are in the dev-tools harness and a test file.
- Layout repair for the harness: `verifyCandidateDependencies` requires `solr-solrj-10.2.0-SNAPSHOT.pom`; snapshot publication writes only timestamped files. Plain-named copies of the same POM and JAR were added in the build-local repository, with identical hashes. Recorded in `r2/artifact-sha256.txt`.
- POM runtime graph: `tools.jackson.core:jackson-core` 3.2.1, `tools.jackson.core:jackson-databind` 3.2.1, `com.fasterxml.jackson.core:jackson-annotations` 2.22, slf4j. No Jackson 2 core or databind. `verifyCandidateDependencies` passed.
- Harness, Java 21: rc 0. All lanes pass: baselineConsumer, binaryConsumer, candidateConsumer, baselineBehavior, candidateBehavior (identical stream-ownership lines), baselineInternalFieldControl, jackson2Coexistence. internalFieldControl prints the expected `NoSuchFieldError` on `DEFAULT_MAPPER`.
- Harness, Java 17: rc 0, same lane results. Consumer launcher is the Gradle toolchain at `languageVersion=17`, resolved to `/workspace/jdk/jdk17` (17.0.21) via `-Dorg.gradle.java.installations.paths=/workspace/jdk/jdk17` on each run. Gradle does not auto-detect that install; the host Gradle home is unchanged.
- HTTP against Solr 10.0.0 standalone (official archive, SHA-512 verified, Java 21 server, `--user-managed`, core `jackson3_compat` created by CoreAdmin CREATE with `_default`, uniqueKey `id`), Java 17 consumer: `candidateHttp` PASS (core status startTime Date and uptime Long conversion; add, commit, query, delete of a UUID document). `binaryHttp` FAILED with the builder-class `NoSuchMethodError` above.
- `check -x test` at 8bfca8edd30 in clean full clone `/workspace/worktrees/pr5235-r3-Fclone`: rc 0, tree clean, BUILD SUCCESSFUL in 19m 8s, `validateSourcePatterns` passed. Log `r2/root-check.log`.

## Execution conditions and repairs

- First HTTP attempt (round 1) failed at the start-time assertion because `bin/solr create` made a SolrCloud collection whose core is `jackson3_compat_shard1_replica_n1`. Setup error, not product behavior. Re-set as a standalone core; the candidate HTTP lane then passed.
- Solr stop needed a forced kill twice; port 8983 confirmed closed afterwards.
- Round 1 root check failed at `validateSourcePatterns` on `JacksonBehavior.java:48` and was repaired in round 2.
- The candidate tests in four core test files call `new ObjectMapper()` where the base used `JacksonContentWriter.DEFAULT_MAPPER`. This is part of the prototype design and is recorded here.

## Remote head

Fork `solr-18325-submit` at receipt time: `8bfca8edd30`. The fork ref was read back with `ls-remote` after the push.

## Not established

- The `binaryHttp` boundary was shown by source-tree and jar-signature evidence, not by running the released binary against an unmodified base SolrJ artifact. A base-artifact `binaryHttp` run would confirm the inheritance directly and was not run.
- Full server migration and release readiness.

## Proposed repair (owner decision, not implemented)

Restore a top-level `HttpSolrClientBuilderBase` (or a compatibility shim) on the Solr branch so 10.0.0 consumer binaries link, then rerun `binaryHttp`. This changes Solr source outside the prototype and needs an owner decision.
