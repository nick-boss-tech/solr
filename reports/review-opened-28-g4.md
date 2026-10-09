# Review of the opened drafts, group 4: SOLR-6065, SOLR-6973, SOLR-7022, SOLR-11475

Group 4 is PR #5078 (SOLR-6065), PR #5079 (SOLR-6973), PR #5080 (SOLR-7022), and PR #5082 (SOLR-11475). Read-only: `gh pr view` only. Each head was confirmed with `git ls-remote`, and the diffs run against the local objects at those heads.

## Checks shared by all four

- Head: each live headRefOid equals the full SHA in its receipt. headRefName is `solr-<ticket>-submit`, baseRefName is `main`, state OPEN, isDraft true.
- Body: each live PR body is byte-identical to `pr-drafts/update-processing/SOLR-<ticket>.md` (cmp).
- Title: none of the four PR titles matches its changelog title line. The rule is in `assignments/open-update-29-prs.md` line 31 (`SOLR-<ticket>: <title>`, with the fragment title). Each is a FIX FIRST item below.
- Diff against the merge base: only the intended files. No TESTING, handoff, or stray files at any head. The handoff docs added and removed in the history of 6973 and 11475 are net zero.
- No em dashes, and no first-person plural wording, in any draft.
- Cross-PR: no ruling in the assignment names group 4. SOLR-6065 and SOLR-7022 both edit `DirectUpdateHandler2.java`, in separate hunks (6065 at lines 85-90 and 420-437; 7022 at lines 940-960). No overlap.

## SOLR-6065 (PR #5078)

**Verdict: FIX FIRST** (the PR title; the other items are cosmetic)

Findings
1. PR title. Current: "SOLR-6065: Solr should give you clear error if you try to add too many docs". Fragment title at head `3d2cec9e1ab3`, `changelog/unreleased/SOLR-6065-max-docs-message.yml` line 2. Replace the title with: `SOLR-6065: Adding a document to an index that has reached Lucene's maximum document count now fails with a clear message instead of being reported as a possible analysis error.`
2. Cosmetic. Limits has no bold one-line summary (`pr-drafts/update-processing/SOLR-6065.md` line 34). Insert before line 36: `**Only the add path is covered, and recognition depends on Lucene's message wording.**`
3. Cosmetic. Proof line 23 says "the same test fails with the old 400 response". The receipt says "fails on base production with the generic analysis-error shape". Same fact. Optional replacement: "fails with the old generic analysis-error response (400)."

Decided status: the Choice section (lines 30-32) states 500 as decided on 2026-10-06 and keeps 400 only as the alternative not taken, as the group note asks. It has no closing question, which fits a decided call. No change proposed.

Checks passed: head matches the receipt; body identical; the "today" and "change" claims match the head (prefix check in `addDoc`, SERVER_ERROR with the limit named, delete and optimize advice, 400 to 500 change, other IllegalArgumentExceptions unchanged); anchors at head hold the cited code (DirectUpdateHandler2.java L442-L448, L422-L437, L784; libs.versions.toml L39 is lucene 10.4.0; MaxDocsLimitCloudTest.java L81-L120; DirectUpdateHandlerTest.java L108-L140); 1/1, 8/8 and 2026-10-09 match the receipt; changelog YAML valid; AI header and footer present; section order correct; no Jira ticket promised.

## SOLR-6973 (PR #5079)

**Verdict: FIX FIRST** (the PR title, and one build claim the receipt does not record)

Findings
1. PR title. Current: "SOLR-6973: Some documents will not update on a cloud server using SignatureUpdateProcessorFactory". Fragment title at head `fa5b59ba07b4`, `changelog/unreleased/SOLR-6973-signature-partial-update.yml` line 1. Replace the title with: `SOLR-6973: SignatureUpdateProcessorFactory no longer adds an empty signature to (or deletes duplicates for) a partial update that contains none of the signature fields.`
2. Proof, draft line 25: "The build checks (changelog parse, tidy, Error Prone compile, and core module check without tests) also passed." The receipt records only SignatureUpdateProcessorFactoryTest 7/7 and "module check rc=0". Replace that sentence with: `The core module check also passed.`

Checks passed: head matches the receipt; body identical; citations hold at head (SignatureUpdateProcessorFactory.java L169-L210 for the loop, throw, and signature write; DirectUpdateHandler2.java L1177-L1180 for updateTerm and updateDocuments; L152-L196 for the no-fields rejection and the pass-through; the test at L205-L236); 7/7 on 2026-10-08 and "7 tests with 1 failure" on base match the receipt; Choice has a live alternative and ends with a question; Limits has its bold line; changelog YAML valid (`nick:` key is used by 74 upstream fragments).

## SOLR-7022 (PR #5080)

**Verdict: FIX FIRST** (the Proof says the live tip was never gated; the refreshed receipt says it was)

