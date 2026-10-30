# SOLR-18600 dual-release ABI premise, Linux gate round 1 (vm6)

Gate: `gates/solr-18600-dual-release-abi-premise-round-1.md` (posted at `29a56979ee`). Claim: `claims/solr-18600-dual-release-abi-premise-round-1-vm6.md`. Receipt: `receipts/solr-18600-dual-release-abi-premise-round-1.md`. Artifacts (scripts, fixtures, prototype diff, descriptor inventories, matrix output, hashes): `reports/solr-18600-dual-release-abi-premise-round-1-artifacts/`.

## Verdict

**COMPLETE WITH DESIGN**, in the job's terms. The inventory, the missing controls, and a viable prototype establish a bounded implementation plan. The current production patch on `solr-18600-http-abi-restore-submit` (`c40665af8d6b`) remains **HOLD**: it breaks 10.1-compiled callers on the public builder and getter surface. The prototype below is descriptor and linkage evidence only. It is not a release-ready patch and has not had a gate run on its head.

Passing the prototype against both cohorts shows that the design links both sets of unchanged bytes. It does not show that the transport behaves correctly.

## Host and toolchain

- Host: vm6, Linux 6.1 aarch64 (Amazon Linux 2023), 4 CPUs, 7 GB RAM.
- Build JDK: Temurin 21.0.12.1+1 (`/workspace/jdk/jdk-21.0.12.1+1`), Gradle 9.7.0 from the fork wrapper, `GRADLE_USER_HOME=/workspace/gradle-home`, host lock `/workspace/tools/.gradle-vm6.lock`.
- Consumer JDK: Temurin 17.0.21+9 (`/workspace/jdk/jdk-17.0.21+9`), used for `javac --release 17` and every runtime leg. JDK 17 was available, so no runtime leg is unrun.
- Gate preflight on the fork tip: `gate-preflight.sh` exit 0 (tidy clean), `/workspace/gates/logs/vm6/preflight/solr-18600-dual-release-abi-premise-round-1/`.

## Pins

| Item | Pin | Note |
| --- | --- | --- |
| Fork branch `solr-18600-http-abi-restore-submit` | `c40665af8d6bd23c85fef565f93de5004a7b2ca4` | Matches the posted pin. Built as `fork`. The first scratch clone was corrupt and was discarded; the second is a clean shallow fetch of the pinned SHA. |
| Unmodified base | `a336bf871b2c2b67abe55808966051d1c553b579` | Shallow fetch of the pinned SHA. Built as `base`. |
| Released 10.0.0 | `org.apache.solr:solr-solrj{,-jetty,-zookeeper}:10.0.0`, `solr-api:10.0.0`; source `releases/solr/10.0.0` at `6c6c48a6f7` (read for semantics only) | Hashes in `hashes/released-solr-jars.sha256`. |
| Released 10.1.0 | same artifacts at 10.1.0 | Source tag not fetched; the artifacts are the evidence. |
| Prototype | `fork` plus the diff in `prototype/prototype-vs-c40665a.diff` | Derived from the pinned fork head. Built as `proto`. |

`branch_10x` has since moved to `255638e7`. The gate used the posted base `a336bf87`.

Every release and candidate jar is hashed in `hashes/`. The candidate builds are in `hashes/candidate-builds.sha256`.

## Dependency sets differ between the cohorts

The two released runtime classpaths resolve through Gradle from Maven Central (45 jars each). Beyond SolrJ, they differ in Jetty (12.0.27 vs 12.0.34), `jakarta.ws.rs-api` (3.1 vs 4.0), Jackson (2.20 vs 2.22), Netty (4.1.119 vs 4.1.130), slf4j, Zookeeper (3.9.4 vs 3.9.5) and swagger annotations. Each cohort runs against its own release classpath.

`base`, `fork` and `proto` run with the 10.1.0 dependency set. That is a stated caveat: the candidate's own Gradle-resolved dependencies could differ. The class-linkage results do not depend on Jetty or Netty versions, but a behavioural leg should pin them.

## Phase A: API and descriptor inventory

