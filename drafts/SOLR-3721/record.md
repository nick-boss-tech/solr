# Skip record: SOLR-3721

- Ticket: SOLR-3721, Multiple concurrent recoveries of same shard?. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-3721-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C1, slice 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Multiple concurrent recoveries of same shard?.

## Code reading

unclear | 48ae881287 (Mark Miller, 2012) fixed only a join-interrupt double-recovery race. DefaultSolrCoreState.java:379-386 has no join. The JIRA is Open with no resolution, so the skip note's "resolved" is unsupported. Attached recovery logs were not reviewed.

## History

- `48ae881287`: cited in the C1 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
