# Skip record: SOLR-5845

- Ticket: SOLR-5845, File locked after indexing. JIRA status as recorded in the B3 reading: Open, 2018-01-11, 0. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-5845-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: File locked after indexing. B3 row type: support. Skip note from the B3 reading: The report concerns a client-side file handle after a SolrJ or extract upload. No server code path was identified.

## Code reading

git grep -i 'file lock' finds only index-segments-merging.adoc:295 (Lucene lock factory, unrelated)

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
