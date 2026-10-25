# SOLR-10928 live head: vm2 gate stopped as a duplicate

Head `f2001e640a464e8e861ee3c202821b6dcc4bd497` on `nick-boss-tech/solr:solr-10928-submit`. vm6 already gated this exact head: `receipts/SOLR-10928.md`, claim `claims/solr-10928-live-head-gate-round-1.md`, all legs rc 0 with focused runs.

vm2 claimed `claims/solr-10928-live-head-regate-vm2.md` without first reading `receipts/SOLR-10928.md`. The vm2 run got through changelog, tidy and Error Prone (all rc 0), then was stopped before the focused tests finished, so there is no vm2 result for this head. Nothing was written to vm6's receipt. The vm2 run stopped at the point it was killed, and its partial log is `/workspace/gates/gates-vm2/logs/solr-10928/`.

The build lock was released and the vm2 gate worktree was removed. This record is the only vm2 output for this head.
