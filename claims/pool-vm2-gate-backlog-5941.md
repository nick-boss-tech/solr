# Claim: SOLR-5941 third getBool site fix and gate

Claimed 2026-10-15T20:48Z by the vm2 gate backlog coordinator.

- Job: gates/SOLR-5941-getbool-fix.md
- Branch: solr-5941-submit
- Head (verified live with git ls-remote at claim time): a4df7bfd214b15d9c8d4519fdac8fd6df9468f2d
- Runner path: /workspace/gates/g5941-gate.sh
- Worktree: /workspace/gates/solr-5941-gate

Scope: the one-line string compare at IgnoreCommitOptimizeUpdateProcessorFactory.java line 125, a SolrCloud chain test for a forwarded commit with commit_end_point=replicas, then the full gate of the job file. On green the job file's push and receipt refresh apply. Stop rule per the job file, step 1.
