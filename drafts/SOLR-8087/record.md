# Skip record: SOLR-8087

- Ticket: SOLR-8087, Look into defensive check in publish that will not let a replica in LIR publish ACTIVE.. JIRA status as recorded in the B1 reading: Open. JIRA was not re-read for this record.
- Verdict: close-obsolete.
- Draft branch: solr-8087-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B1 reading (reports/2026-10-18-skip-repro/B1-removed-round-1.md, vm3, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Look into defensive check in publish that will not let a replica in LIR publish ACTIVE..

## Code reading

LIR removed by a37a213975 (SOLR-11812): LeaderInitiatedRecoveryThread.java deleted. git grep for LeaderInitiatedRecovery and lirState in solr/core/src and solr/solrj/src has no hits.

## History

- `a37a213975`: named or touching the path in the B1 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B1 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head (fixed or removed), as described above.

## Verdict justification

close-obsolete: the feature or class the ticket names was removed, and the search at the pinned head finds no remaining use (cited in the code reading).

Missing for this verdict: The grep result is taken from the B1 reading; it was not re-run in this record.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
