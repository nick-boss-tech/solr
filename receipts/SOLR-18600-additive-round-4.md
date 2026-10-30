# SOLR-18600 additive repair, round 4: receipt (vm2)

Status: head `6f61b6dbaf96` pushed to fork branch `solr-18600-additive` (base `c40665af8d6b`; commits `b5261d29d171`, `37228af35b31`, `6f61b6dbaf96`). Nothing posted to GitHub, Jira or the mailing list. No PR opened.

## What changed

Restores the released 10.0 surface and keeps the 10.1 surface, per the round-3 direction.

- Restored top-level `HttpSolrClientBase` and `HttpSolrClientBuilderBase` (from the branch head).
- `HttpSolrClient.BuilderBase` redeclares its 16 fluent methods with the nested return type, so javac emits the 10.0 erased descriptors as bridges.
- Public Cloud, LB, provider, ConcurrentUpdate and JDK builder members gain the 10.0 overloads. The 10.1 declarations stay.
- `getHttpClient()` (Cloud, Cloud HTTP/2) and `getClient()` (ConcurrentUpdate builder) return the 10.1 type. Package-private `HttpClientAccess`, `SolrClientAccess` and `ClientAccess` interfaces make javac emit the 10.0 bridges.
- Protected members whose 10.0 form differs only by return type keep the 10.1 form (see residuals).
- `SolrClientNodeStateProvider.httpSolrClient()` keeps the 10.1 type.
- Changelog title updated to cover 10.1-compiled code.

## Two-cohort proof (`proof/run.sh`, `proof/run-jetty.sh`)

Consumers compiled once against released 10.0.0 and once against 10.1.0. Each runs on its own release (positive), the 10.0 cohort runs on 10.1.0 (negative), and both cohorts run unchanged on the repaired build.

| Run | Core (12 scenarios) | Jetty (8 scenarios) |
|---|---|---|
| 10.0 cohort on its release | 12/12 | 8/8 |
| 10.1 cohort on its release | 12/12 | 8/8 |
| 10.0 cohort on 10.1.0 (negative) | class load fails: `HttpSolrClientBase` absent | class load fails |
| 10.0 cohort on repaired build | 12/12 | 8/8 |
| 10.1 cohort on repaired build | 12/12 | 8/8 |

Source-level controls against the repaired build: the 10.0 and 10.1 core fixtures compile with 0 errors.

Reference coverage (`proof/check_refs.py`): all 46 SolrJ member references in the 10.0 core cohort and all 50 in the 10.1 core cohort resolve in their own release and in the repaired build.

### Pre-fix controls (same matrix, earlier builds)

| Build | 10.0 cohort | 10.1 cohort |
|---|---|---|
| base `a336bf871b2c` (10.1-dev, before restoration) | class load fails (`HttpSolrClientBase` absent); 13 source errors | 12/12 |
| branch head `c40665af8d6b` (previous patch) | 11/12 (`withInternalClientBuilder` missing: `NoSuchMethodError`) | 3/12 (9 `NoSuchMethodError`, e.g. `HttpJdkSolrClient$Builder.withConnectionTimeout` returning `HttpSolrClient$BuilderBase`) |
| repaired `b5261d29d171` | 12/12 | 12/12 |

The proof matrix was built before the `@Override` and test-assertion commits (`37228af35b31`, `6f61b6dbaf96`); those change no descriptor, and the gate above runs the final head. Raw outputs: `proof/results/{final-b5261d29d171,unrepaired-c40665af8d6,base-a336bf871b2c}/`.

## Descriptor inventory (reflection, `proof/tools/ListApi`, incl. inherited and bridges)

Target = union of 10.0.0 and 10.1.0 public and protected descriptors (`compare.py`). Protected lookups are matched by name and descriptor, so a public inherited method counts.

- Core impl (44 classes): candidate 1280; target 1301 (10.0: 1247, 10.1: 1120). Missing 21. Lost versus base `a336bf871b2c`: 0.
- Jetty and ZooKeeper modules: candidate 1514; target 1555. Missing 48. Lost versus base: 0. Most of the 48 are branch-tree drift from the releases; see below.

## Residuals (not restored by this change)

A. Public 10.0 members removed in 10.1.0 that are not HTTP base-class members. Out of scope unless the owner decides otherwise.
- `connect()` and `connect(long, TimeUnit)` on `CloudSolrClient`, `CloudHttp2SolrClient`, `ClusterStateProvider`, `BaseHttpClusterStateProvider`, `DelegatingClusterStateProvider` and `HttpClusterStateProvider`: 8 descriptors.
- `CloudSolrClient.RouteResponse` (6) and `CloudSolrClient.RouteException` (3) logging and access methods.
- `HttpJettySolrClient.Builder.withSSLConfig(SSLConfig)` and `build(Class)`: absent from 10.1.0 and from base.

