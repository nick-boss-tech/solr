# Claim: SOLR-18010 replace-semantics premise run (vm2)

- Claimed: 2026-10-13T16:55Z by vm2 (gate backlog coordinator). Job: gates/SOLR-18010-replace-semantics-premise.md.
- Code under test: draft PR #5118 head 2b5ad801c9e88807337f4f0a3fdd689126ad37d1 (fork tip of solr-18010-submit at claim time, confirmed by ls-remote), and the merge-base 14c7aac0d151402b00259e2fb9bf5eed7049ec5d for contrast.
- Capability tags: premise-run.
- Scope: Q1 (symlink) run. Q2 (bind mount) and Q3 (ownership) are expected to be skipped: the vm2 account has no root or sudo and no second user. The skip reason is recorded in the job file.
- Throwaway probe only. No fork push, no PR touched, no branch change.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: CLAIMED by vm2 at 2b5ad801c9.
- 2026-10-13T17:08Z vm2 coordinator: Q1 answered by run at both legs (head 2b5ad801c9, base 14c7aac0d1). Q2 and Q3 skipped (no mount rights, no second user). Verdicts in the job file and the review report addendum.
- Status: DONE by vm2, 2026-10-13T17:08Z.
