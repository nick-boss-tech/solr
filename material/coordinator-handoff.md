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

### Pushing to Nick's own open PRs

- Standing authorization from Nick (2026-10-14): pushes to Nick's own open PRs are always fine. No per-push owner yes is needed for a push to a branch that heads one of his open PRs, including packaged heads and patch-loop heads, under the push forms in the section above (plain for live PR branches, `--force-with-lease` for rebuilds).
- This discharges the SOLR-11356 escalation recorded as E1 in `reports/solrj-convergence-round-2-response.md`: the packaged push to `6120dae28d04` stands confirmed by this rule, with no reversal.

### Maintainer requests on our PRs

Owner ruling (Nick, 2026-10-14). When a maintainer asks for a change on one of Nick's own PRs, the default is to implement it as asked, at the scope the request is written at.

- Attempt the design before saying anything about whether it can be done. Feasibility is established by the attempt, never asserted in advance from a reading of the code.
- A request is not narrowed or partially applied on the lane's own judgment. If the attempt shows the request is costly or inadvisable, that finding goes to Nick first, through the main agent, with the design and the cost in front of him. Whether to push back is Nick's call, not the lane's.
- No reply that declines or narrows part of a request is drafted before the design exists, and no reply argues the reviewer out of a feasible request on our own initiative. Objections travel to the owner, not to the reviewer.

The failure this corrects: a reviewer asked twice for all the sleeps in a test to be replaced with latches. The lane converted one sleep, replied with reasons the rest had to stay, and produced the full conversion design only when Nick pressed. The reasons given were the lane's preference presented as a constraint.

### Deciding scope questions

- Either coordinator may decide a scope question under the decision default: a recorded recommendation is adopted; where no recommendation is on record, the harder and more thorough option is taken.
- Each decision is recorded in the category's TESTING.md or answers file, in the same push that acts on it.

### Live PR text edits

Owner ruling (Nick, 2026-10-21): live title and body edits on existing PRs do not need approval. Make the edit, re-read the live text to confirm it, and record it in the receipt or report. Keep the text in `pr-drafts/` as the record. The inventory of edits that were waiting is `reports/live-edit-inventory-2026-10-21.md`.

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
- VM3 is up. Nick confirmed on 2026-10-18 that this host is the new vm3, on a different platform from the 2026-10-11 host that went down. Claims and assignments go to it under the usual rules; its state is in `hosts/vm3.md`.
- GitHub Actions is permanently unavailable. Gate evidence stands on local gates alone; no coordinator dispatches, queues or waits on Actions runs.
- Decision default: a recorded recommendation is adopted as the decision; where no recommendation is on record, take the harder, more thorough option. Coordinators apply the default inside pool work and escalate only the items listed above.
- Public text follows `pr-formula.md`: plain language, a bold one-line summary opening each section, citations as real links (code the change produces at the head commit; the pre-change symptom at the merge base, saying so), no internal process vocabulary, proof numbers only from receipts or gate files.
- Commit identity is Nick Shanin; no other name in authors, committers or trailers; no em dashes in authored text; no new Jira ticket unless a maintainer asks or Nick directs it.

## First loop: the state each coordinator picks up

### Gate queue (VM2 picks up)

- 7 unclaimed gate jobs sit in `gates/` and on the shared backlog list, claimable by either Linux host.
- SOLR-8628's premise run stands at parts 1 and 2 confirmed (the write.lock-only crash state is reachable) with part 3, the test leg, unfinished. VM3 released the claim before going down; the job is claimable.
- SOLR-5262's first gate is blocked on a dependency resolution failure (the develocity plugin) with no tests run. VM3 released the claim; the gate is claimable, and the failure is an environment refusal, not a test failure.
- VM3 is up again (2026-10-18). Sequencing runs across VM1, VM2 and VM3.

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

## Owner directives (2026-10-12)

Two directions from Nick, recorded by the main agent on 2026-10-12.

- When the unclaimed gate queue is large, the main host (vm1) also claims and runs pool gate jobs from `gates/`; it does not sit idle while the queue is deep. Hosts should expect gate claims from vm1 alongside vm2's. The claim rules in WORKFLOW.md apply unchanged: claim before work, hourly heartbeats, and no gate runs on two hosts at once.
- Review assignments may direct the review coordinator's side to write the patch for a finding it raises, and then to re-review its own patch in a following round. That closes the loop on the review side instead of sending the work back to the main agent. This sits inside the extended coordinator authorization above: a branch change goes through the full hardening gate, the patching agent pushes it, and the gating host refreshes the ticket's receipt. The main side verifies the result rather than duplicating the work.

## Owner directives (2026-10-12, later): vm2 patch authority and main-side offload to the review side

Two more directions from Nick, recorded by the main agent on 2026-10-12.

