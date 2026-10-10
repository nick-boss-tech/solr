# CLI, bin scripts and packaging round 1, part S5: SOLR-10390 and SOLR-10667 (audit only, no gate), and the SOLR-10390 and SOLR-18339 interaction

No builds, Gradle, tests, BATS, `gh` writes, commits, pushes or posts. No files edited. Read-only git calls, in-memory merge simulations (these write tree objects to the object store, but no refs or worktree files), and one bash probe of `/dev/tcp` against a closed local port. No drafts written.

## Head verification

- **SOLR-10390:** `ls-remote refs/heads/solr-10390-submit` is `4af4a6834e2b43251381027c24b18761fc5a9513`. It matches the receipt. The local `refs/remotes/origin/solr-10390-submit` is already at that SHA, so no fetch was needed. Branch commits over base `cabedd1d968`: `3f4e4973649` (`bin/solr` and the BATS test), `5c813432a35` (changelog), and `4af4a6834e2` (`SOLR-10390-TESTING.md`).
- **SOLR-10667:** `ls-remote refs/heads/solr-10667-submit` is `32b594f280c5c5f0e5241dadbad7677cdb563c3e`. It matches the receipt. The 2026-10-06 registration tip `5cee0d5bcf1` is an ancestor. Branch commits over `cabedd1d968`: `045e1569396` (BATS check only), `23a4d5e6b3a` (changelog), `5cee0d5bcf1` (handoff doc), and `32b594f280c` (`packaging.gradle` fix).
- `upstream/main` is `8e62c2686882`. Both branches sit on `cabedd1d968`, 37 commits behind main. `solr/bin/solr`, `solr/packaging/test/test_start_solr.bats`, `solr/packaging/test/test_modules.bats` and `gradle/solr/packaging.gradle` are unchanged between `cabedd1d968` and main, so the branch hunks land on the same lines as main. The in-memory merge onto main is clean for both heads.
- For the interaction, SOLR-18339 head `47e53884609c` was also verified (it matches the receipt). Its base is `14c7aac0`, 66 commits behind main. It merges cleanly onto main in simulation. Main's only nearby `bin/solr` change is SOLR-17697 (picocli), at about line 828, outside the 18339 hunk.

## 1. SOLR-10390 (audit only, no gate, premise unverified)

**Premise, read against main `8e62c2686882`, `solr/bin/solr`:**
- The lsof call sites on main are exactly two, both in the start wait: line 1385, `if lsof -v 2>&1 | grep -q revision`, and line 1391, `running=$(lsof -t -PniTCP:... -sTCP:LISTEN || :)`. The no-lsof branch is lines 1409 to 1414: a NOTE at 1410, `sleep 10` at 1411, a ps-based pid lookup at 1412, the "Started Solr server" echo at 1413, and `return` at 1414. On that branch nothing listening on the port is ever checked.
- Stop does not use lsof. `stop_solr` (lines 513 to 570) uses pid files and `ps -o stat`. The pre-start "already in use" check (lines 1013 to 1021) uses `solr_pid_by_port` and ps. Status does not use lsof: main line 628 hands status to `run_tool` (SolrCLI).
- Outside `bin/solr`, `install_solr_service.sh` line 199 runs `lsof -h` to print a recommendation.
- So the start wait is the only lsof dependency in `bin/solr`. The ticket's wider "start and stop" framing is no longer true on main.
- **Premise verdict (by reading):** true for the lsof-absent case. Main prints the NOTE, sleeps 10 seconds, then reports success without checking.

