# Review round 2, slice A2: draft verification against the adopted answers

Scope: the 28 drafts in `pr-drafts/core-admin/` (17), `pr-drafts/suggester/` (8), and `pr-drafts/highlighting/` (3), read on branch `pr-prepare-suggester` (worktree `wt/pr-prepare-suggester`). Adopted answers: `material/core-admin-round-1-answers.md`, `material/suggester-round-4-decisions.md`, `material/suggester-round-4-answers.md`, `material/highlighting-round-1-answers.md`.

Method: live tips read with `git ls-remote origin refs/heads/solr-<ticket>-submit` at 2026-10-10 19:00 UTC. Every draft's cited head equals its live tip, so no draft is written against a moved branch. Proof numbers were checked against `receipts/<TICKET>.md` for the 20 tickets that have a receipt file. The eight suggester tickets have no receipt file (see S1), so their numbers were checked against the adopted answers instead. Citations were checked with `git show <sha>:<path>` and `sed`. No builds, tests, gates, fetches that change state, or remote writes were made.

Totals: PASS 8, FIXED 6, FLAGGED 14 (FLAGGED drafts can also carry edits; those are listed in the edit section).

## Verdicts

| Draft file | Verdict | Notes |
|---|---|---|
| pr-drafts/core-admin/SOLR-4989.md | PASS | Head 5bac95376cd4 = live tip = receipt head. 10 of 10 and the failing new test match the receipt. Test names, changelog file, and citations (L215-218, L235-241, L412-415) verified. Limits opener present. The "2018 comment" is ticket text and was not checked (S4). |
| pr-drafts/core-admin/SOLR-6438.md | PASS | Head 8c77988d5917 = tip = receipt head. MergeIndexesTest 4 of 4, CoreMergeIndexesAdminHandlerTest 1 of 1, CoreAdminOperationTest 44 of 44, and the base 400-vs-500 failure match the receipt. Citations verified (MergeIndexes L107-116, MergeIndexesOp L39-44). Ref guide does present the two sources as alternatives. INTERNAL block and Limits opener already removed or added. |
| pr-drafts/core-admin/SOLR-8275.md | PASS | Head e52e10fa50a3 = tip = receipt head. TestPrepRecovery 3 of 3 and the single base failure match the receipt. Base error code SERVER_ERROR (base L208-209) matches the draft's "error code does not change" claim. Citations PrepRecoveryOp L85, L119-123, L166-172, L224-237 verified. |
| pr-drafts/core-admin/SOLR-8576.md | FLAGGED | Head 4c46f95c7851 = tip = receipt head. 25 tests, 1 skipped, 0 failures match the receipt. The sentence that the alias is checked as unchanged does not match the branch code (F8). The OWED hold line stays until FIX 1 lands. |
| pr-drafts/core-admin/SOLR-9750.md | FIXED | Head f97da6da14aa = tip = receipt head. 2 of 2, 8 and 1 (11 total) match the receipt. Citations ImplicitPlugins.json L97 and guide L471 verified at tip; base L70, L86, L97, L471 verified at 14c7aac0. Choice (rename vs one-release fallback) matches the answers. Base links corrected (E7). The branch changelog does not yet state the upgrade step; the answers own that at packaging. |
| pr-drafts/core-admin/SOLR-11939.md | PASS | Head d4cff5e76430 = tip. Docs only; no gate owed per the answers and the receipt (NO GATE). Note text at collection-management.adoc L264-L265 verified, and the code citations (CreateCollectionCmd L306-311, Assign L182-192, CreateReplica L128 and L157-158, AddReplicaCmd L355-357) verified. ADDREPLICA Limits line matches the answers. The branch still has the root SOLR-11939-TESTING.md, which the answers say to remove before opening; that moves the head (S6). |
| pr-drafts/core-admin/SOLR-12007.md | PASS | Head bdeba582fd63 = tip = receipt head. SolrCoreCleanupOnCloseTest 1 of 1, SolrCoreTest 9 of 9, DirectoryFactoryTest 3 of 3, TestCoreContainer 25 tests with 3 skipped match the receipt. Citations SolrCore L1840-1841, L1871, L3497-3498, L3501-3525, L3519-3524 and test L104-151 verified. Choice poses the synchronous route against the background route, as the answers' DISCUSS list records. Limits opener present. Three commits carry co-author trailers (S2). |
| pr-drafts/core-admin/SOLR-13246.md | FIXED | Head 6817c6c0c267 = tip = receipt head. Class 4 of 4 matches. Citations QuerySenderListener L50-52, SolrIndexSearcher L562-568, SolrCore L2823, test L60-78 verified; base L48 verified. Limits opener present. Base link corrected (E7). |
| pr-drafts/core-admin/SOLR-15024.md | FLAGGED | Head f95b5010b3fe = tip = receipt head. 9 of 9 matches (9 test methods in the class). Citations LukeRequestHandler L1008-1019, L1030-1038, base L1008-1017, test L256-284, schema.js L648-670 verified. Choice and Limits openers present. Ticket-text hold open (F9). Base link corrected (E7). |
| pr-drafts/core-admin/SOLR-15805.md | FIXED | Head 3432f950f0ae = tip = receipt head. CoreContainerProviderTest 1 of 1 matches. Citations CoreContainerProvider head L186-194, base L186-193 and checkReady base L100-110, test L44-62 verified. Choice (fail context vs 503) and the "accepts any RuntimeException" Limits line match the answers. Base links corrected (E7). The head includes commit b2a463cf64f with a co-author trailer, an existing DISCUSS item (S2). |
| pr-drafts/core-admin/SOLR-16725.md | FLAGGED | Head be1838ef8ccf = tip = receipt head. 7 tests, 1 skipped, 0 failures match the receipt. Citations ClusterStatus L341-353 and L377 verified. Mechanism sentence corrected (E8). The generalization "which most collections use" has no source (F10). |
| pr-drafts/core-admin/SOLR-16849.md | FLAGGED | Head d612b055da20 = tip = receipt head. SegmentsInfoRequestHandlerTest 7 of 7 matches. Test L156-170 verified. f2aaf8769fd (SOLR-18083) is in the base, and base L156 and L260 show the post-fix calls the draft describes. The DISCUSS call (open the PR or close the ticket as fixed by SOLR-18083) is not posed (F11). Base links corrected (E7). |
| pr-drafts/core-admin/SOLR-17297.md | PASS | Head c0ab38fc0a8b = tip = receipt head. TestCoreContainer 26 tests, 3 skipped, 0 failures match the receipt (the class has 20 "test" methods and 6 @Test assert-named methods). Choice (widen vs ship narrow) posed as the answers record. Proof placeholders replaced with reworded lines, as the answers allow. Citations NodeConfig L228-229, SolrResourceLoader L263-281 and L270-271, test L466-506 verified. Limits opener present; duplicate changelog line removed. |
| pr-drafts/core-admin/SOLR-17377.md | FIXED | Head 22b5f209a11a = tip = receipt head. 41 focused tests (35, 3, 3) match the receipt. Former-check link corrected (E9). NodeConfig L229-231, L240-256, test L577-615 verified. No Choice, per the answers. Error messages unchanged claim verified against base L689-693. |
| pr-drafts/core-admin/SOLR-17708.md | FLAGGED | Head 10a7fa07a79a = tip = receipt head. 2 of 2, 1 of 1, 9 of 9 match. Citations V2HttpCall L166 and L228-232, HttpSolrCall L480-482 (shouldAuthorize private at base, protected at head), SolrRequestAuthorizer L54, test L47-86 verified. Hold: branch changelog title still overstates (F12). |
| pr-drafts/core-admin/SOLR-17731.md | FLAGGED | Head f2b4ba164f56 = tip = receipt head. V2ResourcePathOverlapTest 2 of 2 matches. Citations CollectionSnapshotApis L37-40 and L58, DeleteAliasApi L28, GetAliasByNameApi L33, CollectionsHandler L1207 verified. Test behavior described (list and delete of the snapshot; alias wire key "name") matches the test body. Hold: fixes and a ListAliasesAPITest run are owed (F13). |
| pr-drafts/core-admin/SOLR-18010.md | FLAGGED | Head c3685bb37d9d = tip = receipt head. SecurityConfHandlerTest 3 of 3, BasicAuthStandaloneTest 1 of 1, V2SecurityAPIMappingTest 5 of 5 match the receipt. Citations verified (SecurityConfHandlerLocal L44-66 and L103-147, tests L203-262 and L271-316, BasicAuthStandaloneTest L107-124, CoreContainer L851, security.js L1248-1283 and SecurityConfHandlerLocal L86-87 at base). The answers hold this ticket with no draft (F14a). One claim is broader than the draft's own Limits (F14b). |
| pr-drafts/suggester/SOLR-9227.md | FIXED | Head 3c23fc5cfa6c = tip. All seven citations verified at the head and base (SolrSuggester L174-182 and L186-197; SuggestComponentContextFilterQueryTest L111-129, L124, L403-405; base L185-186 and L189). Matches decision 1: no Choice owed, store path in Limits, WARN and IOException lines. Proof numbers (11 tests, 0 failures, 1 skip; premise 1 failure) match the adopted answers; no receipt file (S1). "Gate" and "receipt" wording edited (E1). |
| pr-drafts/suggester/SOLR-9637.md | FLAGGED | Head c50fa4ffd932 = tip. Citations verified (SuggestComponent L274-287, L369-383, L389-397; tests L62-72 and L74-79; base L368-377). Stacking and Limits match decision 8. Per-class counts and the 2026-10-05 date are not in the record (F1). "check step" wording edited (E2). |
| pr-drafts/suggester/SOLR-9968.md | FLAGGED | Head c31d2ff1a3fd = tip. Citations verified (base SolrSuggester L111 and L267-268; head L119-127, L269-287, L283-285; tests L136-152, L155-171, L467-469; suggester.adoc L933-944). Choice matches the round-27 candidate. Keyword-test body unchanged since b1865b605184; code and tests unchanged since d688e1efdf2. The top-up gate is recorded as running, not passed (F2). |
| pr-drafts/suggester/SOLR-10937.md | PASS | Head 7d0cd11bafdc = tip. Docs only, so no test numbers. NOTE at suggester.adoc L204-207 sits under "Lookup Implementations" (L199) and names the five lookups that branch commit 363dd367b6e names. Limits offer matches decision 3. The bytecode check has no lane report in material/; accepted because decision 3 is adopted unconditionally and the branch matches it. |
| pr-drafts/suggester/SOLR-11844.md | FLAGGED | Head cbd08c20e9a4 = tip. Citations verified (base L399-400; head L402-L406). Decision 4 is conditional on the Lucene weight-0 check, and the check's result is not recorded (F3). |
| pr-drafts/suggester/SOLR-14171.md | FLAGGED | Head faa262eedc55 = tip. Code citations verified (SuggestComponent L270; SolrSuggester L93, L108-110, L244-247; factories L100-103 and Boolean.getBoolean; tests L381-410 and L412-414). Choice and Limits match decision 5. Pre-fix result and gate counts are not recorded (first gate running). Lucene constant checked on 10.4.0 only (F4). |
| pr-drafts/suggester/SOLR-17215.md | FLAGGED | Head 04d35df186dd = tip. Citations verified (SolrSuggester L230-237 and L240-248; TestFreeTextSuggesterNotBuilt L32-57; suggester.adoc L199-201; solrj AlreadyClosedException extends IllegalStateException). Choice (503 vs 500) and the narrowed catch match decision 6. Gate, counts, and premise run are not recorded; Lucene claims not checked on 9.x (F5). |
| pr-drafts/suggester/SOLR-17393.md | FLAGGED | Head 626241e647e5 = tip. Citations verified (L78-82, L273, L352, L368-378; tests L55 and L62; base L346-378). Limits match decision 7 (merge-only, per-shard follow-up, payload scope, memory bound). Pre-fix result and gate counts are not recorded (baseline running) (F6). |
| pr-drafts/highlighting/SOLR-2681.md | PASS | Head a3b1ea7994d9 = tip = receipt head. HighlighterTest 36 of 36 and 1 base failure match the receipt. Recount done per the answers. Citations DefaultSolrHighlighter L300-303, test L91-109 verified; base has no FunctionQuery case. Changelog title matches the narrowed claim. Planned-submission Limits line matches the answers. |
| pr-drafts/highlighting/SOLR-3704.md | FIXED | Head de63d4e5d5d1 = tip = receipt head. HighlighterTest 36 of 36, LukeRequestHandlerTest 8 of 8, TestPointFields 104 of 104, and the base failure text match the receipt. All twelve citations verified. Internal hold comment removed and a bold line reworded (E3, E4). |
| pr-drafts/highlighting/SOLR-4540.md | FLAGGED | Head 180b6e8a7d3c = tip = receipt head. Citations DefaultSolrHighlighter L415-421, L636-641, test L94-123 verified. The GitHub run is stated at this head, but the receipt says it ran at an earlier head (F7). The Proof does not state the passing result at the head. Internal hold comment and run identifiers removed (E5, E6). |

