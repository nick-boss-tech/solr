# Skip record: SOLR-9769

- Ticket: SOLR-9769, solr stop on a service already stopped should return exit code 0. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-9769-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: solr stop on a service already stopped should return exit code 0.

## Code reading

solr/bin/solr:1030-1033 still prints 'No process found for Solr node running on port' and exits 1 when stop finds no process; init.d script gone but bin/solr behavior unchanged, maintainers disagreed on desired exit code

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code path is still present; the reported behavior is not yet shown on a build.

## Verdict justification

keep-open: the code path the ticket names is still present at the pinned head, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head with the input the ticket describes. The C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
