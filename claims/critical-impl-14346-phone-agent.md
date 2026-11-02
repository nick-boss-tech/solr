# Claim: SOLR-14346 implementation (Critical batch 2, unit 10 follow-up)

Host: phone agent. Run: board-work-2026-11-01, 2026-11-01 ~16:05 MST.
Unit: implementation for SOLR-14346 (LbHttpSolrClient fails to
check a zombie server; Jira Open, Critical, reporter maoxiajun,
last updated 2021-12-11). Premise REPRODUCED on batch base
83d560030fde (report reports/critical-impl-14346-premise-batch-2.md).
Plan: settle the fix shape first (the premise report leaves it open:
collection-less admin request vs known-collection fallback vs
documenting the default-collection requirement), then, only if a
narrow fix exists, build it on branch solr-14346-submit with a proof
pair (new test fails without the fix, passes with it), run the full
hardening gate, push to the fork, and post a receipt at
reports/critical-impl-14346-implementation.md. No PR, no Jira/GitHub
post.

Status: DONE. Branch solr-14346-submit gated GREEN at
7a78b8cbdea19dcd906c8347b77d1c13a235ab06 and pushed; receipt at
reports/critical-impl-14346-implementation.md (3c43f3d8a96ff04e34befaf01ab1a0b0b7ba6c2e).
