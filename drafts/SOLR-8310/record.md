# Skip record: SOLR-8310

- Ticket: SOLR-8310, Solr-5.3.1 doesn't start on CentOS Linux 5 - 64-Bit Server. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-8310-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Solr-5.3.1 doesn't start on CentOS Linux 5 - 64-Bit Server.

## Code reading

present | bin/solr:1381-1387 still runs lsof -t -PniTCP:$port -sTCP:LISTEN, gated on an lsof -v grep for "revision". That selection matches the error letters in the ticket, and it was added by 859415c7e1 (SOLR-7635). Next: a CentOS 5 lsof 4.78 check needs that host and was not run.

## History

- `859415c7e1`: cited in the C2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code path is still present; the reported behavior is not yet shown on a build.

## Verdict justification

keep-open: the code path the ticket names is still present at the pinned head, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head with the input the ticket describes. The C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
