# Receipt (partial, addendum): PR 5299 review fixes gate round 1, JDK 25 helper leg

Status: **INCOMPLETE, not GREEN.** Addendum to `receipts/pr-5299-review-fixes-round-1-phone-agent-partial.md`. The GREEN receipt at `receipts/pr-5299-review-fixes-round-1.md` stays unwritten.
Job: `gates/pr-5299-review-fixes-round-1.md`. Addendum report: `reports/pr-5299-review-fixes-round-1-phone-agent-jdk25-addendum.md`. Claim: `claims/pr-5299-review-fixes-round-1-phone-agent.md`.
Candidate `f342e160417d64ed2dced989052b29a76d31996e` (tree `42e57845f4d23565944ae74b987955028465b9ba`), verified in the gate clone before the run.

Newly executed and passing at this head: proof A leg 1 on JDK 25. Temurin 25.0.5+8 was installed on this host on 2026-11-05 (`~/workspace/tools/jdk25/jdk-25.0.5+8`); the earlier "no JDK 25 installed" blocker was an installed-state fact, and the download works through the egress proxy. `test_bats_assertions.bats` ran normally with `JAVA_HOME`/`SOLR_JAVA_HOME` pinned to JDK 25: exit 0, TAP 14 ok and 0 not ok, fresh JUnit XML totals 14 tests, 0 failures, 0 errors, 0 skipped (2026-11-05T21:28:46Z to 21:28:47Z). Proof A leg 1 is now complete on both runtimes.

Still not executed, blockers unchanged: the proof B Solr-starting matrix on either runtime, the proof A real TLS cases and matched/mismatched control, and the proof D server controls (this VM runs as root; `bin/solr` refuses root); the proof C Docker legs and the report guard's real-report execution (no Docker). The partial receipt's PR body draft needs one correction when applied: the helper suite now passes on both JDK 21 and JDK 25, and only the matrix and Docker execution remain unproven. Artifacts: `~/workspace/tools/gate-5299/` (jdk25 files).
