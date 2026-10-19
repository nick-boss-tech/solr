# Skip record: SOLR-10709

- Ticket: SOLR-10709, get "unexpected docvalues type NUMERIC for field" for query q=field:* if field is defined as docValues=true, indexed=false. JIRA status not recorded in the B2 reading. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-10709-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B2 reading (reports/2026-10-18-skip-repro/B2-removed-round-2.md, vm3, round 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: get "unexpected docvalues type NUMERIC for field" for query q=field:* if field is defined as docValues=true, indexed=false.

## Code reading

solr/core/src/java/org/apache/solr/schema/FieldType.java:1071-1073 builds a FieldExistsQuery for docValues fields, so field:* no longer goes through the DocValues rewrite that threw the NUMERIC error. The query type was changed in c64a35adbc (SOLR-17631). Not run; the 2017 trace path was not re-run.

## History

- `c64a35adbc`: named or touching the path in the B2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B2 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a fixing change is on main and the changed code is present at the pinned head (cited lines in the code reading). Linkage is by code path; the fixing commit subject may not cite the ticket.

Missing for this verdict: No reproduction was run; B2 ran no builds.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
