# Assignment: new-ui-auth-round-1 (assess Christos's new UI authentication tickets)

- Status: DONE. Deliverable `reports/new-ui-auth-round-1.md`; claim `claims/new-ui-auth-round-1.md` (vm6). Requested by phone agent on behalf of Nick Shanin, 2026-10-23.
- Scope: research only. Assess the two tickets below against the new UI code at the repository root (`ui/`, Kotlin Multiplatform) on current `main` and against Solr's security framework. Do not change code, do not push to any branch, do not comment on Jira or GitHub, do not open a PR.
- Why: these tickets were filed by Christos Malliaridis (malliaridis), the committer driving the new UI. Pugh has said Christos is interested in Nick's help on the UI effort. Authentication is the area where a wrong first step is most expensive, so these two get their own read before Nick volunteers publicly on either.

## Tickets

- SOLR-17885: Add support for User Authentication to New UI (Reopened)
- SOLR-17845: Implement authentication with OAuth in Admin UI (Patch Available)

## Steps

1. Read each ticket in full (description, comments, and for SOLR-17845 the attached patch and its state) via the Jira REST API.
2. Establish why SOLR-17885 is Reopened: what was delivered, what was found wanting, and what the reopening comment asks for now.
3. Establish what the Patch Available state of SOLR-17845 means in practice: whose patch, how old, whether it still applies to the current `ui/` tree, and what review it has had.
4. Map both tickets onto what the new UI already does for authentication (see also SOLR-17659, basic authentication, Resolved) and onto the server's authentication plugins (BasicAuthPlugin, MultiAuthPlugin, JWTAuthPlugin), noting which plugin each ticket targets.

## Deliverable

- `reports/new-ui-auth-round-1.md`: one section per ticket with (a) what the ticket asks now, in one or two sentences; (b) current state and evidence; (c) the open design questions, if any, that only the ticket author or a security reviewer can settle; (d) a verdict per ticket: READY TO VOLUNTEER, NEEDS CHRISTOS, ALREADY DONE, or TOO BIG AS STATED (with the smallest first slice named).
- A closing paragraph on whether either ticket is a suitable first UI contribution, given the security surface.
- Status line above set to DONE or HANDED BACK, in the same push as the report.
