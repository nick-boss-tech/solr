# Skip record: SOLR-9468

- Ticket: SOLR-9468, HTTP api endpoint /replication does not handle errors propely. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-9468-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: HTTP api endpoint /replication does not handle errors propely.

## Code reading

ReplicationHandler.doSnapShoot (solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java:649-672) now catches errors and calls reportErrorOnResponse which sets status ERROR (line 375), c951578fca SOLR-6117: Unify ReplicationHandler error handling; HTTP code is still not 4xx/5xx for non-SolrException errors

## History

- `c951578fca`: cited in the C3 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a change on main addresses the reported behavior, and the changed code is present at the pinned head (cited in the code reading). Linkage is by code path or commit title, as the C reading states.

Missing for this verdict: No reproduction was run; the C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
