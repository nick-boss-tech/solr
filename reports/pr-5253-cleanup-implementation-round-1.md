# SOLR-18601 / PR 5253 cleanup follow-up, implementation round 1

Status: IMPLEMENTED, PROOF PENDING.

- Owner direction: build the review fixes and hand them to a Linux gate, October 27, 2026 Eastern.
- Host: windows, implementation/review/records. Claim: claims/pr-5253-cleanup-windows-round-1.md.
- Claim timestamp correction: the claim's 01:15 UTC start line was a transcription error. Its authoritative publication time is 2026-10-28 01:04:29 UTC, commit 5697525b375ae07e2fb9fcaae6bf7f5069c128e6. This report corrects the timestamp without editing the immutable claim.
- Original upstream PR head O: 32b91612edde057c19bf1a7963e8276b816551df. PR: https://github.com/apache/solr/pull/5253.
- Pushed fix head F: 5cbffe2f243c4f2cf3606de9f2434981fc097019, fork nick-boss-tech/solr, branch solr-18601-submit. The GitHub ref API returned this exact SHA after the plain push.
- F is one ordinary commit directly above O. Author Nick Shanin, no attribution trailers. No history rewrite and no change to the contributor's branch or public PR head. Live PR refresh still showed O, with no comments or reviews.
- Review diff: https://github.com/nick-boss-tech/solr/compare/32b91612edde057c19bf1a7963e8276b816551df...5cbffe2f243c4f2cf3606de9f2434981fc097019.
- Linux gate: gates/pr-5253-cleanup-round-1.md. No pre-existing gate or receipt covers F.

## Resulting behavior

1. Failure cleanup replays the target's buffered updates using the existing REQUESTAPPLYUPDATES action. A target buffer is shared by all cross-collection forwarding streams. Discarding it can lose updates or deletes for earlier successful migrations, even if their routing rules remain installed. The new buffer-discard action and its two error-wrapper tests are removed because they have no remaining caller in this patch.
2. Before buffering, MigrateCmd snapshots the live previous routing rule. It derives the exact rule that the new range addition should install, including accumulated ranges and expiry. Installation waits for that exact rule rather than accepting a previously present matching range.
3. Routing cleanup sends the installed rule as an expected value and the previous rule as a replacement. SliceMutator restores/deletes only if the current rule still equals the expected rule. Previous ranges, duplicate-range history, expiry and other properties survive; stale cleanup cannot erase a changed rule. Unconditional expiry-removal messages retain their existing whole-rule behavior.
4. Cleanup resumes target processing only after restoration is observed. Removal failure, timeout or interruption preserves the original failure and leaves buffering alone. An unconfirmed installation also leaves buffering alone, since ownership of the routing change cannot be established safely.
5. An existing rule pointing to a different target collection is rejected before buffering, rather than appending a range to a rule for the wrong target.

This deliberately does not promise a transaction rollback. Replaying can leave partial data for the failed migration on the target; the original flow could already leave a merged index after an ambiguous merge result. The Reference Guide, changelog, and regression explicitly describe/pin partial target data while preserving acknowledged writes. Unconfirmed routing installation and failed cleanup may still require operator intervention. Final-apply failures and the existing asynchronous core-admin failure propagation limitation remain outside this change.

Decision ladder: code and stated intent (rungs 1 and 3). A core-wide buffer lacks per-migration ownership, while the API permits continued forwarding for earlier successful migrations. Replay preserves those writes without introducing a separate buffering protocol. No maintainer or public decision was posted.

## Scope

F changes eleven files relative to O:

- changelog/unreleased/SOLR-18601-migrate-cleanup-on-failure.yml
- solr/core/src/java/org/apache/solr/cloud/api/collections/MigrateCmd.java
- solr/core/src/java/org/apache/solr/cloud/overseer/SliceMutator.java
- solr/core/src/java/org/apache/solr/handler/admin/CoreAdminOperation.java (remove unused new discard action)
- solr/core/src/java/org/apache/solr/util/TestInjection.java
- solr/core/src/test/org/apache/solr/cloud/MigrateRouteKeyTest.java
- solr/core/src/test/org/apache/solr/cloud/api/collections/MigrateCmdCleanupTest.java
- solr/core/src/test/org/apache/solr/cloud/overseer/SliceMutatorRoutingRuleTest.java
- solr/core/src/test/org/apache/solr/handler/admin/CoreAdminOperationTest.java (remove tests of removed action)
- solr/solr-ref-guide/modules/deployment-guide/pages/collection-management.adoc
- solr/solrj/src/java/org/apache/solr/common/params/CoreAdminParams.java (remove unused new enum value)

Relative to upstream main, CoreAdminOperation, its tests and CoreAdminParams return to baseline. The net contribution has eight changed files. The original contributor's commit and authorship remain intact; the changelog credits Boyi Dai and Nick Shanin by name.

## Tests prepared

Eleven new methods, unexecuted on Windows:

- MigrateRouteKeyTest: testFailedMigratePreservesEarlierForwardedUpdates, testFailedMigrateRestoresExistingRule, testFailedMultiShardMigratePreservesEarlierRange. The first verifies three acknowledged writes are actually in the BUFFERING target log before forcing failure, then checks the earlier update/delete and partial failed-key write after replay. The others assert previous rule/range properties and subsequent forwarding.
- MigrateCmdCleanupTest: removal failure, timeout and interruption each assert no target shard handler is created, and that the cleanup failure is suppressed on the original exception. The interrupt case also checks the interrupt flag.
- SliceMutatorRoutingRuleTest: exact previous ranges/expiry restoration, removal of a newly created rule, rejection of stale cleanup, preservation of a duplicate range, and compatibility with ordinary whole-rule expiry removal. State-update messages pass through JSON serialization in these tests.

The three focused classes contain fifteen test methods in total at F. The existing early-failure synchronous/asynchronous retry test remains. A deterministic assertion-only TestInjection hook runs after rule installation; it is a no-op outside tests and is cleared by reset and test finally blocks.

## Validation and handoff

Windows checks: reviewed ticket-scoped diff and new files, git diff --check clean, clean worktree after commit, fork ref read-back exact, branch comparison against freshly fetched upstream main. No Gradle, formatter, compiler, test, or retired local queue ran on Windows. Formatting and compilation are unproven until the Linux gate.

The gate requires clean tidy at its final pinned head, all fifteen focused tests, two additional seeds for the new cloud regressions, neighboring admin/state tests, root check -x test, original-production failure proof for the cloud regressions, and a mutation proving cleanup does not continue after restoration failure. The gate host must preserve logs, fresh XML, hashes and synthetic-tree details. Any tidy/source/test repair gets a new pushed head and superseding gate before proof is accepted. No green result is claimed here.

No public comment, JIRA write, PR opening or live body edit was made. This is a gate handoff; no public follow-up is requested by this job.
