# PR 5206 reconnection assertion and listener cleanup: Linux gate, round 1

Result: all required steps passed at the exact live head `6c6ff8d9a0e173c02b5a11f8ad3b13a59ebd36fa` (candidate F). Proof limits below still apply: the original intermittent failure was never reproduced, so this gate supports compatibility and does not show the race is gone.

## Pinned inputs

- Ticket SOLR-18576. PR https://github.com/apache/solr/pull/5206, open, head F verified equal to fork `nick-boss-tech/solr:solr-18576-submit-apache-main` at the end of the gate (2026-10-27 07:22Z).
- F's parent O `4c8f12f526f54e4e90a8ea49d46c1d28d899cb65`. Apache merge base `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Only file changed by F: `solr/core/src/test/org/apache/solr/cloud/BasicDistributedZk2Test.java` (12 insertions, 7 deletions). No production file, dependency, or changelog change.
- Host vm6, JDK 21.0.12.1+1, Gradle from the branch wrapper. Claim: `claims/pr-5206-reconnection-cleanup-round-1-vm6.md`. Results: `claims/pr-5206-reconnection-cleanup-round-1-vm6-result-1.md`, `-result-2.md`, `-result-3.md`.

## Steps at F

| # | Step | Job and log | Result |
|---|---|---|---|
| 0 | Detached tidy preflight in a clean clone | `/workspace/gates/logs/vm6/pr5206-F-preflight/` | rc 0, "tidy clean at 6c6ff8d9a0e", 05:41Z to 05:47Z |
| 1 | `./gradlew --no-daemon tidy` | `pr5206-F-6c6ff8d9a0e`, `tidy.log` | rc 0, 06:00:06Z to 06:03:13Z; worktree clean after (`git status --porcelain` empty) |
| 2 | Error Prone compile (`compileJava compileTestJava -Pvalidation.errorprone=true --rerun-tasks --no-build-cache`) | `errorprone.log` | rc 0, 06:03:13Z to 06:10:10Z |
| 3 | Original seed `7E5494314774A841`, `BasicDistributedZk2Test` | `seed-7E5494314774A841.log`, XML copied to `seed-7E5494314774A841.xml/` | rc 0. tests=1, skipped=0, failures=0, errors=0. 06:10:10Z to 06:17:26Z |
| 4 | Random iterations `-Ptests.iters=4`, `BasicDistributedZk2Test` | `iters4.log`, XML in `iters4.xml/` | rc 0. tests=4, skipped=0, failures=0, errors=0. Actual seed `A6A04801FC75C1A7`. 06:17:26Z to 06:24:42Z |
| 5 | Root check `check -x test --no-build-cache` | `pr5206-F-6c6ff8d9a0e`, `root-check.log` | **rc 1**, stopped the job. Failing task `:solr:solr-ref-guide:buildLocalAntoraSite`. See "Root check failure" below. |
| 5b | Root check, rerun once in a full clone | `pr5206-F-clone-root`, `root-check.log` | rc 0, BUILD SUCCESSFUL, 557 tasks executed, 06:59:18Z to 07:20:15Z |
| 6 | Final tidy | `pr5206-F-clone-root`, `final-tidy.log` | rc 0, BUILD SUCCESSFUL, 07:20:15Z to 07:22:32Z |

`NODE_ENV` was unset for every step (`env -u NODE_ENV` on the job, and the preflight unsets it too). The shell default is `production`. Step 5b used the same unset setting. Error Prone was enabled only in step 2. The root check runs with it disabled ("errorprone disabled" in `root-check.log`), so the root check does not repeat that check.

## Root check failure

- `pr5206-F-6c6ff8d9a0e` `root-check.log`: every check task before `:solr:solr-ref-guide:buildLocalAntoraSite` ran without failure. That task exited with `npx antora` status 1. The Gradle log has no Antora error text.
- The npm debug log for that run (`/home/claude/.npm/_logs/2026-10-27T06_40_06_589Z-debug-0.log`) showed exit 1 with no message.
- Diagnostic run `antora-diag-info` (`:solr:solr-ref-guide:buildLocalAntoraSite --info --stacktrace`, `pr5206-F-6c6ff8d9a0e/diag/antora-diag-info.log`) printed the cause: `Local content source must be a git repository: /workspace/worktrees/gate-pr5206-vm6`.
- Cause: the gate checkout was a linked git worktree, so its `.git` is a file (`gitdir: /workspace/solr/.git/worktrees/gate-pr5206-vm6`). The ref-guide playbook (`solr/solr-ref-guide/build/antora-config/playbooks/local-playbook.yml`) reads the checkout as a content source, and Antora rejects a `.git` file.
- Not the candidate: F changes no ref-guide file. The UI bundle host and npm registry both returned HTTP 200 from this host.
- Environment change, recorded before the rerun in `-result-2.md`: a full local clone (`git clone --no-hardlinks /workspace/solr`) at F, at `/workspace/gates/clone-pr5206-F`, with a directory `.git`. The failed run executed no tests, so the rerun is the environment relaunch WORKFLOW.md allows once.

## Source inspection of F (not executed as a separate test)

File: `solr/core/src/test/org/apache/solr/cloud/BasicDistributedZk2Test.java` at F.

- Listener registration (`addListener(expiryListener)`, line 169) stays before forced expiry (`try` at line 170).
- The expiry assertion (line 172) and the reconnection assertion (lines 173-175, `assertTrue("client did not reconnect after session expiry", ... blockUntilConnected(30, TimeUnit.SECONDS))`) are inside the `try`. `removeListener` (line 180) is in `finally` (line 176).
- Expiry failure path: a failed `sessionExpired.await` throws, and `finally` removes the listener. Reached through the `try`.
- Reconnection failure path: `blockUntilConnected` returning false fails the assertion before `indexr` (line 183), and `finally` removes the listener. Reached through the `try`.
- Interrupted or timed-out wait: `await` or `blockUntilConnected` throws, and `finally` removes the listener. The timeout path is also a false return, covered above.
- Indexing (line 183) comes only after the checked waits and the cleanup.
- No production file, dependency, or unrelated test changed (single-file diff).

These paths are source-inspected only. The passing runs did not exercise the expiry-failure, timeout, or interruption branches.

## Proof limits

- The original intermittent failure was not reproduced before or after the earlier PR change. The new assertions are pins until a mutation or base run shows them failing. None was run (the gate does not request one).
- Passing runs support compatibility. They do not show the original race is eliminated.
- The clone rerun and the failed root check ran on the same head with the same tests; the only change was the checkout form (see above).

## Cleanliness and live heads

- Clone `/workspace/gates/clone-pr5206-F` after final tidy: HEAD `6c6ff8d9a0e`, `git status --porcelain` empty, `git diff --exit-code` clean.
- Fork `solr-18576-submit-apache-main` = `6c6ff8d9a0e`. PR 5206 open, head `6c6ff8d9a0e`, not merged (both checked 07:22Z).

## Status

Candidate F: **Gate green** for all six required steps, with the root check's first failure explained as environment and the rerun passing. The gate file is not edited; completion is the receipt at `receipts/SOLR-18576-pr5206-review-fixes-round-1.md`. Nothing was posted to GitHub or Jira.
