# Claim: census follow-up, scheduled round 8 (a review of the SOLR-16630 fix chain, and the SOLR-3923 premise result)

- Claimed by: windows (review coordinator), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft
- Trigger: the two-hourly scheduled check. Recorded tip `ec03f014b7f`; live tip at the check `82e64cd9330`. The new commits are records: the SOLR-3923 base premise run (39 tests, one failure at the phrase check in `testPfPs`; `gates/SOLR-3923-premise.md`, `claims/pool-vm2-gate-backlog-3923-premise.md`), and three SOLR-16630 receipt commits recording a node-name fix head (`fcd94a443c`) and a forbidden-apis fix head (`c9caf1e4b8`) as gated green, with corrections to earlier green accounts. No subagent from the previous round was still running.
- Fork tips at the check: `solr-16630-submit` at `c9caf1e4b815795fd95b9ab454ab41ea0e8b0961`; `solr-3923-submit` at `723f61ee4ab7438220193db0c39dad32601dfe07`.
- Scope this round (two subagents):
  - S1, review (read only): SOLR-16630. Verify the three recorded heads (the all-sleeps conversion `136145b598d`, the node-name fix `fcd94a443c`, the forbidden-apis fix `c9caf1e4b8`) against the fork and the receipt. Check each gate record for what its file on disk supports, and each correction for consistency with the commits. Check the live PR head through the read-only API. State whether the live PR body names the current head. Do not edit the PR body, the draft, or the receipt.
  - S2, settlement: SOLR-3923 (`pr-drafts/edismax/SOLR-3923.md`). The premise run is recorded. Update the draft's base-code claim to what the premise record shows, with its date and head. Keep the HOLD: the signed-parenthesis gap (`+(` and `-)`) needs a branch fix first, and the premise run does not test it. Verify the draft's head citations against `723f61ee4ab7438220193db0c39dad32601dfe07`.
- Limits: no PR opened, closed, flipped or commented on; no `solr-*-submit` branch touched; no live PR body edited; no gate job run; no build, test or Gradle on this host; nothing posted.
- Staffing: two subagents. Each may write only the draft file named above under `pr-drafts/`, and gate job files under `gates/` if needed. None pushes, opens a PR, moves a branch, or edits a receipt, a claim or a gate record.
- Deliverable: `reports/census-followup-scheduled-round-8.md`, with one line per ticket and the SOLR-16630 chain review.
- Heartbeat: 2026-10-14T18:37Z (claim taken).
- Status: IN PROGRESS.
