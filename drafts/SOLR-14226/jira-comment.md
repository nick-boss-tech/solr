We checked this against Solr main at commit 498a09aea97 and found that the streaming client now reports 401 and 403 responses with the error message from the server.

What we checked:

- The streaming client checks the response status before it reads the body. For 401 and 403 it throws an error that includes the server's message. That change was made in 2023 (SOLR-16929, commit c7b58302d33).
- The tests linked from this ticket in 2020 cover the cases, but they do not change the client.

How we tried to reproduce it:

- We have not run the reporter's test on current main in this pass.

What this means:

- The error in this report appears to be addressed on current main. The link to this report is inferred from the same symptom and code path, since the change is recorded under a different ticket.

If you still see this:

- If a 401 or 403 from a streaming request still gives an IOException without a message on a current release, please send the Solr version, the request, and the full error, and we will check it against the change.
