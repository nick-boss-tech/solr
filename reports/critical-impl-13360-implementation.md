# SOLR-13360 implementation, batch 2 (fixed queue item 1)

Branch: solr-13360-submit at 0d55e64c9b77475f005d330106c32689d5aa4a82
(pushed to nick-boss-tech/solr), on base apache main 83d560030fde.
No PR opened. No Jira or GitHub post.

## Fix

`SpellCheckCollator.getCollation` replaced query spans in the order
corrections arrived, assuming strictly increasing, non-overlapping
offsets. Synonym expansion breaks that assumption: one possibility
can carry several corrections for overlapping spans, or corrections
listed out of offset order. The running offset then points outside
the already-shortened string and the replacement throws
`StringIndexOutOfBoundsException`.

The fix sorts a copy of the corrections by start offset and skips
any correction overlapping one already applied, so replacements run
left to right in the original query. Corrections already in
increasing, non-overlapping order are unaffected. One production
file changed:
`solr/core/src/java/org/apache/solr/spelling/SpellCheckCollator.java`.

## Regression test

New `SolrTestCase` subclass (never `SolrTestCaseJ4`):
`solr/core/src/test/org/apache/solr/spelling/SpellCheckCollatorOverlapTest.java`,
driving the public `collate()` entry point with query verification
off. Four cases: in-order control, out-of-order corrections,
overlapping spans (first wins), repeated-position spans (first wins).

## Proof pair

- Without the fix: 4 tests, 3 failures, each a
  `StringIndexOutOfBoundsException` through
  `SpellCheckCollator.getCollation`. The repeated-position case
  reproduces the premise's exact `Range [-2, 4) out of bounds for
  length 4`. Seed 4F61CBE5949B3643. Log:
  `~/workspace/tools/13360-prefixed-test.log`.
- With the fix: the new test (4), `SpellCheckCollatorTest` (12),
  and `SpellPossibilityIteratorTest` (3) all pass, 19 tests, 0
  failures. Log: `~/workspace/tools/13360-fixed-tests.log`.

## Full hardening gate

`harden-branch.sh` on the committed head, all steps pass:
step 0 no changelog entry (code change only; no fragment added),
step 1 tidy clean, step 2 Error Prone compile ok, step 3 pre-fix
proof PASS (focused tests fail on unpatched production code),
step 4 focused tests 19/19 green from fresh JUnit XML, step 5
`:solr:core:check -x test` ok. Verdict: HARDENED. Gate log:
`~/workspace/tools/13360-gate.log`.

## Scope note

The fix keeps the first correction in offset order when spans
overlap and drops the rest. It does not merge overlapping intervals
into a combined correction (the closed PR #3112 approach); that
shape was never reviewed to a merge, and first-wins is the narrow
fix for the reported crash.

## Next

Ranking queue resumes at unit 6 (SOLR-7926 premise) after this
implementation unit, per the batch-2 queue rules.
