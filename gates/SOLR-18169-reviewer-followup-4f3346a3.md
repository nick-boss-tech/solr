# Gate: SOLR-18169 PR 5289 reviewer follow-up

Status: OPEN. Implementation is pushed; exact-head Linux proof is pending.

- PR: https://github.com/apache/solr/pull/5289 (draft).
- Ticket: SOLR-18169.
- Implementation report: `reports/pr-5289-reviewer-followup-4f3346a3.md`.
- Tags: gate, records.
- Intended host: any capable Linux gate host with JDK 21, Git, the pinned Gradle 9.7.0 distribution/build dependencies, and Node/npm plus the Ref Guide toolchain. The live startup leg requires a non-root runner; do not work around root restrictions.
- Fork: `nick-boss-tech/solr`, branch `solr-18169-slim-no-examples`.
- Exact candidate: `4f3346a3064aa137d8eb4be5de9adb1e9635adc5`.
- Parent: `f5a5480383a2e8d25d1411ec1d813da593305952`.
- Modules: `solr/packaging`, `solr/core`, `solr/solr-ref-guide`. Focused seed: `18169C0FFEE18169`.
- Gate claimant: unclaimed. Before claiming, refresh the fork branch and verify the exact candidate. Run the detached tidy preflight at this head. If tidy changes files, follow the superseding-head rule below before any positive proof.
- Idempotency key: `SOLR-18169-reviewer-followup@4f3346a3064aa137d8eb4be5de9adb1e9635adc5`.
- Suggested logs: `/workspace/gates/logs/<host>/solr18169-reviewer-followup/`. Preserve commands, complete logs, exit statuses, fresh JUnit XML, package tree listings, and the rendered guide excerpt.
- Receipt: `receipts/SOLR-18169-reviewer-followup.md`, written by the gating host.

## Required proof

1. Use a clean full Linux checkout at the candidate. Record clean status, exact head and parent, JDK, runner and worktree paths. Serialize Gradle behind the host lock. Because the Ref Guide's Antora build is unsafe in a linked worktree, run the guide legs in a separate clean standalone clone at the same exact head with a real `.git` directory.
2. Run `./solr-gradle.sh tidy` at the candidate before proof. Require zero formatting changes. If tidy changes files, commit and plain-push only that scoped fix with Nick Shanin's identity, post a superseding exact-head gate, and rerun all proof at the new head.
3. In the full Linux checkout, run both distribution installs in one invocation:

   ```sh
   ./solr-gradle.sh :solr:packaging:installSlimDist :solr:packaging:installFullDist
   ```

   List both installed trees. Confirm `example/` is absent from slim and present in full.
4. Run the focused CLI suite with fresh XML:

   ```sh
   ./solr-gradle.sh :solr:core:test --tests org.apache.solr.cli.TestSolrCLIRunExample -Dtests.seed=18169C0FFEE18169
   ```

   Record test counts, failures, errors and skips from fresh XML. The suite should cover the default missing-directory text, explicit `--example-dir` validation and unsupported examples with an example directory present.
5. Demonstrate fail-before for the updated error wording in an isolated checkout at parent `f5a5480383a2e8d25d1411ec1d813da593305952`: overlay only the updated `TestSolrCLIRunExample.java`, leave production code unchanged, and run the focused test. Expect the updated exact-message assertion to fail against the old message while the other tests pass. Preserve the isolated diff and fresh XML/logs.
6. Run the core check with Error Prone on compilation:

   ```sh
   ./solr-gradle.sh :solr:core:check -x test -Pvalidation.errorprone=true
   ```

   Preserve the full log and exit status; do not count compile/setup failures as test proof.
7. Run `./solr-gradle.sh :solr:packaging:devSlim`. On a non-root runner, run the development slim distribution's `bin/solr start -e techproducts` and capture the helpful missing-example message. If the host is root or otherwise cannot perform the live startup, record the exact limit without using `-force` or another workaround.
8. In the separate standalone clone at the same candidate, run:

   ```sh
   ./solr-gradle.sh :solr:solr-ref-guide:buildLocalSite
   ./solr-gradle.sh :solr:solr-ref-guide:checkSiteLinks
   ```

   Inspect the rendered five-minute tutorial and package description. Confirm they direct this bundled-file tutorial to the full package and state that slim omits bundled examples. Do not run root `check` or these Ref Guide tasks in the linked worktree.
9. Confirm a clean final tracked tree and that the fork branch still equals the gated head. Record every skipped or limited leg and all artifact paths.

If a gate repair changes the head, the current job no longer proves it. Post a superseding gate pinned to the repaired head before accepting positive results. This job authorizes no PR comment, PR body edit, JIRA comment, PR ready-state change, or merge.
