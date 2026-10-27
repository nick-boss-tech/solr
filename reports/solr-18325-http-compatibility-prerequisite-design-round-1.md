# Design: SOLR-18325 HTTP client compatibility prerequisite (vm2, Linux round 1)

Status: design recommendation only. No production code changed, nothing pushed, no waiver. Scope: the released-binary HTTP client surface that `binaryHttp` uses. Evidence: `reports/solr-18325-evidence-closeout-linux-round-1.md` and the raw artifacts under `api/` and `design-proto/`.

## 1. Recommendation

Use an additive hierarchy that keeps the current implementation, with three exceptions that must restore declared types. Do not revert SOLR-17968 wholesale, and do not add empty alias classes.

1. Restore the released top-level classes as superclasses of the current implementation:
   - `HttpSolrClientBase` (new top-level, `public abstract class HttpSolrClientBase extends SolrClient`). The released 10.0.0 declaration, its 9 protected fields, protected constructor `(String, HttpSolrClientBuilderBase<?, ?>)`, and its 34 public and protected members move here from `HttpSolrClient`.
   - `HttpSolrClient extends HttpSolrClientBase` (was `extends SolrClient`). Implementation stays here.
   - `HttpSolrClientBuilderBase<B extends HttpSolrClientBuilderBase<?, ?>, C extends HttpSolrClientBase>` (new top-level). The body of today's `HttpSolrClient.BuilderBase` moves here (47 members, 20 protected fields, `withHttpClient(C)` whose erasure is `HttpSolrClientBase`).
   - `HttpSolrClient.BuilderBase<B, C extends HttpSolrClient> extends HttpSolrClientBuilderBase<B, C>`: keeps only `public abstract C build()`.
2. Leave `HttpJdkSolrClient.Builder` as it is. javac then provides the released descriptors (see section 3), with no hand-written bridge.
3. Restore the declared types, by hand, only where javac cannot bridge them (section 4).
4. Restore the one released overload that is missing from `SolrRequest` (section 4, item 6).

Why this is practical: the released descriptor set for the four core classes (`HttpSolrClientBase`, `HttpSolrClientBuilderBase`, `HttpJdkSolrClient`, `HttpJdkSolrClient$Builder`) has 106 members. Only the two top-level classes must exist again. `HttpJdkSolrClient` and its builder keep their current shape and get their released descriptors from inheritance, erasure and javac-generated bridges.

## 2. Evidence for the mechanism

A minimal javac prototype (`design-proto/src`, classes `SolrClient`, `HttpSolrClientBase`, `HttpSolrClientBuilderBase`, `HttpSolrClient`, `HttpJdkSolrClient`, `HttpJdkSolrClient$Builder`; compiled with `javac`, JDK 17; not a production build) was checked with `javap -protected -s` against the released member list (`design-proto/check.py`):

| Claim | Prototype result |
| --- | --- |
| `HttpJdkSolrClient$Builder.withConnectionTimeout(JLjava/util/concurrent/TimeUnit;)LHttpSolrClientBuilderBase;` resolves through inheritance once the nested `BuilderBase` extends the top-level class | Resolved (the stub `withConnectionTimeout` is declared on the top-level class and returns `B`, erasure `HttpSolrClientBuilderBase`). This is the exact descriptor that the old consumer fails on. |
| `HttpJdkSolrClient$Builder.build()LHttpSolrClientBase;` exists | Resolved. javac generated the bridge for the erasure of `C extends HttpSolrClientBase` in the chain. The stub output lists `build()LHttpSolrClient;` and `build()LHttpSolrClientBase;`. |
| `HttpJdkSolrClient$Builder.withHttpClient(LHttpSolrClientBase;)LHttpSolrClientBuilderBase;` exists | Resolved after `withHttpClient(C)` is declared on the top-level builder with `C extends HttpSolrClientBase`. The Builder unresolved count fell from 5 to 4. The four remaining misses are two constructors, `withSSLContext` and `withCookieHandler`, which the stub does not model. |
| Overloads that differ only in parameter types (for example `processWithBaseUrl(HttpSolrClientBase, ...)` next to `processWithBaseUrl(HttpSolrClient, ...)`) | Java allows them. Additive. |
| Overloads that differ only in return type | Java does not allow them. Restoration is required (section 4). |

Stub limits: the prototype declares only the members needed for the erasure claims. The 106-member closure check is for the repaired head, not for the stub. The stub is not evidence of behavior.

## 3. Why the additive shape restores the builder descriptors