Tool: `javap -protected -s -v` per class, parsed into rows of (class, signature, descriptor, flags) for the HTTP client family (SolrRequest, HttpSolrClient/Base/BuilderBase, HttpJdk, Jetty, Cloud, LB, LBAsync, ConcurrentUpdate, HttpClusterStateProvider). Script: `scripts/inventory.py`. Raw rows: `inventory/*.tsv`.

Member counts (solrj and solrj-jetty):

| Tree | Members |
| --- | --- |
| released 10.0.0 | 444 |
| released 10.1.0 | 471 |
| base `a336bf87` | 475 |
| fork `c40665af` | 492 |
| proto | 539 |

Member counts include bridge methods, so they rise where the candidate adds legacy bridges. The decision metric is resolution, not textual presence. `scripts/resolve.py` checks each release member against the candidate tree the way the JVM resolves it: the declaring class first, then its superclass and interface chain, using the same name and descriptor.

| Release member set | Tree | Unresolved | What they are |
| --- | --- | --- | --- |
| 10.1.0 | base | **0** | The base is the 10.1 surface. |
| 10.0.0 | base | 122 | The base is the 10.1 surface; 10.0-only names are absent. |
| 10.1.0 | fork c40665a | 37 | 16 nested `BuilderBase` fluent methods, plus Cloud, ConcurrentUpdate, provider, LB and Jetty builder members. Public: `HttpSolrClient$BuilderBase` fluent methods returning B, `CloudSolrClient.getHttpClient`, `HttpClusterStateProvider` constructor and getter, `LBSolrClient$Builder` constructor and `getSolrClient`, `ConcurrentUpdateBaseSolrClient$Builder` constructors and `getClient`. |
| 10.0.0 | fork c40665a | 9 | Public: `CloudSolrClient.connect()` and `connect(long,TimeUnit)`, `Builder.withInternalClientBuilder`, `CustomBlockingQueue(int,int,E)`, `HttpJettySolrClient.addListenerFactory`, `HttpJettySolrClient.Builder.withSSLConfig`. Protected: `doSendUpdateStream`, `prepareRequest`, `Jetty Builder.build(Class)`. |
| 10.1.0 | proto | **9** | All protected: `CloudSolrClient.Builder` (5 protected members incl. the `httpClient` and `internalClientBuilder` fields), `ConcurrentUpdateBaseSolrClient.Builder.client` (field type), `HttpSolrClient.baseUrl` (field), `LBAsyncSolrClient` (`solrClient` field, `getClient(Endpoint)`). |
| 10.0.0 | proto | **6** | Public: `CloudSolrClient.connect()` and `connect(long,TimeUnit)`. Protected: `doSendUpdateStream`, `HttpClusterStateProvider.getSolrClient(String)`, `prepareRequest`, Jetty `Builder.build(Class)`. |

The 10.1 "unresolved" list in the fork includes the public fluent-builder descriptors that break the JDK, Jetty, Cloud and LB builder rows in Phase B. The fork's current public 10.0 gaps are the connect pair, `withInternalClientBuilder`, `addListenerFactory`, `withSSLConfig`, and the `CustomBlockingQueue` constructor. The prototype restores the last four and leaves the connect pair open (see below).

## Phase B: unchanged released consumers

Fixtures: `consumers/c100/fixture/Cohort100.java` (compiled against 10.0.0) and `consumers/c101/fixture/Cohort101.java` (compiled against 10.1.0). Generated by `scripts/gen.py`. Each row is one nested class, loaded inside a `try` through `Class.forName`, so a linkage error in one row cannot stop the others.

Row result: `OK` (call returned), `REACHED` (the call ran and reached the transport, which refused the connection to `127.0.0.1:1`; linkage passed), `LINKAGE` (a `LinkageError`). Only `LINKAGE` is a failure.

Compile and runtime: `javac --release 17` against each cohort's release jars. Class files are hashed before and after the matrix; both cohorts' bytes are unchanged (`matrix/phaseC-run2/*.classes.{before,after}.sha256`, log line `class bytes unchanged`).

Final matrix (fixtures v2, `matrix/phaseC-run2`):

