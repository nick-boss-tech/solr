# Core admin convergence, round 1

Verdict: **NOT CONVERGED.** Eighteen of the twenty-one drafts are NOT CONVERGED. Three are CONVERGED with notes only: SOLR-4989, SOLR-8275 and SOLR-11939.

Counts: drafts checked 21. Drafts with no finding 0. Drafts with a blocking finding 6 (SOLR-5011, 8576, 9750, 16725, 17708, 17731). Drafts with should-fix findings and no blocking finding 12. Drafts with notes only 3. Findings 64: 7 blocking, 28 should-fix, 29 note.

Scope: `assignments/pool-core-admin-convergence-round-1.md`, with the answers in `material/core-admin-round-1-answers.md`. Settled decisions were not reopened. Five read-only subagents covered the drafts in slices, and one more wave-two pair covered the last six. Every head was checked with `git ls-remote origin refs/heads/<branch>`. Two drafts have a head that differs from the one the draft names: SOLR-17708 is on hold, and SOLR-5011 is on hold for the main side's narrowing. The coordinator checked the receipts the blocking items cite. The drafts were not edited in this round.

## Verdict per draft

- **SOLR-4502: NOT CONVERGED.** CA-R1-01, 02 (should-fix).
- **SOLR-4989: CONVERGED.** CA-R1-03 (note).
- **SOLR-5011: NOT CONVERGED.** CA-R1-04, 05 (blocking); CA-R1-06, 07 (should-fix); CA-R1-08 (note).
- **SOLR-6438: NOT CONVERGED.** CA-R1-09 (should-fix).
- **SOLR-8275: CONVERGED.** CA-R1-10, 11 (note).
- **SOLR-8576: NOT CONVERGED.** CA-R1-21 (blocking); CA-R1-22, 23, 24, 27 (should-fix); CA-R1-25 (note).
- **SOLR-9750: NOT CONVERGED.** CA-R1-26 (blocking); CA-R1-27 (should-fix); CA-R1-28 (note).
- **SOLR-11939: CONVERGED.** CA-R1-29, 30 (note).
- **SOLR-12007: NOT CONVERGED.** CA-R1-31 (should-fix); CA-R1-32, 33 (note).
- **SOLR-12916: NOT CONVERGED.** CA-R1-34, 35 (should-fix); CA-R1-36 (note).
- **SOLR-13246: NOT CONVERGED.** CA-R1-42 (should-fix); CA-R1-41, 43 (note).
- **SOLR-15024: NOT CONVERGED.** CA-R1-44, 45, 46 (should-fix); CA-R1-47 (note).
- **SOLR-15805: NOT CONVERGED.** CA-R1-48 (should-fix); CA-R1-49 (note).
- **SOLR-16499: NOT CONVERGED.** CA-R1-50 (should-fix); CA-R1-53 (note).
- **SOLR-16725: NOT CONVERGED.** CA-R1-51 (blocking); CA-R1-52 (note).
- **SOLR-16849: NOT CONVERGED.** CA-R1-61, 62 (should-fix); CA-R1-66, 67 (note).
- **SOLR-17297: NOT CONVERGED.** CA-R1-63 (should-fix); CA-R1-68, 69 (note).
- **SOLR-17377: NOT CONVERGED.** CA-R1-64, 65 (should-fix); CA-R1-70, 71 (note).
- **SOLR-17708: NOT CONVERGED.** CA-R1-81 (blocking); CA-R1-82, 83, 84 (should-fix). HEAD HOLD: see below.
- **SOLR-17731: NOT CONVERGED.** CA-R1-85 (blocking); CA-R1-86 (should-fix); CA-R1-87, 88, 89 (note).
- **SOLR-18010: NOT CONVERGED.** CA-R1-90 (should-fix); CA-R1-91, 92, 93 (note).

## Head holds

- **SOLR-17708.** The draft names `10a7fa07a79`. The live tip of `solr-17708-submit` is `2eceb54730373475e9adfe55bfe555744fb3724a`, and the receipt gates that tip green after a top-up that changed only the changelog title. The draft must move to the live head, which CA-R1-81 covers. Hold until then.
- **SOLR-5011.** The draft is on hold on the main side, pending a narrowing. The hold is not a finding. The register checks only that the text matches the record.

## Register

Blocking and should-fix rows carry the replacement text from the subagent reports. Note rows are summarized.

### SOLR-4502, SOLR-4989, SOLR-5011, SOLR-6438, SOLR-8275 (slice 1)

