# The decision map: classification axes and standing answers

Owner direction, 2026-10-19: agents ask the owner questions that the records already answer. This page exists so they stop. Every item of work is classified on the axes below, and each axis carries its default: what to do without asking. A question reaches the owner only if it passes the escalation test at the end of this page. The owner answers questions that are genuinely ambiguous; a question the map settles is a defect in the asking, and the fix is to read the map, not to wait for an answer.

## The axes

**Work type.** Gates; existing tickets (new implementations, comments, closures); new tickets; existing PRs; review of our branches. The type names the deliverable and the template. A gate is evidence production for a branch, not a separate kind of progress.

**Store.** Follow-ups and reviews of existing work; the skip list; the CSV backlog, pulled in that order (`pool/work-stores.md`). No store is reported empty while another holds unclaimed items; slices are self-serve.

**Solr category.** One of the 14 in `pool/work-map.md`. The category names the brief to read, the siblings to check, and the surfaces where collisions happen.

**Lifecycle state.** Ticket, branch, gated, draft PR, open PR, changes requested, approved, merged, retired. State comes from the live source (Jira for tickets, the fork for branches, GitHub for PRs), never from a record's age.

**Evidence state.** Premise reproduced or code-read only; fail-before shown by content or assumed; gate green at the exact current head or at an older one; receipt fresh or stale against the tip; CI state; review state. Work whose evidence is asserted rather than shown is not done, whatever its status line says. Verify at the tip before acting on any record.

**Authority.** Some acts are pre-authorized: pushes to the owner's own open PRs, edits to his own PR bodies and comments, tidy and re-gate on his own prepared branches, gate repairs under WORKFLOW.md. Some acts always need the owner's word first, recorded on the branch: opening a PR, flipping a draft to ready, posting a new comment, filing a Jira ticket (the owner files tickets himself), retiring a branch, sending anything from the private track. Prepare everything for a held act, record the ready state, and take other work; do not idle in front of it.

**Visibility.** Branch records are public. Private material (the security track, credentials, the owner's personal context) never enters a branch record, a claim, or a report, in any wording.

**Capability fit.** Assignments address a capability, not a host name. The agent categories: phone Linux (the owner's direct line; holds memory and the approval interface; runs no factory tick and no coordination role); VM Linux, the pool hosts (gates, implementation, review; each with its own queue); Windows (review, implementation, and drafting only; no builds, ever). If an item needs a capability you do not hold, post it as an assignment for that capability and take other work. Routing work is a pool act, never an owner question.

**Sequence.** Parents before follow-ups; a follow-up ticket is filed after its parent PR merges; chains and preconditions are read from the records before claiming (SOLR-17393 before SOLR-9637; PR #5065 before PR #5118; SOLR-18339 waits on its release precondition). A blocked item is recorded with its blocker and left; it is not escalated as a question.

**Unit size.** One item, a slice, a batch, a category slate, the portfolio. Pick the unit the assignment names; do not shrink a batch into single items or grow an item into a project without recording why.

**Value.** Priority first (Blocker, then Critical), then harm weighed with maintainer warmth and likelihood to convert. Under a scarce-slots regime, the middle of the portfolio is not worked actively; the map's category tables carry the ranking inputs.

**Cost.** Effort, gate time, and token burn are all costs the claimant spends deliberately. The decision ladder prices effort: where the cost is effort, do the hard thing.

**Reversibility.** Edits, pushes, and records are reversible and proceed. History rewrites, branch deletions, and mass actions are one-way: they stop for the owner unless a standing rule already covers the exact case.

**Record health.** Claimed or unclaimed, heartbeat fresh or stale under the lease rules, duplicate or terminal. Terminal states (DONE, WON'T DO) are never edited; supersession is a new record naming the old.

## Standing answers

- No work for me? Pull the next store (`pool/work-stores.md`). If the posted slices are taken, cut the next one.
- Already done, already claimed, cannot finish, stale paperwork? Refuse it under `pool/advisory-wont-do.md`, clear the record, take other work.
- Which record is true? The live source is true: ls-remote for branches, Jira for tickets, GitHub for PRs, the log and fresh JUnit XML for tests. Records are claims until verified.
- A gate failed. Classify it first (`pool/guides/failure-atlas.md`). An environment refusal may relaunch once; a run that executed tests and failed is recorded and investigated, never relaunched on sight.
- A decision is needed. Apply the ladder (`material/decision-ladder.md`): missing information, get more information; cost is effort, do the hard thing; optional, defer it as a named follow-up; a genuine choice between defensible behaviors is posed as a Choice in the PR. Record the outcome in the item's record.
- A maintainer asked for a change on one of our PRs. Implement it at the scope written. Feasibility is established by attempting the design, never asserted in advance. An objection goes to the owner with the design and its cost in front of him, after the attempt, not before.
- The branch has no ticket number. Its parent is in the work map's table. A ticket that does not exist yet belongs in `pool/tickets-to-open.md` with its trigger; agents maintain the registry and do not file tickets.
- My host lacks the context or the file. That names the host that had it, not a blocker: read the records, or post the item for the capability that holds it.

## The escalation test

A question goes to the owner only when all four hold: no standing rule or record settles it; the ladder does not decide it; the missing fact cannot be obtained by reading Jira, the code, or the branch; and the choice is genuinely ambiguous, with defensible outcomes the owner would weigh differently. When a question passes, it is asked once, in the item's record and in the closeout that reports it, carrying the item, the axis, the options, and a recommendation. A question that fails the test is answered by the map, and the work continues.
