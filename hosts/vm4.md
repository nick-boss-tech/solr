# Host: vm4

Pool Linux host. Capabilities and rules are in `hosts/pool-linux.md`.

Capabilities verified on this host:
- `bats`: verified 2026-10-24T20:52Z. `:solr:packaging:downloadBats` succeeded (log `/workspace/gates/logs/vm4/pr-5204/downloadBats-20261024T204823Z.log`). Runs `solr/packaging/build/bats-libs/bats-core/bin/bats` version Bats 1.8.2 (bats-support 0.2.0, bats-assert 2.0.0, bats-file 0.3.0 from `gradle/libs.versions.toml`). Execution is verified only by the phase claim that runs it.
- Bash 5.1.16 (aarch64). Python 3.11.17 at `/usr/local/bin/python3`. JDK 21.0.12.1 at `/workspace/tools/jdk21`. curl 7.81.0. Git 2.49.1 at `/workspace/tools/git-2.49.1/bin/git`.
- GitHub archive downloads for the BATS libraries: HTTP 200 from this host on 2026-10-24.

Environment added for PR 5204 (2026-10-24):
- `lsof` 4.99.7 from conda-forge at `/workspace/tools/lsof-env/bin` (put first on PATH for BATS runs).
- `/workspace/tools/reap-run.py`: child-subreaper wrapper. This VM's PID 1 does not reap orphaned children, so Solr JVMs left zombies and `solr stop` waited 180 seconds. Wrap each Gradle BATS command with it.

Heartbeat:
- 2026-10-25T08:40Z: PR 5204 final gate PASSED (attempt 3, all four legs, head daa29b06e0). Receipt receipts/SOLR-18566.md written. Nothing running.
- 2026-10-25T08:04Z: PR 5204 re-gate attempt 3 running (claim `claims/pr-5204-phase-g-final-gate-attempt3-vm4-round-1.md`). Leg 1 passed (27 of 27). Leg 2 passed (128 ok, 5 skips). Leg 3 running.
- 2026-10-25T07:00Z: PR 5204 re-gate attempt 3 running (claim `claims/pr-5204-phase-g-final-gate-attempt3-vm4-round-1.md`). Leg 1 passed (27 of 27). Leg 2 passed (128 ok, 5 skips). Leg 3 running.
- 2026-10-25T05:56Z: PR 5204 re-gate attempt 3 running (claim `claims/pr-5204-phase-g-final-gate-attempt3-vm4-round-1.md`). Leg 1 passed (27 of 27). Leg 2 passed (128 ok, 5 skips). Leg 3 running.
- 2026-10-25T04:51Z: PR 5204 re-gate attempt 3 running (claim `claims/pr-5204-phase-g-final-gate-attempt3-vm4-round-1.md`). Leg 1 passed (27 of 27). Leg 2 running.
- 2026-10-25T04:03Z: PR 5204 re-gate attempt 3 running (claim `claims/pr-5204-phase-g-final-gate-attempt3-vm4-round-1.md`). Leg 1 passed (27 of 27). Leg 2 at 89 ok, 0 not ok, still running.
- 2026-10-25T03:11Z: PR 5204 re-gate attempt 3 running (claim `claims/pr-5204-phase-g-final-gate-attempt3-vm4-round-1.md`). Leg 1 passed (27 of 27). Leg 2 running.
- 2026-10-25T02:05Z: PR 5204 re-gate attempt 3 running (claim `claims/pr-5204-phase-g-final-gate-attempt3-vm4-round-1.md`, single launch under the lock). Leg 1 passed (27 of 27). Leg 2 running.
- 2026-10-25T01:36Z: PR 5204 final gate is INCOMPLETE. Leg 1 passed (27 of 27). Leg 2 attempts 1 and 2 are incomplete. Attempt 2 was stopped because of a launch overlap. Verdict and report pushed. Nothing is running.
- 2026-10-25T01:00Z: claim `claims/pr-5204-phase-g-final-gate-vm4-round-1.md` live. Leg 1 passed (27 of 27). Leg 2 at 97+ ok, 0 not ok, still running.
- 2026-10-24T23:57Z: claim `claims/pr-5204-phase-g-final-gate-vm4-round-1.md` live. Leg 1 passed (27 of 27). Leg 2 at 88 ok, 0 not ok, still running.
- 2026-10-24T23:07Z: claim `claims/pr-5204-phase-g-final-gate-vm4-round-1.md` live. Leg 1 passed (27 of 27). Leg 2 at 41 ok, 0 not ok, still running.
- 2026-10-24T21:58Z: active claims `claims/pr-5204-phase-g-final-gate-vm4-round-1.md` (final-head gate, leg 1 running). Earlier: `claims/pr-5204-picocli-review-vm4-round-1-phase-r.md`.

