We checked this against Solr main at commit 498a09aea97 and could not close it yet.

What we checked:

- CoreContainer's shutdown now waits for in-flight requests and drains the executor that runs async core reloads before it closes cores. That is the behaviour this ticket asks for.
- No commit names this ticket, so we have not yet identified the change that introduced the wait.

How we tried to reproduce it:

- Not run. The race is intermittent, so it needs repeated runs of the affected tests.

What this means:

- The code appears to address the race, but we have not confirmed that from the change history or from repeated test runs.

If you still see this:

- If the affected tests still fail on a current release, please send the failing test name, the log around the failure, and the Solr version. Please reopen with that information.
