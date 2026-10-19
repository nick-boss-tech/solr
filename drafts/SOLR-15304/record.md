# Skip record: SOLR-15304

- Ticket: SOLR-15304, collection-admin-edit permissions should imply core-edit permission. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-03-31, updated 2021-05-11, no fix versions, linked to SOLR-13097 (Open), no comments.
- Verdict: keep-open.
- Draft branch: solr-15304-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and every cited name grepped at this head (detached worktree).
- Source: remediation redo of batch 03, item 16, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) cited a feature-proposal reading from audit evidence; not checked here.

## Ticket reading

With a RuleBasedAuthorizationPlugin rule `collection-admin-edit` for role admin and `all` set to none, a collection CREATE fails because the user lacks permission to create cores. The reporter tested on a 9x snapshot. The ticket asks that collection-admin-edit imply core-edit.

## Code reading and grep at the pinned head

- `git grep -n "COLL_EDIT_PERM" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java` finds the collection-admin-edit permission used by collection-level handlers, for example solr/core/src/java/org/apache/solr/handler/ClusterAPI.java:224 and solr/core/src/java/org/apache/solr/handler/admin/ContainerPluginsApi.java:73.
- solr/core/src/java/org/apache/solr/handler/admin/CollectionsHandler.java:236-247: the collection action maps to COLL_EDIT_PERM for write actions (`collectionAction.isWrite`).
- solr/core/src/java/org/apache/solr/handler/admin/CoreAdminHandler.java:280-285, `getPermissionName`: the core action maps to CORE_EDIT_PERM for write actions.
- solr/core/src/java/org/apache/solr/security/PermissionNameProvider.java:41: `COLL_EDIT_PERM("collection-admin-edit", null)`. Line :44: `CORE_EDIT_PERM("core-admin-edit", null)`. So the core permission at the pin is named `core-admin-edit`, not `core-edit` as the ticket says.
- The permission table at PermissionNameProvider.java:41-59 has no implied-permission list: each permission is a separate name with a path or collection restriction. So at the pin, collection-admin-edit does not imply core-admin-edit by any table.

A collection CREATE makes core-create requests on the nodes. Whether those sub-requests are checked against core-admin-edit for the original user (and so fail as the ticket reports) depends on how the sub-requests are authorized. That path was not read in this pass.

## History

- No commit names SOLR-15304. Linked SOLR-13097 is Open.

## Reproduction attempts

Not run. A reproduction needs a secured cluster with the rule in the ticket, a user whose only grant is collection-admin-edit, and a collection CREATE. This pass did not start a cluster.

## Gap analysis

The ticket's permission name is out of date (core-edit is now core-admin-edit), and the feature (implication between the two) is not in the code. What is not shown is whether the collection CREATE actually fails for the user on the pin: that depends on the internal sub-request authorization, which this record did not read.

## Verdict justification

keep-open: the attempts made are the code path reads and greps above at the pinned head, the Jira read, and the linked-ticket check. The missing piece is (1) the run: a user with only collection-admin-edit and a collection CREATE on the pin, with the result; and (2) the sub-request authorization path, to say whether the failure is expected. Whether the change is a feature (implication) or a bug (CREATE failing for a user the rule seems to allow) is a maintainer decision. No close or PR verdict is supported without those.

Missing for a closing or PR verdict: the run output and the sub-request authorization reading.

## Limits

This record does not reproduce the failure. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
