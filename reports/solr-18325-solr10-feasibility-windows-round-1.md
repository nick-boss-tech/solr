# SOLR-18325 Solr 10 feasibility prototype

Solr 10 is a plausible target for a bounded SolrJ migration. A source scan found no unavoidable supported SolrJ signature change; the prototype moves the client to Jackson 3 while preserving the server's Jackson 2 API. This is a testable proposal, not a green compatibility verdict or completion of the full server upgrade.

The earlier impact report's Solr 11 recommendation was conservative. It did not establish that Solr 10 was technically impossible. This follow-up supersedes any reading of that recommendation as a blocker.

## Baseline and scope

- Direct user request: determine whether Solr 10 is viable, scan the code, prepare something concrete and hand Linux proof to the gate agent.
- Source base: freshly fetched apache/solr `branch_10x`, `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`, in-progress 10.2.0. The initial report examined main; this candidate actually starts from Solr 10.
- Source branch: fork `solr-18325-submit`, local `wt/SOLR-18325`. Pushed and remote-verified head: `aa8cc9ab29a012da2ec4e302cae73238e7e78a1f`. Implementation commit `4060e97a971` is followed by a probe-catalog correction: shared annotations use version `2.20`, while databind uses `2.20.0`.
- Scope: client runtime dependencies, its internal mapper and typed-response exception translation, core/system-info conversion, the CLI's dependency on that internal mapper, regression tests, and an isolated consumer harness.
- Server Jersey/provider/serializer APIs and server JSON/Smile/CBOR implementations stay on Jackson 2. This approach intentionally permits both runtimes inside the server. Its value is removing Jackson 2 core/databind from a clean SolrJ application's runtime.
- Publication is experimental. There is no release changelog or public PR. Linux preparation must generate the correct dependency locks and license/checksum changes before an exact-head validation run.

## Contract scan

At the pinned base, only `JacksonContentWriter` and `CoreAdminResponse` directly import Jackson core/databind in SolrJ production sources. `SystemInfoResponse` and `JacksonDataBindResponseParser` use the mapper indirectly.

`JacksonContentWriter.java:26` is `@lucene.internal`. Its `DEFAULT_MAPPER` field at line 30 has a Jackson 2 `ObjectMapper` JVM descriptor. Moving that field to Jackson 3 is a real binary break for callers of this internal field; the probe explicitly expects `NoSuchFieldError` when old bytecode accesses it with both runtimes present. This must be disclosed, not counted as a supported-API pass.

`CoreAdminResponse.java:30-45` exposes model maps, `Date` and `Long`, with Jackson TypeReference confined to implementation. `SystemInfoResponse` similarly exposes Solr types. The typed JSON parser is marked experimental at `JacksonDataBindResponseParser.java:35`, and its public method takes a stream/encoding and returns NamedList while declaring IOException. None of these signatures requires a Jackson package change.

SolrJ's Java floor is 17 (`gradle/libs.versions.toml:125`), distinct from the server's Java 21 floor at line 123. Jackson 3's Java 17 baseline fits both. The Linux harness selects Java 17 for runtime execution; Java 17 bytecode alone is insufficient proof.

