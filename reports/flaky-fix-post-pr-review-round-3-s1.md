# Flaky-fix post-PR review round 3, slice 1: SOLR-18530 (live PR 5098)

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-3.md`, slice 1. Read-only: no PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle run, test run, Selenium run or gate run. The only git action with a side effect was the read-only fork fetch. The only file written is this report.

## Verdict

**STILL OPEN.** Items B1, B2, D2 and D4 are satisfied. The live body equals the draft. Every Proof number matches the receipt. CI and reviews have no findings. One item remains.

Remaining item (B3, scope of "the one exception"):

- Live sentence (line 15 of the body): "The leader still sends each add on to the cut replica: a failed send to a replica is logged and does not fail the add ([DistributedZkUpdateProcessor](...#L1229-L1231)). The one exception is when the leader changed during the send (the `LeaderChanged` case), where the error is returned to the client instead ([DistributedZkUpdateProcessor](...#L1254-L1262))."
- Problem: at the head, "the one exception" is not the only exception. `DistributedZkUpdateProcessor.java` lines 1345-1348 also add the error to `errorsForClient` for a node on another shard that this core does not list (`if (!shardId.equals(cloudDesc.getShardId())) errorsForClient.add(error);`, the splitshard case). The LeaderChanged naming and its link (L1254-L1262) are correct.
- Exact fix: change "a failed send to a replica is logged" to "a failed send to a replica of this shard is logged". Inside one shard, LeaderChanged (lines 1254-1262) is the only case that returns the error to the client, and the test's cut replica is in the same shard. Optional: replace "when the leader changed during the send" with "when the replica reports the LeaderChanged cause, so it thinks it is now the leader", which matches the code's trigger. The body edit does not change the head SHA, so the links stay valid.

## Item table

| Item | Live wording or record | Source check | Verdict |
|---|---|---|---|
| B1. Choice bold line | Live body, "A choice to check": "**The adds after the cut use a leaders-first client, not a retry in the test.**" No "leaders-only" wording remains in the body; the only match is the flag name `directUpdatesToLeadersOnly`. | Test lines 108-110 call `createNewCloudSolrClient(..., true, ...)`, which calls `sendUpdatesOnlyToShardLeaders()` (`solr/test-framework/src/java/org/apache/solr/cloud/AbstractFullDistribZkTestBase.java` 2431-2449). The routing flag is random (`solr/test-framework/.../SolrTestCaseJ4.java` 2518). "leaders-first" is the accurate term. | SATISFIED |
| B2. Changelog line | Live last line: `Changelog: none. "Pull requests that only touch documentation, tests, build files or CI configuration are exempt and pass without an entry." (dev-docs/changelog.adoc, section 4.)` | Source sentence at `3f5d4c5bf8ac` and at the head, `dev-docs/changelog.adoc` lines 130-131, under "== 4. Changelog Validation in Pull Requests". Quote is exact. Head has no `changelog/unreleased/SOLR-18530.yml` (`git ls-tree` count 0). Base-to-head diff is one file, `RecoveryAfterSoftCommitTest.java` (15 insertions, 5 deletions). The "Changelog: none." label is kept; the quoted sentence is exact. | SATISFIED |
| B3. "Logged" sentence names LeaderChanged | Live line 15 (quoted in the Verdict). | LeaderChanged branch, `DistributedZkUpdateProcessor.java` 1254-1262, adds the error to `errorsForClient` at 1261 (returned to client): correct. Logging at 1229-1231: correct for in-shard replicas. "The one exception" is incomplete: 1345-1348 also returns the error for another shard's node. | STILL OPEN (scope wording; see Verdict) |
| D2. Decision on more evidence before opening | `receipts/SOLR-18530.md` line 13 on `origin/pr-prepare`: "D2 decided: open on the current evidence. The extra runs the review named (the CI-log port comparison and 20 base runs) were not run; the draft's Proof and Limits state the limits of the evidence plainly, and Nick directed the posting on that basis." Round 1 wording (`reports/flaky-fix-review-round-1.md` line 22): "D2. More evidence before opening", t2 step 0 (CI log port check) and t2 step 1 (20 base runs). | Record present. Attribution caveat: "Nick directed the posting on that basis" is not corroborated by any other workspace file. The only Nick direction (`assignments/pool-flaky-fix-post-pr-review-round-1.md` line 5) predates the review and does not mention D2. The main side should confirm the attribution. | SATISFIED |
| D4. Keep or drop "A choice to check" | `receipts/SOLR-18530.md` line 13: "D4 decided: the "A choice to check" section stays in the PR as written (leaders-first client versus a retry in the test)." Round 1 wording: "Keep or drop 'A choice to check'". Live body keeps the section (lines 29-35) with the same text as the draft. | Record and body agree. The record says "decided" without naming who decided. | SATISFIED |
| Round 1 F1 (links at head, extra check) | Five body links, all `blob/14868bc7a7f5ec5c4fa1ade5f894cbda9533307d/...`: test L102-L117; DistributedZkUpdateProcessor L1229-L1231 and L1254-L1262; LBSolrClient L675-L680; CheckRetryUnrollTest L97-L103. | Each range shows the claimed code at the head. The test comment at 102-106 is part of the L102-L117 range. | SATISFIED |
| Title (round 2 item, extra check) | "SOLR-18530: Route adds after the proxy cut in RecoveryAfterSoftCommitTest to the shard leaders first" | Post-cut adds use a client built with `sendUpdatesOnlyToShardLeaders()` (leaders first, random flag). Accurate. | SATISFIED |

## Body vs draft differences

None. The live body (fetched read-only through `research/gh.ps1 pr view 5098 --json body`) and `pr-drafts/flaky-fixes/SOLR-18530.md` on `origin/pr-prepare` each have 5,154 bytes, no CR characters, and an empty `diff` after CR stripping. Every round 2 item applied in the draft is present in the live body.

## Proof check

Receipt line numbers refer to `receipts/SOLR-18530.md` on `origin/pr-prepare`.

| Body Proof statement | Receipt | Result |
|---|---|---|
| Fixed test: 6 of 6 runs passed | Line 8: fixed test ran 6 times (CI seed plus 5 random seeds), 0 failures | match |
| CI seed `A57A22E346C237C6` | Line 8 | match |
| Five random seeds | Line 8: "the CI seed plus 5 random seeds" | match |
| Checked 2026-10-10 | Lines 3 and 13: gate finished 2026-10-10 | match |
| Original test: 5 of 5 runs passed at the CI seed | Line 8: unmodified base test ran 5 times at the CI seed, 0 failures | match |
| "at 14868bc7a7f5..." (head named in the check line) | Line 4: gated head `98e5368d99`. Line 11: the gate carries over to `14868bc7a7f` (changelog-only delta). The receipt does not record a run at this SHA. | Not a number mismatch. The blob for `RecoveryAfterSoftCommitTest.java` is identical at both SHAs, so the claim holds in substance. Optional wording note (round 2 O2, still open). |

Limits numbers: "six passing runs" matches line 8 (6 runs). "within 30 seconds" matches the test code (line 130, `ensureAllReplicasAreActive(..., 30)`). No body number lacks a receipt or code source.

## CI and review state

- Head `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`. `git ls-remote origin refs/heads/solr-18530-submit` matches it at the start of the run. The read-only fork fetch completed; the worktree was not changed.
- PR 5098: OPEN, draft, `mergeStateStatus` UNSTABLE, `reviewDecision` empty.
- `statusCheckRollup` (the only check the PR JSON lists): Pull Request Labeler, SUCCESS.
- Workflow runs at the head SHA (`actions/runs?head_sha=`):
  - Pull Request Labeler (`pull_request_target`), run 38079120890: success.
  - Gradle Precommit, run 38079120736: completed, `action_required` (not run).
  - Solr Tests via Crave, run 38079120826: completed, `action_required` (not run).
  - Validate Changelog, run 38079120887: completed, `action_required` (not run).
- Reviews (`pulls/5098/reviews`): none. Inline review comments (`pulls/5098/comments`): none. Issue comments (`issues/5098/comments`): none.
- `action_required` is incomplete validation, not a failure. When a maintainer approves those runs, compare each run's `head_sha` with `14868bc7a7f` before reading a failure as actionable.

## Automated findings

- Verified: none. No review, inline comment or issue comment exists on PR 5098.
- Rejected: none. The labeler added the labels "tests" and "cat:cloud"; those are not review findings.

## Other observations (not blocking)

- O1. Test comment, `RecoveryAfterSoftCommitTest.java` lines 102-106 at the head, states the cause as fact: "an add routed to the cut replica can fail with a ClosedChannelException after the write has committed." The body hedges correctly ("Our reading of the cause, which these runs do not prove"). A comment-only change would alter the blob, so the receipt would need a note. Carried from round 1 finding 1.
- O2. Proof head attribution (see Proof check). Optional.
- O3. Receipt line 3 still reads "PR-ready; opening needs Nick's approval", though PR 5098 is already open. Record hygiene only.
- O4. Length and presentation (formula conformance): the body is 5,154 bytes, against the roughly 3,500-character guide in `pr-formula.md`. The Limits section has no bold one-line summary. Carried from round 1.
- O5. The receipt does not record the D1 (exemption) or D3 (routing) decisions. Both are reflected in the body (the B2 line; the title and bold line now say "shard leaders first" with the random flag disclosed). Not in round 3 scope.

## Scope and reads

- Read from `origin/pr-prepare` (tip `ae3bb783d72`): the round 3 assignment, `pr-formula.md`, `reports/flaky-fix-post-pr-review-round-2.md`, `reports/flaky-fix-post-pr-review-round-2-s1.md`, `reports/flaky-fix-review-round-1.md`, `reports/flaky-fix-post-pr-review-round-1-s1.md`, `reports/flaky-fix-post-pr-review-round-1.md`, `receipts/SOLR-18530.md`, `pr-drafts/flaky-fixes/SOLR-18530.md`, `assignments/pool-flaky-fix-post-pr-review-round-1.md`.
- Read from the head `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` (`git show`, read-only): `RecoveryAfterSoftCommitTest.java`, `DistributedZkUpdateProcessor.java`, `LBSolrClient.java`, `CheckRetryUnrollTest.java`, `SolrTestCaseJ4.java` (test-framework), `AbstractFullDistribZkTestBase.java` (test-framework), `dev-docs/changelog.adoc` (head and `3f5d4c5bf8ac`).
- Live PR 5098, read-only: metadata and body through the GitHub wrapper; reviews, inline comments, issue comments and workflow runs through the REST endpoints.
- Not done: no claim file change, no push, no DONE mark, no build, no test, no gate, no Jira action. Those belong to the lead.
