---
name: solr-maintain
description: Reconcile Solr PR discussion, CI, branch heads, incorporated fixes and gate receipts to determine the next follow-up action. Use for PR maintenance and bounded read-only monitoring plans.
---

# Maintain a Solr contribution

Refresh the actual PR state, current head, substantive discussion including inline threads, and relevant fork/board records. Compare CI run SHAs with that head. Read previous action records to avoid duplicate offers or messages. Work from current session authorization and applicable workspace instructions.

Determine what changed: reviewer request, branch movement, proof completion/failure, incorporation, merge/closure, or no meaningful change. An existing green receipt covers its recorded head only. A posted gate is not execution. A PR merged during preparation requires reconciliation with main before proposing a new patch or pull-in commands.

Return a compact action record: observed revisions, new event and source, evidence status, recommended scoped action and owner/host, publication state and next dependency. Continue authorized work; identify a genuine missing decision only when the existing context cannot settle it. Keep review findings, authored fixes and executed proof distinguishable.

For automation planning, read [the bounded maintenance contract](references/automation-contract.md). An on-demand check does not create a recurring job. Use a scheduler's supported tool only when the user requests a schedule with sufficient cadence/destination context. This skill does not itself grant public-write authority or release a held draft. Report meaningful change, completion, failure or required input rather than repeating unchanged snapshots.
