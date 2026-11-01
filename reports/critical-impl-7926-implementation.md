# SOLR-7926 implementation: fix shape settled, NO narrow fix, NO branch

- Ticket: SOLR-7926, "Hit highlighting with EdgeNGramFilterFactory",
  Bug, Reopened, Critical, last updated 2016-02-05 by reporter
  Bjorn Hjelle. No committer activity on the ticket since 2016.
- Unit: board-work-2026-11-01, fixed queue item 3, inserted unit
  after the reproduced premise
  (reports/critical-impl-7926-premise-batch-2.md). Claim:
  claims/critical-impl-7926-phone-agent.md.
- Premise, already on record: on current main, EdgeNGram index
  tokens for "lucene" are luc[0,6] luce[0,6] lucen[0,6] lucene[0,6],
  and both the unified and original highlighters return
  <em>lucene</em> for query ngram:luc. That verdict stands and is
  not re-litigated here.

## Verdict: no narrow Solr fix exists for this ticket

The implementation unit was charged, by the premise report's own
caution, with settling the fix shape before treating this as a
small narrow fix. Settled: the shape does not fit a Solr branch.
No branch was created, nothing was pushed, no PR, no Jira or
GitHub post from this unit.

## Why no Solr branch

1. The code that sets the offsets is not in Solr. EdgeNGram
   lives in Lucene: org.apache.lucene.analysis.ngram
   .EdgeNGramTokenFilter and EdgeNGramFilterFactory ship in
   lucene-analysis-common, which this repo consumes as a binary
   dependency (10.4.0 in the local Gradle cache). There is no
   Solr-side EdgeNGram factory in solr/core/src/java/org/apache
   /solr/analysis, and Solr's highlighters delegate to Lucene's
   (UnifiedSolrHighlighter wraps Lucene's UnifiedHighlighter).
   A Solr PR cannot change what offsets EdgeNGram emits; it can
   only work around them from outside.
2. The current Lucene API offers no offset option. Inspecting
   lucene-analysis-common 10.4.0 confirms EdgeNGramTokenFilter
   exposes only min/max gram size and preserveOriginal. The
   stacked whole-word offsets are the behavior, not a missing
   parameter.
3. The ticket thread itself never settled a shape, and its one
   concrete direction is a Lucene design change. Jan Hoydahl
   (2015-08-14) noted the stacking follows LUCENE-3907.
   Michael McCandless (2015-08-14) said the stacking seems more
   correct for phrase queries, and that the possible direction
   was relaxing the token-graph requirement that tokens leaving
   a node share a startOffset and tokens arriving at a node
   share an endOffset, with unknown problems. David Smiley
   agreed that direction seemed best, also unknown. Nothing
   since 2016 has resolved those unknowns.

## Candidate shapes evaluated and set aside

- Lucene option on EdgeNGramTokenFilter (gram-length offsets,
  defaulting to current behavior, or a behavior change). This is
  the principled fix, but it is a Lucene-repo change with
  back-compat impact on every consumer of indexed offsets, and
  it needs Lucene maintainer direction. It cannot be landed as
  a Solr branch in this repo, and it is not small.
- A new Solr-side token filter that rewrites stacked tokens'
  endOffsets to startOffset plus gram length after EdgeNGram.
  This would ship exactly the invariant violation McCandless
  flagged (tokens at one position with differing endOffsets)
  into users' indexes, with unknown downstream breakage in
  graph consumers, and it would be a new user-facing filter
  (a feature with docs and migration notes), not a bug fix.
  Rejected as a forced fix for a premise that does not support
  it.
- Highlighter-side truncation of the highlighted span to the
  matched query term's length. There is no clean hook: Lucene's
  UnifiedHighlighter builds passages from stored index offsets,
  passages can hold several matched terms, and the surgery
  would fix one highlighter while leaving the original and
  FastVector highlighters showing the whole word. Rejected as
  inconsistent and fragile.
- Reindexing guidance (query-time EdgeNGram, separate field,
  preserveOriginal) is configuration advice, not a code fix,
  and belongs in a comment or docs discussion, not a branch.

## What this leaves

- The defect is real and reproduced, but its fix is a Lucene
  design decision (option on EdgeNGramTokenFilter, or relaxing
  the token-graph offset invariant), or a maintainer call that
  whole-word highlighting for EdgeNGram fields is accepted
  behavior. Either way the next step is a question to
  maintainers, not a branch. Posting that question is Nick's
  later decision; this unit prepares nothing for posting beyond
  this record.
- A closure/comment draft for this ticket already exists on the
  board under jira/ from the Critical comments program; that
  line is separate from this implementation unit and is not
  touched here.
- No gate ran: there is no branch to gate, and running a gate
  on an unchanged tree would prove nothing.

## Queue effect

SOLR-7926 leaves the implementation queue with this verdict
instead of a branch. The ranking queue resumes at unit 7,
SOLR-8028 premise, on the next wake.
