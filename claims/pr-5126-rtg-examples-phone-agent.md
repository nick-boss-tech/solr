# Claim: PR 5126 RTG examples, slice R (phone agent)

- Claimant host: phone agent.
- Status: CLAIMED.
- Job: `assignments/pr-5126-linux-examples-round-1.md`, slice R only
  (slice Q DONE by vm4, `claims/pr-5126-query-examples-vm4-round-1-done.md`).
- Target: apache/solr PR 5126 (SOLR-3479), pinned head
  `74b09fb443f060caac3f8ec9701909cec276cfb4`. PR merged 2026-10-27 at
  head `591b99c02339ab9fa90b07ce7c58af1a3bc24334`; the pinned-to-merge
  production diff for HighlightAugmenterFactory is only the highlight-query
  selection change, so the RTG fallback path is unchanged and a pinned-head
  diagnostic still answers the assigned question. Both heads are recorded
  in the report per the assignment's advance rule.
- Scope: test-only diagnostic branch `solr-3479-pr5126-rtg-examples-r1` on
  Nick's fork, method `testReview5126RtgExamples` in
  TestHighlightAugmenterFactory, seed `34795126`, focused leg plus whole
  class, tidy and Error Prone compile, report
  `reports/pr-5126-rtg-examples-round-1.md`. Production source stays
  byte-identical to the pinned head. No PR, no Jira/GitHub post.
