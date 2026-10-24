# Claim: pool-ui-SOLR-17850

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`, one ticket per claim. Ticket type: screen (the last claim was a component, SOLR-17909, owner complaint 5).
- Slice: SOLR-17850 (solr.SearchHandler form), gate only. The implementation is on fork branch `solr-17850-search-handler-form` at `887778ce5d`, based on `c8ab71430f`, with `SearchHandlerForm.kt` and `SearchHandlerFormTest.kt`. This claim verifies the branch's tests and spotless on a fresh checkout. No code change.
- Open question from the index-query report (not settled by this gate): the ticket's field set needs Christos's answer (report verdict NEEDS CHRISTOS). The gate verifies the branch as written; it does not decide the field set.
- Capability tags used: ui, jvm-tests.
- Started: 2026-10-24T08:54:50Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-ui-SOLR-17850@887778ce5d.
- Links: Ticket: SOLR-17850 (parent SOLR-17886). Category: ui. Siblings: `claims/pool-ui-SOLR-17849.md` (the same form also appears in the 17849 screen branch, gated at b91f75ed93). Supersedes: none.
- Decision ladder: rung 1 for the branch's files and the test list. GL: to be read from the form code in the Result.
- Constraints: no JIRA or GitHub post, no PR, no push to the branch. Commit as Nick Shanin with no trailers.

Heartbeat: 2026-10-24T08:54:50Z

Run: job `pool-ui-17850-887778ce5d-085936`, log dir `/workspace/gates/logs/vm6/pool-ui-17850/`, head `887778ce5d` (fork branch, not pushed by this claim), started 2026-10-24T08:59:44Z (UTC). Steps: spotless, then `:shared:jvmTest --tests org.apache.solr.ui.views.searchhandler.SearchHandlerFormTest`, through `pool/bin/bg-steps.sh` with the stand-in libEGL and SOFTWARE_FAST.

Heartbeat: 2026-10-24T08:59:44Z