**CA-R1-01 | SOLR-4502 | should-fix.** The changelog title says the change stops "a core that later hits a NullPointerException on search." The premise run fails at create, so that state is not reached on base. Replacement: "CoreContainer.create now fails with a message that names load() when the container has not been loaded, instead of a NullPointerException message."

**CA-R1-02 | SOLR-4502 | should-fix.** Line 21 "The core module check, without tests, also passed." is a gate step name. Replacement: "The core module also builds and passes its checks with the tests skipped."

**CA-R1-03 | SOLR-4989 | note.** The link at line 9 reaches `L235-L238` at the merge-base; lines 235 and 236 are the schema branch. Optional: cite `L237-L238`.

**CA-R1-04 | SOLR-5011 | blocking.** Limits bullet 1 (line 34) says "...so the result is unknown." The record's verdict is UNSAFE by reading (`reports/solr-5011-shared-schema-analysis.md`). Replacement: "- Shared schema: two cores built on one config set, with shareSchema on. If the core that built the cached schema unloads while the other core stays open, a schema add-field-type on the open core that names a lib class not yet loaded fails, because the schema still uses the closed loader. A read of the code finds this case unsafe. No test in this change runs it."

**CA-R1-05 | SOLR-5011 | blocking.** Line 19 says "Once the core closes, its jars are released, so the closed loader should not be used for later class loading." The record shows the closed loader is used later through the surviving core's shared schema. Replacement: "Once the core closes, its jars are released, and its class loader can no longer load lib classes it has not loaded yet. A second core that shares this core's cached schema can still reach that loader, as the first Limits item describes."

**CA-R1-06 | SOLR-5011 | should-fix.** The bold Limits line (32) says "one shared-config case is still open." Replacement: "**Not every close path is tested, and one shared-schema case is unsafe by a read of the code and has no test.**"

**CA-R1-07 | SOLR-5011 | should-fix.** Line 28 "The module check and the Error Prone compile also pass there." is gate step names. Replacement: "The core module also compiles with Error Prone and passes its checks with the tests skipped, at this head."

**CA-R1-08 | SOLR-5011 | note.** The changelog title names the jars released; recheck it when the close is narrowed.

**CA-R1-09 | SOLR-6438 | should-fix.** Line 9 "The request does not fail." conflicts with the draft's own base run, which returned a 500 for its inputs. Replacement: "No error reports that the named cores were ignored."

**CA-R1-10 | SOLR-8275 | note.** The quoted ticket text is not in this tree. Optional: append "The quoted text is not in this tree."

**CA-R1-11 | SOLR-8275 | note.** The default text "no collection state" is not named in Limits. Optional: add a sentence saying the text appears when no collection state arrives during the wait, which the new test does not reach.

### SOLR-8576, SOLR-9750, SOLR-11939, SOLR-12007, SOLR-12916 (slice 2)

**CA-R1-21 | SOLR-8576 | blocking.** Line 13 "checks that the rejected creates change nothing" and line 15 "checks that the original collection and the alias are unchanged". The assertion at head line 1188 checks only that no collection has the alias name, not that the alias is unchanged. Replacement for line 13: "...checks that the rejected creates leave the original collection in place and add no collection named like the alias." Replacement for line 15's last sentence: "After that, it checks that the original collection is unchanged and that no collection has taken the alias name." After the alias fix lands: "...that the original collection is unchanged and that the alias still resolves to it."

**CA-R1-22 | SOLR-8576 | should-fix.** Line 7 "...but no test covers that." Base has no test with either message, but `ReindexCollectionTest` checks "already exists" for a reindex target, and `CollectionsAPISolrJTest` checks a rename. Replacement: "**Creating a collection with a name that a collection or an alias already has is rejected, but no test checks the error message for that.**"

**CA-R1-23 | SOLR-8576 | should-fix.** The link at line 9 to `CreateCollectionCmd.java` L127 to 135 is pre-change code at the merge-base, and the text does not say so. Add: "The link points at the base commit, `b6b2b8f10e9827e3b86e44649fb7966ce646c185`, which is the merge-base."

**CA-R1-24 | SOLR-8576 | should-fix.** Line 24 "[OWED BEFORE POSTING: ...]" is internal process wording. Delete line 24, and update the head and counts after the fix run.

**CA-R1-25 | SOLR-8576 | note.** "It pins error behavior" is jargon, and the link at line 15 cites test lines. Optional: "It checks error behavior that already exists."

