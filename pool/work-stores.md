# The work stores: where pool work comes from

Owner direction, 2026-10-19: the pool's work lives in three stores. They are pulled in the order below. A host that finds nothing claimable in one store moves to the next store and names the store in its heartbeat. "No work for me" is not a reportable state while any store holds unclaimed items: between them the stores hold thousands of items, and the slices below are self-serve, so nobody waits for work to be posted for them.

The other half of this rule is the check before claiming: read the claims folder and the store's own records for the item first. An item that is already done or already claimed gets a WON'T DO under `pool/advisory-wont-do.md`, not a second pass. For CSV rows, also check the skip queue and the earlier store deliverables for the same issue key; the row stays in its two-row slice and is recorded as an overlap with the prior record and its actual status. A queue match alone does not mean the ticket is resolved. Plenty of work plus one claim per item is the whole design.

## Store 1: follow-ups and reviews of existing work

The closest to shipping, so it is pulled first.

- What: follow-ups promised in merged and open PR descriptions (the promise ledger in the merged-PR audit reports), review rounds on the prepared branches and their draft records in `drafts/`, receipts in `receipts/` whose state is NO GATE or awaiting a decision, and owner-decision answers awaiting implementation (`assignments/pool-owner-decisions-round-1-implementation.md`).
- Size: hundreds of prepared branches with drafts and receipts, plus the follow-up ledger.
- Claim unit: one item. Nobody posts these one by one: when you pull an item, post its per-item assignment and your claim in one push, claim first, then verify everything at the live tip before acting on any record.
- Boundary: a follow-up whose parent PR has not merged is worked as a named follow-up inside that PR's branch and records; it does not get its own ticket or branch until the parent merges, unless a maintainer or the owner directs otherwise.
- Every item pulled from this store is categorized and linked under `pool/work-map.md` (ticket or parent, category, siblings, supersedes) before code is written, and any ticket it promises is registered in `pool/tickets-to-open.md`.

## Store 2: the skip list

- What: `material/skip-audit/queue-current.json` (the verdict records) and `material/skip-audit/remediation-current.json` (the audit suspects; read live status because counts change as audits land). The posted work: the `assignments/pool-aaa-skip-verdict-remediation-*.md` redo batches, the re-audits owed on redone items, and `assignments/pool-aab-deferred-reaudit-round-1.md` (the 167 deferred cross-audit items in 13 slices, with the excluded hosts named per slice).
- Claim unit: one batch, one slice, or one ticket, first-post claim wins.
- Boundary: a second host's role on a redone item is re-audit, never a second redo. A remediation ticket is never taken as a fresh skip closeout. When the unbatched open items in remediation-current.json reach 25, the tick posts the next batch itself (see `pool/cron-prompt.md`); that rule is already in force and needs no one's permission.

## Store 3: the CSV backlog

Raw intake, pulled last because its items start furthest from shipping.

- What: `material/jira-issues-csv/` (18,364 tickets, 4,078 open). Critical and Blocker issues first: `assignments/pool-csv-critical-blocker-round-1.md` (100 tickets in four slices of 25, oldest first; this round is DONE). The rest follow by priority, then oldest first, under `assignments/pool-csv-backlog-round-1.md`.
- Claim unit: one slice of 2 open CSV rows. When every posted slice is claimed or done, any host cuts the next two rows itself in CSV pull order, without skipping rows that overlap the skip queue or the completed priority round. Do not renumber earlier slices. Post the slice file and your claim in one push. Nobody waits for a slice to be posted for them.
- Overlap check: for each CSV issue key, inspect the skip-audit records under `material/skip-audit/`, prior files under `material/jira-csv-triage/`, `reports/csv-critblock-*.md`, and matching ticket records under `assignments/`, `claims/`, `reports/`, `drafts/`, `gates/`, and `receipts/`. Follow the lookup and status rules in the backlog assignment. Record a ticket-specific prior result as `overlap`, with its exact path and state; an open remediation or live claim remains open, not handled.
- Per-ticket floor: the critical/blocker assignment required a fresh Jira read, code path, and reproduction where applicable. The general CSV backlog is metadata triage plus the overlap check; it does not re-read Jira, make a verdict, or close a ticket. A `stale` row is only a candidate for a later close review under the skip-queue rubric.

## Cross-cutting rules

- Order is preference, not a wall: a host already holding context in a lower store finishes that item before pulling fresh work. What the order forbids is idling in front of a full store, or reporting no work.
- Heartbeats and closeouts name the store and the slice or item, so a census can see the spread across stores at a glance.
- Where a store's own assignment and this page disagree about pull order, this page governs the order and the assignment governs the method.
- A store is empty only when its underlying records are empty. Slices get cut and batches get posted by the hosts themselves, so a store running out of posted work is a five-minute self-serve task, never a stop.
