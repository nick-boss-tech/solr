# Claim: Query parsing, full-category review to SATISFIED

Claimed 2026-10-13T14:40Z by Claude Code (AI agent) on the Windows review host, working for Nick Shanin.

Host: `windows` (see `hosts/windows.md`).
Capability tags used: `review`, `draft`. No build work: no Gradle, no gate, no test run on this host.
Assignment: `ASSIGNMENT.md` on this branch (Query parsing, full-category review to SATISFIED).
Slices: the whole category (18 drafts under `pr-drafts/query-parsing/`, 25 round 1 tickets), split into sub-batches in the verdict reports. The lane is one claim, so the heartbeat covers all slices.

## Heartbeat

- 2026-10-13T14:40Z: claim recorded (commit `9ecdc53f8a2`). Pushed 2026-10-13 through `research\push-pr-prepare.ps1 -Branch assignment-query-parsing-review`, after the wrapper was given a branch allowlist at the user's direction.
- 2026-10-13T15:30Z: all 18 drafts checked against receipts, live tips, citations, merge-bases and Lucene release sources. Verdicts and wording fixes in `reports/query-parsing-review-q1.md` to `-q4.md` and `reports/query-parsing-review-summary.md`. Three drafts OPEN (gate): 6014, 12608, 17796. Gate requests G1 to G3 in the summary, for the gate coordinator to stock.

## DONE status

Not done. The assignment is open on three gate-bound items (SOLR-6014, SOLR-12608, SOLR-17796) and on the live-PR text findings for SOLR-16130 that the main side must apply. Claim stays active on the review lane until those items reach a gated head.
