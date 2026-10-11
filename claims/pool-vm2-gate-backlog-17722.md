# Claim: SOLR-17722 premise run and build gate (round 2)

Claimant host: vm3. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-17722.md.
Capability tags: premise-run, gate. Staffing: 1.
Branch: `solr-17722-submit` at `fee3a26beb3c8289057ac41d94dfc8d83a969b71` (live tip verified at claim time).
Claim-time merge-base with live `apache/solr` main `d76fc14b9857e77b830d7a82514b28c523a239c2`: `56ec140e3636d5f4150fa87fbf7103529536ac99`.
Started: 2026-10-11T05:53:14Z (UTC).
Heartbeat: 2026-10-11T05:53:14Z (UTC).
Heartbeat: 2026-10-11T06:29:50Z (UTC).
Heartbeat: 2026-10-11T06:37:46Z (UTC).
Heartbeat: 2026-10-11T06:43:41Z (UTC).

Status: DONE, NO GATE by finding. The initial base-only class run had 8 tests and 6 inherited-method NPEs because the scratch setup omitted `a`; this was a harness failure, not premise evidence. The first single-method launch used the wrong working directory and stopped before Gradle started (exit 127); the error was preserved. The main agent then authorized one retry from `/workspace/gates/vm3-build` with the exact method filter. At merge-base `56ec140e3636d5f4150fa87fbf7103529536ac99`, the isolated test passed. Fresh JUnit XML records 1 test, 0 failures, 0 errors, and 0 skipped. The test's captured-wire assertions confirm `wt=javabin`, exclude `wt=json`, and verify captured parameters `wt=javabin`. Therefore the premise is false and the gate and claim are closed as NO GATE. No topic-head run, packaging, or broader gate was done.

Evidence: initial class-run log `/workspace/gates/logs/vm3/17722-base-wire-test.log`; preserved original JUnit XML `/workspace/gates/logs/vm3/17722-base-wire-failed-inherited-20261011.xml`; first launch error `/workspace/gates/logs/vm3/17722-base-wire-isolated-method-20261011T063746Z.log`; successful retry log `/workspace/gates/logs/vm3/17722-base-wire-isolated-method-retry-20261011T064253Z.log`; fresh JUnit XML `/workspace/gates/vm3-build/solr/solrj-jetty/build/test-results/test/TEST-org.apache.solr.client.solrj.jetty.Solr17722WireProbeTest.xml`. The topic branch stayed at `fee3a26beb3c8289057ac41d94dfc8d83a969b71`, the builder was restored cleanly, and no Gradle process remained after the retry.

During the retry, a later VM1 claim for the same slice appeared at 06:43:08Z. The vm3 claim began at 05:53:14Z and the authorized retry began at 06:42:53Z, so the VM1 claim is recorded as a duplicate superseded by this earlier claim and completed result. No VM1 test evidence is asserted. The separate retire decision remains with the owner.
