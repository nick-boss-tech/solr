# Report: SOLR-17968 HTTP client binary compatibility investigation, round 1 (Linux, phone-agent)

Assignment: `assignments/solr-17968-http-abi-investigation-round-1.md`. Receipt: `receipts/SOLR-17968-http-abi-investigation-round-1.md`. Ticket draft (if Nick approves filing): `jira/solr-17968-http-abi-followup-ticket-draft-round-1.md`.

## Bottom line

The SOLR-17968 refactor removed two public classes, `HttpSolrClientBase` and `HttpSolrClientBuilderBase`, in Solr 10.1.0. Code compiled against SolrJ 10.0.0 that uses nothing but the ordinary `HttpJdkSolrClient.Builder` timeout methods fails at runtime on 10.1.0 with a `NoSuchMethodError` on the very first builder call, before any HTTP operation. The identical source recompiles against 10.1.0 and runs fine, so this is a binary compatibility break only: source compatibility holds, and client/server HTTP compatibility is not involved. No record in the SOLR-17968 Jira thread, in PR #3829's discussion, or in the 10.1 upgrade notes shows the binary consequence was considered or accepted, and no existing ticket tracks it.

Recommendation: file one new Jira ticket as a distinct compatibility defect, with the reproducer and evidence in the prepared draft. The policy question (whether a break through a public, unmarked builder base class is acceptable in a minor release) is stated inside the ticket for a maintainer to settle; it is not asserted here. Nothing is filed by this report; filing is Nick's go.

## Q1. Was the consequence accepted?

The rename was deliberate and tracked: SOLR-17968 (Resolved, Fixed, fix version 10.1), PR #3829, main commit `8d3f995c6ac`, branch_10x commit `e5340a954b`. Its purpose, per Smiley's comments, was the base abstraction and a classpath-detecting builder.

Acceptance of the binary consequence: no evidence anywhere it was considered.

- The SOLR-17968 Jira thread (7 comments) contains Smiley's two design comments and commit notices only. Binary compatibility is never mentioned.
- PR #3829's body lists the renames as mechanics. Its 42 review and comment bodies (dsmiley, epugh, gerlowskija, igiguere, jdyer1) contain no discussion of binary compatibility, linkage, or `NoSuchMethodError`. The closest approach is gerlowskija asking for upgrade help in the docs because "lots of users will have used the builders whose names are changing here". The shipped 10.1 upgrade note (ref guide, Major Changes in Solr 10, Solr 10.1 section) is two sentences: "HttpSolrClient returns; this time as a base class for HttpJettySolrClient and HttpJdkSolrClient. Its builder will dynamically detect if solr-jetty is available and use that, otherwise it will use the JDK client." It does not say the old classes were removed or that pre-compiled consumers must recompile.
- The internal-API angle does not cover the failing path. In released 10.0.0, `HttpSolrClientBase` carries `@lucene.internal` in its Javadoc; `HttpSolrClientBuilderBase` does not. The class name baked into ordinary consumer bytecode is the builder base, a public abstract class with no internal marker, and the consumer source never names either class (see Q3).

Classification: renaming deliberately does not establish acceptance of the break, and silence does not establish rejection. The accurate statement is that the consequence is undocumented and undecided in the public record.

Three compatibilities, kept separate:

- Java source compatibility: intact. The Q3 consumer source compiles unchanged against 10.1.0 and the recompiled bytes run correctly.
- Java binary compatibility: broken. Bytes compiled against 10.0.0 fail on 10.1.0 at the first inherited builder call (Q3).
- Client/server HTTP compatibility: unaffected. The failure happens at method resolution before any request is built or sent; no wire behavior changed.

## Q2. Affected versions; still present?

- First published version containing the refactor: 10.1.0. Verified in the published artifact itself, not inferred from the upgrade note: `solr-solrj-10.1.0.jar` (Maven Central, sha256 `115fcfbe1ed8ce04f7da8e200d20ce9105a567b40d6ec33727fcbd0667118bb4`) contains `impl/HttpSolrClient.class` and `impl/HttpSolrClient$BuilderBase.class` and contains neither `HttpSolrClientBase.class` nor `HttpSolrClientBuilderBase.class`. The 10.0.0 jar (sha256 `59178b49e43ffed3d4845b73c0b3302bd77e1b0e3c10c5c4550ba8499bdb3fac`) contains both.
- Maven Central's version list for solr-solrj ends at 10.1.0, so 10.1.0 is the only affected published release at the time of checking (2026-10-27).
- Still present upstream: yes. As of 2026-10-27, `HttpSolrClientBuilderBase.java` returns HTTP 404 on both `main` and `branch_10x` (it still exists on `branch_9x`, where the refactor never landed). No fix, revert, or compatibility bridge has landed since the refactor.

## Q3. Can ordinary code trigger it?

Yes. Minimal consumer (full source in the receipt): a `main` that builds an `HttpJdkSolrClient` with `withConnectionTimeout(10, TimeUnit.SECONDS)` and `withRequestTimeout(60, TimeUnit.SECONDS)` and closes it. No reference to any base type, no subclassing, no internal API.

