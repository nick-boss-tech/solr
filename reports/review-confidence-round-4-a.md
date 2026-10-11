# Review confidence round 4, part A (RC4A): live PR body fidelity

- Assignment: `assignments/pool-review-confidence-round-4.md`. Claim: `claims/pool-review-confidence-round-4.md` (RC4A; not edited here).
- Date of read: 2026-10-11. Read-only: no body edit, comment, review, branch, draft, build, test or gate.
- Bodies: SOLR-18178 (PR 4968); SOLR-18391 (PR 5027, open) and SOLR-18391 (PR 4997, draft), as two bodies; SOLR-18129 (PR 5000); SOLR-16130 (PR 5004); SOLR-18482 (PR 5009).

## Verdicts

| Ticket | PR | Verdict |
|---|---|---|
| SOLR-18178 | 4968 | DRIFT (3 items) |
| SOLR-18391 | 5027 | DRIFT (5 items), plus 1 record gap |
| SOLR-18391 | 4997 | DRIFT (2 items) |
| SOLR-18129 | 5000 | DRIFT (5 items) |
| SOLR-16130 | 5004 | DRIFT (1 item) |
| SOLR-18482 | 5009 | DRIFT (2 items), plus 1 record conflict |

## Head alignment (Method step 1 and 2)

All six bodies were read from the live API (`gh pr view`, JSON fields number, title, state, isDraft, headRefName, headRefOid, updatedAt, body). Each fork tip from `git ls-remote origin` equals the API head SHA, and each receipt names the same head.

| PR | State | Head ref | API head SHA | ls-remote | Receipt head | updatedAt |
|---|---|---|---|---|---|---|
| 4968 | OPEN, ready | solr-18178-verify | b75e7d4d3c464ceeb9f14fdaf7711212adcccac2 | match | match (gated head) | 2026-10-11T02:44:49Z |
| 5027 | OPEN, ready | solr-18391-graceful-create-submit | adcda10b501cbea9f92119bae8e91187da77d9da | match | match | 2026-10-10T21:49:15Z |
| 4997 | OPEN, DRAFT | solr-18391-submit | 3000eeede7adf134ddbb7936192c1961fb464918 | match | match (draft section) | 2026-10-10T21:49:12Z |
| 5000 | OPEN, ready | solr-18129-submit | 657e443d866d5bdfd7b3d7f3448e074a2ae75714 | match | match | 2026-10-10T21:49:20Z |
| 5004 | OPEN, ready | solr-16130-test-followup | 3c48dec4b79faed625c4fb79833d0975a55f3ed7 | match | match | 2026-10-10T21:49:16Z |
| 5009 | OPEN, ready | solr-18482-submit | 49ca9099d8ebc2b43fb4482791fef736cc036d7a | match | match | 2026-10-11T02:45:15Z |

Titles, from the API: 4968 "SOLR-18178: Fix configset archive path handling"; 5027 "SOLR-18391: Clean up a collection when creation fails partway, and name the cause in the error"; 4997 "SOLR-18391: Fail collection creation cleanly when the collection is missing from cluster state during replica assignment"; 5000 "SOLR-18129: Keep multi-valued request-handler defaults, appends and invariants on the Config API"; 5004 "SOLR-16130: Add cross-collection export regression test"; 5009 "SOLR-18482: Reject unsupported parameters on JSON range facets".

Every body has the AI header, the Jira link line and the AI assistance footer, and every section opens with a bold one-line summary. The only body-wide miss is the changelog line (not a link), which is listed under each body below.

---

## SOLR-18178 (PR 4968): DRIFT (3 items)

