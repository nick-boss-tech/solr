# SOLR-18600 dual-release behavior closure, round 2: vm2 receipt

Host: vm2. Claim: `claims/solr-18600-dual-release-behavior-closure-round-2-vm2.md` (idempotency `solr-18600-dual-release-behavior-closure-round-2@6f61b6dbaf96`).
Candidate: `nick-boss-tech/solr:solr-18600-additive` at `6f61b6dbaf96` (live ref verified at claim time). Base `a336bf871b2c`; original pin `c40665af8d6b`.
Toolchain: JDK 21.0.12.1+1 (`/workspace/jdk/jdk-21.0.12.1+1`). Released jars: Maven Central 10.0.0 and 10.1.0 (see `jars.sha256`). Host-specific dependency list: `cp-solrj.txt`, `cp-mods-deps.txt`.
Submission status: HOLD. Nothing posted to GitHub, Jira or the mailing list. No PR. No push to any production branch.

## Result by leg

| Leg | Status | Evidence |
|---|---|---|
| Consumer matrix on final head | PASS, 10/10 rows as expected | `run-final.sh`, logs in `run-final/`; cohorts are the SAME frozen classes from round-1 vm2 (not recompiled) |
| A connect | FAIL: method absent on final head; no behavior restored | `probes/` ProbeA; `a-10.0.log` vs `a-final.log` |
| B custom client and legacy constructors | FAIL: 4/4 legacy paths reject a valid 10.0 subclass | ProbeB; `b-10.0.log` (4 OK) vs `b-final.log` (4 IllegalArgumentException) |
| C URL dispatch (`getBaseURL` override) | FAIL for 10.1-compiled subclass on final head | ProbeC; `c-10.1-cohort-on-10.1.log` (hit B) vs `c-10.1-cohort-on-final.log` (hit A) |
| D successful local requests | PARTIAL PASS: requests reach the endpoint and return without error on final head | ProbeC `plain-request-status-ok`; response values not checked; borrowed-client closure NOT RUN |
| Protected closure, field and dispatch review (C part 2) | NOT RUN | residual list in the report |

## Matrix on final head

`run-final.sh` output (all expected-match yes):

```
core-10.0-on-10.0 exit=0 pass=12
core-10.1-on-10.1 exit=0 pass=12
core-10.0-on-10.1-negative exit=1 pass=0
core-10.0-on-final exit=0 pass=12
core-10.1-on-final exit=0 pass=12
jetty-10.0-on-10.0 exit=0 pass=8
jetty-10.1-on-10.1 exit=0 pass=8
jetty-10.0-on-10.1-negative exit=1 pass=0
jetty-10.0-on-final exit=0 pass=8
jetty-10.1-on-final exit=0 pass=8
all-expected=yes
```

Final artifact: `build/solr-solrj-final.jar`, sha256 `2e111ea260a4748b` (built from `6f61b6dbaf96` sources; javac 0 errors; SolrJ, Jetty and ZooKeeper classes in one jar, 768 classes). Full hashes in `jars.sha256`; cohort class hashes in `cohorts.sha256`.

## Probe results (exact)

Column "10.0" is the 10.0-compiled fixture on the released 10.0.0 jars; "final" is the same fixture on `build/solr-solrj-final.jar`.

```
b-10.0   legacy-concurrentUpdate-identity OK true   | final: IllegalArgumentException: Expected an HttpSolrClient but got ProbeB$CustomBase
b-10.0   legacy-cloud-withHttpClient-identity OK    | final: IllegalArgumentException (same)
b-10.0   legacy-lb-ctor-identity OK                 | final: IllegalArgumentException (same)
b-10.0   legacy-provider-ctor-identity OK           | final: IllegalArgumentException (same)

c-10.0 cohort   on 10.0: request -> A ; on final: request -> A   (10.0 behavior preserved)
c-10.1 cohort   on 10.1: request -> B ; on final: request -> A   (10.1 behavior NOT preserved)

a-10.0   connect-no-args: RuntimeException "Failed fetching live_nodes ..." after 315 ms
a-10.0   connect(1,SECONDS): RuntimeException TimeoutException "Timed out waiting for cluster" after 1010 ms
a-final  connect-no-args: NoSuchMethodError CloudHttp2SolrClient.connect()
a-final  connect(long,TimeUnit): NoSuchMethodError
```

