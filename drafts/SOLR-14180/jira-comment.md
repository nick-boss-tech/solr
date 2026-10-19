We checked this against Solr main at commit 498a09aea97 and found that the assertion this ticket describes is no longer in the code.

What we checked:

- The request handler's error processing no longer asserts that a tragic index error has status 500. The assertion that could replace the real error with an assertion failure is absent, and a search of the code finds no other use.
- The change that removed it was made in 2022 as part of a different change, not for this report.

How we tried to reproduce it:

- Not applicable. The code the report describes is no longer present, so the failure cannot occur in that form.

What this means:

- The assertion that masked the real error is gone on current main. Whether the error reported in its place is always the right one was not tested.

If you still see this:

- If a request returns an assertion error in place of a real error on a current release, please send the Solr version, the request, and the log, and we will check it.
