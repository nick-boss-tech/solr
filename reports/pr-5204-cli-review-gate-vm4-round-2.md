# PR 5204 (SOLR-18566) final-head gate, vm4, round 2: passed

- Ticket and PR: SOLR-18566, https://github.com/apache/solr/pull/5204, reviewed head `7eb288a6f5777770d4472fc2c550b66e0d1cec12`, merge base `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Gated head: `daa29b06e035b57fc5098180113f764dff9cf57d` on `nick-boss-tech/solr:solr-18566-pr5204-cli-review`, read back from the remote at 08:37Z. Tree clean at the gated head before and after the gate. `git diff --check` against the reviewed head is clean.
- Compare, reviewed head to gated head: https://github.com/apache/solr/compare/7eb288a6f5777770d4472fc2c550b66e0d1cec12...nick-boss-tech:solr-18566-pr5204-cli-review (1 commit ahead, 0 behind, 2 files: `solr/packaging/test/bats_helper.bash`, `solr/packaging/test/test_help.bats`).
- Claim: `claims/pr-5204-phase-g-final-gate-attempt3-vm4-round-1.md`. Job: `gates/SOLR-18566-pr5204-cli-review-daa29b06e035.md`.
- Earlier round: `reports/pr-5204-cli-review-gate-vm4-round-1.md` is incomplete (two runs lost or overlapped). It is kept as written. This round is the result.
- Host: vm4. JDK 21.0.12.1, Gradle 9.7.0 wrapper, Bats 1.8.2, bats-support 0.2.0, bats-assert 2.0.0, bats-file 0.3.0, lsof 4.99.7 from conda-forge, Mesa EGL. Each Gradle command ran under `python3 /workspace/tools/reap-run.py` so Solr JVMs were reaped. The Gradle lock `/workspace/tools/.gradle-vm4.lock` was held for the whole run by one launcher.
- Seed: not applicable. BATS has no randomized seed.
- Flags on every leg: `-PdisableJsClient=true`. Without it, the js-client npm build fails at this head (`Cannot find package '@babel/plugin-syntax-dynamic-import'`). The BATS CLI suites do not use that bundle.

## Legs (one launch, run in order, all exit 0)

| Leg | Command | UTC start to end | Plan | ok | not ok | skipped | Logs |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 1. Focused false-mode | `env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests --tests test_help.bats --tests test_post.bats --rerun --no-daemon` | 01:54:32 to 02:00:32 | 27 | 27 | 0 | 0 | `/workspace/gates/logs/vm4/pr-5204/gate-leg1-focused-attempt3/` |
| 2. Legacy full suite | `env -u SOLR_PICOCLI ./gradlew :solr:packaging:integrationTests --rerun --no-daemon` | 02:00:36 to 05:13:20 | 128 | 123 | 0 | 5 | `/workspace/gates/logs/vm4/pr-5204/gate-leg2-legacy-full/` |
| 3. Picocli full suite | `./gradlew :solr:packaging:integrationTestsPicocli --rerun --no-daemon` | 05:13:20 to 08:27:31 | 128 | 122 | 0 | 6 | `/workspace/gates/logs/vm4/pr-5204/gate-leg3-picocli-full/` |
| 4. Tidy and check | `./gradlew tidy :solr:packaging:check -x test --no-daemon` | 08:27:31 to 08:29:43 | none (module tasks) | n/a | 0 | n/a | `/workspace/gates/logs/vm4/pr-5204/gate-leg4-checks/` |

Leg 4 ran `tidy` for every module and `:solr:packaging:check -x test`, and the build succeeded. `tidy` did not change the tree, so the gated head is the tested head.

Each leg's JUnit XML is in its `junit/` directory: 128 testcases with 5 skipped in leg 2, and 128 with 6 skipped in leg 3. Failures are 0 in both. Leg 1's XML has 15 testcases in `test_help.bats` and 12 in `test_post.bats`.

## Skips and reasons

Leg 2 (5 skips):
- 44 "using curl to extract a single pdf file": Docker is not available.
- 45 "using the bin/solr post tool to extract content from pdf": Docker is not available.
- 46 "using the bin/solr post tool to crawl web site": Docker is not available.
- 81 "Docker SolrCloud rolling upgrade": Docker is not available.
- 97 "check stop command doesn't hang": the `timeout` utility is not available.

Leg 3 (6 skips): the same five as leg 2, plus 54 "restart help flag prints help": "restart is not migrated to picocli yet". That is the intended skip from the review.

No test was skipped in leg 1.

## Findings the gate proves

- Under `SOLR_PICOCLI=false`, the helper selects the legacy wording and the restart-help test runs. The explicit false-mode regression test passes in leg 1.
- Under the default legacy suite (leg 2), the legacy help tests pass with the patched helper.
- Under the picocli suite (leg 3), the picocli wording is used and the 122 tests that can run in that mode pass. The only skip beyond Docker and `timeout` is restart help, as expected.

## Limits

- Five legacy skips and six picocli skips are environmental (Docker, `timeout`). They are not covered here.
- The restart-help test for picocli is skipped by design, so picocli restart help has no coverage on this PR.
- The js-client build defect at the reviewed head is a pre-existing build issue. The gate avoids it with `-PdisableJsClient=true`. It is not fixed by this PR.
- The review finding is a harness defect, not a product defect. Production Java is unchanged.

## Not done

- No public PR, comment, or review. No push other than the fork branch (already at the gated head).
- The review-comment draft `reports/pr-5204-review-comment-draft-vm4-round-1.md` is not written in this round. See the next step.
- The worktree `/workspace/worktrees/pr-5204-impl` is kept until the owner decides on the PR.
