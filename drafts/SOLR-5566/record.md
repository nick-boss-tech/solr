# Skip record: SOLR-5566

- Ticket: SOLR-5566, NPE in NamedList when using highlighting with shards. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-5566-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: NPE in NamedList when using highlighting with shards.

## Code reading

unclear | HighlightComponent.java:215-235 finishStage now calls getAllHighlights (lines 275-279), which runs removeNulls before any map is built, so the 4.6 direct path is no longer in finishStage. NamedList.java:91 still takes Entry[], and no commit at the pinned sha cites SOLR-5566. Next: a shards highlighting repro to see whether nulls still reach NamedList.

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