**CA-R1-26 | SOLR-9750 | blocking.** Line 7 "does not follow the naming rule of the other implicit handlers" and line 9 "each implicit handler names its paramset after its endpoint" are not true of the base. `ImplicitPlugins.json` names `/admin/info` `_ADMIN_CORE`, `/tasks/cancel` `_TASK_CANCELLATION` and `/tasks/list` `_LIST_TASKS`. Replacement for line 7: "**The implicit `/graph` handler reads its defaults from a paramset named `_ADMIN_GRAPH`, which does not match its endpoint.**" Replacement for line 9's first sentence: "Most implicit handlers name their paramset after the endpoint: the path is upper-cased and slashes become underscores. A few do not, such as `/admin/info`, which uses `_ADMIN_CORE` (linked at the merge-base)." Keep the other examples and say the links point at the base commit.

**CA-R1-27 | SOLR-8576, SOLR-9750 | should-fix.** The "verified" dates at line 21 are takeover-log record dates, not run dates. Replacement: 8576 "(recorded 2026-10-07)"; 9750 "recorded 2026-10-05". Keep "verified" only if Nick confirms the run dates.

**CA-R1-28 | SOLR-9750 | note.** "What this change does" omits the new test. Optional: "It also adds a test that reads `ImplicitPlugins.json` and checks the value."

**CA-R1-29 | SOLR-11939 | note.** Line 9 gives a reason for the name builder that the code does not show. Optional: drop the clause and cite line 366 at the merge-base. Line 21: "I did not build the guide."

**CA-R1-30 | SOLR-11939 | note.** The head still carries a root testing file that the answers owe removing before opening. At packaging, update the head and the changelog link.

**CA-R1-31 | SOLR-12007 | should-fix.** Line 9 links to `14c7aac` lines 3502 to 3517 and 1870 at the merge-base without saying so. Add: "Both links point at the base commit, `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`, which is the merge-base."

**CA-R1-32 | SOLR-12007 | note.** The reload path and core creation both run the cleanup. Replacement: "Reload and core creation still run the cleanup on a background thread." Range "3501 to 3528".

**CA-R1-33 | SOLR-12007 | note.** About 3,900 characters. The close-lasts-as-long-as-cleanup point appears in both Choice and Limits. Optional trim.

**CA-R1-34 | SOLR-12916 | should-fix.** The fragment at `50b1f4bc` names only the Config API, and the draft's Changelog link resolves to that title. Branch edit, title: "QuerySenderListener accepts warming queries given as flat name and value lists, from the Config API and from XML, instead of ignoring them." Then move the Changelog link to the new head.

**CA-R1-35 | SOLR-12916 | should-fix.** Line 15 links "lines 108 to 110" to `#L107-L126`, which does not match, and "lines 118 and 119" have no link. Replacement: link "lines 108 to 110" to `QuerySenderListener.java#L108-L110` and "lines 118 and 119" to `#L118-L119` at head `50b1f4bc`.

**CA-R1-36 | SOLR-12916 | note.** Branch history: a handoff commit stays in history after packaging. The answers' no-rewrite item does not name this ticket. Decide before the PR commit list is shown.

### SOLR-13246, SOLR-15024, SOLR-15805, SOLR-16499, SOLR-16725 (slice 3)

**CA-R1-41 | SOLR-13246 | note.** A record premise: `SolrCore.java` line 2823 is inside a comment block at head and prints nothing. The draft's generic Limits line is right. Correct the record, not the draft.

**CA-R1-42 | SOLR-13246 | should-fix.** "Before this change, QuerySenderListener.newSearcher logged the searcher object itself." The link is to the merge-base, and the text does not say so. Replacement: "Before this change, at the merge-base commit `97d973814336`, [`QuerySenderListener.newSearcher`](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/core/QuerySenderListener.java#L48) logged the searcher object itself."

**CA-R1-43 | SOLR-13246 | note.** The searcher name format and "tens of kilobytes on a large index" are not in the packet. Optional: use the packet's "more than 20 KB for one message".

**CA-R1-44 | SOLR-15024 | should-fix.** Proof: "checks for an array of two entries with the expected class names and patterns." The test asserts the class name only for entry 1. Replacement: "checks for an array of two entries, the expected class name of the first entry, the expected pattern of each entry, and their order in the JSON output."

**CA-R1-45 | SOLR-15024 | should-fix.** Pre-change link to `getAnalyzerInfo` does not name the merge-base. Replacement: "Before this change, at the merge-base commit `b5c71bc5573c`, in [`getAnalyzerInfo`](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/handler/admin/LukeRequestHandler.java#L1008-L1017), the char filters went into a map keyed by simple class name."

