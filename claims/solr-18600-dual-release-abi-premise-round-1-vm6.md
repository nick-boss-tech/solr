# Claim: solr-18600-dual-release-abi-premise-round-1 (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: `gates/solr-18600-dual-release-abi-premise-round-1.md` (posted at `29a56979ee0c`, OPEN; vm2 preferred, not exclusive; vm2's prior artifacts are on vm2 and were not used).
- Slice: the whole job (Phases A, B, C and the handoff), at fork head `c40665af8d6bd23c85fef565f93de5004a7b2ca4`, base `a336bf871b2c2b67abe55808966051d1c553b579`.
- Capability tags used: gate, premise-run, review, records.
- Started: 2026-10-29T23:04:50Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18600-dual-release-abi-premise-round-1@c40665af8d6b.
- Links: Ticket or parent: SOLR-18600 (branch `solr-18600-http-abi-restore-submit`); SOLR-18325 design. Category: 4 (SolrJ and clients). Siblings: `assignments/vm2-solr-18600-premise-review-round-1.md`, its addendum at `0643ae17ea`, `reports/vm2-solr-18600-dual-release-direction-round-3-windows.md` (vm2 Windows records, read before claiming). Supersedes: none.
- Toolchain: JDK 21.0.12.1+1 at `/workspace/jdk/jdk-21.0.12.1+1`, Gradle 9.7.0 wrapper. JDK 17 is not installed on vm6 at claim time; it will be fetched or the Java 17 runtime leg is recorded as unrun.
- Log and artifact directory: `/workspace/gates/logs/vm6/solr18600-dual-release-round-1/`. Preflight: `/workspace/gates/logs/vm6/preflight/solr-18600-dual-release-abi-premise-round-1/` (rc=0, tidy clean at `c40665af8d6b`).
- Board snapshot read before claiming: `coordination-ui` at `339efc2615`.

Heartbeat: 2026-10-29T23:04:50Z
Heartbeat: 2026-10-29T23:26:35Z

Result: 2026-10-29T23:26:35Z: Phase A descriptor inventory (javap -protected -s -v, member-level, key = class+signature+descriptor+flags) on released 10.0.0 (538 rows), released 10.1.0 (577), base a336bf871b2c (583), fork c40665af8d6b (604). Members: v100 444, v101 471, base 475, fork 492. Base has every 10.1 member and lacks 122 of 10.0's. Fork lacks 92 of 10.1's (27 HttpSolrClient, 45 HttpSolrClient$BuilderBase, plus Cloud, LB, ConcurrentUpdate, HttpJdk/Jetty builder) and 20 of 10.0's (mostly protected MIME hooks moved to HttpSolrClientBase). Artifacts: /workspace/gates/logs/vm6/solr18600-dual-release-round-1/inventory/. Released jars sha256 prefixes: solr-solrj-10.0.0 59178b49, solr-solrj-jetty-10.0.0 19a6252b, solr-solrj-10.1.0 115fcfbe, solr-solrj-jetty-10.1.0 0723d41e. Java 17 at /workspace/jdk/jdk-17.0.21+9. Not yet run: consumer legs (Phase B), prototype (Phase C).
