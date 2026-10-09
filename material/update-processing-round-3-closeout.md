# Material: update processing, round 3 close-out

Sources: the main side's receipts ledger
(files/test-receipts-63-branches.md), the takeover log, and the
gate logs named per item. This addendum answers the eight
owner decisions in reports/update-processing-final-round-3.md
and supplies the SOLR-13696 draft material. Where a disposition
records a decision, it was adopted under the owner's standing
practice (2026-10-08) unless it says the owner decided it
directly.

## 1. SOLR-7022: the receipt exists; the draft releases

The gate receipt for head 6a233ab2fdb is in the takeover log
(Review1 round, 2026-10-06; log g7022r1-gate.log): changelog
YAML ok, tidy rc=0, Error Prone rc=0,
DirectUpdateHandler2AwaitSearcherTest 2 of 2,
DirectUpdateHandler2CommitWaitTest 1 of 1,
DirectUpdateHandlerTest 7 of 7, all from fresh JUnit XML,
:solr:core:check -x test rc=0. The premise for the
commit-level test is also on record (log g7022r1-premise.log):
on untouched base the test fails at exactly the
interrupt-restored assertion, with the ticket's ERROR line in
the base output. A fresh verification run at the same head
(round 33, log g7022r33-verify.log, seed 7022C0FFEE7022)
repeated the counts: 2 of 2, 1 of 1, 7 of 7. GitHub run
37558668356 (SUCCESS) corroborates the same head; it is a
corroboration, not the Proof source.

Deltas from 6a233ab2fdb to the current head db357868610b:
the changelog reword at 5b822b7e8e9 and the comment-only
Javadoc reword at db357868610b. Neither changes behavior.
Proof wording for the draft: cite the gate at 6a233ab2fdb
with its counts, and name the two wording deltas, the same
pattern as SOLR-16673's parent-head receipt.

## 2. SOLR-12705 and SOLR-16655: order

SOLR-16655 lands first; SOLR-12705 rebases onto it. Reason:
the 16655 change is confined to the child-document descent in
mutateDocument; the 12705 change is the broader atomic
operands rework in the same file, so the smaller change
landing first gives the smaller rebase. Both drafts state
this order where they now say the two conflict.

## 3. SOLR-12245: the failing test is named

testDistribErrorMessageNamesTheHostOnce. It was added by the
fix commit f325d5d0576e itself (verified in the commit diff),
and the gate's pre-fix proof (log g12245-describe-gate.log,
step 3) runs the head test class against the pre-fix
production head 4a93167458b and records that the new test
fails there. The draft's Proof may name it as the fail-before
test.

## 4. SOLR-6045: length accepted

Accepted as written. The prose is about 3.4 KB, inside the
guide; the 6.4 KB figure counts the blob citation URLs the
formula requires. No further trim.

## 5. SOLR-16673: the Limits line stays

Keep the line disclosing that the Schema Designer path is
not tested. It is the honest counterpart of the narrowed
changelog title.

## 6. SOLR-5941: the five-class list is confirmed

Confirmed as derived: the round 30 focused list at
62516cc338e, minus AutoCommitUpdateChainTest, which re-ran
at this head. The five are DirectUpdateHandlerTest,
MaxSizeAutoCommitTest, TestUpdate, SolrCmdDistributorTest,
DistributedUpdateProcessorTest.

## 7. SOLR-11475: the count source, corrected wording

Receipts ledger, 2026-10-08 22:33 MDT (log
g11475-fix-gate.log, head 0de48e492fd): the focused step ran
five PeerSync classes, 1 of 1 each: PeerSyncTest,
PeerSyncWithLeaderTest, PeerSyncWithBufferUpdatesTest,
PeerSyncWithIndexFingerprintCachingTest,
PeerSyncWithLeaderAndIndexFingerprintCachingTest. Five
tests total, 0 failures. The material's earlier "PeerSyncTest
5 of 5" compressed that wrongly; the draft's Proof should
say the five class names with 1 of 1 each, or "five PeerSync
tests pass" with the classes named in the citation. The
pre-fix proof stays stated as inconclusive.

## 8. Receipt dates

Noted; no action.

## 9. SOLR-13696 draft material (gate r7 GREEN)

Head: 1d0b8a0a73cd10f4968f57985af93b450c484aca on
solr-13696-submit. Gate r7 GREEN (log g13696-r7-gate.log):
changelog parse ok (no fragment), tidy rc=0 with a clean
tree, Error Prone compile rc=0, pre-fix proofs PASS for all
three repairs (the cast fix cited from gate r5; the
future-date repair cited from gate r6; the [shard] style
repair discriminated in r7 step 3 part c: the pre-fix test
file fails 2 of 2 with the final-loop assertion shape, seed
9DBC31B7C732B317), focused tests from fresh JUnit XML:
CategoryRoutedAliasUpdateProcessorTest 6 of 6,
DimensionalRoutedAliasUpdateProcessorTest 2 of 2,
CreateAliasAPITest 13 of 13, :solr:core:check -x test rc=0.

What the branch carries, for the draft's What sections:

- The repair itself: the base class
  RoutedAliasUpdateProcessorTest loses its
  @AwaitsFix(SOLR-13696), so the Category and Dimensional
  suites execute in normal mode again; addDocsAndCommit
  commits every collection of the alias explicitly instead
  of relying on commitWithin (the dropped commitWithin
  coverage is finding 2, in Limits); a per-method cluster
  shutdown in the base class fixes the leaked-cluster
  teardown failures; the category field is corrected to
  ship_name_s.
- Folded-in production fix 1 (CreateAlias): the V2 remote
  message for a dimensional alias now carries the top-level
  router.name (Dimensional[TIME,CATEGORY] form) and the
  comma-joined router.field, mirroring SolrJ. Pre-existing
  defect on base; without it the repaired Dimensional suite
  cannot create its alias. CreateAliasAPITest's expectation
  moved from 10 entries to 12.
- Folded-in production fix 2 (TimeRoutedAlias):
  formattedRouteValues parses the route value through
  parseRouteKey instead of casting to Date, so a document
  whose timestamp is still a String at update-processor time
  routes instead of throwing ClassCastException.
  Pre-existing defect on base, masked by fix 1's defect.
- Test repair: the Dimensional tests' hard-coded 2020-10-23
  "future" document is computed relative to now (the date
  became the past, and the router created daily collections
  toward it until the client timed out).
- Test repair: the final placement check requests the
  [shard] value in URL style explicitly; the default became
  the bare shard id at luceneMatchVersion 9.5, which contains
  neither the category nor the day the check asserts.

Choices: pose both folded-in production fixes as the scope
question (the implemented position keeps them in this
branch, per the owner's no-new-ticket rule; the alternative
is separate tickets and PRs, with the Dimensional suite
staying broken in the meantime). Limits: finding 2
(commitWithin coverage dropped) stays dropped, with the
follow-up offer. TimeRoutedAliasUpdateProcessorTest keeps
its own @AwaitsFix(SOLR-13059) and stays skipped; state that
plainly so nobody expects Time coverage from this PR.

## 10. SOLR-13943

The stacked move is being applied on the main side onto the
gated 13696 head above, with its normal-mode proof. Its
draft joins this close-out when the main side records the
stacked head and its gate receipt in a further addendum.
