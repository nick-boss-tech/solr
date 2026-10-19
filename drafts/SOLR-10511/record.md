# Skip record: SOLR-10511

- Ticket: SOLR-10511, Unable to view replication status. JIRA status not recorded in the B2 reading. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-10511-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B2 reading (reports/2026-10-18-skip-repro/B2-removed-round-2.md, vm3, round 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Unable to view replication status.

## Code reading

solr/webapp/web/js/angular/controllers/replication.js getIterations now tests matchingIterations[0] and sets status on a new object when nothing matches. The old if (matchingIterations) was always true for an array, so iteration was undefined and iteration.status threw the reported TypeError. Fix: 69d0c70651 (SOLR-10763, 2017-06-23).

## History

- `69d0c70651`: named or touching the path in the B2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B2 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a fixing change is on main and the changed code is present at the pinned head (cited lines in the code reading). Linkage is by code path; the fixing commit subject may not cite the ticket.

Missing for this verdict: No reproduction was run; B2 ran no builds.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