Released 10.0.0 `HttpSolrClientBuilderBase` declares `public abstract C build()` (erasure `HttpSolrClientBase`), `public B withConnectionTimeout(long, TimeUnit)` (erasure returns `HttpSolrClientBuilderBase`), and `public B withHttpClient(C)` (erasure `HttpSolrClientBase`). Released `HttpJdkSolrClient$Builder` declares `build()` returning `HttpJdkSolrClient` and carries the bridges `build()LHttpSolrClientBase;` and `withHttpClient(LHttpSolrClientBase;)LHttpSolrClientBuilderBase;`.

Base 10.2 today (`HttpSolrClient.BuilderBase<B, C extends HttpSolrClient>`) has erasures `HttpSolrClient` and `HttpSolrClient$BuilderBase`. Moving the body to a top-level `HttpSolrClientBuilderBase` with the released bounds restores the erasures the old binary links against, without renaming any current public type. The nested name remains and continues to work for new code.

## 4. Non-bridgeable restorations (explicit declared types)

These differ only by return type or field type, so no additive overload can carry them. Restore the released declared type. This is the exact list from the released and base declarations (`api/inventory.json`, `api/out/solrj-released-base.xml`):

| # | Class | Member | Released (10.0.0) | Base 10.2 today | Action |
| --- | --- | --- | --- | --- | --- |
| 1 | `CloudHttp2SolrClient` | `getHttpClient()` | `HttpSolrClientBase` | `HttpSolrClient` | restore return type |
| 2 | `CloudSolrClient$Builder` | `createOrGetHttpClient()` (protected) | `HttpSolrClientBase` | `HttpSolrClient` | restore return type |
| 3 | `CloudSolrClient$Builder` | field `httpClient` (protected) | `HttpSolrClientBase` | `HttpSolrClient` | restore field type |
| 4 | `CloudSolrClient$Builder` | field `internalClientBuilder` (protected) | `HttpSolrClientBuilderBase<?, ?>` | `HttpSolrClient$BuilderBase<?, ?>` | restore field type |
| 5 | `CloudSolrClient$Builder` | `withHttpClient(HttpSolrClientBase)` and `withHttpClientBuilder(HttpSolrClientBuilderBase<?, ?>)` | present | `HttpSolrClient` and `HttpSolrClient$BuilderBase<?, ?>` overloads | keep base overloads and add released overloads (parameter-only, additive) |
| 6 | `SolrRequest` | `processWithBaseUrl(HttpSolrClientBase, String, String)` (public final) | present | replaced by `(HttpSolrClient, String, String)` | add released overload (parameter-only, additive). Removes 134 inherited-removal entries (section 6). |
| 7 | `HttpClusterStateProvider<C>` | class generic bound | `C extends HttpSolrClientBase` | `C extends HttpSolrClient` | restore bound |
| 8 | `HttpClusterStateProvider<C>` | `getHttpClient()`, `getSolrClient(String)` (both return `C`) | erasure `HttpSolrClientBase` | erasure `HttpSolrClient` | restore the bound in row 7; no source change to the methods |
| 9 | `LBSolrClient$Builder<C>` | class generic bound; `getSolrClient()` returns `C` | `C extends HttpSolrClientBase` (erasure `HttpSolrClientBase`) | `C extends HttpSolrClient` | restore the bound only; no source change to the method |
| 10 | `LBAsyncSolrClient` | `getClient()` and field `solrClient` (protected final) | `HttpSolrClientBase` | `HttpSolrClient` | restore return type and field type |
| 11 | `ConcurrentUpdateBaseSolrClient$Builder` | `getClient()` and field `client` (protected) | `HttpSolrClientBase` | `HttpSolrClient` | restore return type and field type |
| 12 | `HttpJdkSolrClient$Builder` | `build()` returning `HttpJdkSolrClient` | `HttpJdkSolrClient` (bridge `HttpSolrClientBase`) | same, bridge `HttpSolrClient` | no source change; javac provides the bridges (section 3) |

Not part of this prerequisite (separate ConcurrentUpdate refactor): `ConcurrentUpdateBaseSolrClient.doSendUpdateStream` returns `StreamingResponse` in 10.0.0 and `SentStream` in base. It does not reference the HTTP base types.

Cost: rows 1, 2, 10 and 11 change a concrete declared return or field type to the base type, so new code that expects `HttpSolrClient` must narrow. Rows 7–9 change only the generic bound; callers that parameterise with `HttpSolrClient` keep working. New accessors that return `HttpSolrClient` can be added under new names (for example `getHttpSolrClient()`) without changing the released descriptor. This is an API-design choice for the owner.

