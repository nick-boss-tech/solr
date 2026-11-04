# Gate: SOLR-18169 PR 5289 reviewer follow-up, round 3

Status: OPEN. Implemented, Proof Pending. Supersedes `gates/SOLR-18169-reviewer-followup-a8d2cfb1.md` and `gates/SOLR-18169-reviewer-followup-4f3346a3.md`. Older jobs do not prove this candidate.

- PR: https://github.com/apache/solr/pull/5289 (draft).
- Ticket: SOLR-18169.
- Report: `reports/pr-5289-packaged-readme-f2166af9.md`.
- Tags: gate, records.
- Intended host: capable Linux gate host with JDK 21, the pinned Gradle 9.7.0 wrapper/build dependencies, Git, and the Ref Guide Node/npm toolchain. Live launcher proof requires a non-root account; do not bypass root restrictions.
- Fork branch: `nick-boss-tech/solr:solr-18169-slim-no-examples`.
- Exact candidate: `f2166af983d2536d51e4b7ba3bc875e89cbdc1bd`.
- Immediate parent: `a8d2cfb124f584395478124e15c334c435b3119e`.
- Diagnostic baseline: `4f3346a3064aa137d8eb4be5de9adb1e9635adc5` (9 tests and previous wording), deliberately not the immediate parent.
- PR base: `ad26fdf2c6bee8168eae043b94833f05643d1995`.
- Modules: `solr/packaging`, `solr/core`, `solr/solr-ref-guide`.
- Focused seed: `18169C0FFEE18169`.
- Idempotency key: `SOLR-18169-reviewer-followup@f2166af983d2536d51e4b7ba3bc875e89cbdc1bd`.
- Claimant: unclaimed. Nick will take this implementation to the gate after review. Refresh the fork head and board and check for a live execution claim before claiming.
- Suggested logs: `/workspace/gates/logs/<host>/solr18169-reviewer-followup-r3/`.
- Receipt: `receipts/SOLR-18169-reviewer-followup-f2166af9.md`, owned by the executing host.

## Review disposition

Source review covered the full branch diff, shared commons-cli/picocli validation, example assembly, existing successful example tests and relevant PR 4876 comments. The packaged README still advertised examples in slim; candidate f2166af9 fixes the launch instructions and file list. No production or test change was added after a8d2cfb1. Static diff whitespace checks passed; Windows ran no Gradle or tests. The final PR diff has seven files.

## Required proof

1. Use a clean checkout at the candidate. Record head, immediate parent, clean status, JDK, host, runner, paths and fork ref. Serialize builds under the host lock and use `./solr-gradle.sh`. Run Ref Guide tasks in a separate standalone clone at the same candidate with a real `.git` directory, not a linked worktree.
2. Run the detached tidy preflight, then `./solr-gradle.sh tidy` at the candidate. Require no tracked changes. A scoped tidy repair must be committed and plain-pushed with Nick Shanin's identity; post a superseding gate at the repaired head before accepting positive proof.
3. Run both distribution installs:

   ```sh
   ./solr-gradle.sh :solr:packaging:installSlimDist :solr:packaging:installFullDist
   ```

   Save installed-tree listings: slim must omit `example/`; full must retain README.md, exampledocs and films under `example/`. Inspect each installed, filtered `README.txt`. Both must qualify examples and techproducts instructions as requiring full, state that slim omits examples, and mark `example/` excluded from slim. Save README excerpts from both packages.
4. Run the diagnostic-baseline suite at `4f3346a3064aa137d8eb4be5de9adb1e9635adc5` and the focused suite at the candidate, in isolated checkouts with fresh XML:

   ```sh
   ./solr-gradle.sh :solr:core:test --tests org.apache.solr.cli.TestSolrCLIRunExample -Dtests.seed=18169C0FFEE18169
   ```

   Expect baseline 9/9 and candidate 10/10, zero failures, errors or skips. Include techproducts and films missing-default-directory cases, explicit invalid directory and unsupported example with examples present. Record actual XML counts. At the candidate also run the existing neighboring parser suite:

   ```sh
   ./solr-gradle.sh :solr:core:test --tests org.apache.solr.cli.SolrCLIRunExamplePicocliTest -Dtests.seed=18169C0FFEE18169
   ```

   Expect 16 inherited/additional tests with zero failures, errors or skips; record actual counts and reconcile discrepancies. The newly added inherited tests call initDirs/runImpl directly and do not establish picocli-specific failure output; describe that limit.
5. Message fail-before: at diagnostic baseline `4f3346a3064aa137d8eb4be5de9adb1e9635adc5`, overlay only the candidate's `TestSolrCLIRunExample.java`, leaving production unchanged. Run the focused suite; expect exactly the two revised message assertions to fail on the previous wording and the other eight tests to pass. Save isolated diff, complete logs and fresh XML. Compile/setup failures are not fail-before proof.
6. Films coverage discrimination: in an isolated candidate checkout keep tests unchanged. Apply a compiling synthetic mutation that, only for `films` in the missing-default-directory branch, throws the old generic invalid-directory error instead of the helpful message. Keep APIs and normal validation intact. Run `TestSolrCLIRunExample.testMissingDefaultExampleDirectoryExplainsSlimDistributionForFilms`; require its exact-message assertion to fail. Preserve mutation diff, log and fresh XML, then restore production. This proves added coverage discriminates the shared diagnostic behavior, not a separate production bug.
7. Run the core check with Error Prone on compilation:

   ```sh
   ./solr-gradle.sh :solr:core:check -x test -Pvalidation.errorprone=true
   ```

   Preserve full log and exit status; do not count compilation/setup failures as assertion evidence.
8. Run `./solr-gradle.sh :solr:packaging:devSlim`. As a non-root runner invoke that slim installation's `bin/solr start -e techproducts` and `bin/solr start -e films`. Require nonzero status and the helpful missing-example message for each before server startup. Save commands, output and statuses. If the host cannot perform this leg, record the exact limit without force or another workaround; do not mark it passed.
9. In the standalone clone at the candidate, run:

   ```sh
   ./solr-gradle.sh :solr:solr-ref-guide:buildLocalSite
   ./solr-gradle.sh :solr:solr-ref-guide:checkSiteLinks
   ```

   Inspect rendered five-minute tutorial and installation guide. Confirm the tutorial downloads/extracts/enters full so its later books.json input is available, and the package description says slim omits bundled examples. Save rendered excerpts. Do not run guide tasks or root check in a linked worktree.
10. Require clean final tracked trees, restored mutations and live fork head matching the tested head. Receipt must identify every proof leg, commands/statuses, fresh XML counts/skips, logs, package/README excerpts, rendered-guide evidence, environment repairs and unperformed legs. Keep Implemented, Proof Pending if required evidence is incomplete. Receipts at f5a5480 do not prove this candidate.

Investigate executed failures before retrying under board repair rules; separate environment interruptions from assertion failures. Source or formatting changes require a new pushed head and superseding gate. This job authorizes no public comments, body edits, PR state changes or merge.
