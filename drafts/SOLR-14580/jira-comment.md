We checked this against Solr main at commit 498a09aea97 and could not confirm the problem yet.

What we checked:

- The client builder's check on the server address list has changed since this report. It now tests whether the list is empty, not whether a null array was given.

How we tried to reproduce it:

- Not attempted. The check needs a small client subclass that overrides build() with a ZooKeeper host builder.

What this means:

- The check the report describes has changed, but we have not yet found the change that fixed it, or run the failing setup on current main.

If you still see this:

- Please send the Solr version and the client subclass code, and we will run it on current main. Please reopen with that information.
