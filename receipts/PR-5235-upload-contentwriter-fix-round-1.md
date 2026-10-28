# Receipt: PR 5235 upload ContentWriter fix, round 1 (vm4)

- Gate: `gates/pr-5235-upload-contentwriter-fix-round-1.md`. Claim: `claims/pr-5235-upload-contentwriter-fix-round-1-vm4.md`. Outcome: `reports/pr-5235-upload-contentwriter-fix-round-1-outcome.md`.
- Ticket: SOLR-18351. Apache PR 5235.
- Exact patch head: `e2ad60ecedda7451cb331538a5fd40b66144126b` on `nick-boss-tech/solr:solr-18351-submit`. Parent `caf8dde9063fd2470c02d9938b5723e691c0e11e`.
- Apache PR 5235 head is still `ccace89730ae47cf94127643a39da6d57fe22e22` in this receipt's scope. This gate does not validate that head.
- Verdict: GREEN at fork head `e2ad60e`. Linux only. The Windows run is not part of this receipt.

## Environment

- Host vm4, Linux aarch64, Temurin JDK 21.0.12.1 (`/workspace/tools/jdk21`), Gradle 9.7.0 through the repo wrapper.
- Detached worktree `/workspace/worktrees/gate-pr5235-vm4` at `e2ad60e`, removed after closeout.
- Host build lock `/workspace/tools/.gradle-vm4.lock`. Every Gradle command ran with `--no-daemon`. Every test command used `--no-build-cache`.
- Disk before and after: `/` 39% used, 4.6G free; inodes 9% used.
- Driver: `/workspace/gates/logs/vm4/pr5235-upload-contentwriter-fix-round-1/run.sh`, with per-step `.log` and `.rc` files, `state.txt`, and XML copies in `<step>-xml/`.

## Runs

All runs were sequential under the lock.

| Step | Command | UTC start to end | Exit | Result (fresh XML: tests / skipped / failures / errors) |
|---|---|---|---|---|
| tidy | `./gradlew --no-daemon tidy` | 21:14:43 to 21:18:36 | 0 | BUILD SUCCESSFUL |
| tidy-clean-tree | `git status --porcelain` is empty | 21:18:36 to 21:18:39 | 0 | Clean |
| seed1 | `:solr:solrj:cleanTest :solr:solrj:test --tests ...TestConfigSetAdminRequest.testUpload` with `-Ptests.seed=747FA8C56DB2B91B`, the CI `tests.jvmargs`, `tests.useSecurityManager=true`, `tests.file.encoding=ISO-8859-1` | 21:18:40 to 21:23:07 | 0 | `testUpload`: 1 / 0 / 0 / 0 |
| seed2 | same task with `-Ptests.seed=4478EC01207498A8`, `-Ptests.timeoutSuite=600000!`, `tests.file.encoding=UTF-8` | 21:23:07 to 21:25:12 | 0 | `testUpload`: 1 / 0 / 0 / 0 |
| fullclass | `:solr:solrj:cleanTest :solr:solrj:test --tests ...TestConfigSetAdminRequest` with `-Ptests.seed=5235C0FFEE18351` | 21:25:12 to 21:27:10 | 0 | 5 / 0 / 0 / 0 (`testCreate`, `testDelete`, `testNoAction`, `testAdminRequestsChooseExplicitHttpMethods`, `testUpload`) |
| tidy-final | `./gradlew --no-daemon tidy` | 21:27:10 to 21:28:53 | 0 | BUILD SUCCESSFUL |
| tidy-final-clean | `git status --porcelain` is empty | 21:28:53 to 21:28:56 | 0 | Clean |

Counts come from the fresh XML in `seed1-xml/`, `seed2-xml/`, and `fullclass-xml/`. No retries were needed. No test was skipped. No code, format, or test file changed during the run.

## Gaps

- The Windows run is not done, so the Windows proof is still pending.
- Apache PR 5235 still has head `ccace897`. This receipt does not show that the patch is on that PR.
- The fix is covered only by the solrj test class in `:solr:solrj`. The gate does not run the wider Solr build.
