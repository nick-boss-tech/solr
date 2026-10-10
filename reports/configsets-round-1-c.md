# Configsets round 1, part C: consistency passes and overlap check

Result: Both live heads match. PR 5015 (SOLR-13706) cannot merge as it stands (GitHub reports DIRTY). PR 4968 (SOLR-18178) merges cleanly, but its Proof text cites an older head. SOLR-6960 and SOLR-13706 have one textual conflict in SolrConfig.java in either landing order.

Read only. No commit, push, fetch, checkout, reset, merge, or stash. No builds, tests, or Gradle. No gh writes. The local conflict checks used `git merge-file` on copies in the scratchpad, so no branch, index, or worktree changed.

## Head checks

- PR 5015: headRefOid `590dd5c24d97c5c1b8f2ed262037c0a9c4ba6f77` equals `590dd5c24d9`. Match. mergeStateStatus DIRTY, mergeable CONFLICTING. Checks: Check changelog entry pass; Run Solr Tests using Crave.io resources pass; gradle check pass; labeler pass; generate skipping.
- PR 4968: headRefOid `b75e7d4d3c464ceeb9f14fdaf7711212adcccac2` equals `b75e7d4d3c4`. Match. mergeStateStatus CLEAN, mergeable MERGEABLE. Same check set, all pass or skipping.
- Refs on origin: solr-13706-submit 590dd5c24d9, solr-18178-verify b75e7d4d3c4, solr-6960-submit 9bef536fc12. All match the claim table.

## Findings

**F1. FIX. PR 5015 merge state.**
Evidence: `gh pr view 5015` gives mergeStateStatus DIRTY and mergeable CONFLICTING. Locally, the branch and upstream main (ref `upstream/main` = 8e62c2686882, 2026-10-09 11:00 -0400) both change `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-11.adoc` at the same place, after line 43 of the base (merge-base c3e18f1e455). The branch adds `== API Changes` with a child-plugins section. Upstream adds `=== V2 collections tree` there. `git merge-file` gives one conflict (merged lines 45 to 65). The other changed files have the same blob at base and at upstream main (SolrConfig.java 60362f4a113, PluginInfo.java 1b5e501e423, PluginInfoTest.java 239527a9aeb), so this local check finds no other conflict.
Replacement: none for the body. The PR is not ready until D1 is decided. The assignment's "ready" label does not match GitHub's field.

**F2. FIX. PR 5015 Proof: corroboration line.**
Live text: "the same test counts also passed on the fork's GitHub Actions test runner at head b062f3b1ffd on 2026-10-04".
Evidence: `receipts/SOLR-13706.md` says GitHub run 37568486924 (PluginInfoTest only) "succeeded at this head", which is 590dd5c24d9. The branch changed PluginInfo.java after b062f3b1ffd (commit 51075ab7328, 7 insertions and 17 deletions). A run at b062f3b1ffd does not cover the live PluginInfo code.
Replacement: "PluginInfoTest also passed on the fork's GitHub Actions runner (run 37568486924)." Keep only after the owner confirms that run's head (D2).

**F3. FIX. PR 5015 Proof: base-code counts.**
Live text: "(8 failing test executions under randomization)", "(4 failing test executions)", and "The base-code comparisons ran the same test classes against a tree with base production code and this PR's test files."
Evidence: the receipt has none of these counts. Its only proof line is "the gate's pre-fix proof step passed at this head." For a fail-before step, "passed" is unclear (see D3).
Replacement: "On base code the new cases fail. The gate's pre-fix proof step passed at head 590dd5c24d9." Delete the two counts and the sentence about the comparison run.

**F4. FIX. PR 5015 Proof: TestSolrConfigHandler clause.**
Live text: "it navigates into a plugin's children in the /config output by the child's type, matching the new keying."
Evidence: SOLR-13706 does not change TestSolrConfigHandler.java (blob 4cb5b8889b6 on base and on the branch). The file has 8 test methods. Its /config lookups are by name (config/requestHandler, config/searchComponent/tc, overlay/requestHandler/.../defaults/c) and one by index (config/listener[0]). No lookup of plugin children by type was found.
Replacement: "TestSolrConfigHandler: 8 of 8 pass at head 590dd5c24d9."

