# PR 5204 picocli BATS selector: reproduction and narrow implementation (vm4, round 1)

Assignment: `assignments/pr-5204-picocli-review-implementation-and-gate-vm4-round-1.md`.
Claims: `claims/pr-5204-picocli-review-vm4-round-1-phase-r.md`, `claims/pr-5204-phase-r-negative-control-vm4-round-1.md` (superseded by `-b`), `claims/pr-5204-phase-r-negative-control-vm4-round-1-b.md`, `claims/pr-5204-phase-r-post-probe-vm4-round-1.md`, `claims/pr-5204-phase-r-post-probe-lsof-vm4-round-1.md`, `claims/pr-5204-phase-r-post-probe-reaper-vm4-round-1.md`.

## Inputs

- PR: https://github.com/apache/solr/pull/5204, SOLR-18566, author jaykay12 / Jalaz Kumar.
- Reviewed head: `7eb288a6f5777770d4472fc2c550b66e0d1cec12` (read back from `refs/pull/5204/head` on upstream at the time of the run).
- Base and merge base: `79b23df58daac14b92cc35f23b8b81f72098b4ba`. The PR adds 25 commits on top of it.
- Worktree for the probes: `/workspace/worktrees/pr-5204-r`, detached at the reviewed head, unmodified.

## Phase R: reproduction

Source reading (rung 1), at the reviewed head:

- `solr/packaging/test/bats_helper.bash` lines 121-127: `assert_output_for_cli` selects picocli wording on `[ -n "${SOLR_PICOCLI:-}" ]`. Any non-empty value, including `false`, selects picocli.
- `solr/bin/solr` lines 830-835: the start/stop help delegates to Java only for the literal `true`. Otherwise the legacy help prints.
- `solr/packaging/build.gradle` lines 341-343: `SOLR_PICOCLI=true` is set only for `integrationTestsPicocli`. `integrationTests` inherits whatever the shell exports.
- `solr/packaging/test/test_help.bats` line 56: the restart-help test skips on any non-empty value, so an explicit `false` skips legacy coverage.
- `solr/packaging/test/test_post.bats` line 42: a second call site with the same helper.

Probe (rung 2), on the unmodified reviewed head, `env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests -PdisableJsClient=true --tests <file> --rerun --no-daemon`:

| Probe | Tests | Failed | Skipped | Log |
| --- | --- | --- | --- | --- |
| `test_help.bats` | 14 | 7 | 1 (restart help, "not migrated to picocli yet") | `/workspace/gates/logs/vm4/pr-5204/phase-r-negative-control-b/` |
| `test_post.bats` | 12 | 1 | 0 | `/workspace/gates/logs/vm4/pr-5204/phase-r-post-probe-reaper/` |

Failing `test_help.bats` tests, all from `assert_output_for_cli`: 1 "solr help flag prints help" (expected `usage: bin/solr`, output `Usage: solr COMMAND OPTIONS`), 2 "solr with no flags prints help", 3 "start help flag prints help" (expected `usage: bin/solr start`), 4 "start h flag prints help", 5 "stop help flag prints help", 11 "zk help flag prints help" (expected `bin/solr zk ls`), 12 "auth help flag prints help" (expected `bin/solr auth enable`). The other six pass. This matches the review: the legacy launcher prints `Usage: solr ...` and the helper expects the picocli `usage: bin/solr ...` text.

`test_post.bats` test 1 "Check help command" fails at line 42: expected `Missing required option: '--name=name'` (picocli) where the legacy text is `Missing required option: c`. The second call site is confirmed.

The review finding is confirmed as a harness defect. It does not change product behavior.

## Environment findings (not part of the finding)

These came up while getting a Linux BATS run to execute. They are recorded so the final gate can cite them.