**CA-R1-46 | SOLR-15024 | should-fix.** The Limits links to token filter lines 1030 to 1038 and to `schema.js` lines 648 to 670 are at the head, but neither file is changed by the branch. Re-point them to the merge-base. Token filter lines are 1028 to 1036 at `b5c71bc`.

**CA-R1-47 | SOLR-15024 | note.** The base failure reason is a reading. Say "by reading", or quote the base line once read. Confirm the ticket before posting.

**CA-R1-48 | SOLR-15805 | should-fix.** "What happens today" links `CoreContainerProvider` lines 186 to 193 and `checkReady` lines 100 to 110 at `b5c71bc` without naming the merge-base. Replacement: "Before this change, at the merge-base commit `b5c71bc5573c`, [`CoreContainerProvider`](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/servlet/CoreContainerProvider.java#L186-L193) caught every `Throwable` during startup."

**CA-R1-49 | SOLR-15805 | note.** "By code reading, it fails on the base code." The receipt records a pre-fix step with no failure text. If the gate log confirms the base failure, quote its first failing assertion.

**CA-R1-50 | SOLR-16499 | should-fix.** The links at head `51b7addddd45` to the reference guide and to `ReplaceNodeCmd`, `AddReplicaCmd` and `ReplicaMigrationUtils` are pre-change lines. The branch does not change those files, so re-point them to the merge-base `e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f` at the same lines, and say so once.

**CA-R1-51 | SOLR-16725 | blocking.** Choice, first bullet: "This matches CREATE, which most collections use, and clients already read the output that way." No record supports "most collections use" or any client behavior. Replacement: "- Implemented: strings. The JIRA example shows these four keys as strings for a collection made with CREATE, so this keeps that output."

**CA-R1-52 | SOLR-16725 | note.** "A client that parses the output with one type fails on the other." Optional: "A client that expects one type can fail on the other." The head will move with the pending note edits.

**CA-R1-53 | SOLR-16499 | note.** About 6,000 characters including URLs, for a multi-part change. No edit required; recheck after CA-R1-50.

### SOLR-16849, SOLR-17297, SOLR-17377 (slice 4a)

**CA-R1-61 | SOLR-16849 | should-fix.** The bold line says "The new test passes on the base code. It is a regression test, so it does not fail first." The pass comes from the fix already being on the base. Replacement for the bold line: "**The new test passes on the base code, because the base already has the SOLR-18083 fix.**" Delete the body sentence "The base already has the fix, so the new test passes there too."

**CA-R1-62 | SOLR-16849 | should-fix.** Line 9 links `GetSegmentData.java` at `14c7aac0d15` lines 156 and 260, which show the fixed calls, not the change. Replacement: add after the section 2 bold line: "SOLR-18083 (commit `f2aaf8769fd`) changed both index-writer calls in [GetSegmentData.java](https://github.com/nick-boss-tech/solr/blob/b2e1f8ed4a309531d62125319501b313ac39605b/solr/core/src/java/org/apache/solr/handler/admin/api/GetSegmentData.java#L156) and [at line 260](https://github.com/nick-boss-tech/solr/blob/b2e1f8ed4a309531d62125319501b313ac39605b/solr/core/src/java/org/apache/solr/handler/admin/api/GetSegmentData.java#L260) so that they do not fail on a read-only core. The links point to the parent of that commit, where the calls still take an index writer."

**CA-R1-63 | SOLR-17297 | should-fix.** Limits line 41 promises a separate pull request. The record names a follow-up. Replacement: "That reverse order is the named follow-up. We can open a follow-up ticket and pull request for it on request, with its own test for a module SPI when a shared lib is present."

**CA-R1-64 | SOLR-17377 | should-fix.** Line 18 duplicates the Changelog line at line 36. Delete line 18.

**CA-R1-65 | SOLR-17377 | should-fix.** Line 24 "Verified 2026-10-05." The receipt gives only a takeover-log date. Replacement: "That is 41 focused tests. Recorded 2026-10-05 at this head."

**CA-R1-66 | SOLR-16849 | note.** Optional: say that COLSTATUS sends the coreInfo flag only when asked, and that line 260 runs on every segments request.

**CA-R1-67 | SOLR-16849 | note.** Optional: "Reading the code, current main does not return that error."

**CA-R1-68 | SOLR-17297 | note.** Optional: "The Jira ticket describes this with a module enabled in solr.xml."

**CA-R1-69 | SOLR-17297 | note.** The "run on the base code" claim is supported by a pre-fix PASS line in the receipt. The observed line is still owed on the main side.