## Edits made

Each edit changes wording or a citation only. No claim, Proof number, Choice, Limits line, or title was changed.

E1. `pr-drafts/suggester/SOLR-9227.md`. Old: "- Gate at head `3c23fc5cfa6`, receipt dated 2026-10-06: the class runs". New: "- Test run at head `3c23fc5cfa6`, dated 2026-10-06: the class runs". Why: "Gate" and "receipt" are internal vocabulary under pr-formula.md. Head and date unchanged.

E2. `pr-drafts/suggester/SOLR-9637.md`. Old: "The check step exits 0." New: "The module check passes." Why: "check step" is an internal label for a proof step. The claim (passes) is the same.

E3. `pr-drafts/highlighting/SOLR-3704.md`. Old: first line "<!-- Written against branch head de63d4e5d5d1ddde0da6a100a631e254c078bd55. Remove before posting. -->". New: line deleted. Why: internal note in the PR text. The head is stated nowhere else as a note.

E4. `pr-drafts/highlighting/SOLR-3704.md`. Old: "**Covered: stored date fields. Not covered: docValues-only date fields and date unique keys.**" New: "**Tested: stored date fields. Not tested: docValues-only date fields and date unique keys.**" Why: "Covered" reads as "fixed or not fixed" beside "What this change does", which says the fix reaches docValues fields. The bullets below say what is tested. Wording only.

