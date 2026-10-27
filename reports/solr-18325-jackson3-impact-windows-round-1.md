# SOLR-18325 Jackson 3 upgrade impact analysis

Recommend targeting Solr 11, with a coordinated client and server migration and explicit compatibility tests. Ordinary SolrJ applications should have a relatively small source migration if they use only Solr APIs. Applications using Jackson-specific extension points, custom JSON mappings, or server plugins face a materially larger change. Updating coordinates alone is insufficient.

Status: impact analysis complete; upgrade not implemented or validated. This is a source-based assessment, not an estimate of how many downstream applications will break.

## Scope and evidence

Analysis requested by Nick on 2026-10-26 America/New_York. [SOLR-18325](https://issues.apache.org/jira/browse/SOLR-18325) is Open, last updated 2026-08-27. Bernd Wahlen reports a Boot 4 application bundling Jackson 3.1.5 and Jackson 2.21.5 through SolrJ 10.0.0; the approximately 2.4 MB duplication is his measurement, not a measurement from this analysis. Eric Pugh asks about consumer impact and suggests Solr 11 rather than Solr 10. The description also proposes 11.0.

Source: fetched apache/solr main at `e59ce3c97ddbe030eafadf0623fdcd80c4529c31`. Release comparison: local `releases/solr/10.0.0` tag. The ticket's dependency baseline is accurate for 10.0.0: its committed SolrJ lockfile has databind/core 2.20.0. Current main has 2.22.3, annotations 2.22, Jersey 4.0.2, Swagger 2.2.54, and a separate Jackson 3.2.1 test dependency for mock-oauth2-server. The upgrade remains outstanding despite these version changes.

Board reviewed at `69c92fd07cd7bb53875bf26a9991c0197331c2e9`; its earlier day-scan receipt calls this an unclaimed impact-analysis candidate. No matching assignment, claim, gate or report existed in the searched board directories. No PR matching 18325 was returned by a GitHub all-state PR search. This does not rule out work under another title. The direct user assignment was recorded in `claims/solr-18325-impact-windows-round-1.md`, commit `d5dcb8671df6fdc363f780eef214d5c5e1ca1cd7`. Correction to that immutable claim: its 03:15 UTC start line is a transcription error; the claim was published at 2026-10-27 03:04:40 UTC.

## Consumer impact

| Consumer | Expected impact | Reason and action |
| --- | --- | --- |
| Uses SolrJ query/update APIs without accessing Jackson objects | Lower source risk; dependency graph changes | Solr request/response abstractions need not change their signatures. Test existing application behavior with the new runtime. This is not a blanket binary-compatibility claim for Solr 10 to 11. |
| Boot 4 or another Jackson 3 application | Benefit if SolrJ is its remaining Jackson 2 source | The SolrJ runtime dependency can move to Jackson 3. Other application dependencies can still retain Jackson 2. |
| Jackson 2 application upgrading SolrJ | May acquire both runtimes | The duplication reverses direction. Its own Jackson 2 use does not automatically migrate when SolrJ changes. |
| Uses Solr API model POJOs with an application mapper | Mostly reusable annotations; behavior must be checked | Shared `@JsonProperty`, `@JsonAlias`, `@JsonFormat`, etc. retain their package. Date, enum, null, creator and property-order behavior may differ with the mapper. |
| Uses `JacksonContentWriter.DEFAULT_MAPPER` or its mutable configuration | Source and binary break at this field | The public static field is a Jackson 2 `ObjectMapper`; replacing its type changes its JVM field descriptor. Existing module registrations and mutable configuration need a new design. Its enclosing class is explicitly `@lucene.internal`, so this exposure is not evidence of a supported stable extension API. |
| Uses typed JSON response parsing or custom POJOs | Behavior and exception-handling risk | The parser reads through that shared mapper. Test malformed input, dates, unknown fields and application-specific annotations/serializers. |
| Server extension using Solr Jackson helpers, serializers or binary-format classes | Recompile and adapt against Jackson 3 | Provider generics, return types, protected fields and constructor arguments expose Jackson 2 types. A Jackson 2 serializer cannot be registered as a Jackson 3 serializer. |
| HTTP client written in another language | No necessary package-level impact | Java coordinates do not alter the HTTP contract. JSON/CBOR/Smile compatibility must be maintained separately. |

Concrete client evidence at the pinned source:

- [JacksonContentWriter.java, lines 25 to 40](https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/solrj/src/java/org/apache/solr/client/solrj/request/json/JacksonContentWriter.java#L25) exposes and uses the mapper. The same field exists in the 10.0.0 tag.
- [JacksonDataBindResponseParser.java, lines 64 to 84](https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/solrj/src/java/org/apache/solr/client/solrj/response/json/JacksonDataBindResponseParser.java#L64) reads typed JSON, then converts v2 error information into the top-level response. It declares `IOException`; Jackson 3 failures require an explicit decision on preserving this error contract.
- [CoreAdminResponse.java, lines 30 to 45](https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/solrj/src/java/org/apache/solr/client/solrj/response/CoreAdminResponse.java#L30) converts status data into typed models, including a `Date`. `SystemInfoResponse` also uses the shared mapper without directly importing Jackson types.
- [SolrJ build dependencies](https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/solrj/build.gradle#L29) publish a Jackson BOM and runtime implementation dependencies. Both Maven POM and Gradle metadata need inspection after the change.

Jackson 2 and 3 have different coordinates and packages. Changing a version constraint or excluding Jackson 2 cannot make existing `com.fasterxml.jackson.databind` bytecode use `tools.jackson.databind`. Boot 4 supports coexistence, so this ticket addresses dependency duplication rather than an inherent inability to run SolrJ in Boot 4. [Spring's integration explanation](https://spring.io/blog/2025/10/07/introducing-jackson-3-support-in-spring/).

## Direct source and module scope

A tracked-Java search for Jackson core/databind/dataformat packages found 42 files: 25 production and 17 tests. Production imports occur in 23 core files and two SolrJ files; the API module has one directly importing test. Another search found 154 Java files referencing the shared annotation package; that is an annotation-bearing count, not 154 files requiring package replacement. The accompanying `SOLR-18325-source-inventory.txt` lists the direct files. Counts are a lower bound on review scope: indirect users such as the typed parser, `SystemInfoResponse` and `JerseyApplications` also matter.

Reproduce the direct-file inventory from any Solr checkout containing the revision with `git grep -l --extended-regexp 'com[.]fasterxml[.]jackson[.](core|databind|dataformat)|tools[.]jackson' e59ce3c97ddbe030eafadf0623fdcd80c4529c31 -- '*.java'`. The text inventory is a local workspace companion; the pinned command makes the board report independently reproducible.

| Area | Required migration or review |
| --- | --- |
| SolrJ | `JacksonContentWriter`, `CoreAdminResponse`, indirect mapper users and typed JSON parser tests. Decide whether mapper customization should remain exposed at all. |
| Jersey v2 APIs | `SolrJacksonMapper`, `MessageBodyReaders`, `JerseyApplications`, `NullKeyTolerantMap`, and exception mapping. Move provider and resolver together. |
| Serializers and annotations | Port NamedList/SimpleOrderedMap/null-key behavior and `SolrJacksonAnnotationInspector`; preserve Solr's own annotation semantics. |
| JSON/Smile/CBOR | Port factories, parser/generator APIs, pretty printing, feature flags and serializers in the response writers and `CborLoader`. |
| Admin and persisted/config data | Review auth CLI, package metadata/repositories, backup metadata, schema helpers, plugin configuration, query rate limiting and audit logs. |
| Build and distribution | Catalog/BOM entries, direct dependencies, platform constraints, lockfiles, dependency verification and license metadata. Resolve every affected distribution/module configuration. |
| API generation | Keep shared model annotations; separately handle the Jackson runtime used by Swagger's build configuration and compare generated OpenAPI output. |

The parser migration is substantive: [CborLoader.java, lines 46 to 87](https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/handler/loader/CborLoader.java#L46) constructs `ObjectMapper(new CBORFactory())`, uses a factory-associated parser, `FIELD_NAME`, and `getCurrentName()`. These are not all fixed by import replacement. Streaming multiple CBOR documents must keep working.

## Server integration and behavior risks

The highest-priority error-path finding is [AnnotatedApi.java, lines 290 to 294](https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/api/AnnotatedApi.java#L290): it maps `IOException` from Jackson parsing to a BAD_REQUEST response. A straight Jackson 3 replacement would let unchecked Jackson failures bypass this catch. Preserving invalid-payload HTTP status and error shape needs a regression. Audit JSON formatting similarly catches `IOException` at `AuditLoggerPlugin.java:370`.

[SolrJacksonMapper.java, lines 37 to 106](https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/jersey/SolrJacksonMapper.java#L37) has a Jackson 2 `ContextResolver<ObjectMapper>`, mutable mapper construction and custom serializers. Preserve NON_NULL inclusion, permissive unknown fields, plain NamedList conversion and direct SimpleOrderedMap serialization. `MessageBodyReaders.java:51` constructs a Jersey-shaded Jackson provider and permits comments. Merely changing the mapper's import leaves the provider on Jackson 2.

A concrete provider option now exists: [Jersey 4.0.3's Jackson 3 module](https://github.com/eclipse-ee4j/jersey/blob/4.0.3/media/json-jackson3/pom.xml) declares `jersey-media-json-jackson3`, depends on tools.jackson databind, and uses the relocated `org.glassfish.jersey.jackson3.internal.jackson.jakarta.rs` provider namespace. Its source was read through the GitHub Contents API. Solr main currently selects 4.0.2 and the Jackson 2 module. Assess a coordinated Jersey 4.0.3 upgrade/provider switch; compatibility of the new module with Solr's complete Jersey setup remains untested. Jackson's standalone Jakarta-RS provider is another integration option, not a proven drop-in replacement.

Swagger is a separate concern. `solr/api/build.gradle:47-79` places Swagger JAX-RS support on `swaggerBuild`, not the API artifact's ordinary runtime. Its lockfile shows Jackson 2 YAML, JSR310 and Jakarta-RS libraries on that build configuration. The official [Swagger Jackson 3 work](https://github.com/swagger-api/swagger-core/issues/4991) had a 3.0.0 snapshot under testing in its October 21 to 25 discussion. Do not declare a released compatible replacement from that evidence. Build-only Jackson 2 may remain isolated temporarily without defeating the SolrJ consumer goal.

Committed lockfiles show Jackson 2 databind in many optional module and server runtime configurations, including SQL and language-models. Their presence does not identify which dependency introduces them. A Linux dependencyInsight pass must distinguish inherited Solr dependencies from independent consumers before removing anything. A blanket exclusion can break a library still compiled for Jackson 2.

## Compatibility choices

The migration should preserve existing external behavior unless a separate change is deliberately documented. The [Jackson migration guide](https://github.com/FasterXML/jackson/blob/main/jackson3/MIGRATING_TO_JACKSON_3.md) establishes immutable mappers/factories, unchecked exceptions, renamed streaming and serializer APIs, format-specific mappers, and retention of shared annotations. Databind annotations such as `@JsonSerialize` belong to the relocated API and are not covered by the shared-annotation exception.

Test property ordering, date/enum representation, unknown/null fields, trailing data, parser constraints and stream ownership. Do not assume a compatibility builder restores every default. [Jackson 3.1 release notes](https://github.com/FasterXML/jackson/wiki/Jackson-Release-3.1) also document changed provider treatment of missing Media-Type and a 100M default maximum string length. Set or validate limits intentionally on relevant untrusted-input paths.

The same release page designates 3.1 as LTS and lists 3.1.8 released October 17. This supports the ticket's 3.1.x target, subject to provider requirements. Main already uses 3.2.1 for JWT tests, so choose one supported production line deliberately and reconcile that test BOM rather than blindly reusing it. Java 17 is Jackson 3's baseline; Solr's current Java 21 build baseline means this is not an additional Java floor for this branch. Jackson 2 also has supported LTS lines; no immediate universal Jackson 2 EOL deadline was established here.

## Implementation and proof plan

Treat client migration as medium scope and the complete server/module migration as high scope. These are engineering estimates from the source surface, not elapsed-time commitments. Stage changes for review on one migration branch, but keep the release deliverable aligned across client and server.

1. Establish dependency graphs for the published SolrJ artifact, API artifact, default server, each optional module and Swagger build. On Linux run `dependencyInsight --dependency jackson --configuration runtimeClasspath` for the relevant projects, plus `:solr:api:dependencyInsight --dependency jackson --configuration swaggerBuild`. Record why every remaining Jackson 2 core/databind artifact is present.
2. Migrate SolrJ and shared configuration deliberately. Test JSON request writing, typed responses and errors, core status dates, system info conversion and custom POJOs. Add a small published-artifact consumer fixture for a Jackson 3 application with Jackson 2 core/databind absent. Check a Jackson 2 application with both runtimes for coexistence. Verify field/API changes explicitly rather than promising Solr 10 binary compatibility.
3. Move Jersey provider, resolver, custom serializers, annotation inspector and exception translation as a unit. Preserve comments, missing/explicit content types, invalid JSON BAD_REQUEST, null handling, duplicate NamedList keys, nested maps and error bodies. Include `SolrJacksonMapperTest`, `JacksonReflectMapWriterTest`, `TestSolrJacksonAnnotation`, `PostRequestLoggingFilterTest`, and `SchemaChangeOperationSerializationTest`.
4. Migrate binary formats and admin/config consumers. Include `SmileWriterTest`, `TestCborDataFormat`, `TestRawResponseWriter`, `SolrExampleCborTest`, audit integration, package parsing, backup metadata and auth configuration. Cross-read old-produced and new-produced JSON/Smile/CBOR fixtures; compare semantic values as well as output contracts that depend on exact formatting. Preserve multi-document CBOR behavior and closure/flushing behavior.
5. Regenerate and inspect dependency locks, published metadata, license/verification files and OpenAPI output. Run the affected module suites, tidy and root `check -x test` on the exact candidate head using a named Linux job. Benchmark JSON/Smile/CBOR throughput and allocations if performance parity is claimed. No speed or memory improvement is established by this analysis.

Acceptance: a clean consumer of the published SolrJ artifact needs no Jackson 2 core/databind; retained shared annotations are expected. Existing Solr wire/data semantics pass compatibility tests, malformed input retains intended errors, provider registration is correct, generated schemas are reviewed, and any independently required Jackson 2 runtime is explicitly inventoried. A report alone does not close the upgrade ticket.

## Disposition

Proceed with release-scope discussion for Solr 11. The small direct SolrJ import count should not be mistaken for a safe Solr 10 dependency bump. Keeping a Jackson 2 compatibility facade would retain the runtime the ticket wants to remove, so such a compatibility project needs separate justification.

No source patch, Gradle execution or test queue action was performed on Windows. No public JIRA/GitHub comment was posted. This assessment changes no security boundary; implementation must preserve input limits, malformed-input classification and serialization controls. It is not a security certification.