1. `:solr:webapp:js-client:jsClientDownloadDeps` fails at the reviewed head with npm `Cannot find package '@babel/plugin-syntax-dynamic-import'` (`/workspace/gates/logs/vm4/pr-5204/phase-r-negative-control/run.log`). The package is not declared anywhere in `solr/webapp/js-client` at that head. The probes pass `-PdisableJsClient=true`, the switch the build documents. The BATS CLI suites do not use the js-client bundle. This is a pre-existing build defect, not part of this PR's change, and it is not fixed here.
2. `solr stop` needs `lsof`, which this VM does not have. Installed lsof 4.99.7 from conda-forge into `/workspace/tools/lsof-env`. Put on PATH for the runs.
3. PID 1 on this VM is a `python3` process that does not reap orphaned children. Solr's JVM is an orphan after `solr start`, so it stays a zombie, and the stop script waits 180 seconds and then fails. Worked around with `/workspace/tools/reap-run.py`, which sets `PR_SET_CHILD_SUBREAPER` and reaps descendants while the Gradle command runs. Wrapping only the Solr commands does not work, because the JVM reparents to PID 1 when that wrapper exits. Run `/workspace/gates/logs/vm4/pr-5204/phase-r-post-probe-reaper/run.log`: 12 tests, teardown clean.
4. The BATS libraries and `bats` 1.8.2 came from `:solr:packaging:downloadBats` (`/workspace/gates/logs/vm4/pr-5204/downloadBats-20261024T204823Z.log`). This VM now lists `bats` in `hosts/vm4.md`.

Earlier runs that stopped before any valid result are kept, not overwritten: `phase-r-negative-control/` (npm stop), `phase-r-post-probe/` (no lsof, teardown failure), `phase-r-post-probe-lsof/` (zombie teardown, selector failure reproduced).

## Phase I: narrow implementation

Branch: `solr-18566-pr5204-cli-review` on `nick-boss-tech/solr`, created from the reviewed head (absent before, confirmed by `git ls-remote`).

Commit `daa29b06e035b57fc5098180113f764dff9cf57d`, author Nick Shanin, no trailers, message `SOLR-18566: Select picocli help wording only for an explicit true`. Read back from the remote: `refs/heads/solr-18566-pr5204-cli-review` is `daa29b06e035b57fc5098180113f764dff9cf57d`.

Files changed (2, 7 insertions, 2 deletions):

- `solr/packaging/test/bats_helper.bash`: `assert_output_for_cli` picks picocli wording only when `SOLR_PICOCLI` equals `true`, matching `solr/bin/solr`.
- `solr/packaging/test/test_help.bats`: the restart-help skip applies only when `SOLR_PICOCLI` equals `true`, so explicit `false` runs the legacy restart help.
- `solr/packaging/test/test_help.bats`: new test "explicit SOLR_PICOCLI=false selects the legacy help wording". It sets `SOLR_PICOCLI=false` only for one helper call, so the rest of the run keeps its mode. With the old predicate, the helper selects the picocli text and the test fails. With the fix, it passes. It runs in both suites.

Compare, reviewed head to new head (cross-fork, read via the GitHub API): https://github.com/apache/solr/compare/7eb288a6f5777770d4472fc2c550b66e0d1cec12...nick-boss-tech:solr-18566-pr5204-cli-review . Status `ahead`, 1 commit ahead, 0 behind, 2 files as listed above.

Not changed: production Java, default parser selection, auth, include-file behavior, the PR body, the changelog fragment, and the optional whitespace and `test_basic_auth.bats` comment hygiene (left as a follow-up, not needed for this proof).

## Proof status

The final-head gate is not run yet. Job: `gates/SOLR-18566-pr5204-cli-review-daa29b06e035.md`. Legs and the status of each:

1. Original-head negative control: done (above).
2. Patched false-mode focused leg (`test_help.bats`, `test_post.bats`, `SOLR_PICOCLI=false`): pending.
3. Default legacy full suite (`:solr:packaging:integrationTests`, variable unset): pending.
4. Picocli full suite (`:solr:packaging:integrationTestsPicocli`): pending.
5. `tidy`, `:solr:packaging:check -x test`, `git diff --check`: pending.

Limits so far: the new test's fail-before has been argued from the predicate, not run on a helper-reverted tree. The review-comment draft is not written yet. The receipt `receipts/SOLR-18566.md` is not written until the gate passes.
