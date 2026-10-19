# Skip record: SOLR-9713

- Ticket: SOLR-9713, TLS-SSL Mutual-Auth doesn't work, Unable to load keyStore with given password. JIRA status as recorded in the B3 reading: Open, 2019-06-08, 0. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-9713-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: TLS-SSL Mutual-Auth doesn't work, Unable to load keyStore with given password. B3 row type: support. Skip note from the B3 reading: The Windows wiring reads the environment correctly on inspection. The reported failure needs a Windows run with those settings.

## Code reading

solr/bin/solr.cmd:42 includes solr.in.cmd; solr/core/src/java/org/apache/solr/util/configuration/providers/EnvSSLCredentialProvider.java:44,59 reads the environment; SSLConfigurations.java:29 "secret" default has no other use

## History

None cited as an ancestor in the B3 reading.

## Reproduction attempts

Not attempted. The B3 scope ran no builds or tests.

## Gap analysis

The B3 reading gives the missing piece; see the code reading and notes.

## Verdict justification

keep-open: the code reading does not settle the question; the B3 reading names the missing piece below.

Missing for this verdict: Reproduction on the pinned head, or the missing piece named in the B3 reading. B3 ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
