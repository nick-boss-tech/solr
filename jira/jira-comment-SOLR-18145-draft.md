# Jira comment draft: SOLR-18145

Status: DRAFT (not posted). Shape: staged ask, evidence first. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `report-18145.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I read the TaskManagement code at apache/solr main {{e59ce3c97d}}. The two v1 names are the public API, and the v2 API already uses one name for both operations:

 * {{queryUUID}} is the v1 cancel parameter ({{TaskCancellationHandler.java:39}}, {{CommonParams.java:181}}). It is also how a caller sets its own query ID ({{QueryComponent.java:1902}}).
 * {{taskUUID}} is the v1 status parameter ({{ActiveTasksListHandler.java:44}}, {{CommonParams.java:184}}).
 * The v2 API takes the task ID as a path segment for both status and cancel ({{TasksApi.java:44-61}}).
 * The ref guide documents the v1 names ({{task-management.adoc:120}} and {{:217}}).
 * Cross-shard forwarding sends both names ({{ActiveTaskQuerySupport.java:104}} and {{:106}}), so a rename changes that path too.

So the v2 side is already consistent. Renaming the v1 parameters would break clients that use them.

Eric, is the v2 path naming ({{taskID}}) the target for this ticket, with the v1 names left as documented? Or do you want a {{taskID}} alias added to the v1 handlers?

Possible states: close with no code change and a note in the guide; keep open for a v1 alias; or keep open for a breaking v1 rename in a major release. The recommendation is the first: the v2 API is already consistent, and an alias adds surface without a clear user need.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
