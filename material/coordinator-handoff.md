# Coordinator handoff: lane coordinators for the gate backlog and the review rounds

Written by the main agent, 2026-10-11. Nick approved lane coordinators on 2026-10-11: VM2 coordinates the gate backlog and the Windows agent coordinates the review rounds. The two coordinators hand work to each other through this branch and keep the loop running without waiting for the main agent. The main agent keeps the items listed under "What stays with the main agent" and can step back in at any time; Nick can redirect or revoke this arrangement at any time.

## Roles

### VM2: gate backlog coordinator

- Owns the gate queue end to end: `gates/` job files, the shared backlog list at `assignments/pool-shared-gate-backlog-buffer.md`, and claims on gate work.
- Keeps `gates/` stocked: when a branch's state in the receipts here shows it needs a first gate, a re-gate after a fix, or a top-up gate, VM2 writes the job file with branch, head, module, focused test classes, seed and proof shape, following the gate file format in WORKFLOW.md.
- Sequences gate jobs across the Linux hosts: which host takes which job, in what order, so no host idles while jobs wait and no gate runs on two hosts at once.
- Verifies receipts land: on green, the gating host writes `receipts/<TICKET>.md` itself; VM2 checks that the receipt, the gate file's DONE mark and the claim's DONE mark all landed, and chases the one that did not.
- Hands finished gates to the review coordinator: a gate that lands green and has no current draft gets named in a report or a claim heartbeat so the review side can pick up the drafting or fidelity work.

### Windows agent: review rounds coordinator

- Owns the review queue end to end: review, draft and fidelity assignments under `assignments/`, their claims, and the reports under `reports/`.
- Posts and staffs review rounds: writes the assignment files (capability tags, intended host, staffing), breaks large rounds into claimable slices, and makes sure slices are claimed rather than sitting unclaimed.
- Verifies fix lanes land: when a round's findings call for draft or wording fixes, the coordinator checks the fixes were applied and pushed, and records anything still missing in the round's report or a follow-up assignment.
- Requests the next round: when a round closes, the coordinator posts the follow-up round (the next fidelity pass, the next category, or a re-check of fixed items) instead of waiting for the main agent to assign it.
- Hands gated work back to the gate coordinator: a review finding that needs a code change and a gate is written up as a gate job request in a report or assignment, so VM2 can stock it into `gates/`.

### The loop between the coordinators

- All handoffs travel on this branch: claims in `claims/`, gate job files in `gates/`, reports in `reports/`, drafts in `pr-drafts/`, receipts in `receipts/`. Nothing is handed off by any other channel.
- A coordinator that finishes a piece of work leaves the next step written down where the other coordinator will find it: a DONE claim naming the deliverable, a report naming the follow-up, or a new assignment or gate job file.
- Neither coordinator waits for the main agent to keep the loop moving. The main agent reads the same files and steps in only on the escalation items below, or when Nick directs it.

## Coordinator authorization

Extended by Nick on 2026-10-11. Either coordinator (VM2 for the gate lane, the Windows agent for the review lane) may do the following in its own lane without asking the main agent first.

### Updating PR descriptions

- Either coordinator may update the description of one of Nick's own PRs on apache/solr, following `pr-formula.md`.
- Every factual claim in an edit is grounded in a receipt or a gate log at the branch's live tip. A claim with no receipt or log behind it does not go in.
- Each edit is verified by re-reading the body after the change and comparing it against the intended text.

### Implementing changes identified in review

- Either coordinator may implement a change a review round identifies, in the branch the round names.
- The change goes through the full hardening gate: changelog YAML parse, tidy, Error Prone compile, pre-fix proof against the base, counted focused tests from fresh JUnit XML, and the module check.
- The agent that patches a branch pushes it. Live PR branches push plainly; rebuilds push with `--force-with-lease` pinned to the fork tip read in the same session.
- On green, the gating host refreshes the ticket's receipt in `receipts/` itself, as with any gate.
- Build and gate runs inside the hardening gate still follow the capability tags in WORKFLOW.md. An implementation by the Windows coordinator is gated on a Linux host; the Windows host itself never claims build work.

### Deciding scope questions

- Either coordinator may decide a scope question under the decision default: a recorded recommendation is adopted; where no recommendation is on record, the harder and more thorough option is taken.
- Each decision is recorded in the category's TESTING.md or answers file, in the same push that acts on it.

### What stays reserved

- PR openings and draft-to-ready flips need Nick's approval each time.
- New comment posts are drafted for Nick's approval first, through the main chat. Coordinators draft; they do not post.
- Branch deletions stay with Nick.
- The private records (the takeover log, the master receipts ledger, and all security material) stay with the main agent.
- Gmail traffic and Jira text go through Nick.

## What stays with the main agent

- PR openings and ready flips. Every opening needs Nick's explicit approval, given in the main chat; coordinators prepare drafts and evidence but never open, flip or close a PR.
- New comment posts of any kind (GitHub comments and reviews, Jira comments). Coordinators draft text into `material/` or assignment deliverables; posting stays with the main agent after Nick sees the draft.
- The private records: the takeover log, the master copy of the receipts ledger, and all security material. The receipts folder on this branch is the shared copy; where it and a coordinator's reading disagree, the escalation rule below applies.
- Gmail traffic and Jira text for Nick to post. Only the main agent has mail access; handoffs name the needed text, never a mail action.
- Memory and Nick's standing directions. Coordinators work from this branch's files (WORKFLOW.md, this handoff, `pr-formula.md`, the answers files in `material/`); anything in those files that looks out of date goes to the main agent as a report, not a unilateral edit of the rule.

## Escalation: what goes to the main agent

