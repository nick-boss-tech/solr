# Skip record: SOLR-10534

- Ticket: SOLR-10534, "Core state doesn't get cleaned up if core initialization fails". JIRA status read 2026-10-20T12:22:37Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-10534-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T12:22:37Z (UTC).

## Ticket reading

A core whose init fails (a bad update processor in the schema) shows as an initialization failure in the admin UI, but a later create of the same name reports that the core already exists, even though no core is loaded. Restarting the node clears it. Comments: a possible relation to SOLR-10021, and the failure reproduces on 6.4.2 and 6.5.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/core/CoreContainer.java:1738-1739`: a failed core creation records a `CoreLoadFailure` in `coreInitFailures` (line 222). The failure record is kept for the UI.
- `CoreContainer.java:1469`: `coreInitFailures.remove(cd.getName())` runs on a later successful load.
- `CoreContainer.java:1527-1537`: the create path refuses with "Core with name ... already exists" when `getCoreDescriptor(coreName) != null`. Whether a failed init leaves a descriptor behind in that map was not traced to the end in this pass; if it does, the ticket's second error is the code path, not a stale CLI cache.

## History

- evidence.py at the pinned head: no commits naming SOLR-10534.

## Reproduction attempts

Not run. A reproduction needs a node with a configset whose processor fails at core load, a first create that fails, and then a second create. The harness used for the fingerprint benchmark (CoreContainer, solr home) could be adapted, but this pass did not do it.

## Gap analysis

The reporter's second error is consistent with a descriptor left in the container after a failed init. Whether that is what the pinned head does is the open question; a run would settle it.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading (CoreContainer.java:222, 1469, 1527-1539, 1738-1739); the missing piece is a reproduction of the two creates against the pinned head and a check of the descriptor map after the failed init. close-fixed, close-obsolete and close-cannot-reproduce are not supported.

## Limits

This record does not establish the cause. It does not check SOLR-10021's content or the Cloud path.
