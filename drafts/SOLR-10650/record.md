# Skip record: SOLR-10650

- Ticket: SOLR-10650, First start fails due to "Problem accessing /solr/. Reason:      Not Found". JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-fixed.
- Draft branch: solr-10650-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: First start fails due to "Problem accessing /solr/. Reason:      Not Found".

## Code reading

bin/solr now exits with message when Java is below required (JAVA_VER_NUM < JAVA_VER_REQ, solr/bin/solr:179, req 21 at :54), so JDK 9 case gets feedback; check added by 4574cb8cea SOLR-8080 (updated 390e11d1ba)

## History

- `4574cb8cea`: cited in the C3 reading; ancestor of the pinned head: yes (checked).
- `390e11d1ba`: cited in the C3 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the reported behavior no longer applies at the pinned head, as described above.

## Verdict justification

close-fixed: a change on main addresses the reported behavior, and the changed code is present at the pinned head (cited in the code reading). Linkage is by code path or commit title, as the C reading states.

Missing for this verdict: No reproduction was run; the C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
