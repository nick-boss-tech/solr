# Pool assignment: Reproduce SOLR-14159 (TestCloudConsistency connection failures), round 1

Status: OPEN

- Slug: `pool-skip-repro-14159-round-1`.
- Capability tags: sweep, review.
- Intended host: any pool Linux host (see hosts/pool-linux.md). Claim under `claims/` before starting.
- Posted by: main agent coordinator, 2026-10-18, at the direction of Nick.

### Items

1. **Run the class.** Run `:solr:core:test --rerun --tests org.apache.solr.cloud.TestCloudConsistency` three times at the pinned head, one run at a time. Record each result and seed.
2. **Look for the signatures.** In each log, search for `Server refused connection`, `Timeout waiting for active collection`, and `404 Not Found` with `text/html`. Record the matching lines.
3. **Read the parent.** Check SOLR-13486 on JIRA (anonymous REST, read only). The ticket was moved from it. Record its current status and whether its description names the same race.

### Tickets

| Ticket | Test class | Gradle task | Note |
| --- | --- | --- | --- |
| SOLR-14159 | org.apache.solr.cloud.TestCloudConsistency | `:solr:core:test` | Erick Erickson reported 2 failures in about 1,000 runs on 2020-01-23 after commit 6b3e7feba1. Last comment 2020-04-14: no failures in four weeks. |

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
| Scope | decided: the three runs and the reading in the items. |
| Failure found | default: record the failing test method, the first assertion or exception, and the seed. Do not diagnose further. |

### Deliverable

`reports/2026-10-18-skip-repro/D2-14159.md`: run results, seeds, matching lines, and the SOLR-13486 status.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.

