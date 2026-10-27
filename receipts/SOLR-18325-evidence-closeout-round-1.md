# Receipt: SOLR-18325 evidence closeout (vm2, Linux, round 1)

Gating host: vm2. Job: `gates/solr-18325-evidence-closeout-round-1.md` (fork commit `8db594649cf`). Claim: `claims/solr-18325-evidence-closeout-linux-vm2-round-1.md` (board `7c0e78f9aab`). Report: `reports/solr-18325-evidence-closeout-linux-round-1.md`. Design: `reports/solr-18325-http-compatibility-prerequisite-design-round-1.md`.

## Verdict

- Client-first verdict: INCOMPLETE, unchanged. `binaryHttp` still FAILS. Released 10.0.0 binaries cannot link the candidate builder (`NoSuchMethodError` on `HttpJdkSolrClient$Builder.withConnectionTimeout`), inherited from base (SOLR-17968).
- Jackson-specific: the candidate passes its own Maven consumer and all three Jackson suites at the fixed head (15 of 15). Each Jackson 2 compatibility setting, constraint and exception boundary is detected by a failing mutant. The public SolrJ delta vs base is three additive methods, the documented `DEFAULT_MAPPER` field type change, and one anonymous class superclass change. solr-api is unchanged.
- No production shim, restoration, requirement waiver, GitHub/JIRA/upstream write, PR, or fork push. No commit. Candidate and base trees unchanged.

## Heads and sources

- Candidate `nick-boss-tech/solr:solr-18325-submit` at `8bfca8edd30acf11bdf9d6a733312e97f99ec78a` (verified: `git rev-parse HEAD` in `/workspace/gates/solr18325/cand`, clean tree).
- Base `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7` (verified clean tree; contains SOLR-17968 `e5340a954b6`).
- Released: tag `releases/solr/10.0.0` peels to `6c6c48a6f78486130682ea9c1f7a2723af5a87be`; Maven Central artifacts verified against published SHA-512.
- Candidate vs base: 48 files changed; `solr/solrj/src/java/.../client/solrj/impl` 0 lines changed.

## Tools and versions

- Apache Maven 3.9.9 (`8e8579a9e76f7d015ee5ec7bfcdc97d260186937`), SHA-512 verified from `archive.apache.org`, unpacked at `/workspace/tools/maven-user/apache-maven-3.9.9`. Java for Maven: 17.0.21 (Eclipse Adoptium). Maven plugin `maven-dependency-plugin:3.7.0`.
- JDK 17.0.21 (`/workspace/jdk/jdk17`): consumer compile target `--release 17` (class major 61), consumer runs, javap.
- JDK 21.0.12.1+1 (`/workspace/jdk/jdk-21.0.12.1+1`): Solr 10.0.0 standalone, Gradle runs.
- Gradle 9.7.0 (wrapper), `--no-daemon`, `NODE_ENV` unset, `GRADLE_USER_HOME=/workspace/gradle-home`, build lock `/workspace/tools/.gradle-vm2.lock` on every Gradle run.
- japicmp 0.26.5 (jar-with-dependencies, Central `.sha1` verified).

## Maven consumer (actual Maven execution)

- Settings: `maven-consumer/settings.xml` (isolated `m2-candidate`, candidate file repository, Central). POM: `maven-consumer/pom.xml`, one dependency `org.apache.solr:solr-solrj:10.2.0-SNAPSHOT`.
- Commands and exits:
  - `mvn -B -s settings.xml -f pom.xml dependency:tree -Dverbose` → rc 0 (`maven-dependency-tree.txt`).
  - `mvn -B -s settings.xml clean compile dependency:build-classpath -Dmdep.outputFile=runtime-classpath.txt -Dmdep.includeScope=runtime` → rc 0 (`maven-compile.log`).
  - `help:effective-pom` → rc 0 (`effective-pom.xml`).
  - Timestamped-only repo (no plain-named copies) `dependency:tree` → rc 0 (`maven-timestamped-only.log`).
  - Java 17 offline run: `java -ea -cp target/classes:<runtime> org.apache.solr.compat.StableConsumer` → rc 0, `PASS ordinary SolrJ query/update/response/date consumer` (`run-offline.out`).
  - Java 17 HTTP run against Solr 10.0.0 standalone, core `mvn_compat`: `StableConsumer http://localhost:8983/solr mvn_compat` → rc 0, three PASS lines (`run-http.out`).
- Resolved candidate graph (Maven): solr-solrj and solr-api `10.2.0-SNAPSHOT` (timestamp `20261027.074222-1`), jackson-core and jackson-databind `3.2.1`, jackson-annotations `2.22`, slf4j `2.0.20`, jcl-over-slf4j `2.0.20`, swagger-annotations-jakarta `2.2.54`, jakarta.ws.rs-api `4.0.0`, semver4j `6.0.0`, jspecify `1.0.0`. No Jackson 2 core or databind on the runtime path. Same as the Gradle candidate graph.

## Artifact hashes (complete, SHA-256)

