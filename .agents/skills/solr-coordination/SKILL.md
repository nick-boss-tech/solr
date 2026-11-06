---
name: solr-coordination
description: Coordinate Solr factory assignments, claims, gate handoffs and receipts on the active shared board. Use when finding or handing off board work, reconciling ownership, or checking exact-head proof; not for ordinary Solr code review or implementation alone.
---

# Solr coordination

Find the currently designated live board from the workspace bootstrap and explicit owner direction. The owner-directed switch to `coordination-v2` is effective when `reports/coordination-v2-cutover-2026-11-06.md` exists on `coordination-ui`; before that use the old board. Afterward, use `coordination-v2` and consult the old board for historical or late records. Read the live tip and relevant workflow instructions before acting. Take all status and ownership decisions from one fetched snapshot, then refresh and reconcile immediately before a write.

Resolve the requested work from its assignment, claims, gate and receipts, including newer reports and superseding records. Generated indexes can lag. Check host capability and active ownership before claiming; preserve existing claims and never run the same gate concurrently. Link source snapshots and exact fork heads in handoffs. A Linux receipt proves only the named head and completed proof legs; distinguish a failed, incomplete or non-reproduced run from a green gate.

For board writes follow the live board's add-only and concurrency rules, use its approved posting mechanism, and verify the posted content and reachable commit. A handoff is a board record, not just chat. Do not infer public PR or Jira posting permission from an internal assignment; follow the user's explicit direction and applicable standing authorization. Preserve the host's test restrictions.

For this draft's cutover requirements, read [the migration plan](../../../MIGRATION.md). For Solr navigation, review, implementation or proof itself, use the relevant established workflow or skill; this coordination skill does not replace those procedures.

For a project-neutral board example and the proposed Elephant-Goldfish relationship, read [the portable design](../../../docs/portable-board.md). A fresh-context review requires authorized delegation and independent context; do not report that review was performed when it was not. The board stores the resulting records, but does not itself provide independent review or enforce process locks.
