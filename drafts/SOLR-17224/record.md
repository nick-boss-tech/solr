# Skip record: SOLR-17224

- Ticket: SOLR-17224, Getting NPE in apache SOLR 8.11.2 while doing atomic update using add-distinct from java based application. JIRA status as recorded in the B3 reading: Open, 2024-04-28, 2. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-17224-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Getting NPE in apache SOLR 8.11.2 while doing atomic update using add-distinct from java based application. B3 row type: skip note: duplicate of SOLR-17721. Skip note from the B3 reading: The JIRA is still Open; the fix is on the pinned head.

## Code reading

commit a1ebd0433c7 (SOLR-17721 null-pointer fix; ancestor: yes); solr/core/src/java/org/apache/solr/update/processor/AtomicUpdateDocumentMerger.java:549-553 null guard present

## History

None cited as an ancestor in the B3 reading.

## Reproduction attempts

Not attempted. The B3 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a change on main addresses the reported behavior, and the changed code is present at the pinned head (cited in the B3 code reading). Linkage is by commit title or code path, as the B3 reading states.

Missing for this verdict: No reproduction was run; B3 ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
