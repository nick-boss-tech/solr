# Flaky-fix post-PR review round 1, slice 1: SOLR-18530 (live PR 5098)

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-1.md`, slice 1. Reviewed by the Windows review subagent. Read-only: no PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle run, test run, Selenium run or gate run. The only file written is this report.

## Verdict

**DRIFT (not CONSISTENT).** The live body equals the draft at the worktree HEAD line by line. The live head equals the fork tip. Every Proof number matches the receipt. Four items need the main side:

1. **F1, links.** The four code links in the body point at `98e5368d996`, not at the live head `14868bc7a7f`. The file content is identical at both SHAs.
2. **F2, routing claim.** The bold line "sends its adds only to the shard leader" and the title "to leaders only" are stronger than the code when the test helper's random routing flag is off. The cut replica then stays in each route's failover list. This is round 1 D3, still open.
3. **F3, wording.** "A failed forward is logged" uses "forward" for a leader-to-replica send. The cited lines use "forward" for a different node type, and there a failure is returned to the client.
4. **F4, round 1 D1.** The exemption sentence that round 1 asked for in place of the Changelog line is not in the body.

CI state: only the labeler check has run (success). Gradle Precommit, Solr Tests via Crave and Validate Changelog are `action_required`, so they have not run. There are no reviews, no inline review comments and no issue comments.

## 1. Head check

- Live `headRefOid`: `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`. PR is draft, OPEN, head repo `nick-boss-tech/solr`, head ref `solr-18530-submit`, base `main` at `3f5d4c5bf8ac`.
- Fork tip (`git ls-remote origin refs/heads/solr-18530-submit`): `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`. **Match.**
- The local object was missing, so a read-only fetch of the fork branch was run. It moved the local remote-tracking ref `origin/solr-18530-submit` from `98e5368d996` to `14868bc7a7f`. No remote state changed.
- Commits from base to head:
  - `98e5368d996`, "SOLR-18530: send post-partition writes in RecoveryAfterSoftCommitTest through a leaders-only client". Adds `changelog/unreleased/SOLR-18530.yml` and changes `RecoveryAfterSoftCommitTest.java`.
  - `14868bc7a7f`, "SOLR-18530 Drop the changelog entry". Deletes the YAML.
- Base-to-head diff: one file, `RecoveryAfterSoftCommitTest.java`, 15 insertions and 5 deletions. Author and committer are Nick Shanin. No trailers.

## 2. Title

Live title: "SOLR-18530: Route adds after the proxy cut in RecoveryAfterSoftCommitTest to leaders only".

The title describes what the head requests: the adds after the cut use a client built with `sendUpdatesOnlyToShardLeaders` (test lines 107-117). It carries the routing caveat in F2. The title needs no change unless F2 is settled by rewording.

## 3. Body vs draft

- Reference: `pr-drafts/flaky-fixes/SOLR-18530.md` at the worktree HEAD (the committed blob, last changed in `63e0971c954`). The worktree copy has CRLF line endings from checkout conversion. Both were compared after stripping CR.
- Live body: 4,184 bytes, about 4,178 characters.
- **Result: no differences.** Text, links, numbers, section order, the AI header and the AI footer are identical.
- The body has no "Changelog:" line. That matches the head, which has no changelog file.

Observations, not drift (for the main side to decide):

- Length: about 4,178 characters. The pr-formula guide asks for roughly under 3,500 unless the ticket is complex. Round 1 measured about 3,550 for an earlier draft.
- Presentation rule: the Limits section opens with bullets, not a bold one-line summary.
- Proof: the second bullet (the original test) has no verification date or head. pr-formula asks for both on each test line.

## 4. Proof check

| Body (Proof) | Receipt | Result |
|---|---|---|
| Fixed test: 6 of 6 runs passed | line 7: fixed test ran 6 times (CI seed plus 5 random seeds), 0 failures | match |
| CI seed `A57A22E346C237C6` | line 7: same seed | match |
| Five random seeds | line 7: "the CI seed plus 5 random seeds" | match |
| Checked 2026-10-10 | lines 3-4: gate finished 2026-10-10 | match |
| At `98e5368d996518b9f4a94f85d7c2fcd933d3f485` | line 10: "the gate at 98e5368d99 carries over" | match (receipt header differs, see below) |
| Original test: 5 of 5 passed at the CI seed | line 7: unmodified base test ran 5 times at the CI seed, 0 failures | match |

- No other numbers in the Proof section. Numbers in other sections ("30 seconds", "3 documents") are code values and were checked in section 5.
- **Receipt inconsistency (not edited here).** Receipt line 4 names the gated head as `14868bc7a7f`. Line 10 says the runs were at `98e5368d99` and carry over. The test file is identical at both SHAs, so no number changes. The receipt header should name the head the runs used. The body's `98e5368` follows the runs.
- Receipt line 7 says the base-run result is "consistent with the earlier settling runs". Those runs are not in the workspace, and the body does not cite them.

## 5. Citation check

All four links in the body point at `github.com/nick-boss-tech/solr/blob/98e5368d996518b9f4a94f85d7c2fcd933d3f485/...`. None points at the live head. pr-formula section 4 requires the PR head SHA.

| Link in body | Lines | Code at head (`14868bc7a7f`) | Result |
|---|---|---|---|
| `RecoveryAfterSoftCommitTest.java` | L102-L117 | Comment (102-106) and the leaders-only loop (107-117) | content OK; SHA wrong (F1) |
| `DistributedZkUpdateProcessor.java` | L1217-L1240 | Error loop: ForwardNode errors returned (1219-1225); other errors logged (1229-1231) | content OK for the claim; wording (F3) |
| `LBSolrClient.java` | L675-L680 | Non-retryable branch: move to another endpoint only on connect exception or RequestNotSent | content OK; SHA wrong (F1) |
| `CheckRetryUnrollTest.java` | L97-L103 | `testClosedChannelExceptionIsStillNotRetriableEitherWay` asserts ClosedChannelException is not retried (102-103) | content OK; SHA wrong (F1) |

**F1 fix:** re-point the four links to `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`. The content is identical at both SHAs.

Unlinked claims, checked against the head:

- "The document count is unchanged, so the replica still misses more updates than its update log keeps." True. `MAX_BUFFERED_DOCS = 2` and `ULOG_NUM_RECORDS_TO_KEEP = 2` (test line 37). `MAX_DOCS = 6` (line 107). The post-cut adds are i = 3 to 5, which is 3 documents, more than 2.
- "The test checks only that replicas become active within 30 seconds." True. Line 130 passes 30 as `maxWaitSecs` to `ensureAllReplicasAreActive` (`AbstractFullDistribZkTestBase` line 2728).
- "The change touches only this test. No product code changes." True. The base-to-head diff is one test file.
- "SolrJ moves an update to another endpoint only when it could not connect or the request was never sent." True for non-retryable requests (`LBSolrClient` lines 658-683). A ClosedChannelException root cause is not in the move list.
- "The leader still forwards each add to the cut replica." True. The leader builds `StdNode` entries for live, non-DOWN replicas (`DistributedZkUpdateProcessor` lines 938-944).
- "A failed forward is logged and does not fail the add." True for a leader-to-replica failure, which is a `StdNode` (lines 1229-1231). See F3 for the wording problem.

## 6. Changelog check

- Head has no `changelog/unreleased/SOLR-18530.yml` (`git ls-tree` at the head: none). Commit `98e5368d996` has it: type `fixed`, author Nick Shanin, one JIRA link, and a title that describes test internals.
- The body mentions no changelog. This is consistent with the head.
- `dev-docs/changelog.adoc` section 4 (lines 130-131) exempts PRs that touch only tests. That matches the drop.
- `.github/workflows/validate-changelog.yml` skips the entry requirement when no non-test, non-doc file changed (the path regex includes `^solr/.*/test`). The only changed file is under `solr/core/src/test`, so the check should pass when it runs. Not confirmed: the run is `action_required`.

## 7. CI and review state

Head `14868bc7a7f`. Check runs and workflow runs on the head SHA:

| Check | Run | State | Note |
|---|---|---|---|
| labeler (Pull Request Labeler, `pull_request_target`) | 38079120890 | success | added labels "tests" and "cat:cloud" |
| Gradle Precommit | 38079120736 | completed, `action_required` | not run |
| Solr Tests via Crave | 38079120826 | completed, `action_required` | not run |
| Validate Changelog | 38079120887 | completed, `action_required` | not run |

- Live `statusCheckRollup` shows only the labeler check (success).
- `mergeStateStatus` UNSTABLE; `mergeable` true; `reviewDecision` empty; draft true.
- Combined commit status: pending, 0 statuses.
- The three upstream workflows need maintainer approval for this fork PR. The local green gate in the receipt is not a substitute for them. When they run, compare each run's `head_sha` with `14868bc7a7f` before reading a failure as actionable.
- Reviews: none (`pulls/5098/reviews` is empty). Inline review comments: none. Issue comments: none. The only timeline events are two "labeled" events by `github-actions[bot]` at 2026-10-10T19:16:16Z.
- `gh pr view --comments` fails with this token (GraphQL needs the `read:org` scope). The same data was read through the REST endpoints, and all three lists are empty.

## 8. Automated findings

None exist on this PR. Nothing was verified or rejected as automated output. The only bot activity is the labeler. The findings in section 9 come from this review's own check against the head code.

## 9. Findings

**F1. Link SHA (drift).** All four links use `98e5368d996`. pr-formula requires the PR head. Re-point them to `14868bc7a7f`. Content is identical.

**F2. Routing claim is stronger than the mechanism (drift, ties to round 1 D3).**

- Body, bold line in "What this change does": "After the cut, this test sends its adds only to the shard leader." Body text: "a new client that sends updates only to shard leaders." Title: "to leaders only."
- Mechanism at the head:
  - The test calls `createNewCloudSolrClient(..., true, ...)` (test line 110). That calls `sendUpdatesOnlyToShardLeaders()` (`AbstractFullDistribZkTestBase` line 2440). `UpdateRequest.sendToLeaders` defaults to true (`UpdateRequest` line 58), so adds take the direct-update path (`CloudSolrClient` lines 902-926).
  - Each slice's endpoint list comes from `buildUrlMap` (`CloudSolrClient` lines 445-488). With `directUpdatesToLeadersOnly` true, the list is the leader only. With it false, the list is the leader first, then every other replica, including the cut one (lines 470-482).
  - The flag is random. `RandomizingCloudSolrClientBuilder.randomizeCloudSolrClient()` sets it with `random().nextBoolean()` (`SolrTestCaseJ4` line 2518). Each constructor calls it (lines 2486, 2492, 2503). The class default is false (`CloudSolrClient` line 1297).
  - Each route is an `LBSolrClient` request whose endpoints are that list (`UpdateRequest` lines 246-266).
- Effect: with the flag true, adds go only to the leader. With the flag false, the cut replica is a second endpoint. It is used only if the leader refuses the connection or the request was never sent (`LBSolrClient` lines 675-680). The leader is not cut in this test, so in practice adds reach the leader in both settings.
- The Limits line discloses the randomization ("The leader is still tried first"). It does not say the cut replica stays on the list.
- Options for the main side (owner decision, D3):
  - (a) Reword the bold line and the title, for example "the adds after the cut go to the shard leader first; the cut replica stays a fallback when the random routing flag is off."
  - (b) Set `directUpdatesToLeadersOnly` explicitly on the builder, so the list is the leader only. This needs a new gate run, because the six passing runs used the random setting.

**F3. "Forward" wording (drift).**

- Body: "The leader still forwards each add to the cut replica. A failed forward is logged and does not fail the add."
- In the code, "forward" means a non-leader sending to the leader. That is `ForwardNode`, created at `DistributedZkUpdateProcessor` lines 509-520 and 789-799.
- The cited range opens with "if it's a forward, any fail is a problem" (lines 1219-1225). A `ForwardNode` error is returned to the client.
- A leader-to-replica send is a `StdNode` (lines 938-944). Its failure is logged and not returned (lines 1229-1231). The body's claim is correct for this case.
- A reviewer who clicks the link will read the opposite rule first. Suggested wording: "The leader still sends each add to the cut replica. A failed send to a replica is logged and does not fail the add (lines 1229-1231)." Re-check the link range after the edit.

**F4. Round 1 D1 exemption sentence not applied (drift).** Round 1 D1 said: "If dropped, the draft's last line should become the section 4 exemption wording, not the Changelog line." The head drops the entry (receipt line 10). The body has neither the Changelog line nor the exemption sentence (`dev-docs/changelog.adoc` lines 130-131). Add the sentence, or record that the owner chose none.

**F5. Receipt header (not in the PR body).** Receipt line 4 names `14868bc7a7f` as the gated head. Line 10 says the gate ran at `98e5368d99`. The receipt is the main side's file and was not edited here.

**Formula conformance (not drift, main side decides).** About 4,178 characters against the roughly 3,500 guide. Limits has no bold summary line. The original-test Proof line has no verification date or head.

## 10. Round 1 item status (SOLR-18530)

| Item | Status at head and in live body | Still open? |
|---|---|---|
| D1 changelog | Entry dropped at head (`14868bc7a7f`; receipt line 10). Body has no Changelog line, which is consistent. The exemption sentence was not added (F4). The workspace records the drop, not who chose it. | Yes (F4 and the decision record) |
| D2 more evidence | Not done. Receipt: 5 base runs, 0 failures; 6 fixed runs, 0 failures. No CI-log port check (t2 step 0). No N = 20 base runs (t2 step 1). Body says the fix is not shown. The PR was opened as a draft without a recorded D2 decision. | Yes |
| D3 helper routing | Head keeps the random flag (`SolrTestCaseJ4` line 2518). Limits discloses it. The bold line and title say "only" (F2). | Yes |
| D4 choice section | Live body keeps "A choice to check". No keep-or-drop decision is recorded. | Yes, for confirmation |
| D5 opening | PR is open as a draft (opened 2026-10-10 per the assignment). Round 1 held the opening for D1 to D4. No record of Nick's approval for D1 to D4 in the workspace. | Main side to record |
| Round 1 finding 1 (test comment states the cause as fact) | Still in head test comment, lines 102-106: "...after the write has committed...". The body hedges correctly, so this is a code nit only. | Yes (nit) |
| Round 1 finding 2 (gate log not in workspace) | Unchanged. The gate log is on vm1. | Yes (cannot check here) |
| Round 1 findings 3 and 5 (fewer repetitions than the t2 plan; other proxy tests not checked) | Limits names both. | Settled by disclosure |

No round 1 item for SOLR-18530 was dropped without a trace. D1's exemption wording (F4) and finding 1 are the two items that are not in the body and are not recorded as decided.

## 11. Suggested main-side actions (none taken here)

1. Re-point the four body links to `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` (F1).
2. Reword the "forward" sentence and re-check the cited range (F3).
3. Decide F2 / D3: reword the bold line and title, or set the routing flag explicitly and run a new gate.
4. Add the D1 exemption sentence, or record that none is wanted (F4).
5. Record Nick's decisions on D1 to D4 and on the opening. Correct receipt line 4 (F5).
6. After any body edit, re-run the body-equals-draft and head checks.
7. When a maintainer approves the three `action_required` runs, compare each `head_sha` with `14868bc7a7f` before acting on a failure.
8. Optional formula conformance: length, a bold summary line for Limits, and date and head on the original-test Proof line.

## 12. Scope and reads

- Read: assignment slice 1 and Rules; `pr-formula.md`; `WORKFLOW.md`; `reports/flaky-fix-review-round-1.md` and `reports/flaky-fix-review-round-1-s1.md`; `pr-drafts/flaky-fixes/SOLR-18530.md` (worktree HEAD blob); `receipts/SOLR-18530.md`.
- Live PR 5098, read-only: metadata, body, check runs and workflow runs at the head, reviews, inline and issue comments, timeline.
- Head code at `14868bc7a7f`: `RecoveryAfterSoftCommitTest.java`; `DistributedZkUpdateProcessor.java`; `SolrCmdDistributor.java`; `LBSolrClient.java`; `CloudSolrClient.java`; `UpdateRequest.java`; `AbstractFullDistribZkTestBase.java`; `SolrTestCaseJ4.java`; `CheckRetryUnrollTest.java`; `dev-docs/changelog.adoc`; `.github/workflows/validate-changelog.yml`.
- Not available in the workspace: the CI logs, the gate log (on vm1) and the ticket text.
- Written: this report only. The claim file was not touched.
