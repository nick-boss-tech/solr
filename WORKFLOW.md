# Multi-agent workflow: the assignment pool

> Board note (2026-10-17): this board is `coordination`. The previous board, `pr-prepare`, is archived at `archive/pr-prepare-2026-10-17` (54ce894ed8). Historical claims, gates and receipts that name `pr-prepare` are in that archive.

Agents who post or claim assignments and gate jobs: read [`pool/START-HERE.md`](pool/START-HERE.md) first. It gives the order of operations and links the templates in `pool/templates/`. This file holds the rules that START-HERE applies.

Set up 2026-10-10 at Nick's direction. This branch is an open pool, not addressed mail. Any agent that polls this branch may take any assignment it is capable of running. There is no coordinating agent: the pool hosts coordinate and troubleshoot among themselves through claims, gate files and reports on this branch, and Nick answers only questions that are genuinely ambiguous. A claim is what assigns work; until an assignment is claimed it belongs to nobody, and once it is claimed it belongs to the claimant until it is done or the claim goes stale.

## Folders

- `assignments/` holds the pool. Every assignment file names its capability tags, its slices, its deliverables and its rules.
- `claims/` holds one claim file per assignment or per slice of a large assignment.
- `gates/` holds one file per gate job, written by whoever posts the job and updated by the host that runs it. Use `gates/<TICKET>.md` for a single gate and `gates/<TICKET>-phase-<slug>.md` when separate phase heads for one ticket need independent gates.
- `hosts/` holds one file per agent host: its capabilities and its latest heartbeat.
- `receipts/` holds one file per ticket with the verified gate state at the live tip. The host that runs a gate writes or refreshes the receipt itself as the finishing step of that gate, so a receipt never waits on a separate refresh.
- `reports/`, `pr-drafts/`, `material/` hold deliverables as before.

## Capability tags

An assignment lists the tags it needs, and an agent claims only work whose tags it holds. Tags in use:

- `review`: reading code and writing audit findings. Any host.
- `draft`: writing PR drafts from receipts. Any host.
- `records`: reading structured coordination records and producing bounded CSV or record deliverables, without Jira or GitHub writes or source-code changes. A host may claim `records` assignments only when its host file lists this tag.
- `implementation`: editing branch code and committing. Linux hosts.
- `gate`: running the full hardening gate (changelog parse, tidy, Error Prone compile, proof run, focused tests, module check). Linux hosts with the Gradle toolchain.
- `premise-run`, `settling-run`, `sweep`: focused test runs short of a full gate. Linux hosts.
- `bats`: shell integration test runs. Linux hosts.
- `selenium`: the Admin UI Selenium suites. The Windows host; a Linux host only if it has Chrome and says so in its host file.
- `windows-check`: checks that can only run on the Windows host.

