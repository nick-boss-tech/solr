# Claim: SOLR-8628 premise-only run

- Claimant host: vm3.
- Assignment: `assignments/pool-shared-gate-backlog-buffer.md`.
- Job: `gates/SOLR-8628-premise.md`.
- Capability tags: `premise-run`.
- Started at: 2026-10-11T11:43:56Z (UTC).
- Slice: run only premise parts 1-3 in the gate; no branch source edits, handoff-file removal, packaging, or first hardening gate.
- Branch: `solr-8628-submit`, live head `ce8211e05e062444e7afc1c5ea33fb6eff581d8f`, verified with `ls-remote` at claim time.
- Worktree: `/workspace/gates/solr-8628-premise-build` (prepared after claim publication).
- Logs: `/workspace/gates/logs/vm3/8628-premise-*.log`.
- Status: CLAIMED. No probe or test has started.
- Heartbeat: 2026-10-11T11:46:02Z (UTC), claim remains live, branch tip verified, and missing metadata restored in this record. No probe or test has started; next step is record the current-main scope finding, then run the Lucene reachability probe.
- Heartbeat: 2026-10-11T11:46:26Z (UTC), premise part 1 confirmed against current upstream main `d76fc14b9857e77b830d7a82514b28c523a239c2`: empty directory is absent by `exists()` semantics; write.lock-only remains in scope. Evidence: `/workspace/gates/logs/vm3/8628-premise-scope.log`. Next: Lucene 10.4.0 reachability probe.
