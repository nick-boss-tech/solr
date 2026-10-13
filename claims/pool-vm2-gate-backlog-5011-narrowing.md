# Claim: SOLR-5011 shared-schema narrowing, implementation and gate (vm2, gate backlog coordinator)

- Claimant host: vm2 (gate backlog coordinator). Job: gates/SOLR-5011-narrowing.md.
- Capability tags: implementation, gate. Staffing: 1.
- Claimed: 2026-10-13T09:17Z (UTC).
- Head at claim: 3fd85bedef646b5b73c26e26a86a6130b5fc209f (solr-5011-submit, verified by ls-remote at claim time). Merge-base: cabedd1d968059215188f4e7563fb303241899ed.
- Worktree: /workspace/gates/solr-5011-narrowing (detached at the head).
- Runner: gates/g5011-narrowing-gate.sh.
- Proving test class: org.apache.solr.core.CoreUnloadSharedSchemaTest.
- Scope: SolrCore.doClose skip plus a ConfigSetService schema-cache check, and the proving test; the fork push is a plain fast-forward to solr-5011-submit after the gate is green on the committed tree.
- Status: CLAIMED 2026-10-13T09:17Z (UTC); implementation committed locally, gate queued.
- Implementation (local commits in /workspace/gates/solr-5011-narrowing, not yet pushed to the fork): 7fa066e08e (SolrCore.doClose skips the loader close when a schema cache is enabled; ConfigSetService.isSchemaCacheEnabled) and 00060d2ed9 (CoreUnloadSharedSchemaTest). Delta against 3fd85bedef is those three files only.
- Gate queued: gates/g5011-narrowing-gate.sh at 2026-10-13T09:36:36Z. Proof leg reverts SolrCore.java and ConfigSetService.java to 3fd85bedef; the proving test must fail there by the class-loading content, and the control test must pass there.
- Heartbeat 2026-10-13T10:16Z: gate running (GATE START 09:55:22Z, at focused-head step; compile-errorprone rc=0; proof leg rc=1 nonzero as expected, content check pending at GATE END). No GATE END yet; no relaunch.
- Result 2026-10-13T10:21:43Z (GATE END): GREEN by the job's proof shape (proof leg fails by content, as designed). The earlier "NOT GREEN under the all-rc-0 rule" reading is superseded; no escalation needed. Head 00060d2ed92bc6414fb420d7c52558c14c49f75a, log logs/vm2/queue-g5011-narrowing-gate-20261013T095522Z.out. All legs rc=0 except the proof leg, rc=1 by content (both leg tests fail at the add-field-type op with ClassNotFoundException SharedSchemaFieldType; the shareSchema-off control passes). Fork not pushed; receipt not refreshed; not relaunched.
- Status: GATE GREEN at 00060d2ed92bc6414fb420d7c52558c14c49f75a (receipt written); fork push and DONE mark pending in the same session.