## 5. Protected state and subclass contract

- Released `HttpSolrClientBase` protected fields (9): `DEFAULT_PATH`, `FALLBACK_CHARSET`, `serverBaseUrl`, `requestTimeoutMillis`, `urlParamNames`, `requestWriter`, `parser`, `defaultParserMimeTypes`, `basicAuthAuthorizationStr`. Base removed all nine from the released name. They move to `HttpSolrClientBase` with the same names and types. Subclass code compiled against 10.0.0 then links.
- Released protected constructor `HttpSolrClientBase(String, HttpSolrClientBuilderBase<?, ?>)`: restored. `HttpSolrClient(String, BuilderBase<?, ?>)` calls `super(...)`.
- Released protected abstract methods (`createLBSolrClient`, `isFollowRedirects`, `processorAcceptsMimeType`, `allProcessorSupportedContentTypesCommaDelimited`, `updateDefaultMimeTypeForParser`) and the public abstract methods (`builder()`, `requestWithBaseUrl`, `requestAsync`) stay abstract on `HttpSolrClientBase`. `HttpSolrClient` already implements them.
- Released `HttpSolrClientBuilderBase` protected fields (20): moved with the builder body.
- Behavior of `withHttpClient(HttpSolrClientBase)` on the builder: the released overload accepts any `HttpSolrClientBase`. The moved body reads the moved fields, so any `HttpSolrClientBase` works. `HttpJdkSolrClient.Builder` also copies JDK-only state (`executor`, `sslContext`) in its own `withHttpClient(HttpJdkSolrClient)`. The inherited `withHttpClient(HttpSolrClientBase)` does not, so the builder needs a checked override that delegates to `HttpJdkSolrClient` when given one. This is a decision to confirm with the owner: default is to add the override so a released caller keeps the JDK state.

## 6. Scope: what this prerequisite does and does not cover

From the javap inventory and japicmp, released to base (`api/diff-inventory.json`, `api/out/solrj-released-base.xml`):

- Covered by this prerequisite: 2 removed top-level classes (`HttpSolrClientBase`, `HttpSolrClientBuilderBase`); 105 removed members that reference those types or `HttpSolrClient` base types; 134 classes that lose the inherited `SolrRequest.processWithBaseUrl(HttpSolrClientBase, ...)` (one overload, row 6); 15 classes with HTTP-type references in removed or added members (`CloudHttp2SolrClient`, `CloudSolrClient` and its `Builder`, `ConcurrentUpdateBaseSolrClient$Builder`, `ConcurrentUpdateJdkSolrClient$Builder`, `HttpClusterStateProvider`, `HttpJdkSolrClient` and its `Builder`, `HttpSolrClient` and `$BuilderBase`, `HttpSolrClientBase`, `HttpSolrClientBuilderBase`, `LBAsyncSolrClient`, `LBSolrClient$Builder`, `SolrRequest`).
- Not covered (inherited, separate decision): 176 removed members unrelated to HTTP types (`NamedList` and inner classes, `MapWriter`/`MapWriterMap`, `ConditionalMapWriter`, `Cache`/`MapBackedCache`, `Template`, `Callable`, `JavaBinCodec$StringCache`, `QueryingApi$Query`, and others); 11 solr-api breaks (DTO and endpoint interface changes); the `ConcurrentUpdateBaseSolrClient.doSendUpdateStream` return type; and the `ResponseParser` constructor visibility change.

Owner decision needed: does "released-binary compatibility" mean the HTTP client path that `binaryHttp` exercises, or the whole SolrJ and solr-api artifact? If the whole artifact, the repair is larger and separately scoped, covering the 176 members, the 11 solr-api breaks and the ConcurrentUpdate return type. This design does not claim to fix those.

## 7. Fixtures and tests for the repair

Old-compiled fixtures (compile once against released 10.0.0 with `--release 17`, hash the class bytes, run unchanged against each runtime):

