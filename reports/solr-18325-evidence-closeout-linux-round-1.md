# Report: SOLR-18325 Maven, discriminating mutations and API evidence closeout, Linux round 1 (vm2)

Verdict: INCOMPLETE, unchanged. Released-binary compatibility is NOT established: `binaryHttp` still FAILS. The Jackson-specific evidence below is stronger than before. It cannot waive the inherited release-to-base break. Receipt: `receipts/SOLR-18325-evidence-closeout-round-1.md`. Design: `reports/solr-18325-http-compatibility-prerequisite-design-round-1.md`.

Scope: the candidate `8bfca8edd30` and base `f45b38d5f6c0` were not changed. No production shim, restoration, waiver, GitHub/JIRA/upstream write, or PR. Mutants stayed in scratch worktrees and were never pushed.

## 1. Actual Maven consumer

- Maven: Apache Maven 3.9.9 from `https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz`, SHA-512 verified (`apache-maven-3.9.9-bin.tar.gz: OK`). Unpacked at `/workspace/tools/maven-user/apache-maven-3.9.9`, invoked by its literal path. No sudo, no machine-wide install.
- `mvn --version`: Apache Maven 3.9.9 (8e8579a9e76f7d015ee5ec7bfcdc97d260186937), Java 17.0.21 (Eclipse Adoptium), platform encoding UTF-8.
- Isolation: `settings.xml` with a single `candidate-local` file repository (`candidate-maven-local` copy of the round 2 build-local repo, snapshots enabled) and Maven Central. Local repository `m2-candidate` (isolated). No mirror, no exclusion, no forced version, no shading, no edited publication POM.
- Consumer: `maven-consumer/pom.xml` depends only on `org.apache.solr:solr-solrj:10.2.0-SNAPSHOT`. `maven.compiler.release` 17. Compiled `StableConsumer.java` (source hash in `maven-consumer/source.sha256`) to Java 17 class file major version 61.
- Resolution: `dependency:tree -Dverbose` (`maven-dependency-tree.txt`, plugin `maven-dependency-plugin:3.7.0`). Download sources: 2 entries from `candidate-local` (solr-solrj and solr-api, timestamped snapshot via `maven-metadata.xml`), 262 from Central. The candidate local repo was also queried for other coordinates; those fell back to Central because the repo has no copies of them.
- Resolved candidate graph (Maven): `solr-solrj 10.2.0-SNAPSHOT` (timestamp `20261027.074222-1`), `solr-api 10.2.0-SNAPSHOT`, `tools.jackson.core:jackson-core 3.2.1`, `tools.jackson.core:jackson-databind 3.2.1`, `com.fasterxml.jackson.core:jackson-annotations 2.22`, `slf4j-api 2.0.20`, `jcl-over-slf4j 2.0.20`, `swagger-annotations-jakarta 2.2.54`, `jakarta.ws.rs-api 4.0.0`, `semver4j 6.0.0`, `jspecify 1.0.0`. No `com.fasterxml.jackson.core:jackson-core` or `jackson-databind` on the runtime path.
- Same as the Gradle candidate graph (`consumer-candidate/runtime-classpath.txt` from the round 2 closeout). Maven and Gradle agree on the candidate graph. No diagnosis needed.
- Runtime classpath (`dependency:build-classpath -Dmdep.includeScope=runtime`, `runtime-classpath.txt`): the 11 runtime jars above. Build and plugin dependencies are not on it.
- Effective POM: `maven-consumer/effective-pom.xml` (`help:effective-pom`, log `effective-pom.log`).
- Plain-named copies: tested. Maven resolves the candidate with timestamped files only (`candidate-maven-timestamped-only`, `maven-timestamped-only.log`, BUILD SUCCESS, same tree). The plain-named SNAPSHOT copies are not needed by Maven; they remain only as round 2 Gradle layout evidence.
- Execution, Java 17, external JVM, runtime = `target/classes` plus the resolved runtime only:
  - Offline: `java -ea -cp target/classes:<runtime> org.apache.solr.compat.StableConsumer` (no arguments) → `PASS ordinary SolrJ query/update/response/date consumer`, rc 0 (`run-offline.out`).
  - HTTP against Solr 10.0.0 standalone (Java 21), disposable core `mvn_compat` (`_default`, `id` uniqueKey), `StableConsumer http://localhost:8983/solr mvn_compat` → `PASS HTTP CoreAdminResponse startTime Date and uptime Long conversion`, `PASS HTTP add/commit/query/delete`, `PASS ordinary SolrJ query/update/response/date consumer`, rc 0 (`run-http.out`). Core unloaded with `deleteIndex=true&deleteInstanceDir=true` (status 0). Solr stopped.
