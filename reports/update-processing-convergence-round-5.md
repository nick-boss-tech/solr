# Update processing convergence, round 5

Verdict: **NOT CONVERGED.** Two of the four drafts converge (SOLR-5065 and SOLR-7022). SOLR-14718 and SOLR-3657 each have one should-fix item, with the replacement text below. The round 4 receipt item for SOLR-14718 is closed.

Assignment: `assignments/pool-update-processing-convergence-round-5.md`. Two read-only subagents: group 1 checked SOLR-14718 and SOLR-7022, group 2 checked SOLR-3657 and SOLR-5065. Both checked against the main side's round 4 application commit `8a3438088cc`. The drafts' named heads all match `git ls-remote origin refs/heads/<branch>`: SOLR-14718 `29c09959791a`, SOLR-3657 `14edaca577c0`, SOLR-5065 `ab894a996c8a`, SOLR-7022 `db357868610b`. Nothing was edited, posted, built or tested.

## One line per draft

- **SOLR-14718: NOT CONVERGED.** `receipts/SOLR-14718.md` line 9 now records the 2026-10-09 settling run: seeds `14718C0FFEE14718`, `14718C0FFEE14719` and `14718C0FFEE14720`, one AssertionError each at `SolrCmdDistributorTest.java:572`, and a control at the unmodified head with 0 failures. Draft line 25 matches it. Open: UP-R5-01 (line 36, should-fix).
- **SOLR-3657: NOT CONVERGED.** Bold line 22 matches the round 4 replacement word for word. Bullet line 25 agrees with it on the SHA, the overlay, the 2 failures and the 17 tests. Receipt line 7 gives 17/17 at head `14edaca577c0`, and `git merge-base 14edaca577c0 upstream/main` is `97d973814336`. Open: UP-R5-02 (line 25, should-fix).
- **SOLR-5065: CONVERGED.** Lines 9 and 25 say "base production code at `0cc328310f8f`", which matches `receipts/SOLR-5065.md` line 8. The class link `L31-L49` is correct, because the file has 49 lines at `ab894a996c8a`. Round 4's premise on this draft was wrong (UP-R5-05).
- **SOLR-7022: CONVERGED.** Line 24 matches `receipts/SOLR-7022.md` line 5 (2026-10-05 at `a2ed957063b`, DirectUpdateHandler2AwaitSearcherTest 2 of 2, DirectUpdateHandlerTest 7 of 7). The draft claims no 2026-10-06 CommitWaitTest run. Line 26's last sentence ("read to fail" at the interrupt-restored assertion, "this was not run") matches `audits/update-processing/SOLR-7022.md` finding 3 and receipt lines 5 to 7.

## Findings

**UP-R5-01 | SOLR-14718, draft line 36 | should-fix.** The sentence reads "A follow-up submission for that flaw is planned." The record holds only the owner's ship-narrow decision (`audits/update-processing/SOLR-14718.md` lines 35 to 39, and `TESTING.md`). No follow-up is decided, so the sentence promises something the record does not show.
Replacement for the last sentence: "A fix for that case would need a separate change."

**UP-R5-02 | SOLR-3657, draft line 25 | should-fix.** "(gate of 2026-10-07)" uses internal process vocabulary, which `pr-formula.md` keeps out of outbound text.
Replacement for that parenthesis: "(verified 2026-10-07)". The rest of the bullet is unchanged.

**UP-R5-03 | SOLR-14718, draft line 31 | note.** "Landing order: SOLR-5939 lands before this change." has no supporting receipt in this round. Confirm it before posting. No replacement is proposed.

**UP-R5-04 | `audits/update-processing/SOLR-14718.md` lines 18 and 29 to 33 | note.** Line 18 still lists finding 1 as "Still open", and lines 29 to 33 still say "Not ready" with finding 1 as blocking item 1. Lines 35 to 39 record that the 2026-10-09 run disproved the timing mechanism. Update the audit before any PR is cut. The draft is unaffected.

**UP-R5-05 | round 4 report, finding 3 (SOLR-5065) | note, record correction.** The premise of round 4's finding 3 is wrong. `git merge-base ab894a996c8a upstream/main` is `0cc328310f8f`, and `97d973814336` is not an ancestor of the head. The original "merge-base" label was therefore accurate. The current "base production code" wording is also accurate and matches the receipt, so no draft change is needed. Round 4's suggestion to change the class link to `L31-L48` is withdrawn: the file has 49 lines, and `L31-L49` runs from the class declaration to its closing brace. The main side's response (`reports/update-processing-convergence-round-4-response.md` lines 32 to 33) already states the merge-base finding.
Record wording for the main side: "Round 4 finding 3 is withdrawn. `0cc328310f8f` is the merge-base of `ab894a996c8a` with upstream/main, so the original label was accurate. The class link stays at L31-L49."

## Checks that held

- Head SHAs: all four drafts name the head that `ls-remote` returns.
- Anchors cited in the drafts were checked by read-only `git show` (SOLR-7022 `DirectUpdateHandler2.java` L943 and L951 to 960, and the test at L25 to 36; SOLR-14718 `SolrCmdDistributorTest.java` L547, L572 and L575, `SolrCmdDistributor.java` L249 to 262, and base `JavabinLoader.java` L123 against the draft range L105 to 123). All match.
- No em dash or en dash in the four drafts. The "[runner]" link text in SOLR-14718 line 33 is a code identifier, so it stays.

## Not checked

- The raw settling-run log `g14718-settle.log` is not in this worktree, so the receipt's counts are taken as recorded.
- The main side's takeover log and the JIRA ticket text.

## Convergence line

NOT CONVERGED. SOLR-5065 and SOLR-7022 converge. SOLR-14718 waits on UP-R5-01, and SOLR-3657 waits on UP-R5-02. The main side applies the replacements and records UP-R5-04 and UP-R5-05. Nothing was edited, posted, built or tested in this round.
