# Skip record: SOLR-8899

- Ticket: SOLR-8899, MergeStrategy code creates HttpClient(s) and HttoSolrClient(s) that it does not close. JIRA status as recorded in the B3 reading: Open, 2016-04-13, 5. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-8899-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: MergeStrategy code creates HttpClient(s) and HttoSolrClient(s) that it does not close. B3 row type: committed. Skip note from the B3 reading: The committed change is a test-only thread-leak ignore, not a client-close fix. The claimed leak is not shown fixed. Missing: the commit that closes the clients.

## Code reading

commit 79195a8e54 is ancestor: yes, but it only makes AnalyticsMergeStrategyTest ignore thread leaks; solr/core/src/java/org/apache/solr/handler/component/IterativeMergeStrategy.java:47 uses the container-shared getDefaultHttpSolrClient()

## History

- `79195a8e54`: cited in the B3 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B3 scope ran no builds or tests.

## Gap analysis

The B3 reading gives the missing piece; see the code reading and notes.

## Verdict justification

keep-open: the code reading does not settle the question; the B3 reading names the missing piece below.

Missing for this verdict: Reproduction on the pinned head, or the missing piece named in the B3 reading. B3 ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