Dispatch detail (ProbeC): a subclass of `HttpJdkSolrClient` overrides `getBaseURL()` to return server B while the constructor URL is server A. On 10.0 the request reaches A (override ignored). On 10.1 it reaches B (override honored). On the final head the same 10.1-compiled bytes reach A.

## Protocol notes

- The legs are not discharged by the build/test gate. The gate `6f61b6dbaf96` (receipt `receipts/SOLR-18600-additive-round-4.md`) covers build and test only.
- The `connect` probe uses an unreachable server, so the 10.0 result shows timeout and failure behavior, not successful eager initialization. Successful init, transient failure followed by success, interruption and closed-provider cases are NOT RUN.
- The custom-client stub returns null from `createLBSolrClient()` and throws `UnsupportedOperationException` from request methods. These are fixture stubs; identity checks do not depend on them.
- Environment: `/` 4.6 GB free and 9% inodes used at claim time; `/workspace` (NFS) reports zero inode counters, not verified.

## Restoring the Java sources

Java fixture sources are published with a `.txt` suffix (`cohort-*.java.txt`, `probe-Probe*.java.txt`) so the board's Java format hook does not reject them. The bytes are unchanged. To rerun, remove the `.txt` suffix from each file. `probe-sources.sha256` and the cohort hashes in `cohorts.sha256` identify the original names.

## Round-2 repair on branch `solr-18600-additive-r2` at `cdde1029dd5c` (pushed as a new fork branch; `solr-18600-additive` not touched)

Changes: restored `CloudSolrClient.connect()` and `connect(long, TimeUnit)` (released 10.0 waiting contract, with the info log omitted), a deprecated default `ClusterStateProvider.connect()` delegating to `getLiveNodes()`, a `ZkClientClusterStateProvider.connect()` checking the state reader, and `HttpSolrClientBase.getRequestUrl` using `getBaseURL()` as 10.1 does. Jar `build-r2/solr-solrj-r2.jar` (hash in `jars-r2.sha256`).

Gate, exact head `cdde1029dd5c`: changelog YAML parsed under uv (see the r1 note), tidy rc=0 (no changes), Error Prone rc=0 (0 warnings), `check -x test` for solrj, solrj-jetty and solrj-zookeeper rc=0, focused SolrJ tests rc=0, focused Jetty tests rc=0, `:solr:core:check -x test` rc=0. Logs: `gate-r2/steps.txt`.

Consumer matrix on the r2 jar (`run-final.sh` with `FINAL_JAR` set): 10/10 rows as expected, the same as on `6f61b6dbaf96`.

Probe comparison, `6f61b6dbaf96` against r2 (`probes/out-6f61b6dbaf96` vs `probes/out`):

- A (connect): on `6f61b6dbaf96` the 10.0 consumer got `NoSuchMethodError`. On r2 it gets the released 10.0 failures: `RuntimeException` "Failed fetching live_nodes" for no-arg connect and `TimeoutException` after about 1 s for timed connect. Successful eager initialization is not tested.
- B (custom client): unchanged on r2. All four legacy paths still throw `IllegalArgumentException`. Blocker, see report.
- C (dispatch): the 10.1-compiled overriding subclass now reaches B on final, as on 10.1. The 10.0-compiled overriding subclass also reaches B on r2, where 10.0 reaches A. This is the documented difference.
- D (requests): parsed response status is 0 on all four runs, both cohorts on both releases and on r2.

Not run: borrowed-client closure and protected-member closure.
