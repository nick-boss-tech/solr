# Skip record: SOLR-6822

- Ticket: SOLR-6822, Block Join duplicates when _root_ is different type than uniqueKey. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-6822-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Block Join duplicates when _root_ is different type than uniqueKey.

## Code reading

fixed | 6396cb759f (SOLR-10830) makes init fail when _root_ and uniqueKey types differ: IndexSchema.java:582-590, with isUsableForChildDocs at IndexSchema.java:2196. Test schema bad-schema-uniquekey-diff-type-root.xml came in the same commit.

## History

- `6396cb759f`: cited in the C2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a change on main addresses the reported behavior, and the changed code is present at the pinned head (cited in the code reading). Linkage is by code path or commit title, as the C reading states.

Missing for this verdict: No reproduction was run; the C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
