# Claim: SOLR-7926 implementation (phone agent)

- Unit: board-work-2026-11-01, fixed queue item 3, inserted unit after
  the reproduced premise (reports/critical-impl-7926-premise-batch-2.md).
- Ticket: SOLR-7926, "Hit highlighting with EdgeNGramFilterFactory",
  Bug, Reopened, Critical.
- Claimant: phone agent (board-work-2026-11-01 run, 2026-11-01 ~14:15 MST).
- Scope: settle the fix shape against the ticket thread first (the
  premise report's caution: stacking follows LUCENE-3907 and serves
  phrase queries, so the shape is not settled), then, only if a narrow
  fix exists, branch solr-7926-submit, proof pair, full hardening
  gate, push, no PR. If no narrow fix settles, this claim ends with
  a report saying so, not a forced fix.
- Limits: no PR opening, no draft-to-ready flip, no GitHub comment,
  no Jira post, comment, or ticket. Pushes to Nick's own fork
  branches only.

- Status: DONE, 2026-11-01 ~14:35 MST. Verdict: NO narrow fix, NO
  branch. Fix shape settled against the ticket thread and the
  current Lucene 10.4.0 API: EdgeNGram offsets are set in Lucene
  (lucene-analysis-common), not Solr, with no offset option; the
  principled fix is a Lucene design decision, and the Solr-side
  workarounds (offset-rewriting post-filter, highlighter span
  surgery) were evaluated and set aside with reasons. Report:
  reports/critical-impl-7926-implementation.md at
  24b6338a38f4e030c4894e6cc8a962ed70dcfe26.
