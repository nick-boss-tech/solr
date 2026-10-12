# Claim: SOLR-15358 Proof re-check after the main-side fill (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Trigger: `a940d43d7c9` filled the Proof placeholder in `pr-drafts/schema-analysis/SOLR-15358.md` from the 2026-10-12 proof record in `receipts/SOLR-15358.md`. The schema convergence round 2 record (`reports/schema-convergence-round-2.md`, item 3) said the placeholder fill needs a re-check before the draft can close.
- Scope: the filled Proof line and the INTERNAL header of `pr-drafts/schema-analysis/SOLR-15358.md`, checked against the proof record in `receipts/SOLR-15358.md` and the branch at `cbb345f2e7df452e20036d357c82cc63db96ffd3` (ls-remote confirmed; merge-base with upstream/main is `b5c71bc5573c`). Read-only.
- Staffing: 2 read-only subagents, four tasks each.
- Deliverable: `reports/schema-recheck-15358-proof-round-1.md`, verdict first.
- Not in scope: SOLR-17372 (its retire disposition is the main side's call), SOLR-15357, and the schema drafts that converged in round 2.
- Heartbeat: 2026-10-12T10:23Z (claim taken).
- Status: DONE. Deliverable: `reports/schema-recheck-15358-proof-round-1.md`, verdict NOT CONVERGED on the Proof wording: the numbers, names, tree and date match the proof record; two should-fix items remain (SCH-R3-01 "passed" with 21 skipped, SCH-R3-02 one line per test class). The two holds in the header stand.
