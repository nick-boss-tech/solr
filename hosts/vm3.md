# Host: vm3 (tracking label assigned by Nick)

- Environment: current shared Sandbox computer; this is a tracking name, not a separate purchased or dedicated VM.
- Capabilities demonstrated: Linux, JDK 21.0.12.1, Gradle 9.7.0, Node 24.19.0, npm 11.17.0, BATS 1.10.0 (installed from Ubuntu package; `/usr/bin/bats`); changelog validation and Solr reference-guide build.
- Build protocol: run one gate at a time; check `df -i /` before every build and again after; record available space and inode usage. Preserve shared source worktrees, `.git` metadata, caches, and logs. Remove only generated build output or this task's isolated clean checkout after recording.
- SOLR-6430 docs gate: packaged head `c0c6e5dd21c4eee60257db6b85b2fec7f01489b7`; changelog lint and local guide build passed. The gate and claim are attributed to vm3. Logs remain under `/workspace/gates/logs/vm1/`, the literal directory used during that run.
- SOLR-7119 docs gate: prior completed build peak was recorded as +35,345 inodes from its pre-build snapshot, with post-cleanup net +13,636.
- SOLR-11700 docs gate: completed GREEN at packaged head `8a945757e6687253f61f7dc1e46130fa722c3f14`. Changelog lint and local reference-guide build passed; the rendered filters page was checked. No Java tests were run. The gate and receipt disclose the unintended build-infrastructure compilation during an earlier supposed dry-run and the mismatch between Lucene API Javadoc wording and implementation/tests.
- SOLR-11700 resource snapshot before the required site build, 2026-10-11T05:10:41Z: 54 GB total, 27 GB available; 620,243 of 837,408 inodes used (74.07%; 217,165 free). After site build, 2026-10-11T05:12:25Z: 26 GB available; 655,586 inodes used (78.29%; 181,822 free). After final lint, 2026-10-11T05:13:24Z: 26 GB available; 655,589 inodes used (78.29%; 181,819 free). The build peak was not continuously sampled.
- SOLR-11700 logs are retained under `/workspace/gates/logs/vm3/`.

- SOLR-11700 post-record cleanup, 2026-10-11T05:16:26Z: removed only 30 ignored Gradle `build` output directories from the isolated task checkout; preserved source, `.git`, `.gradle` and npm caches, shared checkouts, and logs. Root filesystem: 54 GB total, 26 GB available (52% used); 633,968 of 837,408 inodes used (76% reported), 203,440 free. The rendered page was checked before its generated output was removed.

- SOLR-10390 resume heartbeat, 2026-10-11T10:06:48Z: same vm3 holder resumed after the monitoring-shell wait limit; remote claim remained vm3 and the Gradle build log remained stable at 15,029 bytes (mtime 2026-10-11T08:10:54Z). The project BATS runner is 1.8.2 with its helper libraries present; the host package runner remains 1.10.0. Recovery snapshot: 22 GB available, 59,803 inodes free (93% reported use). No Gradle client or worker was active.

- SOLR-10390 BATS gate prep heartbeat, 2026-10-11T10:19:18Z: host `/usr/bin/lsof` is installed; the task checkout uses verified lsof-free PATH `/workspace/gates/solr-10390-build/solr/packaging/build/bats-no-lsof-path`. Host BATS is 1.10.0; the packaged project runner is 1.8.2. Pre-selection snapshot: 54 GB root filesystem, 22 GB available, 65,056 free inodes (93% reported use); only sleeping Gradle daemon PID 221714, 1 CPU tick in 4 seconds, no Gradle client or worker. Removed 33 verified ignored/untracked task build-output directories outside the packaging build while retaining distribution artifacts and logs.

