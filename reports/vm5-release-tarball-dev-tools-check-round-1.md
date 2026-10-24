# Release tarball check for the dev-tools export-ignore question (PR #5222, SOLR-18119)

Verdict: export-ignoring /dev-tools does not affect building or checking the source release on this evidence: RAT passed from the unpacked tarball, and the two build failures are a help-file check that reads dev-docs (already export-ignored in the PR head) and five solr:core tests that also fail in the RC3 smoke loop, with no dev-tools dependency.

## Setup

- Base: fork branch `solr-18119-rat-exclude-dev-tools` at `58d9573ae0e` (main plus the RAT exclusion from PR #5222). Not modified.
- Scratch: local-only branch `scratch-dev-tools-export-ignore` at `ba22b3eb270`, one commit adding `/dev-tools export-ignore` beside `/dev-docs` in `.gitattributes`. Not pushed.
- Archives: `/workspace/smoke-rc3/tarball/solr-scratch-noDevTools.tar` (155,187,200 bytes) and `/workspace/smoke-rc3/tarball/solr-pr-unmodified.tar` (155,791,360 bytes), from `git archive`.
- Unpacked tree: `/workspace/smoke-rc3/unpack-scratch`, no `.git`, size 1.4G. Built with `./gradlew` from the tarball, `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1`, `GRADLE_USER_HOME=/workspace/gradle-home`, under `flock /workspace/tools/.gradle-vm5.lock`.

## Archive listings

- Unmodified PR archive: 10,034 entries. Scratch archive: 9,995 entries.
- `comm` of the two sorted listings: the only entries present in the PR archive and absent from the scratch archive are 39 entries under `dev-tools/`. Nothing else differs.
- The unpacked tree has no `.git`, no `dev-tools`, and no `dev-docs`.

## RAT

- `./gradlew rat` from the unpacked tree: BUILD SUCCESSFUL in 3m 32s, rc=0. Log: `/workspace/smoke-rc3/tarball/../tarball-logs/rat.log`.
- The full check ran 36 RAT tasks, all passing. The only `dev-tools` reference in the build is `gradle/validation/rat-sources.gradle` lines 122 and 181, which exclude `dev-tools` from RAT. That exclusion is the PR's own change, and it does not need the directory to exist.

## Full check

- `./gradlew check --continue` from the unpacked tree: BUILD FAILED in 1h 56m 36s, rc=1. 567 actionable tasks, 527 executed, 40 up-to-date. Log: `/workspace/smoke-rc3/tarball-logs/check.log`.
- Failed task 1: `:allHelpFilesExit`. "Help file missing: dev-docs/gradle-help/workflow.txt". `gradle/help.gradle` lines 22 to 26 read help files from `dev-docs/gradle-help/`. `/dev-docs` is export-ignored in the PR head's `.gitattributes`, so the unmodified PR archive also lacks these files. This failure does not involve dev-tools. It is a pre-existing gap between the help check and the tarball, and the unmodified archive would fail the same way. Not run on the unmodified archive; this is from the archive listing and the script.
- Failed task 2: `:solr:core:test`. 6,113 tests completed, 5 failed, 271 skipped: `TestTlogReplica.testKillLeader`, `TestTlogReplica.testRecovery`, `TestTlogReplica.testOnlyLeaderIndexes`, `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure`, `TestCoordinatorRole.testNRTRestart`. These are the same five methods that failed in the RC3 smoke loop (see `reports/rc3-smoke-loop-round-1.md`). They are unrelated to dev-tools: no test references dev-tools, and the failures are timeouts and assertions about cluster state.

## Classification (step 4)

- `:allHelpFilesExit`: unrelated to dev-tools; a dev-docs packaging gap that the unmodified archive also has.
- `:solr:core:test` (5 methods): unrelated to dev-tools; the same methods fail in the RC3 loop on the checkout.

## Limits

- The unmodified PR archive was not built or checked; its behaviour on the two failures is inferred from the listing and the scripts.
- Single host: aarch64, 4 CPUs, 7 GB RAM. Failures may be host-specific.
- Scratch and unpacked trees are left in place for the owner to inspect. They can be deleted after the owner reads this report.
