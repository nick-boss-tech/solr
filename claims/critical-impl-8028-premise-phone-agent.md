# Claim: SOLR-8028 premise (Critical batch 2, unit 7)

Host: phone agent. Run: board-work-2026-11-01, 2026-11-01 ~14:24 MST.
Unit: one premise run for SOLR-8028 (ConcurrentModificationException
from JavaBinCodec.writeArray while marshalling a SolrInputDocument
under pressure test; Jira Open, Critical, last updated 2017-02-03).
Plan: fresh worktree on current main, concurrent-marshal probe of a
shared SolrInputDocument plus a read of the current JavaBinCodec
write path; settle whether a reused document instance / reused field
value lists trigger the CME. Scratch probe only, never committed.
Report to reports/critical-impl-8028-premise-batch-2.md. No PR, no
Jira/GitHub post.

Status: DONE. Verdict REPRODUCED (concurrent mutation of a shared
backing list during marshal; sequential and mutation-free concurrent
marshals clean). Report at
reports/critical-impl-8028-premise-batch-2.md, verified by read-back.
