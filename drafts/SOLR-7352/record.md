# Skip record: SOLR-7352

- Ticket: SOLR-7352, Synchronize CloudSolrStream EOF Tuple Map. JIRA status as recorded in the B3 reading: Open, 2019-10-08, 3. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-7352-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Synchronize CloudSolrStream EOF Tuple Map. B3 row type: committed. Skip note from the B3 reading: Title matches the ticket.

## Code reading

commit 69fdcfe306 "SOLR-7352: Synchronize CloudSolrStream EOF Tuple Map"; ancestor of pinned head: yes

## History

- `69fdcfe306`: cited in the B3 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The B3 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a change on main addresses the reported behavior, and the changed code is present at the pinned head (cited in the B3 code reading). Linkage is by commit title or code path, as the B3 reading states.

Missing for this verdict: No reproduction was run; B3 ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
