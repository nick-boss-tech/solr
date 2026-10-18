# Skip audit snapshot

Status: historical reference snapshot, not an active assignment.

This carries forward the skip-list work recorded before the coordination branch was in use. The [queue snapshot](queue-snapshot-2026-10-07.json) was copied from the local `research/pipeline/queue.json`, last modified on 2026-10-07. It contains 1,027 candidate records, with their recorded statuses and audit evidence. Treat it as a point-in-time record, not a live queue.

## State at import

The local audit status reported on 2026-10-17:

| Tier | Current skips | Audited | Remaining |
| --- | ---: | ---: | ---: |
| 1 | 392 | 392 | 0 |
| 2 | 156 | 68 | 88 |
| 3 | 143 | 143 | 0 |
| 4 | 36 | 16 | 20 |
| 5 | 23 | 23 | 0 |

Recorded verdict totals: 173 branched, 599 confirmed skips, and 45 deferred. The original promotion target of 10 branches was met; later work produced additional branches. The remaining 108 unaudited skips in this snapshot are in Tiers 2 and 4.

## Using this snapshot

- Treat the JSON as historical input. Do not treat its branch heads, audit outcomes, or remaining counts as current without checking the live fork and `upstream/main`.
- Before starting follow-up work, post an assignment on `coordination` with a non-overlapping slice, then claim that slice under `claims/` as described in [`pool/START-HERE.md`](../../pool/START-HERE.md).
- Record new findings in the deliverable named by that assignment. Do not silently edit this snapshot as if it were a live shared queue.
- Confirm a skip against the ticket and current code. An old note alone is not enough; use deferred when the available evidence cannot settle it.
- Check existing branches, claims, gates, and receipts before acting on any promoted ticket.
