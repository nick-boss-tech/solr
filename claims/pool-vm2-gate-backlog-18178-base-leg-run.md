# Claim: SOLR-18178 base-leg record run for the receipt's focused set (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T13:48Z by vm2. Job: gates/SOLR-18178-base-leg-run.md.
- Head at claim: b75e7d4d3c464ceeb9f14fdaf7711212adcccac2 (solr-18178-verify, live tip). Merge-base with upstream/main: 0d2a4649c79a, the receipt's recorded base.
- Capability tags: gate.
- Runner: /workspace/gates/g18178-gate.sh. Scratch base worktree /workspace/gates/solr-18178-base at 0d2a4649c79a, uncommitted, never pushed: the branch's changed test files overlaid, UploadConfigSetAPITest.java and DownloadConfigSetAPITest.java reverted to base.
- Status: CLAIMED, queued behind the running SOLR-17680 v2 proof leg. No branch change, no fork push, no PR touched.
- 2026-10-13T14:11:36Z vm2 gate backlog coordinator: DONE. Base-leg record run finished (GATE END 14:11:36Z, log logs/vm2/queue-g18178-gate-20261013T140438Z.out). At base 0d2a4649c79a: DeleteBackupCmdTest 3 tests, 0 failures; TestFileSystemConfigSetService 3 tests, 0 failures; TestSchemaDesignerConfigSetHelper 8 tests, 0 failures. Receipt base-leg section at receipts/SOLR-18178.md; job gates/SOLR-18178-base-leg-run.md DONE. No branch change, no fork push, no PR touched.

Status: CLOSED (vm2 audit, 2026-10-16T00:55Z). Job gates/SOLR-18178-base-leg-run.md is DONE on pr-prepare; no work remains under this claim. Earlier status lines are kept as history.