**F5. FIX. PR 5015 presentation and citation.**
Evidence: no section opens with a bold one-line summary (pr-formula.md, presentation rule of 2026-10-08). The changelog citation is plain code text, not a link.
Replacement for the changelog line: "[changelog/unreleased/SOLR-13706.yml](https://github.com/nick-boss-tech/solr/blob/590dd5c24d97c5c1b8f2ed262037c0a9c4ba6f77/changelog/unreleased/SOLR-13706.yml)". Bold summaries: the owner writes one line per section. Not drafted here.

**F6. NOTE. PR 5015 Proof: gate command name.**
Live text: "tidy, Error Prone compile, and `:solr:core:check -x test` all clean".
Evidence: the receipt says "tidy clean; Error Prone compile passes; module check passes." The command name is not in the receipt or the repo. The owner confirms it against the gate log.

**F7. NOTE. PR 5015 Limits: "no code reads the serialized children by name".**
Evidence: a sample grep of the branch under solr/webapp and solr/solrj found no config-children reader. The hits were unrelated (cloud.js graph children, PathTrie.java). This is not a full search. The owner keeps or weakens the sentence.

**F8. NOTE. PR 5015 choice section.**
The scope choice (fix in the shared PluginInfo serialization, or locally in the highlight output) names a real cost: the serialized shape changes for every plugin with children. That meets the formula's bar for a live alternative. It ends with a pointed question. No change needed.

**F9. FIX. PR 4968 Proof header.**
Live text: "Verified at head 61b8f767c34 on 2026-10-04 (tidy clean, Error Prone compile clean, `:solr:core:check -x test` green)."
Evidence: production code changed after 61b8f767c34 (solr/core/src/java changed in f9c78f08814, 4ccbdd620a0, and b75e7d4d3c4). The receipt at b75e7d4d3c4 says tidy clean, Error Prone compile passes, module check passes.
Replacement: "Verified at head b75e7d4d3c4 (2026-10-06): tidy clean, Error Prone compile passes, module check passes."

**F10. FIX. PR 4968 Proof: old counts and base claim.**
Live text: the bullet "UploadConfigSetAPITest 18/18", including "The new entry-path tests fail on the base code and pass here (run locally)".
Evidence: the receipt has UploadConfigSetAPITest 20 of 20 at b75e7d4d3c4 at three seeds. The body's later bullet agrees. The receipt's only base-code result: against the previous head's production, the final tests run 20 with exactly 1 failure, testZipUploadIgnoresLegacyRootDirectoryMarker (log g4968r3-premise2.log). Base-code failures for the other named tests are not in the receipt.
Replacement: delete "18/18" and the base-code sentence. Use: "Against the previous head's production, UploadConfigSetAPITest runs 20 tests with 1 failure, testZipUploadIgnoresLegacyRootDirectoryMarker."

**F11. FIX. PR 4968 Limits: follow-up wording.**
Live text: "Pre-existing behavior; a separate ticket follows."
Evidence: the formula (sections 4 and 5) asks to name the item and offer a follow-up on request. Standing rule: no new Jira ticket unless someone asks.
Replacement: "Pre-existing behavior. Happy to open a follow-up ticket and PR for it on request."

**F12. FIX. PR 4968 Proof: process narration.**
Live text: "which is the rejection this round removes"; "fixed in this round by comparing entry names as strings"; and the seed sentence that starts "Seed 7777777777771007 is included because a GitHub run at the previous head failed in this class".
Evidence: the formula (section 3) keeps process catches out unless the shipped behavior changed. This was a test-only fix. "this round" is internal vocabulary.
Replacement: "which this head no longer rejects". Delete "fixed in this round by comparing entry names as strings". Replace the seed sentence with "Seeds: 4968D15EA5E2, 4968D15EA5E3, and 7777777777771007."

**F13. FIX. PR 4968 changelog line and presentation.**
Live text: "Changelog: `changelog/unreleased/SOLR-18178.yml` (fixed)".
Evidence: the file exists on the branch with type fixed. The template has no "(fixed)". No bold summaries, as in F5.
Replacement: "Changelog: [changelog/unreleased/SOLR-18178.yml](https://github.com/nick-boss-tech/solr/blob/b75e7d4d3c464ceeb9f14fdaf7711212adcccac2/changelog/unreleased/SOLR-18178.yml)". Bold summaries: the owner writes them.

