# Claim: critical-impl-7926-premise-phone-agent

- Claimant host: phone agent (this VM, Linux gate host).
- Status: DONE.
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

Result: DONE 2026-11-01T21:15Z. Verdict REPRODUCED. Scratch probe
on main 83d560030fde, seed 7926C0FFEE7926, BUILD SUCCESSFUL, 1
test, 0 failures: index tokens luc[0,6] luce[0,6] lucen[0,6]
lucene[0,6]; unified and original highlighters both return
<em>lucene</em> for query ngram:luc, i.e. the whole word, not
the matched prefix. Report at
9bbf415b7247b7802e0bc34d6bb1241b56db0c23
(reports/critical-impl-7926-premise-batch-2.md, verified by
read-back). Probe worktree removed after the run; run log kept
at ~/workspace/tools/7926-premise-probe.log and fresh JUnit XML
at ~/workspace/tools/7926-premise-TEST-Solr7926PremiseProbeTest.xml.
No branch, no push, no PR, no Jira/GitHub post.

DONE: 2026-11-01T21:15Z.