1. `OldBuilderFixture`: `HttpSolrClientBuilderBase<?, ?> b = new HttpJdkSolrClient.Builder().withConnectionTimeout(10, TimeUnit.SECONDS);` and `b = new HttpJdkSolrClient.Builder().withRequestTimeout(30, TimeUnit.SECONDS);` (the descriptors that fail today).
2. `OldBridgeFixture`: `HttpSolrClientBase c = new HttpJdkSolrClient.Builder(url).build();` (uses the `build()LHttpSolrClientBase;` bridge).
3. `OldWithClientFixture`: `HttpSolrClientBase c2 = new HttpJdkSolrClient.Builder().withHttpClient(c).build();` (the `withHttpClient(HttpSolrClientBase)` overload).
4. `OldCastFixture`: `HttpSolrClientBase base = (HttpSolrClientBase) c; boolean ok = base instanceof HttpSolrClientBase;` (assignability and checkcast).
5. `OldSubclassFixture`: a subclass of `HttpSolrClientBase` implementing the released abstract methods, reading protected `serverBaseUrl` and `parser`, and calling the protected constructor `super(url, builder)`. Compile-only plus a `javap` check of the descriptors, since it needs no server.
6. `OldCloudFixture`: compile-only, references `CloudHttp2SolrClient.getHttpClient()` and `CloudSolrClient.Builder.createOrGetHttpClient()` for their released return descriptors.
7. HTTP: the existing `StableConsumer` leg against a disposable 10.0.0 standalone core (core status, add, commit, query, delete, delete-by-id). This already passes on released.

Acceptance checks on the repaired head:
- Closure: every released member of the 15-class set (name and descriptor, through the superclass and interface chain) resolves in the repaired candidate, except the two intentionally removed declarations if any. Current inventory tool: `api/inventory.py` plus a resolve step like `design-proto/check.py`, extended to all 15 classes.
- The fixtures above pass on released (positive), fail today on base and candidate (`binaryHttp` leg reproduces the current failure), and pass on the repaired head.
- japicmp, released to repaired: no binary-incompatible entries for the HTTP cluster (section 6, covered list). Remaining entries must match the uncovered list exactly.
- Repository test (reflection, runs in SolrJ): assert the released descriptors on the restored classes, e.g. `HttpSolrClientBuilderBase.class.getMethod("withConnectionTimeout", long.class, TimeUnit.class).getReturnType() == HttpSolrClientBuilderBase.class`; `HttpSolrClientBase.class.isAssignableFrom(HttpSolrClient.class)`; the `HttpJdkSolrClient$Builder` declared `build` bridge. Reflection guards the descriptors; the javap closure remains the authority.
- Standard gate on the new head: tidy, Error Prone compile, focused SolrJ HTTP/Cloud/LB/ConcurrentUpdate suites, `check -x test` in a standalone clone, and the three Jackson suites (unchanged, their oracles must stay green).

## 8. Where to branch, and cost

- Branch from the exact base `f45b38d5f6c0`, not from the Jackson candidate, as a separate scope (for example `solr-http-abi-restore`). The Jackson experiment keeps its own proof. Do not merge the two without a superseding exact-head gate.
- Files touched (base tree): new `HttpSolrClientBase.java` and `HttpSolrClientBuilderBase.java`; edits to `HttpSolrClient.java`, `HttpJdkSolrClient.java`, `SolrRequest.java`, `CloudHttp2SolrClient.java`, `CloudSolrClient.java`, `HttpClusterStateProvider.java`, `LBSolrClient.java`, `LBAsyncSolrClient.java`, `ConcurrentUpdateBaseSolrClient.java`, plus `ConcurrentUpdateJdkSolrClient.java` where its builder extends the restored base. About 12 files in `solrj/src/java`, one new reflection test, and fixtures in `dev-tools/jackson3-solrj`-style harness (not in production).
- Cost: moderate. The mechanical part is the move of about 80 members (the 47-member builder body, and the 34 released base-class members that the new base carries) and the restorations in section 4 (eight declared return or field types, three generic bounds, three overloads). The risky parts are behavior of the new base (`withHttpClient` JDK state, moved protected fields) and the owner decision on return types. Expect one exact-head gate plus one superseding gate if the head changes.

## 9. Owner decisions (exact remaining choices)

1. Return and field types, rows 1, 2, 10, 11: restore the released `HttpSolrClientBase` declared types and add `HttpSolrClient`-typed accessors under new names, or accept a source break for new callers that expect `HttpSolrClient`. Rows 7–9 (generic bounds) carry no such choice.
2. `withHttpClient(HttpSolrClientBase)` on the JDK builder: add the JDK-state override (recommended) or inherit the generic one.
3. Scope: HTTP path only (this design), or the whole SolrJ and solr-api artifact (adds the 176 members, 11 solr-api breaks, and the ConcurrentUpdate return type).

Limits: this design was checked with stubs and with the released and base inventories. It is not a production implementation and not a proof of behavior. No waiver or requirement change has been made.