**F14. NOTE. PR 4968 Proof: unnamed head.**
Live text: "Also at head (run on the GitHub Actions fork runner): TestFileSystemConfigSetService 3/3 and TestSchemaDesignerConfigSetHelper 8/8 pass."
Evidence: no SHA is named, and the receipt does not carry these counts. The branch method counts match (3 and 8). The owner names the head or drops the line.

**F15. NOTE. PR 4968 Limits: export-side paragraph.**
Live text: "The export-side separator tests were first run on the Linux fork runner..." and "is now done". This is test-process narration. The point that matters is that the Windows run discriminates. The owner decides whether to trim it.

**F16. NOTE. PR 4968 choice section ending.**
Live text: "Happy to revisit if skipping still seems better." The formula asks for a pointed question. The owner writes it.

**F17. NOTE. PR 4968 Windows evidence.**
Evidence: the receipt says a Windows run of the same class succeeded at this head, before the Windows build revocation. The body cites Windows runs only at c3b6ded623e and 61b8f767c34 (2026-10-04). No drift. The receipt has a fact the body does not state. The owner decides.

**F18. NOTE. SOLR-18178 round 2 record.**
Evidence: a search of "18178" and "4968" on origin/pr-prepare under reports/ and claims/ finds only the assignment and the claim. The receipt is the only repo record: "A round 2 review at this head was clean." See D8.

**F19. NOTE. C3 conflict in SolrConfig.java.**
Evidence: SOLR-13706 deletes base lines 986 and 987 (the TODO comment and the skip line). SOLR-6960 replaces base line 988. The changes are adjacent, with no unchanged line between them. A simulated merge (git merge-file on scratch copies) gives one conflict in both orders, at merged lines 986 to 996. Expected resolution for whichever branch lands second: drop the TODO and skip lines (SOLR-13706), keep the applyInitParams put (SOLR-6960). Not built or tested.

**F20. NOTE. C3 semantic check.**
Evidence: SOLR-6960 copies request-handler PluginInfo through applyInitParams, and the copy keeps children (base PluginInfo.java around line 256). SOLR-13706 changes only how children are keyed in writeMap. The defaults, appends, and invariants that the SOLR-6960 test reads are lst nodes (NL_TAGS, base PluginInfo.java lines 244 to 245), so they sit in initArgs, not in children (lines 156 to 165). The SOLR-13706 keying change does not reach those test paths. Read only; not run.

## Task results

**C1. Verdict: head matches; body is not yet consistent with the receipt or the branch.**
The head matches (F1 notes the merge state). PluginInfoTest 11 of 11, TestConfigHighlightOutput 1 of 1, and TestSolrConfigHandler 8 of 8 agree with the receipt and with the branch's test method counts (11, 1, 8). The 2026-10-06 date agrees with the receipt. The file claims agree with the branch: the changelog (type changed), the upgrade note, the removed TODO and skip in SolrConfig.java, and the type grouping in PluginInfo.writeMap. Drift: F2 and F3 (receipt does not carry the counts or the corroboration head), F4 (test clause not supported), F5 (format and citation). F1 blocks the merge. F6 is a name to confirm.

**C2. Verdict: head matches; round 2 record not found in the repo; proof text is stale (F9 to F13).**
The head matches. No round 2 record exists under reports/ or claims/ on origin/pr-prepare. The receipt says the round 2 review was clean at this head. The main-side log is not in the repo. Receipt against branch: all 8 named tests exist. Class counts match the receipt and the body (UploadConfigSetAPITest 20, DownloadConfigSetAPITest 5, TestFileSystemConfigSetService 3, TestSchemaDesignerConfigSetHelper 8). The changelog file exists. The code matches the body: toZipEntryName (DownloadConfigSet.java around line 139); the single "/" directory exemption (UploadConfigSet.java lines 113 to 115); the drive, dot-segment, leading-slash, and empty checks (lines 173 to 190); the single-file path (uploadConfigSetFile, line 196 on); the child-of-root check (FileSystemConfigSetService.java lines 362 to 364). Drift is in the proof text and the limits only.