The Windows host never claims `gate`, `implementation`, `premise-run`, `settling-run` or `sweep` work: builds stay off that machine (Nick's standing rule). It claims `review`, `draft`, `records`, `selenium` and `windows-check` work.

## Claims

1. Claim before work. A claim file at `claims/<assignment-slug>.md` (or `claims/<round>-<slice>.md` for one slice of a large round) records: the claimant host (matching a file in `hosts/`), the slice taken, the capability tags it uses, and a started timestamp in UTC.
2. First push wins. If a push race shows another claim for the same slice landed first, the later claimant stands down and picks other work.
3. Heartbeat. While a claim is active the holder appends a heartbeat line with a fresh UTC timestamp to the claim file at least once an hour and pushes it. Long silent work is indistinguishable from dead work.
4. Staleness. A claim with no heartbeat for 2 hours is stale. Any capable agent may take the slice over by appending a takeover line to the claim file (old holder, time, reason) and pushing. For a `gate` claim there is one more condition: the recorded gate log must have stopped growing, checked by the taking-over host's own reading of the log state recorded in `gates/<TICKET>.md`; a gate never runs on two hosts at once.
5. Done. The holder marks the claim DONE with the deliverable paths when the work lands, in the same push sequence as the deliverable itself; an unmarked completed claim reads as open work to every other host and risks duplicated effort. A claim that ends in failure or a blocker says so in the claim file, with the reason; it does not just go quiet.
6. Missed marks. If a deliverable has landed but its claim was never marked, any agent that verifies the deliverable on this branch may append the DONE mark, stating that it marked on verification and naming the deliverable. The mark is a record correction, not a claim transfer.
7. Terminal states. A claim marked DONE, or marked WON'T DO under `pool/advisory-wont-do.md`, is terminal: its status lines are never edited afterwards by anyone. Superseding a terminal claim happens by posting a new claim that names the old one in its Supersedes line. A late completion, heartbeat, or correction arriving for a terminal claim goes into a new record, not into the closed one.
8. Takeovers resume, they do not restart. A takeover under rule 4 increments the claim's Attempts count, keeps the idempotency key (item at head or slice), and reuses every completed step already recorded in the claim, the gate checklist, or the deliverable. Redoing a recorded step needs a stated reason in the takeover line.
9. Take it to the end. A claimant carries the claim to DONE, or to an ENDED line with the reason the job cannot finish on this host, without stopping to ask permission. A decision inside the claim follows `material/decision-ladder.md`: record the rung used in the claim and continue. The owner is asked only at rung 5, and opening a PR stays owner-approved.

## Posting a file to this branch

Most records on this branch are one file added or edited: a claim, a report, a receipt section, an assignment, a status line. Post those with `pool/bin/post-file.sh`, which writes the file through the GitHub contents API as a server-side commit on the branch's current tip:

    pool/bin/post-file.sh <branch-path> <local-file> <commit-message> [--branch <name>] [--base-sha <blob-sha>]

There is no local tree to fall behind and nothing to rebase when another host lands first; the commit is made on whatever the tip is at that moment. Rules:

- For an edit to an existing file, pass `--base-sha` with the git blob SHA your edit is based on. If the file has moved since, the post is refused rather than overwriting the newer edit: re-read the file, re-apply your change, and post again. That retry is seconds of work, not a rebase.
- Claims keep the first-post-wins rule: the first claim file to land on the branch holds the slice, however it got there.
- A change that must land atomically across several files (a deliverable plus its claim and status marks) still goes through a normal git push with fetch and rebase first. Never force-push this branch.
- The helper reads the credential from `~/.git-credentials` and never prints it. Any host without the helper can make the same contents-API call directly; the route is the rule, not the script.

## Gates

- A gate job file at `gates/<GATE_ID>.md` records: ticket, branch, head, module, focused test classes, seed, proof shape, the claiming host, the runner and log paths on that host, and a step checklist the runner updates as each step finishes. A host that dies mid-gate therefore loses at most one step, and the next host resumes from the checklist instead of from memory.
- When two phase heads for one ticket have separate gate jobs, each has its own gate ID and claim. Both results are recorded as separate top sections in the ticket receipt at `receipts/<TICKET>.md`.
- One gate per host at a time (each host serializes its own Gradle builds behind its local lock). Two hosts may gate different branches in parallel; that is the point of the pool.
- On green, the gating host pushes the branch if the job says to, writes `receipts/<TICKET>.md`, marks the gate file DONE, and marks the claim DONE.
- A gate that executed tests and failed is repaired and retried only under Gate repairs below; otherwise it is handed back for implementation. A run that never executed tests (environment refusal, VM replacement) may be relaunched once by the claiming or taking-over host.

## Gate repairs and hand-back

Windows agents post gate requests but never run tidy or Gradle. The Linux gate host runs tidy first and owns the repair and the re-gate.

GATE INVALID is not a gate outcome. A tidy change, a changelog problem, or a finding in repair scope below is repaired under this section, and the gate reruns once. A job file that says GATE INVALID and stops is read as the repair route, not as a stop.

Repair scope. A gate host may repair these failures and re-gate. Nothing else.
- Tidy changes: apply the tidy output as it is.
- Changelog problems the validator reports (type, links or issues).
- Compile errors, and Error Prone or ECJ lint findings whose message names the fix (a missing import, an unused import, a helper that hides an inherited one).
- Up to two failing tests, when the repair is in test code, or a production change of one or two lines that changes no expected value and is not an escalated decision.

Out of scope: changing an expected value in a test, deleting a test, any change that answers an escalated decision row, edits outside the job's Diff scope, and any repair the host cannot state in one line.

Recording. Each repair is one commit, `TICKET: gate repair: <summary>`, authored as Nick Shanin with no trailers. It is pushed fast-forward to the ticket branch from the head the gate ran on; if the branch tip has moved, stop and record it. The receipt names the original head, each repair commit with its files, and the gated head. The proof leg runs on the repaired head.

One retry. After repairs the gate runs again from step 0 on the new head. The retry is allowed only if the repair changed the tree. If the retry fails at any step, or a repair falls outside scope, the gate job's Status becomes `HANDED BACK (reason)` and the ticket returns to implementation. The receipt records both attempts.

## What stays addressed, not pooled

- Decision slates and answers passes for Nick are prepared by any pool host from the full record and they end at Nick.
- PR openings are executed by any pool host once Nick's approval is recorded on this branch, each opening with his explicit approval.
- Everything else (audits, drafts, implementation, gates, premise runs, settling runs, sweeps) is pool work.

## Standing limits that bind every host

- No cross-host subagent cap (the owner removed it on 2026-10-17: the host agents themselves are the parallelism now). An assignment still names its staffing, and a host says in its claim what it is running. Subagents are for research and implementation, never for waiting on queues or gate results.
- Public text follows `pr-formula.md`: plain language, no internal process vocabulary, proof numbers only from receipts or gate files. PR bodies and PR or JIRA comments never say "we" or "our", and never mention gates, claims, pool hosts or other internal records. Keep proof to one or two sentences per claim. Comments follow `comment-formula.md`.
- Keep posted public text current. When a comment, PR description or PR body we posted is wrong or incomplete, edit it right away, in the same session, and read it back after the edit. Do not leave a known-wrong comment up while waiting for the owner. Edits to text already posted are allowed; creating a new PR, comment or JIRA entry needs the owner's approval.
- Maintainer requests on our PRs are implemented at the scope written; feasibility is settled by attempting the design, and any objection goes to the owner before a reply is drafted (the maintainer requests section of `material/coordinator-handoff.md`).
- No builds on the Windows host (above). No new Jira ticket unless a maintainer asks or Nick directs it.
- Name submission-ready product branches `solr-<TICKET>-submit`. Use a descriptive suffix only when the branch serves a distinct purpose such as a phase, proof, backport, or experiment, and keep the ticket key in the name.
- Commit identity is Nick Shanin; no other name in authors, committers or trailers; no em dashes in authored text.

## Queue buffer

Each queue keeps unclaimed work available so a host finishing a task finds the next one waiting (owner direction, 2026-10-10). Targets: at least 2 unclaimed review or draft assignments, and at least 3 unclaimed gate jobs from the shared backlog (assignments/pool-shared-gate-backlog-buffer.md). Hosts top the buffer up when posting assignments or finishing review rounds; the queue check counts unclaimed items and reports when either queue runs dry.

Status lines: every assignment and gate job file carries a Status line under its title (`Status: OPEN`, `Status: CLAIMED (claims/<claim file>)`, `Status: DONE`, `Status: UNCONFIRMED`, or `Status: META` for a finding aid). The poster sets OPEN. The claimant sets CLAIMED, naming the claim file, in the same push as the claim. Completion sets DONE in the same push as the deliverable. `OPEN-WORK.md` at the branch root is generated from those lines and is where agents find open work; if it and a file's own Status line ever disagree, the file's Status line governs.

Terminal states. Once an agent has touched an item, it must end in one of these states, and OPEN is never left behind:

- `DONE`: the deliverable is pushed and the receipt is written.
- `NEARLY`: the work is finished except for one named, bounded piece (for example a rerun or a pending owner check). The claim names that piece and its next step, so the next host does only that piece.
- `WON'T DO`: refused under `pool/advisory-wont-do.md`, with the evidence.
- `OWNER DECISION`: one question the decision map and ladder cannot settle, written into the item with its options and a recommendation. Rare.
- `RETRY`: the agent gave up for a stated reason (host lost, gate environment refusal, repair outside scope). One more attempt is allowed. The item names what the retry must do differently. A second give-up becomes `HANDED BACK` or `WON'T DO`.
- `HANDED BACK (reason)`: a gate or implementation that failed its retry and returned to implementation, as in the gate rules above.

No re-claim after a pass. An item whose Status is NEARLY, RETRY, DONE, WON'T DO, OWNER DECISION or HANDED BACK is not claimed again by a new host. Only a RETRY or NEARLY item names its next actor, and only that actor takes it. A claim with a stale heartbeat (2 hours) is taken over under the lease rules above, not reclaimed as a fresh OPEN item.

No polling. Hosts do not sleep, loop or re-check status inside a session. Long work starts as a background job (or runs under the lock detached), and the session ends with a scheduled wake that names the job, its log directory and the next step. Results are read on the wake.

## Lane coordinators

Since 2026-10-11 the pool has two lane coordinators (Nick approved): VM2 coordinates the gate backlog end to end, and the Windows agent coordinates the review rounds end to end. They hand work to each other through claims, gate job files and reports on this branch and keep the loop running without waiting for any single host. PR openings and flips and new comment posts are executed by any pool host once Nick's approval for that act is recorded on this branch; anything needing Nick's decision is posted for him and waits for his recorded word; gate failures where tests executed are investigated by the lane coordinator first and are never relaunched until the investigation is recorded in the gate file. Nick extended the coordinators' authorization on 2026-10-11: either coordinator may update Nick's own PR descriptions against receipts, implement changes identified in review through the full hardening gate and push its own patches, and decide scope questions under the decision default, recording each decision where the category records live. The full handoff, with roles, escalation rules, inherited standing rules and the state each coordinator picks up first, is at `material/coordinator-handoff.md`.
