# Skip record: SOLR-16657

- Ticket: SOLR-16657, "Admin UI should refresh after editing security". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2023-02-15, updated 2023-03-27, no fix version, no comments. The reporter describes a minor inconsistency: after a user with security-edit permission changes permissions in the Admin UI, the UI keeps the earlier role and permission list until the page is reloaded.
- Verdict: keep-open. Unchanged from the earlier record; this record replaces the audit-only record with a pinned-head code check.
- Draft branch: solr-16657-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:47-14:47 UTC.

## Ticket reading

The Admin UI reads the current user's roles and permissions from the system info endpoint and uses them to show or hide menus and buttons. After a security edit, the cached list is not refreshed until a manual reload. The reporter calls it a small inconsistency and notes that users with security-edit permission usually have full access anyway.

## Code reading

- `solr/webapp/web/js/angular/app.js:557-559`: `SystemV2.getNodeSystemInfo` is called once at start-up, and `$scope.usersPermissions = data.security.permissions;` is set from its response.
- `solr/webapp/web/js/angular/app.js:527-529`: `isPermitted` checks `$scope.usersPermissions`.
- `git grep -n 'usersPermissions' 498a09aea97... -- solr/webapp/web/js` returns only those two lines in `app.js`. The security controller (`solr/webapp/web/js/angular/controllers/security.js`) never writes it.

What this shows at the pin: the permission list is loaded once and is not updated after a security edit. The ticket's behavior is present in the code. The fix would be a refresh after a successful security save, but whether to make it is a product call.

## History

- No commit on the pinned head names SOLR-16657 (evidence tool: no hits).

## Reproduction attempts

Not attempted. A run needs the Admin UI with security enabled and a user who edits permissions, which is a browser test; this pass is review-only.

## Gap analysis

The code confirms the staleness. The impact is small by the reporter's own account, so the question is whether a refresh after saving is wanted. No maintainer has responded.

Missing piece for a verdict change: a decision from the Admin UI owners on whether a refresh after a security save is wanted. With that, the change is a small refresh in the security controller, and the record can name it as pr-needed, or close the ticket as won't-fix.

## Verdict justification

keep-open meets the rubric: the code path is read (`app.js:527-529, 557-559`), no reproduction is run, and the missing piece is named (the owner decision). close-fixed and close-obsolete are not met: the behavior is still present. pr-needed is not met as a required change: the ticket calls it minor and the owner has not asked for it. close-answered is not met: no docs answer it.

## Limits

This record does not check the UI in a browser and does not confirm the reporter's exact steps. It does not decide whether the refresh should happen. It does not post anything to JIRA or GitHub.
