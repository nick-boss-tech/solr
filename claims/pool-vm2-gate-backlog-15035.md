# Claim: SOLR-15035 base-code run and banked-patch run (gate backlog coordinator)

- Claimed: 2026-10-13T05:48Z. Job: gates/SOLR-15035-base-run.md.
- Head at claim: 12d7491e82cd660ad55063cb85633b7e05e1918c (solr-15035-submit, verified by ls-remote at claim time).
- Merge-base: b5c71bc5573c4e31b4cee5a7965d73587fc0ae58 (`git merge-base upstream/main 12d7491e82c`).
- Capability tags: gate.
- Runner: gates/g15035-gate.sh, worktree /workspace/gates/solr-15035-base (detached at the merge-base). The worktree's production and test files are swapped between the two runs; no code commit, no fork push.
- Status: CLAIMED, queued behind the running SOLR-9155 gate.
