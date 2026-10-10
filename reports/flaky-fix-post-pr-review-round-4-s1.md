# Flaky-fix post-PR review round 4, slice 1: SOLR-18530 (live PR 5098)

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-4.md`, slice 1. Read-only: no PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle run, test run, Selenium run, or gate or test-queue run. The only git fetch was the read-only fork fetch of `refs/heads/solr-18530-submit`. The only file written is this report. Body line numbers below refer to the live body, which is identical to the draft (see the body-versus-draft section).

## Verdict

**STILL OPEN.** The round 3 item B3 is satisfied. The final read finds three items, all in the body text. Each one needs the same edit on the live body and on `pr-drafts/flaky-fixes/SOLR-18530.md`:

1. Proof (lines 25 and 26): a CI seed value and the word "seeds" appear. The body must not name seeds.
2. Limits (line 37): the section has no bold one-line summary, against the presentation rule.
3. Changelog (line 45): the `dev-docs/changelog.adoc` citation is plain text, not a link at the head SHA.

Head check: `git ls-remote origin refs/heads/solr-18530-submit` returns `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`. It equals the PR head and the fork tip.

## Remaining items (exact)

1. **Line 25, Proof, first bullet.** Remove the sentence "The runs used the CI seed `A57A22E346C237C6` and five random seeds." The count "6 of 6 runs passed" and "Checked 2026-10-10 at 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d." stay. Checked against receipt line 8: the fixed test ran 6 times with 0 failures.
   **Line 26, Proof, second bullet.** Change "5 of 5 runs passed at the CI seed `A57A22E346C237C6`." to "5 of 5 runs passed." Checked against receipt line 8: the unmodified base test ran 5 times with 0 failures.
   Rule: lead check 3 lists seeds as internal vocabulary that must not appear.

2. **Line 37, "## Limits".** The section opens with a bullet, not a bold one-line summary. Fix: add one bold line as the first line under the heading, for example "**Three gaps remain: the test checks less than the recovery path, other tests may share the pattern, and SolrJ still does not retry this error.**" The main side picks the final wording. It should carry the limits once and must not repeat the Proof summary line.
   Rule: `pr-formula.md`, presentation rule ("every section opens with a bold one-line summary").

3. **Line 45, "Changelog".** The file citation "(dev-docs/changelog.adoc, section 4.)" is plain text. Fix: "([dev-docs/changelog.adoc](https://github.com/nick-boss-tech/solr/blob/14868bc7a7f5ec5c4fa1ade5f894cbda9533307d/dev-docs/changelog.adoc#L130-L131), section 4.)". The file is byte-identical at the base `3f5d4c5bf8ac` and at the head, and lines 130 to 131 hold the quoted exemption sentence.
   Rule: `pr-formula.md`, citations are real links at the head SHA.

## B3 check: "of this shard" and both client-return cases

Live line 15 (quoted in part): "The leader still sends each add on to the cut replica: a failed send to a replica **of this shard** is logged and does not fail the add ([DistributedZkUpdateProcessor](...#L1229-L1231)). The error is returned to the client instead in two cases: when the leader changed during the send (the `LeaderChanged` case) ([DistributedZkUpdateProcessor](...#L1254-L1262)), and when the replica is on another shard ([DistributedZkUpdateProcessor](...#L1345-L1348))."

Each anchor was checked with `git show 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d:solr/core/src/java/org/apache/solr/update/processor/DistributedZkUpdateProcessor.java`:

| Claim | Anchor at head | Head code | Result |
|---|---|---|---|
| Failed replica send is logged, no client error (same shard) | L1229-L1231 | 1229 to 1230: "for now we don't error - we assume if it was added locally, we succeeded"; 1231: `log.warn("Error sending update", ...)` | Correct |
| LeaderChanged case returns error to client | L1254-L1262 | 1254: `if ("LeaderChanged".equals(cause))`; 1261: `errorsForClient.add(error);` | Correct |
| Other-shard replica returns error to client | L1345-L1348 | 1345: `if (!shardId.equals(cloudDesc.getShardId())) {`; 1348: `errorsForClient.add(error);` | Correct |

"of this shard" is on the logged case, as round 3 asked. The other-shard case is named as "when the replica is on another shard", which matches the code.

Scope note: `errorsForClient.add(error)` also appears at line 1223, for `ForwardNode` errors (a non-leader forwarding to a leader). That is not the leader's send to a replica, so the "two cases" sentence stays correct for this test.

**B3: SATISFIED.**

## Whole-body read (against head and receipt)

- **File citations.** Five are blob links at `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` with lines that hold the claim: test L102-L117 (client built with the shard-leaders flag, lines 108 to 110); DistributedZkUpdateProcessor L1229-L1231, L1254-L1262 and L1345-L1348 (checked above); LBSolrClient L675-L680 (the update-only retry branch: a connect exception or a request never sent); CheckRetryUnrollTest L97-L103 (the test asserts `ClosedChannelException` is not retried). The changelog citation is not a link (item 3).
- **Title.** "Route adds after the proxy cut in RecoveryAfterSoftCommitTest to the shard leaders first" is accurate. The helper sets `shardLeadersOnly` to true (`CloudSolrClient.java` 1416 to 1419, via `sendUpdatesOnlyToShardLeaders()`). The leaders-only routing flag `directUpdatesToLeadersOnly` is randomized (`SolrTestCaseJ4.java` 2518), which the body discloses.
- **Proof numbers.** Each number is in `receipts/SOLR-18530.md` on the tip: line 8 (6 fixed-test runs, 5 base runs, 0 failures) and line 3 (gate finished 2026-10-10). The receipt's gated head is `98e5368d996518b9f4a94f85d7c2fcd933d3f485`. The only later change is the deletion of `changelog/unreleased/SOLR-18530.yml` (`git diff --stat` shows one file, 7 deletions), so the test file is the same at both heads and "checked at 14868bc" is accurate in substance.
- **Changelog statement.** `git ls-tree` at the head finds no `changelog/unreleased/` file for SOLR-18530 (count 0). "Changelog: none." matches. The exemption sentence is quoted exactly from `dev-docs/changelog.adoc` section 4 (lines 130 to 131 at `3f5d4c5bf8ac`). The workflow `.github/workflows/validate-changelog.yml` line 67 exempts paths that match `^solr/.*/test`, and the one changed file matches, so the check should pass once it runs (it has not run).
- **Other claims checked against head.** The adds before the cut use the normal client (test line 87). The cut replica is `notLeader` (lines 95 to 97), so "the leader is not cut" holds. `MAX_DOCS` is unchanged (the base-to-head diff is 15 insertions and 5 deletions, all in this test file). The wait is 30 seconds (line 130). For updates, `LBSolrClient` retries only on a connect exception or a request never sent (lines 659 to 683). The `CheckRetryUnrollTest` assertion matches the Limits text.
- **Not checked.** The symptom text in "What happens today" (`SolrServerException`, `ClosedChannelException`) was not compared with the Jira ticket, which was out of scope for this slice.

Non-blocking observations (not part of the verdict):
- O1. The body is 5,413 characters. `pr-formula.md` gives a guide of about 3,500, with room for complex tickets.
- O2. Line 15 says "when the leader changed during the send". The code trigger is the replica's reply carrying cause `LeaderChanged`, meaning the replica now thinks it is the leader (log text at line 1257). An optional paraphrase is "when the replica reports that it is now the leader".
- O3. `solr/core/src/test/org/apache/solr/cloud/RecoveryAfterSoftCommitTest.java` line 102 (in the diff) says "Use a leaders-only client". The client sends to shard leaders first, and `directUpdatesToLeadersOnly` is random, so the cut replica can stay on the failover list, as the body's Limits says. Any change to the comment would change the head SHA, so it is optional and should not block the body.

## Body versus draft

`pr-drafts/flaky-fixes/SOLR-18530.md` on `origin/pr-prepare` and the live body were compared after CR stripping. Both are 5,413 bytes. Neither contains a CR byte. `diff` reports no differences. The three remaining items therefore apply to the draft and the live body alike.

## CI and review state

- Head `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` equals the fork tip. PR 5098: OPEN, draft, `reviewDecision` empty, `mergeStateStatus` UNKNOWN at read time.
- `statusCheckRollup`: one check, `labeler`, SUCCESS (Pull Request Labeler run 38079120890).
- Workflow runs at head (read with `gh api`, GET only):
  - Pull Request Labeler (`pull_request_target`), run 38079120890: success.
  - Gradle Precommit, run 38079120736: `action_required`. Not run.
  - Solr Tests via Crave, run 38079120826: `action_required`. Not run.
  - Validate Changelog, run 38079120887: `action_required`. Not run.
- Reviews: 0. Review comments: 0. Issue comments: 0.
- `action_required` is an incomplete state, not a failure. Compare each run's `head_sha` with `14868bc7` before a later failure is treated as actionable.

## Automated findings

- Verified: none. No review, inline comment or issue comment exists on PR 5098.
- Rejected: none. The labeler result is a labeling step, not a review finding.

## Reads

- From `origin/pr-prepare` (tip `835cba367157`): `assignments/pool-flaky-fix-post-pr-review-round-4.md`, `pr-formula.md`, `reports/flaky-fix-post-pr-review-round-3.md`, `reports/flaky-fix-post-pr-review-round-3-s1.md`, `receipts/SOLR-18530.md`, `pr-drafts/flaky-fixes/SOLR-18530.md`.
- From head `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` (`git show`, read-only): the DistributedZkUpdateProcessor ranges, `RecoveryAfterSoftCommitTest.java`, `LBSolrClient.java`, `CheckRetryUnrollTest.java`, `CloudSolrClient.java` (builder), `AbstractFullDistribZkTestBase.java` and `SolrTestCaseJ4.java` (test-framework), `.github/workflows/validate-changelog.yml`.
- From base `3f5d4c5bf8ac`: `dev-docs/changelog.adoc` section 4.
- Live PR 5098: metadata and body through `research/gh.ps1 pr view`; checks, reviews and comments through `gh api` GET endpoints.
- Not done: no claim file change, no push, no DONE mark, no commit, no build, no test, no gate, no Jira action.
