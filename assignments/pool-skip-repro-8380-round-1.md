# Pool assignment: Reproduce SOLR-8380 (CollectionsAPIDistributedZkTest 500 on proxied request), round 1

Status: OPEN

- Slug: `pool-skip-repro-8380-round-1`.
- Capability tags: sweep, review.
- Intended host: vm2 (first in its queue). Claim under `claims/` before starting.
- Posted by: vm5 (main agent coordinator), 2026-10-18, at the direction of Nick.

### Items

1. **Run the class.** Run `:solr:core:test --rerun --tests org.apache.solr.cloud.api.collections.CollectionsAPIDistributedZkTest` twice at the pinned head. Record each result and seed.
2. **Look for the signature.** In each log, search for `Response header too large`, `BufferOverflowException` and `Error trying to proxy request`. The ticket log shows Jetty `HttpGenerator.generateHeaders` failing. Record the matching lines with their line numbers.
3. **Read the proxy code.** Read `solr/core/src/java/org/apache/solr/servlet/HttpSolrProxy.java`, the `copyResponseHeaders` method. It copies every non-hop-by-hop response header from the remote node onto the local response. Record which headers a failing response carries, if the log shows them.
4. **Read the header limit.** Read `solr/server/etc/jetty.xml` for `requestHeaderSize` and `solr.jetty.request.header.size`. Record the default and whether a response-header limit is set anywhere. Do not change the file.

### Tickets

| Ticket | Test class | Gradle task | Note |
| --- | --- | --- | --- |
| SOLR-8380 | org.apache.solr.cloud.api.collections.CollectionsAPIDistributedZkTest | `:solr:core:test` | Ticket: fails about 1 in 3 runs on Jenkins (2015); did not reproduce locally in 2016. |

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
| Scope | decided: the two runs and the reading in the items. No other classes. |
| Status of the 500 | default: if no run fails, record "no failure in 2 runs" and stop. Do not add runs. |

### Deliverable

`reports/2026-10-18-skip-repro/D1-8380.md`: run results, seeds, matching log lines with line numbers, and what the reading shows.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.

