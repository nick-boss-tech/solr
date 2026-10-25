# Assignment: RCA for the three stale red PR checks (round 1)

Status: OPEN
Tags: premise-run, records, gate

A census of the owner's 97 open PRs (2026-10-24) found 94 with all checks green and exactly three red, all Crave test failures, all completed on 2026-10-10 and never classified to ground: PR #5083 (SOLR-11483, head `4431a250f6`, job 114192700428), PR #5081 (SOLR-7504, head `2fe06bfd91`, job 114192610971; a 2026-10-10 note called this one infrastructure, a Jetty port collision, without a log citation), and PR #5069 (SOLR-3657, head `14edaca577`, job 114192758248). One claim covers the round; each PR is a unit inside the report.

## Work per PR

1. Fetch the full job log through the GitHub API (`GET /repos/apache/solr/actions/jobs/<job_id>/logs`, retry once after a timeout) and extract the failing test classes and methods and the run's seed from the log itself.
2. Classify against the PR's diff: (a) a failing test the PR touches or plausibly affects; (b) an unrelated failure with a recorded seed (seed-driven ordering or a known flake family); (c) an infrastructure signature (port collision, runner or JVM death, timeout in setup or teardown). "Environment noise" is not a classification; each verdict cites the log lines it rests on.
3. For (a): file the fix as its own implementation assignment (the owner's pushes to his own open PRs are authorized; the fix gates locally before any push). For (b): run or queue a settling run at the recorded seed on the branch head and on main, and record which side fails. For (c): record the signature and the cheapest honest re-run path.

## Deliverables

- `reports/ci-red-prs-rca-round-1.md`: per-PR verdict with log citations, the seed where one exists, and the follow-on assignment filed for any (a).
- The three PRs' receipts or the board's PR records updated with a one-line status each, so the next census does not rediscover them as unknown reds.
