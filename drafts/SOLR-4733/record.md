# Skip record: SOLR-4733

- Ticket: SOLR-4733, Rollback does not work correctly with tlog and optimistic concurrency updates. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-4733-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C1, slice 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Rollback does not work correctly with tlog and optimistic concurrency updates.

## Code reading

unclear | solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java:971-973 rejects SolrCloud rollback (SOLR-4895, commit 93b33b5fdb). Standalone rollback calls rollbackWriter at :983 with no UpdateLog reset. Missing: a runtime reproduction of standalone rollback followed by an atomic update. Tests were not run.

## History

- `93b33b5fdb`: cited in the C1 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
