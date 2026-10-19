# Skip record: SOLR-11827

- Ticket: SOLR-11827, MockAuthorizationPlugin should return 401 if no principal is specified. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-11827-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: MockAuthorizationPlugin should return 401 if no principal is specified.

## Code reading

solr/core/src/test/org/apache/solr/security/MockAuthorizationPlugin.java:35-52 still returns 200 for unprotected resources with no principal. The PKI path was changed separately by 749af1b7c9 (SOLR-15334, error response in PKIAuthenticationPlugin.java:153-155), but the mock the ticket names was not changed.

## History

- `749af1b7c9`: cited in the C4 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code path is still present; the reported behavior is not yet shown on a build.

## Verdict justification

keep-open: the code path the ticket names is still present at the pinned head, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head with the input the ticket describes. The C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
