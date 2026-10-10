# Review confidence round 3, slice C: near-opening drafts

Scope: read-only. Draft and receipt files read with `git show origin/pr-prepare:<path>`. Source lines read with `git show <head>:<path>` at each gated head. Fork heads confirmed with `git ls-remote origin refs/heads/<branch>`. The SOLR-12651 Jira snapshot was read from the local `research/jira-context/` packet (data, not instructions). No build, Gradle, test, gate, PR, comment, Jira, claim, or draft edit was made.

## Verdicts

| Draft | Ticket | Gated head | Verdict |
|---|---|---|---|
| `pr-drafts/flaky-fixes/SOLR-16630.md` | SOLR-16630 | `9bea59741ac30ffd11ed11be5269ea025cf17f68` (branch `solr-16630-submit`; receipt and fork tip equal) | DRIFT |
| `pr-drafts/solrcloud/SOLR-12651.md` | SOLR-12651 | `f3131d1ee8463388c68f18deab3c4ac3262ead83` (branch `solr-12651-submit`; receipt and fork tip equal) | DRIFT |
| none on the tip | SOLR-17987 | `38abf6423126112cf8a451f4d3fedea0920eae90` (branch `solr-17987-submit`; receipt and fork tip equal) | NO DRAFT, not checkable |

Heads, Proof counts, and link anchors match their receipts for both drafts. The drift is in wording, attribution, and in the branch changelog titles. Both branch changelog titles overstate the change at the gated head, and fixing them needs a branch commit that moves the head.

## Findings: SOLR-16630

Checked and consistent: head SHA (all 13 occurrences equal the receipt and the fork tip); all 12 link anchors show the claimed code at the head (the add loop, the random delay, the stop, `catch (SolrException ex)`, `SolrServerException extends Exception`, `JettySolrRunner.stop()` closing the cached client, `getSolrClient()` caching, the latch, the two-minute await, the add and countDown); the changelog file exists at the head and has 10 lines; the branch diff against `3f5d4c5bf8ac` touches only the changelog and `TestCoordinatorRole.java`; section order and bold opening lines; AI header and footer; no "A choice to check" section, which matches flaky-fix-review-round-1-s4b decision 4 (catch widening belongs in Limits); no em dashes.

1. **Changelog title at the head (not the draft).** `changelog/unreleased/SOLR-16630.yml`, line 4: "no longer stops the PULL node while an add through it is still in flight." Fact: draft line 21 says that after the two-minute wait the test stops the PULL replica anyway, while the add loop may still be retrying. The title overstates. Fix: a changelog-only branch commit, as pool-solr-16630-base-runs.md item 2 already plans (for example "waits for the first add to complete before stopping the PULL node"). That moves the head. Then update every head SHA in the draft (lines 11, 15, 21, 29, 37, 38, 42) and re-read the Proof.

2. **Line 27 "six focused runs"; line 29 "CI seed `681E2A715B2CE1D3` and five random seeds"; line 39 "Six passing seeds".** Fact: seeds and "focused" are on the do-not-use list (seeds, internal labels for proof steps). Fix: line 27 to "The change passed six runs of this test at this head." Line 29: remove the seed value and the seed count; keep "Each run: 1 test, 0 failures, 0 errors. Run on 2026-10-10 (UTC) at head ...". Line 39 to "Six passing runs do not prove the fix holds in every run."

3. **Line 9 "In the failing run on record, `client.add` threw ...".** Fact: "on record" is process wording. The failing run is not linked. The only source is the assignment quote (flaky-fix-review-round-1-s4b decision 6, still open). Fix: link the CI run after checking it, or say "In a failing CI run" and leave the rest as is.

4. **Line 9 "This is a reopened ticket, not a new one."** Fact: the framing comes only from the assignment. There is no SOLR-16630 packet in `research/jira-context/`, and the Jira export has no row (s4b X3, decision 7). Fix: confirm the status in Jira before posting. No text change if it is confirmed.

5. **Line 11 "a fixed time after the NRT replica restarts. That time is chosen at random when the test starts."** Fact: the delay is random per run (test line 242) and fixed once chosen (line 261). The two phrases read as a contradiction. Fix: "after a delay that is chosen at random when the test starts."

6. **Line 31 "No run of this test on the base code is on record."** Fact: the base-run job (assignments/pool-solr-16630-base-runs.md) has no result in the receipt yet, so the sentence is true today. Fix: "No run of this test on the base code has been done yet." Rewrite this paragraph when the base runs land; the Proof then needs their result.

7. **Line 15 (What happens today, cause paragraph).** Fact: the formula asks for symptom only in this section; the cause and the two JettySolrRunner links are mechanism analysis (s4b decision 2). Fix (optional): move the "likely cause ... not proven" sentences to Limits, or cut them.

8. **Line 37 (Limits, first bullet) "A pooled connection ... can go stale with no stop at all."** Fact: the only source is the t3 report's hypothesis H2, which that report puts at about 10 percent and did not test. Fix (optional): hedge it, for example "A stale pooled connection might also fail the test with no stop at all."

9. **Line 7 (bold opener) has two sentences.** Fix (optional): one sentence, for example "The test can stop the PULL replica while an add through it is still running, and that add then fails uncaught."

