# Claim: pool-vm3-jira-newdev-bugs-round-1, slice SOLR-13184 (item 3 only)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: DONE. Slice: item 3 only, SOLR-13184. Other items are not claimed by vm3.
- Job or assignment: `assignments/pool-jira-newdev-bugs-round-1.md`, item 3.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T13:51:07Z (UTC).

## Heartbeat gap

- The heartbeat due by 14:51Z was missed. The claim had no heartbeat from 13:51Z until 16:22Z, when vm3 resumed it after an overnight wake.

## Result (vm3, 2026-10-19)

- Branch: `solr-13184-joindf-missing-arg`, head `6bb01a08cd` (base `c8ab71430f`). Commits: `2e178c9d92` adds the regression test, `6bb01a08cd` adds the fix.
- Reproduction at base plus test (`2e178c9d92`): `JoinDocFreqMissingArgTest` fails. `{!frange l=10 u=100}joindf(genre:comedy,$x)` returns a RuntimeException wrapping an NPE, `Cannot invoke "String.hashCode()" because "this.qfield" is null`, thrown from `ValueSourceRangeFilter.hashCode` via `QueryResultKey`. The request gets a 500, not a 400. Seed `8B3C0622AB0B57FC`. Log: `/workspace/gates/logs/vm3/SOLR-13184-focused-20261019T162454Z.log`.
- Fix: `solr/core/src/java/org/apache/solr/search/ValueSourceParser.java`, the `joindf` parser throws `SolrException` with `BAD_REQUEST` when either the field or the query argument is missing.
- Verdict: reproduces. Fix and regression test pushed as a new branch. No PR opened; PR needs Nick's approval.
- Receipt: `receipts/SOLR-13184.md` on coordination.

Heartbeat: 2026-10-19T16:42:46Z

DONE: 2026-10-19. Deliverables: this claim file (result section), `receipts/SOLR-13184.md`, branch `solr-13184-joindf-missing-arg` at `6bb01a08cd`.
