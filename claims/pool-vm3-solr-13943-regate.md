# Claim: SOLR-13943 re-gate of the PR 5089 head (vm3)

Status: DONE 2026-10-18T20:00Z. Deliverable: `receipts/SOLR-13943.md` (GREEN at head 33fa8d1381).

- Claimant host: vm3.
- Slice: merge `solr-13696-submit` head 6914eae1c2 into `solr-13943-submit` (old head ca443f6f68), fast-forward push, re-gate of the merged head, PR 5089 body update. No JIRA or GitHub comment besides the PR body edit.
- Gate: tidy and Error Prone compile pass; normal-mode routed-alias classes 0 failures; `:solr:core:check -x test` passes. Logs under `gates/logs/vm3/13943/gate1/`.
- Interrupted: the first normal-mode run was killed when the previous session ended. Its partial log is kept; the rerun is the one in the receipt.
