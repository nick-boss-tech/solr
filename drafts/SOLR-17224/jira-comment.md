We checked Solr main at commit 498a09aea9 (October 7, 2026) and believe this is fixed by commit a1ebd0433c (SOLR-17721, "null pointer fix in the doAddDistinct method", July 2025). That commit is on main.

What we checked:

- The add-distinct code path in AtomicUpdateDocumentMerger.doAddDistinct at the commit we checked. It now handles an existing field with no values, which is where the null pointer came from in the older code.
- The commit a1ebd0433c, which changes that line and adds a test for it to AtomicUpdatesTest.

How we tried to reproduce it:

- We did not run a reproduction for this check. The conclusion comes from the code history.

What this means:

- The NPE described here should not happen on current main. The fix is the same kind of null check that SOLR-17721 added, and this report is a duplicate of that issue.

If you still see this:

- Please send the Solr version and the exact stored value of the field for the document that fails (for example, the field as returned by a real-time get before the update), and the stack trace from the server log. With those, we can confirm whether the same path is involved.
