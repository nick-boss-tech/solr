# Open-work cleanup, round 2 (phone-agent-directed records)

Posted by: phone agent, 2026-10-27, at Nick Shanin's direction ("clean this up and assign to any host").

## Why

The 2026-10-27 overnight buffer check showed the claimable lanes running dry (unclaimed implementation down to one stale item by 06:58; review and draft at zero around 04:28) while the generated census still counted dozens of open assignments. Most of that paper work is directed at a single host or already finished. The phone agent holds no factory tick, so anything directed at it waits for a session; this batch clears the phone-agent-directed records and makes one of them claimable by any host. Each outcome names the deliverable that settles it; nothing was closed on a status line alone.

## Settled in this batch

- `assignments/solr-18539-main-port-round-1.md`: flipped to DONE in this batch. vm2 claimed the whole assignment (`claims/solr-18539-port-main-round-1.md`, 2026-10-24) and gated it GREEN at the pushed head `9a16d680231` on branch `solr-18539-merge-totals-main`; receipt `receipts/SOLR-18539.md`, including the fail-before (both new assertions fail on base `79b23df58da`). The only remaining step is the owner's PR-opening go, which is not pool work.
- `assignments/board-tools-share-round-1.md`: stays OPEN, re-scoped in this batch from the phone agent to any host with the `records` tag. The work itself is unchanged (WORKFLOW.md names `pool/bin/board-push.sh` as the multi-file route; `pool/bin/post-file.sh` default branch becomes `coordination-ui` with a credential fallback; confirm no host relies on the retired default).

## Left open, on purpose

- `assignments/phone-agent-17861-license-scope-and-handover-round-1.md`: stays OPEN. It carries a license-scope decision for the owner on SOLR-17861, posed with its ladder run and recommendation; it is owner-waiting, not stale, and cannot be re-scoped.
- `assignments/phone-agent-needless-headers-deferred-round-1.md`: stays DEFERRED under its own terms until the owner reopens it.

## Not touched

Phone-agent-2's directed assignments and other hosts' directed items (for example vm5's smoke-data holds) belong to those hosts and were left alone; several are directed for data-locality reasons and re-scoping them would be wrong. Gates were not in this batch's scope.
