# Skip record: SOLR-9094

- Ticket: SOLR-9094, ReRanker should gracefully handle sorts without score. JIRA status as recorded in the B1 reading: Open. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-9094-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B1 reading (reports/2026-10-18-skip-repro/B1-removed-round-1.md, vm3, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: ReRanker should gracefully handle sorts without score.

## Code reading

solr/core/src/java/org/apache/solr/search/ReRankCollector.java:136-138 populates scores only when sort != null, and the scoreMode is COMPLETE_NO_SCORES for sorts that do not need scores. Changed by 55bfadbce1 (LUCENE-8412). Missing: no regression test for a non-score sort was found in TestReRankQParserPlugin; not run.

## History

- `55bfadbce1`: named or touching the path in the B1 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B1 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head (fixed or removed), as described above.

## Verdict justification

close-fixed: a fixing change is on main and the changed code is present at the pinned head (cited lines in the code reading). Linkage is by code path; the fixing commit subject may not cite the ticket.

Missing for this verdict: No reproduction was run; B1 ran no builds.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
