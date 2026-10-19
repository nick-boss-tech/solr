# Skip record: SOLR-11150

- Ticket: SOLR-11150, When V2Request is sent to the wrong node, it throws NPE. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-11150-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: When V2Request is sent to the wrong node, it throws NPE.

## Code reading

ticket has no description or repro and 'wrong node' is undefined; V2HttpCall.java:160-170 now proxies via extractRemotePath/ADMIN_OR_REMOTEPROXY and linked SOLR-11130 is Closed; need the original NPE stack trace

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
