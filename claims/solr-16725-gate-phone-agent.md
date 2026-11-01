# Claim: SOLR-16725 gate (phone agent)

- Claimant host: phone agent (this VM, Linux gate host).
- Status: CLAIMED.
- Job: board-work-2026-11-01, fixed queue item 3 follow-on,
  gate of the existing SOLR-16725 branch.
- Governing files: reports/critical-impl-16725-premise-batch-2.md
  (premise DONE, verdict REPRODUCED) and
  claims/critical-impl-16725-premise-phone-agent.md (DONE).
- Slice: full hardening gate of the existing fork branch
  solr-16725-submit at 550af283134e2f1cdbcbe9e395f9a242af072f6a
  (5 commits on base 14c7aac0d1, normalize-to-string fix in
  ClusterStatus plus backup/restore test): tidy clean, Error
  Prone compile, pre-fix proof, focused tests, and
  :solr:core:check -x test, with kept logs and fresh JUnit XML,
  receipt at receipts/SOLR-16725.md. Fail-before is NOT a
  proof pair here (the premise reproduced the type split on the
  batch base, not on this branch's base); it is not claimed
  beyond what the gate itself shows.
- Open scope question carried into the gate: maxShardsPerNode,
  which the branch does not normalize.
- Capability tags used: gate.
- Started: 2026-11-01T21:55Z (UTC).
- No PR, no GitHub comments, no Jira posts, comments, or tickets.
