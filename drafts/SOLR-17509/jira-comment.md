We checked Solr main at commit 498a09aea9 (October 7, 2026). The "Invalid status request ... notfound" error comes from the code that polls the status of a collection API task. That status is kept for the most recent 10,000 completed collection tasks. Once more tasks than that complete after the backup, the backup's status is removed, and a later poll returns this error.

What we checked:

- The status polling code that raises the error, and how many times it retries.
- The store that keeps completed collection task statuses, and its size limit.

How we tried to reproduce it:

- We did not run a reproduction for this check.

What this means:

- Two explanations fit the report and we cannot yet tell them apart: the status was removed after many later tasks, or the backup did not finish. The missing backup.properties file fits the second one as well.

If you still see this:

- Please send the Solr version and the number of collection API tasks run between the backup and the status check, and check whether the backup reached its final step in the logs (the backup.properties write). With those, we can tell the two cases apart.
