# Assignment: new-ui-collections-configsets-round-1 (assess Christos's Collections and Configsets UI tickets)

- Status: OPEN (unclaimed). Requested by phone agent on behalf of Nick Shanin, 2026-10-23.
- Scope: research only. Assess the six tickets below against the new UI code at the repository root (`ui/`, Kotlin Multiplatform) on current `main`. Do not change code, do not push to any branch, do not comment on Jira or GitHub, do not open a PR.
- Why: these tickets were filed by Christos Malliaridis (malliaridis), the committer driving the new UI. Pugh has said Christos is interested in Nick's help on the UI effort. Before Nick volunteers publicly on any of them, each ticket needs a grounded read: what already exists, what is actually missing, and what the smallest startable slice is.

## Tickets

- SOLR-17912: Introduce Collections Management UI (Open)
- SOLR-17913: Implement Collections Details Card (Open)
- SOLR-17914: Implement Create Collection Dialog (Open)
- SOLR-17846: Introduce Configset Management in New UI (Open)
- SOLR-18222: Create Configset Overview Scene (Open)
- SOLR-18223: Implement editing for solrconfig.xml (Open)

## Steps

1. Read each ticket in full (description and comments) via the Jira REST API.
2. In the `ui/` module on `main`, locate the existing scenes, components, and navigation for collections and configsets. Record what is already implemented (file paths), including anything that makes a ticket partly or wholly stale.
3. For each ticket, note the API surface it depends on (v2 API endpoints) and whether that surface exists and is stable.
4. Note dependencies between the six tickets (which one unblocks which) and any dependency on tickets outside this set.

## Deliverable

- `reports/new-ui-collections-configsets-round-1.md`: one section per ticket with (a) what the ticket asks, in one or two sentences; (b) what exists today, with file paths; (c) what is missing; (d) a verdict: READY TO VOLUNTEER (a newcomer could start from the ticket text plus the codebase), NEEDS CHRISTOS (a design question only the ticket author can settle, stated in one line), ALREADY DONE (evidence), or TOO BIG AS STATED (with the smallest first slice named).
- A closing section ranking the six as volunteer candidates for a first UI contribution, with the reason for the top pick in two sentences.
- Status line above set to DONE or HANDED BACK, in the same push as the report.
