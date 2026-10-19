# Skip record: SOLR-8365

- Ticket: SOLR-8365, If you change LogUpdateProcessor logging from info, rather than turning off update logging, you send more useless update logging to the SolrCore RequestLog.. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-8365-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: If you change LogUpdateProcessor logging from info, rather than turning off update logging, you send more useless update logging to the SolrCore RequestLog..

## Code reading

present | LogUpdateProcessorFactory.java:63-66 builds LogUpdateProcessor only when log.isInfoEnabled() or a slow-update threshold is set, so update logging is still controlled only by logger level. Logging at info is at line 201.

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
