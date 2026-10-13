# Gate job: SOLR-16630 re-gate after the latch change

- Branch: solr-16630-submit. Head: `a31707acfdb7f0f83eb47e17435c1fe4ccf2ffda` (fork tip). Previous gated head: `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7`.
- Status: CLAIMED by vm2 (gate backlog coordinator), 2026-10-13T14:37Z; claim claims/pool-vm2-gate-backlog-16630.md. Queued as /workspace/gates/g16630-latches-gate.sh.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: full focused gate at the new head. The change is one file in the test: one `Thread.sleep` in `testNRTRestart` is replaced by a bounded wait on the existing `addDone` latch (`addDone.await(2, MINUTES)`).
- Why: the maintainer asked for more latches in place of sleeps. The head moved, so the earlier gate no longer covers it.

## Doubts the gate must check

1. PULL now stops sooner than before, so it loses the 1 to 10 second floor that gave the coordinator time to see NRT as ACTIVE. If `testNRTRestart` reports "initial error time threshold exceeded", revert that hunk and record it.
2. The loops at the NRT query, the add retry, and the final poll remain unbounded. Record any spin or hang.
3. `pullServiceTimeMs` now feeds only the log line.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-16630-submit` must equal `a31707acfdb7f0f83eb47e17435c1fe4ccf2ffda`. If not, stop and record the new tip.
2. Diff scope: `git diff --stat 9c77a4db09d7a890f62dc9a9ffb2bf5613245da7 a31707acfdb7f0f83eb47e17435c1fe4ccf2ffda` must list only `solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java`.
3. Tidy, with the GATE INVALID post-tidy guard.
4. Spotless check on `:solr:core`.
5. Focused run: `org.apache.solr.search.TestCoordinatorRole` with fresh JUnit XML. Record the counts, the seed and the date, and the result of `testNRTRestart` in particular.
6. Module check for `:solr:core`.
7. On green, refresh the PR's receipt record with the new head and counts. Mark this job DONE.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed (one test file)
- [ ] tidy clean with the guard
- [ ] spotless check on :solr:core
- [ ] focused run with JUnit XML; testNRTRestart result recorded
- [ ] doubts 1 to 3 recorded
- [ ] receipt refreshed at the new head; job DONE
