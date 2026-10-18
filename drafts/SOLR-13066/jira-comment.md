This was fixed by commit 7de72c9bc7 (December 2018), and the fix is still on Solr main. It was also backported to branch_7x as 4bcad18084.

When reloading a core fails after the new core is registered, the new core is now closed. The change is in CoreContainer.reload, in the finally block (solr/core/src/java/org/apache/solr/core/CoreContainer.java).

What we checked:

- The fix commit is an ancestor of the current Solr main.
- The code that closes the new core on a failed reload is still in CoreContainer.reload.

We have not run a reproduction. This comment rests on the commit and the code path, and the commit adds no regression test.

If you still see a core left open after a failed reload on a current release, please reopen with the steps and the Solr version.
