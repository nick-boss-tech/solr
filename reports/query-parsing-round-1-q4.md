# Query parsing round 1, part q4 report

Result: both live heads match (5012 at c0aec3a7ae0, 5004 at 3c48dec4b79), but git shows PR 5012 already squash-merged on apache/solr main as 93e357bcf6b, so the "awaiting merge" record is stale; PR 5004 conflicts with main by one import line; 13838 and 13903 match their heads on a static check but have no recorded pre-fix outcome.

## Findings

1. FIX (workspace records: claim line 62, assignment line 26, `receipts/SOLR-13202.md` line 3, inventory line 159). PR 5012 is described as open and awaiting merge. Evidence: local `upstream/main` (remote `https://github.com/apache/solr.git`, reflog fast-forward 2026-10-09 11:00 -0400) contains `93e357bcf6b`, "SOLR-13202: Return 400 instead of 500 for join queries missing from/to parameters (#5012)", parent `5e044a331fd5`, dated 2026-10-07 18:03:48 +0300. Its nine PR files are byte-identical to `c0aec3a7ae0` (`git diff --quiet 93e357bcf6b origin/solr-13202-submit -- <file>` exits 0 for each). The PR `state` field is outside the allowed `gh` fields, so this is not yet confirmed on GitHub. Replacement for the claim table row: "13202 | ... | PR #5012 squash-merged to apache/solr main as 93e357bcf6b (2026-10-07), same content as head c0aec3a7ae0; confirm state on GitHub". Owner decision.

2. FIX (PR 5012 body, "What this change does", first paragraph). The parser list omits `AuxIndexJoinQParserPlugin`. Evidence: `git grep -n requireFromAndTo c0aec3a7ae0 -- solr/core/src/java` gives four call sites: `JoinQParserPlugin.java:149`, `join/AuxIndexJoinQParserPlugin.java:411`, `join/CrossCollectionJoinQParser.java:109`, `join/ScoreJoinQParserPlugin.java:379`. Replacement: "`JoinQParserPlugin`, `ScoreJoinQParserPlugin`, `CrossCollectionJoinQParser`, and `AuxIndexJoinQParserPlugin` now share one check, `ScoreJoinQParserPlugin.requireFromAndTo`, which runs before the join is built."

3. FIX (PR 5012 body, "What this change does", unstated behavior change). For `auxIndexJoin`, the 400 message text changes. Evidence: `git diff b3ded4da758 c0aec3a7ae0 -- solr/core/src/java/org/apache/solr/search/join/AuxIndexJoinQParserPlugin.java` removes `throw new SyntaxError("auxIndexJoin query parser requires 'from' and 'to' local params")`. The base test asserted `contains("requires")`; the head asserts the new text. The status stays 400 in both (the base test already asserted BAD_REQUEST). Replacement sentence, added after the parser list: "For `auxIndexJoin`, the 400 message changes from "auxIndexJoin query parser requires 'from' and 'to' local params" to "Join query missing required 'from' parameter" (or 'to'). The status stays 400."

4. FIX (PR 5012 body, "Limits"). The sentence says a missing `v` is "not covered by the new tests", which the head contradicts. Evidence: `TestGlobalOrdinalsJoinQParser.java:445-456` at c0aec3a7ae0 adds a no-`v` case that asserts "from query is required". Replacement for the first Limits sentence: "A missing `v` parameter (the query being joined) is not checked by this change. `globalOrdinalsJoin` already rejects it, and a new test pins that existing check. The new tests do not cover a missing `v` for the other join parsers. Only the `from` and `to` validation from the ticket is in scope here."

5. FIX (PR 5012 body, "Proof"). The Proof names head b3ded4da758 only. Evidence: `git diff --stat b3ded4da758 c0aec3a7ae0` is 3 files, 21 insertions, 7 deletions (`AuxIndexJoinQParserPlugin.java`, `TestAuxIndexJoinQParserPlugin.java`, `TestGlobalOrdinalsJoinQParser.java`). `receipts/SOLR-13202.md` gives no per-class counts for the two test classes, and its gate log `g13202ext-gate.log` is not on disk. Replacement for the head sentence (drop the parenthetical, which is lint output and stays out under pr-formula section 3): "Verified at head b3ded4da758 on 2026-10-04 (`:solr:core:check -x test` green)." Then add: "Head c0aec3a7ae0 adds the same from/to check to `auxIndexJoin`, with tests in `TestAuxIndexJoinQParserPlugin` and `TestGlobalOrdinalsJoinQParser`. Counts for those two classes at c0aec3a7ae0: [not on record; must be run and recorded before any edit]." Do not post with the bracket.