**C3. Verdict: one textual conflict, in SolrConfig.java only. Landing order: SOLR-13706 first, then SOLR-6960.**
Bases used. For SOLR-13706, the merge-base with upstream main is c3e18f1e455 (2026-10-01). For SOLR-6960, the merge-base with upstream main is 97d973814336 (2026-10-06). The merge-base of the two branches is c3e18f1e455. SolrConfig.java and PluginInfo.java have the same blob at both bases (60362f4a113 and 1b5e501e423), so both diffs read the same text. Hunks: SOLR-13706 at `@@ -983,8 +983,6 @@` removes lines 986 and 987. SOLR-6960 at `@@ -985,7 +985,11 @@` replaces line 988, and its leading context is the same lines SOLR-13706 removes. No other shared file: SOLR-6960 has RequestHandlers.java and TestInitParams.java; SOLR-13706 has PluginInfo.java, PluginInfoTest.java, TestConfigHighlightOutput.java, the changelog, and the upgrade notes. Conflict risk: one certain textual conflict, in either order. Semantic risk is low (F20). SOLR-13706 must also clear its upgrade-notes conflict (F1) first.

**C4. Verdict: owner decisions listed below. None made here.**

## Owner decisions

- D1. PR 5015 merge conflict (F1). Options: (a) merge current upstream main into the SOLR-13706 branch and fix the upgrade-notes section by hand (normal push, no force); (b) rebase onto upstream main (force push, needs your explicit direction); (c) wait. Also choose where `== API Changes` goes relative to upstream's `=== V2 collections tree`: before it, after it, or nested.
- D2. PR 5015 corroboration head (F2). Options: keep the replacement line once the head of run 37568486924 is confirmed as 590dd5c24d9; or drop the line and cite the gate receipt only.
- D3. PR 5015 base-code counts and the word "passed" (F3). Options: confirm the counts from g13706-rereview-gate.log and keep them; or drop them and keep the receipt wording.
- D4. PR 5015 TestSolrConfigHandler clause (F4). Options: drop the clause; or keep it only if a type-keyed lookup is found.
- D5. Bold summaries and linked citations on both PRs (F5, F13). Options: edit now; or fold into the next edit.
- D6. PR 4968 proof header and old counts (F9, F10). Options: restate at b75e7d4d3c4 with receipt numbers only; or keep the old lines with their head and date labelled.
- D7. PR 4968 follow-up wording (F11). Options: use the offer wording (no new ticket unless asked); or authorize the follow-up ticket now.
- D8. PR 4968 round 2 record (F18). Options: accept the receipt sentence; or have the main side record the round 2 result under claims/ before the next pass.
- D9. PR 4968 process narration and choice question (F12, F15, F16). Options: trim and add a question; or keep as is.
- D10. Landing order and conflict (F19). Stated order: SOLR-13706 first, then SOLR-6960 resolves the hunk. Other option: SOLR-6960 first, then SOLR-13706 resolves it. Either way, one conflict.
- D11. PR 4968 unnamed run head (F14) and Windows evidence (F17). Options: name the head or drop the line; add the receipt's Windows fact or leave it out.

## Not checked

- Gate and takeover logs (g13706-rereview-gate.log, g4968r3-gate.log, g4968r3-premise2.log, the main-side takeover log) are not in the repo. Round 29, round 2, and gate counts are unverified beyond the receipts.
- No tests, builds, or Gradle runs (rule). So the NPE claim, the pass and fail counts, the cleanup=true claim (PR 4968 Limits), and the Windows results are not verified.
- GitHub run IDs 37568486924 and 37566343572 are not checked. The allowed commands do not show run heads, and `pr checks` gives no per-run head SHA.
- The live check list may not match the head shown. Not confirmed per run.
- The PR 5015 body length (about 3,500 characters guide) was not counted.
- The "no code reads children by name" claim (F7) was sampled, not fully searched.
- The listener[0] lookup in TestSolrConfigHandler (F4) was not traced.
- The conflict simulations use the local upstream/main ref (8e62c2686882). GitHub's base may be newer. The DIRTY state comes from GitHub; the conflict location comes from the local simulation.
- The child-of-root check and normalization in FileSystemConfigSetService.java were read, not run.
- The SOLR-6960 top-up (main side) and the SOLR-7323 and SOLR-18178 FileSystemConfigSetService check (part A) are outside this part.
- Process note: the first `gh pr view` call failed because PowerShell split the unquoted field list. It was rerun with the same fields quoted. No other gh calls. No write calls. The token was not printed.
