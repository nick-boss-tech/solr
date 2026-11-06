---
name: solr-proof
description: Prepare phase-specific Linux proof gates for Solr changes or assess their receipts against the required branch head and proof legs. Use for gate handoffs and evidence reconciliation, with execution limited by host capabilities.
---

# Prepare and assess proof

Choose the requested mode: prepare a gate, assess a receipt, or execute an already authorized gate on a capable host. Read the live board job and host capabilities first. Execution is not implied by a request to assess evidence. In Nick's Windows factory, route Solr builds/tests to Linux and do not use the retired local queue.

Use [the gate contract](references/gate-contract.md) to name the exact pushed head, base, tasks, proof type and execution receipt. Derive checks from the actual change and applicable instructions; do not copy a previous ticket's entire test ladder. Verify the remote fork ref after a push. Keep code changes under Implemented, Proof Pending until the receipt satisfies the required legs.

Distinguish a runtime assertion failure, validator rejection, coverage pin and environment interruption. A coverage-only addition may pass on both original and patched production code; use an executable isolated mutation when discriminating assertion proof is requested. Missing APIs or startup errors do not establish the intended assertion. A workflow syntax repair needs validator failure-before, not a fabricated Python-test failure.

When assessing a receipt, read the actual commands/logs and fresh output locators, check every required leg and compare tested SHA with offered SHA. Historical green, upstream CI, a posted assignment or a claimed job is not current complete proof. Report stale/incomplete results precisely and prepare the smallest continuation. Changed code or formatting requires a new pushed head and superseding proof record under the board's repair rules.
