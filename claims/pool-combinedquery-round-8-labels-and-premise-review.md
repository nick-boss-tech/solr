# Claim: CombinedQueryComponent round 8 labels, and the SOLR-15319 premise verdict check (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft
- Assignment: the round 8 verdict in `reports/combinedquerycomponent-ticket-review-round8.md`, part (c), C1 to C14, and the premise record `gates/SOLR-15319-8051-premise.md`.
- Staffing: five read-only subagents. Four check the C-items in groups of three or four, against the code at main `d76fc14b9857` in `source/`. One checks the SOLR-15319 premise record against its spec and the code at the 15319 head.
- Scope: apply each C-item that the check confirms, as a labelled replacement in `material/combinedquerycomponent-ticket-draft.md`. Do not apply an item the check does not confirm. The premise record is read only. Its verdict is the main side's to record.
- Hold: no PR, no comment, no branch edit, no build, no test, no Gradle run. The 15319 gate was not relaunched and will not be relaunched by this round.
- Deliverable: `reports/combinedquerycomponent-round-8-labels-round-9-prep.md`.
- Heartbeat: 2026-10-13T00:18Z (claim taken).
- Status: CLAIMED.