| Row | c100 on 10.0.0 | c100 on 10.1.0 | c100 on base | c100 on fork | c100 on proto | c101 on 10.1.0 | c101 on 10.0.0 | c101 on base | c101 on fork | c101 on proto |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| jdk.withConnectionTimeout.build | OK | LINKAGE | LINKAGE | OK | OK | OK | LINKAGE | OK | LINKAGE | OK |
| jdk.withRequestTimeout.build | OK | LINKAGE | LINKAGE | OK | OK | OK | LINKAGE | OK | LINKAGE | OK |
| jdk.withHttpClient.jdk | OK | OK | OK | OK | OK | OK | OK | OK | OK | OK |
| jdk.withHttpClient.base | OK | LINKAGE | LINKAGE | OK | OK | OK | LINKAGE | OK | LINKAGE | OK |
| jdk.builderBridge.build | OK | LINKAGE | LINKAGE | OK | OK | OK | LINKAGE | OK | OK | OK |
| jdk.requestWithBaseUrl | REACHED | REACHED | REACHED | REACHED | REACHED | REACHED | REACHED | REACHED | REACHED | REACHED |
| jetty.withConnectionTimeout.build | OK | LINKAGE | LINKAGE | OK | OK | OK | LINKAGE | OK | LINKAGE | OK |
| jetty.withSSLConfig.build | OK | LINKAGE | LINKAGE | LINKAGE | OK | – | – | – | – | – |
| jetty.addListenerFactory | OK | LINKAGE | LINKAGE | LINKAGE | OK | – | – | – | – | – |
| cloud.getHttpClient | OK | LINKAGE | LINKAGE | OK | OK | OK | LINKAGE | OK | LINKAGE | OK |
| cloud.withHttpClient.base | OK | LINKAGE | LINKAGE | OK | OK | OK | LINKAGE | OK | OK | OK |
| cloud.withHttpClientBuilder | OK | LINKAGE | LINKAGE | OK | OK | OK | LINKAGE | OK | OK | OK |
| cloud.withInternalClientBuilder | OK | LINKAGE | LINKAGE | LINKAGE | OK | – | – | – | – | – |
| clusterStateProvider.ctor.getHttpClient | OK | LINKAGE | LINKAGE | OK | OK | OK | LINKAGE | OK | LINKAGE | OK |
| concurrentUpdate.builder.getClient | OK | LINKAGE | LINKAGE | OK | OK | OK | LINKAGE | OK | LINKAGE | OK |
| concurrentUpdateJdk.builder.build | – | – | – | – | – | OK | LINKAGE | OK | OK | OK |
| lb.builder.getSolrClient | OK | LINKAGE | LINKAGE | OK | OK | OK | LINKAGE | OK | LINKAGE | OK |
| solrRequest.processWithBaseUrl | REACHED | LINKAGE | LINKAGE | REACHED | REACHED | REACHED | LINKAGE | REACHED | REACHED | REACHED |
| jdk.withHttpClient.jdkSubtype | – | – | – | – | – | OK | OK | OK | OK | OK |

Reading the table:

- **Positive controls pass.** `c100` on 10.0.0 and `c101` on 10.1.0 are all OK or REACHED.
- **Candidate proto passes both cohorts** on all rows that exist for that cohort.
- **Fork (current restoration) fails the 10.1 cohort** on 8 rows: the JDK timeout builders and `withHttpClient(HttpSolrClient)` on the nested builder, Jetty timeout, Cloud `getHttpClient`, the provider getter path, the ConcurrentUpdate builder, and the LB builder. This is the vm2 finding, confirmed by linkage on every row that covers the family.
- **Fork passes the 10.0 cohort** on the rows it had, and fails the three public 10.0 members the restoration did not carry: `withSSLConfig`, `withInternalClientBuilder` and `addListenerFactory`. Proto fixes these.
- **Base fails the 10.0 cohort** as expected: base has no 10.0 classes.
- `c101` on 10.0.0 is not a requirement (no 10.1-compiled-on-10.0 runtime is imposed). It is shown for information only.
- The `REACHED` rows (`requestWithBaseUrl`, `processWithBaseUrl`) prove the call dispatches to the transport; they do not prove a successful request.

Limits of Phase B:

- Linkage only. No row proves a successful request, a result value, or the ownership of a shared client. The LB `requestWithBaseUrl` dispatch control and the zookeeper adapters are not covered beyond what the rows touch.
- `connect()` is not exercised at runtime: it opens a ZooKeeper session, and this gate has no service to connect to. Its absence is shown by the descriptor inventory instead.
- The ConcurrentUpdate builder row uses an anonymous subclass of the abstract builder. It proves that the builder constructor and `getClient()` link; it does not prove transport ownership.

