# Skip record: SOLR-17763

- Ticket: SOLR-17763, "Solr container was not available: CoreContainer is either not initialized or shutting down". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2025-05-15, no fix version. JIRA is still open; closing it is a human decision.
- Verdict: close-fixed.
- Draft branch: solr-17763-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 13:10-13:45 UTC.

## Ticket reading

The reporter ran Solr 9.6.1 for about ten days. Requests failed with the message above until a restart. The logs show a gap of about 35 minutes (2025-05-14 09:00:41 to 09:35:15) with no entries, which fits requests blocking rather than failing fast. The ticket has no comments and no reproduction steps.

## Code reading

- The 9.6.1 wait is in commit 1177796e32 (SOLR-17118, 2024-05-31, "Simplify/fix CoreContainerProvider initialization (#2474)"). The removed `waitForCoreContainer` in `CoreContainerProvider.java` waited on a `CountDownLatch` with no timeout (`while (!latch.await(10, TimeUnit.SECONDS))`, logging "Still waiting for CoreContainerStartup" each round). Only then did it throw `UnavailableException("Error processing the request. CoreContainer is either not initialized or shutting down.")`. That is the wait that matches the ticket's silent gap.
- `solr/CHANGES.txt` in 1177796e32, line 143: "SOLR-17118: Simplify CoreContainerProvider initialization and JettySolrRunner. Avoid deadlock/hang."
- At the pinned head, `git grep -n -E 'Still waiting for CoreContainerStartup|waitForCoreContainer|CountDownLatch' 498a09aea97... -- solr/core/src/java/org/apache/solr/servlet/` returns nothing. The request path has no such wait.
- `solr/core/src/java/org/apache/solr/handler/admin/api/NodeHealth.java:77-79` at the pinned head still throws "CoreContainer is either not initialized or shutting down" for a null or shut-down container. That is the health endpoint only, not the request path that failed in the ticket.
- The exact prefix "Solr container was not available" does not appear on the pinned head (grep of `solr/`: no hits).

## History

- 1177796e32, 2024-05-31, SOLR-17118: ancestor of the pinned head (`git merge-base --is-ancestor`: yes). It removes the latch wait and is the fixing change.
- No commit on the pinned head names SOLR-17763 (evidence tool: no hits).
- A search of the full history for the ticket's message string timed out and was not completed. The 9.6.1 origin of the exact prefix is therefore not traced; the wait itself is traced through 1177796e32.

## Reproduction attempts

Not attempted. A reproduction needs a 9.6.x build with a slow or stuck container start; this pass is review-only and runs no builds.

## Gap analysis

The reported behavior came from a request path that waited without a timeout for the container to start, then failed with the ticket's message. SOLR-17118 removed that wait. At the pinned head the request path does not wait, so the reported hang cannot come from that code. The message text remains only in the health endpoint.

Uncertainty that remains: the reporter ran 9.6.1, and this record does not check whether the SOLR-17118 change shipped in a 9.6.x release or only on main. Not traced: the exact origin of the "Solr container was not available" prefix in 9.6.1.

## Verdict justification

close-fixed meets the rubric's minimum: the fixing commit is 1177796e32, it is an ancestor of the pinned head (checked), and the changed code is still in its changed state at the head (the latch wait is absent; `git grep` above). The code path shows the cause and the fix; no reproduction is required for close-fixed.

## Limits

This record does not reproduce the ticket and does not confirm a 9.6.x fix. It does not establish which release first shipped SOLR-17118. It does not change the JIRA status. Nothing is posted to JIRA or GitHub.