E5. `pr-drafts/highlighting/SOLR-4540.md`. Old: first line "<!-- Written against branch head 180b6e8a7d3c23ac308a28d547f61816b5815f04. Remove before posting. -->". New: line deleted. Why: same as E3.

E6. `pr-drafts/highlighting/SOLR-4540.md`. Old: " (premise run, seed 4540C0FFEE4540, log g4540-premise.log)." New: ".". Why: run identifiers (seed, log name) and an internal label ("premise run") are out of the PR text. The claim that the test fails at line 106 on the base code is unchanged.

E7. Base-code links changed from `github.com/apache/solr/blob/<sha>/` to `github.com/nick-boss-tech/solr/blob/<sha>/`. SHA, path, and line range unchanged. Why: every other file citation in these drafts uses the fork. Each SHA is the merge base with upstream/main or an ancestor of the branch tip, so the fork has it. Files and link counts: SOLR-9750 (4 links, base 14c7aac0d151), SOLR-13246 (1, base 97d973814336), SOLR-15024 (1, base b5c71bc5573c), SOLR-15805 (2, base b5c71bc5573c), SOLR-16849 (2, base 14c7aac0d151).

E8. `pr-drafts/core-admin/SOLR-16725.md`. Old: "A helper converts the four keys to strings before each collection's status is built (". New: "A helper converts the four keys to strings once each collection's status is built (". Why: the cited call at ClusterStatus.java L377 runs after `getCollectionStatus` at L376. "Before ... built" contradicted the citation. The output claim (strings for every collection) is unchanged.

