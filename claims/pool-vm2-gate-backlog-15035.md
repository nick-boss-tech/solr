# Claim: SOLR-15035 base-code run and banked-patch run (gate backlog coordinator)

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job gates/SOLR-15035-base-run.md: none linked)

- Claimed: 2026-10-13T05:48Z. Job: gates/SOLR-15035-base-run.md.
- Head at claim: 12d7491e82cd660ad55063cb85633b7e05e1918c (solr-15035-submit, verified by ls-remote at claim time).
- Merge-base: b5c71bc5573c4e31b4cee5a7965d73587fc0ae58 (`git merge-base upstream/main 12d7491e82c`).
- Capability tags: gate.
- Runner: gates/g15035-gate.sh, worktree /workspace/gates/solr-15035-base (detached at the merge-base). The worktree's production and test files are swapped between the two runs; no code commit, no fork push.
- Status: RUN ENDED 2026-10-13T06:30Z, gate log logs/vm2/queue-g15035-gate-20261013T060752Z.out. Base run rc=1 (expected premise failure: `expected:<2> but was:<null>`, tests=1 failures=1). Banked-patch-alone run rc=0 (tests=3 failures=0). Receipt section written and job DONE by vm2 on 2026-10-13T08:05Z from the GATE END log; the claim is DONE.
