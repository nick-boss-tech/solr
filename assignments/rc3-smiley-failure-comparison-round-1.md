# Assignment: RC3 smoke failures, compare with Smiley's two data points (round 1)

Status: OPEN
Tags: sweep, records
Intended host: vm5 (it holds the smoke loop's data). One claim covers this whole round.

## Why

David Smiley's vote post on the Solr 10.1.0 RC3 thread (2026-10-24 21:21 UTC) reports two failures from his own smoke runs:

1. On the original JDK 21 release, a test failed that seemed related to metrics; he calls it deja-vu with another failure with the same root cause. After updating to JDK 21.0.12, his run succeeded (51:48).
2. On JDK 25, the BATS test `test_security_manager` allowPaths case failed every time. He has already fixed that one himself in apache/solr PR 5225 (one test file, accepts either the Security Manager message or Solr's own allowPaths rejection on Java 24+). Nothing is needed from the factory on that item beyond the comparison below.

The owner's own RC3 smoke loop ran on vm5 (claim `claims/rc3-smoke-loop-round-1.md`) and produced failures the loop's final report has not yet recorded: run-001 failed with 6 test failures out of 5,898 in solr:core, and runs 002 and 003 also failed. The owner stood the loop down on 2026-10-24. Whether any of vm5's failures match Smiley's metrics failure is the follow-up question on the vote thread, and it cannot be answered until vm5's failing test names are on the board.

## Work

1. From `/workspace/smoke-rc3/runs/results.tsv` and the run logs on vm5, extract every failing test in every completed run (class and method, run number, seed if recorded). State the exact JDK version the loop ran (the full `java -version` string of the toolchain used).
2. File the loop's missing final report as `reports/rc3-smoke-loop-round-1.md`: runs attempted and completed, the failure list from step 1, and the stand-down. This closes the loop's own record.
3. Compare the failure list against Smiley's two data points: (a) is any vm5 failure a metrics-related test, and does vm5's JDK version fit the pattern of failing before 21.0.12 and passing after; (b) did any vm5 run exercise the BATS security manager test at all (the loop ran one JDK, so an absence is expected and is itself the answer for that item).
4. Deliverable: `reports/rc3-smiley-failure-comparison-round-1.md` with a per-item verdict (match, no match, cannot tell and why), the evidence lines behind each verdict, and no conclusions beyond what the logs show.

## Rules

Read-only against the release artifacts and the loop's outputs. No re-runs of the smoke tester in this round. No email, no post, no vote: the owner decides on any follow-up text once the comparison is on the board.
