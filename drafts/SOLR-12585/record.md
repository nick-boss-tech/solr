# Skip record: SOLR-12585

- Ticket: SOLR-12585, Solr fails even ZK quorum has majority. JIRA status not recorded in the B2 reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-12585-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B2 reading (reports/2026-10-18-skip-repro/B2-removed-round-2.md, vm3, round 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Solr fails even ZK quorum has majority.

## Code reading

Missing: the pinned ZooKeeper client version and its host-resolution behavior. That behavior is in the ZooKeeper dependency, not this repo. Not checked.

## History

- None cited in the B2 reading.

## Reproduction attempts

Not attempted. The B2 scope ran no builds or tests.

## Gap analysis

The code reading is inconclusive; the missing piece is named in the code reading below.

## Verdict justification

keep-open: the code reading does not settle the question; the B2 report names the missing piece below.

Missing for this verdict: The missing piece noted in the code reading below.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
