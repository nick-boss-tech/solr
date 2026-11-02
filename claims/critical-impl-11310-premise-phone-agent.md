# Claim: SOLR-11310 premise (Critical batch 2, unit 12)

Host: phone agent. Run: board-work-2026-11-01, 2026-11-01 ~17:47 MST.
Unit: one premise run for SOLR-11310 (QueryElevation does not work
with Solr LTR: with elevateIds and an LTR rq rerank, the elevated
document is not always first because the reranker ignores
elevations; Jira Open, Critical, reporter Rahul Babulal, last
updated 2020-07-30; one 2020 comment reproduces it on Solr 8.3).
Plan: fresh worktree on batch base 83d560030fde, scratch probe only
(never committed): index docs, elevate the worst-scoring document
via elevateIds, run an LTR rq rerank over it, and check whether the
elevated document holds first place. Note for the record: fork
branch solr-11310-submit already exists (Oct 2-4, base 14c7aac0d1,
"apply elevation after full rerank slice" plus test), but no batch-2
premise report or claim exists on coordination-ui, so this premise
run on current main stands on its own.
Report to reports/critical-impl-11310-premise-batch-2.md. No PR, no
Jira/GitHub post.

Status: CLAIMED.
