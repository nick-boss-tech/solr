# Claim: SOLR-17539 docs-shaped re-gate at the live tip (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T10:52Z by vm2. Job: gates/SOLR-17539-tip-regate.md (restock of 2026-10-13T10:39Z).
- Head at claim: f9d201a278b9418ae71d8123ff9269edccd7776a (solr-17539-submit live tip). Gated head: bce505f45acd20f80686cb448e17e7c5717d6f25.
- Module: :solr:solr-ref-guide. The tip changes one ref-guide page (modules/configuration-guide/pages/caches-warming.adoc). The gate log g17539-gate.log named in the job is not on vm2; the module is taken from the changed path and this note records that inference.
- Capability tags: gate (docs only, no focused Java tests, no fix commit, no fork push).
- Runner: gates/g17539-gate.sh, worktree /workspace/gates/solr-17539-gate (detached).
- Status: CLAIMED, runner queued.