E9. `pr-drafts/core-admin/SOLR-17377.md`. Old link (text "former check location"): `https://github.com/nick-boss-tech/solr/blob/22b5f209a11a593302b64b0e4e2c2632b540b3f7/solr/core/src/java/org/apache/solr/core/SolrXmlConfig.java#L682-L684`. New: `https://github.com/nick-boss-tech/solr/blob/56ec140e3636d5f4150fa87fbf7103529536ac99/solr/core/src/java/org/apache/solr/core/SolrXmlConfig.java#L684-L688`. Why: at the branch head, L682-684 is a comment that says the classes are no longer validated there, so the link did not show the former check. At the merge base, L684-688 is the `try` block that calls `loader.findClass` for each clusterSingleton. 56ec140e is the merge base with upstream/main and an ancestor of the tip.

## Flagged items

Each item gives the draft, the exact text, the fact it conflicts with, and where the fact is. No flagged claim was edited.

F1. SOLR-9637 (`pr-drafts/suggester/SOLR-9637.md`).
- Text: "Verified 2026-10-05 at head `c50fa4ffd93`." with "`SuggestComponentMergeTest` 4 of 4", "`SuggestComponentTest` 12 of 12", and "`DistributedSuggestComponentTest` 1 of 1".
- Fact: `material/suggester-round-4-answers.md` (SOLR-9637 item) records only that the gate "is green at c50fa4ffd93; premise grounded against the stacked base". `material/suggester-round-4-decisions.md` item 8 has no counts or date. No `receipts/SOLR-9637.md`.
- Action: main side confirms the counts and date from the gate log, or adds the receipt.

