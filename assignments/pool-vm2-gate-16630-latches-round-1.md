# Pool assignment: gate the SOLR-16630 latch change for the open PR (vm2)

Capability tags: `gate`. Intended host: vm2 (Linux). Windows never claims build work. Staffing: one job, one host. Claim path: `claims/pool-vm2-gate-backlog-16630.md`, under the WORKFLOW.md rules.

## Why this is assigned

The maintainer on the open PR for SOLR-16630 (apache/solr, the TestCoordinatorRole test) asked for more latches in place of sleeps. The fork branch has the change at `a31707acfdb7f0f83eb47e17435c1fe4ccf2ffda`. Nick has directed that vm2 gate it, and that the PR be updated after this assignment is pushed. The gate must run against that head and nothing else.

## The job

The full spec is `gates/SOLR-16630-latches-regate.md`. Follow its steps. In short:

1. Confirm the fork tip is `a31707acfdb7f0f83eb47e17435c1fe4ccf2ffda`. If it is not, stop and record the new tip.
2. Diff scope against `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7`: one test file, `solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java`.
3. Tidy with the GATE INVALID guard, Spotless check on `:solr:core`, and the focused run of `org.apache.solr.search.TestCoordinatorRole` with fresh JUnit XML.
4. Record the counts, the seed, the date, and the result of `testNRTRestart` specifically.
5. Module check on `:solr:core`.

## Doubts the gate must answer

The change makes PULL stop sooner than before, and it leaves unbounded loops in the test. Record these in the gate file:

- If `testNRTRestart` reports "initial error time threshold exceeded", the PULL change is the cause. Revert that hunk on a branch commit, re-run, and record both results. Do not push a revert without recording it first.
- Record any spin or hang in the unbounded loops (the NRT query loop, the add retry loop, the final poll loop).

## Deliverable

- The gate result, with counts, seed and date, in `receipts/SOLR-16630.md` at the head.
- The gate file marked DONE, and the claim marked DONE.
- A one-line statement in the gate file of whether the PR body's Proof can be refreshed from this result.

## Limits

- No PR is opened, closed, marked ready or commented on by this assignment.
- No force-push. A revert, if needed, is a plain fast-forward commit on the same branch.
- No Gradle on any host other than the gate host under the normal rules.
