# SOLR-13360 premise verdict, batch 2 (unit 5)

Verdict: REPRODUCED.

Ticket: `/suggest` with `spellcheck.collate=true` throws
`StringIndexOutOfBoundsException` from
`SpellCheckCollator.getCollation` (2019 report, confirmed reproducers
in 2020, mechanism analysis by Rainer Jung 2021, closed PR #3112 with
reproducing tests in 2025, still Open, last Jira update 2026-06-24
asking whether a fix is still coming).

## What ran

Fresh worktree at apache main 83d560030f (origin/main, fetched
2026-11-01). Scratch probe test only, never committed:
`solr/core/src/test/org/apache/solr/spelling/Premise13360ProbeTest.java`
(a `SolrTestCase` subclass; no core boot needed, query verification
off). It drives the public `SpellCheckCollator.collate()` entry point
with crafted `SpellingResult`s whose correction shapes match the
ticket record, and asserts each throwing case fails with a
`StringIndexOutOfBoundsException` whose stack passes through
`SpellCheckCollator.getCollation` (the exact frame in the ticket
stack).

Run: `:solr:core:test --tests
org.apache.solr.spelling.Premise13360ProbeTest`, seed
33623F1D8106579, BUILD SUCCESSFUL, 4 tests, 0 failures. Fresh JUnit
XML timestamp 2026-11-01T19:50:45Z. Run log:
`~/workspace/tools/13360-premise-probe.log`. Probe worktree removed
after the run.

## Results

- Control, strictly increasing non-overlapping offsets
  ("cats persians tabby" -> "cat persian tabbi"): PASSED.
- Synonym-overlap shape from PR #3112 (synonym
  "panthera pardus, leopard|0.6" expanding the (0,15) span into
  leopard / 0 / 6 corrections sharing one interval, plus pardu
  (9,15) inside it): SIOOBE, `Range [-8, 7) out of bounds for
  length 12`. The second replacement computes startIndex 0 - 8 = -8
  after the first replacement shortened the string by 8.
- Repeated-position shape from Rainer Jung's analysis (two distinct
  synonym-expanded tokens sharing the original token's (0,6)
  interval): SIOOBE, `Range [-2, 4) out of bounds for length 4`.
- Out-of-order possibility shape (the (5,13) correction applied
  before the (0,4) correction): SIOOBE, `Range [-1, 3) out of
  bounds for length 18`. With `suggestionsMayOverlap` off (the
  collator default), `PossibilityIterator` neither sorts nor
  separates the corrections before `getCollation` runs.

## Scope and limits of this verdict

This reproduces the ticket's failure at the collation method through
its public entry point, with the exact correction shapes the ticket
documents. It does not replay a full HTTP `/suggest` request through
a synonym analyzer: PR #3112's end-to-end test needs a spellchecker
config absent from current main, and its collation-only tests used
the pre-refactor Lucene `Token` API (current main's
`SpellCheckToken` is a record), so the probe was rewritten against
the current API instead. The implementation unit should still land a
regression test that pins the fixed behavior at this same level, and
should weigh whether an end-to-end synonym test is cheap enough to
add alongside it.

## Next step

Premise reproduced, so per the batch-2 queue rules the next unit is
the SOLR-13360 implementation: branch `solr-13360-submit` from
current main, narrow fix, proof pair (test fails without the fix,
passes with it), full hardening gate locally, push. No PR opening.
Fix surface to consider: `SpellCheckCollator.getCollation`'s
assumption that corrections arrive in strictly increasing,
non-overlapping offset order (the PR #3112 approach merged
overlapping intervals before replacement; that PR is closed and its
approach was never reviewed to a merge, so the implementation unit
decides the shape on current code). After the implementation unit,
the ranking queue resumes at unit 6 (SOLR-7926 premise).
