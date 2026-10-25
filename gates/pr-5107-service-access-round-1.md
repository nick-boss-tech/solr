# Gate: SOLR-8442 / PR 5107 service-file access patch, round 1

Status: OPEN

- Tags: gate. Intended host: any capable Linux gate host with JDK 21; serialize all Gradle work under that host's documented build lock.
- Authorization: Nick directly requested the #5107 branch gate and comment drafts. This job authorizes only the exact candidate below and the prescribed verification; it does not authorize product fixes, changes to the public PR, posting PR/Jira comments, or ticket-state changes.
- Fork branch: `nick-boss-tech/solr:solr-8442-pr-5107-submit`.
- Candidate head: `0c53fa4c6b36090d4cd7c9870e1259860036d04c`.
- Candidate parent and current Apache PR #5107 head: `e8a488ec4f70de7b5226a6d199def118b151f976` (PR is open; base head `f3a798e424bb933ba988c8d316d0e8f35c8b6e0f`). The candidate is exactly one commit on that PR head; it is on the fork branch and is not the public PR's current head.
- Ticket: SOLR-8442, currently Open. PR: https://github.com/apache/solr/pull/5107.
- Exact candidate commit: Nick Shanin, `SOLR-8442: preserve service access during security setup`; no merge or PR/Jira action is part of this job.
- Changed paths in the candidate commit: `changelog/unreleased/SOLR-8442-security-setup.yml`; `solr/core/src/java/org/apache/solr/cli/{AuthTool,IncludeFileEditor,RunExampleTool,SSLTool}.java`; `solr/core/src/test/org/apache/solr/cli/{AuthToolTest,IncludeFileEditorTest,SSLToolTest}.java`; and `solr/solr-ref-guide/modules/deployment-guide/pages/enabling-ssl.adoc`.
- Relevant source scope: `AuthTool.java:339-377,398-414` preflights auth configuration writes; `IncludeFileEditor.java:110-190,195-235` carries file access through backups, staged writes, and rollback; `SSLTool.java:218-221,303-347,364-373` checks writeability, snapshots/restores access, and distinguishes the keystore password length from an existing PEM key password. `RunExampleTool.java` receives only a static-analysis suppression annotation. The focused tests are in the three changed CLI test classes. The Ref Guide says extended POSIX ACLs and security labels are not preserved; they are outside this job's proof scope.
- Candidate check evidence at setup: the fork candidate has zero GitHub check runs. The existing public PR-head run at `e8a488e` has five failed checks and one successful labeler run (runs started 2026-10-12); those results predate `0c53fa4` and are not evidence for this candidate.
- Claim and receipt targets: future claim `claims/pool-phone-agent-2-gate-pr-5107-service-access-round-1.md`, only after clean detached preflight and all claim preconditions pass; receipt `receipts/SOLR-8442.md` only after a completed gate. Claimant is currently **none**; this gate is OPEN and unclaimed.
- Runner and logs: use `pool/bin/gate-preflight.sh`, then `pool/bin/bg-steps.sh` / `pool/bin/sweep-run.sh` under the host lock documented in `hosts/pool-linux.md`. The claimant records its exact detached worktree and raw log/XML paths under `/workspace/gates/logs/<host>/pr-5107-service-access-round-1/`.

## Exact-head preflight and focused proof

1. Refresh the fork branch and PR head, verify the candidate still equals `0c53fa4c6b36090d4cd7c9870e1259860036d04c` and its parent remains the live PR head, and check current claims/receipts. Run the mandatory detached tidy preflight at this exact head before creating a claim. If tidy changes files, stop and follow the board's narrow tidy-repair rule with a new pinned job; do not count proof at the old head.
2. In an unprivileged Linux environment, compile and run only the changed CLI test classes, with fresh XML/output and seed `8442C0DE`:

   ```bash
   ./gradlew :solr:core:compileTestJava
   ./gradlew :solr:core:test --tests org.apache.solr.cli.AuthToolTest --tests org.apache.solr.cli.IncludeFileEditorTest --tests org.apache.solr.cli.SSLToolTest -Ptests.seed=8442C0DE
   ```

   The run must execute `AuthToolTest.testDirectoryFailurePrecedesSecurityChanges`; it explicitly assumes an unprivileged user and a POSIX filesystem. Record all actual tests, skips, failures, JDK, OS, and the OpenSSL version/tool availability used by `SSLToolTest`.
3. The changed `IncludeFileEditorTest.testDifferentOwnerIsPreserved` explicitly assumes root and a disposable Linux gate container with `nobody`/`nogroup`. Run that method separately as root in an isolated disposable checkout/container, with fresh XML and the same seed:

   ```bash
   ./gradlew :solr:core:test --tests org.apache.solr.cli.IncludeFileEditorTest.testDifferentOwnerIsPreserved -Ptests.seed=8442C0DE
   ```

   Do not count an assumption skip as a pass. Keep this root-only proof separate from the unprivileged run, which is needed for the auth directory-denial test.
4. Check the directly changed modules and docs only, not the repository-wide suite:

   ```bash
   ./gradlew :solr:core:check -x test :solr:solr-ref-guide:check -x test
   git diff --check e8a488ec4f70de7b5226a6d199def118b151f976..0c53fa4c6b36090d4cd7c9870e1259860036d04c
   ```

5. Do not edit product behavior. Only an explicitly prescribed and necessary tidy or test-harness repair is allowed and must be recorded. If a product test or check fails, record the raw failure and hand back; do not patch product behavior. Do not push product code or touch the public PR/Jira.
6. A receipt, if earned, must state actual executions and totals, specifically whether the unprivileged and root-only tests ran or skipped, the exact tested head, and the log/XML paths. No green verdict is possible while the root-only ownership test is skipped or any required run lacks fresh output.

## Current resource stop (phone-agent-2 / vm2)

At 2026-10-25T04:17Z, `/` had 139,440 of 837,408 free inodes (16.6514%). The 15% floor is 125,611.2, leaving 13,828.8 inodes of headroom. The prior SOLR-17786 isolated mutation checkout/test used 20,699 inodes (`claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-two-shard-verdict-incomplete.md`), which would project only 118,741 free inodes (14.1796%) here. Therefore this host did not run detached preflight or any Gradle command and did not create a gate claim; no test has passed or failed on this candidate. The host build lock was free, no active test process was found, and one idle Gradle daemon remained. No files were deleted. Resume only on a host/resource state where the exact checkout plus required runs can remain above the 15% floor; never clean inodes without Nick's explicit approval of exact paths.
