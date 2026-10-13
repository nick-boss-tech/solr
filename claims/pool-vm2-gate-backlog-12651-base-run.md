# Claim: SOLR-12651 base-code run at the live head (gate backlog coordinator)

- Claimed: 2026-10-13T04:45Z. Job: gates/SOLR-12651-base-run.md.
- Head at claim: 404f13b501c97d6c6b65743a27f54f4f38363afa (solr-12651-submit, verified by ls-remote at claim time). Merge-base 14c7aac0d151402b00259e2fb9bf5eed7049ec5d.
- Capability tags: gate.
- Runner: gates/g12651-base-gate.sh, worktree /workspace/gates/solr-12651-base (detached at the merge-base, with the branch's TestLocalFSCloudBackupRestore.java overlaid). No code change, no fork push.
- Run: 2026-10-13T04:48:27Z to 04:55:14Z. Base rc=1; tests=2, failures=1, errors=0. Failure: "Failed restore left collection backuprestore_testokboo2 behind" (seed C039DD4A67379E23). Matches the premise.
- Status: RUN ENDED, result recorded in gates/SOLR-12651-base-run.md. Receipt section and DONE left for the main agent.
