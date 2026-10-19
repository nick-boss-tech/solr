# Skip record: SOLR-5252

- Ticket: SOLR-5252, SolrCloud core UNLOAD + CREATE. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-5252-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C1, slice 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: SolrCloud core UNLOAD + CREATE.

## Code reading

unclear | solr/core/src/test/org/apache/solr/cloud/UnloadDistributedZkTest.java:71 and solr/core/src/java/org/apache/solr/handler/admin/api/UnloadCore.java exist. No UNLOAD-then-CREATE same-name path traced. Missing: whether a test or code path covers unload then recreate of the same core name, and its outcome.

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
