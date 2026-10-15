# Gate job: SOLR-18541 score-only timeAllowed exit, ticket input only

- Ticket: SOLR-18541 (PR #5117 draft at `pr-drafts/search-components/SOLR-18541.md`). Merge-base for this job: upstream main `d76fc14b9857e77b830d7a82514b28c523a239c2`.
Status: UNCONFIRMED (see the census note in OPEN-WORK.md)
- Capability tags: gate. Intended host: any capable Linux host under the WORKFLOW.md claim rules. Windows never claims build work.
- Why: the strong review of the four new draft PRs (`reports/four-new-prs-strong-review-round-1.md`, SOLR-18541 section) reads a second real-exit failure path by code only. A score-only sort whose first sub-query exits on timeAllowed could leave a partial shard with no per-query data, which the document loop might dereference. The review did not run it. The ticket text should say which of the two it is before the owner decides scope.
- Job type: premise run on the merge-base. No code change. No fork push. No PR touched. No test is added.

## Job

- Module: `:solr:core`. Class: `org.apache.solr.handler.component.CombinedQueryComponentRealExitPremiseTest` if that class is on the merge-base; grep for the exact name and package before composing the run.
- Seed: `C0FFEE42C0FFEE42`.
- Setup: a score-only sort (no sort field), a timeAllowed exit forced inside the first sub-query, on the merge-base code, with the branch's test fixture only.
- Questions:
  1. Does `response_per_query` stay absent for the partial shard? Record whether the key is absent, null, or an empty list.
  2. Does the merge throw `NullPointerException` at the document loop? Record the stack frame and the line.
  3. Record `numFound` and `numFoundExact` of every per-query entry the merge produces.

## Expected record

- A recorded answer to each question, with the stack trace or the entry dump in a file under `logs/` on the gating host, and the log path in the receipt section.
- If the run cannot force the exit inside the first sub-query on this code, record that as the result and stop. Do not invent a fixture.

## Step checklist

- [ ] merge-base confirmed; class and package grepped at the merge-base
- [ ] score-only exit run with seed `C0FFEE42C0FFEE42`
- [ ] question 1: per-query key state recorded
- [ ] question 2: NullPointerException present or absent, with the frame
- [ ] question 3: per-query entries dumped
- [ ] result section written to the ticket's receipt; job DONE

## Claim (vm1, 2026-10-13T17:10Z) and a spec note

- Claimed by vm1: claims/pool-vm1-gate-18541-score-only-exit-input.md.
- Runner: ~/workspace/tools/gate-strong-review-vm1-chain.sh (chained, serial on the test-queue flock). Log: ~/workspace/tools/g18541-score-only.log. Worktree: ~/workspace/solr-worktrees/cqc-score-only-exit.
- Spec note (ambiguity, recorded rather than guessed): the class named above, org.apache.solr.handler.component.CombinedQueryComponentRealExitPremiseTest, is not on the merge-base; the runner greps the merge-base tree as step 1 and records the result in the log. The class is the scratch probe written for gates/combinedquerycomponent-real-exit-premise.md and held uncommitted in the vm1 worktree ~/workspace/solr-worktrees/cqc-real-exit. This run uses a score-only variant of that same fixture (CombinedQueryComponentScoreOnlyExitPremiseTest, uncommitted in the job worktree; source at ~/workspace/tools/probes/), which is what "the branch's test fixture" is read to mean. No test is added to any branch, matching the job type.
