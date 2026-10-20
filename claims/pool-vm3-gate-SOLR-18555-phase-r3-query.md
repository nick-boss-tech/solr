# Claim: SOLR-18555 gate, phase r3 (V2 QUERY feasibility for supplied-text highlighting)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Store: 1 (follow-ups and reviews; posted gate job `gates/SOLR-18555-phase-r3-query.md`).
- Status: CLAIMED
- Job or assignment: `gates/SOLR-18555-phase-r3-query.md`.
- Slice: the whole gate job at head e78e776c3e7fe0cf63aac7f0bd705edfeb60e19d (tip confirmed by ls-remote at claim time, equal to the posted head). Steps 0 to 5 in the worktree, step 6 (ref guide check) in a full clone at the same head, then `receipts/SOLR-18555.md`.
- Capability tags used: gate.
- Started: 2026-10-20T07:20:31Z (UTC).
- Idempotency key: SOLR-18555-phase-r3-query@e78e776c3e.

Heartbeat: 2026-10-20T07:20:31Z

## Launch (2026-10-20T07:23:43Z)

- Worktree /workspace/worktrees/solr-18555-r3 at e78e776c3e; full clone /workspace/clones/solr-18555-r3 at the same head (step 6).
- Gate: /workspace/tools/gate-solr-18555-r3.sh, logs /workspace/gates/logs/vm3/solr-18555-phase-r3/ (result-clone, result-step0, result-tidy, result-compile, result-test, result-check, result-guide .txt; done marker). Seed 18555C0FFEE18555, class `org.apache.solr.handler.HighlightRequestHandlerHttpTest`.
- Heartbeat: 2026-10-20T07:23:43Z
