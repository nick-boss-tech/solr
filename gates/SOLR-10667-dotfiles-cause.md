# Gate job: SOLR-10667 example `.gitignore` in the distribution (cause trace, then the fix decision from gates/SOLR-10667-dotfiles.md)

- Branch: solr-10667-submit. Head: 9ce9ef1db5e9d33312ac628d2e3f0b236a474c36 (live tip, verified by ls-remote by the main agent at filing, 2026-10-14; GATE GREEN at this head per receipts/SOLR-10667.md and gates/SOLR-10667-exclusion.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
Status: CLAIMED (claims/pool-vm2-gate-backlog-10667-cause.md, vm2, 2026-10-16)
- Capability tags: gate, premise-run.
- Run shape: claim first under claims/. All Gradle work runs under the host's blocking test-queue flock, one build at a time, through the host's own route per its host file (vm1: ~/workspace/tools/solr-gradle.sh, never ./gradlew; vm2: ./gradlew with the vendored JDK and GRADLE_USER_HOME under /workspace). Copy ~/workspace/solr/gradle.properties into the worktree. Distributions assemble with `:solr:packaging:installFullDist` (not `assembleDist`) and a fresh NPM_CONFIG_CACHE, as in the earlier 10667 gates. Test counts come from fresh JUnit XML only.

## What is already settled (do not re-litigate)

- The tracked file exists: solr/modules/ltr/example/.gitignore (one line: `solrclient/`).
- The file is ABSENT from the assembled full distribution at the head. Confirmed by run twice: the exclusion gate's rc 1 presence check (2026-10-13) and the dotfiles premise run's probe (2026-10-15, 0 present, 1 absent), with the six other tracked example files present in both runs.
- The branch's own exclusion, `solrclient/**` in the copy spec at gradle/solr/packaging.gradle, cannot remove a top-level `.gitignore`; the spec names no dotfile rule. reports/cli-bin-packaging-round-1-s5.md expected Gradle to copy dotfiles by default (read, not run); the runs show it does not, for a reason nobody has isolated yet.

## The exact question the first run failed to isolate

Which layer drops `modules/ltr/example/.gitignore` between the tracked source file and the assembled distribution:

- (A) the module's `assemblePackaging` Sync task in gradle/solr/packaging.gradle: `from("example", { into "example"; exclude "solrclient/**" })`, whose output the distribution consumes through the module's `packaging` configuration;
- (B) the distribution copy in solr/packaging/build.gradle, where the installFullDist chain copies `configurations.modules` into `modules`;
- (C) neither file's text: a behavior of one of those two operations, or of the configuration resolution between them.

The first run (vm2, 2026-10-15T02:05:07Z to 02:18:24Z) captured Gradle `--info` output without discriminating among these, and also ran the step 1b probe without recording its finding. Discriminator for this run: inspect the module's packaging output (the assemblePackaging destination for :solr:modules:ltr) before the distribution copy consumes it. Present there and absent in the distribution puts the drop at (B) or (C)-between; absent there puts it at (A) or (C)-inside.

## Steps

1. Cause trace. At the head above, assemble with `:solr:packaging:installFullDist`. Record `.gitignore` presence at three points: the source tree (present), the ltr module's assemblePackaging output (present or absent), and the assembled distribution (absent; re-confirm in this run). Record which boundary drops the file, with the log paths. If the boundary alone does not name the mechanism, run the single responsible layer's task with `--info` and search its output for the file name and the example file list; a diagnostic print of the copy spec's resolved file collection is allowed as a scratch change in the gate worktree only, never committed. Record the cause in this file: the rule, the spec behavior, or the task behavior, with the evidence.
2. Step 1b record (owed from the first run). With a probe soft link `modules/ltr/example/liblinear` to a local directory present in the source tree (removed afterward, never committed), assemble and record what the distribution holds for `modules/ltr/example/liblinear`: absent, a link, a copied directory, or a failure. Record only; a change for the link is a follow-up job, not this one.
3. Branch change decision (carried from gates/SOLR-10667-dotfiles.md step 2). If the cause is a copy rule in gradle/solr/packaging.gradle or solr/packaging/build.gradle: add the minimal include or change so the example's `.gitignore` is copied, touching nothing else in the copy; one new commit on top of the head, pushed plainly (fast-forward) after step 4 is green. If the cause sits outside those files (a Gradle behavior or a task this branch cannot change in the copy): make no branch change; record the cause and the evidence here and mark the job DONE with the result NO BRANCH CHANGE.
4. Head leg (after a branch change only). With the solrclient probe present in the source tree (a marker file at solr/modules/ltr/example/solrclient/, as in gates/SOLR-10667-exclusion.md), assemble with `:solr:packaging:installFullDist`; assert `modules/ltr/example/.gitignore` present, `modules/ltr/example/solrclient` absent, and the other six tracked example files present. Run `solr/packaging/test/test_modules.bats` filtered to 'ltr module ships its example directory' with SOLR_TIP set to the assembled distribution; count from its fresh JUnit report (1 test, 0 failures). Remove both probes before the tidy step; never commit them.
5. Gate steps after a branch change: changelog YAML parse (no fragment change expected; parse the existing one); tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward). No Java file changes are expected; if one changes, stop and record here.
6. Records. On a branch change: refresh receipts/SOLR-10667.md with the new head, the one-line change, the assembly results and the BATS count; in pr-drafts/cli/SOLR-10667.md remove the Limits bullet that says the example's `.gitignore` is not in the assembled distribution, change the Proof sentence "The hidden `.gitignore` file does not appear in the assembled distribution." to say the file is present, and re-point every link to the new head (re-check each line anchor); the Choice section stays. On NO BRANCH CHANGE: the draft's `.gitignore` Limits bullet stays as written and gains the cause in plain words.
7. Mark this job DONE and the claim DONE.

## Stop conditions

- If the trace shows the drop happens in a task, plugin or resolution step the branch cannot change from the two named files, that is the NO BRANCH CHANGE outcome of step 3, not a failure: record it and finish.
- If a Java file would have to change, stop and record here for the main agent.
- If the assembly fails twice with the same non-branch environment failure, record an environment block with the log evidence and stop. The known pattern (vm2 js-client download, "Cannot find package '@babel/plugin-syntax-dynamic-import'") is cleared by a fresh NPM_CONFIG_CACHE and is not a branch failure.
- A test-executing failure is never relaunched by the queue cron; record it here and in the claim for the main agent.

## Step checklist

- [ ] cause trace at 9ce9ef1db5: presence recorded at the source, the module packaging output and the distribution; dropping layer and mechanism recorded with log paths
- [ ] step 1b record: what the distribution holds for a local `liblinear` link, recorded here
- [ ] branch change, if the cause is in the copy spec; or NO BRANCH CHANGE recorded with the cause
- [ ] head leg with the probes (after a branch change only): `.gitignore` present, `solrclient` absent, six other tracked files present
- [ ] BATS check 'ltr module ships its example directory' at the new head, counted from fresh JUnit XML (after a branch change only)
- [ ] changelog YAML parse; tidy clean (GATE INVALID guard) (after a branch change only)
- [ ] receipt refreshed, draft updated, job and claim DONE
