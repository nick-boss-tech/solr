# PR 5225 BATS follow-up: JDK 21 Picocli continuation

This add-on records only the single additional JDK 21 Picocli leg run under `claims/pr-5225-bats-followup-round-1-jdk21-picocli-phone-agent-2.md`. It does not complete the gate.

## Candidate and result

- Fork branch: `nick-boss-tech/solr:solr-5225-bats-followup`.
- Exact head: `35378004f1797c5b9ae3ba68d2503e4edb3840e8`; the remote branch remained at that SHA.
- Clean detached worktree: `/workspace/tmp/pr5225-jdk21-picocli`; no tracked source changes.
- Gradle JDK: OpenJDK 21.0.12.1 at `/usr/lib/jvm/java-21-openjdk-amd64`.
- Solr runtime: the fresh console log reports `jvm 21.0.12.1+1-1-24.04.4-Ubuntu`.
- Parser/runtime settings: `SOLR_PICOCLI=true`, `SOLR_INCLUDE=/dev/null`, and `SOLR_JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64`.
- Command: `:solr:packaging:integrationTestsPicocli --rerun` with the root-build-only init script `jdk21-picocli.init.gradle` setting `testFiles = ['test/test_security_manager.bats']`.
- Result: Gradle exited 0, `BUILD SUCCESSFUL`; BATS ran exactly one `allowPaths - backup` test, 1 passed, 0 failures, 0 errors, 0 skipped, in 27.325 seconds. Its fresh Solr log contains the expected `AccessControlException: access denied` for `/tmp/bats-run-hH42b3/test/1/backup-dir-other`.

The wrapper log is `gates/logs/phone-agent-2/pr5225-followup-gate/jdk21-picocli.log`; headroom is recorded in `jdk21-picocli.headroom.log`. The preserved fresh BATS XML, transcript, and complete Solr log directory are under `gates/logs/phone-agent-2/pr5225-followup-gate/jdk21-picocli.artifacts/`.

## Resource limit and remaining work

The filesystem had 197,143 free inodes immediately before the run and 166,535 immediately afterward (19.88%). I stopped after this one additional leg, as Nick requested. After copying the fresh BATS evidence, 166,516 inodes remained free. The two prior clean test worktrees were removed only with Nick's approval; no logs, caches, or other paths were removed.

Still not run at the pinned final head: `:solr:packaging:check -x test` and all three isolated failure controls in the gate record. The JDK 21 Commons CLI pass and JDK 25 Commons CLI/Picocli passes are recorded in their existing reports. The gate remains OPEN and INCOMPLETE; no completion receipt is warranted. No candidate source, PR, Jira item, or upstream branch was changed.