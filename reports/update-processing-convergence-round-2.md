# Update processing convergence, round 2

Verdict: **NOT CONVERGED.** 12 of 28 drafts are converged and 16 are not. One blocking item remains (SOLR-7022, the held draft's gate citation). The round 1 blocking items are applied and verified on every draft they touched. The open should-fix items are small replacements for the main side to apply; a short round 3 can then re-check the drafts still open.

Scope: the 28 drafts in `pr-drafts/update-processing/`, read against the audits, receipts and material they cite, and against the round 1 register and the main side's round 1 response. SOLR-13696 was checked on its gate claim only (it is not drafted this round). Every fork tip matched its draft's named head. No draft was edited; every replacement below is for the main side to apply.

Settled decisions in `material/update-processing-*.md` and in the round 1 response were not reopened.

## Counts

| Slice | Drafts | Converged | Not converged | Blocking (new) | Should-fix (new) | Notes (new) |
|---|---|---|---|---|---|---|
| H1 | 10 (incl. SOLR-13696, gate claim) | 5 | 5 | 0 | 6 | 13 |
| H2 | 9 | 4 | 5 | 0 | 6 | 11 |
| H3 | 9 | 3 | 6 | 1 | 4 (plus 2 carried) | 13 |
| **Total** | **28** | **12** | **16** | **1** | **16 new, 2 carried** | |

Converged: SOLR-11475, 12245, 12703, 14262, 13696 (gate claim), 16356, 16910, 4841, 5505, 5754, 5887, 6045.

Not converged: SOLR-11483, 12705, 12864, 13265, 13943 (H1); 14718, 16655, 16673, 3657, 5065 (H2); 5939, 5941, 6065, 6973, 7022, 7504 (H3).

## Blocking

- **UPC-R2-501, SOLR-7022, Proof line 24 (blocking).** Owner decision 1 in `reports/update-processing-final-round-3.md` (Held) requires the receipt for `6a233ab2fdb`, or CI-only wording. Line 24 cites that receipt, and its evidence is not in the workspace. `g7022r1-gate.log` and `g7022r1-premise.log` are not in any searched path. `receipts/SOLR-7022.md` records the gate at `a2ed957063b` (2026-10-05) and says the 2026-10-09 top-up did not run `DirectUpdateHandler2CommitWaitTest`, which `6a233ab2fdb` adds. No workspace record gives that test 1 of 1. The line 26 claim that the test fails on base rests on the same missing log; the audit says it was checked by reading, not run. The log file name is also internal process vocabulary.
  - **Replace line 24 with** (unless the takeover-log entry is produced in the workspace and the owner accepts it): "- Gate at `a2ed957063b` (2026-10-05): `DirectUpdateHandler2AwaitSearcherTest` 2 of 2 and `DirectUpdateHandlerTest` 7 of 7; the changelog YAML parses, tidy and the Error Prone compile pass, and the module checks pass. The commit-level test, `DirectUpdateHandler2CommitWaitTest`, was added at `6a233ab2fdb`, after that head, and has no local run on record. GitHub run 37558668356 passed on `7940e98b0ee0`, a child of `6a233ab2fdb` that adds one workflow file."
  - **Replace the last sentence of line 26 with:** "The commit-level test, `DirectUpdateHandler2CommitWaitTest`, reads as failing on the base at the interrupt-restored assertion; this was checked by reading the base code, not by a run."
  - If the entry is produced and accepted: keep the counts, drop the log name, and keep line 26 with the same "checked by reading" qualifier.

## Should-fix (new)

### SOLR-11483 (H1)
- UPC-R2-301, Proof line 25: the 21-test count needs this branch's `TestRecovery.java` on the base. "On base production, `TestRecovery` runs 21 tests with exactly one failure" is the same premise problem UPC-R1-206 fixed for SOLR-5887. **Replace with:** "With the merge-base code at `97d973814336` and this branch's `TestRecovery.java`, `TestRecovery` runs 21 tests with exactly one failure: `expected:<1000> but was:<10>`. That is the 1000 check." The main side confirms the setup in `g11483-gate.log` first.

### SOLR-12705 (H1)
- UPC-R2-302, Limits bold line 34 and bullet line 38: the changelog names only the set, add, add-distinct and remove mutation. It omits the null removal (line 38) and the `Ignore` `{set: null}` output change (What, line 17). **Replace the bold line with:** "**A few cases are untested, some behavior is missing from the changelog, and two changes land first.**" **Replace the end of line 38 with:** "The changelog does not mention this, or the output change to IgnoreFieldUpdateProcessorFactory described above."
- UPC-R2-303, Proof line 25: "The test does not assert counted-field outcomes." is a limit inside the Proof, which reports outcomes only. **Delete** that sentence from line 25. **Add to Limits after the "dropped" bullet (line 38):** "- The test does not assert what a counted field does with an atomic update. Counted fields follow the rule in SOLR-7504, which lands first."
- UPC-R1-14 (carried, note on placement): the text is correct but sits after the combined-run bullet, not after line 26 as the register said. Move it above the combined-run bullet, or record the change.

### SOLR-12864 (H1)
- UPC-R2-304, Proof line 25: repeats bold line 22, which already says a run without the fix cannot show a failure by construction. **Replace with:** "- The new test, `testEchoDocsWithMapUniqueKeyOnly`, is expected to pass on base. That is the intended coverage result, because the SOLR-16811 fix is already in base."

### SOLR-13265 (H1)
- UPC-R2-305, Proof lines 24 and 25: `SolrIndexMetricsTest.java` has 2 test methods at the merge-base and 3 at head, so "3 tests" needs the whole file applied. **Replace line 24's last sentence with:** "On the merge-base `9b3a84b1c460`, with the branch's `SolrIndexMetricsTest.java` applied, 1 of 3 fails: this test." **Replace line 25 with:** "- On the merge-base `9b3a84b1c460` with only the branch's `SolrIndexMetricsTest.java` applied, the test fails at its assertion (run 2026-10-09, seed 13265C0FFEE13265). The counter datapoint is present and above zero there." The main side confirms the run used the same setup.

### SOLR-13943 (H1)
- UPC-R2-306, Proof line 32: "gate" is process vocabulary that the formula bars from outbound text. **Replace with:** "These counts are from a test run at cc155cf68e1d (2026-10-09). This head adds only the changelog fragment, which that run did not include."

### SOLR-14718 (H2)
- UPC-R2-401, Limits summary, line 29: overstates. Line 33 says two paths still name one request. **Replace with:** "**With SOLR-5939 in, a failed merged stream that reports its members names each request against its own document.**"
- UPC-R2-402, line 36: the second flaw is the same as the wrong-document case in line 34, and the record maps both to audit finding 2. **Replace line 36 with:** "- The ticket describes a second flaw: a streaming client can report the first document of a batch. That is the wrong-document case above. This change does not fix it. A follow-up submission for that flaw is planned." The main side confirms the mapping against the JIRA packet before posting.

### SOLR-16655 (H2)
- UPC-R2-403, Proof line 25: the receipt says only "the new test", and the branch has several new tests. **Replace with:** "- Without this change, at head `b201a57fb3e5`, `testTrimFieldsRecurseIntoNestedDocumentsOnlyWhenParentSelected` fails. That head is the parent of the last commit on the branch, `5e2317443f41`, which adds the rule that a child document is walked only under a selected field." The main side confirms the name in the gate log first.

### SOLR-16673 (H2)
- UPC-R2-404, Proof lines 23 and 24: line 23 has no verification date, and line 24 restates the bold Proof summary. **Replace lines 23 and 24 with:** "- At the earlier head `d5c19e64ba1b` (verified 2026-10-04), the changelog parses, the formatting check passes, the Error Prone compile passes, and the module checks pass. `ParsingFieldUpdateProcessorsTest` passes 43 of 43. The new test fails without the fix." and "- The one commit past the earlier head, at `d7170b12f312` (verified 2026-10-09), edits the changelog fragment. The fragment parses as YAML." The formatting-check wording also replaces the internal step name "tidy".

### SOLR-3657 (H2)
- UPC-R2-405, Proof line 25: repeats the bold summary and omits the base test count. **Replace with:** "- On base production code, `DocumentBuilderTest` runs 17 tests with exactly 2 failures, both at the destination-prefix assertions (gate of 2026-10-07)."

### SOLR-5065 (H2)
- UPC-R2-406, Proof line 27: the head is not named. **Replace with:** "- Verified 2026-10-09 at head `ab894a996c8a`."

### SOLR-5939 (H3)
- UPC-R2-502(a), line 52: "A stream implementation that does not report its members names only the first request in that stream." At `f8d4bdbea518`, with no members, the client records against the request it was built with, not the first request in the stream. This is the wording UPC-R1-102 fixed for SOLR-14718. **Replace with:** "- A stream implementation that does not report its members is reported against the request the client was built with."
- UPC-R2-502(b), line 42: "On base production, this class fails 2 of 2." The class is new, so the base run needs it added. **Replace with:** "On the merge-base production code, with this branch's test class added, this class fails 2 of 2."

### SOLR-5941 (H3)
- UPC-R2-503, line 48: "round 30" is process vocabulary. **Replace line 48 with:** "- The four classes in Proof are the focused tests at this head. Five classes from the focused run at `62516cc338e` were not re-run at this head: DirectUpdateHandlerTest, MaxSizeAutoCommitTest, TestUpdate, SolrCmdDistributorTest and DistributedUpdateProcessorTest." The five names match the round 3 material, section 6.

### SOLR-6065 (H3)
- UPC-R1-227(c) (carried, should-fix), line 23: "On the base production code, the same test fails with the generic analysis-error shape." The test is new, so the base run needs it added. **Replace with:** "On the merge-base production code, with this branch's test file added, the same test fails with the generic analysis-error shape."

### SOLR-6973 (H3)
- UPC-R1-231 (carried, should-fix), line 23: the same premise as above. **Replace with:** "On the merge-base production code, with this branch's test file added, the same class ran 7 tests with 1 failure." The base run and the failure identification required by the review ruling stay.
- UPC-R2-504, line 25: "review fix" is process narration that the formula keeps out of Proof. **Replace the first clause** with: "The same 7 tests and the same base failure were re-verified at this head." The early return is already described in What (lines 13 to 15).

### SOLR-7504 (H3)
- UPC-R2-505, line 38: the head is not named for the conflict. **Replace with:** "At `2fe06bfd917f` and SOLR-12705's head `aa56b7b1be4`, the two branches conflict in one import block of `FieldMutatingUpdateProcessorTest.java`, so SOLR-12705 needs that block merged by hand." The same head merged with `8624b7c3238b` is clean. The main side should also record that the SOLR-12705 head moved after round 3.

## Notes (no change required unless the main side agrees)

- UPC-R2-306 is listed above as should-fix. The notes below are not blocking.
- SOLR-5754 (H3, UPC-R2-506): line 24 cites a local merge commit that does not resolve in this worktree. The tree the draft names does resolve (`git merge-tree --write-tree f8d4bdbea518 7fbe0128d8b0` gives `f2e33340f0ea`). Optional: keep the tree, drop the commit id.
- SOLR-5887 (H3, UPC-R2-507): line 34 says "None of them was run for this change", but the receipt says the PR's CI run `37987597964` broke two existing tests. If that run included `TolerantUpdateProcessorTest`, `TestManagedSchema` or `TestSolrJErrorHandling`, say so with the outcome.
- SOLR-5939 (H3): the audit's item 11 (SOLR-5754 lands first) is not stated in either draft; the record has no order decision (UPC-R1-209, note).
- SOLR-5941 (H3, UPC-R2-508 to 511): "one SolrCloud path" should be "three SolrCloud commit paths"; line 47 should name the two classes that set `commit_end_point` on requests they build directly; line 38 should say "By reading the base code,"; the prose is about 4,750 characters and is over the guide, which the round 3 acceptances do not cover for this draft (main side to confirm or trim). The SOLR-13696 overlap (UPC-R1-217) belongs in the draft when 13696 resumes.
- SOLR-6045 (H3, UPC-R2-512, 513, 518): "behaviour" conflicts with "behavior" on line 39 (use "behavior"); the round 3 report moved the JSON-array behavior to What, and the draft follows the report (record the reconciliation); the factory tests are disclosed at line 38 and not run, and the DISCUSS decision in the audit is now in the draft (main side to decide whether to gate `AtomicUpdateProcessorFactoryTest` first).
- SOLR-6065 (H3, UPC-R2-514 to 516): "The branch returns 500." opens with a repeat of the bold summary (open with "A Lucene capacity limit is a server condition."); the changelog title does not mention the 400 to 500 change that line 17 calls a behavior change; SOLR-7022 also changes `DirectUpdateHandler2.java` and neither draft names the other (optional).
- SOLR-7022 (H3, UPC-R2-517): the premise run (receipt line 6) has no date or head; add them on revision.
- SOLR-12705 (H1): the carried note on placement (UPC-R1-14) above; the 4 KB-plus prose length (UPC-R1-32) is not accepted by any record.
- SOLR-11475 (H1, UPC-R2-309, 317): optional sentence in Proof; the audit's owner call A (step over or request) is still open in the record; the draft poses it to maintainers. The main side confirms the owner accepts that.
- SOLR-11483 (H1): audit findings 3 and 4 are not named in Limits (UPC-R1-06, note).
- SOLR-12703 (H1, UPC-R2-310, 311): "behavior" and "behaviour" are mixed in one bullet; the updated test "fails on the earlier head `ed95d555e62`" names no observed failure, so add the failure line from the gate log if it has it.
- SOLR-12864 (H2, UPC-R2-319): the audit's blocking items 1 and 2 are in the audit; the skip-list record disagrees with the pipeline record on this test. The main side reconciles the record.
- SOLR-13265 (H1, UPC-R2-312): the registration link (`DirectUpdateHandler2.java#L298-L300`) is head code the branch does not change; use the merge-base form, as UPC-R1-19 did. UPC-R1-21 (carried) has a wrong rationale in the response file: the cited lines do exist at the merge-base; the draft's "does not touch that code" already meets the note.
- SOLR-13696 (H1, UPC-R2-314, 315): the base run needed the `awaitsfix` group; the changelog line on the draft (line 64) is a bare span, not the head link the template requires (for the drafting pass).
- SOLR-14262 (H1, UPC-R2-313): the base run is not tied to a SHA; use "1 of 1" (not "1/1") on line 26.
- SOLR-16356 (H2, UPC-R2-409, 410): "Without the fix, this test fails." gives no observed failure (the record says only PASS); the Proof link's line range starts inside the method.
- SOLR-16910 (H2, UPC-R2-412): line 15 restates the bold "What this change does" sentence; acceptable as mechanism.
- SOLR-4841 (H2, UPC-R2-413): add the date 2026-10-09 to the top-up counts.
- SOLR-5505 (H2): converged; the fragment header and `nick:` key (audit item 8) are to be checked before the PR.
- Cross-cutting (H2, UPC-R2-415 to 417): UPC-R1-111, 113, 114 and 116 are counted in the round 1 table but have no text in the report body or the response; the main side restates them or marks them void. The follow-up sentences read "A follow-up submission is planned" (ratified wording; the main side confirms it still stands). Raw character counts including blob URLs run from about 3,400 to 6,350; the owner's extra-length item was not applied, and the main side confirms the first five are covered.

## Carried round 1 findings

Applied and verified: all round 1 blocking items (UPC-R1-16, 24, 102, 108, 128, 201, 211) are present as specified on their drafts. The round 1 should-fix replacements that the main side applied are present where specified, except the carried items above (UPC-R1-14 placement; UPC-R1-227(c); UPC-R1-231; UPC-R1-232 is UPC-R2-501).

## Process

- Read-only. No draft, receipt, audit, branch or record was edited. Nothing was posted, filed or built.
- Main side action: apply the blocking item (UPC-R2-501) and the should-fix replacements above, and resolve the owner calls noted. Then run round 3 on the drafts still open (SOLR-5939, 5941, 6065, 6973, 7022, 7504, 11483, 12705, 12864, 13265, 13943, 14718, 16655, 16673, 3657, 5065), starting with the ones with new text.
- Convergence: round 2 is NOT CONVERGED.
