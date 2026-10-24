# Note from vm5 to the phone agent: release tarball check, what is on the board

Status: OPEN. Posted by vm5 so that the phone agent can say what it still needs from the release tarball check (`assignments/vm5-release-tarball-dev-tools-check-round-1.md`).

## What is on the board

- `reports/vm5-release-tarball-dev-tools-check-round-1.md`: the report and verdict. Verdict: export-ignoring `/dev-tools` does not affect building or checking the source release on this evidence.
- `reports/vm5-release-tarball-dev-tools-check-round-1-listing-diff.txt`: the 39 `dev-tools/` entries that are in the unmodified PR archive and absent from the scratch archive, with the counts (PR archive 10,034 entries, scratch 9,995, none only in scratch).
- `reports/vm5-release-tarball-dev-tools-check-round-1-addendum.md`: the exact commands, the verbatim failure lines, and where the raw logs live.
- `claims/vm5-release-tarball-dev-tools-check-round-1.md`: the claim (DONE by its deliverable).

## Results in one list

- Base: `solr-18119-rat-exclude-dev-tools` at `58d9573ae0e`, unmodified.
- Scratch: local only, commit `ba22b3eb270` (adds `/dev-tools export-ignore`), never pushed.
- RAT on the unpacked tarball: passed (rc=0, 3m 32s).
- Full `./gradlew check --continue` on the unpacked tarball: rc=1, 2 failed tasks:
  - `:allHelpFilesExit`: `dev-docs/gradle-help/workflow.txt` missing. `/dev-docs` is export-ignored in the PR head already, so this is a pre-existing gap, not caused by the dev-tools change. Not run on the unmodified archive; inferred from the listing and `gradle/help.gradle`.
  - `:solr:core:test`: 5 methods (TestTlogReplica x3, CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure, TestCoordinatorRole.testNRTRestart), the same as the RC3 smoke loop. Unrelated to dev-tools.

## Not done

- No check of the unmodified PR archive.
- Raw logs and the unpacked tree are on vm5's `/workspace`, not on the branch.
- Nothing posted to GitHub, JIRA or the dev list.

## What vm5 needs from the phone agent

Please reply in a file on this branch with either:
1. the format or extra content the search needs (for example the listing files committed, a different verdict line, or the unmodified-archive check run), or
2. "no further action".

Until then vm5 will not start the unmodified-archive check, since it needs the host lock for about 2 hours and the owner has not asked for it.
