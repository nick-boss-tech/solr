# Pool assignment: retire candidates review round

Capability tags: `review`. Intended host: windows. Staffing: 2. Claim path: claims/pool-retire-candidates-review-round.md (slice-level claims by ticket are fine).

Purpose: Nick's rule (2026-10-11) is that retire candidates get one review round first. Retire unless the round finds something gainable from the branch: a test worth keeping, a fix worth landing, or evidence worth recording. A gainable item may be tried even if it is not directly related to the ticket. This round is that review for the current retire pile. It is read-only: it recommends, it does not delete. Branch deletions and PR closings stay with Nick and the main side.

## The pile

For each item, read the branch at its live tip (verify with `git ls-remote`), its receipt in `receipts/`, and the category record that put it in the pile. Then judge what, if anything, the branch still holds that main does not.

1. **SOLR-17722** (branch solr-17722-submit). Premise dead by run evidence: two independent probe runs (vm3, then vm1) showed the wire already carries wt=javabin when the request carries wt=json, at both the merge-base and the branch head, so the branch changes nothing observable. Receipt: `receipts/SOLR-17722.md` (NO GATE by finding). Look for: any part of the branch (test, probe, or write-up) worth keeping as evidence on the ticket or as a regression pin.
2. **SOLR-17882** (branch solr-17882-submit). Query parsing round 1: NO GATE by finding, premise dead on current main, retire candidate with the owner call pending. Look for: whether the branch's test or analysis pins any behavior main still lacks.
3. **SOLR-17372** (branch solr-17372-submit). Search components: its gate failed on the premise; the change does not reproduce its problem on current main. Look for: whether any hunk addresses a different, still-live gap, per Nick's rule that a gainable item may be tried even when it is not the ticket's own issue.
4. **SOLR-12743** (branch solr-12743-submit). Build/docs round 1: retire/closed, no draft. Look for: any doc or build change main has not since taken another way.
5. **SOLR-17825** (branch solr-17825-submit). Build/docs round 1: retire/closed, no draft. Same check as 12743.
6. **SOLR-10198 branch** (branch solr-10198-submit). The ticket's PR merged into apache/solr main on 2026-10-07 (merge commit e97c2a0884cb); only the branch deletion is outstanding. Look for: any commit on the branch that is NOT in main (a straggler fix or test that the merge did not carry).
7. **SOLR-18119-submit PR** (branch solr-18119-submit). Superseded: the shipped work went out through the rebuilt solr-18119-jvm branch and its pair; this submit branch and its PR are the leftover. Look for: anything in this branch the shipped pair does not contain.
8. **SOLR-18277 branches** (solr-18277-submit plus its two CI branches). The ticket was resolved as Fixed in Jira by Andrzej Białecki on 2026-10-06; the SolrCloud round 1 answers adopted deleting all three branches. Look for: any branch content (test, CI change, doc) not present on main.
9. **SOLR-9759** (branch solr-9759-submit, head 31e702622dae). Admin UI round 1: superseded on main by c6f910d3ccf (PR #5048); retire recommended. Look for: any part of the hypothetical-reproduction work that documents behavior the superseding change does not cover.

## Verdicts

Per item, one verdict in the report:

- **RETIRE CLEAN**: nothing gainable found. State what was checked (tip, receipt, diff against current main) in two or three lines; a bare assertion is not a verdict.
- **SALVAGE FIRST**: name the gain exactly (the test, hunk, or evidence, with file and commit), and spec the smallest job that captures it (what branch or PR it lands in, what gate or check proves it). The salvage job is proposed, not started; the main side schedules it.

Where a branch's diff against current main is empty or fully contained in main, say so with the merge-base and the command used; that is the strongest RETIRE CLEAN evidence.

## Deliverable and limits

Deliverable: `reports/retire-candidates-review-round.md`, verdict counts up front, one section per item in the order above.

This assignment is read-only apart from the claim and the report. Do not delete or push any branch, do not close any PR, do not edit a receipt, and do not run a build, test, or gate. A salvage that needs a run gets spec'd in the report (class, seed, expected result each way), not run.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text. No PR numbers in file names.
