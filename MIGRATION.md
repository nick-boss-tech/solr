# Fresh-board transition plan

**Review preview only:** `coordination-ui` remains the live board. The intended direction is to start `coordination-v2` fresh and consider old work individually, not bulk-import the old queue. Preserve the old branch and its history as a reference.

## Working from old records

The [legacy work catalog](migration/legacy-work-catalog.md) lists assignments, claims and gates at one pinned source snapshot; the [full inventory](migration/legacy-inventory-0d91325.json) includes all tree paths and Git blob IDs, including evidence. Their statuses and references are observations, not conclusions about current ownership or completion. Refresh the source tip before deciding what to do with an item; use `tools/inventory.py` to compare snapshots and find later changes.

To carry an item over, first read the current old-board assignment, claims, gates, receipts, reports and relevant host state; verify the live source branch or PR head and any in-flight execution. Write a new, scoped v2 assignment that links the old record, its source snapshot, existing owner and the exact evidence needed. Coordinate a handoff with any active owner; never duplicate a running gate, infer that an old claim has expired from time alone, or treat an old receipt as proof for a changed head. Carry links to historical evidence instead of silently rewriting its provenance. Old work is not automatically open on the fresh board.

## Before switching

1. Agree how ongoing old-board work is finished or handed off. A fresh queue does not stop old workers. Identify active hosts and in-flight executions and establish a coordinated write freeze or equivalent single-writer handover, including a plan for offline and late writers. Do not infer quiescence from a stale heartbeat or a generated index.
2. Choose the production record format, canonical claim identity, and safe release/takeover protocol. Test simultaneous claims and process-failure recovery, and verify that all writers enforce the same rules. The preview writer lacks release and takeover; the legacy-compatible writer cannot prevent duplicate claims at different paths.
3. Audit and update the live board selector in each host's bootstrap, scripts, skills and scheduled tasks. Test each on an isolated target, including authentication, concurrency and error handling. No production default is changed by this preview. Capture host readiness explicitly.
4. Confirm that users can navigate the old branch and pinned evidence; document access and retention. Refresh the old snapshot and preserve a change report for late-arriving records. Make a deliberate one-by-one decision for any item that must remain active at the transition, including ongoing gate ownership and source head. Do not require every old record to be classified before starting fresh.
5. Publish the approved fresh-board instructions and make the single active-board switch in a coordinated window. Verify reads, claims, writes, evidence access, host defaults and the ability to find old work; record exact old and new heads. Keep a rollback plan and reconcile late old-board writes without duplicating work. Never force-push or delete the old branch.

An inventory, a preview branch, or a passing local writer test alone does not establish cutover readiness. Until the switch is actually coordinated and verified, all hosts continue to use `coordination-ui`.
