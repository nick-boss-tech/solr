# Skip record: SOLR-5772

- Ticket: SOLR-5772, duplicate documents between solr "block join" documents and "normal" document. JIRA status as recorded in the B3 reading: Open, 2021-02-04, 4. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-5772-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: duplicate documents between solr "block join" documents and "normal" document. B3 row type: support. Skip note from the B3 reading: Docs are silent on mixing block-join and plain documents with a shared uniqueKey, and no collision code path was identified. Missing: a repro of the duplicate.

## Code reading

solr/solr-ref-guide/modules/indexing-guide/pages/indexing-nested-documents.adoc: no hits for uniqueKey, duplicate, same id, or unsupported

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