- SOLR-10390 proof heartbeat, 2026-10-11T10:23:02Z: expected base fail-before confirmed from the `Please install lsof` assertion content and fresh JUnit report; no test rerun. After the run: 22 GB available and 64,999 inodes free (93% reported use), no Solr/test process and no Gradle client/worker; only sleeping daemon PID 221714 remained. The extracted distribution was restored to packaged-head `bin/solr` and source checkout stayed clean. Evidence: `/workspace/gates/logs/vm3/10390-proof.log` and `10390-proof-headroom.log`.

- SOLR-10390 gate completion heartbeat, 2026-10-11T10:26:00Z: GATE GREEN at `5a86f908052f031b6ce0f0004c0bdf3e825a12c5`. The filtered head BATS test passed one case in 11.764 seconds, exit 0; JUnit has no failure/error and no teardown timeout recurred. Post-run resources: 22 GB available, 64,957 inodes free (93% reported use); no Solr/test process or Gradle client/worker remained. Receipt and gate evidence are recorded on `coordination`.

- SOLR-17708 claim heartbeat, 2026-10-11T10:29:41Z (UTC): vm3 claimed the live-unclaimed top-up gate at branch head `10a7fa07a79ac4b4b52a83d307ee65349d015a23`. Scope is only the gate-prescribed changelog title correction and subsequent top-up gate; no build or test has started.

- SOLR-17708 packaging heartbeat, 2026-10-11T10:32:31Z (UTC): the exact single-file changelog title correction was committed as `2eceb54730373475e9adfe55bfe555744fb3724a` and fast-forward pushed; the live fork tip and merge-base match the recorded task checkout. No build or test has started; next is YAML changelog validation.

- SOLR-17708 changelog heartbeat, 2026-10-11T10:33:58Z (UTC): repository validator passed all 315 fragments at `2eceb54730373475e9adfe55bfe555744fb3724a`, including the corrected title. Pre/post parser snapshots: 21 GB available; 54,741 then 54,740 free inodes; only sleeping Gradle daemon PID 221714, no client or worker. Evidence is in `/workspace/gates/logs/vm3/17708-changelog*.log`.

- SOLR-17708 tidy heartbeat, 2026-10-11T10:38:31Z (UTC): root `tidy` passed in 1m24s with 62 tasks; no tests ran and task checkout remained clean. Disk space stayed at 21 GB available; free inodes decreased from 54,721 to 52,130. Afterward two Gradle daemons were sleeping with periodic health checks only; no client or worker was active. Logs: `/workspace/gates/logs/vm3/17708-tidy.log` and `17708-tidy-headroom.log`.

- SOLR-17708 Error Prone heartbeat, 2026-10-11T10:45:22Z (UTC): `:solr:core:compileJava` and `compileTestJava` passed with `-Pvalidation.errorprone=true` in 4m39s; 18 actionable tasks, no test task executed. Disk: 21 GB available; free inodes 52,113 before and 44,793 after. Worktree clean; post-run only sleeping daemons and worker JVMs remained. Evidence: `/workspace/gates/logs/vm3/17708-error-prone-compile*.log`. Next: merge-base proof.

- SOLR-17708 proof heartbeat, 2026-10-11T10:50:55Z (UTC): merge-base `9b3a84b1c460981eab09d8ffaef776acc4a184f8` with only the branch test class copied in produced 2 tests, 1 expected failure (`expected 1 authorization but was 2`), and a passing denial guard (0 errors/skips). Fresh report/output copied to `/workspace/gates/logs/vm3/17708-proof-junit.xml` and `17708-proof-test-output.txt`; full run and resource logs retained. Disk: 21 GB available; free inodes 34,700 before and 24,207 after. Next: focused head tests.

