# Pool assignment: Reproduce SOLR-5516 (ChaosMonkeyNothingIsSafeTest), round 1

Status: HANDED 2026-10-18T15:15Z (vm4, claims/pool-skip-repro-5516-round-1.md). Run at 498a09aea9 skipped the test (nightly group not enabled), so no reproduction ran. Deliverable reports/2026-10-18-skip-repro/D3-5516.md. Needs a main-side decision on the nightly flag before a rerun.

- Slug: `pool-skip-repro-5516-round-1`.
- Capability tags: sweep, review.
- Intended host: any pool Linux host (see hosts/pool-linux.md). Claim under `claims/` before starting.
- Posted by: main agent coordinator, 2026-10-18, at the direction of Nick.

### Items

1. **Run the class.** Run `:solr:core:test --rerun --tests org.apache.solr.cloud.ChaosMonkeyNothingIsSafeTest` once at the pinned head. It is long; allow the full run. Record the result and seed.
2. **Read the mitigation.** Read `solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java` around the `weAreReplacement` branch, where `Thread.sleep(2500)` waits before the replacement leader syncs. Record whether anything in the code confirms that in-flight updates from the old leader have finished, or whether the sleep is the only protection.
3. **Check the test.** In the run log, record any inconsistency assertion and the replica state it names.

### Tickets

| Ticket | Test class | Gradle task | Note |
| --- | --- | --- | --- |
| SOLR-5516 | org.apache.solr.cloud.ChaosMonkeyNothingIsSafeTest | `:solr:core:test` | Mark Miller (2013-12-06): the wait helps but is not enough on slow machines. |

### Decisions

| Decision | Answer or default |
| --- | --- |
| Head | decided: the fork origin/main sha 498a09aea9 at posting time. Record the sha you ran. Do not move it. |
| Build protocol | decided: one Gradle build per host, under the host lock in `hosts/pool-linux.md` (`flock -n`). Record `df -h /` and `df -i /` before and after each run in the claim. |
| Worktree | decided: one detached worktree under `/workspace/worktrees/` at the pinned head. Do not reuse another session worktree. Never run bare `git stash`. |
| JDK and Gradle home | decided: the JDK and GRADLE_USER_HOME recorded in your host file under `hosts/`. Caches under /workspace only. |
| Run cadence | decided: run each class once with `--rerun`. If it fails, run it once more with the failing seed (pass the seed printed in the failing log as `-Ptests.seed`, written out in full). Stop at two runs per ticket. |
| Outcome recording | decided: record the test name, pass or fail, the assertion or exception line, the seed, the log path, and the XML path for each run. Do not interpret beyond that in this assignment. |
| Class not on main | default: record "not on main" and move to the next ticket; no other action. |
| Disk | default: if root filesystem free space falls under 2 GB, stop the sweep, record it, and escalate to the main agent. |
| Code changes | decided: none. No edits to main code or tests. The only commit is the deliverable file. |
| Closing or commenting | decided: none. Nothing is closed, commented or posted to JIRA or GitHub. Escalate any ticket that looks ready to close. |
| Scope | decided: one run and the reading in the items. |
| Failure found | default: record the assertion and the replica state. Do not diagnose further. |

### Deliverable

`reports/2026-10-18-skip-repro/D3-5516.md`: run result, seed, the sleep reading, and any inconsistency record.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.
