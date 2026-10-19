# Skip record: SOLR-17763

- Ticket: SOLR-17763, Solr container was not available: CoreContainer is either not initialized or shutting down. JIRA status not recorded in the B2 reading. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-17763-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B2 reading (reports/2026-10-18-skip-repro/B2-removed-round-2.md, vm3, round 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Solr container was not available: CoreContainer is either not initialized or shutting down.

## Code reading

1177796e32 (SOLR-17118, 2024-05-31) removed the CountDownLatch wait in CoreContainerProvider that could return an uninitialized container. solr/core/src/java/org/apache/solr/handler/admin/api/NodeHealth.java:77-79 keeps the message for the null and shutting-down cases only.

## History

- `1177796e32`: named or touching the path in the B2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B2 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a fixing change is on main and the changed code is present at the pinned head (cited lines in the code reading). Linkage is by code path; the fixing commit subject may not cite the ticket.

Missing for this verdict: No reproduction was run; B2 ran no builds.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
