# Assignment: new-ui-platform-build-round-1 (assess Christos's new UI platform and build tickets)

- Status: CLAIMED by vm5 in `claims/new-ui-platform-build-round-1.md`. Requested by phone agent on behalf of Nick Shanin, 2026-10-23.
- Scope: research only. Assess the three tickets below against the repository on current `main`. Do not change code, do not push to any branch, do not comment on Jira or GitHub, do not open a PR.
- Why: these tickets were filed by Christos Malliaridis (malliaridis), the committer driving the new UI. Pugh has said Christos is interested in Nick's help on the UI effort. These three are about the Kotlin Multiplatform build and distribution rather than UI features; they are also the natural on-ramp for a contributor whose background is build and server work rather than UI, so they get a separate read.

## Tickets

- SOLR-17970: Start publishing new UI desktop clients to nightlies (Open)
- SOLR-17861: Extend updateLicenses task to cover KMP module (Open)
- SOLR-17676: Enable wasmJs tests (Open)

## Steps

1. Read each ticket in full (description and comments) via the Jira REST API.
2. For SOLR-17970: find how nightlies are currently published for Solr itself, what a desktop client artifact for the new UI would be (targets, packaging), and what infrastructure the ticket presupposes.
3. For SOLR-17861: locate the updateLicenses task and the KMP module's dependency set; state concretely what extending the task involves and whether anything blocks it.
4. For SOLR-17676: locate the wasmJs target and its tests in the `ui/` build; state what enabling the tests involves (runner, browser requirements, CI wiring) and what has kept them off.

## Deliverable

- `reports/new-ui-platform-build-round-1.md`: one section per ticket with (a) what the ticket asks, in one or two sentences; (b) what exists today, with file paths; (c) what is missing; (d) a verdict per ticket: READY TO VOLUNTEER, NEEDS CHRISTOS (question stated in one line), NEEDS INFRA (names the infrastructure or PMC-side step), ALREADY DONE (evidence), or TOO BIG AS STATED (smallest first slice named).
- A closing section ranking the three as first contributions for a build-oriented contributor, with the reason for the top pick in two sentences.
- Status line above set to DONE or HANDED BACK, in the same push as the report.
