# Claim: SOLR-15712 UDVAS version gate (corrected test class, full gate)

Status: DONE 2026-10-18 (GREEN at 6c3d87dc98 on solr-15712-submit; receipt receipts/SOLR-15712.md; job gates/SOLR-15712-udvas-version-gate.md)

- Claimed: 2026-10-17T22:20:57Z by vm3. Job: gates/SOLR-15712-udvas-version-gate.md.
- Head: 2d65ed6329 (rebuilt from cb988a2ff4, see job file and gates/SOLR-15712-udvas-test-fix.md).
- Capability tags: implementation, gate.
- Runner: ./gradlew with the env file tools/vm3-env.sh, under flock /workspace/tools/.vm3-gradle.lock. Worktree: /workspace/worktrees/15712.
- Push rule: fast-forward only, and only on green. Gate failures are recorded in the job file before any relaunch.

Heartbeat: 2026-10-17T22:20:57Z (claim).

- 2026-10-17T23:33:41Z: heartbeat. Implementation commit adfaa87733 (on cb988a2ff4, unpushed). Tidy clean on rerun (first tidy run reflowed the new test's Javadoc and invalidated that run; the amend folds that in). Error Prone compile passed. Focused run in progress at this heartbeat. Next: pre-fix proof, module check.

- 2026-10-18T00:20Z: DONE. Deliverables: receipts/SOLR-15712.md, gates/SOLR-15712-udvas-version-gate.md (vm3 relaunch record), fork solr-15712-submit at 6c3d87dc98.