Disk record:
- 2026-10-18T15:09Z before `chaos-5516-run1`: `/` 4.6G free, 39% used; inodes 481348 free (9% used).
- 2026-10-18T15:14Z after `chaos-5516-run1`: `/` 4.6G free, 39% used; inodes 481343 free (9% used).
- 2026-10-24T20:52Z after downloadBats: `/` 4.6G free, 39% used; inodes 479268 free (9% used).
- 2026-10-25T16:41:07Z: PR 5126 slice Q DONE (claim `claims/pr-5126-query-examples-vm4-round-1-done.md`, branch 022eb6d781). Four runs exit 0; nothing running.
- 2026-10-25T17:55:55Z: claimed SOLR-3479 PR 5126 fix gate (`claims/pr-5126-highlight-query-gate-vm4-round-1.md`) at fix head 23c159d06b; preflight tidy clean; nothing running yet.
- 2026-10-25T18:42:12Z: SOLR-3479 PR 5126 fix gate GREEN at 8a685825b6 (claim `claims/pr-5126-highlight-query-gate-vm4-round-1-done.md`). Nothing running; lock free.
- 2026-10-25T19:44:29Z: claimed vm4-comment-assignment-cleanup-round-1 (`claims/vm4-comment-assignment-cleanup-round-1.md`) at board tip 2153d0d51410; records only.
- 2026-10-26T00:28:24Z: claimed backlog-first-100-unit-4 (`claims/backlog-first-100-unit-4.md`), ping-stale only; shortfall named. Drafts to follow.
- 2026-10-26T00:30:35Z: Unit 4 BLOCKED (0 of 25). Report `reports/backlog-first-100-unit-4-round-1.md`. Decisions requested from owner.
- 2026-10-26T00:40:17Z: Unit 4 partial: 11 ping-stale drafts (not posted). Asked phone-agent to decide conflict flags and duplicate targets (`assignments/vm4-to-phone-agent-backlog-decisions-round-1.md`).
- 2026-10-26T00:46:19Z: claimed pr-5221-license-headers-linux-gate-round-1 (`claims/pr-5221-license-headers-gate-vm4-round-1.md`) at 2855d3a04f29; gate worktree /workspace/worktrees/gate-5221.
- 2026-10-26T02:24:48Z: PR 5221 license-header Linux gate GREEN at 2855d3a04f29 (receipt `receipts/PR-5221-license-headers-round-1.md`). Three env failures on leg 4 recorded; clean run in standalone clone. Nothing running.
- 2026-10-26T03:18:10Z: js-client npm failure traced to NODE_ENV=production in vm4 shell (A fails, B control passes at 7eb288a6). Correction report `reports/js-client-npm-failure-cause-round-1.md`.
- 2026-10-26T04:17:12Z: PR 5221 revised comment draft (round 2) written, not posted: `reports/pr-5221-license-headers-comment-draft-round-2.md`.
- 2026-10-26T04:55:34Z: claimed teardown-interrupt-close-fix-final-validation-round-1 (`claims/teardown-interrupt-close-fix-final-validation-vm4-round-1.md`) at fork H 8787c6605a; phase 1 cleanup starting.
- 2026-10-26T15:48:42Z: closed teardown final validation vm4 claim (`claims/teardown-interrupt-close-fix-final-validation-vm4-round-1-done.md`). Phase 1 tidy verified at F `5ec461c401`; phase 2 superseded by vm2 green gate at F. Not running phase 2. Lock free.
- 2026-10-26T16:15:09Z: claimed PR 5232 listener Linux gate round 1 (`claims/pr-5232-listener-followup-linux-vm4-round-1.md`) at F2 `23c68215cf`; superseding gate `gates/pr-5232-listener-followup-f2-round-1.md`. Tidy formatting commit pushed. Gate starting.
- 2026-10-26T17:26:10Z: PR 5232 listener Linux gate GREEN at F2 `23c68215cf` (receipt `receipts/PR-5232-listener-followup.md`). Focused 17/0/0/0, neighbor 4/0/0/0, root check passed on relaunch, negative 3 of 3 intended failures. Nothing running; lock free.
- 2026-10-26T20:06:07Z: claimed PR 5108 reverse merge round 3 Linux gate (`claims/pr-5108-reverse-merge-round-3-vm4.md`) at F2 `495d3fbdbe` (tidy repair of `c5a16393d2`); superseding job `gates/pr-5108-reverse-merge-round-3-f2-vm4.md`. Takes over the stale phone-agent-2 claims on this gate. Preflight tidy at F2 clean. Starting the gate.
- 2026-10-26T21:01:55Z: heartbeat, PR 5108 round 3 gate (`claims/pr-5108-reverse-merge-round-3-vm4.md`) at F2 `495d3fbdbe`: positive legs green, M1 failed as expected (both tests, wrong selected IDs); M2 running.
- 2026-10-27T02:52:48Z: claimed PR 5241 overview replication errors Linux gate round 1 (`claims/pr-5241-overview-errors-gate-vm4-round-1.md`) at offered fork head `80ca9374f4`. Gate starting; lock free before start.
- 2026-10-27T03:55Z: PR 5241 overview errors Linux gate round 1 (`claims/pr-5241-overview-errors-gate-vm4-round-1.md`) stopped, not GREEN. Receipt `receipts/PR-5241-overview-errors.md`. Blockers: forbidden API in the candidate test (check leg, `AdminUiCoreOverviewErrorsTest.java:169`); browser legs refused at setup on Linux ARM (Selenium needs a driver path, third repair not taken). Lock free, nothing running.
- 2026-10-27T04:05Z: claimed PR 5241 overview errors Linux gate round 2 (`claims/pr-5241-overview-errors-gate-vm4-round-2.md`) at repaired head `56365b43ee`. Driver via host JAVA_TOOL_OPTIONS and --no-daemon; round-1 evidence preserved. Starting the gate.
- 2026-10-27T04:45Z: round-2 driver stopped silently during tidy at 56365b43ee (no tests ran; no end line). tidy changed one wrapped line; formatting repair pushed fast-forward `3b36f32e93`. Superseding gate `gates/pr-5241-overview-errors-round-2-f3-vm4.md` pinned to `3b36f32e93`. Lock free. Relaunching from the superseding head.
- 2026-10-27T06:20Z: PR 5241 round-2 Linux gate GREEN at 3b36f32e93 (receipt `receipts/PR-5241-overview-errors.md` round-2 section; completion report `reports/pr-5241-overview-errors-gate-round-2.md`). Fail-before 3 of 3 at intended UI checks. Check ran in standalone clone (worktree .git layout broke Antora). Nothing running; lock free.
- 2026-10-27T06:50Z: wrote records-only evidence closeout `reports/pr-5241-round-2-evidence-closeout-vm4.md` for `assignments/pr-5241-round-2-evidence-closeout.md` (driver-property line in test-worker output, XML totals, fail-before messages, clone check). No tests run.
- 2026-10-27T07:10:56Z: overnight tick. PR 5241 round-2 claim, superseding gate and evidence closeout marked DONE (receipt and reports already on board). No live claim; INTAKE open, OVERNIGHT on. Disk 40% used, 4.5G free, inodes 11%.
- 2026-10-27T07:22:14Z: EOL ping batch 6 claim closed with no work (set exhausted by batch 5). EOL assignment marked DONE, held items listed for the owner. No live claim; INTAKE open.
- 2026-10-27T08:31:31Z: settling run done (both trees seed-clean, report written). Claim and assignment DONE. No live claim; INTAKE open.
- 2026-10-27T11:33:46Z: SOLR-16139 implementation pushed (branch solr-16139-submit at 3feb4c7) and gate requested (gates/SOLR-16139.md at 7490a09421). Claim DONE. Lock free; Solr not running.
- 2026-10-27T12:33:31Z: SOLR-7351 premise differs (exception on date fields, not empty list); NEEDS-OWNER question recorded in claim and report. Solr killed. Lock free.
- 2026-10-27T20:58:58Z: christos-reply-17914-pr-prep-round-1 re-gate at 5ea0055190. Disk before and after: 38% used, 2.8G of 7.8G. Inodes 9% used (44746 of 524288). Clean worktree removed after the gate.
- 2026-10-28T05:46Z before SOLR-17914 browser-check round 1: `/` 4.6G free, 39% used (2.8G used); inodes 44985 used (9% used). 2026-10-28T06:12Z after the run and cleanup: `/` 4.3G free, 43% used (3.2G used); inodes 75533 used (15% used). Logs kept under `/workspace/gates/logs/vm4/solr-17914-browser-check-round-1/`.
- 2026-10-29T05:55Z: claimed SOLR-18178 rebased configset upload round 1 gate (`claims/solr-18178-rebased-head-round-1-vm4.md`) at `d6abc36c94`. Lock free; Solr not running.
- 2026-10-29T07:16Z: SOLR-18178 round-1 gate BLOCKED at `d6abc36c94` on the Antora docs leg (environment: NODE_ENV=production and worktree source). Report `reports/solr-18178-rebased-head-round-1-vm4-blocked.md`. Claim ends here. Lock free.
- 2026-10-29T14:21:03Z: claimed PR 5238 UBI contract Linux gate round 1 (`claims/pr-5238-ubi-contract-linux-round-1-vm4.md`) at fork head `de4d222bb1`. Gate job `gates/pr-5238-ubi-contract-round-1.md` left unedited. Lock free; nothing running yet.
- 2026-10-29T14:29:35Z: PR 5238 UBI contract Linux gate round 1 HALTED at fork head `de4d222bb1`. Clone clean-tree check failed after tidy: tidy rewrapped two `cluster.getSolrClient().request(...)` calls in `SearchRequestListenerUbiTest.java` (formatting only). No tests ran. Fork not pushed. Gate stays unrepaired pending main-agent decision. Logs `/workspace/gates/logs/vm4/pr5238-ubi-contract-linux-round-1/`. Lock free, nothing running.
- 2026-10-29T14:43:13Z: claimed PR 5238 UBI contract Linux gate round 2 (`claims/pr-5238-ubi-contract-linux-round-2-vm4.md`) at fork head `c2c387064a`, superseding round 1 via `gates/pr-5238-ubi-contract-round-2-vm4.md`. Tidy repair pushed to the fork by vm4 with main-agent authorization. Gate starting.
- 2026-10-29T16:18:52Z: PR 5238 UBI contract Linux gate round 2 GREEN at `c2c387064a` (receipt `receipts/PR-5238-ubi-contract.md`, report `reports/pr-5238-ubi-contract-linux-round-2.md`). Tidy repair pushed to the fork by vm4 with main-agent authorization. Three host crashes, no tests lost. Lock free, nothing running.
- 2026-10-30T00:02:20Z: closed PR 5238 UBI contract Linux gate round 1 as halted at tidy and superseded by round 2 (`reports/pr-5238-ubi-contract-linux-round-1-closeout.md`). Round-2 receipt at `c2c387064a` is the proof to cite. Nothing running.
- 2026-10-30T01:50:57Z: claimed PR 2452 UBI context rework and Linux gate round 1 (`claims/pr-2452-ubi-context-rework-vm4-round-1.md`). Preferred base PR 2452 head `6402b4b528`; source fix `09343928e7` ported onto it. Assignment Status set to CLAIMED. Lock free; nothing running.
