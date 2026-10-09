# Final round 3 report: update processing

Round: `assignments/update-processing-final-round-3.md` (status READY, commit `e278aa7ece0`). Claim: `claims/update-processing-final-round-3.md` (commit `b0d7da6b709`). Material: `material/update-processing-final-round-3.md`, with `material/update-processing-receipts-addendum.md`. Draft format: `pr-formula.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two subagents drafted the items. The lead agent checked their drafts against the branches, corrected what did not hold, and made the commits.

## Summary

- 16 items in this round: 15 drafted or updated, 1 held.
  - New drafts (4): SOLR-16673 (released from the scratchpad hold), SOLR-12705, SOLR-11475, SOLR-12245.
  - Updated drafts (8): SOLR-5754, SOLR-5939, SOLR-5941, SOLR-6065, SOLR-5065, SOLR-7504, SOLR-6045, SOLR-16655.
  - Unchanged, verified as final (3): SOLR-4841, SOLR-14718, SOLR-13265.
  - Held (1): SOLR-7022, an update. Its Proof source is not recorded (see Held).
- All in-scope live fork tips matched the named heads when the claim was made.
- Not drafted: SOLR-13696 (gate r6 failed at `08f9384e47c`; must not be drafted this round), SOLR-13943 (joins a later batch). SOLR-18505 is closed in round 1 and was not touched.
- No Gradle, builds, or tests were run. No submit branch was edited. Nothing was posted to GitHub or Jira. No PR was opened or changed.

## Per-branch rows

Proof sources are the files or records each draft names. Where a source gives no counts, the draft gives none.

| Ticket | Head verified | Proof source | Result | Notes |
|---|---|---|---|---|
| SOLR-16673 | `d7170b12f312`, yes | Gate receipt at parent head `d5c19e64ba1b` (addendum). Delta is the changelog title line only (`git diff d5c19e64ba1b d7170b12f312`, one line in `changelog/unreleased/SOLR-16673.yml`) | Released (new draft) | The Schema Designer clause is gone from the title. Limits keeps one line saying the Schema Designer path is not tested. See owner decision 5. |
| SOLR-12705 | `8624b7c3238b`, yes | Settling note appended to `audits/update-processing/SOLR-12705.md` at `9328656fd81`; gate GREEN at `8624b7c3238b` (material) | New draft | Limits: audit findings 1 and 4; `processAdd` removes the field when the mutator returns null (`FieldMutatingUpdateProcessor.java` lines 118-120 and 176-177, read at the head); conflict with SOLR-16655 in the same file. Multi-part, so over the length guide. |
| SOLR-7022 | `db357868610b`, yes | None recorded for the earlier head `6a233ab2fdb`. The only run is GitHub Actions `37558668356`, which covers `7940e98b0ee0`, a child of `6a233ab2fdb` | **Held** | The Javadoc reword at `db357868610b` is verified (old phrase gone, new sentence present). The draft's Proof still says "the focused gate at the earlier head `6a233ab2fdb` is the Proof source." The addendum has no 7022 entry. See Held and owner decision 1. |
| SOLR-5754 | `46b919e2d4e8`, yes | Combined gate: SOLR-5939 at `f8d4bdbea518` with SOLR-5754 at `7fbe0128d8b0`, merged tree `f2e33340f0ea`, receipt 2026-10-08 | Updated | Proof names the gated pair. The head differs from `7fbe0128d8b0` only by the deleted root note. "byte for byte" is not in the draft. |
| SOLR-5939 | `f8d4bdbea518`, yes | Combined gate, 26 of 26 (receipt 2026-10-08) | Updated | All 26 tests named by class: StreamingSolrClientsErrorAttributionTest 2, SolrCmdDistributorTest 1, TestTolerantUpdateProcessorCloud 19, TestTolerantUpdateProcessorRandomCloud 2, StreamingSolrClientsTest 2. StreamingSolrClientsTest is absent at `f8d4bdbea518` and present at `46b919e2d4e8`, so the draft says it comes from SOLR-5754. Over the guide; the material allows it. |
| SOLR-5941 | `a4df7bfd214b`, yes | Doc-fix gate finished 2026-10-09: AutoCommitUpdateChainTest 2 of 2, CommitThroughNonLeaderTest 1 of 1, ParallelCommitExecutionTest 1 of 1, HttpPartitionOnCommitTest 1 of 1 | Updated | Gate date added. The five classes not re-run at this head are named: DirectUpdateHandlerTest, MaxSizeAutoCommitTest, TestUpdate, SolrCmdDistributorTest, DistributedUpdateProcessorTest. The gap is accepted, with no re-run. See owner decision 6. |
| SOLR-4841 | `e4c878627108`, yes | Audit and the constructor at `e4c878627108` (public, line 24) | Unchanged | The Choice (public or package-private constructor) is kept, as the material decides. |
| SOLR-6065 | `3d2cec9e1ab3`, yes | Gate 2026-10-09; the SERVER_ERROR mapping (lines 421-437) and `DirectUpdateHandlerTest` asserting it (line 129), both read at the head | Updated | The Choice is rewritten: 500 (SERVER_ERROR) implemented because a Lucene capacity limit is a server condition; 400 posed for maintainers to decide. Internal decision records are not cited in the outbound text. |
| SOLR-5065 | `ab894a996c8a`, yes | Pre-fix PASS on base `0cc328310f8f`; `DefaultSchemaSuggesterTest` 2 of 2 (material, 2026-10-09) | Updated | The Proof now says the suggester pin test fails on base and passes with the change. The test is `testGuessFieldTypeExponentFormsInferDouble`, which exists at the head. Size grew to about 3.9 KB with URLs. |
| SOLR-7504 | `22b77196e662`, yes | Material gate: pre-fix PASS, FieldMutatingUpdateProcessorTest 29 of 29, ParsingFieldUpdateProcessorsTest 42 of 42 | Updated | Trimmed from about 4.2 KB to about 3.8 KB with URLs. The behavior statements stay as statements, no Choice. The SOLR-12705 reference is plain text, so every link stays at `22b77196e662`. Merges cleanly with `8624b7c3238b`. |
| SOLR-6045 | `e4b77fa7ae53`, yes | Gate at `e4b77fa7ae53`; pre-fix commit `e06aa7624853` confirmed (parent of guard commit `e4b77fa7ae5`) | Updated | The JSON array behavior change is now stated in What, as the formula requires. Limits keeps only the coverage gap. Size is about 6.4 KB with URLs (prose about 3.4 KB). See owner decision 4. |
| SOLR-14718 | `29c09959791a`, yes | Green gate 2026-10-07 (audit: main-side record not in the workspace). Fail-before counts from the audit's settling section dated 2026-10-09: three runs, three seeds, control run | Unchanged | Green wording has no counts, as decided. The fail-before counts are in the audit's settling section, not in the draft's green claim. |
| SOLR-13265 | `c134b34aa27f`, yes | Audit fail-before verdict section (2026-10-09, verification lane): 3 tests, 0 failures at head | Unchanged | Final as written. Links at `c134b34aa27f`. No internal status comment. |
| SOLR-11475 | `0de48e492fd`, yes | Gate GREEN (material); pre-fix inconclusive, since the old code does not terminate (test comment at lines 454-457, read at head) | New draft | The Choice poses requesting the other side's version; the implemented position steps over the sign-mismatched version. The material's "PeerSyncTest 5 of 5" is not in the draft: the class has one test method, `test()`, and its base class has none. See owner decision 7. |
| SOLR-16655 | `5e2317443f`, yes | Gate GREEN 2026-10-09 (material). Local counts (36 of 36, 44 of 44) are not used as Proof | Updated | The round 1 draft is rewritten for the gated descent. The new test pins both shapes (unselected parent: child untouched; selected parent: child mutated). The Choice poses the wider descent. The old "unselected child untested" Limit is dropped. Conflicts with SOLR-12705 in `FieldMutatingUpdateProcessor.java`; no order is stated. |
| SOLR-12245 | `f325d5d057`, yes | Gate GREEN 2026-10-09: pre-fix PASS, DistributedUpdateProcessorTest 6 of 6 (material) | New draft | Both new tests are named (`testDistribErrorMessageNamesTheTargetReplica`, `testDistribErrorMessageNamesTheHostOnce`). The draft says the host check fails before the fix but does not say which test fails; the material does not record it. See owner decision 3. |

## Held

- **SOLR-7022 (`db357868610b`).** The Javadoc reword is verified. The Proof is not. Its headline says the focused gate at `6a233ab2fdb` is the Proof source. No receipt for `6a233ab2fdb` exists in the addendum or the audit. The only recorded run is GitHub Actions `37558668356`, on `7940e98b0ee0`, a child of `6a233ab2fdb` that adds one workflow file. The round 1 draft and the round 2 report both say no local receipt exists there. The draft must not be released with the current headline until the owner names a receipt or approves the CI-run wording.

## Owner decisions

1. **SOLR-7022.** Give the receipt for `6a233ab2fdb`, or approve rewriting the Proof to cite the CI run only. Until then the draft stays held.
2. **SOLR-12705 and SOLR-16655.** The two change `FieldMutatingUpdateProcessor.java` and conflict at their heads. The material asks for sequencing, not an order. Pick which lands first. Both drafts say they conflict and state no order.
3. **SOLR-12245.** Name the new test that fails before the fix, if the gate record names it. The draft currently names no failing test.
4. **SOLR-6045.** About 6.4 KB with URLs. Prose is about 3.4 KB. Accept it as over the guide, or trim further.
5. **SOLR-16673.** Limits keeps the Schema Designer line as a disclosure that the path is not tested. Keep it, or drop it.
6. **SOLR-5941.** The five un-rerun classes are derived from the round 30 focused list at `62516cc338e` (six classes, minus AutoCommitUpdateChainTest, which re-ran at this head). Confirm the list.
7. **SOLR-11475.** The material's "PeerSyncTest 5 of 5" is not in the draft. If a count source exists, give it and it can be added with its head.
8. **Receipt dates.** Dates are included where the material gives them (2026-10-08 for the combined SOLR-5754 and SOLR-5939 gate; 2026-10-09 for the other gates named).

## Checks (lead)

- Claim `b0d7da6b709`. Live fork tips matched the named heads for all items (`git ls-remote`).
- Every blob link in each draft uses that draft's named head (checked per file).
- No em dash characters in any draft (`LC_ALL=C grep`).
- `git merge-tree`: SOLR-12705 with SOLR-16655 conflicts in `FieldMutatingUpdateProcessor.java`. SOLR-12705 with SOLR-7504 merges cleanly.
- SOLR-5939: `StreamingSolrClientsTest` is absent at `f8d4bdbea518` and present at `46b919e2d4e8`.
- SOLR-5065: the named pin test exists at `ab894a996c8a`.
- SOLR-11475: `PeerSyncTest` has one `@Test` method; `BaseDistributedSearchTestCase` has none.
- SOLR-7022: no 7022 entry in the receipts addendum; CI run `37558668356` covers `7940e98b0ee0` only.
- SOLR-16655: the new test pins the unselected-parent case.

## Subagent reports corrected

- Both subagent reports said the claim file was absent. It is committed at `b0d7da6b709`.
- The SOLR-5941 draft named three of the five un-rerun classes. Corrected to name all five.
- The SOLR-11475 draft had a "5 of 5" count that the single test method cannot produce. Removed.
- The SOLR-6045 draft had the JSON array behavior change only in Limits. Moved the behavior to What.
- The SOLR-14718 counts that one subagent flagged as unsupported are in the audit's settling section. Kept.
