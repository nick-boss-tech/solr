# Claim: SOLR-10424 V2 UUID default fix on solr-10424-submit (vm2)

- Claimant host: vm2 (Linux). Follows assignments/pool-review-2026-10-17-10424-retry-round-1.md (CONFIRMED, reports/2026-10-16-pr-batch/SOLR-10424-check.md).
- Capability tags used: implementation, gate.
- Decision basis: the item's recorded default (restore the id-less UUID default on the V2 update paths, pool-review-2026-10-16-code-round-1 decision row), decided under the decision ladder (rung 2). The row's escalation clause for a config change is noted in the decision record, since the fix extends the existing initParams mechanism rather than adding a new config surface.
- Branch solr-10424-submit at 5b087b1a389c892850401f79154824607cb338ac (ls-remote at claim time). Fast-forward push only.
- Scope: one solrconfig.xml initParams path, and the focused test file. No changelog edit, no PR body edit, no comment.

Heartbeat: 2026-10-17T06:55Z (claim).
- 2026-10-17T07:28Z pushed solr-10424-submit fast-forward 5b087b1a38 to 5f38ba54a8 (UpdateAPI rewriteToJsonDocs sets update.chain=json-docs-uuid for both V2 update endpoints; focused test added). Focused run on the exact commit: 2 of 2 pass (logs/vm2/10424-fix-final.log).
- Status: DONE (fix pushed, 2026-10-17T07:28Z). Record: reports/2026-10-16-pr-batch/SOLR-10424-check.md.