## Phase C: dual-descriptor design (prototype)

Prototype: `prototype/prototype-vs-c40665a.diff` (7 modified files, 2 new files; `diffstat.txt`). Built as `proto`. It is source-integrated on the pinned fork head, so the diff is exact and reproducible.

Design, per family:

1. **Nested `HttpSolrClient.BuilderBase` fluent overrides.** Sixteen `B`-returning methods from `HttpSolrClientBuilderBase` are redeclared in the nested class as covariant delegating overrides (`return super.name(args)`). javac emits the bridge with the legacy erasure (`HttpSolrClientBuilderBase`) and keeps the 10.1 erasure (`HttpSolrClient$BuilderBase`). This is the mechanism the direction note asked for: the subclass's covariant override produces both descriptors.
2. **Changed getter return types (`CloudSolrClient.getHttpClient`, `ConcurrentUpdateBaseSolrClient.Builder.getClient`, `LBSolrClient.Builder.getSolrClient`, `HttpClusterStateProvider.getHttpClient`).** Java source cannot declare two methods that differ only in return type. The 10.1 return type is declared on the class. The 10.0 erasure is produced by javac's bridge from a package-private interface (`LegacyAccessors.HttpClientAccess`, `ClientAccess`, `SolrClientAccess`, and `LegacySSLConfigAccess` in the Jetty package) that declares the legacy return type. A differently named getter alone would not repair the old bytecode.
3. **Changed parameter types (constructors and `withHttpClient`/`withHttpClientBuilder` overloads).** Both overloads are kept as ordinary Java overloads with different parameter types.
4. **Generic bounds.** `HttpClusterStateProvider<C>` and `LBSolrClient.Builder<C>` move their bound from `HttpSolrClientBase` to `HttpSolrClient`, so that the 10.1 erasure is `HttpSolrClient`. The 10.0 constructor and getter erasures come back through the overloads and the legacy interfaces above.
5. **Removed 10.0 public members restored.** `Builder.withInternalClientBuilder` (deprecated since 9.10, a one-line delegation to `withHttpClientBuilder`); `HttpJettySolrClient.addListenerFactory(HttpListenerFactory)` (void, adds to the same list the Builder fills); `CustomBlockingQueue(int,int,E)` (the 10.0 body never used `maxConsumers`, so it delegates to `(int,E)`).

Design verification:

- Descriptor inventory on `proto` (`inventory/proto3.tsv`): the 10.1 public surface resolves in full. Every 10.1 member that remains unresolved is protected.
- Consumer matrix: `proto` links every row for both cohorts (table above).
- Bytecode check: the bridges are visible in `javap` output in `inventory/proto3.tsv` as `ACC_BRIDGE, ACC_SYNTHETIC` members with both descriptors.

### Known gaps and risks in the design

1. **Cast-based bridges can throw `ClassCastException`.** The 10.0 erasures return `HttpSolrClientBase`. Where the field is typed `HttpSolrClientBase` but the 10.1 return is `HttpSolrClient` (`ConcurrentUpdateBaseSolrClient.Builder.getClient`, `CloudHttp2SolrClient.getHttpClient`), the new code casts. A 10.0 caller that constructed the builder or cloud client with a `HttpSolrClientBase` that is not an `HttpSolrClient` would now get a `ClassCastException` through the 10.1 path. Every shipped client extends `HttpSolrClient` in both releases, so the risk is only for user-defined `HttpSolrClientBase` subclasses. A behavioural test with such a subclass is needed before any patch is offered.
2. **Protected members are not restored.** 10.0 and 10.1 declare protected members with the same name and parameter types but different return types or field types (for example `CloudSolrClient.Builder.createOrGetHttpClient()`, `ConcurrentUpdateBaseSolrClient.Builder.client`, `LBAsyncSolrClient.solrClient`, `HttpClusterStateProvider.getSolrClient(String)`). Java source cannot express both, and an interface cannot carry protected members. These are extension surface, not the ordinary call contract, and the fixtures do not call them. Listed here as exclusions. The `HttpSolrClient.baseUrl` field (10.1) is in the same position.
3. **`getBaseURL()` versus `serverBaseUrl`.** The restoration's `HttpSolrClientBase.getRequestUrl` path reads the field `serverBaseUrl` (`HttpSolrClientBase.java`, around line 123, `ClientUtils.buildRequestUrl(solrRequest, serverBaseUrl, collection)`), where 10.1 used the virtual `getBaseURL()`. A subclass that overrides `getBaseURL()` changes behaviour between the releases. No fixture covers it. Not changed in the prototype; a behavioural decision for the maintainers.
4. **Not run on the prototype:** tidy, Error Prone, spotless, forbidden-APIs, the SolrJ unit suites, and the Jetty suites. The prototype is a linkage prototype and must not be read as a passing head. A repaired head needs the full fresh Linux gate.