Findings
1. Proof contradicts the refreshed receipt. Line 22 reads: "**The gate ran at `6a233ab2fdb`. The live tip has two later wording commits and was never gated.**" Line 25 ends "Neither was re-gated." The refreshed receipt names `db357868610b` as the gated head. It records DirectUpdateHandler2AwaitSearcherTest 2/2 at the tip (log `g7022-topup.log`, 2026-10-09) and Error Prone compile passing at the tip. `material/update-29-rulings.md`, last paragraph, counts the 7022 top-up as gating. Replace line 22 with: `**The commit-level test was gated at 6a233ab2fdb. The live tip adds a changelog title and a Javadoc comment, and the helper test was re-run at the tip.**` Replace lines 24-25 with:
   - `Gated at 6a233ab2fdb on 2026-10-06: DirectUpdateHandler2AwaitSearcherTest 2 of 2, DirectUpdateHandler2CommitWaitTest 1 of 1, DirectUpdateHandlerTest 7 of 7. The module check returned 0.`
   - `Re-run at the live tip db357868610b, verified 2026-10-09: DirectUpdateHandler2AwaitSearcherTest 2 of 2. Error Prone compile passes at this head.`
   - `The two later commits, 5b822b7e8e9 (changelog title) and db357868610b (Javadoc), change wording only. The Java diff from 6a233ab2fdb is one Javadoc comment.`
   Line 27 (the pre-fix claim) then stands on the 2026-10-06 gate only.
2. Receipt gap (lead action, not a description edit). The 2026-10-06 counts, the 2026-10-06 date, and the pre-fix result ("new commit-wait test fails exactly 1 test, the interrupt-restored assertion") are in the receipt before the refresh (`git show faa275ad1fd^:receipts/SOLR-7022.md`). The refreshed receipt dropped them, and the Proof cites them. Keep them as ledger history in the receipt. Also, its line "Date: 2026-10-09 (gate and top-up verification the same day)" reads as if the gate itself was 2026-10-09. Clarify.
3. PR title. Current: "SOLR-7022: ERROR UpdateHandler java.lang.InterruptedException". Fragment title at head `db357868610b`, `changelog/unreleased/SOLR-7022-interrupted-searcher-wait.yml` line 2. Replace the title with: `SOLR-7022: An interrupt while a commit waits for the new searcher is logged at INFO and the interrupt status is restored, instead of an ERROR with a stack trace.`
4. Cosmetic. Limits has no bold one-line summary (line 35). Insert before line 37: `**The commit-level test is the weak point, and two test classes cover a small change.**`

Not checked: GitHub Actions run `37558668356` (line 26) is not in the receipt and is outside the allowed reads. The CI commit `7940e98b0ee0` is confirmed on `origin/ci/7022-commitwait` directly above `6a233ab2fdb`, not an ancestor of the tip. It adds one file, `.github/workflows/fork-test-runner.yml`, and no source.

Checks passed: head `db357868610b` matches the receipt; body identical; `awaitSearcher` at L951-L960 and its call at L943 match the head; the test at L93-L161 matches; the code claims (INFO for an interrupt, ERROR for ExecutionException, commit returns normally, no API change) match the diff; AwaitSearcherTest has 2 tests; changelog YAML valid; diff is four intended files; Choice has a live alternative and a question.

## SOLR-11475 (PR #5082)

**Verdict: FIX FIRST** (the PR title; the Limits summary is cosmetic)

Findings
1. PR title. Current: "SOLR-11475: Endless loop and OOM in PeerSync". Fragment title at head `0de48e492fd4`, `changelog/unreleased/SOLR-11475-peersync-version-ranges-loop.yml` lines 2-3, folded into one line. Replace the title with: `SOLR-11475: PeerSync no longer loops forever (and runs out of memory) when computing the versions to request if both replicas hold the same version with opposite signs.`
2. Cosmetic. Limits has no bold one-line summary (line 35). Insert before line 37: `**The check calls the PeerSync helper directly, with one set of versions.**` Then change line 37 to: `- It does not run a peer sync between nodes.`

Checks passed: head matches the receipt; body identical; loop L805-L844 and new branch L826-L832 match the head, and the base behavior described (equal absolute values fell into the range branch, which added the same string without moving) is right; test L453-L481 and comment L454-L457 match; PeerSyncWithLeaderTest has no test method of its own and extends PeerSyncTest, and PeerSyncWithLeaderAndIndexFingerprintCachingTest extends PeerSyncWithIndexFingerprintCachingTest, as stated; five classes at 1/1 and 2026-10-08 match the receipt; INCONCLUSIVE pre-fix stated consistently; diff is three intended files; changelog YAML valid; Choice ends with a question.

## Owner calls in group 4

None. Every item above has a clear correction.