B. Protected members whose 10.0 form differs only by return type. The 10.1 form is kept because source cannot declare both, and a bridge needs a supertype that declares the 10.0 form.
- `CloudSolrClient.Builder.createOrGetHttpClient()`; `HttpClusterStateProvider.getSolrClient(String)`; `LBAsyncSolrClient.getClient(Endpoint)`; `ConcurrentUpdateBaseSolrClient.doSendUpdateStream(...)` (10.0 `StreamingResponse`, 10.1 `SentStream`); `SolrClientNodeStateProvider.RemoteCallCtx.httpSolrClient()`.
- Jetty: `CloudJettySolrClient.Builder.createOrGetHttpClient()`; `LBJettySolrClient.getClient(Endpoint)`; `ConcurrentUpdateJettySolrClient.doSendUpdateStream(...)`.
- Effect: a 10.0-compiled subclass that overrides one of these will not be called through the 10.1 dispatch path. Calls from 10.0 code are not affected.
- Protected fields with 10.0 types (`CloudSolrClient.Builder.httpClient`, `internalClientBuilder`; `LBAsyncSolrClient.solrClient`; `ConcurrentUpdateBaseSolrClient.Builder.client`) are not measured by the lister and are not restored.

C. Branch-tree drift relative to 10.1.0 (present in base, so not caused by this change).
- `HttpJettySolrClient.addListenerFactory` (10.1.0) was removed on branch_10x by `c345aa6bb79` (SOLR-18360). Consumers compiled against 10.1.0 that call it fail on this build.
- `VMParamsAllAndReadonlyDigestZkACLProvider`, `ZkMaintenanceUtils`, `ZkStateReader.getReplicaProps`, `PerReplicaStatesOps$PrsZkNodeNotFoundException`, `MapWriter`-based `_forEachEntry`/`toMap` and related members differ from 10.1.0 in the base tree.
- Implication: the target release line is part of the decision. A repair on the 10.1.x line would not carry the branch_10x drift.

D. Extras on the candidate (additive, no release has them): `CloudSolrClient.getHttpClient()` returning `HttpSolrClientBase` (bridge); `HttpJdkSolrClient.builder()` returning `HttpSolrClient$BuilderBase` (covariant).

## Limits of the proof

- Behavior checks use a closed port. Dispatch is exercised only to the point of the client exception.
- The LB builder, Cloud builder and ConcurrentUpdate ownership checks cover the getters and constructors only.
- The ZooKeeper module is covered by the descriptor inventory only, with no cohort.
- Source-level 10.0 code that passes a plain `HttpSolrClientBase` to the JDK or Jetty builder's `withHttpClient` did not compile against 10.0.0 either, so it is not a new restriction.
- 10.1-compiled code on a 10.0 runtime is out of scope, per the round-3 direction.

## Gate (exact head `6f61b6dbaf96`): PASS

Log: `gate/steps.txt`. Run 2026-10-30 00:02–00:24 UTC.

| Step | Result |
|---|---|
| tidy | rc=0, no changes |
| changelog YAML | parsed OK with `uv run --with pyyaml` (the in-gate python lacked PyYAML; the first `rc=1` in steps.txt is that missing module, not the file) |
| Error Prone compile (`-Pvalidation.errorprone=true`) | rc=0, 0 warnings |
| `:solr:solrj`, `:solr:solrj-jetty`, `:solr:solrj-zookeeper` `check -x test` | rc=0 |
| focused SolrJ tests (seed `18600C0FFEE18600`) | rc=0 (includes `HttpSolrClientBinaryCompatTest`, `HttpJdkSolrClientTest`, `CloudHttp2SolrClientBuilderTest`, `CloudSolrClientCacheTest`, `ConcurrentUpdateJdkSolrClientTest`, `LBSolrClientTest`, `LBSolrClientRetryUnsentTest`) |
| focused Jetty tests | rc=0 (`HttpJettySolrClientBinaryCompatTest`, `HttpJettySolrClientTest`) |
| `:solr:core:check -x test` | rc=0 |

### Attempt history

- Attempt 1, head `b5261d29d171`: Error Prone `MissingOverride` on `LBSolrClient.Builder.getSolrClient()`; two descriptor tests failed because reflection returns the 10.1 return type when the 10.0 bridge is present. Fixed in `37228af35b31` (`@Override`; tests check the 10.0 type among all matching methods). Logs: `gate/attempt1/`.
- Attempt 2, head `37228af35b31`: Error Prone `MissingOverride` on `CloudSolrClient.getHttpClient()` (and the same pattern in `HttpClusterStateProvider` and `ConcurrentUpdateBaseSolrClient.Builder`); `testRestoredClassesCarryReleasedMembers` asserted the 10.0 generic bounds and return type. Fixed in `6f61b6dbaf96` (`@Override`; the test asserts the 10.1 bounds and types plus the 10.0 bridge). Logs: `gate/attempt2/`.
- Attempt 3, head `6f61b6dbaf96`: all steps pass (above).
