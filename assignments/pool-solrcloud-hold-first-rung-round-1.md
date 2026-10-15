# Pool assignment: SolrCloud HOLD drafts, first-rung round

Status: DONE

Capability tags: `review`. Intended host: Windows (review coordinator), under the standing coordinator claim. Staffing: one review agent per draft in parallel where the coordinator can staff it, items run per draft independently.

## Owner directive (recorded 2026-10-12)

The review agent applies the patches or wording changes its findings call for and keeps working each item until it settles. A round that only lists findings without acting on them does not discharge this assignment. Scope questions are decided under the decision default and the three-rung ladder, and each decision is recorded where the category records live (`material/solrcloud-round-1-answers.md`, `material/solrcloud-round-2-decisions.md`) or in the round report.

## Mode: the first rung, per draft

This batch is the HOLD set in `reports/solrcloud-pr-audit-round-3.md` (8 HOLD). Batches A and B cover the READY and NOT READY sets in separate assignments. For each draft below, work the ladder's first rung:

- **Where information is missing, get more information.** State exactly what is missing and go get what this host can get by reading (code, drafts, receipts, records on this branch). Runs are not available on this host: a premise or gate need becomes a gate job under `gates/` with the exact question stated (branch, head, base, module, test classes, seed), following the existing gate job shape. Most of these drafts already have their job posted; the first rung then is to verify the job still carries the exact question and is still open, and to say so. Post a job only where none exists.
- **Where the hold is effort rather than information, do the hard thing** under the patch loop: apply the wording or record fix in the same round, to the draft file, committed and pushed with the round's report.
- **Where an item is optional, defer it** as a named follow-up in the draft's Limits, with a stated plan.
- An item that survives all three rungs is an **owner item**: state the surviving question in one sentence in the report.

Governing records: `reports/solrcloud-pr-audit-round-3.md`, `reports/solrcloud-decisions-round-5.md`, `reports/solrcloud-readiness-round-1-application.md`, `reports/solrcloud-application-round-2.md`, `material/solrcloud-round-1-answers.md`, `material/solrcloud-round-2-decisions.md`. Where a later record changes the round 3 state, the starting note below says so.

## The batch

1. **SOLR-11288.** Draft: `pr-drafts/solrcloud/SOLR-11288.md`. What holds it: the Proof's base sentence rests on the prior head; no base log is on disk. Missing information: a base run at the live head `cbf0c678f277`. Job posted: `gates/SOLR-11288-base-run.md`. First rung: verify the job states the exact question; nothing else blocks the text, so any wording item found on the way is applied in this round.
2. **SOLR-12651.** Draft: `pr-drafts/solrcloud/SOLR-12651.md`. What holds it: trailers on three commits, then a base run at the rewritten head. Jobs posted: `gates/SOLR-12651-history-scrub.md`, `gates/SOLR-12651-base-run.md`. One item here is effort, not information: the changelog title ("unless the restore runs asynchronously") overstates the code. Write the corrected title text into the report and into the scrub job file in this round, so the scrub carries it. The landing order after SOLR-9598 is adopted and already stands in the draft's Limits; leave it there.
3. **SOLR-13369.** Draft: `pr-drafts/solrcloud/SOLR-13369.md`. What holds it: a dated focused run at the live head `dfa0db5bdf96`. Job posted: `gates/SOLR-13369-dated-run.md`, whose step 4 is the base-code run the Proof's base sentence needs. The Choice's settling fact is also named in the readiness application: a run where one route prefix really lands in two shards; six seeds on base did not produce one, and the test-side rule is adopted in the record while the Choice stays a maintainer question. First rung: verify the job carries both the dated run and that question.
4. **SOLR-15035.** Draft: `pr-drafts/solrcloud/SOLR-15035.md`. What holds it: a base-code run and a dated banked-patch run. Job posted: `gates/SOLR-15035-base-run.md`. The other question this draft once carried is already settled: the readiness application (round 2) confirmed the Jira summary against the draft and found the receipt green at the tip, so on information this draft is READY TO OPEN; the runs are its remaining record. It opens first in its group, before SOLR-11479, so if its records land it settles to SATISFIED and batch A's SOLR-11479 dependency clears; say so in its line.
5. **SOLR-15106.** Draft: `pr-drafts/solrcloud/SOLR-15106.md`. What held it in round 3: the premise compile text and a dated head run. Job posted: `gates/SOLR-15106-premise-base.md`. Decisions round 5 lists this draft READY. First rung: verify whether the run record has landed at the live head. If it has, close the item as SETTLED TO SATISFIED after a text pass. If it has not, confirm the job carries the exact question and name it in the line.
6. **SOLR-15386.** Draft: `pr-drafts/solrcloud/SOLR-15386.md`. What held it in round 3: a dated run at `ca8cb61ee957`, because the recorded 2026-10-07 date is a takeover-log date. Job posted: `gates/SOLR-15386-dated-run.md`. Decisions round 5 lists this draft READY. Same verify-first shape as SOLR-15106.
7. **SOLR-15674.** Draft: `pr-drafts/solrcloud/SOLR-15674.md`. What holds it is a sequence, not missing information: history scrub (one commit's body is only a co-author trailer line), re-gate, and re-pointing of all links and the changelog. Job posted: `gates/SOLR-15674-history-scrub.md`. First rung: state that plainly in the report, verify the job spec is complete (the contaminated commit is named, the tree-identity checks and the gate at the scrubbed head are specified), and record that the draft's length is accepted for a complex ticket, per the readiness application. Nothing in this item is the reviewer's to run.
8. **SOLR-16437.** Draft: `pr-drafts/solrcloud/SOLR-16437.md`. What holds it: a changelog-only gate at the moved head, and the nightly-group run. Jobs posted: `gates/SOLR-16437-changelog-regate.md`, `gates/SOLR-16437-nightly-run.md`. The round 3 owner question (run the nightly first, or post with the skip disclosure in Limits) is answered: decisions round 5 records the nightly run assigned by Nick. First rung: verify both jobs carry their exact questions; the skip disclosure stays in Limits until the nightly record lands.

## Rules

- Verify every state against the live fork head (`git ls-remote`) and the receipt in `receipts/` at that exact head before writing a line about it; records written earlier in the evening may already have been overtaken by a gate landing.
- Code patches, if a first-rung fix needs one, follow the patch loop: the reviewer writes and pushes the patch, then posts a gate job under `gates/` for the patched head, and the item counts as reviewed only against a gated head. Windows runs no builds.
- Voice and text rules apply to every edit: no em dashes; authored text never uses "we" for the owner plus agent (write as "I" or neutrally); public PR text carries no internal process vocabulary; titles must stay accurate; citations to changed code point at the head SHA and citations to the pre-change symptom point at the merge-base, saying so.
- No PR is opened, closed, or flipped by this assignment, and no comment is posted anywhere. Nothing is sent by email and no Jira text is written.

## Deliverable

`reports/solrcloud-hold-first-rung-round-1.md`, with one section per draft and a closing line per ticket in exactly one of these forms: **SETTLED TO SATISFIED** (with the record that settled it), **GATE JOB POSTED** (naming the job file and the exact question it carries; where the job already existed, "GATE JOB CONFIRMED" with the same naming), or **OWNER ITEM** (the specific question that survived all three rungs, in one sentence). The assignment is discharged only when all eight lines exist.