- Limit: this Maven consumer is a source and publication check for the candidate. It does not replace the released-binary leg, which remains `binaryHttp` FAILED (round 2 and the binary attribution control).

## 2. Discriminating mutations

Setup: detached worktrees from the candidate head `8bfca8edd30acf11bdf9d6a733312e97f99ec78a` (`/workspace/worktrees/s18325-control`, `s18325-mut-defaults`, `s18325-mut-constraints`, `s18325-mut-errors`). Test bytes identical to the candidate. Blob hashes at the fixed head:

| File | Blob |
| --- | --- |
| `solr/solrj/src/test/.../request/json/JacksonContentWriterTest.java` | `6af6135687ce96d166c6d27cd22817413f8a59ea` |
| `solr/solrj/src/test/.../response/json/JacksonDataBindResponseParserTest.java` | `3df2a7b98925c551ab40b82d8875bfb1dd82d94b` |
| `solr/solrj/src/test/.../response/CoreAdminJacksonCompatibilityTest.java` | `297afc03ea25fb676a59a5a273dc12bf3554998f` |
| `solr/solrj/src/java/.../request/json/JacksonContentWriter.java` (production, unmutated) | `2bb1993cbda1da96107f2d06957f347558b91626` |
| `solr/solrj/src/java/.../response/json/JacksonDataBindResponseParser.java` (production, unmutated) | `d89287bbc23fd5a479a834a4aa33ded770746951` |

Command for each leg (under `/workspace/tools/.gradle-vm2.lock`, JDK 21, `GRADLE_USER_HOME=/workspace/gradle-home`, `NODE_ENV` unset, `--no-daemon`; a driver script `mutants/run-all.sh` and `mutants/run-extra.sh`):

```sh
./gradlew --no-daemon :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.client.solrj.request.json.JacksonContentWriterTest --tests org.apache.solr.client.solrj.response.json.JacksonDataBindResponseParserTest --tests org.apache.solr.client.solrj.response.CoreAdminJacksonCompatibilityTest -Ptests.seed=1832510C0FFEE -Ptests.workDir=/tmp/s18325-closeout/<leg> --no-build-cache
```

Mutation diffs: `mutant-defaults.diff`, `mutant-constraints.diff`, `mutant-errors.diff`, `constraints-string-diff.txt`; the scratch-run copies are in `mutants/<leg>-diff.txt`. Mutants were never committed or pushed.

| Leg | Mutation | Tests run | Failed | Result |
| --- | --- | --- | --- | --- |
| control | none (fixed head `8bfca8e`) | 15 | 0 | BUILD SUCCESSFUL in 4m 56s, rc 0 |
| defaults | `builderWithJackson2Defaults()` → `builder()`; `configureForJackson2()` removed | 15 | 3 | rc 1 (intended) |
| constraints (compound) | Jackson 2 read/write depth 1000 and read string 20M overrides removed | 15 | 3 | rc 1 (intended) |
| constraints-string (split) | only `maxStringLength(20_000_000)` removed | 15 | 1 | rc 1 (intended) |
| errors | IOException and IllegalArgumentException wrapping bypassed (writer, parser, both convertValue) | 15 | 5 | rc 1 (intended) |

Failures, from fresh JUnit XML (`mutants/<leg>-xml/`):

- defaults:
  - `JacksonContentWriterTest.testJackson2RequestRepresentationAndStreamOwnership`: `ArrayComparisonFailure`, request body 176 bytes versus 133 expected, first differing byte at index 13.
  - `JacksonContentWriterTest.testSerializationFailureRemainsIOException`: no exception. Verified separately: the Jackson 3 default mapper writes `new Object()` as `{}`, while the Jackson 2 configuration throws `InvalidDefinitionException`.
  - `JacksonDataBindResponseParserTest.testMalformedInputRemainsIOException`: no exception. The test input `{"unknown":1}` is accepted by the Jackson 3 default mapper and rejected by the Jackson 2 configuration (`UnrecognizedPropertyException`), verified separately.
- constraints (compound):
  - `JacksonContentWriterTest.testReadConstraintsMatchJackson2`: `expected:<1000> but was:<500>`. The depth assertion stops the test before the string-length assertion.
  - `JacksonContentWriterTest.testNestedRequestRetainsJackson2DepthLimit`: `Document nesting depth (501) exceeds the maximum allowed (500)`.
  - `JacksonDataBindResponseParserTest.testNestedResponseRetainsJackson2DepthLimit`: same depth failure on the parser.
  - Split required: the compound mutant does not show the string-length boundary, because the depth assertion fails first.
