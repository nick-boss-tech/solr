# Claim: warm-ticket-evidence-units-round-3-unit-2-SOLR-17895 (attribution correction)
- Claimant host: `phone-agent-2`, matching `hosts/phone-agent-2.md`.
- Status: LIVE.
- Job: `assignments/warm-ticket-evidence-units-round-3.md`, unit 2 only.
- Snapshot: live `coordination-ui` tip `f02bbc65c1d9d58fb8de50b259d2004e9def0799`, fetched 2026-10-25T15:32:08Z. Unit 2 was open; no matching claim, report, or Jira draft existed on the live board.
- Slice: trace the distributed collection-command runner's lock-node lifecycle on current Apache Solr main for SOLR-17895; identify which nodes persist and whether the ticket's reported leak still exists after the Curator changes. Deliver `reports/solr-17895-znode-leak-trace-round-1.md`; add `jira/jira-comment-SOLR-17895-draft.md` only if the trace establishes a point the live thread has not already covered. No Jira/GitHub posting, PR, ticket-state change, code edit, build, or test.
- Capability tags used: `draft`, `records`.
- Decision ladder: rung 1 (current code, tests/specs, and history), then rung 3 for the ticket's stated concern. Separate source evidence from any untested runtime hypothesis.
- Supersedes: `claims/warm-ticket-evidence-units-round-3-unit-2-SOLR-17895-phone-agent-2.md` (commit `2325ed98cb8b43cbe7511f5552b4e1fb28e28b71` was recorded as `Nick <nick.boss.us@gmail.com>` by the contents API, not the required `Nick Shanin` identity; the immutable original remains on the branch).
- Started: 2026-10-25T15:33Z (UTC). Attempts: 2.