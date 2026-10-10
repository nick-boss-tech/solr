# Flaky-fix post-PR review round 2, slice 1: SOLR-18530 (live PR 5098)

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-2.md`, slice 1. Read-only: no PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle run, test run, Selenium run or gate run. The only file written is this report. The round 1 items are in `reports/flaky-fix-post-pr-review-round-1-s1.md` and `reports/flaky-fix-post-pr-review-round-1.md`.

## Verdict

**REMAINING. Not satisfied yet.** Round 1 items 1 (links) and 5 (receipt head) are cleared. The title, the routing claim and the Proof numbers are correct at the head. Three body wording items and three record items remain. None changes a link target, a count or a code claim, except B3, which is a precision gap in one sentence.

Body items for the main side:

- **B1. "A choice to check" bold line.** It says "use a leaders-only client". The same section's question says "a leaders-first client", and the rest of the body says "leaders first". Change the bold line to "leaders-first client".
- **B2. Changelog line.** The last line still starts with "Changelog:" and paraphrases the exemption. Round 1 F4 asked for the section 4 sentence in place of that line. Use the doc's own sentence (see item 4).
- **B3. "Forward" sentence, precision.** "A failed send to a replica is logged and does not fail the add" is too broad. A replica that reports `LeaderChanged` gets its error returned to the client (`DistributedZkUpdateProcessor.java` lines 1254-1262). Add that exception, or narrow the sentence.

Record items (no body or receipt edit needed, but the workspace has no record yet):

- **R1. D2.** Neither the receipt nor the body records a decision to open without the N = 20 base runs or the CI-log port check.
- **R2. D4.** The body keeps "A choice to check". No record says Nick chose to keep it.
- **R3. D5, D1 to D4.** The opening is recorded (Nick's direction, `assignments/pool-flaky-fix-post-pr-review-round-1.md` line 5). The decisions D1 to D4 that the opening was held for are not recorded anywhere in the workspace.

Optional, not blocking: O1 to O4 in section 6.

## 1. Head and inputs

- Remote check: `git ls-remote origin refs/heads/solr-18530-submit` = `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`. Live `headRefOid` = the same. **Match.**
- Read-only fork fetch done. `refs/remotes/origin/solr-18530-submit` now = `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`.
- Base to head: `98e5368d996` (test change plus changelog file) then `14868bc7a7f` ("SOLR-18530 Drop the changelog entry", deletes `changelog/unreleased/SOLR-18530.yml`). Author and committer Nick Shanin. Test file changed in one commit only; the diff `98e5368` to `14868bc` is the changelog deletion alone.
- Receipt and draft source: the worktree HEAD `12b80c539a7` (detached, working tree clean). **Caveat:** `origin/pr-prepare` on the remote is at `90de0ce0894d`, which is not present in this worktree and was not fetched (only the fork fetch is in scope). If the receipt or draft changed there, items 5 and 7 need a re-read.
- Live body vs `pr-drafts/flaky-fixes/SOLR-18530.md` (worktree): identical after stripping CRs, except one trailing newline from the `gh` output. Live body is 4,739 bytes.
- Live title: "SOLR-18530: Route adds after the proxy cut in RecoveryAfterSoftCommitTest to the shard leaders first". The PR was renamed by nick-boss-tech at 2026-10-10T20:15:20Z, after round 1.

## 2. Item table

Head = `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`. "Body" = live PR 5098 body.

| Item | State at head | State in live body | Verdict |
|---|---|---|---|
| 1. Links at head (round 1 F1) | Four cited ranges show the claimed code: `RecoveryAfterSoftCommitTest.java` L102-L117 (comment, then leaders-first client loop); `DistributedZkUpdateProcessor.java` L1229-L1231 (`log.warn` for non-forward errors); `LBSolrClient.java` L675-L680 (non-retryable connect or RequestNotSent branch); `CheckRetryUnrollTest.java` L97-L103 (ClosedChannelException asserted not retried). | All four links use `blob/14868bc7a7f5ec5c4fa1ade5f894cbda9533307d/`. No `98e5368` in the body. | SATISFIED |
| 2. Routing wording and title (round 1 F2, D3) | Random flag: `SolrTestCaseJ4.java` L2517-L2521 (`directUpdatesToLeadersOnly = random().nextBoolean()` at L2518). Leader-only list only when the flag is true (`CloudSolrClient.java` L454-L468); when false, leader first then all other replicas (L470-L482). The builder call `sendUpdatesOnlyToShardLeaders()` sets only `shardLeadersOnly` (L1416-L1419), not the route flag. | Title and bold line now say "to the shard leaders first". Limits says the flag is randomized, the cut replica can stay on the failover list, and the leader is tried first. "A choice to check" bold line still says "use a leaders-only client" (B1). | Title, bold line and Limits SATISFIED. Choice bold line REMAINING (B1). |
| 3. "Forward" sentence (round 1 F3) | `ForwardNode` errors are returned to the client (`DistributedZkUpdateProcessor.java` L1219-L1225). Leader-to-replica `StdNode` errors are logged (L1229-L1231). Leader-to-replica nodes are built at L938-L944. `LeaderChanged` `StdNode` errors are returned (L1254-L1262). | No longer says "forward" for a leader-to-replica send. New sentence: "a failed send to a replica is logged and does not fail the add", which omits the `LeaderChanged` exception (B3). | "forward" wording SATISFIED. Sentence precision REMAINING (B3). |
| 4. Changelog exemption (round 1 F4, D1) | No `changelog/unreleased/SOLR-18530.yml` at head (`git ls-tree -r`: none). Removed in `14868bc` (7 deletions). `dev-docs/changelog.adoc` lines 130-131: "Pull requests that only touch documentation, tests, build files or CI configuration are exempt and pass without an entry." | Last line: "Changelog: none. Test-only changes are exempt from the changelog (dev-docs/changelog.adoc), so this change has no entry." Substance right. The wording is a paraphrase and keeps the "Changelog:" label. | REMAINING (B2), wording only |
| 5. Receipt names the gate head (round 1 F5) | Blob `solr/core/src/test/org/apache/solr/cloud/RecoveryAfterSoftCommitTest.java` = `cf399bd1244cf7887150c286e66b3adc5def4a6c` at both `98e5368d996` and `14868bc7a7f`. Identical. | Not a body item. Receipt line 4: "Gated head: 98e5368d99... The gate runs below all used this head." Line 5: current tip `14868bc7a7f`. Line 11: the gate at `98e5368d99` carries over. | SATISFIED |
| 6a. D2, more evidence before opening | Not run. | Body says the fix is not shown ("not shown to fix it"; Limits first bullet). No record of a decision to skip the N = 20 base runs or the CI-log port check. | REMAINING (R1) |
| 6b. D4, keep or drop "A choice to check" | Not a code item. | Kept, with a bold summary and a pointed question. The alternative (retry in the test) is a live route with a cost (any-replica writes kept). Fits pr-formula section 4. No recorded decision. | REMAINING (R2), record only |
| 6c. D5, record of Nick's decision | Not a code item. | Opening: settled by Nick's direction in `assignments/pool-flaky-fix-post-pr-review-round-1.md` line 5 ("Nick directed that the PRs be posted as they become available"). D1 to D4 decisions: not recorded. | Opening SETTLED (where stated). D1 to D4 REMAINING (R3) |
| 6d. Round 1 finding 1 (test comment states cause as fact) | `RecoveryAfterSoftCommitTest.java` L102-L106 still say the add "can fail with a ClosedChannelException after the write has committed". Also L102 says "leaders-only client" and L105 says "The leader forwards to the cut replica". | Body hedges correctly ("Our reading of the cause, which these runs do not prove"). | Code nit, OPTIONAL (O1) |
| 6e. Round 1 findings 3 and 5 (fewer repetitions; other proxy tests unchecked) | n/a | Limits: "The failure did not reproduce in the runs above"; "Other tests that cut a replica's proxy ... I have not checked them." | SETTLED by disclosure |
| 6f. Round 1 finding 2 (gate log only on vm1) | Not on disk in this workspace. | n/a | Cannot check here; unchanged |
| 7. Title accuracy and Proof numbers | See section 3. | | Title SATISFIED. Numbers SATISFIED. Head attribution note (O2). |
| 8. CI and reviews | See section 4. | | No findings. Three upstream checks still `action_required`. |

## 3. Title and Proof check

**Title.** "Route adds after the proxy cut in RecoveryAfterSoftCommitTest to the shard leaders first." At the head, the post-cut adds use a client built with `sendUpdatesOnlyToShardLeaders()` (`AbstractFullDistribZkTestBase.java` L2431-L2449, test L108-L110). Leader first is the right description. The routing flag is random, so "only" would be wrong. **Accurate.**

**Bold line.** "After the cut, this test sends its adds to the shard leaders first." Matches the code.

**Proof numbers against `receipts/SOLR-18530.md` (worktree HEAD):**

| Body | Receipt | Result |
|---|---|---|
| 6 of 6 runs passed | Line 8: fixed test ran 6 times (CI seed plus 5 random seeds), 0 failures | match |
| CI seed `A57A22E346C237C6` | Line 8: same seed | match |
| Five random seeds | Line 8: "the CI seed plus 5 random seeds" | match |
| 5 of 5 base runs at the CI seed | Line 8: unmodified base test ran 5 times at the CI seed, 0 failures | match |
| Checked 2026-10-10 | Line 3: gate finished 2026-10-10 | match |
| "at 14868bc7a7f5..." | Line 4: the runs used `98e5368d99`. Line 11: the gate carries over to `14868bc7a7f` (changelog-only delta) | match by carry-over; the body does not say so (O2) |

No other numbers in the Proof section.

## 4. CI and review state

Head `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`. PR state: OPEN, draft, `mergeStateStatus` UNSTABLE, `reviewDecision` empty.

| Check | Run id | Head SHA | State |
|---|---|---|---|
| labeler (Pull Request Labeler) | 38079120890 | 14868bc7a7f | success (completed 2026-10-10T19:16:19Z) |
| Gradle Precommit | 38079120736 | 14868bc7a7f | `action_required`, not run |
| Solr Tests via Crave | 38079120826 | 14868bc7a7f | `action_required`, not run |
| Validate Changelog | 38079120887 | 14868bc7a7f | `action_required`, not run |

- `statusCheckRollup` lists only the labeler check (SUCCESS).
- Reviews: none (`pulls/5098/reviews` is `[]`). Inline review comments: none (`pulls/5098/comments` is `[]`). Issue comments: none (`issues/5098/comments` is `[]`).
- Timeline: `github-actions[bot]` added labels "tests" and "cat:cloud" at 2026-10-10T19:16:16Z. nick-boss-tech renamed the title at 2026-10-10T20:15:20Z. No other events.
- `action_required` is incomplete validation. When a maintainer approves those runs, compare each run's `head_sha` with `14868bc7a7f` before reading a failure as actionable.

## 5. Automated findings

- **Verified:** none. No automated review or comment exists on PR 5098.
- **Rejected:** none. The labeler's labels are not review findings.

## 6. Other observations (not round 1 items)

- **O1. Code comment, test lines 102-106.** It says "leaders-only client", states the `ClosedChannelException` cause as fact, and says "The leader forwards to the cut replica". The body's link to L102-L117 shows these words. A comment-only edit changes the blob, so the receipt would need a note. Main side decides.
- **O2. Proof head.** The Proof line names `14868bc7a7f` as the check head. The receipt says the runs were at `98e5368d99`. The blob is identical at both, so the numbers hold. Suggested wording: "Checked 2026-10-10 at 98e5368d996 (the test file is identical at 14868bc7a7f)".
- **O3. Formula conformance.** Body is 4,739 bytes against the roughly 3,500 guide. Limits has no bold summary line. The original-test Proof bullet has no verification date or head.
- **O4. Receipt line 3** says "GATE GREEN at the branch head". Line 4 settles which head was gated. Consider "at the gated head" for clarity.
- **Verified, no action:** the base test's `cloudClient` is random (`AbstractFullDistribZkTestBase.java` L368-L371 passes `random().nextBoolean()` as `shardLeadersOnly`), so "that client can send an add to the cut replica" is accurate in outline. `UpdateRequest` routes default to `sendToLeaders = true` (`UpdateRequest.java` L58). SolrJ treats updates as non-retryable (`LBSolrClient.java` L576), so the LBSolrClient claim holds for updates.
- **Not checked:** the "What happens today" claims about the exact exception text and the `ClosedChannelException` cause come from the ticket. The ticket was not read in this slice.

## 7. Suggested main-side edits (none made)

1. B1: in "A choice to check", change "use a leaders-only client" to "use a leaders-first client".
2. B2: replace the last line with: "Pull requests that only touch documentation, tests, build files or CI configuration are exempt and pass without an entry (dev-docs/changelog.adoc, section 4)."
3. B3: in the forward sentence, add: "unless the replica reports that it has become the leader (solr/core/.../DistributedZkUpdateProcessor.java lines 1254-1262, linked at the head)". Or narrow the sentence to the case the test exercises.
4. R1 to R3: record Nick's decisions on D2 (evidence), D4 (keep the choice section) and D1 to D4 for the opening. Round 1 D5 said none is recorded.
5. Optional: O1 to O4.
6. Re-run the body-equals-draft check and the links check after any edit.

## 8. Scope and reads

- Read: assignment slice 1 and Rules; `pr-formula.md`; `WORKFLOW.md`; `reports/flaky-fix-post-pr-review-round-1.md`; `reports/flaky-fix-post-pr-review-round-1-s1.md`; `receipts/SOLR-18530.md`; `pr-drafts/flaky-fixes/SOLR-18530.md`; `gates/SOLR-18530.md`; `claims/pool-flaky-fix-post-pr-review-round-1.md`; `claims/pool-flaky-fix-post-pr-review-round-2.md`; the pool pass reports (for decision records).
- Head code at `14868bc7a7f`: the test file; `AbstractFullDistribZkTestBase.java`; `SolrTestCaseJ4.java`; `CloudSolrClient.java`; `UpdateRequest.java`; `LBSolrClient.java`; `DistributedZkUpdateProcessor.java`; `CheckRetryUnrollTest.java`; `dev-docs/changelog.adoc`; `.github/workflows/validate-changelog.yml`.
- Live PR 5098, read-only: metadata, body, check runs and workflow runs on the head, reviews, inline and issue comments, timeline events.
- Git: read-only commands and the fork-branch fetch only. The worktree HEAD was not changed.
- Scratch copies of the live body and the draft are in the session scratchpad. One temporary copy of `CloudSolrClient.java` was written by mistake outside the scratchpad and deleted at once.
- Not done: no claim file change, no push, no DONE mark. Those are the lead's to make.
