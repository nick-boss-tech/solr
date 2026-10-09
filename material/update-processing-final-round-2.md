# Final round material, batch 2: update processing

Heads and proof sources for `assignments/update-processing-final-round-2.md`.
Every receipt below was verified on the main side from the gate log
and fresh JUnit XML. Verify every head against the live fork tip
before drafting; hold any branch whose tip moved and report it.

## New drafts

- SOLR-4841, head `e4c878627108ee2347cb1d5ee915e3e953c4249f`.
  The code is unchanged from the audited head `f8850ffd4214`; the
  only delta is the changelog fragment type, now `added` per the
  adopted final-round outcome. Proof cites the audit's evidence
  section.
- SOLR-5754, head `46b919e2d4e8bc287ac913edb789c21509e1b215`.
  Delta from the gated head `7fbe0128d8b0` is the removal of the
  root `SOLR-5754-TESTING.md` only. Proof: the combined receipt
  with SOLR-5939 (merged tree `1ddbf36202d`, 26 of 26), which the
  addendum in `material/update-processing-receipts-addendum.md`
  confirms byte-identical to a fresh merge of the gated heads.
  Hardening framing only, per the round 1 notes.
- SOLR-5939, head `f8d4bdbea518901e255ae119f3e5c43e7804a9bf`.
  Same combined receipt and addendum confirmation. The ADOPTED
  items go in Limits; per-request attribution in Choices.
- SOLR-5941, head `a4df7bfd214b15d9c8d4519fdac8fd6df9468f2d`.
  Gate green at this head: changelog, tidy and Error Prone compile
  clean, `AutoCommitUpdateChainTest` 2 of 2,
  `CommitThroughNonLeaderTest` 1 of 1,
  `ParallelCommitExecutionTest` 1 of 1,
  `HttpPartitionOnCommitTest` 1 of 1, check clean. The delta from
  the earlier gated head is the corrected ref-guide sentence. The
  draft discloses the wider `commit_end_point` effect (any request
  carrying the flag is acted on, not only autocommits), per the
  adopted decision: disclose, do not narrow. The pre-fix proof
  history (run against the pre-fix submit head, not base) is
  stated as in round 1's notes.
- SOLR-16673, head `d5c19e64ba1b`. Receipt in
  `material/update-processing-receipts-addendum.md` (tidy and
  Error Prone clean, pre-fix proof PASS,
  `ParsingFieldUpdateProcessorsTest` 43 of 43, check clean). The
  audit's two LOW items go in Limits.
- SOLR-5065, head `ab894a996c8aa9825c4ac5af38536aec1ee865eb`.
  Gate green: pre-fix proof PASS, `DefaultSchemaSuggesterTest`
  2 of 2, `ParsingFieldUpdateProcessorsTest` 44 of 44, check
  clean. Decision (TESTING.md): ship narrow on locale; the
  suggester inference pin test was added by the decision lane and
  is part of this head. Limits state that locale-aware parsing
  stays a ticket decision.
- SOLR-6065, head `3d2cec9e1ab3a19f6b948682b116c2780da4b65b`.
  Gate green: premise PASS (the new cloud test fails on base
  production with the old 400 shape), `MaxDocsLimitCloudTest`
  1 of 1, `DirectUpdateHandlerTest` 8 of 8, check clean. The
  draft's Choices pose the SERVER_ERROR call, per the recorded
  decision.
- SOLR-7504, head `22b77196e662e93aca6ddc926c6738fb69c817fb`.
  Gate green: pre-fix proof PASS (the plain-first test fails on
  the pre-fix factory), `FieldMutatingUpdateProcessorTest`
  29 of 29, `ParsingFieldUpdateProcessorsTest` 42 of 42, check
  clean. Limits carry the decided narrow scope: the counter's
  chain placement stays, the changelog title no longer claims a
  full fix, and the count-requires-set rejection is stated.
- SOLR-12703, head `63c84919c80b15c20d5b93bbacaf3f21835648e5`.
  Gate green: pre-fix proof PASS (the updated test fails on the
  old head), `AtomicUpdatesTest` 27 tests, 0 failures, 1 skipped,
  check clean. The head includes the error-message change that
  closed audit finding 1: the rejection names the nested operation
  instead of echoing its value. The draft notes the sequencing
  with SOLR-6045 (both change `mergeDocHavingSameId`).
- SOLR-12705, head `8624b7c3238b5ca35b448e0d6d62fb7789dad0cd`.
  Gate green: pre-fix proof PASS, `FieldMutatingUpdateProcessorTest`
  25 of 25, `ParsingFieldUpdateProcessorsTest` 43 of 43,
  `NestedAtomicUpdateTest` 15 of 15, `AtomicUpdatesTest` 26 tests
  with 1 skipped, check clean. Option (a) as decided: counting
  processors are excluded from the atomic-operand path. Proof
  also cites the settling run: on the pre-fix head the operand
  itself was counted, and on a single-valued field the appended
  count failed the update outright.
- SOLR-6045, head `e4b77fa7ae53d363e8869e1073a800c324fd6972`.
  Two gates cover this head's content: the factory fix gated
  green at `e06aa7624853`, and the child-document guard gated
  green at this head (pre-fix proof PASS against the pre-fix head,
  `AtomicUpdatesTest` 29 tests with 1 skipped,
  `AtomicUpdateProcessorFactoryTest` 6 of 6, check clean). The
  draft presents both parts: the factory defect fix and the guard
  that keeps child documents out of operation handling (the
  branch's own change had turned base's plain set of a child
  document list into a confusing 400; the guard restores sane
  handling). Sequencing with SOLR-12703 is noted.
- SOLR-14718, head `29c09959791a`. The branch stands on its
  earlier GREEN gate at this head. Proof also cites the settling
  verdict appended to its audit: finding 1's timing mechanism was
  disproven by run (clone reverted: 3 of 3 deterministic failures;
  head control passes). Ship narrow per the decision: Limits
  state the reported document can be the wrong one for async
  failures after the first document, and retries are not per
  document.

## Draft updates (drafts exist from round 1)

- SOLR-13265, head `c134b34aa27f` (unchanged). Update the draft's
  Proof with the fail-before verdict recorded in its audit:
  DISCRIMINATES. Head run 3 of 3 pass; base with only the test
  applied fails in the assertion itself (the error datapoint is
  present and nonzero on base).
- SOLR-16356, head `39c0585072f` (unchanged). Update the draft's
  Proof to cite the head receipt in
  `material/update-processing-receipts-addendum.md` instead of the
  earlier receipt it currently names.
- SOLR-7022, head `5b822b7e8e98839a04d1070f0f9d36f8037df0a4`.
  Delta from the drafted head is the changelog fragment reword
  only. Re-verify the draft against this head and update its head
  line and changelog citation.

## Out of scope

- SOLR-16655: the wider-descent probe found a real behavior
  difference; the owner's gate-or-accept call is pending. Its
  draft stays as round 1 left it.
- SOLR-12245: the settling run returned the response-detail call
  to the owner; pending.
- SOLR-13696 and SOLR-13943: blocked on the CreateAlias defect
  route the owner is deciding; 13943 stacks on 13696.
- SOLR-11475: owner call A still open.
