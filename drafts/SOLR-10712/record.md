# Skip record: SOLR-10712

- Ticket: SOLR-10712, UpdateRequest.setDocIterator() writes the payload in memory before sending to server. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-10712-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: UpdateRequest.setDocIterator() writes the payload in memory before sending to server.

## Code reading

JavaBinRequestWriter.getContentWriter now marshals UpdateRequest straight to the OutputStream (JavaBinRequestWriter.java:40), no BAOS buffer left; ContentStream path removed in 78862e7f54 SOLR-18351

## History

- `78862e7f54`: cited in the C3 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a change on main addresses the reported behavior, and the changed code is present at the pinned head (cited in the code reading). Linkage is by code path or commit title, as the C reading states.

Missing for this verdict: No reproduction was run; the C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
