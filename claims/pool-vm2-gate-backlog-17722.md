# Claim: SOLR-17722 premise run and build gate (round 2)

Claimant host: vm3. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-17722.md.
Capability tags: premise-run, gate. Staffing: 1.
Branch: `solr-17722-submit` at `fee3a26beb3c8289057ac41d94dfc8d83a969b71` (live tip verified at claim time).
Claim-time merge-base with live `apache/solr` main `d76fc14b9857e77b830d7a82514b28c523a239c2`: `56ec140e3636d5f4150fa87fbf7103529536ac99`.
Started: 2026-10-11T05:53:14Z (UTC).
Heartbeat: 2026-10-11T05:53:14Z (UTC).
Heartbeat: 2026-10-11T06:29:50Z (UTC).
Heartbeat: 2026-10-11T06:37:46Z (UTC).

Status: BLOCKED. The initial base-only wire-probe task failed with 8 tests, 6 failures, 0 errors, 0 skipped, seed `8E23EBE00D9764B2`; all six failures were NPEs in inherited `HttpSolrClientTestBase.testQuery*` methods because the scratch subclass did not set the expected `a` parameter. After main-agent review, one isolated base-only launch targeting only `testRawWireQueryOverridesRequestWtJson` was authorized. That launch did not start Gradle: it ran from `/workspace/gates/vm3-build/solr`, where `./gradlew` does not exist, and Bash returned exit 127. The wrapper is at `/workspace/gates/vm3-build/gradlew`; no retry was made per the harness/environment failure instruction. No fresh XML, no new test execution, and no premise verdict. No head run or gate steps. The receipt remains unchanged and still says the premise is unproven.

Evidence: original failed task log `/workspace/gates/logs/vm3/17722-base-wire-test.log`; original JUnit XML preserved at `/workspace/gates/logs/vm3/17722-base-wire-failed-inherited-20261011.xml`; failed launch log `/workspace/gates/logs/vm3/17722-base-wire-isolated-method-20261011T063746Z.log`. Pre-launch checks at 2026-10-11T06:37:46Z showed 26 GB available, 190,631 free inodes, and no Gradle-related Java process. Post-launch checks showed 26 GB available, 190,630 free inodes, and no Gradle-related Java process. The build checkout was restored to detached HEAD `fee3a26beb3c8289057ac41d94dfc8d83a969b71`, with the topic branch ref unchanged and tracked tree clean.
