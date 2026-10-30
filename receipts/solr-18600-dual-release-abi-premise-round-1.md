# Receipt: SOLR-18600 dual-release ABI premise, Linux gate round 1 (vm6)

- Job: `gates/solr-18600-dual-release-abi-premise-round-1.md` (posted `29a56979ee`).
- Host: vm6, Linux 6.1 aarch64 (Amazon Linux 2023). Single writer for this receipt.
- Claim: `claims/solr-18600-dual-release-abi-premise-round-1-vm6.md` (started 2026-10-29T23:04:50Z).
- Verdict: **COMPLETE WITH DESIGN**. Production patch on `solr-18600-http-abi-restore-submit` remains **HOLD** until a fresh repaired-head gate passes.
- Report: `reports/solr-18600-dual-release-abi-premise-linux-round-1.md`.
- Artifacts: `reports/solr-18600-dual-release-abi-premise-round-1-artifacts/`.
- Completed: 2026-10-30 (UTC; time of this receipt's push).

## Pins and toolchain

| Item | Value |
| --- | --- |
| Fork head (`solr-18600-http-abi-restore-submit`) | `c40665af8d6bd23c85fef565f93de5004a7b2ca4` (matches posted pin) |
| Base (unmodified `branch_10x`) | `a336bf871b2c2b67abe55808966051d1c553b579` (matches posted pin; live `branch_10x` is `255638e7`, not used) |
| Released 10.0.0 source (semantics only) | `releases/solr/10.0.0` at `6c6c48a6f78486130682ea9c1f7a2723af5a87be` (matches posted pin) |
| Released 10.1.0 | Maven artifacts only; source tag `749712b71940c9f96d824c533f32f5c56dcd3924` not fetched |
| Prototype | fork head plus `artifacts/prototype/prototype-vs-c40665a.diff` |
| Build JDK | Temurin 21.0.12.1+1, `/workspace/jdk/jdk-21.0.12.1+1`, Gradle 9.7.0 from fork wrapper |
| Consumer JDK | Temurin 17.0.21+9, `/workspace/jdk/jdk-17.0.21+9`, `javac --release 17` and all runtime legs |

## Commands and exit codes

| Step | Command (host paths) | Exit |
| --- | --- | --- |
| Gate preflight on fork head | `gate-preflight.sh https://github.com/nick-boss-tech/solr.git c40665af… <log dir>` | 0 (tidy clean) |
| Released classpath resolution | standalone Gradle project `resolve/` with `:resolveCohorts` (dependency resolution only) | 0 (45 jars per release) |
| Base SolrJ build | `./gradlew :solr:api:jar :solr:solrj:jar :solr:solrj-jetty:jar :solr:solrj-zookeeper:jar` on base | 0 |
| Fork SolrJ build | same tasks on fork (first clone corrupt, re-fetched, rebuilt) | 0 |
| Prototype SolrJ build (3 iterations) | same tasks on proto; failures fixed (a cast in `CloudHttp2SolrClient`, then a rebuild after added restorations) | 1, 1, then 0 |
| Descriptor inventory | `scripts/inventory.py` with `javap -protected -s -v` | 0 for each tree |
| Resolution check | `scripts/resolve.py` | 0 |
| Consumer compile | `javac --release 17` for `Cohort100` against 10.0.0 jars; `Cohort101` against 10.1.0 jars | 0 (fixtures v2) |
| Matrix (final) | `scripts/run-matrix.sh` writes `matrix/phaseC-run2/` | 0 for every run; class bytes unchanged |

Build logs: `/workspace/gates/logs/vm6/solr18600-dual-release-round-1/build-*.log` (host-local). Copies of the matrix outputs are in `artifacts/matrix/`.

## Evidence

- Inventory and resolution: `artifacts/inventory/`. Unresolved counts (member resolution through the superclass and interface chain, not textual presence):
  - 10.1.0 members unresolved in base: 0; in fork: 37; in prototype: 9 (all protected).
  - 10.0.0 members unresolved in base: 122; in fork: 9; in prototype: 6 (2 public `connect` overloads, 4 protected).
- Consumer matrix: `artifacts/matrix/phaseC-run2/` (`*.out`, `*.err`, `*.classes.{before,after}.sha256`) and the table in the report. Positive controls: `c100` on 10.0.0 and `c101` on 10.1.0 link on every row. Prototype links every row for both cohorts. Fork fails 8 rows of the 10.1 cohort and 3 rows of the 10.0 cohort.
- Hashes: `artifacts/hashes/` (released jars, classpaths, candidate builds, consumer sources and classes).
- Prototype diff: `artifacts/prototype/prototype-vs-c40665a.diff` (`diffstat.txt`: 7 modified files, 2 new files).

## Decisions (decision ladder, recorded in the report)

- Preserve both cohorts, public call contract: rung 3 (owner direction in `reports/vm2-solr-18600-dual-release-direction-round-3-windows.md`).
- Nested-builder covariant overrides and legacy-interface getter bridges: rung 2 (matrix and `javap` bridge evidence).
- Protected members excluded: rung 1 (Java language constraint; listed as exclusions).
- Restoring `withInternalClientBuilder`, `addListenerFactory` and `CustomBlockingQueue(int,int,E)`: rung 1 (10.0.0 source).
- `CloudSolrClient.connect()` (two overloads): rung 4 failed to settle it, so it is an owner question (below).

## NEEDS-OWNER

One question. `CloudSolrClient.connect()` and `connect(long, TimeUnit)` exist in 10.0.0 and were removed in 10.1 together with `ClusterStateProvider.connect()`. The owner's direction is to preserve both cohorts. Restore them as `@Deprecated` no-ops that document connect-on-first-use (keeps the 10.0 binary contract, changes the blocking behaviour of `connect(long, TimeUnit)`), or leave them removed and document a 10.0-to-10.1 break for these two methods? The receipt's recommendation is the no-op restoration.

## Not run

- Tidy, Error Prone, spotless, forbidden-APIs, SolrJ and Jetty unit suites on the prototype (it is a linkage prototype, not a gate head).
- A behavioural check of the cast-based bridges with a user-defined `HttpSolrClientBase` subclass (report, gap 1).
- The `serverBaseUrl` versus `getBaseURL()` behaviour (report, gap 3).
- `connect()` at runtime (needs a ZooKeeper service; recorded by inventory only).
- japicmp over the whole artifact.
- Zookeeper adapters beyond the rows that touch them, and LB `requestWithBaseUrl` beyond the transport-reached control.
- The 10.1.0 source tag.

## Side effects and cleanliness

- No JIRA or GitHub post. No PR. No push to the fork. No change to the fork branch or the base.
- Scratch trees and logs are under `/workspace/tmp/s18600/` and `/workspace/gates/logs/vm6/solr18600-dual-release-round-1/`. The artifacts directory above is the portable copy.
- Heartbeats: 2026-10-29T23:26:35Z (after the Phase A inventory). A later heartbeat at push time is in the claim.
- Gate file `gates/solr-18600-dual-release-abi-premise-round-1.md` set to DONE with this receipt.
