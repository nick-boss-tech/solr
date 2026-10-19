# Skip record: SOLR-13627

- Ticket: SOLR-13627, newly created collection can see all replicas go into recovery immediately on first document addition. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-13627-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: newly created collection can see all replicas go into recovery immediately on first document addition.

## Code reading

The ticket describes replicas going into recovery on the first document add of a new 1x3 collection. LEGACY_CLOUD was removed by 1ab9b811c6 (SOLR-12823), but nothing shows it was the mechanism. Missing: the recovery trigger in ZkController or RecoveryStrategy on the first add, which needs a repro or the ticket's comment logs.

## History

- `1ab9b811c6`: cited in the C4 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
