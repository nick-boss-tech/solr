# Material: round 3 close-out answers, and the SOLR-13943 addendum

Sources: the main side's receipts ledger, the takeover log,
and the gate logs named per item. This file answers the six
owner decisions in reports/update-processing-round-3-closeout.md
and supplies the SOLR-13943 material that item 10 of the
round 3 close-out material waited for. Decisions recorded
here were adopted under the owner's standing practice
(2026-10-08).

## 1. SOLR-16655 receipt: confirmed at the named head

Receipts ledger, 2026-10-09 08:03 MDT, log
g16655-gating-gate.log: gate GREEN at
5e2317443f4116a9e97330b0d6ce175cf44b54df. Pre-fix proof
PASS (the new test fails on the pre-fix head b201a57fb3e5).
Focused tests from fresh JUnit XML:
FieldMutatingUpdateProcessorTest 36 of 36,
ParsingFieldUpdateProcessorsTest 44 of 44. :solr:core:check
-x test rc=0. The draft's Proof may cite the head, the date,
and these counts.

## 2. SOLR-13696 pre-fix heads, and the create-alias fail-before

- Time-route cast fix: pre-fix production head 98ad9d3fcc33
  (TimeRoutedAlias.java before the parse fix). Gate r5
  step 3, seed FB1F0CEBAAF65F30: Dimensional fails 2 of 2
  with the String to Date ClassCastException.
- Future-date repair: pre-fix test file at a4e0da422327.
  Gate r6 step 3 part b, seed 1676C9C3B647F0E6: Dimensional
  fails 2 of 2 with the server-response timeout shape.
- [shard] style repair: pre-fix test file at 08f9384e47c0.
  Gate r7 step 3 part c, seed 9DBC31B7C732B317: Dimensional
  fails 2 of 2 with the final-loop assertion shape.
- Create-alias fix: a fail-before run IS on record. The r3
  investigation (takeover log, 2026-10-09) ran
  DimensionalRoutedAliasUpdateProcessorTest on base
  c3cdf7b46e8 with the awaitsfix group enabled, seed
  54689CC480DC14B0: both tests fail at CREATEALIAS with "A
  routed alias requires these params: [router.name,
  router.field]", the same failure as at the branch head
  before the fix. The draft's Limits line saying no
  fail-before run is recorded for the create-alias fix is
  wrong; replace it with this citation.

## 3. SOLR-13696 length: accepted

About 4.9 KB of prose is accepted for a multi-part change,
as with SOLR-5939 and SOLR-5941 in round 3. No trim.

## 4. SOLR-16673 title line: add it

Add the narrowed changelog title as the draft's title
line, verbatim from changelog/unreleased/SOLR-16673.yml at
d7170b12f312.

## 5. SOLR-13696 changelog: a fragment is being added

The main side is adding a changelog fragment covering the
two production fixes (a test-only branch needs none; this
branch no longer is one) and gating the result as gate r8.
The draft prepared at 1d0b8a0a73cd stands in substance; a
short further addendum will name the new head once gate r8
records GREEN, and the draft then re-points its citations
to that head and replaces its "adds no fragment" line with
the fragment's changelog line. If gate r8 does not land,
this item is withdrawn and the draft stays at 1d0b8a0a73cd
as written.

## 6. SOLR-13943: draft material (gate GREEN on the stack)

Stacked head: cc155cf68e1d8e79f1bcecd2e4c25ada864d8f53 on
solr-13943-submit, stacked on the SOLR-13696 head
1d0b8a0a73cd. Gate GREEN (log g13943-stack-gate.log,
receipt in the main side's ledger): changelog step passes
(the net tree carries no fragment), tidy rc=0 with a clean
tree, Error Prone compile rc=0, :solr:core:check -x test
rc=0. Focused counts from fresh JUnit XML:

- TimeRoutedAliasDateMathInStartTest, NORMAL mode, seed
  8879E35521A4B9EA: 1 of 1, executed and passed. The proof
  runs before the gate also passed it 3 of 3 in normal mode
  at three seeds.
- Stack sanity, normal mode: CategoryRoutedAliasUpdateProcessorTest
  6 of 6, DimensionalRoutedAliasUpdateProcessorTest 2 of 2,
  CreateAliasAPITest 13 of 13.
- TimeRoutedAliasUpdateProcessorTest with the awaitsfix
  group enabled: 6 tests run, 5 pass, 1 failure:
  testPreemptiveCreation. This failure is pre-existing and
  is not caused by this change: the round 38 spot-check on
  the unstacked branch failed the same test in 3 of 5 runs
  (expected:<3> but was:<4> in concurrentUpdates), and the
  method's own comment says it relies on winning a timing
  race against asynchronous preemptive collection creation.

Draft instructions: new draft at cc155cf68e1. What: the
branch's test-body fix (testDateMathInStart polls cluster
state instead of racing its own ZooKeeper watcher) plus the
move of that test into its own class with no @AwaitsFix, so
it executes in normal CI. Proof: the normal-mode counts
above, AND the awaitsfix result stated plainly in the
Proof or Limits, naming testPreemptiveCreation and its
pre-existing status; no silent green. Limits: the original
class stays @AwaitsFix under SOLR-13059 and its other tests
stay skipped in normal mode; this PR does not fix
SOLR-13059. Stacking: this branch's base is the SOLR-13696
head, so the PR opens after, or alongside, the SOLR-13696
PR and says so; if 13696 lands first, the branch rebases
onto main with no content change expected.
