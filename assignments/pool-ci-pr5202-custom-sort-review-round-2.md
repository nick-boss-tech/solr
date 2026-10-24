# Assignment: pool-ci-pr5202-custom-sort-review-round-2 (second review of the PR 5202 CI leak report)

- Status: DONE (reports/ci-pr5202-custom-sort-review-round-2.md; phone agent, 2026-10-23). Requested by vm4 on behalf of Nick Shanin.
- Builds on: `assignments/pool-ci-pr5202-custom-sort-leak.md` (DONE) and `reports/ci-pr5202-custom-sort-leak.md`. Read both first.
- Scope: review the conclusion that PR #5202 does not cause the `DistributedQueryComponentCustomSortTest` leak report. Challenge each piece of evidence. Do not change production code, do not push to any PR branch, do not comment on any PR, do not open a PR or JIRA, do not rerun CI.

## Background

- CI run https://github.com/apache/solr/actions/runs/39903819776 (attempt 2), job 120373474924, merge of PR head `7e0251ee02` into base `79b23df58d`. The job reports one class-level tracker failure: `ObjectTracker found 2 object(s) that were not released: [SolrMetricsContext, HttpJettySolrClient]`, followed by a Gradle reporter crash that appears as a second failure. Seed `FFF5A3C3AAC69084`. The full run had 6119 tests.
- The PR changes only `HttpSolrCall.java`, `V2HttpCall.java`, and tests. The leaked objects are created during this class's Jetty startup, through `HttpShardHandlerFactory.initializeMetrics` and `HttpJettySolrClient` construction.
- A single-class run of the test passed on the PR head (vm4, `/workspace/gates/logs/vm4/ci-5202/local-head-run.log`) and on the base (pool, `reports/ci-pr5202-custom-sort-leak.md`).
- The round-1 report also cites upstream `main` leak reports from mailing-list build notifications (Jenkins, not GitHub Actions) for `TestUserManagedReplicationWithAuth`, `PeerSyncTest`, `TestSolrQueryParser`, and `TestDistributedSearch`.
- Other open work on the same leak path: `assignments/pool-teardown-release-path-round-2.md` and `reports/teardown-release-path-review-round-3.md`. A focused candidate test run on vm4 is pending publication.

## Points to challenge

1. **The single-class passes.** The CI failure happened inside a full-suite JVM, where the tracker is shared by every class in that worker. A pass of one class with the same seed does not reproduce that sharing. Check whether the objects in CI's first report were created during this class's own startup (the creation stacks in `job.log` should show it). Say whether an earlier class in the same JVM could have created them.
2. **The base comparison.** The base run used the exact base SHA, but CI tested a merge ref. Confirm the merge commit of run 39903819776 (the job log names it), and whether the base and PR contents differ in any file that the test or its Jetty startup loads.
3. **The mailing-list evidence.** The Jenkins notifications are secondhand. Check whether each cited build actually failed with the cited tracker message, by reading the notification text itself. Note any entry you cannot confirm. Note also that the Jenkins job numbers are not GitHub run IDs.
4. **Intermittency.** One pass per seed says nothing about how often the leak happens. Decide whether a bounded repeat run on the PR head would answer the question, and what the cost is on the shared lock. Propose it; do not run it.

## Steps

1. Fetch the CI job log for run 39903819776 from GitHub (`/actions/jobs/120373474924/logs`). Record the first tracker report and its creation stacks. Record the merge ref.
2. For points 1 and 2, write one paragraph each with the line numbers you rely on.
3. For point 3, check each cited mailing-list notification. Keep a table: build, date, claimed tracker entries, verified yes or no, and why.
4. For point 4, write the proposed command and the expected cost. Ask the main agent before running anything heavier than the one-class command already run.
5. Do not run the full `:solr:core:test` suite without the main agent's OK. A full run on `main` took 1 hour 29 minutes.

## Deliverable

- `reports/ci-pr5202-custom-sort-review-round-2.md`: a verdict on whether PR #5202 is involved (involved, not involved, or not established), with the evidence for each of the four points.
- Status line above set to DONE or HANDED BACK, in the same push as the report.

## Owner decisions (not for this assignment)

- Whether to rerun CI, run the full core suite, comment on PR #5202, or ask the author.
- Whether the main agent wants a repeat-run sweep on the PR head.
