# Skip record: SOLR-4338

- Ticket: SOLR-4338, Atomic Updates on multi-valued fields giving unexpected results (again). JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-4338-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C1, slice 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Atomic Updates on multi-valued fields giving unexpected results (again).

## Code reading

present | By code reading, not run: JsonLoader.java:597-600 adds a field per repeated key; SolrInputField.java:61 addValue; AtomicUpdateDocumentMerger.java:91 and :158 treat only a single Map value as an atomic operation.

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code path is still present; the reported behavior is not yet shown on a build.

## Verdict justification

keep-open: the code path the ticket names is still present at the pinned head, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head with the input the ticket describes. The C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
