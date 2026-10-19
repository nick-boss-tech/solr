# Skip record: SOLR-15226

- Ticket: SOLR-15226, org.apache.lucene.index.IndexNotFoundException: no segments* file found in LockValidatingDirectoryWrapper(NRTCachingDirectory. JIRA status as recorded in the B3 reading: Open, 2024-08-16, 0. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-15226-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: org.apache.lucene.index.IndexNotFoundException: no segments* file found in LockValidatingDirectoryWrapper(NRTCachingDirectory. B3 row type: support. Skip note from the B3 reading: The IndexNotFoundException comes from Lucene outside this repo. No docs or code here explain recovery.

## Code reading

git grep -i 'no segments' at the pinned head finds only UpgradeCoreIndex.java:363 (comment) and reindexing.adoc:141 (unrelated)

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
