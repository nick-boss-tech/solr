# Gate job: SOLR-18564 / PR 5192 exact-head gate

Status: OPEN
Tags: gate

- PR: apache/solr#5192, open and ready. Branch: `corecontainer-shutdown-closes` on the fork.
- Head to gate: `dd65b21b41dc1d008c51031b71f61964ee2ca169` (the live tip; the fix commit `d7caa8cb784` was gated on 2026-10-22, and one changelog commit sits on top of it; this job produces the evidence at the exact head).
- Module: `:solr:core`.
- Legs: changelog YAML parse for the branch's added entry; tidy with the GATE INVALID guard; Error Prone compile (`-Pvalidation.errorprone=true`); focused tests `CoreContainerShutdownTest` and `TestCoreContainer`; `:solr:core:check -x test`.
- Deliverable: extend `receipts/SOLR-18564.md` with the exact-head section, citing the gate log path and the fresh JUnit XML.

Why: the PR is the SOLR-18564 vehicle in the RC3 fix set; its evidence should cover the head a reviewer would merge.
