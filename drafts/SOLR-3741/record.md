# Skip record: SOLR-3741

- Ticket: SOLR-3741, ExtendedDismaxQParser (edismax) does not obey q.op for queries with operators. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-3741-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C1, slice 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: ExtendedDismaxQParser (edismax) does not obey q.op for queries with operators.

## Code reading

fixed | ExtendedDismaxQParser.java:1010-1014 applies q.op to the whole classic parse, including queries with explicit operators. Added by 5e587ab4d1. mm=0% at :436-444 does not reset the default operator.

## History

- `5e587ab4d1`: cited in the C1 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a change on main addresses the reported behavior, and the changed code is present at the pinned head (cited in the code reading). Linkage is by code path or commit title, as the C reading states.

Missing for this verdict: No reproduction was run; the C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
