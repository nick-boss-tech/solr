# SOLR-18145 TaskManagement parameter names: code read

Ticket: SOLR-18145, "Resolve the use of queryUUID and taskUUID in the TaskManagement API" (Open, Minor, reporter Eric Pugh, created 2026-03-03). Body: the cancel API uses `queryUUID` and the task list API uses `taskUUID`; "need to clean this up." One comment (Eric, 2026-03-03) points at SOLR-6122 as a possible related issue.
Pinned main: `e59ce3c97ddb`.

## What the code does

1. Two v1 parameter names carry the same task ID:
   - `queryUUID` is the cancel parameter and the query's custom ID. `CommonParams.java:181` (`QUERY_UUID`). Read by `TaskCancellationHandler.java:39` and by `QueryComponent.java:1902` and `CancellableQueryTracker.java:40` (the caller can set its own ID).
   - `taskUUID` is the status-check parameter. `CommonParams.java:184` (`TASK_CHECK_UUID`). Read by `ActiveTasksListHandler.java:44`.
2. The v1 names are the documented public API: `solr/solr-ref-guide/modules/deployment-guide/pages/task-management.adoc` shows `tasks/cancel?queryUUID=5` (line 120) and `tasks/list?taskUUID=5` (line 217), and `common-query-parameters.adoc:112` documents `queryUUID` for queries.
3. The v2 API already uses one name for both operations: the task ID is a path segment. `TasksApi.java:44-51` (`GET /tasks/{taskID}`, status) and `TasksApi.java:54-61` (`DELETE /tasks/{taskID}`, cancel). The guide says so at `task-management.adoc:232-233`.
4. The two v1 names are also used inside the cross-shard forwarding. `ActiveTaskQuerySupport.java:98-106` sets `QUERY_UUID` for a cancellation request and `TASK_CHECK_UUID` for a status request. Renaming either v1 parameter changes that forwarding too.

## What a rename would do

- Renaming `queryUUID` or `taskUUID` breaks every client that sends the v1 name, and the names are in the guide, so this is a public-API change.
- Adding a `taskID` alias to the v1 handlers would be a new feature, not cleanup.
- The v2 path already gives one name, so no code change is needed to make v2 consistent.

## Verdict

The inconsistency is real, but the only change that would remove it is a breaking v1 rename. No code change is proposed. The question for the reporter is whether the v2 naming (`taskID` in the path) is the target and whether v1 should stay as documented. Draft asks that question.

## Checked and not flagged

- The v1 cancel response code (`TaskCancellationHandler.java:46`-`:53`) and the status response shape differ between v1 and v2 (`task-management.adoc:135`). That is a separate issue and not part of this ticket's title.