- Released solr-solrj 10.0.0 `59178b49e43ffed3d4845b73c0b3302bd77e1b0e3c10c5c4550ba8499bdb3fac`
- Released solr-api 10.0.0 `eba144a935fb4a200420a302c50e22b90d779f86281a0de73c1ba1173d576aee`
- Base solr-solrj `10.2.0-20261027.170400-1` `8cc4e4abd0519dd7e7e785413a3a49d2edf5a43b441ff8ca02275d7a03d51b15`; POM `5f21794d4ef04b5931adfe11ae250db10413355a8edad41363983db9e73ecd35`
- Base solr-api `10.2.0-20261027.170400-1` `130aec9a5999d87381ebd152ad9802c148a38931d9adbb683ed5b11fe44ec308`
- Candidate solr-solrj `10.2.0-SNAPSHOT` `e9a4236a75bf711f92c99266ea145f93ce0d2882e6bf04449c8c73bf8db7be3c`; POM `6e6f07ebc61bec034aea9216bc35ad0b60c85613c34b1f9033a4bcf7da7ed041` (round 2 record matched)
- Candidate solr-api `20261027.074222-1` `2bef835d1607847577b5ff775d2bb9b1ae0b5d603b7a2a349b66887f1e36d064`
- Old consumer `StableConsumer.class` (released 10.0.0 compiled, round 2) `027d21fdb1d79c133aab4e4bb4f109f9d17baa05b822c7457e1c9add44bdf18a`, unchanged after every run.

## Mutation runs (fixed-head test blobs, identical across legs)

Blobs at `8bfca8edd30`: `JacksonContentWriterTest.java` `6af6135687ce96d166c6d27cd22817413f8a59ea`; `JacksonDataBindResponseParserTest.java` `3df2a7b98925c551ab40b82d8875bfb1dd82d94b`; `CoreAdminJacksonCompatibilityTest.java` `297afc03ea25fb676a59a5a273dc12bf3554998f`.

Command (each leg, separate worktree, `cleanTest`, `--no-build-cache`):

```sh
./gradlew --no-daemon :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.client.solrj.request.json.JacksonContentWriterTest --tests org.apache.solr.client.solrj.response.json.JacksonDataBindResponseParserTest --tests org.apache.solr.client.solrj.response.CoreAdminJacksonCompatibilityTest -Ptests.seed=1832510C0FFEE -Ptests.workDir=/tmp/s18325-closeout/<leg> --no-build-cache
```

| Leg | Worktree | Tests | Failed | rc | Note |
| --- | --- | --- | --- | --- | --- |
| control (fixed head) | `s18325-control` | 15 | 0 | 0 | rerun; first attempt rc 1 on an incomplete worktree (setup, not evidence) |
| defaults | `s18325-mut-defaults` | 15 | 3 | 1 | intended: request bytes, empty-bean IOException, unknown-property IOException |
| constraints (compound) | `s18325-mut-constraints` | 15 | 3 | 1 | intended depth 500 vs 1000 failures; string masked by first assertion |
| constraints-string (split) | `s18325-mut-constraints` | 15 | 1 | 1 | intended: string 100M vs 20M |
| errors | `s18325-mut-errors` | 15 | 5 | 1 | intended: leaked unchecked exceptions (writer, parser, convertValue) |

Mutant diffs: `mutant-defaults.diff`, `mutant-constraints.diff`, `mutant-errors.diff`, `constraints-string-diff.txt`, plus per-leg `<leg>-diff.txt`, `<leg>-head.txt`, `<leg>-status.txt`, Gradle logs, XML under `mutants/<leg>-xml/`. Mutants were never committed or pushed.

## API comparison (complete outputs)

- japicmp `-a protected --include-synthetic` outputs: `api/out/<module>-<pair>.{txt,xml}` for solr-solrj and solr-api, pairs released→base, base→candidate, released→candidate. rc 0 for all six runs.
- javap inventories: `api/inventory.json` (6 artifacts), diffs `api/inventory-diff.json`.
- Summary: solr-solrj base→candidate 4 japicmp entries (1 binary-incompatible: `DEFAULT_MAPPER` type; 3 additive). javap base→candidate: removed 2, added 5. solr-api base→candidate: 0.

## Design

Recommended prerequisite: additive hierarchy (restore top-level `HttpSolrClientBase` and `HttpSolrClientBuilderBase` as superclasses of the current implementation, with released bounds and erasure) plus restoration of eight declared return and field types that javac cannot bridge (section 4 rows 1–4, 10, 11), three generic bounds (rows 7–9), and three released overloads (`withHttpClient(HttpSolrClientBase)`, `withHttpClientBuilder(HttpSolrClientBuilderBase<?, ?>)`, `SolrRequest.processWithBaseUrl(HttpSolrClientBase, …)`). Stub-level javac evidence confirms builder descriptor resolution and the `build()` bridge. Owner decisions listed in the design report, section 9.

## Not established

- Released-binary compatibility of the HTTP client path: NOT established. `binaryHttp` FAILS.
- Released-binary compatibility of all of SolrJ and solr-api: NOT established (176 unrelated removed members, 11 solr-api breaks, ConcurrentUpdate return type).
- Full server migration, 3.1 LTS target, release readiness: NOT established.
- SOLR-18587 (CoreAdmin unload failure) remains inherited, not green, not fixed here.
- Maven execution is for the candidate only; the released-binary leg is the Gradle-built old class (as in round 2).

## Raw artifacts

`/workspace/gates/logs/vm2/solr18325-closeout/` (`maven-consumer/`, `mutants/`, `api/`, `design-proto/`, `claim.md`, `report.md`, `design.md`, `receipt.md`). Earlier binary attribution artifacts: `/workspace/gates/logs/vm2/solr18325-binattr/`.
