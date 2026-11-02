# SOLR-11310 premise, Critical batch 2, unit 12: REPRODUCED

Ticket: QueryElevation does not work with Solr LTR. Jira Open,
Critical, reporter Rahul Babulal, last updated 2020-07-30. The
ticket text: with elevateIds and an LTR rq rerank, the elevated
document is not always first, because the reranker ignores
elevations. The one comment (Razvan Tache, 2020-07-30) reproduces
it on Solr 8.3: the elevated document is sent to reranking and
then moved to its reranked position; elevating the worst document
puts it last in the rerank slice.

Probe (scratch only, never committed): fresh worktree at batch
base 83d560030fde, seed 11310C0FFEE11310. The probe ran the
test that the existing fork branch solr-11310-submit carries,
verbatim, against unfixed production code: doc 0 with model
score 2.0, doc 1 with model score 1.0 and elevated, a linear
LTR model over finalScoreFloat, and a ReRankCollector over the
top 2 docs with doc 1 in the boosted set. The production file
solr/core/src/java/org/apache/solr/search/ReRankCollector.java
was verified still unfixed in the worktree (the loop over the
reranked docs still breaks at the first non-boosted doc).

Result: BUILD FAILED, 1 test, 1 failure, from fresh JUnit XML
(timestamp 2026-11-02T00:50:27Z, copy
~/workspace/tools/11310-premise-TEST-TestLTRReRankingPipeline.xml;
log ~/workspace/tools/11310-premise-probe.log):
org.junit.ComparisonFailure, expected first doc "1" (the
elevated doc) but was "0", at
TestLTRReRankingPipeline.java:281. The elevated document did
not hold first place after the LTR rerank, exactly the ticket's
symptom, on current main.

Note for the implementation unit: fork branch solr-11310-submit
already exists at e38ddec5279c465f65a2fa2de22ee9c92e0f5e31 (Oct
2-4, base 14c7aac0d1), with a two-line production fix (remove
the break in ReRankCollector so boosted docs later in the
slice still get their boosted score) plus this test and a
changelog fragment. The next unit should gate that existing
branch at its exact head, as the SOLR-16725 unit did for its
pre-existing branch, rather than writing a fresh implementation;
whether its old base needs a rebase is that unit's pre-claim
check. No branch was created or pushed by this run, no PR, no
Jira/GitHub post. Probe worktree removed.
