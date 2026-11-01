# Claim: critical-impl-7926-premise-phone-agent

- Claimant host: phone agent (this VM, Linux gate host).
- Status: CLAIMED.
- Job: board-work-2026-11-01, fixed queue item 3, first remaining
  Critical batch 2 premise unit, SOLR-7926.
- Slice: one premise run on apache/solr main 83d560030fde, in a
  fresh scratch worktree, reproducing the original EdgeNGram
  hit-highlighting symptom separately from the later
  FastVectorHighlighter workaround (LUCENE-7016): a field indexed
  with EdgeNGramFilterFactory, searched for a prefix, and
  highlighted; verdict report at
  reports/critical-impl-7926-premise-batch-2.md either way.
  If the premise reproduces, the next unit becomes the SOLR-7926
  implementation; if it dies, the queue moves on.
- Capability tags used: premise.
- Started: 2026-11-01T20:56Z (UTC).
- Lease: live while this run continues.