## Open item: `CloudSolrClient.connect()` (10.0 public, removed in 10.1)

10.0 `CloudSolrClient.connect()` delegates to `getClusterStateProvider().connect()`, and `connect(long, TimeUnit)` waits for readiness (both in the 10.0.0 source). `ClusterStateProvider.connect()` was removed from the interface in 10.1 (the base has no such method; 10.0 declares it at `ClusterStateProvider.java:127`). Restoring the 10.0 method therefore requires a lifecycle decision: the provider no longer has a connect step to delegate to.

Options:

- (a) Restore both methods as `@Deprecated` documented no-ops, stating that the client now connects on first use. Keeps the 10.0 binary contract. Behaviour change: a caller that relied on `connect(long, TimeUnit)` blocking until the cluster is ready would no longer block.
- (b) Restore them to wait for readiness with a loop over `getClusterStateProvider()` state. Keeps the blocking semantics. New code, not a delegation, with its own risks.
- (c) Leave them removed and document a 10.0-to-10.1 break for these two methods, outside the repair.

Rung 4 (decision ladder): the evidence favours (a) for binary preservation and is consistent with the owner's direction to preserve both cohorts. The blocking behaviour of `connect(long, TimeUnit)` is a semantic choice the owner should confirm. Recorded as NEEDS-OWNER in the receipt, with one question.

## Recommendations

1. **Regression protection (not implemented here):** keep both immutable release cohorts as fixtures, as in `consumers/`. Add a versioned descriptor baseline generated by the inventory (`inventory/v100.tsv`, `v101.tsv`) and the resolver check. The baseline catches removed or retyped public members; the cohorts catch link failures. A japicmp run over the whole artifact was not done and remains open.
2. **Repair scope:** the diff in `prototype/` plus the three restorations above, subject to the `connect()` decision and the cast risk in gap 1. Protected members are excluded and must be listed in the upgrade notes.
3. **Next gate:** a fresh Linux gate on the repaired head, with tidy, Error Prone compile, the named SolrJ/Jetty/Cloud/LB/ConcurrentUpdate tests, and both immutable cohorts on that exact head. The current receipt cannot cover a new head.

## Decision-ladder record

| Point | Rung | Evidence |
| --- | --- | --- |
| Which surface to preserve | 3 | Owner direction (`reports/vm2-solr-18600-dual-release-direction-round-3-windows.md`): both cohorts, public call contract. |
| Fluent builder fix (nested overrides) | 2 | Probe: prototype bridges visible in `javap`; matrix rows pass. |
| Covariant getter fix (legacy interface) | 2 | Probe: the matrix rows pass and the proto bridges appear in `inventory/proto3.tsv` (37 bridge rows vs 13 on the fork). A renamed getter was rejected on the direction note's reasoning (it cannot repair old bytecode); it was not probed separately. |
| Excluding protected members | 1 | Java language constraint (same name and parameters, different return or field type; no protected members in interfaces). |
| Restoring `withInternalClientBuilder`, `addListenerFactory`, `CustomBlockingQueue(int,int,E)` | 1 | 10.0.0 source read at `6c6c48a6f7`: delegation or unused parameter. |
| `connect()` | 4 → owner | Two defensible behaviours (no-op vs blocking); recorded as one question. |
| `serverBaseUrl` vs `getBaseURL()` | Recorded only | No fixture; behaviour decision belongs to maintainers. |
