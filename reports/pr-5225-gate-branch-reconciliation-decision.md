# PR 5225 gate reconciliation: selected follow-up candidate

Nick confirmed on 2026-10-25 that `solr-5225-bats-followup` is the intended submission candidate. A separate gate record is now posted at `gates/pr-5225-bats-followup-round-1.md`, pinned to fork head `35378004f1797c5b9ae3ba68d2503e4edb3840e8`.

The older `gates/pr-5225-allowpaths-denial-round-1.md` remains unchanged and OPEN for its original branch `solr-pr-5225-allowpaths-tests-submit` at `3a32098d870da94d663f2948a1ba4adf32f1675d`. Its two-test scope, four-leg expectations, and mutation controls do not transfer to the one-test follow-up branch, and its receipt remains absent.

The follow-up gate has not been claimed or run as a gate. The exact-head `tidy` preflight passed and the worktree is clean. Fresh JDK25 Commons CLI and Picocli legs at `35378004f1797c5b9ae3ba68d2503e4edb3840e8` each passed one test. The JDK21 legs currently recorded for this branch are from parent head `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`, so the new gate requires fresh JDK21 runs at the pinned final SHA. The three failure controls and the packaging check at the exact final SHA remain outstanding. No mutation or gate-receipt claim is made.

The reconciliation and new gate record are add-only. No existing assignment, claim, gate, receipt, product source, PR, or Jira record was edited.