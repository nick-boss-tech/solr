# Assignment: new-ui-index-query-components-round-1 (assess Christos's Index & Query and shared-component UI tickets)

- Status: OPEN (unclaimed). Requested by phone agent on behalf of Nick Shanin, 2026-10-23.
- Scope: research only. Assess the eight tickets below against the new UI code at the repository root (`ui/`, Kotlin Multiplatform) on current `main`. Do not change code, do not push to any branch, do not comment on Jira or GitHub, do not open a PR.
- Why: these tickets were filed by Christos Malliaridis (malliaridis), the committer driving the new UI. Pugh has said Christos is interested in Nick's help on the UI effort. Before Nick volunteers publicly on any of them, each ticket needs a grounded read: what already exists, what is actually missing, and what the smallest startable slice is.

## Tickets

- SOLR-17848: Introduce Index & Query in New UI (Open)
- SOLR-17849: Implement Index & Query section header (Open)
- SOLR-17850: Implement solr.SearchHandler UI form (Open)
- SOLR-17886: Implement Reusable Components in New UI (Open)
- SOLR-17887: Implement File Tree Component (Open)
- SOLR-17909: Implement a Breadcrumb Navigation Component (Open)
- SOLR-17911: Implement File Content Viewer Component (Open)
- SOLR-17660: Implement basic list component in Admin UI (Open)

## Steps

1. Read each ticket in full (description and comments) via the Jira REST API.
2. In the `ui/` module on `main`, inventory the existing shared components and the Index & Query area, if any. Record what is already implemented (file paths), including anything that makes a ticket partly or wholly stale.
3. For the SearchHandler form (SOLR-17850), note which request parameters and APIs the form must cover per the ticket, and whether the ticket text enumerates them or leaves the set open.
4. Note dependencies between these tickets and the Collections/Configsets set (file tree and file viewer are presumably shared with configset editing).

## Deliverable

- `reports/new-ui-index-query-components-round-1.md`: one section per ticket with (a) what the ticket asks, in one or two sentences; (b) what exists today, with file paths; (c) what is missing; (d) a verdict: READY TO VOLUNTEER (a newcomer could start from the ticket text plus the codebase), NEEDS CHRISTOS (a design question only the ticket author can settle, stated in one line), ALREADY DONE (evidence), or TOO BIG AS STATED (with the smallest first slice named).
- A closing section naming the single best first contribution for someone new to Kotlin Multiplatform, with the reason in two sentences.
- Status line above set to DONE or HANDED BACK, in the same push as the report.
