# Claim: CombinedQueryComponent Jira ticket draft review

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-combinedquerycomponent-ticket-review.md`
- Date: 2026-10-11
- Staffing: 2 read-only subagents, where the assignment says 1, because the scheduled check asks for at least 2. Slice 1 checks the facts: criteria 1 to 4 and 10 (title, line references, reproduction values, verification level, the Related line). Slice 2 checks the text: criteria 5 to 9 (fix-option reasoning, the bracketed item, Jira markup, disclosure form, vocabulary). Each reports as text; the coordinator writes the one report.
- Scope: the ticket draft `material/combinedquerycomponent-ticket-draft.md` may be edited for wording and markup only; any fact disagreement is flagged. The bracketed maximum-fold item stays bracketed and unresolved. No Jira, email or GitHub write. No build, test or gate.
- Deliverable: `reports/combinedquerycomponent-ticket-review.md`, verdict first (SATISFIED or STILL OPEN), each fix with before and after text, each evidence disagreement flagged.
- Heartbeat: 2026-10-11T23:40Z (claim taken).
- Status: ACTIVE.