F2. SOLR-9968 (`pr-drafts/suggester/SOLR-9968.md`).
- Text: "Gate at `c31d2ff1a3f`, recorded 2026-10-09: `SuggestComponentContextFilterQueryTest` runs 13 tests, 0 failures, and 1 pre-existing skip." and "On the base code, a run at `b1865b605184` (seed `9968C0FFEE9968`) gives exactly 1 failure".
- Fact: `material/suggester-round-4-decisions.md` item 2 and `material/suggester-round-4-answers.md` item 2 record the top-up gate at c31d2ff1a3f as running (log g9968-topup-gate.log, GATE PENDING). No result is recorded. The 13-test count is recorded for d688e1efdf2 (log g9968r1-gate.log), whose production and test code is identical to c31d2ff1a3f; the draft should say that if the top-up is not in by then. The premise run's head is not recorded; the recorded green gate is at b1865b605184. No `receipts/SOLR-9968.md`.
- Wording also to fix when resolved: "Gate", "recorded", "seed", and the Limits phrase "No pre-fix run is on record".
- Action: hold until the top-up result lands.

F3. SOLR-11844 (`pr-drafts/suggester/SOLR-11844.md`).
- Text: "Checked 2026-10-09." and "The blend rules were checked against Lucene 10.4.0 and Lucene 9.12.3 bytecode".
- Fact: `material/suggester-round-4-answers.md` decision 4 is "adopted, conditional on verification"; "If the weight-0 premise does not verify, the lane reports back and the branch is reworked or retired instead of drafted." `material/suggester-round-4-decisions.md` item 4 records the docs verification lane as running. No lane report is in `material/`, and there is no `receipts/SOLR-11844.md`. Branch commit 97a97efc995 matches the claim, but a commit does not record the check.
- Action: main side confirms the lane report before the draft counts as verified.

