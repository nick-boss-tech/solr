# Skip record: SOLR-10534

- Ticket: SOLR-10534, Core state doesn't get cleaned up if core initialization fails. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-10534-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Core state doesn't get cleaned up if core initialization fails.

## Code reading

createFromDescriptor puts coreInitFailures entry on failure (CoreContainer.java:1739) and create() catch (CoreContainer.java:1566-1600) never removes it; CLIUtils.safeCheckCoreExists treats initFailure as existing (CLIUtils.java:427) so 'already exists' still plausible

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
