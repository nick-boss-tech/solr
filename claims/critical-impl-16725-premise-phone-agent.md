# Claim: SOLR-16725 premise (Critical batch 2, unit 8)

Host: phone agent. Run: board-work-2026-11-01, 2026-11-01 ~14:43 MST.
Unit: one premise run for SOLR-16725 (CLUSTERSTATUS API output has
inconsistent data types for a few values against the newly restored
collection; Jira Open, Critical, reporter Indumathy Rajagopalan,
last updated 2023-06-06).
Pre-claim check: no 16725 premise report or claim on coordination-ui,
no receipt, no gate file, no PR. NOTE: fork branch solr-16725-submit
already exists at 550af28313 (Nick Shanin, 5 commits on base
14c7aac0d1) with a normalize-to-string fix in ClusterStatus and a
backup/restore test asserting replica-count wire types; it has no
board receipt or gate, so it does not replace this premise unit.
Plan: scratch probe only, never committed: fresh worktree at batch
base 83d560030fde, run the submit branch's backup/restore test
method with production code unmodified, and check whether the
restored collection reports replica counts as ints while the source
reports strings, as the ticket describes. If the premise reproduces,
the next unit is NOT a fresh implementation (one already exists);
it is verification/gate of solr-16725-submit, with the open question
of whether maxShardsPerNode (also int-vs-string in the ticket) is
in scope.
Report to reports/critical-impl-16725-premise-batch-2.md. No PR, no
Jira/GitHub post.

Status: DONE. Verdict REPRODUCED (tlogReplicas and pullReplicas:
source String, restored Long, on batch base 83d560030fde, seed
16725C0FFEE16725; replicationFactor/nrtReplicas Long on both sides;
maxShardsPerNode absent from both outputs here). Report at
reports/critical-impl-16725-premise-batch-2.md, verified by
read-back. Next unit is a gate of the existing solr-16725-submit
branch at 550af28313, not a fresh implementation.
