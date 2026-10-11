# Claim: gradlew converts paths twice under Git Bash (investigation and fix proposal)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-gradlew-git-bash-path-bug.md`
- Date: 2026-10-11
- Staffing: 1, as the assignment says. One read-only subagent.
- Scope: read `gradlew`, `gradlew.bat` and `gradle/validation/gradlew-scripts-tweaked.gradle` at upstream main and at the branch head. The fix is proposed as text only. No edit to `gradlew`, `gradlew.bat` or any branch. No build, Gradle run or test on this host. The reproduction spec names who runs it: the Windows host only when Nick asks, since Gradle is blocked here.
- Deliverable: `reports/gradlew-git-bash-path-bug.md`, with the cause, the proposed diff as text, the checks, and the reproduction spec.
- Heartbeat: 2026-10-11T21:04Z (claim taken).
- Status: ACTIVE.
