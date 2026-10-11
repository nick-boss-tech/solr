# Retire candidates review round

Assignment: `assignments/pool-retire-candidates-review-round.md`. Claim: `claims/pool-retire-candidates-review-round.md`. Coordinator: the Windows review agent, per `material/coordinator-handoff.md`. Nick's rule (2026-10-11): each retire candidate gets one review round; retire unless the round finds something gainable.

Part reports, with the evidence for each verdict:

- `reports/retire-candidates-review-round-rr1.md`: SOLR-17722, 17882, 17372, 12743, 17825.
- `reports/retire-candidates-review-round-rr2.md`: SOLR-10198, 18119-submit, 18277, 9759.

Read-only throughout. Nothing was deleted, pushed, closed, edited or built. No receipt was edited.

## Verdict counts

Nine items: **7 RETIRE CLEAN, 2 SALVAGE FIRST.**

| Item | Verdict | Part report |
|---|---|---|
| SOLR-17722 | RETIRE CLEAN | rr1 |
| SOLR-17882 | RETIRE CLEAN | rr1 |
| SOLR-17372 | SALVAGE FIRST | rr1 |
| SOLR-12743 | RETIRE CLEAN | rr1 |
| SOLR-17825 | RETIRE CLEAN | rr1 |
| SOLR-10198 (branch) | RETIRE CLEAN | rr2 |
| SOLR-18119-submit (PR branch) | RETIRE CLEAN, holds only while the shipped pair lands or is accepted | rr2 |
| SOLR-18277 (three branches) | SALVAGE FIRST (a receipt record, not code) | rr2 |
| SOLR-9759 | RETIRE CLEAN | rr2 |

## Salvage items

1. **SOLR-17372, salvage first.** The `StatsComponentTest` tolerance hunk (`3257831180f`) is the ticket's own suggested fix. The Jira reproducing seed was never run on base or on main, per the record. The salvage is one premise run: leg A on `upstream/main` without the hunk, leg B on the branch head with it. The run is specified, not started. CI run `37639175484` ran with the hunk included, so it does not test the premise.
2. **SOLR-18277, salvage first.** There is no test or fix gap. Fork runs `37237885304`, `37238381991` and `37238383951` show `ShardSplitTest` passing on base production, so it does not pin the fix. The receipt records only an inconclusive result. The salvage is a documentation-only receipt record. The `fork-test-runner.yml` workflow exists only on `ci/solr-18277` and `ci/solr-18277-proof`.

## Retire-clean items, with what was checked

- **SOLR-17722, SOLR-17882, SOLR-12743, SOLR-17825.** Each branch's live tip matches its receipt. The branch diff against current main holds nothing the record needs. SOLR-17825 carries one unresolved point: transitive `beanutils` absence was not settled without a build.
- **SOLR-10198.** The ticket merged into apache/solr main as squash commit `e97c2a0884cb`. The five branch paths are identical to main. There is no straggler commit.
- **SOLR-18119-submit.** The only content not on main is the `python3` hunk in `changes-to-html.gradle`. The shipped `solr-18119-jvm` pair removes that route, so the branch is retire-clean only while the pair lands or is accepted.
- **SOLR-9759.** Main at `c6f910d3ccf` carries the same files. The documented address-bar residual is already on main. The branch test's positive assertion is not worth a job.

## Decisions for Nick and the main side

- **Branch deletions and PR closings** stay with Nick. None was done.
- **PR #4999** (the python3 route): its review decision is open. The retire call on SOLR-18119-submit waits on it.
- **SOLR-18277 Jira status.** Not settled by the reading. The record says Fixed; confirm it before the branches go.
- **PR #5034 head ancestry.** Closed unmerged on 2026-10-09; the object is not local, so ancestry was not checked.
- **Related finding.** The SOLR-18368 reading (`reports/solr-18368-still-an-issue.md`) returned STILL AN ISSUE on the same review day: all three ref-guide defects remain on current main. Its consequence (a PR or a new ticket) is Nick's call; it is not part of this retire round.

## Not done

- No branch deletion or push, no PR close, no receipt edit, no build, test or gate.
- `g4959-harden2.log` is not in the worktree, so one SOLR-18277 point is unchecked.
- The CI-run and goal-file references in the receipts were read as recorded, not re-fetched.
