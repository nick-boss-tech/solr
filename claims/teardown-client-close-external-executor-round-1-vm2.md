# Claim: vm2 for assignments/teardown-client-close-external-executor-round-1.md

- Claimant host: vm2. Started: 2026-10-26T02:14:59Z.
- Scope: one control and one interrupted invocation, at most two filtered invocations total, counting any setup failure. No retry, no third run, no budget exception.
- Source: apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`, dedicated worktree `/workspace/worktrees/teardown-ext-r1`.
- Base scaffold: v3.1 cumulative diff, SHA-256 `dee219511e648f566c73893b15542e4f76198ff4a8b090c8bad19d1e4f325c05`, adapted to the two new methods (`testExternalExecutorCloseProbeControl`, `testExternalExecutorCloseProbeInterrupted`).
- Duplicate protection: nonblocking driver lock `/workspace/tools/.teardown-ext-pair.lock` (distinct from the host build lock), process-tree check before launch, attempt ledger `/workspace/gates/gates-vm2/ext-attempt-ledger.txt`, exclusive evidence directories created with `Files.createTempDirectory` under java.io.tmpdir.
- Runner: `pool/bin/sweep-run.sh` under `/workspace/tools/.gradle-vm2.lock`.
- Observation cap 30 s per original close; cleanup up to 30 s serial, noninterrupted.
- Earlier round 2 overrun and overwritten first control remain on record; this claim does not approve them.
- Decision ladder: rung 2, the bounded ownership probe the review asked for.