F4. SOLR-14171 (`pr-drafts/suggester/SOLR-14171.md`).
- Text: "Pre-fix proof against base `9b3a84b1c460`: PASS, so the test fails on the base code." and "Gate at `faa262eedc5`, recorded 2026-10-09: `SuggestComponentContextFilterQueryTest` runs 11 tests, 0 failures, and 1 pre-existing skip".
- Fact: `material/suggester-round-4-answers.md` item 5 and `material/suggester-round-4-decisions.md` item 5 record the first full gate (pre-fix proof against 9b3a84b1c460, full class, Error Prone, check) as launched and running (log g14171-gate.log). No result is recorded. No `receipts/SOLR-14171.md`.
- Lucene claim: "In Lucene 10.4.0 that constant is `true`, so both sources agree today." Only 10.4.0 is cited. The cross-version rule in `claims/query-parsing-round-1.md` ("Lucene version claims") asks for the 9.x and 10.x lines.
- Wording also to fix when resolved: "PASS" as a proof label and "Gate".
- Action: hold for the gate result; add the 9.x check or drop the constant sentence.

F5. SOLR-17215 (`pr-drafts/suggester/SOLR-17215.md`).
- Text: "Verified 2026-10-09 at 04d35df186d: TestFreeTextSuggesterNotBuilt 1 of 1; SuggestComponentTest 12 of 12; SuggestComponentContextFilterQueryTest 10 tests, 1 pre-existing skip, 0 failures." and "Gate GREEN at 04d35df186d, recorded 2026-10-09". Also "With `SolrSuggester.java` reverted to `cabedd1d968`, it fails (seed `17215C0FFEE17215`)."
- Fact: `material/suggester-round-4-decisions.md` item 6 says a main-side lane is "gating the result". No gate result, count, or premise run is recorded in `material/`, and there is no `receipts/SOLR-17215.md`.
- Lucene claims: "Lucene's and Solr's `AlreadyClosedException` both extend `IllegalStateException`" and "`AnalyzingInfixSuggester`, which throws "suggester was not built"". Neither is recorded as checked on the 9.x and 10.x lines (`claims/query-parsing-round-1.md`).
- Action: main side supplies the gate record; check the Lucene claims on both lines or narrow them.

F6. SOLR-17393 (`pr-drafts/suggester/SOLR-17393.md`).
- Text: "Pre-fix proof: PASS, so the test fails on the base code. The pre-fix proof used the visibility-only shim, at seed `17393C0FFEE17393`." and "Gate at `626241e647e`, recorded 2026-10-09: `SuggestComponentMergeTest` 2 of 2, `SuggestComponentTest` 12 of 12, and `DistributedSuggestComponentTest` 1 of 1."
- Fact: `material/suggester-round-4-decisions.md` item 7 records "baseline gate running (log g17393-gate.log)". The baseline (pre-fix proof against c3cdf7b46e8, focused classes at head) has no recorded result. No `receipts/SOLR-17393.md`.
- Wording also to fix when resolved: "PASS", "visibility-only shim" (internal label), "seed", and "Gate".
- Action: hold for the baseline result; reword the proof sentence then.

F7. SOLR-4540 (`pr-drafts/highlighting/SOLR-4540.md`).
- Text: "A GitHub Actions run of `:solr:core` that covers FastVectorHighlighterTest completed successfully at this head."
- Fact: `receipts/SOLR-4540.md`: "GitHub run 37622803991 (FastVectorHighlighterTest, :solr:core) at the earlier head completed SUCCESS (2026-10-07, before the Actions suspension); the re-gate stands on the local gate alone."
- Also: the Proof does not state the passing result at the head that the receipt records ("FastVectorHighlighterTest 3 of 3 at the head"), and does not say the new test passes with the change.
- Action: main side names the earlier head or drops the sentence; add the local pass line if Nick wants the pass stated.

