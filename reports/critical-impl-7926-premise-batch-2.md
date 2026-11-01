# SOLR-7926 premise, Critical batch 2: REPRODUCED

- Ticket: SOLR-7926, "Hit highlighting with EdgeNGramFilterFactory",
  Bug, Reopened, Critical, last updated 2016-02-05 by reporter
  Bjorn Hjelle. No pre-existing owner report, receipt, branch, or
  PR for this ticket (board census and solr-work check, 2026-11-01).
- Unit: board-work-2026-11-01, fixed queue item 3, first remaining
  Critical batch 2 premise unit (ranking round 1, unit 6 in the
  batch-2 queue). Claim:
  claims/critical-impl-7926-premise-phone-agent.md.
- Scope, as the batch-01 triage required: reproduce the ORIGINAL
  EdgeNGram case separately from the later FastVectorHighlighter
  workaround failure. The 2016 workaround thread and LUCENE-7016
  (a FastVectorHighlighter StringIndexOutOfBoundsException with
  NGramFilterFactory) are a different symptom and were not
  replayed here. This probe uses EdgeNGramFilterFactory only,
  with the unified and original highlighters, and throws no
  exception; the symptom is the highlight span itself.

## What ran

Fresh scratch worktree at apache/solr main 83d560030fde
(origin/main, same base as the batch-2 queue). Scratch probe test
only, never committed, in
solr/core/src/test/org/apache/solr/highlight/Solr7926PremiseProbeTest.java
(SolrTestCaseJ4 harness, as in TestUnifiedSolrHighlighter, with
the cache system properties that solrconfig-cache-enable-disable.xml
requires), plus a scratch schema
solr/core/src/test-files/solr/collection1/conf/schema-7926.xml
copied from schema-unifiedhighlight.xml with one added field:

- Field type text_ngram, storeOffsetsWithPositions=true.
  Index analyzer: WhitespaceTokenizer, LowerCaseFilter,
  EdgeNGramFilterFactory minGramSize 3, maxGramSize 20 (the
  ticket's shape, without its WordDelimiter/PatternReplace
  stages, which do not affect the offset mechanism).
  Query analyzer: WhitespaceTokenizer, LowerCaseFilter only.
- Field ngram, type text_ngram, indexed and stored.
- One document: id 1, ngram "lucene". Query: ngram:luc.

Run: :solr:core:test --tests
org.apache.solr.highlight.Solr7926PremiseProbeTest, seed
7926C0FFEE7926, BUILD SUCCESSFUL, 1 test, 0 failures.
Run log: ~/workspace/tools/7926-premise-probe.log.
Fresh JUnit XML, timestamp 2026-11-01T21:10:14Z, copied to
~/workspace/tools/7926-premise-TEST-Solr7926PremiseProbeTest.xml.

## Evidence (all from that run's XML, not from ticket text)

- Index-analyzer tokens for "lucene", with offsets, exactly as
  the ticket's Admin analyse output showed:
  luc[0,6] luce[0,6] lucen[0,6] lucene[0,6].
  Every gram starts at 0 and ends at 6, the full word length.
- Unified highlighter (hl.method=unified), query ngram:luc,
  numFound 1, snippet for doc 1: <em>lucene</em>.
- Original highlighter (hl.method=original), same query,
  numFound 1, snippet for doc 1: <em>lucene</em>.
- The matched prefix is "luc"; the highlighted span is the
  whole word "lucene". That is the ticket's symptom, on current
  main, with both highlighters.

Two harness notes, for the record: the first probe attempt
failed at core init only because solrconfig-cache-enable-disable.xml
needs filterCache.enabled, queryResultCache.enabled, and
documentCache.enabled set (as TestUnifiedSolrHighlighter sets
them); the second attempt, with those properties, ran cleanly.
An earlier --no-daemon attempt failed on the Gradle journal
lock held by the idle shared daemon; the successful run reused
the shared daemon, as prior gates did. Neither failure bears on
the verdict.

## Verdict: REPRODUCED

The original EdgeNGram whole-word highlight symptom is live on
current main. Per the queue rule, the next unit becomes the
SOLR-7926 implementation (branch solr-7926-submit, proof pair,
gate, push, no PR).

## Caution for the implementation unit (not a verdict change)

The ticket's own thread does not settle a fix shape. Jan Hoydahl
noted the stacking follows LUCENE-3907; Michael McCandless
(2015-08-14) said the stacking seems more correct for phrase
queries and that relaxing the token-graph offset requirement
was one possible direction with unknown problems, and David
Smiley agreed that direction seemed best but also unknown.
Changing EdgeNGram offsets, or carving out a highlighting-only
path, would therefore touch behavior beyond highlighting, and
the implementation unit should settle the fix shape against
that thread before treating this as a small, narrow fix.
No branch, no push, no PR, no Jira or GitHub post from this
premise run.