- Compiled once against released 10.0.0 with `--release 17`. `Consumer.class` sha256 `a3bb85e5a58d39e226627df1340844d758471e48b7514ac137364cdea485801f`.
- javap shows why: the fluent calls resolve through the builder but their descriptors name the return type `HttpSolrClientBuilderBase`, e.g. `HttpJdkSolrClient$Builder.withConnectionTimeout:(JLjava/util/concurrent/TimeUnit;)Lorg/apache/solr/client/solrj/impl/HttpSolrClientBuilderBase;`. javac emits the erased descriptor of the declaring base class even though the source names only the concrete builder.
- The exact bytes run against released 10.0.0: prints `BUILT org.apache.solr.client.solrj.impl.HttpJdkSolrClient` and `CONSUMER OK`, exit 0.
- The exact bytes run against published 10.1.0: exit 1, `java.lang.NoSuchMethodError: 'org.apache.solr.client.solrj.impl.HttpSolrClientBuilderBase org.apache.solr.client.solrj.impl.HttpJdkSolrClient$Builder.withConnectionTimeout(long, java.util.concurrent.TimeUnit)'` at the first builder call. No server is involved; the failure precedes any HTTP operation.
- The identical source recompiled against 10.1.0 compiles cleanly and the fresh bytes run correctly on 10.1.0. Only pre-compiled artifacts (drop-in jar replacement, prebuilt plugins or libraries, shaded or vendored consumers) hit the break.
- Control: a second consumer using `Builder.withHttpClient(donor)` (whose call site descriptor names only concrete types) runs unchanged on both 10.0.0 and 10.1.0. The break is specific to descriptors that name the two removed classes, which is exactly the inherited-builder-method surface every ordinary timeout/SSL/executor configuration call uses.
- This matches the SOLR-18325 attribution receipt's finding (same failure on the pre-Jackson base and the Jackson candidate) and confirms Jackson is not required to reproduce it.

## Q4. What remains uncertain in the proposed repair?

The existing design (`reports/solr-18325-http-compatibility-prerequisite-design-round-1.md`) restores both top-level classes and moves the released bodies back, which makes the missing descriptors resolvable on paper; its javac prototype checked descriptor presence with `javap` only. Corrected acceptance specification for any repair:

1. Runtime fixture, not descriptors only: an old-compiled subclass fixture (compiled against 10.0.0, extending the released base types and calling the protected constructor) must be instantiated and exercised at runtime against the repaired build, not merely listed by `javap`.
2. Protected-field access at runtime: the fixture must read and write the released protected fields of `HttpSolrClientBase` (the 9 fields in design section 5) and of the builder base (20 fields) through the subclass, proving the moved fields resolve with the released names, types, and access, since field resolution is a runtime linkage act the stub never performed.
3. End-to-end old bytes: the Q3 consumer bytes (sha256 above) and the subclass fixture bytes, both compiled against 10.0.0 and never recompiled, must run to completion against the repaired runtime, including class loading of both restored top-level classes with no `NoClassDefFoundError`, `NoSuchMethodError`, or `NoSuchFieldError`.
4. Donor-client lifecycle probe: build a donor client, derive a second client with `withHttpClient(donor)`, close the derived client, and show the donor still builds requests afterwards (or record the actual behavior). Baseline behavior in released 10.0.0, from source: `Builder.withHttpClient` adopts the donor's executor when the builder has none and copies the donor's `SSLContext` when the builder has none; a client that received an executor does not shut it down on `close()` (`shutdownExecutor` is false), while a client that created its own executor does. Whether closing a derived client also closes the donor's underlying `java.net.http.HttpClient` is not established by the source read alone and must be settled by this probe before any ownership change is proposed.
5. Dispatch characterization before repair: on the current (10.1) builder, `withHttpClient` exists in two forms (the concrete-type method and a bridge naming `HttpSolrClient`), and `HttpClient`-sharing semantics differ between the JDK and Jetty builders (the SOLR-17968 follow-up commit `712c94a4` records Jetty-only internals behind `Builder.withHttpClient`). A repair must state, per builder, which `withHttpClient` overload an old-compiled call site resolves to, verified by running old bytes, not by source inspection.

No repair is implemented or selected by this investigation, per the assignment boundaries.

## Q5. Ticket, tracking update, or nothing?

Duplicate search (2026-10-27, Apache Jira; queries in the receipt): a text search for `HttpSolrClientBuilderBase` returns SOLR-17968 itself and older unrelated tickets; `NoSuchMethodError` with `HttpSolrClient` returns one unrelated closed ticket; a summary search for "binary compatibility" created since 2026-01-01 returns nothing; no other ticket references 17968. This does not prove exhaustive absence, but no existing ticket tracks this regression.

Recommendation: a distinct compatibility defect. It is not already tracked (SOLR-17968 is resolved and never mentions the consequence), and it cannot honestly be filed as an intentional documented break, because no documentation of the break exists and the record shows the question was never put. The prepared draft (`jira/solr-17968-http-abi-followup-ticket-draft-round-1.md`) carries the reproducer, the tested coordinates and hashes, expected versus observed behavior, the exact descriptor and stack, and the open policy question for the maintainer. Filing waits for Nick's go.
