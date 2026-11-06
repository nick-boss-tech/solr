# Assignment: solr-pr-map, group captured pr activity by month, round 1

Status: CLAIMED (Windows initial implementation; independent review follows in the bundle-review assignment).

Tags: implementation, review, records.
Intended host: any capable implementation host; initial foundation claimed by windows.

## Owner direction and scope

Nick requested assignments followed by first-level implementations for review and further development. The ongoing PR scan remains the archaeology owner; this work consumes existing artifacts only.

Implement offline monthly aggregation of the existing scan census and scan register. Include immutable input hashes, Open/Draft/Merged separation, UTC month semantics and coverage. Prefill from current artifacts without running new scans.

## Deliverables

- Skill and supporting resources at `.agents/skills/solr-pr-map/` on `nick-boss-tech/solr:factory-solr-skills-round-1`.
- Shared package entrypoint `handbook/INDEX.md`, evaluated scenarios in `evaluation/scenarios.json`, and the package manifest on that branch.
- Initial implementation report `reports/agent-skills-foundation-round-1.md` on coordination-ui; this report completes the initial foundation slice, not independent acceptance.
- Initial claim: `claims/agent-skill-solr-pr-map-foundation-windows-round-1.md`.

## Acceptance and handoff

Monthly totals reconcile to inputs; a late merge is grouped by mergedAt, open backlog by createdAt; stale scan heads are visible and not current review evidence.

Validate skill frontmatter and linked local resources. Evaluate behavior with realistic input packets rather than asserting that particular wording exists. Record which checks ran and which independent evaluations remain pending. The separate `assignments/agent-skills-bundle-review-round-1.md` owns review of all six initial implementations. Later repairs are new scoped claims/reports, not edits to this assignment.

## Boundaries

No broad source archaeology, new PR scans, Solr builds on Windows, upstream source changes, public messages, skill installation on other hosts, or scheduled jobs. A skill inherits current user authorization and does not grant new authority. Retain existing canonical policy instead of copying an entire AGENTS.md into each skill. The monthly index is captured metadata, not proof of correctness or a complete Solr history.