6. NOTE (PR 5012 body, "Proof"). "`CrossCollectionJoinQueryTest` ... also fails on the base code" is not named in the receipt. The receipt says "with the pre-fix proof passing" and does not say which class that run used. Keep only if the owner confirms. Otherwise replace the sentence with: "`CrossCollectionJoinQueryTest` covers the same missing-parameter case for the cross-collection parser and passes 10/10."

7. NOTE (PR 5012 body, "What happens today"). The premise is not checked against the ticket. The inventory row (`branch-focus-inventory-2026-10-08.md:159`) gives the ticket title as "Three NullPointerExceptions in org.apache.solr.search.JoinQuery.hashCode()". The body names no site. No Jira text is available in this part. If the ticket confirms the site, add it to the sentence after "dies further down with a NullPointerException".

8. FIX (PR 5004 body, "Proof"). "passes 10/10, the new test included" has no count in the record. `receipts/SOLR-16130.md` line 5 records tidy, Error Prone compile, and module check, and line 6 records FAIL-BEFORE PASS. The gate log `g16130r30-gate.log` is not on disk. The only queue record, `research/test-queue/results/SOLR-16130.json`, shows SUCCESS for one method on 2026-10-01, with no head SHA. Replacement: "With the guard (already on main), the new test passes." Add a class count only after a run is recorded.

9. NOTE (PR 5004 body, "Proof"). The body says the corroboration run "dispatched for the class at this head". `receipts/SOLR-16130.md` line 7 says run 37581056636 "succeeded at this head before the Actions suspension". The run conclusion cannot be checked with the allowed `gh` calls. `pr checks` shows passing CI, but its output has no head SHA. Replacement, only if the owner confirms the run: "GitHub Actions run 37581056636 passed for `CrossCollectionJoinQueryTest` at this head."

10. NOTE (PR 5004 body, "Proof", fail-before paragraph). The label "Fail-before" is the process label the public text rule bars. The NPE mechanism matches the code: at `70c1a28995d`, `handler/export/ExportWriter.java:858` is `topDocs()`, which calls `new BitSetIterator(bits, 0)` at line 863; `56ec140e363` adds the `bits == null` guard at lines 860-864. The quoted `SolrServerException` text is not in the receipt. Replacement: "On 2026-10-06, with only this PR's test and configset applied to `70c1a28995d` (the parent of the #4953 guard commit `56ec140e363`), the new test fails with a null bitset NullPointerException in `ExportWriter$SegmentIterator.topDocs`." Remove the `SolrServerException` sentence unless the owner confirms the log text.

