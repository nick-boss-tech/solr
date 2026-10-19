We checked Solr main at commit 498a09aea9 (October 7, 2026). The Admin UI loads the current user's roles and permissions once, when the page starts. It does not reload them after a security change, so the menus can stay out of date until the page is reloaded. This matches the report.

What we checked:

- Where the Admin UI loads the permission list, and where it is used to show or hide menus and buttons.
- Whether the security editing screen updates the list after a save. It does not.

How we tried to reproduce it:

- We did not run the Admin UI in a browser for this check.

What this means:

- The behavior is as reported, and the impact is small. Whether the Admin UI should refresh the permission list after a security change is a decision for the Admin UI owners, so we are leaving the ticket open for that.

If you still see this:

- Please tell us whether you expect the page to update after a permission change, or whether a reload is acceptable. With that, we can make the change or close the report.
