# Final round material: update processing and atomic updates

Heads and proof sources for the final-round assignment
(`assignments/update-processing-final-round.md`). Verify every head
against the live fork tip before drafting; hold any branch whose tip
moved and report it. Proof sections state only what the named source
supports. Where a source gives no counts, the draft says what was run
and does not invent counts.

## Tonight's gate receipts (verified on the main side, 2026-10-08)

- SOLR-6973, head `fa5b59ba07b47fca7bd513ef619bec4632595b98`.
  Gate green. Focused `SignatureUpdateProcessorFactoryTest` 7 of 7.
  Pre-fix proof: the same class on base production ran 7 tests with
  1 failure. Changelog parse, tidy, Error Prone compile and
  `:solr:core:check -x test` all clean.
- SOLR-5939, head `f8d4bdbea518901e255ae119f3e5c43e7804a9bf`, and
  SOLR-5754, head `7fbe0128d8b070db71a3db1dba2232f8cff0c404`.
  Gated together on their merged tree `1ddbf36202d`. Steps 0 to 4
  all rc=0, focused tests 26 of 26 on the merged tree, worktree
  clean. The 26 cover both branches' classes, including
  `TestTolerantUpdateProcessorCloud` and
  `TestTolerantUpdateProcessorRandomCloud`. Each draft names its own
  head and cites this merged-tree receipt as the proof.
- SOLR-5941, head `bf17e860d9ea4462473dc7a09f134524b496c2f7`.
  Gate green. Focused: `AutoCommitUpdateChainTest` 2 of 2,
  `CommitThroughNonLeaderTest` 1 of 1, `ParallelCommitExecutionTest`
  1 of 1, `HttpPartitionOnCommitTest` 1 of 1; check clean. Its
  pre-fix proof ran the new cloud test against the pre-fix submit
  head `62516cc338e`, not against upstream base, because the defect
  was introduced by this branch's own guard; the draft's Proof says
  so plainly.
- SOLR-12864, head `b9c6c1e71ffa9a7f68d7ac126206cd2f14af4aaa`.
  Gate green. Focused `JsonLoaderTest` 32 of 32. Test-only branch:
  the production tree is identical to base, and the pin
  `testEchoDocsWithMapUniqueKeyOnly` passing on base is the expected
  coverage outcome. No changelog fragment exists and none is added.

## Earlier receipts named in the audits and the ledger

- SOLR-4841, head `f8850ffd421439378cb55049d0ecaeaed347a476`;
  SOLR-5505, head `44c444aa5cd38618424c504fff307ead5cf466cf`;
  SOLR-5887, head `c4c57ef7bcbde765807bf02f9e3a6db605868b7e`.
  Their audits (`audits/update-processing/`) verified the gated
  claims at exactly these heads and certified all three Ready for
  final review. Proof cites the audit's evidence section.
- SOLR-3657 and SOLR-13265: heads as stated in their audits.
  SOLR-13265 was confirmed at `c134b34aa27`; its audit notes only
  the final gate's fail-before reporting remains, so its Proof
  states what the gate record supports and nothing more.
- SOLR-7022, head `6a233ab2fdb`; SOLR-11483, head `4431a250f66`;
  SOLR-14262, head `1e8d2b0075d`; SOLR-16655, head as stated in its
  audit (gated green at that head). Proof cites each audit's
  evidence section.
- SOLR-16673, head `d5c19e64ba1b`. Receipt: tidy and Error Prone
  compile clean, pre-fix proof PASS, `ParsingFieldUpdateProcessorsTest`
  43 of 43, `:solr:core:check -x test` clean.
- SOLR-16356, head `39c0585072f`. Receipt: tidy and Error Prone
  compile clean, pre-fix proof PASS, `UpdateLogClosedCoreTest` 1 of
  1, `UpdateLogTest` 5 of 5, `:solr:core:check -x test` clean.
- SOLR-16910, head `9fce3e9a705`. Gated green (third gate run).
  Proof cites its audit's evidence section.
- SOLR-18505, live head `e28739b4069`. Its pull request was gated
  at `8ca33200e37`; the live head adds one comment-only commit to
  `DirectUpdateHandlerTest.java`. The adopted decision records no
  re-gate for that delta. This branch gets no new draft; see the
  assignment's closing item.

## Per-branch draft notes (from the audits and TESTING.md decisions)

- SOLR-5939: the ADOPTED items go in the draft's Limits (the
  client-visible error-text change; every request in a non-retriable
  failed stream marked failed). Per-request attribution design goes
  in Choices.
- SOLR-5754: hardening framing only. The branch does not claim a
  fixed bug; the lost-error consequence is stated as hypothetical
  (the audit's reachability reading found the window unreachable),
  and fail-before is stated as inconclusive by construction.
- SOLR-5941: the adopted design choices (which chain, the buffering
  skip, the extra version stamp, the replica update lock) are stated
  in the draft's main text, not buried in Limits.
- SOLR-12864: framed as test coverage for the SOLR-16811 fix,
  never as a bug fix.
- SOLR-7022: present the cause correctly in the draft (the audit's
  changelog wording note) and state the return-normal commit
  behavior. If the branch's own changelog fragment carries the
  wrong cause wording, flag it in the final-round report instead of
  editing the branch.
- SOLR-11483: retention growth is named in Limits.
- SOLR-14262: document the `commitIgnored` header; pose the
  contract in Choices; make no claims about replica behavior (cloud
  propagation was not traced).
- SOLR-16655: the upgrade note goes in the draft; the overlap with
  SOLR-12705 (both change `FieldMutatingUpdateProcessor`) is named.
  The design stands as implemented (ADOPTED).
- SOLR-16673: the audit's two LOW items go in Limits.
- SOLR-16356: Limits name the `DocExpirationUpdateProcessorFactory`
  commit trace as a follow-up this branch does not cover (DECIDED
  2026-10-08).
- SOLR-16910: Limits name the `SolrCore.Request` logging item as
  not addressed (DECIDED 2026-10-08).
