# Pool overview and priorities, 2026-10-20

Read from coordination at 4f4aef0a80, with the vm3 gate claim on 7f0edc0435. Status values are the ones each record states; a record's age does not make it live.

## 1. Critical and Blocker review round (assignments/pool-newdev-critblock-review-round-1.md)

45 units. Three passes with a regate each. Summary from the claim files:

| State | Units |
|---|---|
| DONE | 1 SOLR-17842, 6 SOLR-11310, 11 SOLR-15326 |
| ENDED | 8 SOLR-12743 (base cannot finish), 10 SOLR-14638 (parked by owner) |
| HANDED BACK at pass 1 (retry needed) | 7 SOLR-11650 (redaction fails open for a raw slash or question mark), 9 SOLR-14200 (empty result for missing shard, divide by zero) |
| Pass 2 recorded or green, pass 3 open; claims stale (last heartbeat over two hours old, so takeable under the lease rule) | 3 SOLR-18458 (last heartbeat 15:25Z), 12 SOLR-16155 (pass 2 green at 0881de1ed6, last heartbeat 15:53Z) |
| Live, other host | 13 SOLR-16655 (vm3 claim, pass 1 gate result pending) |
| NEARLY, owner or vm1 piece | 4 and 5 SOLR-18552 (solr.cmd question for the owner; launcher and bats leg on a vm1 gate job) |
| NEEDS-OWNER | 2 SOLR-18297 (Jetty warning, options A to C) |
| Not yet claimed | 14 to 38 except those named above; 39 to 45 unread in this pass |

Order: Blocker units first (2 to 5), then the Critical units that were handed back (7, 9), then the rest in table order.

## 2. Open gate jobs (gates/*.md with Status OPEN)

| Job | Needs | Status |
|---|---|---|
| SOLR-18507-phase-4-r4 | gate | Claimed by vm3 (claims/pool-vm3-gate-SOLR-18507-phase-4-r4.md) |
| SOLR-18552 launcher bats | bats (vm1 only) | Open; vm1 or any host with bats |
| SOLR-18555 phase r4 solrj-query | gate, npm | Open; needs a host with the reference-guide npm dependencies |

PAUSED gate jobs (SOLR-18555 r4 parent cloud and HTTP) are covered by the integrated child gate at 0c6aae3162f and resume only if the parent is submitted separately.

## 3. Owner decisions waiting

- SOLR-18297: the Jetty warning in CoreReplicationAPITest (options A to C in gates/SOLR-18297-review-pass-1-vm3.md). Registry row 6 in pool/tickets-to-open.md.
- SOLR-18552: add the solr.cmd default sharedArenaMaxPermits=64 on 9.x (claims/pool-newdev-critblock-review-round-1-solr-18552-sharedarena-9x.md).
- The 14 module lockfiles still on Jetty 12.1.12 on the SOLR-18297 branch (vm5 review, reports/newdev-critblock-review-round-1/solr-18297-jetty-12-1-14.md).

## 4. Follow-ups and stores

- Store 1 (follow-ups and reviews of existing work): the promise ledger in pool/work-stores.md; pool/tickets-to-open.md has six rows.
- Store 2 (skip list): pool/work-stores.md describes the batches; not reviewed in this pass.
- Store 3 (CSV backlog): paused by owner direction on 2026-10-20.

## 5. Stale claims

About 180 claim files have no terminal line. Most are probably stale. Verify each heartbeat before counting it live; do not close claims by age.
