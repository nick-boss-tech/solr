# Skip record: SOLR-4382

- Ticket: SOLR-4382, edismax, qf, multiterm analyzer, wildcard query bug. JIRA status as recorded in the B1 reading: Open. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-4382-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B1 reading (reports/2026-10-18-skip-repro/B1-removed-round-1.md, vm3, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: edismax, qf, multiterm analyzer, wildcard query bug.

## Code reading

solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:146 calls validateQueryFields, which throws SyntaxError for an unknown qf field (:223-229, checkFieldInSchema at :276-281). Added by 9481c1f623 (SOLR-5163). The reported qf=sku doesNotExit case is now rejected rather than producing a phrase query.

## History

- `9481c1f623`: named or touching the path in the B1 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B1 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head (fixed or removed), as described above.

## Verdict justification

close-fixed: a fixing change is on main and the changed code is present at the pinned head (cited lines in the code reading). Linkage is by code path; the fixing commit subject may not cite the ticket.

Missing for this verdict: No reproduction was run; B1 ran no builds.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
