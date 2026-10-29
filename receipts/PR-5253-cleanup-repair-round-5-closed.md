# Receipt: SOLR-18601 / PR 5253, round 5 gate closed (vm2, Linux)

Closes the round 5 gate at head K `21804641d0ebbb5199a1c205f031af47c9424290`. Earlier receipts stay as written and are read with this one.

## Checklist

- Changelog YAML parse: pass (`receipt-round5-remaining-steps`).
- Tidy: pass, clean tree.
- Error Prone compile: pass. `:solr:core:compileJava --rerun` and `:solr:core:compileTestJava --rerun` with `-Pvalidation.errorprone=true`, BUILD SUCCESSFUL, 0 warnings, Error Prone not skipped.
- `:check -x test` (root check): pass, rc 0. Error Prone was skipped in that run because it is only enabled with `-Pvalidation.errorprone=true`, which is why it is run separately above.
- Focused, 5253BEEF, 5253CAFE: pass.
- Neighbours: one failure, `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure`, pre-existing (fails on upstream `e59ce3c97d` with the same seed). Accepted by the owner.
- `checkSiteLinks`: pass.
- Negative legs: A 3/3 fail, B 4/4 fail, C 1/1 fail (A ran on the PR's first commit `32b91612`, not upstream; see `receipt-round5-base-correction`).
- Pre-fix proof on upstream `e59ce3c97d`: 5 of 8 `MigrateRouteKeyTest` fail; 2 behavioural, 3 cascade from the same leftover temporary collection (`receipt-round5-remaining-steps`).

## Verdict

GREEN for the gate, with the owner-accepted pre-existing neighbour failure. Verdict recorded by the owner's acceptance (`receipt-round5-accepted`).

## Record correction

While re-running Error Prone, the first attempt's failed-run diagnostics line was removed from `repair-I/steps.txt`. That attempt failed on a bad task name and proved nothing. The line was a log entry, not a result, and the failed run's `errorprone rc=1` line is still there. Recorded here so the log is not read as clean.

No GitHub, PR, or JIRA action taken. No new head pushed.
