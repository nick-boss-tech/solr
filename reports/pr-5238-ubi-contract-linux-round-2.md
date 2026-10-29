# Report: listener UBI contract proof, Linux round 2 (vm4)

- Gate: `gates/pr-5238-ubi-contract-round-2-vm4.md` (supersedes round 1 at `de4d222bb1`). Claim: `claims/pr-5238-ubi-contract-linux-round-2-vm4.md`. Receipt: `receipts/PR-5238-ubi-contract.md`.
- Exact tested head: `c2c387064a83def34cece4163eccded28318caeb` on `nick-boss-tech/solr:solr-11994-pr5238-ubi-submit`. Verified on the remote after all runs: same head.
- Parent: PR 5238 head `aa03c9544968f1d9de9b6b36e2823bcbe76268b9`.
- Round-2 head differs from round 1 by one formatting commit (`c2c387064a`, "PR 5238: tidy UBI contract test formatting"). Its only changed path is `SearchRequestListenerUbiTest.java`. Round 1 at `de4d222bb1` was not tested beyond clone and tidy, so no proof is claimed for it.
- Environment: host vm4, Linux aarch64, Temurin JDK 21.0.12.1 at `/workspace/tools/jdk21`, Gradle 9.7.0 through the repo wrapper, host lock `/workspace/tools/.gradle-vm4.lock`, `--no-daemon` and `--no-build-cache` on every Gradle command.
- Checkout: standalone full clone (not shallow, not a worktree) at `/workspace/worktrees/gate-5238-clone`, detached at `c2c387064a`. Clean before and after the run.
- Driver: `/workspace/gates/logs/vm4/pr5238-ubi-contract-linux-round-2/run.sh`, with per-step `.log`, `.rc`, `state.txt`, and XML copies in `<step>-xml/`.

## Steps

| Step | Command | UTC (end) | Exit | Result |
|---|---|---|---|---|
| checkout | fetch fork branch, detach at `c2c387064a`, clean-tree check | 14:43:40 | 0 | at head, clean |
| scope | `git diff --name-only aa03c95 HEAD` equals the one test file | 14:43:40 | 0 | scope-ok |
| tidy | `./gradlew tidy` | 15:11:36 | 0 | BUILD SUCCESSFUL |
| tidy-clean-tree | `git status --porcelain` empty | 15:11:39 | 0 | clean |
| ubi-seed-1 | `:solr:core:cleanTest :solr:core:test --tests ...SearchRequestListenerUbiTest -Ptests.seed=5238AB1` | 15:17:23 | 0 | 9 tests / 0 skipped / 0 failures / 0 errors |
| ubi-seed-2 | same, `-Ptests.seed=5238AB2` | 15:19:39 | 0 | 9 / 0 / 0 / 0 |
| listener-suite | `...SearchRequestListenerTest -Ptests.seed=5238AB1` | 15:21:54 | 0 | 19 / 0 / 0 / 0 |
| neighbors | `PingRequestHandlerTest`, `TestSolrConfigHandler`, `TestSolrRequestInfo`, `TestSubQueryTransformerDistrib` with `-Ptests.seed=5238AB1` | 15:25:35 | 0 | 6 / 0 / 0 / 0; 8 / 0 / 0 / 0; 3 / 0 / 0 / 0; 1 / 0 / 0 / 0 |
| check | `env -u NODE_ENV ./gradlew check -x test` | 16:17:10 | 0 | BUILD SUCCESSFUL in 4m 45s, 556 actionable tasks (176 executed, 380 up-to-date) |
| diff-check | `git diff --check aa03c95 c2c387064a` | 16:17:06 | 0 | clean |
| final-clean | clean tree and HEAD equals `c2c387064a` | 16:17:10 | 0 | clean |

Counts are from the fresh XML in `ubi-seed-1-xml/`, `ubi-seed-2-xml/`, `listener-suite-xml/`, and `neighbors-xml/`. The seed runs executed all nine methods of `SearchRequestListenerUbiTest` under each seed. The listener suite executed 19 of 19, with the CPU timing test running rather than skipped.

## Interruptions

The host crashed or rebooted three times during this round. Each time the driver was stopped; no step with a recorded `.rc` was rerun, and no completed test run was repeated.

1. During tidy (14:43 to about 15:09). Tidy had no `.rc`, so the driver was relaunched from tidy at 15:09:26 after the reboot. Tidy then passed.
2. During the root check, at static checks (forbiddenApis and spotless, `-x test`). No test task had started. The driver was relaunched at 15:30:29, then stopped again. The log of that attempt was not kept as evidence.
3. During the root check again. The check log had zero test tasks (`grep` for `:test` returned nothing). That log was kept as `check-interrupted-attempt-3.log`. The driver was relaunched at 16:12:18 and the check completed at 16:17:10.

No test ran in any interrupted attempt. The seed, listener, and neighbor results above come from single uninterrupted runs.

## Gaps

- The proof is for the test-only component and listener pattern at this head. It does not prove PR 2452 integration, persistent sinks, core reload or unload, asynchronous lifetime, grouped or ID-omitting results, redaction, or production overhead.
- Round 1 head `de4d222bb1` is not proven. The tidy repair `c2c387064a` is formatting only, and the gate's requirements are the same.
- No Windows run is part of this report.
- The clean-tree and diff-check results are the only evidence that the repair is formatting-only beyond the scope check.
