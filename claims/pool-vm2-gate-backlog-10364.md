# Claim: SOLR-10364 premise run, then first gate (round 2, job 4)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-10364.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-11T02:58:17Z (UTC).
Branch: solr-10364-submit at 502bdbf033faa648792372960d01c54728349ee9 (live tip, verified by ls-remote at claim time).

Heartbeat: 2026-10-11T02:58:17Z.

Premise run 2026-10-11T03:13:24Z: PREMISE HOLDS. Base (DocumentObjectBinder at cabedd1d96, branch test kept) testSetFields fails with "Can not set java.util.Set field ... to ImmutableCollections$ListN"; head passes 6/0. Packaged head efb1e6717f9 (local, TESTING.md removed). Gate running, runner /workspace/gates/g10364-gate.sh, log /workspace/gates/logs/vm2/10364-gate.out.

Result 2026-10-11T03:22:22Z: GATE GREEN at packaged head efb1e6717f9ffda64b52c0820efb52ba58832b50. Changelog OK, tidy clean, compileTestJava rc 0, TestDocumentObjectBinder tests=6 failures=0 at head from fresh XML, module check rc 0. Receipt receipts/SOLR-10364.md. Packaging commit local only, not pushed.
DONE: 2026-10-11T03:22:22Z (UTC), outcome GREEN.