**CA-R1-70 | SOLR-17377 | note.** The draft does not say it must land after SOLR-17297. The citations are right at head and will move on rebase. The record does not require a Limit.

**CA-R1-71 | SOLR-17377 | note.** The Limits line on module classes is supported by code. No change needed.

### SOLR-17708, SOLR-17731, SOLR-18010 (slice 4b)

**CA-R1-81 | SOLR-17708 | blocking.** The Proof says "Counts at head `10a7fa07a79`, from the test run of 2026-10-08". The receipt gates `2eceb547` on 2026-10-11, and the 2026-10-08 date is not in the receipt. Replacement: "Counts at head `2eceb54730`, from the gate of 2026-10-11: JaxRsSingleAuthorizationTest 2 of 2, BasicAuthIntegrationTest 1 of 1, AuditLoggerIntegrationTest 9 of 9." Replace every `10a7fa07a79` link with `2eceb54730373475e9adfe55bfe555744fb3724a`, including the changelog link.

**CA-R1-82 | SOLR-17708 | should-fix.** The bold summary says "The JAX-RS filter is then the only check." With no matching resource the filter never runs. Replacement: "When a JAX-RS resource matches, the JAX-RS filter is then the only check."

**CA-R1-83 | SOLR-17708 | should-fix.** The Limits link to `V2HttpCall.java` L166 is pre-change code. Replacement: "[V2HttpCall.java, at the base commit](https://github.com/nick-boss-tech/solr/blob/9b3a84b1c460981eab09d8ffaef776acc4a184f8/solr/core/src/java/org/apache/solr/api/V2HttpCall.java#L166)".

**CA-R1-84 | SOLR-17708 | should-fix.** "No test covers a JAX-RS API under a core." Only the two cases were read. Replacement: "The new test covers no JAX-RS API under a core."

**CA-R1-85 | SOLR-17731 | blocking.** The Choice says "The change is local to two API declarations." The diff touches nine files, including `ListAliasesApi`, `CollectionsHandler` and a new handler class. Replacement: "Implemented: give the POST and GET endpoints the same class-level path as the DELETE endpoint beside them. The change edits the snapshot declaration, moves the alias GET into its own declaration and handler class, and registers that class."

**CA-R1-86 | SOLR-17731 | should-fix.** "On the base code, both tests fail with HTTP 405 from the other endpoint." The receipt records 405 only. The "other endpoint" attribution is the reporter's account. Replacement: "On the base code, both tests fail with HTTP 405."

**CA-R1-87 | SOLR-17731 | note.** The head and links will move when the owed fixes land. Update them then.

**CA-R1-88 | SOLR-17731 | note.** "It changes routing for every v2 API." No source checked gives that breadth. Optional: "it changes how the router orders endpoints".

**CA-R1-89 | SOLR-17731 | note.** About 3,800 characters, above the guide. Optional trim.

**CA-R1-90 | SOLR-18010 | should-fix.** "A reader sees the old file or the new one, never a partly written file." The code falls back to a plain move when an atomic move is not supported, and the draft's Limits says so. Replacement: "On a file system that allows an atomic move, a reader sees the old file or the new one, never a partly written file. On one that does not, the code makes a plain move instead."

**CA-R1-91 | SOLR-18010 | note.** The changelog title is wider than the one-process lock. Branch owner: "Standalone security.json edits are serialized and written atomically."

**CA-R1-92 | SOLR-18010 | note.** About 5,400 characters, above the guide. Optional trim.

**CA-R1-93 | SOLR-18010 | note.** The "internal version marker" sentence has no citation. Optional link to head lines 109 to 116.

## Record notes for the main side

- The SOLR-15805 branch history includes a commit carrying a Claude co-author trailer, which the answers' history-rewrite item still holds open. That blocks opening that branch, not the draft text. It is a main-side decision.
- The answers' gate states for SOLR-17708 and SOLR-18010 are superseded by the receipts. The receipts govern.
- The answers' line that packaging uses the origin tip for SOLR-16499 is superseded: the packaging commit is now the live head.
- SOLR-17708's round-35 report is absent from the record.
- SOLR-17731's ListAliasesAPITest run is owed before opening.

## Not checked

- Ticket text for drafts that quote a Jira packet. Packets were read as data where present under `research/jira-context/`.
- Gate logs that are not on disk, including the SOLR-16849, SOLR-17297 and SOLR-17377 logs.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Seven blocking and twenty-eight should-fix findings are open.