The [Solr downloads policy](https://solr.apache.org/downloads) says large and breaking changes normally go into the next major version. [Commit Process Guidelines](https://cwiki.apache.org/confluence/display/SOLR/Commit%2BProcess%2BGuidelines) recognize internal and experimental surfaces. These support inspecting the actual contract; they do not grant universal compatibility to every public class or automatically prohibit this client dependency change in a minor release. Maintainers still decide release policy.

A full server migration raises separate binary compatibility questions: `SolrJacksonMapper` exposes a Jackson resolver and mapper return types; `NullKeyTolerantMap` implements a Jackson interface; `SolrJacksonAnnotationInspector` extends a Jackson class; `JacksonJsonWriter` and `SmileResponseWriter` expose protected Jackson fields or generator constructor arguments. The inspected declarations have no internal/experimental marking. Public visibility alone does not settle supported status, but their descriptors/supertypes would change. This prototype leaves those boundaries intact.

## What the candidate changes

- SolrJ uses the existing tools.jackson 3.2.1 BOM/core/databind aliases. Shared `com.fasterxml.jackson.annotation` stays in place. Jackson 2 is explicit test-only oracle/server support in SolrJ's own tests, not a client runtime requirement.
- The internal immutable mapper uses Jackson 2 compatibility settings and explicitly preserves read nesting 1000, write nesting 1000, and string length 20,000,000. Source inspection confirmed the compatibility builders do not restore these constraints: the Jackson 3.2.1 built-ins are 500/500/100,000,000.
- ContentWriter and typed parser adapt unchecked Jackson exceptions to broad IOException, retaining the Jackson exception as cause for path/location and suppressed-failure diagnostics. Exact former Jackson 2 exception subclasses are not preserved.
- Core and system-info value conversion retain broad IllegalArgumentException failures rather than leaking the new Jackson runtime exception type. Request defaults for dates, enums, property order, empty beans and unknown properties are covered by regression/oracle tests.
- The core CLI gets its own Jackson 2 mapper and keeps its protected Jackson 2 exception signature. Four core tests use Jackson 2 directly as an independent oracle instead of consuming SolrJ's internal field.

The prototype reuses 3.2.1 because the central platform constrains every catalog alias, including that existing Jackson 3 BOM required for JWT tests. Adding a second alias for 3.1.8 would not isolate production at 3.1.8; normal conflict resolution can select 3.2.1. Choosing a production LTS line and testing JWT dependency requirements remains a follow-up. This candidate tests the Solr 10 migration architecture, not the exact 3.1.x release target in the ticket.

## Evidence added

`JacksonContentWriterTest` compares request bytes and stream closure/flushing with a fresh Jackson 2 mapper, including dates, enum name/toString differences, null fields, shared annotations, arrays and nested data. It tests output/serialization IOException behavior, read-constraint parity and a 600-level write canary.

`JacksonDataBindResponseParserTest` retains existing success/v2-error extraction tests and adds malformed/truncated/unknown-field IOException cases in both encoding branches, UTF-16LE input, input-stream failures and a 600-level read canary. `CoreAdminJacksonCompatibilityTest` covers public date/numeric conversion, absent cores and invalid conversion errors. These are unrun compatibility pins, not claimed failing-before bug regressions.

`dev-tools/jackson3-solrj` is an independent build consuming published candidate POMs/JARs, not project substitution. It blocks Maven Central from supplying the candidate Solr version and does not exclude or force away Jackson 2. It records the actual resolved runtime graph and fails if Jackson 2 core/databind remains. Shared annotations are required and expected.

The harness compiles an ordinary consumer independently against released SolrJ 10.0.0, then runs those unchanged class bytes against the candidate runtime and records their SHA-256. Separate baseline/candidate behavioral lanes exercise the internal writer and experimental parser without extending the supported-API claim. Other lanes check application-owned Jackson 2 coexistence and the expected internal-field binary break. HTTP lanes require an unchanged Solr 10 disposable core and exercise real client/server requests.

This branch disables GenerateModuleMetadata (`gradle/maven/defaults-maven.gradle:63`), so a `.module` file is not an expected artifact. Linux should inspect the POM and resolve it with Maven as well as the supplied Gradle consumer.

## How the viability decision will be made

| Finding after Linux proof | Permitted conclusion |
| --- | --- |
| Candidate does not compile, metadata retains Jackson 2, or supported consumer calls fail | No viability claim. Diagnose the concrete failure and either repair or document a blocker. |
| Build, behavioral comparisons, ordinary old bytecode, Java 17 runtime, coexistence and baseline-server HTTP all pass | Client-first Jackson 3 migration is technically viable for Solr 10 within the measured contract, subject to maintainer policy and release preparation. |
| Only the internal-field control fails with its expected NoSuchFieldError | Known internal incompatibility is confirmed; it is not a surprise supported-API failure or proof of universal compatibility. |
| Client-first proof passes while server remains Jackson 2 | The downstream duplication goal is supported. The full server upgrade and complete ticket scope remain open. |
| A full server conversion requires an incompatible supported plugin API change | That larger slice needs a compatibility design or a major-release decision. Do not infer it from the client-only result. |

The named Linux job covers dependency/license/format preparation, fixed-head tests, artifact consumers, old-bytecode controls, Java 17/21 execution and HTTP. The final receipt must link logs, XML, class hashes, dependency graphs and actual tested/pushed SHAs. Any preparation/source repair must be committed and reflected in a superseding exact-head gate before treating tests as final proof.

## Current state

Implemented, Proof Pending. No Gradle, compilation, Java test, local test queue or HTTP proof ran on Windows. Static review, source/API checks, `git diff --check`, clean ticket-worktree status and remote-head read-back are the local checks. The workspace-wide sync report did not finish and was interrupted; it is not evidence about other worktrees. No public JIRA/GitHub comment, PR or upstream branch write was made.

The claim's approximate 03:28 UTC Started line is not an authoritative start time and is later than its actual publication; use the claim commit `7f1520e90ed37ff8ddb746b78c9f6233d4be6a0d` timestamp instead. The claim is immutable, so this report records the correction.
