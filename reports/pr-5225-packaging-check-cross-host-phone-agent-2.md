# PR 5225 JDK 21 packaging check cross-host diagnostic

Claim: `claims/pr-5225-packaging-check-cross-host-phone-agent-2.md`. This is a supplementary leg Nick requested. It does not replace vm2's live assignment or complete the PR 5225 gate.

## Exact input and setup

- Fork branch: `nick-boss-tech/solr:solr-5225-bats-followup`
- Exact head: `096da6567e3db6d2bd141e3d2bf556ad1b3d2509`, parent `07fdebdda54a24085086281da1cde701e122564d`
- Host: Phone Agent 2 Sandbox, Linux x86_64
- Build JDK: OpenJDK 21.0.12.1, `/usr/lib/jvm/java-21-openjdk-amd64`
- Gradle: wrapper Gradle 9.7.0
- Preflight: the detached worktree was at the exact head, tracked tree clean, and the host lock was available. The run used the `sweep-run.sh` lock and inode guard.

## Run and result

Command: `:solr:packaging:check -x test`

The task completed at exact head `096da6567e3db6d2bd141e3d2bf556ad1b3d2509` with Gradle exit code 0. It reported `BUILD SUCCESSFUL in 1m 5s` and 54 actionable tasks (29 executed, 25 up-to-date). No tests were included; the `errorProneSkipped` task was reported in the log. The tracked worktree remained clean.

Logs:
- Gradle output: `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/jdk21-packaging-check.log`
- Runner/resource output: `/workspace/gates/logs/phone-agent-2/pr5225-crosshost-jdk21/jdk21-packaging-check.headroom.log`

At the end of the run, the filesystem had 178,606 free inodes (about 21.3%). No cleanup was performed.

## Limits

This is only the packaging check at JDK 21. It does not supply JDK 25 runtime legs, either mutation control, the full JDK 21/25 parser matrix, or a full-gate receipt. No source files or product branches were changed. The full PR 5225 gate remains incomplete.