Checked and consistent:
- Receipt counts: UploadConfigSetAPITest 20 of 20 at the head (three seeds); 18 of 18 at 3c307fa231b with exactly 3 fail-closed failures; exactly 1 failure against the previous head's production (testZipUploadIgnoresLegacyRootDirectoryMarker); DownloadConfigSetAPITest 5 of 5 at head; Windows runs at c3b6ded623e (3 tests, 0 failed; 2 of 3 fail on base) and 61b8f767c34.
- At head, UploadConfigSetAPITest has 20 test methods, and the five named tests exist (testSafeZipEntryPathRules, testZipUploadRejectsPathTraversalEntries, testZipUploadRejectsUnsafeEntryPathsBeforeBackendDispatch, testZipUploadRejectsDriveQualifiedEntries, testZipUploadIgnoresLegacyRootDirectoryMarker).
- UploadConfigSet.java: the two-pass validation (validate every entry, then dispatch); the "/" directory entry is ignored only when it is a directory; a leading "/", "." or ".." segment, a drive prefix and an empty name are rejected with BAD_REQUEST naming the entry; uploadConfigSetFile applies the same check before reading the body; backslashes are normalized unconditionally.
- DownloadConfigSet.toZipEntryName (forward slashes, no leading slash, trailing slash on directories); FileSystemConfigSetService listing normalization.
- Choice (fail the whole upload) matches the commit history (4ccbdd620a0 fail closed; 502a41ef6ba and f9c78f08814 earlier skip version) and the PR discussion (epugh and malliaridis lean to failing; author agrees).
- Limits paragraphs for cleanup=true and the partial-write behavior match the base code path (filesToDelete computed before the upload).

Drift items:

1. Changelog line is not a link to the head SHA.
   - Live: "Changelog: `changelog/unreleased/SOLR-18178.yml` (fixed)"
   - Evidence: pr-formula.md (changelog line links the fragment at the head SHA); the fragment exists at b75e7d4d3c4 (type fixed).
   - Replacement: "Changelog: [changelog/unreleased/SOLR-18178.yml](https://github.com/nick-boss-tech/solr/blob/b75e7d4d3c464ceeb9f14fdaf7711212adcccac2/changelog/unreleased/SOLR-18178.yml) (fixed)"

