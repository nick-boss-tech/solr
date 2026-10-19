# Skip record: SOLR-5579

- Ticket: SOLR-5579, Leader stops processing collection-work-queue after failed collection reload. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-5579-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Leader stops processing collection-work-queue after failed collection reload.

## Code reading

unclear | Overseer.java:961 still defines /overseer/collection-queue-work, and RecoveryStrategy.java:373 and :811 still call getLeaderRetry. OverseerCollectionProcessor is gone from main code (only OverseerStatusTest.java:60 strings remain), replaced by OverseerCollectionMessageHandler.java:65. Next: repro a failed reload with no leader on the rewritten Overseer.

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
