# Multi-agent workflow: the assignment pool

Set up 2026-10-10 at Nick's direction. This branch is an open pool, not addressed mail. Any agent that polls this branch (the main agent on the first Linux VM, the agent on the second Linux VM, the review agent on Windows) may take any assignment it is capable of running. A claim is what assigns work; until an assignment is claimed it belongs to nobody, and once it is claimed it belongs to the claimant until it is done or the claim goes stale.

## Folders

- `assignments/` holds the pool. Every assignment file names its capability tags, its slices, its deliverables and its rules.
- `claims/` holds one claim file per assignment or per slice of a large assignment.
- `gates/` holds one file per gate job, written by whoever posts the job and updated by the host that runs it.
- `hosts/` holds one file per agent host: its capabilities and its latest heartbeat.
- `receipts/` holds one file per ticket with the verified gate state at the live tip. The host that runs a gate writes or refreshes the receipt itself as the finishing step of that gate, so a receipt never waits on a separate refresh.
- `reports/`, `pr-drafts/`, `material/` hold deliverables as before.

## Capability tags

An assignment lists the tags it needs, and an agent claims only work whose tags it holds. Tags in use:

- `review`: reading code and writing audit findings. Any host.
- `draft`: writing PR drafts from receipts. Any host.
- `implementation`: editing branch code and committing. Linux hosts.
- `gate`: running the full hardening gate (changelog parse, tidy, Error Prone compile, proof run, focused tests, module check). Linux hosts with the Gradle toolchain.
- `premise-run`, `settling-run`, `sweep`: focused test runs short of a full gate. Linux hosts.
- `bats`: shell integration test runs. Linux hosts.
- `selenium`: the Admin UI Selenium suites. The Windows host; a Linux host only if it has Chrome and says so in its host file.
- `windows-check`: checks that can only run on the Windows host.

The Windows host never claims `gate`, `implementation`, `premise-run`, `settling-run` or `sweep` work: builds stay off that machine (Nick's standing rule). It claims `review`, `draft`, `selenium` and `windows-check` work.

## Claims

1. Claim before work. A claim file at `claims/<assignment-slug>.md` (or `claims/<round>-<slice>.md` for one slice of a large round) records: the claimant host (matching a file in `hosts/`), the slice taken, the capability tags it uses, and a started timestamp in UTC.
2. First push wins. If a push race shows another claim for the same slice landed first, the later claimant stands down and picks other work.
3. Heartbeat. While a claim is active the holder appends a heartbeat line with a fresh UTC timestamp to the claim file at least once an hour and pushes it. Long silent work is indistinguishable from dead work.
4. Staleness. A claim with no heartbeat for 2 hours is stale. Any capable agent may take the slice over by appending a takeover line to the claim file (old holder, time, reason) and pushing. For a `gate` claim there is one more condition: the recorded gate log must have stopped growing, checked by the taking-over host's own reading of the log state recorded in `gates/<TICKET>.md`; a gate never runs on two hosts at once.
5. Done. The holder marks the claim DONE with the deliverable paths when the work lands. A claim that ends in failure or a blocker says so in the claim file, with the reason; it does not just go quiet.

## Gates

- A gate job file at `gates/<TICKET>.md` records: branch, head, module, focused test classes, seed, proof shape, the claiming host, the runner and log paths on that host, and a step checklist the runner updates as each step finishes. A host that dies mid-gate therefore loses at most one step, and the next host resumes from the checklist instead of from memory.
- One gate per host at a time (each host serializes its own Gradle builds behind its local lock). Two hosts may gate different branches in parallel; that is the point of the pool.
- On green, the gating host pushes the branch if the job says to, writes `receipts/<TICKET>.md`, marks the gate file DONE, and marks the claim DONE.
- A gate that executed tests and failed is never relaunched by another host on sight. The failure is recorded in the gate file and reported; the main agent investigates before any relaunch. A run that never executed tests (environment refusal, VM replacement) may be relaunched once by the claiming or taking-over host.

## What stays addressed, not pooled

- Decision slates and answers passes for Nick stay with the main agent: they need the full record and they end at Nick.
- PR openings stay with the main agent, each with Nick's explicit approval.
- Everything else (audits, drafts, implementation, gates, premise runs, settling runs, sweeps) is pool work.

## Standing limits that bind every host

- At most 6 to 7 subagents running at once in total, across all hosts combined. An assignment names its staffing; slices taken in parallel count toward the same total.
- Public text follows `pr-formula.md`: plain language, no internal process vocabulary, proof numbers only from receipts or gate files.
- No builds on the Windows host (above). No new Jira ticket unless a maintainer asks or Nick directs it.
- Commit identity is Nick Shanin; no other name in authors, committers or trailers; no em dashes in authored text.