- The gate coordinator (vm2) has the same patch authority the review side has. A gate job file may direct vm2 to patch the branch itself so the build succeeds or so the premise is validated, instead of sending the fix back to the main side. The ticket and branch status vm2 needs is on pr-prepare, in `receipts/`, `gates/` and `claims/`. The rules around a patch are unchanged: the full hardening gate, the patching agent pushes, and the gating host refreshes the ticket's receipt.
- While the review side has spare capacity, the main side will hand it settlement and application work as assignments, not just fresh review rounds. Where it fits, that work is framed as an apply-and-re-review loop under the patch-loop directive above. The main side keeps the local queue and the vm1 gates.

## Gate census for vm2, 2026-10-12 evening (main side)

The gate coordinator reported running out of work. The main side checked `gates/` and `claims/` at the pr-prepare tip on the evening of 2026-10-12 and records the true census here, because the gate queue is not empty: it holds more unclaimed jobs than the standing buffer target of 3. Receipts are not the constraint; no new gate jobs need to be created for vm2 to have work. The jobs below are OPEN and unclaimed at this tip, each verified against its job file's status line and against `claims/`, and each is claimable by vm2 under the pool rules (claim before work, hourly heartbeats).

- SOLR-11678-sec-r1-05 (`gates/SOLR-11678-sec-r1-05.md`): the SEC-R1-05 branch fix, written by the claiming host under the patch-loop directive, then the full hardening gate at the new head.
- SOLR-12916-title-fix (`gates/SOLR-12916-title-fix.md`): the CA-R1-34 changelog title correction, then a re-gate at the new head.
- SOLR-15106-premise-base (`gates/SOLR-15106-premise-base.md`): premise and settling run for the SOLR-15106 draft's base claim.
- SOLR-15674-history-scrub (`gates/SOLR-15674-history-scrub.md`): rebuild the branch history without the one trailer line, then the full hardening gate at the scrubbed head.
- SOLR-17733-dated-run (`gates/SOLR-17733-dated-run.md`): dated settling run, then the receipt line.
- SOLR-18391-changelog-wording (`gates/SOLR-18391-changelog-wording.md`): the changelog fragment wording correction, written by the claiming host under the patch-loop directive, then the full hardening gate at the new head.
- SOLR-5011-narrowing (`gates/SOLR-5011-narrowing.md`): implement the adopted narrowing from the shared-schema analysis, add its proving test, then the full hardening gate at the new head.
- SOLR-6014-searchtime-premise (`gates/SOLR-6014-searchtime-premise.md`): premise run only; the SOLR-6014 draft's final Limits wording follows the verdicts.
- SOLR-6430-fragment-drop (`gates/SOLR-6430-fragment-drop.md`): delete the changelog fragment under BD-R1-13, then the docs-shaped gate at the new head.
- SOLR-9091-title-fix (`gates/SOLR-9091-title-fix.md`): the RB-R1-04 changelog title correction, then a re-gate at the new head.
- combinedquerycomponent-real-exit-premise (`gates/combinedquerycomponent-real-exit-premise.md`): premise run only; it settles the real timeAllowed exit questions the ticket draft leaves open.
- pr-drift-round-1-run-records (`gates/pr-drift-round-1-run-records.md`): four record runs behind live description claims; a claimant may take one item or all four.

Already in vm2's hands, so none of these needs a new claim from the list above:

- SOLR-12505, under amendment 1 (`gates/SOLR-12505-fetchstream-fix-amendment-1.md`): the amendment's restructure of the fix gate, after the gate at d81a77311773 failed at compile.
- The SOLR-14919 re-gate at 8f1f4ef91e (`gates/SOLR-14919-processor-only-gate.md`), claimed and running.
- SOLR-17680 (`gates/SOLR-17680-message-regate.md`), claimed by vm2 at 2026-10-13T03:04Z.
- SOLR-10667-exclusion (`gates/SOLR-10667-exclusion.md`), claimed by vm2; its fix commit is recorded and its gate is queued on vm2's serial queue.
- SOLR-9155-declared-route-gate (`gates/SOLR-9155-declared-route-gate.md`), claimed by vm2 at 2026-10-13T02:46Z, so it is already taken and does not belong on the unclaimed list.

Two standing vm2-local items from the 2026-10-11 push request above are still owed. The SOLR-10364 and SOLR-11678 packaged pushes were completed on 2026-10-12, but two packaged heads are still local to vm2 and have not reached the fork:

- SOLR-11356: push packaged head 6120dae28d04a07763f44d4bc5e9d9793af025f6 to solr-11356-submit (fork tip 8474e5a3a26d8aacd25d9fc9838c627bc6c5b14f), and refresh receipts/SOLR-11356.md on the push. After the push, SJ-R1-49 still needs its recorded branch fix (copy the headers in the stream key) and a proof re-run at the new head.
- SOLR-12161: push packaged head cdd265168a2adc76fb788f3d1f5587a3eee05e42 to solr-12161-submit (fork tip 1725cbd8489889a1dad75279e4b2c11b0ec93d66), and refresh receipts/SOLR-12161.md on the push.

