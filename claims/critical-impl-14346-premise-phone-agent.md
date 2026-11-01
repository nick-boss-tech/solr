# Claim: SOLR-14346 premise (Critical batch 2, unit 10)

Host: phone agent. Run: board-work-2026-11-01, 2026-11-01 ~15:44 MST.
Unit: one premise run for SOLR-14346 (LbHttpSolrClient fails to
check a zombie server; Jira Open, Critical, reporter maoxiajun,
last updated 2021-12-11).
Plan: fresh worktree on batch base 83d560030fde, scratch probe only
(never committed): a 2-node MiniSolrCloudCluster, an LBJettySolrClient
over the two nodes' root base URLs (no collection, matching the
ticket's repro) with a short alive-check interval; stop node 2, query
through the LB client so node 2 goes zombie, restart node 2, and
check whether the alive check revives it, against a
withDefaultCollection control. Settle whether the premise reproduces
on current main.
Report to reports/critical-impl-14346-premise-batch-2.md. No PR, no
Jira/GitHub post.

Status: CLAIMED.
