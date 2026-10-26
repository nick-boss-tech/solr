# PR 5225 BATS follow-up gate, round 1 — evidence report

**Verdict: INCOMPLETE; gate remains OPEN.** The JDK 21 Commons CLI leg passed at the pinned candidate SHA. I stopped before the 15% inode floor after that build consumed about 30,600 free inodes; no files or caches were deleted. No completion receipt is published.

## Candidate and completed evidence

The candidate is `nick-boss-tech/solr:solr-5225-bats-followup` at `35378004f1797c5b9ae3ba68d2503e4edb3840e8`. The isolated worktree `/workspace/tmp/pr5225-jdk21` remained at that exact SHA with no tracked source changes; `git diff --check` passed.

The JDK 21 Commons CLI command was:

```sh
env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 SOLR_JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 SOLR_INCLUDE=/dev/null SOLR_PICOCLI=false /workspace/solr-coordination-ui-followup/pool/bin/sweep-run.sh /workspace/tmp/pr5225-jdk21 /workspace/gates/logs/phone-agent-2/pr5225-followup-gate jdk21-commons-cli :solr:packaging:integrationTests --tests test_security_manager.bats --rerun
```

Gradle used OpenJDK 21.0.12.1. Solr’s fresh `solr.log` identifies runtime `21.0.12.1+1-1-24.04.4-Ubuntu`. With `SOLR_PICOCLI=false` and `SOLR_INCLUDE=/dev/null`, BATS executed exactly one `allowPaths - backup` test: 1 passed, 0 failures, 0 errors, 0 skipped, in 35.073 seconds. The complete Solr log contains the expected `AccessControlException: access denied` for the out-of-allowlist backup path.

The run log and headroom record are `/workspace/gates/logs/phone-agent-2/pr5225-followup-gate/jdk21-commons-cli.log` and `jdk21-commons-cli.headroom.log`. Fresh XML, BATS transcript, and the complete Solr log directory are preserved in `/workspace/gates/logs/phone-agent-2/pr5225-followup-gate/jdk21-commons-cli.artifacts/`. The exact command record and JDK 21 Picocli filter are also retained in `run-commands.txt` and `jdk21-picocli.init.gradle` in that directory.

The active gate record accepts the prior exact-head JDK 25 Commons CLI and Picocli legs as 1-test passes; their detailed report and artifacts are listed in `reports/pr-5225-jdk25-regex-fix-final-head-follow-up.md` and `/workspace/gates/logs/phone-agent-2/pr5225-jdk25-regex-fix/`.

## Why the run stopped

Immediately before the first build, the filesystem had 162,668 free inodes. The sweep runner recorded 132,094 free at build end; the direct check immediately afterward measured 132,083 free out of 837,408 (15.7728%). At 2026-10-26T01:39:25Z, 132,072 remained (15.7715%), just 6,461 above the 15% stop cutoff of 125,611. The required further Gradle legs and controls could cross the floor, so none were launched. There was no cache cleanup or file deletion.

## Remaining work and disposition

Not run in this attempt: the exact-head JDK 21 Picocli leg; the final `tidy` and `:solr:packaging:check -x test` checks (the read-only `git diff --check` did pass); and all three isolated discriminating controls. The gate’s earlier exact-head tidy preflight remains recorded, but it does not replace the required final checks. No mutation was made to candidate source, and the separate `gates/pr-5225-allowpaths-denial-round-1.md` gate was not touched.

Because the remaining criteria are unproven, the gate stays OPEN and no completion receipt is written. Resumption needs safe inode headroom under the no-deletion restriction, such as another capable host or explicit owner-approved cleanup. No PR, Jira, or upstream write was made.
