# PR 5225 follow-up branch: JDK 25 cross-host BATS diagnostics

Claim: `claims/pr-5225-jdk25-cross-host-phone-agent-2.md`. These are supplementary diagnostics on `nick-boss-tech/solr:solr-5225-bats-followup` at `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`. The live `gates/pr-5225-allowpaths-denial-round-1.md` still pins a different branch and SHA (`solr-pr-5225-allowpaths-tests-submit` at `3a32098d870da94d663f2948a1ba4adf32f1675d`), so this report is not a result for that gate and does not complete it.

## Runtime setup

- Host: Phone Agent 2 Sandbox, Linux x86_64, Ubuntu 24.04.4
- Build JDK: OpenJDK 21.0.12.1
- Solr runtime JDK: OpenJDK 25.0.4.1, `/usr/lib/jvm/java-25-openjdk-amd64`; Solr log fingerprints show `jvm 25.0.4.1+1-1-24.04.4-Ubuntu`.
- Each test task explicitly received `JAVA_HOME` and `SOLR_JAVA_HOME` for JDK 25, `SOLR_INCLUDE=/dev/null`, and an explicit parser setting. Gradle used its wrapper and the host build lock/inode guard.
- The worktree stayed at the pinned branch head; no source changes were made.

## Results

1. Commons CLI, `:solr:packaging:integrationTests --tests test_security_manager.bats --rerun`, `SOLR_PICOCLI=false`: Gradle ran the test at 2026-10-26T00:50:38Z. One test executed and failed at `test_security_manager.bats:58`, where `assert_file_contains` did not match the expected path-denial text in `solr.log`.
2. Picocli, `:solr:packaging:integrationTestsPicocli --rerun`, `SOLR_PICOCLI=true`, with a root-build-only init script narrowing the suite to `test/test_security_manager.bats`: one test executed and failed at the same line with the same mismatch at 2026-10-26T00:53:56Z.

In both runs, the response-body assertion found the expected wrapper error, `Could not backup all shards`. The Solr log also contains the correct path-specific denial. For example, its message says `Path /tmp/bats-run-r4MMCD/test/1/backup-dir-other/test-fail/COLL_NAME must be relative to SOLR_HOME, SOLR_DATA_HOME coreRootDirectory...` (the Picocli run has its own distinct temp path). The test currently searches with `must be relative to SOLR_HOME.*${backup_dir_other}`, which expects the backup path after the phrase; in the actual message it appears before the phrase. The test is therefore failing on the regex ordering, not because the path denial is missing.

## Evidence

- Commons CLI Gradle output: `/workspace/gates/logs/phone-agent-2/pr5225-jdk25/jdk25-commons-cli.log`
- Commons CLI BATS XML/output and full Solr logs: `/workspace/gates/logs/phone-agent-2/pr5225-jdk25/jdk25-commons-cli.artifacts/`
- Picocli Gradle output: `/workspace/gates/logs/phone-agent-2/pr5225-jdk25/jdk25-picocli.log`
- Picocli init script: `/workspace/gates/logs/phone-agent-2/pr5225-jdk25/picocli-jdk25.init.gradle`
- Picocli BATS XML/output and full Solr logs: `/workspace/gates/logs/phone-agent-2/pr5225-jdk25/jdk25-picocli.artifacts/`

## Limits and next step

Both JDK 25 parser diagnostics are red at the current head. No test fix was made, no synthetic mutation controls were run, and no PR/Jira action was taken. The JDK 21 Commons CLI and Picocli diagnostics and `:solr:packaging:check -x test` passed earlier on the same follow-up branch. Full gate status remains OPEN; proof against the gate-pinned branch and its remaining controls is still outstanding.