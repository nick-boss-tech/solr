# SOLR-18564: typed timeout review follow-up, Windows round 1

Status: IMPLEMENTED, PROOF PENDING

- Host: windows. Owner authorization: implement the proposed typed executor timeout exception and hand it to a Linux gate.
- Claim: `claims/solr-18564-timeout-type-round-1-windows.md`, posted at `ba4e0e10d0e441e1350e53e4da7b14b39cf02e9c`, based on board snapshot `94e1d1c6c2528e70aec97c245ccd95992995a81b`.
- Review request: sigram, 2026-10-27T10:37:10Z, https://github.com/apache/solr/pull/5192#issuecomment-6439747314. Concern: a broad RuntimeException catch may hide unrelated errors; narrow the exception or message.
- Original PR head O: `dd65b21b41dc1d008c51031b71f61964ee2ca169`.
- Pushed follow-up F: `bd42a1d58b9fba4656a200536a007d9d79dc45b2`, commit `SOLR-18564: catch only executor termination timeouts during shutdown`.
- Branch: `nick-boss-tech/solr:corecontainer-shutdown-closes`. The open PR head moved from O to F with a normal fast-forward. No rebase or force-push.
- Diff: https://github.com/nick-boss-tech/solr/compare/dd65b21b41dc1d008c51031b71f61964ee2ca169...bd42a1d58b9fba4656a200536a007d9d79dc45b2.

## Change and assessment

Three files changed. ExecutorUtil introduces public final nested `ExecutorTerminationTimeoutException extends RuntimeException`, with a private constructor, and throws it only at the existing termination-budget exhaustion site. The diagnostic message, pool details, wait durations, forced shutdown, and interruption behavior are preserved. The three public entrypoints document the timeout. Existing RuntimeException catches continue to catch the subtype; callers that inspect the exact exception class will observe the more specific type.

CoreContainer imports and catches only this exception around its close-pool shutdown wait. Unrelated runtime exceptions are no longer swallowed by that catch. The existing trailing close order and warning, including the exception stack, are unchanged.

The existing `ExecutorUtilTest.testExecutorUtilAwaitsTerminationWhenTaskIgnoresInterupt` now requires the specific timeout exception and asserts both the message prefix and pool name. It uses the existing fast real-timeout setup. No new slow test or production test hook was added. CoreContainerShutdownTest remains unchanged and must still pass against the actual two-wait timeout path.

No new security boundary, dependency, endpoint, setting, or wire format is introduced. The existing changelog fragment still describes the cleanup fix and names Nick Shanin only. No reference-guide change is needed for this implementation detail.

## Verification and handoff

Windows performed static diff and scope review; `git diff --check` passed. The commit contains only the three intended source/test files and uses Nick Shanin's ICLA identity, without attribution trailers. No Gradle, tidy, compilation, or test execution ran on Windows.

Linux gate: `gates/SOLR-18564-pr5192-timeout-type-round-1.md`, posted and content/reachability verified at board commit `73ddad8924da0ed6389bac7dd5d54c1a0562633c`. It requires tidy, changelog parsing, Error Prone production/test compilation in solrj and core, two seeds for ExecutorUtilTest, CoreContainerShutdownTest and TestCoreContainer, a one-line synthetic mutation proving the narrowed timeout-type assertion, and a final root `check -x test`. A fresh receipt must cover F. Older green receipts cover O only and are not proof for this change.

The push helper verified the fork by ls-remote; a separate GitHub ref API read returned F, and a separate live PR read also returned F. The PR discussion still contained only the two existing maintainer comments. The ticket worktree was clean after the push.

Expected receipt: `receipts/SOLR-18564-timeout-type-round-1.md`. The gate is available to any capable Linux gate host and is not yet claimed by this Windows implementation record. This implementation claim is complete when this report and the new gate exist; build/test proof remains pending.

No public comment, PR body edit, JIRA write, approval or merge was made. Suggested factual reply after green exact-head proof: the timeout now has a specific unchecked type, CoreContainer catches only that type, the diagnostic text is preserved, and the required checks pass (include actual receipt results and commit link at that time).
