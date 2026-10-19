# Skip record: SOLR-8623

- Ticket: SOLR-8623, JSON FacetAPI with enumField throws "Expected numeric field type". JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-8623-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: JSON FacetAPI with enumField throws "Expected numeric field type".

## Code reading

988a16fe95 SOLR-15191 Fix JSON faceting on EnumFieldType; EnumCalc now handled at FacetRangeProcessor.java:174

## History

- `988a16fe95`: cited in the C3 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a change on main addresses the reported behavior, and the changed code is present at the pinned head (cited in the code reading). Linkage is by code path or commit title, as the C reading states.

Missing for this verdict: No reproduction was run; the C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
