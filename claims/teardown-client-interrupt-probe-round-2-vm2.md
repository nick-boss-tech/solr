# Claim: vm2 for assignments/teardown-client-interrupt-probe-round-2.md

- Claimant host: vm2. Started: 2026-10-26T01:07:12Z.
- Scope: the round 2 corrected premise unit only: scaffold v3 (cumulative, against the pinned original test file), one control invocation, then one interrupted invocation in a separate fresh JVM, same seed and flags. At most two filtered invocations.
- Supersedes the execution recipe of `assignments/teardown-client-interrupt-probe-round-1.md`, per round 2. Round 1 claims and reports are unchanged.
- Source: apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`, clean scratch worktree `/workspace/worktrees/teardown-probe-r3`.
- Seed `FFF5A3C3AAC69084`; JDK `/workspace/jdk/jdk-21.0.12.1+1` (21.0.12.1).
- Runner: `pool/bin/sweep-run.sh` under `/workspace/tools/.gradle-vm2.lock`, with an outer `timeout` of 1200 s for the whole run.
- Observation cap 150 s per arm; cleanup at most 30 s and only after the original closer terminates. Diagnostics and process-JVM containment (Runtime.halt in the controller) run before the JVM exits when the closer is still active at the cap.
- Preserved vm2 original logs and XML, with hashes: `/workspace/gates/gates-vm2/preserved-round1/`, sums in `/workspace/gates/gates-vm2/preserved-round1-SHA256SUMS.txt`.
- Deliverables: `reports/teardown-client-interrupt-probe-round-2-scaffold.md` (full v3 diff, SHA-256, base, watchdog arrangement, commands) and `reports/teardown-client-interrupt-probe-round-2.md` (results), with raw evidence files posted to the board.
- Decision ladder: rung 2, the probe the assignment asks for. No source fix, no third invocation, no other ranked probe.
