# SOLR-6759 (PR 5154) check: does the query-limit test reach the new `finally` block?

Status: escalated. The test does not reach the new block, but I could not find a corrected trip point that does. No test or production change was committed to `solr-6759-submit`.

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

## Escalation

A detecting test needs the limit to throw inside `searcher.search`, so the exception leaves the `try` and the `finally` must run. Finding a count or caller expression that does this needs the trip location instrumented (for example, the stack at each `checkCaller` hit), which is beyond this check. Main agent to decide whether to keep the `finally` change without a detecting test, or to ask the author for one. The default in the decisions table (change the test, not production code) was not applied because no variant was verified.
