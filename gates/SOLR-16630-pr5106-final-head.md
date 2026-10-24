# Gate job: PR 5106 final head (SOLR-16630, TestCoordinatorRole.testNRTRestart)

Status: OPEN
Tags: gate

- Branch: `solr-16630-submit` on the fork (nick-boss-tech/solr).
- Head to gate: `a02cc7ba6b90` (the head carrying Tomas Lobbe's fix commit; the existing receipt covers only the earlier head `c9caf1e4b8`).
- Base: origin/main at gate time.
- Module: `:solr:core`.
- Focused class: `org.apache.solr.cloud.TestCoordinatorRole`, with emphasis on `testNRTRestart`. Because the claim is that the test no longer fails by waiting for the first primary to time out (about 7 seconds at this head against about 30 seconds on main), run the class 10 times at the head and record the wall time and result of each run, plus one run at the base for the timing comparison.
- Standard legs: tidy (with the GATE INVALID guard), Error Prone compile (`-Pvalidation.errorprone=true`), and `:solr:core:check -x test`.
- Deliverable: extend `receipts/SOLR-16630.md` with a section for this head (the file is the single-writer receipt exception), citing the gate log path and the fresh JUnit XML for the focused runs.

Why: the owner's acceptance reply to Tomas on PR 5106 is drafted and held until a gate at this exact head is green. This job is the unblock.
