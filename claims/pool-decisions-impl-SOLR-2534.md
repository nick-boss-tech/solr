# Claim: pool-decisions-impl, slice SOLR-2534 (owner decision row 9, option A)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: DONE 2026-10-20T12:01:23Z (vm5, stale HANDED BACK status updated). Branch `solr-2534-string-min-max` gated green at `a75b012a20` by vm3 round 2 (receipt `receipts/SOLR-2534.md`, focused TestStringMinMaxFunction 5 of 5). Not re-gated here.
- Job or assignment: `assignments/pool-owner-decisions-round-1-implementation.md`, item 8. Source answer: `assignments/pool-vm3-owner-decisions-round-1.md` row 9.
- Slice: the whole ticket.
- Capability tags used: implementation, gate.
- Started: 2026-10-20T06:10:00Z (UTC).
- Links: Ticket or parent: SOLR-2534. Category: Search and query components. Siblings: `MinMaxAgg` (read-only). Supersedes: none.
- Idempotency key: SOLR-2534@c8ab71430f.

Heartbeat: 2026-10-20T03:20:20Z

DONE: 2026-10-20T03:20:20Z. Branch `solr-2534-string-minmax` at `04bbcee582`, receipt `receipts/SOLR-2534.md`, draft `drafts/SOLR-2534/pr-description.md`.

Result: 2026-10-20T08:20:00Z: handed back at `722232054e`, receipt `receipts/SOLR-2534.md`. Failure: test methods in `TestStringMinMaxFunction` do not declare `throws Exception`.

## Note on the answer

The answer says "matching the numeric behavior". The numeric `min` and `max` function queries (Lucene `MinFloatFunction` and `MaxFloatFunction`) are unchanged. The string path rejects multi-valued fields at parse time with a clear message.
