# Update processing convergence, round 1

Verdict: **NOT CONVERGED.** 105 findings across 28 drafts: 7 blocking, 71 should-fix, 27 notes. No draft came back with no finding. Round 2 must re-check the drafts changed since their last review first (SOLR-5754, 5939, 5941, 6045, 6065, 7504 changed after round 3; SOLR-12705 and 13943 changed on 2026-10-10).

Scope: the 28 drafts in `pr-drafts/update-processing/`, read against the audits, receipts and material they cite. SOLR-13696 was checked for its gate claim only (see note UPC-R1-23). SOLR-18505 is closed and not in the folder. No draft was edited in this round. Every replacement below is for the main side to apply.

Head check: every named head matches its fork tip (`git ls-remote origin refs/heads/<branch>`) and the receipt head, except where recorded below (UPC-R1-01 and UPC-R1-24 record receipt/tip differences that are not content problems, or are blocking).

Method: each draft read in full; numbers checked against receipts, addenda and material at the head they name; code citations read at the named SHA; presentation checked against `pr-formula.md`; Limits checked against the audit's open findings; material decisions followed where recorded.

## Counts

| Slice | Drafts | Findings | Blocking | Should-fix | Notes | IDs |
|---|---|---|---|---|---|---|
| G1 | 10 (incl. SOLR-13696, gate claim only) | 32 | 2 | 23 | 7 | UPC-R1-01 to 32 |
| G2 | 9 | 34 | 3 | 23 | 8 | UPC-R1-101 to 134 |
| G3 | 9 | 39 | 2 | 25 | 12 | UPC-R1-201 to 239 |
| **Total** | **28** | **105** | **7** | **71** | **27** | |

## Blocking findings (apply before the draft is used)

- **UPC-R1-16, SOLR-12705, Limits.** "SOLR-7504 also conflicts with this change in two hunks." A merge check of the base, both heads and the pre-fix head of SOLR-7504 shows one conflict block, in the import block of `FieldMutatingUpdateProcessorTest.java`. The "two" count holds only for SOLR-16655. **Replace with:** "SOLR-7504 also conflicts with this change in one hunk, in the import block of FieldMutatingUpdateProcessorTest.java."
- **UPC-R1-24, SOLR-13943, line 32.** "These counts are from runs at this head." The counts were gated at `cc155cf68e1d`. This head adds only the changelog fragment, which that gate did not include. **Replace with:** "These counts are from the gate at cc155cf68e1d (2026-10-09). This head adds only the changelog fragment, which that gate did not include."
- **UPC-R1-102, SOLR-14718, line 32.** "A stream that reports none of its members names only the first request in that stream." The code records the error against the request the client was built with. **Replace with:** "A stream that reports none of its members is reported against the request the client was built with."
- **UPC-R1-108, SOLR-16356, line 9 ("catch block").** The link points at head `39c0585` lines 953 to 955, the generic catch that still logs ERROR. The pre-change ERROR path is the single catch at the merge-base lines 949 to 951. **Replace with:** `https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/update/UpdateLog.java#L949-L951` and add "(the code before this change, at the merge-base 14c7aac0d151)."
- **UPC-R1-128, SOLR-5065, line 18.** "This is an intended effect, and a test pins it." The owner has not confirmed intent (audit item 3 is open). **Replace with:** "Samples with these forms now infer pdouble, and a test pins that result."
- **UPC-R1-201, SOLR-5754, line 7.** "An error added in between can be cleared without a retry." The consequence is hypothetical: the window is unreachable on the paths read. **Replace with:** "**The retry pass copies the errors, then clears the list in a second call. An error added between those two calls would be cleared without a retry.**"
- **UPC-R1-211, SOLR-5941, line 17.** "Before, a leader forwarded it to its replicas, and a non-leader did nothing with it." The base code sends a commit with no end-point parameter to the collection nodes from any node, and a leader also sends it to its replicas. **Replace with:** "Before, it was forwarded to the other nodes of the collection, and a leader also sent it on to its replicas."

## Should-fix findings

### SOLR-11475 (head 229947201fd7)

