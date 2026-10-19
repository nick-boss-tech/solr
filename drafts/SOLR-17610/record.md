# Skip record: SOLR-17610

- Ticket: SOLR-17610, Solr 9.7 on CentOS 7 with OpenJDK 11 has a 10-second delay for local network client access. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-17610-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Solr 9.7 on CentOS 7 with OpenJDK 11 has a 10-second delay for local network client access.

## Code reading

solr/bin/solr:1170-1182 enables -Djava.security.manager by default unless Java 24 or later, and solr/bin/solr.cmd:97-98 does the same on Windows. The 10-second delay itself was not reproduced.

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
