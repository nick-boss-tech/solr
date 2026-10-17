# Claim: code round 1, item 1, SOLR-10424 (PR 5159), V2 update id-less UUID default

- Claimant host: vm2 (Linux). Assignment: assignments/pool-review-2026-10-16-code-round-1.md, item 1.
- Capability tags used: implementation, gate.
- Head: solr-10424-submit at 5b087b1a389 (ls-remote at claim time). Upstream main at e044bf20b405 is the reference base; the check runs at head and at main.
- Scope: a focused test posting an id-less JSON document to /api/update/json, run at the head and at upstream main. A head fix only if the check fails at the head (per the assignment's decisions). Escalate instead if the fix needs a config or API change.
- Deliverable: reports/2026-10-16-pr-batch/SOLR-10424-check.md.

Heartbeat: 2026-10-17T04:20Z (claim).
