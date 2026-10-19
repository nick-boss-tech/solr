We checked this against Solr main at commit 498a09aea9. The first problem in the report, the NullPointerException in the v2 media-type filter during a backup request, is fixed on main. The second problem, the same HTTP 500 for replica creation, is not explained yet, so the ticket stays open.

What we checked:

- The v2 media-type filter now checks that the request is set before it reads the parameters. Commit c058ea5dac added that check; before it, the call was unguarded, which is the NullPointerException in the report.
- Replica creation through the v2 API is handled by a different code path. The media-type filter does not run for it, so the fix above does not cover that path.

What this means: the backup-request 500 should no longer occur on current main. For replica creation we have not found the code that produces the 500 on current main, and we have not reproduced it.

If you still see the 500 on replica creation: please send the full response for an ADDREPLICA request (the JSON body, the Solr version, and the Solr log for the same request), so we can find the code path that raises it.
