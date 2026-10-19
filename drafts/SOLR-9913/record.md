# Skip record: SOLR-9913

- Ticket: SOLR-9913, LIR should continue on SocketTimeoutException. JIRA status not recorded in the B2 reading. JIRA was not re-read for this record.
- Verdict: close-obsolete.
- Draft branch: solr-9913-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B2 reading (reports/2026-10-18-skip-repro/B2-removed-round-2.md, vm3, round 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: LIR should continue on SocketTimeoutException.

## Code reading

LIR removed: zero leaderinitiatedrecovery hits in *.java. Removal in a37a213975 (SOLR-11812, 8.0).

## History

- `a37a213975`: named or touching the path in the B2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B2 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-obsolete: the feature or code the ticket names was removed, and the search at the pinned head finds no remaining use (cited in the code reading).

Missing for this verdict: The grep result is taken from the B2 reading; it was not re-run in this record.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