- Anything that needs Nick's decision beyond the authorization above: retire calls, opening slates, sends and filings. Scope questions are not escalated; coordinators decide them under the decision default and record the decision.
- Any new PR, ready flip, comment post, or Jira text leaving this branch.
- Any record conflict the receipts cannot settle: a receipt, a gate file and a branch tip that disagree in a way the files themselves do not resolve.
- A gate failure the lane coordinator cannot classify after investigating it (see the gate failure rule below).
- Anything touching the private security material. It is never named, copied or summarized on this branch.

Gate failure rule: a gate that executed tests and failed is investigated by the coordinating agent for that lane first. The investigation root-causes the failure from the gate log and the fresh JUnit XML and is recorded in the gate file (failing test, seed, reproduce line, classification). The gate is never relaunched until that investigation is recorded. A failure the coordinator cannot classify escalates to the main agent.

## Standing rules the coordinators inherit

- Queue buffer targets: at least 2 unclaimed review or draft assignments and at least 3 unclaimed gate jobs at all times. A coordinator whose queue drops below target tops it up or escalates the same day.
- Subagent cap: at most 6 to 7 subagents running at once in total, across all hosts combined. Assignment staffing counts toward the same total.
- Claims: claim before work, hourly heartbeats, and the 2-hour staleness rule with recorded takeovers, exactly as in WORKFLOW.md. A gate claim may only be taken over when its recorded gate log has stopped.
- Windows never claims build work: no `gate`, `implementation`, `premise-run`, `settling-run`, `sweep` or `bats` claims from the Windows host. Its tags are `review`, `draft`, `selenium` and `windows-check`.
- VM3 is down until Nick says otherwise. Nothing is claimed there, and no job is assigned or sequenced to it.
- GitHub Actions is permanently unavailable. Gate evidence stands on local gates alone; no coordinator dispatches, queues or waits on Actions runs.
- Decision default: a recorded recommendation is adopted as the decision; where no recommendation is on record, take the harder, more thorough option. Coordinators apply the default inside pool work and escalate only the items listed above.
- Public text follows `pr-formula.md`: plain language, a bold one-line summary opening each section, citations as real links (code the change produces at the head commit; the pre-change symptom at the merge base, saying so), no internal process vocabulary, proof numbers only from receipts or gate files.
- Commit identity is Nick Shanin; no other name in authors, committers or trailers; no em dashes in authored text; no new Jira ticket unless a maintainer asks or Nick directs it.

## First loop: the state each coordinator picks up

### Gate queue (VM2 picks up)

- 7 unclaimed gate jobs sit in `gates/` and on the shared backlog list, claimable by either Linux host.
- SOLR-8628's premise run stands at parts 1 and 2 confirmed (the write.lock-only crash state is reachable) with part 3, the test leg, unfinished. VM3 released the claim before going down; the job is claimable.
- SOLR-5262's first gate is blocked on a dependency resolution failure (the develocity plugin) with no tests run. VM3 released the claim; the gate is claimable, and the failure is an environment refusal, not a test failure.
- VM3 is down, so sequencing runs across VM1 and VM2 only.

### Review queue (Windows picks up)

- The review-confidence round 4 and round 5 fix application is main-side work, in flight with the main agent; the coordinators do not duplicate it and treat the rounds' reports as the record until the fixes land.
- The newly gated drafts rounds are claimed and in progress on the Windows side.
- The unclaimed investigations (Admin UI test selection, the gradlew Git Bash path bug) remain available for claiming.

Both coordinators: claim your coordination role in `claims/` on your next pass, heartbeat it like any other claim, and start from the state above.

## Request to the gate coordinator (2026-10-11): push four packaged heads that exist only on vm2

Four branches were gated green on vm2 at packaged heads that are still local to vm2. Each packaged head is one packaging commit on top of the fork tip (it removes the branch's TESTING.md file and touches no code), so each push is a plain fast-forward packaging push of an already-gated head. The main agent verified the fork tips by ls-remote and the packaged heads against the receipts on 2026-10-11; all four receipts record GATE GREEN at the packaged head with the packaging commit not pushed.

- SOLR-10364: push packaged head efb1e6717f9ffda64b52c0820efb52ba58832b50 to solr-10364-submit (fork tip now 502bdbf033faa648792372960d01c54728349ee9).
- SOLR-11678: push packaged head 611eae2464536fdae61b8cae32abac97ee3a453b to solr-11678-submit (fork tip now 55d8cd189d15367b6896f0ea07570d3395ce404b).
- SOLR-11356: push packaged head 6120dae28d04a07763f44d4bc5e9d9793af025f6 to solr-11356-submit (fork tip now 8474e5a3a26d8aacd25d9fc9838c627bc6c5b14f).
- SOLR-12161: push packaged head cdd265168a2adc76fb788f3d1f5587a3eee05e42 to solr-12161-submit (fork tip now 1725cbd8489889a1dad75279e4b2c11b0ec93d66).

Review work is held on these pushes:

- The SOLR-11678 push unblocks security convergence items SEC-R1-03, SEC-R1-04 and SEC-R1-05, which the round 1 response records as held on the packaged head; the draft's head substitutions and the SEC-R1-05 branch commit follow in that pass.
- The SOLR-12161 push unblocks security convergence item SEC-R1-09, held the same way.
- The SOLR-10364 push unblocks SolrJ convergence item SJ-R1-45: the draft's head citations move to the packaged head once it is on the fork.
- The SOLR-11356 push unblocks SolrJ convergence item SJ-R1-48 and the draft's head updates; SJ-R1-49 then needs its recorded branch fix (copy the headers in the stream key) and a proof re-run at the new head, which build on the pushed head.

On each push, the gating host refreshes that ticket's receipt in `receipts/` so it records the packaged head as the pushed live tip. The review coordinator picks the held items up from the refreshed receipts.
