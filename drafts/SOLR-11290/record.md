# Skip record: SOLR-11290

- Ticket: SOLR-11290, replace expression doesn't allow strings. JIRA status as recorded in the B3 reading: Open, 2019-06-08, 0. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-11290-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: replace expression doesn't allow strings. B3 row type: support. Skip note from the B3 reading: String handling reads as correct. The reported expression was not reproduced. Missing: the failing expression from the ticket.

## Code reading

solrj-streaming/src/java/org/apache/solr/client/solrj/io/ops/ReplaceWithValueOperation.java:91-96; solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/expr/StreamFactory.java:646-647 returns the raw String; git log --grep=SOLR-11290 empty

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
