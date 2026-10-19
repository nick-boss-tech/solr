We checked Solr main at commit 498a09aea9 (October 7, 2026). The report does not include the error that stopped the node from recovering, and no stack trace or log lines are in the ticket. Without that, we cannot match the problem to a specific place in the code.

What we checked:

- The ticket text and the steps the reporter tried (replica recreation, redeploy, configset reuse, heap increase, snapshot restore). None of these identifies a code path.

How we tried to reproduce it:

- We did not run a reproduction. The ticket does not include the configuration needed to reproduce it.

What this means:

- The steps tried so far suggest a problem with the configuration or the data directory, but we cannot confirm that from the report. Solr 7.7.1 is also well out of date, so the current code may behave differently.

If you still see this:

- Please upgrade to a supported Solr release if you can, and send the log from the failing node at the time it stops recovering, with the exception and the configset you use. With those, we can check the exact failure.
