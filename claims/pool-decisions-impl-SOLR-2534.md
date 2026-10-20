# Claim: pool-decisions-impl, slice SOLR-2534 (owner decision row 9, option A)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: CLAIMED. Implementation on branch `solr-2534-string-min-max` from base `c8ab71430f`.
- Job or assignment: `assignments/pool-owner-decisions-round-1-implementation.md`, item 8. Source answer: `assignments/pool-vm3-owner-decisions-round-1.md` row 9.
- Slice: the whole ticket.
- Capability tags used: implementation, gate.
- Started: 2026-10-20T06:10:00Z (UTC).
- Links: Ticket or parent: SOLR-2534. Category: Search and query components (function queries, `ValueSourceParser`). Siblings: `MinMaxAgg` (facet agg_min and agg_max, already handles strings), Lucene `MinFloatFunction` and `MaxFloatFunction` (numeric, unchanged). Supersedes: none.
- Idempotency key: SOLR-2534@c8ab71430f.

Heartbeat: 2026-10-20T06:10:00Z

## Note on the answer

The answer says "matching the numeric behavior". The numeric `min` and `max` functions (Lucene `MinFloatFunction` and `MaxFloatFunction`) do not reject multi-valued fields in the same way the string path must, so the string path rejects multi-valued fields at parse time with a clear message. The numeric path is unchanged.
