# Claim: pool-review-2026-10-16-code-round-2

- Claimant host: vm2, matching a file in `hosts/` (`hosts/vm2.md`).
- Status: DONE: 2026-10-18T06:30Z, deliverables under `reports/2026-10-16-pr-batch/`: `code-round-2-index.md`, `SOLR-6831-round-2-check.md`, `SOLR-9124-round-2-check.md`, `SOLR-18541-check.md`, `SOLR-18010-check.md`, plus patches `SOLR-18541-shortlist-test.patch` and `SOLR-18010-scratch-test.patch`.
- Job or assignment: `assignments/pool-review-2026-10-16-code-round-2.md`.
- Slice: the whole assignment, items 1 to 4. Prior item claims `claims/pool-review-2026-10-16-code-round-2-item-1-6831.md` and `claims/pool-review-2026-10-16-code-round-2-item-3.md` are STALE and are not resumed.
- Capability tags used: implementation, gate.
- Started: 2026-10-18T04:02:20Z (UTC).

Item 1 (SOLR-6831): `solr-6831-submit` has moved. Assignment head `4afed04987b2343af58261b83c4b49fda0af45c0`, current remote tip `5c1124bdef95f4fa1d5bbbb6cf2dfd495111f528` (checked 2026-10-18T04:02Z). Stopped and recorded, per the item's rule.

Heartbeat: 2026-10-18T05:27:00Z

Result: 2026-10-18T04:24:08Z: item 1 (SOLR-6831) head moved. `solr-6831-submit` is now 5c1124bdef95f4fa1d5bbbb6cf2dfd495111f528, not 4afed04987b2343af58261b83c4b49fda0af45c0. Stopped and recorded per the item's rule. No build run.

Result: 2026-10-18T04:24:08Z: items 2 to 4 not run. This host cannot run Gradle: no JDK on PATH and no JAVA_HOME (`/usr/lib/jvm` absent), and `~/.pool-host` is missing (HOME=/home/claude), so `pool/bin/sweep-run.sh` exits 3. The lock `/workspace/tools/.gradle-vm2.lock` was free. Fork heads checked unchanged: solr-9124-submit 3dd985b93ac0e26a3850c338765ba1235bd811d4, combinedquerycomponent-merge-totals 81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d, solr-18010-submit 571307400b6917ce458db989f29febb5cff8a600.

Handoff: 2026-10-18T04:24:08Z: assignment set to HANDED for vm1 with items 2 to 4 open. Item 1 stays stopped at the moved head for the main agent.

Correction: 2026-10-18T04:54:00Z: the "no JDK" reasons above were wrong. A JDK exists at `/workspace/jdk/jdk-21.0.12.1+1` (Temurin 21.0.12.1, runs). The handoff is reverted and this claim is live again. Still blocked: `~/.pool-host` is missing, so `pool/bin/sweep-run.sh` exits 3. No build has run. Item 1 stays stopped at the moved head.

Setup: 2026-10-18T05:27:00Z: `~/.pool-host` was missing, so this host wrote `vm2` to it, matching `/workspace/.pool-host` and AGENTS.md. `~/.pool-env` now sets JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1 and GRADLE_USER_HOME=/workspace/gradle-home. Fork heads rechecked: solr-6831-submit 5c1124bd (moved, item 1 stays stopped), solr-9124-submit 3dd985b9, combinedquerycomponent-merge-totals 81a88e40, solr-18010-submit 57130740 (all as assignment or claim). Next: item 2.

Result: 2026-10-18T06:00Z: item 2 (SOLR-9124, `solr-9124-submit` at 3dd985b93a, seed 9124C0FFEE9124). Gap (a): TestDistributedGrouping 2 of 2, BasicDistributedZkTest 1 of 1, DistribCursorPagingTest 1 of 1 green at head, rc=0 (log `/workspace/gates/logs/vm2/9124/head-distrib3-fixed.log`). Two earlier invocations did not run these classes: a `./gradlew` task argument, and wrong package names for two classes. Logs kept as `head-distrib3.log` and `head-distrib3-fixed` predecessor. Gap (b): stats baseline at head green, 5 of 5 (`stats-head.log`). In scratch copy `vm2-9124-scratch`, no commit, the within-group clause removed: TestExactStatsCache.test fails at TestBaseStatsCache.java:117 (checkWithinGroupOrder), `expected:<[g0=4, g1=3]> but was:<[g0=4, g1=5]>`. Clause is load-bearing and the PR test catches its removal. Clause count: one match, no escalation. Output: `reports/2026-10-16-pr-batch/SOLR-9124-round-2-check.md`. Next: item 3.

Result: 2026-10-18T06:16Z: item 3 (SOLR-18541, `combinedquerycomponent-merge-totals` at 81a88e40dd, seed 18541C0FFEE18541). Whole class `CombinedQueryComponentMergeTotalsTest` 10 of 10 passed in the first scratch run (score-sort variant), log `head-mergetotals.log`. Realistic shape (int sort, one entry in response_per_query, sort_values_0 present, partial): `shortPerQueryListThrowsBeforeSkip` 1 of 1 passed, log `head-shortlist-intsort.log`, with IndexOutOfBoundsException from `CombinedQueryComponent.java:342` asserted. Not conclusive end to end: no timeAllowed run; reach from a real shard is by code reading (SearchHandler.java:507, QueryComponent.java:571 and 848). No production change; fix escalated. Output: `SOLR-18541-check.md`.

Result: 2026-10-18T06:26Z: item 4 (SOLR-18010, `solr-18010-submit` at 571307400b, seed 18010C0FFEE18010). Scratch `testScratch*` 3 of 3 passed, log `head-scratch-modes.log`. Read-only SOLR_HOME: persistConf fails with AccessDeniedException on the temp file, security.json unchanged. New file mode rw-r--r-- (0644, umask 0022). Existing file mode kept. Escalated to Nick. Output: `SOLR-18010-check.md`.

Result: 2026-10-18T06:27Z: item 1 (SOLR-6831) stopped; fork tip still `5c1124bdef95`, not run.
