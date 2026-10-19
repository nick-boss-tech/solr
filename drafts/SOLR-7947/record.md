# Skip record: SOLR-7947

- Ticket: SOLR-7947, SolrCloud: after a solr node restarted, all cores in the node are down in /clusterstate.json due to java.nio.channels.OverlappingFileLockException.. JIRA status as recorded in the B3 reading: Open, 2015-08-20, 1. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-7947-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: SolrCloud: after a solr node restarted, all cores in the node are down in /clusterstate.json due to java.nio.channels.OverlappingFileLockException.. B3 row type: support. Skip note from the B3 reading: Docs do not cover the same-JVM overlapping-lock restart failure. The logs attached to the ticket are unavailable and there is no repro.

## Code reading

solr/solr-ref-guide/modules/configuration-guide/pages/index-segments-merging.adoc:289-310 lists lock types but not this restart case; solr/core/src/java/org/apache/solr/core/StandardDirectoryFactory.java:58-66; git log --grep=SOLR-7947 empty

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
