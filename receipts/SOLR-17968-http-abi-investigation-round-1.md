# Receipt: SOLR-17968 HTTP binary compatibility investigation, round 1

- Host: phone-agent. Claim: `claims/solr-17968-http-abi-investigation-round-1-phone-agent.md`. Assignment: `assignments/solr-17968-http-abi-investigation-round-1.md`.
- Date: 2026-10-27. Report: `reports/solr-17968-http-abi-investigation-linux-round-1.md`. Ticket draft: `jira/solr-17968-http-abi-followup-ticket-draft-round-1.md` (prepared, not filed).
- Toolchain: Temurin JDK 21.0.12.1 (`~/workspace/tools/jdk21/jdk-21.0.12.1+1`); consumer compiled with `javac --release 17`. Probe workspace on this VM: `~/workspace/17968-probe/` (sources, jars, compiled classes).

## Inputs pinned

- Released 10.0.0: `solr-solrj-10.0.0.jar` sha256 `59178b49e43ffed3d4845b73c0b3302bd77e1b0e3c10c5c4550ba8499bdb3fac`; `solr-api-10.0.0.jar`; source tag `releases/solr/10.0.0`.
- Published 10.1.0: `solr-solrj-10.1.0.jar` sha256 `115fcfbe1ed8ce04f7da8e200d20ce9105a567b40d6ec33727fcbd0667118bb4`; `solr-api-10.1.0.jar`. Both from Maven Central, 2026-10-27; Central's solr-solrj version list ends at 10.1.0.
- Refactor: main `8d3f995c6ac1633be0850d7715b788f9876ca4f2`, branch_10x `e5340a954b6700627b8826431a243c2e2759b0f3`, parent `df9941affcd76be0b009f23955c841f42c565926`; follow-up `712c94a47ba` (main) / `f444ec87f42` (branch_10x).
- Existing evidence reused: `receipts/SOLR-18325-binary-attribution-round-1.md` (verdict: released bytes pass on 10.0.0, fail identically with `NoSuchMethodError` on the pre-Jackson base and the Jackson candidate; defect inherited from the base, Jackson not involved), `reports/solr-18325-http-compatibility-prerequisite-design-round-1.md`.

## Q1 evidence

- Jira SOLR-17968 read in full (status Resolved, resolution Fixed, fix version 10.1; 7 comments): dsmiley 2025-11-07 and 2025-11-29 design comments; jira-bot commit notices for `8d3f995c6a`, `e5340a954b`, `712c94a47b`, `f444ec87f4`, `6f27cf39a5`. No mention of binary compatibility, linkage, or compiled consumers.
- GitHub PR #3829: body lists the renames; 16 reviews, 30 review comments, 6 issue comments fetched via API; 42 bodies scanned for binary/compat/NoSuchMethod/break/deprecat. Hits: gerlowskija's request for upgrade docs ("lots of users will have used the builders whose names are changing here"); no compatibility discussion.
- Ref guide, Major Changes in Solr 10, Solr 10.1 section (branch_10x, fetched 2026-10-27): the HttpSolrClient item is the two-sentence return note quoted in the report; no removal or recompile warning.
- Released 10.0.0 sources: `HttpSolrClientBase.java` carries `@lucene.internal` in its class Javadoc; `HttpSolrClientBuilderBase.java` carries no such marker (checked the file header and class declaration at the release tag).

## Q2 evidence

- `unzip -l` on both published jars: 10.0.0 contains `impl/HttpSolrClientBase.class` (16,392 bytes) and `impl/HttpSolrClientBuilderBase.class` (8,415); 10.1.0 contains `impl/HttpSolrClient.class` and `impl/HttpSolrClient$BuilderBase.class` and neither removed class.
- Live branch check via GitHub contents API, 2026-10-27: `HttpSolrClientBuilderBase.java` returns 404 on `main`, 404 on `branch_10x`, 200 on `branch_9x`. No bridge, fix, or revert found.

## Q3 probe (all commands run on this VM, 2026-10-27)

Consumer source (`~/workspace/17968-probe/consumer/Consumer.java`): builds `HttpJdkSolrClient` via `new HttpJdkSolrClient.Builder(url).withConnectionTimeout(10, TimeUnit.SECONDS).withRequestTimeout(60, TimeUnit.SECONDS).build()` in a try-with-resources, prints the class name. No base-type reference.