One exclusion: the SOLR-10667 and SOLR-12347 gate jobs (`gates/SOLR-10667.md` and `gates/SOLR-12347.md`) are blocked on vm2 by its npm cache failure (the @babel/plugin-syntax-dynamic-import resolution failure). The main side takes those two on vm1 under the established workaround (a fresh NPM_CONFIG_CACHE clears them), so vm2 should leave both jobs alone.

## Build queue loaded for vm2, 2026-10-13 (main side)

The main side audited `gates/` at the pr-prepare tip on 2026-10-13 and loaded the build queue. Every job below is OPEN at this tip, verified against its job file's status line and against `claims/`, and claimable by vm2 (most also by vm1) under the pool rules: claim before work, hourly heartbeats, a gate claim also requires its recorded log stopped before any takeover. The shared list in `assignments/pool-shared-gate-backlog-buffer.md` carries the same census as a dated section.

SOLR-16437 first, because vm2's complaint about it is correct: solr-16437-submit's live tip is `a23f770e4b9587acbd14fcdbaae2047bd5c727c4`, one changelog-title commit past the gated head `f72a9edd0a8b`. The receipt (`receipts/SOLR-16437.md`) and the draft (`pr-drafts/solrcloud/SOLR-16437.md`) still name the old head, so neither record settles the live tip. The job `gates/SOLR-16437-changelog-regate.md` was amended by the main side on 2026-10-13 to carry the full hardening gate at the new head (focused class `org.apache.solr.cloud.CollectionsAPISolrJTest`, seed `16437C0FFEE16437`, proof leg against merge-base `e2cdb2d7e8ae`), the receipt refresh, and the draft re-point including the two suspects the moved-heads re-check named (the draft's printed short head `f72a9edd0a6f` vs the true `f72a9edd0a8b`, and a 2026-10-10 date vs the receipt's 2026-10-12 run).

The loaded queue:

- SOLR-16437-changelog-regate (`gates/SOLR-16437-changelog-regate.md`): full re-gate at the moved head `a23f770e4b9`, receipt refresh, draft re-point with both suspects settled.
- SOLR-11479-identity-carry (`gates/SOLR-11479-identity-carry.md`): carry-over check for the identity rewrite at `6f739cfbbd38`; tree identity against the gated head, author and committer check, changelog parse, receipt refreshed at the live tip.
- SOLR-15035-base-run (`gates/SOLR-15035-base-run.md`): the owed base run for SOLR-15035; its landing also clears the SOLR-11479 opening dependency.
- SOLR-11288-base-run (`gates/SOLR-11288-base-run.md`): base run owed for SOLR-11288.
- SOLR-12651-history-scrub (`gates/SOLR-12651-history-scrub.md`): history scrub and re-gate for SOLR-12651, including the overstated changelog title correction the HOLD round assigned.
- SOLR-15674-history-scrub (`gates/SOLR-15674-history-scrub.md`): rebuild the branch history without the one trailer line, then the full hardening gate at the scrubbed head.
- SOLR-18391-changelog-wording (`gates/SOLR-18391-changelog-wording.md`): the changelog fragment wording correction, written by the claiming host under the patch-loop directive, then the full hardening gate at the new head.
- SOLR-5011-narrowing (`gates/SOLR-5011-narrowing.md`): implement the adopted narrowing from the shared-schema analysis, add its proving test, then the full hardening gate at the new head.
- SOLR-6014-searchtime-premise (`gates/SOLR-6014-searchtime-premise.md`): premise run only; the SOLR-6014 draft's final Limits wording follows the verdicts.
- SOLR-6430-fragment-drop (`gates/SOLR-6430-fragment-drop.md`): delete the changelog fragment under BD-R1-13, then the docs-shaped gate at the new head.
- combinedquerycomponent-real-exit-premise (`gates/combinedquerycomponent-real-exit-premise.md`): premise run only; it settles the real timeAllowed exit questions the ticket draft leaves open.
- pr-drift-round-1-run-records (`gates/pr-drift-round-1-run-records.md`): four record runs behind live description claims; a claimant may take one item or all four.

Assigned to vm2 by Nick (2026-10-13), claimed by vm2 under the pool rules before running:

- SOLR-16437-nightly-run (`gates/SOLR-16437-nightly-run.md`): the owed nightly-group run for SOLR-16437's caller classes.
- SOLR-17680-v2-two-router-test (`gates/SOLR-17680-v2-two-router-test.md`): the two-router test run SOLR-17680's round 5 hold names.