- constraints-string (split): `JacksonContentWriterTest.testReadConstraintsMatchJackson2`: `expected:<20000000> but was:<100000000>`. The string-length boundary is detected once depth is not masking it. The Jackson 3 default is 100M; the Jackson 2 configuration keeps 20M.
- errors:
  - `testOutputFailureRemainsIOException`: `tools.jackson.core.exc.JacksonIOException` leaked.
  - `testSerializationFailureRemainsIOException`: `InvalidDefinitionException` leaked.
  - `testInputFailureRemainsIOException`: `JacksonIOException` leaked.
  - `testMalformedInputRemainsIOException`: `UnexpectedEndOfInputException` leaked.
  - `CoreAdminJacksonCompatibilityTest.testInvalidCoreStatusRemainsArgumentError`: `InvalidFormatException` leaked where `IllegalArgumentException` is required.

Every mutant executed its intended assertions and failed for its intended contract difference. No compile or setup failure was counted as detection.

Setup records (not failures of the product):
- The first control run failed because its worktree was still being populated (`solr/test-framework` missing). Rerun on the complete worktree: 15 of 15 pass (`control-rerun`, rc 0). Only the rerun is evidence.
- The gradle wrapper jar is gitignored. Gradle fetched `gradle-wrapper.jar` for the first worktrees from GitHub; its SHA-256 `2db75c40782f5e8ba1fc278a5574bab070adccb2d21ca5a6e5ed840888448046` matches `gradle/wrapper/gradle-wrapper.jar.sha256` in the candidate. The later worktrees received a copy of the candidate jar with the same hash.

No prior round 2 run was reused for the mutant comparison; the positive control is a fresh run of the same three suites at the fixed head.

## 3. Full artifact API comparison

Tools:
- japicmp `0.26.5` (`com.github.siom79.japicmp:japicmp`, jar-with-dependencies, verified against the Central `.sha1`: `japicmp-0.26.5-jar-with-dependencies.jar: OK`). Commands: `-a protected --include-synthetic --old-classpath ... --new-classpath ... -o <old> -n <new> -x <xml>`, outputs in `api/out/`.
- javap inventory (JDK 17.0.21): `javap -protected -s` over every class in every jar (`api/inventory.py`, `api/inventory.json`), diffed by name and descriptor (`api/diff-inventory.py`).

Artifacts (verified):

| Artifact | SHA-256 | Source |
| --- | --- | --- |
| Released solr-solrj 10.0.0 | `59178b49e43ffed3d4845b73c0b3302bd77e1b0e3c10c5c4550ba8499bdb3fac` | Maven Central, SHA-512 `.sha512` verified |
| Released solr-api 10.0.0 | `eba144a935fb4a200420a302c50e22b90d779f86281a0de73c1ba1173d576aee` | Maven Central, SHA-512 verified |
| Base solr-solrj `10.2.0-20261027.170400-1` | `8cc4e4abd0519dd7e7e785413a3a49d2edf5a43b441ff8ca02275d7a03d51b15` | base publication, `base-publication-sha256.txt` |
| Base solr-api `10.2.0-20261027.170400-1` | `130aec9a5999d87381ebd152ad9802c148a38931d9adbb683ed5b11fe44ec308` | base publication |
| Candidate solr-solrj `10.2.0-SNAPSHOT` (`20261027.074222-1`) | `e9a4236a75bf711f92c99266ea145f93ce0d2882e6bf04449c8c73bf8db7be3c` | round 2 `r2/artifact-sha256.txt` |
| Candidate solr-api `20261027.074222-1` | `2bef835d1607847577b5ff775d2bb9b1ae0b5d603b7a2a349b66887f1e36d064` | candidate build-local repo |

Source: released tag `releases/solr/10.0.0` peels to commit `6c6c48a6f78486130682ea9c1f7a2723af5a87be`. Base `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`; contains SOLR-17968 commit `e5340a954b6` (verified ancestor). Candidate `8bfca8edd30acf11bdf9d6a733312e97f99ec78a` differs from base in 48 files; `git diff --stat` for `solr/solrj/src/java/.../client/solrj/impl` is empty (0 lines).

Attribution: released to base is base evolution (SOLR-17968 and other changes between 10.0.0 and base). Base to candidate is the Jackson delta.

### japicmp summary (`-a protected`, synthetic included)