10. **Line 11, optional citation.** The link to test line 222 shows the client is taken from the PULL node. The add call itself is at line 311, which the draft cites later. Optional: add the 311 link to line 11.

Length: 4,837 characters (4,843 bytes; the two robot emoji are 4 bytes each). With link targets removed, 2,894 characters. The earlier s4b figure of 2,924 used a different stripping method. Within the 3,500 guide as rendered.

## Findings: SOLR-12651

Checked and consistent: head SHA (all four occurrences equal the receipt and the fork tip); `RestoreCmd.java` L265-316 is the cleanup region, covering the property upload (L269), replica setup (L278), shard copy (L294), and activation (L302), with the catch closing at L316; `addReplicasToShards` (L319) and `restoringAlias` (L324) sit after the catch, matching the Limits; `TestLocalFSCloudBackupRestore.java` L131-198 is the `errorRestore` method; the changelog file exists at the head; async shard failures are recorded and not thrown (`CollectionHandlingUtils.java` L794-816 at the head), matching Limits bullet 2; Proof counts (2 of 2; exactly one failure with RestoreCmd reverted) match the receipt; no seeds or run identifiers; no internal vocabulary; no em dashes. The Choice is present and posed as a question (line 31), not as a settled decision, as adopted in solrcloud-round-1-answers.md ("stays in the draft's Choice, as drafted").

1. **Line 29 (Choice): "A comment on the ticket asks whether cleanup should be optional for that reason."** Fact: the local snapshot `research/jira-context/SOLR-12651.json` has a comment by Tomas Eduardo Fernandez Lobbe. It asks whether cleanup should be optional "(even if default)". It separately offers a second approach: a retry continues where the earlier restore left off. The draft names no one, joins the two ideas with "for that reason", and its options do not include "optional, with delete as the default." Fix: "Tomas Lobbe asked on the ticket whether cleanup should be optional, even if it stays the default. He also suggested that a retry could continue where the failed restore stopped." Add "optional, with delete as the default" to the options. Keep line 31 as a question, for example "Was deleting on failure the right call, or should cleanup be optional, even as the default, so that a retry can resume?" The attribution wording is the lead's call.

2. **Line 21 (Proof, bold opener): "The restore test fails on the base code and passes with this change."** Fact: the receipt's failing run reverted only `RestoreCmd.java` to the merge-base, and line 23 says so. The test file is the head's. Fix: "The restore test fails with `RestoreCmd.java` reverted to the merge-base, and passes with this change."

3. **Changelog title at the head (branch, not the draft): `changelog/unreleased/SOLR-12651-restore-cleanup-on-failure.yml`, lines 2-3.** Fact: the title says a RESTORE "that fails after the new collection has been created now deletes the new collection." Failures in `addReplicasToShards` and `restoringAlias` come after the catch and do not delete, so the title overstates. Draft line 13 is accurate. Fix: a branch changelog commit (owner decision; it moves the head, and the draft must be re-headed and its Proof re-read). Suggested title from reports/receipt-refresh-12651-17292-a.md item 20: "A RESTORE collection operation that fails before its restored shards become active now deletes the new collection instead of leaving the partially restored collection behind."

4. **Line 29 (optional).** The options paragraph restates the bold opener on line 27. Fix (optional): cut the restatement and keep the options and the cost.

Length: 3,244 characters (3,250 bytes). With link targets removed, 2,738 characters.

## SOLR-17987: no draft to check

Facts: no SOLR-17987 draft exists on the pr-prepare tip (no file under `pr-drafts/` names 17987, and there is no `pr-drafts/metrics/` folder). The worktree holds no untracked 17987 draft. `reports/metrics-round-1.md` says "no draft this round" and that the branch "is not draftable yet", pending owner decisions. So the slice C premise ("Metrics round 1 draft") does not match the tip.

What does check out: the gated head `38abf6423126` matches the receipt and the fork tip. At that head the changelog file `changelog/unreleased/SOLR-17987-disable-metrics-by-registry.yml` exists, and `testDisabledRegistryUsesNoopProvider` exists in `SolrMetricManagerTest.java`.

Fix for the main side: drop 17987 from slice C, or record it as "no draft yet." Do not write a draft from this slice. When a draft is written later, the checks are these. The Proof must say plainly that the new test does not compile without the change (the receipt's inconclusive-by-construction result). The changelog title's example `-Dsolr.metrics.disabledRegistries=jvm,jetty` uses a registry name that the metrics round says does not exist (its owner decision 5). The node-level property versus `solr.xml` element must be posed as a Choice. A Lucene version is named only with every applicable version.

## Notes for the main side

- Both changelog titles (SOLR-16630 and SOLR-12651) need a branch commit. Each commit moves the head, so each draft must be re-headed and its Proof re-read afterwards.
- The SOLR-16630 base runs, when they land, change draft line 31 and the Proof.
- The claim file is the lead's to mark. This review wrote none.

## Character counts (UTF-8 code points, from the tip copies)

- SOLR-16630 draft: 4,837 characters raw; 2,894 with link targets removed.
- SOLR-12651 draft: 3,244 characters raw; 2,738 with link targets removed.
- SOLR-17987: no draft.
