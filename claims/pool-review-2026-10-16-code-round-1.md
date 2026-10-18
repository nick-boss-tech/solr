# Claim: pool-review-2026-10-16-code-round-1

- Claimant host: vm5, matching a file in `hosts/` (`hosts/vm5.md`).
- Status: DONE 2026-10-18T05:05Z (all items 2 to 5 reported; index at `reports/2026-10-16-pr-batch/code-round-1-index.md`).
- Job or assignment: `assignments/pool-review-2026-10-16-code-round-1.md`.
- Slice: items 2 to 5 (SOLR-7390, SOLR-6975, SOLR-6759, SOLR-8939). Item 1 (SOLR-10424) already has a DONE line from vm2 in `claims/pool-review-2026-10-16-code-round-1-item-1.md`, but `reports/2026-10-16-pr-batch/SOLR-10424-check.md` is not on origin/coordination at 6890f7c618; left for the main side to confirm.
- Capability tags used: implementation, gate.
- Started: 2026-10-18T03:48:01Z (UTC).

Heartbeat: 2026-10-18T03:48:01Z
Heartbeat: 2026-10-18T04:58Z. Items 2 to 5 run at the fork heads with `sweep-run.sh` (logs in `/workspace/gates/logs/vm5/code-round-1/`). Item 4 and item 5 had extra trial runs; see their check files.

Result: 2026-10-18T03:59Z: item 2, SOLR-7390, `QueryElevationComponentTest.testMarker` fails at head 7463dd006a (400, unknown transformer `elev`, seed 997E388750B5B333). Escalated; `reports/2026-10-16-pr-batch/SOLR-7390-check.md`.
Result: 2026-10-18T04:35Z: item 3, SOLR-6975, paging repeats and gaps at head c666155ed0 (2 shards x RF 2), and the `_docid_`-leading comparator forms a cycle. Escalated; `reports/2026-10-16-pr-batch/SOLR-6975-check.md`.
Result: 2026-10-18T04:50Z: item 4, SOLR-6759, the query-limit test passes with and without the `finally` at 680044f1d2. Escalated; `reports/2026-10-16-pr-batch/SOLR-6759-check.md`. No fork commit.
Result: 2026-10-18T05:02Z: item 5, SOLR-8939, the duplicate-uniqueKey test passes at 1a4a312bb7 (no null slot; duplicates rejected in `ShardDocQueue.push`). Refuted for string keys; `reports/2026-10-16-pr-batch/SOLR-8939-check.md`.

DONE: 2026-10-18T05:05Z. Deliverables: `reports/2026-10-16-pr-batch/code-round-1-index.md`, `SOLR-7390-check.md`, `SOLR-6975-check.md`, `SOLR-6759-check.md`, `SOLR-8939-check.md`.