11. FIX (PR 5004 branch, merge with main). The branch conflicts with current `upstream/main` by one import line. Evidence: `git merge-tree --write-tree upstream/main origin/solr-16130-test-followup` exits 1; only `CrossCollectionJoinQueryTest.java` conflicts, in one region at the import block. Main adds `import org.apache.solr.common.SolrException;` (from #5012). The branch adds `import org.apache.solr.common.SolrDocument;` at the same place. The new test body merges cleanly. Resolution for the import block: keep both lines, sorted:
```
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
```
The owner chooses between merging `upstream/main` into the branch (no force push) or rebasing (force push, which needs explicit direction). The PR's `mergeable` is UNKNOWN, so this result is local only.

12. NOTE (local ref). The local branch `solr-16130-test-followup` points at `bc48203d9a2` (one commit on `99cd97296ed`), not at the PR head `3c48dec4b79`. Neither is an ancestor of the other. Use `origin/solr-16130-test-followup`, which matches `headRefOid`. Do not use the local ref for any proof.

13. FIX (13903 changelog and record). `changelog/unreleased/SOLR-13903.yml` names only the confusion matrix and error. The head also changes weight updates: `TextLogisticRegressionQParserPlugin.java:203-212` adds the no-term training docs to the training loop, so the weights move. The visit order also changes, from the old `docVectors.entrySet()` order to ascending document ID through `BitSetIterator` (line 206). `research/branch-reviews/round-28/SOLR-13903-review.md` Finding 1 lists the order change as an open owner question. Replacement title: "The tlogit query parser now trains on and evaluates training documents that contain none of the selected terms, and visits training documents in document ID order." If the owner decides the order does not matter, drop the second clause and say so in the record.

14. FIX (13903 record). `receipts/SOLR-13903.md` records no pre-fix (fail-before) outcome at all. Replacement for the record: "Pre-fix proof: not recorded." Any Proof for 13903 needs a recorded outcome first.

15. NOTE (13838 record). "NaN pre-fix proof recorded" (`receipts/SOLR-13838.md` line 5) has no log, date, or head on disk, and the reconcile ledger row is not on disk. `IGainTermsQParserPluginTest.java:67-68` asserts that `zebra` is absent and that scores are finite, which fits a pre-fix NaN. The outcome itself is unverified. A record needs the pre-fix head, the observed failure, and the date.

16. NOTE (queue rows for 13838 and 13903). In `research/pipeline/queue.json`, the SOLR-13838 entry (around lines 3487-3497, commit `b65d31dd272`, "Add testing handoff (remove before upstream PR)") and the SOLR-13903 entry (around lines 3403-3413, commit `1004164278`, same message) name commits that are not ancestors of the live heads `4520c25abe2` and `73ef411dd11`. Both sit on the pre-reconcile line, with merge-base `56ec140e363`. Both are status "implemented". Neither has a file under `research/test-queue/results/`. No replacement text. Owner to update the commit fields or mark them historical.

17. NOTE (premise for 13838 and 13903). The Jira connector returned blank text (`research/branch-reviews/round-28/SOLR-13838-review.md`, "Ticket premise"; same for 13903). The premises rest on `research/pipeline/candidates.csv:230` (13838) and `:224` (13903), which are 2019-2020 exports. Pull the ticket text before any Proof or Limits text.

18. NOTE (shared fixture between 13838 and 13903). Both add `solr/core/src/test-files/solr/configsets/analytics-minimal/conf/schema.xml` and `solrconfig.xml` with identical content (`git diff --quiet` exits 0 for both files). `git merge-tree` of 13838 against 13903 exits 0. Either landing order is clean. A reviewer may ask why the fixture is duplicated.

19. NOTE (presentation). Both live bodies predate the 2026-10-08 presentation rule: no bold one-line summaries and no linked citations. Owner call. This part made no edit.

20. NOTE (interactions, for the lead). `requireFromAndTo` is now on `upstream/main` through 5012. `git merge-tree origin/solr-13202-submit origin/solr-11391-submit` exits 0. 11391 touches `JoinQParserPlugin.java`, the same file as the 13202 hunk at line 149, but the two do not conflict in text. A file-level scan of the other query-parsing heads found no other branch that touches the 13202 files. 16130 touches `CrossCollectionJoinQueryTest.java`, which 13202 also touches (see finding 11). 13838 and 13903 do not touch any 13202 file.

## Task results

**SOLR-13202 (PR 5012).** Head check: live `headRefOid` is `c0aec3a7ae01aa629ba1e5e06249568054dc267f`, which matches the claim head and `origin/solr-13202-submit`. `mergeable` and `mergeStateStatus` are both UNKNOWN. `pr checks`: changelog, Crave tests, gradle check, and labeler pass; generate is skipped; no head SHA is printed. Verdict: head matches. The body has drift (findings 2 to 5) and an unchecked premise (finding 7). Git shows the PR already squash-merged on main (finding 1), so any body edit is an owner call. No edit made.

**SOLR-16130 (PR 5004).** Head check: live `headRefOid` is `3c48dec4b79faed625c4fb79833d0975a55f3ed7`, which matches the claim head and `origin/solr-16130-test-followup`. `mergeable` and `mergeStateStatus` are both UNKNOWN. A local merge with main conflicts on one import line (finding 11). `pr checks`: all pass. Verdict: head matches; the branch needs a rebase or merge of main before it can merge. Proof text needs three fixes (findings 8 to 10). The round-30 review items are addressed at this head: the stale-head sentence, the fail-before run, the two-segment assertion, the id assertion, and the configset side effect. Test-only scope and the dropped changelog (`98ef9347cf4`) match the receipt.

**SOLR-13838.** Head check: `4520c25abe28228bfa011102814b9b1063a4415a` matches `origin/solr-13838-submit`. Static check only: one `@Test` in `IGainTermsQParserPluginTest` (line 67) and one test method in `TestIGainTermsQParserPlugin` (line 50), consistent with "1 of 1". Changelog present with the ICLA name. Verdict: reconciled green is consistent at head but not evidenced. Hold: no recorded pre-fix outcome (finding 15), a queue row on a pre-reconcile commit (finding 16), and no ticket text (finding 17). Not draftable until a fail-before outcome is recorded.

**SOLR-13903.** Head check: `73ef411dd117cc826caf15baa1c71e50ff5d622e` matches `origin/solr-13903-submit`. Static check only: one `@Test` in `TextLogisticRegressionQParserPluginTest` (line 69) and one test method in `TestTextLogisticRegressionQParserPlugin` (line 51), consistent with "1 of 1". Verdict: hold. The record has no pre-fix outcome (finding 14). The changelog understates the weight and order change (finding 13), and the order question is still open. The queue row is stale (finding 16).

## Owner decisions

1. Confirm PR 5012's state on GitHub. Git shows it merged as 93e357bcf6b. If merged, retire "awaiting merge" in the claim, assignment, receipt, and inventory row.
2. If 5012 is still open, decide whether to fix its body (findings 2 to 5). The owner's no-tidy rule for approved PRs applies.
3. Confirm the approval covers c0aec3a7ae0 and not an earlier head. The record gives the approval and the scope push both on 2026-10-06, with no order.
4. For PR 5004: choose merge of `upstream/main` (no force) or rebase (force push, needs explicit direction).
5. For PR 5004: drop the 10/10 count (finding 8), set the corroboration wording after checking run 37581056636 (finding 9), and decide whether to keep the fail-before wording and the SolrServerException sentence (finding 10).
6. For PR 5012: record per-class counts for `TestAuxIndexJoinQParserPlugin` and `TestGlobalOrdinalsJoinQParser` at c0aec3a7ae0, or accept the gap.
7. For 13838: record a pre-fix outcome (a run is owner-only verify work under the Gradle rule) before any Proof claim, or hold.
8. For 13903: record a pre-fix outcome, settle the changelog wording (finding 13), and decide the training-order question from round-28 Finding 1.
9. Update the commit fields in `research/pipeline/queue.json` for 13838 and 13903, or mark them historical.

## Not checked

- PR `state`, `reviewDecision`, the approval commit, and the merge time on GitHub. These fields are outside the allowed `gh` calls, so the merge finding rests on local git only.
- Whether the `pr checks` runs for 5012 and 5004 ran at the live head SHAs. The output has no SHAs.
- The conclusion of Actions run 37581056636.
- The gate logs and ledger rows named in the records (`g13202ext-gate.log`, `g13202-13568-proofs.log`, `g16130r30-gate.log`, `g16130r30-failbefore.log`, the reconcile ledger rows for 13838 and 13903, the takeover log). I searched `research/`, `research/test-queue/`, and the worktree, and none is on disk.
- Jira text for all four tickets. No packet exists in `research/jira-context/` for them.
- No builds, tests, or Gradle runs. The "1 of 1" and "10/10" counts were checked only by counting `@Test` and test methods in the head files, and are not run results.
- The exact NPE and `SolrServerException` text in PR 5004, and the `useFilterForSortedQuery` mechanism (the sorted export path skipping `getLeafCollector`), beyond the configset change itself.
- Lucene version claims. Neither live body names a Lucene version, and no draft was written.
- 11391's own JoinQParserPlugin hunk (owned by q3). Only the textual overlap with 13202 was checked.
- No `gh` calls beyond the four allowed read-only calls. Nothing posted, committed, pushed, or fetched.
