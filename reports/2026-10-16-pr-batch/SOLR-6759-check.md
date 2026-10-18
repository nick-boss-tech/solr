# SOLR-6759 (PR 5154) check: does the query-limit test reach the new `finally` block?

Status: resolved. The test is corrected in `bdeb07d03b` on `solr-6759-submit`: it trips at the sixth `ExpandComponent.process` check, inside the expand search, and fails on the parent code without the `finally`.

- Head: `solr-6759-submit` at `680044f1d2a6a36b4543f4d52bb552e32b142919` (fix commit `680044f1d2`, parent `66809d9564`).
- Test: `TestExpandComponent.testExpandCompletesPostFilterCollectorsWhenQueryLimitExceeded`.
- Command: `:solr:core:test --rerun --tests <test>` with `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1`, through `pool/bin/sweep-run.sh`.

## Runs

| Run | Production code | Trip expression | Result | Log |
| --- | --- | --- | --- | --- |
| A | head (with `finally`) | `ExpandComponent.process:1` | pass | `item4-6759-A-head.log` |
| C | `finally` removed (parent `66809d9564`) | `ExpandComponent.process:1` | pass | `item4-6759-C-nofinally.log` |
| D | `finally` removed | `ExpandComponent.process:2` | pass | `item4-6759-D-nofinally-count2.log` |
| E | `finally` removed | `ExpandComponent.process:3` | pass | `item4-6759-E-nofinally-count3.log` |

## Path analysis

- `ExpandComponent.process` checks `queryLimits.maybeExitWithPartialResults("Expand process")` at `ExpandComponent.java:258`, before the expand search. The `:1` expression trips on that first check, so the search and the `try`/`finally` would not run. That matches the test's own comment ("the first check is ExpandComponent.process before the expand search"), which expects the trip on the next check instead.
- Run C shows the test passes without the `finally`, so it does not detect the fix. Run A confirms the test passes with it.
- Runs D and E move the trip to the second and third `ExpandComponent.process` checks. These also pass without the `finally`, so neither variant makes the test fail on the missing `finally`. The trip site for counts 2 and 3 was not isolated. The likely candidates are the check after the search at `ExpandComponent.java:455` (outside the `try`) and any check inside `searcher.search`, which I did not confirm.
- The assignment's note that the stated base result does not match the base code was not checked. No run was made at `cabedd1d96`.

## Resolution

- Probe on the parent commit `66809d9564` (no `finally`), with the trip count varied from 1 to 8 on `ExpandComponent.process`. Counts 1 to 5 trip before the expand search (one collector, completed). Count 6 trips inside the expand search: two post filter collectors are created and one completes, so the limit exception leaves the search with the collector chain incomplete. Count 7 and above never trip.
- Test change in `bdeb07d03b`: the trip is `ExpandComponent.process:6`. The comment explains the trip point.
- Fail-before: the test fails on the parent code with `expected:<2> but was:<1>` on the completion count (log `6759-failbefore-parent.log`).
- Pass: the test passes at the PR head `680044f1d2` plus the test change (log `6759-pass-head.log`, 1 of 1 passed).
- The PR body is corrected (readback verified). It had said the second test passes on the base code, which the fail-before run disproves.

The escalation to the main agent is withdrawn: there is no main agent. The count was found by measurement, not guessed.
