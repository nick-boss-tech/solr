# Addendum report: PR 5299 review fixes gate round 1, JDK 25 helper leg (phone-agent)

Job: `gates/pr-5299-review-fixes-round-1.md`. Base report: `reports/pr-5299-review-fixes-round-1-phone-agent.md`. Claim: `claims/pr-5299-review-fixes-round-1-phone-agent.md`.
Candidate unchanged: `f342e160417d64ed2dced989052b29a76d31996e` (tree `42e57845f4d23565944ae74b987955028465b9ba`), verified in the gate clone before the run.

## What changed on this host

The partial execution recorded every JDK 25 leg as capability-blocked because no JDK 25 was installed. At Nick's question on 2026-11-05, that premise was tested: Temurin 25 downloads fine here via curl through the egress proxy (Adoptium redirects to GitHub release assets). Temurin 25.0.5+8 (2026-10-20 LTS) is now installed at `~/workspace/tools/jdk25/jdk-25.0.5+8` and `bin/java -version` verifies. "No JDK 25" was an installed-state fact, not a host limit.

## Leg executed: proof A leg 1, JDK 25 half

- Window: 2026-11-05T21:28:46Z to 21:28:47Z, under the host test-queue lock.
- Command shape: BATS 1.8.2 (`~/workspace/tools/bats-core-1.8.2/bin/bats --report-formatter junit --formatter tap test_bats_assertions.bats`) in `solr/packaging/test`, with `JAVA_HOME` and `SOLR_JAVA_HOME` pinned to the Temurin 25.0.5+8 install and `BATS_LIB_PREFIX` unchanged from the JDK 21 leg.
- Result: exit 0. TAP: 14 ok, 0 not ok, no skips. Fresh JUnit XML totals: 14 tests, 0 failures, 0 errors, 0 skipped.
- Artifacts: `~/workspace/tools/gate-5299/run-jdk25.sh`, `jdk25.log`, `java25-version.txt`, `helper-normal-jdk25.tap`, `junit-h25/report.xml`.

Proof A leg 1 is now complete on both runtimes (JDK 21 leg in the base report, JDK 25 leg here), at the same candidate head.

## What remains open (unchanged blockers)

- Proof B Solr-starting matrix on both runtimes, proof A legs 4 to 5 real TLS cases including the matched/mismatched client control, and the proof D server controls: this VM runs as root and `bin/solr` refuses root. The blocker is the root limit, not JDK availability.
- Proof C Docker legs and the report guard's real-report execution: no Docker binary or daemon on this host.
- The GREEN receipt path stays unwritten. State for the remaining legs: Implemented, Proof Pending.

## Correction to the partial receipt's PR body draft

The draft Limits line "the JDK 25 legs have not run" is stale for the helper suite. When Nick gives the go for the PR #5299 body update, the Proof section should say the helper suite passes 14 of 14 in both JDK 21 and JDK 25 environments, and Limits should name only the Solr-starting matrix and the Docker execution as unproven.
