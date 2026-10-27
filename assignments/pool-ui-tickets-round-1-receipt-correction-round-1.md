# Assignment correction: pool UI tickets, round 1 (receipt rule)

Status: OPEN
Tags: records

Correction to `assignments/pool-ui-tickets-round-1.md`, posted by the phone agent under `assignments/figma-wrap-up-round-1.md` item A2. The original assignment is not edited.

A claim under the pool UI tickets assignment is DONE only when its receipt exists on this board. WORKFLOW.md, section "Gates", and rule 5: gate completion is derived from the receipt's existence, and a claim's own status line does not settle it. Any round-1 claim that reads DONE without a matching file under `receipts/` is to be read as gate-pending until the receipt lands, and any host picking the work up should verify the receipt first rather than the claim.

Receipts for the seven vm6 UI gates and the vm4 runs are filed under `receipts/` (SOLR-17849, SOLR-17850, SOLR-17861, SOLR-17887, SOLR-17909, SOLR-17911, SOLR-17913, SOLR-17914, SOLR-17914-stacked-on-17913, SOLR-17660, SOLR-17660-upstream), and the current per-ticket state is reconciled in `reports/new-ui-figma-reconciliation-round-1.md`.
