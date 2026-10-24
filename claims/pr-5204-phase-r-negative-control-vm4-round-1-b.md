# Claim: pr-5204-phase-r-negative-control-vm4-round-1-b

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: LIVE.
- Supersedes: `claims/pr-5204-phase-r-negative-control-vm4-round-1.md` (same head, same assignment). That claim named the command without the js-client switch. Its only attempt stopped before any BATS test ran, so it is not a test result.
- Why the first attempt stopped: Gradle failed at `:solr:webapp:js-client:jsClientDownloadDeps` after 5m16s. npm reported `Cannot find package '@babel/plugin-syntax-dynamic-import'` while building the OpenAPI JavaScript client. Nothing in `solr/webapp/js-client` at the reviewed head declares that package, so this is a build failure at the reviewed head itself, unrelated to the picocli selector. Log: `/workspace/gates/logs/vm4/pr-5204/phase-r-negative-control/run.log` (kept, not overwritten; the next run uses a new log file).
- Changed input: adds `-PdisableJsClient=true`, the switch the build documents in `solr/webapp/js-client/build.gradle.kts` and `settings.gradle`. It removes only the OpenAPI JavaScript client bundle from the webapp. The BATS CLI suites in `solr/packaging/test` do not use that bundle. The Admin UI tests are not part of this assignment. The head and tree are unchanged.
- Input SHA: `7eb288a6f5777770d4472fc2c550b66e0d1cec12`, unmodified. Worktree `/workspace/worktrees/pr-5204-r`, detached.
- Tags used: gate, bats. Decision ladder: rung 2 (probe), rung 1 for the build switch (documented in the repo).
- Command (under `/workspace/tools/.gradle-vm4.lock`): `env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests -PdisableJsClient=true --tests test_help.bats --rerun --no-daemon`. `--rerun` forces this BATS task only. The assignment's shape `--rerun-tasks` would rebuild the whole graph; if `--rerun` does not force execution, the report says so and the run repeats with `--rerun-tasks`.
- Expected: assertion failures from the picocli wording in `test_help.bats`, attributable to the selector, not to compilation, startup, tooling, credentials, or ports. Also recorded: the restart-help test is skipped under false.
- Log path: `/workspace/gates/logs/vm4/pr-5204/phase-r-negative-control-b/`. JUnit XML is copied out of the worktree build directory before any later run.
- Started: 2026-10-24T21:01:00Z (UTC), approximate to the minute; the log's `start_utc` line is authoritative.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 2. Idempotency key: pr-5204-phase-r-negative-control-vm4-round-1-b@2026-10-24T21:01:00Z.
- Constraints: no git push in this claim. No public PR or JIRA comment. Disk guard: stop and record if `/` free space drops below 2 GB or free inodes fall to 15% or less.

Heartbeat: 2026-10-24T21:01:00Z
