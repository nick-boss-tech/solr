# Review confidence round 2, slice B: live body consistency (update-processing)

Scope: the 28 update-processing PR bodies (PRs #5069 to #5096, one per ticket in pr-drafts/update-processing/). Read-only review on the Windows host. No builds, tests, gates, or test-queue runs. No edits to drafts, live bodies, PRs, comments, claims, or submit branches.

Totals: CONSISTENT 24, DRIFT 4, UNREAD 0.

## Method

- PR-to-ticket map from reports/open-update-29-prs.md. Each PR was read with a read-only `gh pr view` (number, title, state, headRefName, headRefOid, body). Every headRefName is solr-<ticket>-submit.
- Live heads: headRefOid equals `git ls-remote origin refs/heads/solr-<ticket>-submit` for all 28.
- Receipts: `git show origin/pr-prepare:receipts/<TICKET>.md`.
- Bodies against drafts: every live body is byte-identical to pr-drafts/update-processing/<TICKET>.md after CRLF normalization, so the checks below are bodies against the branch and the receipt.
- Branch facts: `git diff --stat` against the merge-base with upstream/main, every blob link in each body read with `git show <head>:<path>`, changelog fragments read at head.
- Object access: three heads were absent from the local object store (SOLR-5887 fa60337f88b4, SOLR-6045 04208ba7f0b4, SOLR-13943 ca443f6f6806). They were fetched by SHA with `git fetch --no-write-fetch-head --no-tags`. Objects only; no refs or remote state changed. Two `git merge-tree --write-tree` checks wrote tree objects only.
- Scratch scripts and outputs live in the session scratchpad, not in the repo.

## Results

Legend: CONSISTENT = every claim, link, and number matches the live head and the receipt (notes in the last section do not change this). DRIFT = at least one claim, link, or number does not match. UNREAD = body not readable.

| Ticket | PR | Live head | Body head | Result |
|---|---|---|---|---|
| SOLR-3657 | #5069 | 14edaca577c0 | 14edaca577c0 | CONSISTENT |
| SOLR-4841 | #5070 (MERGED) | bda9f9d640d9 | bda9f9d640d9 | CONSISTENT |
| SOLR-5065 | #5071 | ab894a996c8a | ab894a996c8a | CONSISTENT |
| SOLR-5505 | #5072 | 44c444aa5cd3 | 44c444aa5cd3 | CONSISTENT |
| SOLR-5754 | #5073 | 46b919e2d4e8 | 46b919e2d4e8 | CONSISTENT |
| SOLR-5887 | #5074 | fa60337f88b4 | c4c57ef7bcbd | DRIFT |
| SOLR-5939 | #5075 | f8d4bdbea518 | f8d4bdbea518 | CONSISTENT |
| SOLR-5941 | #5076 | a4df7bfd214b | a4df7bfd214b | CONSISTENT |
| SOLR-6045 | #5077 | 04208ba7f0b4 | 04208ba7f0b4 | CONSISTENT |
| SOLR-6065 | #5078 | 3d2cec9e1ab3 | 3d2cec9e1ab3 | CONSISTENT |
| SOLR-6973 | #5079 | 51fcd05e695a | 51fcd05e695a | CONSISTENT |
| SOLR-7022 | #5080 | db357868610b | db357868610b | CONSISTENT |
| SOLR-7504 | #5081 | 2fe06bfd917f | 2fe06bfd917f | CONSISTENT |
| SOLR-11475 | #5082 | 229947201fd7 | 229947201fd7 | CONSISTENT |
| SOLR-11483 | #5083 | 4431a250f665 | 4431a250f665 | CONSISTENT |
| SOLR-12245 | #5084 | f325d5d0576e | f325d5d0576e | CONSISTENT |
| SOLR-12703 | #5085 | bdd29ba19900 | bdd29ba19900 | CONSISTENT |
| SOLR-12864 | #5086 | b9c6c1e71ffa | b9c6c1e71ffa | CONSISTENT |
| SOLR-13265 | #5087 | c134b34aa27f | c134b34aa27f | CONSISTENT |
| SOLR-13696 | #5088 | da4fa6df1178 | da4fa6df1178 | CONSISTENT |
| SOLR-13943 | #5089 | ca443f6f6806 | cc155cf68e1d | DRIFT |
| SOLR-14262 | #5090 | 1e8d2b0075d7 | 1e8d2b0075d7 | CONSISTENT |
| SOLR-14718 | #5091 | 29c09959791a | 29c09959791a | CONSISTENT |
| SOLR-16356 | #5092 | 39c0585072f0 | 39c0585072f0 | CONSISTENT |
| SOLR-16655 | #5093 | 5e2317443f41 | 5e2317443f41 | CONSISTENT |
| SOLR-16673 | #5094 | d7170b12f312 | d7170b12f312 | DRIFT |
| SOLR-16910 | #5095 | 9fce3e9a7058 | 9fce3e9a7058 | CONSISTENT |
| SOLR-12705 | #5096 | aa56b7b1be4c | aa56b7b1be4c | DRIFT (minor) |

"Body head" is the commit the body's own blob links cite as the branch head. Cross-branch and base links are listed under notes.

## Drift details

### SOLR-5887 (#5074): DRIFT, three items

1. Body head is stale. The body's blob links (What happens today, What this change does, Proof) point at c4c57ef7bcbd. The live head is fa60337f88b4. The only commit between them is fa60337f88b ("update RootFieldTest and JsonLoaderTest"), which changes two test files and no production code. Every cited production line still matches at the live head, so the fix is the link SHA: the links should point at fa60337f88b4.
2. Proof date and head are not in the receipt. Body (Proof): "DocumentBuilderTest passes 17 of 17 (verified 2026-10-07 at this head)". Body (Limits): "It passed 17 of 17 at this head on 2026-10-07." The receipt records DocumentBuilderTest 17 at fa60337f88b4 in the re-gate that finished 2026-10-10 (with RootFieldTest 2 and JsonLoaderTest 31, 50 total, 0 failures). It has no 2026-10-07 entry for this ticket, and that run predates fa60337f88b4.
3. The fix's test-file edits are not disclosed. Branch facts at head: RootFieldTest.testUpdateWithChildDocs now expects "core collection1: Unable to index docs with children: ..." (RootFieldTest.java, the expected-message assertion); JsonLoaderTest.testAddBigIntegerValueToTrieField now uses hasNumberFormatExceptionCause, which walks the cause chain (JsonLoaderTest.java, two assertion sites, plus the new helper). The receipt records both methods re-run green at the head. The body's scope and Limits say nothing about these edits. The Limits list names three other classes and says none was run; those three are not in the diff.

Suggested main-agent action (not applied): link to fa60337f88b4; cite the receipt's counts at that head with its date; add one sentence that the fix updates two existing test expectations and that the touched methods were re-run.

### SOLR-13943 (#5089): DRIFT, three items

1. Changelog statement is false. Body: "Changelog: this branch adds no changelog fragment." At the live head, changelog/unreleased/SOLR-13943.yml exists (7 lines, type: fixed, added by commit ca443f6f680 "restore changelog entry").
2. Scope statement is incomplete. Body: "The commits added on top of SOLR-13696 change two test files only, and no production code." At the live head the commits above 1d0b8a0a73cd change three files: changelog/unreleased/SOLR-13943.yml, TimeRoutedAliasDateMathInStartTest.java (new, 176 lines), and TimeRoutedAliasUpdateProcessorTest.java (92 lines removed). "No production code" is still true.
3. Body head is stale. Links and Proof counts sit at cc155cf68e1d (the receipt's gated head). The live head ca443f6f6806 differs from it only by the changelog file, so the counts still describe the live tree, but the links and the "runs at this head" wording should name ca443f6f6806.

Verified and unchanged: the stack claim (1d0b8a0a73cd is an ancestor of the live head; da4fa6df117, the SOLR-13696 changelog commit, is not, and touches a different changelog file). The moved test has no skip annotation, polls the provider every 100 ms for up to 30 s (TimeOut 30 s at L135, sleep 100 at L158, fail at timeout L151), and the first wait (L109) is before the add. The receipt counts (1/1, 6/6, 2/2, 13/13, and 6 tests with testPreemptiveCreation the only failure under the awaitsfix group) match the body.

Suggested main-agent action (not applied): changelog line "changelog/unreleased/SOLR-13943.yml (type: fixed)"; scope "two test files and one changelog fragment"; links to ca443f6f6806.

### SOLR-16673 (#5094): DRIFT, Proof numbers not in the receipt

- Body (Proof): "At the earlier head d5c19e64ba1b, the changelog parses, tidy passes, the Error Prone compile passes, and the module checks pass. ParsingFieldUpdateProcessorsTest passes 43 of 43. The new test fails without the fix."
- Receipt (origin/pr-prepare:receipts/SOLR-16673.md) records the gated head d7170b12f312, the date 2026-10-09, and a top-up that the one commit past d5c19e64ba1b changes only the changelog title. It records no focused-test count (no 43), no tidy or Error Prone result, and no pre-fix proof result.
- Branch facts: d5c19e64ba1b..d7170b12f312 changes one line in changelog/unreleased/SOLR-16673.yml, as the receipt says. The code claims and links at the head check out (Int L93, Long L104, Float L96, Double L107, Int L101 number.intValue(), DefaultSchemaSuggester L300 and L338, test L499-514).

Suggested main-agent action (not applied): either refresh the receipt with the focused count and the pre-fix result from the gate log, or remove the 43/43 and pre-fix sentences from the body.

### SOLR-12705 (#5096): DRIFT, minor wording

- Body (Limits): "SOLR-7504 also conflicts with this change, once, in one import block of FieldMutatingUpdateProcessorTest.java."
- Branch fact: a merge of the SOLR-7504 head (2fe06bfd917f) with this head conflicts in that file at two hunks, both inside the import block (java.util.HashMap / ArrayList, and java.util.Map / Locale).
- Suggested wording (not applied): "conflicts in two hunks, both in one import block". The other merge claim, against SOLR-16655 ("conflicts twice in FieldMutatingUpdateProcessor.java"), checks out: two hunks.

## Close reads

### SOLR-5887 (#5074), test-only fix fa60337f88b4

Re-checked at the live head: the wrapper (AddUpdateCommand.java L235-248: BAD_REQUEST kept, "core <name>: " prefixed, null-core requests unchanged), the three call sites (L103, L212, L231), the base message (DocumentBuilder.java L301-305), the test (DocumentBuilderTest.java L78-92), the changelog file, and the classification processor calling DocumentBuilder directly (ClassificationUpdateProcessor.java L128). All hold. The failures are the stale link SHA, the unsupported 2026-10-07 date and head, and the undisclosed test edits listed under drift.

### SOLR-13943 (#5089), changelog restored at ca443f6f6806

The changelog and scope statements do not describe the live branch exactly (see drift). Everything else in the body (stack, moved-test structure, polling, annotations, receipt counts, limits about the untested base run and the SOLR-13059 class) matches the branch.

### SOLR-11483 (#5083), removed unclosed-UpdateLog Limits bullet

CONSISTENT. Nothing in the live body depends on the removed claim. The body has no remaining text about closing, lifecycle, or open/close state. Its UpdateLog references are the eviction condition (UpdateLog.java L764-765), the default-count code (L386-393), and the reference-guide default (commits-transaction-logs.adoc L274). The Limits now hold two bullets (retention and disk cost; the key-presence trigger), and the Choice compares the 1000 and 10 defaults only. Constants verified at head: DEFAULT_MAX_NUM_LOGS_TO_KEEP 10, DEFAULT_MAX_NUM_LOGS_TO_KEEP_WITH_RECORDS 1000, numRecordsToKeep default 100 (L385). No shipped config under solr/server sets either setting (git grep returns no match). The receipt's TestRecovery 21/21 and the base failure expected:<1000> but was:<10> match the body.

## Notes (not drift)

- Before-state and cross-branch links. The PR formula asks for head-SHA links. These links point at a base or another branch's head, and their content matches the claim: SOLR-4841 (DetectedLanguage.java L24 at b6b2b8f1); SOLR-5941 (CommitTracker.java L268-271 at cabedd1d); SOLR-6045 (AtomicUpdateDocumentMerger.java L88-97, L157-158, L186 and AtomicUpdateProcessorFactory.java L131-137 at 97d97381); SOLR-7504 (CountFieldValuesUpdateProcessorFactory.java L70-74 at 97d97381); SOLR-12703 (AtomicUpdateDocumentMerger.java L202 at 6045's head 04208ba7f0b4); SOLR-14718 (SolrCmdDistributor.java L241-264 and L417-421 at 9b3a84b1; four SOLR-5939 links at f8d4bdbea518).
- SOLR-5941 branch also edits solr-ref-guide commits-transaction-logs.adoc (+5 lines, the autoCommit paragraph). The body does not mention it. This is an omission, not a contradiction.
- SOLR-5754 combined run. The body names 7fbe0128d8b0 as the combined-run head; the receipt names merge commit 1ddbf36202d (tree f2e33340f0ea) and does not name 7fbe0128d8b0. Recomputing the merge of the SOLR-5939 head and 7fbe0128d8b0 gives tree f2e33340f0ea, which matches the receipt. The merge commit itself is not in the local object store.
- SOLR-5754 "SOLR-5939 changes the same two files" is true as an overlap statement; SOLR-5939 changes six more files.
- SOLR-13696 pre-fix run heads (98ad9d3fcc33, a4e0da422327, 08f9384e47c0) are the parent commits of the time-route, future-date, and shard repairs in branch order, so they match the history. The receipt text does not name them; the gate logs are not in this worktree.
- SOLR-6065: the Lucene 9.12.3 message-wording claim is external and cannot be checked from the branch. The 10.4.0 pin is at gradle/libs.versions.toml L39.
- SOLR-4841: PR #5070 is MERGED on GitHub. The body matches the branch at bda9f9d640d9 (changelog type changed, per the receipt's top-up). The receipt's first line still names e4c878627108, which the top-up entry supersedes.
- SOLR-7022: the post-gate history has a third commit, 6a233ab2fdb, which adds DirectUpdateHandler2CommitWaitTest (162 lines, not run). The body's "the two last commits change wording only" is accurate, and Limits names the class.
- SOLR-11475: the receipt head 0de48e492fd4 and the live head 229947201fd7 have the same tree (3223028c6f4e), so the gate evidence carries over.
- SOLR-7504: the test chain "count" holds only the counter, so "the tests call the counter directly" is accurate.

## UNREAD

None. All 28 live bodies were read through the read-only wrapper.
