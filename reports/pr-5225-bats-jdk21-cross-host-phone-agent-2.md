# PR 5225 JDK 21 BATS cross-host diagnostic

Claim: `claims/pr-5225-bats-jdk21-cross-host-phone-agent-2.md`. This is one diagnostic leg Nick requested to compare with vm2's reported teardown failure. It is not the full gate and does not replace vm2's assignment.

## Exact input and setup

- Fork branch: `nick-boss-tech/solr:solr-5225-bats-followup`
- Exact head: `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`, parent `07fdebdda54a24085086281da1cde701e122564d`
- Host: Phone Agent 2 Sandbox, Linux x86_64
- Build/runtime JDK: OpenJDK 21.0.12.1, `/usr/lib/jvm/java-21-openjdk-amd64`
- Gradle: wrapper Gradle 9.7.0
- BATS: 1.10.0
- Parser: Commons CLI, with `SOLR_PICOCLI=false`
- Preflight: guarded `tidy` completed successfully at the exact head; the detached worktree stayed clean.

## Run

Command: `:solr:packaging:integrationTests --tests test_security_manager.bats --rerun`

The test ran from 2026-10-26T00:10:40Z to 00:13:37Z. Gradle exited 0. Fresh BATS report XML records one test, zero failures, zero errors, and zero skips; BATS output is `ok 1 allowPaths - backup` in 30.770 seconds. The test source's teardown calls `SOLR_STOP_WAIT=1 solr stop --all`, and it completed successfully here with no leftover Solr process.

Logs and artifacts:
- Gradle output: `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/jdk21-commons-cli.log`
- Runner resource log: `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/jdk21-commons-cli.headroom.log`
- BATS output and report XML: `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/jdk21-commons-cli.artifacts/test-output.txt` and `report.xml`
- Complete Solr logs, including `solr.log`: `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/jdk21-commons-cli.artifacts/solr-home/logs/`

## Comparison and limits

The test passed here at the committed one-second stop wait. Vm2's report says teardown failed on Linux aarch64 at both one- and 30-second waits, including on unchanged main. The different outcome suggests an environment-specific issue, but does not establish its cause; architecture is one observed difference, not proven as causal.

Only the JDK 21 Commons CLI test-file leg was run. No picocli run, JDK 25 run, synthetic mutation control, packaging check, or full-gate receipt is included. The full PR 5225 gate remains incomplete. No product files were changed.

The runner recorded 213,424 free inodes before the test and 179,063 immediately afterward. Current headroom is about 21% free. No cleanup was performed.