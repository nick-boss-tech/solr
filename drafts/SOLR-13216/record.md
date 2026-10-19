# Skip record: SOLR-13216

- Ticket: SOLR-13216, Trouble restoring a collection. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-13216-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Trouble restoring a collection.

## Code reading

The logged "Copying file ... to restore directory" text was rewritten by 33d16b570c (SOLR-13608), but RestoreCore.doRestore still copies and rolls back (solr/core/src/java/org/apache/solr/handler/RestoreCore.java:145-244). No commit addresses the missing-segment failure, and the code alone cannot show whether data loss still happens.

## History

- `33d16b570c`: cited in the C4 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