F8. SOLR-8576 (`pr-drafts/core-admin/SOLR-8576.md`).
- Text: "After that, it checks that the original collection and the alias are unchanged."
- Fact: at branch head 4c46f95c7851, `solr/core/src/test/org/apache/solr/cloud/CollectionsAPISolrJTest.java` L1185-L1188 checks the collection by name and asserts `!hasCollection(aliasName)`. No assertion checks that the alias still resolves. `material/core-admin-round-1-answers.md` (SOLR-8576) says the assertion checks "for a collection named like the alias, not that the alias still resolves", and the replacement (part k6, FIX 1) is owed on the branch.
- Action: the OWED hold stays. Rewrite the sentence after FIX 1 lands and `CollectionsAPISolrJTest` is run at the new head.

F9. SOLR-15024 (`pr-drafts/core-admin/SOLR-15024.md`).
- Text: "[OWED BEFORE POSTING: check that the Jira ticket describes duplicate char filter keys in the Luke output. ...]"
- Fact: `material/core-admin-round-1-answers.md` (SOLR-15024): "Owed before posting: confirm the ticket text ... No Jira packet for this ticket is on disk." This slice cannot settle it. The draft's other claims match the branch.
- Action: main side (or Nick) checks the ticket text; hold until then.

F10. SOLR-16725 (`pr-drafts/core-admin/SOLR-16725.md`).
- Text: "This matches CREATE, which most collections use, and clients already read the output that way."
- Fact: no source. `material/core-admin-round-1-answers.md` (SOLR-16725) records only the strings route and the posed numbers route. `receipts/SOLR-16725.md` records test outcomes only.
- Action: confirm or remove the generalization.

F11. SOLR-16849 (`pr-drafts/core-admin/SOLR-16849.md`).
- Text: the draft as written presents the regression-test PR as the outcome, for example "This change adds one regression test and changes no production code." It has no Choice section.
- Fact: `material/core-admin-round-1-answers.md` DISCUSS list item 4: "open the regression-test PR, or close the ticket as fixed by SOLR-18083 ... The call is not taken." A DISCUSS item must be posed as recorded, and this one is not.
- Action: Nick decides; if the PR stays, a Choice posing the open-or-close call is drafted.

F12. SOLR-17708 (`pr-drafts/core-admin/SOLR-17708.md`).
- Text: "[OWED BEFORE POSTING: the changelog title on the branch overstates the change and needs a fix first. After that edit, update the head references ...]"
- Fact: the branch changelog at 10a7fa07a79 (`changelog/unreleased/SOLR-17708-jaxrs-single-authorization.yml`) reads "v2 APIs implemented with JAX-RS are now authorized once per request instead of twice ...". `material/core-admin-round-1-answers.md` (SOLR-17708) says the admin-remote route still checks twice and the replacement title (part k2, finding 7) is not on the branch. The draft's Limits already names that route, so the body holds at the current head.
- Action: hold. The title fix moves the head, and the three test classes need a rerun there.

F13. SOLR-17731 (`pr-drafts/core-admin/SOLR-17731.md`).
- Text: "[OWED BEFORE POSTING: the branch needs its fixes first: the two license header lines, the two comments on the Jersey routing rule, and the changelog title. Then run `ListAliasesAPITest` ...]"
- Fact: `material/core-admin-round-1-answers.md` (SOLR-17731 and cross-cutting owed item 6) lists these fixes as owed. `ListAliasesAPITest` has no recorded run. At f2b4ba164f56, `solr/api/src/java/org/apache/solr/client/api/endpoint/CollectionSnapshotApis.java` L37-L39 still states the Jersey routing rule as fact.
- Action: hold. The head moves after the fixes.

