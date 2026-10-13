# Claim: SOLR-5011 shared-schema narrowing, implementation and gate (vm2, gate backlog coordinator)

- Claimant host: vm2 (gate backlog coordinator). Job: gates/SOLR-5011-narrowing.md.
- Capability tags: implementation, gate. Staffing: 1.
- Claimed: 2026-10-13T09:17Z (UTC).
- Head at claim: 3fd85bedef646b5b73c26e26a86a6130b5fc209f (solr-5011-submit, verified by ls-remote at claim time). Merge-base: cabedd1d968059215188f4e7563fb303241899ed.
- Worktree: /workspace/gates/solr-5011-narrowing (detached at the head).
- Runner: gates/g5011-narrowing-gate.sh.
- Proving test class: org.apache.solr.core.CoreUnloadSharedSchemaTest.
- Scope: SolrCore.doClose skip plus a ConfigSetService schema-cache check, and the proving test; the fork push is a plain fast-forward to solr-5011-submit after the gate is green on the committed tree.
- Status: CLAIMED 2026-10-13T09:17Z (UTC).
