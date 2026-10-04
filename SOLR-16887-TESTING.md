# SOLR-16887 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access and no Docker. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-16887 - "The new 'Crash On Out Of Memory Error' capability breaks auto-restart on the docker container" (Shawn Heisey, 2023; his patch was attached but never committed). The old skip note was "maintainer's own ticket; real and unfixed on main, but should not be implemented uninvited". Reopened in audit round audit-1 (Tier 2 batch 1) as a hypothetical branch only.
- Branch: `solr-16887-submit` off `apache/solr` main `9b3a84b1c46`

## The bug, as understood
SOLR-8803 replaced the old shell-script OOM killer with the JVM flag `-XX:+CrashOnOutOfMemoryError` (plus `-XX:ErrorFile`). That flag makes the JVM abort and write a crash report; per the ticket this prevents a Docker restart policy from restarting Solr. Shawn's proposal: use `-XX:+ExitOnOutOfMemoryError`, which exits with a normal non-zero exit code (3) and prints `Terminating due to java.lang.OutOfMemoryError: ...` to the console, so both the cross-platform behavior and "the cause is always available" goals of SOLR-8803 are kept.

## What the branch changes
- `bin/solr`, `bin/solr.cmd`: `-XX:+ExitOnOutOfMemoryError` replaces `-XX:+CrashOnOutOfMemoryError` and the `-XX:ErrorFile=...jvm_crash_%p.log` option (no crash file is produced any more).
- `CoreContainerProvider`: the startup log line now looks for `-XX:+ExitOnOutOfMemoryError` and says the cause goes to the console (no crash-file path lookup); removed the unused `Optional` import.
- Ref guide: `taking-solr-to-production.adoc` Out-of-Memory Handling paragraph, and the sample JVM args list in `system-info-handler.adoc`.
- Test: `test_start_solr.bats` "solr exits on OutOfMemoryError instead of crashing" starts Solr and asserts `/solr/admin/info/system` shows the new flag and not the old one.

## Guesses to verify first
1. Houston Putman's comment (heap dumps should stay available): `-XX:+HeapDumpOnOutOfMemoryError` is added separately by `bin/solr` when `SOLR_HEAP_DUMP=true`, and Shawn confirmed a dump is still written together with the exit. Not re-verified here.
2. Docker: whether `docker run --restart=on-failure` (or `unless-stopped`) now restarts Solr after an OOME with `-e SOLR_HEAP=32m`. The ticket thread never got a clear answer; this is the main thing to test by hand.
3. `/solr/admin/info/system` returns JVM input arguments in plain text so `assert_output --partial` matches (the ref guide sample shows them under `jvm.jmx.commandLineArgs`).
4. Anything else that parses `jvm_crash_*.log` (monitoring docs, `solr.in.sh` samples) - a grep found none in the repo, but it was not exhaustive for `*.in.sh` / `*.in.cmd`.

## Verify (Gradle/Docker required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew iTest --tests test_start_solr.bats
docker run -e SOLR_HEAP=32m --restart=on-failure -d <image>   # watch it restart
```
Fail-before: revert `bin/solr`; the new BATS test should fail on the `refute_output` / `assert_output` lines.

## Not done
No JIRA comment, no PR.
