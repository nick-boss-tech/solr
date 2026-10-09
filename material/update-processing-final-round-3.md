# Material: update processing, final round 3

Sources: the main side's receipts ledger and takeover log
(2026-10-09), TESTING.md on this branch, and the round 2
report. Where a disposition below records a decision, the
recommendation was adopted under the owner's standing practice
(2026-10-08) unless it says Nick decided it directly.

## Part A heads and deltas

- SOLR-16673: gate receipt at d5c19e64ba1b (changelog parse
  clean, tidy 0, Error Prone 0, pre-fix PASS,
  ParsingFieldUpdateProcessorsTest 43 of 43, check clean).
  New head d7170b12f312 changes one line, the changelog title:
  the clause "which also made the Schema Designer fail on
  sample documents with empty values" is removed. The fragment
  parses (check-changelog-yaml.sh, 1 file OK).
- SOLR-12705: settling note appended to the audit at pr-prepare
  9328656fd81 (decision, settling evidence on pre-fix head
  b053944b127, test pinning {add}, {remove}, and multi-value
  {set}, gate GREEN at 8624b7c3238b with counts).
- SOLR-7022: new head db357868610b changes one Javadoc sentence
  on DirectUpdateHandler2.awaitSearcher. Old: "An interrupt
  (for example the core closing during a reload while an
  autocommit waits) is not an error". New: "If the wait is
  interrupted, that is not an error". Comment-only; scoped tidy
  rc=0. The changelog reword at 5b822b7e8e98 was the prior
  delta. Neither delta changes behavior, so the gate receipt
  at the earlier head stands as the Proof source, with the
  deltas named.

## Part B dispositions

- SOLR-5754: the combined gate covered the pair 5939
  f8d4bdbea518 with 5754 7fbe0128d8b0 (merged tree
  f2e33340f0ea). The draft's Proof should name the gated pair
  instead of claiming byte-for-byte coverage of the current
  head. The root SOLR-5754-TESTING.md deletion is packaging
  (adopted outcome, round 1) and needs no gate of its own.
- SOLR-5939: the 26 tests in the combined run (receipt
  2026-10-08 23:33 MDT, seed 59395754C0FFEE11):
  StreamingSolrClientsErrorAttributionTest 2 of 2,
  SolrCmdDistributorTest 1 of 1,
  TestTolerantUpdateProcessorCloud 19 of 19,
  TestTolerantUpdateProcessorRandomCloud 2 of 2,
  StreamingSolrClientsTest 2 of 2.
- SOLR-5941: doc-fix gate finished 2026-10-09 (fresh JUnit
  XML 2026-10-09T07:52:30Z to 07:53:13Z; the four focused
  classes all pass). The five round-30 autocommit classes not
  re-run at this head: accept the disclosed gap, no re-run.
  The draft already discloses it in Limits.
- SOLR-4841: keep the Choice (public versus package-private
  constructor); the audit calls it a maintainers' judgment.
- SOLR-6065: the SERVER_ERROR decision exists. Nick locked it
  in the Review1 disposition on 2026-10-06: the error code
  moves from BAD_REQUEST to SERVER_ERROR because a Lucene
  capacity limit is a server condition, with the disagreement
  posed in the PR's Choices. The round 33 record confirms 500
  implemented over 400 and DirectUpdateHandlerTest asserting
  the code (goal files/reviews-2026-10-07-round33-reviews/
  6065.md on the main side). Pose 400 as the alternative to
  the implemented 500, not as an open question.
- SOLR-5065: the gate receipt (2026-10-09) records the pre-fix
  proof as PASS with the note "new test fails on unmodified
  base 0cc328310f8f", and DefaultSchemaSuggesterTest 2 of 2.
  The new test is the suggester pin test, so the fail-before
  run TESTING.md asked for covers it; the draft may claim it.
- SOLR-7504: "a null counts as 0" and duplicate values count
  are statements of the code's current behavior, not owner
  decisions. They stay as behavior statements, no Choice.
- SOLR-6045: the pre-fix commit is e06aa7624853 (confirmed;
  round 2 itself verified it is the guard commit's parent).
  The single-child-document behavior change is in scope: the
  guard lane's test covers the list and the single child by
  design. Mixed input and JSON arrays stay in Limits; no new
  tests.
- SOLR-14718: accept the green wording without counts. No
  counts exist in any workspace record.
- SOLR-13265: the updated draft is final as written. Opening
  the PR waits on the owner's per-PR approval, like every PR;
  that is not a draft defect.
- Receipt dates the round 2 drafts lacked: combined
  5939+5754 gate 2026-10-08; 6065 cloud gate, 12703 gate,
  7504 gate, 5065 gate, 5941 doc-fix gate, 12705 gate, all
  2026-10-09.

## Part C evidence notes

- SOLR-11475 gate GREEN at 0de48e492fd (PeerSyncTest 5 of 5,
  recorded 2026-10-09). Audit finding B (the wrong test
  comment) is already fixed at this head: the comment states
  the old code loops endlessly, adding the same range string
  until memory runs out. The pre-fix proof was inconclusive:
  the old code does not terminate, so no clean fail-before
  exists; the draft states this rather than claiming one.
- SOLR-16655: the fix commit on 5e2317443f41 gates the descent
  in FieldMutatingUpdateProcessor.mutateDocument on
  selector.shouldMutate for the parent field, with a test
  pinning both shapes (parent unselected: child untouched;
  parent selected: child mutated). The lane's local counts
  (FieldMutatingUpdateProcessorTest 36 of 36,
  ParsingFieldUpdateProcessorsTest 44 of 44) are not the Proof
  source; the gate receipt in the ledger is.
- SOLR-12245: the fix commit on f325d5d057 fixes the
  duplicated host in describe() on the streaming path; the
  gate receipt in the ledger is the Proof source.
- SOLR-13696: gate r4 at 98ad9d3fcc33 FAILED step 4 after the
  premise passed (DimensionalRoutedAliasUpdateProcessorTest
  0 of 2, String to Date ClassCastException, seed
  FB1F0CEBAAF65F30). An investigation is running on the main
  side. The item joins this round only when a later gate
  records GREEN; the material addendum for that gate will name
  the head and the counts.
