# Claim: mergeIds PR draft text fixes, round 1 (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-mergeids-pr-draft-text-round-1.md` (`dafb9fea6b6`). Under the standing coordinator claim, `claims/coordinator-review-rounds.md`.
- Staffing: two read-only subagents, not the one the assignment names, because the scheduled round asks for at least two. Subagent A verifies the branch code, the test lines and the receipt counts at `4eb996121804`. Subagent B checks the JIRA text of SOLR-18539 and that SOLR-18541 exists (read only).
- Scope: the draft text at `pr-drafts/search-components/mergeids-skipped-shard-totals.md`, and the report `reports/mergeids-pr-draft-text-round-1.md`.
- Limits: no branch change, no gate, no build, test or Gradle. No edit to the receipt, the reviews or any other draft. No PR, comment, JIRA write or email.
- Heartbeat: 2026-10-13T03:32Z (claim taken).
- Status: DONE. Deliverables: `pr-drafts/search-components/mergeids-skipped-shard-totals.md` and `reports/mergeids-pr-draft-text-round-1.md`. Verdict: READY for Nick's review. Opening still needs Nick's approval.
