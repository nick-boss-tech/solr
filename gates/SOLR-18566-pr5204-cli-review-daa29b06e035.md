# Gate job: SOLR-18566 PR 5204 CLI review, final head daa29b06e035

Status: OPEN (immutable job; completion is derived from `receipts/SOLR-18566.md` when a gating host writes it).

- Ticket: SOLR-18566 (PR https://github.com/apache/solr/pull/5204, reviewed head `7eb288a6f5777770d4472fc2c550b66e0d1cec12`).
- Branch: `nick-boss-tech/solr:solr-18566-pr5204-cli-review`.
- Final head: `daa29b06e035b57fc5098180113f764dff9cf57d` (full SHA, read back from the remote).
- Module: `:solr:packaging` (shell and BATS tests only; no Java compile proof is requested because the delta touches only BATS files).
- Assignment: `assignments/pr-5204-picocli-review-implementation-and-gate-vm4-round-1.md`, Phase G.
- Report of the implementation and original-head control: `reports/pr-5204-cli-review-implementation-vm4-round-1.md`.
- Seed: not applicable. BATS has no JUnit randomized-tests seed; record that rather than invent one.
- Runner: `/workspace/tools/.gradle-vm4.lock` for every Gradle build. `JAVA_HOME=/workspace/tools/jdk21`. Gradle 9.7.0 through the repo wrapper. `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib`. `lsof` from `/workspace/tools/lsof-env/bin` first on PATH. Child processes wrapped in `python3 /workspace/tools/reap-run.py` so Solr zombies are reaped. `-PdisableJsClient=true` is passed on every run (the js-client npm build fails at this head; see the report).
- Worktree: a fresh detached worktree at `daa29b06e035b57fc5098180113f764dff9cf57d` for the final gate, separate from the probe worktree at the reviewed head.
- Logs: `/workspace/gates/logs/vm4/pr-5204/<leg>/`, one directory per leg, with `run.log`, the JUnit `junit/report.xml`, and the BATS `test-output.txt`. Each leg clears the build output directory first and copies it out before the next leg starts.

## Legs

1. Patched false-mode focused leg. Command: `env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests -PdisableJsClient=true --tests test_help.bats --tests test_post.bats --rerun --no-daemon`. Expected: all pass; `restart help flag prints help` executes and is not skipped; `explicit SOLR_PICOCLI=false selects the legacy help wording` passes. Record the child environment and the effective parser.
2. Default legacy full suite. Command: `env -u SOLR_PICOCLI ./gradlew :solr:packaging:integrationTests -PdisableJsClient=true --rerun --no-daemon` (variable unset in the process; no include-file override). Expected: all selected non-nightly tests pass. State every skip and why. Nightly-tagged tests are excluded by the build's default filter and are not run.
3. Picocli full suite. Command: `./gradlew :solr:packaging:integrationTestsPicocli -PdisableJsClient=true --rerun --no-daemon`. Expected: all selected non-nightly tests pass, with the intentional restart-help skip accounted for.
4. Formatting and module checks. Detached tidy preflight, then `./gradlew tidy -PdisableJsClient=true`, `./gradlew :solr:packaging:check -x test -PdisableJsClient=true`, and `git diff --check` of the delta from the reviewed head.

Pre-flight before each leg: `df -h /` and `df -i /` recorded in `hosts/vm4.md`. Stop and record if free space on `/` is under 2 GB or free inodes are 15% or less.

## Step checklist

- [ ] Fresh worktree at the final head; `git rev-parse HEAD` equals `daa29b06e035b57fc5098180113f764dff9cf57d` at the start of each leg.
- [ ] Leg 1 run, log and JUnit copied out, counts recorded.
- [ ] Leg 2 run, log and JUnit copied out, skips listed.
- [ ] Leg 3 run, log and JUnit copied out, skips listed.
- [ ] Leg 4 run, log copied out.
- [ ] Report `reports/pr-5204-cli-review-gate-vm4-round-1.md` with the incremental compare URL, original and final full SHAs, final tree status, remote read-back, and each leg's command, child environment, UTC start and end, exit code, counts, and log paths.
- [ ] `receipts/SOLR-18566.md` only if every leg passes. A failed or incomplete leg gets a failure or incomplete report and no green receipt.
- [ ] Remove the scratch worktree after closeout is pushed. Keep the logs.
