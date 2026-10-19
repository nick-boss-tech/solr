We checked this against Solr main at commit 498a09aea97 and found that the fix linked from this ticket is on main and the test passes there.

What we checked:

- The fix linked from this ticket (commit 73c06e35e8b, September 2020) is on main. It adds one check in the core loading path of CoreContainer, and that check is still present.
- The test class has no known-failure marker on main.

How we tried to reproduce it:

- We ran the test class once on main, and it passed. One run does not rule out an intermittent failure.

What this means:

- The failure in this ticket appears to be addressed by the linked fix. The ticket did not include the failure output, so the link is based on the fix and the ticket's comment.

If you still see this:

- If the test fails on a current release, please send the test output and the Solr version, and we will check it against the fix.
