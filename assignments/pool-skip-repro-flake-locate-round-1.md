# Pool assignment: Locate and reproduce flake tickets without a named class, round 1

Status: OPEN

- Slug: `pool-skip-repro-flake-locate-round-1`.
- Capability tags: sweep.
- Intended host: vm3 (after its open claims are done). Claim under `claims/` before starting.
- Posted by: vm5 (main agent coordinator), 2026-10-18, at the direction of Nick.

### Items

1. **Run the tickets in the table.** For each row, run the Gradle task with the class from that row, following the Decisions table. Start from the pinned head. Output: one line per ticket in the deliverable.
2. **Record.** Write each ticket result into the deliverable file as it finishes, so a stop keeps partial results.

### Tickets

| Ticket | Test class (main) | Gradle task | Skip note (from queue) |
| --- | --- | --- | --- |
| SOLR-2715 | locate from the JIRA description (anonymous REST, read only) | per class | test flake |
| SOLR-2720 | locate from the JIRA description (anonymous REST, read only) | per class | test flake |
| SOLR-3944 | locate from the JIRA description (anonymous REST, read only) | per class | OOM with huge docs in Solr Cell, environment |
| SOLR-5948 | locate from the JIRA description (anonymous REST, read only) | per class | test flake (LUCENE-5574) |
| SOLR-6593 | locate from the JIRA description (anonymous REST, read only) | per class | test flake |
| SOLR-6914 | locate from the JIRA description (anonymous REST, read only) | per class | test timeout flake |
| SOLR-7646 | locate from the JIRA description (anonymous REST, read only) | per class | OOM wrapping, general, no concrete fix |
| SOLR-9182 | locate from the JIRA description (anonymous REST, read only) | per class | test infra OOM investigation, resolved via SOLR-9604 |
| SOLR-9426 | locate from the JIRA description (anonymous REST, read only) | per class | test flake |
| SOLR-11168 | locate from the JIRA description (anonymous REST, read only) | per class | test flake (ConcurrentUpdateSolrClientTest timing), no repro |
| SOLR-11193 | locate from the JIRA description (anonymous REST, read only) | per class | per session 2 handoff triage: flake, AwaitsFix on old branch, obsolete (Trie/EnumField removed), or design-level |
| SOLR-11208 | locate from the JIRA description (anonymous REST, read only) | per class | per session 2 handoff triage: flake, AwaitsFix on old branch, obsolete (Trie/EnumField removed), or design-level |
| SOLR-11214 | locate from the JIRA description (anonymous REST, read only) | per class | per session 2 handoff triage: flake, AwaitsFix on old branch, obsolete (Trie/EnumField removed), or design-level |
| SOLR-11259 | locate from the JIRA description (anonymous REST, read only) | per class | per session 2 handoff triage: flake, AwaitsFix on old branch, obsolete (Trie/EnumField removed), or design-level |
| SOLR-12220 | locate from the JIRA description (anonymous REST, read only) | per class | Windows-only test cleanup flake (ant era) |
| SOLR-12369 | locate from the JIRA description (anonymous REST, read only) | per class | beasting flake in ZkController.waitForShardId (Dat), no deterministic repro; not a user-visible bug on main |
| SOLR-12748 | locate from the JIRA description (anonymous REST, read only) | per class | stale 2018 OOM incident, never reproduced; 2018 code path (remoteQuery) gone from main; thread-pileup on hung peer is... |
| SOLR-18007 | locate from the JIRA description (anonymous REST, read only) | per class | SKIP: maintainer (Smiley) investigation ticket, no repro, no comments, no fix direction; lookup-reload flake needs bi... |

### Decisions

| Decision | Answer or default |
| --- | --- |
| Head | decided: the fork origin/main sha 498a09aea9 at posting time. Record the sha you ran. Do not move it. |
| Build protocol | decided: one Gradle build at a time under `flock /workspace/tools/.vm3-gradle.lock`. Record `df -h /` and `df -i /` before and after each run in the claim. |
| Worktree | decided: one detached worktree under `/workspace/worktrees/` at the pinned head. Do not reuse another session worktree. Never run bare `git stash`. |
| JDK and Gradle home | decided: the JDK and GRADLE_USER_HOME recorded in your host file under `hosts/`. Caches under /workspace only. |
| Run cadence | decided: run each class once with `--rerun`. If it fails, run it once more with the failing seed (pass the seed printed in the failing log as `-Ptests.seed`, written out in full). Stop at two runs per ticket. |
| Outcome recording | decided: record the test name, pass or fail, the assertion or exception line, the seed, the log path, and the XML path for each run. Do not interpret beyond that in this assignment. |
| Class not on main | default: record "not on main" and move to the next ticket; no other action. |
| Disk | default: if root filesystem free space falls under 2 GB, stop the sweep, record it, and escalate to the main agent. |
| Code changes | decided: none. No edits to main code or tests. The only commit is the deliverable file. |
| Closing or commenting | decided: none. Nothing is closed, commented or posted to JIRA or GitHub. Escalate any ticket that looks ready to close. |
| Locate | default: if the JIRA description names no test class, record "no test named" and escalate to the main agent. Do not pick a test by guessing. |
| Scope | decided: only the rows in this table. |

### Deliverable

`reports/2026-10-18-skip-repro/A2-flake-locate-round-1.md`: one row per ticket with the class found, run results and seeds.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.