- UPC-R1-01, line 9: the "loop" link is at head `229947201fd7` lines 805 to 844, which contains the new branch code. **Replace with:** "([loop before this change](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/update/PeerSync.java#L805-L837))".
- UPC-R1-02, line 25: "Verified 2026-10-08 at this head." The gate ran at `0de48e492fd4`, and this head has the same tree. **Replace with:** "Verified 2026-10-08 at 0de48e492fd4, and this head has the same tree."
- UPC-R1-03, line 42: the changelog line is a bare code span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-11475-peersync-version-ranges-loop.yml](https://github.com/nick-boss-tech/solr/blob/229947201fd797e57e2d3db1ff2c6552097c9734/changelog/unreleased/SOLR-11475-peersync-version-ranges-loop.yml)".

### SOLR-11483 (head 4431a250f665)

- UPC-R1-04, line 9: the link to `UpdateLog.java#L764-L765` uses the head SHA for pre-change code. **Replace with:** "([UpdateLog.java before this change](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/update/UpdateLog.java#L755-L756))".
- UPC-R1-05, line 40: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-11483-ulog-max-logs-default.yml](https://github.com/nick-boss-tech/solr/blob/4431a250f6650494f8d87bd689a7f45d5b4835ab/changelog/unreleased/SOLR-11483-ulog-max-logs-default.yml)".

### SOLR-12245 (head f325d5d0576e)

- UPC-R1-07, Limits lines 40 to 45: the multi-error form has no test, and the Limits do not say so. **Add:** "- The multi-error form ("N Async exceptions during distributed update") has no test. The tests build one error only."
- UPC-R1-08, line 47: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-12245-distrib-update-error-names-replica.yml](https://github.com/nick-boss-tech/solr/blob/f325d5d0576e582d4488570eabdeabc7410a8a0f/changelog/unreleased/SOLR-12245-distrib-update-error-names-replica.yml)".

### SOLR-12703 (head bdd29ba19900)

- UPC-R1-09, line 9: the "doSet" link uses head lines 479 to 482 for pre-change code. **Replace with:** "([doSet before this change](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/update/processor/AtomicUpdateDocumentMerger.java#L465-L468))".
- UPC-R1-10, line 36: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-12703-nested-atomic-op.yml](https://github.com/nick-boss-tech/solr/blob/bdd29ba19900628e13c311adc72a00b3eebcc8ca/changelog/unreleased/SOLR-12703-nested-atomic-op.yml)".

### SOLR-12705 (head aa56b7b1be4)

- UPC-R1-12, line 9: the `ParseDateFieldUpdateProcessorFactory` link is head code for symptom code the branch does not change. **Replace with:** "([ParseDateFieldUpdateProcessorFactory before this change](https://github.com/nick-boss-tech/solr/blob/0cc328310f8f4162572ea5bd5256cea29543e261/solr/core/src/java/org/apache/solr/update/processor/ParseDateFieldUpdateProcessorFactory.java#L113-L120))".
- UPC-R1-13, lines 13 to 18: the widening is stated, but the output changes in two existing processors are not. **Add after line 16:** "- Two existing processors change output. IgnoreFieldUpdateProcessorFactory keeps an atomic {"set": null} operand, where it used to remove the field. ConcatFieldUpdateProcessorFactory and FieldValueSubsetUpdateProcessorFactory now mutate a multi-value set operand, where they used to pass the map through."
- UPC-R1-14, Proof line 28: the settling run is recorded in the audit but not cited. **Add after line 26:** "- On the earlier pre-fix head b053944b127, the operand itself was counted: {add: "aaa"} came back as {add=1}. On a single-valued field holding 3, the appended {add: 1} failed the update with "multiple values encountered for non multiValued field price"."
- UPC-R1-15, Limits line 37: SOLR-5065 is not named. **Add:** "- SOLR-5065 touches the same parse family. A combined case, a set operation with 4.5E+10, has not been run with both changes."
- UPC-R1-17, line 40: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-12705-field-mutating-atomic-updates.yml](https://github.com/nick-boss-tech/solr/blob/aa56b7b1be4c5491723c58f296e20c906d7b7334/changelog/unreleased/SOLR-12705-field-mutating-atomic-updates.yml) (type: fixed)".

### SOLR-12864 (head b9c6c1e71ffa)

- UPC-R1-18, line 9: "Reading the code on base" links head lines 295 to 326. The lines are the same at the merge-base. **Replace with:** "([getDocMap on base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/handler/loader/JsonLoader.java#L295-L326))".

### SOLR-13265 (head c134b34aa27f)

- UPC-R1-19, line 9: the bypass link uses head code the change adds. **Replace with:** "The bypass path is at [DirectUpdateHandler2.java#L466-L469](https://github.com/nick-boss-tech/solr/blob/9b3a84b1c460981eab09d8ffaef776acc4a184f8/solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java#L466-L469), and the error count is at [#L500-L501](https://github.com/nick-boss-tech/solr/blob/9b3a84b1c460981eab09d8ffaef776acc4a184f8/solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java#L500-L501), both before this change."
- UPC-R1-20, Proof lines 22 to 25: the recorded decision is that the Proof states the fail-before verdict. **Add after line 24:** "On the base code with only this test applied, the test fails at its assertion (run 2026-10-09, seed 13265C0FFEE13265). The counter datapoint is present and above zero there."
- UPC-R1-22, line 34: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-13265-tlog-follower-update-errors.yml](https://github.com/nick-boss-tech/solr/blob/c134b34aa27fc09b754a24d9fed65898a7dd1748/changelog/unreleased/SOLR-13265-tlog-follower-update-errors.yml) (type: fixed)".

### SOLR-13943 (head ca443f6f6806)

- UPC-R1-25, line 9: the annotation link uses head lines 69 to 70 for state that existed before this change. **Replace with:** "([annotation before this change](https://github.com/nick-boss-tech/solr/blob/c3cdf7b46e8cfff3673f76d881f32cf8e7b00622/solr/core/src/test/org/apache/solr/update/processor/TimeRoutedAliasUpdateProcessorTest.java#L75))".
- UPC-R1-26, line 21: the watcher mechanism is stated as fact. The audit records it as a hypothesis, not reproduced. **Replace with:** "The new comment gives the reason, which has not been reproduced: the watcher may fire before the provider has refreshed its aliases."
- UPC-R1-27, line 44: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-13943.yml](https://github.com/nick-boss-tech/solr/blob/ca443f6f680606b6c995ae86b88b4841b53addbf/changelog/unreleased/SOLR-13943.yml) (restored unchanged from its earlier version)."

### SOLR-14262 (head 1e8d2b0075d7)

- UPC-R1-29, line 9: pre-change code linked at head. **Replace with:** "([DistributedUpdateProcessor.java before this change](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java#L1178-L1184))".
- UPC-R1-30, line 46: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-14262-commit-ignored-header.yml](https://github.com/nick-boss-tech/solr/blob/1e8d2b0075d7fded72ac19a049db27123a46ec10/changelog/unreleased/SOLR-14262-commit-ignored-header.yml)".

### SOLR-14718 (head 29c09959791a)

- UPC-R1-101, line 9: the two symptom links use head SHA for pre-change code the branch does not touch. **Replace both SHAs with** `9b3a84b1c460981eab09d8ffaef776acc4a184f8` and add "(links to the code before this change, at the merge-base 9b3a84b1c460)".
- UPC-R1-103, line 33: "For adds, the retry count is per document per node" conflicts with the recorded decision (material, round 2 lines 95 to 96: "retries are not per document"). The code supports the draft. **Main side must reconcile before either wording ships.** If the draft wording stands: "Retries are counted per request, and for adds each request holds one document for one node."
- UPC-R1-104, line 24: the counts bullet conflicts with the recorded "green wording without counts" decision, which the receipt may now supersede. Main side to say which governs. If the decision stands: "- With this change, the focused test class passes at this head (checked 2026-10-07)."
- UPC-R1-105, Proof, add after line 24: the settling verdict required by the decision. "- A check on 2026-10-09 removed the copy at this head. The test then failed the same assertion in each of 3 runs, and the unmodified head passed."
- UPC-R1-106, Limits, add after line 32: "- For an async failure after the first document in a stream, the error can name the wrong document." (required by the recorded decision; audit finding 2.)
- UPC-R1-107, line 36: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-14718-distributor-error-reports-its-document.yml](https://github.com/nick-boss-tech/solr/blob/29c09959791aea2ee46f7b697e1590d19a48ef1f/changelog/unreleased/SOLR-14718-distributor-error-reports-its-document.yml)".

### SOLR-16356 (head 39c0585072f0)

- UPC-R1-109, line 9: "UpdateLog.java" lines 919 to 925 link the head for pre-change code. **Replace the SHA with** `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` and add "(at the merge-base)".
- UPC-R1-110, line 34: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-16356.yml](https://github.com/nick-boss-tech/solr/blob/39c0585072f0eacc0767a173bab3f1d528db64f7/changelog/unreleased/SOLR-16356.yml)".

### SOLR-16655 (head 5e2317443f41)

- UPC-R1-112, lines 16 and 42: the body repeats the bold summaries. **Replace line 16 with:** "- Evidence for the anonymous walk: [anonymous walk](https://github.com/nick-boss-tech/solr/blob/5e2317443f4116a9e97330b0d6ce175cf44b54df/solr/core/src/java/org/apache/solr/update/processor/FieldMutatingUpdateProcessor.java#L148-L153). The selector still decides which field names change inside each child." **Delete from line 42:** "So this change lands first, and SOLR-12705 rebases onto it."
- UPC-R1-115, line 45: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-16655-field-mutating-urps-nested-docs.yml](https://github.com/nick-boss-tech/solr/blob/5e2317443f4116a9e97330b0d6ce175cf44b54df/changelog/unreleased/SOLR-16655-field-mutating-urps-nested-docs.yml)".

### SOLR-16673 (head d7170b12f312)

- UPC-R1-117, line 11: the ParseInt link is merge-base code linked at head. **Replace the SHA with** `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` and add "(at the merge-base)".
- UPC-R1-118, line 34: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-16673.yml](https://github.com/nick-boss-tech/solr/blob/d7170b12f312b364cb280b943a042707e553ea64/changelog/unreleased/SOLR-16673.yml) (type: fixed)."

### SOLR-16910 (head 9fce3e9a7058)

- UPC-R1-119, line 35: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-16910-log-update-processor-slow-format.yml](https://github.com/nick-boss-tech/solr/blob/9fce3e9a70582bcf977595a559acc2795b3774ca/changelog/unreleased/SOLR-16910-log-update-processor-slow-format.yml)".

### SOLR-3657 (head 14edaca577c0)

- UPC-R1-120, line 9: the message-wrapper link uses head SHA for merge-base code. **Replace the SHA with** `97d973814336101e12475558d7419321c743de79` and add "(at the merge-base)".
- UPC-R1-121, line 36: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-3657-copyfield-error-names-destination.yml](https://github.com/nick-boss-tech/solr/blob/14edaca577c0c1c38dc50b1ffa632c31fd1613de/changelog/unreleased/SOLR-3657-copyfield-error-names-destination.yml)".

### SOLR-4841 (head bda9f9d640d9)

- UPC-R1-123, line 9: three pre-change links use the head SHA. **Replace the SHA in those three** with `b6b2b8f10e9827e3b86e44649fb7966ce646c185` and add "(at the merge-base)". The DetectedLanguage link already uses the merge-base; keep it.
- UPC-R1-124, line 26: the sentence is garbled, and "maintainer" is not in the record. **Replace with:** "Both change only the changelog fragment's type. The final type, changed, follows a suggestion made during review."
- UPC-R1-125, line 46: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-4841-detectedlanguage-public-constructor.yml](https://github.com/nick-boss-tech/solr/blob/bda9f9d640d9ba4bc0ecf5ed891c14f9ddc77082/changelog/unreleased/SOLR-4841-detectedlanguage-public-constructor.yml) (type: changed)."

### SOLR-5065 (head ab894a996c8a)

- UPC-R1-127, line 9: the `parsePossibleDouble` link is pre-change code linked at head. **Replace the SHA with** `0cc328310f8f4162572ea5bd5256cea29543e261` and add "(at the merge-base 0cc328310f8f)".
- UPC-R1-129, Limits, add after line 35: "- No test combines this change with [SOLR-12705](https://issues.apache.org/jira/browse/SOLR-12705), which changes the base class the parse processors use. A set operation with an exponent value is not tested with both changes."
- UPC-R1-130, line 37: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-5065-parse-double-exponent-plus.yml](https://github.com/nick-boss-tech/solr/blob/ab894a996c8aa9825c4ac5af38536aec1ee865eb/changelog/unreleased/SOLR-5065-parse-double-exponent-plus.yml)".

### SOLR-5505 (head 44c444aa5cd3)

- UPC-R1-132, line 9: pre-change code linked at head. **Replace the SHA with** `cabedd1d968059215188f4e7563fb303241899ed`, use `LoggingInfoStream.java#L32` for the old log line, and add "(at the merge-base)".
- UPC-R1-133, line 46: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-5505-infostream-core-name.yml](https://github.com/nick-boss-tech/solr/blob/44c444aa5cd38618424c504fff307ead5cf466cf/changelog/unreleased/SOLR-5505-infostream-core-name.yml)".

### SOLR-13696 (gate claim only)

- No should-fix item. See UPC-R1-23 (note).

### SOLR-5754 (head 46b919e2d4e8)

- UPC-R1-202, line 22: "There is no separate run without the fix for this branch" does not state that a run without the fix is inconclusive by construction. **Replace line 22 with:** "**The combined run passed on a merged tree. A run without the fix is inconclusive by construction: the tests use `drainErrors()` and the `errors` field, which the base does not have, so they do not compile there.**"
- UPC-R1-203, line 26: the description of the removed root file is internal process narration. **Replace with:** "The tested head was `7fbe0128d8b0`. The current head `46b919e2d4e8` differs from it only by one removed root-level file that is not part of the change."
- UPC-R1-204, line 38: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-5754-streaming-clients-error-snapshot.yml](https://github.com/nick-boss-tech/solr/blob/46b919e2d4e8bc287ac913edb789c21509e1b215/changelog/unreleased/SOLR-5754-streaming-clients-error-snapshot.yml) (type: changed)."

### SOLR-5887 (head fa60337f88b4)

- UPC-R1-205, lines 9 and 33: the symptom links use head SHAs for merge-base code the branch does not change. **Line 9 replace with:** "as on the merge-base ([DocumentBuilder.java](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/update/DocumentBuilder.java#L301-L305))". **Line 33 link replace with:** "(https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/update/processor/ClassificationUpdateProcessor.java#L128)".
- UPC-R1-206, line 25: "On base production, DocumentBuilderTest runs 17 tests with exactly one failure" rests on a premise setup the receipt does not name. **Replace with:** "With AddUpdateCommand.java reverted to the merge-base b6b2b8f10e98 and this branch's test file, DocumentBuilderTest runs 17 tests with exactly one failure:"
- UPC-R1-207, line 37: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-5887-doc-error-core-name.yml](https://github.com/nick-boss-tech/solr/blob/fa60337f88b427cf53cb6750aae269bd4ab3de6f/changelog/unreleased/SOLR-5887-doc-error-core-name.yml)."

### SOLR-5939 (head f8d4bdbea518)

- UPC-R1-208, line 57: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-5939.yml](https://github.com/nick-boss-tech/solr/blob/f8d4bdbea518901e255ae119f3e5c43e7804a9bf/changelog/unreleased/SOLR-5939.yml) (type: fixed)."

### SOLR-5941 (head a4df7bfd214b)

- UPC-R1-212, line 41: the Proof omits the reason for the pre-fix head. The decided basis is that the defect came in with this branch's own guard. **Replace with:** "Without this fix, the new test fails at head `62516cc338ef`, this branch's own pre-fix head. Base `cabedd1d968` was not run for this test, because the defect came in with this branch's own guard."
- UPC-R1-213, Limits line 48: the claim that the four classes are the focused tests omits the five un-rerun classes the round 3 report requires to be named. **Replace with:** "- The four classes in Proof are the focused tests at this head. Five classes from the round 30 focused list at `62516cc338e` were not re-run here: DirectUpdateHandlerTest, MaxSizeAutoCommitTest, TestUpdate, SolrCmdDistributorTest and DistributedUpdateProcessorTest."
- UPC-R1-214, Limits (missing): audit item 11 (the reference guide check was not run) is not mentioned. **Add:** "- The reference guide check (`:solr:solr-ref-guide:check`) was not run for this change." (If the check did run at a4df7bfd214b, record that instead and drop the bullet.)
- UPC-R1-215, line 51: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-5941.yml](https://github.com/nick-boss-tech/solr/blob/a4df7bfd214b15d9c8d4519fdac8fd6df9468f2d/changelog/unreleased/SOLR-5941.yml) (type: changed)."

### SOLR-6045 (head 04208ba7f0b4)

- UPC-R1-218, lines 9 and 20: the factory link is post-change code; the JsonLoader sentence describes base. **Line 9 replace with:** "([factory on the merge-base](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/update/processor/AtomicUpdateProcessorFactory.java#L131-L137))". **Line 20 replace with:** "([array parser on the merge-base](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/handler/loader/JsonLoader.java#L637-L648))".
- UPC-R1-219, line 42: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-6045-atomic-update-repeated-field.yml](https://github.com/nick-boss-tech/solr/blob/04208ba7f0b45837738e2afe1c428292f9fecae3/changelog/unreleased/SOLR-6045-atomic-update-repeated-field.yml)."
- UPC-R1-220, line 39: the SOLR-7504 overlap is not named. **Add:** "- SOLR-7504 applies the same mixed-field rule in CountFieldValuesUpdateProcessorFactory. The two checks sit in different files, and their combined behaviour is not tested here."

### SOLR-6065 (head 3d2cec9e1ab3)

- UPC-R1-223, line 9: the generic-mapping link is post-change code (the branch changes the file). **Replace with:** "([generic mapping on the merge-base](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java#L418-L429))".
- UPC-R1-224, line 32: "as decided on 2026-10-06" cites an internal decision record, which the outbound text must not do. **Replace with:** "The branch returns 500."
- UPC-R1-225, line 32: the Choice has no closing pointed question. **Append to line 32:** "Was returning 500 the right call for a full index?"
- UPC-R1-226, line 41: the last Limits bullet omits the JVM-wide effect of the reflective setter (audit finding 4, open). **Replace with:** "- The tests set Lucene's document limit, a JVM-wide static, through a reflective call to a package-private setter, and restore it in a finally block. Its effect on other test classes in the same JVM was not checked."
- UPC-R1-228, line 43: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-6065-max-docs-message.yml](https://github.com/nick-boss-tech/solr/blob/3d2cec9e1ab3a19f6b948682b116c2780da4b65b/changelog/unreleased/SOLR-6065-max-docs-message.yml)."

### SOLR-6973 (head 51fcd05e695a)

- UPC-R1-229, line 9: two links use head SHAs for code the branch changes or merge-base code. **Replace with:** "([SignatureUpdateProcessorFactory.java on the merge-base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/update/processor/SignatureUpdateProcessorFactory.java#L200-L204)) and ([DirectUpdateHandler2.java on the merge-base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java#L1177-L1180))".
- UPC-R1-230, line 39: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-6973-signature-partial-update.yml](https://github.com/nick-boss-tech/solr/blob/51fcd05e695ae1a5983ed69a3d2afc50e7f7bc1e/changelog/unreleased/SOLR-6973-signature-partial-update.yml)."

### SOLR-7022 (head db357868610b)

- UPC-R1-232, line 24: "Run on 2026-10-05 at a2ed957063b" conflicts with the closeout's adopted instruction to cite the gate at `6a233ab2fdb`. The hold is still recorded (round 3 report, owner decision 1), and no workspace record withdraws it. **Main side decides.** If the takeover-log receipt is confirmed: "- Run at 6a233ab2fdb (log g7022r1-gate.log, 2026-10-06): DirectUpdateHandler2AwaitSearcherTest 2 of 2, DirectUpdateHandler2CommitWaitTest 1 of 1, DirectUpdateHandlerTest 7 of 7; the changelog YAML parses, tidy and the Error Prone compile pass, and the module checks pass." Add "- Later commits change wording only: 5b822b7e8e9 (changelog title) and db357868610b (one Javadoc comment)." Limits line 39 must then change. If not confirmed, keep a2ed957063b and the hold.
- UPC-R1-233, line 26: the premise-tree result is presented as the fail-without-change result. On the merge-base the helper does not exist, so the class does not compile there. **Append to line 26:** " On the merge-base the helper does not exist, so this class does not compile there. The commit-level test, DirectUpdateHandler2CommitWaitTest, is the one that fails on the base at the interrupt-restored assertion."
- UPC-R1-234, line 42: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-7022-interrupted-searcher-wait.yml](https://github.com/nick-boss-tech/solr/blob/db357868610b92709335129ebada4646ecf11e26/changelog/unreleased/SOLR-7022-interrupted-searcher-wait.yml)."

### SOLR-7504 (head 2fe06bfd917f)

- UPC-R1-236, line 41: bare changelog span. **Replace with:** "Changelog: [changelog/unreleased/SOLR-7504-count-values-atomic-update.yml](https://github.com/nick-boss-tech/solr/blob/2fe06bfd917f193b3591dfa4c088ef0994279ed6/changelog/unreleased/SOLR-7504-count-values-atomic-update.yml)."


## Notes (no change required unless the main side agrees)

- UPC-R1-06 (11483, Limits): audit findings 3 and 4 (package-private constants; a test that does not close its UpdateLog) are open low items and are not mentioned.
- UPC-R1-11 (12703, line 33): "should be sequenced, not merged cold" is idiom. Suggested plain form: "The two should be sequenced, and their combined behaviour tested, before either merges."
- UPC-R1-21 (13265, line 15): the link to `DistributedUpdateProcessor.java` uses head lines for code the branch leaves unchanged. Consider the merge-base or "unchanged by this change".
- UPC-R1-23 (13696, gate claim): the Proof claims a green gate at `da4fa6df1178`. The record supports it (receipts/SOLR-13696.md: gate r8 GREEN, 2026-10-09; addendum). The assignment's status (gate r6 failed, not to be drafted this round) predates r8. **Main side to confirm which status governs.** No draft defect. Citations were not checked (out of scope).
- UPC-R1-28 (13943, line 34): optional addition: the test also failed in 3 of 5 runs on the unstacked branch.
- UPC-R1-31 (14262, Limits): audit finding 4 (response object not null-checked, low) is not mentioned. Not a readiness item.
- UPC-R1-122 (3657, Limits): audit item 6 (a wording cleanup in a test comment) is optional.
- UPC-R1-126 (4841, lines 19 and 21): "f8850ffd421" named as the tested head while the fork tip is bda9f9d640d9. The body covers the new head, so this is clarity only. "Neighbors" is shorthand.
- UPC-R1-131 (5065, lines 9 and 24 to 25): "pin test" is shorthand; "the suggester test" is plainer. The claim holds at suggester level; keep it scoped.
- UPC-R1-134 (5505, line 46 and Proof): audit item 8 (the fragment lacks the header comment and has an extra "nick:" key). Check before the PR.
- UPC-R1-209 (5939, Limits): audit item 11 (SOLR-5754 lands first) is not stated. Audit item 1 still reads Open at `8f7a36fa6dd`, while the success path removes entries at the head. The audit needs a correction note.
- UPC-R1-210 (5939, lines 36 and 42): "On base production, this class fails 2 of 2" rests on a log not in the workspace. Say "with this branch's test class" if the base run used it.
- UPC-R1-216 (5941, line 49): the link to `IgnoreCommitOptimizeUpdateProcessorFactory.java` uses the head for a file the branch does not change. Use the merge-base form.
- UPC-R1-217 (5941, Limits): overlap with the held SOLR-13696 is a hypothesis. Add a line when 13696 resumes.
- UPC-R1-32 (12705 and 13943, length): both bodies are over the 3,500-character guide and are not named in the recorded length acceptances. Main side to confirm both are among the seven long drafts the owner accepted.
- UPC-R1-221 (6045, line 38): the re-gate covers AtomicUpdatesTest only, and the factory tests are not in the run. The draft discloses this. Main side to decide whether to gate AtomicUpdateProcessorFactoryTest first.
- UPC-R1-222 (6045, line 19): "and the changelog does not say so" reads as a reviewer note. Suggest "The changelog text does not mention it."
- UPC-R1-227 (6065, several): (a) the audit's blocking item 1 (no cloud test) is resolved at this head (`MaxDocsLimitCloudTest.java` is in the diff); the audit needs a re-check record. (b) The Lucene 9.12.3 claim is supported by `reports/lucene-version-claims-6065.md` but is not named in the receipt. (c) The base-production wording is UPC-R1-206's premise issue. (d) Limits line 38 links `addIndexes` at head line 784; base line 765 is the form to use.
- UPC-R1-231 (6973, line 23): "On the base production code, the same class ran 7 tests with 1 failure" has the same premise question as UPC-R1-206.
- UPC-R1-235 (7022, line 27): audit finding 1 (changelog's unproven cause) is closed at the head. Nothing to add.
- UPC-R1-237 (7504, lines 25 and 38): the earlier head 22b77196e662 is recorded in the receipt (lines 7 and 8); not a finding. The combined merge conflict is as the draft says (import block only).
- UPC-R1-238 (7504, line 15): audit finding 6 (owner to confirm null and collection semantics). The draft states them openly. Main side to confirm owner acceptance before opening.
- UPC-R1-239 (5754, line 24): the earlier head `7fbe0128d8b0` is the recorded combined-gate pair; not a finding.

## Per-draft verdicts

| Draft | Head | Findings | Blocking | Should-fix |
|---|---|---|---|---|
| SOLR-11475 | 229947201fd7 | 01, 02, 03 | - | 3 |
| SOLR-11483 | 4431a250f665 | 04, 05, 06 | - | 2 |
| SOLR-12245 | f325d5d0576e | 07, 08 | - | 2 |
| SOLR-12703 | bdd29ba19900 | 09, 10, 11 | - | 2 |
| SOLR-12705 | aa56b7b1be4 | 12 to 17 | 16 | 5 |
| SOLR-12864 | b9c6c1e71ffa | 18 | - | 1 |
| SOLR-13265 | c134b34aa27f | 19 to 22 | - | 3 |
| SOLR-13696 | da4fa6df1178 | 23 (gate claim only) | - | - |
| SOLR-13943 | ca443f6f6806 | 24 to 28 | 24 | 3 |
| SOLR-14262 | 1e8d2b0075d7 | 29, 30, 31 | - | 2 |
| SOLR-14718 | 29c09959791a | 101 to 107 | 102 | 6 |
| SOLR-16356 | 39c0585072f0 | 108 to 111 | 108 | 2 |
| SOLR-16655 | 5e2317443f41 | 112 to 116 | - | 2 |
| SOLR-16673 | d7170b12f312 | 117, 118 | - | 2 |
| SOLR-16910 | 9fce3e9a7058 | 119 | - | 1 |
| SOLR-3657 | 14edaca577c0 | 120, 121, 122 | - | 2 |
| SOLR-4841 | bda9f9d640d9 | 123 to 126 | - | 3 |
| SOLR-5065 | ab894a996c8a | 127 to 131 | 128 | 3 |
| SOLR-5505 | 44c444aa5cd3 | 132 to 134 | - | 2 |
| SOLR-5754 | 46b919e2d4e8 | 201 to 204, 239 | 201 | 3 |
| SOLR-5887 | fa60337f88b4 | 205 to 207 | - | 3 |
| SOLR-5939 | f8d4bdbea518 | 208 to 210 | - | 1 |
| SOLR-5941 | a4df7bfd214b | 211 to 217 | 211 | 4 |
| SOLR-6045 | 04208ba7f0b4 | 218 to 222 | - | 3 |
| SOLR-6065 | 3d2cec9e1ab3 | 223 to 228 | - | 5 |
| SOLR-6973 | 51fcd05e695a | 229 to 231 | - | 2 |
| SOLR-7022 | db357868610b | 232 to 235 | - | 3 |
| SOLR-7504 | 2fe06bfd917f | 236 to 238 | - | 1 |

## Process

- Read-only. No draft, PR, branch, receipt or audit was edited. Nothing was posted, filed or built.
- Main side action: apply the blocking and should-fix replacements, resolve the decision conflicts (UPC-R1-103, UPC-R1-104, UPC-R1-232, UPC-R1-23) and the owner calls noted above, then run round 2 on the changed drafts.
- Convergence: round 1 is NOT CONVERGED.
