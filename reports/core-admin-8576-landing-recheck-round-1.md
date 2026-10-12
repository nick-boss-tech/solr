# Core admin, SOLR-8576 landing re-check, round 1

Verdict: **NOT CONVERGED.** The draft's head, links, anchors and the round 1 replacements now match the branch and the gate receipt. Two should-fix items remain: the Proof does not say plainly that the proof is inconclusive by construction (CA-LR-01), and one sentence in What this change does says more than the test asserts (CA-LR-02).

Scope: `pr-drafts/core-admin/SOLR-8576.md` at `0b36caa0e3a`, checked against `reports/core-admin-convergence-round-1.md` (CA-R1-21 to 27), `reports/core-admin-convergence-round-3.md` (the items owed on landing), `receipts/SOLR-8576.md` (the top section, "Gate at 883bb522ca") and the branch `solr-8576-submit`. The claim is `claims/pool-core-admin-8576-landing-recheck.md`. Read-only: no edits, posts, builds or tests. Two read-only subagents, four tasks each.

Head: `ls-remote origin refs/heads/solr-8576-submit` returns `883bb522caf97ca59f5303117efa81d1ec12dbfe`, which the draft names. The merge-base of that head with upstream/main is `b6b2b8f10e9827e3b86e44649fb7966ce646c185`, which the base links use.

## One line per round 1 item

- **CA-R1-21 (blocking), lines 13 and 15: applied, confirmed.** Line 13 has the round 1 replacement. Line 15 has the post-fix sentence. The assertions at `883bb522` (lines 1186 to 1191) check the alias after `aliasesManager.update()`, so the claim that the alias still resolves holds. The word "unchanged" is addressed in CA-LR-02.
- **CA-R1-22 (should-fix), line 7: applied, confirmed.** The bold line matches the replacement word for word.
- **CA-R1-23 (should-fix), line 9: applied, confirmed.** The link at line 9 is to `b6b2b8f10e`, and the text says it is the merge-base. Base lines 129 and 134 of `CreateCollectionCmd.java` hold the two messages.
- **CA-R1-24 (should-fix), the OWED marker: applied, confirmed.** No "owed" or "OWED" remains. Line 24 is blank.
- **CA-R1-25 (note), "It pins error behavior": applied, confirmed.** The phrase is gone, and line 22 reads "It checks error behavior that already exists."
- **CA-R1-27 (should-fix), date: applied, confirmed.** Line 21 reads "(recorded 2026-10-12)", which matches the receipt's gate dates (run 07:58Z to 08:20Z).

## Findings

**CA-LR-01 | line 19 and line 22 | should-fix.** The receipt records no fail-before run. The draft shows the pin result but does not say the proof is inconclusive by construction, which `pr-formula.md` section 3 asks for on a test-only change. The draft's own wording ("does not show a failure before the change") gets close, but it does not use the required statement.
Replacement for line 19 (bold): "**This is a test-only change, so the proof is inconclusive by construction: the new test also passes on the base code.**"
Replacement for line 22: "- `testCreateCollectionNameAlreadyTaken` passed on the base commit `b6b2b8f10e` (1 test, 0 failures, recorded 2026-10-12). It checks error behavior that already exists. No fail-before run was made."

**CA-LR-02 | line 15 | should-fix.** The sentence "it checks that the original collection is unchanged" says more than the test asserts. Lines 1186 and 1187 check only that the collection exists under its name. The alias check at lines 1188 to 1190 is correct.
Replacement for the sentence: "After that, it checks that the original collection still exists under its name and that the alias still resolves to it."

**CA-LR-03 | line 30 | note.** The Limits do not name a real gap: the two `assertThrows` calls (test lines 1169 and 1178) accept any exception type, so the error code is not checked.
Optional replacement for line 30: "- The checks match only the message text and accept any exception type, so the error code is not checked. A change to that text needs a matching test change."

**CA-LR-04 | line 7 | note.** The bold line says no test checks the error message. Narrowing it to the class the branch touches is accurate, and it avoids the wider claim about other test classes.
Optional replacement for the bold line: "**Creating a collection with a name that a collection or an alias already has is rejected, and no test in CollectionsAPISolrJTest checks either error message.**"

**CA-LR-05 | lines 21 and 22 | note.** The class and test name in the Proof are not links. Formula section "Citations are real links" applies.
Optional: link `CollectionsAPISolrJTest` to `https://github.com/nick-boss-tech/solr/blob/883bb522caf97ca59f5303117efa81d1ec12dbfe/solr/core/src/test/org/apache/solr/cloud/CollectionsAPISolrJTest.java#L1157-L1192`.

**CA-LR-06 | line 24 | note.** Blank line before "## Limits" is doubled. Delete one blank line.

## Checks that held

- Every link uses `883bb522caf97ca59f5303117efa81d1ec12dbfe`, except line 9, which uses the base commit.
- The changelog path exists at the head.
- The test runs from line 1157 to line 1192 at `883bb522`.
- The Proof numbers (25 tests, 1 skipped, 0 failures at the head; 1 test, 0 failures on the base) match the receipt, and the receipt's seed is `8576C0FFEE8576`.
- The draft has the AI header, the Jira link, the AI assistance footer and the changelog line.
- No em dash or en dash in the draft. No PR numbers. No process words (gate, receipt, owed, vm2, claim, job, host, rc) in outbound text.

## Not checked

- The gate log `/workspace/gates/logs/vm2/8576-gate.out` (on VM2, not on this host). The Proof cites the receipt, which is the named source.
- The JIRA ticket text.

## Convergence line

NOT CONVERGED. Two should-fix items remain: CA-LR-01 and CA-LR-02. The draft's head, links, anchors and round 1 replacements all hold. Once the main side applies the two replacements, a short re-check of lines 15, 19 and 22 closes the round. Nothing was edited, posted, built or tested in this round.
