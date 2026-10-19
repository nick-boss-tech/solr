We checked this against Solr main at commit 498a09aea97 and could not confirm the leak yet.

What we checked:

- The core close path releases a core's references and logs an error if a core is released too many times. We have not traced every path that removes a core to confirm that the last reference and its plugin classes are released.

How we tried to reproduce it:

- Not run. The check needs the create-and-drop loop from the report running for a long time, with the loaded-class count recorded.

What this means:

- We cannot say yet whether loaded classes keep growing on current main. The investigation in this ticket is still open.

If you still see this:

- Please send the Solr version, the loaded-class count over time, and the steps you used to create and remove collections. Please reopen with that information.
