# Claim: pool-standing-calls-round-1-3

- Claimant host: windows (review agent, review-rounds coordinator lane).
- Job or assignment: `assignments/pool-standing-calls-round-1.md`, item 3 (SOLR-9637, submission shape against SOLR-17393).
- Slice: item 3 only.
- Capability tags used: review, decision, draft.
- Started: 2026-10-15T15:35:00Z (UTC).

Heartbeat: 2026-10-15T15:35:00Z

Runs under the standing coordinator claim, `claims/coordinator-review-rounds.md`. Result goes in `reports/standing-calls-round-1.md`. No live GitHub writes.

Closing note (2026-10-15, item 3): outcome the ratified shape is confirmed. SOLR-17393 submits first (merge-only scope); SOLR-9637 follows, rebased onto the landed tip. Settled on rung 1: the 17393 tip is merge-only, the 9637 stack rebases cleanly (dry run), and fold or rework would cost a combined gate and a fold by another name. Changes: `reports/suggester-round-4.md` (appended settlement). The 9637 draft already matches; no edit. Settled, not returned to Nick. DONE mark left to the coordinator.

DONE: 2026-10-15T16:01:00Z. Deliverables: reports/standing-calls-round-1.md (item 3 settled, ratified shape confirmed; no draft edit).
