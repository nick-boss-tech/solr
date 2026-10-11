# Claim: SOLR-8628 premise-only run

- Claimant host: vm3.
- Assignment: `assignments/pool-shared-gate-backlog-buffer.md`.
- Job: `gates/SOLR-8628-premise.md`.
- Capability tags: `premise-run`.
- Started at: 2026-10-11T11:43:56Z (UTC).
- Slice: run only premise parts 1-3 in the gate; no branch source edits, handoff-file removal, packaging, or first hardening gate.
- Branch: `solr-8628-submit`, live head `ce8211e05e062444e7afc1c5ea33fb6eff581d8f`, verified with `ls-remote` at claim time.
- Worktree: `/workspace/gates/solr-8628-premise-build`, prepared after claim publication and clean at the branch head.
- Logs: `/workspace/gates/logs/vm3/8628-premise-*.log` plus the exact evidence files named below.
- Status: CLAIMED. No Lucene reachability probe or Solr test has run. Premise part 1 is complete; part 2 is not yet complete.
- Heartbeat: 2026-10-11T11:43:56Z (UTC), live claims refreshed and branch tip verified; next step was prepare the isolated premise worktree and inspect the exact source/test context.
- Heartbeat: 2026-10-11T11:46:02Z (UTC), restored the missing assignment, job, capability, branch, worktree, and log fields in the initial claim record; no probe or test had started.
- Heartbeat: 2026-10-11T11:46:26Z (UTC), premise part 1 confirmed against current upstream main `d76fc14b9857e77b830d7a82514b28c523a239c2`: an empty directory is absent by `exists()` semantics; write.lock-only remains in scope. Evidence: `/workspace/gates/logs/vm3/8628-premise-scope.log`.
- Heartbeat: 2026-10-11T11:50:22Z (UTC), the first scratch-probe `javac` invocation exited 1 because the public class name did not match the evidence filename; no Lucene probe or test ran. Evidence: `/workspace/gates/logs/vm3/8628-lucene-probe-compile.log` and `8628-lucene-probe-compile-headroom.log` (21 GB available; 8,807 free inodes before and after; no active Gradle client or test, only sleeping daemon/workers). Before proceeding, removed only 29 verified ignored, untracked generated `build/` directories from the completed SOLR-11479 checkout after verifying its GREEN receipt and central evidence; preserved source, `.git`, Gradle caches, and logs. Restored the missing focused-head output from the matching JUnit `system-err` (fresh XML SHA matched the worktree copy); cleanup evidence: `/workspace/gates/logs/vm3/8628-prereq-cleanup.log`. Free inodes rose to 17,346; disk remains 21 GB available. No source or test changed. Next step: compile a matching-name scratch copy after a fresh preflight.
- Heartbeat: 2026-10-11T11:51:34Z (UTC), a later compile-preflight command stopped before javac when a status guard did not match; no probe or test ran. No corrected-compile log or output directory was created. The active claim metadata is being restored in full before another attempt.