- `javac --release 17 -cp solr-solrj-10.0.0.jar` : OK. `Consumer.class` sha256 `a3bb85e5a58d39e226627df1340844d758471e48b7514ac137364cdea485801f`.
- `javap -c` call sites: `withConnectionTimeout:(JLjava/util/concurrent/TimeUnit;)Lorg/apache/solr/client/solrj/impl/HttpSolrClientBuilderBase;`, same shape for `withRequestTimeout`; `build:()Lorg/apache/solr/client/solrj/impl/HttpJdkSolrClient;`.
- Run vs 10.0.0 classpath (solrj + solr-api + slf4j-api 2.0.17): `BUILT org.apache.solr.client.solrj.impl.HttpJdkSolrClient`, `CONSUMER OK`, exit 0.
- Run vs 10.1.0 classpath, identical bytes: exit 1, `java.lang.NoSuchMethodError: 'org.apache.solr.client.solrj.impl.HttpSolrClientBuilderBase org.apache.solr.client.solrj.impl.HttpJdkSolrClient$Builder.withConnectionTimeout(long, java.util.concurrent.TimeUnit)'` at `Consumer.main(Consumer.java:8)`.
- Recompile identical source vs 10.1.0: OK; fresh bytes run vs 10.1.0: `BUILT ...`, `CONSUMER OK`, exit 0.
- Control `Consumer2` (donor + `withHttpClient(donor)`, class sha256 `c60a932574b2dfcf161864cd07a78977e2b4c12879fb585872748a1373fee987`): runs OK on both 10.0.0 and 10.1.0; its call-site descriptor names only concrete types.

## Q4 basis

- Design report read in full (111 lines): restoration shape, descriptor prototype results, sections 3 to 5 (protected fields: 9 on the client base, 20 on the builder base; protected constructor; dispatch rows). The prototype's checks are `javap` descriptor listings from stubs; no runtime fixture was instantiated. The corrected acceptance specification in the report adds the runtime fixture, runtime protected-field access, end-to-end old bytes, the donor lifecycle probe, and per-builder dispatch verification.
- Released 10.0.0 `HttpJdkSolrClient` source: constructor sets `shutdownExecutor=false` when an executor is supplied or adopted, true when it creates its own; `close()` shuts the executor down only when `shutdownExecutor` is true. `Builder.withHttpClient(HttpJdkSolrClient)` calls `super.withHttpClient`, adopts the donor's executor if the builder has none, copies the donor's `SSLContext` if the builder has none. The derived-close effect on the donor's `java.net.http.HttpClient` was not run; it is a required probe in the corrected spec, not an asserted fact.
- 10.1.0 `javap`: `HttpJdkSolrClient$Builder` carries `withHttpClient(HttpJdkSolrClient)` plus a bridge `withHttpClient(HttpSolrClient)` returning `HttpSolrClient$BuilderBase`; `HttpSolrClient$BuilderBase` holds the timeout methods and abstract `build()`.

## Q5 searches (Apache Jira, 2026-10-27)

- `text ~ "HttpSolrClientBuilderBase"`: 10 results: SOLR-17968 and older unrelated tickets (SOLR-17433, 17240, 599, 17943, 17541, 17771, 17776, 17211, 17161).
- `text ~ "NoSuchMethodError" AND text ~ "HttpSolrClient"`: 1 result, SOLR-8336 (unrelated, closed).
- `summary ~ "binary compatibility" AND created >= 2026-01-01`: 0 results.
- `text ~ "17968" AND key != SOLR-17968`: 0 results.
Absence is not claimed exhaustive; no tracker for this regression was found.

## Remaining uncertainty

- Whether maintainers consider a binary break through a public builder base acceptable in a minor release is a policy call no document settles; the ticket draft poses it rather than answering it.
- Jetty builder (`HttpJettySolrClient`) old-byte behavior was characterized by descriptor shape and the design report, not by a separate runtime probe in this round; the JDK builder probe is the demonstrated ordinary path.
- The donor `HttpClient` close semantics (Q4 item 4) are source-read only.