Direction already in vm2's hands, not a fresh claim: SOLR-12505 under amendment 1 (`gates/SOLR-12505-fetchstream-fix-amendment-1.md`).

Finished since the 2026-10-12 evening census in this file, so they are no longer supply: SOLR-12916-title-fix (gate green at `d62ddfc2461`), SOLR-15106-premise-base (done 2026-10-13T04:45Z), SOLR-9091-title-fix (gate green at `a596a39d2e1`), SOLR-11678-sec-r1-05 (gate green at `2c68a83c64`), SOLR-17733-dated-run (focused run green at `636196b7954`), and the SolrCloud dated runs round 2 (`gates/solrcloud-dated-runs-round-2.md`, items for SOLR-5813 and SOLR-12991 run green at the live heads, receipts updated).

## Review batch posted 2026-10-13 (main side): five assignments, hourly review cron

The owner moved the review agent to an hourly cron on 2026-10-13, so review work is posted in deep batches, not trickled. The three assignments posted on the morning of 2026-10-13 (eDisMax final review, `assignments/pool-edismax-final-review-round-1.md`; landing-order interaction review, `assignments/pool-landing-order-interaction-review-round-1.md`; proof-shape audit, `assignments/pool-proof-shape-audit-round-1.md`) remain claimable ahead of this batch. Behind them, in order:

1. `assignments/pool-branch-review-verdicts-landing-round-1.md`: land the search-components and query-parsing verdict reports from their assignment branches (verdict tips `289d5d3ad5` and `03fbf23793`) onto pr-prepare, then work every verdict: patch loop for NOT SATISFIED drafts, verified ready lists for the rest.
2. `assignments/pool-strong-review-round-2-unblocked-slices.md`: the round 2 slices the outstanding gates do not block: the SOLR-18541 body rewrite at the moved head `a93f9f6a997e`, and the SOLR-18010 draft and patch-prep items that do not depend on vm2's corrected gate. The waiting slices are named in the assignment.
3. `assignments/pool-category-convergence-round-2-settlements.md`: round 2 settlements for build-docs, CLI, security, streaming and SolrJ, claimable per category, each closing with its own response report.
4. `assignments/pool-decision-prep-15357-15144-round-1.md`: decision-ready recommendations with evidence for SOLR-15357 owner decision 2 and SOLR-15144's three options. Read-only plus the report.
5. `assignments/pool-retire-round-final-reports-round-1.md`: final evidence packs for the nine retire-round candidates, the missing SOLR-18277 receipt record, and the SOLR-17372 retire case on its NOT SUPPORTED premise verdict. Deletions and closings stay with the owner.

Each assignment is self-contained, names its starting records and deliverables, and carries the standing patch-loop directive where it applies. Claims and heartbeats follow WORKFLOW.md as usual.

## Standing authority confirmed 2026-10-13 (main side): the two held batch assignments proceed

The Windows review coordinator's latest scheduled-check claim holds back two of the five batch assignments above, `assignments/pool-branch-review-verdicts-landing-round-1.md` and `assignments/pool-category-convergence-round-2-settlements.md`, for owner direction, because their patch loops push branches and edit live PR bodies. That direction is already on record, and no new direction from the owner is needed:

- The review-side patch loop (owner, 2026-10-12): review assignments direct the review agent to apply the patches or wording changes its findings call for; the agent that patches a branch pushes it; review continues until each item comes back SATISFIED.
- The extended coordinator authorization (owner, 2026-10-11): either coordinator may update descriptions of the owner's own PRs, on facts grounded in a receipt or gate log at the live tip with each edit verified by re-reading, and may implement changes identified in review under the full hardening gate.

Both held assignments sit inside that authority: their pushes are patch-loop pushes by the patching agent, and their live body edits are coordinator description updates on receipt-grounded facts. The coordinator may claim them.

Still reserved, unchanged: PR openings and draft-to-ready flips need the owner's approval each time; new comment posts are drafted for the owner's approval first; branch deletions stay with the owner.

## Overnight category close-out posted 2026-10-13 (main side): the pool's priority until the morning slates file exists

The owner directed this evening that the category reviews close out overnight so the openings slates are ready for his approval in the morning. One assignment carries it: `assignments/pool-category-closeout-overnight-round-1.md`, five workstreams in one file (verdicts landing completion, convergence round 2 completion, the SolrCloud slate round 2, the morning slates assembly at `reports/morning-openings-slates-2026-10-14.md`, and the second review of the held PR 5106 reply). It is the pool's priority until that morning slates file exists: a host choosing its next claim takes a close-out workstream first.

The work already in flight keeps its place behind it, not instead of it: the three morning assignments (eDisMax final review, landing-order interaction review, proof-shape audit) and the five-item review batch posted earlier today continue after the close-out, in their posted order.
