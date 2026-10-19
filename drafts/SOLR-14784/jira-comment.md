We checked this against Solr main at commit 498a09aea97 and could not reproduce the failure with the seed in the report.

What we checked:

- The test in the report now sets up a mock file-system directory factory, and the segment and file assumptions described in the comments have not yet been traced in the current code.

How we tried to reproduce it:

- We ran the test class with the seed and options given in the report, and all seven tests passed.

What this means:

- The failure does not show with this seed on current main. We have not yet confirmed why, so this is not a closed case.

If you still see this:

- Please send the Solr version, the Lucene version, and the failing log for the seed. Please reopen with that information.
