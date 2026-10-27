# Receipt: RC3 smoke loop and candidate runs (vm5, 2026-10-24 to 2026-10-26)

Summary of the local run records. Logs are not on the board; they stay on vm5 at `/workspace/smoke-rc3/runs/` and `/workspace/smoke-rc3/candidate-runs/`. Narrative is in `reports/rc3-smoke-loop-round-1.md`.

## Loop on the unpatched RC3 tip (`749712b71940`, `runs/results.tsv`)

| Run | End (UTC) | rc | Outcome |
|---|---|---|---|
| 001 | 2026-10-24 10:18 | 1 | fail |
| 002 | 2026-10-24 12:23 | 1 | fail |
| 003 | 2026-10-24 14:33 | 1 | fail |
| 004 | 2026-10-24 15:06 | - | died (no rc line) |
| 005 | 2026-10-24 17:13 | 1 | fail |
| 006 | 2026-10-24 19:19 | 1 | fail |
| 007 | 2026-10-24 20:02 | - | stopped |

No loop run passed. Methods that failed in the completed runs: `TestTlogReplica.testKillLeader`, `testRecovery`, `testOnlyLeaderIndexes` (intermittent), `CoreAdminHandlerTest` (SOLR-18587), and `TestCoordinatorRole.testNRTRestart`.

## Candidate runs (`candidate-runs/results.tsv`, candidate `8b8c45ca0a7`)

| Run | Outcome | Note |
|---|---|---|
| 001 | no result | log ends at the Java-test step; no rc line and no results row |
| 002 | killed | host restart about 17:44 UTC, root-disk `/tmp` wiped |
| 003 | killed | host restart |
| 004 | killed | host restart, `/tmp` wiped |
| 005 | rc 1 | Java tests: 5897 tests in `solr:core`, 4 failures: `TestTlogReplica.testKillLeader`, `testRecovery`, `testOnlyLeaderIndexes`, `TestCoordinatorRole.testNRTRestart`. 33 EMLINK lines. `CoreAdminHandlerTest` passes. |

Run 005 is the last recorded candidate result. The candidate changes (SOLR-18587 and others) were in it, so the four failures above are the ones left after the CoreAdmin fix.
