# Skip record: SOLR-15443

- Ticket: SOLR-15443, Atomic Update of parent document in nested documents fails and needs solr restart. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-15443-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Atomic Update of parent document in nested documents fails and needs solr restart.

## Code reading

AtomicUpdateDocumentMerger.java:108-113 still throws "Did not find child ID ... in parent ID ...". The LazyField only appears when lazy field loading is on (SolrConfig.java:300, default false, gated at SolrDocumentFetcher.java:164), and the ticket does not state that config. Missing: the reporter's solrconfig.

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
