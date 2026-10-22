# Result: CoreContainer shutdown ticket filed

Status: DONE (filed by Nick; verified by the phone agent against live Jira, 2026-10-22)

- Ticket: SOLR-18564, https://issues.apache.org/jira/browse/SOLR-18564
- Verified live: summary "CoreContainer.shutdown: a client close pool timeout skips the remaining closes and leaks metrics contexts"; issue type Test; priority Minor; status Open; reporter Nick Shanin; created 2026-10-22T06:31:30Z.
- Title and description match the assignment text as filed.
- Next steps sit with the owner: the ticket number goes into the fix branch's changelog (branch corecontainer-shutdown-closes), and the PR attempt off that branch is the owner's cap test.