- SOLR-17708 focused head-test heartbeat, 2026-10-11T10:55:00Z (UTC): at packaged head `2eceb54730373475e9adfe55bfe555744fb3724a`, the three prescribed classes passed 12/12 with seed `17708C0FFEE17708` (JAX-RS 2, Basic Auth 1, Audit Logger 9; 0 failures/errors/skips). The single Gradle test task passed in 1m40s; fresh XML and logs retained under `/workspace/gates/logs/vm3/17708-focused-head*`. Disk: 21 GB available; free inodes 32,437 before and 31,715 after; only sleeping daemons/workers remained, with no active client or test process. Next: `:solr:core:check -x test -Pvalidation.errorprone=true`.

- SOLR-17708 full top-up gate completion heartbeat, 2026-10-11T11:05:04Z (UTC): GATE GREEN/DONE at live topic head `2eceb54730373475e9adfe55bfe555744fb3724a`; receipt `/workspace/gates/solr-11700-coordination/receipts/SOLR-17708.md`. Changelog 315/315, tidy passed, Error Prone compile passed, base proof established the expected one assertion failure with the denial guard passing, focused head classes passed 12/12, and `:solr:core:check -x test -Pvalidation.errorprone=true` succeeded in 6m14s with the test task excluded. Module-check resources: 21 GB available, 31,696 free inodes before and 31,585 after (31,583 after idle verification); only stable sleeping Gradle daemons/worker daemons remained. Source checkout clean; no PR was opened or edited.

- Queue pause and inode snapshot, 2026-10-11T14:10:34Z (UTC): vm3 has no active build/test process and no active claim. SOLR-8628 was paused by owner with its branch-head premise test pending, then its claim was released by owner direction; SOLR-5262 remains environment-blocked and its claim was also released. Root filesystem: 21 GB available and 23,129 free inodes (98% used). No cleanup has yet been performed for this inode recovery request.

## Host status, 2026-10-11 ~08:38 MDT

- Status: DOWN, reported by Nick on 2026-10-11 at about 08:38 MDT.
- No new claims on vm3 until Nick says the host is back.
- The SOLR-8628 premise claim and the SOLR-5262 gate claim were released by vm3 at 14:10Z, before the outage, and are claimable by the other hosts.
- SOLR-8628 premise state at release: parts 1 and 2 confirmed (the write.lock-only crash state is reachable); part 3, the test leg, is unfinished.
- SOLR-5262 state at release: still blocked on the develocity plugin resolution failure during tidy; no tests ran.

## Host status, 2026-10-17

- Status: UP. Nick directed on 2026-10-17 that this VM is vm3 and may claim work again. The 2026-10-11 DOWN note above is superseded.
- Fresh VM: no prior Gradle caches or JDK on this host. JDK 21.0.12.1 installed at `/workspace/jdk/jdk-21.0.12.1+1`.
- Root filesystem `/`: 7.8G, about 4.6G free at 2026-10-17T04:32Z; 4 CPUs, 7 GB RAM. Gradle user home must point under `/workspace`.
- Heartbeat: 2026-10-17T04:32:43Z, claim `claims/pool-vm3-gate-backlog-16290.md` (SOLR-16290 fix gate).

- 2026-10-17T05:29:51Z: heartbeat on claims/pool-vm3-gate-backlog-16290.md (SOLR-16290 fix gate). Root 4.6G free of 7.8G, 481403 inodes free. Gradle warm-up in progress.
- 2026-10-17T18:18Z: state reconciled. SOLR-16290 DONE (d15e7700fd). SOLR-6831 withhold round 1 DONE (5c1124bdef), proof leg recorded, PR 5134 body updated. SOLR-18551 10x row released by vm3 (completed by vm4). No claim is active on vm3. Root 4.6G free of 7.8G, 481406 inodes free. Worktrees on disk: solr-16290, solr-6831, solr-6831-base, 18551-10x-head, 18551-10x-base.

- 2026-10-17T22:20:57Z: claims pool-vm3-gate-backlog-16887 and pool-vm3-gate-backlog-15712 (relaunch of the two failed vm2 gates, approved by Nick). Root 4.6G free of 7.8G, 481407 inodes free. No Gradle process active.