F14. SOLR-18010 (`pr-drafts/core-admin/SOLR-18010.md`).
- (a) Decision conflict. `material/core-admin-round-1-answers.md` records SOLR-18010 as "held, no draft" and says no claim about concurrent `security.json` edits is drafted until "reading the settling run the receipt points to in the takeover record". `receipts/SOLR-18010.md` (re-verified 2026-10-10) now says the settling log is found and on disk. The adopted answers still say this. Main side confirms the read and lifts the hold, or holds the draft.
- (b) Text: "A reader sees the old file or the new one, never a partly written file." (What this change does, second bullet). The draft's own Limits say: "The fallback for file systems without atomic moves is a plain move, and the code does not promise that it is atomic." The code at `solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java` L130-L133 uses `ATOMIC_MOVE` with a plain-move fallback on `AtomicMoveNotSupportedException`. The sentence should be qualified; this is a claim change, so it is not edited.
- (c) Branch changelog (`changelog/unreleased/SOLR-18010-security-json-concurrent-edits.yml` at c3685bb37d9d) says the edits are "written atomically ... can no longer corrupt the file or overwrite each other". The answers (part k2, finding 12) hold a narrower title until the settling run is read.

## Systemic notes

S1. `receipts/` has no `SOLR-<ticket>.md` for the eight suggester tickets (9227, 9637, 9968, 10937, 11844, 14171, 17215, 17393). Their Proof numbers were checked against `material/suggester-round-4-answers.md` and `material/suggester-round-4-decisions.md`, which cite the main-side ledger. The main side should add the receipt files before these are treated as verified.

S2. Co-author trailers on branch heads, against the standing rule. `solr-12007-submit` (head bdeba582fd63): commits 8f44c181c5c, 4a538bc3cf1, and 50a0052c148 carry "Co-Authored-By: Claude Sonnet 5.5". The core-admin answers do not record these. `solr-15805-submit` (head 3432f950f0ae): commit b2a463cf64f carries one, already a DISCUSS item. Any rewrite moves the head, and the draft's links move with it.

S3. Shared worktree. `git status` in `wt/pr-prepare-suggester` shows edits by other slices in `pr-drafts/solrcloud/` and `pr-drafts/spellcheck/`, and untracked `13943`, `reports/review-confidence-round-2-a1.md`, and `reports/review-confidence-round-2-c.md`. This slice did not touch them. This slice's edits are confined to: `pr-drafts/suggester/SOLR-9227.md`, `SOLR-9637.md`; `pr-drafts/highlighting/SOLR-3704.md`, `SOLR-4540.md`; `pr-drafts/core-admin/SOLR-9750.md`, `SOLR-13246.md`, `SOLR-15024.md`, `SOLR-15805.md`, `SOLR-16725.md`, `SOLR-16849.md`, `SOLR-17377.md`. Nothing was committed or pushed.

S4. Ticket-text claims were not checked, because no Jira text is on this side. Examples: SOLR-4989 (2018 comment), SOLR-8275 (quoted ticket text), SOLR-9637 (reporter, Solr 4.9.1), SOLR-11844 (reporter, Solr 6.5), SOLR-17215 (reporter, Solr 8.11.2, and the 2024-12-20 comment), SOLR-16725 (ticket example), SOLR-2681 (ticket example).

S5. Receipt counts for the core-admin and highlighting drafts match the test methods in the cited classes. Two counting conventions matter: Solr test classes run both `@Test` methods and "test"-prefixed methods (TestCoreContainer: 20 "test" methods plus 6 `@Test` assert-named methods make the receipt's 26); and LocalFSCloudIncrementalBackupTest's 7 tests come from AbstractIncrementalBackupTest.

S6. Branch-level items that the answers already own and that will move heads when done: root `SOLR-11939-TESTING.md` (still present at d4cff5e76430); the SOLR-9750 changelog upgrade step (not yet in the branch changelog); the SOLR-8576 alias assertion (FIX 1); the SOLR-17731 fixes and `ListAliasesAPITest` run; the SOLR-17708 changelog title; the SOLR-18010 changelog title; the SOLR-16849 head commit wording (answers, finding 13).