| Pair | Module | Entries | Binary-incompatible | Source-incompatible |
| --- | --- | --- | --- | --- |
| released → base | solr-solrj | 959 | 480 | 480 |
| base → candidate | solr-solrj | 4 | 1 | 1 |
| released → candidate | solr-solrj | 963 | 481 | 481 |
| released → base | solr-api | 394 | 11 | 11 |
| base → candidate | solr-api | 0 | 0 | 0 |
| released → candidate | solr-api | 394 | 11 | 11 |

Base → candidate, all entries (`api/out/solrj-base-cand.txt`, XML `.xml`):
- `FIELD_TYPE_CHANGED` `JacksonContentWriter.DEFAULT_MAPPER` (`ObjectMapper` Jackson 2 → Jackson 3). Binary and source incompatible. This is the documented internal mapper negative control.
- `METHOD_ADDED_TO_PUBLIC_CLASS` ×3 on `JacksonContentWriter`: `asIOException(JacksonException)` and two `convertValue` overloads. Additive, binary and source compatible.

javap-only: `CoreAdminResponse$1` (an anonymous class, skipped by japicmp) changed its superclass from Jackson 2 `TypeReference` to Jackson 3 `TypeReference`. It is anonymous, so no direct consumer should reference it. It is an internal binary change to report.

javap member diff, base → candidate (`inventory-diff.json`): removed 2 (the anonymous class declaration and `DEFAULT_MAPPER`), added 5 (the anonymous class with the Jackson 3 superclass, `DEFAULT_MAPPER`, three `JacksonContentWriter` methods). Solr-api: 0 removed, 0 added.

Protected members: base to candidate has no protected member change in japicmp (0 protected entries) and none in the javap diff. Released to base has protected member removals in the HTTP hierarchy; they are not Jackson-related (see the design report).

### released → base (inherited, not Jackson)

- javap: removed 314 members and declarations, added 760.
- The HTTP base types account for 105 of the 281 removed members (`HttpSolrClientBuilderBase` 47, `HttpSolrClientBase` 34, plus the references listed in the design report).
- javap inherited-member diff: 134 classes lose the inherited `SolrRequest.processWithBaseUrl(HttpSolrClientBase, String, String)`, replaced in base by the `HttpSolrClient` overload. japicmp reports 245 class-level `METHOD_REMOVED_IN_SUPERCLASS` entries in total; the 134 is the subset explained by that one method.
- The other 176 removed members are unrelated to HTTP types: `NamedList` and `NamedList$1/$2`, `MapWriter`/`MapWriterMap`, `ConditionalMapWriter`, `Cache`/`MapBackedCache`, `Template`, `Callable`, `JavaBinCodec$StringCache`, `QueryingApi$Query`, and others.
- solr-api released → base: 11 incompatible entries (DTO and endpoint interface changes, for example `CreateCollectionSnapshotResponse` and the `*Api` endpoint interfaces; `UpsertFieldTypeOperation.className` field). Not HTTP-related.
- Unrelated non-HTTP but in `impl`: `ConcurrentUpdateBaseSolrClient.doSendUpdateStream` returns `SentStream` instead of `StreamingResponse` (a separate ConcurrentUpdate refactor).

## 4. What is and is not established

- Jackson-specific (relative to exact base): the public SolrJ delta is three additive methods, one internal mapper field type change (documented), and one anonymous-class superclass change. The solr-api artifact is unchanged. The three focused suites pin the Jackson 2 contract: request body, depth and string-length limits, exception types. Each mutation of those settings is detected (section 2). Positive control passes.
- Inherited (release to base): the HTTP client surface that `binaryHttp` uses (builder, base class, return and field types, and `SolrRequest` overload) is incompatible with released 10.0.0 binaries. The defect is in base, not in Jackson. Other non-HTTP base evolution also breaks released binaries (section 3).
- Absolute released-consumer result: released 10.0.0 binary passes `binaryHttp` (positive control). Base and candidate fail at `HttpJdkSolrClient$Builder.withConnectionTimeout` before any HTTP operation. So `binaryHttp` still FAILS.
- The client-first verdict remains INCOMPLETE.

## 5. Not run, or limits

- Maven consumer executed against the candidate only. The released-binary leg is Gradle-built, as in round 2 (old class bytes), not a Maven execution.
- No new test was added in the candidate. The four core Jackson 2 oracle tests stayed as confirmed (`new ObjectMapper()`).
- SOLR-18587 remains the inherited unload failure; not fixed or suppressed here.
- The javap member diff checks names and descriptors, not access-rule or behavior changes beyond modifiers. japicmp is the authority for compatibility classification.
- Full server migration, 3.1 LTS target and release readiness: not established.
