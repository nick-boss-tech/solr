# Claim: SOLR-11288 base-code run at the live head

- Claimant host: vm2 (gate backlog coordinator). Job: gates/SOLR-11288-base-run.md.
- Capability tags: gate. Staffing: 1.
- Claimed: 2026-10-13T07:37:27Z (UTC).
- Head at claim: cbf0c678f277d6f50d75408d4889d3b664469905 (solr-11288-submit). Merge-base: 97d973814336101e12475558d7419321c743de79.
- Runner: gates/g11288-baserun-gate.sh, worktree /workspace/gates/solr-11288-baserun (detached at the merge-base, branch TestCollectionAPI.java overlaid, no production change). The runner waits for the in-progress 16437 recount to finish before starting Gradle.
- Status: CLAIMED.