2. Fork-runner bullet implies head coverage and gives no date or head. It also places a vm1 run under the GitHub heading.
   - Live: "- Also on the GitHub Actions fork runner: `TestFileSystemConfigSetService` 3/3 and `TestSchemaDesignerConfigSetHelper` 8/8 pass, covering the export-side directory listing and the schema-designer ZIP entry assertions. `DownloadConfigSetAPITest` passes 5 of 5 at head b75e7d4d3c4 (verified 2026-10-10), covering archives containing subdirectories."
   - Evidence: receipt lines 12 and 15. The two fork runs were on 2026-10-04 at f4945cbc638 (TestFileSystemConfigSetService, 3 tests, 0 failures) and b3a2b73d59d (TestSchemaDesignerConfigSetHelper, 8 tests, 0 failures). Both are ci/ branch commits and neither is an ancestor of b75e7d4d3c4. f4945cbc638 predates the nested-directory assertion added at 61b8f767c34 (the head's copy of that test file differs from f4945 by 9 lines). The 5 of 5 DownloadConfigSetAPITest run is the 2026-10-10 restoration run, recorded as vm1 in the receipt, not a GitHub run. The @Test counts at head (3, 8, 5) match.
   - Replacement: "- On the fork's GitHub Actions runner, 2026-10-04, at CI commits that are not the head: `TestFileSystemConfigSetService` 3 of 3 at f4945cbc638, and `TestSchemaDesignerConfigSetHelper` 8 of 8 at b3a2b73d59d. The head's `TestSchemaDesignerConfigSetHelper` is unchanged from b3a2b73d59d. The head's `TestFileSystemConfigSetService` adds a nested-directory assertion, which the Windows run above covers. `DownloadConfigSetAPITest` passes 5 of 5 at head b75e7d4d3c4 (run 2026-10-10), covering archives containing subdirectories."

3. The choice section does not end with the pointed question the formula asks for.
   - Live (last sentence of "A choice to check"): "Happy to revisit if skipping still seems better."
   - Evidence: pr-formula.md section 4 and the template ("Ends with the pointed question").
   - Replacement: "Was failing the whole upload the right call, or should unsafe entries be skipped instead?"

Observations, not counted as drift:
- The 61b8f767c34 bullet says "passes 3/3 at head". The run was at 61b8f767c34, and TestFileSystemConfigSetService.java and FileSystemConfigSetService.java are unchanged between 61b8f767c34 and the head, so the result holds. Optional: "at 61b8f767c34".
- The paragraph on the earlier drive-letter test failure narrates an internal development catch. Optional deletion under the formula's process-narration rule.

---

## SOLR-18391 (PR 5027, open): DRIFT (5 items), plus 1 record gap

Checked and consistent:
- Every code citation lands on the cited code at adcda10b501, except item 2: CreateCollectionCmd.java 162 (stateWritten), 103-104 (3 attempts, 200 ms), 211-212 (PRS state write), 224 and 259 (30 s wait message), 233-236 (exists guard), 253-255 (30 s visible wait), 269-271 (re-read fill-in), 399-402 (PRS setData), 505-512 (alias write), 515-521 (single catch, interrupt guard), 526-528 (AssignmentException to 400), 531 (Could not create collection), 542-570 (bounded retry, ZooKeeperException only), 588-592 (cleanup logged and suppressed), 726-729 (configset copy), 803-804 (makePath). PlacementPluginAssignStrategy.java 179-187 and 204. DeleteCollectionCmd.java 149-150, 201-212, 237-244. CreateShardCmd.java 151-160. CollectionHandlingUtils.java 346-349. All link paths exist at the head.
- Base behavior described in "What happens today" matches the base file (c3cdf7b46e8): only AssignmentException and core failures run the delete; the catch-all at base has a null message.
- Counts: CreateCollectionCleanupTest 5 of 5 (5 test methods), PlacementPluginIntegrationTest 8 of 8 (8), CreateCollectionCmdRetryTest 4 of 4 (4), OverseerCollectionConfigSetProcessorTest 26 of 26, DeleteCoreRemnantsOnCreateTest 5 of 5, :solr:core:check -x test green: all as in the receipt.
- The stale-view test asserts BAD_REQUEST, the "collection already exists" text, and a byte-identical state.json, as the body says. Its commit d129cc22d01 is two commits before the head.
- PR checks at the head, all pass: "gradle check", "Run Solr Tests using Crave.io resources", "Check changelog entry" (runs 37590919851, 37590919739, 37590919940; head_sha adcda10b501).
- Base comparison (receipt): with base production and the branch tests, 2 of 5 CreateCollectionCleanupTest tests and 1 PlacementPluginIntegrationTest test fail; CreateCollectionCmdRetryTest is excluded. Matches the body.
- Choice 1 matches DeleteCollectionCmd 237-244 (alias refusal). Limits statements about the system-collection branch and the alias wiring are right (no test in CreateCollectionCleanupTest mentions alias; testAssignForMissingCollection covers the non-system path only).

Drift items:

1. Changelog line is not a link to the head SHA.
   - Live: "Changelog: `changelog/unreleased/SOLR-18391-collection-creation-failure-cleanup.yml`"
   - Evidence: pr-formula.md; the fragment exists at adcda10b501.
   - Replacement: "Changelog: [changelog/unreleased/SOLR-18391-collection-creation-failure-cleanup.yml](https://github.com/nick-boss-tech/solr/blob/adcda10b501cbea9f92119bae8e91187da77d9da/changelog/unreleased/SOLR-18391-collection-creation-failure-cleanup.yml)"

2. Two citations do not support the sentence they carry.
   - Live: "...the message for a core that fails to come up now names the first per-node failure ([CreateCollectionCmd.java:445](https://github.com/nick-boss-tech/solr/blob/adcda10b501cbea9f92119bae8e91187da77d9da/solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java#L445), [:471-477](https://github.com/nick-boss-tech/solr/blob/adcda10b501cbea9f92119bae8e91187da77d9da/solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java#L471-L477))."
   - Evidence: head line 445 is the declaration `String failureDetail = null;`. The first failure is taken at 449. Lines 471-477 are the "not all replicas became active" fallback and the start of the throw. The message that carries failureDetail is at 474-482 (failureDetail at 481).
   - Replacement: "...the message for a core that fails to come up now names the first per-node failure ([CreateCollectionCmd.java:446-450](https://github.com/nick-boss-tech/solr/blob/adcda10b501cbea9f92119bae8e91187da77d9da/solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java#L446-L450), [:474-482](https://github.com/nick-boss-tech/solr/blob/adcda10b501cbea9f92119bae8e91187da77d9da/solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java#L474-L482))."

3. Proof names checks that the receipt does not record at this head.
   - Live, Proof paragraph 1: "Verified locally at the head adcda10b501: ..." ending "... and :solr:core:check -x test green, with tidy and the Error Prone compile clean." Live, Proof paragraph 2 (follow-up) ends "CreateCollectionCmdRetryTest 4/4, PlacementPluginIntegrationTest 8/8, DeleteCoreRemnantsOnCreateTest 5/5, and :solr:core:check -x test green, with tidy and the Error Prone compile clean."
   - Evidence: receipt SOLR-18391.md line 5 (gate at adcda10b501): tidy rc=0 with 0 files changed afterward, :solr:core:compileTestJava rc=0, the five focused counts, :solr:core:check -x test rc=0. No Error Prone run is recorded at this head; the receipt records Error Prone only for the draft head 3000eeede7a. The receipt's gate host is vm2, not a local run.
   - Replacement: in both paragraphs, change "Verified locally at the head adcda10b501" to "Verified at the head adcda10b501", and change "with tidy and the Error Prone compile clean" to "with tidy clean and the test sources compiling".

4. A mutation run is stated without any record.
   - Live: "With the exists check disabled in a local run, the same test fails because the create's cleanup deletes the pre-existing collection, so the test pins the guard."
   - Evidence: no receipt entry or log for this run. The receipt records only the base-tree run at c3cdf7b46e8.
   - Replacement: delete the sentence.

5. An earlier verification is stated without any record.
   - Live: "An earlier verification at fca4235be87, four commits before the head, had CreateCollectionCleanupTest 4/4; the class gained its fifth test, the stale-view test, in one of the later commits."
   - Evidence: fca4235be87 is four commits before the head (correct), but the receipt records no run at that commit. Git shows the stale-view test was added by d129cc22d01, two commits before the head.
   - Replacement: "The stale-view test was added in d129cc22d01, two commits before the head."

Record gap (no replacement chosen):
- The Proof section gives no verification date for the focused runs at adcda10b501, and formula section 3 requires one. The receipt gives the tip check and the proof decision as 2026-10-10, but no date for the focused gate run. The main side should supply the date from the gate log, or the body should keep the date out.

---

## SOLR-18391 (PR 4997, draft, superseded): DRIFT (2 items)

Checked and consistent:
- Head 3000eeede7adf134ddbb7936192c1961fb464918 is the fork tip and the receipt head. The 2026-10-04 verification (tidy, compile, Error Prone compile, check, clean tree) matches the receipt.
- The head commit "Drop mock-heavy tests per review feedback" removes CreateCollectionCmdTest and PlacementPluginAssignStrategyTest, as the body says. The attribution holds: David Smiley's review of 2026-10-02 asked to drop the mock-heavy tests, and the author replied "Dropped both test files."
- Only PlacementPluginAssignStrategy.java changes in production. CreateCollectionCmd.java is unchanged against the PR's merge-base 56ec140e363, so "the cleanup path it triggers is existing, unmodified code" holds.
- The PR is a draft, and the "Superseded by #5027" line is correct (same repository, apache/solr).

Drift items:

1. Line count is wrong.
   - Live: "The production change is 17 lines added and 3 removed in one file;"
   - Evidence: `git diff --numstat` for solr/core/src/java from 56ec140e363 to 3000eeede7a (and from c3cdf7b46e8 to 3000eeede7a) gives PlacementPluginAssignStrategy.java 14 added, 3 removed.
   - Replacement: "The production change is 14 lines added and 3 removed in one file;"

2. Changelog line is not a link to the head SHA.
   - Live: "Changelog: `changelog/unreleased/SOLR-18391.yml` (fixed)"
   - Replacement: "Changelog: [changelog/unreleased/SOLR-18391.yml](https://github.com/nick-boss-tech/solr/blob/3000eeede7adf134ddbb7936192c1961fb464918/changelog/unreleased/SOLR-18391.yml) (fixed)"

---

## SOLR-18129 (PR 5000): DRIFT (5 items)

Checked and consistent:
- Head 657e443d866 differs from f5715964a09 only in changelog/unreleased/SOLR-18129.yml, as the body says.
- CommandOperation.java (solrj): AccumulatingObjectBuilder; REQUEST_HANDLER_COMMANDS holds the three commands; MULTI_VALUED_REQUEST_HANDLER_PARAMS holds defaults, appends and invariants; normalizeCommandData runs on every command; repeated sections and other commands collapse to the last value. The file has 118 lines added (the body says "about 120").
- Limits are correct: a repeated section (two defaults objects) is not merged and the last one wins; a mixed array and scalar is flattened, and no test covers that mix (testCommandOperationNestedArrayDefaults is array-only).
- Counts: TestUtils has 20 test methods; TestSolrConfigHandler has 13. The end-to-end tests post to /config (v1). The ref guide diff matches the "portable encoding" sentence.
- The v2 path reaches the same code (ApiBag.getCommandOperations, then CommandOperation.parse).
- The fork's GitHub run 37569145964 of TestUtils at this head (2026-10-06) is in the receipt.

Drift items:

1. Changelog line is not a link to the head SHA.
   - Live: "Changelog: `changelog/unreleased/SOLR-18129.yml` (fixed)"
   - Replacement: "Changelog: [changelog/unreleased/SOLR-18129.yml](https://github.com/nick-boss-tech/solr/blob/657e443d866d5bdfd7b3d7f3448e074a2ae75714/changelog/unreleased/SOLR-18129.yml) (fixed)"

2. Gate claims go beyond the receipt.
   - Live: "Verified at head 657e443d866 on 2026-10-06 (a changelog-title-only change on top of f5715964a09), with Error Prone enabled: `TestUtils` 20 of 20 and `TestSolrConfigHandler` 13 of 13 pass (the class has 13 test methods), with tidy and `:solr:core:check -x test` clean."
   - Evidence: the receipt (lines 4-5 and the count reconciliation) records the round 29 re-review gate at 657e443d866 (log g18129r29-gate.log): `:solr:core:check` rc=0, and fresh JUnit XML counts TestUtils 20 tests, 0 failures, and TestSolrConfigHandler 13 tests, 0 failures. No tidy or Error Prone result is recorded for this head, and the receipt names `:solr:core:check`, not `-x test`.
   - Replacement: "Verified at head 657e443d866 on 2026-10-06 (a changelog-title-only change on top of f5715964a09): `TestUtils` 20 of 20 and `TestSolrConfigHandler` 13 of 13 pass (the class has 13 test methods), from the focused run at this head, with `:solr:core:check` passing."

3. The local re-run is not on record.
   - Live: "The earlier tests also ran on the fork's GitHub Actions test runner; the array-form tests below were verified in a local re-run (tidy, Error Prone compile, focused tests, module check)."
   - Evidence: no local re-run is in the receipt. The recorded GitHub run is for TestUtils at this head (run 37569145964, 2026-10-06). The array-form tests are inside the TestUtils and TestSolrConfigHandler counts above.
   - Replacement: "TestUtils also passed on the fork's GitHub Actions runner at this head (run 37569145964, 2026-10-06). The array-form tests are part of the focused counts above."

4. Base-code and previous-head results are stated without any record.
   - Live, TestUtils bullet: "On base code, the nested duplicate-key cases fail: they expect `[subject, country]` and get only the last value." Live, TestSolrConfigHandler bullet: "On base code, the end-to-end cases fail the same way: the handler read back through the Config API holds only the last value." Live, array-form paragraph: "To isolate the list-form handling itself, both tests were also run against production code from the previous head, 63b16895b5a, ... both fail there too (...), and both pass at this head. The single-object accumulation test passes against the previous head as well, and ..."
   - Evidence: the receipt records no case-level base results and no run at 63b16895b5a. It records only that the GitHub patched-tree runs 37225864392 and 37225860272 succeeded and that the proof tree failed at the test step as designed. 63b16895b5a is the commit before the list-form commit 72fd153d7bc, not the PR's immediate previous head (f5715964a09).
   - Replacement, TestUtils bullet: "* `TestUtils`: 20 of 20 pass with this change."
   - Replacement, TestSolrConfigHandler bullet: "* `TestSolrConfigHandler`: 13 of 13 pass with this change. End to end, create is covered for all three sections and update for `defaults`; `appends` and `invariants` updates are covered at the parser level in `TestUtils`, where all three sections use `update-requesthandler`."
   - Replacement, array-form paragraph: "The array-of-commands form (a JSON array of command objects under one command name) is covered at the parser level in `TestUtils` and end to end in `TestSolrConfigHandler`. A guard test in `TestUtils` pins last-value-wins for other commands in list form. On the pre-fix proof tree the focused tests fail at the test step, and the patched tree passed in GitHub runs 37225864392 and 37225860272."

5. The choice section does not end with the pointed question.
   - Live (last sentence): "If you would rather the parser stay strict, I can reduce this to the documentation route instead."
   - Replacement: keep that sentence and add: "Was accumulating the right call, or should the parser stay strict and document arrays only?"

---

## SOLR-16130 (PR 5004): DRIFT (1 item)

Checked and consistent:
- Head 3c48dec4b79 (commits 1ea8c06f4c0, 98ef9347cf4, 3c48dec4b79; diff is the test class and the ccjoin solrconfig only, no changelog file).
- testCcJoinWithTrailingUnmatchedExportSegment exists with the described steps: matching documents committed, unmatched documents committed, at least two leaves asserted, and the four ids to-0 to to-3 asserted.
- ccjoin configset sets useFilterForSortedQuery, and only CrossCollectionJoinQueryTest uses that configset.
- CrossCollectionJoinQueryTest has 10 test methods; the receipt records 10 of 10 at the head (round 30 gate).
- Fail-before base 70c1a28995d is the parent of guard 56ec140e363 (#4953), which is on upstream/main. The receipt records the NullPointerException at ExportWriter$SegmentIterator.topDocs (ExportWriter.java:863).
- "Changelog: none (test-only change; exempt under dev-docs/changelog.adoc)" is a fair reading: dev-docs/changelog.adoc lists test infrastructure under type "other", where most changes are too small for an entry.

Drift item:

1. The fail-before run detail is not on record.
   - Live: "...the server log records exactly the null-bitset error the guard prevents: `java.lang.NullPointerException: Cannot invoke "org.apache.lucene.util.BitSet.length()" because "bits" is null` at `ExportWriter$SegmentIterator.topDocs`; the broken export stream then fails the join query with `SolrServerException: No live SolrServers available to handle this request`."
   - Evidence: receipt line 7 records the NullPointerException only. No SolrServerException is recorded for the fail-before run.
   - Replacement: "...the server log records exactly the null-bitset error the guard prevents: `java.lang.NullPointerException: Cannot invoke "org.apache.lucene.util.BitSet.length()" because "bits" is null` at `ExportWriter$SegmentIterator.topDocs`." (Restore the SolrServerException clause only if g16130r30-failbefore.log shows it.)

Observation, not counted: the mechanism sentence (useFilterForSortedQuery routes export through the sortDocSet path that skips getLeafCollector for trailing unmatched leaves) was not verified in depth. The flag's routing is visible in SolrIndexSearcher.java (lines 1640-1684).

---

## SOLR-18482 (PR 5009): DRIFT (2 items), plus 1 record conflict

Checked and consistent:
- Head 49ca9099d8e is one docs commit on top of f09608f5446 (one line deleted from json-facet-api.adoc). No Java code changes in it.
- FacetRangeParser.java: the seven parameters (limit, offset, sort, prelim_sort, overrequest, overrefine, refine) in a fixed list; the message "<param> is not supported on range facets"; both as the body says.
- TestJsonFacetErrors has 5 test methods (5 of 5); TestJsonRangeFacets has 10 test methods (10 of 10). The premise run (receipt, 2026-10-10: head 5 of 5 with 0 failures; base-reverted leg 5 tests with 1 failure, testRangeFacetsErrorCases) matches the body.
- Upgrade note and Limits match the code. The changelog file exists at the head.

Drift items:

1. Changelog line is not a link to the head SHA.
   - Live: "Changelog: `changelog/unreleased/SOLR-18482-range-facet-unsupported-params.yml`"
   - Replacement: "Changelog: [changelog/unreleased/SOLR-18482-range-facet-unsupported-params.yml](https://github.com/nick-boss-tech/solr/blob/49ca9099d8ebc2b43fb4482791fef736cc036d7a/changelog/unreleased/SOLR-18482-range-facet-unsupported-params.yml)"

2. The choice section does not end with the pointed question.
   - Live (last sentence of "A choice to check"): "If maintainers prefer warn-first, or holding the rejection for a major release, that change fits in this PR."
   - Replacement: "If maintainers prefer warn-first, or holding the rejection for a major release, that change fits in this PR. Was the immediate 400 the right call, or should these parameters warn first or wait for a major release?"

Record conflict (no replacement chosen):
- Live (Proof): "Verified at head 49ca9099d8e on 2026-10-06 (confirmation run)."
- Receipt SOLR-18482.md line 4 says head 49ca9099 is docs only and "no Gradle gate was run for it". Line 5 says the confirmation run (log g5009r29-confirm.log, 2026-10-06) was "at this head". The commit that is the head is dated 2026-10-05 and changes no code, so the code under test is identical to f09608f5446.
- The receipt does not say whether the 2026-10-06 run used 49ca9099d8e or the earlier head f09608f5446. The main side must decide which SHA the run covers. No replacement text was chosen.

---

## Record conflicts and gaps (summary)

- SOLR-18482 / PR 5009: which SHA the 2026-10-06 confirmation run used (receipt lines 4 and 5 conflict). Flagged; no replacement.
- SOLR-18391 / PR 5027: no gate date for the focused runs at adcda10b501 in the receipt. Gap; no replacement.
- SOLR-18129 / PR 5000: the receipt names `:solr:core:check`, the body names `-x test`. Handled inside drift item 2 of that body.

## Not verified

- The Jira ticket text (outside this slice).
- The SOLR-18178 export-attachment claim (root "/" marker in the Jira attachment).
- The mechanism sentences in SOLR-16130 and the Overseer-mode qualifier in SOLR-18391 choice 2 (not resolved against base code in depth).
- Windows result details in SOLR-18178 (taken from the receipt, not re-run).

## Method notes

- Live reads only: `research\gh.ps1 pr view` (JSON fields above), `pr checks 5027`, `api repos/apache/solr/actions/runs/<id>` (head_sha of three runs), `api .../issues/<n>/comments` and `.../pulls/<n>/reviews` for 4968 and 4997 (attribution and discussion checks).
- `git ls-remote origin` for the six fork branches; source checkout reads (cat-file, show, grep, diff, diff --numstat, merge-base, log, rev-list) for head contents and ancestry.
- Two scratch extracts of CreateCollectionCmd.java (5027 head and base) were written to the session scratchpad, not the repository.
- No GitHub write, no build or test, no gate, no claim or branch edit, no commit or push.