**What the branch covers** (head `4af4a6834e2b`; helper `port_is_listening`, lines 468 to 481; call site line 1404):
- lsof is used whenever `lsof -v` output contains "revision" (lines 472 to 473). Otherwise the probe is `(exec 3<>"/dev/tcp/$host/$port")` (line 479), with host `SOLR_HOST_BIND`, default 127.0.0.1, and 0.0.0.0 or :: mapped to 127.0.0.1.
- **Covered:** lsof missing, or lsof present but its `-v` output lacks "revision".
- **Not covered:** lsof present, `-v` prints "revision", but `lsof -t` returns nothing. That path stays on lsof and prints "Still not seeing Solr listening on <port> after <N> seconds!" after `SOLR_START_WAIT` (default 180, main lines 154 to 155), even when Solr is up. This is the ticket's ptrace case if a broken lsof still prints a version line (unverified). The BATS fake (exit 1 for every call, including `-v`) never reaches this path.
- Bracketed hosts such as `[::]` and `[::1]` are not mapped or bracketed. Not run.
- **A shell without `/dev/tcp`** (by reading): the redirection fails, the probe returns nonzero, and the wait treats the port as closed for the whole `SOLR_START_WAIT`. It prints "Still not seeing..." and tails `solr.log` while Solr may be healthy. On main, the same no-lsof environment reports success after 10 seconds. So the branch regresses for the combination of no lsof and no `/dev/tcp`. The exit status by reading is 0 on both paths (the function ends with the spinner's printf); not run. A local probe: the workspace bash is Cygwin GNU bash 5.3.9, and a refused connection to 127.0.0.1:1 returns exit 1, which confirms the probe's nonzero return. A bash without `/dev/tcp` cannot be produced here.
- lsof present and working: the same `lsof -t` check, plus one extra `lsof -v` per 2-second loop iteration, with no output change.
- lsof present but `-v` lacks "revision": behavior changes from 10 seconds of blind success to a TCP poll.
- Stop and status: no branch hunk touches them. Unchanged.
- Windows: `bin/solr` refuses Cygwin (main lines 60 to 62). `solr.cmd` never used lsof (lsof appears only in `solr/bin/solr` and `install_solr_service.sh`), so leaving `solr.cmd` unchanged is correct, not a gap.

**Verdict: audit only, not draftable.** The premise holds for the lsof-absent case. The fallback is narrower than its wording (see section 4). It is also superseded by SOLR-18339 in the same wait (section 3).

**Premise run needs (main side, not run here):**
- (a) A Linux or macOS host, not this Cygwin workspace.
- (b) A PATH with the lsof directory removed, not cleared. Confirm that `command -v lsof` is empty in that shell. Keep ps, awk, grep, sort, tr and sed. The receipt's "PATH without lsof" is right, with this detail.
- (c) A start that cannot listen: an invalid heap such as `SOLR_JAVA_MEM="-Xmsinvalid"` (the 18339 head uses this trick, in the test at line 31 of its `test_start_solr.bats`), with `SOLR_START_WAIT` small.
- (d) Base `8e62c2686882`: expect the NOTE "Please install lsof", about 10 seconds, then "Started Solr server on port 8983 (pid=)" with nothing listening. That is the premise.
- (e) Head `4af4a6834e2b`, same setup: no NOTE; the probe fails until `SOLR_START_WAIT`; then "Still not seeing..."; for a healthy start, one "Started" line.
- (f) A second premise run for the ptrace gap: a stub lsof that prints a "revision" line for `-v` and nothing for `-t`, with a healthy Solr. Expect the false failure on both base and head. This shows the branch does not cover it.
- (g) The BATS first verification (main side): on base the new test fails at `refute_output --partial 'Please install lsof'` (head line 121 onward); on head it passes.
- (h) Not settled by any run: what a real broken lsof prints for `-v`.

## 2. SOLR-10667 (audit only, no gate, premise unverified; BATS never run)

**Premise, read against main:**
- `gradle/solr/packaging.gradle`: `assemblePackaging` is lines 80 to 102. It copies `README.md` (line 81), `tasks.jar` into `lib` (lines 83 to 85), and external runtime libs into `lib` (lines 87 to 99), and into `deps` (line 101). No example copy exists. The premise is confirmed by reading.
- **Modules with an example directory on main:** only `solr/modules/ltr/example`. All 14 children of `solr/modules` were checked: analysis-extras, clustering, cross-dc, cuvs, extraction, gcs-repository, jwt-auth, langid, language-models, ltr, opentelemetry, s3-repository, scripting and sql. The LTR example tracks `.gitignore`, `README.md`, `config.json`, `exampleFeatures.json`, `libsvm_formatter.py`, `train_and_upload_demo_model.py` and `user_queries.txt`. The other directories named `example*` are under `src/test-files`, not module roots.
- **Distribution path:** `solr/packaging/build.gradle` lines 55 and 56 make each module's packaging configuration a dependency of `modules`. The full distribution copies `configurations.modules` into "modules" (lines 139 to 141). The slim distribution (lines 102 to 119) has no modules. So a copy into the module packaging directory lands at `modules/ltr/example` in the full distribution only. The README's `cd modules/ltr/example` (README step 4) is distribution-relative, which matches. The ref guide `query-guide/pages/learning-to-rank.adoc` line 787 points readers to git instead.
- **Right place:** `gradle/solr/packaging.gradle`, yes. The root spec has no per-module knowledge and consumes only module packaging artifacts. `README.md` is already copied the same way (`packaging.gradle` line 81). The distribution-spec alternative would name `:solr:modules:ltr` inside `solr/packaging/build.gradle`, hard-coding one module. A side effect: the block also applies to every module and to `:solr:cross-dc-manager` (scope at lines 38 to 40). Harmless today, since cross-dc-manager has no example directory.
- **Cautions for the branch as written:**
  1. `from("example")` copies everything in the source directory, including the gitignored `solrclient/` that `copyPythonClientToExample` writes into it (LTR `build.gradle` lines 23 and 62 to 66), and any local liblinear symlink from README step 2. The distribution would then depend on local working-tree state. Recommend excluding `solrclient/**`.
  2. The tracked `.gitignore` would be copied too (Gradle copies dotfiles by default; not run). The TESTING note's file list omits it.
  3. Possible Gradle implicit-dependency problem: `assemblePackaging` reads `example/`, which another task writes. LTR's `localPythonClientCopy` (`build.gradle` lines 40 and 41) is not consumed by `assemblePackaging`, so there is no declared dependency. Whether Gradle warns or fails is unknown; the premise run would show it.
  4. A missing source directory is silently ignored: the TESTING note's assumption, not checked.
- **BATS check:** `test_modules.bats` lines 42 to 47 (head) run `ls "${SOLR_TIP}/modules/ltr/example"` and assert three filenames. It exercises the assembled and installed full distribution, not build output: `integrationTests` depends on `installFullDist` (`solr/packaging/build.gradle` line 291), and `SOLR_TIP` is `distDir` (line 329). On base the `ls` fails with no such directory, so the assertion fails. That is a real fail-before, by reading. Limits: presence of three files only, and no slim coverage.

**Verdict: audit only.** The premise is confirmed by reading, and the placement is right. Before any draft, the branch needs the `solrclient` exclusion and a decision on dotfiles. Verification is owed on main.

**Premise run needs (main side):**
1. Assemble the full distribution at base `8e62c2686882` (`installFullDist`), and show that `modules/ltr` has no example directory, while the module packaging directory holds only `README.md` and `lib`.
2. Run the branch's new test on that install; expect failure.
3. Apply the fix (head lines 83 to 85), reassemble, show `modules/ltr/example` with the listed files, and expect the test to pass.
4. Look at the Gradle output for implicit-dependency warnings, with `solrclient/` absent and present.
5. Confirm what gets packaged with `solrclient/` present, which is the reproducibility question.

## 3. Interaction: SOLR-10390 and SOLR-18339

**Facts:**
- The 18339 head replaces the whole start wait (`bin/solr` lines 1377 to 1400). There is no lsof, no `/dev/tcp`, no subshell and no spinner. The probe is `run_tool status --solr-url ... --max-wait-secs "$SOLR_START_WAIT"` at line 1392, with the failure text and exit 1 following.
- The 18339 success text comes from `StatusTool`: "Waiting up to ..." (`StatusTool.java` line 194) and "Started Solr server on port N. Happy searching!" (line 197, no pid). Main `StatusTool` line 293 already prints the same success text.
- A three-way merge of the two heads (merge base `14c7aac0`) reports a content conflict in `solr/bin/solr` and auto-merges `test_start_solr.bats`. The 10390 helper hunk (lines 465 to 481 on main) is not part of the 18339 hunk, so a line-level merge would keep the helper as dead code unless it is removed by hand.
- Probe call sites: on the 10390 head the helper has exactly one call site (line 1404). The probe serves no other call site on main or on 18339.

**Order A: 18339 lands first.**
- 10390's start-wait change is superseded, and its helper has nothing left to serve. After 18339, `bin/solr` has no lsof reference except two comments (lines 59 and 61). Stop uses pid files and ps; status uses SolrCLI. 18339 also removes `install_solr_service.sh` line 199 and the Red Hat lsof note in `taking-solr-to-production.adoc` line 75.
- What remains of 10390 is its BATS test (head line 121). On the 18339 head it still passes with no 10390 code: nothing calls lsof, `StatusTool` prints "Started Solr server on port", and "Please install lsof" no longer exists. So on that base it is a regression guard, not a fail-before proof. The changelog fragment and TESTING note describe a fallback that would no longer exist and must go.
- Recommendation: close 10390 as superseded, or resubmit only the test as a no-lsof guard for 18339. Either way, the `bin/solr` fallback is dropped.

**Order B: 10390 lands first.**
- 18339's wait replaces 10390's loop. The conflict in `solr/bin/solr` resolves to 18339's block, and the 10390 helper (lines 468 to 481) must be deleted explicitly, or it stays as dead code that still names lsof.
- The no-lsof environment after 18339 is handled by `StatusTool`'s HTTP poll. There is no lsof, no `/dev/tcp`, and no fixed sleep. The no-`/dev/tcp` false failure disappears with the fallback.
- Trade-offs: 18339 waits on HTTP status, not a socket, which is stricter. It inherits the auth Choice in its receipt: `bin/solr` line 1392 passes no `--credentials`, and `StatusTool` rethrows auth-related exceptions instead of retrying (`statusFromRunningSolr` lines 254 to 256; the retry loop at lines 275 to 277 at 18339's head). A secured node answering 401 is reported as a failed start. That exposure is specific to the 18339 wait; 10390's TCP probe never sees a 401.
- 18339 also removes the subshell and the spinner, so 10390's spinner-wrapped loop is gone too. Whether the 7924 spinner survives is part S1's question.

**Order recommended:** 18339 first, and 10390 is then superseded. Under 18339's own timing precondition (SOLR-18336 live in 10.1 for some time), the owner must choose between waiting and an interim 10390 landing with its two gaps.

## 4. Receipt and branch-text disagreements (exact wording)

- **10390 receipt, line 5:** "The branch changes `bin/solr` (start detects the listening port without lsof, via a bash `/dev/tcp` fallback)". Overstated. lsof stays first whenever `lsof -v` prints "revision" (head lines 472 to 473). Suggested wording: "without a working lsof".
- **10390 receipt, line 6:** "a fair premise setup needs a PATH without lsof so the fallback is the path exercised." Correct but incomplete. On main, a PATH without lsof exercises the sleep-10 branch, not a fallback. The premise run must also show main reporting success with nothing listening. The receipt does not cover the lsof-present-but-silent case, which needs a stub.
- **10390 changelog title** (branch file `changelog/unreleased/SOLR-10390-start-without-lsof.yml`): "when lsof is missing or unusable it falls back to a bash `/dev/tcp` connect". "Unusable" overstates. The fallback triggers only when `lsof -v` output lacks "revision". Suggested wording: "when lsof is missing or its version check fails".
- **10390 TESTING note:** "`install_solr_service.sh` still recommends lsof (left as is, the recommendation is still valid for stop/status)." Wrong on main. Stop and status do not call lsof (`stop_solr` lines 513 to 570; status line 628). The install message (line 199) says "for more stable start/stop", which is stale on main regardless.
- **10390 TESTING note:** "Hoss: lsof is more common than perl; if removed, do a plain connect." Incomplete. Hoss's JIRA comment (SOLR-10390 JSON, comment 3) says: "we might as well implement same basic logic as your suggested C/perl code in SolrCLI so that we can use it on windows as well." That SolrCLI direction is what 18339 does. Robert Muir's `/dev/tcp` suggestion is quoted correctly.
- **10390 TESTING note, fail-before:** "the output assertion fails". The failing line on main is a negative assertion, `refute_output --partial 'Please install lsof'`.
- **10390 TESTING note:** "Earlier audit note: 'feature request (lsof replacement)'" and the receipt's JIRA references are not in the JIRA JSON. The JSON has five comments and was last updated 2019-06-08. Not verified.
- **10667 TESTING note, file list:** "`solr/modules/ltr/example/` (README, `config.json`, `exampleFeatures.json`, `libsvm_formatter.py`, `train_and_upload_demo_model.py`, `user_queries.txt`)". It omits the tracked `.gitignore`, which would be copied.
- **10667 commit `045e1569396` subject:** "SOLR-10667: package module example directories (modules/ltr/example) in the distribution". That commit adds only the BATS check (7 lines). Its successor `32b594f280c` says so: "(the 045e156 commit only added the BATS check)". The subject overstates. It matters only if commits are not squashed.
- **10667 changelog title:** "The Solr distribution now includes the Learning To Rank example files (`modules/ltr/example`)." The example ships only in the full distribution (the slim distribution has no modules). The scope wording should say "full distribution".
- **10667 TESTING note:** "The audit note said 'ant build, obsolete'." Not in the JIRA JSON (`CommentCount` 0). Not verified.
- **Matches:** the 10667 receipt's line 5 (the tip moved on 2026-10-08, and the top commit changes only `gradle/solr/packaging.gradle`) is correct. The 10667 TESTING note's "`ltr` is the only module with an `example` directory today" is correct.

## 5. Owner decisions

1. **10390 fate:** close as superseded when 18339 lands (recommended), or keep only its BATS test as a no-lsof guard. Either way, drop the `bin/solr` fallback.
2. **Interim question:** while 18339 waits on its timing precondition, land 10390 as an interim? Recommendation: no. If yes, first fix the two gaps (the no-`/dev/tcp` false failure, and the lsof-present-but-silent case, which is not covered).
3. **18339 auth Choice:** confirm or fix credentials in the status wait before 18339 lands. This decides whether 18339 cleanly supersedes 10390 for secured clusters.
4. **Docs and installer:** the Red Hat lsof note and `install_solr_service.sh` line 199 remain under 10390 alone, and 18339 removes them. Confirm that 18339 carries them.
5. **10667 placement:** keep the copy in `gradle/solr/packaging.gradle` (recommended), not the distribution spec.
6. **10667 copy scope:** exclude `solrclient/**` (recommended), and decide on `.gitignore` and local symlinks.
7. **10667 changelog and ref guide:** the "full distribution" wording, and whether `learning-to-rank.adoc` line 787 is updated in the same change.
8. **10667 target branches:** main only, or backports to `branch_10x` and `branch_9x`. The receipt names no target, and it was not checked whether those branches have the same `packaging.gradle`.

## 6. Not checked

- Builds, Gradle, tests, BATS, and any run of the scripts: excluded by the round's rules. Verification of both tickets is main-side work.
- What a real broken lsof (on a ptrace-restricted kernel) prints for `-v` and `-t`: no access.
- A bash without `/dev/tcp`: not available here. Only the Cygwin bash probe was run.
- IPv6 `SOLR_HOST_BIND` forms in the probe: not run.
- Jetty log noise from the bare TCP probe: not checked.
- Gradle behavior for a missing `from` directory, dotfile copying, symlink following, and implicit-dependency reporting: stated from general knowledge and code reading, not run.
- The 18339 auth path: read at `StatusTool` lines 254 to 277 and in the receipt, not traced end to end. Part S4 owns it.
- The `test_start_solr.bats` counts and names across branches: part S6's work. This part checked only that 10390's test name is unique against 18339, and that the two merge cleanly in the test file.
- The 7924 and 12347 spinner and stop-wait questions: outside this part.
- Newer JIRA comments after the JSON snapshots (10390 last updated 2019-06-08; 10667 last updated 2021-08-14).
- Target branches for 10667 beyond main: not checked.
