# Claim: vm2 for assignments/teardown-client-interrupt-probe-round-1.md

- Claimant host: vm2, matching hosts/vm2.md. Started: 2026-10-25T23:53:28Z.
- Slice: the rank-1 deciding probe only (bounded interrupted Jetty client close). Not the other four ranked probes, not the audit.
- Tags used: premise-run, records, review. Not a source contribution; no PR, comment, Jira or upstream write.
- Pinned source: apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`, in a dedicated scratch worktree `/workspace/worktrees/teardown-probe-vm2`.
- Runner: `pool/bin/sweep-run.sh` under the host lock `/workspace/tools/.gradle-vm2.lock`, one invocation with the four method filters from the assignment, seed `FFF5A3C3AAC69084`.
- JDK: `/workspace/jdk/jdk-21.0.12.1+1` (21.0.12.1, Temurin-equivalent per the host file).
- Deliverables (new files only): `reports/teardown-client-interrupt-probe-round-1-scaffold.md` and `reports/teardown-client-interrupt-probe-round-1.md`.
- Cap: 30 seconds per arm, 20 minutes total including compilation. A cap breach is recorded as incomplete.
- Decision ladder: rung 2 (a probe that separates the explanations), as the assignment specifies.
