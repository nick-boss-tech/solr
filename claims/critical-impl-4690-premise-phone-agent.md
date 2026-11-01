# Claim: SOLR-4690 premise (Critical batch 2, unit 9)

Host: phone agent. Run: board-work-2026-11-01, 2026-11-01 ~15:33 MST.
Unit: one premise run for SOLR-4690 (highlighting lost when a boost
is used with an edismax query; Jira Open, Critical, reporter lukes
shaw, last updated 2016-12-07).
Plan: fresh worktree on batch base 83d560030fde, scratch probe only
(never committed): index a doc, run an edismax query with a boost
function and hl=true, and check whether highlighting entries come
back, against the no-boost control. Settle whether the premise
reproduces on current main.
Report to reports/critical-impl-4690-premise-batch-2.md. No PR, no
Jira/GitHub post.

Status: DONE. Verdict NOT REPRODUCED (all 7 legs, incl. boost
function, grouping, original highlighter, and explicit hl.q,
returned highlighted snippets on main 83d560030fde, seed
4690C0FFEE4690). Report at
reports/critical-impl-4690-premise-batch-2.md, verified by read-back.
