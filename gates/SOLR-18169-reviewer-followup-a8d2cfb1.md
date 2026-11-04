# Gate: SOLR-18169 PR 5289 reviewer follow-up, round 2

Status: OPEN. Implementation is pushed; exact-head Linux proof is pending. This job supersedes `gates/SOLR-18169-reviewer-followup-4f3346a3.md`, which was pinned before the additional #4876 comment follow-up.

- PR: https://github.com/apache/solr/pull/5289 (draft).
- Ticket: SOLR-18169.
- Implementation report: `reports/pr-5289-reviewer-followup-a8d2cfb1.md`.
- Tags: gate, records.
- Intended host: any capable Linux gate host with JDK 21, Git, the pinned Gradle 9.7.0 distribution/build dependencies, and Node/npm plus the Ref Guide toolchain. The live startup leg requires a non-root runner; do not work around root restrictions.
- Fork: `nick-boss-tech/solr`, branch `solr-18169-slim-no-examples`.
- Exact candidate: `a8d2cfb124f584395478124e15c334c435b3119e`.
- Parent: `4f3346a3064aa137d8eb4be5de9adb1e9635adc5`.
- Modules: `solr/packaging`, `solr/core`, `solr/solr-ref-guide`. Focused seed: `18169C0FFEE18169`.
- Gate claimant: unclaimed. Before claiming, refresh the fork branch and verify the exact candidate. Run the detached tidy preflight at this head. If tidy changes files, follow the superseding-head rule below before any positive proof.
- Idempotency key: `SOLR-18169-reviewer-followup@a8d2cfb124f584395478124e15c334c435b3119e`.
- Suggested logs: `/workspace/gates/logs/<host>/solr18169-reviewer-followup-r2/`. Preserve commands, complete logs, exit statuses, fresh JUnit XML, package tree listings, mutation diff and output, and the rendered guide excerpt.
- Receipt: `receipts/SOLR-18169-reviewer-followup-a8d2cfb1.md`, written by the gating host.

## Required proof

1. Use a clean full Linux checkout at the candidate. Record clean status, exact head and parent, JDK, runner and worktree paths. Serialize Gradle behind the host lock. Because the Ref Guide's Antora build is unsafe in a linked worktree, run the guide legs in a separate clean standalone clone at the same exact head with a real `.git` directory.
2. Run `./solr-gradle.sh tidy` at the candidate before proof. Require zero formatting changes. If tidy changes files, commit and plain-push only that scoped fix with Nick Shanin's identity, post a superseding exact-head gate, and rerun all proof at the new head.
3. In the full Linux checkout, run both distribution installs in one invocation:

   ```sh
   ./solr-gradle.sh :solr:packaging:installSlimDist :solr:packaging:installFullDist
   ```

   List both installed trees. Confirm `example/` is absent from slim and present in full.
4. Run the original parent suite at `4f3346a3064aa137d8eb4be5de9adb1e9635adc5`, then the exact-head focused suite at the candidate, each with fresh XML:

   ```sh
   ./solr-gradle.sh :solr:core:test --tests org.apache.solr.cli.TestSolrCLIRunExample -Dtests.seed=18169C0FFEE18169
   ```

   The parent suite should pass its 9 tests. The candidate suite should pass 10 tests, including the default missing-directory cases for `techproducts` and `films`, the explicit missing `--example-dir` validation, and unsupported examples when examples are present. Record counts, failures, errors and skips from XML.
5. Prove the message change fails before the current production change: at parent `4f3346a3064aa137d8eb4be5de9adb1e9635adc5`, overlay only the candidate's `TestSolrCLIRunExample.java`, leave production unchanged, and rerun the focused tests. Expect the two updated exact-message assertions to fail on the previous wording; preserve the isolated diff, logs and XML.
6. Prove the new films test detects the missing-directory regression. At the candidate, retain tests unchanged and apply an isolated synthetic production mutation that bypasses the default missing-directory validation. Run the films test and preserve the mutation diff, intended assertion failure, log and fresh XML. Restore the production source after the mutation run.
7. Run the core check with Error Prone on compilation:

   ```sh
   ./solr-gradle.sh :solr:core:check -x test -Pvalidation.errorprone=true
   ```

   Preserve the full log and exit status; do not count compile/setup failures as test proof.
8. Run `./solr-gradle.sh :solr:packaging:devSlim`. On a non-root runner, run the development slim distribution's `bin/solr start -e techproducts` and `bin/solr start -e films`; capture the helpful missing-example message for each. If the host is root or otherwise cannot perform the live startup, record the exact limit without using `-force` or another workaround.
9. In the separate standalone clone at the same candidate, run:

   ```sh
   ./solr-gradle.sh :solr:solr-ref-guide:buildLocalSite
   ./solr-gradle.sh :solr:solr-ref-guide:checkSiteLinks
   ```

   Inspect the rendered five-minute tutorial and package description. Confirm they direct this bundled-file tutorial to the full package and state that slim omits bundled examples. Do not run root `check` or these Ref Guide tasks in the linked worktree.
10. Confirm a clean final tracked tree and that the fork branch still equals the gated head. Record every skipped or limited leg and all artifact paths.

If a gate repair changes the head, this job no longer proves it. Post a superseding gate pinned to the repaired head before accepting positive results. This job authorizes no PR comment, PR body edit, JIRA comment, PR ready-state change, or merge.
