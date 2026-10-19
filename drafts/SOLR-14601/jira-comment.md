We checked this against Solr main at commit 498a09aea97 and could not confirm the error yet.

What we checked:

- The subquery transformer still rejects a second subquery with the same name in one request. The child transformer uses the request's field list for its child documents when no child field list is given.

How we tried to reproduce it:

- Not attempted. The check needs a query with a child document and a subquery in the same field list.

What this means:

- We have not yet confirmed whether the subquery from the request is applied inside the child documents on current Solr, which is what would trigger the duplicate error.

If you still see this:

- Please send the Solr version, the exact field list, and the error with its stack trace. Please reopen with that information.
