# Claim: SOLR-18145 TaskManagement queryUUID and taskUUID names (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Job or assignment: none on the board. Chosen from Jira SOLR-18145 (reporter Eric Pugh, Open, Minor, created 2026-03-03, one comment). Picked on Nick's go, 2026-10-27, from the no-PR list in the fresh-Eric review.
- Slice: read the code at apache/solr main `e59ce3c97d`; decide whether the v1 parameter names change; draft a Jira comment only.
- Capability tags used: review, draft.
- Started: 2026-10-27T01:33:00Z (UTC, approximate).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18145@e59ce3c97d.
- Links: Ticket: SOLR-18145. Category: review. Siblings: SOLR-18138, 18405, 18428-18431, 18461, 18546, 18527-18529 (each its own claim).
- Decision ladder: rung 1 (code at the pinned SHA: TaskCancellationHandler, ActiveTasksListHandler, ActiveTaskQuerySupport, TasksApi, CommonParams, task-management.adoc).

Heartbeat: 2026-10-27T01:33:00Z
Result: 2026-10-27T01:33:52Z: no code change proposed. The v2 API already uses `taskID` for both status and cancel; renaming the v1 `queryUUID`/`taskUUID` would be a breaking change. Draft asks Eric which target he wants. Report `reports/solr-18145-taskmanagement-names-round-1.md`.

DONE: 2026-10-27T01:33:52Z. Deliverables: `reports/solr-18145-taskmanagement-names-round-1.md`, `jira/jira-comment-SOLR-18145-draft.md`.
