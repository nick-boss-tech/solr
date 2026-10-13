# Claim: SOLR-18178 base-leg record run for the receipt's focused set (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T13:48Z by vm2. Job: gates/SOLR-18178-base-leg-run.md.
- Head at claim: b75e7d4d3c464ceeb9f14fdaf7711212adcccac2 (solr-18178-verify, live tip). Merge-base with upstream/main: 0d2a4649c79a, the receipt's recorded base.
- Capability tags: gate.
- Runner: /workspace/gates/g18178-gate.sh. Scratch base worktree /workspace/gates/solr-18178-base at 0d2a4649c79a, uncommitted, never pushed: the branch's changed test files overlaid, UploadConfigSetAPITest.java and DownloadConfigSetAPITest.java reverted to base.
- Status: CLAIMED, queued behind the running SOLR-17680 v2 proof leg. No branch change, no fork push, no PR touched.
