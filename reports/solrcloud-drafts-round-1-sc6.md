# SolrCloud drafts round 1, slice SC6 (part report)

Date: 2026-10-11. Worktree: `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester` (branch pr-prepare).

Drafts written or corrected: none. No commit, push, build, test, gate or GitHub write was run. Reads only: `git ls-remote` on the five fork branches, `gh pr list` for nick-boss-tech PRs on apache/solr, and local `git cat-file` and `rev-parse` on two SOLR-12998 commits.

No file exists under `pr-drafts/solrcloud/` for any of the five tickets (checked with Glob on `pr-drafts/**`). None of the five has a draftable or drafted answer, so there was nothing to correct and nothing to write.

## Per ticket

- SOLR-17281: not drafted (parked, not ready). Answers: DISCUSS on revert or replace; recommendation revert; parked either way. Receipt: NO GATE, head `ba21ca32791` (solr-17281-submit, matches the live tip). No PR on apache/solr for this branch.
- SOLR-7394: not drafted (live PR #5003, OPEN, not a draft; consistency pass only). Head `9857d9ee802` is the gated head and the live tip. Answers: consistent, no gate owed. Receipt agrees on head and counts. Held: DISCUSS 12 (add the restore half to the live PR title and the branch changelog), the owner's call.
- SOLR-12998: not drafted (live PR #5010, OPEN, not a draft). Live tip `62a17a116b5` equals the receipt's gated head. Drift is in the live text and the receipt-versus-answers gate line; see gaps 1 and 2.
- SOLR-13136: not drafted (live PR #5017, OPEN, not a draft). Live tip `486b38775565f7e369d5429c356ca83d750d710a` is not gated; the gate is at `2ad44409ffe`. Held: DISCUSS on the behavior (keep the live CONSTRUCTION behavior, or restore the gated delete-on-failure). Recommendation keep the live behavior; the call is not taken.
- SOLR-18391: not drafted (two live PRs, both out of drafting scope). Submit branch: PR #4997, OPEN, draft, head `3000eeede7a` (equals the tip). Graceful-create branch: PR #5027, OPEN, not a draft, head `adcda10b501` (equals the tip). Held: DISCUSS on closing #4997 as superseded, recommendation close; not taken, PR state is the owner's call.

## Gaps and discrepancies (the receipt wins where it differs)

1. SOLR-12998 gate state. The answers' "Gate states" line says the gate covers older head `25520806c26`, and main-side item 5 owes a gate at `62a17a116b5`. The receipt records GATE GREEN AT HEAD `62a17a116b5` (ls-remote verified 2026-10-10), with the 2026-10-10 main-agent decision that the gate stands and the proof leg is inconclusive by construction. Receipt counts: SyncStrategyTest 9 tests, 0 failures, 0 errors; CoreAdminOperationTest 47 tests, 0 failures, 0 errors. The owed gate appears met on the receipt; the answers line is stale. The proof leg failed to compile at base (rc=1), so there is no proof count.
2. SOLR-12998 previous-head claim. The answers say the receipt names tidy commit `0f4eead1f99` for the claimed 2026-10-04 runner verification of `ee342298ba4`. The receipt names neither commit. Locally both commits exist and their trees are identical (`64296760999ce5714884846ec7225e762041c498`), so the sentence can be confirmed as same-tree only. The 2026-10-04 runner claim still has no receipt line.
3. SOLR-18391 graceful-create gate state. The answers say gated green at `3a0ff1262bf`, with the live tip `adcda10b501` eight commits past. `3a0ff1262bf` does not appear in the receipt. The receipt records the gated head as `adcda10b501` (ls-remote verified 2026-10-10): CreateCollectionCleanupTest 5 of 5, DeleteCoreRemnantsOnCreateTest 5 of 5, OverseerCollectionConfigSetProcessorTest 26 of 26, CreateCollectionCmdRetryTest 4 of 4, PlacementPluginIntegrationTest 8 of 8, all 0 failures and 0 errors. The answers' "not in the receipt" count claims (CreateCollectionCmdRetryTest 4 of 4; CreateCollectionCleanupTest 5 of 5 at the tip) are in the receipt at the tip. Main-side item 8 appears met.
4. SOLR-18391 graceful-create base run. Receipt: production `CreateCollectionCmd.java` and `PlacementPluginAssignStrategy.java` at merge-base, tests at head. CreateCollectionCmdRetryTest does not compile at base (inconclusive by construction). CreateCollectionCleanupTest at base: 2 failures of 5 (testCleanupAfterUnexpectedPlacementFailure on the message assertion; testCreateDoesNotDeleteExistingCollectionOnStaleView expects 400 and gets 500). PlacementPluginIntegrationTest.testAssignForMissingCollection fails at base; the other 7 pass. The receipt says "exactly 1 failure" does not hold at base, so any live PR text describing a single base failure is wrong. The answers' base-run setup wording (two production files at base) matches the receipt.
5. SOLR-18391 submit verification. The answers say the PR body's 2026-10-04 verification sentence is not in the receipt. The receipt now has a verification record for `3000eeede7a` dated 2026-10-04 (tidy rc=0, compile rc=0, Error Prone compile rc=0, `:solr:core:check -x test` rc=0, tree clean; logs g4997-verify.log and g4997-ep.log; recorded 2026-10-10). The branch has no focused tests, so there is no test count and no fail-before proof. Confirm-or-drop item 10 resolves toward keeping the sentence, subject to the DISCUSS on closing the PR.
6. Heads. All five fork branch tips (ls-remote, 2026-10-11) equal the PR heads from `gh pr list` and the receipt's head, except the receipt-side gate heads covered in gaps 1 and 3. No head discrepancy for SOLR-7394, 13136, 17281 or 18391 submit.
7. Owed on the live PRs and branches (not drafts, not touched here): SOLR-12998 live text ("The local gate passes" wording, the "(fixed)" parenthetical, and the CI sentence about TestUserManagedReplicationWithAuth; answers part p6 findings 4 and 5). SOLR-13136 Proof sentence and counts replaced after the behavior call, and title and changelog revisited (part p6 findings 1 and 13). SOLR-18391 graceful-create: changelog alias wording needs a branch commit (answers branch correction 7, part p6 finding 9); Proof "one later commit" should read four commits; "Round 29 follow-up" and "internal handoff document" are process wording; file links point at old upstream SHAs, not the fork blob at the live head (findings 6 to 10). Presentation hold on the graceful-create PR body: it carries an approving maintainer review, so no description edit without the owner's check.

## Holds (DISCUSS items, not taken)

- SOLR-17281: revert or replace the one-line change; recommendation revert. Parked.
- SOLR-7394: restore half in the live PR title and branch changelog; recommendation apply.
- SOLR-13136: keep the live CONSTRUCTION behavior or restore the gated delete; recommendation keep live. A gate at the live tip `486b38775565f7e369d5429c356ca83d750d710a` follows the call.
- SOLR-18391 submit (#4997): close as superseded or keep as a draft; recommendation close.

## Not verified in this pass

- Live PR titles and bodies were not re-read. Heads, states and draft flags come from `gh pr list` only.
- The approving maintainer review on #5027, the SOLR-17281 round 6 review and DECISION file, and the SOLR-12998 previous-head runner record were not read.
- No build, test, gate or test count was re-derived.
